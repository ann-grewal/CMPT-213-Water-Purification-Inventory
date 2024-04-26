import model.*;
import view.*;

/* Main is a trivial Class that runs User Interface and Inventory. */
public class Main {
    public static void main(String[] args) {
        System.out.println("""
                *************************************************
                Anureet's Water Purification Inventory Management
                *************************************************""");
        Inventory inventory = new Inventory();
        UserInterface userInterface = new UserInterface(inventory);
        userInterface.runMainMenu();
    }
}