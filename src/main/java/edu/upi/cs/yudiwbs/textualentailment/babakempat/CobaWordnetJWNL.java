package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.*;
import net.sf.extjwnl.data.list.PointerTargetNode;
import net.sf.extjwnl.data.list.PointerTargetNodeList;
import net.sf.extjwnl.dictionary.Dictionary;

import java.util.List;

/**
 *     Created by yudiwbs on 10/04/2016.
 *
 *
 *
 */

public class CobaWordnetJWNL {



    public static void main(String[] args) {
        Dictionary dictionary = null;
        try {
            dictionary = Dictionary.getDefaultResourceInstance();
            IndexWord iw = dictionary.getIndexWord(POS.VERB, "accomplish");
            //iw.
            PointerTargetNodeList hypernyms = PointerUtils.getDirectHypernyms(iw.getSenses().get(0));
            System.out.println("Direct hypernyms of \"" + iw.getLemma() + "\":");

            //nanti loop
            //PointerTargetNode ptn = hypernyms.getFirst();
            for (int i = 0; i < hypernyms.size(); i++) {
                System.out.println("i:"+i);
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

    }


}
