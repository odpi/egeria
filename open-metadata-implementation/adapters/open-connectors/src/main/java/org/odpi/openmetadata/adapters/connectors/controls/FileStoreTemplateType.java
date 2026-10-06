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
 * FileStoreTemplateType describes the catalog templates supplied with Egeria for files and folders in object
 * stores, distributed file systems and local file systems.  Each template creates just the asset, identified by
 * its resource name within a namespace (for example the name and namespace that OpenLineage gives a dataset or
 * job).
 */
public enum FileStoreTemplateType implements TemplateDefinition
{
    /**
     * Create an asset with the Amazon S3 Object technology type.
     */
    AMAZON_S3_OBJECT_TEMPLATE(FileStoreDeployedImplementationType.AMAZON_S3_OBJECT.getDeployedImplementationType(),
                              "Create a " + FileStoreDeployedImplementationType.AMAZON_S3_OBJECT.getAssociatedTypeName() + " asset with technology type " + FileStoreDeployedImplementationType.AMAZON_S3_OBJECT.getDeployedImplementationType() + ".",
                              FileStoreDeployedImplementationType.AMAZON_S3_OBJECT.getAssociatedTypeName(),
                              false,
                              "10313788-4f3e-4fa5-9e7d-f91944c0bc29",
                              FileStoreDeployedImplementationType.AMAZON_S3_OBJECT,
                              PlaceholderProperty.getFileResourcePlaceholderPropertyTypes(),
                              null),

    /**
     * Create an asset with the Amazon S3 Folder technology type.
     */
    AMAZON_S3_FOLDER_TEMPLATE(FileStoreDeployedImplementationType.AMAZON_S3_FOLDER.getDeployedImplementationType(),
                              "Create a " + FileStoreDeployedImplementationType.AMAZON_S3_FOLDER.getAssociatedTypeName() + " asset with technology type " + FileStoreDeployedImplementationType.AMAZON_S3_FOLDER.getDeployedImplementationType() + ".",
                              FileStoreDeployedImplementationType.AMAZON_S3_FOLDER.getAssociatedTypeName(),
                              false,
                              "edfff2d9-3b4a-4908-8cd1-193dce4069b1",
                              FileStoreDeployedImplementationType.AMAZON_S3_FOLDER,
                              PlaceholderProperty.getFolderResourcePlaceholderPropertyTypes(),
                              null),

    /**
     * Create an asset with the Google Cloud Storage Object technology type.
     */
    GOOGLE_CLOUD_STORAGE_OBJECT_TEMPLATE(FileStoreDeployedImplementationType.GOOGLE_CLOUD_STORAGE_OBJECT.getDeployedImplementationType(),
                                         "Create a " + FileStoreDeployedImplementationType.GOOGLE_CLOUD_STORAGE_OBJECT.getAssociatedTypeName() + " asset with technology type " + FileStoreDeployedImplementationType.GOOGLE_CLOUD_STORAGE_OBJECT.getDeployedImplementationType() + ".",
                                         FileStoreDeployedImplementationType.GOOGLE_CLOUD_STORAGE_OBJECT.getAssociatedTypeName(),
                                         false,
                                         "24f778c8-66cb-4aba-ad14-184c7208e0ac",
                                         FileStoreDeployedImplementationType.GOOGLE_CLOUD_STORAGE_OBJECT,
                                         PlaceholderProperty.getFileResourcePlaceholderPropertyTypes(),
                                         null),

    /**
     * Create an asset with the Google Cloud Storage Folder technology type.
     */
    GOOGLE_CLOUD_STORAGE_FOLDER_TEMPLATE(FileStoreDeployedImplementationType.GOOGLE_CLOUD_STORAGE_FOLDER.getDeployedImplementationType(),
                                         "Create a " + FileStoreDeployedImplementationType.GOOGLE_CLOUD_STORAGE_FOLDER.getAssociatedTypeName() + " asset with technology type " + FileStoreDeployedImplementationType.GOOGLE_CLOUD_STORAGE_FOLDER.getDeployedImplementationType() + ".",
                                         FileStoreDeployedImplementationType.GOOGLE_CLOUD_STORAGE_FOLDER.getAssociatedTypeName(),
                                         false,
                                         "2a94fff9-6997-48ff-8acf-5db2b7546a47",
                                         FileStoreDeployedImplementationType.GOOGLE_CLOUD_STORAGE_FOLDER,
                                         PlaceholderProperty.getFolderResourcePlaceholderPropertyTypes(),
                                         null),

    /**
     * Create an asset with the Azure Blob Storage Object technology type.
     */
    AZURE_BLOB_STORAGE_OBJECT_TEMPLATE(FileStoreDeployedImplementationType.AZURE_BLOB_STORAGE_OBJECT.getDeployedImplementationType(),
                                       "Create a " + FileStoreDeployedImplementationType.AZURE_BLOB_STORAGE_OBJECT.getAssociatedTypeName() + " asset with technology type " + FileStoreDeployedImplementationType.AZURE_BLOB_STORAGE_OBJECT.getDeployedImplementationType() + ".",
                                       FileStoreDeployedImplementationType.AZURE_BLOB_STORAGE_OBJECT.getAssociatedTypeName(),
                                       false,
                                       "e7e6056b-9b6c-470f-89bf-2e91bfe548a8",
                                       FileStoreDeployedImplementationType.AZURE_BLOB_STORAGE_OBJECT,
                                       PlaceholderProperty.getFileResourcePlaceholderPropertyTypes(),
                                       null),

    /**
     * Create an asset with the Azure Blob Storage Folder technology type.
     */
    AZURE_BLOB_STORAGE_FOLDER_TEMPLATE(FileStoreDeployedImplementationType.AZURE_BLOB_STORAGE_FOLDER.getDeployedImplementationType(),
                                       "Create a " + FileStoreDeployedImplementationType.AZURE_BLOB_STORAGE_FOLDER.getAssociatedTypeName() + " asset with technology type " + FileStoreDeployedImplementationType.AZURE_BLOB_STORAGE_FOLDER.getDeployedImplementationType() + ".",
                                       FileStoreDeployedImplementationType.AZURE_BLOB_STORAGE_FOLDER.getAssociatedTypeName(),
                                       false,
                                       "ac3e792a-9458-4bcd-bae8-979bacde067f",
                                       FileStoreDeployedImplementationType.AZURE_BLOB_STORAGE_FOLDER,
                                       PlaceholderProperty.getFolderResourcePlaceholderPropertyTypes(),
                                       null),

    /**
     * Create an asset with the Azure Data Lake Storage File technology type.
     */
    AZURE_DATA_LAKE_STORAGE_FILE_TEMPLATE(FileStoreDeployedImplementationType.AZURE_DATA_LAKE_STORAGE_FILE.getDeployedImplementationType(),
                                          "Create a " + FileStoreDeployedImplementationType.AZURE_DATA_LAKE_STORAGE_FILE.getAssociatedTypeName() + " asset with technology type " + FileStoreDeployedImplementationType.AZURE_DATA_LAKE_STORAGE_FILE.getDeployedImplementationType() + ".",
                                          FileStoreDeployedImplementationType.AZURE_DATA_LAKE_STORAGE_FILE.getAssociatedTypeName(),
                                          false,
                                          "3ee29e2e-c3dc-4690-95f4-0dbdfce0ffe0",
                                          FileStoreDeployedImplementationType.AZURE_DATA_LAKE_STORAGE_FILE,
                                          PlaceholderProperty.getFileResourcePlaceholderPropertyTypes(),
                                          null),

    /**
     * Create an asset with the Azure Data Lake Storage Directory technology type.
     */
    AZURE_DATA_LAKE_STORAGE_DIRECTORY_TEMPLATE(FileStoreDeployedImplementationType.AZURE_DATA_LAKE_STORAGE_DIRECTORY.getDeployedImplementationType(),
                                               "Create a " + FileStoreDeployedImplementationType.AZURE_DATA_LAKE_STORAGE_DIRECTORY.getAssociatedTypeName() + " asset with technology type " + FileStoreDeployedImplementationType.AZURE_DATA_LAKE_STORAGE_DIRECTORY.getDeployedImplementationType() + ".",
                                               FileStoreDeployedImplementationType.AZURE_DATA_LAKE_STORAGE_DIRECTORY.getAssociatedTypeName(),
                                               false,
                                               "b6aaf460-586e-4ea3-87e8-c412ed372dc3",
                                               FileStoreDeployedImplementationType.AZURE_DATA_LAKE_STORAGE_DIRECTORY,
                                               PlaceholderProperty.getFolderResourcePlaceholderPropertyTypes(),
                                               null),

    /**
     * Create an asset with the Apache Hadoop HDFS File technology type.
     */
    HDFS_FILE_TEMPLATE(FileStoreDeployedImplementationType.HDFS_FILE.getDeployedImplementationType(),
                       "Create a " + FileStoreDeployedImplementationType.HDFS_FILE.getAssociatedTypeName() + " asset with technology type " + FileStoreDeployedImplementationType.HDFS_FILE.getDeployedImplementationType() + ".",
                       FileStoreDeployedImplementationType.HDFS_FILE.getAssociatedTypeName(),
                       false,
                       "11916af2-3aa6-47bd-b913-7c2a2ccabc8b",
                       FileStoreDeployedImplementationType.HDFS_FILE,
                       PlaceholderProperty.getFileResourcePlaceholderPropertyTypes(),
                       null),

    /**
     * Create an asset with the Apache Hadoop HDFS Directory technology type.
     */
    HDFS_DIRECTORY_TEMPLATE(FileStoreDeployedImplementationType.HDFS_DIRECTORY.getDeployedImplementationType(),
                            "Create a " + FileStoreDeployedImplementationType.HDFS_DIRECTORY.getAssociatedTypeName() + " asset with technology type " + FileStoreDeployedImplementationType.HDFS_DIRECTORY.getDeployedImplementationType() + ".",
                            FileStoreDeployedImplementationType.HDFS_DIRECTORY.getAssociatedTypeName(),
                            false,
                            "36f0ae76-a76f-4e8c-a98c-f6c25ea642ca",
                            FileStoreDeployedImplementationType.HDFS_DIRECTORY,
                            PlaceholderProperty.getFolderResourcePlaceholderPropertyTypes(),
                            null),

    /**
     * Create an asset with the Databricks File System File technology type.
     */
    DATABRICKS_FILE_SYSTEM_FILE_TEMPLATE(FileStoreDeployedImplementationType.DATABRICKS_FILE_SYSTEM_FILE.getDeployedImplementationType(),
                                         "Create a " + FileStoreDeployedImplementationType.DATABRICKS_FILE_SYSTEM_FILE.getAssociatedTypeName() + " asset with technology type " + FileStoreDeployedImplementationType.DATABRICKS_FILE_SYSTEM_FILE.getDeployedImplementationType() + ".",
                                         FileStoreDeployedImplementationType.DATABRICKS_FILE_SYSTEM_FILE.getAssociatedTypeName(),
                                         false,
                                         "cb6ecace-558a-447d-8ff1-1203060ab891",
                                         FileStoreDeployedImplementationType.DATABRICKS_FILE_SYSTEM_FILE,
                                         PlaceholderProperty.getFileResourcePlaceholderPropertyTypes(),
                                         null),

    /**
     * Create an asset with the Databricks File System Directory technology type.
     */
    DATABRICKS_FILE_SYSTEM_DIRECTORY_TEMPLATE(FileStoreDeployedImplementationType.DATABRICKS_FILE_SYSTEM_DIRECTORY.getDeployedImplementationType(),
                                              "Create a " + FileStoreDeployedImplementationType.DATABRICKS_FILE_SYSTEM_DIRECTORY.getAssociatedTypeName() + " asset with technology type " + FileStoreDeployedImplementationType.DATABRICKS_FILE_SYSTEM_DIRECTORY.getDeployedImplementationType() + ".",
                                              FileStoreDeployedImplementationType.DATABRICKS_FILE_SYSTEM_DIRECTORY.getAssociatedTypeName(),
                                              false,
                                              "988aa4dc-6fef-4a75-a837-5614f99b7040",
                                              FileStoreDeployedImplementationType.DATABRICKS_FILE_SYSTEM_DIRECTORY,
                                              PlaceholderProperty.getFolderResourcePlaceholderPropertyTypes(),
                                              null),

    /**
     * Create an asset with the Data File technology type.
     */
    LOCAL_FILE_TEMPLATE(DeployedImplementationType.DATA_FILE.getDeployedImplementationType(),
                        "Create a " + DeployedImplementationType.DATA_FILE.getAssociatedTypeName() + " asset with technology type " + DeployedImplementationType.DATA_FILE.getDeployedImplementationType() + ".",
                        DeployedImplementationType.DATA_FILE.getAssociatedTypeName(),
                        false,
                        "ff2a4de1-d8e0-40ab-a5e2-212f06c6a6dd",
                        DeployedImplementationType.DATA_FILE,
                        PlaceholderProperty.getFileResourcePlaceholderPropertyTypes(),
                        null),

    /**
     * Create an asset with the Data Folder technology type.
     */
    LOCAL_FOLDER_TEMPLATE(DeployedImplementationType.DATA_FOLDER.getDeployedImplementationType(),
                          "Create a " + DeployedImplementationType.DATA_FOLDER.getAssociatedTypeName() + " asset with technology type " + DeployedImplementationType.DATA_FOLDER.getDeployedImplementationType() + ".",
                          DeployedImplementationType.DATA_FOLDER.getAssociatedTypeName(),
                          false,
                          "589934b4-3902-4ed0-9ae1-f40880eb0096",
                          DeployedImplementationType.DATA_FOLDER,
                          PlaceholderProperty.getFolderResourcePlaceholderPropertyTypes(),
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
    FileStoreTemplateType(String                               templateName,
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
        return "6.2";
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

        for (FileStoreTemplateType templateTypeEnum : FileStoreTemplateType.values())
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
        return "FileStoreTemplateType{templateName='" + templateName + "'}";
    }
}
