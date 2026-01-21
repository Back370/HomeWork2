/*
 * ファイル操作 (課題07より)
 * - fopen / fclose
 * - fprintf / fscanf
 * - fgets / fputs
 * - ファイル存在確認
 * - バイナリファイル
 */
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

/* === ファイルの存在確認 === */
int file_exists(const char *filename) {
    FILE *fp = fopen(filename, "r");
    if (fp == NULL) {
        return 0;  /* 存在しない */
    }
    fclose(fp);
    return 1;  /* 存在する */
}

void example_file_check(void) {
    const char *filename = "test.txt";
    if (file_exists(filename)) {
        printf("%s は存在します\n", filename);
    } else {
        printf("%s は存在しません\n", filename);
    }
}

/* === テキストファイルへの書き込み === */
void example_write_file(void) {
    FILE *fp = fopen("output.txt", "w");
    if (fp == NULL) {
        perror("fopen");
        return;
    }

    fprintf(fp, "Hello, File!\n");
    fprintf(fp, "数値: %d\n", 42);
    fprintf(fp, "浮動小数点: %.2f\n", 3.14);

    fclose(fp);
    printf("output.txt に書き込みました\n");
}

/* === テキストファイルからの読み込み === */
void example_read_file(void) {
    FILE *fp = fopen("output.txt", "r");
    if (fp == NULL) {
        perror("fopen");
        return;
    }

    char line[256];
    while (fgets(line, sizeof(line), fp) != NULL) {
        printf("読み込み: %s", line);
    }

    fclose(fp);
}

/* === fscanfでフォーマット付き読み込み === */
void example_fscanf(void) {
    FILE *fp = fopen("data.txt", "r");
    if (fp == NULL) {
        /* テスト用にファイル作成 */
        fp = fopen("data.txt", "w");
        fprintf(fp, "Alice 85\nBob 90\nCharlie 78\n");
        fclose(fp);
        fp = fopen("data.txt", "r");
    }

    char name[50];
    int score;
    while (fscanf(fp, "%49s %d", name, &score) == 2) {
        printf("Name: %s, Score: %d\n", name, score);
    }

    fclose(fp);
}

/* === サイン波データをファイルに出力 === */
void example_sin_data(void) {
    FILE *fp = fopen("sin.dat", "w");
    if (fp == NULL) {
        perror("fopen");
        return;
    }

    for (int i = 0; i <= 360; i += 10) {
        double rad = i * M_PI / 180.0;
        fprintf(fp, "%d\t%f\n", i, sin(rad));
    }

    fclose(fp);
    printf("sin.dat に出力しました\n");
}

/* === 追記モード === */
void example_append_file(void) {
    FILE *fp = fopen("log.txt", "a");  /* 追記モード */
    if (fp == NULL) {
        perror("fopen");
        return;
    }

    fprintf(fp, "ログエントリ追加\n");
    fclose(fp);
}

/* === バイナリファイル === */
void example_binary_file(void) {
    int data[5] = {10, 20, 30, 40, 50};

    /* 書き込み */
    FILE *fp = fopen("data.bin", "wb");
    if (fp == NULL) {
        perror("fopen");
        return;
    }
    fwrite(data, sizeof(int), 5, fp);
    fclose(fp);

    /* 読み込み */
    int read_data[5];
    fp = fopen("data.bin", "rb");
    if (fp == NULL) {
        perror("fopen");
        return;
    }
    fread(read_data, sizeof(int), 5, fp);
    fclose(fp);

    printf("バイナリ読み込み: ");
    for (int i = 0; i < 5; i++) {
        printf("%d ", read_data[i]);
    }
    printf("\n");
}

/* === ファイルオープンモード一覧 ===
 * "r"  : 読み込み（ファイルが存在しないとエラー）
 * "w"  : 書き込み（ファイルを新規作成/上書き）
 * "a"  : 追記（ファイル末尾に追加）
 * "r+" : 読み書き（ファイルが存在しないとエラー）
 * "w+" : 読み書き（ファイルを新規作成/上書き）
 * "a+" : 読み書き（追記モード）
 * "rb", "wb", "ab" 等: バイナリモード
 */

int main(void) {
    example_file_check();
    example_write_file();
    example_read_file();
    printf("\n");
    example_fscanf();
    printf("\n");
    example_sin_data();
    example_binary_file();
    return 0;
}
