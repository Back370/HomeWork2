/*
 * 基本入出力 (課題01, 02より)
 * - printf: 標準出力
 * - scanf: 標準入力
 */
#include <stdio.h>

/* === 基本出力 === */
void example_printf(void) {
    printf("Hello, World!\n");

    int num = 42;
    double val = 3.14;
    char ch = 'A';
    char str[] = "test";

    printf("整数: %d\n", num);
    printf("浮動小数点: %f\n", val);
    printf("浮動小数点(桁指定): %.2f\n", val);
    printf("文字: %c\n", ch);
    printf("文字列: %s\n", str);
    printf("16進数: %x\n", num);
    printf("アドレス: %p\n", (void*)&num);
}

/* === 基本入力 === */
void example_scanf(void) {
    int num;
    double val;
    char str[100];

    printf("整数を入力: ");
    scanf("%d", &num);

    printf("小数を入力: ");
    scanf("%lf", &val);  /* doubleは%lf */

    printf("文字列を入力: ");
    scanf("%99s", str);  /* バッファオーバーフロー防止 */

    printf("入力値: %d, %f, %s\n", num, val, str);
}

/* === 複数値の入力 === */
void example_multi_input(void) {
    int a, b, c;
    printf("3つの整数をスペース区切りで入力: ");
    scanf("%d %d %d", &a, &b, &c);
    printf("合計: %d\n", a + b + c);
}

int main(void) {
    example_printf();
    /* example_scanf(); */
    /* example_multi_input(); */
    return 0;
}
