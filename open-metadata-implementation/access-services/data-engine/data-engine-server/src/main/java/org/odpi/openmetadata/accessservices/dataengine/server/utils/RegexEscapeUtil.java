/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.utils;

public class RegexEscapeUtil {

    public static String escapeSpecialGraphRegexCharacters(String inputString) {
        final String[] specialCharacters = {"(", ")", ".", "="};

        for (String metaCharacter : specialCharacters) {
            if (inputString.contains(metaCharacter)) {
                inputString = inputString.replace(metaCharacter, "\\" + metaCharacter);
            }
        }
        return inputString;
    }
}
