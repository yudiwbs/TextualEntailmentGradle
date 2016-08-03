package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.WuPalmer;

/**
 * Created by yudiwbs on 14/04/2016.
 */

//private RelatednessCalculator rc = new Lin(db);
//private RelatednessCalculator rc = new JiangConrath(db);
//private RelatednessCalculator rc = new Resnik(db);
//private RelatednessCalculator rc = new LeacockChodorow(db);
//private RelatednessCalculator rc = new WuPalmer(db);
//private RelatednessCalculator rc = new HirstStOnge(db); //ini lama banget! hasil juga aneh, nanti aja dicoba lagi
//private RelatednessCalculator rc = new  Lesk(db);

public class CobaWS4J {
    public static void main(String[] args) {
        RelatednessCalculator rc;
        ILexicalDatabase db = new NictWordNet();
        rc = new WuPalmer(db);
        //String k1="missing";
        //String k2="search";
        String k1="home";
        String k2="house";
        double out = rc.calcRelatednessOfWords(k1, k2);
        System.out.println(k1+"->"+k2+"="+out);
    }
}
