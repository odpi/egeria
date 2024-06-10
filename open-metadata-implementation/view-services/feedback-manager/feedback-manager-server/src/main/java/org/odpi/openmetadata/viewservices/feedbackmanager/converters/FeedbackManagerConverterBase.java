/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.feedbackmanager.converters;


import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;
import org.odpi.openmetadata.frameworks.governanceaction.converters.OpenMetadataConverterBase;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElements;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;

/**
 * Provide base converter functions for the Feedback Manager OMVS.
 *
 * @param <B> bean class
 */
public abstract class FeedbackManagerConverterBase<B> extends OpenMetadataConverterBase<B>
{
    /**
     * Constructor
     *
     * @param propertyHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public FeedbackManagerConverterBase(PropertyHelper propertyHelper,
                                        String               serviceName,
                                        String               serverName)
    {
        super(propertyHelper, serviceName, serverName);
    }
}
