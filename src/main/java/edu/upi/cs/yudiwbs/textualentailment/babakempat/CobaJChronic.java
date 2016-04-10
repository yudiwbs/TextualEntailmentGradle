package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import com.mdimension.jchronic.Chronic;
import com.mdimension.jchronic.Options;
import com.mdimension.jchronic.utils.Span;

import java.util.Calendar;

/**
 * Created by yudiwbs on 08/04/2016.
 *
 * penting: library yang digunakan sudah diedit,
 * karena hanya berlaku untuk tahun >1900
 *
 * perbaikan ada di source-java/JChronic
 *
 */
public class CobaJChronic {
    public static void main(String[] args) {
        Span time;
        Options opt = new Options();
        //opt.setNow(TIME_2006_08_16_14_00_00);
        //opt.setCompatibilityMode(true);
        //opt.setGuess(true);
        //time  = Chronic.parse("August 1799", opt);
        //opt.setDebug(true);

        /*
             yang masih gagal diparsing:

Tgl tidak bisa diparsing!:1972 to 1975
Tgl tidak bisa diparsing!:the 1950s
Tgl tidak bisa diparsing!:1980s
Tgl tidak bisa diparsing!:November 9 , 1989 , the day
Tgl tidak bisa diparsing!:May-June 1940
Tgl tidak bisa diparsing!:17th-Century
Tgl tidak bisa diparsing!:1900 to 1946
Tgl tidak bisa diparsing!:1811 to 1886
Tgl tidak bisa diparsing!:1986 to 1993
Tgl tidak bisa diparsing!:November , 1989
Tgl tidak bisa diparsing!:the twentieth century
Tgl tidak bisa diparsing!:the early hours of April 15 , 1912
Tgl tidak bisa diparsing!:30th anniversary of mankind
Tgl tidak bisa diparsing!:the 1960s
Tgl tidak bisa diparsing!:the early 1990s

         */
        //time  = Chronic.parse("August 1799", opt);
        time  = Chronic.parse("1986 to 1993", opt);
        Calendar begin = time.getBeginCalendar();
        System.out.println("begin:"+begin.getTime());
        Calendar end = time.getBeginCalendar();
        System.out.println("end:"+end.getTime());
    }
}
