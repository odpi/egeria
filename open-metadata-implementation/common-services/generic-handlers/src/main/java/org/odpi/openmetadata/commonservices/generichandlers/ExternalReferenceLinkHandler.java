/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ExternalReferenceLinkHandler manages retrieval of ExternalReference objects attached to a specific element.  Thus, it includes
 * the relationship properties and entity properties in the bean.  It runs server-side in
 * the OMAG Server Platform and retrieves ExternalReference entities through the OMRSRepositoryConnector.
 */
public class ExternalReferenceLinkHandler<B> extends ReferenceableHandler<B>
{
    private static final Logger log = LoggerFactory.getLogger(ExternalReferenceLinkHandler.class);

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
    public ExternalReferenceLinkHandler(OpenMetadataAPIGenericConverter<B> converter,
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
     * Create a relationship between a host and an operating platform.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param referenceableGUID unique identifier of the referenceable
     * @param referenceableGUIDParameterName parameter supplying the referenceableGUID
     * @param externalReferenceGUID unique identifier of the external reference
     * @param externalReferenceGUIDParameterName parameter supplying the externalReferenceGUID
     * @param referenceId local identifier for the reference
     * @param referenceDescription  short description for the link (think hover text)
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String setupExternalReferenceLink(String  userId,
                                             String  externalSourceGUID,
                                             String  externalSourceName,
                                             String  referenceableGUID,
                                             String  referenceableGUIDParameterName,
                                             String  externalReferenceGUID,
                                             String  externalReferenceGUIDParameterName,
                                             String  referenceId,
                                             String  referenceDescription,
                                             Date    effectiveFrom,
                                             Date    effectiveTo,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing,
                                             Date    effectiveTime,
                                             String  methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        ExternalReferenceBuilder builder = new ExternalReferenceBuilder(repositoryHelper,
                                                                        serviceName,
                                                                        serverName);

        InstanceProperties relationshipProperties = builder.getLinkProperties(referenceId, referenceDescription, methodName);

        return this.multiLinkElementToElement(userId,
                                              externalSourceGUID,
                                              externalSourceName,
                                              referenceableGUID,
                                              referenceableGUIDParameterName,
                                              OpenMetadataType.REFERENCEABLE.typeName,
                                              externalReferenceGUID,
                                              externalReferenceGUIDParameterName,
                                              OpenMetadataType.EXTERNAL_REFERENCE.typeName,
                                              forLineage,
                                              forDuplicateProcessing,
                                              supportedZones,
                                              OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeGUID,
                                              OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName,
                                              setUpEffectiveDates(relationshipProperties, effectiveFrom, effectiveTo),
                                              effectiveTime,
                                              methodName);
    }


    /**
     * Create a relationship between a host and an operating platform.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param relationshipGUID unique identifier of relationship
     * @param relationshipGUIDParameterName parameter supplying the relationshipGUID
     * @param referenceId local identifier for the reference
     * @param referenceDescription  short description for the link (think hover text)
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateExternalReferenceLink(String  userId,
                                            String  externalSourceGUID,
                                            String  externalSourceName,
                                            String  relationshipGUID,
                                            String  relationshipGUIDParameterName,
                                            String  referenceId,
                                            String  referenceDescription,
                                            Date    effectiveFrom,
                                            Date    effectiveTo,
                                            boolean forLineage,
                                            boolean forDuplicateProcessing,
                                            Date    effectiveTime,
                                            String  methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        ExternalReferenceBuilder builder = new ExternalReferenceBuilder(repositoryHelper,
                                                                        serviceName,
                                                                        serverName);

        InstanceProperties relationshipProperties = builder.getLinkProperties(referenceId, referenceDescription, methodName);

        this.updateElementToElementLink(userId,
                                        externalSourceGUID,
                                        externalSourceName,
                                        relationshipGUID,
                                        relationshipGUIDParameterName,
                                        OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName,
                                        forLineage,
                                        forDuplicateProcessing,
                                        supportedZones,
                                        false,
                                        setUpEffectiveDates(relationshipProperties, effectiveFrom, effectiveTo),
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Remove a relationship between a host and an operating platform..
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param relationshipGUID unique identifier of the referenceable
     * @param relationshipGUIDParameterName parameter supplying the relationshipGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return relationship that has just been deleted
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public Relationship clearExternalReferenceLink(String  userId,
                                                   String  externalSourceGUID,
                                                   String  externalSourceName,
                                                   String  relationshipGUID,
                                                   String  relationshipGUIDParameterName,
                                                   boolean forLineage,
                                                   boolean forDuplicateProcessing,
                                                   Date    effectiveTime,
                                                   String  methodName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String referenceableGUIDParameterName     = "relationship.getEntityOneProxy().getGUID()";
        final String externalReferenceGUIDParameterName = "relationship.getEntityTwoProxy().getGUID()";

        Relationship relationship = this.getAttachmentLink(userId,
                                                           relationshipGUID,
                                                           relationshipGUIDParameterName,
                                                           OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName,
                                                           effectiveTime,
                                                           methodName);

        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      relationship.getEntityOneProxy().getGUID(),
                                      referenceableGUIDParameterName,
                                      OpenMetadataType.REFERENCEABLE.typeName,
                                      relationship.getEntityTwoProxy().getGUID(),
                                      externalReferenceGUIDParameterName,
                                      OpenMetadataType.EXTERNAL_REFERENCE.typeGUID,
                                      OpenMetadataType.EXTERNAL_REFERENCE.typeName,
                                      forLineage,
                                      forDuplicateProcessing,
                                      OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName,
                                      relationship,
                                      effectiveTime,
                                      methodName);

        return relationship;
    }


    /**
     * Return the external references attached to an anchor entity.
     *
     * @param userId     calling user
     * @param startingGUID identifier for the entity that the reference is attached to
     * @param startingGUIDParameterName name of parameter supplying the GUID
     * @param startingTypeName name of the type of object being attached to
     * @param serviceSupportedZones supported zones for calling service
     * @param startingFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return list of retrieved objects
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<B>  getExternalReferences(String       userId,
                                          String       startingGUID,
                                          String       startingGUIDParameterName,
                                          String       startingTypeName,
                                          List<String> serviceSupportedZones,
                                          int          startingFrom,
                                          int          pageSize,
                                          boolean      forLineage,
                                          boolean      forDuplicateProcessing,
                                          Date         effectiveTime,
                                          String       methodName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingGUID, startingGUIDParameterName, methodName);

        /*
         * Validates the parameters and retrieves the links to attached keywords that are visible to this user.
         * Relationships are returned so that the isPublic property from the relationship can be retrieved.
         */
        List<Relationship>  relationships = this.getAttachmentLinks(userId,
                                                                    startingGUID,
                                                                    startingGUIDParameterName,
                                                                    startingTypeName,
                                                                    OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeGUID,
                                                                    OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName,
                                                                    null,
                                                                    OpenMetadataType.EXTERNAL_REFERENCE.typeName,
                                                                    2,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    serviceSupportedZones,
                                                                    startingFrom,
                                                                    pageSize,
                                                                    effectiveTime,
                                                                    methodName);

        if ((relationships == null) || (relationships.isEmpty()))
        {
            return null;
        }

        List<B>  results = new ArrayList<>();

        for (Relationship  relationship : relationships)
        {
            if (relationship != null)
            {
                try
                {
                    final String entityGUIDParameterName = "entity.getGUID";

                    EntityDetail entity = this.getEntityFromRepository(userId,
                                                                       relationship.getEntityTwoProxy().getGUID(),
                                                                       entityGUIDParameterName,
                                                                       OpenMetadataType.EXTERNAL_REFERENCE.typeName,
                                                                       null,
                                                                       null,
                                                                       forLineage,
                                                                       forDuplicateProcessing,
                                                                       serviceSupportedZones,
                                                                       effectiveTime,
                                                                       methodName);

                    if (entity != null)
                    {
                        B bean = converter.getNewBean(beanClass, entity, relationship, methodName);

                        if (bean != null)
                        {
                            results.add(bean);
                        }
                    }
                }
                catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException inaccessibleEntity)
                {
                    // skip entities that are not visible to this user
                    if (log.isDebugEnabled())
                    {
                        log.debug("Skipping inaccessible entity", inaccessibleEntity);
                    }
                }
            }
        }

        if (results.isEmpty())
        {
            return null;
        }
        else
        {
            return results;
        }

    }
}
