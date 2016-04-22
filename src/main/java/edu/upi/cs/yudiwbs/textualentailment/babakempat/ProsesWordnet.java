package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.*;
import net.sf.extjwnl.data.list.PointerTargetNode;
import net.sf.extjwnl.data.list.PointerTargetNodeList;
import net.sf.extjwnl.dictionary.Dictionary;

import java.util.List;

/**
 * Created by yudiwbs on 12/04/2016.
 */
public class ProsesWordnet {
    Dictionary dict = null;

    public ProsesWordnet() {
        try {
            dict = Dictionary.getDefaultResourceInstance();
        } catch (JWNLException e) {
            e.printStackTrace();
        }
    }

    //kalau nggak tahu tipenya apa
    public boolean isDirectHypernym(String lemma1,String lemma2) {
        boolean out=false;

        return out;
    }


    //case kata tidak penting, tapi lemmatisasi penting
    //satu synset: hypernim pertama
    public boolean isSatuSynset(POS pLemma1, String lemma1, String lemma2) {
        boolean out=false;
        IndexWord iw = null;
        PointerTargetNodeList hypernyms=null;
        try {
            iw = dict.getIndexWord(pLemma1, lemma1);
            if (iw!=null){
                hypernyms = PointerUtils.getDirectHypernyms(iw.getSenses().get(0));
            }
            if (hypernyms!=null) {
                if (hypernyms.size()>0) {
                    PointerTargetNode node = hypernyms.get(0);
                    Synset sy = node.getSynset();
                    if (sy.containsWord(lemma2)) {
                        out = true; //ketemu
                    }
                }
            }
        } catch (JWNLException e) {
            e.printStackTrace();
        }
        return out;
    }





    //case kata tidak penting, tapi lemmatisasi penting
    //direct hypernim: sense pertama
    public boolean isDirectHypernym(POS pLemma1, String lemma1, String lemma2) {
        boolean out=false;
        IndexWord iw = null;
        PointerTargetNodeList hypernyms=null;
        try {
            iw = dict.getIndexWord(pLemma1, lemma1);
            if (iw!=null){
                hypernyms = PointerUtils.getDirectHypernyms(iw.getSenses().get(0));
            }
            if (hypernyms!=null) {
                for (int i = 0; i < hypernyms.size(); i++) {
                    PointerTargetNode node = hypernyms.get(i);
                    Synset sy = node.getSynset();
                    if (sy.containsWord(lemma2)) {
                        out = true; //ketemu
                        break;
                    }
                }
            }

        } catch (JWNLException e) {
            e.printStackTrace();
        }
        return out;
    }

    public static void main(String[] args) {

        ProsesWordnet pw = new ProsesWordnet();
        /*
        //case tidak penting, tapi lemmatisasi penting
        if (pw.isDirectHypernym(POS.VERB, "Addccomplish","Finish")) {
            System.out.println("direct hypernim");
        } else {
            System.out.println("bukan directy hypernim");
        }
        */

        if (pw.isSatuSynset(POS.VERB, "accomplish","effect")) {
            System.out.println("satu synset");
        } else {
            System.out.println("bukan satu synset");
        }


        /*Dictionary dictionary = null;
        try {
            dictionary = Dictionary.getDefaultResourceInstance();
            IndexWord iw = dictionary.getIndexWord(POS.VERB, "accomplish");
            PointerTargetNodeList hypernyms = PointerUtils.getDirectHypernyms(iw.getSenses().get(0));
            System.out.println("Direct hypernyms of \"" + iw.getLemma() + "\":");

            //nanti loop
            //PointerTargetNode ptn = hypernyms.getFirst();
            for (int i = 0; i < hypernyms.size(); i++) {
                PointerTargetNode node  = (PointerTargetNode) hypernyms.get(i);
                //synsets.add(node.getSynset());
                //System.out.println(ptn);
                Synset sy = node.getSynset();
                //if (sy.containsWord("finish")) {
                //    System.out.println("mengandung finish");
                //}
                List<Word> lw = sy.getWords();
                for (Word w: lw) {
                    System.out.println(w.getLemma());
                }
            }


            //Word w = ptn.getWord();
            //System.out.println(w);
            //System.out.println(w.getLemma());


            //hypernyms.print();
        } catch (JWNLException e) {
            e.printStackTrace();
        }
        */

    }
}
