#include<stdio.h>
int main(void)
{
    int b;
    double a; 

    printf("aに値を入力せよ");
    scanf( "%lf", &a);
    printf("bに値を入力せよ");
    scanf( "%d", &b);

    printf("aの値:%f" ,a);
    printf("bの値:%d" ,b);
}