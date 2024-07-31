/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.client.converters;

import org.odpi.openmetadata.frameworks.governanceaction.converters.OpenMetadataConverterBase;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;

/**
 * Base class converter for Asset Owner OMAS.
 *
 * @param <B> bean class
 */
public abstract class AssetOwnerConverterBase<B> extends OpenMetadataConverterBase<B>
{
    /**
     * Constructor
     *
     * @param propertyHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public AssetOwnerConverterBase(PropertyHelper propertyHelper,
                                   String               serviceName,
                                   String               serverName)
    {
        super(propertyHelper, serviceName, serverName);
    }


}
