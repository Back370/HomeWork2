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
