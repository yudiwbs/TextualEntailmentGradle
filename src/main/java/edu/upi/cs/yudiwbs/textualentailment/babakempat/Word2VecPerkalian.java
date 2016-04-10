package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;

import java.util.ArrayList;

/**
 * Created by yudiwbs on 06/04/2016.
 */
public class Word2VecPerkalian {

    Prepro pp;

    public Word2VecPerkalian() { //constructor
        pp = new Prepro();
        pp.loadStopWords("stopwords","kata");
    }

    public double jarakMaks(WordVectors vec, String t, String h) {
        /*
        So basically do an all-pairs between
        words in the two sentences to find the closest word pairs in word2vec space,
       then sum these distances together.
         */
        //.
        System.out.println("T:"+t);
        System.out.println("H:"+h);

        ArrayList<String> alKataT = pp.loadKataTanpaStopWords(t,true,true);
        ArrayList<String> alKataH = pp.loadKataTanpaStopWords(h,true,true);

        double totalSkor = 1;  //dikali
        double skor;
        for (String vH:alKataH) {

            double maxSkor = 0; //makin besar makin bagus, makin similar
            String strTercocok="";

            for (String vT:alKataT) {
                //kalau katanya sama persis, skor harusnya maksimal  (anehnya bisa nol kalau pake vec.sim, mungkin kalau oov)
                if (vH.equals(vT)) {
                    skor = 1;
                } else {
                    skor = vec.similarity(vH, vT);
                    if (skor>1)
                    {skor =1;} //karena anehnya  vec.sim bisa menghasilkan: 1.0000001192092896
                    else if (skor<0) {
                        {skor = 0;} //minimumn 0 karena dikali
                    }
                }
                if (skor>maxSkor) {
                    maxSkor = skor;
                    strTercocok = vT;
                }
            }
            System.out.print(vH +"-> "+strTercocok+" ");
            System.out.println("("+maxSkor+")");
            totalSkor = totalSkor * maxSkor; //dikali
        }
        System.out.println("Skor:"+totalSkor);
        return totalSkor;
    }
}
