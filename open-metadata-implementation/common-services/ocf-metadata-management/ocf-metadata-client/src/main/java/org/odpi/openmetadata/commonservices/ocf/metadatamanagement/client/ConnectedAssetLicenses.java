/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.client;

import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest.LicensesResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetDescriptor;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetLicense;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetLicenses;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetPropertyBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.License;

import java.util.ArrayList;
import java.util.List;


/**
 * ConnectedAssetLicenses provides the open metadata concrete implementation of the
 * Open Connector Framework (OCF) AssetLicenses abstract class.
 * Its role is to query the property servers (metadata repository cohort) to extract licenses
 * related to the connected asset.
 */
public class ConnectedAssetLicenses extends AssetLicenses
{
    private static final long    serialVersionUID = 1L;

    private String                 serviceName;
    private String                 serverName;
    private String                 userId;
    private String                 omasServerURL;
    private String                 assetGUID;
    private ConnectedAssetUniverse connectedAsset;
    private OCFRESTClient          restClient;



    /**
     * Typical constructor creates an iterator with the supplied list of elements.
     *
     * @param serviceName name of calling service
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
    ConnectedAssetLicenses(String                 serviceName,
                           String                 serverName,
                           String                 userId,
                           String                 omasServerURL,
                           String                 assetGUID,
                           ConnectedAssetUniverse parentAsset,
                           int                    totalElementCount,
                           int                    maxCacheSize,
                           OCFRESTClient          restClient)
    {
        super(parentAsset, totalElementCount, maxCacheSize);

        this.serviceName     = serviceName;
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
    private ConnectedAssetLicenses(ConnectedAssetUniverse parentAsset, ConnectedAssetLicenses template)
    {
        super(parentAsset, template);

        if (template != null)
        {
            this.serviceName    = template.serviceName;
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
    @Override
    protected  AssetLicenses cloneIterator(AssetDescriptor parentAsset)
    {
        return new ConnectedAssetLicenses(connectedAsset, this);
    }



    /**
     * Method implemented by a subclass that ensures the cloning process is a deep clone.
     *
     * @param parentAsset descriptor of parent asset
     * @param template object to clone
     * @return new cloned object.
     */
    @Override
    protected  AssetPropertyBase cloneElement(AssetDescriptor  parentAsset, AssetPropertyBase template)
    {
        return new AssetLicense(parentAsset, (AssetLicense)template);
    }


    /**
     * Method implemented by subclass to retrieve the next cached list of elements.
     *
     * @param cacheStartPointer where to start the cache.
     * @param maximumSize maximum number of elements in the cache.
     * @return list of elements corresponding to the supplied cache pointers.
     * @throws PropertyServerException there is a problem retrieving elements from the property (metadata) server.
     */
    @Override
    protected  List<AssetPropertyBase> getCachedList(int  cacheStartPointer,
                                                     int  maximumSize) throws PropertyServerException
    {
        final String   methodName = "AssetLicenses.getCachedList";
        final String   urlTemplate = "/servers/{0}/open-metadata/common-services/{1}/connected-asset/users/{2}/assets/{3}/licenses?elementStart={4}&maxElements={5}";

        RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

        try
        {
            LicensesResponse restResult = restClient.callLicensesGetRESTCall(methodName,
                                                                             omasServerURL + urlTemplate,
                                                                             serverName,
                                                                             serviceName,
                                                                             userId,
                                                                             assetGUID,
                                                                             cacheStartPointer,
                                                                             maximumSize);

            restExceptionHandler.detectAndThrowInvalidParameterException(restResult);
            restExceptionHandler.detectAndThrowUserNotAuthorizedException(restResult);
            restExceptionHandler.detectAndThrowPropertyServerException(restResult);

            List<License>  beans = restResult.getList();
            if ((beans == null) || (beans.isEmpty()))
            {
                return null;
            }
            else
            {
                List<AssetPropertyBase>   resultList = new ArrayList<>();

                for (License  bean : beans)
                {
                    if (bean != null)
                    {
                        resultList.add(new AssetLicense(connectedAsset, bean));
                    }
                }

                return resultList;
            }
        }
        catch (Throwable  error)
        {
            restExceptionHandler.handleUnexpectedException(error, methodName, serverName, omasServerURL);
        }

        return null;
    }
}
