/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.mendel.controls;

import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ConfigurationPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;

/**
 * MendelConfigurationProperty provides definitions for the configuration properties used with the
 * Mendel Automated Duplicate Manager.
 */
public enum MendelConfigurationProperty
{
    /**
     * The number of validated peer duplicates that must be linked together before they are consolidated into a
     * single element.
     */
    DUPLICATE_CLUSTER_SIZE("duplicateClusterSize",
                           "The number of validated peer duplicates that must be linked together before they are consolidated into a single element.",
                           DataType.INT.getDisplayName(),
                           "3",
                           false),

    /**
     * The relationship types whose duplicate instances are removed.  Two relationships are duplicates when they
     * are of the same type, connect the same two elements, and have the same properties and effectivity dates.
     * The oldest is kept.
     */
    DUPLICATE_RELATIONSHIP_TYPES("duplicateRelationshipTypes",
                                 "The relationship types whose duplicate instances are removed.  Two relationships are duplicates when they are of the same type, connect the same two elements, and have the same properties and effectivity dates.  The oldest is kept.",
                                 DataType.ARRAY_STRING.getDisplayName(),
                                 "AgreementItem,ExternalReferenceLink",
                                 false),

    /**
     * The relationship types that attach elements to an element (at end 1) where duplicate attached elements are
     * removed.  Two attached elements are duplicates when they are anchored to the same element through the same
     * type of relationship, are of the same type, and have the same properties.  The oldest is kept.
     */
    DUPLICATE_ATTACHMENT_TYPES("duplicateAttachmentTypes",
                               "The relationship types that attach elements to an element (at end 1) where duplicate attached elements are removed.  Two attached elements are duplicates when they are anchored to the same element through the same type of relationship, are of the same type, and have the same properties.  The oldest is kept.",
                               DataType.ARRAY_STRING.getDisplayName(),
                               "SearchKeywordLink",
                               false),

    /**
     * Report the duplicate relationships and attached elements in the audit log without removing them.
     */
    REPORT_DUPLICATES_ONLY("reportDuplicatesOnly",
                           "Report the duplicate relationships and attached elements in the audit log without removing them.",
                           DataType.BOOLEAN.getDisplayName(),
                           "true",
                           false),

    ;

    public final String  name;
    public final String  description;
    public final String  dataType;
    public final String  example;
    public final boolean isPlaceholder;


    /**
     * The default number of validated peer duplicates in a cluster before they are consolidated.
     */
    public static final int DEFAULT_DUPLICATE_CLUSTER_SIZE = 3;


    /**
     * The relationship types whose duplicates are removed in the standard deployment: the links that a
     * connector re-creates each time it catalogues a document can multiply when the document is catalogued
     * again.
     */
    public static final List<String> DEFAULT_DUPLICATE_RELATIONSHIP_TYPES = List.of(OpenMetadataType.AGREEMENT_ITEM_RELATIONSHIP.typeName,
                                                                                   OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName);


    /**
     * The attachment relationship types whose duplicate attached elements are removed in the standard deployment.
     */
    public static final List<String> DEFAULT_DUPLICATE_ATTACHMENT_TYPES = List.of(OpenMetadataType.SEARCH_KEYWORD_LINK_RELATIONSHIP.typeName);


    /**
     * Create a specific Enum constant.
     *
     * @param name name of the configuration property
     * @param description description of the configuration property
     * @param dataType type of value of the configuration property
     * @param example example of the configuration property
     * @param isPlaceholder is this also used as a placeholder property?
     */
    MendelConfigurationProperty(String  name,
                                String  description,
                                String  dataType,
                                String  example,
                                boolean isPlaceholder)
    {
        this.name          = name;
        this.description   = description;
        this.dataType      = dataType;
        this.example       = example;
        this.isPlaceholder = isPlaceholder;
    }


    /**
     * Return the name of the configuration property.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the description of the configuration property.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the data type for the configuration property.
     *
     * @return string data type name
     */
    public String getDataType()
    {
        return dataType;
    }


    /**
     * Return an example of the configuration property to help users understand how to set it up.
     *
     * @return string example
     */
    public String getExample()
    {
        return example;
    }


    /**
     * Return whether this value is also used as a placeholder property.
     *
     * @return boolean
     */
    public boolean isPlaceholder()
    {
        return isPlaceholder;
    }


    /**
     * Retrieve the defined configuration properties as a list of the type used in the connector's specification.
     *
     * @return list
     */
    public static List<ConfigurationPropertyType> getConfigurationPropertyTypes()
    {
        List<ConfigurationPropertyType> configurationPropertyTypes = new ArrayList<>();

        for (MendelConfigurationProperty configurationProperty : MendelConfigurationProperty.values())
        {
            configurationPropertyTypes.add(configurationProperty.getConfigurationPropertyType());
        }

        return configurationPropertyTypes;
    }


    /**
     * Return a summary of this enum to use in the connector's specification.
     *
     * @return configuration property type
     */
    public ConfigurationPropertyType getConfigurationPropertyType()
    {
        ConfigurationPropertyType configurationPropertyType = new ConfigurationPropertyType();

        configurationPropertyType.setName(name);
        configurationPropertyType.setDescription(description);
        configurationPropertyType.setDataType(dataType);
        configurationPropertyType.setExample(example);
        configurationPropertyType.setRequired(isPlaceholder);

        return configurationPropertyType;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "MendelConfigurationProperty{ name=" + name + "}";
    }
}
