/*
 * スタック (ADT) (課題13より)
 * - 抽象データ型としてのスタック
 * - LIFO (Last In, First Out)
 * - Push / Pop / Peek 操作
 */
#include <stdio.h>
#include <stdlib.h>

/* === スタック構造体 === */
typedef struct {
    int max;    /* スタックの最大容量 */
    int ptr;    /* スタックポインタ（現在の要素数） */
    int *stk;   /* スタック本体（動的配列） */
} Stack;

/* === スタックの確保 === */
Stack *StackAlloc(int max) {
    Stack *s = (Stack *)calloc(1, sizeof(Stack));
    if (s == NULL) {
        return NULL;
    }

    s->max = max;
    s->ptr = 0;
    s->stk = (int *)calloc(max, sizeof(int));

    if (s->stk == NULL) {
        free(s);
        return NULL;
    }

    return s;
}

/* === スタックの解放 === */
void StackFree(Stack *s) {
    if (s != NULL) {
        free(s->stk);
        free(s);
    }
}

/* === スタックが空か判定 === */
int StackIsEmpty(const Stack *s) {
    return s->ptr == 0;
}

/* === スタックが満杯か判定 === */
int StackIsFull(const Stack *s) {
    return s->ptr >= s->max;
}

/* === プッシュ（積む） === */
int StackPush(Stack *s, int val) {
    if (StackIsFull(s)) {
        return -1;  /* オーバーフロー */
    }
    s->stk[s->ptr++] = val;
    return 0;
}

/* === ポップ（取り出す） === */
int StackPop(Stack *s, int *val) {
    if (StackIsEmpty(s)) {
        return -1;  /* アンダーフロー */
    }
    *val = s->stk[--s->ptr];
    return 0;
}

/* === ピーク（先頭を参照、取り出さない） === */
int StackPeek(const Stack *s, int *val) {
    if (StackIsEmpty(s)) {
        return -1;
    }
    *val = s->stk[s->ptr - 1];
    return 0;
}

/* === スタックをクリア === */
void StackClear(Stack *s) {
    s->ptr = 0;
}

/* === スタックの要素数 === */
int StackSize(const Stack *s) {
    return s->ptr;
}

/* === スタックの内容を表示 === */
void StackPrint(const Stack *s) {
    printf("Stack [%d/%d]: ", s->ptr, s->max);
    for (int i = 0; i < s->ptr; i++) {
        printf("%d ", s->stk[i]);
    }
    printf("| top\n");
}

/* === 使用例 === */
int main(void) {
    Stack *stack = StackAlloc(10);
    if (stack == NULL) {
        printf("スタックの確保に失敗\n");
        return 1;
    }

    /* プッシュ */
    printf("Push: 10, 20, 30\n");
    StackPush(stack, 10);
    StackPush(stack, 20);
    StackPush(stack, 30);
    StackPrint(stack);

    /* ピーク */
    int val;
    if (StackPeek(stack, &val) == 0) {
        printf("Peek: %d\n", val);
    }

    /* ポップ */
    printf("Pop: ");
    while (StackPop(stack, &val) == 0) {
        printf("%d ", val);
    }
    printf("\n");

    printf("IsEmpty: %s\n", StackIsEmpty(stack) ? "Yes" : "No");

    /* 再度プッシュして確認 */
    printf("\nPush: 100, 200\n");
    StackPush(stack, 100);
    StackPush(stack, 200);
    StackPrint(stack);

    StackFree(stack);
    return 0;
}


/* ===================================
 * スタックの応用例
 * =================================== */

/* 逆順出力 */
void reverse_print(int arr[], int n) {
    Stack *s = StackAlloc(n);
    for (int i = 0; i < n; i++) {
        StackPush(s, arr[i]);
    }

    int val;
    printf("逆順: ");
    while (StackPop(s, &val) == 0) {
        printf("%d ", val);
    }
    printf("\n");

    StackFree(s);
}

/* 括弧の対応チェック（簡易版） */
int check_parentheses(const char *expr) {
    Stack *s = StackAlloc(100);
    int val;

    for (int i = 0; expr[i] != '\0'; i++) {
        if (expr[i] == '(') {
            StackPush(s, '(');
        } else if (expr[i] == ')') {
            if (StackPop(s, &val) != 0) {
                StackFree(s);
                return 0;  /* 対応する'('がない */
            }
        }
    }

    int result = StackIsEmpty(s);
    StackFree(s);
    return result;  /* 空なら全て対応している */
}
