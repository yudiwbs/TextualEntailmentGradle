package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.xpath.operations.Number;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;

import java.io.File;
import java.security.acl.Group;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by yudiwbs on 03/04/2016.
 *
 *  menggunakan glove untuk kemiripan dua sentnece
 *
 *  adaptasi dari paper UMBC (han2013)
 *
 */

public class IsiGloveUmbc1 {
    public String namaTabel;
    private Connection conn = null;
    private PreparedStatement pUpd = null;
    private PreparedStatement pSel = null;

    ResultSet rs = null;
    Prepro pp;

    public void init(String vnamaTabel, String kolomTujuan) {
        namaTabel = vnamaTabel;
        KoneksiDB db = new KoneksiDB();
        pp = new Prepro();
        pp.loadStopWords("stopwords","kata");
        //skor_word2vec_verb,skor_word2vec_noun,skor_word2vec_verbHnounT
        try {
            conn = db.getConn();
            //jika sudah ada isi lagi (dikomentari yg bagian is null)
            String strSel = "select id,t,h, t_gram_structure, h_gram_structure,t_ner, h_ner " +
                    " from " + namaTabel + " #limit 10" ; //ditabatasi dulu sepuluh


            String strUpdate = "update "+namaTabel+ " set "+kolomTujuan+"=? " +
                    " where id=? ";

            pSel = conn.prepareStatement(strSel);
            pUpd = conn.prepareStatement(strUpdate);
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
            SimGroupToken sgt = new SimGroupToken("D:\\eksperimen\\glove\\glove.6B.300d.txt");

            while (rs.next()) {
                //id,t,h, t_gram_structure, h_gram_structure
                int id = rs.getInt(1);
                String t = rs.getString(2);
                String h = rs.getString(3);
                String tSynTree = rs.getString(4);
                String hSynTree = rs.getString(5);
                String tNer = rs.getString(6);
                String hNer = rs.getString(7);

                System.out.println("");
                System.out.println("id:"+id);
                //System.out.println("T:"+t);
                //System.out.println("H:"+h);
                //skor_word2vec_verb,skor_word2vec_noun,skor_word2vec_verbHnounT
                //update skor
                //double jarak = jarakMaks(t,h,tNer,hNer);

                System.out.println("T:"+t);
                System.out.println("H:"+h);

                //isi group token
                GroupToken gtT = new GroupToken();
                gtT.ambilToken(t,tNer);
                GroupToken gtH = new GroupToken();
                gtH.ambilToken(h,hNer);

                sgt.setGroupToken(gtT,gtH);
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
        String str840 = "D:\\eksperimen\\glove\\glove.840B.300d\\glove.840B.300d.txt";
        //6B tokens, 400K vocab, uncased, 50d, 100d, 200d, & 300d
        String str6 ="D:\\eksperimen\\glove\\glove.6B.300d.txt";
        //42B tokens, 1.9M vocab, uncased, 300d vectors, hasil kurang
        String str42 = "D:\\eksperimen\\glove\\glove.42B.300d\\glove.42B.300d.txt";

        IsiGloveUmbc1 iw = new IsiGloveUmbc1();
        //iw.init("D:\\eksperimen\\glove\\glove.6B.300d.txt","rte3_babak2");
        //iw.init(str840,"rte3_babak2","skor_glove_perkalian_840"); //gagal
        iw.init("rte3_babak2","umbc_glove1");
        //iw.init("rte3_test_gold","umbc_glove1");

        //iw.init("D:\\eksperimen\\glove\\glove.6B.300d.txt","rte3_test_gold");
        // iw.init("D:\\eksperimen\\textualentailment\\GoogleNews-vectors-negative300.bin.gz",
        //         "rte3_test_gold");

        iw.proses();
        iw.close();

    }

}
