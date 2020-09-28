package API;

import com.google.gson.Gson;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Twist {

    private static Twist instance;
    private Twitter twitter;

    private Item lastUpdatedItem;

    public static Twist getInstance() {
        if (instance == null)
            instance = new Twist();

        return instance;
    }

    private void sendDirectMessage(String username, String message) {
        try {
            twitter.sendDirectMessage(username, message);
            System.out.println("New direct message has been sent to " + username);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    public List<Item> getItems() {
        try {
            URL base = new URL("https://twist.moe/feed/episodes?format=json");
            InputStreamReader reader = new InputStreamReader(base.openStream());
            Gson gson = new Gson();
            Page page = gson.fromJson(reader, Page.class);
            return page.items;
        } catch (Exception e) {
            sendDirectMessage("lolsisko", "Encountered an exception when trying to visit https://twist.moe/feed/episodes?format=json.");
            System.out.println("Site not working. Can't visit https://twist.moe/feed/episodes?format=json.");
            try {
                TimeUnit.MINUTES.sleep(30);
            } catch (InterruptedException ex) {
                    ex.printStackTrace();
            }
            return null;
        }
    }

    public boolean hasBeenUpdated() {
        Item headerItem = getItems().get(0);
        if (lastUpdatedItem == null)
            lastUpdatedItem = headerItem;

        System.out.println("Last updated item: " + lastUpdatedItem.description);

        if (lastUpdatedItem.description.equalsIgnoreCase(headerItem.description))
            return false;

        return true;
    }

    // Index 0 = new episodes
    // Index 1 = new anime
    public List<List<Item>> getUpdatedItems() {
        List<Item> items = getItems();
        List<Item> updatedItems = new ArrayList<Item>();
        for (int i = 0; i < items.size(); i++) {
            if (!items.get(i).description.equalsIgnoreCase(lastUpdatedItem.description))
                updatedItems.add(items.get(i));
            else
                break;
        }

        List<List<Item>> result = new ArrayList<List<Item>>();
        List<Item> updatedAnime = getUpdatedAnime(updatedItems);
        if (updatedAnime.size() > 0) {
            for (Item anime : updatedAnime) {
                for (Item episode : updatedItems) {
                    if (episode.id == anime.id)
                        updatedItems.remove(episode);
                }
            }
        }

        result.add(updatedItems);
        result.add(updatedAnime);

        lastUpdatedItem = items.get(0);
        return result;
    }

    private List<Item> getUpdatedAnime(List<Item> updatedItems) {
        List<Item> updatedAnime = new ArrayList<Item>();
        int previousId = updatedItems.get(0).id;
        int counter = 0;
        for (int i = 1; i < updatedItems.size(); i++) {
            Item currentItem = updatedItems.get(i);
            if (currentItem.id == previousId)
                counter++;

            if (counter > 5 && !containsAnime(currentItem.id, updatedAnime)) {
                currentItem.link = currentItem.link.substring(0, currentItem.link.lastIndexOf('/'));
                updatedAnime.add(currentItem);
                counter = 0;
            }
            previousId = currentItem.id;
        }

        return updatedAnime;
    }

    private boolean containsAnime(int animeId, List<Item> list) {
        for (Item item : list) {
            if (item.id == animeId)
                return true;
        }

        return false;
    }

    public void setTwitter(Twitter twitter) {
        this.twitter = twitter;
    }
}