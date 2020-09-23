package API;

import com.google.gson.Gson;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Twist {

    private static Twist instance;
    private Twitter twitter;

    private Item lastUpdatedItem;
    public boolean isSiteWorking;

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
            this.isSiteWorking = true;
            return page.items;
        } catch (Exception e) {
            this.isSiteWorking = false;
            sendDirectMessage("lolsisko", "Encountered an exception when trying to visit https://twist.moe/feed/episodes?format=json.");
            System.out.println("Site not working: ");
            e.printStackTrace();
            return null;
        }
    }

    public boolean hasBeenUpdated() {
        Item headerItem = getItems().get(0);
        if (lastUpdatedItem == null)
            lastUpdatedItem = headerItem;

        if (lastUpdatedItem.description.equalsIgnoreCase(headerItem.description))
            return false;

        return true;
    }

    public List<Item> getUpdatedItems() {
        List<Item> items = getItems();
        List<Item> updatedItems = new ArrayList<Item>();
        for (int i = 0; i < items.size(); i++) {
            if (!items.get(i).description.equalsIgnoreCase(lastUpdatedItem.description))
                updatedItems.add(items.get(i));
            else
                break;
        }

        return updatedItems;
    }

    public void setLastUpdatedItem(Item item) {
        this.lastUpdatedItem = item;
    }

    public Item getLastUpdatedItem() {
        return this.lastUpdatedItem;
    }

    public void setTwitter(Twitter twitter) {
        this.twitter = twitter;
    }
}