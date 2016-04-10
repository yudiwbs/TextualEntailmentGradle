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
 *  menggunakan perkalian, niru yg wordnet
 */


public class IsiWord2VecPerkalian {

    WordVectors vec  = null;

    public String namaTabel;
    private Connection conn = null;
    private PreparedStatement pUpd = null;
    private PreparedStatement pSel = null;

    ResultSet rs = null;
    Word2VecPerkalian wp = new Word2VecPerkalian();


    public void init(String lokasiFile, String vnamaTabel) {
        namaTabel = vnamaTabel;
        KoneksiDB db = new KoneksiDB();

        //skor_word2vec_verb,skor_word2vec_noun,skor_word2vec_verbHnounT
        try {
            conn = db.getConn();
            //jika sudah ada isi lagi (dikomentari yg bagian is null)
            String strSel = "select id,t,h, t_gram_structure, h_gram_structure " +
                    " from " + namaTabel + " #where skor_word2vec_wmd is null" ;

            //String strUpdate = "update "+namaTabel+ " set skor_word2vec_wmd=? " +
            //        " where id=? ";


            String strUpdate = "update "+namaTabel+ " set skor_word2vec_perkalian=? " +
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
                double jarak = wp.jarakMaks(vec,t,h);

                pUpd.setDouble(1, jarak);
                pUpd.setLong(2, id);
                pUpd.executeUpdate();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static void main(String[] args) {
        IsiWord2VecPerkalian iw = new IsiWord2VecPerkalian();
        //iw.init("D:\\eksperimen\\textualentailment\\GoogleNews-vectors-negative300.bin.gz",
        //        "rte3_babak2");

       iw.init("D:\\eksperimen\\textualentailment\\GoogleNews-vectors-negative300.bin.gz",
                "rte3_test_gold");

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

