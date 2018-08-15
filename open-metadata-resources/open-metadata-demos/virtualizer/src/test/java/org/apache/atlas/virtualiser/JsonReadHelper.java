/* SPDX-License-Identifier: Apache-2.0 */
package org.apache.atlas.virtualiser;

import java.io.*;


public class JsonReadHelper {
    public static String readFile(File fileName){
        BufferedReader reader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line = null;

            String ls = System.getProperty("line.separator");
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                // stringBuilder.append(ls);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }

}
