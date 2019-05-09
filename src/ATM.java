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
            account.balance();
        }
    }

    /**
     * Inserts strings into the account.
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

    void runATM() throws InterruptedException {
        Thread depositThreads[] = new Thread[MAX_INSERT_THREADS];
        Thread balanceThreads[] = new Thread[MAX_INSERT_THREADS];
        Thread withdrawThreads[] = new Thread[MAX_INSERT_THREADS];

        // Insert random strings
        for (int i = 0; i < MAX_INSERT_THREADS; i++) {
            depositThreads[i] = new DepositThread(getRandomNumberInRange(0, BOUND));
            depositThreads[i].join();
            depositThreads[i].run();
        }

        // Join on the inserts
//        for (int i = 0; i < MAX_INSERT_THREADS; i++) {
//            depositThreads[i].join();
//        }

        for (int i = 0; i < MAX_INSERT_THREADS; i++) {
            withdrawThreads[i] = new WithdrawThread(getRandomNumberInRange(0, BOUND));
            withdrawThreads[i].join();
            withdrawThreads[i].run();
        }

        // Join on the inserts
//        for (int i = 0; i < MAX_INSERT_THREADS; i++) {
//            withdrawThreads[i].join();
//        }

        // Run the comparisons
        for (int i = 0; i < MAX_INSERT_THREADS; i++) {
            balanceThreads[i] = new BalanceThread();
            balanceThreads[i].join();
            balanceThreads[i].run();
        }
//        for (int i = 0; i < MAX_COMPARE_THREADS; i++) {
//            balanceThreads[i].join();
//        }
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