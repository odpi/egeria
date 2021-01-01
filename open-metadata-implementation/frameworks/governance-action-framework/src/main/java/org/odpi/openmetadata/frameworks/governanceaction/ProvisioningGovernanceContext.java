/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction;

import org.odpi.openmetadata.frameworks.governanceaction.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RequestSourceElement;

import java.util.List;
import java.util.Map;

/**
 * ProvisioningGovernanceContext provides access to details of the provisioning request along with access to the
 * metadata store.
 *
 * A provisioning service is typically using the requestSourceElements to provision new resources at the actionTargetElements.
 */
public abstract class ProvisioningGovernanceContext extends GovernanceContext
{
    /**
     * Constructor sets up the key parameters for processing the request to the governance service.
     *
     * @param userId calling user
     * @param requestType unique identifier of the asset that the annotations should be attached to
     * @param requestParameters name-value properties to control the governance service
     * @param requestSourceElements metadata elements associated with the request to the governance service
     * @param actionTargetElements metadata elements that need to be worked on by the governance service
     * @param openMetadataStore client to the metadata store for use by the governance service
     */
    public ProvisioningGovernanceContext(String                     userId,
                                         String                     requestType,
                                         Map<String, String>        requestParameters,
                                         List<RequestSourceElement> requestSourceElements,
                                         List<ActionTargetElement>  actionTargetElements,
                                         OpenMetadataStore          openMetadataStore)
    {
        super(userId, requestType, requestParameters, requestSourceElements, actionTargetElements, openMetadataStore);
    }

    // todo methods for creating lineage and assets

}
