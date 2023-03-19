/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.catalog.connector;

import org.odpi.openmetadata.accessservices.assetmanager.client.OpenMetadataStoreClient;
import org.odpi.openmetadata.accessservices.assetmanager.properties.SynchronizationDirection;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElements;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.SearchClassifications;
import org.odpi.openmetadata.frameworks.governanceaction.search.SearchProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.SequencingOrder;
import org.odpi.openmetadata.integrationservices.catalog.ffdc.CatalogIntegratorErrorCode;

import java.util.Date;
import java.util.List;


/**
 * OpenMetadataExchangeService provides a native open metadata interface.   It is possible to find and maintain any type of metadata through
 * this interface.  However, it is recommended to only use this interface when the other, more specialized interfaces do not meet your needs.
 */
public class OpenMetadataExchangeService
{
    private final OpenMetadataStoreClient  openMetadataStoreClient;
    private final String                   userId;
    private final String                   assetManagerGUID;
    private final String                   assetManagerName;
    private final String                   connectorName;
    private final SynchronizationDirection synchronizationDirection;

    private boolean forLineage             = false;
    private boolean forDuplicateProcessing = false;
    private boolean assetManagerIsHome     = false;

    /**
     * Create a new client to exchange any content with open metadata.
     *
     * @param openMetadataStoreClient client for exchange requests
     * @param synchronizationDirection direction(s) that metadata can flow
     * @param userId integration daemon's userId
     * @param assetManagerGUID unique identifier of the software server capability for the asset manager
     * @param assetManagerName unique name of the software server capability for the asset manager
     * @param connectorName name of the connector using this context
     */
    OpenMetadataExchangeService(OpenMetadataStoreClient  openMetadataStoreClient,
                                SynchronizationDirection synchronizationDirection,
                                String                   userId,
                                String                   assetManagerGUID,
                                String                   assetManagerName,
                                String                   connectorName)
    {
        this.openMetadataStoreClient     = openMetadataStoreClient;
        this.synchronizationDirection    = synchronizationDirection;
        this.userId                      = userId;
        this.connectorName               = connectorName;
        this.assetManagerGUID            = assetManagerGUID;
        this.assetManagerName            = assetManagerName;
    }


    /* ========================================================
     * Set up the forLineage flag
     */

    /**
     * Return whether any metadata elements, classifications and relationships created though this interface will be members of the local cohort or
     * members of an external metadata collection.
     *
     * @return boolean flag
     */
    public boolean isAssetManagerHome()
    {
        return assetManagerIsHome;
    }


    /**
     * Set up whether any metadata elements, classifications and relationships created though this interface will be members of the local cohort or
     * members of an external metadata collection.
     *
     * @param assetManagerIsHome boolean flag
     */
    public void setAssetManagerIsHome(boolean assetManagerIsHome)
    {
        this.assetManagerIsHome = assetManagerIsHome;
    }


    /* ========================================================
     * Set up the forLineage flag
     */

    /**
     * Return whether retrieval requests from this service are to include elements with the Memento classification attached or not.
     *
     * @return boolean flag
     */
    public boolean isForLineage()
    {
        return forLineage;
    }


    /**
     * Set up whether retrieval requests from this service are to include elements with the Memento classification attached or not.
     *
     * @param forLineage boolean flag
     */
    public void setForLineage(boolean forLineage)
    {
        this.forLineage = forLineage;
    }


    /* ========================================================
     * Set up the forDuplicateProcessing flag
     */

    /**
     * Return whether retrieval requests from this service are to avoid merging duplicates or not.
     *
     * @return boolean flag
     */
    public boolean isForDuplicateProcessing()
    {
        return forDuplicateProcessing;
    }


    /**
     * Set up whether retrieval requests from this service are to avoid merging duplicates or not.
     *
     * @param forDuplicateProcessing boolean flag
     */
    public void setForDuplicateProcessing(boolean forDuplicateProcessing)
    {
        this.forDuplicateProcessing = forDuplicateProcessing;
    }


    /**
     * Retrieve the metadata element using its unique identifier.
     *
     * @param elementGUID unique identifier for the metadata element
     * @param effectiveTime          only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element properties
     *
     * @throws InvalidParameterException  the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public OpenMetadataElement getMetadataElementByGUID(String elementGUID,
                                                        Date   effectiveTime) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        return openMetadataStoreClient.getMetadataElementByGUID(userId, elementGUID, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Retrieve the metadata element using its unique name (typically the qualified name).
     *
     * @param uniqueName             unique name for the metadata element
     * @param uniquePropertyName     name of property name to test in the open metadata element - if null "qualifiedName" is used
     * @param effectiveTime          only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element properties or null if not found
     *
     * @throws InvalidParameterException the unique identifier is null.
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public OpenMetadataElement getMetadataElementByUniqueName(String  uniqueName,
                                                              String  uniquePropertyName,
                                                              Date    effectiveTime) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        return openMetadataStoreClient.getMetadataElementByUniqueName(userId,
                                                                      uniqueName,
                                                                      uniquePropertyName,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      effectiveTime);
    }


    /**
     * Retrieve the unique identifier of a metadata element using its unique name (typically the qualified name).
     *
     * @param uniqueName             unique name for the metadata element
     * @param uniquePropertyName     name of property name to test in the open metadata element - if null "qualifiedName" is used
     * @param effectiveTime          only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element unique identifier (guid)
     *
     * @throws InvalidParameterException  the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public String getMetadataElementGUIDByUniqueName(String  uniqueName,
                                                     String  uniquePropertyName,
                                                     Date    effectiveTime) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        return openMetadataStoreClient.getMetadataElementGUIDByUniqueName(userId,
                                                                          uniqueName,
                                                                          uniquePropertyName,
                                                                          forLineage,
                                                                          forDuplicateProcessing,
                                                                          effectiveTime);
    }


    /**
     * Retrieve the metadata elements that contain the requested string.
     *
     * @param searchString           name to retrieve
     * @param effectiveTime          only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom              paging start point
     * @param pageSize               maximum results that can be returned
     *
     * @return list of matching metadata elements (or null if no elements match the name)
     *
     * @throws InvalidParameterException  the qualified name is null
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public List<OpenMetadataElement> findMetadataElementsWithString(String  searchString,
                                                                    Date    effectiveTime,
                                                                    int     startFrom,
                                                                    int     pageSize) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        return openMetadataStoreClient.findMetadataElementsWithString(userId,
                                                                      searchString,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      effectiveTime,
                                                                      startFrom,
                                                                      pageSize);
    }


    /**
     * Retrieve the metadata elements connected to the supplied element.
     *
     * @param elementGUID            unique identifier for the starting metadata element
     * @param startingAtEnd          indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param relationshipTypeName   type name of relationships to follow (or null for all)
     * @param effectiveTime          only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom              paging start point
     * @param pageSize               maximum results that can be returned
     *
     * @return list of related elements
     *
     * @throws InvalidParameterException  the unique identifier is null or not known; the relationship type is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public List<RelatedMetadataElement> getRelatedMetadataElements(String  elementGUID,
                                                                   int     startingAtEnd,
                                                                   String  relationshipTypeName,
                                                                   Date    effectiveTime,
                                                                   int     startFrom,
                                                                   int     pageSize) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        return openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                  elementGUID,
                                                                  startingAtEnd,
                                                                  relationshipTypeName,
                                                                  forLineage,
                                                                  forDuplicateProcessing,
                                                                  effectiveTime,
                                                                  startFrom,
                                                                  pageSize);
    }


    /**
     * Return a list of metadata elements that match the supplied criteria.  The results can be returned over many pages.
     *
     * @param metadataElementTypeName    type of interest (null means any element type)
     * @param metadataElementSubtypeName optional list of the subtypes of the metadataElementTypeName to
     *                                   include in the search results. Null means all subtypes.
     * @param searchProperties           Optional list of entity property conditions to match.
     * @param limitResultsByStatus       By default, entities in all statuses (other than DELETE) are returned.  However, it is possible
     *                                   to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param matchClassifications       Optional list of classifications to match.
     * @param sequencingProperty         String name of the property that is to be used to sequence the results.
     *                                   Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder            Enum defining how the results should be ordered.
     * @param effectiveTime              only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom                  paging start point
     * @param pageSize                   maximum results that can be returned
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     *
     * @throws InvalidParameterException  one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public List<OpenMetadataElement> findMetadataElements(String                metadataElementTypeName,
                                                          List<String>          metadataElementSubtypeName,
                                                          SearchProperties      searchProperties,
                                                          List<ElementStatus>   limitResultsByStatus,
                                                          SearchClassifications matchClassifications,
                                                          String                sequencingProperty,
                                                          SequencingOrder       sequencingOrder,
                                                          Date                  effectiveTime,
                                                          int                   startFrom,
                                                          int                   pageSize) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        return openMetadataStoreClient.findMetadataElements(userId,
                                                            metadataElementTypeName,
                                                            metadataElementSubtypeName,
                                                            searchProperties,
                                                            limitResultsByStatus,
                                                            matchClassifications,
                                                            sequencingProperty,
                                                            sequencingOrder,
                                                            forLineage,
                                                            forDuplicateProcessing,
                                                            effectiveTime,
                                                            startFrom,
                                                            pageSize);
    }


    /**
     * Return a list of relationships that match the requested conditions.  The results can be received as a series of pages.
     *
     * @param relationshipTypeName   relationship's type.  Null means all types
     *                               (but may be slow so not recommended).
     * @param searchProperties       Optional list of relationship property conditions to match.
     * @param sequencingProperty     String name of the property that is to be used to sequence the results.
     *                               Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder        Enum defining how the results should be ordered.
     * @param effectiveTime          only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom              paging start point
     * @param pageSize               maximum results that can be returned
     *
     * @return a list of relationships.  Null means no matching relationships.
     *
     * @throws InvalidParameterException  one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public List<RelatedMetadataElements> findRelationshipsBetweenMetadataElements(String           relationshipTypeName,
                                                                                  SearchProperties searchProperties,
                                                                                  String           sequencingProperty,
                                                                                  SequencingOrder  sequencingOrder,
                                                                                  Date             effectiveTime,
                                                                                  int              startFrom,
                                                                                  int              pageSize) throws InvalidParameterException,
                                                                                                                    UserNotAuthorizedException,
                                                                                                                    PropertyServerException
    {
        return openMetadataStoreClient.findRelationshipsBetweenMetadataElements(userId,
                                                                                relationshipTypeName,
                                                                                searchProperties,
                                                                                sequencingProperty,
                                                                                sequencingOrder,
                                                                                forLineage,
                                                                                forDuplicateProcessing,
                                                                                effectiveTime,
                                                                                startFrom,
                                                                                pageSize);
    }


    /**
     * Create a new metadata element in the metadata store.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * This version of the method allows access to advanced features such as multiple states and
     * effectivity dates.
     *
     * @param metadataElementTypeName type name of the new metadata element
     * @param initialStatus           initial status of the metadata element
     * @param effectiveFrom           the date when this element is active - null for active on creation
     * @param effectiveTo             the date when this element becomes inactive - null for active until deleted
     * @param properties              properties of the new metadata element
     * @param templateGUID            the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                                connection etc)
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of element
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    public String createMetadataElementInStore(String            metadataElementTypeName,
                                               ElementStatus     initialStatus,
                                               Date              effectiveFrom,
                                               Date              effectiveTo,
                                               ElementProperties properties,
                                               String            templateGUID) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName = "createMetadataElementInStore";

        if (synchronizationDirection == SynchronizationDirection.TO_THIRD_PARTY)
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }

        if (assetManagerIsHome)
        {
            return openMetadataStoreClient.createMetadataElementInStore(userId,
                                                                        metadataElementTypeName,
                                                                        initialStatus,
                                                                        effectiveFrom,
                                                                        effectiveTo,
                                                                        properties,
                                                                        templateGUID);
        }
        else
        {
            return openMetadataStoreClient.createMetadataElementInStore(userId,
                                                                        assetManagerGUID,
                                                                        assetManagerName,
                                                                        metadataElementTypeName,
                                                                        initialStatus,
                                                                        effectiveFrom,
                                                                        effectiveTo,
                                                                        properties,
                                                                        templateGUID);
        }
    }


    /**
     * Update the properties of a specific metadata element.  The properties must match the type definition associated with the
     * metadata element when it was created.  However, it is possible to update a few properties, or replace all them by
     * the value used in the replaceProperties flag.
     *
     * @param metadataElementGUID    unique identifier of the metadata element to update
     * @param replaceProperties      flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                               the individual properties specified on the request.
     * @param properties             new properties for the metadata element
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  either the unique identifier or the properties are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    public void updateMetadataElementInStore(String            metadataElementGUID,
                                             boolean           replaceProperties,
                                             ElementProperties properties,
                                             Date              effectiveTime) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName = "updateMetadataElementInStore";

        if (synchronizationDirection == SynchronizationDirection.TO_THIRD_PARTY)
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }

        if (assetManagerIsHome)
        {
            openMetadataStoreClient.updateMetadataElementInStore(userId,
                                                                 assetManagerGUID,
                                                                 assetManagerName,
                                                                 metadataElementGUID,
                                                                 replaceProperties,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 properties,
                                                                 effectiveTime);
        }
        else
        {
            openMetadataStoreClient.updateMetadataElementInStore(userId,
                                                                 metadataElementGUID,
                                                                 replaceProperties,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 properties,
                                                                 effectiveTime);
        }
    }


    /**
     * Update the status of specific metadata element. The new status must match a status value that is defined for the element's type
     * assigned when it was created.  The effectivity dates control the visibility of the element
     * through specific APIs.
     *
     * @param metadataElementGUID    unique identifier of the metadata element to update
     * @param newElementStatus       new status value - or null to leave as is
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    public void updateMetadataElementStatusInStore(String        metadataElementGUID,
                                                   ElementStatus newElementStatus,
                                                   Date          effectiveTime) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName = "updateMetadataElementStatusInStore";

        if (synchronizationDirection == SynchronizationDirection.TO_THIRD_PARTY)
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }

        if (assetManagerIsHome)
        {
            openMetadataStoreClient.updateMetadataElementStatusInStore(userId,
                                                                       assetManagerGUID,
                                                                       assetManagerName,
                                                                       metadataElementGUID,
                                                                       forLineage,
                                                                       forDuplicateProcessing,
                                                                       newElementStatus,
                                                                       effectiveTime);
        }
        else
        {
            openMetadataStoreClient.updateMetadataElementStatusInStore(userId,
                                                                       metadataElementGUID,
                                                                       forLineage,
                                                                       forDuplicateProcessing,
                                                                       newElementStatus,
                                                                       effectiveTime);
        }
    }


    /**
     * Update the status of specific metadata element. The new status must match a status value that is defined for the element's type
     * assigned when it was created.  The effectivity dates control the visibility of the element
     * through specific APIs.
     *
     * @param metadataElementGUID    unique identifier of the metadata element to update
     * @param effectiveFrom          the date when this element is active - null for active now
     * @param effectiveTo            the date when this element becomes inactive - null for active until deleted
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    public void updateMetadataElementEffectivityInStore(String metadataElementGUID,
                                                        Date   effectiveFrom,
                                                        Date   effectiveTo,
                                                        Date   effectiveTime) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName = "updateMetadataElementEffectivityInStore";

        if (synchronizationDirection == SynchronizationDirection.TO_THIRD_PARTY)
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }

        if (assetManagerIsHome)
        {
            openMetadataStoreClient.updateMetadataElementEffectivityInStore(userId,
                                                                            assetManagerGUID,
                                                                            assetManagerName,
                                                                            metadataElementGUID,
                                                                            forLineage,
                                                                            forDuplicateProcessing,
                                                                            effectiveFrom,
                                                                            effectiveTo,
                                                                            effectiveTime);
        }
        else
        {
            openMetadataStoreClient.updateMetadataElementEffectivityInStore(userId,
                                                                            metadataElementGUID,
                                                                            forLineage,
                                                                            forDuplicateProcessing,
                                                                            effectiveFrom,
                                                                            effectiveTo,
                                                                            effectiveTime);
        }
    }


    /**
     * Delete a specific metadata element.
     *
     * @param metadataElementGUID    unique identifier of the metadata element to update
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  the unique identifier is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to delete this element
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    public void deleteMetadataElementInStore(String  metadataElementGUID,
                                             Date    effectiveTime) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName = "deleteMetadataElementInStore";

        if (synchronizationDirection == SynchronizationDirection.TO_THIRD_PARTY)
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }

        if (assetManagerIsHome)
        {
            openMetadataStoreClient.deleteMetadataElementInStore(userId,
                                                                 assetManagerGUID,
                                                                 assetManagerName,
                                                                 metadataElementGUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 effectiveTime);
        }
        else
        {
            openMetadataStoreClient.deleteMetadataElementInStore(userId,
                                                                 metadataElementGUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 effectiveTime);
        }
    }


    /**
     * Add a new classification to the metadata element.  Note that only one classification with the same name can be attached to
     * a metadata element.
     *
     * @param metadataElementGUID    unique identifier of the metadata element to update
     * @param classificationName     name of the classification to add (if the classification is already present then use reclassify)
     * @param effectiveFrom          the date when this classification is active - null for active now
     * @param effectiveTo            the date when this classification becomes inactive - null for active until deleted
     * @param properties             properties to store in the new classification.  These must conform to the valid properties associated with the
     *                               classification name
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                    valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    public void classifyMetadataElementInStore(String            metadataElementGUID,
                                               String            classificationName,
                                               Date              effectiveFrom,
                                               Date              effectiveTo,
                                               ElementProperties properties,
                                               Date              effectiveTime) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName = "classifyMetadataElementInStore";

        if (synchronizationDirection == SynchronizationDirection.TO_THIRD_PARTY)
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }

        if (assetManagerIsHome)
        {
            openMetadataStoreClient.classifyMetadataElementInStore(userId,
                                                                   assetManagerGUID,
                                                                   assetManagerName,
                                                                   metadataElementGUID,
                                                                   classificationName,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   effectiveFrom,
                                                                   effectiveTo,
                                                                   properties,
                                                                   effectiveTime);
        }
        else
        {
            openMetadataStoreClient.classifyMetadataElementInStore(userId,
                                                                   metadataElementGUID,
                                                                   classificationName,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   effectiveFrom,
                                                                   effectiveTo,
                                                                   properties,
                                                                   effectiveTime);
        }
    }


    /**
     * Update the properties of a classification that is currently attached to a specific metadata element.
     *
     * @param metadataElementGUID    unique identifier of the metadata element to update
     * @param classificationName     unique name of the classification to update
     * @param replaceProperties      flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                               the individual properties specified on the request.
     * @param properties             new properties for the classification
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                    valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element/classification
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    public void reclassifyMetadataElementInStore(String            metadataElementGUID,
                                                 String            classificationName,
                                                 boolean           replaceProperties,
                                                 ElementProperties properties,
                                                 Date              effectiveTime) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName = "reclassifyMetadataElementInStore";

        if (synchronizationDirection == SynchronizationDirection.TO_THIRD_PARTY)
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }

        if (assetManagerIsHome)
        {
            openMetadataStoreClient.reclassifyMetadataElementInStore(userId,
                                                                     assetManagerGUID,
                                                                     assetManagerName,
                                                                     metadataElementGUID,
                                                                     classificationName,
                                                                     replaceProperties,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     properties,
                                                                     effectiveTime);
        }
        else
        {
            openMetadataStoreClient.reclassifyMetadataElementInStore(userId,
                                                                     metadataElementGUID,
                                                                     classificationName,
                                                                     replaceProperties,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     properties,
                                                                     effectiveTime);
        }
    }


    /**
     * Update the effectivity dates of a specific classification attached to a metadata element.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param metadataElementGUID    unique identifier of the metadata element to update
     * @param classificationName     unique name of the classification to update
     * @param effectiveFrom          the date when this element is active - null for active now
     * @param effectiveTo            the date when this element becomes inactive - null for active until deleted
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    public void updateClassificationEffectivityInStore(String  metadataElementGUID,
                                                       String  classificationName,
                                                       Date    effectiveFrom,
                                                       Date    effectiveTo,
                                                       Date    effectiveTime) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName = "updateClassificationEffectivityInStore";

        if (synchronizationDirection == SynchronizationDirection.TO_THIRD_PARTY)
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }

        if (assetManagerIsHome)
        {
            openMetadataStoreClient.updateClassificationEffectivityInStore(userId,
                                                                           assetManagerGUID,
                                                                           assetManagerName,
                                                                           metadataElementGUID,
                                                                           classificationName,
                                                                           forLineage,
                                                                           forDuplicateProcessing,
                                                                           effectiveFrom,
                                                                           effectiveTo,
                                                                           effectiveTime);
        }
        else
        {
            openMetadataStoreClient.updateClassificationEffectivityInStore(userId,
                                                                           metadataElementGUID,
                                                                           classificationName,
                                                                           forLineage,
                                                                           forDuplicateProcessing,
                                                                           effectiveFrom,
                                                                           effectiveTo,
                                                                           effectiveTime);
        }
    }


    /**
     * Remove the named classification from a specific metadata element.
     *
     * @param metadataElementGUID    unique identifier of the metadata element to update
     * @param classificationName     unique name of the classification to remove
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  the unique identifier or classification name is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to remove this classification
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    public void unclassifyMetadataElementInStore(String  metadataElementGUID,
                                                 String  classificationName,
                                                 Date    effectiveTime) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName = "unclassifyMetadataElementInStore";

        if (synchronizationDirection == SynchronizationDirection.TO_THIRD_PARTY)
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }

        if (assetManagerIsHome)
        {
            openMetadataStoreClient.declassifyMetadataElementInStore(userId,
                                                                     assetManagerGUID,
                                                                     assetManagerName,
                                                                     metadataElementGUID,
                                                                     classificationName,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     effectiveTime);
        }
        else
        {
            openMetadataStoreClient.declassifyMetadataElementInStore(userId,
                                                                     metadataElementGUID,
                                                                     classificationName,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     effectiveTime);
        }
    }


    /**
     * Create a relationship between two metadata elements.  It is important to put the right element at each end of the relationship
     * according to the type definition since this will affect how the relationship is interpreted.
     *
     * @param relationshipTypeName   name of the type of relationship to create.  This will determine the types of metadata elements that can be
     *                               related and the properties that can be associated with this relationship.
     * @param metadataElement1GUID   unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElement2GUID   unique identifier of the metadata element at end 2 of the relationship
     * @param effectiveFrom          the date when this element is active - null for active now
     * @param effectiveTo            the date when this element becomes inactive - null for active until deleted
     * @param properties             the properties of the relationship
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return unique identifier of the new relationship
     *
     * @throws InvalidParameterException  the unique identifier's of the metadata elements are null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of relationship
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    public String createRelatedElementsInStore(String            relationshipTypeName,
                                               String            metadataElement1GUID,
                                               String            metadataElement2GUID,
                                               Date              effectiveFrom,
                                               Date              effectiveTo,
                                               ElementProperties properties,
                                               Date              effectiveTime) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName = "createRelatedElementsInStore";

        if (synchronizationDirection == SynchronizationDirection.TO_THIRD_PARTY)
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }

        if (assetManagerIsHome)
        {
            return openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                        assetManagerGUID,
                                                                        assetManagerName,
                                                                        relationshipTypeName,
                                                                        metadataElement1GUID,
                                                                        metadataElement2GUID,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveFrom,
                                                                        effectiveTo,
                                                                        properties,
                                                                        effectiveTime);
        }
        else
        {
            return openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                        relationshipTypeName,
                                                                        metadataElement1GUID,
                                                                        metadataElement2GUID,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveFrom,
                                                                        effectiveTo,
                                                                        properties,
                                                                        effectiveTime);
        }
    }


    /**
     * Update the properties associated with a relationship.
     *
     * @param relationshipGUID unique identifier of the relationship to update
     * @param replaceProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param properties new properties for the relationship
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateRelatedElementsInStore(String            relationshipGUID,
                                             boolean           replaceProperties,
                                             ElementProperties properties,
                                             Date              effectiveTime) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName = "updateRelatedElementsInStore";

        if (synchronizationDirection == SynchronizationDirection.TO_THIRD_PARTY)
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }

        if (assetManagerIsHome)
        {
            openMetadataStoreClient.updateRelatedElementsInStore(userId,
                                                                 assetManagerGUID,
                                                                 assetManagerName,
                                                                 relationshipGUID,
                                                                 replaceProperties,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 properties,
                                                                 effectiveTime);
        }
        else
        {
            openMetadataStoreClient.updateRelatedElementsInStore(userId,
                                                                 relationshipGUID,
                                                                 replaceProperties,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 properties,
                                                                 effectiveTime);
        }
    }


    /**
     * Update the effectivity dates of a specific relationship between metadata elements.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param relationshipGUID unique identifier of the relationship to update
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public  void updateRelatedElementsEffectivityInStore(String  relationshipGUID,
                                                         Date    effectiveFrom,
                                                         Date    effectiveTo,
                                                         Date    effectiveTime) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName = "updateRelatedElementsEffectivityInStore";

        if (synchronizationDirection == SynchronizationDirection.TO_THIRD_PARTY)
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }

        if (assetManagerIsHome)
        {
            openMetadataStoreClient.updateRelatedElementsEffectivityInStore(userId,
                                                                            assetManagerGUID,
                                                                            assetManagerName,
                                                                            relationshipGUID,
                                                                            forLineage,
                                                                            forDuplicateProcessing,
                                                                            effectiveFrom,
                                                                            effectiveTo,
                                                                            effectiveTime);
        }
        else
        {
            openMetadataStoreClient.updateRelatedElementsEffectivityInStore(userId,
                                                                            relationshipGUID,
                                                                            forLineage,
                                                                            forDuplicateProcessing,
                                                                            effectiveFrom,
                                                                            effectiveTo,
                                                                            effectiveTime);
        }
    }


    /**
     * Delete a relationship between two metadata elements.
     *
     * @param relationshipGUID unique identifier of the relationship to delete
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to delete this relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void deleteRelatedElementsInStore(String  relationshipGUID,
                                             Date    effectiveTime) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName = "deleteRelatedElementsInStore";

        if (synchronizationDirection == SynchronizationDirection.TO_THIRD_PARTY)
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }

        if (assetManagerIsHome)
        {
            openMetadataStoreClient.deleteRelatedElementsInStore(userId,
                                                                 assetManagerGUID,
                                                                 assetManagerName,
                                                                 relationshipGUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 effectiveTime);
        }
        else
        {
            openMetadataStoreClient.deleteRelatedElementsInStore(userId,
                                                                 relationshipGUID,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 effectiveTime);
        }
    }
}
