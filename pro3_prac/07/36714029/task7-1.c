#include <stdio.h>

int main(void){
    char filename[256];

    printf("ファイル名を入力してください");
    scanf("%s", filename);

    FILE* fp;
    fp = fopen(filename, "r");
    if(fp == NULL){
        printf("\aファイル\"%s\"をオープンできませんでした。\n", filename);
    }else{
        printf("\aファイル\"%s\"をオープンしました。\n", filename);
        fclose(fp);
    }
    return 0;
}