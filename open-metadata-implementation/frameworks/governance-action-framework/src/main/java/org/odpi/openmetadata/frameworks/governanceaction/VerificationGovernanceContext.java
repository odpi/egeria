/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction;

import org.odpi.openmetadata.frameworks.governanceaction.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RequestSourceElement;

import java.util.List;
import java.util.Map;

/**
 * VerificationGovernanceContext provides access to details of the verification request along with access to the
 * metadata store.
 *
 * A verification service is typically verifying that the properties associated with actionTargetElements
 * are set appropriately.  Part of these checks may look for consistency with the request source elements.
 * For example, if a discovery service documented a schema extracted from the real world counterpart of an Asset
 * metadata element, a verification service could check that the schema metadata elements for the Asset
 * are correct.
 */
public class VerificationGovernanceContext extends GovernanceContext
{
    /**
     * Constructor sets up the key parameters for processing the request to the governance action service.
     *
     * @param userId calling user
     * @param governanceActionGUID unique identifier of the governance action that triggered this governance service
     * @param requestType unique identifier of the asset that the annotations should be attached to
     * @param requestParameters name-value properties to control the governance action service
     * @param requestSourceElements metadata elements associated with the request to the governance action service
     * @param actionTargetElements metadata elements that need to be worked on by the governance action service
     * @param openMetadataStore client to the metadata store for use by the governance action service
     */
    public VerificationGovernanceContext(String                     userId,
                                         String                     governanceActionGUID,
                                         String                     requestType,
                                         Map<String, String>        requestParameters,
                                         List<RequestSourceElement> requestSourceElements,
                                         List<ActionTargetElement>  actionTargetElements,
                                         OpenMetadataClient openMetadataStore)
    {
        super(userId, governanceActionGUID, requestType, requestParameters, requestSourceElements, actionTargetElements, openMetadataStore);
    }
}
