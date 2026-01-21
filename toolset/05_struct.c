/*
 * 構造体 (課題06より)
 * - 構造体の定義
 * - メンバアクセス
 * - 構造体のポインタ
 * - 関数で構造体を返す
 * - typedef
 */
#include <stdio.h>
#include <string.h>

/* === 構造体の定義 === */
struct Student {
    int id;
    char name[50];
    double score;
};

/* === typedef で型名を簡略化 === */
typedef struct {
    double x;
    double y;
    double z;
} Point3D;

/* === 構造体の基本操作 === */
void example_struct_basic(void) {
    struct Student s1;

    /* メンバへの代入 */
    s1.id = 36714137;
    strcpy(s1.name, "Yamada");
    s1.score = 85.5;

    /* メンバの参照 */
    printf("ID: %d\n", s1.id);
    printf("Name: %s\n", s1.name);
    printf("Score: %.1f\n", s1.score);

    /* 初期化子で初期化 */
    struct Student s2 = {12345678, "Tanaka", 90.0};
    printf("s2: %d, %s, %.1f\n", s2.id, s2.name, s2.score);
}

/* === 構造体のポインタ === */
void print_student(struct Student *s) {
    /* アロー演算子でメンバアクセス */
    printf("ID: %d, Name: %s, Score: %.1f\n",
           s->id, s->name, s->score);
}

void example_struct_pointer(void) {
    struct Student s = {11111111, "Suzuki", 75.0};
    struct Student *p = &s;

    /* (*p).member と p->member は同じ */
    printf("(*p).id = %d\n", (*p).id);
    printf("p->id = %d\n", p->id);

    print_student(&s);
}

/* === 構造体を返す関数 === */
Point3D create_point(double x, double y, double z) {
    Point3D p;
    p.x = x;
    p.y = y;
    p.z = z;
    return p;
}

Point3D scan_point(void) {
    Point3D p;
    printf("x y z を入力: ");
    scanf("%lf %lf %lf", &p.x, &p.y, &p.z);
    return p;
}

void example_return_struct(void) {
    Point3D p1 = create_point(1.0, 2.0, 3.0);
    printf("p1 = (%.1f, %.1f, %.1f)\n", p1.x, p1.y, p1.z);

    /* Point3D p2 = scan_point(); */
}

/* === 構造体のメモリレイアウト（アドレス確認） === */
void example_struct_memory(void) {
    struct Student s = {12345678, "Test", 80.0};

    printf("構造体のサイズ: %zu\n", sizeof(struct Student));
    printf("構造体のアドレス: %p\n", (void*)&s);
    printf("id のアドレス: %p (オフセット: %ld)\n",
           (void*)&s.id, (long)((char*)&s.id - (char*)&s));
    printf("name のアドレス: %p (オフセット: %ld)\n",
           (void*)&s.name, (long)((char*)&s.name - (char*)&s));
    printf("score のアドレス: %p (オフセット: %ld)\n",
           (void*)&s.score, (long)((char*)&s.score - (char*)&s));
}

/* === 構造体の配列 === */
void example_struct_array(void) {
    struct Student students[3] = {
        {1, "A", 80.0},
        {2, "B", 85.0},
        {3, "C", 90.0}
    };

    double total = 0;
    for (int i = 0; i < 3; i++) {
        printf("Student %d: %s - %.1f\n",
               students[i].id, students[i].name, students[i].score);
        total += students[i].score;
    }
    printf("平均: %.1f\n", total / 3);
}

int main(void) {
    example_struct_basic();
    printf("\n");
    example_struct_pointer();
    printf("\n");
    example_return_struct();
    printf("\n");
    example_struct_memory();
    printf("\n");
    example_struct_array();
    return 0;
}
