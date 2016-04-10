package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by yudiwbs on 01/04/2016.
 *
 * ambil semua kata pada T dan H, cari skor antara dua kata tersebut
 *
 */
public class IsiWord2VecSemua {
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
                    " from " + namaTabel  + " where skor_word2vec_semuapasang is null";

            String strUpdate = "update "+namaTabel+ " set skor_word2vec_semuapasang=? " +
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
            if (rs!=null) rs.close();
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String cariSemuaPasang(String t, String h) {

        String out;

        ArrayList<String> alKataT = pp.loadKataTanpaStopWords(t,false,true);
        ArrayList<String> alKataH = pp.loadKataTanpaStopWords(h,false,true);

        StringBuilder sbKombinasi = new StringBuilder();

        double skor = 0;
        for (String vH:alKataH) {
            for (String vT:alKataT) {
                skor = vec.similarity(vH, vT);
                //format:   vH:vT:skor|
                sbKombinasi.append(vH);sbKombinasi.append(":");sbKombinasi.append(vT);
                sbKombinasi.append(":");sbKombinasi.append(skor);sbKombinasi.append("|");
            }
        }
        out = sbKombinasi.toString();
        return out;
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

                String hasil = cariSemuaPasang(t,h);

                //update skor
                pUpd.setString(1, hasil);
                pUpd.setLong(2, id);
                pUpd.executeUpdate();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }


    public static void main(String[] args) {
        IsiWord2VecSemua iw = new IsiWord2VecSemua();
        //iw.namaTabel ="rte3_babak2";
        //iw.namaTabel ="rte3_test_gold";
        iw.init("D:\\eksperimen\\textualentailment\\GoogleNews-vectors-negative300.bin.gz",
                "rte3_babak2");

        /*
        String hasil = iw.cariSemuaPasang("Obama speaks to the media in Illinois","The President greets the press in Chicago");
        String hasil2 = iw.cariSemuaPasang("The band gave a concert in Japan","Obama speaks to the media in Illinois");
        String hasil3 = iw.cariSemuaPasang("Obama speaks in Illinois","The President greets the press in Chicago");
        System.out.println(hasil);
        System.out.println(hasil2);
        System.out.println(hasil3);
        */
        iw.proses();
        iw.close();
    }
}
