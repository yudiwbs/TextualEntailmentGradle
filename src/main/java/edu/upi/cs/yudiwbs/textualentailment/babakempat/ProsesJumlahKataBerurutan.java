package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *   Created by yudiwbs on 01/07/2016.
 *
 *   hitung pasangan kata terpanjang
 *
 *   saya mau makan nasi
 *   saya mau makan telur: output 3, karena "saya mau makan" yang terurut
 *
 */

public class ProsesJumlahKataBerurutan {

    private class MaxStrSama {
        String maxStr;
        int pjgMax;
        public MaxStrSama(String maxStr,int pjgMax) {
            this.maxStr = maxStr;
            this.pjgMax = pjgMax;
        }
    }

    public MaxStrSama cariMaxStrSama(String t,String h) {
        Prepro pp = new Prepro();  //
        ArrayList<String> alT = pp.loadKata(t,true);
        ArrayList<String> alH = pp.loadKata(h,true);

        //loop h
        //terpanjang
        int maxPjgSama = 0;
        String strMax  = "";
        for (int i=0; i<alH.size(); i++) {
            String hh = alH.get(i);
            //System.out.println(hh);
            for (int j=0; j<alT.size(); j++) {
                String tt = alT.get(j);
                if (hh.equals(tt)) {
                    StringBuilder sbGab = new StringBuilder();
                    sbGab.append(hh);
                    sbGab.append(" ");
                    //ketemu, mulai cari sampai berapa banyak token
                    int pjgMax = 1;
                    int cc=1;
                    for (int k=j+1; k<alT.size(); k++) {
                        String tt2 = alT.get(k);
                        String hh2;
                        if (i+cc<alH.size()) {
                            hh2 = alH.get(i + cc);
                        } else {
                            break; //h habis
                        }
                        cc++;
                        if (hh2.equals(tt2)) {
                            pjgMax++;
                            sbGab.append(hh2);
                            sbGab.append(" ");
                        } else {
                            break; //sdh tk cocok
                        }
                    }
                    //System.out.println("str:"+sbGab.toString().trim());
                    //System.out.println("pjgmax:"+pjgMax);
                    if (pjgMax>maxPjgSama) {
                        maxPjgSama = pjgMax;
                        strMax = sbGab.toString().trim();
                    }
                } //equals
            } //loop j
        } //loop i
        //System.out.println("str:"+strMax);
        //System.out.println("pjgmax:"+maxPjgSama);
        return new MaxStrSama(strMax,maxPjgSama);
    }

    public void proses() {
            String fileNeg =  "D:\\desertasi\\eksperimen\\kata_neg.txt";
            String fileTH  =  "D:\\desertasi\\eksperimen\\id_t_h.txt";

            ProsesJumlahKataBerurutan pj = new ProsesJumlahKataBerurutan();


            //load id, t,h
            try {
                Scanner sc = new Scanner(new File(fileTH));
                int cc = 0;
                while (sc.hasNextLine()) {
                    String id = sc.nextLine();
                    String t  = sc.nextLine();
                    String h  = sc.nextLine();
                    cc++;
                    //debug
                    /*System.out.println("id:"+id);
                    System.out.println("t:"+t);
                    System.out.println("h:"+h);
                    MaxStrSama m = pj.cariMaxStrSama(t,h);
                    System.out.println("maxstr:"+m.maxStr);
                    System.out.println("pjg:"+m.pjgMax);
                  /  */
                    MaxStrSama m = pj.cariMaxStrSama(t,h);
                    System.out.println(m.pjgMax);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
    }

    public static void main(String[] args) {
        String t = "saya mau makan nasi goreng yang banyak";
        String h = "saya mau makan telur";
        ProsesJumlahKataBerurutan pj = new ProsesJumlahKataBerurutan();
        pj.proses();
        /*
        MaxStrSama m = pj.cariMaxStrSama(t,h);
        System.out.println("maxstr:"+m.maxStr);
        System.out.println("pjg:"+m.pjgMax);
        */
    } //main
}
