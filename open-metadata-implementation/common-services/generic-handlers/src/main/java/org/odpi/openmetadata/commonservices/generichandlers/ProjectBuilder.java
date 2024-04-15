/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ClassificationOrigin;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.Date;
import java.util.Map;

/**
 * ProjectBuilder creates the parts for an entity that represents a Project.
 */
public class ProjectBuilder extends ReferenceableBuilder
{
    private String identifier     = null;
    private String name           = null;
    private String description    = null;
    private Date   startDate      = null;
    private Date   plannedEndDate = null;
    private String projectPhase  = null;
    private String projectHealth  = null;
    private String projectStatus  = null;


    /**
     * Create constructor
     *
     * @param qualifiedName unique name for the Project
     * @param identifier unique identifier for the Project - typically allocated by an external system
     * @param name name for the project
     * @param description description of the glossary
     * @param startDate start date of the project
     * @param plannedEndDate planned end Date for the project
     * @param projectPhase lifecycle phase of project
     * @param projectHealth how well is the project tracking to plan
     * @param projectStatus status of the project
     * @param additionalProperties additional properties for a Project
     * @param typeGUID unique identifier of this element's type
     * @param typeName unique name of this element's type
     * @param extendedProperties  properties for a Project subtype
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public ProjectBuilder(String               qualifiedName,
                          String               identifier,
                          String               name,
                          String               description,
                          Date                 startDate,
                          Date                 plannedEndDate,
                          String               projectPhase,
                          String               projectHealth,
                          String               projectStatus,
                          Map<String, String>  additionalProperties,
                          String               typeGUID,
                          String               typeName,
                          Map<String, Object>  extendedProperties,
                          OMRSRepositoryHelper repositoryHelper,
                          String               serviceName,
                          String               serverName)
    {
        super(qualifiedName,
              additionalProperties,
              typeGUID,
              typeName,
              extendedProperties,
              repositoryHelper,
              serviceName,
              serverName);

        this.identifier = identifier;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.plannedEndDate = plannedEndDate;
        this.projectPhase = projectPhase;
        this.projectHealth = projectHealth;
        this.projectStatus = projectStatus;
    }


    /**
     * Create constructor
     *
     * @param qualifiedName unique name for the Project
     * @param identifier unique identifier for the Project - typically allocated by an external system
     * @param name name for the project
     * @param description description of the glossary
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public ProjectBuilder(String               qualifiedName,
                          String               identifier,
                          String               name,
                          String               description,
                          OMRSRepositoryHelper repositoryHelper,
                          String               serviceName,
                          String               serverName)
    {
        super(qualifiedName,
              repositoryHelper,
              serviceName,
              serverName);

        this.identifier = identifier;
        this.name = name;
        this.description = description;
    }


    /**
     * Classification constructor
     *
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    ProjectBuilder(OMRSRepositoryHelper repositoryHelper,
                   String               serviceName,
                   String               serverName)
    {
        super(OpenMetadataType.PROJECT.typeGUID,
              OpenMetadataType.PROJECT.typeName,
              repositoryHelper,
              serviceName,
              serverName);
    }


    /**
     * Set up the Campaign classification for this project.
     *
     * @param userId calling user
     * @param methodName calling method
     * @throws InvalidParameterException classification is not supported in the local repository, or any repository
     *                                   connected by an open metadata repository cohort
     */
    public void setCampaignClassification(String userId,
                                          String methodName) throws InvalidParameterException
    {
        this.setProjectTypeClassification(userId, OpenMetadataType.CAMPAIGN_CLASSIFICATION.typeName, methodName);
    }


    /**
     * Set up the Task classification for this project.
     *
     * @param userId calling user
     * @param methodName calling method
     * @throws InvalidParameterException classification is not supported in the local repository, or any repository
     *                                   connected by an open metadata repository cohort
     */
    public void setTaskClassification(String userId,
                                      String methodName) throws InvalidParameterException
    {
        this.setProjectTypeClassification(userId, OpenMetadataType.TASK_CLASSIFICATION.typeName, methodName);
    }


    /**
     * Set up the Task classification for this project.
     *
     * @param userId calling user
     * @param classificationName name of the classification to add
     * @param methodName calling method
     * @throws InvalidParameterException classification is not supported in the local repository, or any repository
     *                                   connected by an open metadata repository cohort
     */
    public void setProjectTypeClassification(String userId,
                                             String classificationName,
                                             String methodName) throws InvalidParameterException
    {
        try
        {
            Classification classification = repositoryHelper.getNewClassification(serviceName,
                                                                                  null,
                                                                                  null,
                                                                                  InstanceProvenanceType.LOCAL_COHORT,
                                                                                  userId,
                                                                                  classificationName,
                                                                                  typeName,
                                                                                  ClassificationOrigin.ASSIGNED,
                                                                                  null,
                                                                                  null);
            newClassifications.put(classification.getName(), classification);
        }
        catch (TypeErrorException error)
        {
            errorHandler.handleUnsupportedType(error, methodName, classificationName);
        }
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    @Override
    public InstanceProperties getInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.IDENTIFIER.name,
                                                                  identifier,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.NAME.name,
                                                                  name,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.DESCRIPTION.name,
                                                                  description,
                                                                  methodName);

        properties = repositoryHelper.addDatePropertyToInstance(serviceName,
                                                                properties,
                                                                OpenMetadataProperty.START_DATE.name,
                                                                startDate,
                                                                methodName);

        properties = repositoryHelper.addDatePropertyToInstance(serviceName,
                                                                properties,
                                                                OpenMetadataProperty.PLANNED_END_DATE.name,
                                                                plannedEndDate,
                                                                methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.PROJECT_PHASE.name,
                                                                  projectPhase,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.PROJECT_HEALTH.name,
                                                                  projectHealth,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.PROJECT_STATUS.name,
                                                                  projectStatus,
                                                                  methodName);

        return properties;
    }
}
