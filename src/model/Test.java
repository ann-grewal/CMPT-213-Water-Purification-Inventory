package model;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/* Unit is the class holds all information on a Unit including date, outcome, and comments.
   Creates String lists of data for report printing (marking comments). */

public class Test {
    private final LocalDate date;
    private final boolean isTestPassed;
    private final String testResultComment;

    public Test(boolean isTestPassed, String testResultComment) {
        this.date = LocalDate.now();
        this.isTestPassed = isTestPassed;
        this.testResultComment = testResultComment;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getStringDate() {
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public boolean getTestPassed() {
        return isTestPassed;
    }

    public String getComment() {
        if (testResultComment == null) {
            return " ";
        }
        return testResultComment;
    }

    public ArrayList<String> testReport() {
        ArrayList<String> testList = new ArrayList<>();
        testList.add(getStringDate());
        if (isTestPassed) {
            testList.add("Passed");
        } else {
            testList.add("Failed");
        }
        String markedComment = "#" + getComment();
        testList.add(markedComment);
        testList.add("\n");
        return testList;
    }
}
