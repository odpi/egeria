/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * GovernanceMetricHandler provides the exchange of metadata about governanceMetrics between the repository and the OMAS.
 *
 * @param <B> class that represents the governanceMetric
 */
public class GovernanceMetricHandler<B> extends ReferenceableHandler<B>
{
    private static final String qualifiedNameParameterName = "qualifiedName";

    /**
     * Construct the handler with information needed to work with B objects.
     *
     * @param converter specific converter for this bean class
     * @param beanClass name of bean class that is represented by the generic class B
     * @param serviceName name of this service
     * @param serverName name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param supportedZones list of zones that the access service is allowed to serve B instances from
     * @param defaultZones list of zones that the access service should set in all new B instances
     * @param publishZones list of zones that the access service sets up in published B instances
     * @param auditLog destination for audit log events
     */
    public GovernanceMetricHandler(OpenMetadataAPIGenericConverter<B> converter,
                                   Class<B>                           beanClass,
                                   String                             serviceName,
                                   String                             serverName,
                                   InvalidParameterHandler            invalidParameterHandler,
                                   RepositoryHandler                  repositoryHandler,
                                   OMRSRepositoryHelper               repositoryHelper,
                                   String                             localServerUserId,
                                   OpenMetadataServerSecurityVerifier securityVerifier,
                                   List<String>                       supportedZones,
                                   List<String>                       defaultZones,
                                   List<String>                       publishZones,
                                   AuditLog                           auditLog)
    {
        super(converter,
              beanClass,
              serviceName,
              serverName,
              invalidParameterHandler,
              repositoryHandler,
              repositoryHelper,
              localServerUserId,
              securityVerifier,
              supportedZones,
              defaultZones,
              publishZones,
              auditLog);
    }


    /**
     * Create the governanceMetric object.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this governanceMetric
     * @param externalSourceName unique name of the software capability that owns this governanceMetric
     * @param qualifiedName unique name for the governanceMetric - used in other configuration
     * @param displayName short display name for the governanceMetric
     * @param description description of the governance governanceMetric
     * @param measurement values describing what to measure
     * @param target values describing the desired state
     * @param domainIdentifier identifier of the governance domain - default 0 (all/any)
     * @param additionalProperties additional properties for a governanceMetric
     * @param suppliedTypeName type name from the caller (enables creation of subtypes)
     * @param extendedProperties  properties for a governance governanceMetric subtype
     * @param effectiveFrom  the time that the relationship element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the relationship must be effective to (null for any time, new Date() for now)
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the new governanceMetric object
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String createGovernanceMetric(String              userId,
                                         String              externalSourceGUID,
                                         String              externalSourceName,
                                         String              qualifiedName,
                                         String              displayName,
                                         String              description,
                                         String              measurement,
                                         String              target,
                                         int                 domainIdentifier,
                                         Map<String, String> additionalProperties,
                                         String              suppliedTypeName,
                                         Map<String, Object> extendedProperties,
                                         Date                effectiveFrom,
                                         Date                effectiveTo,
                                         Date                effectiveTime,
                                         String              methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        String typeName = OpenMetadataType.GOVERNANCE_METRIC_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.GOVERNANCE_METRIC_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        GovernanceMetricBuilder builder = new GovernanceMetricBuilder(qualifiedName,
                                                                      displayName,
                                                                      description,
                                                                      measurement,
                                                                      target,
                                                                      domainIdentifier,
                                                                      additionalProperties,
                                                                      typeGUID,
                                                                      typeName,
                                                                      extendedProperties,
                                                                      repositoryHelper,
                                                                      serviceName,
                                                                      serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        return this.createBeanInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           typeGUID,
                                           typeName,
                                           builder,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Update the anchor object that all elements in a governanceMetric (terms and categories) are linked to.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this governanceMetric
     * @param externalSourceName unique name of the software capability that owns this governanceMetric
     * @param governanceMetricGUID unique identifier of the governanceMetric to update
     * @param governanceMetricGUIDParameterName parameter passing the governanceMetricGUID
     * @param qualifiedName unique name for the governanceMetric - used in other configuration
     * @param displayName short display name for the governanceMetric
     * @param description description of the governance governanceMetric
     * @param measurement values describing what to measure
     * @param target values describing the desired state
     * @param domainIdentifier identifier of the governance domain - default 0 (all/any)
     * @param additionalProperties additional properties for a governance governanceMetric
     * @param suppliedTypeName type of governanceMetric
     * @param extendedProperties  properties for a governance governanceMetric subtype
     * @param effectiveFrom  the time that the relationship element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the relationship must be effective to (null for any time, new Date() for now)
     * @param isMergeUpdate should the properties be merged with the existing properties or completely over-write them
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void   updateGovernanceMetric(String              userId,
                                         String              externalSourceGUID,
                                         String              externalSourceName,
                                         String              governanceMetricGUID,
                                         String              governanceMetricGUIDParameterName,
                                         String              qualifiedName,
                                         String              displayName,
                                         String              description,
                                         String              measurement,
                                         String              target,
                                         int                 domainIdentifier,
                                         Map<String, String> additionalProperties,
                                         String              suppliedTypeName,
                                         Map<String, Object> extendedProperties,
                                         Date                effectiveFrom,
                                         Date                effectiveTo,
                                         boolean             isMergeUpdate,
                                         boolean             forLineage,
                                         boolean             forDuplicateProcessing,
                                         Date                effectiveTime,
                                         String              methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceMetricGUID, governanceMetricGUIDParameterName, methodName);
        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);
        }

        String typeName = OpenMetadataType.GOVERNANCE_METRIC_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.GOVERNANCE_METRIC_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        GovernanceMetricBuilder builder = new GovernanceMetricBuilder(qualifiedName,
                                                                      displayName,
                                                                      description,
                                                                      measurement,
                                                                      target,
                                                                      domainIdentifier,
                                                                      additionalProperties,
                                                                      typeGUID,
                                                                      typeName,
                                                                      extendedProperties,
                                                                      repositoryHelper,
                                                                      serviceName,
                                                                      serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    governanceMetricGUID,
                                    governanceMetricGUIDParameterName,
                                    typeGUID,
                                    typeName,
                                    forLineage,
                                    forDuplicateProcessing,
                                    supportedZones,
                                    builder.getInstanceProperties(methodName),
                                    isMergeUpdate,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Add a GovernanceDefinitionMetric relationship.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this governanceMetric
     * @param externalSourceName unique name of the software capability that owns this governanceMetric
     * @param governanceMetricGUID unique identifier of the governanceMetric
     * @param governanceMetricGUIDParameterName parameter supplying the governanceMetricGUID
     * @param governanceDefinitionGUID unique identifier of the element that is being added to the governanceMetric
     * @param governanceDefinitionGUIDParameterName parameter supplying the governanceDefinitionGUID
     * @param rationale why is the element a member? (optional)
     * @param effectiveFrom  the time that the relationship element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the relationship must be effective to (null for any time, new Date() for now)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void addGovernanceDefinitionMetric(String  userId,
                                              String  externalSourceGUID,
                                              String  externalSourceName,
                                              String  governanceMetricGUID,
                                              String  governanceMetricGUIDParameterName,
                                              String  governanceDefinitionGUID,
                                              String  governanceDefinitionGUIDParameterName,
                                              String  rationale,
                                              Date    effectiveFrom,
                                              Date    effectiveTo,
                                              boolean forLineage,
                                              boolean forDuplicateProcessing,
                                              Date    effectiveTime,
                                              String  methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     OpenMetadataType.RATIONALE_PROPERTY_NAME,
                                                                                     rationale,
                                                                                     methodName);
        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  governanceMetricGUID,
                                  governanceMetricGUIDParameterName,
                                  OpenMetadataType.GOVERNANCE_METRIC_TYPE_NAME,
                                  governanceDefinitionGUID,
                                  governanceDefinitionGUIDParameterName,
                                  OpenMetadataType.REFERENCEABLE.typeName,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataType.GOVERNANCE_DEFINITION_METRIC_TYPE_GUID,
                                  OpenMetadataType.GOVERNANCE_DEFINITION_METRIC_TYPE_GUID,
                                  properties,
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Remove a GovernanceDefinitionMetric relationship.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this governanceMetric
     * @param externalSourceName unique name of the software capability that owns this governanceMetric
     * @param governanceMetricGUID unique identifier of the governanceMetric
     * @param governanceMetricGUIDParameterName parameter supplying the governanceMetricGUID
     * @param governanceDefinitionGUID unique identifier of the element that is being added to the governanceMetric
     * @param governanceDefinitionGUIDParameterName parameter supplying the governanceDefinitionGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeGovernanceDefinitionMetric(String  userId,
                                                 String  externalSourceGUID,
                                                 String  externalSourceName,
                                                 String  governanceMetricGUID,
                                                 String  governanceMetricGUIDParameterName,
                                                 String  governanceDefinitionGUID,
                                                 String  governanceDefinitionGUIDParameterName,
                                                 boolean forLineage,
                                                 boolean forDuplicateProcessing,
                                                 Date    effectiveTime,
                                                 String  methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      governanceMetricGUID,
                                      governanceMetricGUIDParameterName,
                                      OpenMetadataType.GOVERNANCE_METRIC_TYPE_NAME,
                                      governanceDefinitionGUID,
                                      governanceDefinitionGUIDParameterName,
                                      OpenMetadataType.GOVERNANCE_DEFINITION_TYPE_GUID,
                                      OpenMetadataType.GOVERNANCE_DEFINITION_TYPE_NAME,
                                      forLineage,
                                      forDuplicateProcessing,
                                      OpenMetadataType.GOVERNANCE_DEFINITION_METRIC_TYPE_GUID,
                                      OpenMetadataType.GOVERNANCE_DEFINITION_METRIC_TYPE_GUID,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Add a GovernanceResults relationship.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this governanceMetric
     * @param externalSourceName unique name of the software capability that owns this governanceMetric
     * @param governanceMetricGUID unique identifier of the governanceMetric
     * @param governanceMetricGUIDParameterName parameter supplying the governanceMetricGUID
     * @param dataSetGUID unique identifier of the element that is being added to the governanceMetric
     * @param dataSetGUIDParameterName parameter supplying the dataSetGUID
     * @param query formula to access results (optional)
     * @param effectiveFrom  the time that the relationship element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the relationship must be effective to (null for any time, new Date() for now)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void addGovernanceResults(String  userId,
                                     String  externalSourceGUID,
                                     String  externalSourceName,
                                     String  governanceMetricGUID,
                                     String  governanceMetricGUIDParameterName,
                                     String  dataSetGUID,
                                     String  dataSetGUIDParameterName,
                                     String  query,
                                     Date    effectiveFrom,
                                     Date    effectiveTo,
                                     boolean forLineage,
                                     boolean forDuplicateProcessing,
                                     Date    effectiveTime,
                                     String  methodName) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     OpenMetadataType.QUERY_PROPERTY_NAME,
                                                                                     query,
                                                                                     methodName);
        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  governanceMetricGUID,
                                  governanceMetricGUIDParameterName,
                                  OpenMetadataType.GOVERNANCE_METRIC_TYPE_NAME,
                                  dataSetGUID,
                                  dataSetGUIDParameterName,
                                  OpenMetadataType.DATA_SET.typeName,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataType.GOVERNANCE_RESULTS_TYPE_GUID,
                                  OpenMetadataType.GOVERNANCE_RESULTS_TYPE_GUID,
                                  properties,
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Remove a GovernanceResults relationship.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this governanceMetric
     * @param externalSourceName unique name of the software capability that owns this governanceMetric
     * @param governanceMetricGUID unique identifier of the governanceMetric
     * @param governanceMetricGUIDParameterName parameter supplying the governanceMetricGUID
     * @param dataSetGUID unique identifier of the element that is being added to the governanceMetric
     * @param dataSetGUIDParameterName parameter supplying the dataSetGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeGovernanceResults(String  userId,
                                                 String  externalSourceGUID,
                                                 String  externalSourceName,
                                                 String  governanceMetricGUID,
                                                 String  governanceMetricGUIDParameterName,
                                                 String  dataSetGUID,
                                                 String  dataSetGUIDParameterName,
                                                 boolean forLineage,
                                                 boolean forDuplicateProcessing,
                                                 Date    effectiveTime,
                                                 String  methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      governanceMetricGUID,
                                      governanceMetricGUIDParameterName,
                                      OpenMetadataType.GOVERNANCE_METRIC_TYPE_NAME,
                                      dataSetGUID,
                                      dataSetGUIDParameterName,
                                      OpenMetadataType.DATA_SET.typeGUID,
                                      OpenMetadataType.DATA_SET.typeName,
                                      forLineage,
                                      forDuplicateProcessing,
                                      OpenMetadataType.GOVERNANCE_RESULTS_TYPE_GUID,
                                      OpenMetadataType.GOVERNANCE_RESULTS_TYPE_GUID,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Remove the metadata element representing a governanceMetric.  This will delete the governanceMetric and all categories and terms because
     * the Anchors classifications are set up in these elements.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this governanceMetric
     * @param externalSourceName unique name of the software capability that owns this governanceMetric
     * @param governanceMetricGUID unique identifier of the metadata element to remove
     * @param governanceMetricGUIDParameterName parameter supplying the governanceMetricGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeGovernanceMetric(String  userId,
                                       String  externalSourceGUID,
                                       String  externalSourceName,
                                       String  governanceMetricGUID,
                                       String  governanceMetricGUIDParameterName,
                                       boolean forLineage,
                                       boolean forDuplicateProcessing,
                                       Date    effectiveTime,
                                       String  methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        this.deleteBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    governanceMetricGUID,
                                    governanceMetricGUIDParameterName,
                                    OpenMetadataType.GOVERNANCE_METRIC_TYPE_GUID,
                                    OpenMetadataType.GOVERNANCE_METRIC_TYPE_NAME,
                                    null,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Retrieve the list of governanceMetric metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param searchStringParameterName name of parameter supplying the search string
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> findGovernanceMetrics(String  userId,
                                         String  searchString,
                                         String  searchStringParameterName,
                                         int     startFrom,
                                         int     pageSize,
                                         boolean forLineage,
                                         boolean forDuplicateProcessing,
                                         Date    effectiveTime,
                                         String  methodName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        return this.findBeans(userId,
                              searchString,
                              searchStringParameterName,
                              OpenMetadataType.GOVERNANCE_METRIC_TYPE_GUID,
                              OpenMetadataType.GOVERNANCE_METRIC_TYPE_NAME,
                              null,
                              startFrom,
                              pageSize,
                              forLineage,
                              forDuplicateProcessing,
                              effectiveTime,
                              methodName);
    }


    /**
     * Retrieve the list of governanceMetric metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param nameParameterName parameter supplying name
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B>   getGovernanceMetricsByName(String  userId,
                                                String  name,
                                                String  nameParameterName,
                                                int     startFrom,
                                                int     pageSize,
                                                boolean forLineage,
                                                boolean forDuplicateProcessing,
                                                Date    effectiveTime,
                                                String  methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataProperty.QUALIFIED_NAME.name);
        specificMatchPropertyNames.add(OpenMetadataProperty.DISPLAY_NAME.name);

        return this.getBeansByValue(userId,
                                    name,
                                    nameParameterName,
                                    OpenMetadataType.GOVERNANCE_METRIC_TYPE_GUID,
                                    OpenMetadataType.GOVERNANCE_METRIC_TYPE_NAME,
                                    specificMatchPropertyNames,
                                    true,
                                    null,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    supportedZones,
                                    null,
                                    startFrom,
                                    pageSize,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Retrieve the definition metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     * @param guidParameterName parameter name of guid
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> getAttachedGovernanceMetricImplementations(String  userId,
                                                              String  guid,
                                                              String  guidParameterName,
                                                              int     startFrom,
                                                              int     pageSize,
                                                              boolean forLineage,
                                                              boolean forDuplicateProcessing,
                                                              Date    effectiveTime,
                                                              String  methodName) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        List<EntityDetail> entities = this.getAttachedEntities(userId,
                                                               guid,
                                                               guidParameterName,
                                                               OpenMetadataType.GOVERNANCE_DEFINITION_TYPE_NAME,
                                                               OpenMetadataType.GOVERNANCE_DEFINITION_METRIC_TYPE_GUID,
                                                               OpenMetadataType.GOVERNANCE_DEFINITION_METRIC_TYPE_NAME,
                                                               OpenMetadataType.GOVERNANCE_METRIC_TYPE_NAME,
                                                               null,
                                                               null,
                                                               1,
                                                               forLineage,
                                                               forDuplicateProcessing,
                                                               supportedZones,
                                                               startFrom,
                                                               pageSize,
                                                               effectiveTime,
                                                               methodName);

        if (entities != null)
        {
            List<B> results = new ArrayList<>();

            for (EntityDetail entity : entities)
            {
                if (entity != null)
                {
                    List<Relationship> relationships = this.getAllAttachmentLinks(userId,
                                                                                  guid,
                                                                                  guidParameterName,
                                                                                  OpenMetadataType.GOVERNANCE_METRIC_TYPE_NAME,
                                                                                  forLineage,
                                                                                  forDuplicateProcessing,
                                                                                  effectiveTime,
                                                                                  methodName);

                    results.add(converter.getNewComplexBean(beanClass, entity, relationships, methodName));
                }
            }

            if (! results.isEmpty())
            {
                return results;
            }
        }

        return null;
    }


    /**
     * Retrieve the definition metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     * @param guidParameterName parameter name of guid
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public B getGovernanceMetricImplementations(String  userId,
                                                String  guid,
                                                String  guidParameterName,
                                                boolean forLineage,
                                                boolean forDuplicateProcessing,
                                                Date    effectiveTime,
                                                String  methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        EntityDetail entity = this.getEntityFromRepository(userId,
                                                           guid,
                                                           guidParameterName,
                                                           OpenMetadataType.GOVERNANCE_METRIC_TYPE_NAME,
                                                           null,
                                                           null,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           supportedZones,
                                                           effectiveTime,
                                                           methodName);

        if (entity != null)
        {
            List<Relationship> relationships = this.getAllAttachmentLinks(userId,
                                                                          guid,
                                                                          guidParameterName,
                                                                          OpenMetadataType.GOVERNANCE_METRIC_TYPE_NAME,
                                                                          forLineage,
                                                                          forDuplicateProcessing,
                                                                          effectiveTime,
                                                                          methodName);

            return converter.getNewComplexBean(beanClass, entity, relationships, methodName);
        }

        return null;
    }


    /**
     * Retrieve the governanceMetric metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     * @param guidParameterName parameter name of guid
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public B getGovernanceMetricByGUID(String  userId,
                                       String  guid,
                                       String  guidParameterName,
                                       boolean forLineage,
                                       boolean forDuplicateProcessing,
                                       Date    effectiveTime,
                                       String  methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        return this.getBeanFromRepository(userId,
                                          guid,
                                          guidParameterName,
                                          OpenMetadataType.GOVERNANCE_METRIC_TYPE_NAME,
                                          forLineage,
                                          forDuplicateProcessing,
                                          supportedZones,
                                          effectiveTime,
                                          methodName);

    }
}
