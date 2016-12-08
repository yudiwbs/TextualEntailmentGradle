/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.functions.SimpleLogistic;
import weka.classifiers.trees.j48.BinC45Split;
import weka.classifiers.trees.j48.C45Split;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.supervised.attribute.Discretize;
//import weka.filters.supervised.instance.

/**
 *
 * @author MLK
 */
public class CobaWeka {

    public Instances loadData (String filename) throws Exception {
        //i.s: file "filename" berformat weka dan valid. ClassIndex yang terakhir
        //return data1 berisi semua data dari filename
        Instances result = ConverterUtils.DataSource.read(filename);
        if (result.classIndex() == -1)
            result.setClassIndex(result.numAttributes() - 1);
        return result;
    }

    public void saveModelAndStructure (String namafile, AbstractClassifier cls, Instances data) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(namafile));
            oos.writeObject(cls);
            oos.writeObject(data);
            oos.flush();
            oos.close();
        } catch (Exception ex) {
            Logger.getLogger(CobaWeka.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static double crossValidation(AbstractClassifier classifier, Instances trainData, int kfold) {
        //i.s: classifier is defined; trainData is defined; kfold>1
        //f.s: Filename contains classifier model of trainData
        try {
//                System.out.println("Cross validation -- start ...");
            Evaluation eval=new Evaluation(trainData);
            eval.crossValidateModel(classifier, trainData, kfold, new Random(1));
//                System.out.println("Cross validation -- completed");
//                System.out.println(eval.toSummaryString("\nResults", true));

//                System.out.println("Confusion matrix:");
//                double[][] cm=eval.confusionMatrix();
//                for (int i=0;i<cm.length;i++) {
//                    for (int j=0;j<cm[i].length;j++) {
//                        System.out.print(cm[i][j]+" ");
//                    }
//                    System.out.println();
//                }
//                System.out.println("\nF-measure-Precision-Recall");
//                for (int i=0;i<cm.length;i++) {
//                    System.out.println(i+" - "+eval.fMeasure(i)+" - "+eval.precision(i)+" - "+eval.recall(i));
//                }
            System.out.println(" => Accuracy: "+eval.pctCorrect());
            return eval.pctCorrect();
        } catch (Exception ex) {
            Logger.getLogger(CobaWeka.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public static void getBalancedData(Instances trainData) {
        //i.s: classIndex is defined
        //f.s: number of instances of each class is balanced


    }

    public static void main(String[] args) throws Exception
    {
        CobaWeka m=new CobaWeka();
        String datafile="D:\\desertasi\\eksperimen_babak4\\Ujicoba_Juli_Agt_2016 - train9.csv.arff";
        Instances trainData=m.loadData(datafile);
        trainData.setClassIndex(0);

        C45Split split=new C45Split(2, 0, trainData.sumOfWeights(), true);
        split.buildClassifier(trainData);
        System.out.println(split.splitPoint());
    }

//    public static void main(String[] args) throws Exception
//    {
//        Modeling m=new Modeling();
//
//        String datafile="I:\\yw\\rte\\29Mar2016\\Data rte3 - Train_sisa.csv.arff";//I:\\Riset\\2015\\news_aggregator\\5W1H\\dataset90_01072015.arff";//"I:\\Riset\\2014\\news_aggregator\\5W1H\\dataset30_19112014.arff";
//        Instances trainData=m.loadData(datafile);
//        trainData.setClassIndex(0); //jika atr kelas bukan di akhir
//        System.out.println("Load training data ... is completed - "+trainData.classIndex());//+trainData.toSummaryString());
//
//        //weka.filters.supervised.instance.SMOTE -C 1 -K 10 -P 1072.0 -S 1
//
//        //classifier biasa
////        J48 j48=new J48();
////        //j48.buildClassifier(trainData);
////        m.crossValidation(j48, trainData, 10);
//
//
//
//        //classifier tanpa filter
//        //AbstractClassifier baseCls=new AdaBoostM1();
//        //String[] optionsCls = weka.core.Utils.splitOptions("-I 10 -W weka.classifiers.trees.J48 -- -C 0.25 -M 2"); //untuk Adaboost
//        //baseCls.setOptions(optionsCls);
//        //m.crossValidation(baseCls, trainData, 10);
//        //System.out.println("Build model ...");
//        //baseCls.buildClassifier(trainData);
//        //System.out.println("Save model ...");
//        //m.saveModelAndStructure("I:\\Riset\\2015\\news_aggregator\\5W1H\\final\\Adaboost_J48_27092015.mod", baseCls, trainData);
//
//        Remove filter=new Remove();
//        filter.setInputFormat(trainData);
//        FilteredClassifier classifier=new FilteredClassifier();
////        String str="2,4,6,7,13,18,20,21,24,25,26,8";
////        String[] options = weka.core.Utils.splitOptions("-R "+str);//all: -R 2,9,16; subset: -R 2,6,10
////        filter.setOptions(options);
//        classifier.setFilter(filter);
//        AbstractClassifier baseCls=new J48();//SimpleLogistic();
//        classifier.setClassifier(baseCls);
////        double max=m.crossValidation(classifier, trainData, 10);
//        double max=-1;
//        int imax=-1;
//
//        ArrayList<Integer> arr=new ArrayList<>();
//        //backward
////        arr.add(36);//59.63
////        arr.add(17);//60.55
////        arr.add(19);//61.93
////        arr.add(18);//62.84
////        arr.add(15);//63.76
////        arr.add(39);//64.22
////        arr.add(38);//
////        arr.add(16);//65.14
////        arr.add(12);//65.596
////        arr.add(28);//
////        arr.add(26);//
////        arr.add(24);//
////        arr.add(22);//
////        arr.add(10);//
////        arr.add(3);//
//        //J48
////        arr.add(33);//62.098
////        arr.add(7);//62.955
////        arr.add(9);//63.811
////        arr.add(39);//64.45
////        arr.add(38);//65.31
////        arr.add(15);//65.74
////        arr.add(36);//66.17
////        arr.add(16);//66.38
////        arr.add(12);//66.81
////        arr.add(11);//67.02
////        arr.add(28);//
////        arr.add(26);//
////        arr.add(3);//
////        arr.add(34);//67.81
////        arr.add(10);//
////        arr.add(26);//
////        arr.add(23);//74.375
////        arr.add(34);//
////        arr.add(13);//74.5
////        arr.add(10);//
//        //forward
//        arr.add(7);//61.05
//        arr.add(21); //62.63
//        arr.add(22); //65.79
//        arr.add(37);//67.89
//        arr.add(31);//68.42
//        arr.add(36);//
//        arr.add(33);//
//        arr.add(32);//
//        arr.add(26);//
//        arr.add(10);//
//        arr.add(9);//
//        arr.add(3);//
////        arr.add(25);//
////        arr.add(9);
////        arr.add(7);
////        arr.add(10);//71.25
////        arr.add(20); //71.125
////        arr.add(13);
////        arr.add(25);//70.75
////        arr.add(22);//70.5
////        arr.add(26);//arr.add(27);
////        //arr.add(28);
//        for (int i=2;i<=trainData.numAttributes();i++) {
//            String str="";
//            //forward
//            for (int j=2;j<=trainData.numAttributes();j++) {
//                if (arr.indexOf(j)==-1) {
//                    if (i!=j) {
//                        if (str.isEmpty()) str=str+j;
//                        else
//                            str=str+","+j;
//                    }
//                }
//            }
//            //backward
////            for (int j=2;j<=trainData.numAttributes();j++) {
////                if (arr.indexOf(j)==-1) {
////                    if (i==j) {
////                        if (str.isEmpty()) str=str+j;
////                        else
////                            str=str+","+j;
////                    }
////                }
////                else {
////                        if (str.isEmpty()) str=str+j;
////                        else
////                            str=str+","+j;
////                    }
////            }
//            if (arr.indexOf(i)==-1) {//+
////                str=str+Integer.toString(i);
//                System.out.print("Remove "+str);
//                String[] options = weka.core.Utils.splitOptions("-R "+str);//all: -R 2,9,16; subset: -R 2,6,10
//                filter.setOptions(options);
//                try {
//                    classifier.setFilter(filter);
//                    //AbstractClassifier baseCls=new AdaBoostM1();//weka.classifiers.bayes.NaiveBayes
//                    //
//                    //String[] optionsCls = weka.core.Utils.splitOptions("-C 0.5 -M 2"); //untuk J48
//                    //String[] optionsCls = weka.core.Utils.splitOptions("-I 3 -W weka.classifiers.trees.J48 -- -C 0.25 -M 2"); //untuk Adaboost
//                    //String[] optionsCls = weka.core.Utils.splitOptions("-I 3 -W weka.classifiers.functions.SMO");
//                    //weka.classifiers.functions.LibSVM -S 0 -K 2 -D 3 -G 0.0 -R 0.0 -N 0.5 -M 40.0 -C 1.0 -E 0.001 -P 0.1 -model "C:\\Program Files\\Weka-3-7-11" -seed 1
//                    //baseCls.setOptions(optionsCls);
//                    //classifier.setClassifier(baseCls);
//
//                    double acc=m.crossValidation(classifier, trainData, 10);
//                    if (acc>=max) {
//                        imax=i;
//                        max=acc;
//                    }
//                    //m.crossValidation(baseCls, trainData, 10);
//
//    //                System.out.println("Build model ...");
//    //                classifier.buildClassifier(trainData);
//    //                System.out.println("Save model ...");
//    //                m.saveModelAndStructure("I:\\Riset\\2015\\news_aggregator\\5W1H\\Adaboost_02072015.mod", classifier, trainData);
//                } catch (Exception ex) {
//                    Logger.getLogger(Modeling.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        }
//        System.out.println("Hapus atr : "+imax+" - "+max);
//
//            //weka.classifiers.meta.AdaBoostM1 -P 100 -S 1 -I 10 -W weka.classifiers.trees.J48 -- -C 0.25 -M 2
//    }
//
}
