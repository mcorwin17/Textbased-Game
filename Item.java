package PROJECT;

// class for storing items in game like swords and food etc
public class Item {
    // name of the item
    private String name;
    // description of the item
    private String description;

    // constructor to create new item with name and description
    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // getter method for name
    public String getName() { return name; }
    // getter method for description
    public String getDescription() { return description; }

    // override toString to return item name
    @Override
    public String toString() { return name; }
}