#include <math.h>
#include "task15-1.h"

// 関数1: y = sin(x^2) / x
double func1(double x) {
    if (x == 0.0) {
        return 0.0;  // x=0での特異点を回避
    }
    return sin(x * x) / x;
}

// 関数2: y = 1 / x
double func2(double x) {
    if (x == 0.0) {
        return 0.0;  // x=0での特異点を回避
    }
    return 1.0 / x;
}

// 関数3: y = -1 / x
double func3(double x) {
    if (x == 0.0) {
        return 0.0;  // x=0での特異点を回避
    }
    return -1.0 / x;
}
