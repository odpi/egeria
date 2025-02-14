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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * GovernanceDefinitionHandler provides the exchange of metadata about definition definitions between the repository and the OMAS.
 * Note definition definitions are governance metadata and are always defined with LOCAL-COHORT provenance.
 *
 * @param <B> class that represents the governance definition
 */
public class GovernanceDefinitionHandler<B> extends ReferenceableHandler<B>
{
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
    public GovernanceDefinitionHandler(OpenMetadataAPIGenericConverter<B> converter,
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
     * Create the governance definition object.
     *
     * @param userId calling user
     * @param qualifiedName unique name for the definition - used in other configuration
     * @param title short display name for the governance definition
     * @param summary brief description of the governance definition
     * @param description description of the governance definition
     * @param scope breadth of coverage of the governance definition
     * @param domainIdentifier identifier that indicates which governance domain this definition belongs to (0=all)
     * @param priority relative importance of the governance definition
     * @param implications implications to the business in adopting this governance definition
     * @param outcomes expected outcomes from implementing this governance definition
     * @param results actual results achieved from implementing this governance definition
     * @param businessImperatives for the GovernanceStrategy - how does it link to business imperatives
     * @param jurisdiction for Regulations - where does this regulation apply
     * @param implementationDescription for GovernanceControl - how should this be implemented
     * @param namePatterns for NamingStandardsRule - the pattern used to for new names
     * @param details for License or Certification - additional details about the definition
     * @param distinguishedName for Security groups - qualified name for LDAP
     * @param additionalProperties additional properties for a definition
     * @param suppliedTypeName type name from the caller (enables creation of subtypes)
     * @param extendedProperties  properties for a definition subtype
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the new definition object
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String createGovernanceDefinition(String              userId,
                                             String              qualifiedName,
                                             String              title,
                                             String              summary,
                                             String              description,
                                             String              scope,
                                             int                 domainIdentifier,
                                             String              priority,
                                             List<String>        implications,
                                             List<String>        outcomes,
                                             List<String>        results,
                                             List<String>        businessImperatives,
                                             String              jurisdiction,
                                             String              implementationDescription,
                                             List<String>        namePatterns,
                                             String              details,
                                             String              distinguishedName,
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

        String typeName = OpenMetadataType.GOVERNANCE_DEFINITION.typeName;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.GOVERNANCE_DEFINITION.typeName,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        GovernanceDefinitionBuilder builder = new GovernanceDefinitionBuilder(qualifiedName,
                                                                              title,
                                                                              summary,
                                                                              description,
                                                                              scope,
                                                                              domainIdentifier,
                                                                              priority,
                                                                              implications,
                                                                              outcomes,
                                                                              results,
                                                                              businessImperatives,
                                                                              jurisdiction,
                                                                              implementationDescription,
                                                                              namePatterns,
                                                                              details,
                                                                              distinguishedName,
                                                                              additionalProperties,
                                                                              typeGUID,
                                                                              typeName,
                                                                              extendedProperties,
                                                                              repositoryHelper,
                                                                              serviceName,
                                                                              serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        return this.createBeanInRepository(userId,
                                           null,
                                           null,
                                           typeGUID,
                                           typeName,
                                           builder,
                                           effectiveTime,
                                           methodName);
    }
    


    /**
     * Update the governance definition.
     *
     * @param userId calling user
     * @param definitionGUID unique identifier for the definition to update
     * @param definitionGUIDParameterName parameter supplying the definition
     * @param qualifiedName unique name for the definition - used in other configuration
     * @param title short display name for the governance definition
     * @param summary brief description of the governance definition
     * @param description description of the governance definition
     * @param scope breadth of coverage of the governance definition
     * @param domainIdentifier identifier that indicates which governance domain this definition belongs to (0=all)
     * @param priority relative importance of the governance definition
     * @param implications implications to the business in adopting this governance definition
     * @param outcomes expected outcomes from implementing this governance definition
     * @param results actual results achieved from implementing this governance definition
     * @param businessImperatives for the GovernanceStrategy - how does it link to business imperatives
     * @param jurisdiction for Regulations - where does this regulation apply
     * @param implementationDescription for GovernanceControl - how should this be implemented
     * @param namePatterns for NamingStandardsRule - the pattern used to for new names
     * @param details for License or Certification - additional details about the definition
     * @param distinguishedName for Security groups - qualified name for LDAP
     * @param additionalProperties additional properties for a governance definition
     * @param suppliedTypeName type of term
     * @param extendedProperties  properties for a governance definition subtype
     * @param isMergeUpdate should the supplied properties be merged with existing properties (true) only replacing the properties with
     *                      matching names, or should the entire properties of the instance be replaced?
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void updateGovernanceDefinition(String              userId,
                                           String              definitionGUID,
                                           String              definitionGUIDParameterName,
                                           String              qualifiedName,
                                           String              title,
                                           String              summary,
                                           String              description,
                                           String              scope,
                                           int                 domainIdentifier,
                                           String              priority,
                                           List<String>        implications,
                                           List<String>        outcomes,
                                           List<String>        results,
                                           List<String>        businessImperatives,
                                           String              jurisdiction,
                                           String              implementationDescription,
                                           List<String>        namePatterns,
                                           String              details,
                                           String              distinguishedName,
                                           Map<String, String> additionalProperties,
                                           String              suppliedTypeName,
                                           Map<String, Object> extendedProperties,
                                           boolean             isMergeUpdate,
                                           Date                effectiveFrom,
                                           Date                effectiveTo,
                                           boolean             forLineage,
                                           boolean             forDuplicateProcessing,
                                           Date                effectiveTime,
                                           String              methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(definitionGUID, definitionGUIDParameterName, methodName);
        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);
        }

        String typeName = OpenMetadataType.GOVERNANCE_DEFINITION.typeName;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.GOVERNANCE_DEFINITION.typeName,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        GovernanceDefinitionBuilder builder = new GovernanceDefinitionBuilder(qualifiedName,
                                                                              title,
                                                                              summary,
                                                                              description,
                                                                              scope,
                                                                              domainIdentifier,
                                                                              priority,
                                                                              implications,
                                                                              outcomes,
                                                                              results,
                                                                              businessImperatives,
                                                                              jurisdiction,
                                                                              implementationDescription,
                                                                              namePatterns,
                                                                              details,
                                                                              distinguishedName,
                                                                              additionalProperties,
                                                                              typeGUID,
                                                                              typeName,
                                                                              extendedProperties,
                                                                              repositoryHelper,
                                                                              serviceName,
                                                                              serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        this.updateBeanInRepository(userId,
                                    null,
                                    null,
                                    definitionGUID,
                                    definitionGUIDParameterName,
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
     * Create a parent-child relationship between two definitions - for example, between a governance policy and a governance control.
     * The rationale explains why they are linked.
     *
     * @param userId calling user
     * @param definitionParentGUID unique identifier of the definition super-definition
     * @param definitionParentGUIDParameterName parameter supplying the super-definition
     * @param definitionParentTypeName typename of super-definition
     * @param definitionChildGUID unique identifier of the definition sub-definition
     * @param definitionChildGUIDParameterName parameter supplying the sub-definition
     * @param definitionChildTypeName type name of the sub-definition
     * @param relationshipTypeName unique name of the relationship type
     * @param rationale why are these definitions linked
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupDelegationRelationship(String  userId,
                                            String  definitionParentGUID,
                                            String  definitionParentGUIDParameterName,
                                            String  definitionParentTypeName,
                                            String  definitionChildGUID,
                                            String  definitionChildGUIDParameterName,
                                            String  definitionChildTypeName,
                                            String  relationshipTypeName,
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
        final String typeNameParameter = "relationshipTypeName";

        invalidParameterHandler.validateName(relationshipTypeName, typeNameParameter, methodName);

        String relationshipTypeGUID = invalidParameterHandler.validateTypeName(relationshipTypeName,
                                                                               null,
                                                                               serviceName,
                                                                               methodName,
                                                                               repositoryHelper);

        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     OpenMetadataType.RATIONALE_PROPERTY_NAME,
                                                                                     rationale,
                                                                                     methodName);

        this.linkElementToElement(userId,
                                  null,
                                  null,
                                  definitionParentGUID,
                                  definitionParentGUIDParameterName,
                                  definitionParentTypeName,
                                  definitionChildGUID,
                                  definitionChildGUIDParameterName,
                                  definitionChildTypeName,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  relationshipTypeGUID,
                                  relationshipTypeName,
                                  properties,
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Create a parent-child relationship between two definitions - for example, between two governance policies.
     * The description explains why they are linked.
     *
     * @param userId calling user
     * @param definitionParentGUID unique identifier of the definition super-definition
     * @param definitionParentGUIDParameterName parameter supplying the super-definition
     * @param definitionParentTypeName typename of super-definition
     * @param definitionChildGUID unique identifier of the definition sub-definition
     * @param definitionChildGUIDParameterName parameter supplying the sub-definition
     * @param definitionChildTypeName type name of the sub-definition
     * @param relationshipTypeName unique name of the relationship type
     * @param description why are these definitions linked
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupPeerRelationship(String  userId,
                                      String  definitionParentGUID,
                                      String  definitionParentGUIDParameterName,
                                      String  definitionParentTypeName,
                                      String  definitionChildGUID,
                                      String  definitionChildGUIDParameterName,
                                      String  definitionChildTypeName,
                                      String  relationshipTypeName,
                                      String  description,
                                      Date    effectiveFrom,
                                      Date    effectiveTo,
                                      boolean forLineage,
                                      boolean forDuplicateProcessing,
                                      Date    effectiveTime,
                                      String  methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String typeNameParameter = "relationshipTypeName";

        invalidParameterHandler.validateName(relationshipTypeName, typeNameParameter, methodName);

        String relationshipTypeGUID = invalidParameterHandler.validateTypeName(relationshipTypeName,
                                                                               null,
                                                                               serviceName,
                                                                               methodName,
                                                                               repositoryHelper);

        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     OpenMetadataProperty.DESCRIPTION.name,
                                                                                     description,
                                                                                     methodName);

        this.linkElementToElement(userId,
                                  null,
                                  null,
                                  definitionParentGUID,
                                  definitionParentGUIDParameterName,
                                  definitionParentTypeName,
                                  definitionChildGUID,
                                  definitionChildGUIDParameterName,
                                  definitionChildTypeName,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  relationshipTypeGUID,
                                  relationshipTypeName,
                                  properties,
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Remove a relationship between two definitions.
     *
     * @param userId calling user
     * @param definitionParentGUID unique identifier of the definition super-definition
     * @param definitionParentGUIDParameterName parameter supplying the super-definition
     * @param definitionParentTypeName typename of super-definition
     * @param definitionChildGUID unique identifier of the definition sub-definition
     * @param definitionChildGUIDParameterName parameter supplying the sub-definition
     * @param definitionChildTypeName type name of the sub-definition
     * @param relationshipTypeName unique name of the relationship type
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearDefinitionRelationship(String  userId,
                                            String  definitionParentGUID,
                                            String  definitionParentGUIDParameterName,
                                            String  definitionParentTypeName,
                                            String  definitionChildGUID,
                                            String  definitionChildGUIDParameterName,
                                            String  definitionChildTypeName,
                                            String  relationshipTypeName,
                                            boolean forLineage,
                                            boolean forDuplicateProcessing,
                                            Date    effectiveTime,
                                            String  methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String typeNameParameter = "relationshipTypeName";
        final String definitionParentTypeNameParameter = "definitionParentTypeName";
        final String definitionChildTypeNameParameter = "definitionChildTypeName";

        invalidParameterHandler.validateName(relationshipTypeName, typeNameParameter, methodName);
        invalidParameterHandler.validateName(definitionParentTypeName, definitionParentTypeNameParameter, methodName);
        invalidParameterHandler.validateName(definitionChildTypeName, definitionChildTypeNameParameter, methodName);

        String relationshipTypeGUID = invalidParameterHandler.validateTypeName(relationshipTypeName,
                                                                               null,
                                                                               serviceName,
                                                                               methodName,
                                                                               repositoryHelper);

        String definitionChildTypeGUID = invalidParameterHandler.validateTypeName(relationshipTypeName,
                                                                                  null,
                                                                                  serviceName,
                                                                                  methodName,
                                                                                  repositoryHelper);

        this.unlinkElementFromElement(userId,
                                      false,
                                      null,
                                      null,
                                      definitionParentGUID,
                                      definitionParentGUIDParameterName,
                                      definitionParentTypeName,
                                      definitionChildGUID,
                                      definitionChildGUIDParameterName,
                                      definitionChildTypeGUID,
                                      definitionChildTypeName,
                                      forLineage,
                                      forDuplicateProcessing,
                                      relationshipTypeGUID,
                                      relationshipTypeName,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Remove the metadata element representing a governance definition.
     *
     * @param userId calling user
     * @param definitionGUID unique identifier of the metadata element to remove
     * @param definitionGUIDParameterName parameter for definitionGUID
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeGovernanceDefinition(String  userId,
                                           String  definitionGUID,
                                           String  definitionGUIDParameterName,
                                           boolean forLineage,
                                           boolean forDuplicateProcessing,
                                           Date    effectiveTime,
                                           String  methodName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        this.deleteBeanInRepository(userId,
                                    null,
                                    null,
                                    definitionGUID,
                                    definitionGUIDParameterName,
                                    OpenMetadataType.GOVERNANCE_DEFINITION.typeGUID,
                                    OpenMetadataType.GOVERNANCE_DEFINITION.typeName,
                                    null,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Retrieve the list of governance definition metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param suppliedTypeName name of the type of governance definition
     * @param searchString string to find in the properties
     * @param searchStringParameterName name of parameter supplying the search string
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> findGovernanceDefinitions(String  userId,
                                             String  suppliedTypeName,
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
        String typeName = OpenMetadataType.GOVERNANCE_DEFINITION.typeName;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.GOVERNANCE_DEFINITION.typeName,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        return this.findBeans(userId,
                              searchString,
                              searchStringParameterName,
                              typeGUID,
                              typeName,
                              startFrom,
                              pageSize,
                              null,
                              null,
                              SequencingOrder.CREATION_DATE_RECENT,
                              null,
                              forLineage,
                              forDuplicateProcessing,
                              effectiveTime,
                              methodName);
    }


    /**
     * Return information about the defined governance definitions.
     *
     * @param userId calling user
     * @param suppliedTypeName name of the type of governance definition
     * @param startFrom position in the list (used when there are so many reports that paging is needed
     * @param pageSize maximum number of elements to return on this call
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return properties of the governance definitions
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<B> getGovernanceDefinitions(String  userId,
                                            String  suppliedTypeName,
                                            int     startFrom,
                                            int     pageSize,
                                            boolean forLineage,
                                            boolean forDuplicateProcessing,
                                            Date    effectiveTime,
                                            String  methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        String typeName = OpenMetadataType.GOVERNANCE_DEFINITION.typeName;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.GOVERNANCE_DEFINITION.typeName,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);
        return this.getBeansByType(userId,
                                   typeGUID,
                                   typeName,
                                   null,
                                   null,
                                   SequencingOrder.CREATION_DATE_RECENT,
                                   null,
                                   forLineage,
                                   forDuplicateProcessing,
                                   supportedZones,
                                   startFrom,
                                   pageSize,
                                   effectiveTime,
                                   methodName);
    }


    /**
     * Return information about the defined governance zones for a specific domain.
     *
     * @param userId calling user
     * @param suppliedTypeName name of the type of governance definition
     * @param domainIdentifier identifier of domain - 0 is for all domains
     * @param startFrom position in the list (used when there are so many reports that paging is needed
     * @param pageSize maximum number of elements to return on this call
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return properties of the governance zone
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<B> getGovernanceDefinitionsByDomain(String  userId,
                                                    String  suppliedTypeName,
                                                    int     domainIdentifier,
                                                    int     startFrom,
                                                    int     pageSize,
                                                    boolean forLineage,
                                                    boolean forDuplicateProcessing,
                                                    Date    effectiveTime,
                                                    String  methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        if (domainIdentifier == 0)
        {
            return this.getGovernanceDefinitions(userId,
                                                 suppliedTypeName,
                                                 startFrom,
                                                 pageSize,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 effectiveTime,
                                                 methodName);
        }

        String typeName = OpenMetadataType.GOVERNANCE_DEFINITION.typeName;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.GOVERNANCE_DEFINITION.typeName,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        List<EntityDetail> entities = this.getEntitiesByType(userId,
                                                             typeGUID,
                                                             typeName,
                                                             null,
                                                             null,
                                                             SequencingOrder.CREATION_DATE_RECENT,
                                                             null,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             supportedZones,
                                                             startFrom,
                                                             pageSize,
                                                             effectiveTime,
                                                             methodName);

        List<B> results = new ArrayList<>();

        if (entities != null)
        {
            for (EntityDetail entity : entities)
            {
                if (entity != null)
                {
                    if (entity.getProperties() != null)
                    {
                        if (repositoryHelper.getIntProperty(serviceName,
                                                            OpenMetadataType.DOMAIN_IDENTIFIER_PROPERTY_NAME,
                                                            entity.getProperties(),
                                                            methodName) == domainIdentifier)
                        {
                            B bean = converter.getNewBean(beanClass, entity, methodName);

                            results.add(bean);
                        }
                    }
                }
            }
        }

        if (results.isEmpty())
        {
            return null;
        }

        return results;
    }


    /**
     * Retrieve the list of definition metadata elements with a matching qualified or title.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param suppliedTypeName name of the type of governance definition
     * @param name name to search for
     * @param nameParameterName parameter supplying name
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of matching governance definitions
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B>   getGovernanceDefinitionsByName(String  userId,
                                                    String  suppliedTypeName,
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
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataProperty.QUALIFIED_NAME.name);
        specificMatchPropertyNames.add(OpenMetadataProperty.TITLE.name);

        String typeName = OpenMetadataType.GOVERNANCE_DEFINITION.typeName;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.GOVERNANCE_DEFINITION.typeName,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);
        return this.getBeansByValue(userId,
                                    name,
                                    nameParameterName,
                                    typeGUID,
                                    typeName,
                                    specificMatchPropertyNames,
                                    true,
                                    null,
                                    null,
                                    null,
                                    null,
                                    SequencingOrder.CREATION_DATE_RECENT,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    supportedZones,
                                    startFrom,
                                    pageSize,
                                    effectiveTime,
                                    methodName);
    }



    /**
     * Retrieve the list of definition metadata elements with a matching qualified or title.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param typeGUID GUID of the type of governance definition
     * @param typeName name of the type of governance definition
     * @param parameterValue value to search for
     * @param parameterParameterName parameter supplying value
     * @param parameterPropertyName property name in entity to search in
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of matching governance definitions
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> getGovernanceDefinitionsByStringParameter(String  userId,
                                                             String  typeGUID,
                                                             String  typeName,
                                                             String  parameterValue,
                                                             String  parameterParameterName,
                                                             String  parameterPropertyName,
                                                             int     startFrom,
                                                             int     pageSize,
                                                             boolean forLineage,
                                                             boolean forDuplicateProcessing,
                                                             Date    effectiveTime,
                                                             String  methodName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(parameterValue, parameterParameterName, methodName);

        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(parameterPropertyName);

        return this.getBeansByValue(userId,
                                    parameterValue,
                                    parameterParameterName,
                                    typeGUID,
                                    typeName,
                                    specificMatchPropertyNames,
                                    true,
                                    null,
                                    null,
                                    null,
                                    null,
                                    SequencingOrder.CREATION_DATE_RECENT,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    supportedZones,
                                    startFrom,
                                    pageSize,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Retrieve the links to the governance definitions that define the governance for an element.
     *
     * @param userId calling user
     * @param governanceDefinitionTypeName name of the type of required governance definitions
     * @param elementGUID unique identifier of the requested metadata element
     * @param elementGUIDParameterName parameter name of the elementGUID
     * @param elementTypeName name of the type of the starting element
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of relationships
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<Relationship> getGoverningDefinitionLinks(String  userId,
                                                          String  elementGUID,
                                                          String  elementGUIDParameterName,
                                                          String  elementTypeName,
                                                          String  governanceDefinitionTypeName,
                                                          int     startFrom,
                                                          int     pageSize,
                                                          boolean forLineage,
                                                          boolean forDuplicateProcessing,
                                                          Date    effectiveTime,
                                                          String  methodName) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        String typeName = OpenMetadataType.GOVERNANCE_DEFINITION.typeName;

        if (governanceDefinitionTypeName != null)
        {
            typeName = governanceDefinitionTypeName;
        }

        return this.getAttachmentLinks(userId,
                                       elementGUID,
                                       elementGUIDParameterName,
                                       elementTypeName,
                                       OpenMetadataType.GOVERNED_BY_TYPE_GUID,
                                       OpenMetadataType.GOVERNED_BY_TYPE_NAME,
                                       null,
                                       typeName,
                                       1,
                                       null,
                                       null,
                                       SequencingOrder.CREATION_DATE_RECENT,
                                       null,
                                       forLineage,
                                       forDuplicateProcessing,
                                       supportedZones,
                                       startFrom,
                                       pageSize,
                                       effectiveTime,
                                       methodName);
    }


    /**
     * Retrieve the governance definitions that define the governance for an element.
     *
     * @param userId calling user
     * @param governanceDefinitionTypeName name of the type of required governance definitions
     * @param elementGUID unique identifier of the requested metadata element
     * @param elementGUIDParameterName parameter name of the elementGUID
     * @param elementTypeName name of the type of the starting element
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of governance definition element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> getGoverningDefinitions(String  userId,
                                           String  elementGUID,
                                           String  elementGUIDParameterName,
                                           String  elementTypeName,
                                           String  governanceDefinitionTypeName,
                                           int     startFrom,
                                           int     pageSize,
                                           boolean forLineage,
                                           boolean forDuplicateProcessing,
                                           Date    effectiveTime,
                                           String  methodName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        return this.getAttachedElements(userId,
                                        elementGUID,
                                        elementGUIDParameterName,
                                        elementTypeName,
                                        OpenMetadataType.GOVERNED_BY_TYPE_GUID,
                                        OpenMetadataType.GOVERNED_BY_TYPE_NAME,
                                        governanceDefinitionTypeName,
                                        null,
                                        null,
                                        1,
                                        null,
                                        null,
                                        SequencingOrder.CREATION_DATE_RECENT,
                                        null,
                                        forLineage,
                                        forDuplicateProcessing,
                                        startFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }



    /**
     * Retrieve the governance definitions attached to the requested scope.
     *
     * @param userId calling user
     * @param governanceDefinitionTypeName name of the type of required governance definitions
     * @param elementGUID unique identifier of the scoping metadata element
     * @param elementGUIDParameterName parameter name of the elementGUID
     * @param elementTypeName name of the type of the starting element
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of governance definition element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> getScopedGovernanceDefinitions(String  userId,
                                                 String  elementGUID,
                                                 String  elementGUIDParameterName,
                                                 String  elementTypeName,
                                                 String  governanceDefinitionTypeName,
                                                 int     startFrom,
                                                 int     pageSize,
                                                 boolean forLineage,
                                                 boolean forDuplicateProcessing,
                                                 Date    effectiveTime,
                                                 String  methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        return this.getAttachedElements(userId,
                                        elementGUID,
                                        elementGUIDParameterName,
                                        elementTypeName,
                                        OpenMetadataType.GOVERNANCE_DEFINITION_SCOPE_TYPE_GUID,
                                        OpenMetadataType.GOVERNANCE_DEFINITION_SCOPE_TYPE_NAME,
                                        governanceDefinitionTypeName,
                                        null,
                                        null,
                                        2,
                                        null,
                                        null,
                                        SequencingOrder.CREATION_DATE_RECENT,
                                        null,
                                        forLineage,
                                        forDuplicateProcessing,
                                        startFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Returns the list of roles that are responsible for the supplied governance definition.
     *
     * @param userId       String   userId of user making request.
     * @param personRoleGUID    String   unique id for element.
     * @param personRoleGUIDParameterName name of parameter supplying the GUID
     * @param startFrom int      starting position for fist returned element.
     * @param pageSize  int      maximum number of elements to return on the call.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName String calling method
     *
     * @return a list of assets or
     * @throws InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public List<B> getGovernanceResponsibilitiesForRole(String       userId,
                                                        String       personRoleGUID,
                                                        String       personRoleGUIDParameterName,
                                                        int          startFrom,
                                                        int          pageSize,
                                                        boolean      forLineage,
                                                        boolean      forDuplicateProcessing,
                                                        Date         effectiveTime,
                                                        String       methodName) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        personRoleGUID,
                                        personRoleGUIDParameterName,
                                        OpenMetadataType.PERSON_ROLE.typeName,
                                        OpenMetadataType.GOVERNANCE_RESPONSIBILITY_ASSIGNMENT.typeGUID,
                                        OpenMetadataType.GOVERNANCE_RESPONSIBILITY_ASSIGNMENT.typeName,
                                        OpenMetadataType.GOVERNANCE_RESPONSIBILITY.typeName,
                                        (String)null,
                                        null,
                                        2,
                                        null,
                                        null,
                                        SequencingOrder.CREATION_DATE_RECENT,
                                        null,
                                        forLineage,
                                        forDuplicateProcessing,
                                        supportedZones,
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
    public B getGovernanceDefinitionByGUID(String  userId,
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
                                          OpenMetadataType.GOVERNANCE_DEFINITION.typeName,
                                          forLineage,
                                          forDuplicateProcessing,
                                          supportedZones,
                                          effectiveTime,
                                          methodName);

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
    public B getGovernanceDefinitionInContext(String  userId,
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
                                                           null,
                                                           null,
                                                           OpenMetadataType.GOVERNANCE_DEFINITION.typeName,
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
                                                                          OpenMetadataType.GOVERNANCE_DEFINITION.typeName,
                                                                          null,
                                                                          null,
                                                                          SequencingOrder.CREATION_DATE_RECENT,
                                                                          null,
                                                                          forLineage,
                                                                          forDuplicateProcessing,
                                                                          effectiveTime,
                                                                          methodName);

            return converter.getNewComplexBean(beanClass, entity, relationships, methodName);
        }

        return null;
    }
}
