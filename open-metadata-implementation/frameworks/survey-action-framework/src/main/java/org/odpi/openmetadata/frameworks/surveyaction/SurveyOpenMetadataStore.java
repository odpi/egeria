/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.surveyaction;

import org.odpi.openmetadata.frameworks.governanceaction.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.governanceaction.client.OpenMetadataClient;

/**
 * SurveyOpenMetadataStore provides an interface to the open metadata store.  This is part of the Governance Action Framework (GAF)
 * and provides a comprehensive interface for working with all types of metadata, subject to the user's security permissions.
 * The interface supports search, maintenance of metadata elements, classifications and relationships.
 */
public class SurveyOpenMetadataStore extends OpenMetadataStore
{
    /**
     * The constructor needs an implementation of the open metadata store.
     *
     * @param openMetadataStore client implementation
     * @param userId calling user
     * @param externalSourceGUID unique identifier for external source (or null)
     * @param externalSourceName unique name for external source (or null)
     * @param originatorGUID unique identifier of the source of the to do
     *
     */
    public SurveyOpenMetadataStore(OpenMetadataClient      openMetadataStore,
                                   String                  userId,
                                   String                  externalSourceGUID,
                                   String                  externalSourceName,
                                   String                  originatorGUID)
    {
        super(openMetadataStore, userId, externalSourceGUID, externalSourceName, originatorGUID);
    }
}