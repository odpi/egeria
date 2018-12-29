/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

public class GovernancePolicyLinkMapper extends RelationshipMapping {

    private static GovernancePolicyLinkMapper instance = new GovernancePolicyLinkMapper();
    public static GovernancePolicyLinkMapper getInstance() { return instance; }

    private GovernancePolicyLinkMapper() {
        super(
                "information_governance_policy",
                "information_governance_policy",
                "subpolicies",
                "parent_policy",
                "GovernancePolicyLink",
                "linkingPolicies",
                "linkedPolicies"
        );
    }

}
