/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.connectedasset.client;

import org.odpi.openmetadata.accessservices.connectedasset.ffdc.ConnectedAssetErrorCode;
import org.odpi.openmetadata.accessservices.connectedasset.rest.SchemaAttributesResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaAttribute;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaLink;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaType;

import java.util.ArrayList;
import java.util.List;


/**
 * ConnectedAssetSchemaAttributes provides the open metadata iterator for the attributes of a schema.
 * This iterator extends the Open Connector Framework (OCF) AssetSchemaAttributes object.
 */
public class ConnectedAssetSchemaAttributes extends AssetSchemaAttributes
{
    private String                 serverName;
    private String                 userId;
    private String                 omasServerURL;
    private String                 schemaGUID;
    private ConnectedAssetUniverse connectedAsset;
    private int                    maxCacheSize;



    /**
     * Typical constructor creates an iterator with the supplied list of elements.
     *
     * @param serverName  name of the server.
     * @param userId user id to use on server calls.
     * @param omasServerURL url root of the server to use.
     * @param schemaGUID unique identifier of the parent schema type.
     * @param parentAsset descriptor of parent asset.
     * @param totalElementCount the total number of elements to process.  A negative value is converted to 0.
     * @param maxCacheSize maximum number of elements that should be retrieved from the property server and
     *                     cached in the element list at any one time.  If a number less than one is supplied, 1 is used.
     */
    ConnectedAssetSchemaAttributes(String              serverName,
                                   String              userId,
                                   String              omasServerURL,
                                   String              schemaGUID,
                                   ConnectedAssetUniverse parentAsset,
                                   int                 totalElementCount,
                                   int                 maxCacheSize)
    {
        super(parentAsset, totalElementCount, maxCacheSize);

        this.serverName      = serverName;
        this.userId          = userId;
        this.omasServerURL   = omasServerURL;
        this.schemaGUID      = schemaGUID;
        this.connectedAsset  = parentAsset;
        this.maxCacheSize    = maxCacheSize;

    }


    /**
     * Copy/clone constructor.  Used to reset iterator element pointer to 0;
     *
     * @param parentAsset descriptor of parent asset
     * @param template type-specific iterator to copy; null to create an empty iterator
     */
    private ConnectedAssetSchemaAttributes(ConnectedAssetUniverse parentAsset, ConnectedAssetSchemaAttributes template)
    {
        super(parentAsset, template);

        if (template != null)
        {
            this.serverName      = template.serverName;
            this.userId          = template.userId;
            this.omasServerURL   = template.omasServerURL;
            this.schemaGUID      = template.schemaGUID;
            this.connectedAsset  = parentAsset;
            this.maxCacheSize    = template.maxCacheSize;
        }
    }


    /**
     * Clones this iterator.
     *
     * @param parentAsset descriptor of parent asset
     * @return new cloned object.
     */
    protected AssetSchemaAttributes cloneIterator(AssetDescriptor parentAsset)
    {
        return new ConnectedAssetSchemaAttributes(connectedAsset, this);
    }



    /**
     * Method implemented by a subclass that ensures the cloning process is a deep clone.
     *
     * @param parentAsset descriptor of parent asset
     * @param template object to clone
     * @return new cloned object.
     */
    protected AssetPropertyBase cloneElement(AssetDescriptor parentAsset, AssetPropertyBase template)
    {
        return new AssetSchemaAttribute(parentAsset, (AssetSchemaAttribute)template);
    }


    /**
     * Method implemented by subclass to retrieve the next cached list of elements.
     *
     * @param cacheStartPointer where to start the cache.
     * @param maximumSize maximum number of elements in the cache.
     * @return list of elements corresponding to the supplied cache pointers.
     * @throws PropertyServerException there is a problem retrieving elements from the property (metadata) server.
     */
    protected List<AssetPropertyBase> getCachedList(int  cacheStartPointer,
                                                    int  maximumSize) throws PropertyServerException
    {
        final String   methodName = "SchemaAttributes.getCachedList";
        final String   urlTemplate = "/open-metadata/access-services/connected-asset/users/{0}/schemas/{1}/attributes?elementStart={2}&maxElements={3}";

        connectedAsset.validateOMASServerURL(methodName);

        try
        {
            SchemaAttributesResponse restResult = (SchemaAttributesResponse)connectedAsset.callGetRESTCall(methodName,
                                                                                                           SchemaAttributesResponse.class,
                                                                                                           omasServerURL + urlTemplate,
                                                                                                           userId,
                                                                                                           schemaGUID,
                                                                                                           cacheStartPointer,
                                                                                                           maximumSize);

            connectedAsset.detectAndThrowInvalidParameterException(methodName, restResult);
            connectedAsset.detectAndThrowUnrecognizedGUIDException(methodName, restResult);
            connectedAsset.detectAndThrowUserNotAuthorizedException(methodName, restResult);
            connectedAsset.detectAndThrowPropertyServerException(methodName, restResult);

            List<SchemaAttribute> schemaAttributes = restResult.getList();
            if ((schemaAttributes == null) || (schemaAttributes.isEmpty()))
            {
                return null;
            }
            else
            {
                List<AssetPropertyBase> resultList = new ArrayList<>();

                for (SchemaAttribute schemaAttribute : schemaAttributes)
                {
                    if (schemaAttribute != null)
                    {
                        SchemaType schemaTypeBean = schemaAttribute.getAttributeType();
                        SchemaLink schemaLinkBean = schemaAttribute.getExternalAttributeType();

                        if (schemaTypeBean != null)
                        {
                            AssetSchemaType assetSchemaType = connectedAsset.getAssetSchemaType(userId, schemaTypeBean);

                            resultList.add(new AssetSchemaAttribute(connectedAsset,
                                                                    schemaAttribute,
                                                                    assetSchemaType));
                        }
                        else if (schemaLinkBean != null)
                        {
                            resultList.add(new AssetSchemaAttribute(connectedAsset,
                                                                    schemaAttribute,
                                                                    new AssetSchemaLink(schemaLinkBean)));
                        }
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
