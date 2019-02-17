/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.attributes;

/**
 * Singleton to map between OMRS "ConfidentialityLevel" enum and corresponding IGC property values.
 */
public class ConfidentialityLevelMapper extends EnumMapping {

    private static class Singleton {
        private static final ConfidentialityLevelMapper INSTANCE = new ConfidentialityLevelMapper();
    }
    public static ConfidentialityLevelMapper getInstance() {
        return Singleton.INSTANCE;
    }

    private ConfidentialityLevelMapper() {
        super(
                "ConfidentialityLevel"
        );
        addDefaultEnumMapping(99, "Other");
        addEnumMapping("Public", 0, "Unclassified");
        addEnumMapping("Internal", 1, "Internal");
        addEnumMapping("Confidential", 2, "Confidential");
        addEnumMapping("Sensitive", 3, "Sensitive");
        addEnumMapping("Secret", 4, "Restricted");
    }

}
