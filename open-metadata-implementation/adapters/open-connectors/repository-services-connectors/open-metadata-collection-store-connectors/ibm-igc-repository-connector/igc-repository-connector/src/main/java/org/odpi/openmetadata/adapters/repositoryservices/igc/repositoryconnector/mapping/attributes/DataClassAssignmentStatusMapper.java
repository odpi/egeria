/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.attributes;

/**
 * Singleton to map between OMRS "DataClassAssignmentStatus" enum and corresponding IGC property values.
 */
public class DataClassAssignmentStatusMapper extends EnumMapping {

    private static class Singleton {
        private static final DataClassAssignmentStatusMapper INSTANCE = new DataClassAssignmentStatusMapper();
    }
    public static DataClassAssignmentStatusMapper getInstance() {
        return Singleton.INSTANCE;
    }

    private DataClassAssignmentStatusMapper() {
        super(
                "DataClassAssignmentStatus"
        );
        addDefaultEnumMapping(99, "Other");
        addEnumMapping("discovered", 0, "Discovered");
        addEnumMapping("selected", 1, "Proposed");
    }

}
