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
        scheduler.scheduleAtFixedRate(twistAnimeUpdateRunnable, 0, 5, TimeUnit.MINUTES);
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
            if(twist.hasBeenUpdated()){
                List<Item> items = twist.getUpdatedItems();
                for(Item item : items){
                    sendTweet(item.description + " watch it @ " + item.link);
                }
                twist.setLastUpdatedItem(items.get(0));
            }
        }
    };
}