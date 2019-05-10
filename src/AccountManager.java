/*
Kevin Baltazar Reyes
CSC 415
SFSU Spring 2019

Implement a bank account ledger. The account receives the following operations:
- DEPOSIT
- WITHDRAW
- BALANCE

The first two operations take a single integer and applies it to the balance. The last operation takes no arguments. All operations return the current balance when successful.

DEPOSIT is always successful (you can always add money to your account). It does not throw exceptions.

WITHDRAW is only successful when the balance allows it (no overdrafts). It throws an Exception if there are not enough funds.

BALANCE is always successful no matter what

Very important: there are no overdrafts.
You will write two classes: AccountManager and ATM.
AccountManager has the three public operations deposit, withdraw and balance. It is fully concurrent using the Readers-Writers algorithm. It stores the balance in memory. It does not have a main function.
ATM creates ONE SINGLE instance of AccountManager. It then creates many threads for deposit, withdraw and balance using Java Futures. Have many deposits, many withdraws and many balances run in many threads (~200 each if the machine can handle.) Each of those threads must print the result of the call to AccountManger. This is the class that has the main function.
 */

import java.util.concurrent.Semaphore;

public class AccountManager {

    // Class member variables.
    private Semaphore mutex = new Semaphore(1); //used for reader and write
    private Semaphore write = new Semaphore(1); //used just for writer
    private int read_count = 0;
    private int account_balance = 0;

    /**
     * Creates a new AccountManager.
     */
    public static AccountManager create() {
        return new AccountManager();
    }

    /**
     * Adds the deposit amount into the account and displays balance
     */
    public void deposit(int depAmount) {    //deposit is a writer
        try {
            write.acquire();
//            System.out.println("Write semaphore acquired for insert operator");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        account_balance += depAmount;
        System.out.println("The account balance is: $" + account_balance);

        write.release();
    }

    /**
     * Subtracts the withdrawl amount from the account and displays balance
     */
    public void withdraw(int withdrawAmount) {  //withdraw is a writer
        try {
            write.acquire();
//            System.out.println("Write semaphore acquired for insert operator");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int minBalance = account_balance - withdrawAmount;

        if (minBalance > 0) {   //checks if the balance is greater than 0 if balance is not greater than zero then the balance does not change
            account_balance = minBalance;
            System.out.println("The account balance is: $" + account_balance);
        } else if (minBalance == 0) {
            account_balance = 0;
            System.out.println("WATCH OUT you do not have any more money in the account");
        } else {
            System.out.println("You have overdrafted your account. The withdrawal was rejected remaining balance is still: $" + account_balance);
        }
        write.release();
    }


    public int balance() {  //balance is a reader. return the balance of the account
        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        read_count++;
        if (read_count == 1) {
            try {
                write.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mutex.release();

        int currentBalance = account_balance;

        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        read_count--;
        if (read_count == 0) {  //makes sure that read count gets back to zero before releasing the write semaphore
            write.release();
        }
        mutex.release();

        return currentBalance;

    }

    // Private methods

    /**
     * Private constructor.
     */
    private AccountManager() {
    }

}