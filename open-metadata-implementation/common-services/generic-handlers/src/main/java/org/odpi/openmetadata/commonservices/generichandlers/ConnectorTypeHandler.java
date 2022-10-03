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

import java.util.ArrayList;
import java.util.Date;
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
    private String findConnectorType(String        userId,
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
                                                     guidParameterName,
                                                     OpenMetadataAPIMapper.CONNECTOR_TYPE_TYPE_NAME,
                                                     null,
                                                     null,
                                                     false,
                                                     false,
                                                     supportedZones,
                                                     null,
                                                     methodName) != null)
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
                                                             false,
                                                             false,
                                                             supportedZones,
                                                             null,
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
                                                             false,
                                                             false,
                                                             supportedZones,
                                                             null,
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
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param connectorType object to add
     * @param methodName calling method
     *
     * @return unique identifier of the connectorType in the repository.
     * @throws InvalidParameterException the connectorType bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    String saveConnectorType(String                 userId,
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
                                                connectorType.getSupportedAssetTypeName(),
                                                connectorType.getExpectedDataFormat(),
                                                connectorType.getConnectorProviderClassName(),
                                                connectorType.getConnectorFrameworkName(),
                                                connectorType.getConnectorInterfaceLanguage(),
                                                connectorType.getConnectorInterfaces(),
                                                connectorType.getTargetTechnologySource(),
                                                connectorType.getTargetTechnologyName(),
                                                connectorType.getTargetTechnologyInterfaces(),
                                                connectorType.getTargetTechnologyVersions(),
                                                connectorType.getRecognizedAdditionalProperties(),
                                                connectorType.getRecognizedSecuredProperties(),
                                                connectorType.getRecognizedConfigurationProperties(),
                                                connectorType.getAdditionalProperties(),
                                                null,
                                                null,
                                                null,
                                                null,
                                                new Date(),
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
                                         connectorType.getSupportedAssetTypeName(),
                                         connectorType.getExpectedDataFormat(),
                                         connectorType.getConnectorProviderClassName(),
                                         connectorType.getConnectorFrameworkName(),
                                         connectorType.getConnectorInterfaceLanguage(),
                                         connectorType.getConnectorInterfaces(),
                                         connectorType.getTargetTechnologySource(),
                                         connectorType.getTargetTechnologyName(),
                                         connectorType.getTargetTechnologyInterfaces(),
                                         connectorType.getTargetTechnologyVersions(),
                                         connectorType.getRecognizedAdditionalProperties(),
                                         connectorType.getRecognizedSecuredProperties(),
                                         connectorType.getRecognizedConfigurationProperties(),
                                         connectorType.getAdditionalProperties(),
                                         null,
                                         null,
                                         null,
                                         null,
                                         false,
                                         false,
                                         false,
                                         new Date(),
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
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param anchorGUID unique identifier of the anchor entity (or null if freestanding)
     * @param qualifiedName      unique name of the connectorType
     * @param displayName    human memorable name for the connectorType - does not need to be unique
     * @param description  (optional) description of the connectorType.  Setting a description, particularly in a public connectorType
     *                        makes the connectorType more valuable to other users and can act as an embryonic glossary term
     * @param supportedAssetTypeName the type of asset that the connector implementation supports
     * @param expectedDataFormat the format of the data that the connector supports - null for "any"
     * @param connectorProviderClassName class name of the connector provider
     * @param connectorFrameworkName name of the connector framework that the connector implements - default Open Connector Framework (OCF)
     * @param connectorInterfaceLanguage the language that the connector is implemented in - default Java
     * @param connectorInterfaces list of interfaces that the connector supports
     * @param targetTechnologySource the organization that supplies the target technology that the connector implementation connects to
     * @param targetTechnologyName the name of the target technology that the connector implementation connects to
     * @param targetTechnologyInterfaces the names of the interfaces in the target technology that the connector calls
     * @param targetTechnologyVersions the versions of the target technology that the connector supports
     * @param recognizedAdditionalProperties property name for additionalProperties in a linked Connection object.
     * @param recognizedSecuredProperties property name for securedProperties in a linked Connection object.
     * @param recognizedConfigurationProperties property name for configurationProperties in a linked Connection object.
     * @param additionalProperties name value pairs for values that are not formally defined in the type system
     * @param suppliedTypeName name of the subtype for the endpoint or null for standard type
     * @param extendedProperties any properties for a subtype
     * @param effectiveFrom          the date when this element is active - null for active now
     * @param effectiveTo            the date when this element becomes inactive - null for active until deleted
     * @param effectiveTime the time that the retrieved elements must be effective for
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
                                      String              supportedAssetTypeName,
                                      String              expectedDataFormat,
                                      String              connectorProviderClassName,
                                      String              connectorFrameworkName,
                                      String              connectorInterfaceLanguage,
                                      List<String>        connectorInterfaces,
                                      String              targetTechnologySource,
                                      String              targetTechnologyName,
                                      List<String>        targetTechnologyInterfaces,
                                      List<String>        targetTechnologyVersions,
                                      List<String>        recognizedAdditionalProperties,
                                      List<String>        recognizedSecuredProperties,
                                      List<String>        recognizedConfigurationProperties,
                                      Map<String, String> additionalProperties,
                                      String              suppliedTypeName,
                                      Map<String, Object> extendedProperties,
                                      Date                effectiveFrom,
                                      Date                effectiveTo,
                                      Date                effectiveTime,
                                      String              methodName) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        final String nameParameter = "qualifiedName";

        invalidParameterHandler.validateName(qualifiedName, nameParameter, methodName);

        String typeName = OpenMetadataAPIMapper.CONNECTOR_TYPE_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataAPIMapper.CONNECTOR_TYPE_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        ConnectorTypeBuilder builder = new ConnectorTypeBuilder(qualifiedName,
                                                                displayName,
                                                                description,
                                                                supportedAssetTypeName,
                                                                expectedDataFormat,
                                                                connectorProviderClassName,
                                                                connectorFrameworkName,
                                                                connectorInterfaceLanguage,
                                                                connectorInterfaces,
                                                                targetTechnologySource,
                                                                targetTechnologyName,
                                                                targetTechnologyInterfaces,
                                                                targetTechnologyVersions,
                                                                recognizedAdditionalProperties,
                                                                recognizedSecuredProperties,
                                                                recognizedConfigurationProperties,
                                                                additionalProperties,
                                                                typeGUID,
                                                                typeName,
                                                                extendedProperties,
                                                                repositoryHelper,
                                                                serviceName,
                                                                serverName);

        if (anchorGUID != null)
        {
            builder.setAnchors(userId, anchorGUID, methodName);
        }

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        return this.createBeanInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           typeGUID,
                                           typeName,
                                           builder,
                                           effectiveTime,
                                           methodName);
    }




    /**
     * Create a new metadata element to represent a connection using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param templateGUID unique identifier of the metadata element to copy
     * @param qualifiedName unique name for the element - used in other configuration
     * @param displayName short display name for the new element
     * @param description description of the new element
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createConnectorTypeFromTemplate(String userId,
                                                  String externalSourceGUID,
                                                  String externalSourceName,
                                                  String templateGUID,
                                                  String qualifiedName,
                                                  String displayName,
                                                  String description,
                                                  String methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String templateGUIDParameterName   = "templateGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        ConnectorTypeBuilder builder = new ConnectorTypeBuilder(qualifiedName,
                                                                displayName,
                                                                description,
                                                                repositoryHelper,
                                                                serviceName,
                                                                serverName);

        return this.createBeanFromTemplate(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           templateGUID,
                                           templateGUIDParameterName,
                                           OpenMetadataAPIMapper.CONNECTOR_TYPE_TYPE_GUID,
                                           OpenMetadataAPIMapper.CONNECTOR_TYPE_TYPE_NAME,
                                           qualifiedName,
                                           OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                           builder,
                                           supportedZones,
                                           methodName);
    }


    /**
     * Retrieves the connector type for the named asset type and if found, returns its unique identifier.
     *
     * @param userId           userId of user making request
     * @param supportedAssetTypeName the type of asset that the connector implementation supports
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return GUID for new connectorType
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the connectorType properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String getConnectorTypeForAsset(String  userId,
                                           String  supportedAssetTypeName,
                                           boolean forLineage,
                                           boolean forDuplicateProcessing,
                                           Date    effectiveTime,
                                           String  methodName) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        String parameterName = "supportedAssetTypeName";

        List<String> specificMatchPropertyNames = new ArrayList<>();

        specificMatchPropertyNames.add(OpenMetadataAPIMapper.SUPPORTED_ASSET_TYPE_NAME);

        List<EntityDetail> connectorTypes = this.getEntitiesByValue(userId,
                                                                    supportedAssetTypeName,
                                                                    parameterName,
                                                                    OpenMetadataAPIMapper.CONNECTOR_TYPE_TYPE_GUID,
                                                                    OpenMetadataAPIMapper.CONNECTOR_TYPE_TYPE_NAME,
                                                                    specificMatchPropertyNames,
                                                                    true,
                                                                    null,
                                                                    null,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    supportedZones,
                                                                    null,
                                                                    0,
                                                                    invalidParameterHandler.getMaxPagingSize(),
                                                                    effectiveTime,
                                                                    methodName);


        String otherConnectorTypeGUID = null;

        if (connectorTypes != null)
        {
            /*
             * Search for a connector type that supports the OCF framework and ideally is in Java.  When one is found return immediately.
             * If no OCF connector in Java is available, an OCF connector that is not in Java is returned. Notice that the OCF is the
             * default framework and Java is the default language so nulls in these fields is treated as a match.
             */
            for (EntityDetail connectorType : connectorTypes)
            {
                if (connectorType != null)
                {
                    String framework = repositoryHelper.getStringProperty(serviceName,
                                                                          OpenMetadataAPIMapper.CONNECTOR_FRAMEWORK_NAME,
                                                                          connectorType.getProperties(),
                                                                          methodName);
                    String language  = repositoryHelper.getStringProperty(serviceName,
                                                                          OpenMetadataAPIMapper.CONNECTOR_INTERFACE_LANGUAGE,
                                                                          connectorType.getProperties(),
                                                                          methodName);

                    if ((framework == null) || (OpenMetadataAPIMapper.CONNECTOR_FRAMEWORK_NAME_DEFAULT.equals(framework)))
                    {
                        if ((language == null) || (OpenMetadataAPIMapper.CONNECTOR_INTERFACE_LANGUAGE_DEFAULT.equals(language)))
                        {
                            return connectorType.getGUID();
                        }
                        else
                        {
                            otherConnectorTypeGUID = connectorType.getGUID();
                        }
                    }
                }
            }
        }

        return otherConnectorTypeGUID;
    }


    /**
     * Retrieves the connector type for the qualified name and if found, returns its unique identifier.
     * Otherwise, it creates a new connectorType and returns the unique identifier for it.
     *
     * @param userId           userId of user making request
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param anchorGUID unique identifier for the anchor of this connector type (or null if freestanding)
     * @param qualifiedName      unique name of the connectorType
     * @param displayName    human memorable name for the connectorType - does not need to be unique
     * @param description  (optional) description of the connectorType.  Setting a description, particularly in a public connectorType
     *                        makes the connectorType more valuable to other users and can act as an embryonic glossary term
     * @param supportedAssetTypeName the type of asset that the connector implementation supports
     * @param expectedDataFormat the format of the data that the connector supports - null for "any"
     * @param connectorProviderClassName class name of the connector provider
     * @param connectorFrameworkName name of the connector framework that the connector implements - default Open Connector Framework (OCF)
     * @param connectorInterfaceLanguage the language that the connector is implemented in - default Java
     * @param connectorInterfaces list of interfaces that the connector supports
     * @param targetTechnologySource the organization that supplies the target technology that the connector implementation connects to
     * @param targetTechnologyName the name of the target technology that the connector implementation connects to
     * @param targetTechnologyInterfaces the names of the interfaces in the target technology that the connector calls
     * @param targetTechnologyVersions the versions of the target technology that the connector supports
     * @param recognizedAdditionalProperties property name for additionalProperties in a linked Connection object.
     * @param recognizedSecuredProperties property name for securedProperties in a linked Connection object.
     * @param recognizedConfigurationProperties property name for configurationProperties in a linked Connection object.
     * @param additionalProperties name value pairs for values that are not formally defined in the type system
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
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
                                                String              supportedAssetTypeName,
                                                String              expectedDataFormat,
                                                String              connectorProviderClassName,
                                                String              connectorFrameworkName,
                                                String              connectorInterfaceLanguage,
                                                List<String>        connectorInterfaces,
                                                String              targetTechnologySource,
                                                String              targetTechnologyName,
                                                List<String>        targetTechnologyInterfaces,
                                                List<String>        targetTechnologyVersions,
                                                List<String>        recognizedAdditionalProperties,
                                                List<String>        recognizedSecuredProperties,
                                                List<String>        recognizedConfigurationProperties,
                                                Map<String, String> additionalProperties,
                                                boolean             forLineage,
                                                boolean             forDuplicateProcessing,
                                                Date                effectiveTime,
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
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                supportedZones,
                                                                effectiveTime,
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
                                                         supportedAssetTypeName,
                                                         expectedDataFormat,
                                                         connectorProviderClassName,
                                                         connectorFrameworkName,
                                                         connectorInterfaceLanguage,
                                                         connectorInterfaces,
                                                         targetTechnologySource,
                                                         targetTechnologyName,
                                                         targetTechnologyInterfaces,
                                                         targetTechnologyVersions,
                                                         recognizedAdditionalProperties,
                                                         recognizedSecuredProperties,
                                                         recognizedConfigurationProperties,
                                                         additionalProperties,
                                                         null,
                                                         null,
                                                         null,
                                                         null,
                                                         effectiveTime,
                                                         methodName);
        }

        return connectorTypeGUID;
    }


    /**
     * Updates the properties of an existing connectorType.
     *
     * @param userId          userId of user making request.
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param connectorTypeGUID         unique identifier for the connectorType
     * @param connectorTypeGUIDParameterName parameter providing connectorTypeGUID
     * @param qualifiedName      unique name of the connectorType
     * @param displayName    human memorable name for the connectorType - does not need to be unique
     * @param description  (optional) description of the connectorType.  Setting a description, particularly in a public connectorType
     *                        makes the connectorType more valuable to other users and can act as an embryonic glossary term
     * @param supportedAssetTypeName the type of asset that the connector implementation supports
     * @param expectedDataFormat the format of the data that the connector supports - null for "any"
     * @param connectorProviderClassName class name of the connector provider
     * @param connectorFrameworkName name of the connector framework that the connector implements - default Open Connector Framework (OCF)
     * @param connectorInterfaceLanguage the language that the connector is implemented in - default Java
     * @param connectorInterfaces list of interfaces that the connector supports
     * @param targetTechnologySource the organization that supplies the target technology that the connector implementation connects to
     * @param targetTechnologyName the name of the target technology that the connector implementation connects to
     * @param targetTechnologyInterfaces the names of the interfaces in the target technology that the connector calls
     * @param targetTechnologyVersions the versions of the target technology that the connector supports
     * @param recognizedAdditionalProperties property name for additionalProperties in a linked Connection object.
     * @param recognizedSecuredProperties property name for securedProperties in a linked Connection object.
     * @param recognizedConfigurationProperties property name for configurationProperties in a linked Connection object.
     * @param additionalProperties name value pairs for values that are not formally defined in the type system
     * @param suppliedTypeName name of the subtype for the endpoint or null for standard type
     * @param extendedProperties any properties for a subtype
     * @param effectiveFrom          the date when this element is active - null for active now
     * @param effectiveTo            the date when this element becomes inactive - null for active until deleted
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
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
                                      String              supportedAssetTypeName,
                                      String              expectedDataFormat,
                                      String              connectorProviderClassName,
                                      String              connectorFrameworkName,
                                      String              connectorInterfaceLanguage,
                                      List<String>        connectorInterfaces,
                                      String              targetTechnologySource,
                                      String              targetTechnologyName,
                                      List<String>        targetTechnologyInterfaces,
                                      List<String>        targetTechnologyVersions,
                                      List<String>        recognizedAdditionalProperties,
                                      List<String>        recognizedSecuredProperties,
                                      List<String>        recognizedConfigurationProperties,
                                      Map<String, String> additionalProperties,
                                      String              suppliedTypeName,
                                      Map<String, Object> extendedProperties,
                                      Date                effectiveFrom,
                                      Date                effectiveTo,
                                      boolean             isMergeUpdate,
                                      boolean             forLineage,
                                      boolean             forDuplicateProcessing,
                                      Date                effectiveTime,
                                      String              methodName) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        final String nameParameter = "qualifiedName";

        invalidParameterHandler.validateName(qualifiedName, nameParameter, methodName);

        String typeName = OpenMetadataAPIMapper.CONNECTOR_TYPE_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataAPIMapper.CONNECTOR_TYPE_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        ConnectorTypeBuilder builder = new ConnectorTypeBuilder(qualifiedName,
                                                                displayName,
                                                                description,
                                                                supportedAssetTypeName,
                                                                expectedDataFormat,
                                                                connectorProviderClassName,
                                                                connectorFrameworkName,
                                                                connectorInterfaceLanguage,
                                                                connectorInterfaces,
                                                                targetTechnologySource,
                                                                targetTechnologyName,
                                                                targetTechnologyInterfaces,
                                                                targetTechnologyVersions,
                                                                recognizedAdditionalProperties,
                                                                recognizedSecuredProperties,
                                                                recognizedConfigurationProperties,
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
                                    connectorTypeGUID,
                                    connectorTypeGUIDParameterName,
                                    typeGUID,
                                    typeName,
                                    forLineage,
                                    forDuplicateProcessing,
                                    supportedZones,
                                    builder.getInstanceProperties(methodName),
                                    isMergeUpdate,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Remove the metadata element.  This will delete all elements anchored to it.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param guid unique identifier of the metadata element to remove
     * @param guidParameterName parameter supplying the guid
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeConnectorType(String  userId,
                                    String  externalSourceGUID,
                                    String  externalSourceName,
                                    String  guid,
                                    String  guidParameterName,
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
                                    guid,
                                    guidParameterName,
                                    OpenMetadataAPIMapper.CONNECTOR_TYPE_TYPE_GUID,
                                    OpenMetadataAPIMapper.CONNECTOR_TYPE_TYPE_NAME,
                                    null,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Retrieve the list of metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param searchStringParameterName name of parameter supplying the search string
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> findConnectorTypes(String  userId,
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
                              OpenMetadataAPIMapper.CONNECTOR_TYPE_TYPE_GUID,
                              OpenMetadataAPIMapper.CONNECTOR_TYPE_TYPE_NAME,
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
     * Retrieve the list of metadata elements with a matching qualified name, display name or connector provider class name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param nameParameterName parameter supplying name
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> getConnectorTypesByName(String  userId,
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
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME);
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.CONNECTOR_PROVIDER_PROPERTY_NAME);

        return this.getBeansByValue(userId,
                                    name,
                                    nameParameterName,
                                    OpenMetadataAPIMapper.CONNECTOR_TYPE_TYPE_GUID,
                                    OpenMetadataAPIMapper.CONNECTOR_TYPE_TYPE_NAME,
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
     * Retrieve the metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     * @param guidParameterName parameter name of guid
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public B getConnectorTypeByGUID(String  userId,
                                    String  guid,
                                    String  guidParameterName,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing,
                                    Date    effectiveTime,
                                    String  methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        return this.getBeanFromRepository(userId,
                                          guid,
                                          guidParameterName,
                                          OpenMetadataAPIMapper.CONNECTOR_TYPE_TYPE_NAME,
                                          forLineage,
                                          forDuplicateProcessing,
                                          supportedZones,
                                          effectiveTime,
                                          methodName);

    }
}