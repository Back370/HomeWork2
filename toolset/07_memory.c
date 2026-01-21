/*
 * 動的メモリ管理 (課題13, 14より)
 * - malloc / calloc / realloc / free
 * - 動的配列
 * - メモリリーク防止
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/* === malloc: メモリ割り当て（初期化なし） === */
void example_malloc(void) {
    int n = 5;
    int *arr = (int *)malloc(n * sizeof(int));

    if (arr == NULL) {
        perror("malloc");
        return;
    }

    /* 初期化されていないので自分で初期化 */
    for (int i = 0; i < n; i++) {
        arr[i] = i * 10;
    }

    printf("malloc: ");
    for (int i = 0; i < n; i++) {
        printf("%d ", arr[i]);
    }
    printf("\n");

    free(arr);  /* 必ず解放 */
}

/* === calloc: メモリ割り当て（ゼロ初期化） === */
void example_calloc(void) {
    int n = 5;
    int *arr = (int *)calloc(n, sizeof(int));

    if (arr == NULL) {
        perror("calloc");
        return;
    }

    /* callocはゼロ初期化されている */
    printf("calloc (初期値): ");
    for (int i = 0; i < n; i++) {
        printf("%d ", arr[i]);
    }
    printf("\n");

    free(arr);
}

/* === realloc: メモリ再割り当て === */
void example_realloc(void) {
    int *arr = (int *)malloc(3 * sizeof(int));
    if (arr == NULL) return;

    arr[0] = 1; arr[1] = 2; arr[2] = 3;

    printf("realloc前: ");
    for (int i = 0; i < 3; i++) printf("%d ", arr[i]);
    printf("\n");

    /* サイズを5に拡張 */
    int *new_arr = (int *)realloc(arr, 5 * sizeof(int));
    if (new_arr == NULL) {
        free(arr);  /* reallocが失敗しても元のメモリは有効 */
        return;
    }
    arr = new_arr;

    arr[3] = 4; arr[4] = 5;

    printf("realloc後: ");
    for (int i = 0; i < 5; i++) printf("%d ", arr[i]);
    printf("\n");

    free(arr);
}

/* === 関数から動的配列を返す === */
int *array_input(int num) {
    if (num <= 0) {
        return NULL;
    }

    int *arr = (int *)calloc(num, sizeof(int));
    if (arr == NULL) {
        return NULL;
    }

    /* 値を設定（例: 逆順） */
    for (int i = 0; i < num; i++) {
        arr[i] = num - i;
    }

    return arr;
}

void example_return_array(void) {
    int n = 5;
    int *arr = array_input(n);

    if (arr == NULL) {
        printf("配列の作成に失敗\n");
        return;
    }

    printf("関数から返された配列: ");
    for (int i = 0; i < n; i++) {
        printf("%d ", arr[i]);
    }
    printf("\n");

    free(arr);  /* 呼び出し側で解放 */
}

/* === 2次元配列の動的確保 === */
int **create_2d_array(int rows, int cols) {
    int **arr = (int **)malloc(rows * sizeof(int *));
    if (arr == NULL) return NULL;

    for (int i = 0; i < rows; i++) {
        arr[i] = (int *)calloc(cols, sizeof(int));
        if (arr[i] == NULL) {
            /* 途中で失敗したら確保済みを解放 */
            for (int j = 0; j < i; j++) {
                free(arr[j]);
            }
            free(arr);
            return NULL;
        }
    }
    return arr;
}

void free_2d_array(int **arr, int rows) {
    if (arr == NULL) return;
    for (int i = 0; i < rows; i++) {
        free(arr[i]);
    }
    free(arr);
}

void example_2d_dynamic(void) {
    int rows = 3, cols = 4;
    int **matrix = create_2d_array(rows, cols);

    if (matrix == NULL) {
        printf("2次元配列の作成に失敗\n");
        return;
    }

    /* 値を設定 */
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            matrix[i][j] = i * cols + j;
        }
    }

    /* 表示 */
    printf("2次元動的配列:\n");
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            printf("%3d ", matrix[i][j]);
        }
        printf("\n");
    }

    free_2d_array(matrix, rows);
}

/* === 動的文字列 === */
char *duplicate_string(const char *src) {
    size_t len = strlen(src) + 1;
    char *dst = (char *)malloc(len);
    if (dst == NULL) return NULL;
    strcpy(dst, src);
    return dst;
}

void example_dynamic_string(void) {
    char *str = duplicate_string("Hello, Dynamic!");
    if (str != NULL) {
        printf("複製文字列: %s\n", str);
        free(str);
    }
}

int main(void) {
    example_malloc();
    example_calloc();
    example_realloc();
    printf("\n");
    example_return_array();
    printf("\n");
    example_2d_dynamic();
    printf("\n");
    example_dynamic_string();
    return 0;
}
