package btgt.mn.safetyinst.utils;

/**
 * Created by turtuvshin on 10/11/17.
 */

public class SafConstants {
    public static String WebURL = "https://www.mongolgps.com/phone.php";

    public static String getSecretCode(String imei, String time){
        return MD5_Hash(imei + "-BTGT-" + String.valueOf(time));
    }

    public static String MD5_Hash(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

}
