/*
 * 加速度センサーを使用した振動検出LEDコントロールシステム
 * 学籍番号: 36714029
 * 
 * 機能:
 * - 加速度センサーで振動を検出
 * - 振動の強さに応じてLEDの明るさと色を変更
 * - 振動データをSDカードに記録
 * - 統計情報を計算して表示
 */

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>
#include <time.h>
#include <stdbool.h>

// ピン定義 (ESP32 M5Stack StickC Plus用)
#define LED_RED_PIN 10
#define LED_GREEN_PIN 26
#define LED_BLUE_PIN 36
#define ACCEL_X_PIN 32  // アナログピン (GPIO32)
#define ACCEL_Y_PIN 33  // アナログピン (GPIO33)
#define ACCEL_Z_PIN 25  // アナログピン (GPIO25)
#define BUTTON_PIN 37   // M5StickC Plusのボタン

// 閾値定義
#define SHAKE_THRESHOLD 300
#define STRONG_SHAKE_THRESHOLD 600
#define SAMPLE_SIZE 10
#define MAX_LOG_ENTRIES 1000

// LEDモード
typedef enum {
  MODE_OFF = 0,
  MODE_WEAK_SHAKE = 1,
  MODE_MEDIUM_SHAKE = 2,
  MODE_STRONG_SHAKE = 3
} LEDMode;

// 加速度データ構造体
typedef struct {
  int x;
  int y;
  int z;
  float magnitude;
  unsigned long timestamp;
} AccelData;

// RGB色構造体
typedef struct {
  int red;
  int green;
  int blue;
} RGBColor;

// 統計データ構造体
typedef struct {
  float average;
  float maximum;
  float minimum;
  int count;
  float total;
} Statistics;

// システム状態構造体
typedef struct {
  LEDMode currentMode;
  Statistics stats;
  AccelData history[SAMPLE_SIZE];
  int historyIndex;
  bool isLogging;
  int logCount;
} SystemState;

// グローバル変数
SystemState *systemState = NULL;
FILE *logFile = NULL;

// 関数プロトタイプ宣言
void initializeSystem(SystemState *state);
void readAccelerometer(AccelData *data);
float calculateMagnitude(int x, int y, int z);
void updateHistory(SystemState *state, AccelData *data);
LEDMode determineShakeLevel(float magnitude);
void setLEDColor(RGBColor *color);
void generateColorForMode(LEDMode mode, RGBColor *color);
void updateStatistics(Statistics *stats, float value);
void printStatistics(Statistics *stats);
bool openLogFile(const char *filename);
void writeToLogFile(AccelData *data, LEDMode mode);
void closeLogFile(void);
void handleButtonPress(SystemState *state);
float getAverageFromHistory(SystemState *state);
void resetStatistics(Statistics *stats);
void displayStatus(SystemState *state, AccelData *currentData);
void performCalibration(SystemState *state);
int mapValue(int value, int inMin, int inMax, int outMin, int outMax);
void blinkLED(int times, int delayMs);
void smoothTransition(RGBColor *from, RGBColor *to, int steps);

/*
 * 初期化関数
 * システム状態を初期化し、必要なリソースを確保
 */
void initializeSystem(SystemState *state) {
  // ピンモード設定
  pinMode(LED_RED_PIN, OUTPUT);
  pinMode(LED_GREEN_PIN, OUTPUT);
  pinMode(LED_BLUE_PIN, OUTPUT);
  pinMode(BUTTON_PIN, INPUT_PULLUP);
  
  // システム状態初期化
  state->currentMode = MODE_OFF;
  state->historyIndex = 0;
  state->isLogging = true;
  state->logCount = 0;
  
  // 統計データ初期化
  resetStatistics(&state->stats);
  
  // 履歴データ初期化
  for (int i = 0; i < SAMPLE_SIZE; i++) {
    state->history[i].x = 0;
    state->history[i].y = 0;
    state->history[i].z = 0;
    state->history[i].magnitude = 0.0;
    state->history[i].timestamp = 0;
  }
  
  // ログファイルを開く
  if (openLogFile("shake_log.txt")) {
    Serial.println("ログファイルを開きました");
  } else {
    Serial.println("警告: ログファイルを開けませんでした");
    state->isLogging = false;
  }
  
  // 起動確認のLED点滅
  blinkLED(3, 200);
}

/*
 * 加速度センサーからデータを読み取る関数
 */
void readAccelerometer(AccelData *data) {
  // アナログピンから加速度データを読み取り
  data->x = analogRead(ACCEL_X_PIN);
  data->y = analogRead(ACCEL_Y_PIN);
  data->z = analogRead(ACCEL_Z_PIN);
  
  // 中心値(512)からのオフセットを計算
  data->x -= 512;
  data->y -= 512;
  data->z -= 512;
  
  // 加速度の大きさを計算
  data->magnitude = calculateMagnitude(data->x, data->y, data->z);
  
  // タイムスタンプを記録
  data->timestamp = millis();
}

/*
 * 3軸加速度から大きさ(ノルム)を計算する関数
 */
float calculateMagnitude(int x, int y, int z) {
  return sqrt((float)(x * x + y * y + z * z));
}

/*
 * 加速度データを履歴に追加する関数（リングバッファ実装）
 */
void updateHistory(SystemState *state, AccelData *data) {
  // ポインタを使用して履歴配列を操作
  AccelData *historySlot = &state->history[state->historyIndex];
  
  // データをコピー
  historySlot->x = data->x;
  historySlot->y = data->y;
  historySlot->z = data->z;
  historySlot->magnitude = data->magnitude;
  historySlot->timestamp = data->timestamp;
  
  // インデックスを更新（リングバッファ）
  state->historyIndex = (state->historyIndex + 1) % SAMPLE_SIZE;
}

/*
 * 振動の強さからLEDモードを決定する関数
 */
LEDMode determineShakeLevel(float magnitude) {
  if (magnitude < SHAKE_THRESHOLD) {
    return MODE_OFF;
  } else if (magnitude < STRONG_SHAKE_THRESHOLD) {
    return MODE_MEDIUM_SHAKE;
  } else {
    return MODE_STRONG_SHAKE;
  }
}

/*
 * RGB値に基づいてLEDの色を設定する関数
 */
void setLEDColor(RGBColor *color) {
  analogWrite(LED_RED_PIN, color->red);
  analogWrite(LED_GREEN_PIN, color->green);
  analogWrite(LED_BLUE_PIN, color->blue);
}

/*
 * LEDモードに応じた色を生成する関数
 */
void generateColorForMode(LEDMode mode, RGBColor *color) {
  switch (mode) {
    case MODE_OFF:
      color->red = 0;
      color->green = 0;
      color->blue = 0;
      break;
    
    case MODE_WEAK_SHAKE:
      color->red = 0;
      color->green = 100;
      color->blue = 255;
      break;
    
    case MODE_MEDIUM_SHAKE:
      color->red = 255;
      color->green = 200;
      color->blue = 0;
      break;
    
    case MODE_STRONG_SHAKE:
      color->red = 255;
      color->green = 0;
      color->blue = 0;
      break;
    
    default:
      color->red = 255;
      color->green = 255;
      color->blue = 255;
      break;
  }
}

/*
 * 統計データを更新する関数
 */
void updateStatistics(Statistics *stats, float value) {
  stats->count++;
  stats->total += value;
  stats->average = stats->total / stats->count;
  
  // 最大値・最小値の更新
  if (stats->count == 1 || value > stats->maximum) {
    stats->maximum = value;
  }
  if (stats->count == 1 || value < stats->minimum) {
    stats->minimum = value;
  }
}

/*
 * 統計情報をシリアルに出力する関数
 */
void printStatistics(Statistics *stats) {
  Serial.println("=== 振動統計情報 ===");
  Serial.print("測定回数: ");
  Serial.println(stats->count);
  Serial.print("平均値: ");
  Serial.println(stats->average, 2);
  Serial.print("最大値: ");
  Serial.println(stats->maximum, 2);
  Serial.print("最小値: ");
  Serial.println(stats->minimum, 2);
  Serial.println("==================");
}

/*
 * ログファイルを開く関数
 */
bool openLogFile(const char *filename) {
  logFile = fopen(filename, "a");
  if (logFile == NULL) {
    return false;
  }
  
  // ヘッダーを書き込み
  fprintf(logFile, "\n=== 新しいセッション開始: %lu ===\n", millis());
  fprintf(logFile, "タイムスタンプ,X,Y,Z,大きさ,モード\n");
  fflush(logFile);
  
  return true;
}

/*
 * 加速度データをログファイルに書き込む関数
 */
void writeToLogFile(AccelData *data, LEDMode mode) {
  if (logFile == NULL) {
    return;
  }
  
  const char *modeStr;
  switch (mode) {
    case MODE_OFF: modeStr = "OFF"; break;
    case MODE_WEAK_SHAKE: modeStr = "WEAK"; break;
    case MODE_MEDIUM_SHAKE: modeStr = "MEDIUM"; break;
    case MODE_STRONG_SHAKE: modeStr = "STRONG"; break;
    default: modeStr = "UNKNOWN"; break;
  }
  
  fprintf(logFile, "%lu,%d,%d,%d,%.2f,%s\n",
          data->timestamp, data->x, data->y, data->z,
          data->magnitude, modeStr);
  fflush(logFile);
}

/*
 * ログファイルを閉じる関数
 */
void closeLogFile(void) {
  if (logFile != NULL) {
    fprintf(logFile, "=== セッション終了 ===\n");
    fclose(logFile);
    logFile = NULL;
  }
}

/*
 * ボタン押下時の処理関数
 */
void handleButtonPress(SystemState *state) {
  static unsigned long lastPressTime = 0;
  unsigned long currentTime = millis();
  
  // チャタリング防止（200ms）
  if (currentTime - lastPressTime < 200) {
    return;
  }
  
  if (digitalRead(BUTTON_PIN) == LOW) {
    lastPressTime = currentTime;
    
    // 統計情報を表示
    printStatistics(&state->stats);
    
    // LEDで確認
    blinkLED(2, 100);
  }
}

/*
 * 履歴から平均値を計算する関数
 */
float getAverageFromHistory(SystemState *state) {
  float sum = 0.0;
  int count = 0;
  
  for (int i = 0; i < SAMPLE_SIZE; i++) {
    if (state->history[i].timestamp > 0) {
      sum += state->history[i].magnitude;
      count++;
    }
  }
  
  return (count > 0) ? (sum / count) : 0.0;
}

/*
 * 統計データをリセットする関数
 */
void resetStatistics(Statistics *stats) {
  stats->average = 0.0;
  stats->maximum = 0.0;
  stats->minimum = 0.0;
  stats->count = 0;
  stats->total = 0.0;
}

/*
 * 現在の状態を表示する関数
 */
void displayStatus(SystemState *state, AccelData *currentData) {
  Serial.print("加速度: X=");
  Serial.print(currentData->x);
  Serial.print(" Y=");
  Serial.print(currentData->y);
  Serial.print(" Z=");
  Serial.print(currentData->z);
  Serial.print(" | 大きさ=");
  Serial.print(currentData->magnitude, 2);
  Serial.print(" | モード=");
  Serial.println(state->currentMode);
}

/*
 * センサーキャリブレーションを実行する関数
 */
void performCalibration(SystemState *state) {
  Serial.println("キャリブレーション開始...");
  Serial.println("デバイスを静止させてください");
  
  delay(2000);
  
  AccelData calibData;
  for (int i = 0; i < 50; i++) {
    readAccelerometer(&calibData);
    delay(20);
  }
  
  Serial.println("キャリブレーション完了");
  blinkLED(5, 100);
}

/*
 * 値をマッピングする関数
 */
int mapValue(int value, int inMin, int inMax, int outMin, int outMax) {
  return (value - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
}

/*
 * LEDを点滅させる関数
 */
void blinkLED(int times, int delayMs) {
  RGBColor white = {255, 255, 255};
  RGBColor off = {0, 0, 0};
  
  for (int i = 0; i < times; i++) {
    setLEDColor(&white);
    delay(delayMs);
    setLEDColor(&off);
    delay(delayMs);
  }
}

/*
 * 色を滑らかに遷移させる関数
 */
void smoothTransition(RGBColor *from, RGBColor *to, int steps) {
  for (int i = 0; i <= steps; i++) {
    RGBColor intermediate;
    intermediate.red = mapValue(i, 0, steps, from->red, to->red);
    intermediate.green = mapValue(i, 0, steps, from->green, to->green);
    intermediate.blue = mapValue(i, 0, steps, from->blue, to->blue);
    
    setLEDColor(&intermediate);
    delay(10);
  }
}

/*
 * セットアップ関数
 * システムの初期化を実行
 */
void setup() {
  // シリアル通信開始
  Serial.begin(9600);
  Serial.println("振動検出LEDシステム起動中...");
  
  // システム状態のメモリ確保
  systemState = (SystemState *)malloc(sizeof(SystemState));
  if (systemState == NULL) {
    Serial.println("エラー: メモリ確保失敗");
    while (1) {
      delay(1000);
    }
  }
  
  // システム初期化
  initializeSystem(systemState);
  
  // キャリブレーション実行
  performCalibration(systemState);
  
  Serial.println("システム準備完了");
  Serial.println("マイコンを振ってください！");
}

/*
 * メインループ関数
 * 継続的にセンサーを監視しLEDを制御
 */
void loop() {
  static unsigned long lastDisplayTime = 0;
  static unsigned long loopCount = 0;
  
  AccelData currentData;
  RGBColor currentColor;
  RGBColor targetColor;
  
  // 加速度センサーからデータを読み取り
  readAccelerometer(&currentData);
  
  // 履歴に追加
  updateHistory(systemState, &currentData);
  
  // 平均値を計算
  float avgMagnitude = getAverageFromHistory(systemState);
  
  // 振動レベルを判定
  LEDMode newMode = determineShakeLevel(avgMagnitude);
  
  // モードが変更された場合
  if (newMode != systemState->currentMode) {
    // 現在の色を取得
    generateColorForMode(systemState->currentMode, &currentColor);
    
    // 新しい色を取得
    generateColorForMode(newMode, &targetColor);
    
    // 滑らかに遷移
    smoothTransition(&currentColor, &targetColor, 10);
    
    // モードを更新
    systemState->currentMode = newMode;
    
    Serial.print("モード変更: ");
    Serial.println(newMode);
  } else {
    // モードが変わらない場合は現在の色を設定
    generateColorForMode(systemState->currentMode, &currentColor);
    setLEDColor(&currentColor);
  }
  
  // 統計を更新
  updateStatistics(&systemState->stats, currentData.magnitude);
  
  // ログファイルに記録（ログが有効な場合）
  if (systemState->isLogging && systemState->logCount < MAX_LOG_ENTRIES) {
    writeToLogFile(&currentData, systemState->currentMode);
    systemState->logCount++;
    
    // ログが上限に達した場合
    if (systemState->logCount >= MAX_LOG_ENTRIES) {
      Serial.println("ログが上限に達しました");
      closeLogFile();
      systemState->isLogging = false;
    }
  }
  
  // ボタン押下チェック
  handleButtonPress(systemState);
  
  // 1秒ごとに状態を表示
  if (millis() - lastDisplayTime > 1000) {
    displayStatus(systemState, &currentData);
    lastDisplayTime = millis();
  }
  
  // ループカウンタを増加
  loopCount++;
  
  // 短い待機
  delay(50);
}
