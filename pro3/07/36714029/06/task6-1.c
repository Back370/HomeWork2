#include <stdio.h>
#define NAME_LEN 64
struct student{
    char name[NAME_LEN];
    int height;
    double weight;
};
int main(void){
    struct student takao = {"Takao", 173, 70.5};

    printf("\n--- 各メンバのアドレス ---\n");
    printf("takao.nameのアドレス   = %p\n", (void*)&takao.name);
    printf("takao.heightのアドレス = %p\n", (void*)&takao.height);
    printf("takao.weightのアドレス = %p\n", (void*)&takao.weight);

    return 0;
}
