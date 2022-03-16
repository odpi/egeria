/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.client;

import org.odpi.openmetadata.accessservices.itinfrastructure.api.ProcessManagerInterface;
import org.odpi.openmetadata.accessservices.itinfrastructure.client.rest.ITInfrastructureRESTClient;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.AssetElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.ControlFlowElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.DataFlowElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.ElementStatus;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.LineageMappingElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.ProcessCallElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.ProcessElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.RelatedAssetElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.AssetProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.ProcessContainmentType;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.ProcessProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.ProcessStatus;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.TemplateProperties;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

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
     * @param qualifiedName unique identifier for this relationship
     * @param description description and/or purpose of the data flow
     * @param formula function that determines the subset of the data that flows
     * @param effectiveFrom time when this hosting is effective - null means immediately
     * @param effectiveTo time when this hosting is no longer effective - null means forever
     *
     * @return unique identifier of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String setupDataFlow(String  userId,
                                String  infrastructureManagerGUID,
                                String  infrastructureManagerName,
                                boolean infrastructureManagerIsHome,
                                String  dataSupplierGUID,
                                String  dataConsumerGUID,
                                String  qualifiedName,
                                String  description,
                                String  formula,
                                Date    effectiveFrom,
                                Date    effectiveTo) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException

    {
        return null;
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
        return null;
    }


    /**
     * Update relationship between two elements that shows that data flows from one to the other.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param dataFlowGUID unique identifier of the data flow relationship
     * @param qualifiedName unique identifier for this relationship
     * @param description description and/or purpose of the data flow
     * @param formula function that determines the subset of the data that flows
     * @param effectiveFrom time when this hosting is effective - null means immediately
     * @param effectiveTo time when this hosting is no longer effective - null means forever
     *
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateDataFlow(String userId,
                               String infrastructureManagerGUID,
                               String infrastructureManagerName,
                               String dataFlowGUID,
                               String qualifiedName,
                               String description,
                               String formula,
                               Date   effectiveFrom,
                               Date   effectiveTo) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException

    {
    }


    /**
     * Remove the data flow relationship between two elements.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param dataFlowGUID unique identifier of the data flow relationship

     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearDataFlow(String userId,
                              String infrastructureManagerGUID,
                              String infrastructureManagerName,
                              String dataFlowGUID) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException

    {
    }


    /**
     * Retrieve the data flow relationships linked from an specific element to the downstream consumers.
     *
     * @param userId calling user
     * @param dataSupplierGUID unique identifier of the data supplier
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
                                                      Date   effectiveTime) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException

    {
        return null;
    }


    /**
     * Retrieve the data flow relationships linked from an specific element to the upstream suppliers.
     *
     * @param userId calling user
     * @param dataConsumerGUID unique identifier of the data consumer
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
                                                      Date   effectiveTime) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException

    {
        return null;
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
     * @param qualifiedName unique identifier for this relationship
     * @param description description and/or purpose of the data flow
     * @param guard function that must be true to travel down this control flow
     * @param effectiveFrom time when this hosting is effective - null means immediately
     * @param effectiveTo time when this hosting is no longer effective - null means forever
     *
     * @return unique identifier for the control flow relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String setupControlFlow(String  userId,
                                   String  infrastructureManagerGUID,
                                   String  infrastructureManagerName,
                                   boolean infrastructureManagerIsHome,
                                   String  currentStepGUID,
                                   String  nextStepGUID,
                                   String  qualifiedName,
                                   String  description,
                                   String  guard,
                                   Date    effectiveFrom,
                                   Date    effectiveTo) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException

    {
        return null;
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
        return null;
    }


    /**
     * Update the relationship between two elements that shows that when one completes the next is started.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param controlFlowGUID unique identifier of the  control flow relationship
     * @param qualifiedName unique identifier for this relationship
     * @param description description and/or purpose of the data flow
     * @param guard function that must be true to travel down this control flow
     * @param effectiveFrom time when this hosting is effective - null means immediately
     * @param effectiveTo time when this hosting is no longer effective - null means forever
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateControlFlow(String userId,
                                  String infrastructureManagerGUID,
                                  String infrastructureManagerName,
                                  String controlFlowGUID,
                                  String qualifiedName,
                                  String description,
                                  String guard,
                                  Date   effectiveFrom,
                                  Date   effectiveTo) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException

    {
    }


    /**
     * Remove the control flow relationship between two elements.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param controlFlowGUID unique identifier of the  control flow relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearControlFlow(String userId,
                                 String infrastructureManagerGUID,
                                 String infrastructureManagerName,
                                 String controlFlowGUID) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException

    {
    }


    /**
     * Retrieve the control relationships linked from an specific element to the possible next elements in the process.
     *
     * @param userId calling user
     * @param currentStepGUID unique identifier of the current step
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
                                                            Date   effectiveTime) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException

    {
        return null;
    }


    /**
     * Retrieve the control relationships linked from an specific element to the possible previous elements in the process.
     *
     * @param userId calling user
     * @param currentStepGUID unique identifier of the current step
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
                                                                Date   effectiveTime) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException

    {
        return null;
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
     * @param qualifiedName unique identifier for this relationship
     * @param description description and/or purpose of the data flow
     * @param formula function that determines the subset of the data that flows
     * @param effectiveFrom time when this hosting is effective - null means immediately
     * @param effectiveTo time when this hosting is no longer effective - null means forever
     *
     * @return unique identifier of the new relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String setupProcessCall(String  userId,
                                   String  infrastructureManagerGUID,
                                   String  infrastructureManagerName,
                                   boolean infrastructureManagerIsHome,
                                   String  callerGUID,
                                   String  calledGUID,
                                   String  qualifiedName,
                                   String  description,
                                   String  formula,
                                   Date    effectiveFrom,
                                   Date    effectiveTo) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException

    {
        return null;
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
        return null;
    }


    /**
     * Update the relationship between two elements that shows a request-response call between them.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param processCallGUID unique identifier of the process call relationship
     * @param qualifiedName unique identifier for this relationship
     * @param description description and/or purpose of the data flow
     * @param formula function that determines the subset of the data that flows
     * @param effectiveFrom time when this hosting is effective - null means immediately
     * @param effectiveTo time when this hosting is no longer effective - null means forever
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateProcessCall(String userId,
                                  String infrastructureManagerGUID,
                                  String infrastructureManagerName,
                                  String processCallGUID,
                                  String qualifiedName,
                                  String description,
                                  String formula,
                                  Date   effectiveFrom,
                                  Date   effectiveTo) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
    }


    /**
     * Remove the process call relationship.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param processCallGUID unique identifier of the process call relationship

     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearProcessCall(String userId,
                                 String infrastructureManagerGUID,
                                 String infrastructureManagerName,
                                 String processCallGUID) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException

    {
    }


    /**
     * Retrieve the process call relationships linked from an specific element to the elements it calls.
     *
     * @param userId calling user
     * @param callerGUID unique identifier of the element that is making the call
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
                                                     Date   effectiveTime) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        return null;
    }


    /**
     * Retrieve the process call relationships linked from an specific element to its callers.
     *
     * @param userId calling user
     * @param calledGUID unique identifier of the element that is processing the call
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
                                                      Date   effectiveTime) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        return null;
    }


    /**
     * Link two elements together to show that they are part of the lineage of the data that is moving
     * between the processes.  Typically the lineage relationships stitch together processes and data assets
     * supported by different technologies.
     *
     * @param userId calling user
     * @param sourceElementGUID unique identifier of the source
     * @param destinationElementGUID unique identifier of the destination
     * @param effectiveFrom time when this hosting is effective - null means immediately
     * @param effectiveTo time when this hosting is no longer effective - null means forever
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupLineageMapping(String userId,
                                    String sourceElementGUID,
                                    String destinationElementGUID,
                                    Date   effectiveFrom,
                                    Date   effectiveTo) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
    }


    /**
     * Remove the lineage mapping between two elements.
     *
     * @param userId calling user
     * @param sourceElementGUID unique identifier of the source
     * @param destinationElementGUID unique identifier of the destination

     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearLineageMapping(String userId,
                                    String sourceElementGUID,
                                    String destinationElementGUID) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
    }


    /**
     * Retrieve the lineage mapping relationships linked from an specific source element to its destinations.
     *
     * @param userId calling user
     * @param sourceElementGUID unique identifier of the source
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
                                                                     Date   effectiveTime) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        return null;
    }


    /**
     * Retrieve the lineage mapping relationships linked from an specific destination element to its sources.
     *
     * @param userId calling user
     * @param destinationElementGUID unique identifier of the destination
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
                                                                Date   effectiveTime) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        return null;
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
