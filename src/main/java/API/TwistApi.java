package API;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TwistApi {

    private static TwistApi instance;

    private Item lastUpdatedItem;

    public static TwistApi getInstance() {
        if(instance == null)
            instance = new TwistApi();

        return instance;
    }

    public List<Item> getItems() {
        try {
            URL base = new URL("https://twist.moe/feed/episodes?format=json");
            InputStreamReader reader = new InputStreamReader(base.openStream());
            Gson gson = new Gson();
            Page page = gson.fromJson(reader, Page.class);
            return page.items;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    public List<Item> getUpdatedItems(){
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

    public void setLastUpdatedItem(Item item){
        this.lastUpdatedItem = item;
    }
}
