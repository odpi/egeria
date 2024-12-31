/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.postgres.controls;

import org.odpi.openmetadata.frameworks.connectors.controls.TemplateType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TemplateTypeEnum characterises the type of templates supported by an integration connector.  It should be copied
 * by developers of an integration connector to help populate the supportedTemplates property of its provider.
 */
public enum PostgreSQLTemplateType
{
    POSTGRES_SERVER_TEMPLATE(PostgresDeployedImplementationType.POSTGRESQL_SERVER.getDeployedImplementationType(),
                             "Create a " + PostgresDeployedImplementationType.POSTGRESQL_DATABASE.getAssociatedTypeName() + " asset.",
                             PostgresDeployedImplementationType.POSTGRESQL_SERVER.getAssociatedTypeName(),
                           false,
                             "542134e6-b9ce-4dce-8aef-22e8daf34fdb",
                           null),

    POSTGRES_DATABASE_TEMPLATE(PostgresDeployedImplementationType.POSTGRESQL_DATABASE.getDeployedImplementationType(),
                               "Create a " + PostgresDeployedImplementationType.POSTGRESQL_DATABASE.getAssociatedTypeName() + " asset.",
                               PostgresDeployedImplementationType.POSTGRESQL_DATABASE.getAssociatedTypeName(),
                               false,
                               "3d398b3f-7ae6-4713-952a-409f3dea8520",
                               null),

    POSTGRES_SCHEMA_TEMPLATE(PostgresDeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA.getDeployedImplementationType(),
                             "Create a " + PostgresDeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA.getAssociatedTypeName() + " asset.",
                             PostgresDeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA.getAssociatedTypeName(),
                             false,
                             "82a5417c-d882-4271-8444-4c6a996a8bfc",
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


    /**
     * Constructor for Enum
     *
     * @param templateName catalog template name
     * @param typeName open metadata type name for the linked element
     * @param templateDescription deployed implementation type for the linked element
     * @param required is this template required bu the connector
     * @param defaultTemplateGUID is there a default template
     * @param otherPropertyValues other values
     */
    PostgreSQLTemplateType(String              templateName,
                           String              templateDescription,
                           String              typeName,
                           boolean             required,
                           String              defaultTemplateGUID,
                           Map<String, String> otherPropertyValues)
    {
        this.templateName        = templateName;
        this.templateDescription = templateDescription;
        this.typeName            = typeName;
        this.required            = required;
        this.defaultTemplateGUID = defaultTemplateGUID;
        this.otherPropertyValues = otherPropertyValues;
    }


    /**
     * Return the name of the template.
     *
     * @return name
     */
    public String getTemplateName()
    {
        return templateName;
    }


    /**
     * Return the description of the template, such as its content.
     *
     * @return description
     */
    public String getTemplateDescription()
    {
        return templateDescription;
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
     * Return the unique identifier of the default template to use with this connector - this is optional.
     *
     * @return string
     */
    public String getDefaultTemplateGUID()
    {
        return defaultTemplateGUID;
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
        templateType.setTemplateDescription(templateDescription);
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
