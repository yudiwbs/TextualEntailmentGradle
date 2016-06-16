package edu.upi.cs.yudiwbs.textualentailment.babakempat;

/**
 * Created by yudiwbs on 03/06/2016.
 */
public class HasilTebak {
    double nilai;   //nilai prediksi 0 sd 1. 1 artinya semakin mirip
    String tebakan; //yang paling mendkati
    public HasilTebak(double nilai, String tebakan) {
        this.nilai = nilai;
        this.tebakan = tebakan;
    }
}