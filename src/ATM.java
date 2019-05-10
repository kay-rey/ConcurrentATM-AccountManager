import java.util.Random;

public class ATM {

    private static int MAX_INSERT_THREADS = 200;
    private static int BOUND = 1000;

    private AccountManager account = AccountManager.create();

    /**
     * Gives balance of the account
     */
    class BalanceThread extends Thread {

        public BalanceThread() {
        }

        @Override
        public void run() {
            int accountBalance = account.balance();
            System.out.println(String.format("Balance: $%s",
                    accountBalance));
        }
    }

    void runATM() throws InterruptedException {
        Thread[] allThreads = new Thread[MAX_INSERT_THREADS];

        /*
        randomly create deposit, withdraw and balance threads using the random number generator
        0 = deposit
        1 = withdraw
        2 = balance
         */
        for (int i = 0; i < MAX_INSERT_THREADS; ++i) {
            int outcome = getRandomNumberInRange(0, 2);

            if (outcome == 2) {
                allThreads[i] = new DepositThread(getRandomNumberInRange(1, BOUND));
            } else if (outcome == 1) {
                allThreads[i] = new WithdrawThread(getRandomNumberInRange(1, BOUND));
            } else {
                allThreads[i] = new BalanceThread();
            }
            allThreads[i].run();
        }

        // Join on the all the threads
        for (int i = 0; i < MAX_INSERT_THREADS; ++i) {
            allThreads[i].join();
        }
    }

    /**
     * Deposits money into the account.
     */
    class DepositThread extends Thread {
        int depositAmount;

        public DepositThread(int depositAmount) {
            this.depositAmount = depositAmount;
        }

        @Override
        public void run() {
            System.out.println(String.format("Depositing: $%s",
                    depositAmount));
            account.deposit(depositAmount);
        }
    }

    /**
     * Withdraws money from the account.
     */
    class WithdrawThread extends Thread {
        int withdrawAmount;

        public WithdrawThread(int withdrawAmount) {
            this.withdrawAmount = withdrawAmount;
        }

        @Override
        public void run() {
            System.out.println(String.format("Withdrawing: $%s",
                    withdrawAmount));
            account.withdraw(withdrawAmount);
        }
    }

    public static void main(String[] args) throws Exception {
        ATM mainATM = new ATM();
        mainATM.runATM();
    }

    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

}