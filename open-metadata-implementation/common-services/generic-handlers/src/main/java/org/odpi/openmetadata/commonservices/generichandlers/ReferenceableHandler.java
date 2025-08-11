/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.mapper.PropertyFacetValidValues;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException;

import java.util.Date;
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
                                                             OpenMetadataType.KNOWN_DUPLICATE_CLASSIFICATION.typeName,
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
                                               OpenMetadataType.KNOWN_DUPLICATE_CLASSIFICATION.typeGUID,
                                               OpenMetadataType.KNOWN_DUPLICATE_CLASSIFICATION.typeName,
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
                                  OpenMetadataType.PEER_DUPLICATE_LINK.typeGUID,
                                  OpenMetadataType.PEER_DUPLICATE_LINK.typeName,
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
                                                            OpenMetadataType.PEER_DUPLICATE_LINK.typeGUID,
                                                            OpenMetadataType.PEER_DUPLICATE_LINK.typeName,
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
                                    OpenMetadataType.PEER_DUPLICATE_LINK.typeGUID,
                                    OpenMetadataType.PEER_DUPLICATE_LINK.typeName,
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
                                                    OpenMetadataType.KNOWN_DUPLICATE_CLASSIFICATION.typeGUID,
                                                    OpenMetadataType.KNOWN_DUPLICATE_CLASSIFICATION.typeName,
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
                                    OpenMetadataType.PEER_DUPLICATE_LINK.typeGUID,
                                    OpenMetadataType.PEER_DUPLICATE_LINK.typeName,
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
                                                    OpenMetadataType.KNOWN_DUPLICATE_CLASSIFICATION.typeGUID,
                                                    OpenMetadataType.KNOWN_DUPLICATE_CLASSIFICATION.typeName,
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
                                           OpenMetadataType.CONSOLIDATED_DUPLICATE_CLASSIFICATION.typeGUID,
                                           OpenMetadataType.CONSOLIDATED_DUPLICATE_CLASSIFICATION.typeName,
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
                                                  OpenMetadataType.CONSOLIDATED_DUPLICATE_LINK.typeGUID,
                                                  OpenMetadataType.CONSOLIDATED_DUPLICATE_LINK.typeName,
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
                                                                     OpenMetadataType.REFERENCEABLE_FACET_RELATIONSHIP.typeGUID,
                                                                     OpenMetadataType.REFERENCEABLE_FACET_RELATIONSHIP.typeName,
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
                                     OpenMetadataType.REFERENCEABLE_FACET_RELATIONSHIP.typeGUID,
                                     OpenMetadataType.REFERENCEABLE_FACET_RELATIONSHIP.typeName,
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
                                                        false,
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
                                                                     OpenMetadataType.REFERENCEABLE_FACET_RELATIONSHIP.typeGUID,
                                                                     OpenMetadataType.REFERENCEABLE_FACET_RELATIONSHIP.typeName,
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
