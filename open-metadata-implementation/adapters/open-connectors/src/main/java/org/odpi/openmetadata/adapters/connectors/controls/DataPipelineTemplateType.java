/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.controls;

import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.controls.TemplateDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.PlaceholderPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ReplacementAttributeType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.TemplateType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * DataPipelineTemplateType describes the catalog templates supplied with Egeria for jobs that run in data
 * pipelines, and the temporary data they use.  Each template creates just the asset, identified by its resource
 * name within a namespace (for example the name and namespace that OpenLineage gives a dataset or job).
 */
public enum DataPipelineTemplateType implements TemplateDefinition
{
    /**
     * Create an asset with the Apache Airflow DAG technology type.
     */
    APACHE_AIRFLOW_DAG_TEMPLATE(DeployedImplementationType.AIRFLOW_DAG.getDeployedImplementationType(),
                                "Create a " + DeployedImplementationType.AIRFLOW_DAG.getAssociatedTypeName() + " asset with technology type " + DeployedImplementationType.AIRFLOW_DAG.getDeployedImplementationType() + ".",
                                DeployedImplementationType.AIRFLOW_DAG.getAssociatedTypeName(),
                                false,
                                "80343088-b527-4b92-b265-d22f94c29d55",
                                DeployedImplementationType.AIRFLOW_DAG,
                                PlaceholderProperty.getResourcePlaceholderPropertyTypes(),
                                null),

    /**
     * Create an asset with the Apache Airflow Task technology type.
     */
    APACHE_AIRFLOW_TASK_TEMPLATE(DataPipelineDeployedImplementationType.APACHE_AIRFLOW_TASK.getDeployedImplementationType(),
                                 "Create a " + DataPipelineDeployedImplementationType.APACHE_AIRFLOW_TASK.getAssociatedTypeName() + " asset with technology type " + DataPipelineDeployedImplementationType.APACHE_AIRFLOW_TASK.getDeployedImplementationType() + ".",
                                 DataPipelineDeployedImplementationType.APACHE_AIRFLOW_TASK.getAssociatedTypeName(),
                                 false,
                                 "53082f59-6506-4c49-8d10-8b4549e3b2d8",
                                 DataPipelineDeployedImplementationType.APACHE_AIRFLOW_TASK,
                                 PlaceholderProperty.getResourcePlaceholderPropertyTypes(),
                                 null),

    /**
     * Create an asset with the Apache Spark Job technology type.
     */
    APACHE_SPARK_JOB_TEMPLATE(DataPipelineDeployedImplementationType.APACHE_SPARK_JOB.getDeployedImplementationType(),
                              "Create a " + DataPipelineDeployedImplementationType.APACHE_SPARK_JOB.getAssociatedTypeName() + " asset with technology type " + DataPipelineDeployedImplementationType.APACHE_SPARK_JOB.getDeployedImplementationType() + ".",
                              DataPipelineDeployedImplementationType.APACHE_SPARK_JOB.getAssociatedTypeName(),
                              false,
                              "3a57bb36-e073-4d90-9cde-06e591b70f8b",
                              DataPipelineDeployedImplementationType.APACHE_SPARK_JOB,
                              PlaceholderProperty.getResourcePlaceholderPropertyTypes(),
                              null),

    /**
     * Create an asset with the SQL Job technology type.
     */
    SQL_JOB_TEMPLATE(DataPipelineDeployedImplementationType.SQL_JOB.getDeployedImplementationType(),
                     "Create a " + DataPipelineDeployedImplementationType.SQL_JOB.getAssociatedTypeName() + " asset with technology type " + DataPipelineDeployedImplementationType.SQL_JOB.getDeployedImplementationType() + ".",
                     DataPipelineDeployedImplementationType.SQL_JOB.getAssociatedTypeName(),
                     false,
                     "7544dab2-474f-4305-b4b9-24a0719bfdec",
                     DataPipelineDeployedImplementationType.SQL_JOB,
                     PlaceholderProperty.getResourcePlaceholderPropertyTypes(),
                     null),

    /**
     * Create an asset with the Debezium Connector Task technology type.
     */
    DEBEZIUM_TASK_TEMPLATE(DataPipelineDeployedImplementationType.DEBEZIUM_TASK.getDeployedImplementationType(),
                           "Create a " + DataPipelineDeployedImplementationType.DEBEZIUM_TASK.getAssociatedTypeName() + " asset with technology type " + DataPipelineDeployedImplementationType.DEBEZIUM_TASK.getDeployedImplementationType() + ".",
                           DataPipelineDeployedImplementationType.DEBEZIUM_TASK.getAssociatedTypeName(),
                           false,
                           "10215d28-879f-4af7-9e3b-7eebac38e02c",
                           DataPipelineDeployedImplementationType.DEBEZIUM_TASK,
                           PlaceholderProperty.getResourcePlaceholderPropertyTypes(),
                           null),

    /**
     * Create an asset with the In-Memory Data Set technology type.
     */
    IN_MEMORY_DATA_SET_TEMPLATE(DataPipelineDeployedImplementationType.IN_MEMORY_DATA_SET.getDeployedImplementationType(),
                                "Create a " + DataPipelineDeployedImplementationType.IN_MEMORY_DATA_SET.getAssociatedTypeName() + " asset with technology type " + DataPipelineDeployedImplementationType.IN_MEMORY_DATA_SET.getDeployedImplementationType() + ".",
                                DataPipelineDeployedImplementationType.IN_MEMORY_DATA_SET.getAssociatedTypeName(),
                                false,
                                "b1e79ff7-4a67-4fe7-a9fc-d44a93ad5be2",
                                DataPipelineDeployedImplementationType.IN_MEMORY_DATA_SET,
                                PlaceholderProperty.getResourcePlaceholderPropertyTypes(),
                                null)

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

    private final DeployedImplementationTypeDefinition deployedImplementationType;

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
    DataPipelineTemplateType(String                               templateName,
                      String                               templateDescription,
                      String                               typeName,
                      boolean                              required,
                      String                               defaultTemplateGUID,
                      DeployedImplementationTypeDefinition deployedImplementationType,
                      List<PlaceholderPropertyType>        placeholderPropertyTypes,
                      Map<String, String>                  otherPropertyValues)
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
        return "6.3-SNAPSHOT";
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

        for (DataPipelineTemplateType templateTypeEnum : DataPipelineTemplateType.values())
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
        return "DataPipelineTemplateType{templateName='" + templateName + "'}";
    }
}
