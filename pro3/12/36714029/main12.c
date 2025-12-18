#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include "task12-1.h"


int main() {
    TodoList list;
    int choice;

    srand(time(NULL));

    initialize_list(&list);
    load_from_file(&list);

    printf("\n========================================\n");
    printf("  Cursed Todo List Management System\n");
    printf("========================================\n");

    while (1) {
        print_menu();
        printf("Please select: ");

        if (scanf("%d", &choice) != 1) {
            clear_input_buffer();
            printf("\nInvalid input.\n");
            continue;
        }
        clear_input_buffer();

        switch (choice) {
            case 1:
                add_task(&list);
                save_to_file(&list);
                break;
            case 2:
                edit_task(&list);
                save_to_file(&list);
                break;
            case 3:
                display_monthly_tasks(&list);
                break;
            case 4:
                display_today_tasks(&list);
                break;
            case 5:
                printf("\nExiting program...\n");
                printf("Goodbye... See you again...\n");
                save_to_file(&list);
                return 0;
            default:
                printf("\nInvalid choice.\n");
        }

        if (rand() % 5 == 0) {
            show_scary_message();
        }
    }

    return 0;
}


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

void load_from_file(TodoList* list) {
    FILE* file = fopen(DATA_FILE, "rb");
    if (file == NULL) {
        return;
    }

    fread(&list->task_count, sizeof(int), 1, file);
    fread(list->tasks, sizeof(Task), MAX_TASKS, file);

    fclose(file);
}

void get_current_date(Date* date) {
    time_t t = time(NULL);
    struct tm* tm_info = localtime(&t);

    date->year = tm_info->tm_year + 1900;
    date->month = tm_info->tm_mon + 1;
    date->day = tm_info->tm_mday;
}

int compare_dates(Date* d1, Date* d2) {
    if (d1->year != d2->year) return d1->year - d2->year;
    if (d1->month != d2->month) return d1->month - d2->month;
    return d1->day - d2->day;
}

int is_same_month(Date* d1, Date* d2) {
    return (d1->year == d2->year && d1->month == d2->month);
}

int is_same_day(Date* d1, Date* d2) {
    return (d1->year == d2->year && d1->month == d2->month && d1->day == d2->day);
}

void print_priority(Priority p) {
    switch (p) {
        case LOW:
            printf("Low");
            break;
        case MEDIUM:
            printf("Medium");
            break;
        case HIGH:
            printf("High");
            break;
    }
}

char* get_priority_string(Priority p) {
    switch (p) {
        case LOW:
            return "Low";
        case MEDIUM:
            return "Medium";
        case HIGH:
            return "High";
        default:
            return "Unknown";
    }
}

void corrupt_text(char* text) {
    int len = strlen(text);
    for (int i = 0; i < len; i++) {
        if (rand() % 3 == 0) {
            text[i] = '?' + (rand() % 30);
        }
    }
}

void show_scary_message() {
    printf("\n\033[31m");
    printf("******************************************\n");
    printf("  %s\n", scary_messages[rand() % 8]);
    printf("******************************************\n");
    printf("\033[0m\n");
}

void input_date(Date* date) {
    do {
        printf("Enter year: ");
        scanf("%d", &date->year);
        printf("Enter month (1-12): ");
        scanf("%d", &date->month);
        printf("Enter day (1-31): ");
        scanf("%d", &date->day);
        clear_input_buffer();

        if (!is_valid_date(date)) {
            printf("\nInvalid date. Please try again.\n");
        }
    } while (!is_valid_date(date));
}

int is_valid_date(Date* date) {
    if (date->year < 1900 || date->year > 2100) return 0;
    if (date->month < 1 || date->month > 12) return 0;

    int days_in_month[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    if ((date->year % 4 == 0 && date->year % 100 != 0) || (date->year % 400 == 0)) {
        days_in_month[1] = 29;
    }

    if (date->day < 1 || date->day > days_in_month[date->month - 1]) return 0;

    return 1;
}

void clear_input_buffer() {
    int c;
    while ((c = getchar()) != '\n' && c != EOF);
}
