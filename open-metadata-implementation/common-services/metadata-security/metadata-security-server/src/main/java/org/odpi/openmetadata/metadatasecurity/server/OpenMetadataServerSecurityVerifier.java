/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity.server;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.metadatasecurity.OpenMetadataElementSecurity;
import org.odpi.openmetadata.metadatasecurity.OpenMetadataServerSecurity;
import org.odpi.openmetadata.metadatasecurity.OpenMetadataServiceSecurity;
import org.odpi.openmetadata.metadatasecurity.OpenMetadataUserSecurity;
import org.odpi.openmetadata.metadatasecurity.connectors.OpenMetadataSecurityConnector;
import org.odpi.openmetadata.metadatasecurity.ffdc.OpenMetadataSecurityErrorCode;
import org.odpi.openmetadata.metadatasecurity.OpenMetadataRepositorySecurity;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefPatch;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEvent;
import org.odpi.openmetadata.repositoryservices.events.OpenMetadataEventsSecurity;

import java.util.*;


/**
 * OpenMetadataServerSecurityVerifier provides the plug-in point for the open metadata server connector.
 * It supports the same security interfaces, and handles the fact that the security connector is
 * optional.
 */
public class OpenMetadataServerSecurityVerifier implements OpenMetadataRepositorySecurity,
                                                           OpenMetadataEventsSecurity,
                                                           OpenMetadataElementSecurity,
                                                           OpenMetadataServerSecurity,
                                                           OpenMetadataServiceSecurity
{
    private OpenMetadataRepositorySecurity repositorySecurityConnector = null;
    private OpenMetadataEventsSecurity     eventsSecurityConnector     = null;
    private OpenMetadataServerSecurity     serverSecurityConnector     = null;
    private OpenMetadataServiceSecurity    serviceSecurityConnector    = null;
    private OpenMetadataElementSecurity    elementSecurityConnector    = null;
    private OpenMetadataUserSecurity       userSecurityConnector       = null;

    private final InvalidParameterHandler       invalidParameterHandler      = new InvalidParameterHandler();



    /**
     * Default constructor
     */
    public OpenMetadataServerSecurityVerifier()
    {
    }


    /**
     * Register an open metadata server security connector to verify access to the server's services.
     *
     * @param localServerUserId local server's userId
     * @param serverName local server's name
     * @param auditLog logging destination
     * @param connection properties used to create the connector
     *
     * @throws InvalidParameterException the connection is invalid
     */
    synchronized public  void registerSecurityValidator(String     localServerUserId,
                                                        String     serverName,
                                                        AuditLog   auditLog,
                                                        Connection connection) throws InvalidParameterException
    {
        OpenMetadataSecurityConnector connector;

        try
        {
            /*
             * The connector standard has lots of optional interfaces.  The tests below set up local variables, one
             * for each interface, to make it easier to call them when the server is processing requests.
             */
            connector = this.getServerSecurityConnector(localServerUserId,
                                                        serverName,
                                                        auditLog,
                                                        connection);

            if (connector instanceof OpenMetadataRepositorySecurity)
            {
                repositorySecurityConnector = (OpenMetadataRepositorySecurity)connector;
            }
            if (connector instanceof OpenMetadataEventsSecurity)
            {
                eventsSecurityConnector = (OpenMetadataEventsSecurity)connector;
            }
            if (connector instanceof OpenMetadataServerSecurity)
            {
                serverSecurityConnector = (OpenMetadataServerSecurity)connector;
            }
            if (connector instanceof OpenMetadataServiceSecurity)
            {
                serviceSecurityConnector = (OpenMetadataServiceSecurity)connector;
            }
            if (connector instanceof OpenMetadataElementSecurity)
            {
                elementSecurityConnector = (OpenMetadataElementSecurity)connector;
            }
            if (connector instanceof OpenMetadataUserSecurity)
            {
                userSecurityConnector = (OpenMetadataUserSecurity)connector;
            }
        }
        catch (InvalidParameterException error)
        {
            throw new InvalidParameterException(error.getReportedErrorMessage(), error);
        }
    }


    /**
     * Return the Open Metadata Server Security Connector for the connection.
     *
     * @param localServerUserId userId of the server
     * @param serverName name of the server
     * @param auditLog logging destination
     * @param connection connection from the configuration document
     * @return connector or null
     * @throws InvalidParameterException connection did not create a connector
     */
    private   OpenMetadataSecurityConnector getServerSecurityConnector(String     localServerUserId,
                                                                       String     serverName,
                                                                       AuditLog   auditLog,
                                                                       Connection connection) throws InvalidParameterException
    {
        final String methodName = "getServerSecurityConnector";

        OpenMetadataSecurityConnector serverSecurityConnector = null;

        if (connection != null)
        {
            try
            {
                ConnectorBroker connectorBroker = new ConnectorBroker(auditLog);
                Connector       connector       = connectorBroker.getConnector(connection);

                serverSecurityConnector = (OpenMetadataSecurityConnector)connector;

                serverSecurityConnector.setServerName(serverName);
                serverSecurityConnector.setLocalServerUserId(localServerUserId);
                serverSecurityConnector.start();
            }
            catch (Exception error)
            {
                /*
                 * The assumption is that any exceptions creating the new connector are down to a bad connection
                 */
                throw new InvalidParameterException(OpenMetadataSecurityErrorCode.BAD_SERVER_SECURITY_CONNECTION.getMessageDefinition(serverName,
                                                                                                                                      error.getMessage(),
                                                                                                                                      connection.toString()),
                                                    OpenMetadataPlatformSecurityVerifier.class.getName(),
                                                    methodName,
                                                    error,
                                                    "connection");
            }
        }

        return serverSecurityConnector;
    }


    /**
     * Return the list of visible zones for this user.
     *
     * @param userId calling user
     * @param typeName type of the element
     * @param methodName name of the called service
     * @return list of zone names
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem from the verifier
     * @throws UserNotAuthorizedException user not recognized
     */
    public List<String> getSupportedZones(String userId,
                                          String typeName,
                                          String methodName) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        /*
         * The userId is always one of the supported zones
         */
        if (userSecurityConnector != null)
        {
            return userSecurityConnector.getSupportedZonesForUser(new ArrayList<>(Collections.singletonList(userId)),
                                                                  typeName,
                                                                  methodName,
                                                                  userId);
        }

        return new ArrayList<>(Collections.singletonList(userId));
    }


    /**
     * Return the list of visible zones for this user.
     *
     * @param userId calling user
     * @param typeName type of the element
     * @param methodName name of the called service
     * @return list of zone names
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem from the verifier
     * @throws UserNotAuthorizedException user not recognized
     */
    public List<String> getDefaultZones(List<String> initialZones,
                                        String       userId,
                                        String       typeName,
                                        String       methodName) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        /*
         * The userId is always one of the supported zones
         */
        if (userSecurityConnector != null)
        {
            return userSecurityConnector.getDefaultZonesForUser(initialZones,
                                                                typeName,
                                                                methodName,
                                                                userId);
        }

        return initialZones;
    }


    /**
     * Determine the appropriate setting for the default zones depending on the user and the
     * default publish zones set up for the service.  This is called whenever an element is published.
     *
     * @param currentZones default setting of the published zones
     * @param userId calling user
     * @param typeName type of the element
     * @param methodName name of the called service
     *
     * @return list of published zones for the user
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem from the verifier
     * @throws UserNotAuthorizedException user not recognized
     */
    public List<String> getPublishZones(List<String> currentZones,
                                        String       userId,
                                        String       typeName,
                                        String       methodName) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        if (userSecurityConnector != null)
        {
            return userSecurityConnector.getPublishZonesForUser(currentZones,
                                                                typeName,
                                                                methodName,
                                                                userId);
        }

        return currentZones;
    }


    /**
     * Check that the calling user is authorized to issue a (any) request to the OMAG Server Platform.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to access this function
     */
    @Override
    public void  validateUserForServer(String   userId) throws UserNotAuthorizedException
    {
        if (serverSecurityConnector != null)
        {
            serverSecurityConnector.validateUserForServer(userId);
        }
    }


    /**
     * Check that the calling user is authorized to update the configuration for a server.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to change configuration
     */
    @Override
    public void  validateUserAsServerAdmin(String   userId) throws UserNotAuthorizedException
    {
        if (serverSecurityConnector != null)
        {
            serverSecurityConnector.validateUserAsServerAdmin(userId);
        }
    }


    /**
     * Check that the calling user is authorized to issue operator requests to the OMAG Server.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to issue operator commands to this server
     */
    @Override
    public void  validateUserAsServerOperator(String   userId) throws UserNotAuthorizedException
    {
        if (serverSecurityConnector != null)
        {
            serverSecurityConnector.validateUserAsServerOperator(userId);
        }
    }


    /**
     * Check that the calling user is authorized to issue operator requests to the OMAG Server.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to issue diagnostic commands to this server
     */
    @Override
    public void  validateUserAsServerInvestigator(String   userId) throws UserNotAuthorizedException
    {
        if (serverSecurityConnector != null)
        {
            serverSecurityConnector.validateUserAsServerInvestigator(userId);
        }
    }


    /**
     * Check that the calling user is authorized to issue this request.
     *
     * @param userId calling user
     * @param serviceName name of called service
     *
     * @throws UserNotAuthorizedException the user is not authorized to access this service
     */
    @Override
    public void  validateUserForService(String   userId,
                                        String   serviceName) throws UserNotAuthorizedException
    {
        if (serviceSecurityConnector != null)
        {
            serviceSecurityConnector.validateUserForService(userId, serviceName);
        }
    }


    /**
     * Check that the calling user is authorized to issue this specific request.
     *
     * @param userId calling user
     * @param serviceName name of called service
     * @param serviceOperationName name of called operation
     *
     * @throws UserNotAuthorizedException the user is not authorized to access this service
     */
    @Override
    public void  validateUserForServiceOperation(String   userId,
                                                 String   serviceName,
                                                 String   serviceOperationName) throws UserNotAuthorizedException
    {
        if (serviceSecurityConnector != null)
        {
            serviceSecurityConnector.validateUserForServiceOperation(userId, serviceName, serviceOperationName);
        }
    }

    /*
     * =========================================================================================================
     * Element Security
     */

    /**
     * Use the security connector to make a choice on which connection to supply to the requesting user.
     *
     * @param userId calling userId
     * @param assetEntity associated asset - may be null
     * @param connectionEntities list of retrieved connections
     * @param repositoryHelper for working with OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @return single connection entity, or null
     * @throws UserNotAuthorizedException the user is not able to use any of the connections
     */
    @Override
    public EntityDetail selectConnection(String               userId,
                                         EntityDetail         assetEntity,
                                         List<EntityDetail>   connectionEntities,
                                         OMRSRepositoryHelper repositoryHelper,
                                         String               serviceName,
                                         String               methodName) throws UserNotAuthorizedException
    {
        if (elementSecurityConnector != null)
        {
            EntityDetail selectedConnection = elementSecurityConnector.selectConnection(userId, assetEntity, connectionEntities, repositoryHelper, serviceName, methodName);

            if (selectedConnection == null)
            {
                throw new UserNotAuthorizedException(OpenMetadataSecurityErrorCode.NO_CONNECTIONS_ALLOWED.getMessageDefinition(Integer.toString(connectionEntities.size()),
                                                                                                                               assetEntity.getGUID(),
                                                                                                                               userId,
                                                                                                                               methodName),
                                                     this.getClass().getName(),
                                                     userId,
                                                     methodName);
            }

            return selectedConnection;
        }
        else if ((connectionEntities == null) || (connectionEntities.isEmpty()))
        {
            return null;
        }
        else if (connectionEntities.size() == 1)
        {
            return connectionEntities.get(0);
        }
        else
        {
            /*
             * Randomly pick one
             */
            Random rand = new Random();

            return connectionEntities.get(rand.nextInt(connectionEntities.size()));
        }
    }


    /**
     * Tests for whether a specific user should have the right to create an element.
     *
     * @param userId identifier of user
     * @param entityTypeGUID unique identifier of the type of entity to create
     * @param entityTypeName unique name of the type of entity to create
     * @param newProperties properties for new entity
     * @param classifications classifications for new entity
     * @param instanceStatus status for new entity
     * @param repositoryHelper manipulates repository service objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to perform this command
     */
    @Override
    public void validateUserForElementCreate(String                         userId,
                                             String                         entityTypeGUID,
                                             String                         entityTypeName,
                                             InstanceProperties             newProperties,
                                             List<Classification>           classifications,
                                             InstanceStatus                 instanceStatus,
                                             OMRSRepositoryHelper           repositoryHelper,
                                             String                         serviceName,
                                             String                         methodName) throws UserNotAuthorizedException,
                                                                                               InvalidParameterException,
                                                                                               PropertyServerException
    {
        this.elementVisibleToUser(userId, "<new>", entityTypeName, classifications, repositoryHelper, serviceName, methodName);

        if (elementSecurityConnector != null)
        {
            elementSecurityConnector.validateUserForElementCreate(userId, entityTypeGUID, entityTypeName, newProperties, classifications, instanceStatus, repositoryHelper, serviceName, methodName);
        }
    }


    /**
     * Validate that the user has visibility to the zones associated with the element.
     *
     * @param userId calling user
     * @param elementGUID element to verify
     * @param typeName name of type
     * @param classifications element's classification where the ZoneMembership lies
     * @param repositoryHelper repository helper
     * @param serviceName calling service
     * @param methodName calling method
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private void elementVisibleToUser(String               userId,
                                      String               elementGUID,
                                      String               typeName,
                                      List<Classification> classifications,
                                      OMRSRepositoryHelper repositoryHelper,
                                      String               serviceName,
                                      String               methodName) throws UserNotAuthorizedException,
                                                                              InvalidParameterException,
                                                                              PropertyServerException
    {
        if (classifications != null)
        {
            List<String> elementZoneMembership = null;

            for (Classification classification : classifications)
            {
                if ((classification != null) && (OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName.equals(classification.getName())))
                {
                    elementZoneMembership = repositoryHelper.getStringArrayProperty(serviceName,
                                                                                    OpenMetadataProperty.ZONE_MEMBERSHIP.name,
                                                                                    classification.getProperties(),
                                                                                    methodName);
                    break;
                }
            }

            if ((elementZoneMembership != null) && (! elementZoneMembership.isEmpty()))
            {
                invalidParameterHandler.validateElementInSupportedZone(elementGUID,
                                                                       OpenMetadataProperty.ZONE_MEMBERSHIP.name,
                                                                       elementZoneMembership,
                                                                       this.getSupportedZones(userId, typeName, methodName),
                                                                       serviceName,
                                                                       methodName);
            }
        }
    }

    /**
     * Tests for whether a specific user should have read access to a specific element and its contents.
     *
     * @param userId calling user
     * @param requestedEntity entity requested by the caller
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void validateUserForElementRead(String               userId,
                                           EntityDetail         requestedEntity,
                                           OMRSRepositoryHelper repositoryHelper,
                                           String               serviceName,
                                           String               methodName) throws UserNotAuthorizedException,
                                                                                   InvalidParameterException,
                                                                                   PropertyServerException
    {
        this.elementVisibleToUser(userId,
                                  requestedEntity.getGUID(),
                                  requestedEntity.getType().getTypeDefName(),
                                  requestedEntity.getClassifications(),
                                  repositoryHelper,
                                  serviceName,
                                  methodName);

        if (elementSecurityConnector != null)
        {
            elementSecurityConnector.validateUserForElementRead(userId, requestedEntity, repositoryHelper, serviceName, methodName);
        }
    }


    /**
     * Tests for whether a specific user should have read access to a specific element and its contents.
     *
     * @param userId calling user
     * @param anchorEntity entity for the anchor (if extracted - may be null)
     * @param requestedEntity entity requested by the caller
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException user not authorized to issue this request
     */
    @Override
    public void validateUserForAnchorMemberRead(String               userId,
                                                EntityDetail         anchorEntity,
                                                EntityDetail         requestedEntity,
                                                OMRSRepositoryHelper repositoryHelper,
                                                String               serviceName,
                                                String               methodName) throws UserNotAuthorizedException
    {
        if (elementSecurityConnector != null)
        {
            elementSecurityConnector.validateUserForAnchorMemberRead(userId, anchorEntity, requestedEntity, repositoryHelper, serviceName, methodName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to update the properties of an element.
     *
     * @param userId identifier of user
     * @param originalEntity original entity details
     * @param newEntityProperties new properties
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    @Override
    public void validateUserForElementDetailUpdate(String               userId,
                                                   EntityDetail         originalEntity,
                                                   InstanceProperties   newEntityProperties,
                                                   OMRSRepositoryHelper repositoryHelper,
                                                   String               serviceName,
                                                   String               methodName) throws UserNotAuthorizedException
    {
        if (elementSecurityConnector != null)
        {
            elementSecurityConnector.validateUserForElementDetailUpdate(userId, originalEntity, newEntityProperties, repositoryHelper, serviceName, methodName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to update elements attached directly
     * to an anchor such as glossary terms and categories attached to an element.  These updates could be to their properties,
     * classifications and relationships.
     *
     * @param userId identifier of user
     * @param anchorEntity element details
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    @Override
    public void validateUserForAnchorMemberUpdate(String               userId,
                                                  EntityDetail anchorEntity,
                                                  OMRSRepositoryHelper repositoryHelper,
                                                  String               serviceName,
                                                  String               methodName) throws UserNotAuthorizedException
    {
        if (elementSecurityConnector != null)
        {
            elementSecurityConnector.validateUserForAnchorMemberUpdate(userId, anchorEntity, repositoryHelper, serviceName, methodName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to update the properties of an element.
     *
     * @param userId identifier of user
     * @param originalEntity original entity details
     * @param newStatus new value for status
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    @Override
    public void validateUserForElementStatusUpdate(String               userId,
                                                   EntityDetail         originalEntity,
                                                   InstanceStatus       newStatus,
                                                   OMRSRepositoryHelper repositoryHelper,
                                                   String               serviceName,
                                                   String               methodName) throws UserNotAuthorizedException
    {
        if (elementSecurityConnector != null)
        {
            elementSecurityConnector.validateUserForElementStatusUpdate(userId, originalEntity, newStatus, repositoryHelper, serviceName, methodName);
        }
    }

    /**
     * Tests for whether a specific user should have the right to link unanchored elements to this element
     *
     * @param userId           identifier of user
     * @param startingEntity   end 1 details
     * @param attachingEntity  end 1 details
     * @param relationshipName name of the relationship
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName      calling service
     * @param methodName       calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    @Override
    public void validateUserForElementAttach(String userId, EntityDetail startingEntity, EntityDetail attachingEntity, String relationshipName, OMRSRepositoryHelper repositoryHelper, String serviceName, String methodName) throws UserNotAuthorizedException
    {
        if (elementSecurityConnector != null)
        {
            elementSecurityConnector.validateUserForElementAttach(userId, startingEntity, attachingEntity, relationshipName, repositoryHelper, serviceName, methodName);
        }
    }

    /**
     * Tests for whether a specific user should have the right to link unanchored elements to this element
     *
     * @param userId           identifier of user
     * @param startingEntity   end 1 details
     * @param detachingEntity  end 2 details
     * @param relationshipName name of the relationship
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName      calling service
     * @param methodName       calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    @Override
    public void validateUserForElementDetach(String userId, EntityDetail startingEntity, EntityDetail detachingEntity, String relationshipName, OMRSRepositoryHelper repositoryHelper, String serviceName, String methodName) throws UserNotAuthorizedException
    {
        if (elementSecurityConnector != null)
        {
            elementSecurityConnector.validateUserForElementDetach(userId, startingEntity, detachingEntity, relationshipName, repositoryHelper, serviceName, methodName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to attach feedback - such as comments,
     * ratings, tags and likes, to the element.
     *
     * @param userId           identifier of user
     * @param originalEntity   original entity details
     * @param feedbackEntity   feedback element
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName      calling service
     * @param methodName       calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    @Override
    public void validateUserForElementAddFeedback(String userId, EntityDetail originalEntity, EntityDetail feedbackEntity, OMRSRepositoryHelper repositoryHelper, String serviceName, String methodName) throws UserNotAuthorizedException
    {
        if (elementSecurityConnector != null)
        {
            elementSecurityConnector.validateUserForElementAddFeedback(userId, originalEntity, feedbackEntity, repositoryHelper, serviceName, methodName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to detach feedback - such as comments,
     * ratings, tags and likes, to the element.
     *
     * @param userId           identifier of user
     * @param originalEntity   original entity details
     * @param feedbackEntity   feedback element
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName      calling service
     * @param methodName       calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    @Override
    public void validateUserForElementDeleteFeedback(String userId, EntityDetail originalEntity, EntityDetail feedbackEntity, OMRSRepositoryHelper repositoryHelper, String serviceName, String methodName) throws UserNotAuthorizedException
    {
        if (elementSecurityConnector != null)
        {
            elementSecurityConnector.validateUserForElementDeleteFeedback(userId, originalEntity, feedbackEntity, repositoryHelper, serviceName, methodName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to add or update a classification on this element.
     *
     * @param userId             identifier of user
     * @param originalEntity     original entity details
     * @param classificationName name of the classification
     * @param repositoryHelper   helper for OMRS objects
     * @param serviceName        calling service
     * @param methodName         calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    @Override
    public void validateUserForElementClassify(String userId, EntityDetail originalEntity, String classificationName, OMRSRepositoryHelper repositoryHelper, String serviceName, String methodName) throws UserNotAuthorizedException
    {
        if (elementSecurityConnector != null)
        {
            elementSecurityConnector.validateUserForElementClassify(userId, originalEntity, classificationName, repositoryHelper, serviceName, methodName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to remove a classification from this element
     *
     * @param userId             identifier of user
     * @param originalEntity     original entity details
     * @param classificationName name of the classification
     * @param repositoryHelper   helper for OMRS objects
     * @param serviceName        calling service
     * @param methodName         calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    @Override
    public void validateUserForElementDeclassify(String userId, EntityDetail originalEntity, String classificationName, OMRSRepositoryHelper repositoryHelper, String serviceName, String methodName) throws UserNotAuthorizedException
    {
        if (elementSecurityConnector != null)
        {
            elementSecurityConnector.validateUserForElementDeclassify(userId, originalEntity, classificationName, repositoryHelper, serviceName, methodName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to update the instance status of an element.
     *
     * @param userId identifier of user
     * @param anchorEntity anchor details
     * @param originalEntity original entity details
     * @param newStatus new value for status
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    @Override
    public void validateUserForAnchorMemberStatusUpdate(String               userId,
                                                        EntityDetail         anchorEntity,
                                                        EntityDetail         originalEntity,
                                                        InstanceStatus       newStatus,
                                                        OMRSRepositoryHelper repositoryHelper,
                                                        String               serviceName,
                                                        String               methodName) throws UserNotAuthorizedException
    {
        if (elementSecurityConnector != null)
        {
            elementSecurityConnector.validateUserForAnchorMemberStatusUpdate(userId, anchorEntity, originalEntity, newStatus, repositoryHelper, serviceName, methodName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to link unanchored  elements
     *
     * @param userId           identifier of user
     * @param anchorEntity     anchor details
     * @param attachingEntity  new element
     * @param relationshipName name of the relationship
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName      calling service
     * @param methodName       calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    @Override
    public void validateUserForAnchorAttach(String userId, EntityDetail anchorEntity, EntityDetail attachingEntity, String relationshipName, OMRSRepositoryHelper repositoryHelper, String serviceName, String methodName) throws UserNotAuthorizedException
    {
        if (elementSecurityConnector != null)
        {
            elementSecurityConnector.validateUserForAnchorAttach(userId, anchorEntity, attachingEntity, relationshipName, repositoryHelper, serviceName, methodName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to link unanchored  elements
     *
     * @param userId           identifier of user
     * @param anchorEntity     anchor details
     * @param detachingEntity  obsolete element
     * @param relationshipName name of the relationship
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName      calling service
     * @param methodName       calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    @Override
    public void validateUserForAnchorDetach(String userId, EntityDetail anchorEntity, EntityDetail detachingEntity, String relationshipName, OMRSRepositoryHelper repositoryHelper, String serviceName, String methodName) throws UserNotAuthorizedException
    {
        if (elementSecurityConnector != null)
        {
            elementSecurityConnector.validateUserForAnchorDetach(userId, anchorEntity, detachingEntity, relationshipName, repositoryHelper, serviceName, methodName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to attach feedback - such as comments,
     * ratings, tags and likes, to the anchor or member element.
     *
     * @param userId           identifier of user
     * @param anchorEntity     anchor details
     * @param feedbackEntity   feedback element
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName      calling service
     * @param methodName       calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    @Override
    public void validateUserForAnchorAddFeedback(String userId, EntityDetail anchorEntity, EntityDetail feedbackEntity, OMRSRepositoryHelper repositoryHelper, String serviceName, String methodName) throws UserNotAuthorizedException
    {
        if (elementSecurityConnector != null)
        {
            elementSecurityConnector.validateUserForAnchorAddFeedback(userId, anchorEntity, feedbackEntity, repositoryHelper, serviceName, methodName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to detach feedback - such as comments,
     * ratings, tags and likes, to the anchor or member element.
     *
     * @param userId           identifier of user
     * @param anchorEntity     anchor details
     * @param feedbackEntity   feedback element
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName      calling service
     * @param methodName       calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    @Override
    public void validateUserForAnchorDeleteFeedback(String userId, EntityDetail anchorEntity, EntityDetail feedbackEntity, OMRSRepositoryHelper repositoryHelper, String serviceName, String methodName) throws UserNotAuthorizedException
    {
        if (elementSecurityConnector != null)
        {
            elementSecurityConnector.validateUserForAnchorDeleteFeedback(userId, anchorEntity, feedbackEntity, repositoryHelper, serviceName, methodName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to add or update a classification on this anchor or member element.
     *
     * @param userId             identifier of user
     * @param anchorEntity       anchor details
     * @param classificationName name of the classification
     * @param repositoryHelper   helper for OMRS objects
     * @param serviceName        calling service
     * @param methodName         calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    @Override
    public void validateUserForAnchorClassify(String userId, EntityDetail anchorEntity, String classificationName, OMRSRepositoryHelper repositoryHelper, String serviceName, String methodName) throws UserNotAuthorizedException
    {
        if (elementSecurityConnector != null)
        {
            elementSecurityConnector.validateUserForAnchorClassify(userId, anchorEntity, classificationName, repositoryHelper, serviceName, methodName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to remove a classification from this anchor or member element
     *
     * @param userId             identifier of user
     * @param anchorEntity       anchor details
     * @param classificationName name of the classification
     * @param repositoryHelper   helper for OMRS objects
     * @param serviceName        calling service
     * @param methodName         calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    @Override
    public void validateUserForAnchorDeclassify(String userId, EntityDetail anchorEntity, String classificationName, OMRSRepositoryHelper repositoryHelper, String serviceName, String methodName) throws UserNotAuthorizedException
    {
        if (elementSecurityConnector != null)
        {
            elementSecurityConnector.validateUserForAnchorDeclassify(userId, anchorEntity, classificationName, repositoryHelper, serviceName, methodName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to delete an element and all of its contents.
     *
     * @param userId identifier of user
     * @param obsoleteEntity original element details
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    @Override
    public void validateUserForAnchorMemberDelete(String               userId,
                                                  EntityDetail         anchorEntity,
                                                  EntityDetail         obsoleteEntity,
                                                  OMRSRepositoryHelper repositoryHelper,
                                                  String               serviceName,
                                                  String               methodName) throws UserNotAuthorizedException
    {
        if (elementSecurityConnector != null)
        {
            elementSecurityConnector.validateUserForAnchorMemberDelete(userId, anchorEntity, obsoleteEntity, repositoryHelper, serviceName, methodName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to delete an element and all of its contents.
     *
     * @param userId identifier of user
     * @param entity original element details
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    @Override
    public void validateUserForElementDelete(String               userId,
                                             EntityDetail         entity,
                                             OMRSRepositoryHelper repositoryHelper,
                                             String               serviceName,
                                             String               methodName) throws UserNotAuthorizedException
    {
        if (elementSecurityConnector != null)
        {
            elementSecurityConnector.validateUserForElementDelete(userId, entity, repositoryHelper, serviceName, methodName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to attach feedback - such as comments,
     * ratings, tags and likes, to the element.
     *
     * @param userId           identifier of user
     * @param anchorEntity     anchor details
     * @param newMemberEntity  feedback element
     * @param relationshipName name of the relationship
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName      calling service
     * @param methodName       calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    @Override
    public void validateUserForAnchorMemberAdd(String userId, EntityDetail anchorEntity, EntityDetail newMemberEntity, String relationshipName, OMRSRepositoryHelper repositoryHelper, String serviceName, String methodName) throws UserNotAuthorizedException
    {
        if (elementSecurityConnector != null)
        {
            elementSecurityConnector.validateUserForAnchorMemberAdd(userId, anchorEntity, newMemberEntity, relationshipName, repositoryHelper, serviceName, methodName);
        }
    }


    /*
     * =========================================================================================================
     * Type security
     */

    /**
     * Tests for whether a specific user should have the right to create a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param typeDef type details
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    @Override
    public void  validateUserForTypeCreate(String  userId,
                                           String  metadataCollectionName,
                                           TypeDef typeDef) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForTypeCreate(userId, metadataCollectionName, typeDef);
        }
    }


    /**
     * Tests for whether a specific user should have the right to create a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param attributeTypeDef type details
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    @Override
    public void  validateUserForTypeCreate(String           userId,
                                           String           metadataCollectionName,
                                           AttributeTypeDef attributeTypeDef) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForTypeCreate(userId, metadataCollectionName, attributeTypeDef);
        }
    }


    /**
     * Tests for whether a specific user should have read access to a specific type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param typeDef type details
     * @throws UserNotAuthorizedException the user is not authorized to retrieve types
     */
    @Override
    public void  validateUserForTypeRead(String     userId,
                                         String     metadataCollectionName,
                                         TypeDef    typeDef) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForTypeRead(userId, metadataCollectionName, typeDef);
        }
    }


    /**
     * Tests for whether a specific user should have read access to a specific type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param attributeTypeDef type details
     * @throws UserNotAuthorizedException the user is not authorized to retrieve types
     */
    @Override
    public void  validateUserForTypeRead(String              userId,
                                         String              metadataCollectionName,
                                         AttributeTypeDef    attributeTypeDef) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForTypeRead(userId, metadataCollectionName, attributeTypeDef);
        }
    }


    /**
     * Tests for whether a specific user should have the right to update a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param typeDef type details
     * @param patch changes to the type
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    @Override
    public void  validateUserForTypeUpdate(String       userId,
                                           String       metadataCollectionName,
                                           TypeDef      typeDef,
                                           TypeDefPatch patch) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForTypeUpdate(userId, metadataCollectionName, typeDef, patch);
        }
    }


    /**
     * Tests for whether a specific user should have the right to delete a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param typeDef type details
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    @Override
    public void  validateUserForTypeDelete(String     userId,
                                           String     metadataCollectionName,
                                           TypeDef    typeDef) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForTypeDelete(userId, metadataCollectionName, typeDef);
        }
    }


    /**
     * Tests for whether a specific user should have the right to delete a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param attributeTypeDef type details
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    @Override
    public void  validateUserForTypeDelete(String              userId,
                                           String              metadataCollectionName,
                                           AttributeTypeDef    attributeTypeDef) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForTypeDelete(userId, metadataCollectionName, attributeTypeDef);
        }
    }


    /**
     * Tests for whether a specific user should have the right to change the identifiers for a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param originalTypeDef type details
     * @param newTypeDefGUID the new identifier for the type.
     * @param newTypeDefName new name for this type.
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    @Override
    public void  validateUserForTypeReIdentify(String  userId,
                                               String  metadataCollectionName,
                                               TypeDef originalTypeDef,
                                               String  newTypeDefGUID,
                                               String  newTypeDefName) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForTypeReIdentify(userId, metadataCollectionName, originalTypeDef, newTypeDefGUID, newTypeDefName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to change the identifiers for a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param originalAttributeTypeDef type details
     * @param newTypeDefGUID the new identifier for the type.
     * @param newTypeDefName new name for this type.
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    @Override
    public void  validateUserForTypeReIdentify(String           userId,
                                               String           metadataCollectionName,
                                               AttributeTypeDef originalAttributeTypeDef,
                                               String           newTypeDefGUID,
                                               String           newTypeDefName) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForTypeReIdentify(userId,
                                                                      metadataCollectionName,
                                                                      originalAttributeTypeDef,
                                                                      newTypeDefGUID,
                                                                      newTypeDefName);
        }
    }


    /*
     * =========================================================================================================
     * Instance Security
     *
     * No specific security checks made when instances are written and retrieved from the local repository.
     * The methods override the super class that throws a user not authorized exception on all access/update
     * requests.
     */

    /**
     * Tests for whether a specific user should have the right to create an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param entityTypeGUID unique identifier (guid) for the new entity's type.
     * @param initialProperties initial list of properties for the new entity null means no properties.
     * @param initialClassifications initial list of classifications for the new entity null means no classifications.
     * @param initialStatus initial status typically ACTIVE.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityCreate(String                     userId,
                                             String                     metadataCollectionName,
                                             String                     entityTypeGUID,
                                             InstanceProperties         initialProperties,
                                             List<Classification>       initialClassifications,
                                             InstanceStatus             initialStatus) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForEntityCreate(userId,
                                                                    metadataCollectionName,
                                                                    entityTypeGUID,
                                                                    initialProperties,
                                                                    initialClassifications,
                                                                    initialStatus);
        }
    }


    /**
     * Tests for whether a specific user should have read access to a specific instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @return entity to return (maybe altered by the connector)
     * @throws UserNotAuthorizedException the user is not authorized to retrieve instances
     */
    @Override
    public EntityDetail  validateUserForEntityRead(String          userId,
                                                   String          metadataCollectionName,
                                                   EntityDetail    instance) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            return repositorySecurityConnector.validateUserForEntityRead(userId, metadataCollectionName, new EntityDetail(instance));
        }

        return instance;
    }


    /**
     * Tests for whether a specific user should have read access to a specific instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to retrieve instances
     */
    @Override
    public void  validateUserForEntitySummaryRead(String        userId,
                                                  String        metadataCollectionName,
                                                  EntitySummary instance) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForEntitySummaryRead(userId, metadataCollectionName, new EntitySummary(instance));
        }
    }


    /**
     * Tests for whether a specific user should have read access to a specific instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to retrieve instances
     */
    @Override
    public void  validateUserForEntityProxyRead(String      userId,
                                                String      metadataCollectionName,
                                                EntityProxy instance) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForEntityProxyRead(userId, metadataCollectionName, new EntityProxy(instance));
        }
    }


    /**
     * Tests for whether a specific user should have the right to update an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityUpdate(String          userId,
                                             String          metadataCollectionName,
                                             EntityDetail    instance) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForEntityUpdate(userId, metadataCollectionName, new EntityDetail(instance));
        }
    }


    /**
     * Tests for whether a specific user should have the right to add a classification to an entity instance
     * within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param classificationName String name for the classification.
     * @param properties list of properties for the classification.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityClassificationAdd(String               userId,
                                                        String               metadataCollectionName,
                                                        EntitySummary        instance,
                                                        String               classificationName,
                                                        InstanceProperties   properties) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForEntityClassificationAdd(userId,
                                                                               metadataCollectionName,
                                                                               instance,
                                                                               classificationName,
                                                                               properties);
        }
    }


    /**
     * Tests for whether a specific user should have the right to update the classification for an entity instance
     * within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param classificationName String name for the classification.
     * @param properties list of properties for the classification.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityClassificationUpdate(String               userId,
                                                           String               metadataCollectionName,
                                                           EntitySummary        instance,
                                                           String               classificationName,
                                                           InstanceProperties   properties) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForEntityClassificationUpdate(userId,
                                                                                  metadataCollectionName,
                                                                                  instance,
                                                                                  classificationName,
                                                                                  properties);
        }
    }


    /**
     * Tests for whether a specific user should have the right to delete a classification from an entity instance
     * within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param classificationName String name for the classification.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityClassificationDelete(String               userId,
                                                           String               metadataCollectionName,
                                                           EntitySummary        instance,
                                                           String               classificationName) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForEntityClassificationDelete(userId,
                                                                                  metadataCollectionName,
                                                                                  instance,
                                                                                  classificationName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to delete an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityDelete(String       userId,
                                             String       metadataCollectionName,
                                             EntityDetail instance) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForEntityDelete(userId, metadataCollectionName, new EntityDetail(instance));
        }
    }


    /**
     * Tests for whether a specific user should have the right to delete an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param deletedEntityGUID String unique identifier (guid) for the entity.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityRestore(String       userId,
                                              String       metadataCollectionName,
                                              String       deletedEntityGUID) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForEntityRestore(userId, metadataCollectionName, deletedEntityGUID);
        }
    }


    /**
     * Tests for whether a specific user should have the right to change the guid on an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newGUID the new guid for the instance.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityReIdentification(String       userId,
                                                       String       metadataCollectionName,
                                                       EntityDetail instance,
                                                       String       newGUID) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForEntityReIdentification(userId, metadataCollectionName, instance, newGUID);
        }
    }


    /**
     * Tests for whether a specific user should have the right to change an instance's type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newTypeDefSummary details of this instance's new TypeDef.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityReTyping(String         userId,
                                               String         metadataCollectionName,
                                               EntityDetail   instance,
                                               TypeDefSummary newTypeDefSummary) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForEntityReTyping(userId, metadataCollectionName, instance, newTypeDefSummary);
        }
    }


    /**
     * Tests for whether a specific user should have the right to change the home of an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newHomeMetadataCollectionId unique identifier for the new home metadata collection/repository.
     * @param newHomeMetadataCollectionName display name for the new home metadata collection/repository.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityReHoming(String         userId,
                                               String         metadataCollectionName,
                                               EntityDetail   instance,
                                               String         newHomeMetadataCollectionId,
                                               String         newHomeMetadataCollectionName) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForEntityReHoming(userId,
                                                    metadataCollectionName,
                                                    instance,
                                                    newHomeMetadataCollectionId,
                                                    newHomeMetadataCollectionName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to create an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param relationshipTypeGUID unique identifier (guid) for the new relationship's type.
     * @param initialProperties initial list of properties for the new entity null means no properties.
     * @param entityOneSummary the unique identifier of one of the entities that the relationship is connecting together.
     * @param entityTwoSummary the unique identifier of the other entity that the relationship is connecting together.
     * @param initialStatus initial status typically ACTIVE.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForRelationshipCreate(String               userId,
                                                   String               metadataCollectionName,
                                                   String               relationshipTypeGUID,
                                                   InstanceProperties   initialProperties,
                                                   EntitySummary        entityOneSummary,
                                                   EntitySummary        entityTwoSummary,
                                                   InstanceStatus       initialStatus) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForRelationshipCreate(userId,
                                                        metadataCollectionName,
                                                        relationshipTypeGUID,
                                                        initialProperties,
                                                        entityOneSummary,
                                                        entityTwoSummary,
                                                        initialStatus);
        }
    }


    /**
     * Tests for whether a specific user should have read access to a specific instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to retrieve instances
     */
    @Override
    public Relationship  validateUserForRelationshipRead(String          userId,
                                                         String          metadataCollectionName,
                                                         Relationship    instance) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            return repositorySecurityConnector.validateUserForRelationshipRead(userId, metadataCollectionName, new Relationship(instance));
        }

        return instance;
    }


    /**
     * Tests for whether a specific user should have the right to update an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForRelationshipUpdate(String          userId,
                                                   String          metadataCollectionName,
                                                   Relationship    instance) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForRelationshipUpdate(userId, metadataCollectionName, new Relationship(instance));
        }
    }


    /**
     * Tests for whether a specific user should have the right to delete an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForRelationshipDelete(String       userId,
                                                   String       metadataCollectionName,
                                                   Relationship instance) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForRelationshipDelete(userId, metadataCollectionName, new Relationship(instance));
        }
    }


    /**
     * Tests for whether a specific user should have the right to delete an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param deletedRelationshipGUID String unique identifier (guid) for the relationship.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForRelationshipRestore(String       userId,
                                                    String       metadataCollectionName,
                                                    String       deletedRelationshipGUID) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForEntityRestore(userId, metadataCollectionName, deletedRelationshipGUID);
        }
    }


    /**
     * Tests for whether a specific user should have the right to change the guid on an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newGUID the new guid for the instance.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForRelationshipReIdentification(String       userId,
                                                             String       metadataCollectionName,
                                                             Relationship instance,
                                                             String       newGUID) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForRelationshipReIdentification(userId, metadataCollectionName, instance, newGUID);
        }
    }


    /**
     * Tests for whether a specific user should have the right to change an instance's type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newTypeDefSummary details of this instance's new TypeDef.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForRelationshipReTyping(String         userId,
                                                     String         metadataCollectionName,
                                                     Relationship   instance,
                                                     TypeDefSummary newTypeDefSummary) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForRelationshipReTyping(userId, metadataCollectionName, instance, newTypeDefSummary);
        }
    }


    /**
     * Tests for whether a specific user should have the right to change the home of an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newHomeMetadataCollectionId unique identifier for the new home metadata collection/repository.
     * @param newHomeMetadataCollectionName display name for the new home metadata collection/repository.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForRelationshipReHoming(String         userId,
                                                     String         metadataCollectionName,
                                                     Relationship   instance,
                                                     String         newHomeMetadataCollectionId,
                                                     String         newHomeMetadataCollectionName) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForRelationshipReHoming(userId,
                                                          metadataCollectionName,
                                                          instance,
                                                          newHomeMetadataCollectionId,
                                                          newHomeMetadataCollectionName);
        }
    }


    /**
     * Tests for whether a reference copy should be saved to the repository.
     *
     * @param userId identifier of user
     * @param instance instance details
     * @return flag indicating whether the reference copy should be saved
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public boolean  validateEntityReferenceCopySave(String       userId,
                                                    EntityDetail instance) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            return repositorySecurityConnector.validateEntityReferenceCopySave(userId, instance);
        }

        return true;
    }


    /**
     * Tests for whether a reference copy should be saved to the repository.
     *
     * @param userId identifier of user
     * @param instance instance details
     * @return flag indicating whether the reference copy should be saved
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public boolean  validateRelationshipReferenceCopySave(String       userId,
                                                          Relationship instance) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            return repositorySecurityConnector.validateRelationshipReferenceCopySave(userId, instance);
        }

        return true;
    }


    /**
     * Validate whether an event received from another member of the cohort should be processed
     * by this server.
     *
     * @param cohortName name of the cohort
     * @param event event that has been received
     * @return inbound event to process (maybe updated) or null to indicate that the event should be ignored
     */
    public OMRSInstanceEvent validateInboundEvent(String            cohortName,
                                                  OMRSInstanceEvent event)
    {
        if (eventsSecurityConnector != null)
        {
            return eventsSecurityConnector.validateInboundEvent(cohortName, event);
        }

        return event;
    }


    /**
     * Validate whether an event should be sent to the other members of the cohort by this server.
     *
     * @param cohortName name of the cohort
     * @param event event that has been received
     * @return outbound event to send (maybe updated) or null to indicate that the event should be ignored
     */
    public OMRSInstanceEvent validateOutboundEvent(String            cohortName,
                                                   OMRSInstanceEvent event)
    {
        if (eventsSecurityConnector != null)
        {
            return eventsSecurityConnector.validateOutboundEvent(cohortName, event);
        }

        return event;
    }
}
