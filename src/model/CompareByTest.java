package model;

import java.lang.*;
import java.util.*;
import java.time.*;

/* CompareByTest is the helper class that sorts Units by the date of their last test. */

class CompareByTest implements Comparator<Unit> {
    @Override
    public int compare(Unit a, Unit b) {
        if (a.noTests()) {
            return 1;
        }
        if (b.noTests()) {
            return -1;
        }
        LocalDate dateA = a.lastTest().getDate();
        LocalDate dateB = b.lastTest().getDate();
        return dateA.compareTo(dateB);
    }
}

