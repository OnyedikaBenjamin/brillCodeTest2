import java.util.Random;
import java.time.LocalDateTime;
public class NonConcurrent {
private static final int NO_OF_USERS = 3, MAX_PANCAKE_PER_USER = 5, MAX_PANCAKE_PER_SLOT=12;
private int noOfPancakeMade, noOfPancakeConsumed;
private Random ran;
    public NonConcurrent() {
        ran = new Random();
    }
public void service(){
        int startingTime = getCurrentTime();
        int endingTime = startingTime + 30;

        int[] userOrders = generateUserOrders();
        int totalOrders = calculateTotalOrders(userOrders);

        System.out.println("Starting Time: " + formatTime(startingTime) + " PM.");
        System.out.println("Ending Time: " + formatTime(endingTime) + " PM.");

        if (MAX_PANCAKE_PER_SLOT >= totalOrders) {
            noOfPancakeMade = MAX_PANCAKE_PER_SLOT;
            noOfPancakeConsumed = totalOrders;
            System.out.println("Shopkeeper met the needs of all the customers.");
        } else {
            noOfPancakeMade = MAX_PANCAKE_PER_SLOT;
            noOfPancakeConsumed = MAX_PANCAKE_PER_SLOT;
            System.out.println("Shopkeeper could not meet the needs of all the customers.");
            int ordersNotMet = totalOrders - MAX_PANCAKE_PER_SLOT;
            System.out.println("Orders not met: " + ordersNotMet);
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

    private static String formatTime(int timeInSeconds) {
        int hours = timeInSeconds / 3600;
        int minutes = (timeInSeconds % 3600) / 60;
        int seconds = timeInSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static void main(String[] args) {
        NonConcurrent nonConcurrent = new NonConcurrent();
        nonConcurrent.service();
    }




}
