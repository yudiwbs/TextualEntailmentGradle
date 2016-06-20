package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by yudiwbs on 16/06/2016.
 */
public class ProsesKalimatNegatif {

    //id,umbc_paragram_fixtgl,cocok_obyek_syntaxnet,isEntail
    private class Item {
        String id;
        String t;
        String h;

        public Item(String id, String t, String h) {
            this.id = id;
            this.t = t;
            this.h = h;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("id:"+id);
            sb.append(System.lineSeparator());
            sb.append("T:"+t);
            sb.append(System.lineSeparator());
            sb.append("H:"+h);
            sb.append(System.lineSeparator());
            return sb.toString();
        }

    }

    ArrayList<Item> arrItem = new ArrayList<>();
    ArrayList<String> arrKataNeg = new ArrayList<>();

    public void proses() {
        String fileNeg =  "D:\\desertasi\\eksperimen\\kata_neg.txt";
        String fileTH  =  "D:\\desertasi\\eksperimen\\id_t_h.txt";

        //looad id, t,h
        try {
            Scanner sc = new Scanner(new File(fileTH));
            int cc = 0;
            while (sc.hasNextLine()) {
                String id = sc.nextLine();
                String t  = sc.nextLine();
                String h  = sc.nextLine();
                Item itm = new Item(id,t,h);
                arrItem.add(itm);
                cc++;
                //System.out.println(itm);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //load kata negatif
        try {
            Scanner sc = new Scanner(new File(fileNeg));
            int cc = 0;
            while (sc.hasNextLine()) {
                String kataNeg = sc.nextLine();
                cc++;
                if (!kataNeg.trim().equals("")) { //cegah baris kosong masuk
                    arrKataNeg.add(kataNeg);
                }
                //System.out.println(kataNeg);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        //proses

        for (Item itm: arrItem) {
            boolean isTNeg = false;
            boolean isHNeg = false;
            for (String kataNeg:arrKataNeg) {
                if (itm.t.contains(kataNeg)) {
                    isTNeg = true;
                    break;
                }
            }
            for (String kataNeg:arrKataNeg) {
                if (itm.h.contains(kataNeg)) {
                    isHNeg = true;
                    break;
                }
            }

            //debug
            /*
            if (isTNeg || isHNeg) {
                System.out.println("t neg:"+isTNeg);
                System.out.println("h neg:"+isHNeg);
                System.out.println(itm);
            }
            */

            //untuk weka
            //tneg, hneg
            boolean xorTH = isTNeg ^ isHNeg;

            System.out.println(isTNeg+","+isHNeg+","+xorTH);
        }


    }

    public static void main(String[] args) {
        ProsesKalimatNegatif pkn = new ProsesKalimatNegatif();
        pkn.proses();;
    }
}
