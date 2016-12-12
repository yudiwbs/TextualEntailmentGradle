package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;

import java.io.*;
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
 *   lihat class Proses SubjObjSyntaxNet (tapi class ini lebih baru)
 */


public class ProsesRootSyntaxNet {

    ArrayList<ParagraphDepTree> alParT = new ArrayList<>();   //satu Par artinya satu T yang terdiri dari banyak subkalimat
    ArrayList<ParagraphDepTree> alParH = new ArrayList<>();   //untuk H, sebenarnya H hanya ada satu sentence, tapi biar lebih konsisten
    ArrayList<String> alEntail = new ArrayList<>(); //untuk debug

    public boolean isNumeric(String s) {
        return s.matches("[-+]?\\d*\\.?\\d+");
    }

    private double simKata(String s1, String s2) {
        double out = 0;
        //numeric tidak diproses
        if (!isNumeric(s1) && !isNumeric(s2)) {
            out = vecGlove.similarity(s1.trim(), s2.trim());
        }
        return out;
    }


    WordVectors vecGlove  = null;
    Prepro objPrepro;


    public ProsesRootSyntaxNet (boolean isInit) {

        if (isInit) {

            String fileVecGlove = "D:\\eksperimen\\paragram\\paragram_300_sl999\\paragram_300_sl999\\paragram_300_sl999.txt";
            try {
                try {
                    vecGlove = WordVectorSerializer.loadTxtVectors(new File(fileVecGlove));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            objPrepro = new Prepro();
            objPrepro.loadStopWords("stopwords2", "kata");
        }
    }

    //untuk debug
    public void loadFileEntail() {
        String fileEntail    ="D:\\desertasi\\eksperimen\\train_entail.txt";  //untuk debug
        Scanner scInp = null;
        try {
            scInp = new Scanner(new File(fileEntail));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //load data input supaya tahu terdiri dari berapa sentence untuk setiap par
        while (scInp.hasNextLine()) {
            String ln = scInp.nextLine().trim();
            alEntail.add(ln);
            //System.out.println(ln); //debug
        }
    }




    //IS: alPar sudah dicreate
   //FS: alPar terisi
    //file input adalah file mentah yg menjadi input SyntaxNet
    //sedangkan file output adalah file hasil SyntaxNet (bukan tree)
    public void loadDepTree(String strFileInput, String strFileOut, ArrayList<ParagraphDepTree> alPar) {
        Scanner scInp = null;
        try {
            scInp = new Scanner(new File(strFileInput));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //load data input supaya tahu terdiri dari berapa sentence untuk setiap par
        ParagraphDepTree pd = new ParagraphDepTree();
        alPar.add(pd);
        int cc = 0;
        while (scInp.hasNextLine()) {
            String ln = scInp.nextLine().trim();
            if (ln.equals("")) { //ganti baris, ganti paragraph
                pd = new ParagraphDepTree();
                alPar.add(pd);
                cc++;
            } else {
                pd.addKalimatAsli(ln,cc+1);
            }
        }

        //bersihkan sisa kalau ada kelebihan pd
        Iterator<ParagraphDepTree> iter = alPar.iterator();
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
            for (ParagraphDepTree p:alPar) {
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

    //menghitung kesamaan dua predikat, pasti terdiri atas satu kata?
    //ini dipanggil saat .equals return false
    //digunakan untuk mencari kemiripan pred
    //perlu pake lematisasi?
    public HasilTebak tebakPred(String f1, String f2) {
        HasilTebak out = null;
        double skor = simKata(f1.trim(),f2.trim());
        if (skor>1)      {skor =1;} //karena anehnya  vec.sim bisa menghasilkan: 1.0000001192092896
        else if (skor<0) {skor = 0;} //minimumn 0
        out = new HasilTebak(skor, "");
        return out;
    }



    //menghitung kesamaan dua frase
    //digunakan untuk mencari kemiripan antara subyek, antara obyek
    public HasilTebak tebakSubjObj(String f1, String f2) {
        HasilTebak out = null;

        //cari F1 di dalam F2, jadinya F1 harus lebih pendek dari F2
        //atau bakal sama ya? sigh..
        ArrayList<String> alKataF1;
        ArrayList<String> alKataF2;
        if (f1.length()<f2.length()) {
            alKataF1 = objPrepro.loadKataTanpaStopWords(f1, true, true);
            alKataF2 = objPrepro.loadKataTanpaStopWords(f2, true, true);
        } else  {
            alKataF1 = objPrepro.loadKataTanpaStopWords(f2, true, true);
            alKataF2 = objPrepro.loadKataTanpaStopWords(f1, true, true);
        }


        double totalSkor = 0;
        double skor;
        //loop dari yang paling pendek v1
        for (String v1:alKataF1) {
            double maxSkor = 0; //makin besar makin bagus, makin similar
            String strTercocok="";
            for (String v2:alKataF2) {
                //kalau katanya sama persis, skor harusnya maksimal  (anehnya bisa nol kalau pake vec.sim, mungkin kalau oov)
                if (v1.equals(v2)) {
                    skor = 1;
                } else {
                    skor = simKata(v1,v2);
                    if (skor>1)
                    {skor =1;} //karena anehnya  vec.sim bisa menghasilkan: 1.0000001192092896
                    else if (skor<0) {
                        {skor = 0;} //minimumn 0
                    }
                }

                if (skor>maxSkor) {
                    maxSkor = skor;
                    strTercocok = v2;
                }
            } //end loop v2
            System.out.print(v1 +"-> "+strTercocok+" ");
            System.out.println("("+maxSkor+")");
            totalSkor = totalSkor + maxSkor;
        } //loop v1
        //normalisasi total skor
        //bisa nol karena stopwords
        if (alKataF1.size()!=0) {
            totalSkor = (double) totalSkor / alKataF1.size();
            System.out.println("Skor tebak:" + totalSkor);
            out = new HasilTebak(totalSkor, "");
        } else {
            out = new HasilTebak(0, "");
        }
        return out;
    }

    /*     cari predikat yang paling cocok dulu, baru cari obyeknya
     */
    public void prosesBerdPredikat() {
        //load dulu
        String strFileInputT ="D:\\desertasi\\eksperimen\\input_train_t.txt";
        String strFileOutT   ="D:\\desertasi\\eksperimen\\out_train_t.txt";
        String strFileInputH ="D:\\desertasi\\eksperimen\\input_train_h.txt";
        String strFileOutH   ="D:\\desertasi\\eksperimen\\out_train_h.txt";

        //load dependency tree
        loadDepTree(strFileInputT,strFileOutT,alParT);
        loadDepTree(strFileInputH,strFileOutH,alParH);

        //sudah masuk ke paragraph

        //isi instance+root per sentence
        for (ParagraphDepTree p: alParT) {
            for (SentenceDepTree sd: p.alSenDepTree) {
                sd.prosesRootSubyekObyek(objPrepro);
            }
        }

        //cari instace root per sentence
        for (ParagraphDepTree p: alParH) {
            for (SentenceDepTree sd: p.alSenDepTree) {
                sd.prosesRootSubyekObyek(objPrepro);
            }
        }


        //alPredikat untuk T dan H sudah terisi
        /*
            -prosesnya adalah cari pred di T yang paling mirip dengan pred di H
            -jika sudah ketemu, maka hitung kemiripan intansce, skor inilah yg dipake
            -todo: tangani yg pasif, nanti disimpan jenisnya
         */

        //loop H
        int cc = 0;
        for (ParagraphDepTree parH: alParH) {  //loop untuk semua H
            //debug
            System.out.println("H:"+parH.toString());
            ParagraphDepTree parT = alParT.get(cc); //ambil pasangan par T yang sesuai
            System.out.println("T:"+parT.toString());
            //loop semua sentence (cuma satu sih sebenarnya untuk H)
            for (SentenceDepTree sdH : parH.alSenDepTree) { //satu H pasti satu sentence
                double totalSkor = 0;
                //loop seemua instance (Subj/obj)  - predikat
                for (Predikat rH : sdH.alPredikat) {
                    double maxSkor = 0;
                    String maxStrEntitas = "";
                    String maxStrPredikat = "";
                    System.out.println("Predikat H:"+rH.predikat);
                    System.out.println("Instance H:"+rH.instance);

                    //cari predikat  h yang paling cocok dengan T
                    //loop semua sentence di T
                    for (SentenceDepTree sdT : parT.alSenDepTree) {
                        System.out.println("Proses sentence T:"+sdT.getKalimatAsli());
                        //loop semua instance-Pred T
                        //mungkin perlu diperluas? tidak hanya suby/oby tapi apapun asal verb?
                        //nanti lihat dulu deh hasilnya
                        for (Predikat rT : sdT.alPredikat) {
                            //rH.instance
                            System.out.println("Predikat T:"+rT.predikat);
                            System.out.println("Instance T:"+rT.instance);
                            double skor;
                            int pos = rH.predikat.indexOf(rT.predikat);
                            if (pos == -1) {  //tidak ketemu, gunakan perkiraan
                                HasilTebak ht = tebakPred(rH.predikat, rT.predikat);
                                skor = ht.nilai;
                            } else {
                                skor = 1;
                            }
                            //cari apa terbesar
                            if (skor > maxSkor) {
                                maxSkor = skor;
                                maxStrEntitas  = rT.instance;
                                maxStrPredikat = rT.predikat;
                            }
                        } //for semua elemen T
                    } //for semua sentence di T

                    //dapat predikat yang paling cocok
                    System.out.println("");
                    System.out.println("====> Pasangan Terbaik berd Predikat H-T");
                    System.out.println("H:"+rH.predikat);
                    System.out.println("T:"+maxStrPredikat);
                    System.out.println("Skor pasangan ini:"+maxSkor);

                    //hitung skor kemiripan instancedisini:
                    HasilTebak htInst = tebakSubjObj(rH.instance, maxStrEntitas);
                    System.out.println("Tebak instance untuk instance H-T: "+rH.instance+" -> "+maxStrEntitas);
                    System.out.println("Skor instance:"+htInst.nilai);
                    totalSkor = totalSkor + htInst.nilai;
                } //for semua elemen di H
                cc++;
                double skorRata2Inst = 0;
                if (sdH.alPredikat.size()!=0) {
                    skorRata2Inst = (double) totalSkor / sdH.alPredikat.size();
                }
                System.out.println("Skor rata2 semua predikat:"+skorRata2Inst);
                System.out.println("-----------");
            } //for semua sentence H
        } //for semua par H



        /*
                for (Root r:sd.alRoot) {
                    System.out.println(r);
                }
                System.out.println();

         */

    }

    /*
        idenya mencari objek H di dep tree T, lalu cari parentnya
        parent yang berupa verb baru dibandingkan dengan root
     */

    public void prosesBerdEntitas() {


        //load dulu
        String strFileInputT ="D:\\desertasi\\eksperimen\\input_train_t.txt";
        String strFileOutT   ="D:\\desertasi\\eksperimen\\out_train_t.txt";
        String strFileInputH ="D:\\desertasi\\eksperimen\\input_train_h.txt";
        String strFileOutH   ="D:\\desertasi\\eksperimen\\out_train_h.txt";

        //load dep tree
        loadDepTree(strFileInputT,strFileOutT,alParT);
        loadDepTree(strFileInputH,strFileOutH,alParH);

        //sudah masuk ke paragraph

        //cari pasangan obj/suby-predikat T
        for (ParagraphDepTree p: alParT) {
            for (SentenceDepTree sd: p.alSenDepTree) {
                sd.prosesRootSubyekObyek(objPrepro);
            }
        }

        //cari pasangan obj/suby-predikat H
        for (ParagraphDepTree p: alParH) {
            for (SentenceDepTree sd: p.alSenDepTree) {
                sd.prosesRootSubyekObyek(objPrepro);
            }
        }


        //alPredikat untuk T dan H sudah terisi
        /*
            -prosesnya adalah cari suby/obyek di T yang paling mirip dengan semua obyek/subyek di H
            -jika sudah ketemu, maka hitung kemiripan predikatnya (verb), skor inilah yg dipake
            -todo: tangani yg pasif, nanti disimpan jenisnya
         */

        //loop H
        int cc = 0;
        for (ParagraphDepTree parH: alParH) {  //loop untuk semua H
            //debug
            System.out.println("H:"+parH.toString());
            ParagraphDepTree parT = alParT.get(cc); //ambil pasangan T yang sesuai
            System.out.println("T:"+parT.toString());
            //loop semua sentence (cuma satu)
            for (SentenceDepTree sdH : parH.alSenDepTree) { //sebenarnya satu H pasti satu sentence
                double totalSkor = 0;
                //loop seemua subj/obj - predikat
                for (Predikat rH : sdH.alPredikat) {
                    double maxSkor = 0;
                    String maxStrEntitas = "";
                    String maxStrPredikat = "";
                    System.out.println("Instance H:"+rH.instance);
                    System.out.println("Predikat H:"+rH.predikat);
                    //cari instance (subj/obj) h yang paling cocok dengan T
                    //loop semua sentence di T
                    for (SentenceDepTree sdT : parT.alSenDepTree) {
                        System.out.println("Proses sentence T:"+sdT.getKalimatAsli());
                        //loop semua instance T
                        for (Predikat rT : sdT.alPredikat) {
                            //rH.instance
                            System.out.println("Instance T:"+rT.instance);
                            System.out.println("Predikat T:"+rT.predikat);
                            double skor;
                            int pos = rH.instance.indexOf(rT.instance);
                            if (pos == -1) {  //tidak ketemu, gunakan perkiraan
                                HasilTebak ht = tebakSubjObj(rH.instance, rT.instance);
                                skor = ht.nilai;
                            } else {
                                skor = 1;
                            }
                            //cari apa terbesar
                            if (skor > maxSkor) {
                                maxSkor = skor;
                                maxStrEntitas = rT.instance;
                                maxStrPredikat = rT.predikat;
                            }
                        } //for semua elemen T
                    } //for semua sentence di T

                    //dapat entitas yang paling cocok
                    System.out.println("");
                    System.out.println("====> Pasangan Terbaik Instance H-T");
                    System.out.println("H:"+rH.instance);
                    System.out.println("T:"+maxStrEntitas);
                    System.out.println("Skor pasangan instance:"+maxSkor);
                    //hitung skor kemiripan root disini:
                    HasilTebak htPred = tebakPred(rH.predikat, maxStrPredikat);
                    System.out.println("Tebak predikat untuk instance H-T: "+rH.predikat+" -> "+maxStrPredikat);
                    System.out.println("Skor predikat:"+htPred.nilai);
                    totalSkor = totalSkor + htPred.nilai;

                    //nanti ditotal
                } //for semua elemen di H
                cc++;
                double skorRata2Pred = 0;
                if (sdH.alPredikat.size()!=0) {
                    skorRata2Pred = (double) totalSkor / sdH.alPredikat.size();
                }
                System.out.println("Skor rata2 semua predikat:"+skorRata2Pred);
                System.out.println("-----------");
            } //for semua sentence H
        } //for semua par H



        /*
                for (Root r:sd.alRoot) {
                    System.out.println(r);
                }
                System.out.println();

         */





        //cari subyek dan obyek di dep tree, lalu cari parentnya yg bisa dibandingkan dengan root
        //if (d.rel.equals("iobj")||d.rel.equals("dobj")||d.rel.equals("pobj")) {
        //(d.rel.equals("nsubj")||d.rel.equals("nsubjpass")
    }

    //entah kenapa error kalau pake scanner
    public void prosesFileOutWeka() {
        //String fileInput   = "D:\\desertasi\\eksperimen\\final_ProsesRootSyntaxNet.txt";
        String fileInput   = "D:\\desertasi\\eksperimen\\pred_ProsesRootSyntaxNet.txt";


        //error kalau pake scanner (hanya keambil sebagian, hati2 dengan output log IntelliJ!
        //StringBuilder outYgDipilih = new StringBuilder(); //
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(new File(fileInput)));
            String line;
            while((line = br.readLine()) != null) {
                if (line.contains("Skor rata2 semua")) {
                    String[] arrStr = line.split(":");
                    double skor = Double.parseDouble(arrStr[1]);
                    System.out.println(skor);
                }
            } //end while
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //entah kenapa error kalau pake scanner
    public void prosesFileOut() {
        loadFileEntail(); //arIsEntail terisi
        //String fileInput   = "D:\\desertasi\\eksperimen\\final_ProsesRootSyntaxNet.txt";
        String fileInput   = "D:\\desertasi\\eksperimen\\pred_ProsesRootSyntaxNet.txt";


        //error kalau pake scanner (hanya keambil sebagian, hati2 dengan output log IntelliJ!
        StringBuilder outYgDipilih = new StringBuilder(); //
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(new File(fileInput)));
            String line;
            int cc = 0;
            while((line = br.readLine()) != null) {
                //result.add(availalbe);
                outYgDipilih.append(line); //add semua
                outYgDipilih.append(System.lineSeparator());
                if (line.contains("Skor rata2 semua predikat")) {
                    //System.out.println(line);
                    outYgDipilih.append("entail:"+alEntail.get(cc)); //add semua
                    outYgDipilih.append(System.lineSeparator());
                    String[] arrStr = line.split(":");
                    double skor = Double.parseDouble(arrStr[1]);
                    //hanya print yg entail dan skornya rendah
                    if (alEntail.get(cc).equals("true") && skor <= 0.1) {
                        System.out.println(outYgDipilih.toString());
                    }
                    outYgDipilih = new StringBuilder(); //reset pengambilan data
                    cc++;
                } else {
                    //System.out.println("-->"+line);
                }

            } //end while
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }




    public static void main(String[] args) {

        //ProsesRootSyntaxNet prs = new ProsesRootSyntaxNet(true);
        //prs.prosesBerdPredikat();
        //prs.prosesBerdEntitas(); //pastikan gunakan constructor dengan parameter true

        ProsesRootSyntaxNet prs = new ProsesRootSyntaxNet(false);
        //prs.prosesFileOut();
        prs.prosesFileOutWeka();
    }
}
