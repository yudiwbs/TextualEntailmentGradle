package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

/**
 * Created by yudiwbs on 19/06/2016.
 * ambil data dari file ppdb, split dgan gsplit (hati2 baris pertama dan baris terakhir bisa terpotong)
 * hati2 karena datanya sangat besar (23 jt), bisa lama

 create table ppdblarge(
 id serial primary key,
 key1 varchar(150),
 key2 varchar(150),
 value text,
 type varchar(10),
 alignment varchar(150)
 )
 */

public class ProsesPPDBtoDB {



    /*
       menghasilkan file dalam satu direktori yang terurut

     */

    public ArrayList<String> getFiles(String strFolder) {
        ArrayList<String> alFile = new ArrayList<>();
        final File folder = new File(strFolder);
        for (File fileEntry : folder.listFiles()) {
            if (!fileEntry.isDirectory()) {
                alFile.add(fileEntry.getName());
            }
        }

        //diurut, jadi disk1, disk2, disk..., disk11, disk12
        Collections.sort(alFile, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return extractInt(o1) - extractInt(o2);
            }

            int extractInt(String s) {
                String num = s.replaceAll("\\D", "");
                // return 0 if no digits found
                return num.isEmpty() ? 0 : Integer.parseInt(num);
            }
        });

        //debug
        /*
        for (String s : alFile) {
            System.out.println(s);
        }
        */
        return alFile;
    }

    public void proses() {

        //connect
        Connection conn=null;
        PreparedStatement pIns;
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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //load file dalam direktori
        String dir = "D:\\corpus\\ppdb\\ppdb-1.0-large-all\\pecahan\\";
        ArrayList<String> alFiles = getFiles(dir);


        try {
            String strIns = "insert into ppdblarge(key1,key2,value,type,alignment) values (?,?,?,?,?)";
            System.out.println("Mulai");
            pIns = conn.prepareStatement(strIns);
            int batchSize = 5000;
            int ccBatch = 0;
            for (String sFile:alFiles) {
                System.out.println("proses file:"+sFile);
                Scanner sc = new Scanner(new File(dir+sFile));
                int cc = 0;
                while (sc.hasNextLine()) {
                    String line  = sc.nextLine();
                    //[CD] ||| 10/17/01 ||| 17/10/2001 ||| Abstract=0 Adjacent=0 CharCountDiff=2 CharLogCR=0.22314 ContainsX=0 GlueRule=0 Identity=0 Lex(e|f)=62.90141 Lex(f|e)=62.90141 Lexical=1 LogCount=0 Monotonic=1 PhrasePenalty=1 RarityPenalty=0.13534 SourceTerminalsButNoTarget=0 SourceWords=1 TargetTerminalsButNoSource=0 TargetWords=1 UnalignedSource=0 UnalignedTarget=0 WordCountDiff=0 WordLenDiff=2.00000 WordLogCR=0 p(LHS|e)=0 p(LHS|f)=0 p(e|LHS)=16.92877 p(e|f)=0.69315 p(e|f,LHS)=0.69315 p(f|LHS)=16.92877 p(f|e)=0.69315 p(f|e,LHS)=0.69315 AGigaSim=0 GoogleNgramSim=0 ||| 0-0
                    //LHS ||| SOURCE ||| TARGET ||| (FEATURE=VALUE )* ||| ALIGNMENT
                    String[] arrLine = line.split("\\|\\|\\|");
                    int jumElemen = arrLine.length;
                    //System.out.println(); //harus 5
                    if (jumElemen==5) {
                        //System.out.println(arrLine[0]); //lhs
                        //System.out.println(arrLine[1]); //source
                        //System.out.println(arrLine[2]); //target
                        //System.out.println(arrLine[3]); //feature-val
                        //System.out.println(arrLine[4]); //alignmnet
                        //key1,key2,value,type,alignment
                        //StringEscapeUtils.es
                        pIns.setString(1,arrLine[1].trim());
                        pIns.setString(2,arrLine[2].trim());
                        pIns.setString(3,arrLine[3].trim());
                        pIns.setString(4,arrLine[0].trim());
                        pIns.setString(5,arrLine[4].trim());
                        pIns.addBatch();
                        if (cc % batchSize == 0) {
                            ccBatch++;
                            pIns.executeBatch();
                            System.out.println("Tulis batch:"+ccBatch);
                        }
                    } else {
                        System.out.println("skip, jumlah elemen<>5...");
                    }
                    cc++;
                    /*
                    if (cc>5) {
                        break;  //test 5 baris dulu
                    }
                    */
                }
                System.out.println("Jumlah baris yg sudah diproses:"+cc);
                //break; //debug, stop satu file dulu
            }
            pIns.executeBatch();
            pIns.close();
            conn.close();
            System.out.println("selesai");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.getNextException().printStackTrace();
            System.out.println("---");
            e.printStackTrace();
        }

        /*

        String strIns = "insert into ppdblarge(key1,key2,value,type) values (?,?,?,?,?)";
        //System.out.println(strSel);
        String kata;
        System.out.println("Mulai");
        try {
            pIns = conn.prepareStatement(strIns);
            pIns.setString(1, "key1_tiga");
            pIns.setString(2, "key2_tiga");
            pIns.setString(3, "value ....... kaljsd lajsdfkj askdfj alksdjflsa dfflkasdfljas dfjlajsdfl sdaf");
            pIns.setString(4, "CDDD");
            pIns.executeUpdate();
            pIns.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        */
    }



    public static void main(String[] args) {
        ProsesPPDBtoDB pp = new ProsesPPDBtoDB();
        pp.proses();
    }
}
