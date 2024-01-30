/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.client;

import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.rest.ConnectionsResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.Connections;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementBase;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;


/**
 * ConnectedConnections provides the open metadata concrete implementation of the
 * Open Connector Framework (OCF) Connections abstract class.
 * Its role is to query the property servers (metadata repository cohort) to extract connections
 * related to the connected asset.
 */
public class ConnectedConnections extends Connections
{
    private String                 serviceName;
    private String                 serverName;
    private String                 userId;
    private String                 platformURLRoot;
    private String                 assetGUID;
    private OCFRESTClient          restClient;

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Typical constructor creates an iterator with the supplied list of elements.
     *
     * @param serviceName calling service
     * @param serverName  name of the server.
     * @param userId user id to use on server calls.
     * @param platformURLRoot url root of the server to use.
     * @param assetGUID unique identifier of the asset.
     * @param totalElementCount the total number of elements to process.  A negative value is converted to 0.
     * @param maxCacheSize maximum number of elements that should be retrieved from the property server and
     *                     cached in the element list at any one time.  If a number less than one is supplied, 1 is used.
     * @param restClient client to call REST API
     */
    ConnectedConnections(String                 serviceName,
                         String                 serverName,
                         String                 userId,
                         String                 platformURLRoot,
                         String                 assetGUID,
                         int                    totalElementCount,
                         int                    maxCacheSize,
                         OCFRESTClient          restClient)
    {
        super(totalElementCount, maxCacheSize);

        this.serviceName     = serviceName;
        this.serverName      = serverName;
        this.userId          = userId;
        this.platformURLRoot   = platformURLRoot;
        this.assetGUID       = assetGUID;
        this.restClient      = restClient;
    }


    /**
     * Copy/clone constructor.  Used to reset iterator element pointer to 0;
     *
     * @param template type-specific iterator to copy; null to create an empty iterator
     */
    private ConnectedConnections(ConnectedConnections template)
    {
        super(template);

        if (template != null)
        {
            this.serviceName    = template.serviceName;
            this.serverName     = template.serverName;
            this.userId         = template.userId;
            this.platformURLRoot  = template.platformURLRoot;
            this.assetGUID      = template.assetGUID;
            this.restClient     = template.restClient;
        }
    }


    /**
     * Clones this iterator.
     *
     * @return new cloned object.
     */
    @Override
    protected Connections cloneIterator()
    {
        return new ConnectedConnections(this);
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
    protected  List<ElementBase> getCachedList(int  cacheStartPointer,
                                               int  maximumSize) throws PropertyServerException
    {
        final String   methodName = "Connections.getCachedList";
        final String   urlTemplate = "/servers/{0}/open-metadata/framework-services/{1}/connected-asset/users/{2}/assets/{3}/connections?elementStart={4}&maxElements={5}";

        RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

        try
        {
            ConnectionsResponse restResult = restClient.callOCFConnectionsGetRESTCall(methodName,
                                                                                      platformURLRoot + urlTemplate,
                                                                                      serverName,
                                                                                      serviceName,
                                                                                      userId,
                                                                                      assetGUID,
                                                                                      cacheStartPointer,
                                                                                      maximumSize);

            restExceptionHandler.detectAndThrowInvalidParameterException(restResult);
            restExceptionHandler.detectAndThrowUserNotAuthorizedException(restResult);
            restExceptionHandler.detectAndThrowPropertyServerException(restResult);

            List<Connection>  beans = restResult.getList();
            if ((beans == null) || (beans.isEmpty()))
            {
                return null;
            }
            else
            {
                List<ElementBase>   resultList = new ArrayList<>();

                for (Connection  bean : beans)
                {
                    if (bean != null)
                    {
                        resultList.add(bean);
                    }
                }

                return resultList;
            }
        }
        catch (Exception  error)
        {
            restExceptionHandler.handleUnexpectedException(error, methodName, serverName, platformURLRoot);
        }

        return null;
    }
}
