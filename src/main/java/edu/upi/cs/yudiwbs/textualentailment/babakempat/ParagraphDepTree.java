package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import java.util.ArrayList;

/**
 * Created by yudiwbs on 30/05/2016.
 * menampung hasil parsing dokumen output syntaxnet
 *
 * lihat class ProsesRootSyntaxNet yang menggunakan class ini
 *
 */

public class ParagraphDepTree {
    int id;
    ArrayList<String> alKalimatAsli = new  ArrayList<>();
    ArrayList<SentenceDepTree> alSenDepTree = new ArrayList<>();

    public void addKalimatAsli(String s, int id) {
        alKalimatAsli.add(s);
        this.id = id;
    }

    public void addSentence(SentenceDepTree sd) {
        alSenDepTree.add(sd);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id:"+id+" ; Jum kal asli:"+alKalimatAsli.size());
        sb.append(System.lineSeparator());
        sb.append("Jum kal dep tree:"+alSenDepTree.size());
        sb.append(System.lineSeparator());
        for (String s:alKalimatAsli) {
            sb.append(s);
            sb.append(System.lineSeparator());
        }

        for (SentenceDepTree s:alSenDepTree) {
            sb.append(s);
            sb.append(System.lineSeparator());
        }

        return sb.toString();
    }
}
