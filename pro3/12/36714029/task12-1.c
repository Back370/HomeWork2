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
void display_monthly_tasks(TodoList* list) {
    Date current_date;
    get_current_date(&current_date);

    int is_cursed = (rand() % 8 == 0);

    printf("\n");
    print_separator(is_cursed);
    if (is_cursed) {
        printf("\033[31m");
    }
    printf("     Monthly Task List (%d/%d)\n", current_date.year, current_date.month);
    if (is_cursed) {
        printf("\033[0m");
    }
    print_separator(is_cursed);

    int found = 0;
    Task* task_ptr;

    for (int i = 0; i < list->task_count; i++) {
        task_ptr = &list->tasks[i];

        if (task_ptr->is_active && is_same_month(&task_ptr->deadline, &current_date)) {
            found = 1;

            if (is_cursed) {
                printf("\033[31m");
            }

            printf("Date: %d/%d\n", task_ptr->deadline.month, task_ptr->deadline.day);
            printf("  Content: %s\n", task_ptr->content);
            printf("  Priority: ");
            print_priority(task_ptr->priority);
            printf("\n  Deadline: %d/%d/%d\n",
                   task_ptr->deadline.year,
                   task_ptr->deadline.month,
                   task_ptr->deadline.day);

            if (is_cursed) {
                printf("\033[0m");
            }

            print_separator(0);
        }
    }

    if (!found) {
        printf("No tasks for this month.\n");
        print_separator(0);
    }

    if (rand() % 6 == 0) {
        printf("\n\033[31m");
        printf("Date: ??/??\n");
        printf("  Content: %s\n", scary_messages[rand() % 8]);
        printf("  Priority: MAXIMUM\n");
        printf("  Deadline: NOW\n");
        printf("\033[0m");
        print_separator(1);
    }
}

void display_today_tasks(TodoList* list) {
    Date current_date;
    get_current_date(&current_date);

    printf("\n========================================\n");
    printf("  Today's Tasks (%d/%d/%d)\n",
           current_date.year, current_date.month, current_date.day);
    printf("========================================\n");

    int found = 0;
    int task_number = 1;

    for (int i = 0; i < list->task_count; i++) {
        Task* task = &list->tasks[i];

        if (task->is_active && is_same_day(&task->deadline, &current_date)) {
            found = 1;
            printf("\n%d. %s\n", task_number++, task->content);
            printf("   Priority: ");
            print_priority(task->priority);
            printf("\n");
        }
    }

    if (!found) {
        printf("\nNo tasks for today.\n");
    }

    printf("========================================\n");
}

void save_to_file(TodoList* list) {
    FILE* file = fopen(DATA_FILE, "wb");
    if (file == NULL) {
        printf("Could not open file.\n");
        return;
    }

    fwrite(&list->task_count, sizeof(int), 1, file);
    fwrite(list->tasks, sizeof(Task), MAX_TASKS, file);

    fclose(file);
}
void load_from_file(TodoList* list);
void get_current_date(Date* date);
int compare_dates(Date* d1, Date* d2);
void print_priority(Priority p);
char* get_priority_string(Priority p);
void corrupt_text(char* text);
void show_scary_message();
int is_same_month(Date* d1, Date* d2);
int is_same_day(Date* d1, Date* d2);
void print_menu() {
    printf("\n========================================\n");
    printf("1. Register Task\n");
    printf("2. Edit Task\n");
    printf("3. Display Monthly Tasks\n");
    printf("4. Display Today's Tasks\n");
    printf("5. Exit\n");
    printf("========================================\n");
}
void print_separator(int is_cursed) {
    if (is_cursed) {
        printf("\033[31m");
        for (int i = 0; i < 60; i++) {
            printf("=");
        }
        printf("\033[0m\n");
    } else {
        for (int i = 0; i < 60; i++) {
            printf("-");
        }
        printf("\n");
    }
}
void input_date(Date* date);
int is_valid_date(Date* date);
void clear_input_buffer();

