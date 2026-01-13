#include <stdio.h>
#include <stdlib.h>
#include "task13.h"

int StackAlloc(Stack *s, int max)
{
	s->ptr = 0;
	if ((s->stk = calloc(max, sizeof(int))) == NULL) {	
		s->max = 0;
		return -1;
	}
	s->max = max;
	return 0;
}

void StackFree(Stack *s)
{
	if (s->stk != NULL) {			
		free(s->stk);				
		s->max = s->ptr = 0;
	}
}

int StackPush(Stack *s, int x)
{
	if (s->ptr >= s->max)					
		 return -1;
	s->stk[s->ptr++] = x;
	return 0;
}

int StackPop(Stack *s, int *x)
{
	if (s->ptr <= 0)						
		return -1;
	*x = s->stk[--s->ptr];
	return 0;
}

int StackPeek(const Stack *s, int *x)
{
	if (s->ptr <= 0)						
		return -1;
	*x = s->stk[s->ptr - 1];
	return 0;
}

int StackSize(const Stack *s)
{
	return s->max;
}

int StackNo(const Stack *s)
{
	return s->ptr;
}

int StackIsEmpty(const Stack *s)
{
	return s->ptr <= 0;
}

int StackIsFull(const Stack *s)
{
	return s->ptr >= s->max;
}

void StackClear(Stack *s)
{
	s->ptr = 0;
}

void StackPrint(const Stack *s)
{
	int i;
	if (s->ptr <= 0) {
		puts("スタックは空です。");
		return;
	}
	printf("スタック内のデータ（%d個）：\n", s->ptr);
	for (i = 0; i < s->ptr; i++) {
		printf("[%d] %d\n", i, s->stk[i]);
	}
}