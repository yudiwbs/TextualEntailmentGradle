package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import java.sql.*;

/**
 * Created by yudiwbs on 19/06/2016.
 * ambil data dari file ppdb, split dgan gsplit (hati2 baris pertama dan baris terakhir bisa terpotong)
 */

public class ProsesPPDBtoDB {


    public static void main(String[] args) {

        //test insert
        Connection conn=null;
        //PreparedStatement pSel;
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

        //strSel       = String.format("select id  from ppdblarge");

        String strIns = "insert into ppdblarge(key1,key2,value,type) values (?,?,?,?)";
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
    }
}
