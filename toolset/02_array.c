/*
 * 配列・行列操作 (課題03より)
 * - 1次元配列
 * - 2次元配列（多次元配列）
 * - 行列の積
 * - 集計処理（合計・平均）
 */
#include <stdio.h>

/* === 1次元配列の基本 === */
void example_1d_array(void) {
    int arr[5] = {1, 2, 3, 4, 5};
    int n = sizeof(arr) / sizeof(arr[0]);

    /* 走査 */
    for (int i = 0; i < n; i++) {
        printf("arr[%d] = %d\n", i, arr[i]);
    }

    /* 合計・平均 */
    int sum = 0;
    for (int i = 0; i < n; i++) {
        sum += arr[i];
    }
    printf("合計: %d, 平均: %.2f\n", sum, (double)sum / n);
}

/* === 2次元配列の基本 === */
void example_2d_array(void) {
    int matrix[3][4] = {
        {1, 2, 3, 4},
        {5, 6, 7, 8},
        {9, 10, 11, 12}
    };
    int rows = 3, cols = 4;

    /* 走査 */
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            printf("%3d ", matrix[i][j]);
        }
        printf("\n");
    }
}

/* === 行列の積 (A[m][k] × B[k][n] = C[m][n]) === */
void matrix_multiply(void) {
    int A[2][3] = {{1, 2, 3}, {4, 5, 6}};
    int B[3][2] = {{7, 8}, {9, 10}, {11, 12}};
    int C[2][2] = {0};

    int m = 2, k = 3, n = 2;

    /* C[i][j] = Σ A[i][l] * B[l][j] */
    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
            C[i][j] = 0;
            for (int l = 0; l < k; l++) {
                C[i][j] += A[i][l] * B[l][j];
            }
        }
    }

    /* 結果表示 */
    printf("行列の積:\n");
    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
            printf("%4d ", C[i][j]);
        }
        printf("\n");
    }
}

/* === 成績集計（行ごと・列ごとの合計/平均） === */
void grade_summary(void) {
    double scores[3][4] = {
        {80, 70, 90, 85},  /* 学生0の各科目 */
        {75, 85, 80, 90},  /* 学生1の各科目 */
        {90, 95, 85, 80}   /* 学生2の各科目 */
    };
    int students = 3, subjects = 4;

    /* 学生ごとの平均 */
    printf("学生ごとの平均:\n");
    for (int i = 0; i < students; i++) {
        double sum = 0;
        for (int j = 0; j < subjects; j++) {
            sum += scores[i][j];
        }
        printf("  学生%d: %.2f\n", i, sum / subjects);
    }

    /* 科目ごとの平均 */
    printf("科目ごとの平均:\n");
    for (int j = 0; j < subjects; j++) {
        double sum = 0;
        for (int i = 0; i < students; i++) {
            sum += scores[i][j];
        }
        printf("  科目%d: %.2f\n", j, sum / students);
    }
}

int main(void) {
    example_1d_array();
    printf("\n");
    example_2d_array();
    printf("\n");
    matrix_multiply();
    printf("\n");
    grade_summary();
    return 0;
}
