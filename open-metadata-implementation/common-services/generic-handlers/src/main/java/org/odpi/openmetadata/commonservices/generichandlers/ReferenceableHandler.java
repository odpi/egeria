/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.enums.GlossaryTermAssignmentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.mapper.PropertyFacetValidValues;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


/**
 * ReferenceableHandler manages methods on generic referenceables.
 */
public class ReferenceableHandler<B> extends OpenMetadataAPITemplateHandler<B>
{
    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param converter specific converter for this bean class
     * @param beanClass name of bean class that is represented by the generic class B
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param supportedZones list of zones that the access service is allowed to serve Asset instances from.
     * @param defaultZones list of zones that the access service should set in all new Asset instances.
     * @param publishZones list of zones that the access service sets up in published Asset instances.
     * @param auditLog destination for audit log events.
     */
    public ReferenceableHandler(OpenMetadataAPIGenericConverter<B> converter,
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
     * Return the unique identifier of the bean with the requested qualified name.
     * The match is exact.  It uses the supportedZones supplied with the service.
     *
     * @param userId calling user
     * @param typeGUID unique identifier of the asset type to search for (null for the generic Asset type)
     * @param typeName unique identifier of the asset type to search for (null for the generic Asset type)
     * @param name name to search for
     * @param nameParameterName property that provided the name
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return matching B bean
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String getBeanGUIDByQualifiedName(String       userId,
                                             String       typeGUID,
                                             String       typeName,
                                             String       name,
                                             String       nameParameterName,
                                             boolean      forLineage,
                                             boolean      forDuplicateProcessing,
                                             Date         effectiveTime,
                                             String       methodName) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        return this.getBeanGUIDByQualifiedName(userId,
                                               typeGUID,
                                               typeName,
                                               name,
                                               nameParameterName,
                                               forLineage,
                                               forDuplicateProcessing,
                                               supportedZones,
                                               effectiveTime,
                                               methodName);
    }


    /**
     * Return the bean with the requested qualified name.
     * The match is exact.
     *
     * @param userId calling user
     * @param typeGUID unique identifier of the asset type to search for (null for the generic Asset type)
     * @param typeName unique identifier of the asset type to search for (null for the generic Asset type)
     * @param name name to search for
     * @param nameParameterName property that provided the name
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param serviceSupportedZones list of supported zones for this service
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return matching B bean
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String getBeanGUIDByQualifiedName(String       userId,
                                             String       typeGUID,
                                             String       typeName,
                                             String       name,
                                             String       nameParameterName,
                                             boolean      forLineage,
                                             boolean      forDuplicateProcessing,
                                             List<String> serviceSupportedZones,
                                             Date         effectiveTime,
                                             String       methodName) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        String resultTypeGUID = OpenMetadataType.REFERENCEABLE.typeGUID;
        String resultTypeName = OpenMetadataType.REFERENCEABLE.typeName;

        if (typeGUID != null)
        {
            resultTypeGUID = typeGUID;
        }
        if (typeName != null)
        {
            resultTypeName = typeName;
        }

        return this.getBeanGUIDByUniqueName(userId,
                                            name,
                                            nameParameterName,
                                            OpenMetadataProperty.QUALIFIED_NAME.name,
                                            resultTypeGUID,
                                            resultTypeName,
                                            null,
                                            null,
                                            SequencingOrder.CREATION_DATE_RECENT,
                                            null,
                                            forLineage,
                                            forDuplicateProcessing,
                                            serviceSupportedZones,
                                            effectiveTime,
                                            methodName);
    }


    /**
     * Return the bean with the requested qualified name.
     * The match is exact. It uses the supportedZones supplied with the service.
     *
     * @param userId calling user
     * @param typeGUID unique identifier of the asset type to search for (null for the generic Asset type)
     * @param typeName unique identifier of the asset type to search for (null for the generic Asset type)
     * @param name name to search for
     * @param nameParameterName property that provided the name
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return matching B bean
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public B getBeanByQualifiedName(String  userId,
                                    String  typeGUID,
                                    String  typeName,
                                    String  name,
                                    String  nameParameterName,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing,
                                    Date    effectiveTime,
                                    String  methodName) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        return this.getBeanByQualifiedName(userId,
                                           typeGUID,
                                           typeName,
                                           name,
                                           nameParameterName,
                                           forLineage,
                                           forDuplicateProcessing,
                                           supportedZones,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Return the bean with the requested qualified name.
     * The match is exact.
     *
     * @param userId calling user
     * @param typeGUID unique identifier of the asset type to search for (null for the generic Asset type)
     * @param typeName unique identifier of the asset type to search for (null for the generic Asset type)
     * @param name name to search for
     * @param nameParameterName property that provided the name
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param serviceSupportedZones list of supported zones for this service
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return matching B bean
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public B getBeanByQualifiedName(String       userId,
                                    String       typeGUID,
                                    String       typeName,
                                    String       name,
                                    String       nameParameterName,
                                    boolean      forLineage,
                                    boolean      forDuplicateProcessing,
                                    List<String> serviceSupportedZones,
                                    Date         effectiveTime,
                                    String       methodName) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        String resultTypeGUID = OpenMetadataType.REFERENCEABLE.typeGUID;
        String resultTypeName = OpenMetadataType.REFERENCEABLE.typeName;

        if (typeGUID != null)
        {
            resultTypeGUID = typeGUID;
        }
        if (typeName != null)
        {
            resultTypeName = typeName;
        }

        return this.getBeanByUniqueName(userId,
                                        name,
                                        nameParameterName,
                                        OpenMetadataProperty.QUALIFIED_NAME.name,
                                        resultTypeGUID,
                                        resultTypeName,
                                        null,
                                        null,
                                        SequencingOrder.CREATION_DATE_RECENT,
                                        null,
                                        forLineage,
                                        forDuplicateProcessing,
                                        serviceSupportedZones,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Return the entity for a qualified name that is effective at a specific time -
     * if multiple entities have this name, an exception is thrown.
     *
     * @param userId calling user
     * @param typeGUID unique identifier of the asset type to search for (null for the generic Asset type)
     * @param typeName unique identifier of the asset type to search for (null for the generic Asset type)
     * @param name name to search for
     * @param nameParameterName property that provided the name
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return list of B beans
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public EntityDetail getEntityByUniqueQualifiedName(String       userId,
                                                       String       typeGUID,
                                                       String       typeName,
                                                       String       name,
                                                       String       nameParameterName,
                                                       boolean      forLineage,
                                                       boolean      forDuplicateProcessing,
                                                       Date         effectiveTime,
                                                       String       methodName) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        String resultTypeGUID = OpenMetadataType.REFERENCEABLE.typeGUID;
        String resultTypeName = OpenMetadataType.REFERENCEABLE.typeName;

        if (typeGUID != null)
        {
            resultTypeGUID = typeGUID;
        }
        if (typeName != null)
        {
            resultTypeName = typeName;
        }

        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataProperty.QUALIFIED_NAME.name);

        List<EntityDetail> matchingEntities = this.getEntitiesByValue(userId,
                                                                      name,
                                                                      nameParameterName,
                                                                      resultTypeGUID,
                                                                      resultTypeName,
                                                                      specificMatchPropertyNames,
                                                                      true,
                                                                      false,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      SequencingOrder.CREATION_DATE_RECENT,
                                                                      null,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      supportedZones,
                                                                      0,
                                                                      0,
                                                                      effectiveTime,
                                                                      methodName);

        if (matchingEntities == null)
        {
            return null;
        }
        else if (matchingEntities.size() == 1)
        {
            return matchingEntities.get(0);
        }

        errorHandler.handleAmbiguousEntityName(name,
                                               nameParameterName,
                                               resultTypeName,
                                               matchingEntities,
                                               methodName);
        /* not reachable */
        return null;
    }


    /**
     * Add or replace the ownership for a referenceable.
     *
     * @param userId calling user
     * @param beanGUID unique identifier of bean
     * @param beanGUIDParameterName name of parameter supplying the beanGUID
     * @param beanGUIDTypeName type of bean
     * @param owner name of the owner
     * @param ownerTypeName type of element that owner comes from
     * @param ownerPropertyName name of property used to identify owner
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void addOwner(String  userId,
                         String  beanGUID,
                         String  beanGUIDParameterName,
                         String  beanGUIDTypeName,
                         String  owner,
                         String  ownerTypeName,
                         String  ownerPropertyName,
                         boolean forLineage,
                         boolean forDuplicateProcessing,
                         Date    effectiveTime,
                         String  methodName) throws InvalidParameterException,
                                                    UserNotAuthorizedException,
                                                    PropertyServerException
    {
        ReferenceableBuilder builder = new ReferenceableBuilder(OpenMetadataType.REFERENCEABLE.typeGUID,
                                                                OpenMetadataType.REFERENCEABLE.typeName,
                                                                repositoryHelper,
                                                                serviceName,
                                                                serverName);

        this.setClassificationInRepository(userId,
                                           null,
                                           null,
                                           beanGUID,
                                           beanGUIDParameterName,
                                           beanGUIDTypeName,
                                           OpenMetadataType.OWNERSHIP_CLASSIFICATION.typeGUID,
                                           OpenMetadataType.OWNERSHIP_CLASSIFICATION.typeName,
                                           builder.getOwnershipProperties(owner, ownerTypeName, ownerPropertyName, methodName),
                                           true,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Remove the ownership classification from a referenceable.
     *
     * @param userId calling user
     * @param beanGUID unique identifier of entity to update
     * @param beanGUIDParameterName name of parameter providing beanGUID
     * @param beanGUIDTypeName type of bean
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeOwner(String  userId,
                             String  beanGUID,
                             String  beanGUIDParameterName,
                             String  beanGUIDTypeName,
                             boolean forLineage,
                             boolean forDuplicateProcessing,
                             Date    effectiveTime,
                             String  methodName) throws InvalidParameterException,
                                                        UserNotAuthorizedException,
                                                        PropertyServerException
    {
        this.removeClassificationFromRepository(userId,
                                                null,
                                                null,
                                                beanGUID,
                                                beanGUIDParameterName,
                                                beanGUIDTypeName,
                                                OpenMetadataType.OWNERSHIP_CLASSIFICATION.typeGUID,
                                                OpenMetadataType.OWNERSHIP_CLASSIFICATION.typeName,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
    }


    /**
     * Returns the list of elements that are classified with a specific owner.
     *
     * @param userId       String   userId of user making request.
     * @param ownerName    String   unique identifier for the owner.
     * @param startFrom int      starting position for fist returned element.
     * @param pageSize  int      maximum number of elements to return on the call.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName String calling method
     *
     * @return a list of elements or
     * @throws InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public List<B> getOwnersElements(String       userId,
                                     String       ownerName,
                                     int          startFrom,
                                     int          pageSize,
                                     boolean      forLineage,
                                     boolean      forDuplicateProcessing,
                                     Date         effectiveTime,
                                     String       methodName) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String nameParameterName = "ownerName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(ownerName, nameParameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        InstanceProperties classificationMatchProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                                        null,
                                                                                                        OpenMetadataProperty.OWNER.name,
                                                                                                        ownerName,
                                                                                                        methodName);

        List<EntityDetail> entities = repositoryHandler.getEntitiesForClassificationType(userId,
                                                                                         OpenMetadataType.REFERENCEABLE.typeGUID,
                                                                                         OpenMetadataType.OWNERSHIP_CLASSIFICATION.typeName,
                                                                                         classificationMatchProperties,
                                                                                         MatchCriteria.ANY,
                                                                                         null,
                                                                                         null,
                                                                                         SequencingOrder.CREATION_DATE_RECENT,
                                                                                         null,
                                                                                         forLineage,
                                                                                         forDuplicateProcessing,
                                                                                         startFrom,
                                                                                         queryPageSize,
                                                                                         effectiveTime,
                                                                                         methodName);

        return super.getValidatedBeans(userId,
                                       effectiveTime,
                                       forLineage,
                                       forDuplicateProcessing,
                                       supportedZones,
                                       methodName,
                                       entities);
    }


    /**
     * Add or replace the security tags for a referenceable.
     *
     * @param userId calling user
     * @param beanGUID unique identifier of bean
     * @param beanGUIDParameterName name of parameter supplying the beanGUID
     * @param beanGUIDTypeName type of bean
     * @param securityLabels list of security labels defining the security characteristics of the element
     * @param securityProperties descriptive labels describing origin of the element
     * @param accessGroups map from operation to list of security groups
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addSecurityTags(String                   userId,
                                 String                   beanGUID,
                                 String                   beanGUIDParameterName,
                                 String                   beanGUIDTypeName,
                                 List<String>             securityLabels,
                                 Map<String, Object>      securityProperties,
                                 Map<String,List<String>> accessGroups,
                                 boolean                  forLineage,
                                 boolean                  forDuplicateProcessing,
                                 Date                     effectiveTime,
                                 String                   methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        ReferenceableBuilder builder = new ReferenceableBuilder(OpenMetadataType.REFERENCEABLE.typeGUID,
                                                                OpenMetadataType.REFERENCEABLE.typeName,
                                                                repositoryHelper,
                                                                serviceName,
                                                                serverName);

        this.setClassificationInRepository(userId,
                                           null,
                                           null,
                                           beanGUID,
                                           beanGUIDParameterName,
                                           beanGUIDTypeName,
                                           OpenMetadataType.SECURITY_TAGS_CLASSIFICATION_TYPE_GUID,
                                           OpenMetadataType.SECURITY_TAGS_CLASSIFICATION_TYPE_NAME,
                                           builder.getSecurityTagProperties(securityLabels, securityProperties, accessGroups, methodName),
                                           true,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Remove the security tags classification from a referenceable.
     *
     * @param userId calling user
     * @param beanGUID unique identifier of entity to update
     * @param beanGUIDParameterName name of parameter providing beanGUID
     * @param beanGUIDTypeName type of bean
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeSecurityTags(String  userId,
                                    String  beanGUID,
                                    String  beanGUIDParameterName,
                                    String  beanGUIDTypeName,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing,
                                    Date    effectiveTime,
                                    String  methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        this.removeClassificationFromRepository(userId,
                                                null,
                                                null,
                                                beanGUID,
                                                beanGUIDParameterName,
                                                beanGUIDTypeName,
                                                OpenMetadataType.SECURITY_TAGS_CLASSIFICATION_TYPE_GUID,
                                                OpenMetadataType.SECURITY_TAGS_CLASSIFICATION_TYPE_NAME,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
    }


    /**
     * Returns the list of elements that are classified with the SecurityTags classification.
     *
     * @param userId          userId of user making request
     * @param startFrom int      starting position for fist returned element.
     * @param pageSize  int      maximum number of elements to return on the call.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName String calling method
     *
     * @return a list of elements or
     * @throws InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public List<B> getSecurityTagsClassifiedElements(String              userId,
                                                     int                 startFrom,
                                                     int                 pageSize,
                                                     boolean             forLineage,
                                                     boolean             forDuplicateProcessing,
                                                     Date                effectiveTime,
                                                     String              methodName) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<EntityDetail> entities = repositoryHandler.getEntitiesForClassificationType(userId,
                                                                                         OpenMetadataType.REFERENCEABLE.typeName,
                                                                                         OpenMetadataType.SECURITY_TAGS_CLASSIFICATION_TYPE_NAME,
                                                                                         null,
                                                                                         null,
                                                                                         null,
                                                                                         null,
                                                                                         SequencingOrder.CREATION_DATE_RECENT,
                                                                                         null,
                                                                                         forLineage,
                                                                                         forDuplicateProcessing,
                                                                                         startFrom,
                                                                                         queryPageSize,
                                                                                         effectiveTime,
                                                                                         methodName);

        return super.getValidatedBeans(userId,
                                       effectiveTime,
                                       forLineage,
                                       forDuplicateProcessing,
                                       supportedZones,
                                       methodName,
                                       entities);
    }


    /**
     * Add or replace the governance action classification on a referenceable.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this project
     * @param externalSourceName unique name of the software capability that owns this project
     * @param beanGUID unique identifier of bean
     * @param beanGUIDParameterName name of parameter supplying the beanGUID
     * @param beanGUIDTypeName type of bean
     * @param classificationTypeGUID type GUID of governance action classification
     * @param classificationTypeName type name of governance action classification
     * @param statusIdentifier identifier for the status of the classification
     * @param confidence how confident? is this classification right? 0=unknown; 0-100% confident
     * @param steward who is responsible for this classification
     * @param stewardTypeName what type of element is used to identify the steward
     * @param stewardPropertyName what property name is used to identify the steward? guid, qualifiedName etc
     * @param source what is the source of the classification
     * @param notes any notes from the steward
     * @param levelIdentifier what level should the classification be set up as?
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveFrom  the time that the relationship element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the relationship must be effective to (null for any time, new Date() for now)
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addGovernanceActionClassification(String                userId,
                                                   String                externalSourceGUID,
                                                   String                externalSourceName,
                                                   String                beanGUID,
                                                   String                beanGUIDParameterName,
                                                   String                beanGUIDTypeName,
                                                   String                classificationTypeGUID,
                                                   String                classificationTypeName,
                                                   int                   statusIdentifier,
                                                   int                   confidence,
                                                   String                steward,
                                                   String                stewardTypeName,
                                                   String                stewardPropertyName,
                                                   String                source,
                                                   String                notes,
                                                   int                   levelIdentifier,
                                                   boolean               forLineage,
                                                   boolean               forDuplicateProcessing,
                                                   Date                  effectiveFrom,
                                                   Date                  effectiveTo,
                                                   Date                  effectiveTime,
                                                   String                methodName) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        InstanceProperties properties = repositoryHelper.addIntPropertyToInstance(serviceName, null, OpenMetadataProperty.STATUS_IDENTIFIER.name, statusIdentifier, methodName);
        properties = repositoryHelper.addIntPropertyToInstance(serviceName, properties, OpenMetadataProperty.CONFIDENCE.name, confidence, methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties, OpenMetadataProperty.STEWARD.name, steward, methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties, OpenMetadataProperty.STEWARD_TYPE_NAME.name, stewardTypeName, methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties, OpenMetadataProperty.STEWARD_PROPERTY_NAME.name, stewardPropertyName, methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties, OpenMetadataProperty.SOURCE.name, source, methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties, OpenMetadataProperty.NOTES.name, notes, methodName);
        properties = repositoryHelper.addIntPropertyToInstance(serviceName, properties, OpenMetadataType.LEVEL_IDENTIFIER_PROPERTY_NAME, levelIdentifier, methodName);

        this.setClassificationInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           beanGUID,
                                           beanGUIDParameterName,
                                           beanGUIDTypeName,
                                           classificationTypeGUID,
                                           classificationTypeName,
                                           this.setUpEffectiveDates(properties, effectiveFrom, effectiveTo),
                                           true,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Remove the governance expectations classification from a referenceable.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this project
     * @param externalSourceName unique name of the software capability that owns this project
     * @param beanGUID unique identifier of entity to update
     * @param beanGUIDParameterName name of parameter providing beanGUID
     * @param beanGUIDTypeName type of bean
     * @param classificationTypeGUID type GUID of governance action classification
     * @param classificationTypeName type name of governance action classification
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeGovernanceActionClassification(String  userId,
                                                      String  externalSourceGUID,
                                                      String  externalSourceName,
                                                      String  beanGUID,
                                                      String  beanGUIDParameterName,
                                                      String  beanGUIDTypeName,
                                                      String  classificationTypeGUID,
                                                      String  classificationTypeName,
                                                      boolean forLineage,
                                                      boolean forDuplicateProcessing,
                                                      Date    effectiveTime,
                                                      String  methodName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        this.removeClassificationFromRepository(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                beanGUID,
                                                beanGUIDParameterName,
                                                beanGUIDTypeName,
                                                classificationTypeGUID,
                                                classificationTypeName,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
    }


    /**
     * Returns the list of elements that are classified with a specific owner.
     *
     * @param userId       String   userId of user making request.
     * @param classificationName       unique name for the governance action classification
     * @param returnSpecificLevel flag indicating whether to filter by levelIdentifier
     * @param levelIdentifier filter value
     * @param startFrom int      starting position for fist returned element.
     * @param pageSize  int      maximum number of elements to return on the call.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName String calling method
     *
     * @return a list of elements or
     * @throws InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public List<B> getGovernanceActionClassifiedElements(String       userId,
                                                         String       classificationName,
                                                         boolean      returnSpecificLevel,
                                                         int          levelIdentifier,
                                                         int          startFrom,
                                                         int          pageSize,
                                                         boolean      forLineage,
                                                         boolean      forDuplicateProcessing,
                                                         Date         effectiveTime,
                                                         String       methodName) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        InstanceProperties classificationMatchProperties = null;

        if (returnSpecificLevel)
        {
            classificationMatchProperties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                                                      null,
                                                                                      OpenMetadataType.LEVEL_IDENTIFIER_PROPERTY_NAME,
                                                                                      levelIdentifier,
                                                                                      methodName);
        }

        List<EntityDetail> entities = repositoryHandler.getEntitiesForClassificationType(userId,
                                                                                         OpenMetadataType.REFERENCEABLE.typeGUID,
                                                                                         classificationName,
                                                                                         classificationMatchProperties,
                                                                                         MatchCriteria.ANY,
                                                                                         null,
                                                                                         null,
                                                                                         SequencingOrder.CREATION_DATE_RECENT,
                                                                                         null,
                                                                                         forLineage,
                                                                                         forDuplicateProcessing,
                                                                                         startFrom,
                                                                                         queryPageSize,
                                                                                         effectiveTime,
                                                                                         methodName);

        return super.getValidatedBeans(userId,
                                       effectiveTime,
                                       forLineage,
                                       forDuplicateProcessing,
                                       supportedZones,
                                       methodName,
                                       entities);
    }


    /**
     * Add or replace the governance action classification on a referenceable.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this project
     * @param externalSourceName unique name of the software capability that owns this project
     * @param beanGUID unique identifier of bean
     * @param beanGUIDParameterName name of parameter supplying the beanGUID
     * @param beanGUIDTypeName type of bean
     * @param statusIdentifier identifier for the status of the classification
     * @param confidence how confident? is this classification right? 0=unknown; 0-100% confident
     * @param steward who is responsible for this classification
     * @param stewardTypeName what type of element is used to identify the steward
     * @param stewardPropertyName what property name is used to identify the steward? guid, qualifiedName etc
     * @param source what is the source of the classification
     * @param notes any notes from the steward
     * @param basisIdentifier what level should the classification be set up as?
     * @param associatedGUID unique identifier of policy/rule controlling this retention decision
     * @param archiveAfter when should the element be archived?
     * @param deleteAfter when should the element be deleted?
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveFrom  the time that the relationship element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the relationship must be effective to (null for any time, new Date() for now)
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addRetentionClassification(String                userId,
                                            String                externalSourceGUID,
                                            String                externalSourceName,
                                            String                beanGUID,
                                            String                beanGUIDParameterName,
                                            String                beanGUIDTypeName,
                                            int                   statusIdentifier,
                                            int                   confidence,
                                            String                steward,
                                            String                stewardTypeName,
                                            String                stewardPropertyName,
                                            String                source,
                                            String                notes,
                                            int                   basisIdentifier,
                                            String                associatedGUID,
                                            Date                  archiveAfter,
                                            Date                  deleteAfter,
                                            boolean               forLineage,
                                            boolean               forDuplicateProcessing,
                                            Date                  effectiveFrom,
                                            Date                  effectiveTo,
                                            Date                  effectiveTime,
                                            String                methodName) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        InstanceProperties properties = repositoryHelper.addIntPropertyToInstance(serviceName, null, OpenMetadataProperty.STATUS_IDENTIFIER.name, statusIdentifier, methodName);
        properties = repositoryHelper.addIntPropertyToInstance(serviceName, properties, OpenMetadataProperty.CONFIDENCE.name, confidence, methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties, OpenMetadataProperty.STEWARD.name, steward, methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties, OpenMetadataProperty.STEWARD_TYPE_NAME.name, stewardTypeName, methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties, OpenMetadataProperty.STEWARD_PROPERTY_NAME.name, stewardPropertyName, methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties, OpenMetadataProperty.SOURCE.name, source, methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties, OpenMetadataProperty.NOTES.name, notes, methodName);
        properties = repositoryHelper.addIntPropertyToInstance(serviceName, properties, OpenMetadataProperty.RETENTION_BASIS_IDENTIFIER.name, basisIdentifier, methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties, OpenMetadataType.RETENTION_ASSOCIATED_GUID_PROPERTY_NAME, associatedGUID, methodName);
        properties = repositoryHelper.addDatePropertyToInstance(serviceName, properties, OpenMetadataType.RETENTION_ARCHIVE_AFTER_PROPERTY_NAME, archiveAfter, methodName);
        properties = repositoryHelper.addDatePropertyToInstance(serviceName, properties, OpenMetadataType.RETENTION_DELETE_AFTER_PROPERTY_NAME, deleteAfter, methodName);

        this.setClassificationInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           beanGUID,
                                           beanGUIDParameterName,
                                           beanGUIDTypeName,
                                           OpenMetadataType.RETENTION_CLASSIFICATION.typeGUID,
                                           OpenMetadataType.RETENTION_CLASSIFICATION.typeName,
                                           this.setUpEffectiveDates(properties, effectiveFrom, effectiveTo),
                                           true,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Remove the governance expectations classification from a referenceable.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this project
     * @param externalSourceName unique name of the software capability that owns this project
     * @param beanGUID unique identifier of entity to update
     * @param beanGUIDParameterName name of parameter providing beanGUID
     * @param beanGUIDTypeName type of bean
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeRetentionClassification(String  userId,
                                               String  externalSourceGUID,
                                               String  externalSourceName,
                                               String  beanGUID,
                                               String  beanGUIDParameterName,
                                               String  beanGUIDTypeName,
                                               boolean forLineage,
                                               boolean forDuplicateProcessing,
                                               Date    effectiveTime,
                                               String  methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        this.removeClassificationFromRepository(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                beanGUID,
                                                beanGUIDParameterName,
                                                beanGUIDTypeName,
                                                OpenMetadataType.RETENTION_CLASSIFICATION.typeGUID,
                                                OpenMetadataType.RETENTION_CLASSIFICATION.typeName,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
    }


    /**
     * Returns the list of elements that are classified with a specific owner.
     *
     * @param userId       String   userId of user making request.
     * @param returnSpecificBasisLevel flag indicating whether to filter by levelIdentifier
     * @param levelIdentifier filter value
     * @param startFrom int      starting position for fist returned element.
     * @param pageSize  int      maximum number of elements to return on the call.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName String calling method
     *
     * @return a list of elements or
     * @throws InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public List<B> getRetentionClassifiedElements(String       userId,
                                                  boolean      returnSpecificBasisLevel,
                                                  int          levelIdentifier,
                                                  int          startFrom,
                                                  int          pageSize,
                                                  boolean      forLineage,
                                                  boolean      forDuplicateProcessing,
                                                  Date         effectiveTime,
                                                  String       methodName) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        InstanceProperties classificationMatchProperties = null;

        if (returnSpecificBasisLevel)
        {
            classificationMatchProperties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                                                      null,
                                                                                      OpenMetadataProperty.RETENTION_BASIS_IDENTIFIER.name,
                                                                                      levelIdentifier,
                                                                                      methodName);
        }

        List<EntityDetail> entities = repositoryHandler.getEntitiesForClassificationType(userId,
                                                                                         OpenMetadataType.REFERENCEABLE.typeGUID,
                                                                                         OpenMetadataType.RETENTION_CLASSIFICATION.typeName,
                                                                                         classificationMatchProperties,
                                                                                         MatchCriteria.ANY,
                                                                                         null,
                                                                                         null,
                                                                                         SequencingOrder.CREATION_DATE_RECENT,
                                                                                         null,
                                                                                         forLineage,
                                                                                         forDuplicateProcessing,
                                                                                         startFrom,
                                                                                         queryPageSize,
                                                                                         effectiveTime,
                                                                                         methodName);

        return super.getValidatedBeans(userId,
                                       effectiveTime,
                                       forLineage,
                                       forDuplicateProcessing,
                                       supportedZones,
                                       methodName,
                                       entities);
    }


    /**
     * Add or replace the governance expectations classification to a referenceable.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this project
     * @param externalSourceName unique name of the software capability that owns this project
     * @param beanGUID unique identifier of bean
     * @param beanGUIDParameterName name of parameter supplying the beanGUID
     * @param beanGUIDTypeName type of bean
     * @param counts map of name-value counts
     * @param values map of name-value pairs
     * @param flags map of name-value flags
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveFrom  the time that the relationship element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the relationship must be effective to (null for any time, new Date() for now)
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addGovernanceExpectationsClassification(String                userId,
                                                         String                externalSourceGUID,
                                                         String                externalSourceName,
                                                         String                beanGUID,
                                                         String                beanGUIDParameterName,
                                                         String                beanGUIDTypeName,
                                                         Map<String, Integer>  counts,
                                                         Map<String, String>   values,
                                                         Map<String, Boolean>  flags,
                                                         boolean               forLineage,
                                                         boolean               forDuplicateProcessing,
                                                         Date                  effectiveFrom,
                                                         Date                  effectiveTo,
                                                         Date                  effectiveTime,
                                                         String                methodName) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        InstanceProperties properties = repositoryHelper.addIntMapPropertyToInstance(serviceName, null, OpenMetadataType.COUNTS_PROPERTY_NAME, counts, methodName);
        properties = repositoryHelper.addStringMapPropertyToInstance(serviceName, properties, OpenMetadataType.VALUES_PROPERTY_NAME, values, methodName);
        properties = repositoryHelper.addBooleanMapPropertyToInstance(serviceName, properties, OpenMetadataType.FLAGS_PROPERTY_NAME, flags, methodName);

        this.setClassificationInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           beanGUID,
                                           beanGUIDParameterName,
                                           beanGUIDTypeName,
                                           OpenMetadataType.GOVERNANCE_EXPECTATIONS_CLASSIFICATION_TYPE_GUID,
                                           OpenMetadataType.GOVERNANCE_EXPECTATIONS_CLASSIFICATION_TYPE_NAME,
                                           this.setUpEffectiveDates(properties, effectiveFrom, effectiveTo),
                                           true,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Remove the governance expectations classification from a referenceable.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this project
     * @param externalSourceName unique name of the software capability that owns this project
     * @param beanGUID unique identifier of entity to update
     * @param beanGUIDParameterName name of parameter providing beanGUID
     * @param beanGUIDTypeName type of bean
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeGovernanceExpectationsClassification(String  userId,
                                                            String  externalSourceGUID,
                                                            String  externalSourceName,
                                                            String  beanGUID,
                                                            String  beanGUIDParameterName,
                                                            String  beanGUIDTypeName,
                                                            boolean forLineage,
                                                            boolean forDuplicateProcessing,
                                                            Date    effectiveTime,
                                                            String  methodName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        this.removeClassificationFromRepository(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                beanGUID,
                                                beanGUIDParameterName,
                                                beanGUIDTypeName,
                                                OpenMetadataType.GOVERNANCE_EXPECTATIONS_CLASSIFICATION_TYPE_GUID,
                                                OpenMetadataType.GOVERNANCE_EXPECTATIONS_CLASSIFICATION_TYPE_NAME,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
    }


    /**
     * Add or replace the governance measurements classification to a referenceable.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this project
     * @param externalSourceName unique name of the software capability that owns this project
     * @param beanGUID unique identifier of bean
     * @param beanGUIDParameterName name of parameter supplying the beanGUID
     * @param beanGUIDTypeName type of bean
     * @param counts map of name-value counts
     * @param values map of name-value pairs
     * @param flags map of name-value flags
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveFrom  the time that the relationship element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the relationship must be effective to (null for any time, new Date() for now)
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addGovernanceMeasurementsClassification(String                userId,
                                                         String                externalSourceGUID,
                                                         String                externalSourceName,
                                                         String                beanGUID,
                                                         String                beanGUIDParameterName,
                                                         String                beanGUIDTypeName,
                                                         Map<String, Integer>  counts,
                                                         Map<String, String>   values,
                                                         Map<String, Boolean>  flags,
                                                         boolean               forLineage,
                                                         boolean               forDuplicateProcessing,
                                                         Date                  effectiveFrom,
                                                         Date                  effectiveTo,
                                                         Date                  effectiveTime,
                                                         String                methodName) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        InstanceProperties properties = repositoryHelper.addIntMapPropertyToInstance(serviceName, null, OpenMetadataType.MEASUREMENT_COUNTS_PROPERTY_NAME, counts, methodName);
        properties = repositoryHelper.addStringMapPropertyToInstance(serviceName, properties, OpenMetadataType.MEASUREMENT_VALUES_PROPERTY_NAME, values, methodName);
        properties = repositoryHelper.addBooleanMapPropertyToInstance(serviceName, properties, OpenMetadataType.MEASUREMENT_FLAGS_PROPERTY_NAME, flags, methodName);

        this.setClassificationInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           beanGUID,
                                           beanGUIDParameterName,
                                           beanGUIDTypeName,
                                           OpenMetadataType.GOVERNANCE_MEASUREMENTS_CLASSIFICATION_TYPE_GUID,
                                           OpenMetadataType.GOVERNANCE_MEASUREMENTS_CLASSIFICATION_TYPE_NAME,
                                           this.setUpEffectiveDates(properties, effectiveFrom, effectiveTo),
                                           true,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }



    /**
     * Remove the governance measurements classification from a referenceable.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this project
     * @param externalSourceName unique name of the software capability that owns this project
     * @param beanGUID unique identifier of entity to update
     * @param beanGUIDParameterName name of parameter providing beanGUID
     * @param beanGUIDTypeName type of bean
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeGovernanceMeasurementsClassification(String  userId,
                                                            String  externalSourceGUID,
                                                            String  externalSourceName,
                                                            String  beanGUID,
                                                            String  beanGUIDParameterName,
                                                            String  beanGUIDTypeName,
                                                            boolean forLineage,
                                                            boolean forDuplicateProcessing,
                                                            Date    effectiveTime,
                                                            String  methodName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        this.removeClassificationFromRepository(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                beanGUID,
                                                beanGUIDParameterName,
                                                beanGUIDTypeName,
                                                OpenMetadataType.GOVERNANCE_MEASUREMENTS_CLASSIFICATION_TYPE_GUID,
                                                OpenMetadataType.GOVERNANCE_MEASUREMENTS_CLASSIFICATION_TYPE_NAME,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
    }


    /**
     * Add or replace the subject area classification to a referenceable.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this project
     * @param externalSourceName unique name of the software capability that owns this project
     * @param beanGUID unique identifier of bean
     * @param beanGUIDParameterName name of parameter supplying the beanGUID
     * @param beanGUIDTypeName type of bean
     * @param subjectAreaName name of the subject area
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveFrom  the time that the relationship element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the relationship must be effective to (null for any time, new Date() for now)
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addSubjectAreaClassification(String                userId,
                                              String                externalSourceGUID,
                                              String                externalSourceName,
                                              String                beanGUID,
                                              String                beanGUIDParameterName,
                                              String                beanGUIDTypeName,
                                              String                subjectAreaName,
                                              boolean               forLineage,
                                              boolean               forDuplicateProcessing,
                                              Date                  effectiveFrom,
                                              Date                  effectiveTo,
                                              Date                  effectiveTime,
                                              String                methodName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName, null, OpenMetadataProperty.NAME.name, subjectAreaName, methodName);

        this.setClassificationInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           beanGUID,
                                           beanGUIDParameterName,
                                           beanGUIDTypeName,
                                           OpenMetadataType.SUBJECT_AREA_CLASSIFICATION_TYPE_GUID,
                                           OpenMetadataType.SUBJECT_AREA_CLASSIFICATION_TYPE_NAME,
                                           this.setUpEffectiveDates(properties, effectiveFrom, effectiveTo),
                                           true,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Remove the subject area classification from a referenceable.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this project
     * @param externalSourceName unique name of the software capability that owns this project
     * @param beanGUID unique identifier of entity to update
     * @param beanGUIDParameterName name of parameter providing beanGUID
     * @param beanGUIDTypeName type of bean
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeSubjectAreaClassification(String  userId,
                                                 String  externalSourceGUID,
                                                 String  externalSourceName,
                                                 String  beanGUID,
                                                 String  beanGUIDParameterName,
                                                 String  beanGUIDTypeName,
                                                 boolean forLineage,
                                                 boolean forDuplicateProcessing,
                                                 Date    effectiveTime,
                                                 String  methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        this.removeClassificationFromRepository(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                beanGUID,
                                                beanGUIDParameterName,
                                                beanGUIDTypeName,
                                                OpenMetadataType.SUBJECT_AREA_CLASSIFICATION_TYPE_GUID,
                                                OpenMetadataType.SUBJECT_AREA_CLASSIFICATION_TYPE_NAME,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
    }


    /**
     * Returns the list of elements that are classified with a subject area name.
     *
     * @param userId       String   userId of user making request.
     * @param subjectAreaName    String   unique id for the subject area.
     * @param startFrom int      starting position for fist returned element.
     * @param pageSize  int      maximum number of elements to return on the call.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName String calling method
     *
     * @return a list of elements or
     * @throws InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public List<B> getSubjectAreaMembers(String  userId,
                                         String  subjectAreaName,
                                         int     startFrom,
                                         int     pageSize,
                                         boolean forLineage,
                                         boolean forDuplicateProcessing,
                                         Date    effectiveTime,
                                         String  methodName) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        return this.getSubjectAreaMembers(userId,
                                          subjectAreaName,
                                          supportedZones,
                                          startFrom,
                                          pageSize,
                                          forLineage,
                                          forDuplicateProcessing,
                                          effectiveTime,
                                          methodName);
    }


    /**
     * Returns the list of elements that are classified with a subject area name.
     *
     * @param userId       String   userId of user making request.
     * @param subjectAreaName    String   unique id for the subject area.
     * @param serviceSupportedZones supported zones for calling service
     * @param startFrom int      starting position for fist returned element.
     * @param pageSize  int      maximum number of elements to return on the call.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName String calling method
     *
     * @return a list of elements or
     * @throws InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public List<B> getSubjectAreaMembers(String       userId,
                                         String       subjectAreaName,
                                         List<String> serviceSupportedZones,
                                         int          startFrom,
                                         int          pageSize,
                                         boolean      forLineage,
                                         boolean      forDuplicateProcessing,
                                         Date         effectiveTime,
                                         String       methodName) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String nameParameterName = "subjectAreaName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(subjectAreaName, nameParameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        InstanceProperties classificationMatchProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                                        null,
                                                                                                        OpenMetadataProperty.NAME.name,
                                                                                                        subjectAreaName,
                                                                                                        methodName);

        List<EntityDetail> entities = repositoryHandler.getEntitiesForClassificationType(userId,
                                                                                         OpenMetadataType.REFERENCEABLE.typeGUID,
                                                                                         OpenMetadataType.SUBJECT_AREA_CLASSIFICATION_TYPE_NAME,
                                                                                         classificationMatchProperties,
                                                                                         MatchCriteria.ANY,
                                                                                         null,
                                                                                         null,
                                                                                         SequencingOrder.CREATION_DATE_RECENT,
                                                                                         null,
                                                                                         forLineage,
                                                                                         forDuplicateProcessing,
                                                                                         startFrom,
                                                                                         queryPageSize,
                                                                                         effectiveTime,
                                                                                         methodName);

        return super.getValidatedBeans(userId,
                                       effectiveTime,
                                       forLineage,
                                       forDuplicateProcessing,
                                       serviceSupportedZones,
                                       methodName,
                                       entities);
    }


    /**
     * Classify a referenceable as "BusinessSignificant" (this may affect the way that lineage is displayed).
     *
     * @param userId calling user
     * @param beanGUID unique identifier of entity to update
     * @param beanGUIDParameterName name of parameter providing beanGUID
     * @param beanGUIDTypeName type of bean
     * @param description description of why this is significant
     * @param scope scope of its business significance
     * @param businessCapabilityGUID unique identifier of the business capability that rates this as significant
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setBusinessSignificant(String  userId,
                                       String  beanGUID,
                                       String  beanGUIDParameterName,
                                       String  beanGUIDTypeName,
                                       String  description,
                                       String  scope,
                                       String  businessCapabilityGUID,
                                       boolean forLineage,
                                       boolean forDuplicateProcessing,
                                       Date    effectiveTime,
                                       String  methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        ReferenceableBuilder builder = new ReferenceableBuilder(OpenMetadataType.REFERENCEABLE.typeGUID,
                                                                OpenMetadataType.REFERENCEABLE.typeName,
                                                                repositoryHelper,
                                                                serviceName,
                                                                serverName);

        this.setClassificationInRepository(userId,
                                           null,
                                           null,
                                           beanGUID,
                                           beanGUIDParameterName,
                                           beanGUIDTypeName,
                                           OpenMetadataType.BUSINESS_SIGNIFICANCE_CLASSIFICATION_TYPE_GUID,
                                           OpenMetadataType.BUSINESS_SIGNIFICANCE_CLASSIFICATION_TYPE_NAME,
                                           builder.getBusinessSignificanceProperties(description, scope, businessCapabilityGUID, methodName),
                                           true,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Remove the "BusinessSignificant" designation from the element.
     *
     * @param userId calling user
     * @param beanGUID unique identifier of entity to update
     * @param beanGUIDParameterName name of parameter providing beanGUID
     * @param beanGUIDTypeName type of bean
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
    *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearBusinessSignificant(String  userId,
                                         String  beanGUID,
                                         String  beanGUIDParameterName,
                                         String  beanGUIDTypeName,
                                         boolean forLineage,
                                         boolean forDuplicateProcessing,
                                         Date    effectiveTime,
                                         String  methodName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        this.removeClassificationFromRepository(userId,
                                                null,
                                                null,
                                                beanGUID,
                                                beanGUIDParameterName,
                                                beanGUIDTypeName,
                                                OpenMetadataType.BUSINESS_SIGNIFICANCE_CLASSIFICATION_TYPE_GUID,
                                                OpenMetadataType.BUSINESS_SIGNIFICANCE_CLASSIFICATION_TYPE_NAME,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
    }


    /**
     * Classify an entity as suitable to be used as a template for cataloguing assets of a similar types.
     *
     * @param userId calling user
     * @param beanGUID unique identifier of bean
     * @param beanGUIDParameterName name of parameter supplying the beanGUID
     * @param beanGUIDTypeName type of bean
     * @param name name of the template
     * @param versionIdentifier version identifier for the template eg 1.0
     * @param description description of when, where and how to use the template
     * @param additionalProperties any additional properties
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException asset or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void addTemplateClassification(String              userId,
                                          String              beanGUID,
                                          String              beanGUIDParameterName,
                                          String              beanGUIDTypeName,
                                          String              name,
                                          String              versionIdentifier,
                                          String              description,
                                          Map<String, String> additionalProperties,
                                          boolean             forLineage,
                                          boolean             forDuplicateProcessing,
                                          Date                effectiveTime,
                                          String              methodName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        ReferenceableBuilder builder = new ReferenceableBuilder(OpenMetadataType.REFERENCEABLE.typeGUID,
                                                                OpenMetadataType.REFERENCEABLE.typeName,
                                                                repositoryHelper,
                                                                serviceName,
                                                                serverName);

        this.setClassificationInRepository(userId,
                                           null,
                                           null,
                                           beanGUID,
                                           beanGUIDParameterName,
                                           beanGUIDTypeName,
                                           OpenMetadataType.TEMPLATE_CLASSIFICATION.typeGUID,
                                           OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName,
                                           builder.getTemplateProperties(name, versionIdentifier, description, additionalProperties, methodName),
                                           true,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Remove the classification that indicates that this entity can be used as a template.
     *
     * @param userId calling user
     * @param beanGUID unique identifier of bean
     * @param beanGUIDParameterName name of parameter supplying the beanGUID
     * @param beanGUIDTypeName type of bean
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException asset or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void removeTemplateClassification(String  userId,
                                             String  beanGUID,
                                             String  beanGUIDParameterName,
                                             String  beanGUIDTypeName,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing,
                                             Date    effectiveTime,
                                             String  methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        this.removeClassificationFromRepository(userId,
                                                null,
                                                null,
                                                beanGUID,
                                                beanGUIDParameterName,
                                                beanGUIDTypeName,
                                                OpenMetadataType.TEMPLATE_CLASSIFICATION.typeGUID,
                                                OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
    }


    /**
     * Classify the referenceable to indicate that it describes a data field and supply
     * properties that describe the characteristics of the data values found within.
     *
     * @param userId calling user
     * @param beanGUID unique identifier of bean
     * @param beanGUIDParameterName name of parameter supplying the beanGUID
     * @param beanGUIDTypeName type of bean
     * @param defaultValue initial value of a data field value
     * @param sampleValues list of sample values
     * @param dataPattern regular expression describing the values in the data filed
     * @param namePattern regular expression describing the name of the data field
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException asset or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void addDataFieldValuesClassification(String              userId,
                                                 String              beanGUID,
                                                 String              beanGUIDParameterName,
                                                 String              beanGUIDTypeName,
                                                 String              defaultValue,
                                                 List<String>        sampleValues,
                                                 List<String>        dataPattern,
                                                 List<String>        namePattern,
                                                 boolean             forLineage,
                                                 boolean             forDuplicateProcessing,
                                                 Date                effectiveTime,
                                                 String              methodName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        ReferenceableBuilder builder = new ReferenceableBuilder(OpenMetadataType.REFERENCEABLE.typeGUID,
                                                                OpenMetadataType.REFERENCEABLE.typeName,
                                                                repositoryHelper,
                                                                serviceName,
                                                                serverName);

        this.setClassificationInRepository(userId,
                                           null,
                                           null,
                                           beanGUID,
                                           beanGUIDParameterName,
                                           beanGUIDTypeName,
                                           OpenMetadataType.DATA_FIELD_VALUES_CLASSIFICATION_GUID,
                                           OpenMetadataType.DATA_FIELD_VALUES_CLASSIFICATION_NAME,
                                           builder.getDataFieldValuesProperties(defaultValue, sampleValues, dataPattern, namePattern, methodName),
                                           true,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Remove the classification that indicates that this referenceable describeds a data field.
     *
     * @param userId calling user
     * @param beanGUID unique identifier of bean
     * @param beanGUIDParameterName name of parameter supplying the beanGUID
     * @param beanGUIDTypeName type of bean
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException asset or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void removeDataFieldValuesClassification(String  userId,
                                                    String  beanGUID,
                                                    String  beanGUIDParameterName,
                                                    String  beanGUIDTypeName,
                                                    boolean forLineage,
                                                    boolean forDuplicateProcessing,
                                                    Date    effectiveTime,
                                                    String  methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        this.removeClassificationFromRepository(userId,
                                                null,
                                                null,
                                                beanGUID,
                                                beanGUIDParameterName,
                                                beanGUIDTypeName,
                                                OpenMetadataType.DATA_FIELD_VALUES_CLASSIFICATION_GUID,
                                                OpenMetadataType.DATA_FIELD_VALUES_CLASSIFICATION_NAME,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
    }

    /**
     * Returns the list of elements that are classified with a specific set of DataFieldValues properties.
     *
     * @param userId          userId of user making request
     * @param defaultValue initial value of a data field value
     * @param sampleValues list of sample values
     * @param dataPattern regular expression describing the values in the data filed
     * @param namePattern regular expression describing the name of the data field
     * @param serviceSupportedZones supported zones for calling service
     * @param startFrom int      starting position for fist returned element.
     * @param pageSize  int      maximum number of elements to return on the call.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName String calling method
     *
     * @return a list of elements or
     * @throws InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public List<B> getDataFieldValuesClassifiedElement(String              userId,
                                                       String              defaultValue,
                                                       List<String>        sampleValues,
                                                       List<String>        dataPattern,
                                                       List<String>        namePattern,
                                                       List<String>        serviceSupportedZones,
                                                       int                 startFrom,
                                                       int                 pageSize,
                                                       boolean             forLineage,
                                                       boolean             forDuplicateProcessing,
                                                       Date                effectiveTime,
                                                       String              methodName) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<EntityDetail> entities = repositoryHandler.getEntitiesForClassificationType(userId,
                                                                                         OpenMetadataType.REFERENCEABLE.typeName,
                                                                                         OpenMetadataType.DATA_VALUE_CLASSIFICATION_TYPE_NAME,
                                                                                         null,
                                                                                         null,
                                                                                         null,
                                                                                         null,
                                                                                         SequencingOrder.CREATION_DATE_RECENT,
                                                                                         null,
                                                                                         forLineage,
                                                                                         forDuplicateProcessing,
                                                                                         startFrom,
                                                                                         queryPageSize,
                                                                                         effectiveTime,
                                                                                         methodName);
        List<EntityDetail> validatedEntities = super.validateEntitiesAndAnchorsForRead(userId,
                                                                                       entities,
                                                                                       forLineage,
                                                                                       forDuplicateProcessing,
                                                                                       serviceSupportedZones,
                                                                                       effectiveTime,
                                                                                       methodName);
        if (validatedEntities != null)
        {
            List<B>  beans = new ArrayList<>();

            for (EntityDetail entity : validatedEntities)
            {
                if (entity != null)
                {
                    try
                    {
                        Classification classification = repositoryHelper.getClassificationFromEntity(serviceName,
                                                                                                     entity,
                                                                                                     OpenMetadataType.DATA_VALUE_CLASSIFICATION_TYPE_NAME,
                                                                                                     methodName);
                        if (classification != null)
                        {
                            String retrievedDefaultValue = repositoryHelper.getStringProperty(serviceName,
                                                                                              OpenMetadataType.DEFAULT_VALUE_PROPERTY_NAME,
                                                                                              classification.getProperties(),
                                                                                              methodName);

                            if ((defaultValue == null) || (defaultValue.equals(retrievedDefaultValue)))
                            {
                                boolean match = true;

                                if (sampleValues != null)
                                {
                                    List<String> retrievedSampleValues = repositoryHelper.getStringArrayProperty(serviceName,
                                                                                                                 OpenMetadataType.SAMPLE_VALUES_PROPERTY_NAME,
                                                                                                                 classification.getProperties(),
                                                                                                                 methodName);

                                    if ((retrievedSampleValues == null) ||
                                                (sampleValues.isEmpty() && (! retrievedSampleValues.isEmpty())) ||
                                                ((! sampleValues.isEmpty()) && retrievedSampleValues.isEmpty()) ||
                                                (! new HashSet<>(retrievedSampleValues).containsAll(sampleValues)) ||
                                                (! new HashSet<>(sampleValues).containsAll(retrievedSampleValues)))
                                    {
                                        match = false;
                                    }
                                }

                                if (dataPattern != null)
                                {
                                    List<String> retrievedDataPatterns = repositoryHelper.getStringArrayProperty(serviceName,
                                                                                                                 OpenMetadataType.DATA_PATTERN_PROPERTY_NAME,
                                                                                                                 classification.getProperties(),
                                                                                                                 methodName);

                                    if ((retrievedDataPatterns == null) ||
                                                (dataPattern.isEmpty() && (! retrievedDataPatterns.isEmpty())) ||
                                                ((! dataPattern.isEmpty()) && retrievedDataPatterns.isEmpty()) ||
                                                (! new HashSet<>(retrievedDataPatterns).containsAll(dataPattern)) ||
                                                (! new HashSet<>(dataPattern).containsAll(retrievedDataPatterns)))
                                    {
                                        match = false;
                                    }
                                }

                                if (namePattern != null)
                                {
                                    List<String> retrievedNamePatterns = repositoryHelper.getStringArrayProperty(serviceName,
                                                                                                                 OpenMetadataType.NAME_PATTERN_PROPERTY_NAME,
                                                                                                                 classification.getProperties(),
                                                                                                                 methodName);

                                    if ((retrievedNamePatterns == null) ||
                                                (namePattern.isEmpty() && (! retrievedNamePatterns.isEmpty())) ||
                                                ((! namePattern.isEmpty()) && retrievedNamePatterns.isEmpty()) ||
                                                (! new HashSet<>(retrievedNamePatterns).containsAll(namePattern)) ||
                                                (! new HashSet<>(namePattern).containsAll(retrievedNamePatterns)))
                                    {
                                        match = false;
                                    }
                                }

                                if (match)
                                {
                                    beans.add(converter.getNewBean(beanClass, entity, methodName));
                                }
                            }
                        }
                    }
                    catch (Exception notVisible)
                    {
                        // entity not visible
                    }
                }
            }

            if (! beans.isEmpty())
            {
                return beans;
            }
        }

        return null;
    }


    /**
     * Page through the list of consumers for a valid value.
     *
     * @param userId                  calling user
     * @param validValueGUID          unique identifier of valid value to query
     * @param validValueGUIDParameter parameter name for validValueGUID
     * @param startFrom               paging starting point
     * @param pageSize                maximum number of return values.
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName              calling method
     * @return list of valid value consumer beans
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public List<B> getValidValueConsumers(String       userId,
                                          String       validValueGUID,
                                          String       validValueGUIDParameter,
                                          int          startFrom,
                                          int          pageSize,
                                          boolean      forLineage,
                                          boolean      forDuplicateProcessing,
                                          Date         effectiveTime,
                                          String       methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        return this.getAttachedElements(userId,
                                        validValueGUID,
                                        validValueGUIDParameter,
                                        OpenMetadataType.REFERENCEABLE.typeName,
                                        OpenMetadataType.VALID_VALUES_ASSIGNMENT_RELATIONSHIP.typeGUID,
                                        OpenMetadataType.VALID_VALUES_ASSIGNMENT_RELATIONSHIP.typeName,
                                        OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
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
     * Page through the list of referenceables that have this valid value as a reference value.
     *
     * @param userId         calling user
     * @param validValueGUID unique identifier of valid value to query
     * @param validValueGUIDParameter name of parameter that provides the validValueGUID value
     * @param startFrom      paging starting point
     * @param pageSize       maximum number of return values
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime        the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName     calling method
     * @return list of referenceable beans
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to make this request
     * @throws PropertyServerException    the repository is not available or not working properly
     */
    public List<B> getReferenceValueAssignees(String       userId,
                                              String       validValueGUID,
                                              String       validValueGUIDParameter,
                                              int          startFrom,
                                              int          pageSize,
                                              boolean      forLineage,
                                              boolean      forDuplicateProcessing,
                                              Date         effectiveTime,
                                              String       methodName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        return this.getAttachedElements(userId,
                                        validValueGUID,
                                        validValueGUIDParameter,
                                        OpenMetadataType.REFERENCEABLE.typeName,
                                        OpenMetadataType.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP.typeGUID,
                                        OpenMetadataType.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP.typeName,
                                        OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
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
     * Add a more information to element.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this project
     * @param externalSourceName unique name of the software capability that owns this project
     * @param elementGUID unique identifier of the element
     * @param elementGUIDParameterName parameter supplying the elementGUID
     * @param moreInformationGUID unique identifier of the element that is supplying more information
     * @param moreInformationGUIDParameterName parameter supplying the moreInformationGUID
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
    public void addMoreInformation(String  userId,
                                   String  externalSourceGUID,
                                   String  externalSourceName,
                                   String  elementGUID,
                                   String  elementGUIDParameterName,
                                   String  moreInformationGUID,
                                   String  moreInformationGUIDParameterName,
                                   Date    effectiveFrom,
                                   Date    effectiveTo,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing,
                                   Date    effectiveTime,
                                   String  methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  elementGUID,
                                  elementGUIDParameterName,
                                  OpenMetadataType.REFERENCEABLE.typeName,
                                  moreInformationGUID,
                                  moreInformationGUIDParameterName,
                                  OpenMetadataType.REFERENCEABLE.typeName,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataType.MORE_INFORMATION_RELATIONSHIP.typeGUID,
                                  OpenMetadataType.MORE_INFORMATION_RELATIONSHIP.typeName,
                                  this.setUpEffectiveDates(null, effectiveFrom, effectiveTo),
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Remove more information from an element.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this project
     * @param externalSourceName unique name of the software capability that owns this project
     * @param elementGUID unique identifier of the project
     * @param elementGUIDParameterName parameter supplying the elementGUID
     * @param moreInformationGUID unique identifier of the element that is being added to the project
     * @param moreInformationGUIDParameterName parameter supplying the moreInformationGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeMoreInformation(String  userId,
                                      String  externalSourceGUID,
                                      String  externalSourceName,
                                      String  elementGUID,
                                      String  elementGUIDParameterName,
                                      String  moreInformationGUID,
                                      String  moreInformationGUIDParameterName,
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
                                      elementGUID,
                                      elementGUIDParameterName,
                                      OpenMetadataType.REFERENCEABLE.typeName,
                                      moreInformationGUID,
                                      moreInformationGUIDParameterName,
                                      OpenMetadataType.REFERENCEABLE.typeGUID,
                                      OpenMetadataType.REFERENCEABLE.typeName,
                                      forLineage,
                                      forDuplicateProcessing,
                                      OpenMetadataType.MORE_INFORMATION_RELATIONSHIP.typeGUID,
                                      OpenMetadataType.MORE_INFORMATION_RELATIONSHIP.typeName,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Returns the list of elements that are linked to provide more information.  It uses the supportedZones supplied with the service.
     *
     * @param userId       String   userId of user making request.
     * @param startingGUID    String   unique id for element.
     * @param startingGUIDParameterName name of parameter supplying the GUID
     * @param suppliedStartingTypeName name of the type of object that the search begins with - null means referenceable
     * @param suppliedResultingTypeName name of the type of object that the search returns - null mean referenceable
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
    public List<B> getMoreInformation(String  userId,
                                      String  startingGUID,
                                      String  startingGUIDParameterName,
                                      String  suppliedStartingTypeName,
                                      String  suppliedResultingTypeName,
                                      int     startFrom,
                                      int     pageSize,
                                      boolean forLineage,
                                      boolean forDuplicateProcessing,
                                      Date    effectiveTime,
                                      String  methodName) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        return this.getMoreInformation(userId,
                                       startingGUID,
                                       startingGUIDParameterName,
                                       suppliedStartingTypeName,
                                       suppliedResultingTypeName,
                                       supportedZones,
                                       startFrom,
                                       pageSize,
                                       forLineage,
                                       forDuplicateProcessing,
                                       effectiveTime,
                                       methodName);
    }


    /**
     * Returns the list of elements that are linked to provide more information.
     *
     * @param userId       String   userId of user making request.
     * @param startingGUID    String   unique id for element.
     * @param startingGUIDParameterName name of parameter supplying the GUID
     * @param suppliedStartingTypeName name of the type of object that the search begins with - null means referenceable
     * @param suppliedResultingTypeName name of the type of object that the search returns - null mean referenceable
     * @param serviceSupportedZones supported zones for calling service
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
    public List<B> getMoreInformation(String       userId,
                                      String       startingGUID,
                                      String       startingGUIDParameterName,
                                      String       suppliedStartingTypeName,
                                      String       suppliedResultingTypeName,
                                      List<String> serviceSupportedZones,
                                      int          startFrom,
                                      int          pageSize,
                                      boolean      forLineage,
                                      boolean      forDuplicateProcessing,
                                      Date         effectiveTime,
                                      String       methodName) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        String startingTypeName  = OpenMetadataType.REFERENCEABLE.typeName;
        String resultingTypeName = OpenMetadataType.REFERENCEABLE.typeName;

        if (suppliedStartingTypeName != null)
        {
            startingTypeName = suppliedStartingTypeName;
        }
        if (suppliedResultingTypeName != null)
        {
            resultingTypeName = suppliedResultingTypeName;
        }

        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        startingGUID,
                                        startingGUIDParameterName,
                                        startingTypeName,
                                        OpenMetadataType.MORE_INFORMATION_RELATIONSHIP.typeGUID,
                                        OpenMetadataType.MORE_INFORMATION_RELATIONSHIP.typeName,
                                        resultingTypeName,
                                        (String)null,
                                        null,
                                        2,
                                        null,
                                        null,
                                        SequencingOrder.CREATION_DATE_RECENT,
                                        null,
                                        forLineage,
                                        forDuplicateProcessing,
                                        serviceSupportedZones,
                                        startFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Retrieve the descriptive elements linked via a "MoreInformation" relationship between two referenceables.
     *
     * @param userId       String   userId of user making request.
     * @param startingGUID    String   unique id for element.
     * @param startingGUIDParameterName name of parameter supplying the GUID
     * @param suppliedStartingTypeName name of the type of object that the search begins with - null means referenceable
     * @param suppliedResultingTypeName name of the type of object that the search returns - null mean referenceable
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
    public List<B> getDescriptiveElements(String       userId,
                                          String       startingGUID,
                                          String       startingGUIDParameterName,
                                          String       suppliedStartingTypeName,
                                          String       suppliedResultingTypeName,
                                          int          startFrom,
                                          int          pageSize,
                                          boolean      forLineage,
                                          boolean      forDuplicateProcessing,
                                          Date         effectiveTime,
                                          String       methodName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        String startingTypeName  = OpenMetadataType.REFERENCEABLE.typeName;
        String resultingTypeName = OpenMetadataType.REFERENCEABLE.typeName;

        if (suppliedStartingTypeName != null)
        {
            startingTypeName = suppliedStartingTypeName;
        }
        if (suppliedResultingTypeName != null)
        {
            resultingTypeName = suppliedResultingTypeName;
        }

        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        startingGUID,
                                        startingGUIDParameterName,
                                        startingTypeName,
                                        OpenMetadataType.MORE_INFORMATION_RELATIONSHIP.typeGUID,
                                        OpenMetadataType.MORE_INFORMATION_RELATIONSHIP.typeName,
                                        resultingTypeName,
                                        (String)null,
                                        null,
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
     * Create relationships between the identified glossary terms and an Asset.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source - null for local
     * @param beanGUID unique identifier of the referenceable that is being described
     * @param beanGUIDParameter parameter supply the beanGUID
     * @param glossaryTermGUIDs list of unique identifiers of the glossary terms
     * @param glossaryTermGUIDsParameter parameter supplying the list of GlossaryTermGUIDs
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException the guid properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void  saveSemanticAssignments(String         userId,
                                         String         externalSourceGUID,
                                         String         externalSourceName,
                                         String         beanGUID,
                                         String         beanGUIDParameter,
                                         List<String>   glossaryTermGUIDs,
                                         String         glossaryTermGUIDsParameter,
                                         Date           effectiveFrom,
                                         Date           effectiveTo,
                                         boolean        forLineage,
                                         boolean        forDuplicateProcessing,
                                         Date           effectiveTime,
                                         String         methodName)  throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        if (glossaryTermGUIDs != null)
        {
            for (String glossaryTermGUID : glossaryTermGUIDs)
            {
                if (glossaryTermGUID != null)
                {
                    this.saveSemanticAssignment(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                beanGUID,
                                                beanGUIDParameter,
                                                glossaryTermGUID,
                                                glossaryTermGUIDsParameter,
                                                effectiveFrom,
                                                effectiveTo,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
                }
            }
        }
    }


    /**
     * Create a simple relationship between a glossary term and a referenceable.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source - null for local
     * @param beanGUID unique identifier of the asset that is being described
     * @param beanGUIDParameter parameter supply the beanGUID
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param glossaryTermGUIDParameter parameter supplying the list of GlossaryTermGUID
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException the guid properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void  saveSemanticAssignment(String  userId,
                                        String  externalSourceGUID,
                                        String  externalSourceName,
                                        String  beanGUID,
                                        String  beanGUIDParameter,
                                        String  glossaryTermGUID,
                                        String  glossaryTermGUIDParameter,
                                        Date    effectiveFrom,
                                        Date    effectiveTo,
                                        boolean forLineage,
                                        boolean forDuplicateProcessing,
                                        Date    effectiveTime,
                                        String  methodName)  throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  beanGUID,
                                  beanGUIDParameter,
                                  OpenMetadataType.REFERENCEABLE.typeName,
                                  glossaryTermGUID,
                                  glossaryTermGUIDParameter,
                                  OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataType.SEMANTIC_ASSIGNMENT.typeGUID,
                                  OpenMetadataType.SEMANTIC_ASSIGNMENT.typeName,
                                  this.setUpEffectiveDates(null, effectiveFrom, effectiveTo),
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Create a simple relationship between a glossary term and a referenceable.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source - null for local
     * @param beanGUID unique identifier of the element that is being described
     * @param beanGUIDParameter parameter supply the beanGUID
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param glossaryTermGUIDParameter parameter supplying the list of GlossaryTermGUID
     * @param description description of the assignment
     * @param expression expression used to determine the assignment
     * @param statusOrdinal the status of the assignment
     * @param confidence how confident is the assignment - no confidence = 0 to complete confidence = 100
     * @param createdBy who/what created the assignment
     * @param steward which steward is responsible for assignment
     * @param source where was the source of the assignment
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException the guid properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void  saveSemanticAssignment(String  userId,
                                        String  externalSourceGUID,
                                        String  externalSourceName,
                                        String  beanGUID,
                                        String  beanGUIDParameter,
                                        String  glossaryTermGUID,
                                        String  glossaryTermGUIDParameter,
                                        String  description,
                                        String  expression,
                                        int     statusOrdinal,
                                        int     confidence,
                                        String  createdBy,
                                        String  steward,
                                        String  source,
                                        Date    effectiveFrom,
                                        Date    effectiveTo,
                                        boolean forLineage,
                                        boolean forDuplicateProcessing,
                                        Date    effectiveTime,
                                        String  methodName)  throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     OpenMetadataProperty.DESCRIPTION.name,
                                                                                     description,
                                                                                     methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.EXPRESSION.name,
                                                                  expression,
                                                                  methodName);

        try
        {
            properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                    properties,
                                                                    OpenMetadataProperty.TERM_ASSIGNMENT_STATUS.name,
                                                                    GlossaryTermAssignmentStatus.getOpenTypeGUID(),
                                                                    GlossaryTermAssignmentStatus.getOpenTypeName(),
                                                                    statusOrdinal,
                                                                    methodName);
        }
        catch (TypeErrorException error)
        {
            errorHandler.handleUnsupportedType(error, methodName, OpenMetadataProperty.TERM_ASSIGNMENT_STATUS.name);
        }

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               OpenMetadataProperty.CONFIDENCE.name,
                                                               confidence,
                                                               methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataType.CREATED_BY_PROPERTY_NAME,
                                                                  createdBy,
                                                                  methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.STEWARD.name,
                                                                  steward,
                                                                  methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.SOURCE.name,
                                                                  source,
                                                                  methodName);

        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  beanGUID,
                                  beanGUIDParameter,
                                  OpenMetadataType.REFERENCEABLE.typeName,
                                  glossaryTermGUID,
                                  glossaryTermGUIDParameter,
                                  OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataType.SEMANTIC_ASSIGNMENT.typeGUID,
                                  OpenMetadataType.SEMANTIC_ASSIGNMENT.typeName,
                                  this.setUpEffectiveDates(properties, effectiveFrom, effectiveTo),
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Remove the relationship between a glossary term and a referenceable (typically
     * a field in the schema).
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param beanGUID unique identifier of the element that is being described
     * @param beanGUIDParameter parameter supply the beanGUID
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param glossaryTermGUIDParameter parameter supplying the list of GlossaryTermGUID
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeSemanticAssignment(String  userId,
                                          String  externalSourceGUID,
                                          String  externalSourceName,
                                          String  beanGUID,
                                          String  beanGUIDParameter,
                                          String  glossaryTermGUID,
                                          String  glossaryTermGUIDParameter,
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
                                      beanGUID,
                                      beanGUIDParameter,
                                      OpenMetadataType.REFERENCEABLE.typeName,
                                      glossaryTermGUID,
                                      glossaryTermGUIDParameter,
                                      OpenMetadataType.GLOSSARY_TERM_TYPE_GUID,
                                      OpenMetadataType.GLOSSARY_TERM_TYPE_NAME,
                                      forLineage,
                                      forDuplicateProcessing,
                                      supportedZones,
                                      OpenMetadataType.SEMANTIC_ASSIGNMENT.typeGUID,
                                      OpenMetadataType.SEMANTIC_ASSIGNMENT.typeName,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Returns the list of glossary terms that are linked to provide a semantic definition of a data item.
     *
     * @param userId       String   userId of user making request.
     * @param startingGUID    String   unique id for element.
     * @param startingGUIDParameterName name of parameter supplying the GUID
     * @param suppliedStartingTypeName name of the type of object that the search begins with - null means referenceable
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
    public List<B> getMeanings(String       userId,
                               String       startingGUID,
                               String       startingGUIDParameterName,
                               String       suppliedStartingTypeName,
                               int          startFrom,
                               int          pageSize,
                               boolean      forLineage,
                               boolean      forDuplicateProcessing,
                               Date         effectiveTime,
                               String       methodName) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        String startingTypeName  = OpenMetadataType.REFERENCEABLE.typeName;
        String resultingTypeName = OpenMetadataType.GLOSSARY_TERM_TYPE_NAME;

        if (suppliedStartingTypeName != null)
        {
            startingTypeName = suppliedStartingTypeName;
        }

        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        startingGUID,
                                        startingGUIDParameterName,
                                        startingTypeName,
                                        OpenMetadataType.SEMANTIC_ASSIGNMENT.typeGUID,
                                        OpenMetadataType.SEMANTIC_ASSIGNMENT.typeName,
                                        resultingTypeName,
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
     * Returns the list of elements that are linked to a glossary term that is providing a semantic definition.
     *
     * @param userId       String   userId of user making request.
     * @param startingGUID    String   unique id for element.
     * @param startingGUIDParameterName name of parameter supplying the GUID
     * @param suppliedResultingTypeName name of the type of object that the search returns - null mean referenceable
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
    public List<B> getSemanticAssignments(String       userId,
                                          String       startingGUID,
                                          String       startingGUIDParameterName,
                                          String       suppliedResultingTypeName,
                                          int          startFrom,
                                          int          pageSize,
                                          boolean      forLineage,
                                          boolean      forDuplicateProcessing,
                                          Date         effectiveTime,
                                          String       methodName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        String startingTypeName = OpenMetadataType.GLOSSARY_TERM_TYPE_NAME;
        String resultingTypeName  = OpenMetadataType.REFERENCEABLE.typeName;

        if (suppliedResultingTypeName != null)
        {
            resultingTypeName = suppliedResultingTypeName;
        }

        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        startingGUID,
                                        startingGUIDParameterName,
                                        startingTypeName,
                                        OpenMetadataType.SEMANTIC_ASSIGNMENT.typeGUID,
                                        OpenMetadataType.SEMANTIC_ASSIGNMENT.typeName,
                                        resultingTypeName,
                                        (String)null,
                                        null,
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
     * Create a relationship between a referenceable and a resource it uses.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source - null for local
     * @param beanGUID unique identifier of the starting element
     * @param beanGUIDParameter parameter supplying the beanGUID
     * @param memberGUID unique identifier of the element to link
     * @param memberGUIDParameter parameter supplying the memberGUID
     * @param resourceUse string description (use ResourceUse enum from GAF)
     * @param resourceUseDescription description of how the resource is used
     * @param resourceUseProperties additional properties associated with the resource
     * @param watchResource should changes in the members result in notifications
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException the guid properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void  saveResourceListMember(String              userId,
                                        String              externalSourceGUID,
                                        String              externalSourceName,
                                        String              beanGUID,
                                        String              beanGUIDParameter,
                                        String              memberGUID,
                                        String              memberGUIDParameter,
                                        String              resourceUse,
                                        String              resourceUseDescription,
                                        Map<String, String> resourceUseProperties,
                                        boolean             watchResource,
                                        Date                effectiveFrom,
                                        Date                effectiveTo,
                                        boolean             forLineage,
                                        boolean             forDuplicateProcessing,
                                        Date                effectiveTime,
                                        String              methodName)  throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     OpenMetadataProperty.RESOURCE_USE.name,
                                                                                     resourceUse,
                                                                                     methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.RESOURCE_USE_DESCRIPTION.name,
                                                                  resourceUseDescription,
                                                                  methodName);

        properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                     properties,
                                                                     OpenMetadataProperty.RESOURCE_USE_PROPERTIES.name,
                                                                     resourceUseProperties,
                                                                     methodName);

        properties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                   properties,
                                                                   OpenMetadataProperty.WATCH_RESOURCE.name,
                                                                   watchResource,
                                                                   methodName);

        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  beanGUID,
                                  beanGUIDParameter,
                                  OpenMetadataType.REFERENCEABLE.typeName,
                                  memberGUID,
                                  memberGUIDParameter,
                                  OpenMetadataType.REFERENCEABLE.typeName,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeGUID,
                                  OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName,
                                  this.setUpEffectiveDates(properties, effectiveFrom, effectiveTo),
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Remove the relationship between a referenceable and a resource it uses.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param beanGUID unique identifier of the referenceable
     * @param beanGUIDParameter parameter supplying beanGUID
     * @param memberGUID unique identifier of the glossary term
     * @param memberGUIDParameter parameter supplying memberGUID
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeResourceListMember(String  userId,
                                          String  externalSourceGUID,
                                          String  externalSourceName,
                                          String  beanGUID,
                                          String  beanGUIDParameter,
                                          String  memberGUID,
                                          String  memberGUIDParameter,
                                          boolean forLineage,
                                          boolean forDuplicateProcessing,
                                          Date    effectiveTime,
                                          String  methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        if (beanGUID != null)
        {
            this.unlinkElementFromElement(userId,
                                          false,
                                          externalSourceGUID,
                                          externalSourceName,
                                          beanGUID,
                                          beanGUIDParameter,
                                          OpenMetadataType.REFERENCEABLE.typeName,
                                          memberGUID,
                                          memberGUIDParameter,
                                          OpenMetadataType.REFERENCEABLE.typeGUID,
                                          OpenMetadataType.REFERENCEABLE.typeName,
                                          forLineage,
                                          forDuplicateProcessing,
                                          supportedZones,
                                          OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeGUID,
                                          OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName,
                                          effectiveTime,
                                          methodName);
        }
    }



    /**
     * Returns the list of elements that are linked to provide resources to the starting element (for example, a community provides
     * people to a governance domain).
     *
     * @param userId       String   userId of user making request.
     * @param startingGUID    String   unique id for element.
     * @param startingGUIDParameterName name of parameter supplying the GUID
     * @param suppliedStartingTypeName name of the type of object that the search begins with - null means referenceable
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
    public List<B> getResourceList(String       userId,
                                   String       startingGUID,
                                   String       startingGUIDParameterName,
                                   String       suppliedStartingTypeName,
                                   int          startFrom,
                                   int          pageSize,
                                   boolean      forLineage,
                                   boolean      forDuplicateProcessing,
                                   Date         effectiveTime,
                                   String       methodName) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        String startingTypeName  = OpenMetadataType.REFERENCEABLE.typeName;
        String resultingTypeName = OpenMetadataType.REFERENCEABLE.typeName;

        if (suppliedStartingTypeName != null)
        {
            startingTypeName = suppliedStartingTypeName;
        }

        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        startingGUID,
                                        startingGUIDParameterName,
                                        startingTypeName,
                                        OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeGUID,
                                        OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName,
                                        resultingTypeName,
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
     * Retrieve the list of elements assigned to a resource via the "ResourceList" relationship between two referenceables.
     *
     * @param userId       String   userId of user making request.
     * @param startingGUID    String   unique id for element.
     * @param startingGUIDParameterName name of parameter supplying the GUID
     * @param suppliedStartingTypeName name of the type of object that the search begins with - null means referenceable
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
    public List<B> getSupportedByResource(String       userId,
                                          String       startingGUID,
                                          String       startingGUIDParameterName,
                                          String       suppliedStartingTypeName,
                                          int          startFrom,
                                          int          pageSize,
                                          boolean      forLineage,
                                          boolean      forDuplicateProcessing,
                                          Date         effectiveTime,
                                          String       methodName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        String startingTypeName  = OpenMetadataType.REFERENCEABLE.typeName;
        String resultingTypeName = OpenMetadataType.REFERENCEABLE.typeName;

        if (suppliedStartingTypeName != null)
        {
            startingTypeName = suppliedStartingTypeName;
        }

        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        startingGUID,
                                        startingGUIDParameterName,
                                        startingTypeName,
                                        OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeGUID,
                                        OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName,
                                        resultingTypeName,
                                        (String)null,
                                        null,
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
     * Create a relationship between a referenceable and a resource it uses.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source - null for local
     * @param beanGUID unique identifier of the starting element
     * @param beanGUIDParameter parameter supplying the beanGUID
     * @param memberGUID unique identifier of the element to link
     * @param memberGUIDParameter parameter supplying the memberGUID
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException the guid properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void  saveCatalogTemplate(String              userId,
                                     String              externalSourceGUID,
                                     String              externalSourceName,
                                     String              beanGUID,
                                     String              beanGUIDParameter,
                                     String              memberGUID,
                                     String              memberGUIDParameter,
                                     Date                effectiveFrom,
                                     Date                effectiveTo,
                                     boolean             forLineage,
                                     boolean             forDuplicateProcessing,
                                     Date                effectiveTime,
                                     String              methodName)  throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  beanGUID,
                                  beanGUIDParameter,
                                  OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                  memberGUID,
                                  memberGUIDParameter,
                                  OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataType.CATALOG_TEMPLATE_RELATIONSHIP.typeGUID,
                                  OpenMetadataType.CATALOG_TEMPLATE_RELATIONSHIP.typeName,
                                  this.setUpEffectiveDates(null, effectiveFrom, effectiveTo),
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Remove the relationship between a referenceable and a template it uses.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param beanGUID unique identifier of the referenceable
     * @param beanGUIDParameter parameter supplying beanGUID
     * @param memberGUID unique identifier of the glossary term
     * @param memberGUIDParameter parameter supplying memberGUID
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeCatalogTemplate(String  userId,
                                       String  externalSourceGUID,
                                       String  externalSourceName,
                                       String  beanGUID,
                                       String  beanGUIDParameter,
                                       String  memberGUID,
                                       String  memberGUIDParameter,
                                       boolean forLineage,
                                       boolean forDuplicateProcessing,
                                       Date    effectiveTime,
                                       String  methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        if (beanGUID != null)
        {
            this.unlinkElementFromElement(userId,
                                          false,
                                          externalSourceGUID,
                                          externalSourceName,
                                          beanGUID,
                                          beanGUIDParameter,
                                          OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                          memberGUID,
                                          memberGUIDParameter,
                                          OpenMetadataType.OPEN_METADATA_ROOT.typeGUID,
                                          OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                          forLineage,
                                          forDuplicateProcessing,
                                          supportedZones,
                                          OpenMetadataType.CATALOG_TEMPLATE_RELATIONSHIP.typeGUID,
                                          OpenMetadataType.CATALOG_TEMPLATE_RELATIONSHIP.typeName,
                                          effectiveTime,
                                          methodName);
        }
    }



    /**
     * Returns the list of elements that are linked to provide templates to the starting element.
     *
     * @param userId       String   userId of user making request.
     * @param startingGUID    String   unique id for element.
     * @param startingGUIDParameterName name of parameter supplying the GUID
     * @param suppliedStartingTypeName name of the type of object that the search begins with - null means referenceable
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
    public List<B> getCatalogTemplateList(String       userId,
                                          String       startingGUID,
                                          String       startingGUIDParameterName,
                                          String       suppliedStartingTypeName,
                                          int          startFrom,
                                          int          pageSize,
                                          boolean      forLineage,
                                          boolean      forDuplicateProcessing,
                                          Date         effectiveTime,
                                          String       methodName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        String startingTypeName  = OpenMetadataType.OPEN_METADATA_ROOT.typeName;
        String resultingTypeName = OpenMetadataType.OPEN_METADATA_ROOT.typeName;

        if (suppliedStartingTypeName != null)
        {
            startingTypeName = suppliedStartingTypeName;
        }

        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        startingGUID,
                                        startingGUIDParameterName,
                                        startingTypeName,
                                        OpenMetadataType.CATALOG_TEMPLATE_RELATIONSHIP.typeGUID,
                                        OpenMetadataType.CATALOG_TEMPLATE_RELATIONSHIP.typeName,
                                        resultingTypeName,
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
     * Retrieve the list of templates assigned to a resource via the "CatalogTemplate" relationship between two referenceables.
     *
     * @param userId       String   userId of user making request.
     * @param startingGUID    String   unique id for element.
     * @param startingGUIDParameterName name of parameter supplying the GUID
     * @param suppliedStartingTypeName name of the type of object that the search begins with - null means referenceable
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
    public List<B> getSupportedByTemplate(String       userId,
                                          String       startingGUID,
                                          String       startingGUIDParameterName,
                                          String       suppliedStartingTypeName,
                                          int          startFrom,
                                          int          pageSize,
                                          boolean      forLineage,
                                          boolean      forDuplicateProcessing,
                                          Date         effectiveTime,
                                          String       methodName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        String startingTypeName  = OpenMetadataType.OPEN_METADATA_ROOT.typeName;
        String resultingTypeName = OpenMetadataType.OPEN_METADATA_ROOT.typeName;

        if (suppliedStartingTypeName != null)
        {
            startingTypeName = suppliedStartingTypeName;
        }

        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        startingGUID,
                                        startingGUIDParameterName,
                                        startingTypeName,
                                        OpenMetadataType.CATALOG_TEMPLATE_RELATIONSHIP.typeGUID,
                                        OpenMetadataType.CATALOG_TEMPLATE_RELATIONSHIP.typeName,
                                        resultingTypeName,
                                        (String)null,
                                        null,
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
     * Add a scope to an actor - both referenceables.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this project
     * @param externalSourceName unique name of the software capability that owns this project
     * @param assignedActorGUID unique identifier of the actor
     * @param assignedActorGUIDParameterName parameter supplying the assignedActorGUID
     * @param assignedScopeGUID unique identifier of the scope
     * @param assignedScopeGUIDParameterName parameter supplying the assignedScopeGUID
     * @param assignmentType Type of assignment - such as accountable, responsible, observing ...
     * @param description mode details about the assignment
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
    public void addAssignmentScope(String  userId,
                                   String  externalSourceGUID,
                                   String  externalSourceName,
                                   String  assignedActorGUID,
                                   String  assignedActorGUIDParameterName,
                                   String  assignedScopeGUID,
                                   String  assignedScopeGUIDParameterName,
                                   String  assignmentType,
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
        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     OpenMetadataProperty.ASSIGNMENT_TYPE.name,
                                                                                     assignmentType,
                                                                                     methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.DESCRIPTION.name,
                                                                  description,
                                                                  methodName);

        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  assignedActorGUID,
                                  assignedActorGUIDParameterName,
                                  OpenMetadataType.REFERENCEABLE.typeName,
                                  assignedScopeGUID,
                                  assignedScopeGUIDParameterName,
                                  OpenMetadataType.REFERENCEABLE.typeName,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeGUID,
                                  OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                  this.setUpEffectiveDates(properties, effectiveFrom, effectiveTo),
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Remove a scope from an actor - both referenceables.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this project
     * @param externalSourceName unique name of the software capability that owns this project
     * @param assignedActorGUID unique identifier of the actor
     * @param assignedActorGUIDParameterName parameter supplying the assignedActorGUID
     * @param assignedScopeGUID unique identifier of the scope
     * @param assignedScopeGUIDParameterName parameter supplying the assignedScopeGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeAssignmentScope(String  userId,
                                      String  externalSourceGUID,
                                      String  externalSourceName,
                                      String  assignedActorGUID,
                                      String  assignedActorGUIDParameterName,
                                      String  assignedScopeGUID,
                                      String  assignedScopeGUIDParameterName,
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
                                      assignedActorGUID,
                                      assignedActorGUIDParameterName,
                                      OpenMetadataType.REFERENCEABLE.typeName,
                                      assignedScopeGUID,
                                      assignedScopeGUIDParameterName,
                                      OpenMetadataType.REFERENCEABLE.typeGUID,
                                      OpenMetadataType.REFERENCEABLE.typeName,
                                      forLineage,
                                      forDuplicateProcessing,
                                      OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeGUID,
                                      OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Returns the list of elements that are scoping the work of one or more actors/roles.
     *
     * @param userId       String   userId of user making request.
     * @param startingGUID    String   unique id for element.
     * @param startingGUIDParameterName name of parameter supplying the GUID
     * @param suppliedStartingTypeName name of the type of object that the search begins with - null means referenceable
     * @param suppliedResultingTypeName name of the type of object that the search returns - null mean referenceable
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
    public List<B> getAssignmentScope(String       userId,
                                      String       startingGUID,
                                      String       startingGUIDParameterName,
                                      String       suppliedStartingTypeName,
                                      String       suppliedResultingTypeName,
                                      int          startFrom,
                                      int          pageSize,
                                      boolean      forLineage,
                                      boolean      forDuplicateProcessing,
                                      Date         effectiveTime,
                                      String       methodName) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        String startingTypeName  = OpenMetadataType.REFERENCEABLE.typeName;
        String resultingTypeName = OpenMetadataType.REFERENCEABLE.typeName;

        if (suppliedStartingTypeName != null)
        {
            startingTypeName = suppliedStartingTypeName;
        }
        if (suppliedResultingTypeName != null)
        {
            resultingTypeName = suppliedResultingTypeName;
        }

        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        startingGUID,
                                        startingGUIDParameterName,
                                        startingTypeName,
                                        OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeGUID,
                                        OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                        resultingTypeName,
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
     * Returns the list of actors/roles that assigned to a specific scope.
     *
     * @param userId       String   userId of user making request.
     * @param startingGUID    String   unique id for element.
     * @param startingGUIDParameterName name of parameter supplying the GUID
     * @param suppliedStartingTypeName name of the type of object that the search begins with - null means referenceable
     * @param suppliedResultingTypeName name of the type of object that the search returns - null mean referenceable
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
    public List<B> getAssignedActors(String       userId,
                                     String       startingGUID,
                                     String       startingGUIDParameterName,
                                     String       suppliedStartingTypeName,
                                     String       suppliedResultingTypeName,
                                     int          startFrom,
                                     int          pageSize,
                                     boolean      forLineage,
                                     boolean      forDuplicateProcessing,
                                     Date         effectiveTime,
                                     String       methodName) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        String startingTypeName  = OpenMetadataType.REFERENCEABLE.typeName;
        String resultingTypeName = OpenMetadataType.REFERENCEABLE.typeName;

        if (suppliedStartingTypeName != null)
        {
            startingTypeName = suppliedStartingTypeName;
        }
        if (suppliedResultingTypeName != null)
        {
            resultingTypeName = suppliedResultingTypeName;
        }

        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        startingGUID,
                                        startingGUIDParameterName,
                                        startingTypeName,
                                        OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeGUID,
                                        OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                        resultingTypeName,
                                        (String)null,
                                        null,
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
     * Add a commissioning stakeholder to a project/team/community etc.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this project
     * @param externalSourceName unique name of the software capability that owns this project
     * @param commissionedGUID unique identifier of the project etc
     * @param commissionedGUIDParameterName parameter supplying the commissionedGUID
     * @param stakeholderGUID unique identifier of the stakeholder - may be a person, team, role, ...
     * @param stakeholderGUIDParameterName parameter supplying the stakeholderGUID
     * @param stakeholderRole what are the responsibilities of a stakeholder?
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
    public void addStakeholder(String  userId,
                               String  externalSourceGUID,
                               String  externalSourceName,
                               String  commissionedGUID,
                               String  commissionedGUIDParameterName,
                               String  stakeholderGUID,
                               String  stakeholderGUIDParameterName,
                               String  stakeholderRole,
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
                                                                                     OpenMetadataProperty.STAKEHOLDER_ROLE.name,
                                                                                     stakeholderRole,
                                                                                     methodName);

        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  commissionedGUID,
                                  commissionedGUIDParameterName,
                                  OpenMetadataType.REFERENCEABLE.typeName,
                                  stakeholderGUID,
                                  stakeholderGUIDParameterName,
                                  OpenMetadataType.REFERENCEABLE.typeName,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataType.STAKEHOLDER_RELATIONSHIP.typeGUID,
                                  OpenMetadataType.STAKEHOLDER_RELATIONSHIP.typeName,
                                  this.setUpEffectiveDates(properties, effectiveFrom, effectiveTo),
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Remove a commissioning stakeholder from a project/team/community etc.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this project
     * @param externalSourceName unique name of the software capability that owns this project
     * @param commissionedGUID unique identifier of the project etc
     * @param commissionedGUIDParameterName parameter supplying the commissionedGUID
     * @param stakeholderGUID unique identifier of the stakeholder
     * @param stakeholderGUIDParameterName parameter supplying the stakeholderGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeStakeholder(String  userId,
                                  String  externalSourceGUID,
                                  String  externalSourceName,
                                  String  commissionedGUID,
                                  String  commissionedGUIDParameterName,
                                  String  stakeholderGUID,
                                  String  stakeholderGUIDParameterName,
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
                                      commissionedGUID,
                                      commissionedGUIDParameterName,
                                      OpenMetadataType.REFERENCEABLE.typeName,
                                      stakeholderGUID,
                                      stakeholderGUIDParameterName,
                                      OpenMetadataType.REFERENCEABLE.typeGUID,
                                      OpenMetadataType.REFERENCEABLE.typeName,
                                      forLineage,
                                      forDuplicateProcessing,
                                      OpenMetadataType.STAKEHOLDER_RELATIONSHIP.typeGUID,
                                      OpenMetadataType.STAKEHOLDER_RELATIONSHIP.typeName,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Returns the list one or more actors/roles are stakeholders for the starting element.
     *
     * @param userId       String   userId of user making request.
     * @param startingGUID    String   unique id for element.
     * @param startingGUIDParameterName name of parameter supplying the GUID
     * @param suppliedStartingTypeName name of the type of object that the search begins with - null means referenceable
     * @param suppliedResultingTypeName name of the type of object that the search returns - null mean referenceable
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
    public List<B> getStakeholders(String       userId,
                                   String       startingGUID,
                                   String       startingGUIDParameterName,
                                   String       suppliedStartingTypeName,
                                   String       suppliedResultingTypeName,
                                   int          startFrom,
                                   int          pageSize,
                                   boolean      forLineage,
                                   boolean      forDuplicateProcessing,
                                   Date         effectiveTime,
                                   String       methodName) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        String startingTypeName  = OpenMetadataType.REFERENCEABLE.typeName;
        String resultingTypeName = OpenMetadataType.REFERENCEABLE.typeName;

        if (suppliedStartingTypeName != null)
        {
            startingTypeName = suppliedStartingTypeName;
        }
        if (suppliedResultingTypeName != null)
        {
            resultingTypeName = suppliedResultingTypeName;
        }

        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        startingGUID,
                                        startingGUIDParameterName,
                                        startingTypeName,
                                        OpenMetadataType.STAKEHOLDER_RELATIONSHIP.typeGUID,
                                        OpenMetadataType.STAKEHOLDER_RELATIONSHIP.typeName,
                                        resultingTypeName,
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
     * Returns the list of elements that one or more actors/roles are stakeholders for.
     *
     * @param userId       String   userId of user making request.
     * @param startingGUID    String   unique id for element.
     * @param startingGUIDParameterName name of parameter supplying the GUID
     * @param suppliedStartingTypeName name of the type of object that the search begins with - null means referenceable
     * @param suppliedResultingTypeName name of the type of object that the search returns - null mean referenceable
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
    public List<B> getCommissionedByStakeholder(String       userId,
                                                String       startingGUID,
                                                String       startingGUIDParameterName,
                                                String       suppliedStartingTypeName,
                                                String       suppliedResultingTypeName,
                                                int          startFrom,
                                                int          pageSize,
                                                boolean      forLineage,
                                                boolean      forDuplicateProcessing,
                                                Date         effectiveTime,
                                                String       methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        String startingTypeName  = OpenMetadataType.REFERENCEABLE.typeName;
        String resultingTypeName = OpenMetadataType.REFERENCEABLE.typeName;

        if (suppliedStartingTypeName != null)
        {
            startingTypeName = suppliedStartingTypeName;
        }
        if (suppliedResultingTypeName != null)
        {
            resultingTypeName = suppliedResultingTypeName;
        }

        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        startingGUID,
                                        startingGUIDParameterName,
                                        startingTypeName,
                                        OpenMetadataType.STAKEHOLDER_RELATIONSHIP.typeGUID,
                                        OpenMetadataType.STAKEHOLDER_RELATIONSHIP.typeName,
                                        resultingTypeName,
                                        (String)null,
                                        null,
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
     * Retrieve the elements linked via a "SourceFrom" relationship to the requested element.
     * The elements returned were used to create the requested element.  Typically only one element is returned.
     *
     * @param userId       String   userId of user making request.
     * @param startingGUID    String   unique id for element.
     * @param startingGUIDParameterName name of parameter supplying the GUID
     * @param suppliedStartingTypeName name of the type of object that the search begins with - null means referenceable
     * @param suppliedResultingTypeName name of the type of object that the search returns - null mean referenceable
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
    public List<B> getSourceElements(String       userId,
                                     String       startingGUID,
                                     String       startingGUIDParameterName,
                                     String       suppliedStartingTypeName,
                                     String       suppliedResultingTypeName,
                                     int          startFrom,
                                     int          pageSize,
                                     boolean      forLineage,
                                     boolean      forDuplicateProcessing,
                                     Date         effectiveTime,
                                     String       methodName) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        String startingTypeName  = OpenMetadataType.REFERENCEABLE.typeName;
        String resultingTypeName = OpenMetadataType.REFERENCEABLE.typeName;

        if (suppliedStartingTypeName != null)
        {
            startingTypeName = suppliedStartingTypeName;
        }
        if (suppliedResultingTypeName != null)
        {
            resultingTypeName = suppliedResultingTypeName;
        }

        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        startingGUID,
                                        startingGUIDParameterName,
                                        startingTypeName,
                                        OpenMetadataType.SOURCED_FROM_RELATIONSHIP.typeGUID,
                                        OpenMetadataType.SOURCED_FROM_RELATIONSHIP.typeName,
                                        resultingTypeName,
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
     * Retrieve the elements linked via a "SourceFrom" relationship to the requested element.
     * The elements returned were created using the requested element as a template.
     *
     * @param userId       String   userId of user making request.
     * @param startingGUID    String   unique id for element.
     * @param startingGUIDParameterName name of parameter supplying the GUID
     * @param suppliedStartingTypeName name of the type of object that the search begins with - null means referenceable
     * @param suppliedResultingTypeName name of the type of object that the search returns - null mean referenceable
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
    public List<B> getElementsSourceFrom(String       userId,
                                         String       startingGUID,
                                         String       startingGUIDParameterName,
                                         String       suppliedStartingTypeName,
                                         String       suppliedResultingTypeName,
                                         int          startFrom,
                                         int          pageSize,
                                         boolean      forLineage,
                                         boolean      forDuplicateProcessing,
                                         Date         effectiveTime,
                                         String       methodName) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        String startingTypeName  = OpenMetadataType.REFERENCEABLE.typeName;
        String resultingTypeName = OpenMetadataType.REFERENCEABLE.typeName;

        if (suppliedStartingTypeName != null)
        {
            startingTypeName = suppliedStartingTypeName;
        }
        if (suppliedResultingTypeName != null)
        {
            resultingTypeName = suppliedResultingTypeName;
        }

        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        startingGUID,
                                        startingGUIDParameterName,
                                        startingTypeName,
                                        OpenMetadataType.SOURCED_FROM_RELATIONSHIP.typeGUID,
                                        OpenMetadataType.SOURCED_FROM_RELATIONSHIP.typeName,
                                        resultingTypeName,
                                        (String)null,
                                        null,
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
     * Add a governedBy relationship between a governance definition and a referenceable.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this project
     * @param externalSourceName unique name of the software capability that owns this project
     * @param governanceDefinitionGUID unique identifier of the governance definition
     * @param governanceDefinitionGUIDParameterName parameter supplying the governanceDefinitionGUID
     * @param referenceableGUID unique identifier of the referenceable being governed
     * @param referenceableGUIDParameterName parameter supplying the referenceableGUID
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
    public void addGovernedBy(String  userId,
                              String  externalSourceGUID,
                              String  externalSourceName,
                              String  governanceDefinitionGUID,
                              String  governanceDefinitionGUIDParameterName,
                              String  referenceableGUID,
                              String  referenceableGUIDParameterName,
                              Date    effectiveFrom,
                              Date    effectiveTo,
                              boolean forLineage,
                              boolean forDuplicateProcessing,
                              Date    effectiveTime,
                              String  methodName) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  governanceDefinitionGUID,
                                  governanceDefinitionGUIDParameterName,
                                  OpenMetadataType.GOVERNANCE_DEFINITION_TYPE_NAME,
                                  referenceableGUID,
                                  referenceableGUIDParameterName,
                                  OpenMetadataType.REFERENCEABLE.typeName,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataType.GOVERNED_BY_TYPE_GUID,
                                  OpenMetadataType.GOVERNED_BY_TYPE_NAME,
                                  this.setUpEffectiveDates(null, effectiveFrom, effectiveTo),
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Remove a governedBy relationship between a governance definition and a referenceable.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this project
     * @param externalSourceName unique name of the software capability that owns this project
     * @param governanceDefinitionGUID unique identifier of the governance definition
     * @param governanceDefinitionGUIDParameterName parameter supplying the governanceDefinitionGUID
     * @param referenceableGUID unique identifier of the referenceable being governed
     * @param referenceableGUIDParameterName parameter supplying the referenceableGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeGovernedBy(String  userId,
                                 String  externalSourceGUID,
                                 String  externalSourceName,
                                 String  governanceDefinitionGUID,
                                 String  governanceDefinitionGUIDParameterName,
                                 String  referenceableGUID,
                                 String  referenceableGUIDParameterName,
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
                                      governanceDefinitionGUID,
                                      governanceDefinitionGUIDParameterName,
                                      OpenMetadataType.GOVERNANCE_DEFINITION_TYPE_NAME,
                                      referenceableGUID,
                                      referenceableGUIDParameterName,
                                      OpenMetadataType.REFERENCEABLE.typeGUID,
                                      OpenMetadataType.REFERENCEABLE.typeName,
                                      forLineage,
                                      forDuplicateProcessing,
                                      OpenMetadataType.GOVERNED_BY_TYPE_GUID,
                                      OpenMetadataType.GOVERNED_BY_TYPE_NAME,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Returns the list of elements that are governed by a governance definition.
     *
     * @param userId       String   userId of user making request.
     * @param governanceDefinitionGUID    String   unique id for element.
     * @param governanceDefinitionGUIDParameterName name of parameter supplying the GUID
     * @param suppliedResultingTypeName name of the type of object that the search returns - null mean referenceable
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
    public List<B> getGovernedElements(String       userId,
                                       String       governanceDefinitionGUID,
                                       String       governanceDefinitionGUIDParameterName,
                                       String       suppliedResultingTypeName,
                                       int          startFrom,
                                       int          pageSize,
                                       boolean      forLineage,
                                       boolean      forDuplicateProcessing,
                                       Date         effectiveTime,
                                       String       methodName) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        String resultingTypeName = OpenMetadataType.REFERENCEABLE.typeName;

        if (suppliedResultingTypeName != null)
        {
            resultingTypeName = suppliedResultingTypeName;
        }

        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        governanceDefinitionGUID,
                                        governanceDefinitionGUIDParameterName,
                                        OpenMetadataType.GOVERNANCE_DEFINITION_TYPE_NAME,
                                        OpenMetadataType.GOVERNED_BY_TYPE_GUID,
                                        OpenMetadataType.GOVERNED_BY_TYPE_NAME,
                                        resultingTypeName,
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
     * Add a governedBy relationship between a governance definition and a referenceable.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this project
     * @param externalSourceName unique name of the software capability that owns this project
     * @param governanceDefinitionGUID unique identifier of the governance definition
     * @param governanceDefinitionGUIDParameterName parameter supplying the governanceDefinitionGUID
     * @param referenceableGUID unique identifier of the referenceable being governed
     * @param referenceableGUIDParameterName parameter supplying the referenceableGUID
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
    public void addGovernanceDefinitionScope(String  userId,
                                             String  externalSourceGUID,
                                             String  externalSourceName,
                                             String  governanceDefinitionGUID,
                                             String  governanceDefinitionGUIDParameterName,
                                             String  referenceableGUID,
                                             String  referenceableGUIDParameterName,
                                             Date    effectiveFrom,
                                             Date    effectiveTo,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing,
                                             Date    effectiveTime,
                                             String  methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  referenceableGUID,
                                  referenceableGUIDParameterName,
                                  OpenMetadataType.REFERENCEABLE.typeName,
                                  governanceDefinitionGUID,
                                  governanceDefinitionGUIDParameterName,
                                  OpenMetadataType.GOVERNANCE_DEFINITION_TYPE_NAME,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataType.GOVERNANCE_DEFINITION_SCOPE_TYPE_GUID,
                                  OpenMetadataType.GOVERNANCE_DEFINITION_SCOPE_TYPE_NAME,
                                  this.setUpEffectiveDates(null, effectiveFrom, effectiveTo),
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Remove a governedBy relationship between a governance definition and a referenceable.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this project
     * @param externalSourceName unique name of the software capability that owns this project
     * @param governanceDefinitionGUID unique identifier of the governance definition
     * @param governanceDefinitionGUIDParameterName parameter supplying the governanceDefinitionGUID
     * @param referenceableGUID unique identifier of the referenceable being governed
     * @param referenceableGUIDParameterName parameter supplying the referenceableGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeGovernanceDefinitionScope(String  userId,
                                                String  externalSourceGUID,
                                                String  externalSourceName,
                                                String  governanceDefinitionGUID,
                                                String  governanceDefinitionGUIDParameterName,
                                                String  referenceableGUID,
                                                String  referenceableGUIDParameterName,
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
                                      referenceableGUID,
                                      referenceableGUIDParameterName,
                                      OpenMetadataType.REFERENCEABLE.typeGUID,
                                      governanceDefinitionGUID,
                                      governanceDefinitionGUIDParameterName,
                                      OpenMetadataType.GOVERNANCE_DEFINITION_TYPE_GUID,
                                      OpenMetadataType.GOVERNANCE_DEFINITION_TYPE_NAME,
                                      forLineage,
                                      forDuplicateProcessing,
                                      OpenMetadataType.GOVERNANCE_DEFINITION_SCOPE_TYPE_GUID,
                                      OpenMetadataType.GOVERNANCE_DEFINITION_SCOPE_TYPE_NAME,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Returns the list of one or more referenceables that define the scope of a governance definition.
     *
     * @param userId       String   userId of user making request.
     * @param governanceDefinitionGUID    String   unique id for element.
     * @param governanceDefinitionGUIDParameterName name of parameter supplying the GUID
     * @param suppliedResultingTypeName name of the type of object that the search returns - null mean referenceable
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
    public List<B> getGovernanceDefinitionScope(String       userId,
                                                String       governanceDefinitionGUID,
                                                String       governanceDefinitionGUIDParameterName,
                                                String       suppliedResultingTypeName,
                                                int          startFrom,
                                                int          pageSize,
                                                boolean      forLineage,
                                                boolean      forDuplicateProcessing,
                                                Date         effectiveTime,
                                                String       methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        String resultingTypeName = OpenMetadataType.REFERENCEABLE.typeName;

        if (suppliedResultingTypeName != null)
        {
            resultingTypeName = suppliedResultingTypeName;
        }

        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        governanceDefinitionGUID,
                                        governanceDefinitionGUIDParameterName,
                                        OpenMetadataType.GOVERNANCE_DEFINITION_TYPE_NAME,
                                        OpenMetadataType.GOVERNANCE_DEFINITION_SCOPE_TYPE_GUID,
                                        OpenMetadataType.GOVERNANCE_DEFINITION_SCOPE_TYPE_NAME,
                                        resultingTypeName,
                                        (String)null,
                                        null,
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
     * Add a link between a design artifact and its implementation.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this project
     * @param externalSourceName unique name of the software capability that owns this project
     * @param designArtifactGUID unique identifier of the project
     * @param designArtifactGUIDParameterName parameter supplying the designArtifactGUID
     * @param implementationGUID unique identifier of the element that is being added to the project
     * @param implementationGUIDParameterName parameter supplying the implementationGUID
     * @param designStep match to design process
     * @param role describe role performing from design
     * @param description additional description
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
    public void addImplementedBy(String  userId,
                                 String  externalSourceGUID,
                                 String  externalSourceName,
                                 String  designArtifactGUID,
                                 String  designArtifactGUIDParameterName,
                                 String  implementationGUID,
                                 String  implementationGUIDParameterName,
                                 String  designStep,
                                 String  role,
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
        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     OpenMetadataType.DESIGN_STEP_PROPERTY_NAME,
                                                                                     designStep,
                                                                                     methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataType.ROLE_PROPERTY_NAME,
                                                                  role,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.DESCRIPTION.name,
                                                                  description,
                                                                  methodName);



        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  designArtifactGUID,
                                  designArtifactGUIDParameterName,
                                  OpenMetadataType.REFERENCEABLE.typeName,
                                  implementationGUID,
                                  implementationGUIDParameterName,
                                  OpenMetadataType.REFERENCEABLE.typeName,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP_TYPE_GUID,
                                  OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP_TYPE_NAME,
                                  this.setUpEffectiveDates(properties, effectiveFrom, effectiveTo),
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Remove a link between a design artifact and its implementation.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this project
     * @param externalSourceName unique name of the software capability that owns this project
     * @param designArtifactGUID unique identifier of the project
     * @param designArtifactGUIDParameterName parameter supplying the designArtifactGUID
     * @param implementationGUID unique identifier of the element that is being added to the project
     * @param implementationGUIDParameterName parameter supplying the implementationGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeImplementedBy(String  userId,
                                    String  externalSourceGUID,
                                    String  externalSourceName,
                                    String  designArtifactGUID,
                                    String  designArtifactGUIDParameterName,
                                    String  implementationGUID,
                                    String  implementationGUIDParameterName,
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
                                      designArtifactGUID,
                                      designArtifactGUIDParameterName,
                                      OpenMetadataType.REFERENCEABLE.typeName,
                                      implementationGUID,
                                      implementationGUIDParameterName,
                                      OpenMetadataType.REFERENCEABLE.typeGUID,
                                      OpenMetadataType.REFERENCEABLE.typeName,
                                      forLineage,
                                      forDuplicateProcessing,
                                      OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP_TYPE_GUID,
                                      OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP_TYPE_NAME,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Return the list of elements associated with a collection.
     *
     * @param userId calling user
     * @param collectionGUID unique identifier of the collection to query
     * @param collectionGUIDParameterName name of the parameter supplying collectionGUID
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return list of metadata elements describing the definitions associated with the requested definition
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B>   getCollectionMembers(String  userId,
                                          String  collectionGUID,
                                          String  collectionGUIDParameterName,
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
                                        collectionGUID,
                                        collectionGUIDParameterName,
                                        OpenMetadataType.COLLECTION.typeName,
                                        OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeGUID,
                                        OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                        OpenMetadataType.REFERENCEABLE.typeName,
                                        null,
                                        null,
                                        0,
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
     * Set the KnownDuplicate classification on an entity if it is not already set up.
     *
     * @param userId calling user
     * @param entity retrieved entity
     * @param guidParameterName parameter name to use of the requested GUID
     * @param serviceSupportedZones list of supported zones for this service
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private void setKnowDuplicateClassification(String       userId,
                                                EntityDetail entity,
                                                String       guidParameterName,
                                                List<String> serviceSupportedZones,
                                                String       methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        boolean classificationNeeded = true;
        try
        {
            if (repositoryHelper.getClassificationFromEntity(serviceName,
                                                             entity,
                                                             OpenMetadataType.KNOWN_DUPLICATE_CLASSIFICATION_TYPE_NAME,
                                                             methodName) != null)
            {
                classificationNeeded = false;
            }
        }
        catch (ClassificationErrorException classificationError)
        {
            // nothing to do
        }

        if (classificationNeeded)
        {
            this.setClassificationInRepository(userId,
                                               null,
                                               null,
                                               entity,
                                               guidParameterName,
                                               OpenMetadataType.REFERENCEABLE.typeName,
                                               OpenMetadataType.KNOWN_DUPLICATE_CLASSIFICATION_TYPE_GUID,
                                               OpenMetadataType.KNOWN_DUPLICATE_CLASSIFICATION_TYPE_NAME,
                                               null,
                                               true,
                                               false,
                                               true,
                                               serviceSupportedZones,
                                               new Date(),
                                               methodName);
        }
    }


    /**
     * Set up the standard properties for elements related to stewardship
     *
     * @param statusIdentifier what is the status of this relationship (negative means untrusted, 0 means unverified and positive means trusted)
     * @param steward identifier of the steward
     * @param stewardTypeName type of element used to identify the steward
     * @param stewardPropertyName property name used to identify steward
     * @param source source of the duplicate detection processing
     * @param notes notes for the steward
     * @param methodName calling method
     */
    private InstanceProperties createStewardshipProperties(int     statusIdentifier,
                                                           String  steward,
                                                           String  stewardTypeName,
                                                           String  stewardPropertyName,
                                                           String  source,
                                                           String  notes,
                                                           String  methodName)
    {
        InstanceProperties properties  = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                      null,
                                                                                      OpenMetadataProperty.STEWARD.name,
                                                                                      steward,
                                                                                      methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.STEWARD_TYPE_NAME.name,
                                                                  stewardTypeName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.STEWARD_PROPERTY_NAME.name,
                                                                  stewardPropertyName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.SOURCE.name,
                                                                  source,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.NOTES.name,
                                                                  notes,
                                                                  methodName);

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               OpenMetadataProperty.STATUS_IDENTIFIER.name,
                                                               statusIdentifier,
                                                               methodName);

        return properties;
    }


    /**
     * Create a simple relationship between two elements in an Asset description (typically the asset itself or
     * attributes in their schema).
     *
     * @param userId calling user
     * @param element1GUID unique identifier of first element
     * @param element1GUIDParameter name of parameter supplying element1GUID
     * @param element2GUID unique identifier of second element
     * @param element2GUIDParameter name of parameter supplying element2GUID
     * @param setKnownDuplicate should the KnownDuplicate classification be set on the elements?
     * @param statusIdentifier what is the status of this relationship (negative means untrusted, 0 means unverified and positive means trusted)
     * @param steward identifier of the steward
     * @param stewardTypeName type of element used to identify the steward
     * @param stewardPropertyName property name used to identify steward
     * @param source source of the duplicate detection processing
     * @param notes notes for the steward
     * @param serviceSupportedZones list of supported zones for this service
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void linkElementsAsPeerDuplicates(String       userId,
                                             String       element1GUID,
                                             String       element1GUIDParameter,
                                             String       element2GUID,
                                             String       element2GUIDParameter,
                                             boolean      setKnownDuplicate,
                                             int          statusIdentifier,
                                             String       steward,
                                             String       stewardTypeName,
                                             String       stewardPropertyName,
                                             String       source,
                                             String       notes,
                                             List<String> serviceSupportedZones,
                                             String       methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(element1GUID, element1GUIDParameter, methodName);
        invalidParameterHandler.validateGUID(element2GUID, element2GUIDParameter, methodName);

        /*
         * First check the GUIDs are valid.
         */
        EntityDetail entity1 = this.getEntityFromRepository(userId,
                                                            element1GUID,
                                                            element1GUIDParameter,
                                                            OpenMetadataType.REFERENCEABLE.typeName,
                                                            null,
                                                            null,
                                                            false,
                                                            true,
                                                            serviceSupportedZones,
                                                            null,
                                                            methodName);

        EntityDetail entity2 = this.getEntityFromRepository(userId,
                                                            element2GUID,
                                                            element2GUIDParameter,
                                                            OpenMetadataType.REFERENCEABLE.typeName,
                                                            null,
                                                            null,
                                                            false,
                                                            true,
                                                            serviceSupportedZones,
                                                            null,
                                                            methodName);

        /*
         * Next set up the classifications if needed.
         */
        if (setKnownDuplicate)
        {
            this.setKnowDuplicateClassification(userId, entity1, element1GUIDParameter, serviceSupportedZones, methodName);
            this.setKnowDuplicateClassification(userId, entity2, element2GUIDParameter, serviceSupportedZones, methodName);
        }

        /*
         * Finally, link the entities together.
         */
        InstanceProperties properties = this.createStewardshipProperties(statusIdentifier,
                                                                         steward,
                                                                         stewardTypeName,
                                                                         stewardPropertyName,
                                                                         source,
                                                                         notes,
                                                                         methodName);

        this.linkElementToElement(userId,
                                  null,
                                  null,
                                  element1GUID,
                                  element1GUIDParameter,
                                  OpenMetadataType.REFERENCEABLE.typeName,
                                  element2GUID,
                                  element2GUIDParameter,
                                  OpenMetadataType.REFERENCEABLE.typeName,
                                  false,
                                  true,
                                  serviceSupportedZones,
                                  OpenMetadataType.PEER_DUPLICATE_LINK_TYPE_GUID,
                                  OpenMetadataType.PEER_DUPLICATE_LINK_TYPE_NAME,
                                  properties,
                                  null,
                                  null,
                                  null,
                                  methodName);
    }


    /**
     * Remove the relationship between two elements that marks them as duplicates.
     *
     * @param userId calling user
     * @param element1GUID unique identifier of first element
     * @param element1GUIDParameter name of parameter supplying element1GUID
     * @param element2GUID unique identifier of second element
     * @param element2GUIDParameter name of parameter supplying element2GUID
     * @param serviceSupportedZones list of supported zones for this service
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid or the elements are not linked as duplicates
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void unlinkElementsAsPeerDuplicates(String       userId,
                                               String       element1GUID,
                                               String       element1GUIDParameter,
                                               String       element2GUID,
                                               String       element2GUIDParameter,
                                               List<String> serviceSupportedZones,
                                               String       methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(element1GUID, element1GUIDParameter, methodName);
        invalidParameterHandler.validateGUID(element2GUID, element2GUIDParameter, methodName);

        /*
         * The repository helper will validate the types of GUIDs etc
         */
        repositoryHandler.removeRelationshipBetweenEntities(userId,
                                                            null,
                                                            null,
                                                            OpenMetadataType.PEER_DUPLICATE_LINK_TYPE_GUID,
                                                            OpenMetadataType.PEER_DUPLICATE_LINK_TYPE_NAME,
                                                            element1GUID,
                                                            OpenMetadataType.REFERENCEABLE.typeName,
                                                            element2GUID,
                                                            null,
                                                            null,
                                                            SequencingOrder.CREATION_DATE_RECENT,
                                                            null,
                                                            true,
                                                            true,
                                                            null,
                                                            methodName);

        /*
         * Determine whether the entities are still linked as duplicates.  If they are not, then remove their known duplicate classification.
         */
        if (this.getAttachmentLinks(userId,
                                    element1GUID,
                                    element1GUIDParameter,
                                    OpenMetadataType.REFERENCEABLE.typeName,
                                    OpenMetadataType.PEER_DUPLICATE_LINK_TYPE_GUID,
                                    OpenMetadataType.PEER_DUPLICATE_LINK_TYPE_NAME,
                                    null,
                                    null,
                                    0,
                                    null,
                                    null,
                                    SequencingOrder.CREATION_DATE_RECENT,
                                    null,
                                    true,
                                    true,
                                    serviceSupportedZones,
                                    0,
                                    invalidParameterHandler.getMaxPagingSize(),
                                    null,
                                    methodName) == null)
        {
            this.removeClassificationFromRepository(userId,
                                                    null,
                                                    null,
                                                    element1GUID,
                                                    element1GUIDParameter,
                                                    OpenMetadataType.REFERENCEABLE.typeName,
                                                    OpenMetadataType.KNOWN_DUPLICATE_CLASSIFICATION_TYPE_GUID,
                                                    OpenMetadataType.KNOWN_DUPLICATE_CLASSIFICATION_TYPE_NAME,
                                                    true,
                                                    true,
                                                    serviceSupportedZones,
                                                    null,
                                                    methodName);
        }


        if (this.getAttachmentLinks(userId,
                                    element2GUID,
                                    element2GUIDParameter,
                                    OpenMetadataType.REFERENCEABLE.typeName,
                                    OpenMetadataType.PEER_DUPLICATE_LINK_TYPE_GUID,
                                    OpenMetadataType.PEER_DUPLICATE_LINK_TYPE_NAME,
                                    null,
                                    null,
                                    0,
                                    null,
                                    null,
                                    SequencingOrder.CREATION_DATE_RECENT,
                                    null,
                                    false,
                                    true,
                                    serviceSupportedZones,
                                    0,
                                    invalidParameterHandler.getMaxPagingSize(),
                                    null,
                                    methodName) == null)
        {
            this.removeClassificationFromRepository(userId,
                                                    null,
                                                    null,
                                                    element2GUID,
                                                    element2GUIDParameter,
                                                    OpenMetadataType.REFERENCEABLE.typeName,
                                                    OpenMetadataType.KNOWN_DUPLICATE_CLASSIFICATION_TYPE_GUID,
                                                    OpenMetadataType.KNOWN_DUPLICATE_CLASSIFICATION_TYPE_NAME,
                                                    false,
                                                    true,
                                                    serviceSupportedZones,
                                                    null,
                                                    methodName);
        }
    }


    /**
     * Set the ConsolidatedDuplicate classification on an entity if it is not already set up.
     *
     * @param userId calling user
     * @param entity retrieved entity
     * @param guidParameterName parameter name to use of the requested GUID
     * @param statusIdentifier what is the status of this relationship (negative means untrusted, 0 means unverified and positive means trusted)
     * @param steward identifier of the steward
     * @param stewardTypeName type of element used to identify the steward
     * @param stewardPropertyName property name used to identify steward
     * @param source source of the duplicate detection processing
     * @param notes notes for the steward
     * @param serviceSupportedZones list of supported zones for this service
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private void setConsolidatedDuplicateClassification(String       userId,
                                                        EntityDetail entity,
                                                        String       guidParameterName,
                                                        int          statusIdentifier,
                                                        String       steward,
                                                        String       stewardTypeName,
                                                        String       stewardPropertyName,
                                                        String       source,
                                                        String       notes,
                                                        List<String> serviceSupportedZones,
                                                        String       methodName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        InstanceProperties properties = this.createStewardshipProperties(statusIdentifier,
                                                                             steward,
                                                                             stewardTypeName,
                                                                             stewardPropertyName,
                                                                             source,
                                                                             notes,
                                                                             methodName);
        this.setClassificationInRepository(userId,
                                           null,
                                           null,
                                           entity,
                                           guidParameterName,
                                           OpenMetadataType.REFERENCEABLE.typeName,
                                           OpenMetadataType.CONSOLIDATED_DUPLICATE_TYPE_GUID,
                                           OpenMetadataType.CONSOLIDATED_DUPLICATE_TYPE_NAME,
                                           properties,
                                           false,
                                           false,
                                           true,
                                           serviceSupportedZones,
                                           null,
                                           methodName);
    }


    /**
     * Identify an element that acts as a consolidated version for a set of duplicate elements.
     * (The consolidated element is created using createMetadataElement.)
     * Creates a simple relationship between the elements. If the ConsolidatedDuplicate
     * classification already exists, the properties are updated.
     *
     * @param userId calling user
     * @param consolidatedElementGUID unique identifier of the metadata element
     * @param consolidatedElementGUIDParameter parameter name to use for the requested GUID
     * @param statusIdentifier what is the status of this relationship (negative means untrusted, 0 means unverified and positive means trusted)
     * @param steward identifier of the steward
     * @param stewardTypeName type of element used to identify the steward
     * @param stewardPropertyName property name used to identify steward
     * @param source source of the duplicate detection processing
     * @param notes notes for the steward
     * @param sourceElementGUIDs List of the source elements that must be linked to the consolidated element.  It is assumed that they already
     *                           have the KnownDuplicateClassification.
     * @param sourceElementGUIDsParameterName parameter name for the source GUIDs
     * @param serviceSupportedZones list of supported zones for this service
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void linkConsolidatedDuplicate(String       userId,
                                          String       consolidatedElementGUID,
                                          String       consolidatedElementGUIDParameter,
                                          int          statusIdentifier,
                                          String       steward,
                                          String       stewardTypeName,
                                          String       stewardPropertyName,
                                          String       source,
                                          String       notes,
                                          List<String> sourceElementGUIDs,
                                          String       sourceElementGUIDsParameterName,
                                          List<String> serviceSupportedZones,
                                          String       methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(consolidatedElementGUID, consolidatedElementGUIDParameter, methodName);

        /*
         * First check the GUIDs are valid.
         */
        EntityDetail consolidatedEntity = this.getEntityFromRepository(userId,
                                                                       consolidatedElementGUID,
                                                                       consolidatedElementGUIDParameter,
                                                                       OpenMetadataType.REFERENCEABLE.typeName,
                                                                       null,
                                                                       null,
                                                                       false,
                                                                       true,
                                                                       serviceSupportedZones,
                                                                       null,
                                                                       methodName);

        if (consolidatedEntity != null)
        {
            this.setConsolidatedDuplicateClassification(userId,
                                                        consolidatedEntity,
                                                        consolidatedElementGUIDParameter,
                                                        statusIdentifier,
                                                        steward,
                                                        stewardTypeName,
                                                        stewardPropertyName,
                                                        source,
                                                        notes,
                                                        serviceSupportedZones,
                                                        methodName);

            if (sourceElementGUIDs != null)
            {
                for (String sourceElementGUID : sourceElementGUIDs)
                {
                    if (sourceElementGUID != null)
                    {
                        this.linkElementToElement(userId,
                                                  null,
                                                  null,
                                                  sourceElementGUID,
                                                  sourceElementGUIDsParameterName,
                                                  OpenMetadataType.REFERENCEABLE.typeName,
                                                  consolidatedElementGUID,
                                                  consolidatedElementGUIDParameter,
                                                  OpenMetadataType.REFERENCEABLE.typeName,
                                                  false,
                                                  true,
                                                  serviceSupportedZones,
                                                  OpenMetadataType.CONSOLIDATED_DUPLICATE_LINK_TYPE_GUID,
                                                  OpenMetadataType.CONSOLIDATED_DUPLICATE_LINK_TYPE_NAME,
                                                  null,
                                                  null,
                                                  null,
                                                  null,
                                                  methodName);
                    }
                }
            }
        }
    }


    /**
     * Create the property facet for the vendor properties.
     *
     * @param userId calling user
     * @param referenceableGUID unique identifier of the software capability
     * @param vendorProperties properties for the vendor
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void setVendorProperties(String               userId,
                                    String               referenceableGUID,
                                    Map<String, String>  vendorProperties,
                                    boolean              forLineage,
                                    boolean              forDuplicateProcessing,
                                    Date                 effectiveTime,
                                    String               methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String referenceableGUIDParameter = "referenceableGUID";
        final String propertyFacetGUIDParameter = "propertyFacet[x]";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(referenceableGUID, referenceableGUIDParameter, methodName);

        List<EntityDetail> propertyFacets = this.getAttachedEntities(userId,
                                                                     referenceableGUID,
                                                                     referenceableGUIDParameter,
                                                                     OpenMetadataType.REFERENCEABLE.typeName,
                                                                     OpenMetadataType.REFERENCEABLE_FACET.typeGUID,
                                                                     OpenMetadataType.REFERENCEABLE_FACET.typeName,
                                                                     OpenMetadataType.PROPERTY_FACET.typeName,
                                                                     null,
                                                                     null,
                                                                     2,
                                                                     null,
                                                                     null,
                                                                     SequencingOrder.CREATION_DATE_RECENT,
                                                                     null,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     supportedZones,
                                                                     0,
                                                                     invalidParameterHandler.getMaxPagingSize(),
                                                                     effectiveTime,
                                                                     methodName);

        if (vendorProperties != null)
        {
            PropertyFacetBuilder builder = new PropertyFacetBuilder(referenceableGUID + "_" + PropertyFacetValidValues.VENDOR_PROPERTIES_DESCRIPTION_VALUE,
                                                                    null,
                                                                    PropertyFacetValidValues.VENDOR_PROPERTIES_DESCRIPTION_VALUE,
                                                                    vendorProperties,
                                                                    repositoryHelper,
                                                                    serviceName,
                                                                    serverName);

            if (propertyFacets != null)
            {
                for (EntityDetail propertyFacet : propertyFacets)
                {
                    if (propertyFacet != null)
                    {
                        String description = repositoryHelper.getStringProperty(serviceName,
                                                                                OpenMetadataProperty.DESCRIPTION.name,
                                                                                propertyFacet.getProperties(),
                                                                                methodName);

                        if (PropertyFacetValidValues.VENDOR_PROPERTIES_DESCRIPTION_VALUE.equals(description))
                        {
                            this.updateBeanInRepository(userId,
                                                        null,
                                                        null,
                                                        propertyFacet.getGUID(),
                                                        propertyFacetGUIDParameter,
                                                        OpenMetadataType.PROPERTY_FACET.typeGUID,
                                                        OpenMetadataType.PROPERTY_FACET.typeName,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        supportedZones,
                                                        builder.getInstanceProperties(methodName),
                                                        true,
                                                        effectiveTime,
                                                        methodName);
                        }
                    }
                }
            }
            else
            {
                this.addAnchorGUIDToBuilder(userId,
                                            referenceableGUID,
                                            referenceableGUIDParameter,
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveTime,
                                            supportedZones,
                                            builder,
                                            methodName);

                String propertyFacetGUID = createBeanInRepository(userId,
                                                                  null,
                                                                  null,
                                                                  OpenMetadataType.PROPERTY_FACET.typeGUID,
                                                                  OpenMetadataType.PROPERTY_FACET.typeName,
                                                                  builder,
                                                                  effectiveTime,
                                                                  methodName);

                InstanceProperties relationshipProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                                         null,
                                                                                                         OpenMetadataProperty.SOURCE.name,
                                                                                                         serviceName,
                                                                                                         methodName);
                linkElementToElement(userId,
                                     null,
                                     null,
                                     referenceableGUID,
                                     referenceableGUIDParameter,
                                     OpenMetadataType.REFERENCEABLE.typeName,
                                     propertyFacetGUID,
                                     propertyFacetGUIDParameter,
                                     OpenMetadataType.PROPERTY_FACET.typeName,
                                     forLineage,
                                     forDuplicateProcessing,
                                     supportedZones,
                                     OpenMetadataType.REFERENCEABLE_FACET.typeGUID,
                                     OpenMetadataType.REFERENCEABLE_FACET.typeName,
                                     relationshipProperties,
                                     null,
                                     null,
                                     effectiveTime,
                                     methodName);

            }
        }
        else
        {
            if (propertyFacets != null)
            {
                for (EntityDetail propertyFacet : propertyFacets)
                {
                    if (propertyFacet != null)
                    {
                        String description = repositoryHelper.getStringProperty(serviceName,
                                                                                OpenMetadataProperty.DESCRIPTION.name,
                                                                                propertyFacet.getProperties(),
                                                                                methodName);

                        if (PropertyFacetValidValues.VENDOR_PROPERTIES_DESCRIPTION_VALUE.equals(description))
                        {
                            this.deleteBeanInRepository(userId,
                                                        null,
                                                        null,
                                                        propertyFacet.getGUID(),
                                                        propertyFacetGUIDParameter,
                                                        OpenMetadataType.PROPERTY_FACET.typeGUID,
                                                        OpenMetadataType.PROPERTY_FACET.typeName,
                                                        null,
                                                        null,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        effectiveTime,
                                                        methodName);
                        }
                    }
                }
            }
        }
    }


    /**
     * Retrieve the property facet for the vendor properties. It uses the supportedZones supplied with the service.
     *
     * @param userId calling user
     * @param referenceableGUID unique identifier of the metadata element
     * @param referenceableGUIDParameter parameter name for referenceableGUID
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return map of properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public Map<String, String> getVendorProperties(String  userId,
                                                   String  referenceableGUID,
                                                   String  referenceableGUIDParameter,
                                                   boolean forLineage,
                                                   boolean forDuplicateProcessing,
                                                   Date    effectiveTime,
                                                   String  methodName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        List<EntityDetail> propertyFacets = this.getAttachedEntities(userId,
                                                                     referenceableGUID,
                                                                     referenceableGUIDParameter,
                                                                     OpenMetadataType.REFERENCEABLE.typeName,
                                                                     OpenMetadataType.REFERENCEABLE_FACET.typeGUID,
                                                                     OpenMetadataType.REFERENCEABLE_FACET.typeName,
                                                                     OpenMetadataType.PROPERTY_FACET.typeName,
                                                                     null,
                                                                     null,
                                                                     0,
                                                                     null,
                                                                     null,
                                                                     SequencingOrder.CREATION_DATE_RECENT,
                                                                     null,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     supportedZones,
                                                                     0,
                                                                     invalidParameterHandler.getMaxPagingSize(),
                                                                     effectiveTime,
                                                                     methodName);

        if (propertyFacets != null)
        {
            for (EntityDetail propertyFacet : propertyFacets)
            {
                if (propertyFacet != null)
                {
                    String description = repositoryHelper.getStringProperty(serviceName,
                                                                            OpenMetadataProperty.DESCRIPTION.name,
                                                                            propertyFacet.getProperties(),
                                                                            methodName);

                    if (PropertyFacetValidValues.VENDOR_PROPERTIES_DESCRIPTION_VALUE.equals(description))
                    {
                        return repositoryHelper.getStringMapFromProperty(serviceName,
                                                                         OpenMetadataProperty.PROPERTIES.name,
                                                                         propertyFacet.getProperties(),
                                                                         methodName);
                    }
                }
            }
        }

        return null;
    }
}
