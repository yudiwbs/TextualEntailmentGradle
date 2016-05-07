package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Created by yudiwbs on 03/05/2016.
 *
 */

public class ProsesPasif {

    public static void main(String[] args) {

        Connection conn;
        PreparedStatement pSel;
        PreparedStatement pUpdate;
        String strSel;
        String strUpdate;

        //set nama tabel langsung di query
        //strSel       = String.format("select id,t,h from rte3_babak3");
        //strUpdate    = String.format("update rte3_babak3 set h_normal=?, t_normal=? where id=?");

        strSel       = String.format("select id,t,h from rte3_test_normal");
        strUpdate    = String.format("update rte3_test_normal set h_normal=?, t_normal=? where id=?");


        System.out.println(strSel);
        System.out.println(strUpdate);

        KoneksiDB db = new KoneksiDB();
        String kata;
        System.out.println("Mulai");
        try {
            conn = db.getConn();
            pSel  =  conn.prepareStatement (strSel);
            pUpdate = conn.prepareStatement(strUpdate);

            //loop untuk semua instance
            ResultSet rs = pSel.executeQuery();

            Prepro pp = new Prepro();
            pp.initSplitKalimat();

            while (rs.next()) {
                long id = rs.getLong(1);
                String t = rs.getString(2);
                String h = rs.getString(3);
                System.out.println("id:"+id);
                System.out.println("h:"+h);
                //String sL = lemmatize(s);
                String hasilH;
                String hasilT;

                //proses h
                //jika mengandung is/was/were located atau is/was/were based diskip
                //hati2 masalah spasi belum ditangani, misal "is   located"  --> harusnya regex
                if ( h.contains("is located") || h.contains("was located") ||h.contains("were located") ||
                     h.contains("is based")   || h.contains("was based")   ||h.contains("were based")
                   )
                {
                    hasilH = h;  //tidak berubah
                } else
                {
                    summarySentence stc = new summarySentence(h);
                    stc.createDependencyRelations();
                    stc.toActive();
                    hasilH = stc.toString();
                }
                //System.out.println(stc.toString());

                //update h dulu
                //strUpdate    = String.format("update rte3_babak3 set h_normal=? where id=?");

                //proses T
                ArrayList<String> alKal = pp.splitKalimat(t);
                StringBuilder sb = new StringBuilder();
                for (String kal:alKal ) {
                    String hasilKal;
                    if ( kal.contains("is located")  || kal.contains("was located")|| kal.contains("were located")
                         || kal.contains("is based") || kal.contains("was based")  || kal.contains("were based")
                        )
                    {
                        hasilKal = kal;  //tidak berubah
                    } else
                    {
                        System.out.println("kalimat di T:"+kal);
                        summarySentence stc = new summarySentence(kal);
                        stc.createDependencyRelations();
                        stc.toActive();
                        hasilKal = stc.toString();
                    }
                    sb.append(hasilKal+" "); //gabung lagi kalimatnya
                }
                hasilT =  sb.toString();

                pUpdate.setString(1,hasilH);
                pUpdate.setString(2,hasilT);
                pUpdate.setLong(3, id);
                pUpdate.executeUpdate();
            }
            rs.close();
            pUpdate.close();
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
