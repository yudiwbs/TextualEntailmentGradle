package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *    Created by yudiwbs on 03/04/2016.
 *
 *    Khusus untuk word2vec
 *    Proses seperti San Francisco = San_Francisco  tambah underscore
 *
 */


public class IsiWord2VecFrase {

    public String namaTabel;
    private Connection conn = null;
    private PreparedStatement pUpd = null;
    private PreparedStatement pSel = null;

    ResultSet rs = null;
    Prepro pp;
    WordVectors vec  = null;
    Word2VecPerkalian wp = new Word2VecPerkalian();

    public void init(String lokasiFile, String vnamaTabel) {

        namaTabel = vnamaTabel;
        KoneksiDB db = new KoneksiDB();
        pp = new Prepro();
        pp.loadStopWords("stopwords","kata");
        //skor_word2vec_verb,skor_word2vec_noun,skor_word2vec_verbHnounT
        try {
            conn = db.getConn();
            String strSel = "select id,t,h,t_ner,h_ner  " +
                    " from " + namaTabel ;

            String strUpdate = "update "+namaTabel+ " set " +
                    "skor_word2vec_perkalian_frase=?  where id=? ";

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

    private String prosesFrase(String str, String ner) {
        String out = new String(str);
        HashMap<String,ArrayList<String>> hmNer = pp.ambilInfoNer(ner);
        //ArrayList<String> multikata = new ArrayList<>();
        for (String key : hmNer.keySet()) {

            if (  key.equals("PERSON") ||
                    key.equals("ORGANIZATION" )  ||
                    key.equals("LOCATION")  ) {

                ArrayList<String> al = hmNer.get(key);
                System.out.println("key:"+key);
                for (String s : al) {
                    s = s.trim();
                    if (s.contains(" ")) {
                        //multikata.add(s);
                        //System.out.println(s);
                        //System.out.println(s.replaceAll(" ","_"));
                        out = out.replace(s,s.replaceAll(" ","_"));
                    }
                    //System.out.println(s);
                }
            }
        }
        return out;
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
                String tNer = rs.getString(4);
                String hNer = rs.getString(5);

                //System.out.println("id:"+id);
                //System.out.println("T:"+t);
                //System.out.println("H:"+h);
                System.out.println("tNer:"+tNer);
                System.out.println("hNer:"+hNer);


                //proses T
                String tProses = prosesFrase(t,tNer);
                System.out.println("T proses="+tProses);

                String hProses = prosesFrase(h,hNer);
                System.out.println("H proses="+hProses);

                double jarak = wp.jarakMaks(vec,tProses,hProses);

                //skor_word2vec_verb,skor_word2vec_noun,skor_word2vec_verbHnounT
                //update skor

                pUpd.setDouble(1, jarak);
                pUpd.setLong(2, id);
                pUpd.executeUpdate();

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static void main(String[] args) {
        //-Xms1024m -Xmx10g
        IsiWord2VecFrase iw = new IsiWord2VecFrase();
        //iw.namaTabel ="rte3_babak2";
        //iw.namaTabel ="rte3_test_gold";
        //iw.init("D:\\eksperimen\\textualentailment\\GoogleNews-vectors-negative300.bin.gz",
        //        "rte3_babak2");
        iw.init("D:\\eksperimen\\textualentailment\\GoogleNews-vectors-negative300.bin.gz",
                "rte3_test_gold");
        iw.proses();
        iw.close();
    }


}
