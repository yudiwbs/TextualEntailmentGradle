package edu.upi.cs.yudiwbs.textualentailment.babakempat;


import org.canova.api.util.ClassPathResource;
import org.deeplearning4j.models.embeddings.WeightLookupTable;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.LineSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentencePreProcessor;
import org.nd4j.linalg.api.ndarray.INDArray;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;


/**
 * Created by yudiwbs on 17/03/2016.
 */
public class CobaWord2Vec {

    public static void main(String[] args) {

        File gModel = new File("D:\\eksperimen\\textualentailment\\GoogleNews-vectors-negative300.bin.gz");
        try {
            System.out.println("mulai load");
            WordVectors vec = WordVectorSerializer.loadGoogleModel(gModel, true);
            System.out.println("load selesai");


            //double v = pw.hitungSimilarity("visit","oppress");
            //double v = pw.hitungSimilarity("visit","visiting");
            //hitungSimWordnet2
            //double v = pw.hitungSimWordnet2("visit come","approach come");
            //double v = pw.hitungSimWordnet2("gila","gather win promote");
            //double v = pw.hitungSimWordnet2("rejected","passed");


            System.out.println("rejected:passed="+vec.similarity("rejected", "passed"));
            System.out.println("visit:visiting="+vec.similarity("visit", "visiting"));
            System.out.println("discovered:studied"+vec.similarity("discovered", "studied"));
            System.out.println("locate relocate"+vec.similarity("locate", " relocate"));


            WeightLookupTable weightLookupTable = vec.lookupTable();
            //Iterator<INDArray> vectors = weightLookupTable.vectors();
            INDArray wordVector = vec.getWordVectorMatrix("myword");
            //double[] wordVector = vec.getWordVector("myword");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
