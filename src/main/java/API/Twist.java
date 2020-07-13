package API;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Twist {

    private static Twist instance;

    private Item lastUpdatedItem;

    public static Twist getInstance() {
        if (instance == null)
            instance = new Twist();

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

    //Index 0 = new episodes
    //Index 1 = new anime
    public List<ArrayList> getUpdatedItems() {
        List<Item> items = getItems();
        ArrayList<Item> updatedEpisodes = getUpdatedEpisodes(items);
        ArrayList<Item> updatedAnime = getUpdatedAnime(updatedEpisodes);

        for (Item item : updatedAnime) {
            for (Item episodeItem : updatedEpisodes) {
                if (episodeItem.id == item.id)
                    updatedEpisodes.remove(episodeItem);
            }
        }
        List<ArrayList> res = new ArrayList<ArrayList>();
        res.add(updatedEpisodes);
        res.add(updatedAnime);
        return res;
    }

    private ArrayList<Item> getUpdatedAnime(List<Item> updatedEpisodes) {
        ArrayList<Item> updatedAnime = new ArrayList<Item>();
        int previousId = 0;
        int counter = 0;
        for (Item item : updatedEpisodes) {
            if (item.id == previousId && !containsAnime(item.id, updatedAnime)) {
                counter++;
                if (counter >= 5) {
                    item.link = item.link.substring(0, item.link.lastIndexOf('/'));
                    updatedAnime.add(item);
                    counter = 0;
                    previousId = 0;
                    continue;
                }
            }
            previousId = item.id;
        }

        return updatedAnime;
    }

    private ArrayList<Item> getUpdatedEpisodes(List<Item> items) {
        ArrayList<Item> updatedEpisodes = new ArrayList<Item>();
        for (int i = 0; i < items.size(); i++) {
            if (!items.get(i).description.equalsIgnoreCase(lastUpdatedItem.description))
                updatedEpisodes.add(items.get(i));
            else
                break;
        }
        return updatedEpisodes;
    }

    private boolean containsAnime(int animeId, ArrayList<Item> list) {
        for (Item item : list) {
            if (item.id == animeId)
                return true;
        }
        return false;
    }

    public void setLastUpdatedItem(Item item) {
        this.lastUpdatedItem = item;
    }
}
