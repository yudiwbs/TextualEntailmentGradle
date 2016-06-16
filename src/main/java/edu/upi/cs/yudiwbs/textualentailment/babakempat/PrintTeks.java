package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by yudiwbs on 13/05/2016.
 *
 * untuk keperluan pemindahan isi db ke file teks
 *
 */
public class PrintTeks {


    //cetak H, tapi lengkap tidak dipisah berdasarkan subkalimat
    //asumsinya H hanya terdiri atas satu kalimat
    //tambah spasi antar H untuk jadi input di syntaxnet
    public static void printHPar() {
        Connection conn;
        PreparedStatement pSel;
        String strSel;

        //data train
        //strSel       = String.format("select id,t,h,isEntail from rte3_babak2 order by id");

        //data test
        strSel         = String.format("select id,t,h,isEntail from rte3_test_gold order by id");

        System.out.println(strSel);

        KoneksiDB db = new KoneksiDB();
        String kata;
        //System.out.println("Mulai");
        try {
            conn = db.getConn();
            pSel  =  conn.prepareStatement (strSel);
            //loop untuk semua instance
            ResultSet rs = pSel.executeQuery();
            while (rs.next()) {
                long id = rs.getLong(1);
                //String t = rs.getString(2);
                String h = rs.getString(3);
                boolean isEntail = rs.getBoolean(4);
                System.out.println(h);                 //print h
                System.out.println();
            }
            rs.close();
            pSel.close();
            conn.close();
        } catch (Exception e) {
            //log.log(Level.SEVERE,e.getMessage(),e);
            e.printStackTrace();
        }
        System.out.println("selesai ...");
        //log.log(Level.INFO,"Selesai");
    }

    //cetak T, tapi lengkap tidak dipisah berdasarkan subkalimat
    public static void printTPar() {
        Connection conn;
        PreparedStatement pSel;
        String strSel;

        //data train
        //strSel       = String.format("select id,t,h,isEntail from rte3_babak2 order by id");

        //data test
        strSel         = String.format("select id,t,h,isEntail from rte3_test_gold order by id");

        System.out.println(strSel);

        KoneksiDB db = new KoneksiDB();
        String kata;
        //System.out.println("Mulai");
        try {
            conn = db.getConn();
            pSel  =  conn.prepareStatement (strSel);
            //loop untuk semua instance
            ResultSet rs = pSel.executeQuery();
            while (rs.next()) {
                long id = rs.getLong(1);
                String t = rs.getString(2);
                //String h = rs.getString(3);
                boolean isEntail = rs.getBoolean(4);
                System.out.println(t);                 //print t
            }
            rs.close();
            pSel.close();
            conn.close();
        } catch (Exception e) {
            //log.log(Level.SEVERE,e.getMessage(),e);
            e.printStackTrace();
        }
        System.out.println("selesai ...");
        //log.log(Level.INFO,"Selesai");
    }

    public static void printEntail() {
        Connection conn;
        PreparedStatement pSel;
        String strSel;
        strSel       = String.format("select id,isEntail from rte3_babak2 order by id");
        System.out.println(strSel);
        KoneksiDB db = new KoneksiDB();
        String kata;
        try {
            conn = db.getConn();
            pSel  =  conn.prepareStatement (strSel);
            //loop untuk semua instance
            ResultSet rs = pSel.executeQuery();
            while (rs.next()) {
                long id = rs.getLong(1);
                boolean isEntail = rs.getBoolean(2);
                System.out.println(isEntail);
            }
            rs.close();
            pSel.close();
            conn.close();
        } catch (Exception e) {
            //log.log(Level.SEVERE,e.getMessage(),e);
            e.printStackTrace();
        }
    }

    /*
       tiga baris
       id
       t
       h
       id
       t
       h
     */

    public static void printIdTH() {
        Connection conn;
        PreparedStatement pSel;
        String strSel;
        strSel       = String.format("select id,t,h  from rte3_babak2 order by id");
        System.out.println(strSel);
        KoneksiDB db = new KoneksiDB();
        String kata;
        try {
            conn = db.getConn();
            pSel  =  conn.prepareStatement (strSel);
            //loop untuk semua instance
            ResultSet rs = pSel.executeQuery();
            Prepro pp = new Prepro();
            pp.initSplitKalimat();
            while (rs.next()) {
                long id = rs.getLong(1);
                String t = rs.getString(2);
                String h = rs.getString(3);
                System.out.println(id);
                System.out.println(t);
                System.out.println(h);
            }
            rs.close();
            pSel.close();
            conn.close();
        } catch (Exception e) {
            //log.log(Level.SEVERE,e.getMessage(),e);
            e.printStackTrace();
        }
    }

    //split h jadi subkalimat
    //untuk kemudian diproses. Lihat class ParsingSyntaxNet
    public static void printTHentail() {

        Connection conn;
        PreparedStatement pSel;
        String strSel;


        strSel       = String.format("select id,t,h,isEntail from rte3_babak2 order by id");
        System.out.println(strSel);

        KoneksiDB db = new KoneksiDB();
        String kata;
        System.out.println("Mulai");
        try {
            conn = db.getConn();
            pSel  =  conn.prepareStatement (strSel);
            //loop untuk semua instance
            ResultSet rs = pSel.executeQuery();

            Prepro pp = new Prepro();
            pp.initSplitKalimat();
            while (rs.next()) {
                long id = rs.getLong(1);
                String t = rs.getString(2);
                String h = rs.getString(3);
                boolean isEntail = rs.getBoolean(4);
                System.out.println("id"+id);
                ArrayList<String> alSubKalT = pp.splitKalimat(t);
                System.out.println("T:"+id);
                for (String subKal:alSubKalT) {
                    System.out.println(subKal);
                }
                System.out.println("H:");
                ArrayList<String> alSubKalH = pp.splitKalimat(h);
                for (String subKal:alSubKalH) {
                    System.out.println(subKal);
                }
                System.out.println("isEntail:"+isEntail);
                System.out.println(" ");
            }
            rs.close();
            pSel.close();
            conn.close();
        } catch (Exception e) {
            //log.log(Level.SEVERE,e.getMessage(),e);

            e.printStackTrace();
        }
        System.out.println("selesai ...");
        //log.log(Level.INFO,"Selesai");
    }

    public static void printTSyntaxNet() {
    //untuk keperluan diproses syntaxnet (lihat class ParsingSyntaxNet)
    //split T jadi subkalimat (T umumnya terdiri atas beberapa kalimat)
    //harus displit karena hasil dep tree jadi salah jika tidak displit
    //antar T dipisahkan dengan enter kosong
    //Selanjutnya class ParsingSyntaxNet

        Connection conn;
        PreparedStatement pSel;
        String strSel;

        //data train
        //strSel       = String.format("select id,t,h,isEntail from rte3_babak2 order by id");

        //data test
        strSel  = String.format("select id,t,h,isEntail from rte3_test_gold order by id");


        System.out.println(strSel);

        KoneksiDB db = new KoneksiDB();
        String kata;
        //System.out.println("Mulai");
        try {
            conn = db.getConn();
            pSel  =  conn.prepareStatement (strSel);
            //loop untuk semua instance
            ResultSet rs = pSel.executeQuery();

            Prepro pp = new Prepro();
            pp.initSplitKalimat();
            while (rs.next()) {
                long id = rs.getLong(1);
                String t = rs.getString(2);
                //String h = rs.getString(3);
                boolean isEntail = rs.getBoolean(4);
                //System.out.println("id"+id);
                ArrayList<String> alSubKal = pp.splitKalimat(t); //t atau h
                //System.out.println("id:"+id);
                for (String subKal:alSubKal) {
                    System.out.println(subKal);
                }
                System.out.println(" ");
            }
            rs.close();
            pSel.close();
            conn.close();
        } catch (Exception e) {
            //log.log(Level.SEVERE,e.getMessage(),e);

            e.printStackTrace();
        }
        //System.out.println("selesai ...");
        //log.log(Level.INFO,"Selesai");
    }

    public static void main(String[] args) {
        printIdTH();
        //printEntail();
        //printTHentail();
        //printTPar();
        //printHPar();
        //printTSyntaxNet();
    }


}
