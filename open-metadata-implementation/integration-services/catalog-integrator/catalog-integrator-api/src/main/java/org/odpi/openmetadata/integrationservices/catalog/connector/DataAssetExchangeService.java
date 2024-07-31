/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.catalog.connector;

import org.odpi.openmetadata.accessservices.assetmanager.client.exchange.DataAssetExchangeClient;

import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.DataAssetElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ExternalIdentifierProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelationshipElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataAssetProperties;
import org.odpi.openmetadata.integrationservices.catalog.ffdc.CatalogIntegratorErrorCode;

import java.util.Date;
import java.util.List;


/**
 * DataAssetExchangeService is the context for managing data assets and associated elements such as schemas.
 */
public class DataAssetExchangeService extends SchemaExchangeService
{
    private final DataAssetExchangeClient  dataAssetExchangeClient;

    /**
     * Create a new client to exchange data asset content with open metadata.
     *
     * @param dataAssetExchangeClient client for exchange requests
     * @param permittedSynchronization direction(s) that metadata can flow
     * @param userId integration daemon's userId
     * @param assetManagerGUID unique identifier of the software server capability for the asset manager
     * @param assetManagerName unique name of the software server capability for the asset manager
     * @param connectorName name of the connector using this context
     * @param auditLog logging destination
     */
    DataAssetExchangeService(DataAssetExchangeClient  dataAssetExchangeClient,
                             PermittedSynchronization permittedSynchronization,
                             String                   userId,
                             String                   assetManagerGUID,
                             String                   assetManagerName,
                             String                   connectorName,
                             AuditLog                 auditLog)
    {
        super (dataAssetExchangeClient, permittedSynchronization, userId, assetManagerGUID, assetManagerName, connectorName, auditLog);

        this.dataAssetExchangeClient = dataAssetExchangeClient;
    }


    /* ======================================================================================
     * The Asset entity is the top level element to describe an implemented data asset such as a data store or data set.
     */

    /**
     * Create a new metadata element to represent the root of an asset.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param assetProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDataAsset(boolean                      assetManagerIsHome,
                                  ExternalIdentifierProperties externalIdentifierProperties,
                                  DataAssetProperties          assetProperties) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName = "createDataAsset";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            return dataAssetExchangeClient.createDataAsset(userId,
                                                           assetManagerGUID,
                                                           assetManagerName,
                                                           assetManagerIsHome,
                                                           externalIdentifierProperties,
                                                           assetProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Create a new metadata element to represent an asset using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new asset.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this process
     * @param templateGUID unique identifier of the metadata element to copy
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDataAssetFromTemplate(boolean                      assetManagerIsHome,
                                              String                       templateGUID,
                                              ExternalIdentifierProperties externalIdentifierProperties,
                                              TemplateProperties           templateProperties) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        final String methodName = "createDataAssetFromTemplate";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            return dataAssetExchangeClient.createDataAssetFromTemplate(userId,
                                                                       assetManagerGUID,
                                                                       assetManagerName,
                                                                       assetManagerIsHome,
                                                                       templateGUID,
                                                                       externalIdentifierProperties,
                                                                       templateProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Update the metadata element representing an asset.
     *
     * @param assetGUID unique identifier of the metadata element to update
     * @param assetExternalIdentifier unique identifier of the asset in the external asset manager
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param assetProperties new properties for this element
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateDataAsset(String              assetGUID,
                                String              assetExternalIdentifier,
                                boolean             isMergeUpdate,
                                DataAssetProperties assetProperties,
                                Date                effectiveTime) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String methodName = "updateDataAsset";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            dataAssetExchangeClient.updateDataAsset(userId,
                                                    assetManagerGUID,
                                                    assetManagerName,
                                                    assetGUID,
                                                    assetExternalIdentifier,
                                                    isMergeUpdate,
                                                    assetProperties,
                                                    effectiveTime,
                                                    forLineage,
                                                    forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Update the zones for the asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Asset Manager OMAS).
     *
     * @param assetGUID unique identifier of the metadata element to publish
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void publishDataAsset(String assetGUID,
                                 Date   effectiveTime) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        final String methodName = "publishDataAsset";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            dataAssetExchangeClient.publishDataAsset(userId,
                                                     assetManagerGUID,
                                                     assetManagerName,
                                                     assetGUID,
                                                     effectiveTime,
                                                     forLineage,
                                                     forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Update the zones for the asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Asset Manager OMAS.  This is the setting when the database is first created).
     *
     * @param assetGUID unique identifier of the metadata element to withdraw
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void withdrawDataAsset(String assetGUID,
                                  Date   effectiveTime) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        final String methodName = "withdrawDataAsset";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            dataAssetExchangeClient.withdrawDataAsset(userId,
                                                      assetManagerGUID,
                                                      assetManagerName,
                                                      assetGUID,
                                                      effectiveTime,
                                                      forLineage,
                                                      forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove the metadata element representing an asset.  This will delete the asset and all anchored
     * elements such as schema and comments.
     *
     * @param assetGUID unique identifier of the metadata element to remove
     * @param assetExternalIdentifier unique identifier of the asset in the external asset manager
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeDataAsset(String assetGUID,
                                String assetExternalIdentifier,
                                Date   effectiveTime) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        final String methodName = "removeDataAsset";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            dataAssetExchangeClient.removeDataAsset(userId,
                                                    assetManagerGUID,
                                                    assetManagerName,
                                                    assetGUID,
                                                    assetExternalIdentifier,
                                                    effectiveTime,
                                                    forLineage,
                                                    forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Classify the asset to indicate that it can be used as reference data.
     *
     * @param assetGUID unique identifier of the metadata element to update
     * @param assetExternalIdentifier unique identifier of the asset in the external asset manager
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setDataAssetAsReferenceData(String assetGUID,
                                            String assetExternalIdentifier,
                                            Date   effectiveTime) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName = "setDataAssetAsReferenceData";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            dataAssetExchangeClient.setDataAssetAsReferenceData(userId,
                                                                assetManagerGUID,
                                                                assetManagerName,
                                                                assetGUID,
                                                                assetExternalIdentifier,
                                                                effectiveTime,
                                                                forLineage,
                                                                forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove the reference data designation from the asset.
     *
     * @param assetGUID unique identifier of the metadata element to update
     * @param assetExternalIdentifier unique identifier of the asset in the external asset manager
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearDataAssetAsReferenceData(String assetGUID,
                                              String assetExternalIdentifier,
                                              Date   effectiveTime) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName = "clearDataAssetAsReferenceData";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            dataAssetExchangeClient.clearDataAssetAsReferenceData(userId,
                                                                  assetManagerGUID,
                                                                  assetManagerName,
                                                                  assetGUID,
                                                                  assetExternalIdentifier,
                                                                  effectiveTime,
                                                                  forLineage,
                                                                  forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Link two asset together.
     * Use information from the relationship type definition to ensure the fromAssetGUID and toAssetGUID are the right way around.
     *
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param relationshipTypeName type name of relationship to create
     * @param fromAssetGUID unique identifier of the asset at end 1 of the relationship
     * @param toAssetGUID unique identifier of the asset at end 2 of the relationship
     * @param relationshipProperties unique identifier for this relationship
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String setupRelatedDataAsset(boolean                assetManagerIsHome,
                                        String                 relationshipTypeName,
                                        String                 fromAssetGUID,
                                        String                 toAssetGUID,
                                        RelationshipProperties relationshipProperties,
                                        Date                   effectiveTime) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName = "setupRelatedDataAsset";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            return dataAssetExchangeClient.setupRelatedDataAsset(userId,
                                                                 assetManagerGUID,
                                                                 assetManagerName,
                                                                 assetManagerIsHome,
                                                                 relationshipTypeName,
                                                                 fromAssetGUID,
                                                                 toAssetGUID,
                                                                 relationshipProperties,
                                                                 effectiveTime,
                                                                 forLineage,
                                                                 forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Retrieve the relationship between two elements.
     *
     * @param relationshipTypeName type name of relationship to create
     * @param fromAssetGUID unique identifier of the asset at end 1 of the relationship
     * @param toAssetGUID unique identifier of the asset at end 2 of the relationship
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelationshipElement getAssetRelationship(String relationshipTypeName,
                                                    String fromAssetGUID,
                                                    String toAssetGUID,
                                                    Date   effectiveTime) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return dataAssetExchangeClient.getAssetRelationship(userId,
                                                            assetManagerGUID,
                                                            assetManagerName,
                                                            relationshipTypeName,
                                                            fromAssetGUID,
                                                            toAssetGUID,
                                                            effectiveTime,
                                                            forLineage,
                                                            forDuplicateProcessing);
    }


    /**
     * Update relationship between two elements.
     *
     * @param relationshipTypeName type name of relationship to update
     * @param relationshipGUID unique identifier of the relationship
     * @param relationshipProperties description and/or purpose of the relationship
     * @param isMergeUpdate should the new properties be merged with the existing properties, or replace them entirely
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void   updateAssetRelationship(String                 relationshipTypeName,
                                          String                 relationshipGUID,
                                          boolean                isMergeUpdate,
                                          RelationshipProperties relationshipProperties,
                                          Date                   effectiveTime) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName = "updateAssetRelationship";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            dataAssetExchangeClient.updateAssetRelationship(userId,
                                                            assetManagerGUID,
                                                            assetManagerName,
                                                            relationshipTypeName,
                                                            relationshipGUID,
                                                            isMergeUpdate,
                                                            relationshipProperties,
                                                            effectiveTime,
                                                            forLineage,
                                                            forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove the relationship between two elements.
     *
     * @param relationshipTypeName type name of relationship to delete
     * @param relationshipGUID unique identifier of the relationship
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearAssetRelationship(String relationshipTypeName,
                                       String relationshipGUID,
                                       Date   effectiveTime) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName = "clearAssetRelationship";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            dataAssetExchangeClient.clearAssetRelationship(userId,
                                                           assetManagerGUID,
                                                           assetManagerName,
                                                           relationshipTypeName,
                                                           relationshipGUID,
                                                           effectiveTime,
                                                           forLineage,
                                                           forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Retrieve the requested relationships linked from a specific element at end 2.
     *
     * @param relationshipTypeName type name of relationship to delete
     * @param fromAssetGUID unique identifier of the asset at end 1 of the relationship
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<RelationshipElement> getRelatedAssetsAtEnd2(String relationshipTypeName,
                                                            String fromAssetGUID,
                                                            int    startingFrom,
                                                            int    pageSize,
                                                            Date   effectiveTime) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        return dataAssetExchangeClient.getRelatedAssetsAtEnd2(userId,
                                                              assetManagerGUID,
                                                              assetManagerName,
                                                              relationshipTypeName,
                                                              fromAssetGUID,
                                                              startingFrom,
                                                              pageSize,
                                                              effectiveTime,
                                                              forLineage,
                                                              forDuplicateProcessing);
    }


    /**
     * Retrieve the relationships linked from a specific element at end 2 of the relationship.
     *
     * @param relationshipTypeName type name of relationship to delete
     * @param toAssetGUID unique identifier of the asset at end 2 of the relationship
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<RelationshipElement> getRelatedAssetsAtEnd1(String relationshipTypeName,
                                                            String toAssetGUID,
                                                            int    startingFrom,
                                                            int    pageSize,
                                                            Date   effectiveTime) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        return dataAssetExchangeClient.getRelatedAssetsAtEnd1(userId,
                                                              assetManagerGUID,
                                                              assetManagerName,
                                                              relationshipTypeName,
                                                              toAssetGUID,
                                                              startingFrom,
                                                              pageSize,
                                                              effectiveTime,
                                                              forLineage,
                                                              forDuplicateProcessing);
    }



    /**
     * Retrieve the list of asset metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DataAssetElement> findDataAssets(String searchString,
                                                 int    startFrom,
                                                 int    pageSize,
                                                 Date   effectiveTime) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        return dataAssetExchangeClient.findDataAssets(userId,
                                                      assetManagerGUID,
                                                      assetManagerName,
                                                      searchString,
                                                      startFrom,
                                                      pageSize,
                                                      effectiveTime,
                                                      forLineage,
                                                      forDuplicateProcessing);
    }


    /**
     * Retrieve the list of asset metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DataAssetElement> getDataAssetsByName(String name,
                                                      int    startFrom,
                                                      int    pageSize,
                                                      Date   effectiveTime) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        return dataAssetExchangeClient.getDataAssetsByName(userId,
                                                           assetManagerGUID,
                                                           assetManagerName,
                                                           name,
                                                           startFrom,
                                                           pageSize,
                                                           effectiveTime,
                                                           forLineage,
                                                           forDuplicateProcessing);
    }


    /**
     * Retrieve the list of assets created on behalf of the named asset manager.
     *
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DataAssetElement> getDataAssetsForAssetManager(int  startFrom,
                                                               int  pageSize,
                                                               Date effectiveTime) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        return dataAssetExchangeClient.getDataAssetsForAssetManager(userId,
                                                                    assetManagerGUID,
                                                                    assetManagerName,
                                                                    startFrom,
                                                                    pageSize,
                                                                    effectiveTime,
                                                                    forLineage,
                                                                    forDuplicateProcessing);
    }


    /**
     * Retrieve the asset metadata element with the supplied unique identifier.
     *
     * @param dataAssetGUID unique identifier of the requested metadata element
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DataAssetElement getDataAssetByGUID(String dataAssetGUID,
                                               Date   effectiveTime) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        return dataAssetExchangeClient.getDataAssetByGUID(userId,
                                                          assetManagerGUID,
                                                          assetManagerName,
                                                          dataAssetGUID,
                                                          effectiveTime,
                                                          forLineage,
                                                          forDuplicateProcessing);
    }
}
