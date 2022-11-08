#include <stdio.h>
#include <stdlib.h>
#include <math.h>

/* Name: Zaina Walker-Bey
 * Date: September 7, 2022
 * Course: CIS 2107
 * HW Number: Lab 03 ATM Banking
 * Description:
 */

#define pin 3014
double balance_amount = 5000;
int transactions = 0;
int total_deposit = 0;
int total_withdraw = 0;

void balance();

void withdrawal();

void deposit();

void quit();

int main() {
    int amount;
    double deposit_amount;
    int again = 1;
    int option;
    int typedPin;
    int attempts = 0;
    while (again != 0) {

        printf("Welcome to Temple Banking ATM :) \n");
        while (typedPin != pin) {
            attempts++;
            printf("\n Enter 4 digit pin: ");
            scanf("%d", &typedPin);

            if (typedPin != pin && attempts == 3) {
                printf("Wrong pin was entered too many times :( Try again later.");
                exit(1);
            }
            if (typedPin != pin) {
                printf("Wrong pin! Please try again.");
            }
        }

        printf("\nMenu Options:\n");
        printf("1. Balance\n2. Cash Withdrawal\n3. Cash Deposition\n4. Quit");
        printf("\nSelect a option:");
        scanf("%d", &option);
        if (option == 1) {
            balance(balance_amount);
        }
        if (option == 2) {
            //printf("\namount:%d",amount);
            withdrawal(amount);
        }
        if (option == 3) {
            //printf("\namount:%d",amount);
            deposit(deposit_amount);
        }
        if (option == 4) {
            quit();
        }
        printf("\nWould you like to go back to main menu? (1 = yes, 2 = no)");
        scanf("%d", &again);
        if (again == 2) {
            quit();
            exit(1);
        }
    }


    return 0;
}

void balance(double balance_amount) {
    printf("Your balance is: $%.2lf", balance_amount);
}

void withdrawal(int amount) {
    int attempts = 0;
    int limit = 1000;
    int choice = 2;
    while (amount % 20 != 0 || amount <= 0) {
        printf("How much do you want to withdraw?");
        scanf("%d", &amount);
        attempts++;
        if (amount % 20 != 0 && attempts == 3) {
            printf("Invalid amount was entered too many times :( Try again later.\n");
            exit(1);
        }

        if (amount % 20 != 0 || amount <= 0) {
            printf("Invalid amount :( Please try again.\n");
        }
    }
    total_withdraw += amount;
    if (total_withdraw > limit || amount > limit) {
        printf("You took out our limited amount today ($1000)! Please, come again tommorrow.");
        amount = 0;
        transactions--;
    }

    balance_amount -= amount;
    transactions++;

    printf("\nWould you like a receipt :) (1-yes, 2-no)");
    scanf("%d", &choice);
    if (choice == 1) {
        printf("\nRecepit: You have withdrawn $%d!", amount);
    }

}

void deposit(double deposit_money) {
    int attempts = 0;
    int choice = 2;
    int limit = 1000;
    while (deposit_money <= 0 || fmod(deposit_money, 1) != 0) {
        printf("How much do you want to deposit?");
        scanf("%lf", &deposit_money);
        attempts++;
        if (attempts == 3 && (deposit_money <= 0 || fmod(deposit_money, 1) != 0)) {
            printf("Invalid amount was entered too many times :( Try again later.");
            exit(1);
        }

        if (fmod(deposit_money, 1) != 0 || deposit_money <= 0) {
            printf("Invalid amount :( Please try again.\n");
        }
    }

    total_deposit += deposit_money;
    if (total_deposit > limit || deposit_money > limit) {
        printf("You deposited out our limited amount ($1000) today! Please, come again tommorrow.");
        deposit_money = 0;
        transactions--;
    }
    balance_amount += deposit_money;
    transactions++;



    printf("\nWould you like a receipt :) (1-yes, 2-no)");
    scanf("%d", &choice);
    if (choice == 1) {
        printf("\nRecepit: You have deposited $%.2lf!", deposit_money);
    }


}

void quit() {
    printf("Total Transactions: %d", transactions);
    printf("\nThank you for banking with Temple Banking :)\nHave a great day!");
    exit(1);

}

