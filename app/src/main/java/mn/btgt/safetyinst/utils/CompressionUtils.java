package mn.btgt.safetyinst.utils;

import com.orhanobut.logger.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * Author: Turtuvshin Byambaa.
 * Project: Safety Inst
 * URL: https://www.github.com/tortuvshin
 */
@Deprecated
public class CompressionUtils {

    public static byte[] compress(byte[] data){
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        deflater.finish();
        byte[] buffer = new byte[512];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer); // returns the generated code... index
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] output = outputStream.toByteArray();
        Logger.d("Original: ".concat(data.length / 512 + " Kb"));
        Logger.d("Compressed: ".concat(output.length / 512 + " Kb"));
        return output;
    }

    public static byte[] decompress(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[512];
        try {
            while (!inflater.finished()) {
                int count = 0;

                count = inflater.inflate(buffer);

                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DataFormatException e) {
            e.printStackTrace();
        }
        byte[] output = outputStream.toByteArray();
        Logger.d("Original: " + data.length);
        Logger.d("Compressed: " + output.length);
        return output;
    }

}
