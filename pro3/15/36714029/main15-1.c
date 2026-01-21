#define _USE_MATH_DEFINES
#include <math.h>
#include <stdio.h>
#include "task15-1.h"

#define NUM_POINTS 401  // データ点数（400点以上）

int main(void) {
    FILE *fp;
    double x, y;
    double dx = 2.0 * M_PI / (NUM_POINTS - 1);  // xの刻み幅

    // 関数1: y = sin(x^2) / x を 36714029-1.dat に出力
    if ((fp = fopen("36714029-1.dat", "w")) == NULL) {
        printf("ファイルをオープンできません。\n");
        return 1;
    }
    for (int i = 0; i < NUM_POINTS; i++) {
        x = i * dx;
        y = func1(x);
        fprintf(fp, "%lf %lf\n", x, y);
    }
    fclose(fp);

    // 関数2: y = 1 / x を 36714029-2.dat に出力
    if ((fp = fopen("36714029-2.dat", "w")) == NULL) {
        printf("ファイルをオープンできません。\n");
        return 1;
    }
    for (int i = 0; i < NUM_POINTS; i++) {
        x = i * dx;
        y = func2(x);
        fprintf(fp, "%lf %lf\n", x, y);
    }
    fclose(fp);

    // 関数3: y = -1 / x を 36714029-3.dat に出力
    if ((fp = fopen("36714029-3.dat", "w")) == NULL) {
        printf("ファイルをオープンできません。\n");
        return 1;
    }
    for (int i = 0; i < NUM_POINTS; i++) {
        x = i * dx;
        y = func3(x);
        fprintf(fp, "%lf %lf\n", x, y);
    }
    fclose(fp);

    printf("データファイルを作成しました。\n");
    return 0;
}
