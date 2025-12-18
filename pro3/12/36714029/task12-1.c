#include "task12-1.h"

void initialize_list(TodoList* list) {
    list->task_count = 0;
    for (int i = 0; i < MAX_TASKS; i++) {
        list->tasks[i].is_active = 0;
        list->tasks[i].id = 0;
    }
}

void add_task(TodoList* list) {
    if (list->task_count >= MAX_TASKS) {
        printf("\nCannot add more tasks.\n");
        return;
    }

    Task* new_task = &list->tasks[list->task_count];
    new_task->id = list->task_count + 1;
    new_task->is_active = 1;

    printf("\n========== Task Registration ==========\n");

    printf("Enter task content: ");
    fgets(new_task->content, MAX_CONTENT, stdin);
    new_task->content[strcspn(new_task->content, "\n")] = 0;

    int priority_choice;
    printf("\nSelect task priority:\n");
    printf("1. Low\n");
    printf("2. Medium\n");
    printf("3. High\n");
    printf("Choice (1-3): ");
    scanf("%d", &priority_choice);
    clear_input_buffer();

    switch (priority_choice) {
        case 1:
            new_task->priority = LOW;
            break;
        case 2:
            new_task->priority = MEDIUM;
            break;
        case 3:
            new_task->priority = HIGH;
            break;
        default:
            new_task->priority = MEDIUM;
    }

    printf("\nEnter task deadline:\n");
    input_date(&new_task->deadline);

    if (rand() % 10 == 0) {
        corrupt_text(new_task->content);
        printf("\n\033[31mWARNING: Data corrupted...\033[0m\n");
    }

    list->task_count++;
    printf("\nTask registered. (ID: %d)\n", new_task->id);
}

void edit_task(TodoList* list) {
    if (list->task_count == 0) {
        printf("\nNo tasks to edit.\n");
        return;
    }

    printf("\n========== Task Edit ==========\n");
    printf("Current task list:\n");

    for (int i = 0; i < list->task_count; i++) {
        if (list->tasks[i].is_active) {
            printf("ID %d: %s [%s] (Deadline: %d/%d/%d)\n",
                   list->tasks[i].id,
                   list->tasks[i].content,
                   get_priority_string(list->tasks[i].priority),
                   list->tasks[i].deadline.year,
                   list->tasks[i].deadline.month,
                   list->tasks[i].deadline.day);
        }
    }

    int edit_id;
    printf("\nEnter task ID to edit: ");
    scanf("%d", &edit_id);
    clear_input_buffer();

    Task* task = NULL;
    for (int i = 0; i < list->task_count; i++) {
        if (list->tasks[i].id == edit_id && list->tasks[i].is_active) {
            task = &list->tasks[i];
            break;
        }
    }

    if (task == NULL) {
        printf("\nTask ID not found.\n");
        return;
    }

    printf("\n1. Edit content\n");
    printf("2. Edit priority\n");
    printf("3. Edit deadline\n");
    printf("4. Delete task\n");
    printf("Choice: ");

    int edit_choice;
    scanf("%d", &edit_choice);
    clear_input_buffer();

    switch (edit_choice) {
        case 1:
            printf("New content: ");
            fgets(task->content, MAX_CONTENT, stdin);
            task->content[strcspn(task->content, "\n")] = 0;
            break;
        case 2:
            printf("New priority (1:Low, 2:Medium, 3:High): ");
            int new_priority;
            scanf("%d", &new_priority);
            clear_input_buffer();
            task->priority = (Priority)new_priority;
            break;
        case 3:
            printf("New deadline:\n");
            input_date(&task->deadline);
            break;
        case 4:
            task->is_active = 0;
            printf("Task deleted.\n");
            return;
        default:
            printf("Invalid choice.\n");
            return;
    }

    printf("Task updated.\n");
}
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

