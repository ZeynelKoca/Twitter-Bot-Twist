import API.Item;
import API.Twist;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Bot {

    private static Twitter twitter;
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
            sendTwitterExceptionDM();
            System.out.println("Twitter Exception for '" + text + "' :");
            e.printStackTrace();
        }
    }

    private static void sendTwitterExceptionDM(){
        try {
            twitter.sendDirectMessage("lolsisko", "Encountered a TwitterException. Check the logs and restart the worker.");
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    private static Runnable twistAnimeUpdateRunnable = new Runnable() {
        public void run() {
            if (twist.hasBeenUpdated()) {
                System.out.println("Updated");
                List<List<Item>> updatedItems = twist.getUpdatedItems();
                List<Item> updatedEpisodes = updatedItems.get(0);
                List<Item> updatedAnime = updatedItems.get(1);

                if (updatedEpisodes.size() > 0) {
                    for (Item item : updatedEpisodes)
                        sendTweet(item.description + " watch it @ " + item.link);
                }
                if(updatedAnime.size() > 0){
                    for(Item item : updatedAnime)
                        sendTweet(item.title + " has just been added to Twist! Watch it @ " + item.link);
                }
            }
        }
    };
}