package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by yudiwbs on 03/04/2016.
 *
 *  menggunakan glove untuk kemiripan dua sentnece
 *  cari kata H yg paling tinggi skornya dengan T
 *
 *  lalu total dikalikan (mirip spt word2vec perkalian)
 *
 */

public class IsiGlove {
    WordVectors vec  = null;

    public String namaTabel;
    private Connection conn = null;
    private PreparedStatement pUpd = null;
    private PreparedStatement pSel = null;

    ResultSet rs = null;
    Prepro pp;

    public void init(String lokasiFile, String vnamaTabel, String kolomTujuan) {
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


            String strUpdate = "update "+namaTabel+ " set "+kolomTujuan+"=? " +
                    " where id=? ";

            pSel = conn.prepareStatement(strSel);
            pUpd = conn.prepareStatement(strUpdate);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //File gModel = new File(lokasiFile);
        try {
            System.out.println("Mulai Load, agak lama ...");
            //vec = WordVectorSerializer.loadGoogleModel(gModel, true);
            vec = WordVectorSerializer.loadTxtVectors(new File(lokasiFile));
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


    public static void main(String[] args) {
        //-Xms1024m -Xmx10g
        //yg 840B token saat dijalankan makan waktu 3 jam dan akhirnya kehabisan memori, test lagi nanti
        //840B tokens, 2.2M vocab, cased, 300d vectors, 2.03 GB download
        String str840 = "D:\\eksperimen\\glove\\glove.840B.300d\\glove.840B.300d.txt";
        //6B tokens, 400K vocab, uncased, 50d, 100d, 200d, & 300d
        String str6 ="D:\\eksperimen\\glove\\glove.6B.300d.txt";

        //42B tokens, 1.9M vocab, uncased, 300d vectors
        String str42 = "D:\\eksperimen\\glove\\glove.42B.300d\\glove.42B.300d.txt";

        IsiGlove iw = new IsiGlove();
        //iw.init("D:\\eksperimen\\glove\\glove.6B.300d.txt","rte3_babak2");
        //iw.init(str840,"rte3_babak2","skor_glove_perkalian_840"); //gagal
        //iw.init(str42,"rte3_babak2","skor_glove_perkalian_42");
        iw.init(str42,"rte3_test_gold","skor_glove_perkalian_42");


        //iw.init("D:\\eksperimen\\glove\\glove.6B.300d.txt","rte3_test_gold");


        //

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
