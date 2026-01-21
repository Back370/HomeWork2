#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

#define MAX_TASKS 100
#define MAX_CONTENT 256
#define DATA_FILE "task12.dat"

//task priority level
typedef enum {
    LOW = 1,
    MEDIUM,
    HIGH
} Priority;

//data structures
typedef struct {
    int year;
    int month;
    int day;
} Date;

//task structure
typedef struct {
    int id;
    char content[MAX_CONTENT];
    Priority priority;
    Date deadline;
    int is_active;
} Task;

//todo list structure
typedef struct {
    Task tasks[MAX_TASKS];
    int task_count;
} TodoList;

//scary messages
extern const char* scary_messages[];

void initialize_list(TodoList* list);
void add_task(TodoList* list);
void edit_task(TodoList* list);
void display_monthly_tasks(TodoList* list);
void display_today_tasks(TodoList* list);
void save_to_file(TodoList* list);
void load_from_file(TodoList* list);
void get_current_date(Date* date);
int compare_dates(Date* d1, Date* d2);
void print_priority(Priority p);
char* get_priority_string(Priority p);
void corrupt_text(char* text);
void show_scary_message();
int is_same_month(Date* d1, Date* d2);
int is_same_day(Date* d1, Date* d2);
void print_menu();
void print_separator(int is_cursed);
void input_date(Date* date);
int is_valid_date(Date* date);
void clear_input_buffer();