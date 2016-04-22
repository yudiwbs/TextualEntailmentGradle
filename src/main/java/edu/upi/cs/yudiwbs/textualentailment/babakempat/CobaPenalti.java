package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by yudiwbs on 13/04/2016.
 * testing berbagai skenario penalti
 */

public class CobaPenalti {
    public String namaTabel;
    private Connection conn = null;
    private PreparedStatement pUpd = null;
    private PreparedStatement pSel = null;

    ResultSet rs = null;

    public void init(String vnamaTabel, String kolomTujuan) {
        namaTabel = vnamaTabel;
        KoneksiDB db = new KoneksiDB();
        try {
            conn = db.getConn();
            //jika sudah ada isi lagi (dikomentari yg bagian is null)
            String strSel = "select id,t,h,umbc_glove_fixdate " +
                    " from " + namaTabel + " #limit 10" ; //ditabatasi dulu sepuluh



            pSel = conn.prepareStatement(strSel);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void finalize() {
        close();
    }

    public void close() {
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

    public void cariMiripTapiNotEntail() {

    }

    public static void main(String[] args) {
        CobaPenalti cp = new CobaPenalti();
        cp.cariMiripTapiNotEntail();
    }
}
