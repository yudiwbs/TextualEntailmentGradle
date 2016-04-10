package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by yudiwbs on 31/03/2016.
 *
 * kemiripan dua kalimat berdasarkan word2wec
 *
 * berdasarkan http://sujitpal.blogspot.co.id/2015/09/sentence-similarity-using-word2vec-and.html
 *
 *
 *  The WMD is a distance function that measures the distance between two texts as
 *  the cumulative sum of minimum distance each word in one text must move in vector
 *  space to the closest word in the other text. So basically do an all-pairs between
 *  words in the two sentences to find the closest word pairs in word2vec space,
 *  then sum these distances together.
 *
 *
 *  implementasi masih bukan WMD.. versi python masih gagal :(
 */


public class IsiWord2VecSujitpal {

    WordVectors vec  = null;

    public String namaTabel;
    private Connection conn = null;
    private PreparedStatement pUpd = null;
    private PreparedStatement pSel = null;

    ResultSet rs = null;
    Prepro pp;

    public void init(String lokasiFile, String vnamaTabel) {
        namaTabel = vnamaTabel;
        KoneksiDB db = new KoneksiDB();
        pp = new Prepro();
        pp.loadStopWords("stopwords","kata");
        //skor_word2vec_verb,skor_word2vec_noun,skor_word2vec_verbHnounT
        try {
            conn = db.getConn();
            //jika sudah ada isi lagi (dikomentari yg bagian is null)
            String strSel = "select id,t,h, t_gram_structure, h_gram_structure " +
                    " from " + namaTabel + " #where skor_word2vec_wmd is null" ;

            //String strUpdate = "update "+namaTabel+ " set skor_word2vec_wmd=? " +
            //        " where id=? ";


            String strUpdate = "update "+namaTabel+ " set skor_word2vec_wmd_casesensitif=? " +
                           " where id=? ";

            pSel = conn.prepareStatement(strSel);
            pUpd = conn.prepareStatement(strUpdate);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        File gModel = new File(lokasiFile);
        try {
            System.out.println("Mulai Load, akan lama (4 menitan)...");
            vec = WordVectorSerializer.loadGoogleModel(gModel, true);
            System.out.println("Load selesai... ");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void finalize() {
        close();
    }

    public void close() {
        try {
            pSel.close();
            pUpd.close();
            rs.close();
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void proses() {
        rs = null;
        try {
            rs = pSel.executeQuery();
            while (rs.next()) {
                //id,t,h, t_gram_structure, h_gram_structure
                int id = rs.getInt(1);
                String t = rs.getString(2);
                String h = rs.getString(3);
                String tSynTree = rs.getString(4);
                String hSynTree = rs.getString(5);

                System.out.println("id:"+id);
                //System.out.println("T:"+t);
                //System.out.println("H:"+h);
                //skor_word2vec_verb,skor_word2vec_noun,skor_word2vec_verbHnounT
                //update skor
                double jarak = jarakMaks(t,h);

                pUpd.setDouble(1, jarak);
                pUpd.setLong(2, id);
                pUpd.executeUpdate();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    public double jarakMaks(String t, String h) {
        /*
        So basically do an all-pairs between
        words in the two sentences to find the closest word pairs in word2vec space,
       then sum these distances together.
         */
        //.
        System.out.println("T:"+t);
        System.out.println("H:"+h);
        //tidak lowercase karena ternyata vektor 100milyar kata gooogle itu casesentsive
        ArrayList<String> alKataT = pp.loadKataTanpaStopWords(t,true,true);
        ArrayList<String> alKataH = pp.loadKataTanpaStopWords(h,true,true);
        double totalSkor = 0;
        double skor;
        for (String vH:alKataH) {
            double maxSkor = -1; //makin besar makin bagus, makin similar
            String strTercocok="";

            for (String vT:alKataT) {
                //kalau katanya sama persis, skor harusnya maksimal  (bisa nol kalau pake vec.sim, mungkin kalau oov)
                if (vH.equals(vT)) {
                    skor = 1.1;  //karena anehnya  vec.sim bisa menghasilkan: 1.0000001192092896
                } else {
                    skor = vec.similarity(vH, vT);
                }

                //double skor2 = vec.getWordVector()
                //cari yang paling dekat (paling besar) dengan vH
                if (skor>maxSkor) {
                    maxSkor = skor;
                    strTercocok = vT;
                }
            }
            System.out.println(vH +"-> "+strTercocok);
            System.out.println("Makskor :"+maxSkor);
            totalSkor = totalSkor + maxSkor;
        }
        System.out.println("Skor:"+totalSkor);
        return totalSkor;
    }


    public static void main(String[] args) {
        IsiWord2VecSujitpal iw = new IsiWord2VecSujitpal();
        iw.init("D:\\eksperimen\\textualentailment\\GoogleNews-vectors-negative300.bin.gz",
                "rte3_babak2");

       // iw.init("D:\\eksperimen\\textualentailment\\GoogleNews-vectors-negative300.bin.gz",
       //         "rte3_test_gold");

        iw.proses();
        iw.close();

        /*
        String kata1="A glass of cider";
        String kata2="A full cup of apple juice";
        iw.jarakMaks(kata1,kata2);
        iw.jarakMaks("It is a dog.","That must be your dog.");
        iw.jarakMaks("Red alcoholic drink.", "An English dictionary.");
        */

    }

}

