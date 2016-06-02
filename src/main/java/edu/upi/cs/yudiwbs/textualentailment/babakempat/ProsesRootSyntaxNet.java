package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/**
 *
 *  cat input_test_t.txt | syntaxnet/demo3.sh > out_test_t.txt

 * Created by yudiwbs on 30/05/2016.
 *
 *   mencari root H yang paling tepat di T (allignment)
 *
 *
 */


public class ProsesRootSyntaxNet {

    ArrayList<ParagraphDepTree> alParT = new ArrayList<>();   //satu Par artinya satu T yang terdiri dari banyak subkalimat


    //FS: alParT terisi
    //file input adalah file mentah yg menjadi input SyntaxNet
    //sedangkan file output adalah file hasil SyntaxNet (bukan tree)
    public void loadDepTree(String strFileInput, String strFileOut) {
        Scanner scInp = null;
        try {
            scInp = new Scanner(new File(strFileInput));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //load data input supaya tahu terdiri dari berapa sentence untuk setiap par
        ParagraphDepTree pd = new ParagraphDepTree();
        alParT.add(pd);
        int cc = 0;
        while (scInp.hasNextLine()) {
            String ln = scInp.nextLine().trim();
            if (ln.equals("")) { //ganti baris, ganti paragraph
                pd = new ParagraphDepTree();
                alParT.add(pd);
                cc++;
            } else {
                pd.addKalimatAsli(ln,cc+1);
            }
        }

        //bersihkan sisa kalau ada kelebihan pd
        Iterator<ParagraphDepTree> iter = alParT.iterator();
        while (iter.hasNext()) {
            ParagraphDepTree p = iter.next(); // must be called before you can call i.remove()
            if (p.alKalimatAsli.size()==0) {
                iter.remove();
            }
        }

        //debug
        /*
        for (ParagraphDepTree p: alPar) {
            System.out.println(p);
        }
        */


        //load data dep parsernya
        //ingat satu T bisa terdiri atas beberapa sentence

        Scanner scOut = null;
        try {
            //load semua sentence kedalam  alSemuaSentence
            scOut = new Scanner(new File(strFileOut));
            ArrayList<SentenceDepTree> alSemuaSentence  = new ArrayList<>();
            SentenceDepTree  sentence = new SentenceDepTree();
            while (scOut.hasNextLine()) {
                String ln = scOut.nextLine().trim();
                if (ln.equals("")) { //ganti baris, ganti sentence
                    //break sentence
                    alSemuaSentence.add(sentence); //tambah sentence
                    sentence = new SentenceDepTree();
                } else {
                    DataDepTree d = new DataDepTree(ln);
                    sentence.add(d);
                }
            }

            //baru load ke paragraph
            int pp = 0;
            for (ParagraphDepTree p:alParT) {
                int jumKal = p.alKalimatAsli.size();
                int batas = pp + jumKal;
                while (pp<batas) {
                    p.addSentence(alSemuaSentence.get(pp));
                    pp++;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /*
        idenya mencari objek H di dep tree T, lalu cari parentnya
        parent yang berupa verb baru dibandingkan dengan root
     */

    public void proses() {
        //load dulu
        String strFileInputT="D:\\desertasi\\eksperimen\\input_train_t.txt";
        String strFileOutT  ="D:\\desertasi\\eksperimen\\out_train_t.txt";
        loadDepTree(strFileInputT,strFileOutT);

        //sudah masuk ke paragraph

        Prepro objPrepro = new Prepro();
        objPrepro.loadStopWords("stopwords2","kata");

        //cari rootnya
        for (ParagraphDepTree p: alParT) {
            for (SentenceDepTree sd: p.alSenDepTree) {
                System.out.println(sd.getKalimatAsli());
                sd.prosesRootSubyekObyek(objPrepro);
                //System.out.println(sd);
                //debug print root-subyek-obyek
                /*
                for (Root r:sd.alRoot) {
                    System.out.println(r);
                }
                System.out.println();
                */
            }
        }


        //alRoot sudah terisi






        //cari subyek dan obyek di dep tree, lalu cari parentnya yg bisa dibandingkan dengan root
        //if (d.rel.equals("iobj")||d.rel.equals("dobj")||d.rel.equals("pobj")) {
        //(d.rel.equals("nsubj")||d.rel.equals("nsubjpass")
    }

    public static void main(String[] args) {
        ProsesRootSyntaxNet prs = new ProsesRootSyntaxNet();
        prs.proses();
    }
}
