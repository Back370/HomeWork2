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
