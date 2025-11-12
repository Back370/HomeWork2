#include<stdio.h>

int main(void){
    double array[3];
    for(int i = 0; i < 3; i++){
        scanf("%lf", &array[i]);
    }
    double high = 0;
    double low = 0;

    if(array[0] >= array[1]){
        if(array[1] >= array[2]){
            high = array[0];
            low = array[2];
        }else if(array[0] >= array[2]){
            high = array[0];
            low = array[1];
        }else{
            high = array[2];
            low = array[1];
        }
    }else{
        if(array[0] >= array[2]){
            high = array[1];
            low = array[2];
        }else if(array[1] >= array[2]){
            high = array[1];
            low = array[0];
        }else{
            high = array[2];
            low = array[0];
        }
    }
    
    printf("最大値：%f", high);
    printf("最小値：%f" ,low);
}