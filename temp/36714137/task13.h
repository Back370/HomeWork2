#ifndef TASK13_H
#define TASK13_H

typedef struct {
    int max;
    int ptr;
    int *stk;
} Stack;

int StackAlloc(Stack *s, int max);
int StackFree(Stack *s);
int StackPush(Stack *s, int x);
int StackPop(Stack *s, int *x);
int StackPeek(const Stack *s, int *x);
int StackSize(const Stack *s);
int StackNo(const Stack *s);
int StackIsEmpty(const Stack *s);
int StackIsFull(const Stack *s);
int StackClear(Stack *s);

#endif
