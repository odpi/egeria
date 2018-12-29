/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

/**
 * Singleton to represent the AttachedTag relationship in OMRS.
 */
public class AttachedTagMapper extends RelationshipMapping {

    private static class Singleton {
        private static final AttachedTagMapper INSTANCE = new AttachedTagMapper();
    }
    public static AttachedTagMapper getInstance() {
        return Singleton.INSTANCE;
    }

    private AttachedTagMapper() {
        super(
                "main_object",
                "label",
                "labels",
                "labeled_assets",
                "AttachedTag",
                "taggedElement",
                "tags"
        );
    }

}
