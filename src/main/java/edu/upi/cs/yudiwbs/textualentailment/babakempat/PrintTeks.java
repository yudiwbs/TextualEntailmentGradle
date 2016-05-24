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

    public static void main(String[] args) {
        Connection conn;
        PreparedStatement pSel;
        String strSel;

        //set nama tabel langsung di query
        //strSel       = String.format("select id,t,h from rte3_babak3");
        //strUpdate    = String.format("update rte3_babak3 set h_normal=?, t_normal=? where id=?");

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
                //System.out.println("id"+id);
                ArrayList<String> alSubKal = pp.splitKalimat(h); //t atau h
                //System.out.println("id:"+id);
                for (String subKal:alSubKal) {
                    System.out.println(subKal);
                }
                //System.out.println("H:");
                //System.out.println(h);
                //System.out.println("isEntail:"+isEntail);
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


}
