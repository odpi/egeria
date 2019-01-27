/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.connectedasset.client;


import org.odpi.openmetadata.accessservices.connectedasset.ffdc.ConnectedAssetErrorCode;
import org.odpi.openmetadata.accessservices.connectedasset.rest.RelatedMediaReferencesResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetDescriptor;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetRelatedMediaReference;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetRelatedMediaReferences;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetPropertyBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.RelatedMediaReference;

import java.util.ArrayList;
import java.util.List;


/**
 * ConnectedAssetRelatedMediaReferences provides the open metadata concrete implementation of the
 * Open Connector Framework (OCF) AssetRelatedMediaReferences abstract class.
 * Its role is to query the property servers (metadata repository cohort) to extract related media references
 * related to the connected asset.
 */
public class ConnectedAssetRelatedMediaReferences extends AssetRelatedMediaReferences
{
    private String                 serverName;
    private String                 userId;
    private String                 omasServerURL;
    private String                 assetGUID;
    private ConnectedAssetUniverse connectedAsset;
    private RESTClient             restClient;



    /**
     * Typical constructor creates an iterator with the supplied list of elements.
     *
     * @param serverName  name of the server.
     * @param userId user id to use on server calls.
     * @param omasServerURL url root of the server to use.
     * @param assetGUID unique identifier of the asset.
     * @param parentAsset descriptor of parent asset.
     * @param totalElementCount the total number of elements to process.  A negative value is converted to 0.
     * @param maxCacheSize maximum number of elements that should be retrieved from the property server and
     *                     cached in the element list at any one time.  If a number less than one is supplied, 1 is used.
     * @param restClient client to call REST API
     */
    ConnectedAssetRelatedMediaReferences(String                 serverName,
                                         String                 userId,
                                         String                 omasServerURL,
                                         String                 assetGUID,
                                         ConnectedAssetUniverse parentAsset,
                                         int                    totalElementCount,
                                         int                    maxCacheSize,
                                         RESTClient             restClient)
    {
        super(parentAsset, totalElementCount, maxCacheSize);

        this.serverName      = serverName;
        this.userId          = userId;
        this.omasServerURL   = omasServerURL;
        this.assetGUID       = assetGUID;
        this.connectedAsset  = parentAsset;
        this.restClient      = restClient;
    }


    /**
     * Copy/clone constructor.  Used to reset iterator element pointer to 0;
     *
     * @param parentAsset descriptor of parent asset
     * @param template type-specific iterator to copy; null to create an empty iterator
     */
    private ConnectedAssetRelatedMediaReferences(ConnectedAssetUniverse parentAsset, ConnectedAssetRelatedMediaReferences template)
    {
        super(parentAsset, template);

        if (template != null)
        {
            this.serverName     = template.serverName;
            this.userId         = template.userId;
            this.omasServerURL  = template.omasServerURL;
            this.assetGUID      = template.assetGUID;
            this.connectedAsset = parentAsset;
            this.restClient     = template.restClient;
        }
    }


    /**
     * Clones this iterator.
     *
     * @param parentAsset descriptor of parent asset
     * @return new cloned object.
     */
    protected  AssetRelatedMediaReferences cloneIterator(AssetDescriptor parentAsset)
    {
        return new ConnectedAssetRelatedMediaReferences(connectedAsset, this);
    }



    /**
     * Method implemented by a subclass that ensures the cloning process is a deep clone.
     *
     * @param parentAsset descriptor of parent asset
     * @param template object to clone
     * @return new cloned object.
     */
    protected  AssetPropertyBase cloneElement(AssetDescriptor  parentAsset, AssetPropertyBase template)
    {
        return new AssetRelatedMediaReference(parentAsset, (AssetRelatedMediaReference)template);
    }


    /**
     * Method implemented by subclass to retrieve the next cached list of elements.
     *
     * @param cacheStartPointer where to start the cache.
     * @param maximumSize maximum number of elements in the cache.
     * @return list of elements corresponding to the supplied cache pointers.
     * @throws PropertyServerException there is a problem retrieving elements from the property (metadata) server.
     */
    protected  List<AssetPropertyBase> getCachedList(int  cacheStartPointer,
                                                     int  maximumSize) throws PropertyServerException
    {
        final String   methodName = "AssetRelatedMediaReferences.getCachedList";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/connected-asset/users/{1}/assets/{2}/related-media-references?elementStart={3}&maxElements={4}";

        InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
        RESTExceptionHandler    restExceptionHandler    = new RESTExceptionHandler();

        invalidParameterHandler.validateOMASServerURL(omasServerURL, methodName);

        try
        {
            RelatedMediaReferencesResponse restResult = restClient.callRelatedMediaReferenceGetRESTCall(methodName,
                                                                                                        omasServerURL + urlTemplate,
                                                                                                        userId,
                                                                                                        assetGUID,
                                                                                                        cacheStartPointer,
                                                                                                        maximumSize);

            restExceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
            restExceptionHandler.detectAndThrowUnrecognizedAssetGUIDException(methodName, restResult);
            restExceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
            restExceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

            List<RelatedMediaReference>  beans = restResult.getList();
            if ((beans == null) || (beans.isEmpty()))
            {
                return null;
            }
            else
            {
                List<AssetPropertyBase>   resultList = new ArrayList<>();

                for (RelatedMediaReference  bean : beans)
                {
                    if (bean != null)
                    {
                        resultList.add(new AssetRelatedMediaReference(connectedAsset, bean));
                    }
                }

                return resultList;
            }
        }
        catch (Throwable  error)
        {
            ConnectedAssetErrorCode errorCode = ConnectedAssetErrorCode.EXCEPTION_RESPONSE_FROM_API;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(error.getClass().getName(),
                                                                                                     methodName,
                                                                                                     omasServerURL,
                                                                                                     error.getMessage());

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction(),
                                              error);
        }
    }
}
