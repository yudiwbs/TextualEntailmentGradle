package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import javax.xml.crypto.Data;
import java.util.ArrayList;

/**
 * Created by yudiwbs on 24/05/2016.
 * merepresentasikan sentece
 * terkait denga DataDepTree dan ParsingSyntaxNet
 */

public class SentenceDepTree {
    ArrayList<DataDepTree> alDataDepTree = new ArrayList<>();

    public String getKalimatAsli() {
        StringBuilder sb = new StringBuilder();
        for (DataDepTree d: alDataDepTree) {
            sb.append(d.kata);
            sb.append(" ");
        }
        return sb.toString();
    }

    public void add(DataDepTree d) {
        alDataDepTree.add(d);
    }

    //rekursif
    private ArrayList<DataDepTree>  rekursifCariChild(String idParent) {
        //return child
        ArrayList<DataDepTree> child = new ArrayList<>();
        //cari element yang parentnya id dalam sentence itu
        for (DataDepTree d:alDataDepTree) {
            if (d.parent.equals(idParent)) {
                //masuk ke child
                child.add(d);
                ArrayList<DataDepTree> childRek = rekursifCariChild(d.id);
                child.addAll(childRek);
            }
        }
        return child;
    }

    public ArrayList<DataDepTree> getChild (String idParent) {
        ArrayList<DataDepTree> child = rekursifCariChild  (idParent);
        return child;
    }

}
