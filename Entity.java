package PROJECT;

// base class for all game entities. made it abstract for ease of use. allows polymorphism for ALL my game objects which is real nice
public abstract class Entity {

    // made protected so subclasses can access it
    // name of the entity
    protected String name;
    // description of the entity
    protected String description;

    // constructor to create a new entity
    public Entity(String name, String description) {
        this.name = name;
        this.description = description;
    }
    // returns the entity's name
    public String getName() { return name; }
    // returns the entity's description
    public String getDescription() { return description; }
}
