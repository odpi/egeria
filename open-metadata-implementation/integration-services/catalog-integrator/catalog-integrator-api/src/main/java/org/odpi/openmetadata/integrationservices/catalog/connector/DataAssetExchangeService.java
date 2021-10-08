/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.catalog.connector;

import org.odpi.openmetadata.accessservices.assetmanager.client.DataAssetExchangeClient;

import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.DataAssetElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.integrationservices.catalog.ffdc.CatalogIntegratorErrorCode;

import java.util.List;
import java.util.Map;


/**
 * DataAssetExchangeService is the context for managing data assets and associated elements such as schemas.
 */
public class DataAssetExchangeService extends SchemaExchangeService
{
    private DataAssetExchangeClient  dataAssetExchangeClient;


    /**
     * Create a new client to exchange data asset content with open metadata.
     *
     * @param dataAssetExchangeClient client for exchange requests
     * @param synchronizationDirection direction(s) that metadata can flow
     * @param userId integration daemon's userId
     * @param assetManagerGUID unique identifier of the software server capability for the asset manager
     * @param assetManagerName unique name of the software server capability for the asset manager
     * @param connectorName name of the connector using this context
     * @param auditLog logging destination
     */
    DataAssetExchangeService(DataAssetExchangeClient  dataAssetExchangeClient,
                             SynchronizationDirection synchronizationDirection,
                             String                   userId,
                             String                   assetManagerGUID,
                             String                   assetManagerName,
                             String                   connectorName,
                             AuditLog                 auditLog)
    {
        super (dataAssetExchangeClient, synchronizationDirection,userId, assetManagerGUID, assetManagerName, connectorName, auditLog);

        this.dataAssetExchangeClient = dataAssetExchangeClient;
    }




    /* ======================================================================================
     * The Asset entity is the top level element to describe an implemented data asset such as a data store or data set.
     */

    /**
     * Create a new metadata element to represent the root of an asset.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
     * @param assetExternalIdentifier unique identifier of the asset in the external asset manager
     * @param assetExternalIdentifierName name of property for the external identifier in the external asset manager
     * @param assetExternalIdentifierUsage optional usage description for the external identifier when calling the external asset manager
     * @param assetExternalIdentifierKeyPattern  pattern for the external identifier within the external asset manager (default is LOCAL_KEY)
     * @param mappingProperties additional properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param assetProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDataAsset(boolean             assetManagerIsHome,
                                  String              assetExternalIdentifier,
                                  String              assetExternalIdentifierName,
                                  String              assetExternalIdentifierUsage,
                                  KeyPattern          assetExternalIdentifierKeyPattern,
                                  Map<String, String> mappingProperties,
                                  DataAssetProperties assetProperties) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName = "createDataAsset";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            return dataAssetExchangeClient.createDataAsset(userId,
                                                           assetManagerGUID,
                                                           assetManagerName,
                                                           assetManagerIsHome,
                                                           assetExternalIdentifier,
                                                           assetExternalIdentifierName,
                                                           assetExternalIdentifierUsage,
                                                           connectorName,
                                                           assetExternalIdentifierKeyPattern,
                                                           mappingProperties,
                                                           assetProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     * @param assetExternalIdentifier unique identifier of the asset in the external asset manager
     * @param assetExternalIdentifierName name of property for the external identifier in the external asset manager
     * @param assetExternalIdentifierUsage optional usage description for the external identifier when calling the external asset manager
     * @param assetExternalIdentifierKeyPattern pattern for the external identifier within the external asset manager (default is LOCAL_KEY)
     * @param mappingProperties additional properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDataAssetFromTemplate(boolean             assetManagerIsHome,
                                              String              templateGUID,
                                              String              assetExternalIdentifier,
                                              String              assetExternalIdentifierName,
                                              String              assetExternalIdentifierUsage,
                                              KeyPattern          assetExternalIdentifierKeyPattern,
                                              Map<String, String> mappingProperties,
                                              TemplateProperties  templateProperties) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName = "createDataAssetFromTemplate";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            return dataAssetExchangeClient.createDataAssetFromTemplate(userId,
                                                                       assetManagerGUID,
                                                                       assetManagerName,
                                                                       assetManagerIsHome,
                                                                       templateGUID,
                                                                       assetExternalIdentifier,
                                                                       assetExternalIdentifierName,
                                                                       assetExternalIdentifierUsage,
                                                                       connectorName,
                                                                       assetExternalIdentifierKeyPattern,
                                                                       mappingProperties,
                                                                       templateProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetGUID unique identifier of the metadata element to update
     * @param assetExternalIdentifier unique identifier of the asset in the external asset manager
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param assetProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateDataAsset(String              userId,
                                String              assetManagerGUID,
                                String              assetManagerName,
                                String              assetGUID,
                                String              assetExternalIdentifier,
                                boolean             isMergeUpdate,
                                DataAssetProperties assetProperties) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String methodName = "updateDataAsset";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            dataAssetExchangeClient.updateDataAsset(userId,
                                                    assetManagerGUID,
                                                    assetManagerName,
                                                    assetGUID,
                                                    assetExternalIdentifier,
                                                    isMergeUpdate,
                                                    assetProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     * @param userId calling user
     * @param assetGUID unique identifier of the metadata element to publish
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void publishDataAsset(String userId,
                                 String assetGUID) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {
        final String methodName = "publishDataAsset";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            dataAssetExchangeClient.publishDataAsset(userId, assetManagerGUID, assetManagerName, assetGUID);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     * @param userId calling user
     * @param assetGUID unique identifier of the metadata element to withdraw
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void withdrawDataAsset(String userId,
                                  String assetGUID) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        final String methodName = "withdrawDataAsset";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            dataAssetExchangeClient.withdrawDataAsset(userId, assetManagerGUID, assetManagerName, assetGUID);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetGUID unique identifier of the metadata element to remove
     * @param assetExternalIdentifier unique identifier of the asset in the external asset manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeDataAsset(String userId,
                                String assetManagerGUID,
                                String assetManagerName,
                                String assetGUID,
                                String assetExternalIdentifier) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String methodName = "removeDataAsset";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            dataAssetExchangeClient.removeDataAsset(userId, assetManagerGUID, assetManagerName, assetGUID, assetExternalIdentifier);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetGUID unique identifier of the metadata element to update
     * @param assetExternalIdentifier unique identifier of the asset in the external asset manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setDataAssetAsReferenceData(String userId,
                                            String assetManagerGUID,
                                            String assetManagerName,
                                            String assetGUID,
                                            String assetExternalIdentifier) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName = "setDataAssetAsReferenceData";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            dataAssetExchangeClient.setDataAssetAsReferenceData(userId, assetManagerGUID, assetManagerName, assetGUID, assetExternalIdentifier);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetGUID unique identifier of the metadata element to update
     * @param assetExternalIdentifier unique identifier of the asset in the external asset manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearDataAssetAsReferenceData(String userId,
                                              String assetManagerGUID,
                                              String assetManagerName,
                                              String assetGUID,
                                              String assetExternalIdentifier) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName = "clearDataAssetAsReferenceData";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            dataAssetExchangeClient.clearDataAssetAsReferenceData(userId, assetManagerGUID, assetManagerName, assetGUID, assetExternalIdentifier);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Retrieve the list of asset metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DataAssetElement> findDataAssets(String searchString,
                                                 int    startFrom,
                                                 int    pageSize) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        return dataAssetExchangeClient.findDataAssets(userId, assetManagerGUID, assetManagerName, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of asset metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DataAssetElement> getDataAssetsByName(String name,
                                                      int    startFrom,
                                                      int    pageSize) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        return dataAssetExchangeClient.getDataAssetsByName(userId, assetManagerGUID, assetManagerName, name, startFrom, pageSize);
    }


    /**
     * Retrieve the list of assets created on behalf of the named asset manager.
     *
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DataAssetElement> getDataAssetsForAssetManager(int startFrom,
                                                               int pageSize) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        return dataAssetExchangeClient.getDataAssetsForAssetManager(userId, assetManagerGUID, assetManagerName, startFrom, pageSize);
    }


    /**
     * Retrieve the asset metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param dataAssetGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DataAssetElement getDataAssetByGUID(String userId,
                                               String assetManagerGUID,
                                               String assetManagerName,
                                               String dataAssetGUID) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return dataAssetExchangeClient.getDataAssetByGUID(userId, assetManagerGUID, assetManagerName, dataAssetGUID);
    }
}
