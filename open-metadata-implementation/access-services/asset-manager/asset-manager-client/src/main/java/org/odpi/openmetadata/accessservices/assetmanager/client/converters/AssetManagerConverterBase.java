/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.client.converters;

import org.odpi.openmetadata.frameworks.governanceaction.converters.OpenMetadataConverterBase;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;

/**
 * Common routines for converters from Asset Manager OMAS
 * @param <B> bean class
 */
public abstract class AssetManagerConverterBase<B> extends OpenMetadataConverterBase<B>
{
    /**
     * Constructor
     *
     * @param propertyHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public AssetManagerConverterBase(PropertyHelper propertyHelper,
                                     String               serviceName,
                                     String               serverName)
    {
        super(propertyHelper, serviceName, serverName);
    }
}
