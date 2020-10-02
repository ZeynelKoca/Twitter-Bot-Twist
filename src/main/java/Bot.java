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
        twist.setTwitter(twitter);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(twistAnimeUpdateRunnable, 0, 10, TimeUnit.MINUTES);
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
            if (twist.hasBeenUpdated()) {
                System.out.println("Updated");
                List<List<Item>> updatedItems = twist.getUpdatedItems();
                List<Item> updatedEpisodes = updatedItems.get(0);
                List<Item> updatedAnime = updatedItems.get(1);

                //System.out.println("Before. Anime size: " + updatedAnime.size() + ". Episode size: " + updatedEpisodes.size());
                if (updatedEpisodes.size() > 0) {
                    //System.out.println("Here " + updatedEpisodes.get(0).id);
                    for (Item item : updatedEpisodes)
                        sendTweet(item.description + " watch it @ " + item.link);
                }
                if(updatedAnime.size() > 0){
                    //System.out.println("Here2: " + updatedAnime.get(0).id);
                    for(Item item : updatedAnime)
                        sendTweet(item.title + " has just been added to Twist! Watch it @ " + item.link);
                }
            }

        }
    };
}