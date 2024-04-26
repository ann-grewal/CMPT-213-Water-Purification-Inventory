package view;

import java.util.Scanner;

/* Creates a Menu consisting of a title and options paired with a Runnable Object
   Inspired from Dr. Brian Fraser's menu solution to Assignment 1. */

public class Menu {
    // Inner class creates menu options.
    public static class MenuEntry {
        private final String text;
        private final Runnable runner;

        public MenuEntry(String text, Runnable runner) {
            this.text = text;
            this.runner = runner;
        }
    }

    private final MenuEntry[] entries;
    private final String title;

    public Menu(MenuEntry[] entries, String title) {
        this.entries = entries;
        this.title = title;
    }

    public void runMenu(boolean runOnce) {
        do {
            displayMenu();
            int entry = getValidEntry();
            Runnable runner = entries[entry].runner;
            if (runner == null) {
                return;
            } else {
                runner.run();
            }
        } while (!runOnce);
    }

    private void displayMenu() {
        System.out.println();
        String star = "";
        for (int i = 0; i < title.length() + 2; i++) {
            star = star + "*";
        }
        System.out.println(star);
        System.out.println("*" + title + "*");
        System.out.println(star);
        for (int i = 0; i < entries.length; i++) {
            System.out.println((i + 1) + ". " + entries[i].text);
        }
    }

    private int getValidEntry() {
        Scanner myScanner = new Scanner(System.in);
        int userSelection = 0;
        while (userSelection < 1 || userSelection > entries.length) {
            System.out.print("Please enter a valid menu selection: ");
            userSelection = myScanner.nextInt();
        }
        return userSelection - 1;
    }
}