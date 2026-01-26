/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.validmetadatavalues;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.*;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.DynamicOpenMetadataDataSetProviderBase;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.controls.ReferenceDataConfigurationProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * ValidValueDataSetProvider is the connector provider for the ValidValueDataSet connector that manages the members of
 * a valid values set as a tabular data set.
 */
public class ValidMetadataValueDataSetProvider extends DynamicOpenMetadataDataSetProviderBase
{
    /*
     * Class of the connector.
     */
    private static final String connectorClassName = ValidMetadataValueDataSetConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific
     * store implementation.
     */
    public ValidMetadataValueDataSetProvider()
    {
        super(EgeriaOpenConnectorDefinition.VALID_METADATA_VALUE_TABULAR_DATA_SET,
              connectorClassName,
              List.of(ReferenceDataConfigurationProperty.IDENTIFIER_PROPERTY_VALUE.name,
                      ReferenceDataConfigurationProperty.CANONICAL_NAME.name,
                      ReferenceDataConfigurationProperty.PRODUCT_DESCRIPTION.name));
    }


    /**
     * Retrieve the product definition base on a supplied identifier.
     *
     * @param identifier unique identifier for the product - stored in open metadata identifier property
     * @param canonicalName name used for the product and table name
     * @param description description of the product
     *
     * @return product definition
     */
    public ProductDefinition getProductDefinition(String identifier,
                                                  String canonicalName,
                                                  String description)
    {
        /*
         * Create a dynamic product definition and add it to the open metadata ecosystem.
         * The configuration properties enable the connector to retrieve the product definition when it runs.
         */
        Map<String, Object> configurationProperties = new HashMap<>();

        configurationProperties.put(ReferenceDataConfigurationProperty.IDENTIFIER_PROPERTY_VALUE.name, identifier);
        configurationProperties.put(ReferenceDataConfigurationProperty.CANONICAL_NAME.name, canonicalName);
        configurationProperties.put(ReferenceDataConfigurationProperty.PRODUCT_DESCRIPTION.name, description);

        return new ProductDefinitionBean(OpenMetadataType.DIGITAL_PRODUCT.typeName,
                                         new ProductDefinition[]{ ProductDefinitionEnum.VALID_METADATA_VALUE_SETS},
                                         "Valid Metadata Value Set: " + identifier,
                                         identifier,
                                         null,
                                         canonicalName,
                                         description,
                                         ProductCategoryDefinition.REFERENCE_DATA.getPreferredValue(),
                                         ProductGovernanceDefinition.INTERNAL_USE_ONLY,
                                         ProductCommunityDefinition.REFERENCE_DATA_SIG,
                                         new ProductSubscriptionDefinition[]{
                                                 ProductSubscriptionDefinition.EVALUATION_SUBSCRIPTION,
                                                 ProductSubscriptionDefinition.DAILY_REFRESH_SUBSCRIPTION,
                                                 ProductSubscriptionDefinition.WEEKLY_REFRESH_SUBSCRIPTION,
                                                 ProductSubscriptionDefinition.ONGOING_UPDATE},
                                         canonicalName,
                                         new ProductDataFieldDefinition[]{
                                                 ProductDataFieldDefinition.GUID},
                                         new ProductDataFieldDefinition[]{
                                                 ProductDataFieldDefinition.CREATE_TIME,
                                                 ProductDataFieldDefinition.UPDATE_TIME,
                                                 ProductDataFieldDefinition.QUALIFIED_NAME,
                                                 ProductDataFieldDefinition.IDENTIFIER,
                                                 ProductDataFieldDefinition.DISPLAY_NAME,
                                                 ProductDataFieldDefinition.DESCRIPTION,
                                                 ProductDataFieldDefinition.CATEGORY,
                                                 ProductDataFieldDefinition.NAMESPACE,
                                                 ProductDataFieldDefinition.PREFERRED_VALUE,
                                                 ProductDataFieldDefinition.IS_CASE_SENSITIVE,
                                                 ProductDataFieldDefinition.DATA_TYPE,
                                                 ProductDataFieldDefinition.SCOPE,
                                                 ProductDataFieldDefinition.USAGE},
                                         OpenMetadataType.REFERENCE_CODE_TABLE.typeName,
                                         "Data set",
                                         new ValidMetadataValueDataSetProvider(),
                                         configurationProperties,
                                         "Valid Metadata Value Set: " + identifier); // Value used in Jacquard's havestValidMetadataValues() method - change in both places

    }
}
