/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.client;


import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetUniverse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.governanceaction.properties.IntegrationReport;
import org.odpi.openmetadata.frameworks.governanceaction.properties.IntegrationReportProperties;

import java.util.Date;
import java.util.List;


/**
 * OpenIntegrationClient defines the interface of the client that implements calls to the metadata server.
 */
public abstract class OpenIntegrationClient
{
    protected final String   serverName;               /* Initialized in constructor */
    protected final String   serverPlatformURLRoot;    /* Initialized in constructor */
    protected final String   serviceURLMarker;         /* Initialized in constructor */


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     */
    public OpenIntegrationClient(String serviceURLMarker,
                                 String serverName,
                                 String serverPlatformURLRoot)
    {
        this.serviceURLMarker = serviceURLMarker;
        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
    }


    /**
     * Retrieve the unique identifier of the metadata element that represents the metadata source.
     *
     * @param userId calling user
     * @param qualifiedName unique name of the metadata source
     *
     * @return unique identifier of the metadata source
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public abstract String getMetadataSourceGUID(String  userId,
                                                 String  qualifiedName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;


    /**
     * Create a new metadata element to represent a software capability.  This describes the metadata source.
     *
     * @param userId calling user
     * @param softwareCapabilityTypeName name of software capability type to describe the metadata source
     * @param classificationName optional classification name that refines the type of the software capability.
     * @param qualifiedName unique name for the external source
     * @param deployedImplementationType type of technology
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public abstract String createMetadataSource(String userId,
                                                String softwareCapabilityTypeName,
                                                String classificationName,
                                                String qualifiedName,
                                                String deployedImplementationType) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException;



    /**
     * Create a new integration report for an element (identified by anchorGUID).
     *
     * @param userId calling user
     * @param anchorGUID element to attach the integration report to
     * @param properties properties of the report
     *
     * @throws InvalidParameterException one of the parameters is null or invalid,
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem with the metadata server.
     */
    public abstract void publishIntegrationReport(String                      userId,
                                                  String                      anchorGUID,
                                                  IntegrationReportProperties properties) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException;


    /**
     * Retrieve a specific integration report by unique identifier.
     *
     * @param userId calling user
     * @param reportGUID unique identifier of the integration report
     *
     * @return report or null
     * @throws InvalidParameterException one of the parameters is null or invalid,
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem with the metadata server.
     */
    public abstract IntegrationReport getIntegrationReport(String userId,
                                                           String reportGUID) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException;


    /**
     * Retrieve the integration reports attached to an element.  The list can be filtered by start and completion date of the report along with the
     * paging options if there are many integration reports.
     *
     * @param userId calling user
     * @param elementGUID calling user
     * @param afterCompletionDate restrict reports to those that completed after the requested time (null for any completion time).
     * @param beforeStartDate restrict reports to those that started before the requested time (null for any start time).
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of qualifying reports
     * @throws InvalidParameterException one of the parameters is null or invalid,
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem with the metadata server.
     */
    public abstract List<IntegrationReport> getIntegrationReportsForElement(String  userId,
                                                                            String  elementGUID,
                                                                            Date    afterCompletionDate,
                                                                            Date    beforeStartDate,
                                                                            int     startingFrom,
                                                                            int     maximumResults) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException;


    /**
     * Retrieve the published integration reports.  The list can be filtered by start and completion date of the report along with the
     * paging options if there are many integration reports.
     *
     * @param userId calling user
     * @param afterCompletionDate restrict reports to those that completed after the requested time (null for any completion time).
     * @param beforeStartDate restrict reports to those that started before the requested time (null for any start time).
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of qualifying reports
     * @throws InvalidParameterException one of the parameters is null or invalid,
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem with the metadata server.
     */
    public abstract List<IntegrationReport> getIntegrationReports(String  userId,
                                                                  Date    afterCompletionDate,
                                                                  Date    beforeStartDate,
                                                                  int     startingFrom,
                                                                  int     maximumResults) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException;


    /**
     * Returns the unique identifier corresponding to the supplied connection.
     *
     * @param userId calling user
     * @param connection the connection object that contains the properties needed to create the connection.
     * @return guid
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving the asset properties from the property servers.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                    the creation of a connector.
     * @throws ConnectorCheckedException  there are errors in the initialization of the connector.
     */
    public abstract String saveConnection(String    userId,
                                          Connection connection) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException,
                                                                        ConnectionCheckedException,
                                                                        ConnectorCheckedException;

    /**
     * Returns the unique identifier corresponding to the supplied connection.
     *
     * @param userId calling user
     * @param assetGUID  the unique identifier of an asset to attach the connection to
     * @param connection the connection object that contains the properties needed to create the connection.
     * @return guid
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving the asset properties from the property servers.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                    the creation of a connector.
     * @throws ConnectorCheckedException  there are errors in the initialization of the connector.
     */
    public abstract String saveConnection(String     userId,
                                          String     assetGUID,
                                          Connection connection) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException,
                                                                        ConnectionCheckedException,
                                                                        ConnectorCheckedException;

    /**
     * Returns a comprehensive collection of properties about the requested asset.
     *
     * @param userId calling user
     * @param assetGUID the unique identifier of an asset to attach the connection to
     * @return a comprehensive collection of properties about the asset.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving the asset properties from the property servers.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public abstract AssetUniverse getAssetProperties(String userId,
                                                     String assetGUID) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException;


    /**
     * Return the connector to the requested asset.
     *
     * @param userId calling user
     * @param assetGUID the unique identifier of an asset to attach the connection to
     * @param auditLog    optional logging destination
     * @return Open Connector Framework (OCF) connector
     * @throws InvalidParameterException  the asset guid is not recognized or the userId is null
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                    the creation of a connector.
     * @throws ConnectorCheckedException  there are errors in the initialization of the connector.
     * @throws UserNotAuthorizedException the user is not authorized to access the asset and/or connection needed to
     *                                    create the connector.
     * @throws PropertyServerException    there was a problem in the store whether the asset/connection properties are kept.
     */
    public abstract Connector getConnectorToAsset(String   userId,
                                                  String   assetGUID,
                                                  AuditLog auditLog) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException,
                                                                            ConnectionCheckedException,
                                                                            ConnectorCheckedException;
}
