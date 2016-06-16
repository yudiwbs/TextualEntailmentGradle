package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import java.io.*;
import java.util.Scanner;

/**
 * Created by yudiwbs on 06/06/2016.
 */
public class CobaBacaFile {

    public static void baca2() {
        String posLoc = "D:\\desertasi\\eksperimen\\final_ProsesRootSyntaxNet.txt";
        //Set<String> result = new TreeSet<String>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(new File(posLoc)));
            String availalbe;
            while((availalbe = br.readLine()) != null) {
                //result.add(availalbe);
                System.out.println(availalbe);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    public static void main(String[] args) {
        baca2();

        /*
        String fileInput = "D:\\desertasi\\eksperimen\\final_ProsesRootSyntaxNet.txt";

        try {
            Scanner scInput = new Scanner(new File(fileInput));
            while (scInput.hasNextLine()) {
                String line = scInput.next();
                //if (line.contains("Skor rata2 semua predikat")) {
                System.out.println(line);
                //} else {
                //    System.out.println("-->"+line);
                //}
            }
            scInput.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        */
    }
}
