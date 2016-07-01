package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import java.util.ArrayList;

/**
 * Created by yudiwbs on 29/06/2016.
 */
public class CobaLambda {
    public static void main(String[] args) {
        ArrayList<String> alString = new ArrayList<>();
        alString.add("aljsfdldfjasdfjlsadf");
        alString.add("ssdlkfjsdf");
        alString.add("ssdlkfjsdf lkj askdfj asdflksaj flkjasdfkl asdf");
        alString.add("ssd");
        alString.sort((s1, s2) -> Integer.compare(s1.length(),s2.length()));
        alString.forEach(System.out::println);
    }
}
