package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created by yudiwbs on 23/06/2016.
 *
 * cari kata di H yang memiliki data di PPDB
 *
 * saya mau makan => saya, mau, makan, saya mau, mau makan dst.. sampai span 4 kata
 *
 */


public class ProsesPPDBHypothesis {

    Connection conn=null;
    PreparedStatement pSel=null;
    Prepro pp = null;

    /*
        proses ambil nilai h, ambil frasa, ambil sinonimnya
        lalu ganti kata2 t yg terkait dengan sinonim
    */
    public String gantiSinonimPPDB(String t,String h) {
        String out = "";
        assert conn!=null;
        assert pSel!=null;
        assert pp!=null;


        String tPengganti = new String(t);


        //proses h
        //buang titik di akhir kalimat
        h = pp.buangTitikDiAkhir(h);


        ArrayList<String>  alFrasa =  new ArrayList<>();
        ArrayList<String>  alKata  =  pp.loadKata(h,true);

        //bentuk window  yg terisi 2 kata, 3 kata dan 4 kata
        //satu kata tidak perlu diproses karena sudah dihandle di umbc

        //for (int jumKata=2; jumKata<=4; jumKata++) {
        //cobain dengan satu kata
        for (int jumKata=1; jumKata<=4; jumKata++) {
            for (int j=0; j<=alKata.size();j++) {
                StringBuilder sb = new StringBuilder();
                int k = j;
                int kataAmbil = 0;
                while (k<alKata.size() && kataAmbil <jumKata ) {
                    sb.append(alKata.get(k));
                    sb.append(" ");
                    k++;
                    kataAmbil++;
                }
                String frasa = sb.toString().trim();
                if (!frasa.equals("")&&!alFrasa.contains(frasa)&&frasa.contains(" ")) {
                    alFrasa.add(frasa);  //hilangkan duplikasi
                }
            }
        }

        //frasa sudah berisi (dan unik)
        //urutkan dari kecil ke besar

        alFrasa.sort((s1, s2) -> Integer.compare(s1.length(),s2.length()));

        //debug
        //alFrasa.forEach(System.out::println);

        //cek apakah overlap
        /*
        ArrayList<String> alFrasa2 = new ArrayList<>();
        for (int i=0;i<alFrasa.size();i++) {
            boolean isKetemu=false;
            for (int j=i+1;j<alFrasa.size();j++) {
                if (alFrasa.get(j).contains(alFrasa.get(i))) {
                    isKetemu = true;
                    break;
                }
            }
            if (!isKetemu) {
                alFrasa2.add(alFrasa.get(i));
            }
        }
        */

        //debug
        //alFrasa2.forEach(System.out::println);

        //alFrasa = alFrasa2;

        //for (String fr: alFrasa) {
        for (int i=alFrasa.size()-1;i>=0;i--) {
            String fr = alFrasa.get(i);
            ArrayList<String> alSinonim = new ArrayList<>();
            //cari di database ppdb
            try {
                pSel.setString(1,fr);
                ResultSet rs = pSel.executeQuery();
                while (rs.next()) {
                    int idTbl   = rs.getInt(1);
                    String key2 = rs.getString(2);
                    String value = rs.getString(3);
                    if (!alSinonim.contains(key2)) {
                        alSinonim.add(key2);
                    }
                }
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            //frasa h ada sinonimnya?
            if (alSinonim.size()>0) {
                ArrayList<String> alStrKetemu = new ArrayList<>(); //yg ada di T
                for (String s:alSinonim) {
                    //cari sinonim ini di T
                    boolean ketemu = t.matches(".*?\\b"+s+"\\b.*?");
                    if (ketemu) {
                        alStrKetemu.add(s); //yg ada pasangannya t
                    }
                }
                //ada yang cocok
                if (alStrKetemu.size()>0) {
                    ArrayList<String> alSinonimValid= new ArrayList<>();
                    for (String sk:alStrKetemu) {
                        ArrayList<String>  alSk = pp.loadKataTanpaStopWords(sk,true,false);
                        //hanya yg >1 kata setelah stopwords dibuang
                        if (alSk.size()>1) {
                            alSinonimValid.add(sk);
                        }
                    }
                    if (alSinonimValid.size()>0) {
                        //cari  t, ganti dengan h
                        //
                        System.out.println("Frase H:"+fr);
                        System.out.println("Sinonimnya:");
                        for (String sv:alSinonimValid) {
                            System.out.println(sv);  //umumnya cuma satu
                            //ganti sinomim di H dengan frasa di T
                            tPengganti = tPengganti.replaceAll("(?i)"+ Pattern.quote(sv), " "+fr+" ");
                            //tPengganti = tPengganti.replace(sv,fr);
                        }
                    }
                }  //sinonim tsb ada di T
            } //if ada sinonimnya di database
        } //semua frasa
        return tPengganti;
    }

    /*
       dipanggil sebelum gantisinonim dipanggil

     */

    public void init() {
        connectDB();
        pp = new Prepro();
        pp.loadStopWords("stopwords2","kata");
    }

    public void connectDB() {
        String strSel;
        String host   = "localhost";
        String user   = "user";
        String passwd = "user";
        String db =     "ppdb";
        try {
            Class.forName("org.postgresql.Driver");
            String koneksiDB = "jdbc:postgresql://"+host+'/'+db+'?';
            String userPwd = "user="+user+'&'+"password="+passwd;
            conn = DriverManager.getConnection(koneksiDB+userPwd);
            strSel = "select id,key2,value from ppdblarge where key1=?";
            pSel = conn.prepareStatement(strSel);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void close() {
        try {
            if (pSel!=null){
                pSel.close();
            }
            if (conn!=null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void proses() {
        //looad id, t,h
        //baca file id,t,h
        String fileTH  =  "D:\\desertasi\\eksperimen\\id_t_h.txt";
        Prepro pp = new Prepro();
        ProsesPPDBHypothesis p  = new ProsesPPDBHypothesis();
        p.init();
        try {
            Scanner sc = new Scanner(new File(fileTH));
            while (sc.hasNextLine()) {
                String id = sc.nextLine();  //tidak dipake
                System.out.println(id);
                String t = sc.nextLine();
                String h = sc.nextLine();
                String t2 = p.gantiSinonimPPDB(t,h);
                if (!t2.equals(t)) {
                    System.out.println("H:"+h);
                    System.out.println("T sebelum:"+t);
                    System.out.println("output:" + t2);
                }
            }
            p.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        ProsesPPDBHypothesis p  = new ProsesPPDBHypothesis();
        p.proses();
        p.close();




        /*
        ProsesPPDBHypothesis p  = new ProsesPPDBHypothesis();
        //p.connectDB();
        //p.prosesAmbilSinonimDariDB();
        p.init();

        //String h = "After the resignation of six pro-Syrian ministers a deep political crises started in Lebanon";
        //String t = "Gemayel's assassination is almost certain to deepen the political crisis in Lebanon, which started after the resignation of six pro-Syrian ministers, two from Hezbollah, hours after all-party round table talks collapsed.";

        String h= "Domestic fires are a major cause of unintentional injury in the home";
        String t= "Domestic fire incidents have an important impact on population health by contributing to early death, serious and often disfiguring injuries, and ongoing disability. Fire-related injury is a leading cause of unintentional injury in the home in all age groups.";
        String t2 = p.gantiSinonimPPDB(t,h);
        System.out.println("output:"+t2);
        p.close();
        */
    }
}
