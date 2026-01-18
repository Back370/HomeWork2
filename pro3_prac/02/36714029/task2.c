#include <stdio.h>

int main(void){
    int height;
    float weight;

    scanf("%d", &height);
    weight = (height - 100) * 0.9;

    printf("あなたの重さは%.1fです", weight);
}