package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by yudiwbs on 10/06/2016.
 *
 *  memproses csv file, lalu menggunakan rule mengeluarkan
 *  prediksi
 *
 *
 */

public class ProsesKlasifikasi {

    private class Classifier {


        public boolean klasifikasi(Item itm) {
                /*
                umbc_paragram_fixtgl <= 0.542626: notentail (275.0/61.0)
                umbc_paragram_fixtgl > 0.542626
                |   umbc_paragram_fixtgl <= 0.774608
                |   |   cocok_obyek_syntaxnet <= 0.25: notentail (18.9/1.95)
                |   |   cocok_obyek_syntaxnet > 0.25: entail (160.1/69.05)
                |   umbc_paragram_fixtgl > 0.774608: entail (346.0/87.0)
                 */
            boolean out = false;
            if (!itm.str_umbc_paragram_fixtgl.equals("?")) {
                double umbc_paragram_fixtgl = Double.parseDouble(itm.str_umbc_paragram_fixtgl);
                if (umbc_paragram_fixtgl<=0.542626) {
                    return false;
                } else if (umbc_paragram_fixtgl>0.774608) {
                    return true;
                } else  {
                    if (!itm.str_cocok_obyek_syntaxnet.equals("?")) {
                        double cocok_obyek_syntaxnet = Double.parseDouble(itm.str_cocok_obyek_syntaxnet);
                        if (cocok_obyek_syntaxnet<=0.25) {
                            return false;
                        } else  {
                            return true;
                        }
                    }
                }
            }

            return out;
        }

    }

    //id,umbc_paragram_fixtgl,cocok_obyek_syntaxnet,isEntail
    private class Item {
        String id;
        String t;
        String h;
        String  str_umbc_paragram_fixtgl;
        String  str_cocok_obyek_syntaxnet;
        String  str_isEntail;

        public boolean getEntail() {
            if (str_isEntail.equals("entail")) {
                return true;
            } else {
                return false;
            }
        }


        public void parse(String line) {
            //id,umbc_paragram_fixtgl,cocok_obyek_syntaxnet,isEntail
            //input: 789,-0.06772219212,?,notentail
            String[] data = line.split(",");
            id = data[0];
            str_umbc_paragram_fixtgl  = data[1];
            str_cocok_obyek_syntaxnet = data[2];
            str_isEntail = data[3];
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
            sb.append("umbc_paragram_fixtgl:"+str_umbc_paragram_fixtgl);
            sb.append(System.lineSeparator());
            sb.append("cocok_obyek_syntaxnet:"+str_cocok_obyek_syntaxnet);
            sb.append(System.lineSeparator());
            sb.append("isEntail:"+str_isEntail);
            sb.append(System.lineSeparator());
            return sb.toString();
        }

    }

    ArrayList<Item> arrItem = new ArrayList<>();

    //arrItem terisi
    public void load() {
        //csv yang akan diproses
        //kolom pertama
        String fileCsv = "D:\\desertasi\\eksperimen\\Data rte3 - train_terbaik_10jun.csv";
        String fileTH =  "D:\\desertasi\\eksperimen\\id_t_h.txt";

        try {
            Scanner sc = new Scanner(new File(fileCsv));
            //skip header
            if (sc.hasNextLine()) {
                sc.nextLine();
            }
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                //System.out.println(line);
                Item it = new Item();
                it.parse(line);
                arrItem.add(it);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //ambil data t-h
        //dihasilkan oleh PrintTeks.printIdTH
        /*
        id
        t
        h
        id
        t
        h
        */


        try {
            Scanner sc = new Scanner(new File(fileTH));
            //asumsi urut
            int cc = 0;
            while (sc.hasNextLine()) {
                String id = sc.nextLine();
                String t  = sc.nextLine();
                String h  = sc.nextLine();
                if (arrItem.get(cc).id.equals(id)) { //pengaman aja, harusnya sama
                    arrItem.get(cc).t = t;
                    arrItem.get(cc).h = h;
                } else {
                    System.out.println("error!");
                }
                //System.out.println(arrItem.get(cc));
                cc++;
                //System.out.println("id:"+id);
                //System.out.println("t:"+t);
                //System.out.println("h:"+h);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void proses() {
        //load sudah dipanggil arrItem terisi
        Classifier c = new Classifier();
        int predBenar=0;
        int predSalah=0;
        for (Item itm:arrItem) {
            boolean pred = c.klasifikasi(itm);
            if (pred == itm.getEntail()) {
                predBenar++;
            } else {
                predSalah++;
                System.out.println(itm);
            }
            // /System.out.println(arrItem.get(cc));
        }
        System.out.println("Jumlah prediksi benar:"+predBenar);
        System.out.println("Jumlah prediksi salah:"+predSalah);
        double akurasi = (double) predBenar / (predBenar+predSalah);
        System.out.println("Akurasi:"+akurasi);
    }

    public static void main(String[] args) {
        ProsesKlasifikasi pk = new ProsesKlasifikasi();
        pk.load();
        pk.proses();
    }
}
