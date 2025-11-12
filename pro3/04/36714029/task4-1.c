#include<stdio.h>

int main(void){
    double array[3];
    double ans[3];
    for(int i = 0; i < 3; i++){
        scanf("%lf", &array[i]);
    }
    for(int i = 0; i < 2; i++){
        if(array[i] > array[i + 1]){
           double high = array[0];
           double low = array[1];
           array[i + 1] = high;
           array[i] = low;
        }
    }
    printf("最大値：%f", array[2]);
    printf("最小値：%f" ,array[0]);
}