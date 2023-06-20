import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Concurrent {

    private static final int NO_OF_USERS = 3, MAX_PANCAKE_PER_USER = 5, MAX_PANCAKE_PER_SLOT=12;
    private int noOfPancakeMade, noOfPancakeConsumed;
    private Random ran;
    public Concurrent() {
        ran = new Random();
    }
    public void service() {
        int startingTime = getCurrentTime();
        int endingTime = startingTime + 30;

        int[] userOrders = generateUserOrders();
        int totalOrders = calculateTotalOrders(userOrders);

        System.out.println("Starting Time: " + startingTime);
        System.out.println("Ending Time: " + endingTime);

        CountDownLatch latch = new CountDownLatch(NO_OF_USERS);
        Semaphore semaphore = new Semaphore(MAX_PANCAKE_PER_SLOT);

        CompletableFuture<Integer>[] futures = new CompletableFuture[NO_OF_USERS];
        for (int i = 0; i < NO_OF_USERS; i++) {
            int order = userOrders[i];
            futures[i] = CompletableFuture.supplyAsync(() -> {
                try {
                    semaphore.acquire(order);
                    return order;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release(order);
                    latch.countDown();
                }
                return 0;
            });
        }

        try {
            latch.await();
            int ordersNotMet = totalOrders - noOfPancakeMade;
            if (ordersNotMet > 0) {
                System.out.println("Shopkeeper could not meet the needs of all the customers.");
                System.out.println("Orders not met: " + ordersNotMet);
            } else {
                System.out.println("Shopkeeper met the needs of all the customers.");
            }

            int wastedPancakes = noOfPancakeMade - noOfPancakeConsumed;
            System.out.println("Pancakes wasted: " + wastedPancakes);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (CompletableFuture<Integer> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private static int getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        int theCurrentTime = now.getHour() * 3600 + now.getMinute() * 60 + now.getSecond();
        return theCurrentTime;
    }

    private int[] generateUserOrders() {
        int[] userOrders = new int[NO_OF_USERS];
        for (int i = 0; i < NO_OF_USERS; i++) {
            userOrders[i] = ran.nextInt(MAX_PANCAKE_PER_USER + 1);
        }
        return userOrders;
    }

    private int calculateTotalOrders(int[] userOrders) {
        int totalOrders = 0;
        for (int i = 0; i < NO_OF_USERS; i++) {
            totalOrders += userOrders[i];
        }
        return totalOrders;
    }
    public static void main(String[] args) {
        Concurrent concurrent = new Concurrent();
        concurrent.service();
    }
}
