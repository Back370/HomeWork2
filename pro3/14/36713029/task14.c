#include "task14.h"

void IPtoArray(int *ip, int n){
    for (int i = 0; i < n; i++) {
        ip[i] = i + 1;
    }
}

void OutputArrayValue(int *ip, int n){
    printf("配列の値：\n");
    for(int i = 0; i < n; i++) {
        printf("array[%d] = %d\n", i, ip[i]);
    }
}
