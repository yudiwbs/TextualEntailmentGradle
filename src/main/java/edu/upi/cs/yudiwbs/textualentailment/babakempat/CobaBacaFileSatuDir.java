package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * Created by yudiwbs on 20/06/2016.
 */
public class CobaBacaFileSatuDir {



    public static void main(String[] args) {


        ArrayList<String> alFile = new ArrayList<String>();
        final File folder = new File("D:\\corpus\\ppdb\\ppdb-1.0-large-all\\pecahan");
        for (File fileEntry : folder.listFiles()) {
            if (!fileEntry.isDirectory()) {
                //System.out.println(fileEntry.getName());
                alFile.add(fileEntry.getName());
            }
        }

        //diurut, jadi disk1, disk2, disk..., disk11, disk12
        Collections.sort(alFile, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return extractInt(o1) - extractInt(o2);
            }

            int extractInt(String s) {
                String num = s.replaceAll("\\D", "");
                // return 0 if no digits found
                return num.isEmpty() ? 0 : Integer.parseInt(num);
            }
        });

        for (String s : alFile) {
            System.out.println(s);
        }
    }
}
