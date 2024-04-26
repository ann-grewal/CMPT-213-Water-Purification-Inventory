package model;

import com.google.gson.*;
import com.google.gson.reflect.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

/* Inventory is the class that stores all units and manipulates them as a class.
   Tasks include reading from JSON file, finding Units, and sorting for Reports.
   JSON file may throw a File Not Found Exception.
   Collects String lists of data from Units for report printing. */

public class Inventory {
    private ArrayList<Unit> Units = new ArrayList<>();
    private ReportSort sortOrder = ReportSort.BY_SERIAL_NUMBER;

    public boolean empty() {
        return Units.isEmpty();
    }

    public Unit findBySerialNumber(String toFind) {
        for (Unit data : Units) {
            int comparison = toFind.compareTo(data.getSerialNumber());
            if (comparison == 0) {
                return data;
            }
        }
        return null;
    }

    public void checkUniqueSerialNumber(String toFind) throws RuntimeException {
        Unit selected = findBySerialNumber(toFind);
        if (selected != null) {
            throw new RuntimeException();
        }
    }

    public void addUnit(String serialNumberInput, String modelInput) {
        checkUniqueSerialNumber(serialNumberInput);
        Units.add(new Unit(serialNumberInput, modelInput));
    }

    // Inspired from Dr. Brian Fraser's videos on GSON use.
    public void readJSONFile(String fileName) throws FileNotFoundException {
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
        FileReader file = new FileReader(fileName);
        Type jsonUnitType = new TypeToken<ArrayList<Unit>>() {
        }.getType();
        Units = gson.fromJson(file, jsonUnitType);
    }

    public void changeSort(ReportSort sortOrder) {
        this.sortOrder = sortOrder;
    }

    public void runSort() {
        switch (sortOrder) {
            case BY_MODEL:
                Collections.sort(Units, new CompareByModel());
                break;
            case BY_LAST_TEST:
                Collections.sort(Units, new CompareByTest());
                break;
            default:
                Collections.sort(Units, new CompareBySerialNumber());
        }
    }

    public void runSerialSort() {
        Collections.sort(Units, new CompareBySerialNumber());
    }

    public ArrayList<String> collectReport(ReportType selected) {
        if (selected == null) {
            return new ArrayList<>();
        }
        ArrayList<String> list = new ArrayList<>();
        switch (selected) {
            case DEFECTIVE:
                for (Unit current : Units) {
                    list.addAll(current.reportDefective());
                }
                break;
            case READY_TO_SHIP:
                for (Unit current : Units) {
                    list.addAll(current.reportReadyToShip());
                }
                break;
            default:
                for (Unit current : Units) {
                    list.addAll(current.reportAll());
                }
        }
        return list;
    }
}
