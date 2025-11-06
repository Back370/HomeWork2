#include <stdio.h>
void gobai(int *a, int *b){
    *a = *a * 5;
    *b = *b * 5;
};
int main(void)
{
int a, b;
a = 2;
b = 3;
gobai(&a, &b);
printf("a=%d, b=%d",a,b );
return (0);
}
