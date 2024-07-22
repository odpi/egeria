/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.survey;

import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogAnnotationType;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogSurveyRequestParameter;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyActionServiceConnector;
import org.odpi.openmetadata.frameworks.surveyaction.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.surveyaction.properties.ResourceProfileAnnotation;
import org.odpi.openmetadata.frameworks.surveyaction.properties.ResourceProfileLogAnnotation;

import java.io.IOException;
import java.util.*;

public class OSSUnityCatalogServerSurveyBase extends SurveyActionServiceConnector
{
    private static final String fullNameProperty = "Full Name";
    private static final String descriptionProperty = "Description";
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
     */
    protected void setFinalAnalysisStep() throws ConnectorCheckedException
    {
        if (connectionProperties.getConfigurationProperties() != null)
        {
            Object finalAnalysisStepPropertyObject = connectionProperties.getConfigurationProperties().get(UnityCatalogSurveyRequestParameter.FINAL_ANALYSIS_STEP.getName());

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
                if (surveyContext.getRequestParameters().get(UnityCatalogSurveyRequestParameter.FINAL_ANALYSIS_STEP.getName()) != null)
                {
                    finalAnalysisStep = surveyContext.getRequestParameters().get(UnityCatalogSurveyRequestParameter.FINAL_ANALYSIS_STEP.getName());
                }
            }
        }
    }


    /**
     * Fill out a name list annotation.
     *
     * @param annotationType type of annotation
     * @param nameList list of names with descriptions
     * @return annotation
     */
    protected ResourceProfileAnnotation getNameListAnnotation(UnityCatalogAnnotationType annotationType,
                                                              Map<String, String>        nameList)
    {
        ResourceProfileAnnotation resourceProfileAnnotation = new ResourceProfileAnnotation();

        setUpAnnotation(resourceProfileAnnotation, annotationType);

        resourceProfileAnnotation.setProfilePropertyNames(annotationType.getProfilePropertyNames());
        resourceProfileAnnotation.setProfileProperties(nameList);

        return resourceProfileAnnotation;
    }




    /**
     * Return the name of the catalog to survey; or throw an exception if there is no catalog name.
     *
     * @return name of the catalog to survey
     * @throws ConnectorCheckedException the catalog name is not specified
     */
    protected String getCatalogName() throws ConnectorCheckedException
    {
        final String methodName = "getCatalogName";

        String catalogName = null;

        if (connectionProperties.getConfigurationProperties() != null)
        {
            Object catalogNamePropertyObject = connectionProperties.getConfigurationProperties().get(UnityCatalogConfigurationProperty.CATALOG_NAME.getName());

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
     * Return the name of the schema to survey; or throw an exception if there is no catalog name.
     *
     * @return name of the catalog to survey
     * @throws ConnectorCheckedException the catalog name is not specified
     */
    protected String getSchemaName() throws ConnectorCheckedException
    {
        final String methodName = "getSchemaName";

        String schemaName = null;

        if (connectionProperties.getConfigurationProperties() != null)
        {
            Object catalogNamePropertyObject = connectionProperties.getConfigurationProperties().get(UnityCatalogConfigurationProperty.SCHEMA_NAME.getName());

            if (catalogNamePropertyObject != null)
            {
                schemaName = catalogNamePropertyObject.toString();
            }
        }

        if (surveyContext.getRequestParameters() != null)
        {
            String catalogNameProperty = surveyContext.getRequestParameters().get(UnityCatalogSurveyRequestParameter.SCHEMA_NAME.getName());

            if (catalogNameProperty != null)
            {
                schemaName = catalogNameProperty;
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
    protected void writeInventory(String              inventoryName,
                                  Map<String, String> catalogList,
                                  Map<String, String> schemaList,
                                  Map<String, String> functionList,
                                  Map<String, String> tableList,
                                  Map<String, String> volumeList) throws ConnectorCheckedException,
                                                                         InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException,
                                                                         IOException
    {
        List<String>              propertyNames = Arrays.asList(new String[]{fullNameProperty, descriptionProperty, deployedImplementationTypeProperty});
        List<Map<String, String>> propertyList = new ArrayList<>();

        this.addResourcesToPropertyList(DeployedImplementationType.OSS_UC_CATALOG.getDeployedImplementationType(), catalogList, propertyList);
        this.addResourcesToPropertyList(DeployedImplementationType.OSS_UC_SCHEMA.getDeployedImplementationType(), schemaList, propertyList);
        this.addResourcesToPropertyList(DeployedImplementationType.OSS_UC_TABLE.getDeployedImplementationType(), tableList, propertyList);
        this.addResourcesToPropertyList(DeployedImplementationType.OSS_UC_FUNCTION.getDeployedImplementationType(), functionList, propertyList);
        this.addResourcesToPropertyList(DeployedImplementationType.OSS_UC_VOLUME.getDeployedImplementationType(), volumeList, propertyList);

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
    private void addResourcesToPropertyList(String                   deployedImplementationType,
                                            Map<String, String>      resourceList,
                                            List<Map<String,String>> propertyList)
    {
        if (resourceList != null)
        {
            for (String name : resourceList.keySet())
            {
                if (name != null)
                {
                    Map<String, String> properties = new HashMap<>();

                    properties.put(fullNameProperty, name);
                    properties.put(descriptionProperty, resourceList.get(name));
                    properties.put(deployedImplementationTypeProperty, deployedImplementationType);

                    propertyList.add(properties);
                }
            }
        }
    }
}
