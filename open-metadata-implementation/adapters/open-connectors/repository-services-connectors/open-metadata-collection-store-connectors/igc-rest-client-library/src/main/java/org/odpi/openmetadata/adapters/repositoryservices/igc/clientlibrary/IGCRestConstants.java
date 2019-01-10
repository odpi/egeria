/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IGCRestConstants {

    /**
     * Singleton for array-based constants, since these are otherwise mutable even when final (and thus a potential
     * vulnerability).
     */
    private static class Singleton {
        private static final IGCRestConstants INSTANCE = new IGCRestConstants();
    }
    public static IGCRestConstants getInstance() {
        return Singleton.INSTANCE;
    }

    public static final String MOD_CREATED_BY = "created_by";
    public static final String MOD_CREATED_ON = "created_on";
    public static final String MOD_MODIFIED_BY = "modified_by";
    public static final String MOD_MODIFIED_ON = "modified_on";

    public static final Pattern INVALID_NAMING_CHARS = Pattern.compile("[()/&$\\- ]");

    public static final String IGC_REST_COMMON_MODEL_PKG = "org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common";
    public static final String IGC_REST_GENERATED_MODEL_PKG = "org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated";

    // ie. this constant must not be public or it is a potential vulnerability, so must be exposed by getter below
    private static final String[] MODIFICATION_DETAILS = new String[] {
            MOD_CREATED_BY,
            MOD_CREATED_ON,
            MOD_MODIFIED_BY,
            MOD_MODIFIED_ON
    };

    public static final String[] MODIFICATION_DETAILS() { return MODIFICATION_DETAILS; }

    private static final Map<String, String> NON_UNIQUE_CLASSNAMES = new HashMap<String, String>() {{
        put("valid_value_list", "ValidValueList");
        put("validvaluelist", "ValidValueList2");
        put("valid_value_range", "ValidValueRange");
        put("validvaluerange", "ValidValueRange2");
        put("parameter_set", "ParameterSet");
        put("parameterset", "ParameterSet2");
        put("function_call", "FunctionCall");
        put("functioncall", "FunctionCall2");
    }};

    public static final Map<String, String> NON_UNIQUE_CLASSNAMES() { return NON_UNIQUE_CLASSNAMES; }

    /**
     * Retrieve the name of a POJO class from the IGC asset type name.
     *
     * @param igcAssetType the name of the IGC asset type for which to retrieve a POJO classname
     * @return
     */
    public static final String getClassNameForAssetType(String igcAssetType) {
        if (NON_UNIQUE_CLASSNAMES().containsKey(igcAssetType)) {
            return NON_UNIQUE_CLASSNAMES().get(igcAssetType);
        } else {
            return getCamelCase(igcAssetType);
        }
    }

    /**
     * Converts an IGC type or property (something_like_this) into a camelcase class name (SomethingLikeThis).
     *
     * @param input
     * @return String
     */
    public static final String getCamelCase(String input) {
        Matcher m = INVALID_NAMING_CHARS.matcher(input);
        String invalidsRemoved = m.replaceAll("_");
        StringBuilder sb = new StringBuilder(invalidsRemoved.length());
        for (String token : invalidsRemoved.split("_")) {
            if (token.length() > 0) {
                sb.append(token.substring(0, 1).toUpperCase());
                sb.append(token.substring(1).toLowerCase());
            }
        }
        String result = sb.toString();
        return result;
    }

    private IGCRestConstants() { }

}
