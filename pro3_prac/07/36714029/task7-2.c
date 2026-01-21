#define _USE_MATH_DEFINES
#include <stdio.h>
#include <math.h>

int main(void){

    FILE* fp;
    double x, y;

    fp = fopen("sin.dat", "w");
    if(fp == NULL){
        printf("\aファイル\"%s\"をオープンできませんでした。\n", "sin.dat");
    }else{
        printf("\aファイル\"%s\"をオープンしました。\n", "sin.dat");
        for(int i=0;i<100;i++){
            x=i/100.0;

            y = sin(2.0 * M_PI * x);

            fprintf(fp, "%lf, %lf\n", x, y);
        }
        fclose(fp);
    }
    return 0;
}