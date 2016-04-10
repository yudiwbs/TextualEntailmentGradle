package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yudiwbs on 07/04/2016.
 *
 * iseng bandingkan NER dengan Natty, dugaan saya bagusan natty
 *
 * ternyata natty lebih jelek dari NER-nya stanford... digunakan untuk parsing saja setelah
 * diidentifikasi NER
 *
 */

public class CobaBandingkanNattyNER {

        public String namaTabel = "rte3_babak2";
        private Connection conn = null;
        private PreparedStatement pSel = null;

        ResultSet rs = null;
        Prepro pp;
        Parser par;

        public void init() {
            par = new Parser();

            KoneksiDB db = new KoneksiDB();
            pp = new Prepro();
            try {
                conn = db.getConn();
                //jika sudah ada isi lagi (dikomentari yg bagian is null)
                String strSel = "select id,t,h, t_ner, h_ner " +
                        " from " + namaTabel + " limit 10 " ;
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


        public void proses() {
            rs = null;
            try {
                rs = pSel.executeQuery();
                while (rs.next()) {
                    //String strSel = "select id,t,h, t_ner, h_ner "
                    int id = rs.getInt(1);
                    String t = rs.getString(2);
                    String h = rs.getString(3);
                    String tNer = rs.getString(4);
                    String hNer = rs.getString(5);

                    System.out.println("id:"+id);
                    System.out.println("T:"+t);
                    System.out.println("H:"+h);
                    //System.out.println("tNer:"+tNer);
                    //System.out.println("hNer:"+hNer);

                    HashMap<String,ArrayList<String>> hmTNer = pp.ambilInfoNer(tNer);
                    System.out.println("Tgl NER T");
                    for (String key : hmTNer.keySet()) {
                        //filter key yg diinginkan
                        if (key.equals("DATE")) {
                            //ambil datanya
                            ArrayList<String> alData = hmTNer.get(key);
                            if (alData!=null) {
                                for (String s : alData) {
                                    System.out.println(s);
                                }
                            }
                        }
                    }

                    System.out.println("Tgl Parser T");
                    List<DateGroup> groups = par.parse(t);
                    for(DateGroup group:groups) {
                        List<Date> dates = group.getDates();
                        for (Date d : dates) {
                            System.out.println(d);
                        }
                        String matchingValue = group.getText();
                        System.out.println("matchingValue:" + matchingValue);
                    }

                    HashMap<String,ArrayList<String>> hmHNer = pp.ambilInfoNer(hNer);
                    System.out.println("Tgl NER H");
                    for (String key : hmTNer.keySet()) {
                        //filter key yg diinginkan
                        if (key.equals("DATE")) {
                            //ambil datanya
                            ArrayList<String> alData = hmHNer.get(key);
                            if (alData!=null) {
                                for (String s : alData) {
                                    System.out.println(s);
                                }
                            }
                        }
                    }

                    System.out.println("Tgl Parser H");
                    groups = par.parse(h);
                    for(DateGroup group:groups) {
                        List<Date> dates = group.getDates();
                        for (Date d : dates) {
                            System.out.println(d);
                        }
                        String matchingValue = group.getText();
                        System.out.println("matchingValue:" + matchingValue);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

    public static void main(String[] args) {
        CobaBandingkanNattyNER cb = new CobaBandingkanNattyNER();
        cb.init();
        cb.proses();
        cb.close();
    }


    }
