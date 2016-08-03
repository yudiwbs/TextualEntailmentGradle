package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by yudiwbs on 03/04/2016.IsiWordEmbedUmbc
 *
 *   lihat class isiWordEmbedUmbc <- kelas ini jgn diganti karena sdh yg terbaik
 *   tambahan kelas ini:
 *   - fokus untuk melihat kesalahan tiap word embeded
 *   - langkah2nya, hasilkan skor umbc di tabel (lihat method init)
 *   - dengan weka, cari thresholdnya
 *   - jalankan method klasifikasi untuk debug hasil mengeluarkan yang salah
 *
 *

 select
 IF(isEntail = 1, "true", "false") as is_Entail,
 juli_sl999
 from rte3_babak2

 juli_sl999 <= 0.542626: FALSE (276.0/61.0)
 juli_sl999 > 0.542626: TRUE (524.0/172.0)
 *
 */

public class IsiWordUmbcVer2 {
    public String namaTabel;
    private Connection conn = null;
    private PreparedStatement pUpd = null;
    private PreparedStatement pSel = null;

    ResultSet rs = null;
    Prepro pp;
    SimGroupToken sgt;

    //threshold dicari dengan weka
    public void klasifikasi(double threshold,String namaTabelTest) {
        PreparedStatement pSelTest = null;
        String strSelTest = "select id,t,h, t_gram_structure, " +
                "h_gram_structure,t_ner, h_ner, isEntail " +
                " from " + namaTabelTest + " #limit 10" ; //ditabatasi dulu sepuluh

        rs = null;
        try {
            pSelTest = conn.prepareStatement(strSelTest);
            rs = pSelTest.executeQuery();
            int jumPredCocok = 0;
            int cc = 0;
            StringBuilder sb = new StringBuilder();

            sb.append("id,isCocok");
            sb.append(System.getProperty("line.separator"));

            while (rs.next()) {
                //id,t,h, t_gram_structure, h_gram_structure
                cc++;
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


                boolean predEntail;
                predEntail = (jarak>threshold);

                System.out.println("Jarak:"+jarak);
                System.out.println("PredEntail:"+predEntail);
                if (isEntail == predEntail) {
                    jumPredCocok++;
                    sb.append(id+","+1);
                } else {
                    sb.append(id+","+0);
                }
                sb.append(System.getProperty("line.separator"));
            }
            System.out.println("Jumlah pred cocok:"+jumPredCocok);
            System.out.println("Akurasi:"+(double) jumPredCocok/cc);

            System.out.println("Rincian");
            System.out.println(sb.toString());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     * @param vnamaTabel
     * @param kolomTujuan   kolom sudah dicreate di tabel
     * @param gloveOrW2vec  0: glove, 1: w2vec
     * @param fileVector
     */
    public void init(String vnamaTabel, String kolomTujuan, int gloveOrW2vec, String fileVector) {

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
                    " from " + namaTabel + " #limit 10" ; //ditabatasi dulu sepuluh, tambah # untuk comment

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

        //word2vec
        //sgt = new SimGroupToken(1,"D:\\eksperimen\\textualentailment\\GoogleNews-vectors-negative300.bin.gz");


        sgt = new SimGroupToken(gloveOrW2vec,fileVector);

        //glovec
        //sgt = new SimGroupToken(0,"D:\\eksperimen\\paragram\\paragram_300_sl999\\paragram_300_sl999\\paragram_300_sl999.txt");
        //sgt = new SimGroupToken(0,"D:\\eksperimen\\paragram\\paragram_300_ws353\\paragram_300_ws353\\paragram_300_ws353.txt");
        //sgt = new SimGroupToken(0,"D:\\eksperimen\\glove\\glove.6B.300d.txt");

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

        IsiWordUmbcVer2 iw = new IsiWordUmbcVer2();
        //iw.init("D:\\eksperimen\\glove\\glove.6B.300d.txt","rte3_babak2");
        //iw.init(str840,"rte3_babak2","skor_glove_perkalian_840"); //gagal
        //iw.init("rte3_test_gold","umbc_glove2");
        //iw.init("rte3_babak2","umbc_glove_lematisasi");
        //iw.init("rte3_babak2","umbc_paragram_penaltisubj_fix"); //pake vektor paragram, dari paper From Paraphrase Database to Compositional Paraphrase Model and Back (Wieting)
        //iw.init("rte3_babak2","umbc_paragram_best");
        //iw.init("rte3_test_gold","umbc_paragram_best");
        //iw.init("rte3_test_normal","umbc_paragram");
        //iw.init("rte3_babak3_normalisasi","umbc_paragram");

        //iw.init("rte3_test_gold","umbc_paragram");
        //iw.init("rte3_babak2","umbc_paragram");
        //iw.init("rte3_babak2","juli_sl999");
        //iw.init("rte3_babak2","juli_glove_ws353");  //lihat ke dalam mehtod init untuk ganti model
        //iw.init("rte3_babak2","juli_glove_6B");

        //iw.init("rte3_babak2","juli_w2vec_neg300",1,"D:\\eksperimen\\textualentailment\\GoogleNews-vectors-negative300.bin.gz");

        //yang freebase jelek hasilnya, tidak perlu sampai test (55 untuk training)
        //iw.init("rte3_babak2","juli_w2vec_freebase",1,"D:\\eksperimen\\textualentailment\\freebase-vectors-skipgram1000.bin\\knowledge-vectors-skipgram1000.bin");

        //iw.init("rte3_babak2","",0,"D:\\eksperimen\\paragram\\paragram_300_sl999\\paragram_300_sl999\\paragram_300_sl999.txt");
        //iw.init("rte3_babak2","",0,"D:\\eksperimen\\glove\\glove.6B.300d.txt");
        //iw.init("rte3_babak2","juli_glove_42B",0,"D:\\eksperimen\\glove\\glove.42B.300d.txt");  //<-- terbaik
        iw.init("rte3_babak2","juli_glove_840B",0,"D:\\eksperimen\\glove\\glove.840B.300d.txt");
        //iw.proses();  //kalau klasifikasi, proses dimatikan

        /*
        glove:
        juli_sl999 <= 0.542626: FALSE (276.0/61.0)
        juli_sl999 > 0.542626: TRUE (524.0/172.0)

        juli_glove_ws353 <= 0.774786: FALSE (431.0/138.0)
        juli_glove_ws353 > 0.774786: TRUE (369.0/94.0)

        juli_glove_6B <= 0.836064: FALSE (411.0/119.0)
        juli_glove_6B > 0.836064: TRUE (389.0/95.0)

        juli_glove_840B <= 0.776634: FALSE (317.0/74.0)
        juli_glove_840B > 0.776634: TRUE (483.0/144.0)

        juli_w2vec_neg300 <= 0.637661: FALSE (311.0/80.0)
        juli_w2vec_neg300 > 0.637661: TRUE (489.0/156.0)

         <= 0.83624: FALSE (367.0/100.0)
        glove_42B > 0.83624: TRUE (433.0/120.0)

        */
        //iw.klasifikasi(0.542626,"rte3_test_gold");  //glove pargram_sl999
        //iw.klasifikasi(0.774786,"rte3_test_gold"); //glove paragram_ws353
        //iw.klasifikasi(0.836064,"rte3_test_gold");   //glove 6b
        //iw.klasifikasi(0.83624,"rte3_test_gold");  //glove_42B   <-- terbaik!
        iw.klasifikasi(0.776634,"rte3_test_gold"); //glove_840B  <-- berat!
        //iw.klasifikasi(0.637661,"rte3_test_gold"); //w2vec neg
        iw.close();

        //iw.init("rte3_babak2","umbc_glove_w2c"); //gabungan antara w2c dan glove (rata2? max? min?)
        //iw.init("rte3_test_gold","umbc_w2v_fix1"); //hanya w2v, tapi berdasarkan fix1
        //iw.init("rte3_test_gold","umbc_glove_fixdate_stopwords2");
        //iw.init("rte3_test_gold","umbc_glove_fixdate");
        //iw.init("rte3_test_gold","umbc_glove_wordnet");
        //iw.init("rte3_test_gold","umbc_glove3");

        //iw.init("D:\\eksperimen\\glove\\glove.6B.300d.txt","rte3_test_gold");
        // iw.init("D:\\eksperimen\\textualentailment\\GoogleNews-vectors-negative300.bin.gz",
        //         "rte3_test_gold");


    }

}

