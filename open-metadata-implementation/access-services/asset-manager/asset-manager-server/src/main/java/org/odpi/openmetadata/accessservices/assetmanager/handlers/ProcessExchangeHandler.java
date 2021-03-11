/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.handlers;

import org.odpi.openmetadata.accessservices.assetmanager.converters.*;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.*;
import org.odpi.openmetadata.accessservices.assetmanager.properties.*;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.commonservices.generichandlers.ProcessHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * ProcessExchangeHandler is the server-side for managing processes.
 */
public class ProcessExchangeHandler extends ExchangeHandlerBase
{
    private ProcessHandler<ProcessElement,
                                  PortElement,
                                  DataFlowElement,
                                  ControlFlowElement,
                                  ProcessCallElement,
                                  LineageMappingElement> processHandler;

    private final static String processGUIDParameterName = "processGUID";
    private final static String portGUIDParameterName = "portGUID";

    /**
     * Construct the process exchange handler with information needed to work with process related objects
     * for Asset Manager OMAS.
     *
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param supportedZones list of zones that the access service is allowed to serve instances from.
     * @param defaultZones list of zones that the access service should set in all new instances.
     * @param publishZones list of zones that the access service sets up in published instances.
     * @param auditLog destination for audit log events.
     */
    public ProcessExchangeHandler(String                             serviceName,
                                  String                             serverName,
                                  InvalidParameterHandler            invalidParameterHandler,
                                  RepositoryHandler                  repositoryHandler,
                                  OMRSRepositoryHelper               repositoryHelper,
                                  String                             localServerUserId,
                                  OpenMetadataServerSecurityVerifier securityVerifier,
                                  List<String>                       supportedZones,
                                  List<String>                       defaultZones,
                                  List<String>                       publishZones,
                                  AuditLog                           auditLog)
    {
        super(serviceName,
              serverName,
              invalidParameterHandler,
              repositoryHandler,
              repositoryHelper,
              localServerUserId,
              securityVerifier,
              supportedZones,
              defaultZones,
              publishZones,
              auditLog);

        processHandler = new ProcessHandler<>(new ProcessConverter<>(repositoryHelper, serviceName, serverName),
                                              ProcessElement.class,
                                              new PortConverter<>(repositoryHelper, serviceName, serverName),
                                              PortElement.class,
                                              new DataFlowConverter<>(repositoryHelper, serviceName, serverName),
                                              DataFlowElement.class,
                                              new ControlFlowConverter<>(repositoryHelper, serviceName, serverName),
                                              ControlFlowElement.class,
                                              new ProcessCallConverter<>(repositoryHelper, serviceName, serverName),
                                              ProcessCallElement.class,
                                              new LineageMappingConverter<>(repositoryHelper, serviceName, serverName),
                                              LineageMappingElement.class,
                                              serviceName,
                                              serverName,
                                              invalidParameterHandler,
                                              repositoryHandler,
                                              repositoryHelper,
                                              localServerUserId,
                                              securityVerifier,
                                              supportedZones,
                                              defaultZones,
                                              publishZones,
                                              auditLog);
    }


    /* ========================================================
     * Managing the externalIds and related correlation properties.
     */



    /**
     * Update each returned element with details of the correlation properties for the supplied asset manager.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param results list of elements
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    private void addCorrelationPropertiesToProcesses(String               userId,
                                                     String               assetManagerGUID,
                                                     String               assetManagerName,
                                                     List<ProcessElement> results,
                                                     String               methodName) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        if ((results != null) && (assetManagerGUID != null))
        {
            for (MetadataElement element : results)
            {
                if ((element != null) && (element.getElementHeader() != null) && (element.getElementHeader().getGUID() != null))
                {
                    element.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                                element.getElementHeader().getGUID(),
                                                                                processGUIDParameterName,
                                                                                OpenMetadataAPIMapper.PROCESS_TYPE_NAME,
                                                                                assetManagerGUID,
                                                                                assetManagerName,
                                                                                methodName));
                }
            }
        }
    }


    /**
     * Update each returned element with details of the correlation properties for the supplied asset manager.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param results list of elements
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    private void addCorrelationPropertiesToPorts(String            userId,
                                                 String            assetManagerGUID,
                                                 String            assetManagerName,
                                                 List<PortElement> results,
                                                 String            methodName) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        if ((results != null) && (assetManagerGUID != null))
        {
            for (MetadataElement element : results)
            {
                if ((element != null) && (element.getElementHeader() != null) && (element.getElementHeader().getGUID() != null))
                {
                    element.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                                element.getElementHeader().getGUID(),
                                                                                portGUIDParameterName,
                                                                                OpenMetadataAPIMapper.PORT_TYPE_NAME,
                                                                                assetManagerGUID,
                                                                                assetManagerName,
                                                                                methodName));
                }
            }
        }
    }


    /**
     * Convert the ProcessStatus to an InstanceStatus understood by the repository services.
     *
     * @param status status from caller
     * @return instance status
     */
    private InstanceStatus getInstanceStatus(ProcessStatus status)
    {
        if (status != null)
        {
            switch (status)
            {
                case DRAFT:
                    return InstanceStatus.DRAFT;

                case APPROVED:
                    return InstanceStatus.APPROVED;

                case PROPOSED:
                    return InstanceStatus.PROPOSED;

                case ACTIVE:
                    return InstanceStatus.ACTIVE;
            }
        }

        return InstanceStatus.UNKNOWN;
    }



    /* =====================================================================================================================
     * A process describes a well defined series of steps that gets something done.
     */

    /**
     * Create a new metadata element to represent a process.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param processProperties properties about the process to store
     * @param initialStatus status value for the new process (default = ACTIVE)
     * @param methodName calling method
     *
     * @return unique identifier of the new process
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createProcess(String                        userId,
                                MetadataCorrelationProperties correlationProperties,
                                boolean                       assetManagerIsHome,
                                ProcessProperties             processProperties,
                                ProcessStatus                 initialStatus,
                                String                        methodName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String propertiesParameterName     = "processProperties";
        final String qualifiedNameParameterName  = "processProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(processProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(processProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        String typeName = OpenMetadataAPIMapper.DEPLOYED_SOFTWARE_COMPONENT_TYPE_NAME;

        if (processProperties.getTypeName() != null)
        {
            typeName = processProperties.getTypeName();
        }

        int ownerCategory = OwnerCategory.USER_ID.getOpenTypeOrdinal();

        if (processProperties.getOwnerCategory() != null)
        {
            ownerCategory = processProperties.getOwnerCategory().getOpenTypeOrdinal();
        }

        String processGUID = processHandler.createProcess(userId,
                                                          this.getExternalSourceGUID(correlationProperties, assetManagerIsHome),
                                                          this.getExternalSourceName(correlationProperties, assetManagerIsHome),
                                                          processProperties.getQualifiedName(),
                                                          processProperties.getTechnicalName(),
                                                          processProperties.getTechnicalDescription(),
                                                          processProperties.getFormula(),
                                                          processProperties.getImplementationLanguage(),
                                                          processProperties.getZoneMembership(),
                                                          processProperties.getOwner(),
                                                          ownerCategory,
                                                          processProperties.getOriginOrganizationGUID(),
                                                          processProperties.getOriginBusinessCapabilityGUID(),
                                                          processProperties.getOtherOriginValues(),
                                                          processProperties.getAdditionalProperties(),
                                                          typeName,
                                                          processProperties.getExtendedProperties(),
                                                          this.getInstanceStatus(initialStatus),
                                                          methodName);

        if (processGUID != null)
        {
            this.maintainSupplementaryProperties(userId,
                                                 processGUID,
                                                 processProperties.getQualifiedName(),
                                                 processProperties,
                                                 false,
                                                 methodName);

            this.createExternalIdentifier(userId,
                                          processGUID,
                                          processGUIDParameterName,
                                          OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                          correlationProperties,
                                          methodName);
        }

        return processGUID;
    }


    /**
     * Create a new metadata element to represent a process using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     * @param methodName calling method
     *
     * @return unique identifier of the new process
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createProcessFromTemplate(String                        userId,
                                            MetadataCorrelationProperties correlationProperties,
                                            boolean                       assetManagerIsHome,
                                            String                        templateGUID,
                                            TemplateProperties            templateProperties,
                                            String                        methodName) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String templateGUIDParameterName   = "templateGUID";
        final String propertiesParameterName     = "templateProperties";
        final String qualifiedNameParameterName  = "templateProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        String processGUID = processHandler.createProcessFromTemplate(userId,
                                                                      this.getExternalSourceGUID(correlationProperties, assetManagerIsHome),
                                                                      this.getExternalSourceName(correlationProperties, assetManagerIsHome),
                                                                      templateGUID,
                                                                      templateGUIDParameterName,
                                                                      templateProperties.getQualifiedName(),
                                                                      qualifiedNameParameterName,
                                                                      templateProperties.getDisplayName(),
                                                                      templateProperties.getDescription(),
                                                                      methodName);
        if (processGUID != null)
        {
            this.createExternalIdentifier(userId,
                                          processGUID,
                                          processGUIDParameterName,
                                          OpenMetadataAPIMapper.PROCESS_TYPE_NAME,
                                          correlationProperties,
                                          methodName);
        }

        return processGUID;
    }


    /**
     * Update the metadata element representing a process.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param processGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param processProperties new properties for the metadata element
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateProcess(String                        userId,
                              MetadataCorrelationProperties correlationProperties,
                              String                        processGUID,
                              boolean                       isMergeUpdate,
                              ProcessProperties             processProperties,
                              String                        methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String processGUIDParameterName   = "processGUID";
        final String propertiesParameterName    = "processProperties";
        final String qualifiedNameParameterName = "processProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, processGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(processProperties, propertiesParameterName, methodName);

        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(processProperties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        this.validateExternalIdentifier(userId,
                                        processGUID,
                                        processGUIDParameterName,
                                        OpenMetadataAPIMapper.PROCESS_TYPE_NAME,
                                        correlationProperties,
                                        methodName);

        String externalSourceGUID = null;
        String externalSourceName = null;

        if ((correlationProperties != null) && (correlationProperties.getAssetManagerGUID() != null))
        {
            externalSourceGUID = correlationProperties.getAssetManagerGUID();
            externalSourceName = correlationProperties.getAssetManagerName();
        }

        String typeName = OpenMetadataAPIMapper.DEPLOYED_SOFTWARE_COMPONENT_TYPE_NAME;

        if (processProperties.getTypeName() != null)
        {
            typeName = processProperties.getTypeName();
        }

        int ownerCategory = OwnerCategory.USER_ID.getOpenTypeOrdinal();

        if (processProperties.getOwnerCategory() != null)
        {
            ownerCategory = processProperties.getOwnerCategory().getOpenTypeOrdinal();
        }

        processHandler.updateProcess(userId,
                                     externalSourceGUID,
                                     externalSourceName,
                                     processGUID,
                                     processGUIDParameterName,
                                     isMergeUpdate,
                                     processProperties.getQualifiedName(),
                                     processProperties.getTechnicalName(),
                                     processProperties.getTechnicalDescription(),
                                     processProperties.getFormula(),
                                     processProperties.getImplementationLanguage(),
                                     processProperties.getZoneMembership(),
                                     processProperties.getOwner(),
                                     ownerCategory,
                                     processProperties.getOriginOrganizationGUID(),
                                     processProperties.getOriginBusinessCapabilityGUID(),
                                     processProperties.getOtherOriginValues(),
                                     processProperties.getAdditionalProperties(),
                                     typeName,
                                     processProperties.getExtendedProperties(),
                                     methodName);

        this.maintainSupplementaryProperties(userId,
                                             processGUID,
                                             processProperties.getQualifiedName(),
                                             processProperties,
                                             isMergeUpdate,
                                             methodName);
    }


    /**
     * Update the status of the metadata element representing a process.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param processGUID unique identifier of the process to update
     * @param processStatus new status for the process
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateProcessStatus(String                        userId,
                                    MetadataCorrelationProperties correlationProperties,
                                    String                        processGUID,
                                    ProcessStatus                 processStatus,
                                    String                        methodName) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String propertiesParameterName = "processStatus";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, processGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(processStatus, propertiesParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        processGUID,
                                        ProcessExchangeHandler.processGUIDParameterName,
                                        OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                        correlationProperties,
                                        methodName);

        String externalSourceGUID = null;
        String externalSourceName = null;

        if ((correlationProperties != null) && (correlationProperties.getAssetManagerGUID() != null))
        {
            externalSourceGUID = correlationProperties.getAssetManagerGUID();
            externalSourceName = correlationProperties.getAssetManagerName();
        }

        processHandler.updateProcessStatus(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           processGUID,
                                           processGUIDParameterName,
                                           getInstanceStatus(processStatus),
                                           propertiesParameterName,
                                           methodName);
    }


    /**
     * Create a parent-child relationship between two processes.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param parentProcessGUID unique identifier of the process in the external process manager that is to be the parent process
     * @param childProcessGUID unique identifier of the process in the external process manager that is to be the nested sub-process
     * @param containmentType describes the ownership of the sub-process
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupProcessParent(String                 userId,
                                   String                 assetManagerGUID,
                                   String                 assetManagerName,
                                   boolean                assetManagerIsHome,
                                   String                 parentProcessGUID,
                                   String                 childProcessGUID,
                                   ProcessContainmentType containmentType,
                                   String                 methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String parentProcessGUIDParameterName = "parentProcessGUID";
        final String childProcessGUIDParameterName  = "childProcessGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentProcessGUID, parentProcessGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(childProcessGUID, childProcessGUIDParameterName, methodName);

        String externalSourceGUID = null;
        String externalSourceName = null;

        if ((assetManagerIsHome) && (assetManagerGUID != null))
        {
            externalSourceGUID = assetManagerGUID;
            externalSourceName = assetManagerName;
        }

        int containmentTypeOrdinal = ProcessContainmentType.USED.getOpenTypeOrdinal();

        if (containmentType != null)
        {
            containmentTypeOrdinal = containmentType.getOpenTypeOrdinal();
        }

        processHandler.setupProcessParent(userId,
                                          externalSourceGUID,
                                          externalSourceName,
                                          parentProcessGUID,
                                          parentProcessGUIDParameterName,
                                          childProcessGUID,
                                          childProcessGUIDParameterName,
                                          containmentTypeOrdinal,
                                          methodName);
    }


    /**
     * Remove a parent-child relationship between two processes.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param parentProcessGUID unique identifier of the process in the external process manager that is to be the parent process
     * @param childProcessGUID unique identifier of the process in the external process manager that is to be the nested sub-process
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearProcessParent(String userId,
                                   String assetManagerGUID,
                                   String assetManagerName,
                                   String parentProcessGUID,
                                   String childProcessGUID,
                                   String methodName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        final String parentProcessGUIDParameterName = "parentProcessGUID";
        final String childProcessGUIDParameterName  = "childProcessGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentProcessGUID, parentProcessGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(childProcessGUID, childProcessGUIDParameterName, methodName);

        processHandler.clearProcessParent(userId,
                                          assetManagerGUID,
                                          assetManagerName,
                                          parentProcessGUID,
                                          parentProcessGUIDParameterName,
                                          childProcessGUID,
                                          childProcessGUIDParameterName,
                                          methodName);
    }


    /**
     * Update the zones for the process so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Asset Manager OMAS).
     *
     * @param userId calling user
     * @param processGUID unique identifier of the metadata element to publish
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void publishProcess(String userId,
                               String processGUID,
                               String methodName) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, processGUIDParameterName, methodName);

        processHandler.publishProcess(userId, processGUID, processGUIDParameterName, methodName);
    }


    /**
     * Update the zones for the process so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Asset Manager OMAS.  This is the setting when the process is first created).
     *
     * @param userId calling user
     * @param processGUID unique identifier of the metadata element to withdraw
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void withdrawProcess(String userId,
                                String processGUID,
                                String methodName) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, processGUIDParameterName, methodName);

        processHandler.withdrawProcess(userId, processGUID, processGUIDParameterName, methodName);
    }


    /**
     * Remove the metadata element representing a process.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param processGUID unique identifier of the metadata element to remove
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeProcess(String                        userId,
                              MetadataCorrelationProperties correlationProperties,
                              String                        processGUID,
                              String                        methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, processGUIDParameterName, methodName);

        String externalSourceGUID = null;
        String externalSourceName = null;

        if ((correlationProperties != null) && (correlationProperties.getAssetManagerGUID() != null))
        {
            externalSourceGUID = correlationProperties.getAssetManagerGUID();
            externalSourceName = correlationProperties.getAssetManagerName();
        }

        this.validateExternalIdentifier(userId,
                                        processGUID,
                                        processGUIDParameterName,
                                        OpenMetadataAPIMapper.PROCESS_TYPE_NAME,
                                        correlationProperties,
                                        methodName);

        processHandler.removeProcess(userId,
                                     externalSourceGUID,
                                     externalSourceName,
                                     processGUID,
                                     processGUIDParameterName,
                                     methodName);
    }


    /**
     * Retrieve the list of process metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param searchString string to find in the properties
     * @param searchStringParameterName parameter supplying searchString
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ProcessElement> findProcesses(String userId,
                                              String assetManagerGUID,
                                              String assetManagerName,
                                              String searchString,
                                              String searchStringParameterName,
                                              int    startFrom,
                                              int    pageSize,
                                              String methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        List<ProcessElement> results = processHandler.findProcesses(userId,
                                                                    searchString,
                                                                    searchStringParameterName,
                                                                    startFrom,
                                                                    pageSize,
                                                                    methodName);

        addCorrelationPropertiesToProcesses(userId, assetManagerGUID, assetManagerName, results, methodName);

        return results;
    }


    /**
     * Return the list of processes associated with the process manager.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param methodName calling method
     *
     * @return list of metadata elements describing the processes associated with the requested process manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ProcessElement>   getProcessesForAssetManager(String userId,
                                                              String assetManagerGUID,
                                                              String assetManagerName,
                                                              int    startFrom,
                                                              int    pageSize,
                                                              String methodName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String assetManagerGUIDParameterName  = "assetManagerGUID";
        final String processEntityParameterName     = "processEntity";
        final String processEntityGUIDParameterName = "processEntity.getGUID()";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetManagerGUID, assetManagerGUIDParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<ProcessElement> results = new ArrayList<>();

        List<EntityDetail> processEntities = externalIdentifierHandler.getElementEntitiesForScope(userId,
                                                                                                  assetManagerGUID,
                                                                                                  assetManagerGUIDParameterName,
                                                                                                  OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                                                                                  OpenMetadataAPIMapper.PROCESS_TYPE_NAME,
                                                                                                  startFrom,
                                                                                                  validatedPageSize,
                                                                                                  methodName);

        if (processEntities != null)
        {
            for (EntityDetail processEntity : processEntities)
            {
                if (processEntity != null)
                {
                    ProcessElement processElement = processHandler.getBeanFromEntity(userId,
                                                                                     processEntity,
                                                                                     processEntityParameterName,
                                                                                     methodName);

                    if (processElement != null)
                    {
                        processElement.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                                           processEntity.getGUID(),
                                                                                           processEntityGUIDParameterName,
                                                                                           OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                                                           assetManagerGUID,
                                                                                           assetManagerName,
                                                                                           methodName));

                        results.add(processElement);
                    }
                }
            }
        }

        if (results.isEmpty())
        {
            return null;
        }
        else
        {
            return results;
        }
    }


    /**
     * Retrieve the list of process metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param name name to search for
     * @param nameParameterName parameter supplying name
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ProcessElement>   getProcessesByName(String userId,
                                                     String assetManagerGUID,
                                                     String assetManagerName,
                                                     String name,
                                                     String nameParameterName,
                                                     int    startFrom,
                                                     int    pageSize,
                                                     String methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<ProcessElement> results = processHandler.getProcessesByName(userId,
                                                                         name,
                                                                         nameParameterName,
                                                                         startFrom,
                                                                         validatedPageSize,
                                                                         methodName);

        addCorrelationPropertiesToProcesses(userId, assetManagerGUID, assetManagerName, results, methodName);

        return results;
    }


    /**
     * Retrieve the process metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param processGUID unique identifier of the requested metadata element
     * @param methodName calling method
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ProcessElement getProcessByGUID(String userId,
                                           String assetManagerGUID,
                                           String assetManagerName,
                                           String processGUID,
                                           String methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, processGUIDParameterName, methodName);

        ProcessElement element = processHandler.getProcessByGUID(userId, processGUID, processGUIDParameterName, methodName);

        if (element != null)
        {
            element.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                        element.getElementHeader().getGUID(),
                                                                        processGUIDParameterName,
                                                                        OpenMetadataAPIMapper.PROCESS_TYPE_NAME,
                                                                        assetManagerGUID,
                                                                        assetManagerName,
                                                                        methodName));
        }

        return element;
    }


    /**
     * Retrieve the process metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param processGUID unique identifier of the requested metadata element
     * @param methodName calling method
     *
     * @return parent process element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ProcessElement getProcessParent(String userId,
                                           String assetManagerGUID,
                                           String assetManagerName,
                                           String processGUID,
                                           String methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, processGUIDParameterName, methodName);

        ProcessElement element = processHandler.getProcessParent(userId, processGUID, processGUIDParameterName, methodName);

        if (element != null)
        {
            element.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                        element.getElementHeader().getGUID(),
                                                                        processGUIDParameterName,
                                                                        OpenMetadataAPIMapper.PROCESS_TYPE_NAME,
                                                                        assetManagerGUID,
                                                                        assetManagerName,
                                                                        methodName));
        }

        return element;
    }


    /**
     * Retrieve the process metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param processGUID unique identifier of the requested metadata element
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param methodName calling method
     *
     * @return list of process element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ProcessElement> getSubProcesses(String userId,
                                                String assetManagerGUID,
                                                String assetManagerName,
                                                String processGUID,
                                                int    startFrom,
                                                int    pageSize,
                                                String methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, processGUIDParameterName, methodName);

        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<ProcessElement> results = processHandler.getSubProcesses(userId,
                                                                      processGUID,
                                                                      processGUIDParameterName,
                                                                      startFrom,
                                                                      validatedPageSize,
                                                                      methodName);

        addCorrelationPropertiesToProcesses(userId, assetManagerGUID, assetManagerName, results, methodName);

        return results;
    }


    /* ===============================================================================
     * A process typically contains ports that show the flow of data and control to and from it.
     */

    /**
     * Create a new metadata element to represent a port.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param assetManagerIsHome ensure that only the process manager can update this port
     * @param processGUID unique identifier of the process where the port is located
     * @param portProperties properties for the port
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element for the port
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createPort(String                        userId,
                             MetadataCorrelationProperties correlationProperties,
                             boolean                       assetManagerIsHome,
                             String                        processGUID,
                             PortProperties                portProperties,
                             String                        methodName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String propertiesParameterName     = "portProperties";
        final String qualifiedNameParameterName  = "portProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, processGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(portProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(portProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        String externalSourceGUID = null;
        String externalSourceName = null;

        if ((assetManagerIsHome) && (correlationProperties != null) && (correlationProperties.getAssetManagerGUID() != null))
        {
            externalSourceGUID = correlationProperties.getAssetManagerGUID();
            externalSourceName = correlationProperties.getAssetManagerName();
        }

        String typeName = OpenMetadataAPIMapper.PORT_TYPE_NAME;

        if (portProperties.getTypeName() != null)
        {
            typeName = portProperties.getTypeName();
        }

        int portType = PortType.OTHER.getOpenTypeOrdinal();

        if (portProperties.getPortType() != null)
        {
            portType = portProperties.getPortType().getOpenTypeOrdinal();
        }

        String portGUID = processHandler.createPort(userId,
                                                    externalSourceGUID,
                                                    externalSourceName,
                                                    processGUID,
                                                    processGUIDParameterName,
                                                    portProperties.getQualifiedName(),
                                                    portProperties.getDisplayName(),
                                                    portType,
                                                    portProperties.getAdditionalProperties(),
                                                    typeName,
                                                    portProperties.getExtendedProperties(),
                                                    methodName);

        if (portGUID != null)
        {
            this.createExternalIdentifier(userId,
                                          portGUID,
                                          portGUIDParameterName,
                                          OpenMetadataAPIMapper.PORT_TYPE_NAME,
                                          correlationProperties,
                                          methodName);
        }

        return portGUID;
    }


    /**
     * Update the properties of the metadata element representing a port.  This call replaces
     * all existing properties with the supplied properties.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param portGUID unique identifier of the port to update
     * @param portProperties new properties for the port
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updatePort(String                        userId,
                           MetadataCorrelationProperties correlationProperties,
                           String                        portGUID,
                           PortProperties                portProperties,
                           String                        methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String portGUIDParameterName       = "portGUID";
        final String propertiesParameterName     = "portProperties";
        final String qualifiedNameParameterName  = "portProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(portGUID, portGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(portProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(portProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        String typeName = OpenMetadataAPIMapper.PORT_TYPE_NAME;

        if (portProperties.getTypeName() != null)
        {
            typeName = portProperties.getTypeName();
        }

        this.validateExternalIdentifier(userId,
                                        portGUID,
                                        portGUIDParameterName,
                                        typeName,
                                        correlationProperties,
                                        methodName);

        int portType = PortType.OTHER.getOpenTypeOrdinal();

        if (portProperties.getPortType() != null)
        {
            portType = portProperties.getPortType().getOpenTypeOrdinal();
        }

        processHandler.updatePort(userId,
                                  correlationProperties.getAssetManagerGUID(),
                                  correlationProperties.getAssetManagerName(),
                                  portGUID,
                                  portGUIDParameterName,
                                  portProperties.getQualifiedName(),
                                  portProperties.getDisplayName(),
                                  portType,
                                  portProperties.getAdditionalProperties(),
                                  typeName,
                                  portProperties.getExtendedProperties(),
                                  methodName);
    }


    /**
     * Link a port to a process.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param processGUID unique identifier of the process
     * @param portGUID unique identifier of the port
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupProcessPort(String  userId,
                                 String  assetManagerGUID,
                                 String  assetManagerName,
                                 boolean assetManagerIsHome,
                                 String  processGUID,
                                 String  portGUID,
                                 String  methodName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, processGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(portGUID, portGUIDParameterName, methodName);

        String externalSourceGUID = null;
        String externalSourceName = null;

        if ((assetManagerIsHome) && (assetManagerGUID != null))
        {
            externalSourceGUID = assetManagerGUID;
            externalSourceName = assetManagerName;
        }

        processHandler.setupProcessPort(userId,
                                        externalSourceGUID,
                                        externalSourceName,
                                        processGUID,
                                        processGUIDParameterName,
                                        portGUID,
                                        portGUIDParameterName,
                                        methodName);
    }


    /**
     * Unlink a port from a process.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param processGUID unique identifier of the process
     * @param portGUID unique identifier of the port
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearProcessPort(String userId,
                                 String assetManagerGUID,
                                 String assetManagerName,
                                 String processGUID,
                                 String portGUID,
                                 String methodName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, processGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(portGUID, portGUIDParameterName, methodName);

        processHandler.clearProcessPort(userId,
                                        assetManagerGUID,
                                        assetManagerName,
                                        processGUID,
                                        processGUIDParameterName,
                                        portGUID,
                                        portGUIDParameterName,
                                        methodName);
    }


    /**
     * Link two ports together to show that portTwo is an implementation of portOne. (That is, portOne delegates to
     * portTwo.)
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param portOneGUID unique identifier of the port at end 1
     * @param portTwoGUID unique identifier of the port at end 2
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupPortDelegation(String  userId,
                                    String  assetManagerGUID,
                                    String  assetManagerName,
                                    boolean assetManagerIsHome,
                                    String  portOneGUID,
                                    String  portTwoGUID,
                                    String  methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String portOneGUIDParameterName = "portOneGUID";
        final String portTwoGUIDParameterName = "portTwoGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(portOneGUID, portOneGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(portTwoGUID, portTwoGUIDParameterName, methodName);

        String externalSourceGUID = null;
        String externalSourceName = null;

        if ((assetManagerIsHome) && (assetManagerGUID != null))
        {
            externalSourceGUID = assetManagerGUID;
            externalSourceName = assetManagerName;
        }

        processHandler.setupPortDelegation(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           portOneGUID,
                                           portOneGUIDParameterName,
                                           portTwoGUID,
                                           portTwoGUIDParameterName,
                                           methodName);
    }


    /**
     * Remove the port delegation relationship between two ports.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param portOneGUID unique identifier of the port at end 1
     * @param portTwoGUID unique identifier of the port at end 2
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearPortDelegation(String userId,
                                    String assetManagerGUID,
                                    String assetManagerName,
                                    String portOneGUID,
                                    String portTwoGUID,
                                    String methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        final String portOneGUIDParameterName = "portOneGUID";
        final String portTwoGUIDParameterName = "portTwoGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(portOneGUID, portOneGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(portTwoGUID, portTwoGUIDParameterName, methodName);

        processHandler.clearPortDelegation(userId,
                                           assetManagerGUID,
                                           assetManagerName,
                                           portOneGUID,
                                           portOneGUIDParameterName,
                                           portTwoGUID,
                                           portTwoGUIDParameterName,
                                           methodName);
    }


    /**
     * Link a schema type to a port to show the structure of data it accepts.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param portGUID unique identifier of the port
     * @param schemaTypeGUID unique identifier of the schemaType
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupPortSchemaType(String  userId,
                                    String  assetManagerGUID,
                                    String  assetManagerName,
                                    boolean assetManagerIsHome,
                                    String  portGUID,
                                    String  schemaTypeGUID,
                                    String  methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String portGUIDParameterName       = "portGUID";
        final String schemaTypeGUIDParameterName = "schemaTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(portGUID, portGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, schemaTypeGUIDParameterName, methodName);

        String externalSourceGUID = null;
        String externalSourceName = null;

        if ((assetManagerIsHome) && (assetManagerGUID != null))
        {
            externalSourceGUID = assetManagerGUID;
            externalSourceName = assetManagerName;
        }

        processHandler.setupPortSchemaType(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           portGUID,
                                           portGUIDParameterName,
                                           schemaTypeGUID,
                                           schemaTypeGUIDParameterName,
                                           methodName);
    }


    /**
     * Remove the schema type from a port.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param portGUID unique identifier of the port
     * @param schemaTypeGUID unique identifier of the schemaType
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearPortSchemaType(String userId,
                                    String assetManagerGUID,
                                    String assetManagerName,
                                    String portGUID,
                                    String schemaTypeGUID,
                                    String methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        final String portGUIDParameterName       = "portGUID";
        final String schemaTypeGUIDParameterName = "schemaTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(portGUID, portGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, schemaTypeGUIDParameterName, methodName);

        processHandler.clearPortSchemaType(userId,
                                           assetManagerGUID,
                                           assetManagerName,
                                           portGUID,
                                           portGUIDParameterName,
                                           schemaTypeGUID,
                                           schemaTypeGUIDParameterName,
                                           methodName);
    }


    /**
     * Remove the metadata element representing a port.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param portGUID unique identifier of the metadata element to remove
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removePort(String                        userId,
                           MetadataCorrelationProperties correlationProperties,
                           String                        portGUID,
                           String                        methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(portGUID, portGUIDParameterName, methodName);

        String externalSourceGUID = null;
        String externalSourceName = null;

        if ((correlationProperties != null) && (correlationProperties.getAssetManagerGUID() != null))
        {
            externalSourceGUID = correlationProperties.getAssetManagerGUID();
            externalSourceName = correlationProperties.getAssetManagerName();
        }

        this.validateExternalIdentifier(userId,
                                        portGUID,
                                        portGUIDParameterName,
                                        OpenMetadataAPIMapper.PORT_TYPE_NAME,
                                        correlationProperties,
                                        methodName);

        processHandler.removePort(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  portGUID,
                                  portGUIDParameterName,
                                  methodName);
    }


    /**
     * Retrieve the list of port metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param searchString string to find in the properties
     * @param searchStringParameterName parameter supplying search string
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<PortElement>   findPorts(String userId,
                                         String assetManagerGUID,
                                         String assetManagerName,
                                         String searchString,
                                         String searchStringParameterName,
                                         int    startFrom,
                                         int    pageSize,
                                         String methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        List<PortElement> results = processHandler.findPorts(userId,
                                                             searchString,
                                                             searchStringParameterName,
                                                             startFrom,
                                                             pageSize,
                                                             methodName);

        addCorrelationPropertiesToPorts(userId, assetManagerGUID, assetManagerName, results, methodName);

        return results;
    }


    /**
     * Retrieve the list of ports associated with a process.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param processGUID unique identifier of the process of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param methodName calling method
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<PortElement>    getPortsForProcess(String userId,
                                                   String assetManagerGUID,
                                                   String assetManagerName,
                                                   String processGUID,
                                                   int    startFrom,
                                                   int    pageSize,
                                                   String methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        List<PortElement> results = processHandler.getPortsForProcess(userId,
                                                                      processGUID,
                                                                      processGUIDParameterName,
                                                                      startFrom,
                                                                      pageSize,
                                                                      methodName);

        addCorrelationPropertiesToPorts(userId, assetManagerGUID, assetManagerName, results, methodName);

        return results;
    }


    /**
     * Retrieve the list of ports that delegate to this port.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param portGUID unique identifier of the starting port
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param methodName calling method
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<PortElement>  getPortUse(String userId,
                                         String assetManagerGUID,
                                         String assetManagerName,
                                         String portGUID,
                                         int    startFrom,
                                         int    pageSize,
                                         String methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        List<PortElement> results = processHandler.getPortUse(userId,
                                                              portGUID,
                                                              portGUIDParameterName,
                                                              startFrom,
                                                              pageSize,
                                                              methodName);

        addCorrelationPropertiesToPorts(userId, assetManagerGUID, assetManagerName, results, methodName);

        return results;
    }


    /**
     * Retrieve the port that this port delegates to.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param portGUID unique identifier of the starting port alias
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public PortElement getPortDelegation(String userId,
                                         String assetManagerGUID,
                                         String assetManagerName,
                                         String portGUID,
                                         String methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        PortElement element = processHandler.getPortDelegation(userId, portGUID, portGUIDParameterName, methodName);

        if ((element != null) && (element.getElementHeader() != null) && (element.getElementHeader().getGUID() != null))
        {
            element.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                        element.getElementHeader().getGUID(),
                                                                        portGUIDParameterName,
                                                                        OpenMetadataAPIMapper.PORT_TYPE_NAME,
                                                                        assetManagerGUID,
                                                                        assetManagerName,
                                                                        methodName));
        }

        return element;
    }


    /**
     * Retrieve the list of port metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param name name to search for
     * @param nameParameterName parameter supplying name
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<PortElement>   getPortsByName(String userId,
                                              String assetManagerGUID,
                                              String assetManagerName,
                                              String name,
                                              String nameParameterName,
                                              int    startFrom,
                                              int    pageSize,
                                              String methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        List<PortElement> results = processHandler.getPortsByName(userId,
                                                                  name,
                                                                  nameParameterName,
                                                                  startFrom,
                                                                  pageSize,
                                                                  methodName);

        addCorrelationPropertiesToPorts(userId, assetManagerGUID, assetManagerName, results, methodName);

        return results;
    }


    /**
     * Retrieve the port metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param portGUID unique identifier of the requested metadata element
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public PortElement getPortByGUID(String userId,
                                     String assetManagerGUID,
                                     String assetManagerName,
                                     String portGUID,
                                     String methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        PortElement element = processHandler.getPortByGUID(userId, portGUID, portGUIDParameterName, methodName);

        if ((element != null) && (element.getElementHeader() != null) && (element.getElementHeader().getGUID() != null))
        {
            element.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                        element.getElementHeader().getGUID(),
                                                                        portGUIDParameterName,
                                                                        OpenMetadataAPIMapper.PORT_TYPE_NAME,
                                                                        assetManagerGUID,
                                                                        assetManagerName,
                                                                        methodName));
        }

        return element;
    }


    /* ===============================================================================
     * General linkage and classifications
     */


    /**
     * Classify a port, process or process as "BusinessSignificant" (this may effect the way that lineage is displayed).
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param elementGUID unique identifier of the metadata element to update
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setBusinessSignificant(String                        userId,
                                       MetadataCorrelationProperties correlationProperties,
                                       String                        elementGUID,
                                       String                        methodName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String elementGUIDParameterName = "elementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        elementGUID,
                                        elementGUIDParameterName,
                                        OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                        correlationProperties,
                                        methodName);

        processHandler.setBusinessSignificant(userId, elementGUID, elementGUIDParameterName, methodName);
    }


    /**
     * Remove the "BusinessSignificant" designation from the element.
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param elementGUID unique identifier of the metadata element to update
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearBusinessSignificant(String                        userId,
                                         MetadataCorrelationProperties correlationProperties,
                                         String                        elementGUID,
                                         String                        methodName) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String elementGUIDParameterName = "elementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        elementGUID,
                                        elementGUIDParameterName,
                                        OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                        correlationProperties,
                                        methodName);

        processHandler.clearBusinessSignificant(userId, elementGUID, elementGUIDParameterName, methodName);
    }


    /**
     * Link two elements together to show that data flows from one to the other.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param dataSupplierGUID unique identifier of the data supplier
     * @param dataConsumerGUID unique identifier of the data consumer
     * @param qualifiedName unique identifier for this relationship
     * @param description description and/or purpose of the data flow
     * @param formula function that determines the subset of the data that flows
     * @param methodName calling method
     *
     * @return unique identifier of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String setupDataFlow(String  userId,
                                String  assetManagerGUID,
                                String  assetManagerName,
                                boolean assetManagerIsHome,
                                String  dataSupplierGUID,
                                String  dataConsumerGUID,
                                String  qualifiedName,
                                String  description,
                                String  formula,
                                String  methodName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        final String dataSupplierGUIDParameterName = "dataSupplierGUID";
        final String dataConsumerGUIDParameterName = "dataConsumerGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataSupplierGUID, dataSupplierGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(dataConsumerGUID, dataConsumerGUIDParameterName, methodName);

        String externalSourceGUID = null;
        String externalSourceName = null;

        if ((assetManagerIsHome) && (assetManagerGUID != null))
        {
            externalSourceGUID = assetManagerGUID;
            externalSourceName = assetManagerName;
        }

        return processHandler.setupDataFlow(userId,
                                            externalSourceGUID,
                                            externalSourceName,
                                            dataSupplierGUID,
                                            dataSupplierGUIDParameterName,
                                            dataConsumerGUID,
                                            dataSupplierGUIDParameterName,
                                            qualifiedName,
                                            description,
                                            formula,
                                            methodName);
    }


    /**
     * Retrieve the data flow relationship between two elements.  The qualifiedName is optional unless there
     * is more than one data flow relationships between these two elements since it is used to disambiguate
     * the request.
     *
     * @param userId calling user
     * @param dataSupplierGUID unique identifier of the data supplier
     * @param dataConsumerGUID unique identifier of the data consumer
     * @param qualifiedName unique identifier for this relationship
     * @param methodName calling method
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DataFlowElement getDataFlow(String userId,
                                       String dataSupplierGUID,
                                       String dataConsumerGUID,
                                       String qualifiedName,
                                       String methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String dataSupplierGUIDParameterName = "dataSupplierGUID";
        final String dataConsumerGUIDParameterName = "dataConsumerGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataSupplierGUID, dataSupplierGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(dataConsumerGUID, dataConsumerGUIDParameterName, methodName);

        return processHandler.getDataFlow(userId,
                                          dataSupplierGUID,
                                          dataSupplierGUIDParameterName,
                                          dataConsumerGUID,
                                          dataConsumerGUIDParameterName,
                                          qualifiedName,
                                          methodName);
    }


    /**
     * Update relationship between two elements that shows that data flows from one to the other.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param dataFlowGUID unique identifier of the data flow relationship
     * @param qualifiedName unique identifier for this relationship
     * @param description description and/or purpose of the data flow
     * @param formula function that determines the subset of the data that flows
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void   updateDataFlow(String userId,
                                 String assetManagerGUID,
                                 String assetManagerName,
                                 String dataFlowGUID,
                                 String qualifiedName,
                                 String description,
                                 String formula,
                                 String methodName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        final String dataFlowGUIDParameterName = "dataFlowGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataFlowGUID, dataFlowGUIDParameterName, methodName);

        processHandler.updateDataFlow(userId,
                                      assetManagerGUID,
                                      assetManagerName,
                                      dataFlowGUID,
                                      dataFlowGUIDParameterName,
                                      qualifiedName,
                                      description,
                                      formula,
                                      methodName);
    }


    /**
     * Remove the data flow relationship between two elements.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param dataFlowGUID unique identifier of the data flow relationship
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearDataFlow(String userId,
                              String assetManagerGUID,
                              String assetManagerName,
                              String dataFlowGUID,
                              String methodName) throws InvalidParameterException,
                                                        UserNotAuthorizedException,
                                                        PropertyServerException
    {
        final String dataFlowGUIDParameterName = "dataFlowGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataFlowGUID, dataFlowGUIDParameterName, methodName);

        processHandler.clearDataFlow(userId,
                                     assetManagerGUID,
                                     assetManagerName,
                                     dataFlowGUID,
                                     dataFlowGUIDParameterName,
                                     methodName);
    }


    /**
     * Retrieve the data flow relationships linked from an specific element to the downstream consumers.
     *
     * @param userId calling user
     * @param dataSupplierGUID unique identifier of the data supplier
     * @param methodName calling method
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DataFlowElement> getDataFlowConsumers(String userId,
                                                      String dataSupplierGUID,
                                                      String methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String dataSupplierGUIDParameterName = "dataSupplierGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataSupplierGUID, dataSupplierGUIDParameterName, methodName);

        return processHandler.getDataFlowConsumers(userId, dataSupplierGUID, dataSupplierGUIDParameterName, methodName);
    }


    /**
     * Retrieve the data flow relationships linked from an specific element to the upstream suppliers.
     *
     * @param userId calling user
     * @param dataConsumerGUID unique identifier of the data consumer
     * @param methodName calling method
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DataFlowElement> getDataFlowSuppliers(String userId,
                                                      String dataConsumerGUID,
                                                      String methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String dataConsumerGUIDParameterName = "dataConsumerGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataConsumerGUID, dataConsumerGUIDParameterName, methodName);

        return processHandler.getDataFlowSuppliers(userId, dataConsumerGUID, dataConsumerGUIDParameterName, methodName);
    }


    /**
     * Link two elements to show that when one completes the next is started.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param currentStepGUID unique identifier of the previous step
     * @param nextStepGUID unique identifier of the next step
     * @param qualifiedName unique identifier for this relationship
     * @param description description and/or purpose of the data flow
     * @param guard function that must be true to travel down this control flow
     * @param methodName calling method
     *
     * @return unique identifier for the control flow relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String setupControlFlow(String  userId,
                                   String  assetManagerGUID,
                                   String  assetManagerName,
                                   boolean assetManagerIsHome,
                                   String  currentStepGUID,
                                   String  nextStepGUID,
                                   String  qualifiedName,
                                   String  description,
                                   String  guard,
                                   String  methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        final String currentStepGUIDParameterName = "currentStepGUID";
        final String nextStepGUIDParameterName    = "nextStepGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(currentStepGUID, currentStepGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(nextStepGUID, nextStepGUIDParameterName, methodName);

        String externalSourceGUID = null;
        String externalSourceName = null;

        if ((assetManagerIsHome) && (assetManagerGUID != null))
        {
            externalSourceGUID = assetManagerGUID;
            externalSourceName = assetManagerName;
        }

        return processHandler.setupControlFlow(userId,
                                               externalSourceGUID,
                                               externalSourceName,
                                               currentStepGUID,
                                               currentStepGUIDParameterName,
                                               nextStepGUID,
                                               nextStepGUIDParameterName,
                                               qualifiedName,
                                               description,
                                               guard,
                                               methodName);
    }


    /**
     * Retrieve the control flow relationship between two elements.  The qualifiedName is optional unless there
     * is more than one control flow relationships between these two elements since it is used to disambiguate
     * the request.
     *
     * @param userId calling user
     * @param currentStepGUID unique identifier of the previous step
     * @param nextStepGUID unique identifier of the next step
     * @param qualifiedName unique identifier for this relationship
     * @param methodName calling method
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ControlFlowElement getControlFlow(String userId,
                                             String currentStepGUID,
                                             String nextStepGUID,
                                             String qualifiedName,
                                             String methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String currentStepGUIDParameterName = "currentStepGUID";
        final String nextStepGUIDParameterName    = "nextStepGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(currentStepGUID, currentStepGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(nextStepGUID, nextStepGUIDParameterName, methodName);

        return processHandler.getControlFlow(userId,
                                             currentStepGUID,
                                             currentStepGUIDParameterName,
                                             nextStepGUID,
                                             nextStepGUIDParameterName,
                                             qualifiedName,
                                             methodName);
    }


    /**
     * Update the relationship between two elements that shows that when one completes the next is started.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param controlFlowGUID unique identifier of the  control flow relationship
     * @param qualifiedName unique identifier for this relationship
     * @param description description and/or purpose of the data flow
     * @param guard function that must be true to travel down this control flow
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateControlFlow(String userId,
                                  String assetManagerGUID,
                                  String assetManagerName,
                                  String controlFlowGUID,
                                  String qualifiedName,
                                  String description,
                                  String guard,
                                  String methodName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        final String controlFlowGUIDParameterName = "controlFlowGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(controlFlowGUID, controlFlowGUIDParameterName, methodName);

        processHandler.updateControlFlow(userId,
                                         assetManagerGUID,
                                         assetManagerName,
                                         controlFlowGUID,
                                         controlFlowGUIDParameterName,
                                         qualifiedName,
                                         description,
                                         guard,
                                         methodName);
    }


    /**
     * Remove the control flow relationship between two elements.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param controlFlowGUID unique identifier of the  control flow relationship
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearControlFlow(String userId,
                                 String assetManagerGUID,
                                 String assetManagerName,
                                 String controlFlowGUID,
                                 String methodName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        final String controlFlowGUIDParameterName = "controlFlowGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(controlFlowGUID, controlFlowGUIDParameterName, methodName);

        processHandler.clearControlFlow(userId,
                                         assetManagerGUID,
                                         assetManagerName,
                                         controlFlowGUID,
                                         controlFlowGUIDParameterName,
                                         methodName);
    }


    /**
     * Retrieve the control relationships linked from an specific element to the possible next elements in the process.
     *
     * @param userId calling user
     * @param currentStepGUID unique identifier of the current step
     * @param methodName calling method
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ControlFlowElement> getControlFlowNextSteps(String userId,
                                                            String currentStepGUID,
                                                            String methodName) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String currentStepGUIDParameterName = "currentStepGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(currentStepGUID, currentStepGUIDParameterName, methodName);

        return processHandler.getControlFlowNextSteps(userId, currentStepGUID, currentStepGUIDParameterName, methodName);
    }


    /**
     * Retrieve the control relationships linked from an specific element to the possible previous elements in the process.
     *
     * @param userId calling user
     * @param currentStepGUID unique identifier of the current step
     * @param methodName calling method
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ControlFlowElement> getControlFlowPreviousSteps(String userId,
                                                                String currentStepGUID,
                                                                String methodName) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String currentStepGUIDParameterName = "currentStepGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(currentStepGUID, currentStepGUIDParameterName, methodName);

        return processHandler.getControlFlowPreviousSteps(userId, currentStepGUID, currentStepGUIDParameterName, methodName);
    }


    /**
     * Link two elements together to show a request-response call between them.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param callerGUID unique identifier of the element that is making the call
     * @param calledGUID unique identifier of the element that is processing the call
     * @param qualifiedName unique identifier for this relationship
     * @param description description and/or purpose of the data flow
     * @param formula function that determines the subset of the data that flows
     * @param methodName calling method
     *
     * @return unique identifier of the new relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String setupProcessCall(String  userId,
                                   String  assetManagerGUID,
                                   String  assetManagerName,
                                   boolean assetManagerIsHome,
                                   String  callerGUID,
                                   String  calledGUID,
                                   String  qualifiedName,
                                   String  description,
                                   String  formula,
                                   String  methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        final String callerGUIDParameterName = "callerGUID";
        final String calledGUIDParameterName = "calledGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(callerGUID, callerGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(calledGUID, calledGUIDParameterName, methodName);

        String externalSourceGUID = null;
        String externalSourceName = null;

        if ((assetManagerIsHome) && (assetManagerGUID != null))
        {
            externalSourceGUID = assetManagerGUID;
            externalSourceName = assetManagerName;
        }

        return processHandler.setupProcessCall(userId,
                                               externalSourceGUID,
                                               externalSourceName,
                                               callerGUID,
                                               callerGUIDParameterName,
                                               calledGUID,
                                               calledGUIDParameterName,
                                               qualifiedName,
                                               description,
                                               formula,
                                               methodName);
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
     * @param methodName calling method
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ProcessCallElement getProcessCall(String userId,
                                             String callerGUID,
                                             String calledGUID,
                                             String qualifiedName,
                                             String methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String callerGUIDParameterName    = "callerGUID";
        final String calledGUIDParameterName    = "calledGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(callerGUID, callerGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(calledGUID, calledGUIDParameterName, methodName);

        return processHandler.getProcessCall(userId,
                                             callerGUID,
                                             callerGUIDParameterName,
                                             calledGUID,
                                             calledGUIDParameterName,
                                             qualifiedName,
                                             methodName);
    }


    /**
     * Update the relationship between two elements that shows a request-response call between them.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param processCallGUID unique identifier of the process call relationship
     * @param qualifiedName unique identifier for this relationship
     * @param description description and/or purpose of the data flow
     * @param formula function that determines the subset of the data that flows
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateProcessCall(String userId,
                                  String assetManagerGUID,
                                  String assetManagerName,
                                  String processCallGUID,
                                  String qualifiedName,
                                  String description,
                                  String formula,
                                  String methodName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        final String processCallGUIDParameterName = "processCallGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processCallGUID, processCallGUIDParameterName, methodName);

        processHandler.updateProcessCall(userId,
                                         assetManagerGUID,
                                         assetManagerName,
                                         processCallGUID,
                                         processCallGUIDParameterName,
                                         qualifiedName,
                                         description,
                                         formula,
                                         methodName);
    }


    /**
     * Remove the process call relationship.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param processCallGUID unique identifier of the process call relationship
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearProcessCall(String userId,
                                 String assetManagerGUID,
                                 String assetManagerName,
                                 String processCallGUID,
                                 String methodName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        final String processCallGUIDParameterName = "processCallGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processCallGUID, processCallGUIDParameterName, methodName);

        processHandler.clearProcessCall(userId,
                                        assetManagerGUID,
                                        assetManagerName,
                                        processCallGUID,
                                        processCallGUIDParameterName,
                                        methodName);
    }


    /**
     * Retrieve the process call relationships linked from an specific element to the elements it calls.
     *
     * @param userId calling user
     * @param callerGUID unique identifier of the element that is making the call
     * @param methodName calling method
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ProcessCallElement> getProcessCalled(String userId,
                                                     String callerGUID,
                                                     String methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String callerGUIDParameterName    = "callerGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(callerGUID, callerGUIDParameterName, methodName);

        return processHandler.getProcessCallers(userId,
                                                callerGUID,
                                                callerGUIDParameterName,
                                                methodName);
    }


    /**
     * Retrieve the process call relationships linked from an specific element to its callers.
     *
     * @param userId calling user
     * @param calledGUID unique identifier of the element that is processing the call
     * @param methodName calling method
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ProcessCallElement> getProcessCallers(String userId,
                                                      String calledGUID,
                                                      String methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String calledGUIDParameterName    = "calledGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(calledGUID, calledGUIDParameterName, methodName);

        return processHandler.getProcessCallers(userId,
                                                calledGUID,
                                                calledGUIDParameterName,
                                                methodName);
    }


    /**
     * Link to elements together to show that they are part of the lineage of the data that is moving
     * between the processes.  Typically the lineage relationships stitch together processes and data assets
     * supported by different technologies.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param sourceElementGUID unique identifier of the source
     * @param destinationElementGUID unique identifier of the destination
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupLineageMapping(String userId,
                                    String assetManagerGUID,
                                    String assetManagerName,
                                    String sourceElementGUID,
                                    String destinationElementGUID,
                                    String methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        final String sourceElementGUIDParameterName      = "sourceElementGUID";
        final String destinationElementGUIDParameterName = "destinationElementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(sourceElementGUID, sourceElementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(destinationElementGUID, destinationElementGUIDParameterName, methodName);

        processHandler.setupLineageMapping(userId,
                                           sourceElementGUID,
                                           sourceElementGUIDParameterName,
                                           destinationElementGUID,
                                           destinationElementGUIDParameterName,
                                           methodName);

        externalIdentifierHandler.logRelationshipCreation(assetManagerGUID,
                                                          assetManagerName,
                                                          OpenMetadataAPIMapper.LINEAGE_MAPPING_TYPE_NAME,
                                                          sourceElementGUID,
                                                          OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                          destinationElementGUID,
                                                          OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                          methodName);
    }


    /**
     * Remove the lineage mapping between two elements.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param sourceElementGUID unique identifier of the source
     * @param destinationElementGUID unique identifier of the destination
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearLineageMapping(String userId,
                                    String assetManagerGUID,
                                    String assetManagerName,
                                    String sourceElementGUID,
                                    String destinationElementGUID,
                                    String methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        final String sourceElementGUIDParameterName      = "sourceElementGUID";
        final String destinationElementGUIDParameterName = "destinationElementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(sourceElementGUID, sourceElementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(destinationElementGUID, destinationElementGUIDParameterName, methodName);

        processHandler.clearLineageMapping(userId,
                                           sourceElementGUID,
                                           sourceElementGUIDParameterName,
                                           destinationElementGUID,
                                           destinationElementGUIDParameterName,
                                           methodName);

        externalIdentifierHandler.logRelationshipRemoval(assetManagerGUID,
                                                         assetManagerName,
                                                         OpenMetadataAPIMapper.LINEAGE_MAPPING_TYPE_NAME,
                                                         sourceElementGUID,
                                                         OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                         destinationElementGUID,
                                                         OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                         methodName);
    }


    /**
     * Retrieve the lineage mapping relationships linked from an specific source element to its destinations.
     *
     * @param userId calling user
     * @param sourceElementGUID unique identifier of the source
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<LineageMappingElement> getDestinationLineageMappings(String userId,
                                                                     String sourceElementGUID,
                                                                     String methodName) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String sourceElementGUIDParameterName      = "sourceElementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(sourceElementGUID, sourceElementGUIDParameterName, methodName);

        return processHandler.getDestinationLineageMappings(userId,
                                                            sourceElementGUID,
                                                            sourceElementGUIDParameterName,
                                                            methodName);
    }


    /**
     * Retrieve the lineage mapping relationships linked from an specific destination element to its sources.
     *
     * @param userId calling user
     * @param destinationElementGUID unique identifier of the destination
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<LineageMappingElement> getSourceLineageMappings(String userId,
                                                                String destinationElementGUID,
                                                                String methodName) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String destinationElementGUIDParameterName = "destinationElementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(destinationElementGUID, destinationElementGUIDParameterName, methodName);

        return processHandler.getSourceLineageMappings(userId,
                                                       destinationElementGUID,
                                                       destinationElementGUIDParameterName,
                                                       methodName);
    }
}
