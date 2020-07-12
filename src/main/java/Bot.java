import API.Item;
import API.Twist;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.ArrayList;
import java.util.Collections;
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
                List<ArrayList> temp = twist.getUpdatedItems();
                ArrayList<Item> updatedEpisodes = temp.get(0);
                ArrayList<Item> updatedAnime = temp.get(1);
                for(Item item : updatedEpisodes){
                    sendTweet(item.description + " watch it @ " + item.link);
                }
                for(Item item : updatedAnime){
                    sendTweet(item.title + " has just been added to Twist! Watch it @ " + item.link);
                }
                twist.setLastUpdatedItem(updatedEpisodes.get(0));
            }
        }
    };
}