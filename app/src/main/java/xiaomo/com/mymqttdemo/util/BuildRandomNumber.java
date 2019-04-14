package xiaomo.com.mymqttdemo.util;

import java.util.Random;

/**
 * Created by xiaomo
 * Date on  2019/4/14
 *
 * @Desc
 */

public class BuildRandomNumber {
    public static String createGUID() {
        String result = "00000000";
        try {
            char[] content = { '0', '1', '2',
                    '3', '4', '5', '6', '7', '8', '9' };
            Random random = new Random();
            char[] charArray = result.toCharArray();
            for (int i = 0; i < charArray.length; i++) {
                if (charArray[i] == '0') {
                    charArray[i] = content[random.nextInt(10)];
                }
            }
            result = String.valueOf(charArray);
        } catch (Exception ex) {
        }
        return result;
    }
}
