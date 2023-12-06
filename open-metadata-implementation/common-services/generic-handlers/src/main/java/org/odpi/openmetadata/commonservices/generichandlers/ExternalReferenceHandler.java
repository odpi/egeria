/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ExternalReferenceHandler manages ExternalReference objects.  It runs server-side in
 * the OMAG Server Platform and retrieves ExternalReference entities through the OMRSRepositoryConnector.
 */
public class ExternalReferenceHandler<B> extends ReferenceableHandler<B>
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
    public ExternalReferenceHandler(OpenMetadataAPIGenericConverter<B> converter,
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
     * Create a definition of a external reference.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param anchorGUID optional element to link the external reference to that will act as an anchor - that is, this external reference
     *                   will be deleted when the element is deleted (once the external reference is linked to the anchor).
     * @param qualifiedName unique name for the reference
     * @param displayName short display name for the reference
     * @param description description of the reference
     * @param url the location of the reference
     * @param referenceVersion version of the reference
     * @param organization owning org for the reference
     * @param additionalProperties additional properties for an external reference
     * @param suppliedTypeName type of external reference
     * @param extendedProperties  properties for an external reference subtype
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of external reference
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String createExternalReference(String              userId,
                                          String              externalSourceGUID,
                                          String              externalSourceName,
                                          String              anchorGUID,
                                          String              qualifiedName,
                                          String              displayName,
                                          String              description,
                                          String              url,
                                          String              referenceVersion,
                                          String              organization,
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
        final String anchorGUIDParameterName = "anchorGUID";

        String typeName = OpenMetadataAPIMapper.EXTERNAL_REFERENCE_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataAPIMapper.EXTERNAL_REFERENCE_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        ExternalReferenceBuilder builder = new ExternalReferenceBuilder(qualifiedName,
                                                                        displayName,
                                                                        description,
                                                                        url,
                                                                        referenceVersion,
                                                                        organization,
                                                                        additionalProperties,
                                                                        typeGUID,
                                                                        suppliedTypeName,
                                                                        extendedProperties,
                                                                        repositoryHelper,
                                                                        serviceName,
                                                                        serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        this.addAnchorGUIDToBuilder(userId,
                                    anchorGUID,
                                    anchorGUIDParameterName,
                                    false,
                                    false,
                                    effectiveTime,
                                    supportedZones,
                                    builder,
                                    methodName);

        return this.createBeanInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           OpenMetadataAPIMapper.EXTERNAL_REFERENCE_TYPE_GUID,
                                           OpenMetadataAPIMapper.EXTERNAL_REFERENCE_TYPE_NAME,
                                           builder,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Update the external reference.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param externalReferenceGUID unique identifier of the external reference to update
     * @param externalReferenceGUIDParameterName parameter passing the externalReferenceGUID
     * @param qualifiedName unique name for the reference
     * @param displayName short display name for the reference
     * @param description description of the reference
     * @param url the location of the reference
     * @param referenceVersion version of the reference
     * @param organization owning org for the reference
     * @param additionalProperties additional properties for an external reference
     * @param suppliedTypeName name of subtype or null
     * @param extendedProperties  properties for a governance externalReference subtype
     * @param isMergeUpdate should the properties be merged with existing properties or replace the existing properties?
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void   updateExternalReference(String              userId,
                                          String              externalSourceGUID,
                                          String              externalSourceName,
                                          String              externalReferenceGUID,
                                          String              externalReferenceGUIDParameterName,
                                          String              qualifiedName,
                                          String              displayName,
                                          String              description,
                                          String              url,
                                          String              referenceVersion,
                                          String              organization,
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
        final String qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(externalReferenceGUID, externalReferenceGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        String typeName = OpenMetadataAPIMapper.EXTERNAL_REFERENCE_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataAPIMapper.EXTERNAL_REFERENCE_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        ExternalReferenceBuilder builder = new ExternalReferenceBuilder(qualifiedName,
                                                                        displayName,
                                                                        description,
                                                                        url,
                                                                        referenceVersion,
                                                                        organization,
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
                                    externalReferenceGUID,
                                    externalReferenceGUIDParameterName,
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
     * Remove the metadata element representing an external reference.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param externalReferenceGUID unique identifier of the metadata element to remove
     * @param externalReferenceGUIDParameterName parameter supplying the externalReferenceGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeExternalReference(String  userId,
                                        String  externalSourceGUID,
                                        String  externalSourceName,
                                        String  externalReferenceGUID,
                                        String  externalReferenceGUIDParameterName,
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
                                    externalReferenceGUID,
                                    externalReferenceGUIDParameterName,
                                    OpenMetadataAPIMapper.EXTERNAL_REFERENCE_TYPE_GUID,
                                    OpenMetadataAPIMapper.EXTERNAL_REFERENCE_TYPE_NAME,
                                    null,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Return information about a specific external reference.
     *
     * @param userId calling user
     * @param qualifiedName unique name for the external reference
     * @param qualifiedNameParameter name of parameter supplying the qualifiedName
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return properties of the external reference
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public B getExternalReferenceByQualifiedName(String  userId,
                                                 String  qualifiedName,
                                                 String  qualifiedNameParameter,
                                                 boolean forLineage,
                                                 boolean forDuplicateProcessing,
                                                 Date    effectiveTime,
                                                 String  methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        return this.getBeanByUniqueName(userId,
                                        qualifiedName,
                                        qualifiedNameParameter,
                                        OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                        OpenMetadataAPIMapper.EXTERNAL_REFERENCE_TYPE_GUID,
                                        OpenMetadataAPIMapper.EXTERNAL_REFERENCE_TYPE_NAME,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Retrieve the list of external reference metadata elements with a matching referenceId.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param referenceId referenceId to search for
     * @param referenceIdParameterName parameter supplying referenceId
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
    public List<B>  getExternalReferencesById(String  userId,
                                              String  referenceId,
                                              String  referenceIdParameterName,
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
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);

        return this.getBeansByValue(userId,
                                    referenceId,
                                    referenceIdParameterName,
                                    OpenMetadataAPIMapper.EXTERNAL_REFERENCE_TYPE_GUID,
                                    OpenMetadataAPIMapper.EXTERNAL_REFERENCE_TYPE_NAME,
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
     * Retrieve the list of external reference metadata elements with a matching name.
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
    public List<B>  getExternalReferencesByName(String  userId,
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
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME);

        return this.getBeansByValue(userId,
                                    name,
                                    nameParameterName,
                                    OpenMetadataAPIMapper.EXTERNAL_REFERENCE_TYPE_GUID,
                                    OpenMetadataAPIMapper.EXTERNAL_REFERENCE_TYPE_NAME,
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
     * Retrieve the list of external reference metadata elements with a matching url.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param url url to search for
     * @param urlParameterName parameter supplying url
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
    public List<B>  getExternalReferencesByURL(String  userId,
                                               String  url,
                                               String  urlParameterName,
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
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.URL_PROPERTY_NAME);

        return this.getBeansByValue(userId,
                                    url,
                                    urlParameterName,
                                    OpenMetadataAPIMapper.EXTERNAL_REFERENCE_TYPE_GUID,
                                    OpenMetadataAPIMapper.EXTERNAL_REFERENCE_TYPE_NAME,
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
     * Return information about the defined external references.
     *
     * @param userId calling user
     * @param startingFrom position in the list (used when there are so many reports that paging is needed
     * @param pageSize maximum number of elements to return an this call
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return properties of the external reference
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<B> getExternalReferences(String  userId,
                                         int     startingFrom,
                                         int     pageSize,
                                         boolean forLineage,
                                         boolean forDuplicateProcessing,
                                         Date    effectiveTime,
                                         String  methodName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        return this.getBeansByType(userId,
                                   OpenMetadataAPIMapper.EXTERNAL_REFERENCE_TYPE_GUID,
                                   OpenMetadataAPIMapper.EXTERNAL_REFERENCE_TYPE_NAME,
                                   null,
                                   forLineage,
                                   forDuplicateProcessing,
                                   supportedZones,
                                   startingFrom,
                                   pageSize,
                                   effectiveTime,
                                   methodName);
    }


    /**
     * Retrieve the list of metadata elements that contain the search string.
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
    public List<B> findExternalReferences(String  userId,
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
                              OpenMetadataAPIMapper.EXTERNAL_REFERENCE_TYPE_GUID,
                              OpenMetadataAPIMapper.EXTERNAL_REFERENCE_TYPE_NAME,
                              null,
                              startFrom,
                              pageSize,
                              forLineage,
                              forDuplicateProcessing,
                              effectiveTime,
                              methodName);
    }
}
