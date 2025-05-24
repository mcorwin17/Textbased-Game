package PROJECT;
import java.util.List;
import java.util.ArrayList;

public class Player extends Entity {
    // player's current location in the game
    private Room currentRoom;
    // list to store player's collected items
    private List<Item> inventory;

    public Player(String name, Room startRoom) {
        super(name, "The hero of this story.");
        currentRoom = startRoom;
        inventory = new ArrayList<>();
    }

    // returns the room where player is currently located
    public Room getCurrentRoom() { return currentRoom; }

    // handles player movement between rooms
    public void goRoom(String direction) {
        Room next = currentRoom.getExit(direction);
        if (next != null) {
            currentRoom = next;
        } else {
            System.out.println("You can't go that way.");
        }
    }

    // examines items in inventory or room
    public void look(String target) {
        if (target.isEmpty()) {
            System.out.println(currentRoom.getLongDescription());
        } else {
            // check if the item is in player's inventory
            for (Item item : inventory) {
                if (item.getName().equalsIgnoreCase(target)) {
                    System.out.println(item.getDescription());
                    return;
                }
            }
            // check if the item is in the current room
            Item roomItem = currentRoom.getItem(target);
            if (roomItem != null) {
                System.out.println(roomItem.getDescription());
                return;
            }
            System.out.println("There is nothing special about " + target + ".");
        }
    }

    // placeholder for item usage functionality
    public void use(String itemName) {
        System.out.println("You can't use " + itemName + " right now.");
    }

    // picks up an item from the current room
    public void take(String itemName) {
        Item item = currentRoom.getItem(itemName);
        if (item != null) {
            inventory.add(item);
            currentRoom.removeItem(item);
            System.out.println("Taken: " + itemName);
        } else {
            System.out.println("There is no " + itemName + " here.");
        }
    }
    
    // drops an item from inventory to current room
    public void drop(String itemName) {
        Item itemToDrop = inventory.stream()
            .filter(item -> item.getName().equalsIgnoreCase(itemName))
            .findFirst()
            .orElse(null);
        
        if (itemToDrop != null) {
            inventory.remove(itemToDrop);
            currentRoom.addItem(itemToDrop);
            System.out.println("Dropped: " + itemName);
        } else {
            System.out.println("You don't have " + itemName + ".");
        }
    }
    
    // handles conversation with npcs
    public void talk(String argument) {
        String[] parts = argument.trim().split("\\s+", 2);
        String npcName = parts[0];
        String topic = parts.length > 1 ? parts[1] : "";
        NPC npc = currentRoom.getNPC(npcName);
        if (npc != null) {
            if (topic.isEmpty()) {
                System.out.println(npc.getName() + " says: '" + npc.getDescription() + "'");
            } else {
                System.out.println(npc.getName() + " says: '" + npc.talk(topic) + "'");
            }
        } else {
            System.out.println("There's no one here by that name.");
        }
    }
    
    // displays contents of player's inventory
    public void showInventory() {
        if (inventory.isEmpty()) {
            System.out.println("Your inventory is empty.");
        } else {
            System.out.println("You are carrying: " + inventory);
        }
    }

    // removes and returns an item from inventory by name
    public Item removeFromInventory(String itemName) {
        for (Item i : inventory) {
            if (i.getName().equalsIgnoreCase(itemName)) {
                inventory.remove(i);
                return i;
            }
        }
        return null;
    }

    // adds an item to player's inventory
    public void addToInventory(Item item) {
        inventory.add(item);
    }

    // sets player's current location
    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }
}