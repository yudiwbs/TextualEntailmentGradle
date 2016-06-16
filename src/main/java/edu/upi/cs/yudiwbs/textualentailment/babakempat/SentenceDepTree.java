package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import javax.xml.crypto.Data;
import java.util.ArrayList;

/**
 * Created by yudiwbs on 24/05/2016.
 * merepresentasikan sentece
 * terkait denga DataDepTree dan ParsingSyntaxNet
 *
 * untuk T, bisa saja terdiri atas beberapa sentence, lihat ParagraphDepTree
 */

public class SentenceDepTree {


    ArrayList<DataDepTree> alDataDepTree = new ArrayList<>();

    //cari obyek & subyek nanti kelas prosesSubjObjSyntaxNet bisa digabung ke sini
    ArrayList<Predikat> alPredikat = new ArrayList<>();

    //ambil subyek, obyek dan root:
    //FS: alRoot terisi
    //objek prepro dipassing agar tidak berat untuk setiap pemanggilan
    public void prosesRootSubyekObyek(Prepro pp) {
        //bisa lebih dari satu, misal suby pasif (atau dipisah?)
        //ArrayList<String> alSubyekOby = new ArrayList<>();
        //ArrayList<String> alObyek = new ArrayList<>();
        String strRoot="";

        //loop untuk semua kata
        for (DataDepTree d:alDataDepTree) {
            //subyek & obyek diperlakukan sama
            if (
                d.rel.equals("nsubj") || d.rel.equals("nsubjpass") ||
                d.rel.equals("iobj")  || d.rel.equals("dobj")      ||
                d.rel.equals("pobj")
                ) {


                ArrayList<DataDepTree> alData = new ArrayList<>();
                alData.add(d);

                //cari parent yang verb, verb inilah yang paling terkait dengan suby/obyek
                String verbParent = this.getVerbParent(d.parent);
                if (verbParent.equals("")) {
                    continue; //tidak diproses kalu tidak ada rootnya
                }
                Predikat r = new Predikat();
                r.predikat = verbParent;
                //cari semua childnya kalau ada, jadi bisa diambil kalau frase
                ArrayList<DataDepTree> alChild = this.getChild(d.id);
                alData.addAll(alChild);
                //susun ulang berdasarikan id (urutan aslinya)
                alData.sort((o1, o2) -> o1.idInt - o2.idInt);
                StringBuilder sb = new StringBuilder();
                for (DataDepTree dS : alData) {
                    //System.out.println(dS.kata);
                    sb.append(dS.kata);
                    sb.append(" ");
                }
                String subyekOby = sb.toString().trim();

                //buang stopwords dan selain alpha numeric
                subyekOby = pp.loadKataTanpaStopWordstoString(subyekOby, true, true);
                r.instance = subyekOby;

                if (!r.instance.equals("")) {
                //cek subyek/obyek tidak boleh kosong. Misalnya ada subyek which,
                // kena stopword rem jadi hilang
                    alPredikat.add(r);
                }
                //tidak dicek duplikasi karena yang penting adalah rootnya
                //kecuali kalau rootnya juga dobel?
                //nanti lihat dulu

                //cek apakah subyek/oby sudah ada sebelumnya, contoh
                //sudah ada ini: assassination six jesuits
                //lalu masuk lagi : six jesuits
                /*
                boolean isKetemu = false;
                for (String s : alSubyekOby) {
                    if (s.contains(subyekOby)) {
                        isKetemu = true;
                        break;
                    }
                }
                //tidak ada duplikasi? masuk
                if (!isKetemu) {
                    alSubyekOby.add(subyekOby);

                }
                */

            } //if subyek
        } //loop semua kata
    }

    //
    private String getVerbParent(String idParent) {
        //VBD, VBZ, VBP, VBN, VBG
        //bentuk umumnya VERB
        String out = "";
        //loop semua untuk cari parent, mungkin nanti hashmap?
        for (DataDepTree dt: alDataDepTree) {

            if (dt.id.equals(idParent)) { //parent cocok
                if (dt.posUmum.equals("VERB")) {  //ketemu parent verb?
                    out = dt.kata;
                    break;
                } else //bukan verb, rekursif
                {
                    if (dt.parent.equals(0)) {
                        //kondisi berhenti rekursif, mentok sampai ujung
                        return ""; //tidak ditemukan
                    } else {
                        //rekursif, cari prent yang verb
                        out = getVerbParent(dt.parent);
                        break;
                    }
                }
            }
        }
        return out;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (DataDepTree d: alDataDepTree) {
            sb.append(d.kata+":"+d.rel);
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

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
