package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import com.mysql.fabric.xmlrpc.base.Param;
import weka.classifiers.trees.j48.C45Split;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by yudiwbs on 03/04/2016.IsiWordEmbedUmbc
 *
 *   
 *   lihat class isiWordEmbedUmbcVer2
 *
 *   tambahan:
 *     - otomatiskan penghitungan parameter untuk data training, lalu ukur akurasinya di data testing
 *
 *
 *
 */

public class IsiWordUmbcVer3 {
    double akurasiTerakhir  = 0;
    String fileArff = "D:\\desertasi\\final\\eksperimen\\out.arff";
    public String namaTabel;
    private Connection conn = null;
    private PreparedStatement pUpd = null;
    private PreparedStatement pSel = null;

    ResultSet rs = null;
    Prepro pp;                 //Prepro versi stopwords
    SimGroupToken sgt;



    //threshold dicari dengan weka  --> nanti dibuat otomatis
    //ingat data dari proses umbc adalah satu skor, lalu dengan weka dicari thresholdnya


    //ambil data t,h dari tabel, berdasarkan threshold an parameter lalu menghitung akurasi
    //tidak efisien juga kalau baru memanggil proses
    public void klasifikasi(double threshold, String namaTabelTest) {
        PreparedStatement pSelTest = null;
        String strSelTest = "select id,t,h, t_gram_structure, " +
                "h_gram_structure,t_ner, h_ner, isEntail " +
                " from " + namaTabelTest + " #limit 10" ; //

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
                //System.out.println("");
                //System.out.println("id:"+id);
                //System.out.println("T:"+t);
                //System.out.println("H:"+h);
                //skor_word2vec_verb,skor_word2vec_noun,skor_word2vec_verbHnounT
                //update skor
                //double jarak = jarakMaks(t,h,tNer,hNer);
                /*
                System.out.println("T:"+t);
                System.out.println("H:"+h);
                System.out.println("IsEntail:----------->" +
                        ""+isEntail);
                */
                //isi group token
                GroupToken gtT = new GroupToken(pp);
                gtT.ambilToken(t,tNer);
                GroupToken gtH = new GroupToken(pp);
                gtH.ambilToken(h,hNer);

                //nanti bisa gabung pengisian variabelnya
                sgt.setGroupToken(gtT,gtH);
                sgt.setTH(t,h);
                sgt.setPosTag(tSynTree,hSynTree);

                double jarak = sgt.getSim();


                boolean predEntail;
                predEntail = (jarak>threshold);

                /*
                System.out.println("Jarak:"+jarak);
                System.out.println("PredEntail:"+predEntail);
                */
                if (isEntail == predEntail) {
                    jumPredCocok++;
                    sb.append(id+","+1);
                } else {
                    sb.append(id+","+0);
                }
                sb.append(System.getProperty("line.separator"));
            }
            System.out.println("Jumlah pred cocok:"+jumPredCocok);
            akurasiTerakhir = (double) jumPredCocok/cc;
            System.out.println("Akurasi:"+akurasiTerakhir);

            //System.out.println("Rincian");
            //System.out.println(sb.toString());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     *  - inisisasi database
     *  - inisiasi simGroupToken (yang membandingkan tingkat kemiripan antara dua token)
     *
     *
     */

    //parameter dipisahkan
    //jangan lupa panggil juga initDB
    public void init(int gloveOrW2vec, String fileVector ) {
        pp = new Prepro();
        pp.loadStopWords("stopwords","kata");
        //skor_word2vec_verb,skor_word2vec_noun,skor_word2vec_verbHnounT


        //paling akhir, karena lama
        //paragram sl999: lebih bagus pada hasil testing

        //word2vec
        //sgt = new SimGroupToken(1,"D:\\eksperimen\\textualentailment\\GoogleNews-vectors-negative300.bin.gz");
        //ParameterSimGroupToken par = new ParameterSimGroupToken();
        //hati2 ini initnya load model, bisa gede dan lama, jangan dipanggil berulang2
        //Prepro pp = new Prepro();
        //pp.loadStopWords("stopwords2","kata");
        sgt = new SimGroupToken(gloveOrW2vec,fileVector,null,pp);  //par diisi dalam loop


        //glovec
        //sgt = new SimGroupToken(0,"D:\\eksperimen\\paragram\\paragram_300_sl999\\paragram_300_sl999\\paragram_300_sl999.txt");
        //sgt = new SimGroupToken(0,"D:\\eksperimen\\paragram\\paragram_300_ws353\\paragram_300_ws353\\paragram_300_ws353.txt");
        //sgt = new SimGroupToken(0,"D:\\eksperimen\\glove\\glove.6B.300d.txt");

        //System.out.println("Menggunakan W2Vec");

        //batas terendah sebelum kena penalti
    }

    @Override
    public void finalize() {
        closeDB();
    }


    public void initDB(String vnamaTabel, String kolomTujuan) {
        akurasiTerakhir = 0;
        namaTabel = vnamaTabel;
        KoneksiDB db = new KoneksiDB();
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
    }

    public void closeDB() {
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

    /*
           loop setiap pasang T-H di tabel
           hitung skor jarak
           modifikasi: tidak tulis ke tabel tapi tulis ke file format arff

           @relation 'multiparam'

           @attribute is_Entail {TRUE,FALSE}
           @attribute data numeric

           @data
           TRUE,1,1,1,1,1,1,1

     */
    public void proses() {


        rs = null;
        try {
            rs = pSel.executeQuery();
            PrintWriter pw = new PrintWriter(fileArff);
            pw.println("@relation 'multiparam'");
            pw.println("@attribute is_Entail {true,false}");
            pw.println("@attribute data numeric");
            pw.println("@data");
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
                //System.out.println("");
                //System.out.println("id:"+id);
                //System.out.println("T:"+t);
                //System.out.println("H:"+h);
                //skor_word2vec_verb,skor_word2vec_noun,skor_word2vec_verbHnounT
                //update skor
                //double jarak = jarakMaks(t,h,tNer,hNer);

                /*
                System.out.println("T:"+t);
                System.out.println("H:"+h);
                System.out.println("IsEntail:----------->" +
                        ""+isEntail);
                */

                //isi group token
                GroupToken gtT = new GroupToken(pp);
                gtT.ambilToken(t,tNer);           //tNer dan hNer diambil dari database (sudah diproses sebelumnya)
                GroupToken gtH = new GroupToken(pp);
                gtH.ambilToken(h,hNer);

                //nanti bisa gabung pengisian variabelnya
                sgt.setGroupToken(gtT,gtH);
                sgt.setTH(t,h);
                sgt.setPosTag(tSynTree,hSynTree);
                double jarak = sgt.getSim();
                //System.out.println("Jarak:"+jarak);

                /*
                pUpd.setDouble(1, jarak);
                pUpd.setLong(2, id);
                pUpd.executeUpdate();
                */

                //TRUE,1,1,1,1,1,1,1
                pw.println(isEntail+","+jarak);
            }
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public double hitungThreshold() {
        double t = 0;
        Instances ins = null;
        try {
            ins = ConverterUtils.DataSource.read(fileArff);
            //if (ins.classIndex() == -1)
            //    ins.setClassIndex(result.numAttributes() - 1);
            ins.setClassIndex(0);
            C45Split split=new C45Split(1, 0, ins.sumOfWeights(), true);
            split.buildClassifier(ins);
            t = split.splitPoint();
            System.out.println("threshold:"+t);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return t;
    }


    public static void main(String[] args) {

        //yg 840B token saat dijalankan makan waktu 3 jam dan akhirnya kehabisan memori, test lagi nanti
        //840B tokens, 2.2M vocab, cased, 300d vectors, 2.03 GB download
        //String str840 = "D:\\eksperimen\\glove\\glove.840B.300d\\glove.840B.300d.txt";
        //6B tokens, 400K vocab, uncased, 50d, 100d, 200d, & 300d
        //String str6 ="D:\\eksperimen\\glove\\glove.6B.300d.txt";
        //42B tokens, 1.9M vocab, uncased, 300d vectors, hasil kurang
        //ambbil yang terbaik


        String glove42 = "D:\\eksperimen\\glove\\glove.42B.300d.txt";

        IsiWordUmbcVer3 iw = new IsiWordUmbcVer3();
        iw.init(0,glove42);
        //iw.initDB("rte3_babak2","skor_des_multiparam");
        //DEBUG biar cepet
        //iw.init("rte3_babak2","skor_des_multiparam ",0,str6);


        //kalau klasifikasi, proses dimatikan
        //memberikan skor untuk setiap pasangan T-H
        //loop parameternya harusnya disini
        //iw.sgt.

        //parameter harus di loop disini
        String namaFileHasil = "D:\\desertasi\\final\\eksperimen\\hasil.csv";
        try (PrintWriter pwHasil = new PrintWriter(namaFileHasil)) {
            ParameterSimGroupToken par = new ParameterSimGroupToken();
            iw.sgt.setParam(par); //idealnya tidak diakses langsung, tapi biarlah.. hehe
            double increment = 0.25;
            for (par.penaltiAngka = 0;par.penaltiAngka<=1;par.penaltiAngka = par.penaltiAngka + increment) {
                for (par.penaltiLokasi = 0;par.penaltiLokasi<=1;par.penaltiLokasi = par.penaltiLokasi + increment) {
                    for (par.penaltiTgl = 0;par.penaltiTgl<=1;par.penaltiTgl= par.penaltiTgl + increment) {
                        for (par.penaltiUang = 0;par.penaltiUang<=1;par.penaltiUang= par.penaltiUang + increment) {
                            for (par.penaltiKataVerbNoun = 0;par.penaltiKataVerbNoun<=1;par.penaltiKataVerbNoun= par.penaltiKataVerbNoun + increment) {
                                for (par.penaltiKataLain = 0;par.penaltiKataLain<=1;par.penaltiKataLain= par.penaltiKataLain + increment) {
                                    for (par.batasPenaltiKata = 0;par.batasPenaltiKata<=1;par.batasPenaltiKata= par.batasPenaltiKata + increment) {
                                        for (par.penaltiKalNeg = 0;par.penaltiKalNeg<=1;par.penaltiKalNeg= par.penaltiKalNeg + increment) {
                                            iw.initDB("rte3_babak2","skor_des_multiparam");

                                            System.out.println("-------------");

                                            System.out.println("Parameter:");
                                            System.out.println(par);
                                            System.out.println("Mulai proses jarak");
                                            iw.proses();  //dengan param yang berbeda utk setiap iterasi
                                            //FS: data tersimpan di tabel
                                            double threshold = iw.hitungThreshold();
                                            System.out.println("Threshold:"+threshold);
                                            System.out.println("Mulai proses menghitung akurasi klasifikasi");
                                            iw.klasifikasi(threshold,"rte3_test_gold");
                                            //iw.klasifikasi(threshold,par);
                                            System.out.println("-------------");
                                            System.out.println("");
                                            // panggil weka
                                            // panggil klasfikasi
                                            //debug stop dulu
                                            //System.exit(0);
                                            iw.closeDB();
                                            pwHasil.println(par.penaltiAngka+","+par.penaltiLokasi+","+
                                                    par.penaltiTgl+","+par.penaltiUang+","+
                                                    par.batasPenaltiKata+","+par.penaltiKataVerbNoun+","+
                                                    par.penaltiKataLain+","+par.batasPenaltiKata+","+
                                                    par.penaltiKalNeg+","+iw.akurasiTerakhir);
                                            pwHasil.flush();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            pwHasil.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }



        //iw.klasifikasi(0.542626,"rte3_test_gold");  //glove pargram_sl999

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
        //iw.init("rte3_babak2","juli_glove_840B",0,"D:\\eksperimen\\glove\\glove.840B.300d.txt");


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
        //iw.klasifikasi(0.776634,"rte3_test_gold"); //glove_840B  <-- berat!
        //iw.klasifikasi(0.637661,"rte3_test_gold"); //w2vec neg
        iw.closeDB();

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

