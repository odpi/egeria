/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.db2luw.controls;

import org.odpi.openmetadata.adapters.connectors.controls.DB2LUWDeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.TemplateType;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.PlaceholderPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ReplacementAttributeType;
import org.odpi.openmetadata.frameworks.openmetadata.controls.TemplateDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.DeployedImplementationTypeDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TemplateTypeEnum characterises the type of templates supported by an integration connector.  It should be copied
 * by developers of an integration connector to help populate the supportedTemplates property of its provider.
 */
public enum DB2LUWTemplateType implements TemplateDefinition
{
    DB2LUW_SERVER_TEMPLATE(DB2LUWDeployedImplementationType.DB2LUW_SERVER.getDeployedImplementationType(),
                           "Create a " + DB2LUWDeployedImplementationType.DB2LUW_SERVER.getAssociatedTypeName() + " asset.",
                           DB2LUWDeployedImplementationType.DB2LUW_SERVER.getAssociatedTypeName(),
                           false,
                           "540da969-2b19-49d9-9217-e14cc9742f8a",
                           DB2LUWDeployedImplementationType.DB2LUW_SERVER,
                           DB2LUWPlaceholderProperty.getDB2LUWServerPlaceholderPropertyTypes(),
                           null),

    DB2LUW_DATABASE_TEMPLATE(DB2LUWDeployedImplementationType.DB2LUW_DATABASE.getDeployedImplementationType(),
                             "Create a " + DB2LUWDeployedImplementationType.DB2LUW_DATABASE.getAssociatedTypeName() + " asset.",
                             DB2LUWDeployedImplementationType.DB2LUW_DATABASE.getAssociatedTypeName(),
                             false,
                             "9c6c25c9-0778-4d7f-b997-1d790b6f76db",
                             DB2LUWDeployedImplementationType.DB2LUW_DATABASE,
                             DB2LUWPlaceholderProperty.getDB2LUWDatabasePlaceholderPropertyTypes(),
                             null),

    DB2LUW_TABULAR_DATA_SET_TEMPLATE(DB2LUWDeployedImplementationType.DB2LUW_TABULAR_DATA_SET.getDeployedImplementationType(),
                                     "Create a " + DB2LUWDeployedImplementationType.DB2LUW_TABULAR_DATA_SET.getAssociatedTypeName() + " asset.",
                                     DB2LUWDeployedImplementationType.DB2LUW_TABULAR_DATA_SET.getAssociatedTypeName(),
                                     false,
                                     "fea174aa-1fcb-44e8-9e75-533ea8651080",
                                     DB2LUWDeployedImplementationType.DB2LUW_TABULAR_DATA_SET,
                                     DB2LUWPlaceholderProperty.getDB2LUWTablePlaceholderPropertyTypes(),
                                     null),

    DB2LUW_TABULAR_DATA_SET_COLLECTION_TEMPLATE(DB2LUWDeployedImplementationType.DB2LUW_TABULAR_DATA_SET_COLLECTION.getDeployedImplementationType(),
                                                "Create a " + DB2LUWDeployedImplementationType.DB2LUW_TABULAR_DATA_SET_COLLECTION.getAssociatedTypeName() + " asset.",
                                                DB2LUWDeployedImplementationType.DB2LUW_TABULAR_DATA_SET_COLLECTION.getAssociatedTypeName(),
                                                false,
                                                "5d98a87a-6d6f-495f-a6bf-2d082d6b1518",
                                                DB2LUWDeployedImplementationType.DB2LUW_TABULAR_DATA_SET_COLLECTION,
                                                DB2LUWPlaceholderProperty.getDB2LUWSchemaPlaceholderPropertyTypes(),
                                                null),
    ;


    /**
     * Symbolic name of the template.
     */
    private final String templateName;

    /**
     * Description of the value to provide for this template.
     */
    private final String templateDescription;

    /**
     * Open metadata type name of the template.
     */
    private final String typeName;

    /**
     * Is this catalog template required for the connector to work successfully.
     */
    private final boolean required;


    /**
     * Option guid for a template to use if no template is specified.
     */
    private final String defaultTemplateGUID;

    /**
     * A map of property name to property value for values that should match in the catalog template for it to be compatible with this integration
     * connector.
     */
    private final Map<String, String> otherPropertyValues;

    private final DB2LUWDeployedImplementationType deployedImplementationType;

    private final List<PlaceholderPropertyType>        placeholderPropertyTypes;


    /**
     * Constructor for Enum
     *
     * @param templateName catalog template name
     * @param typeName open metadata type name for the linked element
     * @param templateDescription deployed implementation type for the linked element
     * @param required is this template required bu the connector
     * @param defaultTemplateGUID is there a default template
     * @param deployedImplementationType       deployed implementation type for the technology
     * @param placeholderPropertyTypes         placeholder variables used in the supplied parameters
     * @param otherPropertyValues other values
     */
    DB2LUWTemplateType(String                             templateName,
                       String                             templateDescription,
                       String                             typeName,
                       boolean                            required,
                       String                             defaultTemplateGUID,
                       DB2LUWDeployedImplementationType   deployedImplementationType,
                       List<PlaceholderPropertyType>      placeholderPropertyTypes,
                       Map<String, String>                otherPropertyValues)
    {
        this.templateName               = templateName;
        this.templateDescription        = templateDescription;
        this.typeName                   = typeName;
        this.required                   = required;
        this.defaultTemplateGUID        = defaultTemplateGUID;
        this.deployedImplementationType = deployedImplementationType;
        this.placeholderPropertyTypes   = placeholderPropertyTypes;
        this.otherPropertyValues        = otherPropertyValues;
    }


    /**
     * Return the unique identifier of the template.
     *
     * @return name
     */
    @Override
    public String getTemplateGUID()
    {
        return defaultTemplateGUID;
    }

    /**
     * Return the name of the template.
     *
     * @return name
     */
    @Override
    public String getTemplateName()
    {
        return templateName;
    }


    /**
     * Return the description of the template, such as its content.
     *
     * @return description
     */
    @Override
    public String getTemplateDescription()
    {
        return templateDescription;
    }


    /**
     * Return the version identifier for the template classification.
     *
     * @return string
     */
    @Override
    public String getTemplateVersionIdentifier()
    {
        return "6.1-SNAPSHOT";
    }


    /**
     * Return the supported deployed implementation for this template.
     *
     * @return enum
     */
    @Override
    public DeployedImplementationTypeDefinition getDeployedImplementationType()
    {
        return deployedImplementationType;
    }


    /**
     * Return the value to use in the element that describes its version.
     *
     * @return version identifier placeholder
     */
    @Override
    public String getElementVersionIdentifier()
    {
        return PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder();
    }


    /**
     * Return the list of placeholders supported by this template.
     *
     * @return list of placeholder types
     */
    @Override
    public List<PlaceholderPropertyType> getPlaceholders()
    {
        return placeholderPropertyTypes;
    }


    /**
     * Return the list of attributes that should be supplied by the caller using this template.
     *
     * @return list of replacement attributes
     */
    @Override
    public List<ReplacementAttributeType> getReplacementAttributes()
    {
        return null;
    }


    /**
     * Return the open metadata type name.
     *
     * @return open metadata type name
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Return whether this catalog template is required for this service to work successful.
     *
     * @return boolean flag
     */
    public boolean getRequired()
    {
        return required;
    }


    /**
     * Return a map of property name to property value that the catalog template should have to be valid for this integration connector.
     *
     * @return map of string to string
     */
    public Map<String, String> getOtherPropertyValues()
    {
        return otherPropertyValues;
    }


    /**
     * Return all the template types defined by this enum.
     *
     * @return list of catalog template type
     */
    public static List<TemplateType> getTemplateTypes()
    {
        List<TemplateType> templateTypes = new ArrayList<>();

        for (DB2LUWTemplateType templateTypeEnum : DB2LUWTemplateType.values())
        {
            templateTypes.add(templateTypeEnum.getTemplateType());
        }

        return templateTypes;
    }


    /**
     * Return the catalog template type for a specific catalog template enum.
     *
     * @return catalog template type
     */
    public TemplateType getTemplateType()
    {
        TemplateType templateType = new TemplateType();

        templateType.setName(templateName);
        templateType.setOpenMetadataTypeName(typeName);
        templateType.setDescription(templateDescription);
        templateType.setRequired(required);
        templateType.setDefaultTemplateGUID(defaultTemplateGUID);
        templateType.setOtherPropertyValues(otherPropertyValues);

        return templateType;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "TemplateType{templateName='" + templateName + "'}";
    }
}
