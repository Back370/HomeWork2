#include <stdio.h>
#include <stdlib.h>
#include "task13.h"

int main(void)
{
	Stack stk;

	if (StackAlloc(&stk, 100) == -1) {
		puts("スタックの確保に失敗しました。");
		return 1;
	}
	while (1) {
		int m, x;

		printf("現在のデータ数：%d/%d\n", StackNo(&stk), StackSize(&stk));
		printf("(1) プッシュ  (2) ポップ  (3) 表示  (0) 終了：");
		scanf("%d", &m);
		if (m == 0) break;

		switch (m) {
		 case 1: printf("データ：");  scanf("%d", &x);
				 if (StackPush(&stk, x) == -1)
					puts("プッシュできません。");
				 break;

		 case 2: if (StackPop(&stk, &x) == -1)
					puts("ポップできません。");
				 else
					printf("ポップしたデータは%dです。\n", x);
				 break;

		 case 3: StackPrint(&stk);
				 break;
		}
	}
	StackFree(&stk);

	return 0;
}
