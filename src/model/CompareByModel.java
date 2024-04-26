package model;

import java.math.BigInteger;
import java.lang.*;
import java.util.*;

/* CompareByModel is the helper class that sorts Units by their model. */

class CompareByModel implements Comparator<Unit> {
    @Override
    public int compare(Unit a, Unit b) {
        int compare = a.getModel().compareTo(b.getModel());
        if (compare == 0) {
            BigInteger intA = new BigInteger(a.getSerialNumber());
            BigInteger intB = new BigInteger(b.getSerialNumber());
            return intA.compareTo(intB);
        }
        return compare;
    }
}