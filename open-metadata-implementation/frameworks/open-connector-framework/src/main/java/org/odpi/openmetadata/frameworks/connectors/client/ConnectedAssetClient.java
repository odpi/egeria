/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.client;


import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.api.ConnectorFactoryInterface;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;


/**
 * ConnectedAssetClient provides calls for callers that work with connectors.
 * In particular, it manages the retrieval of connections for assets, and the creation of connectors.
 */
public abstract class ConnectedAssetClient implements ConnectorFactoryInterface
{
    protected final String   serverName;               /* Initialized in constructor */
    protected final String   serverPlatformURLRoot;    /* Initialized in constructor */
    protected final AuditLog auditLog;                 /* Initialized in constructor */
    protected final int      maxPageSize;              /* Initialized in constructor */

    protected PropertyHelper propertyHelper = new PropertyHelper();


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OCF REST services
     * @param maxPageSize maximum page size for this process
     * @param auditLog destination for log messages
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ConnectedAssetClient(String   serverName,
                                String   serverPlatformURLRoot,
                                int      maxPageSize,
                                AuditLog auditLog) throws InvalidParameterException
    {
        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.auditLog = auditLog;
        this.maxPageSize = maxPageSize;
    }


    /**
     * Returns the unique identifier corresponding to the supplied connection.
     *
     * @param userId calling user
     * @param connection   the connection object that contains the properties needed to create the connection.
     *
     * @return guid
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     */
    public abstract String saveConnection(String     userId,
                                          Connection connection) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException,
                                                                        ConnectionCheckedException,
                                                                        ConnectorCheckedException;


    /**
     * Returns the unique identifier corresponding to the supplied connection.
     *
     * @param userId calling user
     * @param assetGUID the unique identifier of an asset to attach the connection to
     * @param connection   the connection object that contains the properties needed to create the connection.
     *
     * @return guid
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     */
    public abstract String saveConnection(String     userId,
                                          String     assetGUID,
                                          Connection connection) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException,
                                                                        ConnectionCheckedException,
                                                                        ConnectorCheckedException;


    /*
     * ===============================================
     * ConnectorFactoryInterface
     * ===============================================
     */


    /**
     * Returns the connector corresponding to the supplied connection name.
     *
     * @param userId           userId of user making request.
     * @param connectionName   this may be the qualifiedName or displayName of the connection.
     *
     * @return   connector instance - or null if there is no connection
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public abstract Connector getConnectorByName(String userId,
                                                 String connectionName) throws InvalidParameterException,
                                                                               ConnectionCheckedException,
                                                                               ConnectorCheckedException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException;


    /**
     * Returns the connector corresponding to the supplied asset GUID.
     *
     * @param userId       userId of user making request.
     * @param assetGUID   the unique id for the asset within the metadata repository.
     *
     * @return    connector instance - or null if there is no connection
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public abstract Connector getConnectorForAsset(String   userId,
                                                   String   assetGUID) throws InvalidParameterException,
                                                                              ConnectionCheckedException,
                                                                              ConnectorCheckedException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException;


    /**
     * Returns the connector corresponding to the supplied asset GUID.
     *
     * @param userId       userId of user making request.
     * @param assetGUID   the unique id for the asset within the metadata repository.
     * @param auditLog    optional logging destination
     *
     * @return    connector instance - or null if there is no connection
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public abstract Connector getConnectorForAsset(String   userId,
                                                   String   assetGUID,
                                                   AuditLog auditLog) throws InvalidParameterException,
                                                                             ConnectionCheckedException,
                                                                             ConnectorCheckedException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException;


    /**
     * Returns the connector corresponding to the supplied connection GUID.
     *
     * @param userId           userId of user making request.
     * @param connectionGUID   the unique id for the connection within the metadata repository.
     *
     * @return  connector instance - or null if there is no connection
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public abstract Connector getConnectorByGUID(String userId,
                                                 String connectionGUID) throws InvalidParameterException,
                                                                               ConnectionCheckedException,
                                                                               ConnectorCheckedException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException;


    /**
     * Returns the connector corresponding to the supplied connection.
     *
     * @param userId       userId of user making request.
     * @param connection   the connection object that contains the properties needed to create the connection.
     *
     * @return  connector instance
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws UserNotAuthorizedException connector disconnected
     */
    @Override
    public abstract Connector  getConnectorByConnection(String     userId,
                                                        Connection connection) throws InvalidParameterException,
                                                                                      ConnectionCheckedException,
                                                                                      ConnectorCheckedException,
                                                                                      UserNotAuthorizedException;
}
