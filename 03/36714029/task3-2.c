#include<stdio.h>
#include<string.h>

int main(void)
{
   int a[4][3];
   int b[3][4];
   int ans[4][4];

   printf("4×3行列に記入してください: \n");
   for(int i = 0; i < 4; i++){
    for(int k = 0; k < 3; k++){
        scanf("%d", &a[i][k]);
    }
   }

   printf("3×4行列に記入してください: \n");
   for(int k = 0; k < 3; k++){
    for(int i = 0; i < 4; i++){
        scanf("%d", &b[k][i]);
    }
   }

   // ansをmemsetで確実に0で初期化
   memset(ans, 0, sizeof(ans));

   // 4×3行列 × 3×4行列 = 4×4行列
   for(int i = 0; i < 4; i++){
    for(int j = 0; j < 4; j++){
        for(int k = 0; k < 3; k++){
            ans[i][j] += a[i][k] * b[k][j];
        }
    }
   }

   printf("計算結果（4×4行列）:\n");
   for(int i = 0; i < 4; i++){
        for(int j = 0; j < 4; j++){
            printf("%d ", ans[i][j]);
        }
        printf("\n");
    }
    
    return 0;
}