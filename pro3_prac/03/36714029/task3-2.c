#include <stdio.h>

int main(void){
    int a[4][3];
    int b[3][4];
    int ans[4][4];

    printf("a配列に値を入れてください");
    for(int i = 0; i < 4; i++){
        for(int k = 0; k < 3; k++){
            printf("a[%d][%d]:", i, k);
            scanf("%d", &a[i][k]);
        }
    }

    printf("b配列に値を入れてください");
    for(int i = 0; i < 3; i++){
        for(int k = 0; k < 4; k++){
            printf("b[%d][%d]:", i, k);
            scanf("%d", &b[i][k]);
        }
    }
    
    for(int i = 0; i < 4; i++){
        for(int k = 0; k < 4; k++){
            for(int j = 0; j < 3; j++){
                ans[i][k] = a[i][j] + b[j][k];
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