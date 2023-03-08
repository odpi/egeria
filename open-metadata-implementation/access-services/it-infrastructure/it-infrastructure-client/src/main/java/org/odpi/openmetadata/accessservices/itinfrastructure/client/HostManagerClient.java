/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.client;

import org.odpi.openmetadata.accessservices.itinfrastructure.api.HostManagerInterface;
import org.odpi.openmetadata.accessservices.itinfrastructure.client.rest.ITInfrastructureRESTClient;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.AssetElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.HostElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.RelatedAssetElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.AssetProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.HostProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.TemplateProperties;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * HostManagerClient supports the APIs to maintain hosts and their related objects.
 */
public class HostManagerClient extends AssetManagerClientBase implements HostManagerInterface
{
    private static final String hostEntityType                = "Host";
    private static final String hostClusterEntityType         = "HostCluster";
    private static final String hostClusterMemberRelationship = "HostClusterMember";


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public HostManagerClient(String   serverName,
                             String   serverPlatformURLRoot,
                             AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public HostManagerClient(String serverName,
                             String serverPlatformURLRoot) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public HostManagerClient(String serverName,
                             String serverPlatformURLRoot,
                             String userId,
                             String password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public HostManagerClient(String   serverName,
                             String   serverPlatformURLRoot,
                             String   userId,
                             String   password,
                             AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, auditLog);
    }


    /**
     * Create a new client that is going to be used in an OMAG Server.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param restClient client that issues the REST API calls
     * @param maxPageSize maximum number of results supported by this server
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public HostManagerClient(String                     serverName,
                             String                     serverPlatformURLRoot,
                             ITInfrastructureRESTClient restClient,
                             int                        maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, restClient, maxPageSize);
    }



    /* =====================================================================================================================
     * The host describes the computer or container that provides the operating system for the platforms.
     */


    /**
     * Create a new metadata element to represent a host.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param infrastructureManagerIsHome should the host be marked as owned by the infrastructure manager so others can not update?
     * @param hostProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createHost(String         userId,
                             String         infrastructureManagerGUID,
                             String         infrastructureManagerName,
                             boolean        infrastructureManagerIsHome,
                             HostProperties hostProperties) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String methodName = "createHost";

        AssetProperties assetProperties = hostProperties.cloneToAsset();

        return super.createAsset(userId, infrastructureManagerGUID, infrastructureManagerName, infrastructureManagerIsHome, assetProperties, null, methodName);
    }


    /**
     * Create a new metadata element to represent a host using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param infrastructureManagerIsHome should the host be marked as owned by the infrastructure manager so others can not update?
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createHostFromTemplate(String             userId,
                                         String             infrastructureManagerGUID,
                                         String             infrastructureManagerName,
                                         boolean            infrastructureManagerIsHome,
                                         String             templateGUID,
                                         TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName = "createHost";

        return super.createAssetFromTemplate(userId, infrastructureManagerGUID, infrastructureManagerName, infrastructureManagerIsHome, templateGUID, templateProperties, methodName);
    }


    /**
     * Update the metadata element representing a host.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param hostGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param hostProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateHost(String         userId,
                           String         infrastructureManagerGUID,
                           String         infrastructureManagerName,
                           String         hostGUID,
                           boolean        isMergeUpdate,
                           HostProperties hostProperties) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String methodName = "updateHost";

        AssetProperties assetProperties = hostProperties.cloneToAsset();

        super.updateAsset(userId, infrastructureManagerGUID, infrastructureManagerName, hostGUID, isMergeUpdate, assetProperties, methodName);
    }


    /**
     * Create a relationship between a host and an cluster member host.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param infrastructureManagerIsHome ensure that only the infrastructure manager can update this asset
     * @param hostGUID unique identifier of the host
     * @param clusterMemberGUID unique identifier of the cluster member host
     * @param effectiveFrom time when this hosting is effective - null means immediately
     * @param effectiveTo time when this hosting is no longer effective - null means forever
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupClusterMember(String  userId,
                                   String  infrastructureManagerGUID,
                                   String  infrastructureManagerName,
                                   boolean infrastructureManagerIsHome,
                                   String  hostGUID,
                                   String  clusterMemberGUID,
                                   Date    effectiveFrom,
                                   Date    effectiveTo) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String methodName = "setupClusterMember";

        super.setupRelatedAsset(userId, infrastructureManagerGUID, infrastructureManagerName, infrastructureManagerIsHome, hostClusterEntityType, hostGUID, hostClusterMemberRelationship, hostEntityType, clusterMemberGUID, effectiveFrom, effectiveTo,null, methodName);
    }


    /**
     * Remove a relationship between a host and an cluster member host.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param hostGUID unique identifier of the host
     * @param clusterMemberGUID unique identifier of the cluster member host
     * @param effectiveTime time when the hosting is effective
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearClusterMember(String userId,
                                   String infrastructureManagerGUID,
                                   String infrastructureManagerName,
                                   String hostGUID,
                                   String clusterMemberGUID,
                                   Date   effectiveTime) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        final String methodName = "clearClusterMember";

        super.clearRelatedAsset(userId, infrastructureManagerGUID, infrastructureManagerName, hostClusterEntityType, hostGUID, hostClusterMemberRelationship, hostEntityType, clusterMemberGUID, effectiveTime, methodName);
    }



    /**
     * Update the zones for the host asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the IT Infrastructure OMAS).
     *
     * @param userId calling user
     * @param hostGUID unique identifier of the metadata element to publish
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void publishHost(String userId,
                            String hostGUID) throws InvalidParameterException,
                                                    UserNotAuthorizedException,
                                                    PropertyServerException
    {
        final String methodName = "publishHost";

        super.publishAsset(userId, hostGUID, methodName);
    }


    /**
     * Update the zones for the host asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the IT Infrastructure OMAS.  This is the setting when the host is first created).
     *
     * @param userId calling user
     * @param hostGUID unique identifier of the metadata element to withdraw
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void withdrawHost(String userId,
                             String hostGUID) throws InvalidParameterException,
                                                     UserNotAuthorizedException,
                                                     PropertyServerException
    {
        final String methodName = "withdrawHost";

        super.withdrawAsset(userId, hostGUID, methodName);
    }


    /**
     * Remove the metadata element representing a host.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param hostGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeHost(String userId,
                           String infrastructureManagerGUID,
                           String infrastructureManagerName,
                           String hostGUID) throws InvalidParameterException,
                                                   UserNotAuthorizedException,
                                                   PropertyServerException
    {
        final String methodName = "removeHost";

        super.removeAsset(userId, infrastructureManagerGUID, infrastructureManagerName, hostGUID, methodName);
    }



    /**
     * Retrieve the list of host metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<HostElement> findHosts(String userId,
                                       String searchString,
                                       Date   effectiveTime,
                                       int    startFrom,
                                       int    pageSize) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String methodName = "findHosts";

        return this.convertAssetElements(super.findAssets(userId, searchString, hostEntityType, effectiveTime, startFrom, pageSize, methodName));
    }


    /**
     * Retrieve the list of host metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<HostElement> getHostsByName(String userId,
                                            String name,
                                            Date   effectiveTime,
                                            int    startFrom,
                                            int    pageSize) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName = "getHostsByName";

        return this.convertAssetElements(super.getAssetsByName(userId, name, hostEntityType, effectiveTime, startFrom, pageSize, methodName));
    }


    /**
     * Retrieve the list of hosts created by this caller.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<HostElement> getHostsForInfrastructureManager(String userId,
                                                              String infrastructureManagerGUID,
                                                              String infrastructureManagerName,
                                                              Date   effectiveTime,
                                                              int    startFrom,
                                                              int    pageSize) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName = "getHostsForInfrastructureManager";

        return this.convertAssetElements(super.getAssetsForInfrastructureManager(userId, infrastructureManagerGUID, infrastructureManagerName, hostEntityType, effectiveTime, startFrom, pageSize, methodName));
    }


    /**
     * Return the list of cluster members associated with a host.
     *
     * @param userId calling user
     * @param hostGUID unique identifier of the host to query
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<HostElement> getClusterMembersForHost(String userId,
                                                      String hostGUID,
                                                      Date   effectiveTime,
                                                      int    startFrom,
                                                      int    pageSize) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName = "getClusterMembersForHost";

        return this.convertRelatedAssetElements(super.getRelatedAssets(userId, hostClusterEntityType, hostGUID, 1, hostClusterMemberRelationship, hostEntityType, effectiveTime, startFrom, pageSize, methodName));
    }


    /**
     * Retrieve the host metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public HostElement getHostByGUID(String userId,
                                     String guid) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        final String methodName = "getHostByGUID";

        return this.convertAssetElement(super.getAssetByGUID(userId, hostEntityType, guid, null, methodName));
    }


    /**
     * Convert a list of RelatedAssetElements into a list of HostElements.
     *
     * @param relatedAssetElements returned assets
     * @return result for caller
     */
    private List<HostElement> convertRelatedAssetElements(List<RelatedAssetElement> relatedAssetElements)
    {
        if (relatedAssetElements != null)
        {
            List<HostElement> hostElements = new ArrayList<>();

            for (RelatedAssetElement relatedAssetElement : relatedAssetElements)
            {
                hostElements.add(this.convertAssetElement(relatedAssetElement.getRelatedAsset()));
            }

            if (! hostElements.isEmpty())
            {
                return hostElements;
            }
        }

        return null;
    }


    /**
     * Convert a list of AssetElements into a list of HostElements.
     *
     * @param assetElements returned assets
     * @return result for caller
     */
    private List<HostElement> convertAssetElements(List<AssetElement> assetElements)
    {
        if (assetElements != null)
        {
            List<HostElement> hostElements = new ArrayList<>();

            for (AssetElement assetElement : assetElements)
            {
                hostElements.add(this.convertAssetElement(assetElement));
            }

            if (! hostElements.isEmpty())
            {
                return hostElements;
            }
        }

        return null;
    }


    /**
     * Convert a single AssetElement to a HostElement.
     *
     * @param assetElement return asset
     * @return result for caller
     */
    private HostElement convertAssetElement(AssetElement assetElement)
    {
        if (assetElement != null)
        {
            return new HostElement(assetElement);
        }

        return null;
    }
}
