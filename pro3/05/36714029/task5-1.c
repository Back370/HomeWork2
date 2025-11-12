#include <stdio.h>
#define NAME_LEN 64
struct student{
    char name[NAME_LEN];
    int height;
    double weight;
};
int main(void){
    struct student takao = {"Takao", 173, 70.5};

    printf("氏名 = %s\n", takao.name);
    printf("身長 = %d\n", takao.height);
    printf("体重 = %.1f\n", takao.weight);

    return 0;
}
