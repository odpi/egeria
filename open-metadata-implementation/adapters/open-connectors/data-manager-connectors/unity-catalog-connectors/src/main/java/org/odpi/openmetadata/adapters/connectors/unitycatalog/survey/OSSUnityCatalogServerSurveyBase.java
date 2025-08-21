/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.survey;

import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogAnnotationType;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogSurveyRequestParameter;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyActionServiceConnector;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnnotationType;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyRequestParameter;
import org.odpi.openmetadata.frameworks.opensurvey.properties.ResourceProfileAnnotation;
import org.odpi.openmetadata.frameworks.opensurvey.properties.ResourceProfileLogAnnotation;

import java.io.IOException;
import java.util.*;

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
     * This is the point after which the processing stops.  The default is PRODUCE_INVENTORY - which
     * is the last analysis step.  It can be changed through configuration properties or
     * request parameters passed when this survey action service is called.
     */
    protected String finalAnalysisStep = AnalysisStep.PRODUCE_INVENTORY.getName();


    /**
     * Retrieve any configured final analysis step
     *
     * @throws ConnectorCheckedException problem with context
     * @throws UserNotAuthorizedException connector is disconnected
     */
    protected void setFinalAnalysisStep() throws ConnectorCheckedException,
                                                 UserNotAuthorizedException
    {
        if (connectionBean.getConfigurationProperties() != null)
        {
            Object finalAnalysisStepPropertyObject = connectionBean.getConfigurationProperties().get(SurveyRequestParameter.FINAL_ANALYSIS_STEP.getName());

            if (finalAnalysisStepPropertyObject != null)
            {
                finalAnalysisStep = finalAnalysisStepPropertyObject.toString();
            }

            /*
             * The finalAnalysisStep property in requestParameters takes precedent over the value in the
             * configuration properties.
             */
            if (surveyContext.getRequestParameters() != null)
            {
                if (surveyContext.getRequestParameters().get(SurveyRequestParameter.FINAL_ANALYSIS_STEP.getName()) != null)
                {
                    finalAnalysisStep = surveyContext.getRequestParameters().get(SurveyRequestParameter.FINAL_ANALYSIS_STEP.getName());
                }
            }
        }
    }


    /**
     * Fill out a name list annotation.
     *
     * @param annotationType type of annotation
     * @param nameProperties list of names with properties
     * @return annotation
     */
    protected ResourceProfileAnnotation getNameListAnnotation(AnnotationType                  annotationType,
                                                              Map<String, ResourceProperties> nameProperties)
    {
        Map<String, String> nameList = new HashMap<>();

        if (nameProperties != null)
        {
            for (String name : nameProperties.keySet())
            {
                nameList.put(name, nameProperties.get(name).description);
            }
        }

        ResourceProfileAnnotation resourceProfileAnnotation = new ResourceProfileAnnotation();

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

        ResourceProfileLogAnnotation annotation = super.writePropertyListInventory(UnityCatalogAnnotationType.RESOURCE_INVENTORY,
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
