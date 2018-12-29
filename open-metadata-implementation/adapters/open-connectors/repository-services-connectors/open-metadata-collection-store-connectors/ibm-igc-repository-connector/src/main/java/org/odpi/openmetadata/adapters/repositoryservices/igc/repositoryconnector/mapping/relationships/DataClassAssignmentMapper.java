/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

/**
 * Singleton to represent the DataClassAssignment relationship in OMRS.
 */
public class DataClassAssignmentMapper extends RelationshipMapping {

    private static DataClassAssignmentMapper instance = new DataClassAssignmentMapper();
    public static DataClassAssignmentMapper getInstance() { return instance; }

    private DataClassAssignmentMapper() {
        super(
                "main_object",
                "data_class",
                "detected_classifications",
                "classified_assets_detected",
                "DataClassAssignment",
                "elementsAssignedToDataClass",
                "dataClassesAssignedToElement"
        );
        setOptimalStart(OptimalStart.CUSTOM);
        addAlternativePropertyFromOne("selected_classification");
        addAlternativePropertyFromTwo("classifications_selected");
    }

}
