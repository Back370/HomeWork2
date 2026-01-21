#include <stdio.h>

struct xyz{
    int x;
    long y;
    double z;
};

struct xyz xyz_of(int x, long y, double z)
{
    struct xyz temp;

    temp.x = x;
    temp.y = y;
    temp.z = z;

    return temp;
}

int main(void){
    struct xyz s;

    s = xyz_of(4, 222, 2.0);

    printf("xyz.x = %d\n", s.x);
    printf("xyz.y = %ld\n", s.y);
    printf("xyz.z = %f\n", s.z);

    return 0;
} 