/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ProcessHandler provides the methods to create and maintain processes and their contents.
 */
public class ProcessHandler<PROCESS, PORT, DATA_FLOW, CONTROL_FLOW, PROCESS_CALL, LINEAGE_MAPPING> extends ReferenceableHandler<PROCESS>
{
    private AssetHandler<PROCESS>                                          processHandler;
    private PortHandler<PORT>                                              portHandler;
    private OpenMetadataAPIGenericConverter<DATA_FLOW>                     dataFlowConverter;
    private Class<DATA_FLOW>                                               dataFlowBeanClass;
    private OpenMetadataAPIGenericConverter<CONTROL_FLOW>                  controlFlowConverter;
    private Class<CONTROL_FLOW>                                            controlFlowBeanClass;
    private OpenMetadataAPIGenericConverter<PROCESS_CALL>                  processCallConverter;
    private Class<PROCESS_CALL>                                            processCallBeanClass;
    private OpenMetadataAPIGenericConverter<LINEAGE_MAPPING>               validValueMappingConverter;
    private Class<LINEAGE_MAPPING>                                         validValueMappingClass;


    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param converter               specific converter for the PROCESS bean class
     * @param beanClass               name of bean class that is represented by the generic class PROCESS
     * @param portConverter           specific converter for the PORT bean class
     * @param portBeanClass           name of bean class that is represented by the generic class PORT
     * @param dataFlowConverter       specific converter for the DATA_FLOW bean class
     * @param dataFlowBeanClass       name of bean class that is represented by the generic class DATA_FLOW
     * @param controlFlowConverter    specific converter for the CONTROL_FLOW bean class
     * @param controlFlowBeanClass    name of bean class that is represented by the generic class CONTROL_FLOW
     * @param processCallConverter    specific converter for the PROCESS_CALL bean class
     * @param processCallBeanClass    name of bean class that is represented by the generic class PROCESS_CALL
     * @param lineageMappingConverter specific converter for the LINEAGE_MAPPING bean class
     * @param lineageMappingClass     name of bean class that is represented by the generic class LINEAGE_MAPPING
     * @param serviceName             name of this service
     * @param serverName              name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler       manages calls to the repository services
     * @param repositoryHelper        provides utilities for manipulating the repository services objects
     * @param localServerUserId       userId for this server
     * @param securityVerifier        open metadata security services verifier
     * @param supportedZones          list of zones that the access service is allowed to serve Asset instances from.
     * @param defaultZones            list of zones that the access service should set in all new Asset instances.
     * @param publishZones            list of zones that the access service sets up in published Asset instances.
     * @param auditLog destination for audit log events.
     */
    public ProcessHandler(OpenMetadataAPIGenericConverter<PROCESS>         converter,
                          Class<PROCESS>                                   beanClass,
                          OpenMetadataAPIGenericConverter<PORT>            portConverter,
                          Class<PORT>                                      portBeanClass,
                          OpenMetadataAPIGenericConverter<DATA_FLOW>       dataFlowConverter,
                          Class<DATA_FLOW>                                 dataFlowBeanClass,
                          OpenMetadataAPIGenericConverter<CONTROL_FLOW>    controlFlowConverter,
                          Class<CONTROL_FLOW>                              controlFlowBeanClass,
                          OpenMetadataAPIGenericConverter<PROCESS_CALL>    processCallConverter,
                          Class<PROCESS_CALL>                              processCallBeanClass,
                          OpenMetadataAPIGenericConverter<LINEAGE_MAPPING> lineageMappingConverter,
                          Class<LINEAGE_MAPPING>                           lineageMappingClass,
                          String                                           serviceName,
                          String                                           serverName,
                          InvalidParameterHandler                          invalidParameterHandler,
                          RepositoryHandler                                repositoryHandler,
                          OMRSRepositoryHelper                             repositoryHelper,
                          String                                           localServerUserId,
                          OpenMetadataServerSecurityVerifier               securityVerifier,
                          List<String>                                     supportedZones,
                          List<String>                                     defaultZones,
                          List<String>                                     publishZones,
                          AuditLog                                         auditLog)

    {
        super(converter,
              beanClass,
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

        this.processHandler = new AssetHandler<>(converter,
                                                 beanClass,
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

        this.portHandler = new PortHandler<>(portConverter,
                                             portBeanClass,
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

        this.dataFlowConverter          = dataFlowConverter;
        this.dataFlowBeanClass          = dataFlowBeanClass;
        this.controlFlowConverter       = controlFlowConverter;
        this.controlFlowBeanClass       = controlFlowBeanClass;
        this.processCallConverter       = processCallConverter;
        this.processCallBeanClass       = processCallBeanClass;
        this.validValueMappingConverter = lineageMappingConverter;
        this.validValueMappingClass     = lineageMappingClass;
    }


    /* =====================================================================================================================
     * A process describes a well defined series of steps that gets something done.
     */

    /**
     * Create a new metadata element to represent a process.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param qualifiedName unique name for this asset
     * @param technicalName the stored display name property for the asset
     * @param technicalDescription the stored description property associated with the asset
     * @param formula the formula that characterize the processing behavior of the process
     * @param implementationLanguage the implementation language used to create the process
     * @param zoneMembership initial zones for the asset - or null to allow the security module to set it up
     * @param owner identifier of the owner
     * @param ownerType is the owner identifier a user id, personal profile or team profile
     * @param originOrganizationCapabilityGUID unique identifier of originating organization
     * @param originBusinessCapabilityGUID unique identifier of originating business capability
     * @param otherOriginValues the properties that characterize where this asset is from
     * @param additionalProperties any arbitrary properties not part of the type system
     * @param suppliedTypeName name of the type that is a subtype of asset - or null to create standard type
     * @param suppliedExtendedProperties properties from any subtype
     * @param initialStatus status value for the new process (default = ACTIVE)
     * @param methodName calling method
     *
     * @return unique identifier of the new process
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createProcess(String              userId,
                                String              externalSourceGUID,
                                String              externalSourceName,
                                String              qualifiedName,
                                String              technicalName,
                                String              technicalDescription,
                                String              formula,
                                String              implementationLanguage,
                                List<String>        zoneMembership,
                                String              owner,
                                int                 ownerType,
                                String              originOrganizationCapabilityGUID,
                                String              originBusinessCapabilityGUID,
                                Map<String, String> otherOriginValues,
                                Map<String, String> additionalProperties,
                                String              suppliedTypeName,
                                Map<String, Object> suppliedExtendedProperties,
                                InstanceStatus      initialStatus,
                                String              methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        String typeName = OpenMetadataAPIMapper.PROCESS_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        Map<String, Object> extendedProperties = suppliedExtendedProperties;

        if (formula != null)
        {
            if (extendedProperties == null)
            {
                extendedProperties = new HashMap<>();
            }

            extendedProperties.put(OpenMetadataAPIMapper.FORMULA_PROPERTY_NAME, formula);
        }

        if (implementationLanguage != null)
        {
            if (extendedProperties == null)
            {
                extendedProperties = new HashMap<>();
            }

            extendedProperties.put(OpenMetadataAPIMapper.IMPLEMENTATION_LANGUAGE_PROPERTY_NAME, implementationLanguage);
        }

        return processHandler.createAssetInRepository(userId,
                                                      externalSourceGUID,
                                                      externalSourceName,
                                                      qualifiedName,
                                                      technicalName,
                                                      technicalDescription,
                                                      zoneMembership,
                                                      owner,
                                                      ownerType,
                                                      originOrganizationCapabilityGUID,
                                                      originBusinessCapabilityGUID,
                                                      otherOriginValues,
                                                      additionalProperties,
                                                      typeGUID,
                                                      typeName,
                                                      extendedProperties,
                                                      initialStatus,
                                                      methodName);
    }


    /**
     * Create a new metadata element to represent a process using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateGUIDParameterName parameter supplying templateGUID
     * @param qualifiedName unique name for the term - used in other configuration
     * @param qualifiedNameParameterName parameter supplying qualifiedName
     * @param displayName short display name for the term
     * @param description description of the  term
     * @param methodName calling method
     *
     * @return unique identifier of the new process
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createProcessFromTemplate(String userId,
                                            String externalSourceGUID,
                                            String externalSourceName,
                                            String templateGUID,
                                            String templateGUIDParameterName,
                                            String qualifiedName,
                                            String qualifiedNameParameterName,
                                            String displayName,
                                            String description,
                                            String methodName) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        return processHandler.addAssetFromTemplate(userId,
                                                   externalSourceGUID,
                                                   externalSourceName,
                                                   templateGUID,
                                                   templateGUIDParameterName,
                                                   OpenMetadataAPIMapper.PROCESS_TYPE_GUID,
                                                   OpenMetadataAPIMapper.PROCESS_TYPE_NAME,
                                                   qualifiedName,
                                                   qualifiedNameParameterName,
                                                   displayName,
                                                   description,
                                                   null,
                                                   methodName);
    }


    /**
     * Update the metadata element representing a process.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param processGUID unique identifier of the metadata element to update
     * @param processGUIDParameterName unique identifier of the process in the external process manager
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param qualifiedName unique name for this asset
     * @param technicalName the stored display name property for the asset
     * @param technicalDescription the stored description property associated with the asset
     * @param formula the formula that characterize the processing behavior of the process
     * @param implementationLanguage the implementation language used to create the process
     * @param zoneMembership initial zones for the asset - or null to allow the security module to set it up
     * @param owner identifier of the owner
     * @param ownerType is the owner identifier a user id, personal profile or team profile
     * @param originOrganizationCapabilityGUID unique identifier of originating organization
     * @param originBusinessCapabilityGUID unique identifier of originating business capability
     * @param otherOriginValues the properties that characterize where this asset is from
     * @param additionalProperties any arbitrary properties not part of the type system
     * @param suppliedTypeName name of the type that is a subtype of asset - or null to create standard type
     * @param suppliedExtendedProperties properties from any subtype
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateProcess(String              userId,
                              String              externalSourceGUID,
                              String              externalSourceName,
                              String              processGUID,
                              String              processGUIDParameterName,
                              boolean             isMergeUpdate,
                              String              qualifiedName,
                              String              technicalName,
                              String              technicalDescription,
                              String              formula,
                              String              implementationLanguage,
                              List<String>        zoneMembership,
                              String              owner,
                              int                 ownerType,
                              String              originOrganizationCapabilityGUID,
                              String              originBusinessCapabilityGUID,
                              Map<String, String> otherOriginValues,
                              Map<String, String> additionalProperties,
                              String              suppliedTypeName,
                              Map<String, Object> suppliedExtendedProperties,
                              String              methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {

    }


    /**
     * Update the status of the metadata element representing a process.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param processGUID unique identifier of the process to update
     * @param processGUIDParameterName unique identifier of the process in the external process manager
     * @param processStatus new status for the process
     * @param processStatusParameterName parameter supplying processStatus
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateProcessStatus(String         userId,
                                    String         externalSourceGUID,
                                    String         externalSourceName,
                                    String         processGUID,
                                    String         processGUIDParameterName,
                                    InstanceStatus processStatus,
                                    String         processStatusParameterName,
                                    String         methodName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        this.updateBeanStatusInRepository(userId,
                                          externalSourceGUID,
                                          externalSourceName,
                                          processGUID,
                                          processGUIDParameterName,
                                          OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_GUID,
                                          OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_NAME,
                                          supportedZones,
                                          processStatus,
                                          processStatusParameterName,
                                          methodName);
    }


    /**
     * Create a parent-child relationship between two processes.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param parentProcessGUID unique identifier of the process in the external process manager that is to be the parent process
     * @param parentProcessGUIDParameterName parameter supplying parentProcessGUID
     * @param childProcessGUID unique identifier of the process in the external process manager that is to be the nested sub-process
     * @param childProcessGUIDParameterName parameter supplying childProcessGUID
     * @param containmentType describes the ownership of the sub-process
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupProcessParent(String userId,
                                   String externalSourceGUID,
                                   String externalSourceName,
                                   String parentProcessGUID,
                                   String parentProcessGUIDParameterName,
                                   String childProcessGUID,
                                   String childProcessGUIDParameterName,
                                   int    containmentType,
                                   String methodName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {

    }


    /**
     * Remove a parent-child relationship between two processes.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param parentProcessGUID unique identifier of the process in the external process manager that is to be the parent process
     * @param parentProcessGUIDParameterName parameter supplying parentProcessGUID
     * @param childProcessGUID unique identifier of the process in the external process manager that is to be the nested sub-process
     * @param childProcessGUIDParameterName parameter supplying childProcessGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearProcessParent(String userId,
                                   String externalSourceGUID,
                                   String externalSourceName,
                                   String parentProcessGUID,
                                   String parentProcessGUIDParameterName,
                                   String childProcessGUID,
                                   String childProcessGUIDParameterName,
                                   String methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {

    }


    /**
     * Update the zones for the process so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Asset Manager OMAS).
     *
     * @param userId calling user
     * @param processGUID unique identifier of the metadata element to publish
     * @param processGUIDParameterName parameter supplying processGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void publishProcess(String userId,
                               String processGUID,
                               String processGUIDParameterName,
                               String methodName) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {
        processHandler.publishAsset(userId, processGUID, processGUIDParameterName, methodName);
    }


    /**
     * Update the zones for the process so that it is no longer visible to consumers.
     *
     * @param userId calling user
     * @param processGUID unique identifier of the metadata element to withdraw
     * @param processGUIDParameterName parameter supplying processGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void withdrawProcess(String userId,
                                String processGUID,
                                String processGUIDParameterName,
                                String methodName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        processHandler.publishAsset(userId, processGUID, processGUIDParameterName, methodName);
    }


    /**
     * Remove the metadata element representing a process.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param processGUID unique identifier of the metadata element to remove
     * @param processGUIDParameterName unique identifier of the process in the external process manager
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeProcess(String userId,
                              String externalSourceGUID,
                              String externalSourceName,
                              String processGUID,
                              String processGUIDParameterName,
                              String methodName) throws InvalidParameterException,
                                                        UserNotAuthorizedException,
                                                        PropertyServerException
    {
        processHandler.deleteBeanInRepository(userId,
                                              externalSourceGUID,
                                              externalSourceName,
                                              processGUID,
                                              processGUIDParameterName,
                                              OpenMetadataAPIMapper.PROCESS_TYPE_GUID,
                                              OpenMetadataAPIMapper.PROCESS_TYPE_NAME,
                                              null,
                                              null,
                                              methodName);
    }


    /**
     * Retrieve the list of process metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
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
    public List<PROCESS> findProcesses(String userId,
                                       String searchString,
                                       String searchStringParameterName,
                                       int    startFrom,
                                       int    pageSize,
                                       String methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<PROCESS> results = processHandler.findAssets(userId,
                                                          searchString,
                                                          searchStringParameterName,
                                                          startFrom,
                                                          validatedPageSize,
                                                          methodName);

        return results;
    }


    /**
     * Retrieve the list of process metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
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
    public List<PROCESS>   getProcessesByName(String userId,
                                              String name,
                                              String nameParameterName,
                                              int    startFrom,
                                              int    pageSize,
                                              String methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        return null;
    }


    /**
     * Retrieve the process metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param processGUID unique identifier of the requested metadata element
     * @param processGUIDParameterName parameter supplying processGUID
     * @param methodName calling method
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public PROCESS getProcessByGUID(String userId,
                                    String processGUID,
                                    String processGUIDParameterName,
                                    String methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        return null;
    }


    /**
     * Retrieve the process metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param processGUID unique identifier of the requested metadata element
     * @param processGUIDParameterName parameter supplying processGUID
     * @param methodName calling method
     *
     * @return parent process element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public PROCESS getProcessParent(String userId,
                                    String processGUID,
                                    String processGUIDParameterName,
                                    String methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        return null;
    }


    /**
     * Retrieve the process metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param processGUID unique identifier of the requested metadata element
     * @param processGUIDParameterName parameter supplying processGUID
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
    public List<PROCESS> getSubProcesses(String userId,
                                         String processGUID,
                                         String processGUIDParameterName,
                                         int    startFrom,
                                         int    pageSize,
                                         String methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        return null;
    }


    /* ===============================================================================
     * A process typically contains ports that show the flow of data and control to and from it.
     */


    /**
     * Create a new metadata element to represent a port.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param processGUID unique identifier of the process where the port is located
     * @param processGUIDParameterName parameter supplying processGUID
     * @param qualifiedName unique name for the port - used in other configuration
     * @param displayName short display name for the port
     * @param portType description of the port
     * @param additionalProperties additional properties for a port
     * @param suppliedTypeName type name from the caller (enables creation of subtypes)
     * @param extendedProperties  properties for a port subtype
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element for the port
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createPort(String              userId,
                             String              externalSourceGUID,
                             String              externalSourceName,
                             String              processGUID,
                             String              processGUIDParameterName,
                             String              qualifiedName,
                             String              displayName,
                             int                 portType,
                             Map<String, String> additionalProperties,
                             String              suppliedTypeName,
                             Map<String, Object> extendedProperties,
                             String              methodName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        return portHandler.createPort(userId,
                                      externalSourceGUID,
                                      externalSourceName,
                                      processGUID,
                                      processGUIDParameterName,
                                      qualifiedName,
                                      displayName,
                                      portType,
                                      additionalProperties,
                                      suppliedTypeName,
                                      extendedProperties,
                                      methodName);
    }


    /**
     * Update the properties of the metadata element representing a port.  This call replaces
     * all existing properties with the supplied properties.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param portGUID unique identifier of the port to update
     * @param portGUIDParameterName parameter supplying portGUID
     * @param qualifiedName unique name for the port - used in other configuration
     * @param displayName short display name for the port
     * @param portType description of the port
     * @param additionalProperties additional properties for a port
     * @param suppliedTypeName type name from the caller (enables creation of subtypes)
     * @param extendedProperties  properties for a port subtype
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updatePort(String              userId,
                           String              externalSourceGUID,
                           String              externalSourceName,
                           String              portGUID,
                           String              portGUIDParameterName,
                           String              qualifiedName,
                           String              displayName,
                           int                 portType,
                           Map<String, String> additionalProperties,
                           String              suppliedTypeName,
                           Map<String, Object> extendedProperties,
                           String              methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        portHandler.updatePort(userId,
                               externalSourceGUID,
                               externalSourceName,
                               portGUID,
                               portGUIDParameterName,
                               qualifiedName,
                               displayName,
                               portType,
                               additionalProperties,
                               suppliedTypeName,
                               extendedProperties,
                               methodName);
    }


    /**
     * Link a port to a process.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param processGUID unique identifier of the process
     * @param processGUIDParameterName parameter supplying processGUID
     * @param portGUID unique identifier of the port
     * @param portGUIDParameterName parameter supplying portGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupProcessPort(String userId,
                                 String externalSourceGUID,
                                 String externalSourceName,
                                 String processGUID,
                                 String processGUIDParameterName,
                                 String portGUID,
                                 String portGUIDParameterName,
                                 String methodName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        portHandler.setupProcessPort(userId,
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
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param processGUID unique identifier of the process
     * @param processGUIDParameterName parameter supplying processGUID
     * @param portGUID unique identifier of the port
     * @param portGUIDParameterName parameter supplying portGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearProcessPort(String userId,
                                 String externalSourceGUID,
                                 String externalSourceName,
                                 String processGUID,
                                 String processGUIDParameterName,
                                 String portGUID,
                                 String portGUIDParameterName,
                                 String methodName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        portHandler.clearProcessPort(userId,
                                     externalSourceGUID,
                                     externalSourceName,
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
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param portOneGUID unique identifier of the port at end 1
     * @param portOneGUIDParameterName parameter supplying portOneGUID
     * @param portTwoGUID unique identifier of the port at end 2
     * @param portTwoGUIDParameterName parameter supplying portTwoGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupPortDelegation(String userId,
                                    String externalSourceGUID,
                                    String externalSourceName,
                                    String portOneGUID,
                                    String portOneGUIDParameterName,
                                    String portTwoGUID,
                                    String portTwoGUIDParameterName,
                                    String methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        portHandler.setupPortDelegation(userId,
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
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param portOneGUID unique identifier of the port at end 1
     * @param portOneGUIDParameterName parameter supplying portOneGUID
     * @param portTwoGUID unique identifier of the port at end 2
     * @param portTwoGUIDParameterName parameter supplying portTwoGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearPortDelegation(String userId,
                                    String externalSourceGUID,
                                    String externalSourceName,
                                    String portOneGUID,
                                    String portOneGUIDParameterName,
                                    String portTwoGUID,
                                    String portTwoGUIDParameterName,
                                    String methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        portHandler.clearPortDelegation(userId,
                                        externalSourceGUID,
                                        externalSourceName,
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
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param portGUID unique identifier of the port
     * @param portGUIDParameterName parameter supplying portGUID
     * @param schemaTypeGUID unique identifier of the schemaType
     * @param schemaTypeGUIDParameterName parameter supplying schemaTypeGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupPortSchemaType(String userId,
                                    String externalSourceGUID,
                                    String externalSourceName,
                                    String portGUID,
                                    String portGUIDParameterName,
                                    String schemaTypeGUID,
                                    String schemaTypeGUIDParameterName,
                                    String methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        portHandler.setupPortSchemaType(userId,
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
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param portGUID unique identifier of the port
     * @param portGUIDParameterName parameter supplying portGUID
     * @param schemaTypeGUID unique identifier of the schemaType
     * @param schemaTypeGUIDParameterName parameter supplying schemaTypeGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearPortSchemaType(String userId,
                                    String externalSourceGUID,
                                    String externalSourceName,
                                    String portGUID,
                                    String portGUIDParameterName,
                                    String schemaTypeGUID,
                                    String schemaTypeGUIDParameterName,
                                    String methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        portHandler.clearPortSchemaType(userId,
                                        externalSourceGUID,
                                        externalSourceName,
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
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param portGUID unique identifier of the metadata element to remove
     * @param portGUIDParameterName parameter supplying portGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removePort(String userId,
                           String externalSourceGUID,
                           String externalSourceName,
                           String portGUID,
                           String portGUIDParameterName,
                           String methodName) throws InvalidParameterException,
                                                     UserNotAuthorizedException,
                                                     PropertyServerException
    {
        portHandler.removePort(userId, externalSourceGUID, externalSourceName, portGUID, portGUIDParameterName, methodName);
    }


    /**
     * Retrieve the list of port metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
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
    public List<PORT> findPorts(String userId,
                                String searchString,
                                String searchStringParameterName,
                                int    startFrom,
                                int    pageSize,
                                String methodName) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {
        return portHandler.findPorts(userId, searchString, searchStringParameterName, startFrom, pageSize, methodName);
    }


    /**
     * Retrieve the list of ports associated with a process.
     *
     * @param userId calling user
     * @param processGUID unique identifier of the process of interest
     * @param processGUIDParameterName parameter passing processGUID
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
    public List<PORT> getPortsForProcess(String userId,
                                         String processGUID,
                                         String processGUIDParameterName,
                                         int    startFrom,
                                         int    pageSize,
                                         String methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        return portHandler.getPortsForProcess(userId, processGUID, processGUIDParameterName, startFrom, pageSize, methodName);
    }


    /**
     * Retrieve the list of ports that delegate to this port.
     *
     * @param userId calling user
     * @param portGUID unique identifier of the starting port
     * @param portGUIDParameterName parameter passing portGUID
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
    public List<PORT>  getPortUse(String userId,
                                  String portGUID,
                                  String portGUIDParameterName,
                                  int    startFrom,
                                  int    pageSize,
                                  String methodName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        return portHandler.getPortUse(userId, portGUID, portGUIDParameterName, startFrom, pageSize, methodName);
    }


    /**
     * Retrieve the port that this port delegates to.
     *
     * @param userId calling user
     * @param portGUID unique identifier of the starting port alias
     * @param portGUIDParameterName parameter passing portGUID
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public PORT getPortDelegation(String userId,
                                  String portGUID,
                                  String portGUIDParameterName,
                                  String methodName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        return portHandler.getPortDelegation(userId, portGUID, portGUIDParameterName, methodName);
    }


    /**
     * Retrieve the list of port metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
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
    public List<PORT> getPortsByName(String userId,
                                     String name,
                                     String nameParameterName,
                                     int    startFrom,
                                     int    pageSize,
                                     String methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        return portHandler.getPortsByName(userId, name, nameParameterName, startFrom, pageSize, methodName);
    }


    /**
     * Retrieve the port metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param portGUID unique identifier of the requested metadata element
     * @param portGUIDParameterName parameter passing portGUID
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public PORT getPortByGUID(String userId,
                              String portGUID,
                              String portGUIDParameterName,
                              String methodName) throws InvalidParameterException,
                                                        UserNotAuthorizedException,
                                                        PropertyServerException
    {
        return portHandler.getPortByGUID(userId, portGUID, portGUIDParameterName, methodName);
    }


    /* ===============================================================================
     * Process linkage and lineage stitching
     */
    

    /**
     * Classify a port, process or process as "BusinessSignificant" (this may effect the way that lineage is displayed).
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to update
     * @param elementGUIDParameterName parameter name for elementGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setBusinessSignificant(String userId,
                                       String elementGUID,
                                       String elementGUIDParameterName,
                                       String methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {

    }


    /**
     * Remove the "BusinessSignificant" designation from the element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to update
     * @param elementGUIDParameterName parameter name for elementGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearBusinessSignificant(String userId,
                                         String elementGUID,
                                         String elementGUIDParameterName,
                                         String methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {

    }


    /**
     * Link two elements together to show that data flows from one to the other.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param dataSupplierGUID unique identifier of the data supplier
     * @param dataSupplierGUIDParameterName parameter supplying dataSupplierGUID
     * @param dataConsumerGUID unique identifier of the data consumer
     * @param dataConsumerGUIDParameterName parameter supplying dataConsumerGUID
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
    public String setupDataFlow(String userId,
                                String externalSourceGUID,
                                String externalSourceName,
                                String dataSupplierGUID,
                                String dataSupplierGUIDParameterName,
                                String dataConsumerGUID,
                                String dataConsumerGUIDParameterName,
                                String qualifiedName,
                                String description,
                                String formula,
                                String methodName) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {
        return null;
    }


    /**
     * Retrieve the data flow relationship between two elements.  The qualifiedName is optional unless there
     * is more than one data flow relationships between these two elements since it is used to disambiguate
     * the request.
     *
     * @param userId calling user
     * @param dataSupplierGUID unique identifier of the data supplier
     * @param dataSupplierGUIDParameterName parameter supplying dataSupplierGUID
     * @param dataConsumerGUID unique identifier of the data consumer
     * @param dataConsumerGUIDParameterName parameter supplying dataConsumerGUID
     * @param qualifiedName unique identifier for this relationship
     * @param methodName calling method
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DATA_FLOW getDataFlow(String userId,
                                 String dataSupplierGUID,
                                 String dataSupplierGUIDParameterName,
                                 String dataConsumerGUID,
                                 String dataConsumerGUIDParameterName,
                                 String qualifiedName,
                                 String methodName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        return null;
    }


    /**
     * Update relationship between two elements that shows that data flows from one to the other.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param dataFlowGUID unique identifier of the data flow relationship
     * @param dataFlowGUIDParameterName parameter supplying dataFlowGUID
     * @param qualifiedName unique identifier for this relationship
     * @param description description and/or purpose of the data flow
     * @param formula function that determines the subset of the data that flows
     * @param methodName calling method
     *
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void   updateDataFlow(String userId,
                                 String externalSourceGUID,
                                 String externalSourceName,
                                 String dataFlowGUID,
                                 String dataFlowGUIDParameterName,
                                 String qualifiedName,
                                 String description,
                                 String formula,
                                 String methodName) throws InvalidParameterException,
                                                        UserNotAuthorizedException,
                                                        PropertyServerException
    {

    }


    /**
     * Remove the data flow relationship between two elements.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param dataFlowGUID unique identifier of the data flow relationship
     * @param dataFlowGUIDParameterName parameter supplying dataFlowGUID
     * @param methodName calling method

     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearDataFlow(String userId,
                              String externalSourceGUID,
                              String externalSourceName,
                              String dataFlowGUID,
                              String dataFlowGUIDParameterName,
                              String methodName) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {

    }


    /**
     * Retrieve the data flow relationships linked from an specific element to the downstream consumers.
     *
     * @param userId calling user
     * @param dataSupplierGUID unique identifier of the data supplier
     * @param dataSupplierGUIDParameterName parameter supplying dataSupplierGUID
     * @param methodName calling method
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DATA_FLOW> getDataFlowConsumers(String userId,
                                                String dataSupplierGUID,
                                                String dataSupplierGUIDParameterName,
                                                String methodName) throws InvalidParameterException,
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
     * @param dataConsumerGUIDParameterName parameter supplying dataConsumerGUID
     * @param methodName calling method
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DATA_FLOW> getDataFlowSuppliers(String userId,
                                                String dataConsumerGUID,
                                                String dataConsumerGUIDParameterName,
                                                String methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        return null;
    }


    /**
     * Link two elements to show that when one completes the next is started.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param currentStepGUID unique identifier of the previous step
     * @param currentStepGUIDParameterName parameter supplying currentStepGUID
     * @param nextStepGUID unique identifier of the next step
     * @param nextStepGUIDParameterName parameter supplying nextStepGUID
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
    public String setupControlFlow(String userId,
                                   String externalSourceGUID,
                                   String externalSourceName,
                                   String currentStepGUID,
                                   String currentStepGUIDParameterName,
                                   String nextStepGUID,
                                   String nextStepGUIDParameterName,
                                   String qualifiedName,
                                   String description,
                                   String guard,
                                   String methodName) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        return null;
    }


    /**
     * Retrieve the control flow relationship between two elements.  The qualifiedName is optional unless there
     * is more than one control flow relationships between these two elements since it is used to disambiguate
     * the request.
     *
     * @param userId calling user
     * @param currentStepGUID unique identifier of the previous step
     * @param currentStepGUIDParameterName parameter supplying currentStepGUID
     * @param nextStepGUID unique identifier of the next step
     * @param nextStepGUIDParameterName parameter supplying nextStepGUID
     * @param qualifiedName unique identifier for this relationship
     * @param methodName calling method
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public CONTROL_FLOW getControlFlow(String userId,
                                       String currentStepGUID,
                                       String currentStepGUIDParameterName,
                                       String nextStepGUID,
                                       String nextStepGUIDParameterName,
                                       String qualifiedName,
                                       String methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        return null;
    }


    /**
     * Update the relationship between two elements that shows that when one completes the next is started.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param controlFlowGUID unique identifier of the  control flow relationship
     * @param controlFlowGUIDParameterName parameter supplying controlFlowGUID
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
                                  String externalSourceGUID,
                                  String externalSourceName,
                                  String controlFlowGUID,
                                  String controlFlowGUIDParameterName,
                                  String qualifiedName,
                                  String description,
                                  String guard,
                                  String methodName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {

    }


    /**
     * Remove the control flow relationship between two elements.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param controlFlowGUID unique identifier of the  control flow relationship
     * @param controlFlowGUIDParameterName parameter supplying controlFlowGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearControlFlow(String userId,
                                 String externalSourceGUID,
                                 String externalSourceName,
                                 String controlFlowGUID,
                                 String controlFlowGUIDParameterName,
                                 String methodName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
    }


    /**
     * Retrieve the control relationships linked from an specific element to the possible next elements in the process.
     *
     * @param userId calling user
     * @param currentStepGUID unique identifier of the current step
     * @param currentStepGUIDParameterName parameter supplying currentStepGUID
     * @param methodName calling method
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<CONTROL_FLOW> getControlFlowNextSteps(String userId,
                                                      String currentStepGUID,
                                                      String currentStepGUIDParameterName,
                                                      String methodName) throws InvalidParameterException,
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
     * @param currentStepGUIDParameterName parameter supplying currentStepGUID
     * @param methodName calling method
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<CONTROL_FLOW> getControlFlowPreviousSteps(String userId,
                                                          String currentStepGUID,
                                                          String currentStepGUIDParameterName,
                                                          String methodName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        return null;
    }


    /**
     * Link two elements together to show a request-response call between them.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param callerGUID unique identifier of the element that is making the call
     * @param callerGUIDParameterName parameter supplying callerGUID
     * @param calledGUID unique identifier of the element that is processing the call
     * @param calledGUIDParameterName parameter supplying calledGUID
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
    public String setupProcessCall(String userId,
                                   String externalSourceGUID,
                                   String externalSourceName,
                                   String callerGUID,
                                   String callerGUIDParameterName,
                                   String calledGUID,
                                   String calledGUIDParameterName,
                                   String qualifiedName,
                                   String description,
                                   String formula,
                                   String methodName) throws InvalidParameterException,
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
     * @param callerGUIDParameterName parameter supplying callerGUID
     * @param calledGUID unique identifier of the element that is processing the call
     * @param calledGUIDParameterName parameter supplying calledGUID
     * @param qualifiedName unique identifier for this relationship
     * @param methodName calling method
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public PROCESS_CALL getProcessCall(String userId,
                                       String callerGUID,
                                       String callerGUIDParameterName,
                                       String calledGUID,
                                       String calledGUIDParameterName,
                                       String qualifiedName,
                                       String methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        return null;
    }


    /**
     * Update the relationship between two elements that shows a request-response call between them.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param processCallGUID unique identifier of the process call relationship
     * @param processCallGUIDParameterName parameter supplying processCallGUID
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
                                  String externalSourceGUID,
                                  String externalSourceName,
                                  String processCallGUID,
                                  String processCallGUIDParameterName,
                                  String qualifiedName,
                                  String description,
                                  String formula,
                                  String methodName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
    }


    /**
     * Remove the process call relationship.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software server capability representing the caller
     * @param externalSourceName unique name of software server capability representing the caller
     * @param processCallGUID unique identifier of the process call relationship
     * @param processCallGUIDParameterName parameter supplying processCallGUID
     * @param methodName calling method

     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearProcessCall(String userId,
                                 String externalSourceGUID,
                                 String externalSourceName,
                                 String processCallGUID,
                                 String processCallGUIDParameterName,
                                 String methodName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
    }


    /**
     * Retrieve the process call relationships linked from an specific element to the elements it calls.
     *
     * @param userId calling user
     * @param callerGUID unique identifier of the element that is making the call
     * @param callerGUIDParameterName parameter supplying callerGUID
     * @param methodName calling method
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<PROCESS_CALL> getProcessCalled(String userId,
                                               String callerGUID,
                                               String callerGUIDParameterName,
                                               String methodName) throws InvalidParameterException,
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
     * @param calledGUIDParameterName parameter supplying calledGUID
     * @param methodName calling method
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<PROCESS_CALL> getProcessCallers(String userId,
                                                String calledGUID,
                                                String calledGUIDParameterName,
                                                String methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        return null;
    }


    /**
     * Link to elements together to show that they are part of the lineage of the data that is moving
     * between the processes.  Typically the lineage relationships stitch together processes and data assets
     * supported by different technologies.
     *
     * @param userId calling user
     * @param sourceElementGUID unique identifier of the source
     * @param sourceElementGUIDParameterName parameter supplying sourceElementGUID
     * @param destinationElementGUID unique identifier of the destination
     * @param destinationElementGUIDParameterName parameter supplying destinationElementGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupLineageMapping(String userId,
                                    String sourceElementGUID,
                                    String sourceElementGUIDParameterName,
                                    String destinationElementGUID,
                                    String destinationElementGUIDParameterName,
                                    String methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
    }


    /**
     * Remove the lineage mapping between two elements.
     *
     * @param userId calling user
     * @param sourceElementGUID unique identifier of the source
     * @param sourceElementGUIDParameterName parameter supplying sourceElementGUID
     * @param destinationElementGUID unique identifier of the destination
     * @param destinationElementGUIDParameterName parameter supplying destinationElementGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearLineageMapping(String userId,
                                    String sourceElementGUID,
                                    String sourceElementGUIDParameterName,
                                    String destinationElementGUID,
                                    String destinationElementGUIDParameterName,
                                    String methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
    }


    /**
     * Retrieve the lineage mapping relationships linked from an specific source element to its destinations.
     *
     * @param userId calling user
     * @param sourceElementGUID unique identifier of the source
     * @param sourceElementGUIDParameterName parameter supplying sourceElementGUID
     * @param methodName calling method
     *
     * @return list of lineage mapping relationships
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<LINEAGE_MAPPING> getDestinationLineageMappings(String userId,
                                                               String sourceElementGUID,
                                                               String sourceElementGUIDParameterName,
                                                               String methodName) throws InvalidParameterException,
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
     * @param destinationElementGUIDParameterName parameter supplying destinationElementGUID
     * @param methodName calling method
     *
     * @return list of lineage mapping relationships
     * 
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<LINEAGE_MAPPING> getSourceLineageMappings(String userId,
                                                          String destinationElementGUID,
                                                          String destinationElementGUIDParameterName,
                                                          String methodName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        return null;
    }

}
