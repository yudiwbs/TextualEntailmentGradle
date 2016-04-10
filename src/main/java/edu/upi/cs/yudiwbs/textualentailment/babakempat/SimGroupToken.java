package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import com.mdimension.jchronic.Chronic;
import com.mdimension.jchronic.Options;
import com.mdimension.jchronic.utils.Span;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;

import java.io.File;
import java.util.Calendar;

/**
 * Created by yudiwbs on 09/04/2016.
 * menghitung jarak/similarity antar dua grup token
 */

public class SimGroupToken {
    WordVectors vec  = null;
    GroupToken gtT=null;
    GroupToken gtH=null;
    String posTagT;
    String posTagH;

    Options optTime;

    public  void setGroupToken(GroupToken gtT,GroupToken gtH) {
        this.gtT = gtT;
        this.gtH = gtH;
    }

    public void setPosTag(String posTagT, String posTagH) {
        this.posTagT = posTagT;
        this.posTagH = posTagH;
    }

    public SimGroupToken(String fileVec) {
        optTime = new Options();
        optTime.setCompatibilityMode(true); //untuk apa??
        //opt.setGuess(true); //untuk apa??

        //load vector
        try {
            System.out.println("Mulai Load, agak lama ...");
            //vec = WordVectorSerializer.loadGoogleModel(gModel, true);
            vec = WordVectorSerializer.loadTxtVectors(new File(fileVec));
            System.out.println("Load selesai... ");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //hitung sim antara gtH dan gtT, gtH dan gtT sudah diisi
    //proses kata per kata
    public double getSim(){
        assert (gtH!=null && gtT!=null);
        double out = 0;
        double totalSkor = 0;  //ditambah
        //hitung penalti bagi yg skornya rendah
        double batasPenalti = 0.25; //<= 0.2 masuk jadi penalti
        double totalPenalti  = 0;

        //penalti *belum* memperhitunkan bobot 1/log (freq) spt di paper


        //cari mulai dari H

        //uang

        if (gtH.tokenUang.size()>0) {
            for (String sH: gtH.tokenUang) {
                System.out.println("Cari uang:"+sH);
                boolean ketemuCocok = false;
                for (String sT: gtT.tokenUang) {
                    if (sH.equals(sT)) {
                        ketemuCocok = true;
                        totalSkor++;;
                        System.out.println(sH+"->"+sT);
                        break; //proses sH berikutnya
                    }
                }
                if (!ketemuCocok) { //kena penalti
                    double penalti = 0.5;
                    totalPenalti = totalPenalti+penalti;
                    System.out.println("Tdk ada pasangan, kena penalti:"+sH+"("+penalti+")");
                }
            }
        }

        //angka
        if (gtH.tokenAngka.size()>0) {
            for (String sH: gtH.tokenAngka) {
                System.out.println("Cari angka:"+sH);
                boolean ketemuCocok = false;
                for (String sT: gtT.tokenAngka) {
                    if (sH.equals(sT)) {
                        ketemuCocok = true;
                        totalSkor++;;
                        System.out.println(sH+"->"+sT);
                        break; //proses sH berikutnya
                    }
                }
                if (!ketemuCocok) { //kena penalti
                    double penalti = 0.5;
                    totalPenalti = totalPenalti+penalti;
                    System.out.println("Tdk ada pasangan, kena penalti:"+sH+"("+penalti+")");
                }
            }
        }

        //tanggal
        if (gtH.tokenTgl.size()>0) {
            for (String sH: gtH.tokenTgl) {
                boolean ketemuCocok = false;
                System.out.println("Cari tgl:"+sH);
                Span timeH = Chronic.parse(sH, optTime);
                if (timeH!=null) {
                    Calendar calH = timeH.getBeginCalendar();
                    System.out.println("" + calH.getTime());
                    for (String sT : gtT.tokenTgl) {
                        //konversi tgl
                        Span timeT = Chronic.parse(sT, optTime);
                        if (timeT != null) {
                            Calendar calT = timeT.getBeginCalendar();
                            System.out.println("" + calT.getTime());
                            if (calH.equals(calT)) {
                                ketemuCocok = true;
                                totalSkor++;
                                System.out.println(sH + "->" + sT);
                                break; //proses sH berikutnya
                            }
                        } else {
                            //ERROR, tgl tidak bisa diparsing!
                            System.out.println("Tgl tidak bisa diparsing!:" + sT);
                        }
                    }   //for sT
                } //if timeH<>null
                else {
                    System.out.println("Tgl tidak bisa diparsing!:" + sH);
                }
                if (!ketemuCocok) { //kena penalti
                    double penalti = 0.5;
                    totalPenalti = totalPenalti+penalti;
                    System.out.println("Tdk ada pasangan, kena penalti:"+sH+"("+penalti+")");
                }
            }
        }



        //sisanya (kata)
        //perlu dicari nilai masikum
        for (String vH:gtH.tokenKata) {
            double maxSkor = 0; //makin besar makin bagus, makin similar
            String strTercocok="";
            //loop untuk setiap T
            for (String vT:gtT.tokenKata) {
                if (vH.equals(vT)) {  //sama persis
                    maxSkor=1;
                    strTercocok = vT;
                    //System.out.println(vH+"->"+vT);
                    break; //tidak perlu diproses berikutnya
                } else
                    //bagian akronim UN dgn United atau Nations = 1 (belum)
                    //dua term berurutan cocok dengan single term (long term = long-term) (belum)
                    //simLSA, kalau disini diganti dengan w2vec atau glove
                    //booster wordnet
                    //tdk ada di vocab: karakter bigram, dice cooeficent, jika > 2/3 maka 1 else 0

                    //I,we, they,she  jika sama dgn obj pronoun (me, him, her) = 1
                    if (    (vH.equals("i")&&vT.equals("me"))    ||
                            (vH.equals("she")&&vT.equals("her"))  ||
                            (vH.equals("he")&&vT.equals("him"))   ||
                            (vH.equals("they")&&vT.equals("them"))   ||
                            (vH.equals("he")&&vT.equals("him"))
                            ) {
                        maxSkor=1;
                        strTercocok = vT;
                        break;
                        //System.out.println(vH+"->"+vT);
                    } else {
                        //System.out.println("Proses Glove (TBD)");
                        //cek skor kemiripan GLOVE
                        double skorEm = vec.similarity(vH,vT);
                        if (skorEm > maxSkor) {
                            maxSkor = skorEm;
                            strTercocok = vT;
                        }
                        //TBD
                        //jika melebihi thresold tertentu tambah boosting wordnet
                    }
            } //loop for vT
            System.out.print(vH +"-> "+strTercocok+" ");
            System.out.println("("+maxSkor+")");

            //terlalu rendah, kena penalti
            if (maxSkor<=batasPenalti) {
                //
            }


            totalSkor = totalSkor + maxSkor;
        }

        int jumToken = gtH.tokenUang.size()+gtH.tokenTgl.size()+gtH.tokenAngka.size()+gtH.tokenKata.size();
        out = totalSkor / (jumToken); //tidak perlu dikali 2 karena hanya dari H->T (tidak bolakbalik)
        return out;
    }

    public static void main(String[] args) {
        //debug


        String t = "After his release, the clean-shaven Magdy el-Nashar told reporters outside his home that he had " +
                "nothing to do with the July 7 transit attacks, which killed 52 people and the four bombers.";
        String tNer = "PERSON=Magdy el-Nashar;DATE=July 7;NUMBER=52;NUMBER=four;";
        String h = "52 people and four bombers were killed on July 7.";
        String hNer = "NUMBER=52;NUMBER=four;DATE=July 7;";


        /*
        String t = "Take consumer products giant Procter and Gamble. Even with a $1.8 billion Research and Development " +
                "budget, it still manages 500 active partnerships each year, many of them with small companies.";
        String h = "500 small companies are partners of Procter and Gamble.";
        */
        GroupToken gtT = new GroupToken();
        gtT.ambilToken(t,tNer);

        GroupToken gtH = new GroupToken();
        gtH.ambilToken(h,hNer);

        SimGroupToken sgt = new SimGroupToken("D:\\eksperimen\\glove\\glove.6B.300d.txt");
        sgt.setGroupToken(gtT,gtH);
        double skor = sgt.getSim();
        System.out.println("skor:"+skor);
    }

}
