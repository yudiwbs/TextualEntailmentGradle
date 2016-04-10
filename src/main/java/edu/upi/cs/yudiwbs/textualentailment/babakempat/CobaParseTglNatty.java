package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import com.joestelmach.natty.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by yudiwbs on 07/04/2016.
 *
 * web: http://natty.joestelmach.com/
 *
 * kurang bagus hasilnyappake: jZChronic
 */
public class CobaParseTglNatty {

    public static void main(String[] args) {
        Parser parser = new Parser();
        List<DateGroup> groups = parser.parse("On May 17, 2005, the National Assembly of Kuwait passed, " +
                "by a majority of 35 to 23 (with 1 abstention), on August 1799 an amendment to its electoral law that" +
                " would allow women to vote and to stand as parliamentary candidates.");
        //List<DateGroup> groups = parser.parse("August of 1799");
        for(DateGroup group:groups) {
            List<Date> dates = group.getDates();
            for (Date d: dates) {
                System.out.println(d);
            }
            int line = group.getLine();
            System.out.println("line:"+line);
            int column = group.getPosition();
            System.out.println("column:"+column);

            String matchingValue = group.getText();
            System.out.println("matchingValue:"+matchingValue);

            String syntaxTree = group.getSyntaxTree().toStringTree();
            System.out.println("syntaxTree :"+syntaxTree );

            Map parseMap = group.getParseLocations();

            boolean isRecurreing = group.isRecurring();
            Date recursUntil = group.getRecursUntil();
        }
    }
}



