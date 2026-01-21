/*
 * ポインタ (課題05より)
 * - ポインタの基本
 * - 配列とポインタ
 * - 参照渡し（ポインタ渡し）
 * - ポインタを使った配列操作
 */
#include <stdio.h>

/* === ポインタの基本 === */
void example_pointer_basic(void) {
    int a = 10;
    int *p = &a;  /* aのアドレスをpに格納 */

    printf("a = %d\n", a);
    printf("&a = %p\n", (void*)&a);
    printf("p = %p\n", (void*)p);
    printf("*p = %d\n", *p);  /* pが指す値 */

    *p = 20;  /* ポインタ経由で値を変更 */
    printf("a = %d (変更後)\n", a);
}

/* === 配列とポインタ === */
void example_array_pointer(void) {
    int arr[5] = {10, 20, 30, 40, 50};
    int *p = arr;  /* 配列名は先頭要素のアドレス */

    printf("ポインタで配列走査:\n");
    for (int i = 0; i < 5; i++) {
        printf("  *(p+%d) = %d, p[%d] = %d\n", i, *(p+i), i, p[i]);
    }

    /* ポインタのインクリメント */
    printf("ポインタ移動:\n");
    p = arr;
    for (int i = 0; i < 5; i++) {
        printf("  *p = %d\n", *p);
        p++;
    }
}

/* === 参照渡し: 関数内で呼び出し元の値を変更 === */
void swap(int *a, int *b) {
    int tmp = *a;
    *a = *b;
    *b = tmp;
}

void multiply_by_5(int *a) {
    *a = *a * 5;
}

void example_pass_by_reference(void) {
    int x = 10, y = 20;
    printf("swap前: x=%d, y=%d\n", x, y);
    swap(&x, &y);
    printf("swap後: x=%d, y=%d\n", x, y);

    int n = 7;
    printf("multiply_by_5前: n=%d\n", n);
    multiply_by_5(&n);
    printf("multiply_by_5後: n=%d\n", n);
}

/* === 配列の特定位置から値を設定 === */
void ary_set(int *p, int n, int val) {
    for (int i = 0; i < n; i++) {
        p[i] = val;  /* または *(p+i) = val; */
    }
}

void example_array_set(void) {
    int arr[5] = {1, 2, 3, 4, 5};

    printf("変更前: ");
    for (int i = 0; i < 5; i++) printf("%d ", arr[i]);
    printf("\n");

    /* arr[2]から2要素を99に設定 */
    ary_set(&arr[2], 2, 99);

    printf("変更後: ");
    for (int i = 0; i < 5; i++) printf("%d ", arr[i]);
    printf("\n");
}

/* === ポインタのポインタ === */
void example_pointer_to_pointer(void) {
    int a = 100;
    int *p = &a;
    int **pp = &p;

    printf("a = %d\n", a);
    printf("*p = %d\n", *p);
    printf("**pp = %d\n", **pp);

    **pp = 200;
    printf("a = %d (変更後)\n", a);
}

int main(void) {
    example_pointer_basic();
    printf("\n");
    example_array_pointer();
    printf("\n");
    example_pass_by_reference();
    printf("\n");
    example_array_set();
    printf("\n");
    example_pointer_to_pointer();
    return 0;
}
