package view;

import model.*;

import java.io.FileNotFoundException;
import java.util.*;

/* UserInterface is the class in charge of all interactions with the user through console.
   Repeatedly captures information from user via Menus and calls the corresponding Inventory functions.
   Kill UserInterface and entire program by exiting from Main Menu.
   Catches all errors both potential Runtime and File Not Found Error. */

public class UserInterface {
    private final Inventory waterInventory;
    private final Scanner userScanner = new Scanner(System.in);

    public UserInterface(Inventory waterInventory) {
        this.waterInventory = waterInventory;
    }

    public void runMainMenu() {
        Menu.MenuEntry[] menuEntries = new Menu.MenuEntry[]{
                new Menu.MenuEntry("Read JSON input file.", this::readJSONFile),
                new Menu.MenuEntry("Display info on a unit.", this::displayUnit),
                new Menu.MenuEntry("Create new unit.", this::newUnit),
                new Menu.MenuEntry("Test a unit", this::newTest),
                new Menu.MenuEntry("Ship a unit.", this::shipUnit),
                new Menu.MenuEntry("Print report.", this::printReport),
                new Menu.MenuEntry("Set report sort order.", this::sortSelect),
                new Menu.MenuEntry("Exit", null)};
        Menu mainMenu = new Menu(menuEntries, "Main Menu");
        mainMenu.runMenu(false);
    }

    // Inspired from Dr. Brian Fraser's videos on GSON use.
    private void readJSONFile() {
        while (true) {
            System.out.println("Please note that data from JSON file will overwrite current data!");
            System.out.print("Enter a valid JSON file or blank to cancel: ");
            String fileName = userScanner.nextLine();
            if (fileName.equals("\n") || fileName.isEmpty()) {
                return;
            }
            try {
                waterInventory.readJSONFile(fileName);
            } catch (FileNotFoundException error) {
                System.out.println("Error could not access file, please try again.");
                continue;
            }
            System.out.println("Successfully got water unit information from JSON file.");
            break;
        }
    }

    private Unit unitInput() {
        String userInput;
        if (waterInventory.empty()) {
            System.out.println("No units defined, please try again after defining units.");
            return null;
        }
        while (true) {
            System.out.print("Please enter 0 for all items or a specific serial number (-1 to cancel): ");
            userInput = userScanner.nextLine();
            String CANCEL = "-1";
            if (userInput.compareTo(CANCEL) == 0) {
                return null;
            }
            String PRINT_ALL = "0";
            if (userInput.compareTo(PRINT_ALL) == 0) {
                waterInventory.runSerialSort();
                reportAll();
            }
            Unit selected = waterInventory.findBySerialNumber(userInput);
            if (selected != null) {
                return selected;
            }
        }
    }

    private void displayUnit() {
        Unit selected = unitInput();
        if (selected == null) {
            return;
        }
        System.out.println("Unit.java Details");
        System.out.println("\tSerial Number: " + selected.getSerialNumber());
        System.out.println("\tModel: " + selected.getModel());
        System.out.println("\tShip Date: " + selected.getShipDate());
        displayTestTable(selected);
    }

    private void displayTestTable(Unit selected) {
        ArrayList<String> testList = selected.reportTest();
        if (testList.isEmpty()) {
            System.out.println("\tNo tests to display!");
        } else {
            ArrayList<String> columns = new ArrayList<>(List.of("Date", "Passed?", "Test Comments"));
            Table testTable = new Table(testList, columns, "Tests");
            testTable.display();
        }
    }

    private void newUnit() {
        System.out.println("Please enter product info, blank line to quit");
        System.out.print("Model: ");
        String model = userScanner.nextLine();
        if (model.equals("\n") || model.isEmpty()) {
            return;
        }
        while (true) {
            System.out.print("Serial Number: ");
            String serialNumber = userScanner.nextLine();
            if (serialNumber.equals("\n") || serialNumber.isEmpty()) {
                return;
            }
            try {
                waterInventory.addUnit(serialNumber, model);
            } catch (RuntimeException error) {
                System.out.println("Error in serial number, please try again!");
                continue;
            }
            System.out.println("Successfully made a product!");
            break;
        }
    }

    private void newTest() {
        Unit selected = unitInput();
        if (selected == null) {
            return;
        }
        boolean outcome = true;
        while (true) {
            System.out.print("Was the test a success (y/n or blank for success): ");
            String outcomeInput = userScanner.nextLine();
            boolean implicitYes = outcomeInput.equals("\n") || outcomeInput.isEmpty();
            boolean explicitYes = outcomeInput.equals("Y") || outcomeInput.contains("y");
            boolean explicitNo = outcomeInput.equals("N") || outcomeInput.equals("n");
            if (implicitYes || explicitYes) {
                break;
            }
            if (explicitNo) {
                outcome = false;
                break;
            }
        }
        System.out.print("Comments about the test: ");
        String comments = userScanner.nextLine();
        selected.newTest(outcome, comments);
        System.out.println("Test Logged.");
    }

    private void shipUnit() {
        Unit selected = unitInput();
        if (selected == null) {
            return;
        }
        selected.shipUnit();
        System.out.println("Shipped Unit Today.");
    }

    private void sortSelect() {
        Menu.MenuEntry[] menuEntries = new Menu.MenuEntry[]{
                new Menu.MenuEntry("Sort by Serial Number", this::sortBySerialNumber),
                new Menu.MenuEntry("Sort by Model", this::sortByModel),
                new Menu.MenuEntry("Sort by Last Test Date", this::sortByTest),
                new Menu.MenuEntry("Return to Main Menu", null)};
        Menu sortMenu = new Menu(menuEntries, "Report Sort Order");
        sortMenu.runMenu(true);
    }

    private void sortBySerialNumber() {
        waterInventory.changeSort(ReportSort.BY_SERIAL_NUMBER);
    }

    private void sortByModel() {
        waterInventory.changeSort(ReportSort.BY_MODEL);
    }

    private void sortByTest() {
        waterInventory.changeSort(ReportSort.BY_LAST_TEST);
    }

    private void printReport() {
        waterInventory.runSort();
        Menu.MenuEntry[] menuEntries = new Menu.MenuEntry[]{
                new Menu.MenuEntry("Report All", this::reportAll),
                new Menu.MenuEntry("Report Defective", this::reportDefective),
                new Menu.MenuEntry("Report Ready To Ship", this::reportReadyToShip),
                new Menu.MenuEntry("Return to Main Menu", null)};
        Menu reportMenu = new Menu(menuEntries, "Report Sort Order");
        reportMenu.runMenu(true);
    }

    private void reportAll() {
        String title = "List of All Water Purification Units";
        ArrayList<String> columns = new ArrayList<>(
                Arrays.asList("Model", "Serial Number", "# of Tests", "Ship Date"));
        ArrayList<String> dataList = waterInventory.collectReport(ReportType.ALL);
        Table report = new Table(dataList, columns, title);
        report.display();
    }

    private void reportDefective() {
        String title = "List of Defective Water Purification Units";
        ArrayList<String> columns = new ArrayList<>(
                Arrays.asList("Model", "Serial Number", "# of Tests", "Test Date", "Test Comments"));
        ArrayList<String> dataList = waterInventory.collectReport(ReportType.DEFECTIVE);
        Table report = new Table(dataList, columns, title);
        report.display();
    }

    private void reportReadyToShip() {
        String title = "List of Ready To Ship Water Purification Units";
        ArrayList<String> columns = new ArrayList<>(
                Arrays.asList("Model", "Serial Number", "Test Date"));
        ArrayList<String> dataList = waterInventory.collectReport(ReportType.READY_TO_SHIP);
        Table report = new Table(dataList, columns, title);
        report.display();
    }
}
