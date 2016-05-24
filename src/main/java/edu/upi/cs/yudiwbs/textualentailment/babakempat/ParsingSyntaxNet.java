package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by yudiwbs on 16/05/2016.
 *
 * parsing keluaran parser google syntaxnet
 *
 * output dari demo.sh, input demo.sh dihasilkan oleh class PrintTeks (split kalimat)

 contoh hasil PrintTeks (antar paragraph dipisah oleh line kosong, antar kalimat oleh ganti baris)

 The sale was made to pay Yukos' US$ 27.5 billion tax bill, Yuganskneftegaz was originally sold for US$ 9.4 billion to a little known company Baikalfinansgroup which was later bought by the Russian state-owned oil company Rosneft .
 Baikalfinansgroup was sold to Rosneft.

 The sale was made to pay Yukos' US$ 27.5 billion tax bill, Yuganskneftegaz was originally sold for US$9.4 billion to a little known company Baikalfinansgroup which was later bought by the Russian state-owned oil company Rosneft .
 Yuganskneftegaz cost US$ 27.5 billion.

 *
 * cara menjalankan:
 * cat rte3_ver3.txt | syntaxnet/demo3.sh > out_ver4.txt
 *
 * perlu dua versi, versi pertama biasa, versi kedua tanpa tree (demo3.sh)
 * edit demo.sh, buang bagian yang mengenerate tree lalu saveas:
     bazel-bin/syntaxnet/conll2tree \
     --task_context=$MODEL_DIR/context.pbtxt \
     --alsologtostderr

 *

 contoh datanya:

 file 1 (satu kalimat satu baris!), masalah coref jadi penting:
 id:1
 The sale was made to pay Yukos' US$ 27.5 billion tax bill, Yuganskneftegaz was originally sold for US$ 9.4 billion to a little known company Baikalfinansgroup which was later bought by the Russian state-owned oil company Rosneft .
 H:
 Baikalfinansgroup was sold to Rosneft.
 isEntail:true
 id:3
 Loraine besides participating in Broadway's Dreamgirls, also participated in the Off-Broadway production of "Does A Tiger Have A Necktie".
 In 1999, Loraine went to London, United Kingdom.
 There she participated in the production of "RENT" where she was cast as "Mimi" the understudy.
 H:
 "Does A Tiger Have A Necktie" was produced in London.
 isEntail:false


 file 2:
 1	The	_	DET	DT	_	2	det	_	_
 2	sale	_	NOUN	NN	_	4	nsubjpass	_	_
 3	was	_	VERB	VBD	_	4	auxpass	_	_
 4	made	_	VERB	VBN	_	0	ROOT	_	_



 */


public class ParsingSyntaxNet {



    //isi dari satu baris file2
    //24	and	_	CONJ	CC	_	23	_	_
    //id    kata    POS     rel   parent
    private class DataDepTree {
        String id;
        String kata;
        String posUmum;
        String posKhusus;
        String rel;
        String parent;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("id:"+id);
            sb.append("\n");
            sb.append("kata:"+kata);
            sb.append("\n");
            sb.append("pos Umum:"+posUmum);
            sb.append("\n");
            sb.append("pos Khusus:"+posKhusus);
            sb.append("\n");
            sb.append("rel:"+rel);
            sb.append("\n");
            sb.append("parent:"+parent);
            return sb.toString();
        }

        public  DataDepTree(String s) {
            //parsing
            //3	    was	  _	  VERB	 VBD	_	4	     auxpass	_	_
            //id    kata      POS     rel       parent   rel
            //0     1     2   3       4     5   6        7
            Scanner sc = new Scanner(s);
            int cc = 0;
            while (sc.hasNext()) {
                String k = sc.next();

                if (cc == 0) {
                    id = k;
                } else if (cc ==1) {
                    kata = k;
                } else if (cc ==3) {
                    posUmum = k;
                } else if (cc==4) {
                    posKhusus = k;
                } else if (cc==6) {
                    parent = k;
                }  else if (cc==7) {
                    rel = k;
                }
                cc++;
            }
        }

    }



    /*
        load dari file text hasil syntaxnet

     */
    public ArrayList<ArrayList<DataDepTree>>  load(String file1, String file2) {
        Scanner scRef = new Scanner(file1);

        Scanner scData = null;
        try {
            scData = new Scanner(new File(file2));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ArrayList<ArrayList<DataDepTree>> alSemuaSentence  = new ArrayList<>();

        ArrayList<DataDepTree>  alSentence = new ArrayList<>();
        while (scData.hasNextLine()) {
            String ln = scData.nextLine().trim();
            if (ln.equals("")) {
                //break sentence
                alSemuaSentence.add(alSentence); //teambah setnence
                alSentence = new ArrayList<>();
            }
            //System.out.println("line:"+ln);
            DataDepTree d = new DataDepTree(ln);
            alSentence.add(d);
            //System.out.println(d);
            //break; //debug, satu baris dulu
        }

        for (ArrayList<DataDepTree>  als: alSemuaSentence) {
            //als itu per kalimat dalams satu par
            for (DataDepTree d: als) {
                System.out.println(d);
            }
            break; //testing satu dulu
        }

        return alSemuaSentence;

    }


    public static void main(String[] args) {
        ParsingSyntaxNet psn = new ParsingSyntaxNet();
        //psn.proses("D:\\desertasi\\eksperimen\\t-h-entail.txt","D:\\desertasi\\eksperimen\\out_ver4_h.txt");
    }
}
