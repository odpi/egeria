/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSMetadataCollection;

/**
 * Singleton to map the OMRS "SemanticAssignment" relationship for IGC "term" assets.
 */
public class SemanticAssignmentMapper extends RelationshipMapping {

    private static class Singleton {
        private static final SemanticAssignmentMapper INSTANCE = new SemanticAssignmentMapper();
    }
    public static SemanticAssignmentMapper getInstance() {
        return Singleton.INSTANCE;
    }

    private SemanticAssignmentMapper() {
        super(
                IGCOMRSMetadataCollection.DEFAULT_IGC_TYPE,
                "term",
                "assigned_to_terms",
                "assigned_assets",
                "SemanticAssignment",
                "assignedElements",
                "meaning"
        );
        // We will explicitly exclude terms from being applied a SemanticAssignment, as it would overlap with
        // classifications like Confidentiality (and is probably better done via other more meaningful
        // term-to-term relationships) - we also need to exclude other assets which have no 'assigned_to_terms'
        // relationships
        ProxyMapping pmOne = getProxyOneMapping();
        pmOne.addExcludedIgcAssetType("term");
        pmOne.addExcludedIgcAssetType("connector");
        pmOne.addExcludedIgcAssetType("data_connection");
        pmOne.addExcludedIgcAssetType("group");
        pmOne.addExcludedIgcAssetType("information_governance_policy");
        pmOne.addExcludedIgcAssetType("label");
        pmOne.addExcludedIgcAssetType("user");
    }

}
