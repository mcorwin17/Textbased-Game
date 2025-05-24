package PROJECT;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class NPC extends Entity {    // NPC class extending Entity
    private List<DialogueEntry> dialogues;
    private List<TradeEntry> trades;
    private List<String> defaultDialogues;

    public NPC(String name, String description) {
        super(name, description);
        dialogues = new ArrayList<>();
        trades = new ArrayList<>();
        defaultDialogues = new ArrayList<>();
    }

    public void addDialogue(String topic, String response) {
        dialogues.add(new DialogueEntry(topic, response));
    }

    public void addTradeItem(String key, Item item) {
        trades.add(new TradeEntry(key, item));
    }

    public void addDefaultDialogue(String text) {
        defaultDialogues.add(text);
    }

    public String defaultTalk() {    // returns a random default dialogue line
        if (defaultDialogues.isEmpty()) {
            return "They have nothing else to say.";
        }
        Random rand = new Random();
        return defaultDialogues.get(rand.nextInt(defaultDialogues.size()));
    }

    public String talk(String topic) {    // handles NPC conversation
        for (DialogueEntry d : dialogues) {
            if (d.topic.equalsIgnoreCase(topic)) {
                return d.response;
            }
        }
        return "I have nothing to say about that.";
    }

    public Item trade(String itemName) {    // handles NPC trading
        for (TradeEntry t : trades) {
            if (t.key.equalsIgnoreCase(itemName)) {
                return t.item;
            }
        }
        return null;
    }

    private static class DialogueEntry {
        String topic;
        String response;
        
        DialogueEntry(String topic, String response) {
            this.topic = topic;
            this.response = response;
        }
    }

    private static class TradeEntry {
        String key;
        Item item;
        
        TradeEntry(String key, Item item) {
            this.key = key;
            this.item = item;
        }
    }

    public List<String> getDialogueTopics() {
        List<String> topics = new ArrayList<>();
        for (DialogueEntry d : dialogues) { // lil for each loop to get topics
            topics.add(d.topic);
        }
        topics.add("default");
        return topics;
    }
}
