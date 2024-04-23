/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ffdc.GenericHandlersErrorCode;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Manages the maintenance of port entities. It does not currently support effectivity dates but probably
 * should give that option.
 *
 * @param <B> class of bean for returning the port attributes
 */
public class PortHandler<B> extends OpenMetadataAPIGenericHandler<B>
{
    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param converter specific converter for this bean class
     * @param beanClass name of bean class that is represented by the generic class B
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param supportedZones list of zones that the access service is allowed to serve Asset instances from.
     * @param defaultZones list of zones that the access service should set in all new Asset instances.
     * @param publishZones list of zones that the access service sets up in published Asset instances.
     * @param auditLog destination for audit log events.
     */
    public PortHandler(OpenMetadataAPIGenericConverter<B> converter,
                       Class<B>                           beanClass,
                       String                             serviceName,
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
    }


    /**
     * Create a new metadata element to represent a port.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param processGUID unique identifier of the process where the port is located
     * @param processGUIDParameterName parameter supplying processGUID
     * @param qualifiedName unique name for the port - used in other configuration
     * @param displayName short display name for the port
     * @param portType description of the port
     * @param additionalProperties additional properties for a port
     * @param suppliedTypeName type name from the caller (enables creation of subtypes)
     * @param extendedProperties  properties for a port subtype
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
                             boolean             forLineage,
                             boolean             forDuplicateProcessing,
                             Date                effectiveTime,
                             String              methodName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, processGUIDParameterName, methodName);

        String typeName = OpenMetadataType.PORT_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.PORT_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        PortBuilder builder = new PortBuilder(qualifiedName,
                                              displayName,
                                              portType,
                                              additionalProperties,
                                              typeGUID,
                                              typeName,
                                              extendedProperties,
                                              repositoryHelper,
                                              serviceName,
                                              serverName);

        this.addAnchorGUIDToBuilder(userId,
                                    processGUID,
                                    processGUIDParameterName,
                                    false,
                                    false,
                                    effectiveTime,
                                    supportedZones,
                                    builder,
                                    methodName);

        String portGUID = this.createBeanInRepository(userId,
                                                      externalSourceGUID,
                                                      externalSourceName,
                                                      typeGUID,
                                                      typeName,
                                                      builder,
                                                      effectiveTime,
                                                      methodName);

        if (portGUID != null)
        {
            /*
             * Link the category to its glossary.
             */
            final String portGUIDParameterName = "portGUID";

            this.uncheckedLinkElementToElement(userId,
                                               externalSourceGUID,
                                               externalSourceName,
                                               processGUID,
                                               processGUIDParameterName,
                                               OpenMetadataType.PROCESS.typeName,
                                               portGUID,
                                               portGUIDParameterName,
                                               OpenMetadataType.PORT_TYPE_NAME,
                                               forLineage,
                                               forDuplicateProcessing,
                                               supportedZones,
                                               OpenMetadataType.PROCESS_PORT_TYPE_GUID,
                                               OpenMetadataType.PROCESS_PORT_TYPE_NAME,
                                               null,
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
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param portGUID unique identifier of the port to update
     * @param portGUIDParameterName parameter supplying portGUID
     * @param qualifiedName unique name for the port - used in other configuration
     * @param displayName short display name for the port
     * @param portType description of the port
     * @param additionalProperties additional properties for a port
     * @param suppliedTypeName type name from the caller (enables creation of subtypes)
     * @param extendedProperties  properties for a port subtype
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
                           Date                effectiveFrom,
                           Date                effectiveTo,
                           boolean             forLineage,
                           boolean             forDuplicateProcessing,
                           Date                effectiveTime,
                           String              methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(portGUID, portGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        String typeName = OpenMetadataType.PORT_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }
        
        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.PORT_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        PortBuilder builder = new PortBuilder(qualifiedName,
                                              displayName,
                                              portType,
                                              additionalProperties,
                                              typeGUID,
                                              typeName,
                                              extendedProperties,
                                              repositoryHelper,
                                              serviceName,
                                              serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    portGUID,
                                    portGUIDParameterName,
                                    typeGUID,
                                    typeName,
                                    forLineage,
                                    forDuplicateProcessing,
                                    supportedZones,
                                    builder.getInstanceProperties(methodName),
                                    false,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Link a port to a process.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param processGUID unique identifier of the process
     * @param processGUIDParameterName parameter supplying processGUID
     * @param portGUID unique identifier of the port
     * @param portGUIDParameterName parameter supplying portGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupProcessPort(String  userId,
                                 String  externalSourceGUID,
                                 String  externalSourceName,
                                 String  processGUID,
                                 String  processGUIDParameterName,
                                 String  portGUID,
                                 String  portGUIDParameterName,
                                 Date    effectiveFrom,
                                 Date    effectiveTo,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing,
                                 Date    effectiveTime,
                                 String  methodName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  processGUID,
                                  processGUIDParameterName,
                                  OpenMetadataType.PROCESS.typeName,
                                  portGUID,
                                  portGUIDParameterName,
                                  OpenMetadataType.PORT_TYPE_NAME,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataType.PROCESS_PORT_TYPE_GUID,
                                  OpenMetadataType.PROCESS_PORT_TYPE_NAME,
                                  null,
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Unlink a port from a process.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param processGUID unique identifier of the process
     * @param processGUIDParameterName parameter supplying processGUID
     * @param portGUID unique identifier of the port
     * @param portGUIDParameterName parameter supplying portGUID
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
                                 String  externalSourceGUID,
                                 String  externalSourceName,
                                 String  processGUID,
                                 String  processGUIDParameterName,
                                 String  portGUID,
                                 String  portGUIDParameterName,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing,
                                 Date    effectiveTime,
                                 String  methodName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      processGUID,
                                      processGUIDParameterName,
                                      OpenMetadataType.PROCESS.typeName,
                                      portGUID,
                                      portGUIDParameterName,
                                      OpenMetadataType.PORT_TYPE_GUID,
                                      OpenMetadataType.PORT_TYPE_NAME,
                                      forLineage,
                                      forDuplicateProcessing,
                                      OpenMetadataType.PROCESS_PORT_TYPE_GUID,
                                      OpenMetadataType.PROCESS_PORT_TYPE_NAME,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Link two ports together to show that portTwo is an implementation of portOne. (That is, portOne delegates to
     * portTwo.)
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param portOneGUID unique identifier of the port at end 1
     * @param portOneGUIDParameterName parameter supplying portOneGUID
     * @param portTwoGUID unique identifier of the port at end 2
     * @param portTwoGUIDParameterName parameter supplying portTwoGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupPortDelegation(String  userId,
                                    String  externalSourceGUID,
                                    String  externalSourceName,
                                    String  portOneGUID,
                                    String  portOneGUIDParameterName,
                                    String  portTwoGUID,
                                    String  portTwoGUIDParameterName,
                                    Date    effectiveFrom,
                                    Date    effectiveTo,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing,
                                    Date    effectiveTime,
                                    String  methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  portOneGUID,
                                  portOneGUIDParameterName,
                                  OpenMetadataType.PORT_TYPE_NAME,
                                  portTwoGUID,
                                  portTwoGUIDParameterName,
                                  OpenMetadataType.PORT_TYPE_NAME,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataType.PORT_DELEGATION_TYPE_GUID,
                                  OpenMetadataType.PORT_DELEGATION_TYPE_NAME,
                                  null,
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Remove the port delegation relationship between two ports.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param portOneGUID unique identifier of the port at end 1
     * @param portOneGUIDParameterName parameter supplying portOneGUID
     * @param portTwoGUID unique identifier of the port at end 2
     * @param portTwoGUIDParameterName parameter supplying portTwoGUID
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
                                    String  externalSourceGUID,
                                    String  externalSourceName,
                                    String  portOneGUID,
                                    String  portOneGUIDParameterName,
                                    String  portTwoGUID,
                                    String  portTwoGUIDParameterName,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing,
                                    Date    effectiveTime,
                                    String  methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      portOneGUID,
                                      portOneGUIDParameterName,
                                      OpenMetadataType.PORT_TYPE_NAME,
                                      portTwoGUID,
                                      portTwoGUIDParameterName,
                                      OpenMetadataType.PORT_TYPE_GUID,
                                      OpenMetadataType.PORT_TYPE_NAME,
                                      forLineage,
                                      forDuplicateProcessing,
                                      OpenMetadataType.PORT_DELEGATION_TYPE_GUID,
                                      OpenMetadataType.PORT_DELEGATION_TYPE_NAME,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Link a schema type to a port to show the structure of data it accepts.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param portGUID unique identifier of the port
     * @param portGUIDParameterName parameter supplying portGUID
     * @param schemaTypeGUID unique identifier of the schemaType
     * @param schemaTypeGUIDParameterName parameter supplying schemaTypeGUID
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
                                    String  externalSourceGUID,
                                    String  externalSourceName,
                                    String  portGUID,
                                    String  portGUIDParameterName,
                                    String  schemaTypeGUID,
                                    String  schemaTypeGUIDParameterName,
                                    Date    effectiveFrom,
                                    Date    effectiveTo,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing,
                                    Date    effectiveTime,
                                    String  methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  portGUID,
                                  portGUIDParameterName,
                                  OpenMetadataType.PORT_TYPE_NAME,
                                  schemaTypeGUID,
                                  schemaTypeGUIDParameterName,
                                  OpenMetadataType.SCHEMA_TYPE_TYPE_NAME,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataType.PORT_SCHEMA_RELATIONSHIP_TYPE_GUID,
                                  OpenMetadataType.PORT_SCHEMA_RELATIONSHIP_TYPE_NAME,
                                  null,
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Remove the schema type from a port.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param portGUID unique identifier of the port
     * @param portGUIDParameterName parameter supplying portGUID
     * @param schemaTypeGUID unique identifier of the schemaType
     * @param schemaTypeGUIDParameterName parameter supplying schemaTypeGUID
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
                                    String  externalSourceGUID,
                                    String  externalSourceName,
                                    String  portGUID,
                                    String  portGUIDParameterName,
                                    String  schemaTypeGUID,
                                    String  schemaTypeGUIDParameterName,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing,
                                    Date    effectiveTime,
                                    String  methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      portGUID,
                                      portGUIDParameterName,
                                      OpenMetadataType.PORT_TYPE_NAME,
                                      schemaTypeGUID,
                                      schemaTypeGUIDParameterName,
                                      OpenMetadataType.SCHEMA_TYPE_TYPE_GUID,
                                      OpenMetadataType.SCHEMA_TYPE_TYPE_NAME,
                                      forLineage,
                                      forDuplicateProcessing,
                                      OpenMetadataType.PORT_SCHEMA_RELATIONSHIP_TYPE_GUID,
                                      OpenMetadataType.PORT_SCHEMA_RELATIONSHIP_TYPE_NAME,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Remove the metadata element representing a port.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param portGUID unique identifier of the metadata element to remove
     * @param portGUIDParameterName parameter supplying portGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removePort(String  userId,
                           String  externalSourceGUID,
                           String  externalSourceName,
                           String  portGUID,
                           String  portGUIDParameterName,
                           boolean forLineage,
                           boolean forDuplicateProcessing,
                           Date    effectiveTime,
                           String  methodName) throws InvalidParameterException,
                                                      UserNotAuthorizedException,
                                                      PropertyServerException
    {
        this.deleteBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    portGUID,
                                    portGUIDParameterName,
                                    OpenMetadataType.PORT_TYPE_GUID,
                                    OpenMetadataType.PORT_TYPE_NAME,
                                    null,
                                    null,
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
    public List<B> findPorts(String  userId,
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
        return this.findBeans(userId,
                              searchString,
                              searchStringParameterName,
                              OpenMetadataType.PORT_TYPE_GUID,
                              OpenMetadataType.PORT_TYPE_NAME,
                              forLineage,
                              forDuplicateProcessing,
                              supportedZones,
                              null,
                              startFrom,
                              pageSize,
                              effectiveTime,
                              methodName);
    }


    /**
     * Retrieve the list of ports associated with a process.
     *
     * @param userId calling user
     * @param processGUID unique identifier of the process of interest
     * @param processGUIDParameterName parameter passing processGUID
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
    public List<B> getPortsForProcess(String  userId,
                                      String  processGUID,
                                      String  processGUIDParameterName,
                                      int     startFrom,
                                      int     pageSize,
                                      boolean forLineage,
                                      boolean forDuplicateProcessing,
                                      Date    effectiveTime,
                                      String  methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        return this.getAttachedElements(userId,
                                        processGUID,
                                        processGUIDParameterName,
                                        OpenMetadataType.PROCESS.typeName,
                                        OpenMetadataType.PROCESS_PORT_TYPE_GUID,
                                        OpenMetadataType.PROCESS_PORT_TYPE_NAME,
                                        OpenMetadataType.PORT_TYPE_NAME,
                                        null,
                                        null,
                                        2,
                                        forLineage,
                                        forDuplicateProcessing,
                                        startFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Retrieve the list of ports that delegate to this port.
     *
     * @param userId calling user
     * @param portGUID unique identifier of the starting port
     * @param portGUIDParameterName parameter passing portGUID
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
    public List<B>  getPortUse(String  userId,
                               String  portGUID,
                               String  portGUIDParameterName,
                               int     startFrom,
                               int     pageSize,
                               boolean forLineage,
                               boolean forDuplicateProcessing,
                               Date    effectiveTime,
                               String  methodName) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {
        return this.getAttachedElements(userId,
                                        portGUID,
                                        portGUIDParameterName,
                                        OpenMetadataType.PORT_TYPE_NAME,
                                        OpenMetadataType.PORT_DELEGATION_TYPE_GUID,
                                        OpenMetadataType.PORT_DELEGATION_TYPE_NAME,
                                        OpenMetadataType.PORT_TYPE_NAME,
                                        null,
                                        null,
                                        1,
                                        forLineage,
                                        forDuplicateProcessing,
                                        startFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Retrieve the port that this port delegates to.
     *
     * @param userId calling user
     * @param portGUID unique identifier of the starting port alias
     * @param portGUIDParameterName parameter passing portGUID
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
    public B getPortDelegation(String  userId,
                               String  portGUID,
                               String  portGUIDParameterName,
                               boolean forLineage,
                               boolean forDuplicateProcessing,
                               Date    effectiveTime,
                               String  methodName) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {
        List<B> results = this.getAttachedElements(userId,
                                                   portGUID,
                                                   portGUIDParameterName,
                                                   OpenMetadataType.PORT_TYPE_NAME,
                                                   OpenMetadataType.PORT_DELEGATION_TYPE_GUID,
                                                   OpenMetadataType.PORT_DELEGATION_TYPE_NAME,
                                                   OpenMetadataType.PORT_TYPE_NAME,
                                                   null,
                                                   null,
                                                   2,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   0,
                                                   invalidParameterHandler.getMaxPagingSize(),
                                                   effectiveTime,
                                                   methodName);

        if ((results == null) || (results.isEmpty()))
        {
            return null;
        }
        else if (results.size() == 1)
        {
            return results.get(0);
        }
        else
        {
            throw new PropertyServerException(GenericHandlersErrorCode.MULTIPLE_BEANS_FOUND.getMessageDefinition(OpenMetadataType.PORT_TYPE_NAME,
                                                                                                                 methodName,
                                                                                                                 results.toString(),
                                                                                                                 portGUID,
                                                                                                                 serviceName,
                                                                                                                 serverName),
                                              this.getClass().getName(),
                                              methodName);
        }
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
    public List<B> getPortsByName(String  userId,
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
        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataProperty.QUALIFIED_NAME.name);
        specificMatchPropertyNames.add(OpenMetadataProperty.DISPLAY_NAME.name);

        return this.getBeansByValue(userId,
                                    name,
                                    nameParameterName,
                                    OpenMetadataType.PORT_TYPE_GUID,
                                    OpenMetadataType.PORT_TYPE_NAME,
                                    specificMatchPropertyNames,
                                    true,
                                    null,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    supportedZones,
                                    null,
                                    startFrom,
                                    pageSize,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Retrieve the port metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param portGUID unique identifier of the requested metadata element
     * @param portGUIDParameterName parameter passing portGUID
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
    public B getPortByGUID(String  userId,
                           String  portGUID,
                           String  portGUIDParameterName,
                           boolean forLineage,
                           boolean forDuplicateProcessing,
                           Date    effectiveTime,
                           String  methodName) throws InvalidParameterException,
                                                      UserNotAuthorizedException,
                                                      PropertyServerException
    {
        return this.getBeanFromRepository(userId,
                                          portGUID,
                                          portGUIDParameterName,
                                          OpenMetadataType.PORT_TYPE_NAME,
                                          forLineage,
                                          forDuplicateProcessing,
                                          supportedZones,
                                          effectiveTime,
                                          methodName);
    }
}
