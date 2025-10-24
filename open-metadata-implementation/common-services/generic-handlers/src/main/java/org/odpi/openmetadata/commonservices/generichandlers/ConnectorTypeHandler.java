/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.connectors.mapper.OpenConnectorsValidValues;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ConnectorTypeHandler manages ConnectorType objects.  These describe the information necessary to construct a connector.  They are used by
 * connection objects to describe the connector provider for the connector.
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
                                                     OpenMetadataType.CONNECTOR_TYPE.typeName,
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
                                                             OpenMetadataProperty.QUALIFIED_NAME.name,
                                                             OpenMetadataType.CONNECTOR_TYPE.typeGUID,
                                                             OpenMetadataType.CONNECTOR_TYPE.typeName,
                                                             null,
                                                             null,
                                                             SequencingOrder.CREATION_DATE_RECENT,
                                                             null,
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
                                                             OpenMetadataProperty.DISPLAY_NAME.name,
                                                             OpenMetadataType.CONNECTOR_TYPE.typeGUID,
                                                             OpenMetadataType.CONNECTOR_TYPE.typeName,
                                                             null,
                                                             null,
                                                             SequencingOrder.CREATION_DATE_RECENT,
                                                             null,
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
     * @param parentQualifiedName qualified name of parent object (typically connection)
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
                             String                 parentQualifiedName,
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
                String connectorTypeQualifiedName = connectorType.getQualifiedName();

                if (connectorTypeQualifiedName == null)
                {
                    connectorTypeQualifiedName = parentQualifiedName + "-ConnectorType";
                }

                return this.createConnectorType(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                null,
                                                connectorTypeQualifiedName,
                                                connectorType.getDisplayName(),
                                                connectorType.getDescription(),
                                                connectorType.getSupportedAssetTypeName(),
                                                connectorType.getSupportedDeployedImplementationType(),
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
                                         connectorType.getSupportedDeployedImplementationType(),
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
                                         true,
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
     * @param supportedDeployedImplementationType the deployed implementation type that the connector implementation supports
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
                                      String              supportedDeployedImplementationType,
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
        final String nameParameter = "connectorType.qualifiedName";
        final String anchorGUIDParameter = "anchorGUID";

        invalidParameterHandler.validateName(qualifiedName, nameParameter, methodName);

        String typeName = OpenMetadataType.CONNECTOR_TYPE.typeName;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.CONNECTOR_TYPE.typeName,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        ConnectorTypeBuilder builder = new ConnectorTypeBuilder(qualifiedName,
                                                                displayName,
                                                                description,
                                                                supportedAssetTypeName,
                                                                supportedDeployedImplementationType,
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

        this.addAnchorGUIDToBuilder(userId,
                                    anchorGUID,
                                    anchorGUIDParameter,
                                    false,
                                    false,
                                    effectiveTime,
                                    supportedZones,
                                    builder,
                                    methodName);

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

        specificMatchPropertyNames.add(OpenMetadataProperty.SUPPORTED_ASSET_TYPE_NAME.name);

        List<EntityDetail> connectorTypes = this.getEntitiesByValue(userId,
                                                                    supportedAssetTypeName,
                                                                    parameterName,
                                                                    OpenMetadataType.CONNECTOR_TYPE.typeGUID,
                                                                    OpenMetadataType.CONNECTOR_TYPE.typeName,
                                                                    specificMatchPropertyNames,
                                                                    true,
                                                                    false,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    SequencingOrder.CREATION_DATE_RECENT,
                                                                    null,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    supportedZones,
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
                                                                          OpenMetadataProperty.CONNECTOR_FRAMEWORK_NAME.name,
                                                                          connectorType.getProperties(),
                                                                          methodName);
                    String language  = repositoryHelper.getStringProperty(serviceName,
                                                                          OpenMetadataProperty.CONNECTOR_INTERFACE_LANGUAGE.name,
                                                                          connectorType.getProperties(),
                                                                          methodName);

                    if ((framework == null) || (OpenConnectorsValidValues.CONNECTOR_FRAMEWORK_DEFAULT.equals(framework)))
                    {
                        if ((language == null) || (OpenConnectorsValidValues.CONNECTOR_INTERFACE_LANGUAGE_DEFAULT.equals(language)))
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
     * @param supportedDeployedImplementationType type of technology
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
                                                String              supportedDeployedImplementationType,
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
        final String qualifiedNameParameterName = "qualifiedName";
        final String connectorProviderClassNameParameterName = "connectorProviderClassName";

        String connectorTypeGUID = this.getBeanGUIDByUniqueName(userId,
                                                                connectorProviderClassName,
                                                                connectorProviderClassNameParameterName,
                                                                OpenMetadataProperty.CONNECTOR_PROVIDER_CLASS_NAME.name,
                                                                OpenMetadataType.CONNECTOR_TYPE.typeGUID,
                                                                OpenMetadataType.CONNECTOR_TYPE.typeName,
                                                                null,
                                                                null,
                                                                SequencingOrder.CREATION_DATE_RECENT,
                                                                null,
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                supportedZones,
                                                                effectiveTime,
                                                                methodName);

        if (connectorTypeGUID == null)
        {
            connectorTypeGUID = this.getBeanGUIDByUniqueName(userId,
                                                             qualifiedName,
                                                             qualifiedNameParameterName,
                                                             OpenMetadataProperty.QUALIFIED_NAME.name,
                                                             OpenMetadataType.CONNECTOR_TYPE.typeGUID,
                                                             OpenMetadataType.CONNECTOR_TYPE.typeName,
                                                             null,
                                                             null,
                                                             SequencingOrder.CREATION_DATE_RECENT,
                                                             null,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             supportedZones,
                                                             effectiveTime,
                                                             methodName);
        }

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
                                                         supportedDeployedImplementationType,
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
     * @param supportedDeployedImplementationType the deployed implementation type that the connector implementation supports
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
                                      String              supportedDeployedImplementationType,
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

        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(qualifiedName, nameParameter, methodName);
        }

        String typeName = OpenMetadataType.CONNECTOR_TYPE.typeName;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.CONNECTOR_TYPE.typeName,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        ConnectorTypeBuilder builder = new ConnectorTypeBuilder(qualifiedName,
                                                                displayName,
                                                                description,
                                                                supportedAssetTypeName,
                                                                supportedDeployedImplementationType,
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
}
