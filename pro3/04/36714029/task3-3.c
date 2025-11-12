#include <stdio.h>

// 正しい構造体定義（科目の点数を2つ保持する配列）
typedef struct {
    //生徒id
    int id;
    //科目の点数
    int subject[2];
} student;

int main(void) {
    // 入力を受け取る形式に変更：4人分のidと2科目の点数を読み込む
    student s[4];

    printf("4人分の生徒IDと2科目の点数を入力してください。各行に「id 科目1 科目2」の形式で入力します。\n");
    for (int i = 0; i < 4; i++) {
        printf("%d人目: ", i+1);
        if (scanf("%d %d %d", &s[i].id, &s[i].subject[0], &s[i].subject[1]) != 3) {
            fprintf(stderr, "入力が不正です。整数を3つ入力してください。\n");
            return 1;
        }
    }

    int total_subject1 = 0;
    int total_subject2 = 0;
    for (int i = 0; i < 4; i++) {
        total_subject1 += s[i].subject[0];
        total_subject2 += s[i].subject[1];
    }
    printf("一科目目の合計点 %d, 二科目目の合計点: %d\n", total_subject1, total_subject2);
    printf("一科目目の平均点 %d, 二科目目の平均点: %d\n", total_subject1/4, total_subject2/4);
    
    for (int i = 0; i < 4; i++) {
        int total = s[i].subject[0] + s[i].subject[1];
        printf("%d番目の生徒 (ID=%d) の合計点 %d\n", i+1, s[i].id, total);
    }
    for (int i = 0; i < 4; i++) {
        int total = s[i].subject[0] + s[i].subject[1];
        printf("%d番目の生徒 (ID=%d) の平均点 %d\n", i+1, s[i].id, total/2);
    }


    return 0;
}


