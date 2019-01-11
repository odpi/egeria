/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSMetadataCollection;

/**
 * Singleton to map the OMRS "AttachedTag" relationship for IGC "label" assets.
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
                IGCOMRSMetadataCollection.DEFAULT_IGC_TYPE,
                "label",
                "labels",
                "labeled_assets",
                "AttachedTag",
                "taggedElement",
                "tags"
        );
    }

}
