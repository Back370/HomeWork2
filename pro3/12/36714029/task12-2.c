#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main(void){
    int *ip;
    int i, n;

    printf("Enter number of elements: ");
    scanf("%d", &n);

    ip = (int *)malloc(n * sizeof(int));

    printf("Enter %d integers:\n", n);
    for (i = 0; i < n; i++) {
        scanf("%d", &ip[i]);
    }

    for(i = 0; i < n; i++) {
        printf("%d ", ip[i]);
        printf("\n");
    }

    free(ip);
    return 0;
}