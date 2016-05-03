package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by yudiwbs on 03/04/2016.
 *
 *   menggunakan glove/word2vec untuk kemiripan dua sentnece,
 *
 *   - lihat di bagian init untuk setvectornya
 *   - untuk set penalti liha tclass simGroupToken
 *   adaptasi dari paper UMBC (han2013)
 *
 */

public class IsiWordEmbedUmbc {
    public String namaTabel;
    private Connection conn = null;
    private PreparedStatement pUpd = null;
    private PreparedStatement pSel = null;

    ResultSet rs = null;
    Prepro pp;
    SimGroupToken sgt;

    public void init(String vnamaTabel, String kolomTujuan) {
        namaTabel = vnamaTabel;
        KoneksiDB db = new KoneksiDB();
        pp = new Prepro();
        pp.loadStopWords("stopwords","kata");
        //skor_word2vec_verb,skor_word2vec_noun,skor_word2vec_verbHnounT
        try {
            conn = db.getConn();
            //jika sudah ada isi lagi (dikomentari yg bagian is null)
            String strSel = "select id,t,h, t_gram_structure, " +
                    "h_gram_structure,t_ner, h_ner, isEntail " +
                    " from " + namaTabel + " #limit 10" ; //ditabatasi dulu sepuluh


            String strUpdate = "update "+namaTabel+ " set "+kolomTujuan+"=? " +
                    " where id=? ";

            pSel = conn.prepareStatement(strSel);
            pUpd = conn.prepareStatement(strUpdate);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //paling akhir, karena lama

        //
        //paragram sl999: lebih bagus pada hasil testing


        sgt = new SimGroupToken("D:\\eksperimen\\paragram\\paragram_300_sl999\\paragram_300_sl999\\paragram_300_sl999.txt",
                "D:\\eksperimen\\textualentailment\\GoogleNews-vectors-negative300.bin.gz");


        //paragaram ws353
        /*
        sgt = new SimGroupToken("D:\\eksperimen\\paragram\\paragram_300_ws353\\paragram_300_ws353\\paragram_300_ws353.txt",
                "D:\\eksperimen\\textualentailment\\GoogleNews-vectors-negative300.bin.gz");
        */


        //glove
        /*
        sgt = new SimGroupToken("D:\\eksperimen\\glove\\glove.6B.300d.txt",
                "D:\\eksperimen\\textualentailment\\GoogleNews-vectors-negative300.bin.gz");
        */


        //sgt = new SimGroupToken("D:\\eksperimen\\textualentailment\\GoogleNews-vectors-negative300.bin.gz");
        //System.out.println("Menggunakan W2Vec");

        //batas terendah sebelum kena penalti
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
                String tNer = rs.getString(6);
                String hNer = rs.getString(7);
                Boolean isEntail = rs.getBoolean(8);

                //debug print
                System.out.println("");
                System.out.println("id:"+id);
                //System.out.println("T:"+t);
                //System.out.println("H:"+h);
                //skor_word2vec_verb,skor_word2vec_noun,skor_word2vec_verbHnounT
                //update skor
                //double jarak = jarakMaks(t,h,tNer,hNer);

                System.out.println("T:"+t);
                System.out.println("H:"+h);
                System.out.println("IsEntail:----------->" +
                        ""+isEntail);

                //isi group token
                GroupToken gtT = new GroupToken();
                gtT.ambilToken(t,tNer);
                GroupToken gtH = new GroupToken();
                gtH.ambilToken(h,hNer);

                //nanti bisa gabung pengisian variabelnya
                sgt.setGroupToken(gtT,gtH);
                sgt.setTH(t,h);
                sgt.setPosTag(tSynTree,hSynTree);
                double jarak = sgt.getSim();
                System.out.println("Jarak:"+jarak);

                pUpd.setDouble(1, jarak);
                pUpd.setLong(2, id);
                pUpd.executeUpdate();

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static void main(String[] args) {

        //yg 840B token saat dijalankan makan waktu 3 jam dan akhirnya kehabisan memori, test lagi nanti
        //840B tokens, 2.2M vocab, cased, 300d vectors, 2.03 GB download
        //String str840 = "D:\\eksperimen\\glove\\glove.840B.300d\\glove.840B.300d.txt";
        //6B tokens, 400K vocab, uncased, 50d, 100d, 200d, & 300d
        //String str6 ="D:\\eksperimen\\glove\\glove.6B.300d.txt";
        //42B tokens, 1.9M vocab, uncased, 300d vectors, hasil kurang
        //String str42 = "D:\\eksperimen\\glove\\glove.42B.300d\\glove.42B.300d.txt";

        IsiWordEmbedUmbc iw = new IsiWordEmbedUmbc();
        //iw.init("D:\\eksperimen\\glove\\glove.6B.300d.txt","rte3_babak2");
        //iw.init(str840,"rte3_babak2","skor_glove_perkalian_840"); //gagal
        //iw.init("rte3_test_gold","umbc_glove2");
        //iw.init("rte3_babak2","umbc_glove_lematisasi");
        //iw.init("rte3_babak2","umbc_paragram_penaltisubj_fix"); //pake vektor paragram, dari paper From Paraphrase Database to Compositional Paraphrase Model and Back (Wieting)
        //iw.init("rte3_babak2","umbc_paragram_best");
        //iw.init("rte3_test_gold","umbc_paragram_best");
        iw.init("rte3_babak2","umbc_glove_dua_arah"); //lanjutan dari fix1, T->H plus H->T, jelek 67.125
        //iw.init("rte3_babak2","umbc_glove_w2c"); //gabungan antara w2c dan glove (rata2? max? min?)
        //iw.init("rte3_test_gold","umbc_w2v_fix1"); //hanya w2v, tapi berdasarkan fix1
        //iw.init("rte3_test_gold","umbc_glove_fixdate_stopwords2");
        //iw.init("rte3_test_gold","umbc_glove_fixdate");
        //iw.init("rte3_test_gold","umbc_glove_wordnet");
        //iw.init("rte3_test_gold","umbc_glove3");

        //iw.init("D:\\eksperimen\\glove\\glove.6B.300d.txt","rte3_test_gold");
        // iw.init("D:\\eksperimen\\textualentailment\\GoogleNews-vectors-negative300.bin.gz",
        //         "rte3_test_gold");

        iw.proses();
        iw.close();
    }

}
