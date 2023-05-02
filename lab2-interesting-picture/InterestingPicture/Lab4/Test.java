import java.security.MessageDigest;

public class Test {
    public static void main(String[] args) throws Exception {
        int i = 0;
        String SHA256Str;
        while (true) {
            SHA256Str = getSHA256(i + ",4,19,Pink,Orange,002fdb16086d97e03613fa0caa87b280eca956216e61a35400408bdd3a449e45");
            if (SHA256Str.substring(0, 2).equals("00")) {
                break;
            } else {
                i++;
            }
        }
        System.out.println(SHA256Str);
        System.out.println("Find This Nonce is " + SHA256Str);
    }

    /**
     * Implement SHA256 encryption
     * @param str
     * @return
     */
    public static String getSHA256(String str) throws Exception{
        MessageDigest messageDigest;
        String encodeStr;
        messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(str.getBytes("UTF-8"));
        encodeStr = byte2Hex(messageDigest.digest());
        return encodeStr;
    }

    /**
     * Convert byte to hexadecimal
     * @param bytes
     * @return
     */
    private static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;

        // Code from GitHub
        // https://gist.github.com/arloor/15e98d7d76f93560b337924d6f6c5b56
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);

            if (temp.length() == 1) {
                // 1 to get a bit of the complementary 0 operation
                stringBuffer.append("0");
            }

            stringBuffer.append(temp);
        }

        return stringBuffer.toString().toUpperCase();
    }
}

/*  Output
    Hello World
    A591A6D40BF420404A011733CFB7B190D62C65BF0BCDA32B57B277D9AD9F146E
*/