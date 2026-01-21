/*
 * 制御フロー (課題04より)
 * - if-else（ネスト含む）
 * - switch文
 * - 三項演算子
 * - ループ（for, while, do-while）
 */
#include <stdio.h>

/* === if-else: 最大値・最小値を求める === */
void example_if_else(void) {
    int a = 5, b = 3, c = 8;
    int max, min;

    /* ネストされたif-else */
    if (a >= b) {
        if (a >= c) {
            max = a;
        } else {
            max = c;
        }
    } else {
        if (b >= c) {
            max = b;
        } else {
            max = c;
        }
    }

    if (a <= b) {
        if (a <= c) {
            min = a;
        } else {
            min = c;
        }
    } else {
        if (b <= c) {
            min = b;
        } else {
            min = c;
        }
    }

    printf("max=%d, min=%d\n", max, min);
}

/* === 三項演算子: 簡潔な条件分岐 === */
void example_ternary(void) {
    int a = 5, b = 3;

    int max = (a >= b) ? a : b;
    int min = (a <= b) ? a : b;

    printf("max=%d, min=%d\n", max, min);

    /* 3つの値から最大値（三項演算子のネスト） */
    int c = 8;
    int max3 = (a >= b) ? ((a >= c) ? a : c) : ((b >= c) ? b : c);
    printf("max3=%d\n", max3);
}

/* === switch文 === */
void example_switch(int n) {
    switch (n) {
        case 1:
            printf("One\n");
            break;
        case 2:
            printf("Two\n");
            break;
        case 3:
            printf("Three\n");
            break;
        default:
            printf("Other\n");
            break;
    }
}

/* === switch文: 範囲判定（成績評価） === */
void grade_evaluation(int score) {
    switch (score / 10) {
        case 10:
        case 9:
            printf("A\n");
            break;
        case 8:
            printf("B\n");
            break;
        case 7:
            printf("C\n");
            break;
        case 6:
            printf("D\n");
            break;
        default:
            printf("F\n");
            break;
    }
}

/* === ループパターン === */
void example_loops(void) {
    /* for: 回数が決まっている場合 */
    printf("for: ");
    for (int i = 0; i < 5; i++) {
        printf("%d ", i);
    }
    printf("\n");

    /* while: 条件が満たされる間 */
    printf("while: ");
    int n = 5;
    while (n > 0) {
        printf("%d ", n);
        n--;
    }
    printf("\n");

    /* do-while: 最低1回は実行 */
    printf("do-while: ");
    int m = 0;
    do {
        printf("%d ", m);
        m++;
    } while (m < 5);
    printf("\n");
}

int main(void) {
    example_if_else();
    example_ternary();
    example_switch(2);
    grade_evaluation(85);
    example_loops();
    return 0;
}
