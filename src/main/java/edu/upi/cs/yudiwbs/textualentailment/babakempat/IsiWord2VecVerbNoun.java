package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by yudiwbs on 18/03/2016.
 *
 *    cari skor word2vec antara
 *      verb H - verb T
 *      noun H - Noun T
 *      verb H - Noun T
 *
 *   namatabel diisi, tabel mengandung field skor_word2vec_verb, skor_word2vec_noun,skor_word2vec_verbHnounT
 *
 *
 *
 */

public class IsiWord2VecVerbNoun {
    public String namaTabel;
    private Connection conn = null;
    private PreparedStatement pUpd = null;
    private PreparedStatement pSel = null;

    ResultSet rs = null;
    Prepro pp;
    WordVectors vec  = null;

    public void init(String lokasiFile, String vnamaTabel) {

        namaTabel = vnamaTabel;
        KoneksiDB db = new KoneksiDB();
        pp = new Prepro();
        pp.loadStopWords("stopwords","kata");
        //skor_word2vec_verb,skor_word2vec_noun,skor_word2vec_verbHnounT
        try {
            conn = db.getConn();
            String strSel = "select id,t,h, t_gram_structure, h_gram_structure " +
                    " from " + namaTabel + " where skor_word2vec_verb is null" ;

            String strUpdate = "update "+namaTabel+ " set skor_word2vec_verb=?,skor_word2vec_noun=?," +
                               "skor_word2vec_verbHnounT=?  where id=? ";

            pSel = conn.prepareStatement(strSel);
            pUpd = conn.prepareStatement(strUpdate);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        /*
        File gModel = new File(lokasiFile);
        try {
            System.out.println("Mulai Load, akan lama (4 menitan)...");
            vec = WordVectorSerializer.loadGoogleModel(gModel, true);
            System.out.println("Load selesai... ");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        */
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
                System.out.println("T:"+t);
                System.out.println("H:"+h);

                InfoTeks hPrepro = pp.isiInfoTeks(h, hSynTree);
                hPrepro.id = id;

                InfoTeks tPrepro = pp.isiInfoTeks(t, tSynTree);
                tPrepro.id = id;

                //cari pasangaan kedekatan semua verb H - verb T
                //cari pasangaan kedekatan semua noun H - noun T
                //cari pasangaan kedekatan semua verb H - noun T T

                //skor_word2vec_verb,skor_word2vec_noun,skor_word2vec_verbHnounT

                //cari skor antara VH dan VT

                //verb dengan verb.

                StringBuilder sbVerb = new StringBuilder();
                for (String vH:hPrepro.alVerb) {
                    for (String vT:tPrepro.alVerb) {
                        double skor = vec.similarity(vH, vT);
                        sbVerb.append(vH);sbVerb.append(":");sbVerb.append(vT);
                        sbVerb.append(":");sbVerb.append(skor);sbVerb.append("|");
                    }
                }

                //noun dgn noun
                StringBuilder sbNoun = new StringBuilder();
                for (String vH:hPrepro.alNoun) {
                    for (String vT:tPrepro.alNoun) {
                        double skor = vec.similarity(vH, vT);
                        sbNoun.append(vH);sbNoun.append(":");sbNoun.append(vT);
                        sbNoun.append(":");sbNoun.append(skor);sbNoun.append("|");
                    }
                }

                //verbH dengan noun T
                StringBuilder sbVerbNoun = new StringBuilder();
                for (String vH:hPrepro.alVerb) {
                    for (String vT:tPrepro.alNoun) {
                        double skor = vec.similarity(vH, vT);
                        sbVerbNoun.append(vH);sbVerbNoun.append(":");sbVerbNoun.append(vT);
                        sbVerbNoun.append(":");sbVerbNoun.append(skor);sbVerbNoun.append("|");
                    }
                }


                //skor_word2vec_verb,skor_word2vec_noun,skor_word2vec_verbHnounT
                //update skor

                pUpd.setString(1, sbVerb.toString());
                pUpd.setString(2, sbNoun.toString());
                pUpd.setString(3, sbVerbNoun.toString());
                pUpd.setLong(4, id);
                pUpd.executeUpdate();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }


    public static void main(String[] args) {
        IsiWord2VecVerbNoun iw = new IsiWord2VecVerbNoun();
        //iw.namaTabel ="rte3_babak2";
        //iw.namaTabel ="rte3_test_gold";
        iw.init("D:\\eksperimen\\textualentailment\\GoogleNews-vectors-negative300.bin.gz",
                "rte3_babak2");
        iw.proses();
        iw.close();
    }
}
