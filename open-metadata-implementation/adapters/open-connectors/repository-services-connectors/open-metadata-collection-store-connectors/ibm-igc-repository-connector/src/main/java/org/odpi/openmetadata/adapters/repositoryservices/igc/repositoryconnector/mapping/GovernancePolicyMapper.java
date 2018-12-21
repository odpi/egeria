/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;

public class GovernancePolicyMapper extends ReferenceableMapper {

    /**
     * Sets the basic criteria to use for mapping between an IGC 'information_governance_policy' object and
     * an OMRS 'GovernancePolicy' object.
     *
     * @param policy the IGC 'information_governance_policy' object
     * @param igcomrsRepositoryConnector the IGC repository connector to use for retrieving any additional info required
     * @param userId the userId of the user doing any further detailed information retrievals (currently unused)
     */
    public GovernancePolicyMapper(Reference policy, IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {

        // Start by calling the superclass's constructor to initialise the Mapper
        super(
                policy,
                "information_governance_policy",
                "GovernancePolicy",
                igcomrsRepositoryConnector,
                userId
        );

        // The list of properties that should be mapped
        addSimplePropertyMapping("name", "title");
        addSimplePropertyMapping("short_description", "summary");
        addSimplePropertyMapping("long_description", "description");

        // The list of relationships that should be mapped (inverted as likely to be more performant if many policies)
        addInvertedRelationshipMapping(
                "information_governance_policy",
                "parent_policy",
                "GovernancePolicyLink",
                "linkedPolicies",
                "linkingPolicies"
        );
        addInvertedRelationshipMapping(
                "information_governance_rule",
                "referencing_policies",
                "GovernanceImplementation",
                "implementations",
                "policies"
        );

        // Finally list any properties that will be used to map Classifications
        // (to do the actual mapping, implement the 'getMappedClassifications' function -- example below)

    }

    /**
     * We implement this method to apply any classifications -- since IGC itself doesn't have a "Classification"
     * asset type, we need to apply our own translation between how we're using other IGC asset types and the
     * Classification(s) we want them to represent in OMRS.
     * <br><br>
     * For GovernancePolicies no classifications are currently implemented.
     */
    @Override
    protected void getMappedClassifications() {
        // Nothing to do...
    }

}
