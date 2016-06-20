package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import java.sql.*;

/**
 * Created by yudiwbs on 19/06/2016.
 *
 * GRANT CONNECT ON DATABASE PPDB TO "user";
 * GRANT ALL PRIVILEGES ON DATABASE PPDB TO "user";
 * GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO "user";
 *
 */

public class CobaPostGre {

    public static void main(String[] args) {


        Connection conn=null;
        PreparedStatement pSel;
        PreparedStatement pUpdate;
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

        strSel       = String.format("select id  from ppdblarge");

        System.out.println(strSel);

        String kata;
        System.out.println("Mulai select");
        try {
            pSel = conn.prepareStatement(strSel);
            ResultSet rs = pSel.executeQuery();
            while (rs.next()) {
                long id = rs.getLong(1);
                System.out.println(id);
            }
            rs.close();
            pSel.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
