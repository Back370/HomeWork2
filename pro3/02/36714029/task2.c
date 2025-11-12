#include<stdio.h>
int main(void)
{
    int height;
    float weight; 

    printf("身長を入力せよ");
    scanf( "%d", &height);
    weight = (height - 100) * 0.9;

    printf("標準体重は%.1fです", weight);
}