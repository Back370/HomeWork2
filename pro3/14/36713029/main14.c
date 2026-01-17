#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "task14.h"

int main(void){
    int *ip;
    int i, n;

    printf("配列の要素数を入力してください：");
    scanf("%d", &n);

    if (n <= 0) {
        printf("正の整数を入力してください。\n");
        return 1;
    }

    // 動的にメモリを確保
    ip = (int *)malloc(n * sizeof(int));
    if (ip == NULL) {
        printf("メモリの確保に失敗しました。\n");
        return 1;
    }

    // 配列に1, 2, ..., nの値を代入
    IPtoArray(ip, n);

    // 配列の値を出力
    OutputArrayValue(ip, n);

    // メモリを解放
    free(ip);
    return 0;
}