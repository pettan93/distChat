package distChat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static String formatDate(Date d, String format) {
        return d != null ? new SimpleDateFormat(format).format(d) : null;
    }

}
