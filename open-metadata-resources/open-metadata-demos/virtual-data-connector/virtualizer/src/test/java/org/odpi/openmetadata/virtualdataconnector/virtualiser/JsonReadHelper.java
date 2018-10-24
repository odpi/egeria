/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.virtualdataconnector.virtualiser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;


public class JsonReadHelper {
    private static final Logger log = LoggerFactory.getLogger(JsonReadHelper.class);

    public static String readFile(File fileName){
        BufferedReader reader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line;

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                // stringBuilder.append(ls);
            }
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return stringBuilder.toString();
    }

}
