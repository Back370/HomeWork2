#define _USE_MATH_DEFINES
#include <math.h>
#include <stdio.h>

int main(void){
    FILE *fp;
    double x, y;

    if((fp=fopen("sin.dat","w"))==NULL){
        printf("ファイルをオープンできません。\n");
    }else{
        for(int i=0;i<100;i++){
            x=i/100.0;

            y = sin(2.0 * M_PI * x);

            fprintf(fp, "%lf, %lf\n", x, y);
        }
        fclose(fp);
    }
    return 0;
}