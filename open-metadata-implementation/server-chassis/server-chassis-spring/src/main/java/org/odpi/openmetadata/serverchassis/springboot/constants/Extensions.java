package org.odpi.openmetadata.serverchassis.springboot.constants;

import java.io.Serializable;
import java.util.stream.Stream;

/**
 * Created by YM21WO on egeria
 * on 17/08/2023 at 11:19
 */
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
