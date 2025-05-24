package PROJECT;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;

public class AdventureGame {
    private Player player;
    private List<Room> rooms;
    private boolean isRunning;
    private String code;
    private int codeAttempts = 0;

    public AdventureGame() {
        rooms = new ArrayList<>();
        createRooms();
        player = new Player("Hero", getRoomByName("Apartment"));
        isRunning = true;
        Random rand = new Random();
        code = String.format("%04d", rand.nextInt(10000));
    }

    // find room one by one will use cool search later
    private Room getRoomByName(String name) {
        for (Room room : rooms) {
            if (room.getName().equals(name)) {
                return room;
            }
        }
        return null;
    }

    private void createRooms() {
        // create rooms
        Room apartment = new Room("Apartment", "Your small apartment. It's eerily quiet.");
        Room street = new Room("Street", "The street is littered with debris and flickering streetlights.");
        Room gasStation = new Room("Gas Station", "An old gas station, pumps are dead, but the door is locked.");
        Room supermarket = new Room("Supermarket", "Shelves are toppled, but something glints in the dark.");
        Room tunnel = new Room("Underground Tunnel", "A dark tunnel leading to the shelter.");
        // hidden room
        Room cellar = new Room("Cellar", "A hidden cellar beneath the supermarket.");

        // initialize exits
        apartment.setExit("south", street);
        street.setExit("north", apartment);
        street.setExit("east", gasStation);
        gasStation.setExit("west", street);
        street.setExit("south", supermarket);
        supermarket.setExit("north", street);
        supermarket.setExit("down", cellar);
        cellar.setExit("up", supermarket);
        cellar.setExit("south", tunnel);
        tunnel.setExit("north", cellar);

        // store rooms
        rooms.add(apartment);
        rooms.add(street);
        rooms.add(gasStation);
        rooms.add(supermarket);
        rooms.add(cellar);
        rooms.add(tunnel);

        // Hospital area
        Random randRoom = new Random();
        Room hospital = new Room("Hospital", "A sprawling hospital with flickering lights and deserted corridors.");
        Room hNorth = new Room("North Wing", "The quiet north wing of the hospital with empty beds.");
        Room hSouth = new Room("South Wing", "The busy south wing, echoes of distant alarms.");
        Room hWest = new Room("Pharmacy", "A small pharmacy stocked with dusty medical supplies.");

        // initialize hospital exits
        street.setExit("west", hospital);
        hospital.setExit("east", street);
        hospital.setExit("north", hNorth);
        hospital.setExit("south", hSouth);
        hospital.setExit("west", hWest);
        hNorth.setExit("south", hospital);
        hSouth.setExit("north", hospital);
        hWest.setExit("east", hospital);

        // add hospital rooms to list
        rooms.add(hospital);
        rooms.add(hNorth);
        rooms.add(hSouth);
        rooms.add(hWest);

        // add random items in hospital rooms
        String[] hospitalItems = {"bandage", "mask", "syringe", "medkit", "painkillers"};
        for (Room r : Arrays.asList(hospital, hNorth, hSouth, hWest)) {
            String itemName = hospitalItems[randRoom.nextInt(hospitalItems.length)];
            r.addItem(new Item(itemName, "A " + itemName + " left behind."));
        }

        // mask is available in the North Wing for SECRET QUEST!!!
        Item maskItem = new Item("mask", "A protective face mask.");
        hNorth.addItem(maskItem);

        // add NPCs to hospital rooms
        NPC doctor = new NPC("Doctor", "A weary doctor checking the area.");
        doctor.addDefaultDialogue("Cough Cough.. I wish I had a mask.");
        hospital.addNPC(doctor);

        NPC nurse = new NPC("Nurse", "A busy nurse with stained scrubs.");
        nurse.addDefaultDialogue("I haven't had a break in hours.");
        nurse.addDefaultDialogue("This ward is understaffed.");
        nurse.addDefaultDialogue("The patients need attention.");
        hNorth.addNPC(nurse);

        NPC patient = new NPC("Patient", "A frightened patient clutching their chest.");
        patient.addDefaultDialogue("I feel so weak...");
        patient.addDefaultDialogue("Will someone help me?");
        patient.addDefaultDialogue("I just want to go home.");
        hSouth.addNPC(patient);

        NPC pharmacist = new NPC("Pharmacist", "A pharmacist restocking shelves.");
        pharmacist.addDefaultDialogue("We're running low on medications.");
        pharmacist.addDefaultDialogue("I can't find the painkillers.");
        pharmacist.addDefaultDialogue("Keep your distance, please.");
        hWest.addNPC(pharmacist);

        // Quest items
        Item tire = new Item("tire", "A spare tire.");
        getRoomByName("Street").addItem(tire);
        Item cannedBeans = new Item("canned beans", "A can of beans.");

        // NPCs
        NPC roommate = new NPC("Roommate", "Your cautious roommate.");
        roommate.addDialogue("hello", "I can't believe this happened... Stay sharp.");
        roommate.addDialogue("help", "Find me some canned food and I'll join you.");
        roommate.addDefaultDialogue("I really miss my parents...");
        roommate.addDefaultDialogue("I hope they're safe out there.");
        roommate.addDefaultDialogue("It's been too long since I heard from them.");
        getRoomByName("Apartment").addNPC(roommate);

        NPC stranger = new NPC("Stranger", "A frightened stranger clutching a backpack.");
        stranger.addDialogue("hello", "Please keep your distance!");
        stranger.addDefaultDialogue("Do you know if it's safe outside?");
        stranger.addDefaultDialogue("I haven't eaten in days...");
        stranger.addDefaultDialogue("Why is this happening?");
        getRoomByName("Street").addNPC(stranger);

        NPC attendant = new NPC("Attendant", "A tired gas station attendant.");
        attendant.addDialogue("radio", "I lost the radio frequency, sorry.");
        attendant.addDefaultDialogue("We haven't had fuel for weeks.");
        attendant.addDefaultDialogue("If you find a working radio, let me know.");
        attendant.addDefaultDialogue("Stay inside after dark.");
        getRoomByName("Gas Station").addNPC(attendant);

        NPC scavenger = new NPC("Scavenger", "A resourceful scavenger eyeing your supplies.");
        scavenger.addDialogue("trade", "I'll give you a canned beans for a tire.");
        scavenger.addTradeItem("tire", cannedBeans);
        scavenger.addDefaultDialogue("You got anything to trade?");
        scavenger.addDefaultDialogue("Supplies are running out everywhere.");
        scavenger.addDefaultDialogue("Watch out for the guards in the tunnel.");
        getRoomByName("Supermarket").addNPC(scavenger);

        NPC guard = new NPC("Guard", "A stern guard protecting the tunnel exit.");
        guard.addDialogue("code", "What's the code to get through?");
        guard.addDefaultDialogue("No one goes through without the code.");
        guard.addDefaultDialogue("Orders are orders.");
        guard.addDefaultDialogue("Stay back unless you know the code.");
        getRoomByName("Underground Tunnel").addNPC(guard);
    }

    public void play() {
        printWelcome();
        Scanner in = new Scanner(System.in);
        while (isRunning) {
            System.out.print("> ");
            String input = in.nextLine();
            processCommand(input);
        }
        in.close();
    }

    private void printWelcome() {
        System.out.println("Welcome to Apocalypse Survival! Try to figure out how to get to the safe room and survive! Good luck!!");
        System.out.println(player.getCurrentRoom().getLongDescription());
        System.out.println("Type 'help' to see available commands.");
    }

    private void processCommand(String input) {
        String[] words = input.trim().split("\\s+", 2);
        String command = words[0].toLowerCase();
        String argument = words.length > 1 ? words[1] : "";

        switch(command) {
            case "trade":
                handleTrade(argument);
                break;
            case "give":
                handleGive(argument);
                break;
            case "go":
                if (argument.equalsIgnoreCase("lab")) {
                    player.goRoom("lab");
                    System.out.println("Secret Ending: The doctor cures the disease. You saved humanity!");
                    isRunning = false;
                    break;
                }
                // Safe room exit
                if (argument.equalsIgnoreCase("safe")) {
                    player.goRoom(argument);
                    System.out.println("You survived the apocalypse by making it to the safe room. Congrats!");
                    isRunning = false;
                } else {
                    player.goRoom(argument);
                }
                break;
            case "look":
                player.look(argument);
                break;
            case "take":
                player.take(argument);
                break;
            case "drop":
                player.drop(argument);
                break;
            case "use":
                handleUse(argument);
                break;
            case "talk":
                handleTalk(argument);
                break;
            case "inventory":
                player.showInventory();
                break;
            case "help":
                printHelp();
                break;
            case "quit":
                isRunning = false;
                System.out.println("Thanks for playing!");
                break;
            default:
                System.out.println("I don't understand that command.");
        }
        // Display the current room and exits after each action, if still running
        if (isRunning) {
            System.out.println(player.getCurrentRoom().getLongDescription());
        }
    }

    private void handleTrade(String argument) {
        String[] parts = argument.trim().split("\\s+", 2);
        if (parts.length < 2) {
            System.out.println("Trade with whom and for what?");
            return;
        }
        String npcName = parts[0];
        String itemName = parts[1];
        NPC npc = player.getCurrentRoom().getNPC(npcName);
        if (npc == null) {
            System.out.println("There's no one here by that name.");
            return;
        }
        Item toGive = player.removeFromInventory(itemName);
        if (toGive == null) {
            System.out.println("You don't have " + itemName + ".");
            return;
        }
        Item received = npc.trade(itemName);
        if (received != null) {
            player.addToInventory(received);
            System.out.println("You traded " + toGive.getName() + " for " + received.getName() + ".");
        } else {
            System.out.println(npc.getName() + " doesn't want that.");
            player.addToInventory(toGive);
        }
    }

    private void handleGive(String argument) {
        String[] parts = argument.trim().split("\\s+", 2);
        if (parts.length < 2) {
            System.out.println("Give to whom and what?");
            return;
        }
        String npcName = parts[0];
        String itemName = parts[1];
        NPC npc = player.getCurrentRoom().getNPC(npcName);
        if (npc == null) {
            System.out.println("There's no one here by that name.");
            return;
        }
        Item toGive = player.removeFromInventory(itemName);
        if (toGive == null) {
            System.out.println("You don't have " + itemName + ".");
            return;
        }
        if (npc.getName().equalsIgnoreCase("Roommate") && toGive.getName().equalsIgnoreCase("canned beans")) {
            System.out.println("Thanks for the beans... I worked at the gas station before the apocalypse. Take this key to unlock the gas station.");
            Item key = new Item("key", "A small brass key.");
            player.addToInventory(key);
            System.out.println("You received a key.");
        } 
        else if (npc.getName().equalsIgnoreCase("Doctor") && toGive.getName().equalsIgnoreCase("mask")) {
            System.out.println("Thank you.. Now quickly, we need to get to the lab!");
            Room lab = new Room("Lab", "A secret laboratory where the disease cure is underway.");
            rooms.add(lab);
            Room hospitalMain = getRoomByName("Hospital");
            hospitalMain.setExit("lab", lab);
            lab.setExit("back", hospitalMain);
            return;
        }else {
            System.out.println(npc.getName() + " has no use for that.");
            player.addToInventory(toGive);
        }
    }

    private void handleTalk(String argument) {
        String[] parts = argument.trim().split("\\s+", 2);
        String npcName = parts[0];
        NPC npc = player.getCurrentRoom().getNPC(npcName);
        if (npc == null) {
            System.out.println("There's no one here by that name.");
            return;
        }
        if (npc.getName().equalsIgnoreCase("Guard")) {
            System.out.println("Guard: 'You need to enter the code to proceed.'");
            System.out.print("Enter the 4-digit code: ");
            Scanner inputScanner = new Scanner(System.in);
            String entry = inputScanner.nextLine().trim();
            if (entry.equals(code)) {
                System.out.println("Correct! The guard steps aside to reveal a safe room.");
                Room guardRoom = player.getCurrentRoom();
                Room safeRoom = new Room("Safe Room", "A secure room filled with supplies.");
                rooms.add(safeRoom);
                guardRoom.setExit("safe", safeRoom);
                safeRoom.setExit("back", guardRoom);
            } else {
                codeAttempts++;
                if (codeAttempts >= 3) {
                    System.out.println("The guard thinks you're messing with him and kills you. Game Over.");
                    isRunning = false;
                } else {
                    System.out.println("Incorrect code.");
                }
                return;
            }
        } else {
            // default dialogue
            String topic = parts.length > 1 ? parts[1] : "";
            if (topic.equalsIgnoreCase("default")) {
                System.out.println(npc.getName() + " says: '" + npc.defaultTalk() + "'");
            } else if (topic.isEmpty()) {
                System.out.println(npc.getName() + " says: '" + npc.getDescription() + "'");
            } else {
                System.out.println(npc.getName() + " says: '" + npc.talk(topic) + "'");
            }
        }
    }

    // for item use
    private void handleUse(String itemName) {
        // Using key at the gas station unlocks the interior and secret room
        if (itemName.equalsIgnoreCase("key") &&
            player.getCurrentRoom().getName().equals("Gas Station")) {
            // Must have the key in inventory
            Item key = player.removeFromInventory("key");
            if (key == null) {
                System.out.println("You don't have the key.");
                return;
            }
            System.out.println("You unlock the gas station door with the key and go inside.");
            // Create inside room
            Room inside = new Room("Inside Gas Station", "The interior of the gas station. Dusty shelves line the walls.");
            rooms.add(inside);
            // Connect outside to inside
            Room gas = player.getCurrentRoom();
            gas.setExit("in", inside);
            inside.setExit("out", gas);
            // Create secret area inside
            Room secret = new Room("Secret Area", "A hidden alcove behind the counter.");
            rooms.add(secret);
            inside.setExit("secret", secret);
            secret.setExit("back", inside);
            // Place the code item in the secret area
            Item codeItem = new Item("Code", "Code: " + code);
            secret.addItem(codeItem);
            // Move player inside
            player.setCurrentRoom(inside);
            // Removed duplicate description print; will be shown by processCommand.
        } else {
            System.out.println("You can't use " + itemName + " right now.");
        }
    }

    // prints available commands and makes it INSANELY obvious on how to use them
    private void printHelp() {
        System.out.println("Available commands:");
        System.out.println("  GO [direction]          - Move to a different room (e.g. GO north)");
        System.out.println("  LOOK [object]           - Examine the room or an object (e.g. LOOK key)");
        System.out.println("  TAKE [item]             - Pick up an item (e.g. TAKE flashlight)");
        System.out.println("  DROP [item]             - Drop an item (e.g. DROP key)");
        System.out.println("  USE [item]              - Use an item in your inventory (e.g. USE key)");
        System.out.println("  TALK [npc] [topic]      - Talk to a character (e.g. TALK Scavenger trade)");
        System.out.println("  TRADE [npc] [item]      - Trade an item with an NPC (e.g. TRADE Scavenger tire)");
        System.out.println("  GIVE [npc] [item]       - Give an item to an NPC (e.g. GIVE Roommate canned beans)");
        System.out.println("  INVENTORY               - Show your current inventory");
        System.out.println("  HELP                    - Show this help message");
        System.out.println("  QUIT                    - Exit the game");
    }

    public static void main(String[] args) {
        AdventureGame game = new AdventureGame();
        game.play();
    }
}