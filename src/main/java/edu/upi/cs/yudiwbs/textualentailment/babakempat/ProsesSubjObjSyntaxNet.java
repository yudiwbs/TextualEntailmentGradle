package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by yudiwbs on 24/05/2016.
 * berdasarkan hasil dep parsing Syntaxnet, tentukan subj dan obj
 *
 *
 *


 masih bug:
  id kalimat:4
 30 die in a bus collision in Uganda .
 T:A bus collision with a truck in Uganda has resulted in at least 30 fatalities and has left a further 21 injured.
 Subyek
 subyek:30
 Pos subyek:64
 Obyek
 obyek:a bus collision in Uganda
 Pos obyek:-1
 obyek:Uganda
 Pos obyek:32


 persen digabung lagi (hilangkn spasi)
 id kalimat:7
 80 % approve of Mr. Bush .
 T:Mrs. Bush's approval ratings have remained very high, above 80%,
 even as her husband's have recently dropped below 50%.
 Subyek
 subyek:80 %
 Pos subyek:-1
 Obyek
 obyek:Mr. Bush
 Pos obyek:-1

 complete skull (parsial? a complete)
 id kalimat:8
 A complete Dakosaurus was discovered by Diego Pol .
 T:Recent Dakosaurus research comes from a complete skull found in Argentina in 1996,
 studied by Diego Pol of Ohio State University, Zulma Gasparini of Argentinas National
 University of La Plata, and their colleagues.
 Subyek
 subyek:A complete Dakosaurus
 Pos subyek:-1
 Obyek
 obyek:Diego Pol
 Pos obyek:94

 parsial
 id kalimat:28
 Burns surgeons approve Dr Wood 's spray-on skin .
 T:Dr Wood led a courageous and committed team in the fight to save 28 patients suffering
 from between two and 92 per cent body burns, deadly infections and delayed shock.
 As well as receiving much praise from both her own patients and the media, she also
 attracted controversy among other burns surgeons due to the fact that spray-on skin
 had not yet been subjected to clinical trials.
 Subyek
 subyek:Burns surgeons
 Pos subyek:-1
 Obyek
 obyek:Dr Wood 's spray-on skin
 Pos obyek:-1

 susah:
 A pro-women amendment == an amendment to its electoral law that would allow women to vote ..
 id kalimat:9
 A pro-women amendment was rejected by the National Assembly of Kuwait .
 T:On May 17, 2005, the National Assembly of Kuwait passed, by a majority of 35 to 23
 (with 1 abstention), an amendment to its electoral law that would allow women to vote and
 to stand as parliamentary candidates.
 Subyek
 subyek:A pro-women amendment
 Pos subyek:-1
 Obyek
 obyek:the National Assembly of Kuwait
 Pos obyek:17
 obyek:Kuwait
 Pos obyek:42

 7 june == june 7
 id kalimat:15
 Alfredo Cristiani visits Mexico on June 7 .
 T:Cauhtemoc Cardenas said during a news conference on 7 June that the visit to Mexico by Salvadoran president Alfredo Cristiani is a visit by "a repressive ruler who oppresses a large sector of his people."
 Subyek
 subyek:Alfredo Cristiani
 Pos subyek:108
 Obyek
 obyek:Mexico
 Pos obyek:77
 obyek:June 7
 Pos obyek:-1



 singkatan nama
 Capt. Robert F. Scott  =  Capt. Scott
 id kalimat:30
 Capt. Scott reached Scott Island in December 1902 .
 T:Scott Island was discovered and landed upon in December 1902 by
 Captain William Colbeck commander of the Morning, relief ship
 for Capt. Robert F. Scott's expedition.
 Subyek
 subyek:Capt. Scott
 Pos subyek:-1
 Obyek
 obyek:Scott Island
 Pos obyek:0
 obyek:December 1902
 Pos obyek:47

 nama: neil amstrong = armstrong
 id kalimat:460
 Neil Armstrong was the first man who landed on the Moon .
 T:spacecraft commander apollo xi first manned lunar landing mission armstrong first man walk moon one small step man one giant leap mankind historic words man dream ages fulfilled
 T prepro:spacecraft commander apollo xi first manned lunar landing mission armstrong first man walk moon one small step man one giant leap mankind historic words man dream ages fulfilled
 subyek:neil armstrong
 Pos subyek:-1

 buang tanda baca
 id kalimat:35
 Clark is a relative of Jones ' .
 T:The car which crashed against the mail-box belonged to James Clark, 68, an acquaintance of James Jones' family.
 Subyek
 subyek:Clark
 Pos subyek:61
 Obyek
 obyek:Jones '
 Pos obyek:-1

 ada sinonim dalam objek
 id kalimat:38
 Cristiani is accused of the assassination of six Jesuits .
 T:He said that "there is evidence that Cristiani was involved in the murder of the six Jesuit priests" which occurred on 16 November in San Salvador.
 Subyek
 subyek:Cristiani
 Pos subyek:37
 Obyek
 obyek:the assassination of six Jesuits
 Pos obyek:-1
 obyek:six Jesuits
 Pos obyek:-1

 stopwords the
 id kalimat:42
 David Cameron works as the shadow education secretary .
 T:Parents also had to contribute "much more fully", while "coasting" schools would be tackled, Ms Kelly told MPs. But shadow education secretary David Cameron, who backed some ideas, said other parts were a "complete muddle".
 Subyek
 subyek:David Cameron
 Pos subyek:143
 Obyek
 obyek:the shadow education secretary
 Pos obyek:-1

 stopwords: a
 id kalimat:44
 Dean invented a revolver .
 T:James Kerr had been the foreman for the Deane, Adams and Deane gun factory. Robert Adams, one of the partners and inventor of the Adams revolver, was Kerr's cousin.
 Subyek
 subyek:Dean
 Pos subyek:40
 Obyek
 obyek:a revolver
 Pos obyek:-1


 EU budget = budget for the EU
 id kalimat:46
 Diplomats agree on EU budget .
 T:The BBC's Tim Franks says frustrated British diplomats insist there have been
 several achievements but there is no doubt that the continued delay in reaching
 agreement on a budget for the EU hangs over all discussions.
 Subyek
 subyek:Diplomats
 Pos subyek:-1
 Obyek
 obyek:EU budget
 Pos obyek:-1

 a treatment for burns victims = spray on skin for burns victims, a treatment

 id kalimat:47
 Dr Fiona Wood has invented a treatment for burns victims .
 T:She has become world renowned for her patented invention of spray on skin for burns victims,
 a treatment which is continually developing. Via her research, Fiona found that scarring is
 greatly reduced if replacement skin could be provided within 10 days. As a burns specialist
 the holy grail for Dr Fiona Wood is 'scarless, woundless healing'.

 Subyek
 subyek:Dr Fiona Wood
 Pos subyek:296
 Obyek
 obyek:a treatment for burns victims
 Pos obyek:-1
 obyek:burns victims
 Pos obyek:78

 variasi kalimat:
 the government of the United States = the governments of both the United States...
 the Civil War = the U.S. Civil War
 id kalimat:48
 During the Civil War the government of the United States bought arms from Britain .
 T:The British government did not initially purchase the weapon and civilian sales
 were modest. However the U.S. Civil War began in 1860 and the governments of both
 the United States and the Confederacy began purchasing arms in Britain.
 Subyek
 subyek:the government of the United States
 Pos subyek:-1
 Obyek
 obyek:the Civil War
 Pos obyek:-1
 obyek:the United States
 Pos obyek:162
 obyek:arms
 Pos obyek:217
 obyek:Britain
 Pos obyek:225

 terrorism = terrorist
 id kalimat:50
 El-Nashar is accused of terrorism .
 T:"I want to go back again. But I am afraid, honestly, I am afraid.
 Propaganda against me made people think I am terrorist.", said el-Nashar.
 Subyek
 subyek:El-Nashar
 Pos subyek:-1
 Obyek
 obyek:terrorism
 Pos obyek:-1

 Europe 's first pyramid = the first European pyramid
 id kalimat:54
 Europe 's first pyramid has been discovered near Sarajevo .
 T:Bosnia's leading Muslim daily Dnevni Avaz writes excitedly about
 "a sensational discovery" of "the first European pyramid" in the central
 town of Visoko, just north of Sarajevo.
 Subyek
 subyek:Europe 's first pyramid
 Pos subyek:-1
 Obyek
 obyek:Sarajevo
 Pos obyek:168


 a blackout in the capital = a blackout throughout most of the capital
 id kalimat:57
 FMLN caused a blackout in the capital .
 T:On the morning of 1 June, there was a blackout throughout most of the capital caused by urban commandos of the Farabundo Marti National Liberation Front (FMLN).
 T prepro:morning june blackout throughout capital caused urban commandos farabundo marti national liberation front fmln
 obyek:blackout capital
 Pos obyek:-1


 married = wife
 id kalimat:468
 The name of George H.W. Bush 's wife is Barbara .
 T:George Herbert Walker Bush (born June 12, 1924) is the former 41st President of the United States of America. Almost immediately upon his return from the war in December 1944, George Bush married Barbara Pierce.
 T prepro:george herbert walker bush born june 12 1924 former 41st president united states america almost immediately upon return war december 1944 george bush married barbara pierce
 subyek:name george bush wife
 Pos subyek:-1
 obyek:george bush wife
 Pos obyek:-1

 nama
 id kalimat:471
 Alfred Nobel is the inventor of dynamite .
 T:In 1867, Nobel obtained a patent on a special type of nitroglycerine, which he called "dynamite". The invention quickly proved its usefulness in building and construction in many countries.
 T prepro:1867 nobel obtained patent special type nitroglycerine called dynamite invention quickly proved usefulness building construction countries
 subyek:alfred nobel
 Pos subyek:-1

 tanggal june 1944 == june 6th 1944
 id kalimat:482
 The Normandy landings took place in June 1944 .
 T:The D-Day was the largest seaborne invasion force ever assembled headed for France on June 6th 1944.
 T prepro:day largest seaborne invasion force ever assembled headed france june 6th 1944
 subyek:normandy landings  <-- yang ini udah bener
 Pos subyek:-1
 obyek:place
 Pos obyek:-1
 obyek:june 1944
 Pos obyek:-1

 objek Christian Democratic Union tidak ketemu, salah parser
 id kalimat:483
 The name of Helmut Kohl 's political party is the Christian Democratic Union .
 T:Kohl participated in the late stage of WWII as a teenage soldier. He joined the Christian-Democratic Union (CDU) in 1947.
 T prepro:kohl participated late stage wwii teenage soldier joined christian democratic union cdu 1947
 subyek:name helmut kohl political party
 Pos subyek:-1
 obyek:helmut kohl political party
 Pos obyek:-1


 */

public class ProsesSubjObjSyntaxNet {

    /*
       contoh:

       id kalimat:4
        30 die in a bus collision in Uganda .
        T:A bus collision with a truck in Uganda has resulted in at least 30 fatalities and has left a further 21 injured.
        T prepro:bus collision truck uganda resulted least 30 fatalities left 21 injured
        obyek:bus collision uganda

        contoh:
        bus collision uganda == bus collision truck uganda  (perhatikan ada truck)
        output adalah: bus collision truck uganda   (mungkin posisi awal dan akhir?)

        contoh lain:
         T:On the morning of 1 June, there was a blackout throughout most of the capital caused by
            urban commandos of the Farabundo Marti National Liberation Front (FMLN).

         obyek: blackout in the capital

         output: a blackout throughout most of the capital

        cat: subkal dan kalimat sudah diprepro

     */

    public String tebak(String kalimat,String subKal) {
        String out ="";
        //bergerak berdasarkan window, besarnya jumkata di subkal + 50%

        //parsing kata
        ArrayList<String> alSubkal = new ArrayList<>();
        Scanner scSubKal  = new Scanner(subKal);
        while (scSubKal.hasNext()) {
            String kata = scSubKal.next();
            alSubkal.add(kata);
        }

        ArrayList<String> alKal = new ArrayList<>();
        Scanner scKal  = new Scanner(kalimat);
        while (scKal.hasNext()) {
            String kata = scKal.next();
            alKal.add(kata);
        }

        int ukuranWindow = alSubkal.size()+2;
        //System.out.println("ukuran window"+ukuranWindow);

        ArrayList<String> alWindow = new ArrayList<>();

        //loop untuk semua posisi window
        //  0  1  2 3 4 5 6 7 8 9
        // [0  1] 2
        //  0 [1  2]
        //  ...
        //                   [8 9]
        double  maxSkor=-999;
        String maxWindow="";
        ArrayList<String> maxAlWindow = new ArrayList<>();
        for (int i=0;i<=alKal.size()-ukuranWindow;i++) { //looop semua iwndows

            //buat window berisi kata2
            alWindow.clear();
            StringBuilder sbWindow = new StringBuilder();
            for(int j=i; j<i+ukuranWindow ; j++) {
                alWindow.add(alKal.get(j));
                sbWindow.append(alKal.get(j));
                sbWindow.append(" ");
            }
            //System.out.println("window:"+sbWindow.toString());

            //hitung kemiripan isi window dengan alSubkal,
            // nanti harusnya bisa digunakan word sim spt word2vec
            int jumCocok = 0;
            for (String strSubKal:alSubkal) {
                boolean isKetemu = false;
                for (String strWindow:alWindow) { //loop dalam window
                    if (strSubKal.equals(strWindow)) {
                       jumCocok++;
                       isKetemu = true;
                       break;
                    }
                }
                if (!isKetemu) {
                    //tidak ketemu
                    //penalti? pake word2vec?
                }
            }

            double skor = (double) jumCocok / alSubkal.size();
            if (skor>maxSkor) {
                maxSkor = skor;
                maxAlWindow.clear();
                maxAlWindow.addAll(alWindow);
                maxWindow = sbWindow.toString().trim();
            }
        }


        //trim depan belakang untuk kata yg tidak ada di subkal
        //contoh: bus collision truck uganda resulted,bus collision uganda  =>
        //        bus collision truck uganda
        //resulted dibuang tidak ada di subkalimat

        //buang depan
        for (int i=0; i<maxAlWindow.size();i++) {
            boolean isKetemu = false;
            for (String strSubKal:alSubkal) {
                if (maxAlWindow.get(i).equals(strSubKal))  {
                    isKetemu = true;
                    break;
                }
            }
            if (!isKetemu) {
                //tidak ada di subkal, hapus saja
                maxAlWindow.remove(i);
            } else { //ada, ketemu batas, stop
               break;
            }
        }

        //buang belakang
        for (int i=maxAlWindow.size()-1; i>=0;i--) {
            boolean isKetemu = false;
            for (String strSubKal:alSubkal) {
                if (maxAlWindow.get(i).equals(strSubKal))  {
                    isKetemu = true;
                    break;
                }
            }
            if (!isKetemu) {
                //tidak ada di subkal, hapus saja
                maxAlWindow.remove(i);
            } else { //ada, ketemu batas, stop
                break;
            }
        }

        StringBuilder sb = new StringBuilder();
        for (String s:maxAlWindow) {
            sb.append(s);
            sb.append(" ");
        }
        System.out.println("Kalimat:"+kalimat); //nanti ada batas?
        System.out.println("Yg dicari:"+subKal); //nanti ada batas?
        System.out.println("max window awal:"+maxWindow);
        maxWindow = sb.toString().trim();
        System.out.println("max window setelah trim:"+maxWindow);
        System.out.println("max skor:"+maxSkor); //nanti ada batas?

        return maxWindow;
    }







    public void proses() {
        //load T mentah (tdk displit)
        String fileT  = "D:\\desertasi\\eksperimen\\t.txt";
        ArrayList<String> alT = new ArrayList<>();

        Prepro pp = new Prepro();
        pp.loadStopWords("stopwords2","kata");



        try {
            Scanner sc = new Scanner(new File(fileT));
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                alT.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ParsingSyntaxNet psn = new ParsingSyntaxNet();
        ArrayList<SentenceDepTree> alSentence = psn.load("D:\\desertasi\\eksperimen\\out_ver4_h.txt");

        System.out.println("Jumlah kal:"+alSentence.size());

        int cc=0;
        for (SentenceDepTree sentence: alSentence) {
            //System.out.println("id kalimat:"+cc);
            //System.out.println(sentence.getKalimatAsli()); //h
            //String subyek="";
            //String obyek="";

            //bisa lebih dari satu, misal suby pasif (atau dipisah?)
            ArrayList<String> alSubyek = new ArrayList<>();
            ArrayList<String> alObyek = new ArrayList<>();

            //loop semua dep dalam satu kalimat
            for (DataDepTree d: sentence.alDataDepTree) {
                if (d.rel.equals("nsubj")||d.rel.equals("nsubjpass")) {
                    ArrayList<DataDepTree> alData = new ArrayList<>();
                    alData.add(d);
                    //cari semua childnya kalau ada
                    ArrayList<DataDepTree> alChild = sentence.getChild(d.id);
                    alData.addAll(alChild);
                    //susun ulang berdasarikan id
                    alData.sort((o1, o2) -> o1.idInt - o2.idInt );
                    StringBuilder sb = new StringBuilder();
                    for (DataDepTree dS: alData) {
                        //System.out.println(dS.kata);
                        sb.append(dS.kata);
                        sb.append(" ");
                    }
                    String subyek = sb.toString().trim();

                    //buat stopwords dan selain alpha numeric
                    subyek = pp.loadKataTanpaStopWordstoString(subyek,true,true);
                    alSubyek.add(subyek);
                    //System.out.println("Subyek:"+subyek);
                } else
                    //masih copy paste, nanti dirapikan
                    if (d.rel.equals("iobj")||d.rel.equals("dobj")||d.rel.equals("pobj")) {
                        ArrayList<DataDepTree> alData = new ArrayList<>();
                        alData.add(d);
                        //cari semua childnya kalau ada
                        ArrayList<DataDepTree> alChild = sentence.getChild(d.id);
                        alData.addAll(alChild);
                        //susun ulang berdasarikan id
                        alData.sort((o1, o2) -> o1.idInt - o2.idInt );
                        StringBuilder sb = new StringBuilder();
                        for (DataDepTree dS: alData) {
                            //System.out.println(dS.kata);
                            sb.append(dS.kata);
                            sb.append(" ");
                        }
                        String obyek = sb.toString().trim();
                        obyek = pp.loadKataTanpaStopWordstoString(obyek,true,true);
                        alObyek.add(obyek);
                        //System.out.println("Obyek:"+obyek);
                    }
            } // for (DataDepTree d: sentence.alDataDepTree)
            //obj dan subj H didapat, cari batasan untuk T
            String t = alT.get(cc);
            String tPrepro = pp.loadKataTanpaStopWordstoString(t,true,true);

            /*
            System.out.println("id kalimat:"+cc);
            System.out.println(sentence.getKalimatAsli()); //h
            System.out.println("T:"+t);
            System.out.println("T prepro:"+tPrepro);
            */
            //di proses stopword


            //proses pencarian objek dan subyek di T
            //System.out.println("Subyek");
            boolean adaYgTdkKetemu = false;
            StringBuilder sbOut = new StringBuilder();
            for (String subyek: alSubyek) {

                int posSubj = tPrepro.indexOf(subyek);
                //debug, hanya tampilkan yang tidak ketemu
                if (posSubj==-1) {  //tidak ketemu, gunakan perkiraan?
                    //System.out.println("subyek:"+subyek);
                    //System.out.println("Pos subyek:"+posSubj);
                    adaYgTdkKetemu = true;

                    //coba ditebak

                    String subjTebak = tebak(tPrepro,subyek);
                    sbOut.append("Subyek tebak:"+subjTebak);
                    sbOut.append(System.lineSeparator());
                }
                sbOut.append("subyek:"+subyek);
                sbOut.append(System.lineSeparator());
                sbOut.append("Pos subyek:"+posSubj);
                sbOut.append(System.lineSeparator());
            }

            //System.out.println("Obyek");
            for (String obyek: alObyek) {
                int posObj  = tPrepro.indexOf(obyek);
                if (posObj==-1) {
                    //System.out.println("obyek:"+obyek);
                    //System.out.println("Pos obyek:"+posObj);
                    adaYgTdkKetemu = true;
                    //proses tebakan
                    //prosesTebak;
                    String obyekTebak = tebak(tPrepro,obyek);
                    sbOut.append("Obyek tebak:"+obyekTebak);
                    sbOut.append(System.lineSeparator());
                }
                sbOut.append("obyek:"+obyek);
                sbOut.append(System.lineSeparator());
                sbOut.append("Pos obyek:"+posObj);
                sbOut.append(System.lineSeparator());
            }

            //print hanya tidak ketemu
            if (adaYgTdkKetemu) {
                System.out.println("");
                System.out.println("id kalimat:"+cc);
                System.out.println(sentence.getKalimatAsli()); //h
                System.out.println("T:"+t);
                System.out.println("T prepro:"+tPrepro);
                System.out.println(sbOut.toString());
            }
            cc++;
        }
    }

    public static void main(String[] args) {
        ProsesSubjObjSyntaxNet ps = new ProsesSubjObjSyntaxNet();
        ps.proses();
        //ps.tebak("morning june blackout throughout capital caused urban commandos farabundo marti national " +
        //        "liberation front fmln","blackout throughout capital ");
        //ps.tebak("collision truck uganda resulted least 30 fatalities left 21 injured",
        //         "bus collision uganda");
        //ps.tebak("collision truck uganda resulted least 30 fatalities left 21 injured",
        //         "bus collision uganda");
    }

}
