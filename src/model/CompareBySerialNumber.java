package model;

import java.math.BigInteger;
import java.lang.*;
import java.util.*;

/* CompareBySerialNumber is the helper class that sorts Units by their serial number. */

class CompareBySerialNumber implements Comparator<Unit> {
    @Override
    public int compare(Unit a, Unit b) {
        BigInteger intA = new BigInteger(a.getSerialNumber());
        BigInteger intB = new BigInteger(b.getSerialNumber());
        return intA.compareTo(intB);
    }
}