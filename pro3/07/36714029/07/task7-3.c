#include <stdio.h>
struct input_data{
    int x;
    long y;
    double z;
};
int main(void){
    struct input_data data;

    printf("int型、long型、double型の各変数のサイズを入力せよ。\n");
    scanf("%d %ld %lf", &data.x, &data.y, &data.z);


    printf("a = %d\n", data.x);
    printf("b= %ld\n", data.y);
    printf("c= %.1f\n", data.z);

    return 0;
}

