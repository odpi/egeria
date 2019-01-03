/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary;

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

    public static final String IGC_REST_COMMON_MODEL_PKG = "org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common";
    public static final String IGC_REST_GENERATED_MODEL_PKG = "org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated";

    public static final String VERSION_115 = "v115";
    public static final String VERSION_117 = "v117";

    // ie. this constant must not be public or it is a potential vulnerability, so must be exposed by getter below
    private static final String[] MODIFICATION_DETAILS = new String[] {
            MOD_CREATED_BY,
            MOD_CREATED_ON,
            MOD_MODIFIED_BY,
            MOD_MODIFIED_ON
    };

    public static final String[] MODIFICATION_DETAILS() { return MODIFICATION_DETAILS; }

    private IGCRestConstants() { }

}
