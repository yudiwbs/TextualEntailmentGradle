package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

/**
 * Created by yudiwbs on 03/04/2016.
 * coba glove
 */

public class CobaGlove {


    public void coba1() {
        //File gModel = new File("D:\\eksperimen\\textualentailment\\GoogleNews-vectors-negative300.bin.gz");
        try {
            System.out.println("mulai load glove");
            //WordVectors vec = WordVectorSerializer.loadGoogleModel(gModel, true);
            WordVectors vec = WordVectorSerializer.loadTxtVectors(new File("D:\\eksperimen\\glove\\glove.6B.300d.txt"));
            System.out.println("load selesai");


            /*
            System.out.println("rejected:passed="+vec.similarity("rejected", "passed"));
            System.out.println("visit:visiting="+vec.similarity("visit", "visiting"));
            System.out.println("discovered:studied"+vec.similarity("discovered", "studied"));
            System.out.println("locate relocate"+vec.similarity("locate", " relocate"));
            */

            /*
            System.out.println("Obama:President="+vec.similarity("Obama", "President"));
            System.out.println("speaks:greets="+vec.similarity("speaks", "greets"));
            System.out.println("media:press"+vec.similarity("discovered", "studied"));
            System.out.println("Illinois:Chicago"+vec.similarity("Illinois", "Chicago"));
            */


            /*
            //output the 10 nearest words to the vector king - queen + woman,
            Collection<String> strList =
                    vec.wordsNearest(Arrays.asList("king", "woman"),
                            Arrays.asList("queen"), 10);
            */
            /*
            Collection<String> strList =
                    vec.wordsNearest(Arrays.asList("bandung", "rendang"),
                            Arrays.asList("bakso"), 10);
            */
            //cari sinonim
            /*
            Collection<String> strList =
                    vec.wordsNearest(Arrays.asList("big", "beautiful"),
                            Arrays.asList("large"), 10);
            */
            //antonim??
            //gagal!!
            Collection<String> strList =
                    vec.wordsNearest(Arrays.asList("beautiful", "big"),
                            Arrays.asList("small"), 10);

            /*Collection<String> strList =
                    vec.wordsNearest(Arrays.asList("New_York", "San_Francisco"),
                            Arrays.asList("Washington"), 10);
            */

            //Collection<String> strList = vec.wordsNearest("washington", 10);
            //Collection<String> strList = vec.wordsNearest("san", 10);

            for (String s:strList) {
                System.out.println(s);
            }


            //WeightLookupTable weightLookupTable = vec.lookupTable();
            //Iterator<INDArray> vectors = weightLookupTable.vectors();
            //INDArray wordVector = vec.getWordVectorMatrix("myword");
            //double[] wordVector = vec.getWordVector("myword");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cobaInteraktif() {
        //bisa dientry, jadi tidak perlu load berulang2
        //load glove
        WordVectors vec=null;
        System.out.println("mulai load glove");
        try {
            vec = WordVectorSerializer.loadTxtVectors(
                    new File("D:\\eksperimen\\glove\\glove.6B.300d.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("load selesai");

        boolean stop = false;
        while (!stop) {
            System.out.print("Pilihan (0:cari sim, 1: cari analogi, 9: selesai):");
            int pil = ambilInputUserInt();
            if (pil == 0) {
                System.out.print("Masukkan kata1:");
                String str1 = ambilInputUserStr();
                System.out.print("Masukkan kata2:");
                String str2 = ambilInputUserStr();
                //aneh lowercase memberikan hasil yg beda walaupun inputnya sama
                System.out.println("Sim "+str1+":"+str2+"="+vec.similarity(str1,
                        str2));
            } else if (pil == 1) {
                //output the 10 nearest words to the vector king - queen + woman,
                System.out.print("Masukkan kata positif (contoh: king):");
                String strPos1 = ambilInputUserStr();
                System.out.print("Masukkan kata negatif (contoh: queen):");
                String strNeg1 = ambilInputUserStr();
                System.out.print("Masukkan kata positif yg akan dicari " +
                        "pasangan neg (contoh: woman):");
                String strPos2 = ambilInputUserStr();

                //output the 10 nearest words to the vector king - queen + woman,
                /*
                Collection<String> strList =
                        vec.wordsNearest(Arrays.asList("king", "woman"),
                                Arrays.asList("queen"), 10);
                */


                Collection<String> strList =
                        vec.wordsNearest(Arrays.asList(strPos1, strPos2),
                                Arrays.asList(strNeg1), 10);
                for (String s: strList) {
                    System.out.println(s);
                }
            } else if (pil==9) {
                stop = true;
            }
        } //loop foverever

    }

    private  int ambilInputUserInt() {
        int myInt=-1;
        try {
            Scanner keyboard = new Scanner(System.in);
            myInt = keyboard.nextInt();
        }
        catch (Exception ex) {
            return -1;
        }
        return myInt;
    }

    private  String ambilInputUserStr() {
        Scanner keyboard = new Scanner(System.in);
        String out = keyboard.next();
        return out;
    }


    public static void main(String[] args) {
            CobaGlove cg = new CobaGlove();
            cg.cobaInteraktif();
    }
}
