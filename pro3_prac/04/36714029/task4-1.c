#include <stdio.h>

int main(void){
    int a, b, c;

    printf("a = ");
    scanf("%d", &a);
    printf("b = ");
    scanf("%d", &b);
    printf("c = ");
    scanf("%d", &c);

    if(a < b){
        if(b < c){
            printf("最小値：%d, 最大値：%d", a, c);
        }else{
            if(a < c){
                printf("最小値：%d, 最大値：%d", a, b);
            }else{
                printf("最小値：%d, 最大値：%d", c, b);
            }
        }
    }else{
        if(a < c){
            printf("最小値：%d, 最大値：%d", b, c);
        }else{
            if(b < c){
                printf("最小値：%d, 最大値：%d", b, a);
            }else{
                printf("最小値：%d, 最大値：%d", c, a);
            }
        }
    }
}