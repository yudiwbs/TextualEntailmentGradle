package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import java.io.File;
import java.util.TreeSet;

/**
 * Created by yudiwbs on 20/06/2016.
 */
public class CobaBacaFileSatuDir {

    public static void main(String[] args) {

        TreeSet<String> ts = new TreeSet<String>();
        final File folder = new File("D:\\corpus\\ppdb\\ppdb-1.0-large-all\\pecahan");
        for (File fileEntry : folder.listFiles()) {
            if (!fileEntry.isDirectory()) {
                //System.out.println(fileEntry.getName());
                ts.add(fileEntry.getName());
            }
        }

        for (String s : ts) {
            System.out.println(s);
        }
    }
}
