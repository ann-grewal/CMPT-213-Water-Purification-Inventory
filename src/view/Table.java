package view;

import java.util.List;

/* Table Class is the class that outputs a formatted Table.
   All data needs to be converted and held as an ArrayList<String */

public class Table {
    private final List<String> dataList;
    private final List<String> columns;
    private final String title;

    public Table(List<String> dataList, List<String> columns, String title) {
        this.dataList = dataList;
        this.columns = columns;
        this.title = title;
    }

    public void display() {
        System.out.println();
        System.out.println(title);
        String star = "";
        for (int i = 0; i < title.length(); i++) {
            star = star + "*";
        }
        System.out.println(star);
        for (String titles : columns) {
            System.out.printf("%15s", titles);
            System.out.print("  ");
        }
        System.out.println();
        for (String titles : columns) {
            System.out.print("---------------");
            System.out.print("  ");
        }
        System.out.println();
        for (String dataPoints : dataList) {
            if (dataPoints.equals("\n")) {
                System.out.println();
            } else {
                if (dataPoints.startsWith("#")) {
                    String unmarkedComment = dataPoints.substring(1);
                    System.out.print(unmarkedComment);
                } else {
                    System.out.printf("%15s", dataPoints);
                }
                System.out.print("  ");
            }
        }
    }
}
