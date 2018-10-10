/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.connectedasset.client;


import org.odpi.openmetadata.accessservices.connectedasset.ffdc.ConnectedAssetErrorCode;
import org.odpi.openmetadata.accessservices.connectedasset.rest.NotesResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Note;

import java.util.ArrayList;
import java.util.List;


/**
 * ConnectedAssetLikes provides the open metadata concrete implementation of the
 * Open Connector Framework (OCF) AssetLikes abstract class.
 * Its role is to query the property servers (metadata repository cohort) to extract likes
 * related to the connected asset.
 */
public class ConnectedAssetNotes extends AssetNotes
{
    private String         userId;
    private String         omasServerURL;
    private String         noteLogGUID;
    private ConnectedAsset connectedAsset;


    /**
     * Typical constructor creates an iterator with the supplied list of elements.
     *
     * @param userId user id to use on server calls.
     * @param omasServerURL url root of the server to use.
     * @param noteLogGUID unique identifier of the asset.
     * @param parentAsset descriptor of parent asset.
     * @param totalElementCount the total number of elements to process.  A negative value is converted to 0.
     * @param maxCacheSize maximum number of elements that should be retrieved from the property server and
     *                     cached in the element list at any one time.  If a number less than one is supplied, 1 is used.
     */
    ConnectedAssetNotes(String              userId,
                        String              omasServerURL,
                        String              noteLogGUID,
                        ConnectedAsset      parentAsset,
                        int                 totalElementCount,
                        int                 maxCacheSize)
    {
        super(parentAsset, totalElementCount, maxCacheSize);

        this.userId          = userId;
        this.omasServerURL   = omasServerURL;
        this.noteLogGUID     = noteLogGUID;
        this.connectedAsset  = parentAsset;
    }


    /**
     * Copy/clone constructor.  Used to reset iterator element pointer to 0;
     *
     * @param parentAsset descriptor of parent asset
     * @param template type-specific iterator to copy; null to create an empty iterator
     */
    private ConnectedAssetNotes(ConnectedAsset   parentAsset, ConnectedAssetNotes template)
    {
        super(parentAsset, template);

        if (template != null)
        {
            this.userId         = template.userId;
            this.omasServerURL  = template.omasServerURL;
            this.noteLogGUID    = template.noteLogGUID;
            this.connectedAsset = parentAsset;
        }
    }


    /**
     * Clones this iterator.
     *
     * @param parentAsset descriptor of parent asset
     * @return new cloned object.
     */
    protected  AssetNotes cloneIterator(AssetDescriptor parentAsset)
    {
        return new ConnectedAssetNotes(connectedAsset, this);
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
        return new AssetNote(parentAsset, (AssetNote)template);
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
        final String   methodName = "AssetNotes.getCachedList";
        final String   urlTemplate = "/open-metadata/access-services/connected-asset/users/{0}/note-logs/{1}/notes?elementStart={2}&maxElements={3}";

        connectedAsset.validateOMASServerURL(methodName);

        try
        {
            NotesResponse restResult = (NotesResponse)connectedAsset.callGetRESTCall(methodName,
                                                                                     NotesResponse.class,
                                                                                     omasServerURL + urlTemplate,
                                                                                     userId,
                                                                                     noteLogGUID,
                                                                                     cacheStartPointer,
                                                                                     maximumSize);

            connectedAsset.detectAndThrowInvalidParameterException(methodName, restResult);
            connectedAsset.detectAndThrowUnrecognizedAssetGUIDException(methodName, restResult);
            connectedAsset.detectAndThrowUserNotAuthorizedException(methodName, restResult);
            connectedAsset.detectAndThrowPropertyServerException(methodName, restResult);

            List<Note>  beans = restResult.getList();
            if ((beans == null) || (beans.isEmpty()))
            {
                return null;
            }
            else
            {
                List<AssetPropertyBase>   resultList = new ArrayList<>();

                for (Note  bean : beans)
                {
                    if (bean != null)
                    {
                        resultList.add(new AssetNote(connectedAsset, bean));
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
