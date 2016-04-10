package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by yudiwbs on 03/04/2016.
 * coba glove
 */

public class CobaGlove {
    public static void main(String[] args) {
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
            Collection<String> strList =
                    vec.wordsNearest(Arrays.asList("king", "woman"),
                            Arrays.asList("queen"), 10);
            */
            Collection<String> strList =
                    vec.wordsNearest(Arrays.asList("bandung", "rendang"),
                            Arrays.asList("bakso"), 10);

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
}
