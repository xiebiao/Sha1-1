import static org.junit.Assert.*;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;


public class sha1Test {

    @Test
        public void test() {
            assertEquals(actual_hex_value("Hello") + " != " + my_value("Hello"), actual_hex_value("Hello"), my_value("Hello"));

            assertEquals(actual_hex_value("0") + " != " + my_value("0"), actual_hex_value("0"), my_value("0"));
            assertEquals(actual_hex_value("") + " != " + my_value(""), actual_hex_value(""), my_value(""));
            assertEquals(actual_hex_value("123456789") + " != " + my_value("123456789"), actual_hex_value("123456789"), my_value("123456789"));
            assertEquals(actual_hex_value("!@##$%%^&*()") + " != " + my_value("!@##$%%^&*()"), actual_hex_value("!@##$%%^&*()"), my_value("!@##$%%^&*()"));
            assertEquals(actual_hex_value("abcdefghijklmnop") + " != " + my_value("abcdefghijklmnop"), actual_hex_value("abcdefghijklmnop"), my_value("abcdefghijklmnop"));
            assertEquals(veryLargeString() + " != " + veryLargeString(), actual_hex_value(veryLargeString()), my_value(veryLargeString()));



        }

    public String veryLargeString() {

        String output = "";
        for (int i = 0; i < 10000 ; i++) {
            output += "a";
        }
        return output;
    }

    public String my_value(String value) {
        Charset cs = Charset.forName("UTF-8");
        byte[] data = value.getBytes(cs);
        byte[] hash = sha1.encode(data);

        StringBuilder hex = new StringBuilder(hash.length * 2);
        int len = hash.length;
        for (int i = 0 ; i < len ; i++) {
            hex.append(String.format("%02X", hash[i]));
        }		
        return hex.toString();

    }

    public String actual_hex_value(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.reset();
            md.update(value.getBytes("UTF-8"));
            byte[] actual = md.digest();
            int actual_len = actual.length;
            StringBuilder actual_hex = new StringBuilder(actual.length * 2);
            for (int i = 0 ; i < actual_len ; i++) {
                actual_hex.append(String.format("%02X", actual[i]));
            }
            return actual_hex.toString();		
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
