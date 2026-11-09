/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.organization;

import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDataFieldDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinitionEnum;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.OpenMetadataRowDataSetConnectorBase;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.BusinessCapabilityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Map;


/**
 * BusinessCapabilitiesTabularDataSetConnector lists the business capabilities stored in open metadata as if
 * they are a tabular data set.
 */
public class BusinessCapabilitiesTabularDataSetConnector extends OpenMetadataRowDataSetConnectorBase
{
    private static final String myConnectorName = BusinessCapabilitiesTabularDataSetConnector.class.getName();


    /**
     * Default constructor
     */
    public BusinessCapabilitiesTabularDataSetConnector()
    {
        super(myConnectorName, ProductDefinitionEnum.BUSINESS_CAPABILITIES);
    }


    /**
     * One row per business capability.
     *
     * @throws Exception problem retrieving the metadata
     */
    @Override
    protected void collectRows() throws Exception
    {
        for (OpenMetadataRootElement businessCapability : getAllElements(OpenMetadataType.BUSINESS_CAPABILITY.typeName, 0))
        {
            Map<String, String> values = newValues();

            if (businessCapability.getProperties() instanceof BusinessCapabilityProperties properties)
            {
                if (properties.getBusinessCapabilityType() != null)
                {
                    putValue(values, ProductDataFieldDefinition.BUSINESS_CAPABILITY_TYPE, properties.getBusinessCapabilityType().getDisplayName());
                }

                putValue(values, ProductDataFieldDefinition.BUSINESS_IMPLEMENTATION_TYPE, properties.getBusinessImplementationType());
            }

            addRow(businessCapability, values);
        }
    }
}
