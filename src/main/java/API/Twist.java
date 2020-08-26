package API;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Twist {

    private static Twist instance;

    private Item lastUpdatedItem;
    private InputStreamReader reader;

    public static Twist getInstance() {
        if (instance == null)
            instance = new Twist();

        return instance;
    }

    public boolean isSiteWorking() {
        try {
            URL base = new URL("https://twist.moe/feed/episodes?format=json");
            reader = new InputStreamReader(base.openStream());
            return true;
        } catch (Exception e) {
            System.out.println("Site not working:");
            e.printStackTrace();
            return false;
        }
    }

    public List<Item> getItems() {
        Gson gson = new Gson();
        Page page = gson.fromJson(reader, Page.class);
        return page.items;
    }

    public boolean hasBeenUpdated() {
        Item headerItem = getItems().get(0);
        if (lastUpdatedItem == null)
            lastUpdatedItem = headerItem;

        if (lastUpdatedItem.description.equalsIgnoreCase(headerItem.description))
            return false;
        else
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
    public Item getLastUpdatedItem(){
        return this.lastUpdatedItem;
    }
}