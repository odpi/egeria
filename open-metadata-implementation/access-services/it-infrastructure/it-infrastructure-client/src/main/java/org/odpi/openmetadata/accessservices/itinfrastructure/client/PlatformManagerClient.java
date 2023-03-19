/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.client;

import org.odpi.openmetadata.accessservices.itinfrastructure.api.SoftwareServerPlatformManagerInterface;
import org.odpi.openmetadata.accessservices.itinfrastructure.client.rest.ITInfrastructureRESTClient;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.AssetElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.SoftwareServerPlatformElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.AssetProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.SoftwareServerPlatformProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.TemplateProperties;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * PlatformManagerClient supports the APIs to maintain software server platforms and their related objects.
 */
public class PlatformManagerClient extends AssetManagerClientBase implements SoftwareServerPlatformManagerInterface
{
    private static final String platformEntityType             = "SoftwareServerPlatform";


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public PlatformManagerClient(String   serverName,
                                 String   serverPlatformURLRoot,
                                 AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public PlatformManagerClient(String serverName,
                                 String serverPlatformURLRoot) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public PlatformManagerClient(String serverName,
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
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public PlatformManagerClient(String   serverName,
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
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param restClient client that issues the REST API calls
     * @param maxPageSize maximum number of results supported by this server
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public PlatformManagerClient(String                     serverName,
                                 String                     serverPlatformURLRoot,
                                 ITInfrastructureRESTClient restClient,
                                 int                        maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, restClient, maxPageSize);
    }


    /**
     * Create a new metadata element to represent a platform.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param infrastructureManagerIsHome should the platform be marked as owned by the infrastructure manager so others can not update?
     * @param platformProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createSoftwareServerPlatform(String                           userId,
                                               String                           infrastructureManagerGUID,
                                               String                           infrastructureManagerName,
                                               boolean                          infrastructureManagerIsHome,
                                               SoftwareServerPlatformProperties platformProperties) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        final String methodName = "createSoftwareServerPlatform";

        AssetProperties assetProperties = platformProperties.cloneToAsset();

        return super.createAsset(userId, infrastructureManagerGUID, infrastructureManagerName, infrastructureManagerIsHome, assetProperties, null, methodName);
    }


    /**
     * Create a new metadata element to represent a platform using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param infrastructureManagerIsHome should the platform be marked as owned by the infrastructure manager so others can not update?
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
    public String createSoftwareServerPlatformFromTemplate(String             userId,
                                                           String             infrastructureManagerGUID,
                                                           String             infrastructureManagerName,
                                                           boolean            infrastructureManagerIsHome,
                                                           String             templateGUID,
                                                           TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        final String methodName = "createSoftwareServerPlatformFromTemplate";

        return super.createAssetFromTemplate(userId, infrastructureManagerGUID, infrastructureManagerName, infrastructureManagerIsHome, templateGUID, templateProperties, methodName);
    }


    /**
     * Update the metadata element representing a platform.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param platformGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param platformProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateSoftwareServerPlatform(String                           userId,
                                             String                           infrastructureManagerGUID,
                                             String                           infrastructureManagerName,
                                             String                           platformGUID,
                                             boolean                          isMergeUpdate,
                                             SoftwareServerPlatformProperties platformProperties) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException

    {
        final String methodName = "updateSoftwareServerPlatform";

        AssetProperties assetProperties = platformProperties.cloneToAsset();

        super.updateAsset(userId, infrastructureManagerGUID, infrastructureManagerName, platformGUID, isMergeUpdate, assetProperties, methodName);
    }


    /**
     * Update the zones for the platform asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the IT Infrastructure OMAS).
     *
     * @param userId calling user
     * @param platformGUID unique identifier of the metadata element to publish
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void publishSoftwareServerPlatform(String userId,
                                              String platformGUID) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String methodName = "publishSoftwareServerPlatform";

        super.publishAsset(userId, platformGUID, methodName);
    }


    /**
     * Update the zones for the platform asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the IT Infrastructure OMAS.  This is the setting when the platform is first created).
     *
     * @param userId calling user
     * @param platformGUID unique identifier of the metadata element to withdraw
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void withdrawSoftwareServerPlatform(String userId,
                                               String platformGUID) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException

    {
        final String methodName = "withdrawSoftwareServerPlatform";

        super.withdrawAsset(userId, platformGUID, methodName);
    }


    /**
     * Remove the metadata element representing a platform.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param platformGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeSoftwareServerPlatform(String userId,
                                             String infrastructureManagerGUID,
                                             String infrastructureManagerName,
                                             String platformGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException

    {
        final String methodName = "removeSoftwareServerPlatform";

        super.removeAsset(userId, infrastructureManagerGUID, infrastructureManagerName, platformGUID, methodName);
    }



    /**
     * Retrieve the list of platform metadata elements that contain the search string.
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
    public List<SoftwareServerPlatformElement> findSoftwareServerPlatforms(String userId,
                                                                           String searchString,
                                                                           Date   effectiveTime,
                                                                           int    startFrom,
                                                                           int    pageSize) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String methodName = "findSoftwareServerPlatforms";

        return this.convertAssetElements(super.findAssets(userId, searchString, platformEntityType, effectiveTime, startFrom, pageSize, methodName));
    }


    /**
     * Retrieve the list of platform metadata elements with a matching qualified or display name.
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
    public List<SoftwareServerPlatformElement> getSoftwareServerPlatformsByName(String userId,
                                                                                String name,
                                                                                Date   effectiveTime,
                                                                                int    startFrom,
                                                                                int    pageSize) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException

    {
        final String methodName = "getSoftwareServerPlatformsByName";

        return this.convertAssetElements(super.getAssetsByName(userId, name, platformEntityType, effectiveTime, startFrom, pageSize, methodName));
    }


    /**
     * Retrieve the list of platforms created by this caller.
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
    public List<SoftwareServerPlatformElement> getSoftwareServerPlatformsForInfrastructureManager(String userId,
                                                                                                  String infrastructureManagerGUID,
                                                                                                  String infrastructureManagerName,
                                                                                                  Date   effectiveTime,
                                                                                                  int    startFrom,
                                                                                                  int    pageSize) throws InvalidParameterException,
                                                                                                                          UserNotAuthorizedException,
                                                                                                                          PropertyServerException

    {
        final String methodName = "getSoftwareServerPlatformsForInfrastructureManager";

        return this.convertAssetElements(super.getAssetsForInfrastructureManager(userId, infrastructureManagerGUID, infrastructureManagerName, platformEntityType, effectiveTime, startFrom, pageSize, methodName));
    }


    /**
     * Retrieve the platform metadata element with the supplied unique identifier.
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
    public SoftwareServerPlatformElement getSoftwareServerPlatformByGUID(String userId,
                                                                         String guid) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName = "getSoftwareServerPlatformByGUID";

        return this.convertAssetElement(super.getAssetByGUID(userId, platformEntityType, guid, null, methodName));
    }



    /**
     * Convert a list of AssetElements into a list of SoftwareServerPlatformElements.
     *
     * @param assetElements returned assets
     * @return result for caller
     */
    private List<SoftwareServerPlatformElement> convertAssetElements(List<AssetElement> assetElements)
    {
        if (assetElements != null)
        {
            List<SoftwareServerPlatformElement> hostElements = new ArrayList<>();

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
     * Convert a single AssetElement to a SoftwareServerPlatformElement.
     *
     * @param assetElement return asset
     * @return result for caller
     */
    private SoftwareServerPlatformElement convertAssetElement(AssetElement assetElement)
    {
        if (assetElement != null)
        {
            return new SoftwareServerPlatformElement(assetElement);
        }

        return null;
    }
}
