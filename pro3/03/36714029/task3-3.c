#include <stdio.h>

// 正しい構造体定義（科目の点数を2つ保持する配列）
typedef struct {
    //生徒id
    int id;
    //科目の点数
    int subject[2];
} student;

int main(void) {
    // 構造体の利用例
    student s[4] = { {1, {80, 90}}, {2, {85, 95}}, {3, {78, 88}}, {4, {30, 69}} }; // 2番目の生徒を選択

    int total_subject1 = 0;
    int total_subject2 = 0;
    for(int i = 0; i < 4; i++) {
        total_subject1 += s[i].subject[0];
        total_subject2 += s[i].subject[1];
    }
    printf("一科目目の合計点 %d, 二科目目の合計点: %d\n", total_subject1, total_subject2);
    printf("一科目目の平均点 %d, 二科目目の平均点: %d\n", total_subject1/4, total_subject2/4);
    
    int total_student1 = s[0].subject[0] + s[0].subject[1];
    int total_student2 = s[1].subject[0] + s[1].subject[1];
    int total_student3 = s[2].subject[0] + s[2].subject[1];
    int total_student4 = s[3].subject[0] + s[3].subject[1];
    for(int i = 0; i < 4; i++) {
        printf("%d番目の生徒の合計点 %d\n", i+1, s[i].subject[0] + s[i].subject[1]);
    }
    for(int i = 0; i < 4; i++) {
        printf("%d番目の生徒の平均点 %d\n", i+1, (s[i].subject[0] + s[i].subject[1])/2);
    }


    return 0;
}


