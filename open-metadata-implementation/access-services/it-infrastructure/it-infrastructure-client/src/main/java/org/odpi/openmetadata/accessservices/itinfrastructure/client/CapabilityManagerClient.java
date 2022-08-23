/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.client;

import org.odpi.openmetadata.accessservices.itinfrastructure.api.SoftwareCapabilityManagerInterface;
import org.odpi.openmetadata.accessservices.itinfrastructure.api.SoftwareServerCapabilityManagerInterface;
import org.odpi.openmetadata.accessservices.itinfrastructure.client.rest.ITInfrastructureRESTClient;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.RelatedAssetElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.ServerAssetUseElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.SoftwareCapabilityElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.SoftwareServerCapabilityElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.CapabilityDeploymentProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.ServerAssetUseProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.ServerAssetUseType;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.SoftwareCapabilityProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.SoftwareServerCapabilityProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.TemplateProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.MetadataSourceRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.ServerAssetUseListResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.ServerAssetUseRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.ServerAssetUseResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.SoftwareCapabilityRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.SoftwareCapabilityListResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.SoftwareCapabilityResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.TemplateRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.UseTypeRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.EffectiveTimeRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * CapabilityManagerClient supports the APIs to maintain software server capabilities and their related objects.
 */
public class CapabilityManagerClient extends AssetManagerClientBase implements SoftwareCapabilityManagerInterface, SoftwareServerCapabilityManagerInterface
{
    private static final String capabilityURLTemplatePrefix = baseURLTemplatePrefix + "/software-capabilities";
    private static final String assetUsesURLTemplatePrefix  = baseURLTemplatePrefix + "/server-asset-uses";

    private static final String softwareCapabilityTypeName      = "SoftwareCapability";
    private static final String deploymentRelationshipTypeName  = "SupportedSoftwareCapability";


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public CapabilityManagerClient(String   serverName,
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
    public CapabilityManagerClient(String serverName,
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
    public CapabilityManagerClient(String serverName,
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
    public CapabilityManagerClient(String   serverName,
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
    public CapabilityManagerClient(String                     serverName,
                                   String                     serverPlatformURLRoot,
                                   ITInfrastructureRESTClient restClient,
                                   int                        maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, restClient, maxPageSize);
    }


    /* =====================================================================================================================
     * The software server capability links assets to the hosting server.
     */


    /**
     * Create a new metadata element to represent a software server capability.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param infrastructureManagerIsHome should the software server capability be marked as owned by the infrastructure manager so others can not update?
     * @param classificationName optional classification name that refines the type of the software server capability.
     * @param capabilityProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    @Deprecated
    public String createSoftwareServerCapability(String                             userId,
                                                 String                             infrastructureManagerGUID,
                                                 String                             infrastructureManagerName,
                                                 boolean                            infrastructureManagerIsHome,
                                                 String                             classificationName,
                                                 SoftwareServerCapabilityProperties capabilityProperties) throws InvalidParameterException,
                                                                                                                 UserNotAuthorizedException,
                                                                                                                 PropertyServerException
    {
        if ((capabilityProperties != null) && (capabilityProperties.getTypeName() == null))
        {
            capabilityProperties.setTypeName("SoftwareCapability");
        }

        return this.createSoftwareCapability(userId, infrastructureManagerGUID, infrastructureManagerName, infrastructureManagerIsHome, classificationName, capabilityProperties);
    }


    /**
     * Create a new metadata element to represent a software server capability using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param infrastructureManagerIsHome should the software server capability be marked as owned by the infrastructure manager so others can not update?
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
    @Deprecated
    public String createSoftwareServerCapabilityFromTemplate(String             userId,
                                                             String             infrastructureManagerGUID,
                                                             String             infrastructureManagerName,
                                                             boolean            infrastructureManagerIsHome,
                                                             String             templateGUID,
                                                             TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        return this.createSoftwareCapabilityFromTemplate(userId, infrastructureManagerGUID, infrastructureManagerName, infrastructureManagerIsHome, templateGUID, templateProperties);
    }


    /**
     * Update the metadata element representing a software server capability.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param capabilityGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param capabilityProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    @Deprecated
    public void updateSoftwareServerCapability(String                             userId,
                                               String                             infrastructureManagerGUID,
                                               String                             infrastructureManagerName,
                                               String                             capabilityGUID,
                                               boolean                            isMergeUpdate,
                                               SoftwareServerCapabilityProperties capabilityProperties) throws InvalidParameterException,
                                                                                                               UserNotAuthorizedException,
                                                                                                               PropertyServerException
    {
        updateSoftwareCapability(userId, infrastructureManagerGUID, infrastructureManagerName, capabilityGUID, isMergeUpdate, capabilityProperties);
    }


    /**
     * Remove the metadata element representing a software server capability.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param capabilityGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    @Deprecated
    public void removeSoftwareServerCapability(String userId,
                                               String infrastructureManagerGUID,
                                               String infrastructureManagerName,
                                               String capabilityGUID) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        this.removeSoftwareCapability(userId, infrastructureManagerGUID, infrastructureManagerName, capabilityGUID);
    }


    /**
     * Retrieve the list of software server capability metadata elements that contain the search string.
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
    @Deprecated
    public List<SoftwareServerCapabilityElement> findSoftwareServerCapabilities(String userId,
                                                                                String searchString,
                                                                                Date   effectiveTime,
                                                                                int    startFrom,
                                                                                int    pageSize) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        return this.getSoftwareServerCapabilityElements(findSoftwareCapabilities(userId, searchString, effectiveTime, startFrom, pageSize));
    }


    /**
     * Retrieve the list of software server capability metadata elements with a matching qualified or display name.
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
    @Deprecated
    @Override
    public List<SoftwareServerCapabilityElement> getSoftwareServerCapabilitiesByName(String userId,
                                                                                     String name,
                                                                                     Date   effectiveTime,
                                                                                     int    startFrom,
                                                                                     int    pageSize) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException
    {
        return this.getSoftwareServerCapabilityElements(getSoftwareCapabilitiesByName(userId, name, effectiveTime, startFrom, pageSize));
    }


    /**
     * Retrieve the software server capability metadata element with the supplied unique identifier.
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
    @Deprecated
    @Override
    public SoftwareServerCapabilityElement getSoftwareServerCapabilityByGUID(String userId,
                                                                             String guid) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        return this.getSoftwareServerCapabilityElement(this.getSoftwareCapabilityByGUID(userId, guid));
    }



    @Deprecated
    private List<SoftwareServerCapabilityElement> getSoftwareServerCapabilityElements(List<SoftwareCapabilityElement> softwareCapabilityElements)
    {
        if (softwareCapabilityElements != null)
        {
            List<SoftwareServerCapabilityElement> softwareServerCapabilityElements = new ArrayList<>();

            for (SoftwareCapabilityElement softwareCapabilityElement : softwareCapabilityElements)
            {
                if (softwareCapabilityElement != null)
                {
                    softwareCapabilityElements.add(this.getSoftwareServerCapabilityElement(softwareCapabilityElement));
                }
            }
            return  softwareServerCapabilityElements;
        }

        return null;
    }


    @Deprecated
    private SoftwareServerCapabilityElement getSoftwareServerCapabilityElement(SoftwareCapabilityElement softwareCapabilityElement)
    {
        if (softwareCapabilityElement != null)
        {
            SoftwareServerCapabilityElement softwareServerCapabilityElement = new SoftwareServerCapabilityElement();

            softwareServerCapabilityElement.setElementHeader(softwareCapabilityElement.getElementHeader());
            softwareServerCapabilityElement.setProperties(softwareCapabilityElement.getProperties());

            return softwareServerCapabilityElement;
        }

        return null;
    }


    /* =====================================================================================================================
     * The software capability links assets to the hosting server.
     */

    /**
     * Create a new metadata element to represent a software server capability.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param infrastructureManagerIsHome should the software server capability be marked as owned by the infrastructure manager so others can not update?
     * @param classificationName optional classification name that refines the type of the software server capability.
     * @param capabilityProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createSoftwareCapability(String                       userId,
                                           String                       infrastructureManagerGUID,
                                           String                       infrastructureManagerName,
                                           boolean                      infrastructureManagerIsHome,
                                           String                       classificationName,
                                           SoftwareCapabilityProperties capabilityProperties) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        final String methodName                  = "createSoftwareServerCapability";
        final String propertiesParameterName     = "capabilityProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(capabilityProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(capabilityProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + capabilityURLTemplatePrefix + "?infrastructureManagerIsHome={2}";

        SoftwareCapabilityRequestBody requestBody = new SoftwareCapabilityRequestBody(capabilityProperties);

        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);
        requestBody.setClassificationName(classificationName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  infrastructureManagerIsHome);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent a software capability using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param infrastructureManagerIsHome should the software server capability be marked as owned by the infrastructure manager so others can not update?
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
    public String createSoftwareCapabilityFromTemplate(String             userId,
                                                       String             infrastructureManagerGUID,
                                                       String             infrastructureManagerName,
                                                       boolean            infrastructureManagerIsHome,
                                                       String             templateGUID,
                                                       TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        final String methodName                  = "createSoftwareServerCapabilityFromTemplate";
        final String templateGUIDParameterName   = "templateGUID";
        final String propertiesParameterName     = "templateProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + capabilityURLTemplatePrefix + "/from-template/{2}?infrastructureManagerIsHome={3}";

        TemplateRequestBody requestBody = new TemplateRequestBody(templateProperties);

        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  templateGUID,
                                                                  infrastructureManagerIsHome);

        return restResult.getGUID();
    }


    /**
     * Update the metadata element representing a software capability.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param capabilityGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param capabilityProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateSoftwareCapability(String                       userId,
                                         String                       infrastructureManagerGUID,
                                         String                       infrastructureManagerName,
                                         String                       capabilityGUID,
                                         boolean                      isMergeUpdate,
                                         SoftwareCapabilityProperties capabilityProperties) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String methodName                  = "updateSoftwareCapability";
        final String elementGUIDParameterName    = "capabilityGUID";
        final String propertiesParameterName     = "capabilityProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(capabilityGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(capabilityProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(capabilityProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + capabilityURLTemplatePrefix + "/{2}?isMergeUpdate={3}";

        SoftwareCapabilityRequestBody requestBody = new SoftwareCapabilityRequestBody(capabilityProperties);

        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        capabilityGUID,
                                        isMergeUpdate);
    }




    /**
     * Link a software capability to a software server.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param infrastructureManagerIsHome should the software server capability be marked as owned by the infrastructure manager so others can not update?
     * @param capabilityGUID unique identifier of the software server capability
     * @param infrastructureAssetGUID unique identifier of the software server
     * @param deploymentProperties describes the deployment of the capability onto the server
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void deployCapability(String                         userId,
                                 String                         infrastructureManagerGUID,
                                 String                         infrastructureManagerName,
                                 boolean                        infrastructureManagerIsHome,
                                 String                         capabilityGUID,
                                 String                         infrastructureAssetGUID,
                                 CapabilityDeploymentProperties deploymentProperties) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName = "deployCapability";

        Map<String, Object> propertyMap   = null;
        Date                effectiveFrom = null;
        Date                effectiveTo   = null;

        if (deploymentProperties != null)
        {
            propertyMap = deploymentProperties.cloneToMap();
            effectiveFrom = deploymentProperties.getEffectiveFrom();
            effectiveTo = deploymentProperties.getEffectiveTo();
        }

        super.setupRelatedAsset(userId,
                                infrastructureManagerGUID,
                                infrastructureManagerName,
                                infrastructureManagerIsHome,
                                itAssetTypeName,
                                infrastructureAssetGUID,
                                deploymentRelationshipTypeName,
                                softwareCapabilityTypeName,
                                capabilityGUID,
                                effectiveFrom,
                                effectiveTo,
                                propertyMap,
                                methodName);
    }


    /**
     * Update the properties of a server capability's deployment.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param deploymentGUID unique identifier of the relationship
     * @param deploymentProperties describes the deployment of the capability onto the server
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void updateCapabilityDeployment(String                         userId,
                                           String                         infrastructureManagerGUID,
                                           String                         infrastructureManagerName,
                                           String                         deploymentGUID,
                                           boolean                        isMergeUpdate,
                                           CapabilityDeploymentProperties deploymentProperties) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        final String methodName = "updateCapabilityDeployment";

        Map<String, Object> propertyMap   = null;
        Date                effectiveFrom = null;
        Date                effectiveTo   = null;

        if (deploymentProperties != null)
        {
            propertyMap = deploymentProperties.cloneToMap();
            effectiveFrom = deploymentProperties.getEffectiveFrom();
            effectiveTo = deploymentProperties.getEffectiveTo();
        }

        super.updateAssetRelationship(userId, infrastructureManagerGUID, infrastructureManagerName,
                                deploymentGUID, deploymentRelationshipTypeName,
                                effectiveFrom, effectiveTo, isMergeUpdate, propertyMap, methodName);
    }


    /**
     * Remove the link between a software server capability and a software server.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param itAssetGUID unique identifier of the software server/platform/host
     * @param capabilityGUID unique identifier of the software server capability
     * @param effectiveTime time that the relationship is effective
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void removeCapabilityDeployment(String userId,
                                           String infrastructureManagerGUID,
                                           String infrastructureManagerName,
                                           String itAssetGUID,
                                           String capabilityGUID,
                                           Date   effectiveTime) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String methodName = "removeCapabilityDeployment";

        super.clearRelatedAsset(userId,
                                infrastructureManagerGUID,
                                infrastructureManagerName,
                                itAssetTypeName,
                                itAssetGUID,
                                deploymentRelationshipTypeName,
                                softwareCapabilityTypeName,
                                capabilityGUID,
                                effectiveTime,
                                methodName);
    }



    /**
     * Remove the metadata element representing a software capability.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param capabilityGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeSoftwareCapability(String userId,
                                         String infrastructureManagerGUID,
                                         String infrastructureManagerName,
                                         String capabilityGUID) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String methodName = "removeSoftwareCapability";
        final String elementGUIDParameterName    = "capabilityGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(capabilityGUID, elementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + capabilityURLTemplatePrefix + "/{2}/delete";

        MetadataSourceRequestBody requestBody = new MetadataSourceRequestBody();

        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        capabilityGUID);
    }


    /**
     * Retrieve the list of software capability metadata elements that contain the search string.
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
    public List<SoftwareCapabilityElement> findSoftwareCapabilities(String userId,
                                                                    String searchString,
                                                                    Date   effectiveTime,
                                                                    int    startFrom,
                                                                    int    pageSize) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName                = "findSoftwareCapabilities";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + capabilityURLTemplatePrefix + "/by-search-string?startFrom={2}&pageSize={3}";

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setEffectiveTime(effectiveTime);
        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        SoftwareCapabilityListResponse restResult = restClient.callSoftwareServerCapabilityListPostRESTCall(methodName,
                                                                                                            urlTemplate,
                                                                                                            requestBody,
                                                                                                            serverName,
                                                                                                            userId,
                                                                                                            startFrom,
                                                                                                            validatedPageSize);

        return restResult.getElementList();
    }


    /**
     * Retrieve the list of software capability metadata elements with a matching qualified or display name.
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
    public List<SoftwareCapabilityElement> getSoftwareCapabilitiesByName(String userId,
                                                                         String name,
                                                                         Date   effectiveTime,
                                                                         int    startFrom,
                                                                         int    pageSize) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        final String methodName        = "getSoftwareCapabilitiesByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + capabilityURLTemplatePrefix + "/by-name?startFrom={2}&pageSize={3}";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setEffectiveTime(effectiveTime);
        requestBody.setName(name);
        requestBody.setNamePropertyName(nameParameterName);

        SoftwareCapabilityListResponse restResult = restClient.callSoftwareServerCapabilityListPostRESTCall(methodName,
                                                                                                            urlTemplate,
                                                                                                            requestBody,
                                                                                                            serverName,
                                                                                                            userId,
                                                                                                            startFrom,
                                                                                                            validatedPageSize);

        return restResult.getElementList();
    }


    /**
     * Retrieve the IT asset metadata elements where the software with the supplied unique identifier is deployed.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     * @param effectiveTime effective time for the query
     *
     * @return list of related IT Assets
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<RelatedAssetElement> getSoftwareCapabilityDeployments(String userId,
                                                                      String guid,
                                                                      Date   effectiveTime,
                                                                      int    startFrom,
                                                                      int    pageSize) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String methodName = "getSoftwareCapabilityDeployments";

        return super.getRelatedAssets(userId, softwareCapabilityTypeName, guid, 2, null, itAssetTypeName, effectiveTime, startFrom, pageSize, methodName);
    }


    /**
     * Retrieve the software capabilities that are deployed to an IT asset.
     *
     * @param userId calling user
     * @param itAssetGUID unique identifier of the hosting metadata element
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of related IT Assets
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<SoftwareCapabilityElement> getDeployedSoftwareCapabilities(String userId,
                                                                           String itAssetGUID,
                                                                           Date   effectiveTime,
                                                                           int    startFrom,
                                                                           int    pageSize) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String methodName = "getDeployedSoftwareCapabilities";
        final String guidParameterName = "itAssetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(itAssetGUID, guidParameterName, methodName);

        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + capabilityURLTemplatePrefix + "/deployed-on-it-assets/{2}?startFrom={3}&pageSize={4}";

        EffectiveTimeRequestBody requestBody = new EffectiveTimeRequestBody();

        requestBody.setEffectiveTime(effectiveTime);

        SoftwareCapabilityListResponse restResult = restClient.callSoftwareServerCapabilityListPostRESTCall(methodName,
                                                                            urlTemplate,
                                                                            requestBody,
                                                                            serverName,
                                                                            userId,
                                                                            itAssetGUID,
                                                                            startFrom,
                                                                            validatedPageSize);

        return restResult.getElementList();
    }


    /**
     * Retrieve the list of capabilities created by this caller.
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
    public List<SoftwareCapabilityElement> getSoftwareCapabilitiesForInfrastructureManager(String userId,
                                                                                           String infrastructureManagerGUID,
                                                                                           String infrastructureManagerName,
                                                                                           Date   effectiveTime,
                                                                                           int    startFrom,
                                                                                           int    pageSize) throws InvalidParameterException,
                                                                                                                   UserNotAuthorizedException,
                                                                                                                   PropertyServerException
    {
        final String methodName = "getSoftwareCapabilitiesForInfrastructureManager";

        final String infrastructureManagerGUIDParameterName = "infrastructureManagerGUID";
        final String infrastructureManagerNameParameterName = "infrastructureManagerName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(infrastructureManagerGUID, infrastructureManagerGUIDParameterName, methodName);
        invalidParameterHandler.validateName(infrastructureManagerName, infrastructureManagerNameParameterName, methodName);

        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/infrastructure-managers/{2}/{3}/software-capabilities" + "?startFrom={4}&pageSize={5}";

        EffectiveTimeRequestBody requestBody = new EffectiveTimeRequestBody();

        requestBody.setEffectiveTime(effectiveTime);

        SoftwareCapabilityListResponse restResult = restClient.callSoftwareServerCapabilityListPostRESTCall(methodName,
                                                                            urlTemplate,
                                                                            requestBody,
                                                                            serverName,
                                                                            userId,
                                                                            infrastructureManagerGUID,
                                                                            infrastructureManagerName,
                                                                            startFrom,
                                                                            validatedPageSize);

        return restResult.getElementList();

    }



    /**
     * Retrieve the software capability metadata element with the supplied unique identifier.
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
    public SoftwareCapabilityElement getSoftwareCapabilityByGUID(String userId,
                                                                 String guid) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName = "getSoftwareCapabilityByGUID";
        final String guidParameterName = "guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + capabilityURLTemplatePrefix + "/{2}";

        SoftwareCapabilityResponse restResult = restClient.callSoftwareServerCapabilityGetRESTCall(methodName,
                                                                                                   urlTemplate,
                                                                                                   serverName,
                                                                                                   userId,
                                                                                                   guid);

        return restResult.getElement();
    }


    /*
     * A software server capability works with assets
     */

    /**
     * Create a new metadata relationship to represent the use of an asset by a software server capability.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param infrastructureManagerIsHome should the software server capability be marked as owned by the infrastructure manager so others can not update?
     * @param capabilityGUID unique identifier of a software server capability
     * @param assetGUID unique identifier of an asset
     * @param properties properties about the ServerAssetUse relationship
     *
     * @return unique identifier of the new ServerAssetUse relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createServerAssetUse(String                   userId,
                                       String                   infrastructureManagerGUID,
                                       String                   infrastructureManagerName,
                                       boolean                  infrastructureManagerIsHome,
                                       String                   capabilityGUID,
                                       String                   assetGUID,
                                       ServerAssetUseProperties properties) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName                  = "createServerAssetUse";
        final String capabilityGUIDParameterName = "capabilityGUID";
        final String assetGUIDParameterName      = "assetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(capabilityGUID, capabilityGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetUsesURLTemplatePrefix + "/software-capabilities/{2}/assets/{3}?infrastructureManagerIsHome={4}";

        ServerAssetUseRequestBody requestBody = new ServerAssetUseRequestBody();

        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);
        requestBody.setProperties(properties);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  capabilityGUID,
                                                                  assetGUID,
                                                                  infrastructureManagerIsHome);

        return restResult.getGUID();
    }


    /**
     * Update the metadata relationship to represent the use of an asset by a software server capability.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param serverAssetUseGUID unique identifier of the relationship between a software server capability and an asset
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param properties new properties for the ServerAssetUse relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateServerAssetUse(String                   userId,
                                     String                   infrastructureManagerGUID,
                                     String                   infrastructureManagerName,
                                     String                   serverAssetUseGUID,
                                     boolean                  isMergeUpdate,
                                     ServerAssetUseProperties properties) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String methodName               = "updateServerAssetUse";
        final String elementGUIDParameterName = "serverAssetUseGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(serverAssetUseGUID, elementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetUsesURLTemplatePrefix + "/{2}?isMergeUpdate={3}";

        ServerAssetUseRequestBody requestBody = new ServerAssetUseRequestBody();

        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);
        requestBody.setProperties(properties);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        serverAssetUseGUID,
                                        isMergeUpdate);
    }


    /**
     * Remove the metadata relationship to represent the use of an asset by a software server capability.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param serverAssetUseGUID unique identifier of the relationship between a software server capability and an asset
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeServerAssetUse(String userId,
                                     String infrastructureManagerGUID,
                                     String infrastructureManagerName,
                                     String serverAssetUseGUID) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String methodName               = "removeServerAssetUse";
        final String elementGUIDParameterName = "serverAssetUseGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(serverAssetUseGUID, elementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetUsesURLTemplatePrefix + "/{2}/delete";

        MetadataSourceRequestBody requestBody = new MetadataSourceRequestBody();

        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        serverAssetUseGUID);
    }


    /**
     * Return the list of server asset use relationships associated with a software server capability.
     *
     * @param userId calling user
     * @param capabilityGUID unique identifier of the software server capability to query
     * @param useType value to search for.  Null means all use types.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching relationships
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ServerAssetUseElement> getServerAssetUsesForCapability(String             userId,
                                                                       String             capabilityGUID,
                                                                       ServerAssetUseType useType,
                                                                       Date               effectiveTime,
                                                                       int                startFrom,
                                                                       int                pageSize) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        final String methodName               = "getServerAssetUsesForCapability";
        final String elementGUIDParameterName = "capabilityGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(capabilityGUID, elementGUIDParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetUsesURLTemplatePrefix + "/software-capabilities/{2}?startFrom={3}&pageSize={4}";

        UseTypeRequestBody requestBody = new UseTypeRequestBody();

        requestBody.setEffectiveTime(effectiveTime);
        requestBody.setUseType(useType);

        ServerAssetUseListResponse restResult = restClient.callServerAssetUseListPostRESTCall(methodName,
                                                                             urlTemplate,
                                                                             requestBody,
                                                                             serverName,
                                                                             userId,
                                                                             capabilityGUID,
                                                                             startFrom,
                                                                             validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Return the list of software server capabilities that make use of a specific asset.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset to query
     * @param useType Optionally restrict the search to a specific user type.  Null means all use types.
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching relationships
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ServerAssetUseElement> getCapabilityUsesForAsset(String             userId,
                                                                 String             assetGUID,
                                                                 ServerAssetUseType useType,
                                                                 Date               effectiveTime,
                                                                 int                startFrom,
                                                                 int                pageSize) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        final String methodName               = "getCapabilityUsesForAsset";
        final String elementGUIDParameterName = "assetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, elementGUIDParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetUsesURLTemplatePrefix + "/assets/{2}?startFrom={3}&pageSize={4}";

        UseTypeRequestBody requestBody = new UseTypeRequestBody();

        requestBody.setEffectiveTime(effectiveTime);
        requestBody.setUseType(useType);

        ServerAssetUseListResponse restResult = restClient.callServerAssetUseListPostRESTCall(methodName,
                                                                                              urlTemplate,
                                                                                              requestBody,
                                                                                              serverName,
                                                                                              userId,
                                                                                              assetGUID,
                                                                                              startFrom,
                                                                                              validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Retrieve the list of relationships between a specific software server capability and a specific asset.
     *
     * @param userId calling user
     * @param capabilityGUID unique identifier of a software server capability
     * @param assetGUID unique identifier of an asset
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching relationships
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ServerAssetUseElement> getServerAssetUsesForElements(String userId,
                                                                     String capabilityGUID,
                                                                     String assetGUID,
                                                                     Date   effectiveTime,
                                                                     int    startFrom,
                                                                     int    pageSize) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName               = "getServerAssetUsesForElements";
        final String capabilityGUIDParameterName = "capabilityGUID";
        final String assetGUIDParameterName = "assetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(capabilityGUID, capabilityGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetUsesURLTemplatePrefix + "/software-capabilities/{2}/assets/{3}/by-elements?startFrom={4}&pageSize={5}";

        EffectiveTimeRequestBody requestBody = new EffectiveTimeRequestBody();

        requestBody.setEffectiveTime(effectiveTime);

        ServerAssetUseListResponse restResult = restClient.callServerAssetUseListPostRESTCall(methodName,
                                                                                              urlTemplate,
                                                                                              requestBody,
                                                                                              serverName,
                                                                                              userId,
                                                                                              capabilityGUID,
                                                                                              assetGUID,
                                                                                              startFrom,
                                                                                              validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Retrieve the server asset use type relationship with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return requested relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public ServerAssetUseElement getServerAssetUseByGUID(String userId,
                                                         String guid) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName               = "getServerAssetUseByGUID";
        final String elementGUIDParameterName = "guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, elementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + assetUsesURLTemplatePrefix + "/{2}";

        ServerAssetUseResponse restResult = restClient.callServerAssetUseGetRESTCall(methodName,
                                                                                     urlTemplate,
                                                                                     serverName,
                                                                                     userId,
                                                                                     guid);

        return restResult.getElement();
    }
}
