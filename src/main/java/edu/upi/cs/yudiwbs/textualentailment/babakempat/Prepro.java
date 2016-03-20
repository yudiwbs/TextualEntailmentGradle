package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by yudiwbs on 11/27/2015.
 *
 *   buang selain kata benda dan kata kerja
 *
 */

public class Prepro {
    Logger logger;
    public String dbName;
    public String userName;
    public String password;
    private ArrayList<String> alStopWords = new ArrayList<>();

    public void loadStopWords(String namaTabel,String namaField) {
        //memindahkan data stopwords dari tabel ke memori alStopWords
        System.out.println("loadStopWords");
        Connection conn=null;
        PreparedStatement pSel=null;
        KoneksiDB db = new KoneksiDB();
        alStopWords.clear();
        try {
            conn = db.getConn();
            pSel  = conn.prepareStatement (String.format("select id,%s from %s",namaField,namaTabel));
            ResultSet rs = pSel.executeQuery();
            int jumDiproses = 0;
            while (rs.next())  {
                String kata = rs.getString(2).trim();
                alStopWords.add(kata);
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

       public void fileStopwordsToDB(String fileName,String tableName,String fieldName) {
              //fieldname: nama field di tabel stopwords

              //utility memindahkan isi file teks ke tabel
              //berguna untuk menambahkan data stopwords baru
              //melakukan pengecekan, kalau ada duplikasi maka tidak dimasukkan,
              // jadi tidak perlu dihapus sebelumnya


              System.out.println("filetodbstopwords");
              Connection conn=null;
              PreparedStatement pSdhAda=null;
              PreparedStatement pIns=null;
              int jumTdkDiproses=0;
              int jumDiproses=0;

              try {
                     Class.forName("com.mysql.jdbc.Driver");
                     conn = DriverManager.getConnection  ("jdbc:mysql://"+dbName+"?user="+userName+"&password="+password);
                     pSdhAda = conn.prepareStatement     (" select id from  "+ tableName + " where "+ fieldName +" = ?");
                     pIns    =  conn.prepareStatement    (" insert into  "+ tableName + "("+fieldName+") values (?)");

                     FileInputStream fstream = new FileInputStream(fileName);
                     DataInputStream in = new DataInputStream(fstream);
                     BufferedReader br = new BufferedReader(new InputStreamReader(in));
                     String strLine;
                     ResultSet rs;
                     while ((strLine = br.readLine()) != null)   {
                            if (strLine.equals("")) {continue;}
                            //masuk ke tabel?
                            pSdhAda.setString(1,strLine);
                            rs = pSdhAda.executeQuery();
                            if (rs.next()) {
                                   //sudah ada, batalkan masuk
                                   jumTdkDiproses++;
                            } else {
                                   jumDiproses++;
                                   pIns.setString(1,strLine);
                                   pIns.executeUpdate();
                            }
                     }
              } catch (Exception e) {
                  e.printStackTrace();
                  logger.log(Level.SEVERE, null, e);
              }
              finally  {
                     try  {
                            if (pSdhAda != null) {pSdhAda.close();}
                            if (pIns != null)    {pIns.close();}
                            if (conn != null) {conn.close();}
                     } catch (Exception e) {
                            logger.log(Level.SEVERE, null, e);
                     }
              }
              System.out.println("selesai");
       }

       //menghasilkan infoteks yang didalamnya ada daftar verb dan noun
       //pangil loadStopWords terlebih dulu jika mau stopwords dihilangkan
       public InfoTeks isiInfoTeks(String strIn,String synTree)  {
           InfoTeks out = new InfoTeks();
           out.teksAsli = strIn;
           out.strukturSyn = synTree;

           //buang kata selain verb dan noun
           //buang kata yg ada di stopwords
           //menggunakan synctatic tree

           /*

           Contoh input:
           The sale was made to pay Yukos' US$ 27.5 billion tax bill, Yuganskneftegaz was originally sold for
           US$ 9.4 billion to a little known company Baikalfinansgroup which was later bought by the Russian
           state-owned oil company Rosneft .

           Contoh synctatic tree:
           (ROOT (S (S (NP (DT The) (NN sale)) (VP (VBD was) (VP (VBN made)
           (S (VP (TO to) (VP (VB pay) (NP (NP (NNP Yukos) (POS '))
           (ADJP (QP ($ US$) (QP (CD 27.5) (CD billion)))) (NN tax) (NN bill)))))))) (, ,)
           (NP (NNP Yuganskneftegaz)) (VP (VBD was) (ADVP (RB originally))
           (VP (VBN sold) (PP (IN for) (NP (QP ($ US$) (QP (CD 9.4) (CD billion)))))
           (PP (TO to) (NP (NP (DT a) (ADJP (RB little) (VBN known)) (NN company)
           (NN Baikalfinansgroup)) (SBAR (WHNP (WDT which)) (S (VP (VBD was) (ADVP (RB later))
           (VP (VBN bought) (PP (IN by) (NP (DT the) (JJ Russian) (JJ state-owned) (NN oil)
           (NN company) (NN Rosneft))))))))))) (. .)))

           //alstopwords terisi

           outputnya:
           Verb:was made pay was sold known was bought
           Noun:sale yukos tax bill yuganskneftegaz company baikalfinansgroup oil company rosneft

           */

           //String strOut;
           //strOut = strIn.toLowerCase().replaceAll("[\\.]"," . ").replaceAll("'"," ' ").replaceAll(","," , ");   //casefolding, titik dibuat
                                                                             // ada spasi karena kata. <> kata
           //System.out.println(strOut);
           String  synTree2 = synTree.replaceAll("\\)", " ) ").toLowerCase(); //biar kurung tutup ngga lengket
           //System.out.println(synTree2);

           //strOut = strOut.replaceAll("[0-9()\"\\-.,]"," ");
           //strOut = strOut.replaceAll("[[^a-z ][\\-]]"," ");
           //proses stopwords
           //Scanner sc = new Scanner(strOut);
           StringBuilder sb = new StringBuilder();
           Scanner scTree = new Scanner (synTree2);

           StringBuilder sbVerb = new StringBuilder();
           StringBuilder sbNoun = new StringBuilder();
           int cc = 0;
           String lastTag="";
           String kata="";
           while (scTree.hasNext()) {
               String kataTree = scTree.next().trim();

               //cari sampai ketemu kata di synctree
               if (kataTree.contains("(")) {  //tag
                   lastTag = kataTree;
               } else {                      //nontag
                   kata = kataTree;
                   if (alStopWords.contains(kata) ||  kata.equals(")") ) {
                       continue;  //skip stop words
                   }
                   if (       lastTag.equals("(vbd") || lastTag.equals("(vbn") || lastTag.equals("(vb")
                           || lastTag.equals("(vbg") || lastTag.equals("(vbz") || lastTag.equals("(vbp") ) {
                       //verb
                       out.alVerb.add(kata);
                       //sbVerb.append(kata);
                       //sbVerb.append(" ");
                       //NNS Noun, plural 14. NNP Proper noun, singular 15. NNPS
                   } else if ( lastTag.equals("(nn")   || lastTag.equals("(nns") || lastTag.equals("(nnp")
                            || lastTag.equals("(nnps") ) {
                       //sbNoun.append(kata);
                       //sbNoun.append(" ");
                       out.alNoun.add(kata);
                   } else {
                       //System.out.println("tag tdk ketemu"+lastTag);
                   }
                   sb.append(kata);
                   sb.append(" ");
               }
           }
           scTree.close();
           //out = sb.toString();

           //System.out.println("verb:"+sbVerb.toString());
           //System.out.println("noun:"+sbNoun.toString());
           return out;
       }



       public static void main(String[] args) {

       }
}
