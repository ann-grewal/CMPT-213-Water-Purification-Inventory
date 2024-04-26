package model;

import java.time.*;
import java.util.*;

/* Unit is the class holds all information on a Unit including serial number, model, date shipped, and tests.
   Unit constructor may throw a Runtime Error if invalid serial number inputted.
   Creates String lists of data for report printing (except Tests). */

public class Unit {
    private final String serialNumber;
    private final String model;
    private final ArrayList<Test> tests = new ArrayList<>();
    private LocalDate dateShipped;

    public Unit(String serialNumber, String model) throws RuntimeException {
        if (!checkSerialNumber(serialNumber)) {
            throw new RuntimeException();
        }
        this.serialNumber = serialNumber;
        this.model = model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getModel() {
        return model;
    }

    public String getNumberOfTests() {
        return String.valueOf(tests.size());
    }

    public String getShipDate() {
        if (dateShipped != null) {
            return dateShipped.toString();
        }
        return "-";
    }

    public boolean noTests() {
        return tests.isEmpty();
    }

    public Test lastTest() {
        return tests.getLast();
    }

    public void newTest(boolean outcome, String comments) {
        tests.add(new Test(outcome, comments));
    }

    public void shipUnit() {
        dateShipped = LocalDate.now();
    }

    private boolean checkSerialNumber(String serialNumber) {
        if (serialNumber.length() < 3) {
            return false;
        }
        int sumInt = 0;
        for (int i = 0; i < serialNumber.length() - 2; i++) {
            String sumString = serialNumber.substring(i, i + 1);
            sumInt += Integer.parseInt(sumString);
        }
        String checkSumString = serialNumber.substring(serialNumber.length() - 2);
        int checkSumInt = Integer.parseInt(checkSumString);
        return (sumInt % 100) == checkSumInt;
    }

    public ArrayList<String> reportTest() {
        ArrayList<String> list = new ArrayList<>();
        for (Test current : tests) {
            list.addAll(current.testReport());
        }
        return list;
    }

    public ArrayList<String> reportAll() {
        ArrayList<String> list = new ArrayList<>(Arrays.asList(model, serialNumber, getNumberOfTests(), getShipDate()));
        list.add("\n");
        return list;
    }

    public ArrayList<String> reportDefective() {
        if (tests.isEmpty()) {
            return new ArrayList<>();
        }
        Test lastTest = tests.getLast();
        if (lastTest.getTestPassed()) {
            return new ArrayList<>();
        }
        String markedComment = "#" + lastTest().getComment();
        ArrayList<String> list = new ArrayList<>
                (Arrays.asList(model, serialNumber, getNumberOfTests(), lastTest.getStringDate(), markedComment));
        list.add("\n");
        return list;
    }

    public ArrayList<String> reportReadyToShip() {
        if (!getShipDate().equals("-")) {
            return new ArrayList<>();
        }
        if (tests.isEmpty()) {
            return new ArrayList<>();
        }
        Test lastTest = tests.getLast();
        if (!lastTest.getTestPassed()) {
            return new ArrayList<>();
        }
        ArrayList<String> list = new ArrayList<>(Arrays.asList(model, serialNumber, lastTest.getStringDate()));
        list.add("\n");
        return list;
    }
}
