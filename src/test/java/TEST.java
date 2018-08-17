import java.sql.Timestamp;
import java.util.Date;
import java.util.TimeZone;
public class TEST {
    public static void main(){
        Timestamp timestamp=new Timestamp(System.currentTimeMillis());
        timestamp.toGMTString();
        String hour=timestamp.toString();
        Date date=new Date();
        System.out.print(date.getHours());


    }
}
