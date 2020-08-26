import API.Item;
import API.Twist;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Bot {

    public static Twitter twitter;

    private static Twist twist;

    public static void main(String[] args) {
        twitter = new Config().getTwitterInstance();
        twist = Twist.getInstance();

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(twistAnimeUpdateRunnable, 0, 10, TimeUnit.MINUTES);
    }

    public static void sendDirectMessage(String username, String message) {
        try {
            twitter.sendDirectMessage(username, message);
            System.out.println("New direct message has been sent to " + username);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    private static void sendTweet(String text) {
        try {
            twitter.updateStatus(text);
            System.out.println("New tweet has been sent: " + text);
        } catch (TwitterException e) {
            System.out.println("Twitter Exception:");
            e.printStackTrace();
        }
    }

    private static Runnable twistAnimeUpdateRunnable = new Runnable() {
        public void run() {
            if (twist.isSiteWorking()) {
                if (twist.hasBeenUpdated()) {
                    List<Item> items = twist.getUpdatedItems();
                    for (Item item : items)
                        sendTweet(item.description + " watch it @ " + item.link);

                    twist.setLastUpdatedItem(twist.getItems().get(0));
                } else{
                    System.out.println("Site working, but no new anime update. Last updated item: " + twist.getLastUpdatedItem().link);
                }
            } else {
                    System.out.println("CAN'T ACCESS TWIST.MOE THREAD SLEEP FOR 30 MINUTES");
                    sendDirectMessage("lolsisko", "Encountered an exception when trying to visit https://twist.moe/feed/episodes?format=json.");
                try {
                    TimeUnit.MINUTES.sleep(30);
                } catch (InterruptedException e) {
                    System.out.println("Thread sleeping interrupted:");
                    e.printStackTrace();
                }
            }
        }
    };
}