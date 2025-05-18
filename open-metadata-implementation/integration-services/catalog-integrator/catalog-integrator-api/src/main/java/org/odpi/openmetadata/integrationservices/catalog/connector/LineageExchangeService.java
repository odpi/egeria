/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.catalog.connector;

import org.odpi.openmetadata.accessservices.assetmanager.client.exchange.LineageExchangeClient;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.PortElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ProcessElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ExternalIdentifierProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.lineage.*;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.integrationservices.catalog.ffdc.CatalogIntegratorErrorCode;

import java.util.Date;
import java.util.List;


/**
 * LineageExchangeService is the context for managing process definitions and lineage linkage.
 */
public class LineageExchangeService extends SchemaExchangeService
{
    private final LineageExchangeClient    lineageExchangeClient;

    /**
     * Create a new client to exchange lineage content with open metadata.
     *
     * @param lineageExchangeClient client for exchange requests
     * @param permittedSynchronization direction(s) that metadata can flow
     * @param userId integration daemon's userId
     * @param assetManagerGUID unique identifier of the software server capability for the asset manager
     * @param assetManagerName unique name of the software server capability for the asset manager
     * @param connectorName name of the connector using this context
     * @param auditLog logging destination
     */
    LineageExchangeService(LineageExchangeClient    lineageExchangeClient,
                           PermittedSynchronization permittedSynchronization,
                           String                   userId,
                           String                   assetManagerGUID,
                           String                   assetManagerName,
                           String                   connectorName,
                           AuditLog                 auditLog)
    {
        super(lineageExchangeClient, permittedSynchronization, userId, assetManagerGUID, assetManagerName, connectorName, auditLog);

        this.lineageExchangeClient = lineageExchangeClient;
    }


    /* =====================================================================================================================
     * A process describes a well defined series of steps that gets something done.
     */

    /**
     * Create a new metadata element to represent a process.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this process
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param processProperties properties about the process to store
     * @param initialStatus status value for the new process (default = ACTIVE)
     *
     * @return unique identifier of the new process
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createProcess(boolean                      assetManagerIsHome,
                                ExternalIdentifierProperties externalIdentifierProperties,
                                ProcessProperties            processProperties,
                                ProcessStatus                initialStatus) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName = "createProcess";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            return lineageExchangeClient.createProcess(userId,
                                                       assetManagerGUID,
                                                       assetManagerName,
                                                       assetManagerIsHome,
                                                       externalIdentifierProperties,
                                                       initialStatus,
                                                       processProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Create a new metadata element to represent a process using an existing metadata element as a template.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this process
     * @param templateGUID unique identifier of the metadata element to copy
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new process
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createProcessFromTemplate(boolean                      assetManagerIsHome,
                                            String                       templateGUID,
                                            ExternalIdentifierProperties externalIdentifierProperties,
                                            TemplateProperties           templateProperties) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String methodName = "createProcessFromTemplate";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            return lineageExchangeClient.createProcessFromTemplate(userId,
                                                                   assetManagerGUID,
                                                                   assetManagerName,
                                                                   assetManagerIsHome,
                                                                   templateGUID,
                                                                   externalIdentifierProperties,
                                                                   templateProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Update the metadata element representing a process.
     *
     * @param processGUID unique identifier of the metadata element to update
     * @param processExternalIdentifier unique identifier of the process in the external asset manager
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param processProperties new properties for the metadata element
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateProcess(String            processGUID,
                              String            processExternalIdentifier,
                              boolean           isMergeUpdate,
                              ProcessProperties processProperties,
                              Date              effectiveTime) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName = "updateProcess";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            lineageExchangeClient.updateProcess(userId,
                                                assetManagerGUID,
                                                assetManagerName,
                                                processGUID,
                                                processExternalIdentifier,
                                                isMergeUpdate,
                                                processProperties,
                                                effectiveTime,
                                                forLineage,
                                                forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Update the status of the metadata element representing a process.
     *
     * @param processGUID unique identifier of the process to update
     * @param processExternalIdentifier unique identifier of the process in the external asset manager
     * @param processStatus new status for the process
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateProcessStatus(String        processGUID,
                                    String        processExternalIdentifier,
                                    ProcessStatus processStatus,
                                    Date          effectiveTime) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String methodName = "updateProcessStatus";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            lineageExchangeClient.updateProcessStatus(userId,
                                                      assetManagerGUID,
                                                      assetManagerName,
                                                      processGUID,
                                                      processExternalIdentifier,
                                                      processStatus,
                                                      effectiveTime,
                                                      forLineage,
                                                      forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Create a parent-child relationship between two processes.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
     * @param parentProcessGUID unique identifier of the process in the external asset manager that is to be the parent process
     * @param childProcessGUID unique identifier of the process in the external asset manager that is to be the nested sub-process
     * @param containmentProperties describes the ownership of the sub-process
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupProcessParent(boolean                      assetManagerIsHome,
                                   String                       parentProcessGUID,
                                   String                       childProcessGUID,
                                   ProcessContainmentProperties containmentProperties,
                                   Date                         effectiveTime) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName = "setupProcessParent";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            lineageExchangeClient.setupProcessParent(userId,
                                                     assetManagerGUID,
                                                     assetManagerName,
                                                     assetManagerIsHome,
                                                     parentProcessGUID,
                                                     childProcessGUID,
                                                     containmentProperties,
                                                     effectiveTime,
                                                     forLineage,
                                                     forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove a parent-child relationship between two processes.
     *
     * @param parentProcessGUID unique identifier of the process in the external asset manager that is to be the parent process
     * @param childProcessGUID unique identifier of the process in the external asset manager that is to be the nested sub-process
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearProcessParent(String parentProcessGUID,
                                   String childProcessGUID,
                                   Date   effectiveTime) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        final String methodName = "clearProcessParent";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            lineageExchangeClient.clearProcessParent(userId,
                                                     assetManagerGUID,
                                                     assetManagerName,
                                                     parentProcessGUID,
                                                     childProcessGUID,
                                                     effectiveTime,
                                                     forLineage,
                                                     forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Update the zones for the asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Asset Manager OMAS).
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @param processGUID unique identifier of the metadata element to publish
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void publishProcess(String processGUID,
                               Date   effectiveTime) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        final String methodName = "publishProcess";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            lineageExchangeClient.publishProcess(userId,
                                                 assetManagerGUID,
                                                 assetManagerName,
                                                 processGUID,
                                                 effectiveTime,
                                                 forLineage,
                                                 forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Update the zones for the asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Asset Manager OMAS.  This is the setting when the process is first created).
     *
     * @param processGUID unique identifier of the metadata element to withdraw
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void withdrawProcess(String processGUID,
                                Date   effectiveTime) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        final String methodName = "withdrawProcess";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            lineageExchangeClient.withdrawProcess(userId,
                                                  assetManagerGUID,
                                                  assetManagerName,
                                                  processGUID,
                                                  effectiveTime,
                                                  forLineage,
                                                  forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove the metadata element representing a process.
     *
     * @param processGUID unique identifier of the metadata element to remove
     * @param processExternalIdentifier unique identifier of the process in the external asset manager
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeProcess(String processGUID,
                              String processExternalIdentifier,
                              Date   effectiveTime) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        final String methodName = "removeProcess";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            lineageExchangeClient.removeProcess(userId,
                                                assetManagerGUID,
                                                assetManagerName,
                                                processGUID,
                                                processExternalIdentifier,
                                                effectiveTime,
                                                forLineage,
                                                forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Retrieve the list of process metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ProcessElement> findProcesses(String searchString,
                                              int    startFrom,
                                              int    pageSize,
                                              Date   effectiveTime) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        return lineageExchangeClient.findProcesses(userId,
                                                   assetManagerGUID,
                                                   assetManagerName,
                                                   searchString,
                                                   startFrom,
                                                   pageSize,
                                                   effectiveTime,
                                                   forLineage,
                                                   forDuplicateProcessing);
    }


    /**
     * Return the list of processes associated with the asset manager.
     *
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return list of metadata elements describing the processes associated with the requested asset manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ProcessElement>   getProcessesForAssetManager(int  startFrom,
                                                              int  pageSize,
                                                              Date effectiveTime) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        return lineageExchangeClient.getProcessesForAssetManager(userId,
                                                                 assetManagerGUID,
                                                                 assetManagerName,
                                                                 startFrom,
                                                                 pageSize,
                                                                 effectiveTime,
                                                                 forLineage,
                                                                 forDuplicateProcessing);
    }


    /**
     * Retrieve the list of process metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ProcessElement>   getProcessesByName(String name,
                                                     int    startFrom,
                                                     int    pageSize,
                                                     Date   effectiveTime) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        return lineageExchangeClient.getProcessesByName(userId,
                                                        assetManagerGUID,
                                                        assetManagerName,
                                                        name,
                                                        startFrom,
                                                        pageSize,
                                                        effectiveTime,
                                                        forLineage,
                                                        forDuplicateProcessing);
    }


    /**
     * Retrieve the process metadata element with the supplied unique identifier.
     *
     * @param processGUID unique identifier of the requested metadata element
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ProcessElement getProcessByGUID(String processGUID,
                                           Date   effectiveTime) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        return lineageExchangeClient.getProcessByGUID(userId,
                                                      assetManagerGUID,
                                                      assetManagerName,
                                                      processGUID,
                                                      effectiveTime,
                                                      forLineage,
                                                      forDuplicateProcessing);
    }


    /**
     * Retrieve the process metadata element with the supplied unique identifier.
     *
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param processGUID unique identifier of the requested metadata element
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return parent process element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ProcessElement getProcessParent(String assetManagerGUID,
                                           String assetManagerName,
                                           String processGUID,
                                           Date   effectiveTime) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        return lineageExchangeClient.getProcessParent(userId,
                                                      assetManagerGUID,
                                                      assetManagerName,
                                                      processGUID,
                                                      effectiveTime,
                                                      forLineage,
                                                      forDuplicateProcessing);
    }


    /**
     * Retrieve the process metadata element with the supplied unique identifier.
     *
     * @param processGUID unique identifier of the requested metadata element
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return list of process element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ProcessElement> getSubProcesses(String processGUID,
                                                int    startFrom,
                                                int    pageSize,
                                                Date   effectiveTime) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        return lineageExchangeClient.getSubProcesses(userId,
                                                     assetManagerGUID,
                                                     assetManagerName,
                                                     processGUID,
                                                     startFrom,
                                                     pageSize,
                                                     effectiveTime,
                                                     forLineage,
                                                     forDuplicateProcessing);
    }


    /* ===============================================================================
     * A process typically contains ports that show the flow of data and control to and from it.
     */

    /**
     * Create a new metadata element to represent a port.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this port
     * @param processGUID unique identifier of the process where the port is located
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param portProperties properties for the port
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier of the new metadata element for the port
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createPort(boolean                      assetManagerIsHome,
                             String                       processGUID,
                             ExternalIdentifierProperties externalIdentifierProperties,
                             PortProperties               portProperties,
                             Date                         effectiveTime) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName = "createPort";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            return lineageExchangeClient.createPort(userId,
                                                    assetManagerGUID,
                                                    assetManagerName,
                                                    assetManagerIsHome,
                                                    processGUID,
                                                    externalIdentifierProperties,
                                                    portProperties,
                                                    effectiveTime,
                                                    forLineage,
                                                    forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Update the properties of the metadata element representing a port.  This call replaces
     * all existing properties with the supplied properties.
     *
     * @param portGUID unique identifier of the port to update
     * @param portProperties new properties for the port
     * @param portExternalIdentifier unique identifier of the port in the external asset manager
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updatePort(String         portGUID,
                           String         portExternalIdentifier,
                           PortProperties portProperties,
                           Date           effectiveTime) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        final String methodName = "updatePort";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            lineageExchangeClient.updatePort(userId,
                                             assetManagerGUID,
                                             assetManagerName,
                                             portGUID,
                                             portExternalIdentifier,
                                             portProperties,
                                             effectiveTime,
                                             forLineage,
                                             forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Link a port to a process.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
     * @param processGUID unique identifier of the process
     * @param portGUID unique identifier of the port
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupProcessPort(boolean assetManagerIsHome,
                                 String  processGUID,
                                 String  portGUID,
                                 Date    effectiveTime) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String methodName = "setupProcessPort";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            lineageExchangeClient.setupProcessPort(userId,
                                                   assetManagerGUID,
                                                   assetManagerName,
                                                   assetManagerIsHome,
                                                   processGUID,
                                                   portGUID,
                                                   effectiveTime,
                                                   forLineage,
                                                   forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Unlink a port from a process.
     *
     * @param processGUID unique identifier of the process
     * @param portGUID unique identifier of the port
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearProcessPort(String processGUID,
                                 String portGUID,
                                 Date   effectiveTime) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        final String methodName = "clearProcessPort";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            lineageExchangeClient.clearProcessPort(userId,
                                                   assetManagerGUID,
                                                   assetManagerName,
                                                   processGUID,
                                                   portGUID,
                                                   effectiveTime,
                                                   forLineage,
                                                   forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Link two ports together to show that portTwo is an implementation of portOne. (That is, portOne delegates to
     * portTwo.)
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
     * @param portOneGUID unique identifier of the port at end 1
     * @param portTwoGUID unique identifier of the port at end 2
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupPortDelegation(boolean assetManagerIsHome,
                                    String  portOneGUID,
                                    String  portTwoGUID,
                                    Date    effectiveTime) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String methodName = "setupPortDelegation";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            lineageExchangeClient.setupPortDelegation(userId,
                                                      assetManagerGUID,
                                                      assetManagerName,
                                                      assetManagerIsHome,
                                                      portOneGUID,
                                                      portTwoGUID,
                                                      effectiveTime,
                                                      forLineage,
                                                      forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove the port delegation relationship between two ports.
     *
     * @param portOneGUID unique identifier of the port at end 1
     * @param portTwoGUID unique identifier of the port at end 2
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearPortDelegation(String portOneGUID,
                                    String portTwoGUID,
                                    Date   effectiveTime) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String methodName = "clearPortDelegation";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            lineageExchangeClient.clearPortDelegation(userId,
                                                      assetManagerGUID,
                                                      assetManagerName,
                                                      portOneGUID,
                                                      portTwoGUID,
                                                      effectiveTime,
                                                      forLineage,
                                                      forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Link a schema type to a port to show the structure of data it accepts.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
     * @param portGUID unique identifier of the port
     * @param schemaTypeGUID unique identifier of the schemaType
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupPortSchemaType(boolean assetManagerIsHome,
                                    String  portGUID,
                                    String  schemaTypeGUID,
                                    Date    effectiveTime) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String methodName = "setupPortSchemaType";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            lineageExchangeClient.setupPortSchemaType(userId,
                                                      assetManagerGUID,
                                                      assetManagerName,
                                                      assetManagerIsHome,
                                                      portGUID,
                                                      schemaTypeGUID,
                                                      effectiveTime,
                                                      forLineage,
                                                      forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove the schema type from a port.
     *
     * @param portGUID unique identifier of the port
     * @param schemaTypeGUID unique identifier of the schemaType
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearPortSchemaType(String portGUID,
                                    String schemaTypeGUID,
                                    Date   effectiveTime) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String methodName = "clearPortSchemaType";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            lineageExchangeClient.clearPortSchemaType(userId,
                                                      assetManagerGUID,
                                                      assetManagerName,
                                                      portGUID,
                                                      schemaTypeGUID,
                                                      effectiveTime,
                                                      forLineage,
                                                      forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove the metadata element representing a port.
     *
     * @param portGUID unique identifier of the metadata element to remove
     * @param portExternalIdentifier unique identifier of the port in the external asset manager
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removePort(String portGUID,
                           String portExternalIdentifier,
                           Date   effectiveTime) throws InvalidParameterException,
                                                        UserNotAuthorizedException,
                                                        PropertyServerException
    {
        final String methodName = "removePort";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            lineageExchangeClient.removePort(userId,
                                             assetManagerGUID,
                                             assetManagerName,
                                             portGUID,
                                             portExternalIdentifier,
                                             effectiveTime,
                                             forLineage,
                                             forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Retrieve the list of port metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<PortElement>   findPorts(String searchString,
                                         int    startFrom,
                                         int    pageSize,
                                         Date   effectiveTime) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        return lineageExchangeClient.findPorts(userId,
                                               assetManagerGUID,
                                               assetManagerName,
                                               searchString,
                                               startFrom,
                                               pageSize,
                                               effectiveTime,
                                               forLineage,
                                               forDuplicateProcessing);
    }


    /**
     * Retrieve the list of ports associated with a process.
     *
     * @param processGUID unique identifier of the process of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<PortElement>    getPortsForProcess(String processGUID,
                                                   int    startFrom,
                                                   int    pageSize,
                                                   Date   effectiveTime) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        return lineageExchangeClient.getPortsForProcess(userId,
                                                        assetManagerGUID,
                                                        assetManagerName,
                                                        processGUID,
                                                        startFrom,
                                                        pageSize,
                                                        effectiveTime,
                                                        forLineage,
                                                        forDuplicateProcessing);
    }


    /**
     * Retrieve the list of ports that delegate to this port.
     *
     * @param portGUID unique identifier of the starting port
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<PortElement>  getPortUse(String portGUID,
                                         int    startFrom,
                                         int    pageSize,
                                         Date   effectiveTime) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        return lineageExchangeClient.getPortUse(userId,
                                                assetManagerGUID,
                                                assetManagerName,
                                                portGUID,
                                                startFrom,
                                                pageSize,
                                                effectiveTime,
                                                forLineage,
                                                forDuplicateProcessing);
    }


    /**
     * Retrieve the port that this port delegates to.
     *
     * @param portGUID unique identifier of the starting port alias
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public PortElement getPortDelegation(String portGUID,
                                         Date   effectiveTime) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        return lineageExchangeClient.getPortDelegation(userId,
                                                       assetManagerGUID,
                                                       assetManagerName,
                                                       portGUID,
                                                       effectiveTime,
                                                       forLineage,
                                                       forDuplicateProcessing);
    }


    /**
     * Retrieve the list of port metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<PortElement>   getPortsByName(String name,
                                              int    startFrom,
                                              int    pageSize,
                                              Date   effectiveTime) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        return lineageExchangeClient.getPortsByName(userId,
                                                    assetManagerGUID,
                                                    assetManagerName,
                                                    name,
                                                    startFrom,
                                                    pageSize,
                                                    effectiveTime,
                                                    forLineage,
                                                    forDuplicateProcessing);
    }


    /**
     * Retrieve the port metadata element with the supplied unique identifier.
     *
     * @param portGUID unique identifier of the requested metadata element
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public PortElement getPortByGUID(String portGUID,
                                     Date   effectiveTime) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        return lineageExchangeClient.getPortByGUID(userId,
                                                   assetManagerGUID,
                                                   assetManagerName,
                                                   portGUID,
                                                   effectiveTime,
                                                   forLineage,
                                                   forDuplicateProcessing);
    }


    /* ===============================================================================
     * General linkage and classifications
     */


    /**
     * Classify a port, process or asset as "BusinessSignificant" (this may affect the way that lineage is displayed).
     *
     * @param elementGUID unique identifier of the metadata element to update
     * @param elementExternalIdentifier unique identifier of the port in the external asset manager
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setBusinessSignificant(String elementGUID,
                                       String elementExternalIdentifier,
                                       Date   effectiveTime) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName = "setBusinessSignificant";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            lineageExchangeClient.setBusinessSignificant(userId,
                                                         assetManagerGUID,
                                                         assetManagerName,
                                                         elementGUID,
                                                         elementExternalIdentifier,
                                                         effectiveTime,
                                                         forLineage,
                                                         forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove the "BusinessSignificant" designation from the element.
     *
     * @param elementGUID unique identifier of the metadata element to update
     * @param elementExternalIdentifier unique identifier of the element in the external asset manager (can be null)
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearBusinessSignificant(String elementGUID,
                                         String elementExternalIdentifier,
                                         Date   effectiveTime) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName = "clearBusinessSignificant";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            lineageExchangeClient.clearBusinessSignificant(userId,
                                                           assetManagerGUID,
                                                           assetManagerName,
                                                           elementGUID,
                                                           elementExternalIdentifier,
                                                           effectiveTime,
                                                           forLineage,
                                                           forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Link two elements together to show that data flows from one to the other.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
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
    public String setupDataFlow(boolean            assetManagerIsHome,
                                String             dataSupplierGUID,
                                String             dataConsumerGUID,
                                DataFlowProperties properties,
                                Date               effectiveTime) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName = "setupDataFlow";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            return lineageExchangeClient.setupDataFlow(userId,
                                                       assetManagerGUID,
                                                       assetManagerName,
                                                       assetManagerIsHome,
                                                       dataSupplierGUID,
                                                       dataConsumerGUID,
                                                       properties,
                                                       effectiveTime,
                                                       forLineage,
                                                       forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Retrieve the data flow relationship between two elements.  The qualifiedName is optional unless there
     * is more than one data flow relationships between these two elements since it is used to disambiguate
     * the request.  This is often used in conjunction with update.
     *
     * @param dataSupplierGUID unique identifier of the data supplier
     * @param dataConsumerGUID unique identifier of the data consumer
     * @param qualifiedName unique identifier for this relationship
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DataFlowElement getDataFlow(String dataSupplierGUID,
                                       String dataConsumerGUID,
                                       String qualifiedName,
                                       Date   effectiveTime) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        return lineageExchangeClient.getDataFlow(userId,
                                                 assetManagerGUID,
                                                 assetManagerName,
                                                 dataSupplierGUID,
                                                 dataConsumerGUID,
                                                 qualifiedName,
                                                 effectiveTime,
                                                 forLineage,
                                                 forDuplicateProcessing);
    }


    /**
     * Update relationship between two elements that shows that data flows from one to the other.
     *
     * @param dataFlowGUID unique identifier of the data flow relationship
     * @param properties unique identifier for this relationship along with description and/or additional relevant properties
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateDataFlow(String             dataFlowGUID,
                               DataFlowProperties properties,
                               Date               effectiveTime) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String methodName = "updateDataFlow";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            lineageExchangeClient.updateDataFlow(userId,
                                                 assetManagerGUID,
                                                 assetManagerName,
                                                 dataFlowGUID,
                                                 properties,
                                                 effectiveTime,
                                                 forLineage,
                                                 forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove the data flow relationship between two elements.
     *
     * @param dataFlowGUID unique identifier of the data flow relationship
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearDataFlow(String dataFlowGUID,
                              Date   effectiveTime) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        final String methodName = "clearDataFlow";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            lineageExchangeClient.clearDataFlow(userId,
                                                assetManagerGUID,
                                                assetManagerName,
                                                dataFlowGUID,
                                                effectiveTime,
                                                forLineage,
                                                forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Retrieve the data flow relationships linked from a specific element to the downstream consumers.
     *
     * @param dataSupplierGUID unique identifier of the data supplier
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DataFlowElement> getDataFlowConsumers(String dataSupplierGUID,
                                                      int    startFrom,
                                                      int    pageSize,
                                                      Date   effectiveTime) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        return lineageExchangeClient.getDataFlowConsumers(userId,
                                                          assetManagerGUID,
                                                          assetManagerName,
                                                          dataSupplierGUID,
                                                          startFrom,
                                                          pageSize,
                                                          effectiveTime,
                                                          forLineage,
                                                          forDuplicateProcessing);
    }


    /**
     * Retrieve the data flow relationships linked from a specific element to the upstream suppliers.
     *
     * @param dataConsumerGUID unique identifier of the data consumer
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DataFlowElement> getDataFlowSuppliers(String dataConsumerGUID,
                                                      int    startFrom,
                                                      int    pageSize,
                                                      Date   effectiveTime) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        return lineageExchangeClient.getDataFlowSuppliers(userId,
                                                          assetManagerGUID,
                                                          assetManagerName,
                                                          dataConsumerGUID,
                                                          startFrom,
                                                          pageSize,
                                                          effectiveTime,
                                                          forLineage,
                                                          forDuplicateProcessing);
    }


    /**
     * Link two elements to show that when one completes the next is started.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
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
    public String setupControlFlow(boolean               assetManagerIsHome,
                                   String                currentStepGUID,
                                   String                nextStepGUID,
                                   ControlFlowProperties properties,
                                   Date                  effectiveTime) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName = "setupControlFlow";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            return lineageExchangeClient.setupControlFlow(userId,
                                                          assetManagerGUID,
                                                          assetManagerName,
                                                          assetManagerIsHome,
                                                          currentStepGUID,
                                                          nextStepGUID,
                                                          properties,
                                                          effectiveTime,
                                                          forLineage,
                                                          forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Retrieve the control flow relationship between two elements.  The qualifiedName is optional unless there
     * is more than one control flow relationships between these two elements since it is used to disambiguate
     * the request.  This is often used in conjunction with update.
     *
     * @param currentStepGUID unique identifier of the previous step
     * @param nextStepGUID unique identifier of the next step
     * @param qualifiedName unique identifier for this relationship
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ControlFlowElement getControlFlow(String currentStepGUID,
                                             String nextStepGUID,
                                             String qualifiedName,
                                             Date   effectiveTime) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        return lineageExchangeClient.getControlFlow(userId,
                                                    assetManagerGUID,
                                                    assetManagerName,
                                                    currentStepGUID,
                                                    nextStepGUID,
                                                    qualifiedName,
                                                    effectiveTime,
                                                    forLineage,
                                                    forDuplicateProcessing);
    }


    /**
     * Update the relationship between two elements that shows that when one completes the next is started.
     *
     * @param controlFlowGUID unique identifier of the  control flow relationship
     * @param properties unique identifier for this relationship along with description and/or additional relevant properties
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateControlFlow(String                controlFlowGUID,
                                  ControlFlowProperties properties,
                                  Date                  effectiveTime) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName = "updateControlFlow";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            lineageExchangeClient.updateControlFlow(userId,
                                                    assetManagerGUID,
                                                    assetManagerName,
                                                    controlFlowGUID,
                                                    properties,
                                                    effectiveTime,
                                                    forLineage,
                                                    forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove the control flow relationship between two elements.
     *
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param controlFlowGUID unique identifier of the  control flow relationship
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearControlFlow(String assetManagerGUID,
                                 String assetManagerName,
                                 String controlFlowGUID,
                                 Date   effectiveTime) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        final String methodName = "clearControlFlow";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            lineageExchangeClient.clearControlFlow(userId,
                                                   assetManagerGUID,
                                                   assetManagerName,
                                                   controlFlowGUID,
                                                   effectiveTime,
                                                   forLineage,
                                                   forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Retrieve the control relationships linked from a specific element to the possible next elements in the process.
     *
     * @param currentStepGUID unique identifier of the current step
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ControlFlowElement> getControlFlowNextSteps(String currentStepGUID,
                                                            int    startFrom,
                                                            int    pageSize,
                                                            Date   effectiveTime) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        return lineageExchangeClient.getControlFlowNextSteps(userId,
                                                             assetManagerGUID,
                                                             assetManagerName,
                                                             currentStepGUID,
                                                             startFrom,
                                                             pageSize,
                                                             effectiveTime,
                                                             forLineage,
                                                             forDuplicateProcessing);
    }


    /**
     * Retrieve the control relationships linked from a specific element to the possible previous elements in the process.
     *
     * @param currentStepGUID unique identifier of the current step
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ControlFlowElement> getControlFlowPreviousSteps(String currentStepGUID,
                                                                int    startFrom,
                                                                int    pageSize,
                                                                Date   effectiveTime) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        return lineageExchangeClient.getControlFlowPreviousSteps(userId,
                                                                 assetManagerGUID,
                                                                 assetManagerName,
                                                                 currentStepGUID,
                                                                 startFrom,
                                                                 pageSize,
                                                                 effectiveTime,
                                                                 forLineage,
                                                                 forDuplicateProcessing);
    }


    /**
     * Link two elements together to show a request-response call between them.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
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
    public String setupProcessCall(boolean               assetManagerIsHome,
                                   String                callerGUID,
                                   String                calledGUID,
                                   ProcessCallProperties properties,
                                   Date                  effectiveTime) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName = "setupProcessCall";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            return lineageExchangeClient.setupProcessCall(userId,
                                                          assetManagerGUID,
                                                          assetManagerName,
                                                          assetManagerIsHome,
                                                          callerGUID,
                                                          calledGUID,
                                                          properties,
                                                          effectiveTime,
                                                          forLineage,
                                                          forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Retrieve the process call relationship between two elements.  The qualifiedName is optional unless there
     * is more than one process call relationships between these two elements since it is used to disambiguate
     * the request.  This is often used in conjunction with update.
     *
     * @param callerGUID unique identifier of the element that is making the call
     * @param calledGUID unique identifier of the element that is processing the call
     * @param qualifiedName unique identifier for this relationship
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ProcessCallElement getProcessCall(String callerGUID,
                                             String calledGUID,
                                             String qualifiedName,
                                             Date   effectiveTime) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        return lineageExchangeClient.getProcessCall(userId,
                                                    assetManagerGUID,
                                                    assetManagerName,
                                                    callerGUID,
                                                    calledGUID,
                                                    qualifiedName,
                                                    effectiveTime,
                                                    forLineage,
                                                    forDuplicateProcessing);
    }


    /**
     * Update the relationship between two elements that shows a request-response call between them.
     *
     * @param processCallGUID unique identifier of the process call relationship
     * @param properties unique identifier for this relationship along with description and/or additional relevant properties
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateProcessCall(String                processCallGUID,
                                  ProcessCallProperties properties,
                                  Date                  effectiveTime) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName = "updateProcessCall";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            lineageExchangeClient.updateProcessCall(userId,
                                                    assetManagerGUID,
                                                    assetManagerName,
                                                    processCallGUID,
                                                    properties,
                                                    effectiveTime,
                                                    forLineage,
                                                    forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove the process call relationship.
     *
     * @param processCallGUID unique identifier of the process call relationship
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearProcessCall(String processCallGUID,
                                 Date   effectiveTime) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        final String methodName = "clearProcessCall";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            lineageExchangeClient.clearProcessCall(userId,
                                                   assetManagerGUID,
                                                   assetManagerName,
                                                   processCallGUID,
                                                   effectiveTime,
                                                   forLineage,
                                                   forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Retrieve the process call relationships linked from a specific element to the elements it calls.
     *
     * @param callerGUID unique identifier of the element that is making the call
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ProcessCallElement> getProcessCalled(String callerGUID,
                                                     int    startFrom,
                                                     int    pageSize,
                                                     Date   effectiveTime) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        return lineageExchangeClient.getProcessCalled(userId,
                                                      assetManagerGUID,
                                                      assetManagerName,
                                                      callerGUID,
                                                      startFrom,
                                                      pageSize,
                                                      effectiveTime,
                                                      forLineage,
                                                      forDuplicateProcessing);
    }


    /**
     * Retrieve the process call relationships linked from a specific element to its callers.
     *
     * @param calledGUID unique identifier of the element that is processing the call
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ProcessCallElement> getProcessCallers(String calledGUID,
                                                      int    startFrom,
                                                      int    pageSize,
                                                      Date   effectiveTime) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        return lineageExchangeClient.getProcessCallers(userId,
                                                       assetManagerGUID,
                                                       assetManagerName,
                                                       calledGUID,
                                                       startFrom,
                                                       pageSize,
                                                       effectiveTime,
                                                       forLineage,
                                                       forDuplicateProcessing);
    }


    /**
     * Link to elements together to show that they are part of the lineage of the data that is moving
     * between the processes.  Typically, the lineage relationships stitch together processes and data assets
     * supported by different technologies.
     *
     * @param sourceElementGUID unique identifier of the source
     * @param destinationElementGUID unique identifier of the destination
     * @param properties unique identifier for this relationship along with description and/or additional relevant properties
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupLineageMapping(String                   sourceElementGUID,
                                    String                   destinationElementGUID,
                                    LineageMappingProperties properties,
                                    Date                     effectiveTime) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName = "setupLineageMapping";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            lineageExchangeClient.setupLineageMapping(userId,
                                                      assetManagerGUID,
                                                      assetManagerName,
                                                      sourceElementGUID,
                                                      destinationElementGUID,
                                                      properties,
                                                      effectiveTime,
                                                      forLineage,
                                                      forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Retrieve the relationship between two elements.  The qualifiedName is optional unless there
     * is more than one relationship between these two elements since it is used to disambiguate
     * the request.  This is often used in conjunction with update.
     *
     * @param sourceElementGUID unique identifier of the element that is making the call
     * @param destinationElementGUID unique identifier of the element that is processing the call
     * @param qualifiedName unique identifier for this relationship
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public LineageMappingElement getLineageMapping(String  sourceElementGUID,
                                                   String  destinationElementGUID,
                                                   String  qualifiedName,
                                                   Date    effectiveTime,
                                                   boolean forLineage,
                                                   boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        return lineageExchangeClient.getLineageMapping(userId,
                                                       assetManagerGUID,
                                                       assetManagerName,
                                                       sourceElementGUID,
                                                       destinationElementGUID,
                                                       qualifiedName,
                                                       effectiveTime,
                                                       forLineage,
                                                       forDuplicateProcessing);
    }


    /**
     * Update the lineage mapping relationship between two elements.
     *
     * @param lineageMappingGUID unique identifier of the lineage mapping relationship
     * @param properties unique identifier for this relationship along with description and/or additional relevant properties
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
      *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateLineageMapping(String                   lineageMappingGUID,
                                     LineageMappingProperties properties,
                                     Date                     effectiveTime) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String methodName = "updateLineageMapping";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            lineageExchangeClient.updateLineageMapping(userId,
                                                       assetManagerGUID,
                                                       assetManagerName,
                                                       lineageMappingGUID,
                                                       properties,
                                                       effectiveTime,
                                                       forLineage,
                                                       forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove the lineage mapping between two elements.
     *
     * @param lineageMappingGUID unique identifier of the source
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearLineageMapping(String lineageMappingGUID,
                                    Date   effectiveTime) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String methodName = "clearLineageMapping";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            lineageExchangeClient.clearLineageMapping(userId,
                                                      assetManagerGUID,
                                                      assetManagerName,
                                                      lineageMappingGUID,
                                                      effectiveTime,
                                                      forLineage,
                                                      forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Retrieve the lineage mapping relationships linked from a specific source element to its destinations.
     *
     * @param sourceElementGUID unique identifier of the source
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return list of lineage mapping relationships
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<LineageMappingElement> getDestinationLineageMappings(String sourceElementGUID,
                                                                     int    startFrom,
                                                                     int    pageSize,
                                                                     Date   effectiveTime) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        return lineageExchangeClient.getDestinationLineageMappings(userId,
                                                                   assetManagerGUID,
                                                                   assetManagerName,
                                                                   sourceElementGUID,
                                                                   startFrom,
                                                                   pageSize,
                                                                   effectiveTime,
                                                                   forLineage,
                                                                   forDuplicateProcessing);
    }


    /**
     * Retrieve the lineage mapping relationships linked from a specific destination element to its sources.
     *
     * @param destinationElementGUID unique identifier of the destination
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return list of lineage mapping relationships
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<LineageMappingElement> getSourceLineageMappings(String destinationElementGUID,
                                                                int    startFrom,
                                                                int    pageSize,
                                                                Date   effectiveTime) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        return lineageExchangeClient.getSourceLineageMappings(userId,
                                                              assetManagerGUID,
                                                              assetManagerName,
                                                              destinationElementGUID,
                                                              startFrom,
                                                              pageSize,
                                                              effectiveTime,
                                                              forLineage,
                                                              forDuplicateProcessing);
    }
}
