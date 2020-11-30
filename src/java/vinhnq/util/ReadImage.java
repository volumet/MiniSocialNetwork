/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vinhnq.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

/**
 *
 * @author Admin
 */
public class ReadImage {
    public static String encode64(InputStream input) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] b = new byte[4048];
        int readByte = 0;
        while ((readByte = input.read(b)) != -1) {
            outputStream.write(b, 0, readByte);
        }
        
        byte[] outputImage = outputStream.toByteArray();
        String encode = Base64.getEncoder().encodeToString(outputImage);
        outputStream.close();
        
        return encode;
    }
}
