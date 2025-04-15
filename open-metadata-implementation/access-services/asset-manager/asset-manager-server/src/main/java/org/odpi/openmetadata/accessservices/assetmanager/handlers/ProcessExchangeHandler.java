/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.handlers;

import org.odpi.openmetadata.accessservices.assetmanager.converters.*;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.*;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.PortElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ProcessElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.*;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.governanceaction.properties.MetadataCorrelationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.*;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.PortProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.ProcessProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
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
import java.util.Date;
import java.util.List;


/**
 * ProcessExchangeHandler is the server-side for managing processes.
 */
public class ProcessExchangeHandler extends ExchangeHandlerBase
{
    private final ProcessHandler<ProcessElement,
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
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
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
                                                     boolean              forLineage,
                                                     boolean              forDuplicateProcessing,
                                                     Date                 effectiveTime,
                                                     String               methodName) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        if (results != null)
        {
            for (ProcessElement element : results)
            {
                if ((element != null) && (element.getElementHeader() != null) && (element.getElementHeader().getGUID() != null))
                {
                    element.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                                element.getElementHeader().getGUID(),
                                                                                processGUIDParameterName,
                                                                                OpenMetadataType.PROCESS.typeName,
                                                                                assetManagerGUID,
                                                                                assetManagerName,
                                                                                forLineage,
                                                                                forDuplicateProcessing,
                                                                                effectiveTime,
                                                                                methodName));

                    this.getSupplementaryProperties(element.getElementHeader().getGUID(),
                                                    processGUIDParameterName,
                                                    OpenMetadataType.PROCESS.typeName,
                                                    element.getProcessProperties(),
                                                    forLineage,
                                                    forDuplicateProcessing,
                                                    effectiveTime,
                                                    methodName);
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
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
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
                                                 boolean           forLineage,
                                                 boolean           forDuplicateProcessing,
                                                 Date              effectiveTime,
                                                 String            methodName) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        if ((results != null) && (assetManagerGUID != null))
        {
            for (CorrelatedMetadataElement element : results)
            {
                if ((element != null) && (element.getElementHeader() != null) && (element.getElementHeader().getGUID() != null))
                {
                    element.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                                element.getElementHeader().getGUID(),
                                                                                portGUIDParameterName,
                                                                                OpenMetadataType.PORT.typeName,
                                                                                assetManagerGUID,
                                                                                assetManagerName,
                                                                                forLineage,
                                                                                forDuplicateProcessing,
                                                                                effectiveTime,
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
     * @param processStatus initial status of the process
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
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
                                ProcessStatus                 processStatus,
                                Date                          effectiveTime,
                                String                        methodName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String propertiesParameterName     = "processProperties";
        final String qualifiedNameParameterName  = "processProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(processProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(processProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        String typeName = OpenMetadataType.DEPLOYED_SOFTWARE_COMPONENT.typeName;

        if (processProperties.getTypeName() != null)
        {
            typeName = processProperties.getTypeName();
        }

        String processGUID = processHandler.createProcess(userId,
                                                          this.getExternalSourceGUID(correlationProperties, assetManagerIsHome),
                                                          this.getExternalSourceName(correlationProperties, assetManagerIsHome),
                                                          processProperties.getQualifiedName(),
                                                          processProperties.getName(),
                                                          processProperties.getResourceName(),
                                                          processProperties.getVersionIdentifier(),
                                                          processProperties.getResourceDescription(),
                                                          processProperties.getDeployedImplementationType(),
                                                          processProperties.getFormula(),
                                                          processProperties.getFormulaType(),
                                                          processProperties.getImplementationLanguage(),
                                                          processProperties.getAdditionalProperties(),
                                                          typeName,
                                                          processProperties.getExtendedProperties(),
                                                          this.getInstanceStatus(processStatus),
                                                          processProperties.getEffectiveFrom(),
                                                          processProperties.getEffectiveTo(),
                                                          effectiveTime,
                                                          methodName);

        if (processGUID != null)
        {
            this.maintainSupplementaryProperties(userId,
                                                 processGUID,
                                                 processGUIDParameterName,
                                                 OpenMetadataType.PROCESS.typeName,
                                                 OpenMetadataType.ASSET.typeName,
                                                 processProperties.getQualifiedName(),
                                                 processProperties,
                                                 true,
                                                 false,
                                                 false,
                                                 effectiveTime,
                                                 methodName);

            this.createExternalIdentifier(userId,
                                          processGUID,
                                          processGUIDParameterName,
                                          OpenMetadataType.PROCESS.typeName,
                                          correlationProperties,
                                          false,
                                          false,
                                          null,
                                          methodName);
        }

        return processGUID;
    }


    /**
     * Create a new metadata element to represent a process using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
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
                                            Date                          effectiveTime,
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
                                                                      templateProperties.getVersionIdentifier(),
                                                                      templateProperties.getDescription(),
                                                                      false,
                                                                      false,
                                                                      effectiveTime,
                                                                      methodName);
        if (processGUID != null)
        {
            this.createExternalIdentifier(userId,
                                          processGUID,
                                          processGUIDParameterName,
                                          OpenMetadataType.PROCESS.typeName,
                                          correlationProperties,
                                          false,
                                          false,
                                          null,
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
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
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
                              boolean                       forLineage,
                              boolean                       forDuplicateProcessing,
                              Date                          effectiveTime,
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
                                        OpenMetadataType.PROCESS.typeName,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        String typeName = OpenMetadataType.DEPLOYED_SOFTWARE_COMPONENT.typeName;

        if (processProperties.getTypeName() != null)
        {
            typeName = processProperties.getTypeName();
        }

        processHandler.updateProcess(userId,
                                     this.getExternalSourceGUID(correlationProperties),
                                     this.getExternalSourceName(correlationProperties),
                                     processGUID,
                                     processGUIDParameterName,
                                     isMergeUpdate,
                                     processProperties.getQualifiedName(),
                                     processProperties.getName(),
                                     processProperties.getResourceName(),
                                     processProperties.getVersionIdentifier(),
                                     processProperties.getResourceDescription(),
                                     processProperties.getDeployedImplementationType(),
                                     processProperties.getFormula(),
                                     processProperties.getImplementationLanguage(),
                                     processProperties.getAdditionalProperties(),
                                     typeName,
                                     processProperties.getExtendedProperties(),
                                     processProperties.getEffectiveFrom(),
                                     processProperties.getEffectiveTo(),
                                     forLineage,
                                     forDuplicateProcessing,
                                     effectiveTime,
                                     methodName);

        this.maintainSupplementaryProperties(userId,
                                             processGUID,
                                             processGUIDParameterName,
                                             OpenMetadataType.PROCESS.typeName,
                                             OpenMetadataType.ASSET.typeName,
                                             processProperties.getQualifiedName(),
                                             processProperties,
                                             isMergeUpdate,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
    }


    /**
     * Update the status of the metadata element representing a process.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param processGUID unique identifier of the process to update
     * @param processStatus new status for the process
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
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
                                    boolean                       forLineage,
                                    boolean                       forDuplicateProcessing,
                                    Date                          effectiveTime,
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
                                        OpenMetadataType.ASSET.typeName,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        processHandler.updateProcessStatus(userId,
                                           this.getExternalSourceGUID(correlationProperties),
                                           this.getExternalSourceName(correlationProperties),
                                           processGUID,
                                           processGUIDParameterName,
                                           getInstanceStatus(processStatus),
                                           propertiesParameterName,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
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
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
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
                                   Date                   effectiveFrom,
                                   Date                   effectiveTo,
                                   boolean                forLineage,
                                   boolean                forDuplicateProcessing,
                                   Date                   effectiveTime,
                                   String                 methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String parentProcessGUIDParameterName = "parentProcessGUID";
        final String childProcessGUIDParameterName  = "childProcessGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentProcessGUID, parentProcessGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(childProcessGUID, childProcessGUIDParameterName, methodName);

        int containmentTypeOrdinal = ProcessContainmentType.USED.getOrdinal();

        if (containmentType != null)
        {
            containmentTypeOrdinal = containmentType.getOrdinal();
        }

        processHandler.setupProcessParent(userId,
                                          this.getExternalSourceGUID(assetManagerGUID, assetManagerIsHome),
                                          this.getExternalSourceName(assetManagerName, assetManagerIsHome),
                                          parentProcessGUID,
                                          parentProcessGUIDParameterName,
                                          childProcessGUID,
                                          childProcessGUIDParameterName,
                                          effectiveFrom,
                                          effectiveTo,
                                          containmentTypeOrdinal,
                                          forLineage,
                                          forDuplicateProcessing,
                                          effectiveTime,
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
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearProcessParent(String  userId,
                                   String  assetManagerGUID,
                                   String  assetManagerName,
                                   String  parentProcessGUID,
                                   String  childProcessGUID,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing,
                                   Date    effectiveTime,
                                   String  methodName) throws InvalidParameterException,
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
                                          forLineage,
                                          forDuplicateProcessing,
                                          effectiveTime,
                                          methodName);
    }


    /**
     * Update the zones for the process so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Asset Manager OMAS).
     *
     * @param userId calling user
     * @param processGUID unique identifier of the metadata element to publish
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void publishProcess(String  userId,
                               String  processGUID,
                               boolean forLineage,
                               boolean forDuplicateProcessing,
                               Date    effectiveTime,
                               String  methodName) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, processGUIDParameterName, methodName);

        processHandler.publishProcess(userId,
                                      processGUID,
                                      processGUIDParameterName,
                                      forLineage,
                                      forDuplicateProcessing,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Update the zones for the process so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Asset Manager OMAS.  This is the setting when the process is first created).
     *
     * @param userId calling user
     * @param processGUID unique identifier of the metadata element to withdraw
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void withdrawProcess(String  userId,
                                String  processGUID,
                                boolean forLineage,
                                boolean forDuplicateProcessing,
                                Date    effectiveTime,
                                String  methodName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, processGUIDParameterName, methodName);

        processHandler.withdrawProcess(userId,
                                       processGUID,
                                       processGUIDParameterName,
                                       forLineage,
                                       forDuplicateProcessing,
                                       effectiveTime, methodName);
    }


    /**
     * Remove the metadata element representing a process.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param processGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeProcess(String                        userId,
                              MetadataCorrelationProperties correlationProperties,
                              String                        processGUID,
                              boolean                       forLineage,
                              boolean                       forDuplicateProcessing,
                              Date                          effectiveTime,
                              String                        methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, processGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        processGUID,
                                        processGUIDParameterName,
                                        OpenMetadataType.PROCESS.typeName,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        processHandler.removeProcess(userId,
                                     this.getExternalSourceGUID(correlationProperties),
                                     this.getExternalSourceName(correlationProperties),
                                     processGUID,
                                     processGUIDParameterName,
                                     forLineage,
                                     forDuplicateProcessing,
                                     effectiveTime,
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
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ProcessElement> findProcesses(String  userId,
                                              String  assetManagerGUID,
                                              String  assetManagerName,
                                              String  searchString,
                                              String  searchStringParameterName,
                                              int     startFrom,
                                              int     pageSize,
                                              boolean forLineage,
                                              boolean forDuplicateProcessing,
                                              Date    effectiveTime,
                                              String  methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        List<ProcessElement> results = processHandler.findProcesses(userId,
                                                                    searchString,
                                                                    searchStringParameterName,
                                                                    startFrom,
                                                                    pageSize,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    effectiveTime,
                                                                    methodName);

        addCorrelationPropertiesToProcesses(userId,
                                            assetManagerGUID,
                                            assetManagerName,
                                            results,
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveTime,
                                            methodName);

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
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of metadata elements describing the processes associated with the requested process manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ProcessElement>   getProcessesForAssetManager(String  userId,
                                                              String  assetManagerGUID,
                                                              String  assetManagerName,
                                                              int     startFrom,
                                                              int     pageSize,
                                                              boolean forLineage,
                                                              boolean forDuplicateProcessing,
                                                              Date    effectiveTime,
                                                              String  methodName) throws InvalidParameterException,
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
                                                                                                  OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                                                                  OpenMetadataType.PROCESS.typeName,
                                                                                                  startFrom,
                                                                                                  validatedPageSize,
                                                                                                  effectiveTime,
                                                                                                  forLineage,
                                                                                                  forDuplicateProcessing,
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
                                                                                           OpenMetadataType.ASSET.typeName,
                                                                                           assetManagerGUID,
                                                                                           assetManagerName,
                                                                                           forLineage,
                                                                                           forDuplicateProcessing,
                                                                                           effectiveTime,
                                                                                           methodName));

                        this.getSupplementaryProperties(processElement.getElementHeader().getGUID(),
                                                        processGUIDParameterName,
                                                        OpenMetadataType.PROCESS.typeName,
                                                        processElement.getProcessProperties(),
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        effectiveTime,
                                                        methodName);

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
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ProcessElement>   getProcessesByName(String  userId,
                                                     String  assetManagerGUID,
                                                     String  assetManagerName,
                                                     String  name,
                                                     String  nameParameterName,
                                                     int     startFrom,
                                                     int     pageSize,
                                                     boolean forLineage,
                                                     boolean forDuplicateProcessing,
                                                     Date    effectiveTime,
                                                     String  methodName) throws InvalidParameterException,
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
                                                                         forLineage,
                                                                         forDuplicateProcessing,
                                                                         effectiveTime,
                                                                         methodName);

        addCorrelationPropertiesToProcesses(userId,
                                            assetManagerGUID,
                                            assetManagerName,
                                            results,
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveTime, methodName);

        return results;
    }


    /**
     * Retrieve the process metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param processGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ProcessElement getProcessByGUID(String  userId,
                                           String  assetManagerGUID,
                                           String  assetManagerName,
                                           String  processGUID,
                                           boolean forLineage,
                                           boolean forDuplicateProcessing,
                                           Date    effectiveTime,
                                           String  methodName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, processGUIDParameterName, methodName);

        ProcessElement element = processHandler.getProcessByGUID(userId,
                                                                 processGUID,
                                                                 processGUIDParameterName,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 effectiveTime,
                                                                 methodName);

        if (element != null)
        {
            element.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                        element.getElementHeader().getGUID(),
                                                                        processGUIDParameterName,
                                                                        OpenMetadataType.PROCESS.typeName,
                                                                        assetManagerGUID,
                                                                        assetManagerName,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName));

            this.getSupplementaryProperties(element.getElementHeader().getGUID(),
                                            processGUIDParameterName,
                                            OpenMetadataType.PROCESS.typeName,
                                            element.getProcessProperties(),
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveTime,
                                            methodName);
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
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return parent process element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ProcessElement getProcessParent(String  userId,
                                           String  assetManagerGUID,
                                           String  assetManagerName,
                                           String  processGUID,
                                           boolean forLineage,
                                           boolean forDuplicateProcessing,
                                           Date    effectiveTime,
                                           String  methodName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, processGUIDParameterName, methodName);

        ProcessElement element = processHandler.getProcessParent(userId,
                                                                 processGUID,
                                                                 processGUIDParameterName,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 effectiveTime,
                                                                 methodName);

        if (element != null)
        {
            element.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                        element.getElementHeader().getGUID(),
                                                                        processGUIDParameterName,
                                                                        OpenMetadataType.PROCESS.typeName,
                                                                        assetManagerGUID,
                                                                        assetManagerName,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName));

            this.getSupplementaryProperties(element.getElementHeader().getGUID(),
                                            processGUIDParameterName,
                                            OpenMetadataType.PROCESS.typeName,
                                            element.getProcessProperties(),
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveTime,
                                            methodName);
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
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of process element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ProcessElement> getSubProcesses(String userId,
                                                String  assetManagerGUID,
                                                String  assetManagerName,
                                                String  processGUID,
                                                int     startFrom,
                                                int     pageSize,
                                                boolean forLineage,
                                                boolean forDuplicateProcessing,
                                                Date    effectiveTime,
                                                String  methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        List<ProcessElement> results = processHandler.getSubProcesses(userId,
                                                                      processGUID,
                                                                      processGUIDParameterName,
                                                                      startFrom,
                                                                      pageSize,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      effectiveTime,
                                                                      methodName);

        addCorrelationPropertiesToProcesses(userId,
                                            assetManagerGUID,
                                            assetManagerName,
                                            results,
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveTime, methodName);

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
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
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
                             boolean                       forLineage,
                             boolean                       forDuplicateProcessing,
                             Date                          effectiveTime,
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

        String typeName = OpenMetadataType.PORT.typeName;

        if (portProperties.getTypeName() != null)
        {
            typeName = portProperties.getTypeName();
        }

        int portType = PortType.OTHER.getOrdinal();

        if (portProperties.getPortType() != null)
        {
            portType = portProperties.getPortType().getOrdinal();
        }

        String portGUID = processHandler.createPort(userId,
                                                    this.getExternalSourceGUID(correlationProperties, assetManagerIsHome),
                                                    this.getExternalSourceName(correlationProperties, assetManagerIsHome),
                                                    processGUID,
                                                    processGUIDParameterName,
                                                    portProperties.getQualifiedName(),
                                                    portProperties.getDisplayName(),
                                                    portType,
                                                    portProperties.getAdditionalProperties(),
                                                    typeName,
                                                    portProperties.getExtendedProperties(),
                                                    forLineage,
                                                    forDuplicateProcessing,
                                                    effectiveTime,
                                                    methodName);

        if (portGUID != null)
        {
            this.createExternalIdentifier(userId,
                                          portGUID,
                                          portGUIDParameterName,
                                          OpenMetadataType.PORT.typeName,
                                          correlationProperties,
                                          forLineage,
                                          forDuplicateProcessing,
                                          effectiveTime,
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
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
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
                           boolean                       forLineage,
                           boolean                       forDuplicateProcessing,
                           Date                          effectiveTime,
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

        String typeName = OpenMetadataType.PORT.typeName;

        if (portProperties.getTypeName() != null)
        {
            typeName = portProperties.getTypeName();
        }

        this.validateExternalIdentifier(userId,
                                        portGUID,
                                        portGUIDParameterName,
                                        typeName,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        int portType = PortType.OTHER.getOrdinal();

        if (portProperties.getPortType() != null)
        {
            portType = portProperties.getPortType().getOrdinal();
        }

        processHandler.updatePort(userId,
                                  this.getExternalSourceGUID(correlationProperties),
                                  this.getExternalSourceName(correlationProperties),
                                  portGUID,
                                  portGUIDParameterName,
                                  portProperties.getQualifiedName(),
                                  portProperties.getDisplayName(),
                                  portType,
                                  portProperties.getAdditionalProperties(),
                                  typeName,
                                  portProperties.getExtendedProperties(),
                                  portProperties.getEffectiveFrom(),
                                  portProperties.getEffectiveTo(),
                                  forLineage,
                                  forDuplicateProcessing,
                                  effectiveTime,
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
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
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
                                 Date    effectiveFrom,
                                 Date    effectiveTo,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing,
                                 Date    effectiveTime,
                                 String  methodName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, processGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(portGUID, portGUIDParameterName, methodName);

        processHandler.setupProcessPort(userId,
                                        this.getExternalSourceGUID(assetManagerGUID, assetManagerIsHome),
                                        this.getExternalSourceName(assetManagerName, assetManagerIsHome),
                                        processGUID,
                                        processGUIDParameterName,
                                        portGUID,
                                        portGUIDParameterName,
                                        effectiveFrom,
                                        effectiveTo,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
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
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearProcessPort(String  userId,
                                 String  assetManagerGUID,
                                 String  assetManagerName,
                                 String  processGUID,
                                 String  portGUID,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing,
                                 Date    effectiveTime,
                                 String  methodName) throws InvalidParameterException,
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
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
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
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
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
                                    Date    effectiveFrom,
                                    Date    effectiveTo,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing,
                                    Date    effectiveTime,
                                    String  methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String portOneGUIDParameterName = "portOneGUID";
        final String portTwoGUIDParameterName = "portTwoGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(portOneGUID, portOneGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(portTwoGUID, portTwoGUIDParameterName, methodName);

        processHandler.setupPortDelegation(userId,
                                           this.getExternalSourceGUID(assetManagerGUID, assetManagerIsHome),
                                           this.getExternalSourceName(assetManagerName, assetManagerIsHome),
                                           portOneGUID,
                                           portOneGUIDParameterName,
                                           portTwoGUID,
                                           portTwoGUIDParameterName,
                                           effectiveFrom,
                                           effectiveTo,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
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
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearPortDelegation(String  userId,
                                    String  assetManagerGUID,
                                    String  assetManagerName,
                                    String  portOneGUID,
                                    String  portTwoGUID,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing,
                                    Date    effectiveTime,
                                    String  methodName) throws InvalidParameterException,
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
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
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
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
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
                                    Date    effectiveFrom,
                                    Date    effectiveTo,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing,
                                    Date    effectiveTime,
                                    String  methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String portGUIDParameterName       = "portGUID";
        final String schemaTypeGUIDParameterName = "schemaTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(portGUID, portGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, schemaTypeGUIDParameterName, methodName);

        processHandler.setupPortSchemaType(userId,
                                           this.getExternalSourceGUID(assetManagerGUID, assetManagerIsHome),
                                           this.getExternalSourceName(assetManagerName, assetManagerIsHome),
                                           portGUID,
                                           portGUIDParameterName,
                                           schemaTypeGUID,
                                           schemaTypeGUIDParameterName,
                                           effectiveFrom,
                                           effectiveTo,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
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
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearPortSchemaType(String  userId,
                                    String  assetManagerGUID,
                                    String  assetManagerName,
                                    String  portGUID,
                                    String  schemaTypeGUID,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing,
                                    Date    effectiveTime,
                                    String  methodName) throws InvalidParameterException,
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
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Remove the metadata element representing a port.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param portGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removePort(String                        userId,
                           MetadataCorrelationProperties correlationProperties,
                           String                        portGUID,
                           boolean                       forLineage,
                           boolean                       forDuplicateProcessing,
                           Date                          effectiveTime,
                           String                        methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(portGUID, portGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        portGUID,
                                        portGUIDParameterName,
                                        OpenMetadataType.PORT.typeName,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        processHandler.removePort(userId,
                                  this.getExternalSourceGUID(correlationProperties),
                                  this.getExternalSourceName(correlationProperties),
                                  portGUID,
                                  portGUIDParameterName,
                                  forLineage,
                                  forDuplicateProcessing,
                                  effectiveTime,
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
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<PortElement>   findPorts(String  userId,
                                         String  assetManagerGUID,
                                         String  assetManagerName,
                                         String  searchString,
                                         String  searchStringParameterName,
                                         int     startFrom,
                                         int     pageSize,
                                         boolean forLineage,
                                         boolean forDuplicateProcessing,
                                         Date    effectiveTime,
                                         String  methodName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        List<PortElement> results = processHandler.findPorts(userId,
                                                             searchString,
                                                             searchStringParameterName,
                                                             startFrom,
                                                             pageSize,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime,
                                                             methodName);

        addCorrelationPropertiesToPorts(userId,
                                        assetManagerGUID,
                                        assetManagerName,
                                        results,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime, methodName);

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
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<PortElement>    getPortsForProcess(String  userId,
                                                   String  assetManagerGUID,
                                                   String  assetManagerName,
                                                   String  processGUID,
                                                   int     startFrom,
                                                   int     pageSize,
                                                   boolean forLineage,
                                                   boolean forDuplicateProcessing,
                                                   Date    effectiveTime,
                                                   String  methodName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        List<PortElement> results = processHandler.getPortsForProcess(userId,
                                                                      processGUID,
                                                                      processGUIDParameterName,
                                                                      startFrom,
                                                                      pageSize,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      effectiveTime,
                                                                      methodName);

        addCorrelationPropertiesToPorts(userId,
                                        assetManagerGUID,
                                        assetManagerName,
                                        results,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime, methodName);

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
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<PortElement>  getPortUse(String  userId,
                                         String  assetManagerGUID,
                                         String  assetManagerName,
                                         String  portGUID,
                                         int     startFrom,
                                         int     pageSize,
                                         boolean forLineage,
                                         boolean forDuplicateProcessing,
                                         Date    effectiveTime,
                                         String  methodName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        List<PortElement> results = processHandler.getPortUse(userId,
                                                              portGUID,
                                                              portGUIDParameterName,
                                                              startFrom,
                                                              pageSize,
                                                              forLineage,
                                                              forDuplicateProcessing,
                                                              effectiveTime,
                                                              methodName);

        addCorrelationPropertiesToPorts(userId,
                                        assetManagerGUID,
                                        assetManagerName,
                                        results,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        return results;
    }


    /**
     * Retrieve the port that this port delegates to.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param portGUID unique identifier of the starting port alias
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public PortElement getPortDelegation(String  userId,
                                         String  assetManagerGUID,
                                         String  assetManagerName,
                                         String  portGUID,
                                         boolean forLineage,
                                         boolean forDuplicateProcessing,
                                         Date    effectiveTime,
                                         String  methodName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        PortElement element = processHandler.getPortDelegation(userId,
                                                               portGUID,
                                                               portGUIDParameterName,
                                                               forLineage,
                                                               forDuplicateProcessing,
                                                               effectiveTime,
                                                               methodName);

        if ((element != null) && (element.getElementHeader() != null) && (element.getElementHeader().getGUID() != null))
        {
            element.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                        element.getElementHeader().getGUID(),
                                                                        portGUIDParameterName,
                                                                        OpenMetadataType.PORT.typeName,
                                                                        assetManagerGUID,
                                                                        assetManagerName,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
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
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<PortElement>   getPortsByName(String  userId,
                                              String  assetManagerGUID,
                                              String  assetManagerName,
                                              String  name,
                                              String  nameParameterName,
                                              int     startFrom,
                                              int     pageSize,
                                              boolean forLineage,
                                              boolean forDuplicateProcessing,
                                              Date    effectiveTime,
                                              String  methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        List<PortElement> results = processHandler.getPortsByName(userId,
                                                                  name,
                                                                  nameParameterName,
                                                                  startFrom,
                                                                  pageSize,
                                                                  forLineage,
                                                                  forDuplicateProcessing,
                                                                  effectiveTime,
                                                                  methodName);

        addCorrelationPropertiesToPorts(userId,
                                        assetManagerGUID,
                                        assetManagerName,
                                        results,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        return results;
    }


    /**
     * Retrieve the port metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param portGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public PortElement getPortByGUID(String  userId,
                                     String  assetManagerGUID,
                                     String  assetManagerName,
                                     String  portGUID,
                                     boolean forLineage,
                                     boolean forDuplicateProcessing,
                                     Date    effectiveTime,
                                     String  methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        PortElement element = processHandler.getPortByGUID(userId,
                                                           portGUID,
                                                           portGUIDParameterName,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           effectiveTime,
                                                           methodName);

        if ((element != null) && (element.getElementHeader() != null) && (element.getElementHeader().getGUID() != null))
        {
            element.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                        element.getElementHeader().getGUID(),
                                                                        portGUIDParameterName,
                                                                        OpenMetadataType.PORT.typeName,
                                                                        assetManagerGUID,
                                                                        assetManagerName,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName));
        }

        return element;
    }


    /* ===============================================================================
     * General linkage and classifications
     */


    /**
     * Classify a port, process or process as "BusinessSignificant" (this may affect the way that lineage is displayed).
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param elementGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setBusinessSignificant(String                        userId,
                                       MetadataCorrelationProperties correlationProperties,
                                       String                        elementGUID,
                                       boolean                       forLineage,
                                       boolean                       forDuplicateProcessing,
                                       Date                          effectiveTime,
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
                                        OpenMetadataType.REFERENCEABLE.typeName,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        processHandler.setBusinessSignificant(userId,
                                              elementGUID,
                                              elementGUIDParameterName,
                                              OpenMetadataType.REFERENCEABLE.typeName,
                                              null,
                                              null,
                                              null,
                                              forLineage,
                                              forDuplicateProcessing,
                                              effectiveTime,
                                              methodName);
    }


    /**
     * Remove the "BusinessSignificant" designation from the element.
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param elementGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearBusinessSignificant(String                        userId,
                                         MetadataCorrelationProperties correlationProperties,
                                         String                        elementGUID,
                                         boolean                       forLineage,
                                         boolean                       forDuplicateProcessing,
                                         Date                          effectiveTime,
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
                                        OpenMetadataType.REFERENCEABLE.typeName,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        processHandler.clearBusinessSignificant(userId,
                                                elementGUID,
                                                elementGUIDParameterName,
                                                OpenMetadataType.REFERENCEABLE.typeName,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
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
     * @param qualifiedName unique identifier for the information supply chain
     * @param label label for the visualization of the relationship
     * @param description description and/or purpose of the data flow
     * @param formula function that determines the subset of the data that flows
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
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
                                String  label,
                                String  description,
                                String  formula,
                                Date    effectiveFrom,
                                Date    effectiveTo,
                                boolean forLineage,
                                boolean forDuplicateProcessing,
                                Date    effectiveTime,
                                String  methodName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        final String dataSupplierGUIDParameterName = "dataSupplierGUID";
        final String dataConsumerGUIDParameterName = "dataConsumerGUID";

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
                                            dataConsumerGUIDParameterName,
                                            effectiveFrom,
                                            effectiveTo,
                                            qualifiedName,
                                            label,
                                            description,
                                            formula,
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveTime,
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
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DataFlowElement getDataFlow(String  userId,
                                       String  dataSupplierGUID,
                                       String  dataConsumerGUID,
                                       String  qualifiedName,
                                       boolean forLineage,
                                       boolean forDuplicateProcessing,
                                       Date    effectiveTime,
                                       String  methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String dataSupplierGUIDParameterName = "dataSupplierGUID";
        final String dataConsumerGUIDParameterName = "dataConsumerGUID";

        return processHandler.getDataFlow(userId,
                                          dataSupplierGUID,
                                          dataSupplierGUIDParameterName,
                                          dataConsumerGUID,
                                          dataConsumerGUIDParameterName,
                                          qualifiedName,
                                          forLineage,
                                          forDuplicateProcessing,
                                          effectiveTime,
                                          methodName);
    }


    /**
     * Update relationship between two elements that shows that data flows from one to the other.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param dataFlowGUID unique identifier of the data flow relationship
     * @param qualifiedName unique identifier for the information supply chain
     * @param label label for the visualization of the relationship
     * @param description description and/or purpose of the data flow
     * @param formula function that determines the subset of the data that flows
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void   updateDataFlow(String  userId,
                                 String  assetManagerGUID,
                                 String  assetManagerName,
                                 String  dataFlowGUID,
                                 String  qualifiedName,
                                 String  label,
                                 String  description,
                                 String  formula,
                                 Date    effectiveFrom,
                                 Date    effectiveTo,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing,
                                 Date    effectiveTime,
                                 String  methodName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        final String dataFlowGUIDParameterName = "dataFlowGUID";

        processHandler.updateDataFlow(userId,
                                      assetManagerGUID,
                                      assetManagerName,
                                      dataFlowGUID,
                                      dataFlowGUIDParameterName,
                                      effectiveFrom,
                                      effectiveTo,
                                      qualifiedName,
                                      label,
                                      description,
                                      formula,
                                      forLineage,
                                      forDuplicateProcessing,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Remove the data flow relationship between two elements.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param dataFlowGUID unique identifier of the data flow relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearDataFlow(String  userId,
                              String  assetManagerGUID,
                              String  assetManagerName,
                              String  dataFlowGUID,
                              boolean forLineage,
                              boolean forDuplicateProcessing,
                              Date    effectiveTime,
                              String  methodName) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        final String dataFlowGUIDParameterName = "dataFlowGUID";

        processHandler.clearDataFlow(userId,
                                     assetManagerGUID,
                                     assetManagerName,
                                     dataFlowGUID,
                                     dataFlowGUIDParameterName,
                                     forLineage,
                                     forDuplicateProcessing,
                                     effectiveTime,
                                     methodName);
    }


    /**
     * Retrieve the data flow relationships linked from a specific element to the downstream consumers.
     *
     * @param userId calling user
     * @param dataSupplierGUID unique identifier of the data supplier
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DataFlowElement> getDataFlowConsumers(String  userId,
                                                      String  dataSupplierGUID,
                                                      int     startFrom,
                                                      int     pageSize,
                                                      boolean forLineage,
                                                      boolean forDuplicateProcessing,
                                                      Date    effectiveTime,
                                                      String  methodName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String dataSupplierGUIDParameterName = "dataSupplierGUID";

        return processHandler.getDataFlowConsumers(userId,
                                                   dataSupplierGUID,
                                                   dataSupplierGUIDParameterName,
                                                   startFrom,
                                                   pageSize,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   effectiveTime,
                                                   methodName);
    }


    /**
     * Retrieve the data flow relationships linked from a specific element to the upstream suppliers.
     *
     * @param userId calling user
     * @param dataConsumerGUID unique identifier of the data consumer
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DataFlowElement> getDataFlowSuppliers(String  userId,
                                                      String  dataConsumerGUID,
                                                      int     startFrom,
                                                      int     pageSize,
                                                      boolean forLineage,
                                                      boolean forDuplicateProcessing,
                                                      Date    effectiveTime,
                                                      String  methodName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String dataConsumerGUIDParameterName = "dataConsumerGUID";

        return processHandler.getDataFlowSuppliers(userId,
                                                   dataConsumerGUID,
                                                   dataConsumerGUIDParameterName,
                                                   startFrom,
                                                   pageSize,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   effectiveTime,
                                                   methodName);
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
     * @param qualifiedName unique identifier for the information supply chain
     * @param label label for the visualization of the relationship
     * @param description description and/or purpose of the control flow
     * @param guard function that must be true to travel down this control flow
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
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
                                   String  label,
                                   String  description,
                                   String  guard,
                                   Date    effectiveFrom,
                                   Date    effectiveTo,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing,
                                   Date    effectiveTime,
                                   String  methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        final String currentStepGUIDParameterName = "currentStepGUID";
        final String nextStepGUIDParameterName    = "nextStepGUID";

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
                                               effectiveFrom,
                                               effectiveTo,
                                               qualifiedName,
                                               label,
                                               description,
                                               guard,
                                               forLineage,
                                               forDuplicateProcessing,
                                               effectiveTime,
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
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ControlFlowElement getControlFlow(String  userId,
                                             String  currentStepGUID,
                                             String  nextStepGUID,
                                             String  qualifiedName,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing,
                                             Date    effectiveTime,
                                             String  methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String currentStepGUIDParameterName = "currentStepGUID";
        final String nextStepGUIDParameterName    = "nextStepGUID";

        return processHandler.getControlFlow(userId,
                                             currentStepGUID,
                                             currentStepGUIDParameterName,
                                             nextStepGUID,
                                             nextStepGUIDParameterName,
                                             qualifiedName,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
    }


    /**
     * Update the relationship between two elements that shows that when one completes the next is started.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param controlFlowGUID unique identifier of the  control flow relationship
     * @param qualifiedName unique identifier for the information supply chain
     * @param label label for the visualization of the relationship
     * @param description description and/or purpose of the control flow
     * @param guard function that must be true to travel down this control flow
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateControlFlow(String  userId,
                                  String  assetManagerGUID,
                                  String  assetManagerName,
                                  String  controlFlowGUID,
                                  String  qualifiedName,
                                  String  label,
                                  String  description,
                                  String  guard,
                                  Date    effectiveFrom,
                                  Date    effectiveTo,
                                  boolean forLineage,
                                  boolean forDuplicateProcessing,
                                  Date    effectiveTime,
                                  String  methodName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        final String controlFlowGUIDParameterName = "controlFlowGUID";

        processHandler.updateControlFlow(userId,
                                         assetManagerGUID,
                                         assetManagerName,
                                         controlFlowGUID,
                                         controlFlowGUIDParameterName,
                                         effectiveFrom,
                                         effectiveTo,
                                         qualifiedName,
                                         label,
                                         description,
                                         guard,
                                         forLineage,
                                         forDuplicateProcessing,
                                         effectiveTime,
                                         methodName);
    }


    /**
     * Remove the control flow relationship between two elements.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param controlFlowGUID unique identifier of the  control flow relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearControlFlow(String  userId,
                                 String  assetManagerGUID,
                                 String  assetManagerName,
                                 String  controlFlowGUID,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing,
                                 Date    effectiveTime,
                                 String  methodName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        final String controlFlowGUIDParameterName = "controlFlowGUID";

        processHandler.clearControlFlow(userId,
                                        assetManagerGUID,
                                        assetManagerName,
                                        controlFlowGUID,
                                        controlFlowGUIDParameterName,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Retrieve the control relationships linked from a specific element to the possible next elements in the process.
     *
     * @param userId calling user
     * @param currentStepGUID unique identifier of the current step
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ControlFlowElement> getControlFlowNextSteps(String  userId,
                                                            String  currentStepGUID,
                                                            int     startFrom,
                                                            int     pageSize,
                                                            boolean forLineage,
                                                            boolean forDuplicateProcessing,
                                                            Date    effectiveTime,
                                                            String  methodName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String currentStepGUIDParameterName = "currentStepGUID";

        return processHandler.getControlFlowNextSteps(userId,
                                                      currentStepGUID,
                                                      currentStepGUIDParameterName,
                                                      startFrom,
                                                      pageSize,
                                                      forLineage,
                                                      forDuplicateProcessing,
                                                      effectiveTime,
                                                      methodName);
    }


    /**
     * Retrieve the control relationships linked from a specific element to the possible previous elements in the process.
     *
     * @param userId calling user
     * @param currentStepGUID unique identifier of the current step
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ControlFlowElement> getControlFlowPreviousSteps(String  userId,
                                                                String  currentStepGUID,
                                                                int     startFrom,
                                                                int     pageSize,
                                                                boolean forLineage,
                                                                boolean forDuplicateProcessing,
                                                                Date    effectiveTime,
                                                                String  methodName) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String currentStepGUIDParameterName = "currentStepGUID";

        return processHandler.getControlFlowPreviousSteps(userId,
                                                          currentStepGUID,
                                                          currentStepGUIDParameterName,
                                                          startFrom,
                                                          pageSize,
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          effectiveTime,
                                                          methodName);
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
     * @param qualifiedName unique identifier for the information supply chain
     * @param label label for the visualization of the relationship
     * @param description description and/or purpose of the process call
     * @param formula function that describes the function performed
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
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
                                   String  label,
                                   String  description,
                                   String  formula,
                                   Date    effectiveFrom,
                                   Date    effectiveTo,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing,
                                   Date    effectiveTime,
                                   String  methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        final String callerGUIDParameterName = "callerGUID";
        final String calledGUIDParameterName = "calledGUID";

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
                                               effectiveFrom,
                                               effectiveTo,
                                               qualifiedName,
                                               label,
                                               description,
                                               formula,
                                               forLineage,
                                               forDuplicateProcessing,
                                               effectiveTime,
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
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ProcessCallElement getProcessCall(String  userId,
                                             String  callerGUID,
                                             String  calledGUID,
                                             String  qualifiedName,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing,
                                             Date    effectiveTime,
                                             String  methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String callerGUIDParameterName    = "callerGUID";
        final String calledGUIDParameterName    = "calledGUID";

        return processHandler.getProcessCall(userId,
                                             callerGUID,
                                             callerGUIDParameterName,
                                             calledGUID,
                                             calledGUIDParameterName,
                                             qualifiedName,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
    }


    /**
     * Update the relationship between two elements that shows a request-response call between them.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param processCallGUID unique identifier of the process call relationship
     * @param qualifiedName unique identifier for the information supply chain
     * @param label label for the visualization of the relationship
     * @param description description and/or purpose of the data flow
     * @param formula function that describes the function performed
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateProcessCall(String  userId,
                                  String  assetManagerGUID,
                                  String  assetManagerName,
                                  String  processCallGUID,
                                  String  qualifiedName,
                                  String  label,
                                  String  description,
                                  String  formula,
                                  Date    effectiveFrom,
                                  Date    effectiveTo,
                                  boolean forLineage,
                                  boolean forDuplicateProcessing,
                                  Date    effectiveTime,
                                  String  methodName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        final String processCallGUIDParameterName = "processCallGUID";

        processHandler.updateProcessCall(userId,
                                         assetManagerGUID,
                                         assetManagerName,
                                         processCallGUID,
                                         processCallGUIDParameterName,
                                         effectiveFrom,
                                         effectiveTo,
                                         qualifiedName,
                                         label,
                                         description,
                                         formula,
                                         forLineage,
                                         forDuplicateProcessing,
                                         effectiveTime,
                                         methodName);
    }


    /**
     * Remove the process call relationship.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param processCallGUID unique identifier of the process call relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearProcessCall(String  userId,
                                 String  assetManagerGUID,
                                 String  assetManagerName,
                                 String  processCallGUID,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing,
                                 Date    effectiveTime,
                                 String  methodName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        final String processCallGUIDParameterName = "processCallGUID";

        processHandler.clearProcessCall(userId,
                                        assetManagerGUID,
                                        assetManagerName,
                                        processCallGUID,
                                        processCallGUIDParameterName,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Retrieve the process call relationships linked from a specific element to the elements it calls.
     *
     * @param userId calling user
     * @param callerGUID unique identifier of the element that is making the call
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ProcessCallElement> getProcessCalled(String  userId,
                                                     String  callerGUID,
                                                     int     startFrom,
                                                     int     pageSize,
                                                     boolean forLineage,
                                                     boolean forDuplicateProcessing,
                                                     Date    effectiveTime,
                                                     String  methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String callerGUIDParameterName = "callerGUID";

        return processHandler.getProcessCallers(userId,
                                                callerGUID,
                                                callerGUIDParameterName,
                                                startFrom,
                                                pageSize,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
    }


    /**
     * Retrieve the process call relationships linked from a specific element to its callers.
     *
     * @param userId calling user
     * @param calledGUID unique identifier of the element that is processing the call
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ProcessCallElement> getProcessCallers(String  userId,
                                                      String  calledGUID,
                                                      int     startFrom,
                                                      int     pageSize,
                                                      boolean forLineage,
                                                      boolean forDuplicateProcessing,
                                                      Date    effectiveTime,
                                                      String  methodName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String calledGUIDParameterName    = "calledGUID";

        return processHandler.getProcessCallers(userId,
                                                calledGUID,
                                                calledGUIDParameterName,
                                                startFrom,
                                                pageSize,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
    }


    /**
     * Link to elements together to show that they are part of the lineage of the data that is moving
     * between the processes.  Typically, the lineage relationships stitch together processes and data assets
     * supported by different technologies.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param sourceElementGUID unique identifier of the source
     * @param destinationElementGUID unique identifier of the destination
     * @param qualifiedName unique identifier for associated information supply chain
     * @param label label for the visualization of the relationship
     * @param description description and/or purpose of the mapping
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return unique identifier of the new relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String setupLineageMapping(String  userId,
                                      String  assetManagerGUID,
                                      String  assetManagerName,
                                      String  sourceElementGUID,
                                      String  destinationElementGUID,
                                      String  qualifiedName,
                                      String  label,
                                      String  description,
                                      Date    effectiveFrom,
                                      Date    effectiveTo,
                                      boolean forLineage,
                                      boolean forDuplicateProcessing,
                                      Date    effectiveTime,
                                      String  methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String sourceElementGUIDParameterName      = "sourceElementGUID";
        final String destinationElementGUIDParameterName = "destinationElementGUID";

        String guid = processHandler.setupLineageMapping(userId,
                                                         sourceElementGUID,
                                                         sourceElementGUIDParameterName,
                                                         destinationElementGUID,
                                                         destinationElementGUIDParameterName,
                                                         qualifiedName,
                                                         label,
                                                         description,
                                                         effectiveFrom,
                                                         effectiveTo,
                                                         forLineage,
                                                         forDuplicateProcessing,
                                                         effectiveTime,
                                                         methodName);

        externalIdentifierHandler.logRelationshipCreation(assetManagerGUID,
                                                          assetManagerName,
                                                          OpenMetadataType.LINEAGE_MAPPING_RELATIONSHIP.typeName,
                                                          sourceElementGUID,
                                                          OpenMetadataType.REFERENCEABLE.typeName,
                                                          destinationElementGUID,
                                                          OpenMetadataType.REFERENCEABLE.typeName,
                                                          methodName);

        return guid;
    }



    /**
     * Link to elements together to show that they are part of the lineage of the data that is moving
     * between the processes.  Typically, the lineage relationships stitch together processes and data assets
     * supported by different technologies.
     *
     * @param userId calling user
     * @param sourceElementGUID unique identifier of the source
     * @param destinationElementGUID unique identifier of the destination
     * @param qualifiedName unique identifier for this relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public LineageMappingElement getLineageMapping(String  userId,
                                                   String  sourceElementGUID,
                                                   String  destinationElementGUID,
                                                   String  qualifiedName,
                                                   boolean forLineage,
                                                   boolean forDuplicateProcessing,
                                                   Date    effectiveTime,
                                                   String  methodName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String sourceElementGUIDParameterName      = "sourceElementGUID";
        final String destinationElementGUIDParameterName = "destinationElementGUID";

        return processHandler.getLineageMapping(userId,
                                                sourceElementGUID,
                                                sourceElementGUIDParameterName,
                                                destinationElementGUID,
                                                destinationElementGUIDParameterName,
                                                qualifiedName,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
    }


    /**
     * Remove the lineage mapping between two elements.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param lineageMappingGUID unique identifier of the process call relationship
     * @param qualifiedName unique identifier for this relationship
     * @param label label for the visualization of the relationship
     * @param description description and/or purpose of the mapping
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public void updateLineageMapping(String  userId,
                                     String  assetManagerGUID,
                                     String  assetManagerName,
                                     String  lineageMappingGUID,
                                     String  qualifiedName,
                                     String  label,
                                     String  description,
                                     Date    effectiveFrom,
                                     Date    effectiveTo,
                                     boolean forLineage,
                                     boolean forDuplicateProcessing,
                                     Date    effectiveTime,
                                     String  methodName) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
       final String  lineageMappingGUIDParameterName = "lineageMappingGUID";

       processHandler.updateLineageMapping(userId,
                                           lineageMappingGUID,
                                           lineageMappingGUIDParameterName,
                                           qualifiedName,
                                           label,
                                           description,
                                           effectiveFrom,
                                           effectiveTo,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Remove the lineage mapping between two elements.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param lineageMappingGUID unique identifier of the lineage mapping relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public void clearLineageMapping(String  userId,
                                    String  assetManagerGUID,
                                    String  assetManagerName,
                                    String  lineageMappingGUID,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing,
                                    Date    effectiveTime,
                                    String  methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String  lineageMappingGUIDParameterName = "lineageMappingGUID";

        processHandler.clearLineageMapping(userId,
                                           lineageMappingGUID,
                                           lineageMappingGUIDParameterName,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Retrieve the lineage mapping relationships linked from a specific source element to its destinations.
     *
     * @param userId calling user
     * @param sourceElementGUID unique identifier of the source
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of mapping elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<LineageMappingElement> getDestinationLineageMappings(String  userId,
                                                                     String  sourceElementGUID,
                                                                     int     startFrom,
                                                                     int     pageSize,
                                                                     boolean forLineage,
                                                                     boolean forDuplicateProcessing,
                                                                     Date    effectiveTime,
                                                                     String  methodName) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String sourceElementGUIDParameterName = "sourceElementGUID";

        return processHandler.getDestinationLineageMappings(userId,
                                                            sourceElementGUID,
                                                            sourceElementGUIDParameterName,
                                                            startFrom,
                                                            pageSize,
                                                            forLineage,
                                                            forDuplicateProcessing,
                                                            effectiveTime,
                                                            methodName);
    }


    /**
     * Retrieve the lineage mapping relationships linked from a specific destination element to its sources.
     *
     * @param userId calling user
     * @param destinationElementGUID unique identifier of the destination
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of mapping elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<LineageMappingElement> getSourceLineageMappings(String  userId,
                                                                String  destinationElementGUID,
                                                                int     startFrom,
                                                                int     pageSize,
                                                                boolean forLineage,
                                                                boolean forDuplicateProcessing,
                                                                Date    effectiveTime,
                                                                String  methodName) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String destinationElementGUIDParameterName = "destinationElementGUID";

        return processHandler.getSourceLineageMappings(userId,
                                                       destinationElementGUID,
                                                       destinationElementGUIDParameterName,
                                                       startFrom,
                                                       pageSize,
                                                       forLineage,
                                                       forDuplicateProcessing,
                                                       effectiveTime,
                                                       methodName);
    }
}
