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

    private static String formatTime(int timeInSeconds) {
        int hours = timeInSeconds / 3600;
        int minutes = (timeInSeconds % 3600) / 60;
        int seconds = timeInSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public void service() {
        int startingTime = getCurrentTime();
        int endingTime = startingTime + 30;

        int[] userOrders = generateUserOrders();
        int totalOrders = calculateTotalOrders(userOrders);

        System.out.println("Starting Time: " + formatTime(startingTime) + " PM.");
        System.out.println("Ending Time: " + formatTime(endingTime) + " PM.");

        int noOfPancakeMade = Math.min(MAX_PANCAKE_PER_SLOT, totalOrders);
        int noOfPancakeConsumed = 0;

        if (noOfPancakeMade >= totalOrders) {
            System.out.println("Shopkeeper met the needs of all the customers.");
            noOfPancakeConsumed = totalOrders;
        } else {
            System.out.println("Shopkeeper could not meet the needs of all the customers.");
            System.out.println("Orders not met: " + (totalOrders - noOfPancakeMade));
            noOfPancakeConsumed = noOfPancakeMade;
        }

        int wastedPancakes = noOfPancakeMade - noOfPancakeConsumed;
        System.out.println("Pancakes wasted: " + wastedPancakes);
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
