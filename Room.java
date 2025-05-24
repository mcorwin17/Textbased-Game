package PROJECT;
import java.util.List;
import java.util.ArrayList;


public class Room { // room class for game locations with exits, items, and npcs
    private String name; // room name
    private String description; // room description
    private List<Exit> exits; // list of exits
    private List<Item> items; // list of items in room
    private List<NPC> npcs; // list of npcs in room

    public Room(String name, String description) { // constructor for new room
        this.name = name;
        this.description = description;
        exits = new ArrayList<>();
        items = new ArrayList<>();
        npcs = new ArrayList<>();
    }

    public void setExit(String direction, Room neighbor) { // adds exit to room
        exits.add(new Exit(direction, neighbor));
    }

    public Room getExit(String direction) { // gets room in given direction
        for (Exit e : exits) {
            if (e.direction.equals(direction)) {
                return e.room;
            }
        }
        return null;
    }

    public void addItem(Item item) { // adds item to room
        items.add(item);
    }
    
    public void removeItem(Item item) { // removes item from room
        items.remove(item);
    }
    
    public void addNPC(NPC npc) { // adds npc to room
        npcs.add(npc);
    }
    
    public Item getItem(String itemName) { // gets item by name using selection sort + binary search
        // make a copy of the list to sort
        List<Item> sorted = new ArrayList<>(items);
        int n = sorted.size();
        // selection sort by name
        for (int i = 0; i < n - 1; i++) {
            int min = i;
            for (int j = i + 1; j < n; j++) {
                if (sorted.get(j).getName().compareToIgnoreCase(sorted.get(min).getName()) < 0) {
                    min = j;
                }
            }
            Item tmp = sorted.get(min);
            sorted.set(min, sorted.get(i));
            sorted.set(i, tmp);
        }
        // binary search for the itemName
        int left = 0, right = n - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            int cmp = sorted.get(mid).getName().compareToIgnoreCase(itemName);
            if (cmp == 0) {
                return sorted.get(mid);
            } else if (cmp < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return null;
    }
    
    public NPC getNPC(String npcName) { // gets npc by name using selection sort + binary search
        List<NPC> sorted = new ArrayList<>(npcs);
        int n = sorted.size();
        // selection sort by name -- easily could've used a comparator but I did this just because it was required I think idk
        for (int i = 0; i < n - 1; i++) {
            int min = i;
            for (int j = i + 1; j < n; j++) {
                if (sorted.get(j).getName().compareToIgnoreCase(sorted.get(min).getName()) < 0) {
                    min = j;
                }
            }
            NPC tmp = sorted.get(min);
            sorted.set(min, sorted.get(i));
            sorted.set(i, tmp);
        }
        // binary search for the npcName
        int left = 0, right = n - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            int cmp = sorted.get(mid).getName().compareToIgnoreCase(npcName);
            if (cmp == 0) {
                return sorted.get(mid);
            } else if (cmp < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return null;
    }
    
    public String getLongDescription() { // gets full room description
        List<String> dirs = new ArrayList<>();
        for (Exit e : exits) {
            dirs.add(e.direction);
        }
        String exitsList = String.join(", ", dirs);

        String desc = description + "\nExits: [" + exitsList + "]";

        if (!items.isEmpty()) {
            desc += "\nItems: " + items;
        }

        if (!npcs.isEmpty()) {
            String peopleList = "";
            for (int i = 0; i < npcs.size(); i++) {
                NPC npc = npcs.get(i);
                peopleList += npc.getName() + " (";
                List<String> topics = npc.getDialogueTopics();
                for (int j = 0; j < topics.size(); j++) {
                    peopleList += topics.get(j);
                    if (j < topics.size() - 1) {
                        peopleList += ", ";
                    }
                }
                peopleList += ")";
                if (i < npcs.size() - 1) {
                    peopleList += ", ";
                }
            }
            desc += "\nPeople: " + peopleList;
        }

        return desc;
    }

    public String getName() { // gets room name
        return name;
    }

    private static class Exit { // helper class for exits
        String direction;
        Room room;
        Exit(String direction, Room room) {
            this.direction = direction;
            this.room = room;
        }
    }
}
