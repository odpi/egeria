/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.postgres.controls;

import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.TemplateType;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.PlaceholderPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ReplacementAttributeType;
import org.odpi.openmetadata.frameworks.openmetadata.controls.TemplateDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TemplateTypeEnum characterises the type of templates supported by an integration connector.  It should be copied
 * by developers of an integration connector to help populate the supportedTemplates property of its provider.
 */
public enum PostgreSQLTemplateType implements TemplateDefinition
{
    POSTGRES_SERVER_TEMPLATE(PostgresDeployedImplementationType.POSTGRESQL_SERVER.getDeployedImplementationType(),
                             "Create a " + PostgresDeployedImplementationType.POSTGRESQL_DATABASE.getAssociatedTypeName() + " asset.",
                             PostgresDeployedImplementationType.POSTGRESQL_SERVER.getAssociatedTypeName(),
                           false,
                             "542134e6-b9ce-4dce-8aef-22e8daf34fdb",
                           PostgresDeployedImplementationType.POSTGRESQL_SERVER,
                           PostgresPlaceholderProperty.getPostgresServerPlaceholderPropertyTypes(),
                           null),

    POSTGRES_DATABASE_TEMPLATE(PostgresDeployedImplementationType.POSTGRESQL_DATABASE.getDeployedImplementationType(),
                               "Create a " + PostgresDeployedImplementationType.POSTGRESQL_DATABASE.getAssociatedTypeName() + " asset.",
                               PostgresDeployedImplementationType.POSTGRESQL_DATABASE.getAssociatedTypeName(),
                               false,
                               "3d398b3f-7ae6-4713-952a-409f3dea8520",
                               PostgresDeployedImplementationType.POSTGRESQL_DATABASE,
                               PostgresPlaceholderProperty.getPostgresDatabasePlaceholderPropertyTypes(),
                               null),

    POSTGRES_SCHEMA_TEMPLATE(PostgresDeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA.getDeployedImplementationType(),
                             "Create a " + PostgresDeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA.getAssociatedTypeName() + " asset.",
                             PostgresDeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA.getAssociatedTypeName(),
                             false,
                             "82a5417c-d882-4271-8444-4c6a996a8bfc",
                             PostgresDeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA,
                             PostgresPlaceholderProperty.getPostgresSchemaPlaceholderPropertyTypes(),
                             null),

    POSTGRES_TABULAR_DATA_SET_TEMPLATE(PostgresDeployedImplementationType.POSTGRESQL_TABULAR_DATA_SET.getDeployedImplementationType(),
                                       "Create a " + PostgresDeployedImplementationType.POSTGRESQL_TABULAR_DATA_SET.getAssociatedTypeName() + " asset.",
                                       PostgresDeployedImplementationType.POSTGRESQL_TABULAR_DATA_SET.getAssociatedTypeName(),
                                       false,
                                       "1e76e109-61a5-42d8-a29b-86cb675fc750",
                                       PostgresDeployedImplementationType.POSTGRESQL_TABULAR_DATA_SET,
                                       PostgresPlaceholderProperty.getPostgresTablePlaceholderPropertyTypes(),
                                       null),

    POSTGRES_TABULAR_DATA_SET_COLLECTION_TEMPLATE(PostgresDeployedImplementationType.POSTGRESQL_TABULAR_DATA_SET_COLLECTION.getDeployedImplementationType(),
                                       "Create a " + PostgresDeployedImplementationType.POSTGRESQL_TABULAR_DATA_SET_COLLECTION.getAssociatedTypeName() + " asset.",
                                       PostgresDeployedImplementationType.POSTGRESQL_TABULAR_DATA_SET_COLLECTION.getAssociatedTypeName(),
                                       false,
                                       "3f9a0ab3-072c-4cb1-a14f-6e0492e00dd7",
                                       PostgresDeployedImplementationType.POSTGRESQL_TABULAR_DATA_SET_COLLECTION,
                                       PostgresPlaceholderProperty.getPostgresSchemaPlaceholderPropertyTypes(),
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

    private final PostgresDeployedImplementationType deployedImplementationType;

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
    PostgreSQLTemplateType(String                             templateName,
                           String                             templateDescription,
                           String                             typeName,
                           boolean                            required,
                           String                             defaultTemplateGUID,
                           PostgresDeployedImplementationType deployedImplementationType,
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
        return "5.4-SNAPSHOT";
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

        for (PostgreSQLTemplateType templateTypeEnum : PostgreSQLTemplateType.values())
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

        templateType.setTemplateName(templateName);
        templateType.setTypeName(typeName);
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
