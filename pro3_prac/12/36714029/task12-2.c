#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main(void){
    int* ip;
    int n;

    printf("please element size");
    scanf("%d", &n);
    
    ip = (int *)malloc(n * sizeof(int));

    for(int i = 0; i < n; i++){
        ip[i] = i;
    }

    for(int i = 0; i < n; i++){
        printf("%d", ip[i]);
        printf("\n");
    }

    free(ip);
    return 0;


}