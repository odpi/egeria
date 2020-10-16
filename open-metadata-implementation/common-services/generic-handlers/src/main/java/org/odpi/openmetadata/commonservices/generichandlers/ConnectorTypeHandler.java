/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;
import java.util.Map;

/**
 * ConnectorTypeHandler manages ConnectorType objects.  These describe the information necessary to construct a connector.  They are used by
 * connection objects to describe the connector provider for the connector.
 *
 * ConnectorTypeHandler runs server-side in the OMAG Server Platform and retrieves ConnectorType entities through the OMRSRepositoryConnector via the
 * generic handler and repository handler.
 */
public class ConnectorTypeHandler<B> extends ReferenceableHandler<B>
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
    public ConnectorTypeHandler(OpenMetadataAPIGenericConverter<B> converter,
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
     * Find out if the connection type is already stored in the repository.
     *
     * @param userId calling user
     * @param connectorType connectorType to find
     * @param methodName calling method
     *
     * @return unique identifier of the connectorType or null
     *
     * @throws InvalidParameterException the connectorType bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    String findConnectorType(String        userId,
                             ConnectorType connectorType,
                             String        methodName) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        final String guidParameterName      = "connectorType.getGUID";
        final String qualifiedNameParameter = "connectorType.getQualifiedName";
        final String displayNameParameter   = "connectorType.getDisplayName";

        if (connectorType != null)
        {
            if (connectorType.getGUID() != null)
            {
                try
                {
                    if (this.getEntityFromRepository(userId,
                                                     connectorType.getGUID(),
                                                     OpenMetadataAPIMapper.CONNECTOR_TYPE_TYPE_NAME,
                                                     methodName,
                                                     guidParameterName) != null)
                    {
                        return connectorType.getGUID();
                    }
                }
                catch (InvalidParameterException notFound)
                {
                    /*
                     * Keep searching ...
                     */
                }
            }

            String retrievedGUID = null;

            if (connectorType.getQualifiedName() != null)
            {
                retrievedGUID = this.getBeanGUIDByUniqueName(userId,
                                                             connectorType.getQualifiedName(),
                                                             qualifiedNameParameter,
                                                             OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                             OpenMetadataAPIMapper.CONNECTOR_TYPE_TYPE_GUID,
                                                             OpenMetadataAPIMapper.CONNECTOR_TYPE_TYPE_NAME,
                                                             supportedZones,
                                                             methodName);
            }

            if ((retrievedGUID == null) && (connectorType.getDisplayName() != null))
            {
                retrievedGUID = this.getBeanGUIDByUniqueName(userId,
                                                             connectorType.getDisplayName(),
                                                             displayNameParameter,
                                                             OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                             OpenMetadataAPIMapper.CONNECTOR_TYPE_TYPE_GUID,
                                                             OpenMetadataAPIMapper.CONNECTOR_TYPE_TYPE_NAME,
                                                             supportedZones,
                                                             methodName);
            }

            return retrievedGUID;
        }

        return null;
    }


    /**
     * Verify that the ConnectorType object is stored in the repository and create it if it is not.
     * If the connectorType is located, there is no check that the connectorType values are equal to those in
     * the supplied connectorType object.
     *
     * @param userId calling userId
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param connectorType object to add
     * @param methodName calling method
     *
     * @return unique identifier of the connectorType in the repository.
     * @throws InvalidParameterException the connectorType bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String saveConnectorType(String                 userId,
                                    String                 externalSourceGUID,
                                    String                 externalSourceName,
                                    ConnectorType          connectorType,
                                    String                 methodName) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        if (connectorType != null)
        {
            String existingConnectorType = this.findConnectorType(userId, connectorType, methodName);

            if (existingConnectorType == null)
            {
                return this.createConnectorType(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                null,
                                                connectorType.getQualifiedName(),
                                                connectorType.getDisplayName(),
                                                connectorType.getDescription(),
                                                connectorType.getConnectorProviderClassName(),
                                                connectorType.getRecognizedAdditionalProperties(),
                                                connectorType.getRecognizedSecuredProperties(),
                                                connectorType.getRecognizedConfigurationProperties(),
                                                connectorType.getAdditionalProperties(),
                                                methodName);
            }
            else
            {
                final String connectorTypeGUIDParameterName = "connectorType.getGUID";

                this.updateConnectorType(userId,
                                         externalSourceGUID,
                                         externalSourceName,
                                         existingConnectorType,
                                         connectorTypeGUIDParameterName,
                                         connectorType.getQualifiedName(),
                                         connectorType.getDisplayName(),
                                         connectorType.getDescription(),
                                         connectorType.getConnectorProviderClassName(),
                                         connectorType.getRecognizedAdditionalProperties(),
                                         connectorType.getRecognizedSecuredProperties(),
                                         connectorType.getRecognizedConfigurationProperties(),
                                         connectorType.getAdditionalProperties(),
                                         methodName);

                return  existingConnectorType;
            }
        }

        return null;
    }


    /**
     * Creates a new connectorType and returns the unique identifier for it.
     *
     * @param userId           userId of user making request
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param anchorGUID unique identifier of the anchor entity (or null if free-standing)
     * @param qualifiedName      unique name of the connectorType
     * @param displayName    human memorable name for the connectorType - does not need to be unique
     * @param description  (optional) description of the connectorType.  Setting a description, particularly in a public connectorType
     *                        makes the connectorType more valuable to other users and can act as an embryonic glossary term
     * @param connectorProviderClassName class name of the connector provider.
     * @param recognizedAdditionalProperties property name for additionalProperties in a linked Connection object.
     * @param recognizedSecuredProperties property name for securedProperties in a linked Connection object.
     * @param recognizedConfigurationProperties property name for configurationProperties in a linked Connection object.
     * @param additionalProperties name value pairs for values that are not formally defined in the type system
     * @param methodName calling method
     *
     * @return GUID for new connectorType
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the connectorType properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createConnectorType(String              userId,
                                      String              externalSourceGUID,
                                      String              externalSourceName,
                                      String              anchorGUID,
                                      String              qualifiedName,
                                      String              displayName,
                                      String              description,
                                      String              connectorProviderClassName,
                                      List<String>        recognizedAdditionalProperties,
                                      List<String>        recognizedSecuredProperties,
                                      List<String>        recognizedConfigurationProperties,
                                      Map<String, String> additionalProperties,
                                      String              methodName) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        final String nameParameter = "qualifiedName";

        invalidParameterHandler.validateName(qualifiedName, nameParameter, methodName);

        ConnectorTypeBuilder builder = new ConnectorTypeBuilder(qualifiedName,
                                                                displayName,
                                                                description,
                                                                connectorProviderClassName,
                                                                recognizedAdditionalProperties,
                                                                recognizedSecuredProperties,
                                                                recognizedConfigurationProperties,
                                                                additionalProperties,
                                                                OpenMetadataAPIMapper.CONNECTOR_TYPE_TYPE_GUID,
                                                                OpenMetadataAPIMapper.CONNECTOR_TYPE_TYPE_NAME,
                                                                null,
                                                                repositoryHelper,
                                                                serviceName,
                                                                serverName);

        if (anchorGUID != null)
        {
            builder.setAnchors(userId, anchorGUID, methodName);
        }

        return this.createBeanInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           OpenMetadataAPIMapper.CONNECTOR_TYPE_TYPE_GUID,
                                           OpenMetadataAPIMapper.CONNECTOR_TYPE_TYPE_NAME,
                                           qualifiedName,
                                           OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                           builder,
                                           methodName);
    }


    /**
     * Retrieves the connector type for the qualified name and if found, returns its unique identifier.
     * Otherwise, it creates a new connectorType and returns the unique identifier for it.
     *
     * @param userId           userId of user making request
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param anchorGUID unique identifier for the anchor of this connector type (or null if free standing)
     * @param qualifiedName      unique name of the connectorType
     * @param displayName    human memorable name for the connectorType - does not need to be unique
     * @param description  (optional) description of the connectorType.  Setting a description, particularly in a public connectorType
     *                        makes the connectorType more valuable to other users and can act as an embryonic glossary term
     * @param connectorProviderClassName class name of the connector provider.
     * @param recognizedAdditionalProperties property name for additionalProperties in a linked Connection object.
     * @param recognizedSecuredProperties property name for securedProperties in a linked Connection object.
     * @param recognizedConfigurationProperties property name for configurationProperties in a linked Connection object.
     * @param additionalProperties name value pairs for values that are not formally defined in the type system
     * @param methodName calling method
     *
     * @return GUID for new connectorType
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the connectorType properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String getConnectorTypeForConnection(String              userId,
                                                String              externalSourceGUID,
                                                String              externalSourceName,
                                                String              anchorGUID,
                                                String              qualifiedName,
                                                String              displayName,
                                                String              description,
                                                String              connectorProviderClassName,
                                                List<String>        recognizedAdditionalProperties,
                                                List<String>        recognizedSecuredProperties,
                                                List<String>        recognizedConfigurationProperties,
                                                Map<String, String> additionalProperties,
                                                String              methodName) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        String qualifiedNameParameterName = "qualifiedName";

        String connectorTypeGUID = this.getBeanGUIDByUniqueName(userId,
                                                                qualifiedName,
                                                                qualifiedNameParameterName,
                                                                OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                OpenMetadataAPIMapper.CONNECTOR_TYPE_TYPE_GUID,
                                                                OpenMetadataAPIMapper.CONNECTOR_TYPE_TYPE_NAME,
                                                                supportedZones,
                                                                methodName);

        if (connectorTypeGUID == null)
        {
            connectorTypeGUID = this.createConnectorType(userId,
                                                         externalSourceGUID,
                                                         externalSourceName,
                                                         anchorGUID,
                                                         qualifiedName,
                                                         displayName,
                                                         description,
                                                         connectorProviderClassName,
                                                         recognizedAdditionalProperties,
                                                         recognizedSecuredProperties,
                                                         recognizedConfigurationProperties,
                                                         additionalProperties,
                                                         methodName);
        }

        return connectorTypeGUID;
    }


    /**
     * Updates the properties of an existing connectorType.
     *
     * @param userId          userId of user making request.
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param connectorTypeGUID         unique identifier for the connectorType
     * @param connectorTypeGUIDParameterName parameter providing connectorTypeGUID
     * @param qualifiedName      unique name of the connectorType
     * @param displayName    human memorable name for the connectorType - does not need to be unique
     * @param description  (optional) description of the connectorType.  Setting a description, particularly in a public connectorType
     *                        makes the connectorType more valuable to other users and can act as an embryonic glossary term
     * @param connectorProviderClassName class name of the connector provider.
     * @param recognizedAdditionalProperties property name for additionalProperties in a linked Connection object.
     * @param recognizedSecuredProperties property name for securedProperties in a linked Connection object.
     * @param recognizedConfigurationProperties property name for configurationProperties in a linked Connection object.
     * @param additionalProperties name value pairs for values that are not formally defined in the type system
     * @param methodName      calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the connectorType properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   updateConnectorType(String              userId,
                                      String              externalSourceGUID,
                                      String              externalSourceName,
                                      String              connectorTypeGUID,
                                      String              connectorTypeGUIDParameterName,
                                      String              qualifiedName,
                                      String              displayName,
                                      String              description,
                                      String              connectorProviderClassName,
                                      List<String>        recognizedAdditionalProperties,
                                      List<String>        recognizedSecuredProperties,
                                      List<String>        recognizedConfigurationProperties,
                                      Map<String, String> additionalProperties,
                                      String              methodName) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        final String nameParameter = "qualifiedName";

        invalidParameterHandler.validateName(qualifiedName, nameParameter, methodName);

        ConnectorTypeBuilder builder = new ConnectorTypeBuilder(qualifiedName,
                                                                displayName,
                                                                description,
                                                                connectorProviderClassName,
                                                                recognizedAdditionalProperties,
                                                                recognizedSecuredProperties,
                                                                recognizedConfigurationProperties,
                                                                additionalProperties,
                                                                OpenMetadataAPIMapper.CONNECTOR_TYPE_TYPE_GUID,
                                                                OpenMetadataAPIMapper.CONNECTOR_TYPE_TYPE_NAME,
                                                                null,
                                                                repositoryHelper,
                                                                serviceName,
                                                                serverName);

        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    connectorTypeGUID,
                                    connectorTypeGUIDParameterName,
                                    OpenMetadataAPIMapper.CONNECTOR_TYPE_TYPE_GUID,
                                    OpenMetadataAPIMapper.CONNECTOR_TYPE_TYPE_NAME,
                                    supportedZones,
                                    builder.getInstanceProperties(methodName),
                                    true,
                                    methodName);
    }


    /**
     * Count the number of informal connectorTypes attached to a supplied entity.
     *
     * @param userId     calling user
     * @param elementGUID identifier for the entity that the object is attached to
     * @param methodName calling method
     * @return count of attached objects
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public int countConnectorTypes(String userId,
                                   String elementGUID,
                                   String methodName) throws InvalidParameterException,
                                                             PropertyServerException,
                                                             UserNotAuthorizedException
    {
        return this.countAttachments(userId,
                                     elementGUID,
                                     OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                     OpenMetadataAPIMapper.CONNECTION_CONNECTOR_TYPE_TYPE_GUID,
                                     OpenMetadataAPIMapper.CONNECTION_CONNECTOR_TYPE_TYPE_NAME,
                                     methodName);
    }
}