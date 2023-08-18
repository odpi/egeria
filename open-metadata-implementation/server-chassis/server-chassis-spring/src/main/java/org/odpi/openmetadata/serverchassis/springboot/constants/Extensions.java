/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.serverchassis.springboot.constants;

import java.io.Serializable;
import java.util.stream.Stream;
public enum Extensions implements Serializable {

    YAML(".yaml"),
    YML(".yml"),
    JSON(".json");


    private final String value;

    Extensions(String value) {
        this.value = value;
    }

    public static String stream() {
        StringBuffer stringBuffer = new StringBuffer();
        Stream.of(Extensions.values())
                .forEach(e -> stringBuffer.append(e.getValue()).append(", "));

        return stringBuffer.toString();
    }

    private String getValue() {
        return value;
    }

}
