/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.client;

import org.odpi.openmetadata.accessservices.itinfrastructure.api.ProcessManagerInterface;
import org.odpi.openmetadata.accessservices.itinfrastructure.client.rest.ITInfrastructureRESTClient;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.AssetElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.ControlFlowElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.DataFlowElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.LineageMappingElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.ProcessCallElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.ProcessElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.RelatedAssetElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.AssetProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.ControlFlowProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.DataFlowProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.LineageMappingProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.ProcessCallProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.ProcessContainmentType;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.ProcessProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.ProcessStatus;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.TemplateProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.ControlFlowElementResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.ControlFlowElementsResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.ControlFlowRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.DataFlowElementResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.DataFlowElementsResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.DataFlowRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.EffectiveTimeMetadataSourceRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.LineageMappingElementResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.LineageMappingElementsResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.LineageMappingRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.ProcessCallElementResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.ProcessCallElementsResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.ProcessCallRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.EffectiveTimeRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PlatformManagerClient supports the APIs to maintain software server platforms and their related objects.
 */
public class ProcessManagerClient extends AssetManagerClientBase implements ProcessManagerInterface
{
    private static final String processEntityType             = "Process";
    private static final String containmentTypePropertyName   = "containmentType";
    private static final String processHierarchyRelationship  = "ProcessHierarchy";
    private static final String businessSignificanceClassification = "BusinessSignificance";


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ProcessManagerClient(String   serverName,
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
    public ProcessManagerClient(String serverName,
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
    public ProcessManagerClient(String serverName,
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
    public ProcessManagerClient(String   serverName,
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
    public ProcessManagerClient(String                     serverName,
                                String                     serverPlatformURLRoot,
                                ITInfrastructureRESTClient restClient,
                                int                        maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, restClient, maxPageSize);
    }



    /* =====================================================================================================================
     * A process describes a well defined series of steps that gets something done.
     */

    /**
     * Create a new metadata element to represent a process.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param infrastructureManagerIsHome ensure that only the infrastructure manager can update this process
     * @param processStatus initial status of the process
     * @param processProperties properties about the process to store
     *
     * @return unique identifier of the new process
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createProcess(String            userId,
                                String            infrastructureManagerGUID,
                                String            infrastructureManagerName,
                                boolean           infrastructureManagerIsHome,
                                ProcessStatus     processStatus,
                                ProcessProperties processProperties) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String methodName = "createProcess";

        AssetProperties assetProperties = processProperties.cloneToAsset();

        ElementStatus initialStatus = null;

        if (processStatus != null)
        {
            initialStatus = processStatus.getElementStatus();
        }

        return super.createAsset(userId, infrastructureManagerGUID, infrastructureManagerName, infrastructureManagerIsHome, assetProperties, initialStatus, methodName);
    }


    /**
     * Create a new metadata element to represent a process using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param infrastructureManagerIsHome ensure that only the infrastructure manager can update this process
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new process
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createProcessFromTemplate(String             userId,
                                            String             infrastructureManagerGUID,
                                            String             infrastructureManagerName,
                                            boolean            infrastructureManagerIsHome,
                                            String             templateGUID,
                                            TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException

    {
        final String methodName = "createProcessFromTemplate";

        return super.createAssetFromTemplate(userId, infrastructureManagerGUID, infrastructureManagerName, infrastructureManagerIsHome, templateGUID, templateProperties, methodName);
    }


    /**
     * Update the metadata element representing a process.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param processGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param processProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateProcess(String            userId,
                              String            infrastructureManagerGUID,
                              String            infrastructureManagerName,
                              String            processGUID,
                              boolean           isMergeUpdate,
                              ProcessProperties processProperties) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException

    {
        final String methodName = "updateProcess";

        AssetProperties assetProperties = processProperties.cloneToAsset();

        super.updateAsset(userId, infrastructureManagerGUID, infrastructureManagerName, processGUID, isMergeUpdate, assetProperties, methodName);
    }


    /**
     * Update the status of the metadata element representing a process.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param processGUID unique identifier of the process to update
     * @param processStatus new status for the process
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateProcessStatus(String        userId,
                                    String        infrastructureManagerGUID,
                                    String        infrastructureManagerName,
                                    String        processGUID,
                                    ProcessStatus processStatus) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String methodName = "updateProcessStatus";
        final String statusParameterName = "processStatus";

        invalidParameterHandler.validateObject(processStatus, statusParameterName, methodName);

        super.updateAssetStatus(userId, infrastructureManagerGUID, infrastructureManagerName, processEntityType, processGUID, processStatus.getElementStatus(), methodName);
    }


    /**
     * Create a parent-child relationship between two processes.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param infrastructureManagerIsHome ensure that only the infrastructure manager can update this asset
     * @param parentProcessGUID unique identifier of the process in the external infrastructure manager that is to be the parent process
     * @param childProcessGUID unique identifier of the process in the external infrastructure manager that is to be the nested sub-process
     * @param containmentType describes the ownership of the sub-process
     * @param effectiveFrom time when this relationship is effective - null means immediately
     * @param effectiveTo time when this relationship is no longer effective - null means forever
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupProcessParent(String                 userId,
                                   String                 infrastructureManagerGUID,
                                   String                 infrastructureManagerName,
                                   boolean                infrastructureManagerIsHome,
                                   String                 parentProcessGUID,
                                   String                 childProcessGUID,
                                   ProcessContainmentType containmentType,
                                   Date                   effectiveFrom,
                                   Date                   effectiveTo) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName = "setupProcessParent";

        Map<String, Object> propertyMap = null;

        if (containmentType != null)
        {
            propertyMap = new HashMap<>();

            propertyMap.put(containmentTypePropertyName, containmentType);
        }

        super.setupRelatedAsset(userId, infrastructureManagerGUID, infrastructureManagerName, infrastructureManagerIsHome, processEntityType, parentProcessGUID, processHierarchyRelationship, processEntityType, childProcessGUID, effectiveFrom, effectiveTo, propertyMap, methodName);
    }


    /**
     * Remove a parent-child relationship between two processes.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param parentProcessGUID unique identifier of the process in the external infrastructure manager that is to be the parent process
     * @param childProcessGUID unique identifier of the process in the external infrastructure manager that is to be the nested sub-process
     * @param effectiveTime time when the relationship is effective
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearProcessParent(String userId,
                                   String infrastructureManagerGUID,
                                   String infrastructureManagerName,
                                   String parentProcessGUID,
                                   String childProcessGUID,
                                   Date   effectiveTime) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        final String methodName = "clearProcessParent";

        super.clearRelatedAsset(userId, infrastructureManagerGUID, infrastructureManagerName, processEntityType, parentProcessGUID, processHierarchyRelationship, processEntityType, childProcessGUID, effectiveTime, methodName);
    }


    /**
     * Update the zones for the asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the IT Infrastructure OMAS).
     *
     * @param userId calling user
     * @param processGUID unique identifier of the metadata element to publish
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void publishProcess(String userId,
                               String processGUID) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException

    {
        final String methodName = "publishProcess";

        super.publishAsset(userId, processGUID, methodName);
    }


    /**
     * Update the zones for the asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the IT Infrastructure OMAS.  This is the setting when the host is first created).
     *
     * @param userId calling user
     * @param processGUID unique identifier of the metadata element to withdraw
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void withdrawProcess(String userId,
                                String processGUID) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException

    {
        final String methodName = "withdrawProcess";

        super.withdrawAsset(userId, processGUID, methodName);
    }


    /**
     * Remove the metadata element representing a process.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param processGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeProcess(String userId,
                              String infrastructureManagerGUID,
                              String infrastructureManagerName,
                              String processGUID) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String methodName = "removeProcess";

        super.removeAsset(userId, infrastructureManagerGUID, infrastructureManagerName, processGUID, methodName);
    }


    /**
     * Retrieve the list of process metadata elements that contain the search string.
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
    public List<ProcessElement> findProcesses(String userId,
                                              String searchString,
                                              Date   effectiveTime,
                                              int    startFrom,
                                              int    pageSize) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName = "findProcesses";

        return this.convertAssetElements(super.findAssets(userId, searchString, processEntityType, effectiveTime, startFrom, pageSize, methodName));
    }



    /**
     * Retrieve the list of process metadata elements with a matching qualified or display name.
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
    public List<ProcessElement> getProcessesByName(String userId,
                                                   String name,
                                                   Date   effectiveTime,
                                                   int    startFrom,
                                                   int    pageSize) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName = "getProcessesByName";

        return this.convertAssetElements(super.getAssetsByName(userId, name, processEntityType, effectiveTime, startFrom, pageSize, methodName));
    }


    /**
     * Return the list of processes associated with the infrastructure manager.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of metadata elements describing the processes associated with the requested infrastructure manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ProcessElement> getProcessesForInfrastructureManager(String userId,
                                                                     String infrastructureManagerGUID,
                                                                     String infrastructureManagerName,
                                                                     Date   effectiveTime,
                                                                     int    startFrom,
                                                                     int    pageSize) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException

    {
        final String methodName = "getHostsForInfrastructureManager";

        return this.convertAssetElements(super.getAssetsForInfrastructureManager(userId, infrastructureManagerGUID, infrastructureManagerName, processEntityType, effectiveTime, startFrom, pageSize, methodName));
    }



    /**
     * Retrieve the process metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param processGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public ProcessElement getProcessByGUID(String userId,
                                           String processGUID) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName = "getProcessByGUID";

        return this.convertAssetElement(super.getAssetByGUID(userId, processEntityType, processGUID, null, methodName));
    }


    /**
     * Retrieve the process metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param processGUID unique identifier of the requested metadata element
     * @param effectiveTime effective time for the query
     *
     * @return parent process element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public ProcessElement getProcessParent(String userId,
                                           String processGUID,
                                           Date   effectiveTime) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String methodName = "getProcessParent";

        List<RelatedAssetElement> results = super.getRelatedAssets(userId, processEntityType, processGUID, 2, processHierarchyRelationship, processEntityType, effectiveTime, 0, 0, methodName);

        if ((results == null) || (results.isEmpty()))
        {
            return null;
        }

        RelatedAssetElement result = results.get(0);

        return this.convertAssetElement(result.getRelatedAsset());
    }


    /**
     * Retrieve the process metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param processGUID unique identifier of the requested metadata element
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of process element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ProcessElement> getSubProcesses(String userId,
                                                String processGUID,
                                                Date   effectiveTime,
                                                int    startFrom,
                                                int    pageSize) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String methodName = "getProcessParent";

        List<RelatedAssetElement> results = super.getRelatedAssets(userId, processEntityType, processGUID, 2, processHierarchyRelationship, processEntityType, effectiveTime, startFrom, pageSize, methodName);

        if ((results == null) || (results.isEmpty()))
        {
            return null;
        }

        List<ProcessElement> subProcesses = new ArrayList<>();

        for (RelatedAssetElement relatedAssetElement : results)
        {
            if (relatedAssetElement != null)
            {
                subProcesses.add(this.convertAssetElement(relatedAssetElement.getRelatedAsset()));
            }
        }

        return subProcesses;
    }


    /* ===============================================================================
     * General linkage and classifications
     */


    /**
     * Classify a port, process or asset as "BusinessSignificant" (this may effect the way that lineage is displayed).
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param elementGUID unique identifier of the metadata element to update
     * @param effectiveFrom time when this hosting is effective - null means immediately
     * @param effectiveTo time when this hosting is no longer effective - null means forever
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setBusinessSignificant(String userId,
                                       String infrastructureManagerGUID,
                                       String infrastructureManagerName,
                                       String elementGUID,
                                       Date   effectiveFrom,
                                       Date   effectiveTo) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String methodName = "setBusinessSignificant";

        super.addClassification(userId, infrastructureManagerGUID, infrastructureManagerName, false, referencableTypeName, elementGUID, businessSignificanceClassification, effectiveFrom, effectiveTo, null, methodName);
    }


    /**
     * Remove the "BusinessSignificant" designation from the element.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param effectiveTime effective time for the query
     * @param elementGUID unique identifier of the metadata element to update
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearBusinessSignificant(String userId,
                                         String infrastructureManagerGUID,
                                         String infrastructureManagerName,
                                         String elementGUID,
                                         Date   effectiveTime) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName = "clearBusinessSignificant";

        super.clearClassification(userId, infrastructureManagerGUID, infrastructureManagerName, referencableTypeName, elementGUID, businessSignificanceClassification, effectiveTime, methodName);
    }


    /**
     * Link two elements together to show that data flows from one to the other.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param infrastructureManagerIsHome ensure that only the infrastructure manager can update this asset
     * @param dataSupplierGUID unique identifier of the data supplier
     * @param dataConsumerGUID unique identifier of the data consumer
     * @param properties unique identifier for this relationship along with description and/or additional relevant properties
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String setupDataFlow(String             userId,
                                String             infrastructureManagerGUID,
                                String             infrastructureManagerName,
                                boolean            infrastructureManagerIsHome,
                                String             dataSupplierGUID,
                                String             dataConsumerGUID,
                                DataFlowProperties properties,
                                Date               effectiveTime) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException

    {
        final String methodName                    = "setupDataFlow";
        final String dataSupplierGUIDParameterName = "dataSupplierGUID";
        final String dataConsumerGUIDParameterName = "dataConsumerGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataSupplierGUID, dataSupplierGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(dataConsumerGUID, dataConsumerGUIDParameterName, methodName);

        DataFlowRequestBody requestBody = new DataFlowRequestBody();

        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);
        requestBody.setProperties(properties);
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/data-flows/suppliers/{2}/consumers/{3}?assetManagerIsHome={4}";

        GUIDResponse results = restClient.callGUIDPostRESTCall(methodName,
                                                               urlTemplate,
                                                               requestBody,
                                                               serverName,
                                                               userId,
                                                               dataSupplierGUID,
                                                               dataConsumerGUID,
                                                               infrastructureManagerIsHome);

        return results.getGUID();
    }


    /**
     * Retrieve the data flow relationship between two elements.  The qualifiedName is optional unless there
     * is more than one data flow relationships between these two elements since it is used to disambiguate
     * the request. This is often used in conjunction with update.
     *
     * @param userId calling user
     * @param dataSupplierGUID unique identifier of the data supplier
     * @param dataConsumerGUID unique identifier of the data consumer
     * @param qualifiedName unique identifier for this relationship
     * @param effectiveTime time when the hosting is effective
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public DataFlowElement getDataFlow(String userId,
                                       String dataSupplierGUID,
                                       String dataConsumerGUID,
                                       String qualifiedName,
                                       Date   effectiveTime) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException

    {
        final String methodName                    = "getDataFlow";
        final String dataSupplierGUIDParameterName = "dataSupplierGUID";
        final String dataConsumerGUIDParameterName = "dataConsumerGUID";
        final String qualifiedNameParameterName    = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataSupplierGUID, dataSupplierGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(dataConsumerGUID, dataConsumerGUIDParameterName, methodName);

        NameRequestBody requestBody = new NameRequestBody();
        requestBody.setName(qualifiedName);
        requestBody.setNameParameterName(qualifiedNameParameterName);
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/data-flows/suppliers/{2}/consumers/{3}/retrieve";

        DataFlowElementResponse restResult = restClient.callDataFlowPostRESTCall(methodName,
                                                                                 urlTemplate,
                                                                                 requestBody,
                                                                                 serverName,
                                                                                 userId,
                                                                                 dataSupplierGUID,
                                                                                 dataConsumerGUID);

        return restResult.getElement();
    }


    /**
     * Update relationship between two elements that shows that data flows from one to the other.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param dataFlowGUID unique identifier of the data flow relationship
     * @param properties unique identifier for this relationship along with description and/or additional relevant properties
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateDataFlow(String             userId,
                               String             infrastructureManagerGUID,
                               String             infrastructureManagerName,
                               String             dataFlowGUID,
                               DataFlowProperties properties,
                               Date               effectiveTime) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException

    {
        final String methodName                    = "updateDataFlow";
        final String dataFlowGUIDParameterName     = "dataFlowGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataFlowGUID, dataFlowGUIDParameterName, methodName);

        DataFlowRequestBody requestBody = new DataFlowRequestBody();
        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);
        requestBody.setProperties(properties);
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/data-flows/{2}/update";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        dataFlowGUID);
    }


    /**
     * Remove the data flow relationship between two elements.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param dataFlowGUID unique identifier of the data flow relationship
     * @param effectiveTime time when the relationship is effective
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearDataFlow(String userId,
                              String infrastructureManagerGUID,
                              String infrastructureManagerName,
                              String dataFlowGUID,
                              Date   effectiveTime) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException

    {
        final String methodName                = "clearDataFlow";
        final String dataFlowGUIDParameterName = "dataFlowGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataFlowGUID, dataFlowGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/data-flows/{2}/remove";

        EffectiveTimeMetadataSourceRequestBody requestBody = new EffectiveTimeMetadataSourceRequestBody();
        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);
        requestBody.setEffectiveTime(effectiveTime);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        dataFlowGUID);
    }


    /**
     * Retrieve the data flow relationships linked from a specific element to the downstream consumers.
     *
     * @param userId calling user
     * @param dataSupplierGUID unique identifier of the data supplier
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime time when the hosting is effective
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<DataFlowElement> getDataFlowConsumers(String userId,
                                                      String dataSupplierGUID,
                                                      int    startFrom,
                                                      int    pageSize,
                                                      Date   effectiveTime) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException

    {
        final String methodName                    = "getDataFlowConsumers";
        final String dataSupplierGUIDParameterName = "dataSupplierGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataSupplierGUID, dataSupplierGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/data-flows/suppliers/{2}/consumers/retrieve?startFrom={3}&pageSize={4}";

        EffectiveTimeRequestBody requestBody = new EffectiveTimeRequestBody();
        requestBody.setEffectiveTime(effectiveTime);

        DataFlowElementsResponse restResult = restClient.callDataFlowsPostRESTCall(methodName,
                                                                                   urlTemplate,
                                                                                   requestBody,
                                                                                   serverName,
                                                                                   userId,
                                                                                   dataSupplierGUID,
                                                                                   startFrom,
                                                                                   pageSize);

        return restResult.getElementList();
    }


    /**
     * Retrieve the data flow relationships linked from a specific element to the upstream suppliers.
     *
     * @param userId calling user
     * @param dataConsumerGUID unique identifier of the data consumer
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime time when the hosting is effective
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<DataFlowElement> getDataFlowSuppliers(String userId,
                                                      String dataConsumerGUID,
                                                      int    startFrom,
                                                      int    pageSize,
                                                      Date   effectiveTime) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException

    {
        final String methodName                    = "getDataFlowSuppliers";
        final String dataConsumerGUIDParameterName = "dataConsumerGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataConsumerGUID, dataConsumerGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/data-flows/consumers/{2}/suppliers/retrieve?startFrom={3}&pageSize={4}";

        EffectiveTimeRequestBody requestBody = new EffectiveTimeRequestBody();
        requestBody.setEffectiveTime(effectiveTime);

        DataFlowElementsResponse restResult = restClient.callDataFlowsPostRESTCall(methodName,
                                                                                   urlTemplate,
                                                                                   requestBody,
                                                                                   serverName,
                                                                                   userId,
                                                                                   dataConsumerGUID,
                                                                                   startFrom,
                                                                                   pageSize);

        return restResult.getElementList();
    }


    /**
     * Link two elements to show that when one completes the next is started.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param infrastructureManagerIsHome ensure that only the infrastructure manager can update this asset
     * @param currentStepGUID unique identifier of the previous step
     * @param nextStepGUID unique identifier of the next step
     * @param properties unique identifier for this relationship along with description and/or additional relevant properties
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier for the control flow relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String setupControlFlow(String                userId,
                                   String                infrastructureManagerGUID,
                                   String                infrastructureManagerName,
                                   boolean               infrastructureManagerIsHome,
                                   String                currentStepGUID,
                                   String                nextStepGUID,
                                   ControlFlowProperties properties,
                                   Date                  effectiveTime) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException

    {
        final String methodName                   = "setupControlFlow";
        final String currentStepGUIDParameterName = "currentStepGUID";
        final String nextStepGUIDParameterName    = "nextStepGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(currentStepGUID, currentStepGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(nextStepGUID, nextStepGUIDParameterName, methodName);

        ControlFlowRequestBody requestBody = new ControlFlowRequestBody();
        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);
        requestBody.setProperties(properties);
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/control-flows/current-steps/{2}/next-steps/{3}?assetManagerIsHome={4}";

        GUIDResponse results = restClient.callGUIDPostRESTCall(methodName,
                                                               urlTemplate,
                                                               requestBody,
                                                               serverName,
                                                               userId,
                                                               currentStepGUID,
                                                               nextStepGUID,
                                                               infrastructureManagerIsHome);

        return results.getGUID();
    }


    /**
     * Retrieve the control flow relationship between two elements.  The qualifiedName is optional unless there
     * is more than one control flow relationships between these two elements since it is used to disambiguate
     * the request.  This is often used in conjunction with update.
     *
     * @param userId calling user
     * @param currentStepGUID unique identifier of the previous step
     * @param nextStepGUID unique identifier of the next step
     * @param qualifiedName unique identifier for this relationship
     * @param effectiveTime time when the hosting is effective
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public ControlFlowElement getControlFlow(String userId,
                                             String currentStepGUID,
                                             String nextStepGUID,
                                             String qualifiedName,
                                             Date   effectiveTime) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException

    {
        final String methodName                   = "getControlFlow";
        final String currentStepGUIDParameterName = "currentStepGUID";
        final String nextStepGUIDParameterName    = "nextStepGUID";
        final String qualifiedNameParameterName   = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(currentStepGUID, currentStepGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(nextStepGUID, nextStepGUIDParameterName, methodName);

        NameRequestBody requestBody = new NameRequestBody();
        requestBody.setEffectiveTime(effectiveTime);
        requestBody.setName(qualifiedName);
        requestBody.setNameParameterName(qualifiedNameParameterName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/control-flows/current-steps/{2}/next-steps/{3}/retrieve";

        ControlFlowElementResponse restResult = restClient.callControlFlowPostRESTCall(methodName,
                                                                                       urlTemplate,
                                                                                       requestBody,
                                                                                       serverName,
                                                                                       userId,
                                                                                       currentStepGUID,
                                                                                       nextStepGUID);

        return restResult.getElement();
    }


    /**
     * Update the relationship between two elements that shows that when one completes the next is started.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param controlFlowGUID unique identifier of the  control flow relationship
     * @param properties unique identifier for this relationship along with description and/or additional relevant properties
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateControlFlow(String                userId,
                                  String                infrastructureManagerGUID,
                                  String                infrastructureManagerName,
                                  String                controlFlowGUID,
                                  ControlFlowProperties properties,
                                  Date                  effectiveTime) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException

    {
        final String methodName                   = "updateControlFlow";
        final String controlFlowGUIDParameterName = "controlFlowGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(controlFlowGUID, controlFlowGUIDParameterName, methodName);

        ControlFlowRequestBody requestBody = new ControlFlowRequestBody();
        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);
        requestBody.setProperties(properties);
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/control-flows/{2}/update";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        controlFlowGUID);
    }


    /**
     * Remove the control flow relationship between two elements.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param controlFlowGUID unique identifier of the  control flow relationship
     * @param effectiveTime time when the relationship is effective
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearControlFlow(String userId,
                                 String infrastructureManagerGUID,
                                 String infrastructureManagerName,
                                 String controlFlowGUID,
                                 Date   effectiveTime) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException

    {
        final String methodName                   = "clearControlFlow";
        final String controlFlowGUIDParameterName = "controlFlowGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(controlFlowGUID, controlFlowGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/control-flows/{2}/remove";

        EffectiveTimeMetadataSourceRequestBody requestBody = new EffectiveTimeMetadataSourceRequestBody();
        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);
        requestBody.setEffectiveTime(effectiveTime);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        controlFlowGUID);
    }


    /**
     * Retrieve the control relationships linked from a specific element to the possible next elements in the process.
     *
     * @param userId calling user
     * @param currentStepGUID unique identifier of the current step
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime time when the hosting is effective
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ControlFlowElement> getControlFlowNextSteps(String userId,
                                                            String currentStepGUID,
                                                            int    startFrom,
                                                            int    pageSize,
                                                            Date   effectiveTime) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException

    {
        final String methodName                   = "getControlFlowNextSteps";
        final String currentStepGUIDParameterName = "currentStepGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(currentStepGUID, currentStepGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/control-flows/current-steps/{2}/next-steps/retrieve?startFrom={3}&pageSize={4}";

        EffectiveTimeRequestBody requestBody = new EffectiveTimeRequestBody();
        requestBody.setEffectiveTime(effectiveTime);

        ControlFlowElementsResponse restResult = restClient.callControlFlowsPostRESTCall(methodName,
                                                                                         urlTemplate,
                                                                                         requestBody,
                                                                                         serverName,
                                                                                         userId,
                                                                                         currentStepGUID,
                                                                                         startFrom,
                                                                                         pageSize);

        return restResult.getElementList();
    }


    /**
     * Retrieve the control relationships linked from a specific element to the possible previous elements in the process.
     *
     * @param userId calling user
     * @param currentStepGUID unique identifier of the current step
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime time when the hosting is effective
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ControlFlowElement> getControlFlowPreviousSteps(String userId,
                                                                String currentStepGUID,
                                                                int    startFrom,
                                                                int    pageSize,
                                                                Date   effectiveTime) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException

    {
        final String methodName                   = "getControlFlowPreviousSteps";
        final String currentStepGUIDParameterName = "currentStepGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(currentStepGUID, currentStepGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/control-flows/current-steps/{2}/previous-steps/retrieve?startFrom={3}&pageSize={4}";

        EffectiveTimeRequestBody requestBody = new EffectiveTimeRequestBody();
        requestBody.setEffectiveTime(effectiveTime);

        ControlFlowElementsResponse restResult = restClient.callControlFlowsPostRESTCall(methodName,
                                                                                         urlTemplate,
                                                                                         requestBody,
                                                                                         serverName,
                                                                                         userId,
                                                                                         currentStepGUID,
                                                                                         startFrom,
                                                                                         pageSize);

        return restResult.getElementList();
    }


    /**
     * Link two elements together to show a request-response call between them.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param infrastructureManagerIsHome ensure that only the infrastructure manager can update this asset
     * @param callerGUID unique identifier of the element that is making the call
     * @param calledGUID unique identifier of the element that is processing the call
     * @param properties unique identifier for this relationship along with description and/or additional relevant properties
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier of the new relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String setupProcessCall(String                userId,
                                   String                infrastructureManagerGUID,
                                   String                infrastructureManagerName,
                                   boolean               infrastructureManagerIsHome,
                                   String                callerGUID,
                                   String                calledGUID,
                                   ProcessCallProperties properties,
                                   Date                  effectiveTime) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException

    {
        final String methodName              = "setupProcessCall";
        final String callerGUIDParameterName = "callerGUID";
        final String calledGUIDParameterName = "calledGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(callerGUID, callerGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(calledGUID, calledGUIDParameterName, methodName);

        ProcessCallRequestBody requestBody = new ProcessCallRequestBody();
        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);
        requestBody.setProperties(properties);
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/process-calls/callers/{2}/called/{3}?assetManagerIsHome={4}";

        GUIDResponse results = restClient.callGUIDPostRESTCall(methodName,
                                                               urlTemplate,
                                                               requestBody,
                                                               serverName,
                                                               userId,
                                                               callerGUID,
                                                               calledGUID,
                                                               infrastructureManagerIsHome);

        return results.getGUID();
    }


    /**
     * Retrieve the process call relationship between two elements.  The qualifiedName is optional unless there
     * is more than one process call relationships between these two elements since it is used to disambiguate
     * the request.  This is often used in conjunction with update.
     *
     * @param userId calling user
     * @param callerGUID unique identifier of the element that is making the call
     * @param calledGUID unique identifier of the element that is processing the call
     * @param qualifiedName unique identifier for this relationship
     * @param effectiveTime time when the hosting is effective
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public ProcessCallElement getProcessCall(String userId,
                                             String callerGUID,
                                             String calledGUID,
                                             String qualifiedName,
                                             Date   effectiveTime) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String methodName                 = "getProcessCall";
        final String callerGUIDParameterName    = "callerGUID";
        final String calledGUIDParameterName    = "calledGUID";
        final String qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(callerGUID, callerGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(calledGUID, calledGUIDParameterName, methodName);

        NameRequestBody requestBody = new NameRequestBody();
        requestBody.setName(qualifiedName);
        requestBody.setNameParameterName(qualifiedNameParameterName);
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/process-calls/callers/{2}/called/{3}/retrieve";

        ProcessCallElementResponse restResult = restClient.callProcessCallPostRESTCall(methodName,
                                                                                       urlTemplate,
                                                                                       requestBody,
                                                                                       serverName,
                                                                                       userId,
                                                                                       callerGUID,
                                                                                       calledGUID);

        return restResult.getElement();
    }


    /**
     * Update the relationship between two elements that shows a request-response call between them.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param processCallGUID unique identifier of the process call relationship
     * @param properties unique identifier for this relationship along with description and/or additional relevant properties
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateProcessCall(String                userId,
                                  String                infrastructureManagerGUID,
                                  String                infrastructureManagerName,
                                  String                processCallGUID,
                                  ProcessCallProperties properties,
                                  Date                  effectiveTime) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName                   = "updateProcessCall";
        final String processCallGUIDParameterName = "processCallGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processCallGUID, processCallGUIDParameterName, methodName);

        ProcessCallRequestBody requestBody = new ProcessCallRequestBody();
        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);
        requestBody.setProperties(properties);
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/process-calls/{2}/update";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        processCallGUID);
    }


    /**
     * Remove the process call relationship.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param processCallGUID unique identifier of the process call relationship
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearProcessCall(String userId,
                                 String infrastructureManagerGUID,
                                 String infrastructureManagerName,
                                 String processCallGUID,
                                 Date   effectiveTime) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException

    {
        final String methodName                   = "clearProcessCall";
        final String processCallGUIDParameterName = "processCallGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processCallGUID, processCallGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/process-calls/{2}/remove";

        EffectiveTimeMetadataSourceRequestBody requestBody = new EffectiveTimeMetadataSourceRequestBody();
        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);
        requestBody.setEffectiveTime(effectiveTime);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        processCallGUID);
    }


    /**
     * Retrieve the process call relationships linked from a specific element to the elements it calls.
     *
     * @param userId calling user
     * @param callerGUID unique identifier of the element that is making the call
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime time when the hosting is effective
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ProcessCallElement> getProcessCalled(String userId,
                                                     String callerGUID,
                                                     int    startFrom,
                                                     int    pageSize,
                                                     Date   effectiveTime) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName                 = "getProcessCalled";
        final String callerGUIDParameterName    = "callerGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(callerGUID, callerGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/process-calls/callers/{2}/called/retrieve?startFrom={3}&pageSize={4}";

        EffectiveTimeRequestBody requestBody = new EffectiveTimeRequestBody();
        requestBody.setEffectiveTime(effectiveTime);

        ProcessCallElementsResponse restResult = restClient.callProcessCallsPostRESTCall(methodName,
                                                                                         urlTemplate,
                                                                                         requestBody,
                                                                                         serverName,
                                                                                         userId,
                                                                                         callerGUID,
                                                                                         startFrom,
                                                                                         pageSize);

        return restResult.getElementList();
    }


    /**
     * Retrieve the process call relationships linked from a specific element to its callers.
     *
     * @param userId calling user
     * @param calledGUID unique identifier of the element that is processing the call
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime time when the hosting is effective
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ProcessCallElement> getProcessCallers(String userId,
                                                      String calledGUID,
                                                      int    startFrom,
                                                      int    pageSize,
                                                      Date   effectiveTime) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName                 = "getProcessCallers";
        final String calledGUIDParameterName    = "calledGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(calledGUID, calledGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/process-calls/called/{2}/callers/retrieve?startFrom={3}&pageSize={4}";

        EffectiveTimeRequestBody requestBody = new EffectiveTimeRequestBody();
        requestBody.setEffectiveTime(effectiveTime);

        ProcessCallElementsResponse restResult = restClient.callProcessCallsPostRESTCall(methodName,
                                                                                         urlTemplate,
                                                                                         requestBody,
                                                                                         serverName,
                                                                                         userId,
                                                                                         calledGUID,
                                                                                         startFrom,
                                                                                         pageSize);

        return restResult.getElementList();
    }


    /**
     * Link two elements together to show that they are part of the lineage of the data that is moving
     * between the processes.  Typically, the lineage relationships stitch together processes and data assets
     * supported by different technologies.
     *
     * @param userId calling user
     * @param sourceElementGUID unique identifier of the source
     * @param destinationElementGUID unique identifier of the destination
     * @param properties unique identifier for this relationship along with description and/or additional relevant properties
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier of the new relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String setupLineageMapping(String                   userId,
                                      String                   sourceElementGUID,
                                      String                   destinationElementGUID,
                                      LineageMappingProperties properties,
                                      Date                     effectiveTime) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName                          = "setupLineageMapping";
        final String sourceElementGUIDParameterName      = "sourceElementGUID";
        final String destinationElementGUIDParameterName = "destinationElementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(sourceElementGUID, sourceElementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(destinationElementGUID, destinationElementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/lineage-mappings/sources/{2}/destinations/{3}";

        LineageMappingRequestBody requestBody = new LineageMappingRequestBody();
        requestBody.setProperties(properties);
        requestBody.setEffectiveTime(effectiveTime);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  sourceElementGUID,
                                                                  destinationElementGUID);

        return restResult.getGUID();
    }




    /**
     * Retrieve the lineage mapping relationship between two elements.  The qualifiedName is optional unless there
     * is more than one lineage mapping relationship between these two elements since it is used to disambiguate
     * the request.  This is often used in conjunction with update.
     *
     * @param userId calling user
     * @param sourceElementGUID unique identifier of the source
     * @param destinationElementGUID unique identifier of the destination
     * @param qualifiedName unique identifier for this relationship
     * @param effectiveTime effective time for the query
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public LineageMappingElement getLineageMapping(String userId,
                                                   String sourceElementGUID,
                                                   String destinationElementGUID,
                                                   String qualifiedName,
                                                   Date   effectiveTime) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName                          = "getLineageMapping";
        final String qualifiedNameParameterName          = "qualifiedName";
        final String sourceElementGUIDParameterName      = "sourceElementGUID";
        final String destinationElementGUIDParameterName = "destinationElementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(sourceElementGUID, sourceElementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(destinationElementGUID, destinationElementGUIDParameterName, methodName);

        NameRequestBody requestBody = new NameRequestBody();
        requestBody.setName(qualifiedName);
        requestBody.setNameParameterName(qualifiedNameParameterName);
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/lineage-mappings/sources/{2}/destinations/{3}/retrieve";

        LineageMappingElementResponse restResult = restClient.callLineageMappingPostRESTCall(methodName,
                                                                                             urlTemplate,
                                                                                             requestBody,
                                                                                             serverName,
                                                                                             userId,
                                                                                             sourceElementGUID,
                                                                                             destinationElementGUID);

        return restResult.getElement();
    }


    /**
     * Update the lineage mapping relationship between two elements.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param lineageMappingGUID unique identifier of the relationship
     * @param properties qualified name for this relationship and other related properties
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateLineageMapping(String                   userId,
                                     String                   infrastructureManagerGUID,
                                     String                   infrastructureManagerName,
                                     String                   lineageMappingGUID,
                                     LineageMappingProperties properties,
                                     Date                     effectiveTime) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String methodName                      = "updateLineageMapping";
        final String lineageMappingGUIDParameterName = "lineageMappingGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(lineageMappingGUID, lineageMappingGUIDParameterName, methodName);

        LineageMappingRequestBody requestBody = new LineageMappingRequestBody();
        requestBody.setExternalSourceGUID(infrastructureManagerGUID);
        requestBody.setExternalSourceName(infrastructureManagerName);
        requestBody.setProperties(properties);
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/lineage-mappings/{2}/update";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        lineageMappingGUID);
    }


    /**
     * Remove the lineage mapping between two elements.
     *
     * @param userId calling user
     * @param lineageMappingGUID unique identifier of the relationship
     * @param effectiveTime time when the relationship is effective
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearLineageMapping(String userId,
                                    String lineageMappingGUID,
                                    Date   effectiveTime) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String methodName                      = "clearLineageMapping";
        final String lineageMappingGUIDParameterName = "lineageMapping";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(lineageMappingGUID, lineageMappingGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/lineage-mappings/{2}/remove";

        EffectiveTimeMetadataSourceRequestBody requestBody = new EffectiveTimeMetadataSourceRequestBody();
        requestBody.setEffectiveTime(effectiveTime);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        lineageMappingGUID);
    }


    /**
     * Retrieve the lineage mapping relationships linked from a specific source element to its destinations.
     *
     * @param userId calling user
     * @param sourceElementGUID unique identifier of the source
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime time when the hosting is effective
     *
     * @return list of lineage mapping relationships
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<LineageMappingElement> getDestinationLineageMappings(String userId,
                                                                     String sourceElementGUID,
                                                                     int    startFrom,
                                                                     int    pageSize,
                                                                     Date   effectiveTime) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String methodName                          = "getDestinationLineageMappings";
        final String sourceElementGUIDParameterName      = "sourceElementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(sourceElementGUID, sourceElementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/lineage-mappings/sources/{2}/destinations/retrieve?startFrom={3}&pageSize={4}";

        EffectiveTimeRequestBody requestBody = new EffectiveTimeRequestBody();
        requestBody.setEffectiveTime(effectiveTime);

        LineageMappingElementsResponse results = restClient.callLineageMappingsPostRESTCall(methodName,
                                                                                            urlTemplate,
                                                                                            requestBody,
                                                                                            serverName,
                                                                                            userId,
                                                                                            sourceElementGUID,
                                                                                            startFrom,
                                                                                            pageSize);

        return results.getElementList();
    }


    /**
     * Retrieve the lineage mapping relationships linked from a specific destination element to its sources.
     *
     * @param userId calling user
     * @param destinationElementGUID unique identifier of the destination
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime time when the hosting is effective
     *
     * @return list of lineage mapping relationships
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<LineageMappingElement> getSourceLineageMappings(String userId,
                                                                String destinationElementGUID,
                                                                int    startFrom,
                                                                int    pageSize,
                                                                Date   effectiveTime) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName                          = "getSourceLineageMappings";
        final String destinationElementGUIDParameterName = "destinationElementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(destinationElementGUID, destinationElementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + baseURLTemplatePrefix + "/lineage-mappings/destinations/{2}/sources/retrieve?startFrom={3}&pageSize={4}";

        EffectiveTimeRequestBody requestBody = new EffectiveTimeRequestBody();
        requestBody.setEffectiveTime(effectiveTime);

        LineageMappingElementsResponse results = restClient.callLineageMappingsPostRESTCall(methodName,
                                                                                            urlTemplate,
                                                                                            requestBody,
                                                                                            serverName,
                                                                                            userId,
                                                                                            destinationElementGUID,
                                                                                            startFrom,
                                                                                            pageSize);

        return results.getElementList();
    }


    /**
     * Convert a list of AssetElements into a list of ProcessElements.
     *
     * @param assetElements returned assets
     * @return result for caller
     */
    private List<ProcessElement> convertAssetElements(List<AssetElement> assetElements)
    {
        if (assetElements != null)
        {
            List<ProcessElement> hostElements = new ArrayList<>();

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
     * Convert a single AssetElement to a ProcessElement.
     *
     * @param assetElement return asset
     * @return result for caller
     */
    private ProcessElement convertAssetElement(AssetElement assetElement)
    {
        if (assetElement != null)
        {
            return new ProcessElement(assetElement);
        }

        return null;
    }
}
