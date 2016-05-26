package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import java.util.Scanner;

/**
 * Created by yudiwbs on 24/05/2016.
 *
 * menyimpan dep tree satu kalimat, lihat class ParsingSyntaxNet
 *
 */

//isi dari satu baris file2
//24	and	_	CONJ	CC	_	23	_	_
//id    kata    POS     rel   parent
public class DataDepTree {



    int idInt;
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
                idInt = Integer.parseInt(id);
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
