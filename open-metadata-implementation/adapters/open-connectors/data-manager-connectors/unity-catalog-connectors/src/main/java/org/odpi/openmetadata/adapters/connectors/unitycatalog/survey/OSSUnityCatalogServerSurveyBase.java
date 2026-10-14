/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.survey;

import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogAnnotationType;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.controls.UnityCatalogDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogSurveyRequestParameter;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCAuditCode;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCErrorCode;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.properties.ColumnInfo;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.properties.ElementBase;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.properties.FunctionInfo;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.properties.RegisteredModelInfo;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.properties.TableInfo;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.properties.VolumeInfo;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.OSSUnityCatalogResourceConnector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.ResourceProfileAnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyActionServiceConnector;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnnotationType;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.ResourceProfileLogAnnotationProperties;

import java.io.IOException;
import java.util.*;

/**
 * Common functions for the OSS Unity Catalog survey action services.
 * <br><br>
 * The surveys retrieve their information through the Unity Catalog REST API, one list call per type of resource
 * in each schema.  Each call is made independently: if one fails (for example, because the survey's userId may
 * not read a schema, or the element was deleted while the survey was running), the failure is logged and the
 * survey continues, so that one inaccessible element leaves a gap in the results rather than ending the survey.
 */
public class OSSUnityCatalogServerSurveyBase extends SurveyActionServiceConnector
{
    private static final String fullNameProperty                   = "Full Name";
    private static final String descriptionProperty                = "Description";
    private static final String creationDateProperty               = "Creation Date";
    private static final String createdByProperty                  = "Created By";
    private static final String lastUpdateProperty                 = "Last Update Date";
    private static final String lastUpdatedByProperty              = "Last Updated By";
    private static final String deployedImplementationTypeProperty = "Deployed Implementation Type";


    /**
     * Fill out a name list annotation.
     *
     * @param annotationType type of annotation
     * @param nameProperties list of names with properties
     * @return annotation
     * @throws UserNotAuthorizedException if there is a problem with the user's authorization
     */
    protected ResourceProfileAnnotationProperties getNameListAnnotation(AnnotationType                  annotationType,
                                                                        Map<String, ResourceProperties> nameProperties) throws UserNotAuthorizedException
    {
        Map<String, String> nameList = new HashMap<>();

        if (nameProperties != null)
        {
            for (String name : nameProperties.keySet())
            {
                nameList.put(name, nameProperties.get(name).description);
            }
        }

        ResourceProfileAnnotationProperties resourceProfileAnnotation = new ResourceProfileAnnotationProperties();

        setUpAnnotation(resourceProfileAnnotation, annotationType);

        List<String> propertyNames = new ArrayList<>();

        propertyNames.add(OpenMetadataProperty.PROFILE_PROPERTIES.name);

        resourceProfileAnnotation.setProfilePropertyNames(propertyNames);
        resourceProfileAnnotation.setProfileProperties(nameList);

        return resourceProfileAnnotation;
    }




    /**
     * Return the name of the catalog to survey; or throw an exception if there is no catalog name.
     *
     * @return name of the catalog to survey
     * @throws ConnectorCheckedException the catalog name is not specified
     * @throws UserNotAuthorizedException connector is disconnected
     */
    protected String getCatalogName() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "getCatalogName";

        String catalogName = null;

        if (connectionBean.getConfigurationProperties() != null)
        {
            Object catalogNamePropertyObject = connectionBean.getConfigurationProperties().get(UnityCatalogConfigurationProperty.CATALOG_NAME.getName());

            if (catalogNamePropertyObject != null)
            {
                catalogName = catalogNamePropertyObject.toString();
            }
        }

        if (surveyContext.getRequestParameters() != null)
        {
            String catalogNameProperty = surveyContext.getRequestParameters().get(UnityCatalogSurveyRequestParameter.CATALOG_NAME.getName());

            if (catalogNameProperty != null)
            {
                catalogName = catalogNameProperty;
            }
        }

        if (catalogName != null)
        {
            return catalogName;
        }

        throw new ConnectorCheckedException(UCErrorCode.MISSING_PROPERTY_NAME.getMessageDefinition(surveyActionServiceName,
                                                                                                   UnityCatalogSurveyRequestParameter.CATALOG_NAME.getName()),
                                            this.getClass().getName(),
                                            methodName);
    }


    /**
     * Return the name of the schema to survey; or throw an exception if there is no schema name.
     *
     * @return name of the catalog to survey
     * @throws ConnectorCheckedException the catalog name is not specified
     * @throws UserNotAuthorizedException connector is disconnected
     */
    protected String getSchemaName() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "getSchemaName";

        String schemaName = null;

        if (connectionBean.getConfigurationProperties() != null)
        {
            Object schemaNamePropertyObject = connectionBean.getConfigurationProperties().get(UnityCatalogConfigurationProperty.SCHEMA_NAME.getName());

            if (schemaNamePropertyObject != null)
            {
                schemaName = schemaNamePropertyObject.toString();
            }
        }

        if (surveyContext.getRequestParameters() != null)
        {
            String schemaNameProperty = surveyContext.getRequestParameters().get(UnityCatalogSurveyRequestParameter.SCHEMA_NAME.getName());

            if (schemaNameProperty != null)
            {
                schemaName = schemaNameProperty;
            }
        }

        if (schemaName != null)
        {
            return schemaName;
        }

        throw new ConnectorCheckedException(UCErrorCode.MISSING_PROPERTY_NAME.getMessageDefinition(surveyActionServiceName,
                                                                                                   UnityCatalogSurveyRequestParameter.SCHEMA_NAME.getName()),
                                            this.getClass().getName(),
                                            methodName);
    }


    /**
     * Build the inventory properties for a Unity Catalog element.
     *
     * @param element element retrieved from Unity Catalog
     * @return inventory properties
     */
    protected ResourceProperties getResourceProperties(ElementBase element)
    {
        ResourceProperties resourceProperties = new ResourceProperties();

        resourceProperties.description  = element.getComment();
        resourceProperties.creationDate = new Date(element.getCreated_at());
        resourceProperties.createdBy    = element.getCreated_by();
        if (element.getUpdated_at() != 0L)
        {
            resourceProperties.lastUpdateDate = new Date(element.getUpdated_at());
        }
        resourceProperties.lastUpdatedBy = element.getUpdated_by();
        resourceProperties.owner         = element.getOwner();

        return resourceProperties;
    }


    /**
     * Gather the volumes, tables (with their columns), registered models and functions in a schema.  Each type
     * of resource is retrieved with its own call to Unity Catalog; a call that fails is logged and the others
     * still contribute their results.
     *
     * @param ucConnector    connector to Unity Catalog
     * @param catalogName    name of the catalog that owns the schema
     * @param schemaName     name of the schema
     * @param schemaContents accumulating results
     */
    protected void surveySchemaContents(OSSUnityCatalogResourceConnector ucConnector,
                                        String                           catalogName,
                                        String                           schemaName,
                                        SchemaContents                   schemaContents)
    {
        final String schemaDescription = "schema " + catalogName + "." + schemaName;

        try
        {
            List<VolumeInfo> volumeInfos = ucConnector.listVolumes(catalogName, schemaName);

            if (volumeInfos != null)
            {
                for (VolumeInfo volumeInfo : volumeInfos)
                {
                    if (volumeInfo != null)
                    {
                        schemaContents.volumeList.put(volumeInfo.getFull_name(), this.getResourceProperties(volumeInfo));
                        schemaContents.volumeCount ++;
                    }
                }
            }
        }
        catch (Exception error)
        {
            this.logRetrievalFailure("volumes", schemaDescription, error);
        }

        try
        {
            List<TableInfo> tableInfos = ucConnector.listTables(catalogName, schemaName);

            if (tableInfos != null)
            {
                for (TableInfo tableInfo : tableInfos)
                {
                    if (tableInfo != null)
                    {
                        String             tableName          = tableInfo.getCatalog_name() + "." + tableInfo.getSchema_name() + "." + tableInfo.getName();
                        ResourceProperties resourceProperties = this.getResourceProperties(tableInfo);

                        schemaContents.tableList.put(tableName, resourceProperties);
                        schemaContents.tableCount ++;

                        if (tableInfo.getColumns() != null)
                        {
                            for (ColumnInfo columnInfo : tableInfo.getColumns())
                            {
                                ResourceProperties columnResourceProperties = new ResourceProperties(resourceProperties);

                                columnResourceProperties.description = columnInfo.getComment();
                                schemaContents.columnList.put(tableName + "." + columnInfo.getName(), columnResourceProperties);
                                schemaContents.columnCount ++;
                            }
                        }
                    }
                }
            }
        }
        catch (Exception error)
        {
            this.logRetrievalFailure("tables", schemaDescription, error);
        }

        try
        {
            List<RegisteredModelInfo> registeredModelInfos = ucConnector.listRegisteredModels(catalogName, schemaName);

            if (registeredModelInfos != null)
            {
                for (RegisteredModelInfo registeredModelInfo : registeredModelInfos)
                {
                    if (registeredModelInfo != null)
                    {
                        schemaContents.modelList.put(registeredModelInfo.getFull_name(), this.getResourceProperties(registeredModelInfo));
                        schemaContents.modelCount ++;
                    }
                }
            }
        }
        catch (Exception error)
        {
            this.logRetrievalFailure("registered models", schemaDescription, error);
        }

        try
        {
            List<FunctionInfo> functionInfos = ucConnector.listFunctions(catalogName, schemaName);

            if (functionInfos != null)
            {
                for (FunctionInfo functionInfo : functionInfos)
                {
                    if (functionInfo != null)
                    {
                        schemaContents.functionList.put(functionInfo.getFull_name(), this.getResourceProperties(functionInfo));
                        schemaContents.functionCount ++;
                    }
                }
            }
        }
        catch (Exception error)
        {
            this.logRetrievalFailure("functions", schemaDescription, error);
        }
    }


    /**
     * Record that a call to Unity Catalog failed.  The survey carries on without its results.
     *
     * @param informationSought description of the information the call was retrieving
     * @param elementName       description of the element the call was about
     * @param error             the failure
     */
    protected void logRetrievalFailure(String    informationSought,
                                       String    elementName,
                                       Exception error)
    {
        final String methodName = "logRetrievalFailure";

        logRecord(methodName, UCAuditCode.SURVEY_RETRIEVAL_FAILED.getMessageDefinition(surveyActionServiceName,
                                                                                       informationSought,
                                                                                       elementName,
                                                                                       error.getClass().getName(),
                                                                                       error.getMessage()));
    }


    /**
     * Add an inventory annotation to the survey report.
     *
     * @param inventoryName name of the inventory
     * @param catalogList list of catalog names and descriptions
     * @param schemaList list of schema names and descriptions
     * @param functionList list of function names and descriptions
     * @param tableList  list of table names and descriptions
     * @param volumeList list of volume names and descriptions
     * @throws IOException problem writing file
     * @throws InvalidParameterException problem creating CSV file asset
     * @throws PropertyServerException repository problem creating CSV file asset
     * @throws UserNotAuthorizedException authorization problem creating CSV file asset
     * @throws ConnectorCheckedException survey has been asked to stop
     */
    protected void writeInventory(String                          inventoryName,
                                  Map<String, ResourceProperties> catalogList,
                                  Map<String, ResourceProperties> schemaList,
                                  Map<String, ResourceProperties> functionList,
                                  Map<String, ResourceProperties> tableList,
                                  Map<String, ResourceProperties> volumeList) throws ConnectorCheckedException,
                                                                                     InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException,
                                                                                     IOException
    {
        List<String>              propertyNames = Arrays.asList(new String[]{fullNameProperty, descriptionProperty, creationDateProperty, createdByProperty, lastUpdateProperty, lastUpdatedByProperty, deployedImplementationTypeProperty});
        List<Map<String, String>> propertyList = new ArrayList<>();

        this.addResourcesToPropertyList(UnityCatalogDeployedImplementationType.OSS_UC_CATALOG.getDeployedImplementationType(), catalogList, propertyList);
        this.addResourcesToPropertyList(UnityCatalogDeployedImplementationType.OSS_UC_SCHEMA.getDeployedImplementationType(), schemaList, propertyList);
        this.addResourcesToPropertyList(UnityCatalogDeployedImplementationType.OSS_UC_TABLE.getDeployedImplementationType(), tableList, propertyList);
        this.addResourcesToPropertyList(UnityCatalogDeployedImplementationType.OSS_UC_FUNCTION.getDeployedImplementationType(), functionList, propertyList);
        this.addResourcesToPropertyList(UnityCatalogDeployedImplementationType.OSS_UC_VOLUME.getDeployedImplementationType(), volumeList, propertyList);

        ResourceProfileLogAnnotationProperties annotation = super.writePropertyListInventory(UnityCatalogAnnotationType.RESOURCE_INVENTORY,
                                                                                             inventoryName,
                                                                                             propertyNames,
                                                                                             propertyList,
                                                                                             surveyContext.getAnnotationStore().getSurveyReportGUID());

        surveyContext.getAnnotationStore().addAnnotation(annotation, null);
    }


    /**
     * Add a collection of resources to the property list.
     *
     * @param deployedImplementationType type of resources
     * @param resourceList details of the resources
     * @param propertyList accumulating list
     */
    private void addResourcesToPropertyList(String                          deployedImplementationType,
                                            Map<String, ResourceProperties> resourceList,
                                            List<Map<String,String>>        propertyList)
    {
        if (resourceList != null)
        {
            for (String name : resourceList.keySet())
            {
                if (name != null)
                {

                    ResourceProperties resourceProperties = resourceList.get(name);
                    if (resourceProperties != null)
                    {
                        Map<String, String> properties = new HashMap<>();

                        properties.put(fullNameProperty, name);
                        properties.put(descriptionProperty, resourceProperties.description);
                        properties.put(creationDateProperty, resourceProperties.creationDate.toString());
                        if (resourceProperties.createdBy != null)
                        {
                            properties.put(createdByProperty, resourceProperties.createdBy);
                        }
                        else
                        {
                            properties.put(createdByProperty, "???");
                        }
                        if (resourceProperties.lastUpdateDate != null)
                        {
                            properties.put(lastUpdateProperty, resourceProperties.lastUpdateDate.toString());
                        }
                        else
                        {
                            properties.put(lastUpdateProperty, "---");
                        }
                        if (resourceProperties.lastUpdatedBy != null)
                        {
                            properties.put(lastUpdatedByProperty, resourceProperties.lastUpdatedBy);
                        }
                        else
                        {
                            properties.put(lastUpdatedByProperty, "???");
                        }
                        properties.put(deployedImplementationTypeProperty, deployedImplementationType);

                        propertyList.add(properties);
                    }
                }
            }
        }
    }


    /**
     * The resources found in the schemas surveyed, accumulated across the schemas.
     */
    protected static class SchemaContents
    {
        protected final Map<String, ResourceProperties> functionList = new HashMap<>();
        protected final Map<String, ResourceProperties> tableList    = new HashMap<>();
        protected final Map<String, ResourceProperties> columnList   = new HashMap<>();
        protected final Map<String, ResourceProperties> volumeList   = new HashMap<>();
        protected final Map<String, ResourceProperties> modelList    = new HashMap<>();

        protected long functionCount = 0;
        protected long tableCount    = 0;
        protected long columnCount   = 0;
        protected long volumeCount   = 0;
        protected long modelCount    = 0;

        /**
         * Default constructor
         */
        public SchemaContents()
        {
        }
    }


    /**
     * Information about a UC resource collected for the inventory.
     */
    protected static class ResourceProperties
    {
        protected String description    = null;
        protected Date   creationDate   = null;
        protected String createdBy      = null;
        protected Date   lastUpdateDate = null;
        protected String lastUpdatedBy  = null;
        protected String owner          = null;

        /**
         * Default constructor
         */
        public ResourceProperties()
        {
        }

        /**
         * Copy/clone constructor
         *
         * @param template object to copy
         */
        public ResourceProperties(ResourceProperties template)
        {
            this.description    = template.description;
            this.creationDate   = template.creationDate;
            this.createdBy      = template.createdBy;
            this.lastUpdateDate = template.lastUpdateDate;
            this.lastUpdatedBy  = template.lastUpdatedBy;
            this.owner          = template.owner;
        }
    }
}
