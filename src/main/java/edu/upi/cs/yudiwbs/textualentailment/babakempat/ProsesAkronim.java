package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by yudiwbs on 09/05/2016.
 *
 * disimpan dalam lowercase!
 *
 */
public class ProsesAkronim {

    Logger logger = Logger.getLogger(Prepro.class.getName());



    HashMap<String,String> hmAkronim = new HashMap<>();  //lowercase


    //constructor
    public ProsesAkronim(String namaTabel) {
       //init tabel load semua ke memori
        //memindahkan data stopwords dari tabel ke memori arraylist
        System.out.println("load akronim");
        Connection conn=null;
        PreparedStatement pSel=null;
        KoneksiDB db = new KoneksiDB();
        hmAkronim.clear();
        try {
            conn = db.getConn();
            pSel  = conn.prepareStatement (String.format("select id,term,akronim from %s",namaTabel));
            ResultSet rs = pSel.executeQuery();
            int jumDiproses = 0;
            while (rs.next())  {
                String singkat = rs.getString(2).trim();
                String panjang = rs.getString(3).trim();
                //alAkronim.add(new Akronim(singkat,panjang));
                hmAkronim.put(singkat.toLowerCase(),panjang.toLowerCase());
                //System.out.println(kata);
                jumDiproses++;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, null, e);
        }
        finally  {
            try  {
                if (pSel!= null) {pSel.close();}
                if (conn != null) {conn.close();}
            } catch (Exception e) {
                e.printStackTrace();
                logger.log(Level.SEVERE, null, e);
            }
        }
    }


    //kalau not found return null
    public String cekAdaAkronim(String pendek) {
        assert hmAkronim.size()>0;
        String out;
        out = hmAkronim.get(pendek);
        return out;
    }


    public static void main(String[] args) {
        ProsesAkronim pa = new ProsesAkronim("akronim");
        //System.out.println(pa.cekAdaAkronim("FBIII"));

        //test ada berapa akronim di DB
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
                    " from " + namaTabel + " #where id=35 #limit 10" ; // bisa ditabatasi dulu sepuluh

            pSel = conn.prepareStatement(strSel);

            rs = pSel.executeQuery();
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
                System.out.println("id:" + id);

                InfoTeks itT = pp.isiInfoTeks(t, tSynTree);
                InfoTeks itH = pp.isiInfoTeks(h, hSynTree);

                //proses disini
                //kondisi penting:
                //  ada singkatan di H tapi hanya ada panjangnya di T atau
                //  ada panjangan di H tapi hanya ada singkatan di T

                boolean adaAkronimT=false;
                boolean adaAkronimH=false;
                boolean adaPanjangT=false;
                boolean adaPanjangH=false;
                System.out.println("t:"+t);
                for (String s : itT.alNoun) {
                    String kepanjangan = pa.cekAdaAkronim(s);

                    if (kepanjangan!=null) {
                        System.out.println("Akronim:"+s);
                        System.out.println("Kepanjangan:"+kepanjangan);
                        if (itT.teksAsli.toLowerCase().contains(kepanjangan)) {
                            System.out.println("XKepanjangan ada di T!");
                        }
                        if (itH.teksAsli.toLowerCase().contains(kepanjangan)) {
                            System.out.println("XKepanjangan ada di H!");
                        }
                    }
                }

                System.out.println("h:"+h);
                for (String s : itH.alNoun) {
                    String kepanjangan = pa.cekAdaAkronim(s);
                    if (kepanjangan!=null) {
                        System.out.println("Akronim:"+s);
                        System.out.println("Kepanjangan:"+kepanjangan);
                        if (itT.teksAsli.toLowerCase().contains(kepanjangan)) {
                            System.out.println("XKepanjangan ada di T!");
                        }
                        if (itH.teksAsli.toLowerCase().contains(kepanjangan)) {
                            System.out.println("XKepanjangan ada di H!");
                        }
                    }

                }
            } //while

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
