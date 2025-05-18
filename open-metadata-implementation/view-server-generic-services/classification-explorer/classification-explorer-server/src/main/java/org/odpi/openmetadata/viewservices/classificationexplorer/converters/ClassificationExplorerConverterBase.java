/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.classificationexplorer.converters;


import org.odpi.openmetadata.frameworks.openmetadata.converters.OpenMetadataConverterBase;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;

/**
 * Provide base converter functions for the Classification Explorer OMVS.
 *
 * @param <B> bean class
 */
public abstract class ClassificationExplorerConverterBase<B> extends OpenMetadataConverterBase<B>
{
    /**
     * Constructor
     *
     * @param propertyHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public ClassificationExplorerConverterBase(PropertyHelper propertyHelper,
                                        String               serviceName,
                                        String               serverName)
    {
        super(propertyHelper, serviceName, serverName);
    }
}
