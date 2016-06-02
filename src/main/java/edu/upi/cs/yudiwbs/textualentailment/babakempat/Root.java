package edu.upi.cs.yudiwbs.textualentailment.babakempat;

/**
 * Created by yudiwbs on 02/06/2016.
 */
public class Root{
    //menyimpan root dan subyek/obyek
    String instance;  //subyek atau obyek yg parentya root
    String root; //predikat
    //int tipeIntansce; //1: subyek, 2:obyek <- belum digunakan
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Suby/Oby:"+instance);
        sb.append(System.lineSeparator());
        sb.append("predikat:"+root);
        return sb.toString();
    }
}
