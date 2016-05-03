package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by yudiwbs on 02/05/2016.
 */
public class ProsesIsA {


    public static void main(String[] args) {
        KoneksiDB db = new KoneksiDB();
        Connection conn = null;
        PreparedStatement pSel = null;
        String namaTabel = "rte3_babak2";
        ResultSet rs=null;
        Prepro pp = new Prepro();
        //debug
        try {
            conn = db.getConn();
            //jika sudah ada isi lagi (dikomentari yg bagian is null)
            String strSel = "select id,t,h, t_gram_structure, " +
                    "h_gram_structure,t_ner, h_ner, isEntail " +
                    " from " + namaTabel + " #limit 10" ; //ditabatasi dulu sepuluh

            pSel = conn.prepareStatement(strSel);

            rs = pSel.executeQuery();
            ProsesIsA pia = new ProsesIsA();
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
                System.out.println("id:"+id);

                //proses disini
                



                //perlu ngga
                InfoTeks itT = pp.isiInfoTeks(t,tSynTree);
                InfoTeks itH = pp.isiInfoTeks(h,hSynTree);


            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            pSel.close();
            rs.close();
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
