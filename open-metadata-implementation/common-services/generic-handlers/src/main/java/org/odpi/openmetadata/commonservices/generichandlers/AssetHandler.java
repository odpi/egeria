/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryEntitiesIterator;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryRelationshipsIterator;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AssetHandler manages B objects and optionally connections in the property server.  It runs server-side in
 * the OMAG Server Platform and retrieves Assets and Connections through the OMRSRepositoryConnector.
 *
 * @param <B> class that represents the asset
 */
public class AssetHandler<B> extends ReferenceableHandler<B>
{
    private final ConnectionHandler<OpenMetadataAPIDummyBean> connectionHandler;

    /**
     * Construct the handler with information needed to work with B objects.
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
     * @param supportedZones list of zones that the access service is allowed to serve B instances from.
     * @param defaultZones list of zones that the access service should set in all new B instances.
     * @param publishZones list of zones that the access service sets up in published B instances.
     * @param auditLog destination for audit log events.
     */
    public AssetHandler(OpenMetadataAPIGenericConverter<B> converter,
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

        this.connectionHandler = new ConnectionHandler<>(new OpenMetadataAPIDummyBeanConverter<>(repositoryHelper, serviceName, serverName),
                                                         OpenMetadataAPIDummyBean.class,
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
     * Return the list of asset subtype names.
     *
     * @return list of type names that are subtypes of asset
     */
    public List<String> getTypesOfAssetList()
    {
        return repositoryHelper.getSubTypesOf(serviceName, OpenMetadataAPIMapper.ASSET_TYPE_NAME);
    }


    /**
     * Return the list of asset subtype names mapped to their descriptions.
     *
     * @return list of type names that are subtypes of asset
     */
    public Map<String, String> getTypesOfAssetDescriptions()
    {
        List<String>        assetTypeList = repositoryHelper.getSubTypesOf(serviceName, OpenMetadataAPIMapper.ASSET_TYPE_NAME);
        Map<String, String> assetDescriptions = new HashMap<>();

        if (assetTypeList != null)
        {
            for (String  assetTypeName : assetTypeList)
            {
                if (assetTypeName != null)
                {
                    TypeDef assetTypeDef = repositoryHelper.getTypeDefByName(serviceName, assetTypeName);

                    if (assetTypeDef != null)
                    {
                        assetDescriptions.put(assetTypeName, assetTypeDef.getDescription());
                    }
                }
            }

        }

        if (assetDescriptions.isEmpty())
        {
            return null;
        }

        return assetDescriptions;
    }


    /**
     * The externalSource identifier is supplied on the APIs that supply external source identifiers for two purposes.
     * The first is the standard mechanism to control the ownership/provenance of the resulting elements.
     * The second is to enable a relationship between an asset and the software capability to be created.
     * The externalSourceIsHome boolean determines whether the identifier is used to control ownership or not.
     * The relationship is set up if the externalSourceGUID is not null.
     *
     * @param externalSourceIsHome use the external source GUID as the owner of this element
     * @param externalSourceID supplied external source unique identifier/name
     * @return externalSource
     */
    public String getExternalSourceID(boolean externalSourceIsHome,
                                      String  externalSourceID)
    {
        if (externalSourceIsHome)
        {
            return externalSourceID;
        }

        return null;
    }


    /**
     * Add or replace the governance measurements results dataset classification to a dataset.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this project
     * @param externalSourceName unique name of the software capability that owns this project
     * @param beanGUID unique identifier of bean
     * @param beanGUIDParameterName name of parameter supplying the beanGUID
     * @param beanGUIDTypeName type of bean
     * @param description of the  data set
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveFrom  the time that the relationship element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the relationship must be effective to (null for any time, new Date() for now)
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addGovernanceMeasurementsDataSetClassification(String                userId,
                                                                String                externalSourceGUID,
                                                                String                externalSourceName,
                                                                String                beanGUID,
                                                                String                beanGUIDParameterName,
                                                                String                beanGUIDTypeName,
                                                                String                description,
                                                                boolean               forLineage,
                                                                boolean               forDuplicateProcessing,
                                                                Date                  effectiveFrom,
                                                                Date                  effectiveTo,
                                                                Date                  effectiveTime,
                                                                String                methodName) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName, null, OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);

        this.setClassificationInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           beanGUID,
                                           beanGUIDParameterName,
                                           beanGUIDTypeName,
                                           OpenMetadataAPIMapper.GOVERNANCE_MEASUREMENTS_DATA_SET_CLASSIFICATION_TYPE_GUID,
                                           OpenMetadataAPIMapper.GOVERNANCE_MEASUREMENTS_DATA_SET_CLASSIFICATION_TYPE_NAME,
                                           this.setUpEffectiveDates(properties, effectiveFrom, effectiveTo),
                                           true,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }



    /**
     * Remove the governance measurements results dataset classification from a dataset.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of the software capability that owns this project
     * @param externalSourceName unique name of the software capability that owns this project
     * @param beanGUID unique identifier of entity to update
     * @param beanGUIDParameterName name of parameter providing beanGUID
     * @param beanGUIDTypeName type of bean
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeGovernanceMeasurementsDataSetClassification(String  userId,
                                                                   String  externalSourceGUID,
                                                                   String  externalSourceName,
                                                                   String  beanGUID,
                                                                   String  beanGUIDParameterName,
                                                                   String  beanGUIDTypeName,
                                                                   boolean forLineage,
                                                                   boolean forDuplicateProcessing,
                                                                   Date    effectiveTime,
                                                                   String  methodName) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        this.removeClassificationFromRepository(userId,
                                                externalSourceGUID,
                                                externalSourceName,
                                                beanGUID,
                                                beanGUIDParameterName,
                                                beanGUIDTypeName,
                                                OpenMetadataAPIMapper.GOVERNANCE_MEASUREMENTS_DATA_SET_CLASSIFICATION_TYPE_GUID,
                                                OpenMetadataAPIMapper.GOVERNANCE_MEASUREMENTS_DATA_SET_CLASSIFICATION_TYPE_NAME,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
    }


    /**
     * Save any associated Connection.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source - null for local
     * @param assetGUID unique identifier of the asset
     * @param assetGUIDParameterName parameter passing the assetGUID
     * @param assetSummary short description of the asset
     * @param connectionGUID unique identifier of the connection
     * @param connectionGUIDParameterName name of parameter for the connectionGUID
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime what is the effective time for related queries needed to do the update
     * @param methodName calling method
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private void attachConnectionToAsset(String  userId,
                                         String  externalSourceGUID,
                                         String  externalSourceName,
                                         String  assetGUID,
                                         String  assetGUIDParameterName,
                                         String  assetSummary,
                                         String  connectionGUID,
                                         String  connectionGUIDParameterName,
                                         Date    effectiveFrom,
                                         Date    effectiveTo,
                                         boolean forLineage,
                                         boolean forDuplicateProcessing,
                                         Date    effectiveTime,
                                         String  methodName) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        /*
         * The asset summary property is stored in the relationship between the asset and the connection.
         */
        InstanceProperties properties = null;

        if (assetSummary != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      OpenMetadataAPIMapper.ASSET_SUMMARY_PROPERTY_NAME,
                                                                      assetSummary,
                                                                      methodName);
        }

        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  assetGUID,
                                  assetGUIDParameterName,
                                  OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                  connectionGUID,
                                  connectionGUIDParameterName,
                                  OpenMetadataAPIMapper.CONNECTION_TYPE_NAME,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataAPIMapper.ASSET_TO_CONNECTION_TYPE_GUID,
                                  OpenMetadataAPIMapper.ASSET_TO_CONNECTION_TYPE_NAME,
                                  properties,
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Link the schema type and asset.  This is called from outside the AssetHandler.  The assetGUID is checked to ensure the
     * asset exists and updates are allowed.  If there is already a schema attached, it is deleted.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source - null for local
     * @param assetGUID unique identifier of the asset to connect the schema to
     * @param assetGUIDParameterName parameter providing the assetGUID
     * @param schemaTypeGUID identifier for schema Type object
     * @param schemaTypeGUIDParameterName parameter providing the schemaTypeGUID
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public  void attachSchemaTypeToAsset(String  userId,
                                         String  externalSourceGUID,
                                         String  externalSourceName,
                                         String  assetGUID,
                                         String  assetGUIDParameterName,
                                         String  schemaTypeGUID,
                                         String  schemaTypeGUIDParameterName,
                                         Date    effectiveFrom,
                                         Date    effectiveTo,
                                         boolean forLineage,
                                         boolean forDuplicateProcessing,
                                         Date    effectiveTime,
                                         String  methodName) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        /*
         * An asset is only allowed one attached schema.  Check that an update is permitted and remove any previously attached schema.
         */
        removeAssociatedSchemaType(userId,
                                   externalSourceGUID,
                                   externalSourceName,
                                   assetGUID,
                                   assetGUIDParameterName,
                                   forLineage,
                                   forDuplicateProcessing,
                                   effectiveTime,
                                   methodName);

        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  assetGUID,
                                  assetGUIDParameterName,
                                  OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                  schemaTypeGUID,
                                  schemaTypeGUIDParameterName,
                                  OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_GUID,
                                  OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_NAME,
                                  null,
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Remove any associated schema type.  This may be called from outside the AssetHandler.  The assetGUID is checked to ensure the
     * asset exists and updates are allowed.  If there is a schema attached, it is deleted.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source - null for local
     * @param assetGUID unique identifier of the asset to connect the schema to
     * @param assetGUIDParameterName parameter providing the assetGUID
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void removeAssociatedSchemaType(String  userId,
                                           String  externalSourceGUID,
                                           String  externalSourceName,
                                           String  assetGUID,
                                           String  assetGUIDParameterName,
                                           boolean forLineage,
                                           boolean forDuplicateProcessing,
                                           Date    effectiveTime,
                                           String  methodName) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        this.unlinkAllElements(userId,
                               false,
                               externalSourceGUID,
                               externalSourceName,
                               assetGUID,
                               assetGUIDParameterName,
                               OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                               forLineage,
                               forDuplicateProcessing,
                               supportedZones,
                               OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_GUID,
                               OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_NAME,
                               effectiveTime,
                               methodName);
    }


    /**
     * Link the asset to the associated software capability if supplied.  This is called from outside the AssetHandler.
     * The assetGUID and softwareServerCapabilityGUID are checked to ensure they are not null snd if all is well, the relationship is established.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source - null for local
     * @param assetGUID unique identifier of the asset to connect the schema to
     * @param assetGUIDParameterName parameter providing the assetGUID
     * @param softwareServerCapabilityGUID identifier for schema Type object
     * @param softwareServerCapabilityGUIDParameterName parameter providing the softwareServerCapabilityGUID
     * @param effectiveFrom      starting time for this relationship (null for all time)
     * @param effectiveTo        ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public  void attachAssetToSoftwareServerCapability(String  userId,
                                                       String  externalSourceGUID,
                                                       String  externalSourceName,
                                                       String  assetGUID,
                                                       String  assetGUIDParameterName,
                                                       String  softwareServerCapabilityGUID,
                                                       String  softwareServerCapabilityGUIDParameterName,
                                                       Date    effectiveFrom,
                                                       Date    effectiveTo,
                                                       boolean forLineage,
                                                       boolean forDuplicateProcessing,
                                                       Date    effectiveTime,
                                                       String  methodName) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        if ((assetGUID != null) && (softwareServerCapabilityGUID != null))
        {
            this.linkElementToElement(userId,
                                      externalSourceGUID,
                                      externalSourceName,
                                      softwareServerCapabilityGUID,
                                      softwareServerCapabilityGUIDParameterName,
                                      OpenMetadataAPIMapper.SOFTWARE_CAPABILITY_TYPE_NAME,
                                      assetGUID,
                                      assetGUIDParameterName,
                                      OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                      forLineage,
                                      forDuplicateProcessing,
                                      supportedZones,
                                      OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_GUID,
                                      OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_NAME,
                                      null,
                                      effectiveFrom,
                                      effectiveTo,
                                      effectiveTime,
                                      methodName);
        }
    }


    /**
     * Remove any associated schema type.  This may be called from outside the AssetHandler.  The assetGUID is checked to ensure the
     * asset exists and updates are allowed.  If there is a schema attached, it is deleted.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source - null for local
     * @param assetGUID unique identifier of the asset to connect the schema to
     * @param assetGUIDParameterName parameter providing the assetGUID
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return guid of previously attached schema type or null if there is no schema
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String detachSchemaTypeFromAsset(String  userId,
                                            String  externalSourceGUID,
                                            String  externalSourceName,
                                            String  assetGUID,
                                            String  assetGUIDParameterName,
                                            boolean forLineage,
                                            boolean forDuplicateProcessing,
                                            Date    effectiveTime,
                                            String  methodName) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        return this.unlinkConnectedElement(userId,
                                           false,
                                           externalSourceGUID,
                                           externalSourceName,
                                           assetGUID,
                                           assetGUIDParameterName,
                                           OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                           forLineage,
                                           forDuplicateProcessing,
                                           supportedZones,
                                           OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_GUID,
                                           OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_NAME,
                                           OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Add a simple asset description to the metadata repository.  Null values for requested typename, ownership,
     * zone membership and latest change are filled in with default values.
     *
     * @param userId calling userId
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateGUIDParameterName name of parameter providing the templateGUID
     * @param expectedTypeGUID unique identifier of type (or super type of asset identified by templateGUID)
     * @param expectedTypeName unique name of type (or super type of asset identified by templateGUID)
     * @param qualifiedName unique name for this asset - must not be null
     * @param qualifiedNameParameterName parameter name providing qualifiedName
     * @param name the stored name property for the asset - if null, the value from the template is used
     * @param versionIdentifier the stored versionIdentifier property for the asset
     * @param description the stored description property associated with the database - if null, the value from the template is used
     * @param pathName the physical address of the storage where the data is held (for DataStore assets)
     * @param networkAddress if there is a connection object for this asset - update the endpoint network address
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the asset in the repository.  If a connection or schema object is provided,
     *         it is stored linked to the asset.
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String addAssetFromTemplate(String  userId,
                                       String  externalSourceGUID,
                                       String  externalSourceName,
                                       String  templateGUID,
                                       String  templateGUIDParameterName,
                                       String  expectedTypeGUID,
                                       String  expectedTypeName,
                                       String  qualifiedName,
                                       String  qualifiedNameParameterName,
                                       String  name,
                                       String  versionIdentifier,
                                       String  description,
                                       String  pathName,
                                       String  networkAddress,
                                       boolean forLineage,
                                       boolean forDuplicateProcessing,
                                       Date    effectiveTime,
                                       String  methodName) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        Map<String, Object> extendedProperties = null;

        if (pathName != null)
        {
            extendedProperties = new HashMap<>();

            extendedProperties.put(OpenMetadataAPIMapper.PATH_NAME_PROPERTY_NAME, pathName);
        }

        AssetBuilder builder = new AssetBuilder(qualifiedName,
                                                name,
                                                versionIdentifier,
                                                description,
                                                null,
                                                expectedTypeGUID,
                                                expectedTypeName,
                                                extendedProperties,
                                                repositoryHelper,
                                                serviceName,
                                                serverName);

        String assetGUID = this.createBeanFromTemplate(userId,
                                                       externalSourceGUID,
                                                       externalSourceName,
                                                       templateGUID,
                                                       templateGUIDParameterName,
                                                       expectedTypeGUID,
                                                       expectedTypeName,
                                                       qualifiedName,
                                                       OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                       builder,
                                                       supportedZones,
                                                       methodName);

        if (assetGUID != null)
        {
            Relationship  assetConnectionRelationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                                      assetGUID,
                                                                                                      OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                                                                      OpenMetadataAPIMapper.CONNECTION_TO_ASSET_TYPE_GUID,
                                                                                                      OpenMetadataAPIMapper.CONNECTION_TO_ASSET_TYPE_NAME,
                                                                                                      1,
                                                                                                      forLineage,
                                                                                                      forDuplicateProcessing,
                                                                                                      effectiveTime,
                                                                                                      methodName);

            if (assetConnectionRelationship != null)
            {
                String connectionGUID = assetConnectionRelationship.getEntityOneProxy().getGUID();

                Relationship  connectionEndpointRelationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                                             connectionGUID,
                                                                                                             OpenMetadataAPIMapper.CONNECTION_TYPE_NAME,
                                                                                                             OpenMetadataAPIMapper.CONNECTION_ENDPOINT_TYPE_GUID,
                                                                                                             OpenMetadataAPIMapper.CONNECTION_ENDPOINT_TYPE_NAME,
                                                                                                             1,
                                                                                                             forLineage,
                                                                                                             forDuplicateProcessing,
                                                                                                             effectiveTime,
                                                                                                             methodName);
                if (connectionEndpointRelationship != null)
                {
                    final String endpointGUIDParameterName = "endpointGUID";

                    String endpointGUID = connectionEndpointRelationship.getEntityOneProxy().getGUID();

                    EntityDetail endpointEntity = this.getEntityFromRepository(userId,
                                                                               endpointGUID,
                                                                               endpointGUIDParameterName,
                                                                               OpenMetadataAPIMapper.ENDPOINT_TYPE_NAME,
                                                                               null,
                                                                               null,
                                                                               forLineage,
                                                                               forDuplicateProcessing,
                                                                               supportedZones,
                                                                               effectiveTime,
                                                                               methodName);

                    String anchorGUID = this.getAnchorGUIDFromAnchorsClassification(endpointEntity, methodName);

                    if (assetGUID.equals(anchorGUID))
                    {
                        InstanceProperties endpointProperties = endpointEntity.getProperties();

                        endpointProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                          endpointProperties,
                                                                                          OpenMetadataAPIMapper.NETWORK_ADDRESS_PROPERTY_NAME,
                                                                                          networkAddress,
                                                                                          methodName);
                        repositoryHandler.updateEntityProperties(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 endpointGUID,
                                                                 endpointEntity,
                                                                 OpenMetadataAPIMapper.ENDPOINT_TYPE_GUID,
                                                                 OpenMetadataAPIMapper.ENDPOINT_TYPE_NAME,
                                                                 endpointProperties,
                                                                 methodName);
                    }
                }
            }
        }

        return assetGUID;
    }


    /**
     * Add a simple asset description to the metadata repository.  Null values for requested typename, ownership,
     * zone membership and latest change are filled in with default values.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param qualifiedName unique name for this asset
     * @param technicalName the stored name property for the asset
     * @param versionIdentifier the stored versionIdentifier property for the asset
     * @param technicalDescription the stored description property associated with the asset
     * @param zoneMembership initial zones for the asset - or null to allow the security module to set it up
     * @param owner identifier of the owner
     * @param ownerType is the owner identifier a user id, personal profile or team profile
     * @param originOrganizationCapabilityGUID unique identifier of originating organization
     * @param originBusinessCapabilityGUID unique identifier of originating business capability
     * @param otherOriginValues the properties that characterize where this asset is from
     * @param additionalProperties any arbitrary properties not part of the type system
     * @param typeGUID identifier of the type that is a subtype of asset - or null to create standard type
     * @param typeName name of the type that is a subtype of asset - or null to create standard type
     * @param extendedProperties properties from any subtype
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param instanceStatus initial status of the Asset in the metadata repository
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the new asset
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String  createAssetInRepository(String               userId,
                                           String               externalSourceGUID,
                                           String               externalSourceName,
                                           String               qualifiedName,
                                           String               technicalName,
                                           String               versionIdentifier,
                                           String               technicalDescription,
                                           List<String>         zoneMembership,
                                           String               owner,
                                           int                  ownerType,
                                           String               originOrganizationCapabilityGUID,
                                           String               originBusinessCapabilityGUID,
                                           Map<String, String>  otherOriginValues,
                                           Map<String, String>  additionalProperties,
                                           String               typeGUID,
                                           String               typeName,
                                           Map<String, Object>  extendedProperties,
                                           Date                 effectiveFrom,
                                           Date                 effectiveTo,
                                           InstanceStatus       instanceStatus,
                                           Date                 effectiveTime,
                                           String               methodName) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        String assetTypeName = OpenMetadataAPIMapper.ASSET_TYPE_NAME;

        if (typeName != null)
        {
            assetTypeName = typeName;
        }

        String assetTypeId = invalidParameterHandler.validateTypeName(assetTypeName,
                                                                      OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                                      serviceName,
                                                                      methodName,
                                                                      repositoryHelper);

        AssetBuilder builder = new AssetBuilder(qualifiedName,
                                                technicalName,
                                                versionIdentifier,
                                                technicalDescription,
                                                additionalProperties,
                                                assetTypeId,
                                                assetTypeName,
                                                extendedProperties,
                                                instanceStatus,
                                                repositoryHelper,
                                                serviceName,
                                                serverName);

        if (zoneMembership != null)
        {
            builder.setAssetZones(userId, zoneMembership, methodName);
        }
        else if (defaultZones != null)
        {
            builder.setAssetZones(userId, defaultZones, methodName);
        }

        builder.setAssetOwnership(userId, owner, ownerType, methodName);
        builder.setAssetOrigin(userId,
                               originOrganizationCapabilityGUID,
                               OpenMetadataAPIMapper.GUID_PROPERTY_NAME,
                               originBusinessCapabilityGUID,
                               OpenMetadataAPIMapper.GUID_PROPERTY_NAME,
                               otherOriginValues,
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
     * Add a simple asset description to the metadata repository.  Null values for requested typename, ownership,
     * zone membership and latest change are filled in with default values.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param qualifiedName unique name for this asset
     * @param technicalName the stored name property for the asset
     * @param versionIdentifier the stored versionIdentifier property for the asset
     * @param technicalDescription the stored description property associated with the asset
     * @param additionalProperties any arbitrary properties not part of the type system
     * @param typeName name of the type that is a subtype of asset - or null to create standard type
     * @param extendedProperties properties from any subtype
     * @param instanceStatus initial status of the Asset in the metadata repository
     * @param effectiveFrom      starting time for this relationship (null for all time)
     * @param effectiveTo        ending time for this relationship (null for all time)
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the new asset
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String  createAssetInRepository(String               userId,
                                           String               externalSourceGUID,
                                           String               externalSourceName,
                                           String               qualifiedName,
                                           String               technicalName,
                                           String               versionIdentifier,
                                           String               technicalDescription,
                                           Map<String, String>  additionalProperties,
                                           String               typeName,
                                           Map<String, Object>  extendedProperties,
                                           InstanceStatus       instanceStatus,
                                           Date                 effectiveFrom,
                                           Date                 effectiveTo,
                                           Date                 effectiveTime,
                                           String               methodName) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        String assetTypeName = OpenMetadataAPIMapper.ASSET_TYPE_NAME;

        if (typeName != null)
        {
            assetTypeName = typeName;
        }

        String assetTypeId = invalidParameterHandler.validateTypeName(assetTypeName,
                                                                      OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                                      serviceName,
                                                                      methodName,
                                                                      repositoryHelper);

        AssetBuilder builder = new AssetBuilder(qualifiedName,
                                                technicalName,
                                                versionIdentifier,
                                                technicalDescription,
                                                additionalProperties,
                                                assetTypeId,
                                                assetTypeName,
                                                extendedProperties,
                                                instanceStatus,
                                                repositoryHelper,
                                                serviceName,
                                                serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        if (defaultZones != null)
        {
            builder.setAssetZones(userId, defaultZones, methodName);
        }

        return this.createBeanInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           assetTypeId,
                                           assetTypeName,
                                           builder,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Add a simple asset description to the metadata repository.  Null values for requested typename, ownership,
     * zone membership and latest change are filled in with default values.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param assetGUIDParameterName parameter name of the resulting asset's GUID
     * @param assetQualifiedName unique name for this asset
     * @param technicalName the stored name property for the asset
     * @param versionIdentifier the stored versionIdentifier property for the asset
     * @param technicalDescription the stored description property associated with the asset
     * @param additionalProperties any arbitrary properties not part of the type system
     * @param assetTypeName name of the type that is a subtype of asset - or null to create standard type
     * @param extendedProperties properties from any subtype
     * @param instanceStatus initial status of the Asset in the metadata repository
     * @param anchorEndpointToAsset set to true if the network address is unique for the asset and should not be reused. False if this is an endpoint
     *                              that is relevant for multiple assets.
     * @param configurationProperties configuration properties for the connection
     * @param connectorProviderClassName Java class name for the connector provider
     * @param networkAddress the network address (typically the URL but this depends on the protocol)
     * @param protocol the name of the protocol to use to connect to the endpoint
     * @param encryptionMethod encryption method to use when passing data to this endpoint
     * @param effectiveFrom      starting time for this relationship (null for all time)
     * @param effectiveTo        ending time for this relationship (null for all time)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     *
     * @return unique identifier of the new asset
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String  createAssetWithConnection(String              userId,
                                             String              externalSourceGUID,
                                             String              externalSourceName,
                                             String              assetGUIDParameterName,
                                             String              assetQualifiedName,
                                             String              technicalName,
                                             String              versionIdentifier,
                                             String              technicalDescription,
                                             Map<String, String> additionalProperties,
                                             String              assetTypeName,
                                             Map<String, Object> extendedProperties,
                                             InstanceStatus      instanceStatus,
                                             boolean             anchorEndpointToAsset,
                                             Map<String, Object> configurationProperties,
                                             String              connectorProviderClassName,
                                             String              networkAddress,
                                             String              protocol,
                                             String              encryptionMethod,
                                             Date                effectiveFrom,
                                             Date                effectiveTo,
                                             boolean             forLineage,
                                             boolean             forDuplicateProcessing,
                                             Date                effectiveTime,
                                             String              methodName) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        String assetGUID = this.createAssetInRepository(userId,
                                                        externalSourceGUID,
                                                        externalSourceName,
                                                        assetQualifiedName,
                                                        technicalName,
                                                        versionIdentifier,
                                                        technicalDescription,
                                                        additionalProperties,
                                                        assetTypeName,
                                                        extendedProperties,
                                                        instanceStatus,
                                                        effectiveFrom,
                                                        effectiveTo,
                                                        effectiveTime,
                                                        methodName);

        if (assetGUID != null)
        {
            connectionHandler.addAssetConnection(userId,
                                                 externalSourceGUID,
                                                 externalSourceName,
                                                 assetGUID,
                                                 assetGUIDParameterName,
                                                 assetTypeName,
                                                 assetQualifiedName,
                                                 anchorEndpointToAsset,
                                                 configurationProperties,
                                                 connectorProviderClassName,
                                                 networkAddress,
                                                 protocol,
                                                 encryptionMethod,
                                                 effectiveFrom,
                                                 effectiveTo,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 effectiveTime,
                                                 methodName);
        }

        return assetGUID;
    }



    /**
     * Update an asset's properties.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param assetGUID unique identifier of the metadata element to update
     * @param assetGUIDParameterName parameter name that supplied the assetGUID
     * @param qualifiedName unique name for this database
     * @param technicalName the stored name property for the asset
     * @param versionIdentifier the stored versionIdentifier property for the asset
     * @param technicalDescription the stored description property associated with the asset
     * @param additionalProperties any arbitrary properties not part of the type system
     * @param typeName name of the type that is a subtype of Database - or null to create standard type
     * @param extendedProperties properties from any subtype
     * @param effectiveFrom      starting time for this relationship (null for all time)
     * @param effectiveTo        ending time for this relationship (null for all time)
     * @param isMergeUpdate should the new properties be merged with the existing properties of overlay them?
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateAsset(String               userId,
                            String               externalSourceGUID,
                            String               externalSourceName,
                            String               assetGUID,
                            String               assetGUIDParameterName,
                            String               qualifiedName,
                            String               technicalName,
                            String               versionIdentifier,
                            String               technicalDescription,
                            Map<String, String>  additionalProperties,
                            String               typeName,
                            Map<String, Object>  extendedProperties,
                            Date                 effectiveFrom,
                            Date                 effectiveTo,
                            boolean              isMergeUpdate,
                            boolean              forLineage,
                            boolean              forDuplicateProcessing,
                            Date                 effectiveTime,
                            String               methodName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        this.updateAsset(userId,
                         externalSourceGUID,
                         externalSourceName,
                         assetGUID,
                         assetGUIDParameterName,
                         qualifiedName,
                         technicalName,
                         versionIdentifier,
                         technicalDescription,
                         additionalProperties,
                         typeGUID,
                         typeName,
                         supportedZones,
                         extendedProperties,
                         effectiveFrom,
                         effectiveTo,
                         isMergeUpdate,
                         forLineage,
                         forDuplicateProcessing,
                         effectiveTime,
                         methodName);
    }


    /**
     * Update an asset's properties.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param assetGUID unique identifier of the metadata element to update
     * @param assetGUIDParameterName parameter name that supplied the assetGUID
     * @param qualifiedName unique name for this database
     * @param technicalName the stored  name property for the asset
     * @param versionIdentifier the stored versionIdentifier property for the asset
     * @param technicalDescription the stored description property associated with the asset
     * @param additionalProperties any arbitrary properties not part of the type system
     * @param typeGUID identifier of the type that is a subtype of Database - or null to create standard type
     * @param typeName name of the type that is a subtype of Database - or null to create standard type
     * @param effectiveFrom      starting time for this relationship (null for all time)
     * @param effectiveTo        ending time for this relationship (null for all time)
     * @param isMergeUpdate indicates whether supplied properties should replace
     * @param extendedProperties properties from any subtype
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateAsset(String               userId,
                            String               externalSourceGUID,
                            String               externalSourceName,
                            String               assetGUID,
                            String               assetGUIDParameterName,
                            String               qualifiedName,
                            String               technicalName,
                            String               versionIdentifier,
                            String               technicalDescription,
                            Map<String, String>  additionalProperties,
                            String               typeGUID,
                            String               typeName,
                            Map<String, Object>  extendedProperties,
                            Date                 effectiveFrom,
                            Date                 effectiveTo,
                            boolean              isMergeUpdate,
                            boolean              forLineage,
                            boolean              forDuplicateProcessing,
                            Date                 effectiveTime,
                            String               methodName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        this.updateAsset(userId,
                         externalSourceGUID,
                         externalSourceName,
                         assetGUID,
                         assetGUIDParameterName,
                         qualifiedName,
                         technicalName,
                         versionIdentifier,
                         technicalDescription,
                         additionalProperties,
                         typeGUID,
                         typeName,
                         supportedZones,
                         extendedProperties,
                         effectiveFrom,
                         effectiveTo,
                         isMergeUpdate,
                         forLineage,
                         forDuplicateProcessing,
                         effectiveTime,
                         methodName);
    }


    /**
     * Update an asset's properties.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param assetGUID unique identifier of the metadata element to update
     * @param assetGUIDParameterName parameter name that supplied the assetGUID
     * @param qualifiedName unique name for this database
     * @param technicalName the stored name property for the asset
     * @param versionIdentifier the stored versionIdentifier property for the asset
     * @param technicalDescription the stored description property associated with the asset
     * @param additionalProperties any arbitrary properties not part of the type system
     * @param typeGUID identifier of the type that is a subtype of Asset - or null to create standard type
     * @param typeName name of the type that is a subtype of Asset - or null to create standard type
     * @param suppliedSupportedZones supported zones that are specific to the caller
     * @param extendedProperties properties from any subtype
     * @param effectiveFrom      starting time for this relationship (null for all time)
     * @param effectiveTo        ending time for this relationship (null for all time)
     * @param isMergeUpdate should the new properties be merged with the existing properties of overlay them?
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateAsset(String               userId,
                            String               externalSourceGUID,
                            String               externalSourceName,
                            String               assetGUID,
                            String               assetGUIDParameterName,
                            String               qualifiedName,
                            String               technicalName,
                            String               versionIdentifier,
                            String               technicalDescription,
                            Map<String, String>  additionalProperties,
                            String               typeGUID,
                            String               typeName,
                            List<String>         suppliedSupportedZones,
                            Map<String, Object>  extendedProperties,
                            Date                 effectiveFrom,
                            Date                 effectiveTo,
                            boolean              isMergeUpdate,
                            boolean              forLineage,
                            boolean              forDuplicateProcessing,
                            Date                 effectiveTime,
                            String               methodName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        AssetBuilder builder = new AssetBuilder(qualifiedName,
                                                technicalName,
                                                versionIdentifier,
                                                technicalDescription,
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
                                    assetGUID,
                                    assetGUIDParameterName,
                                    typeGUID,
                                    typeName,
                                    forLineage,
                                    forDuplicateProcessing,
                                    suppliedSupportedZones,
                                    builder.getInstanceProperties(methodName),
                                    isMergeUpdate,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Update an asset's properties.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param assetGUID unique identifier of the metadata element to update
     * @param assetGUIDParameterName parameter name that supplied the assetGUID
     * @param qualifiedName unique name for this asset
     * @param name the stored name property for the asset
     * @param versionIdentifier the stored versionIdentifier property for the asset
     * @param description the stored description property associated with the asset
     * @param additionalProperties any arbitrary properties not part of the type system
     * @param typeGUID identifier of the type that is a subtype of Database - or null to create standard type
     * @param typeName name of the type that is a subtype of Database - or null to create standard type
     * @param extendedProperties properties from any subtype
     * @param effectiveFrom      starting time for this relationship (null for all time)
     * @param effectiveTo        ending time for this relationship (null for all time)
     * @param isMergeUpdate should the supplied properties be merged with existing properties (true) only replacing the properties with
     *                      matching names, or should the entire properties of the instance be replaced?
     * @param connection connection associated with the asset
     * @param assetSummary description of the asset from the perspective of the connection
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateAssetWithConnection(String               userId,
                                          String               externalSourceGUID,
                                          String               externalSourceName,
                                          String               assetGUID,
                                          String               assetGUIDParameterName,
                                          String               qualifiedName,
                                          String               name,
                                          String               versionIdentifier,
                                          String               description,
                                          Map<String, String>  additionalProperties,
                                          String               typeGUID,
                                          String               typeName,
                                          Map<String, Object>  extendedProperties,
                                          Date                 effectiveFrom,
                                          Date                 effectiveTo,
                                          boolean              isMergeUpdate,
                                          String               assetSummary,
                                          Connection           connection,
                                          boolean              forLineage,
                                          boolean              forDuplicateProcessing,
                                          Date                 effectiveTime,
                                          String               methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        this.updateAsset(userId,
                         externalSourceGUID,
                         externalSourceName,
                         assetGUID,
                         assetGUIDParameterName,
                         qualifiedName,
                         name,
                         versionIdentifier,
                         description,
                         additionalProperties,
                         typeGUID,
                         typeName,
                         extendedProperties,
                         effectiveFrom,
                         effectiveTo,
                         isMergeUpdate,
                         forLineage,
                         forDuplicateProcessing,
                         effectiveTime,
                         methodName);

        Relationship  assetConnectionRelationship = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                                  assetGUID,
                                                                                                  OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                                                                  OpenMetadataAPIMapper.CONNECTION_TO_ASSET_TYPE_GUID,
                                                                                                  OpenMetadataAPIMapper.CONNECTION_TO_ASSET_TYPE_NAME,
                                                                                                  1,
                                                                                                  forLineage,
                                                                                                  forDuplicateProcessing,
                                                                                                  effectiveTime,
                                                                                                  methodName);
        if (connection == null)
        {
            this.unlinkConnectedElement(userId,
                                        false,
                                        null,
                                        null,
                                        assetGUID,
                                        assetGUIDParameterName,
                                        typeName,
                                        forLineage,
                                        forDuplicateProcessing,
                                        supportedZones,
                                        OpenMetadataAPIMapper.CONNECTION_TO_ASSET_TYPE_GUID,
                                        OpenMetadataAPIMapper.CONNECTION_TO_ASSET_TYPE_NAME,
                                        OpenMetadataAPIMapper.COLLECTION_TYPE_NAME,
                                        effectiveTime,
                                        methodName);
        }
        else /* connection to add */
        {
            String  connectionGUID = connectionHandler.saveConnection(userId,
                                                                      null,
                                                                      null,
                                                                      assetGUID,
                                                                      assetGUID,
                                                                      assetGUIDParameterName,
                                                                      typeName,
                                                                      connection,
                                                                      assetSummary,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      effectiveTime,
                                                                      methodName);

            InstanceProperties relationshipProperties = null;

            if (assetSummary != null)
            {
                relationshipProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                      null,
                                                                                      OpenMetadataAPIMapper.ASSET_SUMMARY_PROPERTY_NAME,
                                                                                      assetSummary,
                                                                                      methodName);
            }

            if (assetConnectionRelationship == null)
            {

                repositoryHandler.createRelationship(userId,
                                                     OpenMetadataAPIMapper.CONNECTION_TO_ASSET_TYPE_GUID,
                                                     null,
                                                     null,
                                                     connectionGUID,
                                                     assetGUID,
                                                     relationshipProperties,
                                                     methodName);
            }
            else
            {
                repositoryHandler.updateUniqueRelationshipByType(userId,
                                                                 null,
                                                                 null,
                                                                 connectionGUID,
                                                                 OpenMetadataAPIMapper.CONNECTION_TYPE_NAME,
                                                                 assetGUID,
                                                                 OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                                 OpenMetadataAPIMapper.CONNECTION_TO_ASSET_TYPE_GUID,
                                                                 OpenMetadataAPIMapper.CONNECTION_TO_ASSET_TYPE_NAME,
                                                                 relationshipProperties,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 effectiveTime,
                                                                 methodName);
            }
        }
    }


    /**
     * Add the ReferenceData classification to an asset.  If the asset is already classified
     * in this way, the method is a no-op.
     *
     * @param userId calling user.
     * @param assetGUID unique identifier of the asset that contains reference data.
     * @param assetGUIDParameterName name of parameter providing assetGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public void  classifyAssetAsReferenceData(String  userId,
                                              String  assetGUID,
                                              String  assetGUIDParameterName,
                                              boolean forLineage,
                                              boolean forDuplicateProcessing,
                                              Date    effectiveTime,
                                              String  methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        this.setClassificationInRepository(userId,
                                           null,
                                           null,
                                           assetGUID,
                                           assetGUIDParameterName,
                                           OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                           OpenMetadataAPIMapper.REFERENCE_DATA_CLASSIFICATION_TYPE_GUID,
                                           OpenMetadataAPIMapper.REFERENCE_DATA_CLASSIFICATION_TYPE_NAME,
                                           null,
                                           false,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Remove the ReferenceData classification from an Asset.  If the asset was not classified in this way,
     * this call is a no-op.
     *
     * @param userId calling user.
     * @param assetGUID unique identifier of asset.
     * @param assetGUIDParameterName name of parameter providing assetGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public void  declassifyAssetAsReferenceData(String  userId,
                                                String  assetGUID,
                                                String  assetGUIDParameterName,
                                                boolean forLineage,
                                                boolean forDuplicateProcessing,
                                                Date    effectiveTime,
                                                String  methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        this.removeClassificationFromRepository(userId,
                                                null,
                                                null,
                                                assetGUID,
                                                assetGUIDParameterName,
                                                OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                OpenMetadataAPIMapper.REFERENCE_DATA_CLASSIFICATION_TYPE_GUID,
                                                OpenMetadataAPIMapper.REFERENCE_DATA_CLASSIFICATION_TYPE_NAME,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
    }


    /**
     * Add the asset origin classification to an asset.  The method needs to build a before an after image of the
     * asset to perform a security check before the update is pushed to the repository.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param assetGUIDParameterName parameter name supplying assetGUID
     * @param organizationGUID Unique identifier (GUID) of the organization where this asset originated from - or null
     * @param organizationGUIDParameterName parameter name supplying organizationGUID
     * @param businessCapabilityGUID  Unique identifier (GUID) of the business capability where this asset originated from.
     * @param businessCapabilityGUIDParameterName parameter name supplying businessCapabilityGUID
     * @param otherOriginValues Descriptive labels describing origin of the asset
     * @param effectiveFrom the time that the elements must be effective from (null for any time)
     * @param effectiveTo the time that the elements must be effective from (null for any time)
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addAssetOrigin(String                userId,
                                String                assetGUID,
                                String                assetGUIDParameterName,
                                String                organizationGUID,
                                String                organizationGUIDParameterName,
                                String                businessCapabilityGUID,
                                String                businessCapabilityGUIDParameterName,
                                Map<String, String>   otherOriginValues,
                                Date                  effectiveFrom,
                                Date                  effectiveTo,
                                boolean               isMergeUpdate,
                                boolean               forLineage,
                                boolean               forDuplicateProcessing,
                                Date                  effectiveTime,
                                String                methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        /*
         * Validate the organization and the business capability exist.
         */
        if (organizationGUID  != null)
        {
            this.validateAnchorEntity(userId,
                                      organizationGUID,
                                      organizationGUIDParameterName,
                                      OpenMetadataAPIMapper.ORGANIZATION_TYPE_NAME,
                                      false,
                                      forLineage,
                                      forDuplicateProcessing,
                                      supportedZones,
                                      effectiveFrom,
                                      methodName);
        }

        if (businessCapabilityGUID != null)
        {
            this.validateAnchorEntity(userId,
                                      businessCapabilityGUID,
                                      businessCapabilityGUIDParameterName,
                                      OpenMetadataAPIMapper.BUSINESS_CAPABILITY_TYPE_NAME,
                                      false,
                                      forLineage,
                                      forDuplicateProcessing,
                                      supportedZones,
                                      effectiveFrom,
                                      methodName);
        }

        AssetBuilder builder = new AssetBuilder(repositoryHelper, serviceName, serverName);

        InstanceProperties properties = builder.getOriginProperties(organizationGUID,
                                                                    OpenMetadataAPIMapper.GUID_PROPERTY_NAME,
                                                                    businessCapabilityGUID,
                                                                    OpenMetadataAPIMapper.GUID_PROPERTY_NAME,
                                                                    otherOriginValues,
                                                                    methodName);

        this.setUpEffectiveDates(properties, effectiveFrom, effectiveTo);

        this.setClassificationInRepository(userId,
                                           null,
                                           null,
                                           assetGUID,
                                           assetGUIDParameterName,
                                           OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                           OpenMetadataAPIMapper.ASSET_ORIGIN_CLASSIFICATION_GUID,
                                           OpenMetadataAPIMapper.ASSET_ORIGIN_CLASSIFICATION_NAME,
                                           properties,
                                           isMergeUpdate,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Remove the asset origin classification to an asset.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param assetGUIDParameterName parameter name supplying assetGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeAssetOrigin(String  userId,
                                   String  assetGUID,
                                   String  assetGUIDParameterName,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing,
                                   Date    effectiveTime,
                                   String  methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        this.removeClassificationFromRepository(userId,
                                                null,
                                                null,
                                                assetGUID,
                                                assetGUIDParameterName,
                                                OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                OpenMetadataAPIMapper.ASSET_ORIGIN_CLASSIFICATION_GUID,
                                                OpenMetadataAPIMapper.ASSET_ORIGIN_CLASSIFICATION_NAME,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
    }


    /**
     * Update the zones for a specific asset to the list set up in publish zones.
     *
     * @param userId calling user
     * @param assetGUID unique identifier for the asset to update
     * @param assetGUIDParameterName parameter name supplying assetGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException guid or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void publishAsset(String  userId,
                             String  assetGUID,
                             String  assetGUIDParameterName,
                             boolean forLineage,
                             boolean forDuplicateProcessing,
                             Date    effectiveTime,
                             String  methodName) throws InvalidParameterException,
                                                        UserNotAuthorizedException,
                                                        PropertyServerException
    {
        updateAssetZones(userId, assetGUID, assetGUIDParameterName, publishZones, true, forLineage, forDuplicateProcessing, effectiveTime, methodName);
    }


    /**
     * Update the zones for a specific asset to the list set up in publish zones.
     *
     * @param userId calling user
     * @param assetGUID unique identifier for the asset to update
     * @param assetGUIDParameterName parameter name supplying assetGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException guid or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void withdrawAsset(String  userId,
                              String  assetGUID,
                              String  assetGUIDParameterName,
                              boolean forLineage,
                              boolean forDuplicateProcessing,
                              Date    effectiveTime,
                              String  methodName) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        updateAssetZones(userId, assetGUID,  assetGUIDParameterName, defaultZones, true, forLineage, forDuplicateProcessing, effectiveTime, methodName);
    }


    /**
     * Update the zones for a specific asset. The method needs to build a before an after image of the
     * asset to perform a security check before the update is pushed to the repository.
     *
     * @param userId calling user
     * @param assetGUID unique identifier for the asset to update
     * @param assetGUIDParameterName parameter name supplying assetGUID
     * @param assetZones list of zones for the asset - these values override the current values - null means belongs
     *                   to all zones.
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException guid or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void updateAssetZones(String        userId,
                                 String        assetGUID,
                                 String        assetGUIDParameterName,
                                 List<String>  assetZones,
                                 boolean       isMergeUpdate,
                                 boolean       forLineage,
                                 boolean       forDuplicateProcessing,
                                 Date          effectiveTime,
                                 String        methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameterName, methodName);

        AssetBuilder builder = new AssetBuilder(repositoryHelper, serviceName, serverName);

        this.setClassificationInRepository(userId,
                                           null,
                                           null,
                                           assetGUID,
                                           assetGUIDParameterName,
                                           OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                           OpenMetadataAPIMapper.ASSET_ZONES_CLASSIFICATION_GUID,
                                           OpenMetadataAPIMapper.ASSET_ZONES_CLASSIFICATION_NAME,
                                           builder.getZoneMembershipProperties(assetZones, methodName),
                                           isMergeUpdate,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Update the owner information for a specific asset.
     *
     * @param userId calling user
     * @param assetGUID unique identifier for the asset to update
     * @param assetGUIDParameterName parameter name supplying assetGUID
     * @param ownerId userId or profileGUID of the owner - or null to clear the field
     * @param ownerType indicator of the type of id provided above - or null to clear the field
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Deprecated
    public void updateAssetOwner(String    userId,
                                 String    assetGUID,
                                 String    assetGUIDParameterName,
                                 String    ownerId,
                                 int       ownerType,
                                 boolean   forLineage,
                                 boolean   forDuplicateProcessing,
                                 Date      effectiveTime,
                                 String    methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        String ownerTypeName = null;

        if (ownerType == 0)
        {
            ownerTypeName = OpenMetadataAPIMapper.USER_IDENTITY_TYPE_NAME;
        }
        else if (ownerType == 1)
        {
            ownerTypeName = OpenMetadataAPIMapper.ACTOR_PROFILE_TYPE_NAME;
        }

        this.addOwner(userId,
                      assetGUID,
                      assetGUIDParameterName,
                      OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                      ownerId,
                      ownerTypeName,
                      null,
                      forLineage,
                      forDuplicateProcessing,
                      effectiveTime,
                      methodName);
    }


    /**
     * Returns the unique identifier for the asset connected to the requested connection.
     *
     * @param userId the userId of the requesting user
     * @param connectionGUID  unique identifier for the connection
     * @param connectionGUIDParameterName name of parameter supplying connectionGUID
     * @param serviceSupportedZones list of supported zones for any connected asset
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String  getAssetForConnection(String       userId,
                                         String       connectionGUID,
                                         String       connectionGUIDParameterName,
                                         List<String> serviceSupportedZones,
                                         boolean      forLineage,
                                         boolean      forDuplicateProcessing,
                                         Date         effectiveTime,
                                         String       methodName) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        return this.getAttachedElementGUID(userId,
                                           connectionGUID,
                                           connectionGUIDParameterName,
                                           OpenMetadataAPIMapper.CONNECTION_TYPE_NAME,
                                           OpenMetadataAPIMapper.ASSET_TO_CONNECTION_TYPE_GUID,
                                           OpenMetadataAPIMapper.ASSET_TO_CONNECTION_TYPE_NAME,
                                           OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                           0,
                                           forLineage,
                                           forDuplicateProcessing,
                                           serviceSupportedZones,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Remove any data sets connected to the asset by the DataContentForDataSet relationship.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param assetGUID unique identifier for asset
     * @param assetTypeName type of asset
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeLinkedDataSets(String  userId,
                                     String  externalSourceGUID,
                                     String  externalSourceName,
                                     String  assetGUID,
                                     String  assetTypeName,
                                     boolean forLineage,
                                     boolean forDuplicateProcessing,
                                     Date    effectiveTime,
                                     String  methodName) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                       invalidParameterHandler,
                                                                                       userId,
                                                                                       assetGUID,
                                                                                       assetTypeName,
                                                                                       OpenMetadataAPIMapper.DATA_CONTENT_FOR_DATA_SET_TYPE_GUID,
                                                                                       OpenMetadataAPIMapper.DATA_CONTENT_FOR_DATA_SET_TYPE_NAME,
                                                                                       2,
                                                                                       forLineage,
                                                                                       forDuplicateProcessing,
                                                                                       0,
                                                                                       0,
                                                                                       effectiveTime,
                                                                                       methodName);

        while (iterator.moreToReceive())
        {
            Relationship relationship = iterator.getNext();

            if (relationship != null)
            {
                final String elementGUIDParameterName = "relationship.getEntityTwoProxy().getGUID()";

                this.deleteBeanInRepository(userId,
                                            externalSourceGUID,
                                            externalSourceName,
                                            relationship.getEntityTwoProxy().getGUID(),
                                            elementGUIDParameterName,
                                            OpenMetadataAPIMapper.DATA_SET_TYPE_GUID,
                                            OpenMetadataAPIMapper.DATA_SET_TYPE_NAME,
                                            null,
                                            null,
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveTime,
                                            methodName);
            }
        }
    }


    /**
     * Returns the asset corresponding to the supplied connection name.
     *
     * @param userId           userId of user making request.
     * @param connectionName   this may be the qualifiedName or displayName of the connection.
     * @param connectionNameParameter name of parameter supplying
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName       calling method
     *
     * @return unique identifier of asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String  getAssetForConnectionName(String  userId,
                                             String  connectionName,
                                             String  connectionNameParameter,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing,
                                             Date    effectiveTime,
                                             String  methodName) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME);

        List<String> connectionGUIDs = this.getEntityGUIDsByValue(userId,
                                                                  connectionName,
                                                                  connectionNameParameter,
                                                                  OpenMetadataAPIMapper.CONNECTION_TYPE_GUID,
                                                                  OpenMetadataAPIMapper.CONNECTION_TYPE_NAME,
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

        if (connectionGUIDs != null)
        {
            for (String connectionGUID : connectionGUIDs)
            {
                if (connectionGUID != null)
                {
                    return this.getAssetForConnection(userId,
                                                      connectionGUID,
                                                      connectionNameParameter,
                                                      supportedZones,
                                                      forLineage,
                                                      forDuplicateProcessing,
                                                      effectiveTime,
                                                      methodName);
                }
            }
        }

        return null;
    }


    /**
     * Return an asset along with any associated connection.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @param assetGUIDParameterName name of parameter supplying assetGUID
     * @param assetTypeName type name of asset
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return an asset bean (with embedded connection details if available)
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public B getAssetWithConnection(String  userId,
                                    String  assetGUID,
                                    String  assetGUIDParameterName,
                                    String  assetTypeName,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing,
                                    Date    effectiveTime,
                                    String  methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        EntityDetail assetEntity = this.getEntityFromRepository(userId,
                                                                assetGUID,
                                                                assetGUIDParameterName,
                                                                assetTypeName,
                                                                null,
                                                                null,
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                supportedZones,
                                                                effectiveTime,
                                                                methodName);

        if (assetEntity != null)
        {
            return this.getAssetWithConnectionBean(userId,
                                                   assetEntity,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   effectiveTime,
                                                   methodName);
        }

        return null;
    }


    /**
     * Return an asset along with any associated connection.
     *
     * @param userId calling user
     * @param name unique identifier of the asset
     * @param nameParameterName name of parameter supplying name
     * @param assetTypeGUID type identifier of asset
     * @param assetTypeName type name of asset
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return an asset bean (with embedded connection details if available)
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public B getAssetByNameWithConnection(String  userId,
                                          String  name,
                                          String  nameParameterName,
                                          String  assetTypeGUID,
                                          String  assetTypeName,
                                          boolean forLineage,
                                          boolean forDuplicateProcessing,
                                          Date    effectiveTime,
                                          String  methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.NAME_PROPERTY_NAME);

        List<EntityDetail> results = this.getEntitiesByValue(userId,
                                                             name,
                                                             nameParameterName,
                                                             assetTypeGUID,
                                                             assetTypeName,
                                                             specificMatchPropertyNames,
                                                             true,
                                                             false,
                                                             null,
                                                             null,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             supportedZones,
                                                             OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                             0,
                                                             invalidParameterHandler.getMaxPagingSize(),
                                                             effectiveTime,
                                                             methodName);

        if ((results != null) && (results.size() == 1))
        {
            return getAssetWithConnectionBean(userId,
                                              results.get(0),
                                              forLineage,
                                              forDuplicateProcessing,
                                              effectiveTime,
                                              methodName);
        }
        else
        {
            errorHandler.handleAmbiguousEntityName(name, nameParameterName, assetTypeName, results, methodName);
        }

        return null;
    }


    /**
     * Return the list of assets that are stored.
     *
     * @param userId identifier of calling user
     * @param assetTypeGUID subtype of asset required
     * @param assetTypeName subtype of asset required
     * @param startFrom initial position in the stored list
     * @param pageSize maximum number of definitions to return on this call
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of beans.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery service definitions.
     */
    public List<B> getAllAssetsWithConnection(String  userId,
                                              String  assetTypeGUID,
                                              String  assetTypeName,
                                              int     startFrom,
                                              int     pageSize,
                                              boolean forLineage,
                                              boolean forDuplicateProcessing,
                                              Date    effectiveTime,
                                              String  methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        List<EntityDetail> entities = this.getEntitiesByType(userId,
                                                             assetTypeGUID,
                                                             assetTypeName,
                                                             null,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             supportedZones,
                                                             startFrom,
                                                             pageSize,
                                                             effectiveTime,
                                                             methodName);

        if ((entities != null) && (! entities.isEmpty()))
        {
            List<B> results = new ArrayList<>();

            for (EntityDetail entity : entities)
            {
                if (entity != null)
                {
                    B bean = this.getAssetWithConnectionBean(userId,
                                                             entity,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime,
                                                             methodName);
                    if (bean != null)
                    {
                        results.add(bean);
                    }
                }
            }

            if (! results.isEmpty())
            {
                return results;
            }
        }

        return null;
    }


    /**
     * Retrieve a connection object.  This may be a standard connection or a virtual connection.  The method includes the
     * connector type, endpoint and any embedded connections in the returned bean.  (This is an alternative to calling
     * the standard generic handler methods that would only retrieve the properties of the Connection entity.)
     *
     * @param userId calling user
     * @param assetEntity entity for root connection object
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return connection object
     * @throws InvalidParameterException the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private B getAssetWithConnectionBean(String       userId,
                                         EntityDetail assetEntity,
                                         boolean      forLineage,
                                         boolean      forDuplicateProcessing,
                                         Date         effectiveTime,
                                         String       methodName) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        if (assetEntity != null)
        {
            EntityDetail connectionEntity = null;
            Relationship relationshipToConnection = repositoryHandler.getUniqueRelationshipByType(userId,
                                                                                                  assetEntity.getGUID(),
                                                                                                  assetEntity.getType().getTypeDefName(),
                                                                                                  OpenMetadataAPIMapper.CONNECTION_TO_ASSET_TYPE_GUID,
                                                                                                  OpenMetadataAPIMapper.CONNECTION_TO_ASSET_TYPE_NAME,
                                                                                                  1,
                                                                                                  forLineage,
                                                                                                  forDuplicateProcessing,
                                                                                                  effectiveTime,
                                                                                                  methodName);

            if (relationshipToConnection != null)
            {
                EntityProxy end1 = relationshipToConnection.getEntityOneProxy();

                if (end1 != null)
                {
                    final String connectionGUIDParameterName = "relationshipToConnection.getEntityOneProxy().getGUID()";

                    connectionEntity = this.getEntityFromRepository(userId,
                                                                    end1.getGUID(),
                                                                    connectionGUIDParameterName,
                                                                    OpenMetadataAPIMapper.CONNECTION_TYPE_NAME,
                                                                    null,
                                                                    null,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    supportedZones,
                                                                    effectiveTime,
                                                                    methodName);
                }
            }

            List<Relationship> supplementaryRelationships = new ArrayList<>();
            List<EntityDetail> supplementaryEntities      = new ArrayList<>();

            if ((connectionEntity != null) && (connectionEntity.getType() != null))
            {
                /*
                 * The relationships are retrieved first.  It is not possible to follow the same pattern as SchemaType where
                 * embedded instances are retrieve as beans and then assembled in the final bean at the end because of the problem of
                 * matching the properties in the EmbeddedConnection relationship with the Connection bean it links to.
                 * So the entire graph of instances for the connection are retrieved and passed to the converter.
                 */
                supplementaryEntities.add(connectionEntity);
                supplementaryRelationships.add(relationshipToConnection);

                List<Relationship> connectionRelationships = this.getEmbeddedConnectionRelationships(userId,
                                                                                                     connectionEntity,
                                                                                                     forLineage,
                                                                                                     forDuplicateProcessing,
                                                                                                     effectiveTime,
                                                                                                     methodName);

                if (connectionRelationships != null)
                {
                    supplementaryRelationships.addAll(connectionRelationships);

                    for (Relationship relationship : connectionRelationships)
                    {
                        if ((relationship != null) && (relationship.getType() != null))
                        {
                            EntityProxy entityProxy = null;

                            if ((repositoryHelper.isTypeOf(serviceName,
                                                           relationship.getType().getTypeDefName(),
                                                           OpenMetadataAPIMapper.CONNECTION_CONNECTOR_TYPE_TYPE_NAME))
                                    || (repositoryHelper.isTypeOf(serviceName,
                                                                  relationship.getType().getTypeDefName(),
                                                                  OpenMetadataAPIMapper.EMBEDDED_CONNECTION_TYPE_NAME)))
                            {
                                entityProxy = relationship.getEntityTwoProxy();
                            }
                            else if (repositoryHelper.isTypeOf(serviceName,
                                                               relationship.getType().getTypeDefName(),
                                                               OpenMetadataAPIMapper.CONNECTION_ENDPOINT_TYPE_NAME))
                            {
                                entityProxy = relationship.getEntityOneProxy();
                            }
                            if ((entityProxy != null) && (entityProxy.getGUID() != null) && (entityProxy.getType() != null))
                            {
                                final String entityGUIDParameterName = "embeddedRelationship proxy";
                                EntityDetail supplementaryEntity = this.getEntityFromRepository(userId,
                                                                                                entityProxy.getGUID(),
                                                                                                entityGUIDParameterName,
                                                                                                entityProxy.getType().getTypeDefName(),
                                                                                                null,
                                                                                                null,
                                                                                                forLineage,
                                                                                                forDuplicateProcessing,
                                                                                                supportedZones,
                                                                                                effectiveTime,
                                                                                                methodName);
                                if (supplementaryEntity != null)
                                {
                                    supplementaryEntities.add(supplementaryEntity);
                                }
                            }
                        }
                    }
                }
            }

            if (supplementaryEntities.isEmpty())
            {
                supplementaryEntities = null;
            }

            if (supplementaryRelationships.isEmpty())
            {
                supplementaryEntities = null;
            }

            return converter.getNewComplexBean(beanClass, assetEntity, supplementaryEntities, supplementaryRelationships, methodName);
        }

        return null;
    }


    /**
     * Recursively retrieve the list of relationships within a connection object.  The recursion occurs in VirtualConnections
     * that embed connections within themselves.
     *
     * @param userId calling user
     * @param connectionEntity entity for root connection object
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of relationships
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    private List<Relationship> getEmbeddedConnectionRelationships(String        userId,
                                                                  EntitySummary connectionEntity,
                                                                  boolean       forLineage,
                                                                  boolean       forDuplicateProcessing,
                                                                  Date          effectiveTime,
                                                                  String        methodName) throws PropertyServerException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   InvalidParameterException
    {
        List<Relationship> supplementaryRelationships = new ArrayList<>();

        if ((connectionEntity != null) && (connectionEntity.getType() != null))
        {
            RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                           invalidParameterHandler,
                                                                                           userId,
                                                                                           connectionEntity.getGUID(),
                                                                                           connectionEntity.getType().getTypeDefName(),
                                                                                           null,
                                                                                           null,
                                                                                           2,
                                                                                           forLineage,
                                                                                           forDuplicateProcessing,
                                                                                           0,
                                                                                           invalidParameterHandler.getMaxPagingSize(),
                                                                                           effectiveTime,
                                                                                           methodName);


            while (iterator.moreToReceive())
            {
                Relationship relationship = iterator.getNext();

                if ((relationship != null) && (relationship.getType() != null))
                {
                    if ((repositoryHelper.isTypeOf(serviceName,
                                                   relationship.getType().getTypeDefName(),
                                                   OpenMetadataAPIMapper.CONNECTION_ENDPOINT_TYPE_NAME))
                            || (repositoryHelper.isTypeOf(serviceName,
                                                          relationship.getType().getTypeDefName(),
                                                          OpenMetadataAPIMapper.CONNECTION_CONNECTOR_TYPE_TYPE_NAME))
                            || (repositoryHelper.isTypeOf(serviceName,
                                                          relationship.getType().getTypeDefName(),
                                                          OpenMetadataAPIMapper.CONNECTION_TO_ASSET_TYPE_NAME)))
                    {
                        supplementaryRelationships.add(relationship);
                    }
                    else if (repositoryHelper.isTypeOf(serviceName,
                                                       relationship.getType().getTypeDefName(),
                                                       OpenMetadataAPIMapper.EMBEDDED_CONNECTION_TYPE_NAME))
                    {
                        supplementaryRelationships.add(relationship);

                        EntityProxy embeddedConnectionEnd = relationship.getEntityTwoProxy();
                        if ((embeddedConnectionEnd != null) && (embeddedConnectionEnd.getGUID() != null))
                        {
                            List<Relationship> embeddedConnectionRelationships = this.getEmbeddedConnectionRelationships(userId,
                                                                                                                         embeddedConnectionEnd,
                                                                                                                         forLineage,
                                                                                                                         forDuplicateProcessing,
                                                                                                                         effectiveTime,
                                                                                                                         methodName);

                            if (embeddedConnectionRelationships != null)
                            {
                                supplementaryRelationships.addAll(embeddedConnectionRelationships);
                            }
                        }
                    }
                }
            }
        }

        if (supplementaryRelationships.isEmpty())
        {
            return null;
        }

        return supplementaryRelationships;
    }


    /**
     * Scan through the repository looking for assets by type.  The type name
     * may be null which means, all assets will be returned.
     *
     * @param userId calling user
     * @param subTypeGUID type of asset to scan for (null for all asset types)
     * @param subTypeName type of asset to scan for (null for all asset types)
     * @param startFrom scan pointer
     * @param pageSize maximum number of results
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of matching assets
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<String>  assetGUIDsScan(String       userId,
                                        String       subTypeGUID,
                                        String       subTypeName,
                                        int          startFrom,
                                        int          pageSize,
                                        boolean      forLineage,
                                        boolean      forDuplicateProcessing,
                                        Date         effectiveTime,
                                        String       methodName) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        return this.assetGUIDZoneScan(userId,
                                      null,
                                      subTypeGUID,
                                      subTypeName,
                                      supportedZones,
                                      startFrom,
                                      pageSize,
                                      forLineage,
                                      forDuplicateProcessing,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Scan through the repository looking for assets by type.  The type name
     * may be null which means, all assets will be returned.
     *
     * @param userId calling user
     * @param subTypeGUID type of asset to scan for (null for all asset types)
     * @param subTypeName type of asset to scan for (null for all asset types)
     * @param suppliedSupportedZones list of supported zones from calling service
     * @param startFrom scan pointer
     * @param pageSize maximum number of results
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of matching assets
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<String>  assetGUIDsScan(String       userId,
                                        String       subTypeGUID,
                                        String       subTypeName,
                                        List<String> suppliedSupportedZones,
                                        int          startFrom,
                                        int          pageSize,
                                        boolean      forLineage,
                                        boolean      forDuplicateProcessing,
                                        Date         effectiveTime,
                                        String       methodName) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        return this.assetGUIDZoneScan(userId,
                                      null,
                                      subTypeGUID,
                                      subTypeName,
                                      suppliedSupportedZones,
                                      startFrom,
                                      pageSize,
                                      forLineage,
                                      forDuplicateProcessing,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Scan through the repository looking for assets by type.  The type name
     * may be null which means, all assets will be returned.
     *
     * @param userId calling user
     * @param subTypeGUID type of asset to scan for (null for all asset types)
     * @param subTypeName type of asset to scan for (null for all asset types)
     * @param startFrom scan pointer
     * @param pageSize maximum number of results
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of matching assets
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B>  assetScan(String       userId,
                              String       subTypeGUID,
                              String       subTypeName,
                              int          startFrom,
                              int          pageSize,
                              boolean      forLineage,
                              boolean      forDuplicateProcessing,
                              Date         effectiveTime,
                              String       methodName) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        return this.assetZoneScan(userId,
                                  null,
                                  subTypeGUID,
                                  subTypeName,
                                  supportedZones,
                                  startFrom,
                                  pageSize,
                                  forLineage,
                                  forDuplicateProcessing,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Scan through the repository looking for assets by type.  The type name
     * may be null which means, all assets will be returned.
     *
     * @param userId calling user
     * @param subTypeGUID type of asset to scan for (null for all asset types)
     * @param subTypeName type of asset to scan for (null for all asset types)
     * @param startFrom scan pointer
     * @param pageSize maximum number of results
     * @param suppliedSupportedZones list of supported zones from calling service
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of matching assets
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B>  assetScan(String       userId,
                              String       subTypeGUID,
                              String       subTypeName,
                              List<String> suppliedSupportedZones,
                              int          startFrom,
                              int          pageSize,
                              boolean      forLineage,
                              boolean      forDuplicateProcessing,
                              Date         effectiveTime,
                              String       methodName) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        return this.assetZoneScan(userId,
                                  null,
                                  subTypeGUID,
                                  subTypeName,
                                  suppliedSupportedZones,
                                  startFrom,
                                  pageSize,
                                  forLineage,
                                  forDuplicateProcessing,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Scan through the repository looking for assets by type and/or zone.  The type name
     * may be null which means, all types of assets will be returned.  The zone name may be null
     * which means all supportedZones for the service are returned.
     *
     * @param userId calling user
     * @param zoneName name of zone to scan
     * @param subTypeName type of asset to scan for (null for all asset types)
     * @param startFrom scan pointer
     * @param pageSize maximum number of results
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of matching assets
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B>  assetZoneScan(String       userId,
                                  String       zoneName,
                                  String       subTypeName,
                                  int          startFrom,
                                  int          pageSize,
                                  boolean      forLineage,
                                  boolean      forDuplicateProcessing,
                                  Date         effectiveTime,
                                  String       methodName) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        if (subTypeName == null)
        {
            return this.assetZoneScan(userId,
                                      zoneName,
                                      null,
                                      null,
                                      supportedZones,
                                      startFrom,
                                      pageSize,
                                      forLineage,
                                      forDuplicateProcessing,
                                      effectiveTime,
                                      methodName);
        }
        else
        {
            String subTypeGUID = invalidParameterHandler.validateTypeName(subTypeName,
                                                                          OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                                          serviceName,
                                                                          methodName,
                                                                          repositoryHelper);

            return this.assetZoneScan(userId,
                                      zoneName,
                                      subTypeGUID,
                                      subTypeName,
                                      supportedZones,
                                      startFrom,
                                      pageSize,
                                      forLineage,
                                      forDuplicateProcessing,
                                      effectiveTime,
                                      methodName);
        }
    }

    /**
     * Scan through the repository looking for assets by type and/or zone.  The type name
     * may be null which means, all types of assets will be returned.  The zone name may be null
     * which means all supportedZones for the service are returned.
     *
     * @param userId calling user
     * @param zoneName name of zone to scan
     * @param subTypeGUID type of asset to scan for (null for all asset types)
     * @param subTypeName type of asset to scan for (null for all asset types)
     * @param suppliedSupportedZones list of supported zones from calling service
     * @param startFrom scan pointer
     * @param pageSize maximum number of results
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of matching assets
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B>  assetZoneScan(String       userId,
                                  String       zoneName,
                                  String       subTypeGUID,
                                  String       subTypeName,
                                  List<String> suppliedSupportedZones,
                                  int          startFrom,
                                  int          pageSize,
                                  boolean      forLineage,
                                  boolean      forDuplicateProcessing,
                                  Date         effectiveTime,
                                  String       methodName) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        String resultTypeGUID = OpenMetadataAPIMapper.ASSET_TYPE_GUID;
        String resultTypeName = OpenMetadataAPIMapper.ASSET_TYPE_NAME;

        if (subTypeGUID != null)
        {
            resultTypeGUID = subTypeGUID;
        }

        if (subTypeName != null)
        {
            resultTypeName = subTypeName;
        }

        List<B>  results = new ArrayList<>();

        if ((zoneName == null) || (suppliedSupportedZones == null) || (suppliedSupportedZones.contains(zoneName)))
        {
            RepositoryEntitiesIterator iterator = new RepositoryEntitiesIterator(repositoryHandler,
                                                                                 invalidParameterHandler,
                                                                                 userId,
                                                                                 resultTypeGUID,
                                                                                 resultTypeName,
                                                                                 OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                                 forLineage,
                                                                                 forDuplicateProcessing,
                                                                                 startFrom,
                                                                                 queryPageSize,
                                                                                 effectiveTime,
                                                                                 methodName);


            while ((iterator.moreToReceive()) && ((queryPageSize == 0) || results.size() < queryPageSize))
            {
                EntityDetail entity = iterator.getNext();

                if (entity != null)
                {
                    List<String> assetZones;

                    try
                    {
                        Classification classification = repositoryHelper.getClassificationFromEntity(serviceName,
                                                                                                     entity,
                                                                                                     OpenMetadataAPIMapper.ASSET_ZONES_CLASSIFICATION_NAME,
                                                                                                     methodName);

                        assetZones = repositoryHelper.getStringArrayProperty(serviceName,
                                                                             OpenMetadataAPIMapper.ZONE_MEMBERSHIP_PROPERTY_NAME,
                                                                             classification.getProperties(),
                                                                             methodName);
                    }
                    catch (ClassificationErrorException notPresent)
                    {
                        assetZones = repositoryHelper.getStringArrayProperty(serviceName,
                                                                             OpenMetadataAPIMapper.ZONE_MEMBERSHIP_PROPERTY_NAME,
                                                                             entity.getProperties(),
                                                                             methodName);
                    }

                    if ((assetZones != null) && (assetZones.contains(zoneName)))
                    {
                        results.add(converter.getNewBean(beanClass, entity, methodName));
                    }
                }
            }

            if (! results.isEmpty())
            {
                return results;
            }
        }

        return null;
    }


    /**
     * Scan through the repository looking for assets by type and/or zone.  The type name
     * may be null which means, all types of assets will be returned.  The zone name may be null
     * which means all supportedZones for the service are returned.
     *
     * @param userId calling user
     * @param zoneName name of zone to scan
     * @param subTypeGUID type of asset to scan for (null for all asset types)
     * @param subTypeName type of asset to scan for (null for all asset types)
     * @param suppliedSupportedZones list of supported zones from calling service
     * @param startFrom scan pointer
     * @param pageSize maximum number of results
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of GUIDs for matching assets
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<String>  assetGUIDZoneScan(String       userId,
                                           String       zoneName,
                                           String       subTypeGUID,
                                           String       subTypeName,
                                           List<String> suppliedSupportedZones,
                                           int          startFrom,
                                           int          pageSize,
                                           boolean      forLineage,
                                           boolean      forDuplicateProcessing,
                                           Date         effectiveTime,
                                           String       methodName) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        String resultTypeGUID = OpenMetadataAPIMapper.ASSET_TYPE_GUID;
        String resultTypeName = OpenMetadataAPIMapper.ASSET_TYPE_NAME;

        if (subTypeGUID != null)
        {
            resultTypeGUID = subTypeGUID;
        }

        if (subTypeName != null)
        {
            resultTypeName = subTypeName;
        }

        List<String>  results = new ArrayList<>();

        if ((zoneName == null) || (suppliedSupportedZones == null) || (suppliedSupportedZones.contains(zoneName)))
        {
            RepositoryEntitiesIterator iterator = new RepositoryEntitiesIterator(repositoryHandler,
                                                                                 invalidParameterHandler,
                                                                                 userId,
                                                                                 resultTypeGUID,
                                                                                 resultTypeName,
                                                                                 OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                                 forLineage,
                                                                                 forDuplicateProcessing,
                                                                                 startFrom,
                                                                                 queryPageSize,
                                                                                 effectiveTime,
                                                                                 methodName);


            while ((iterator.moreToReceive()) && ((queryPageSize == 0) || results.size() < queryPageSize))
            {
                EntityDetail entity = iterator.getNext();

                if (entity != null)
                {
                    List<String>    assetZones = repositoryHelper.getStringArrayProperty(serviceName,
                                                                                         OpenMetadataAPIMapper.ZONE_MEMBERSHIP_PROPERTY_NAME,
                                                                                         entity.getProperties(),
                                                                                         methodName);


                    if ((assetZones != null) && (assetZones.contains(zoneName)))
                    {
                        results.add(entity.getGUID());
                    }
                }
            }

            if (! results.isEmpty())
            {
                return results;
            }
        }

        return null;
    }


    /**
     * Return a list of assets with the requested name in either the display name or qualified name.
     * The match must be exact.  SupportedZones set up for this service is used.
     *
     * @param userId calling user
     * @param typeGUID unique identifier of the asset type to search for (null for the generic Asset type)
     * @param typeName unique identifier of the asset type to search for (null for the generic Asset type)
     * @param name name to search for - this is a regular expression (RegEx)
     * @param nameParameterName property that provided the name
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of B beans
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B> findAssetsByName(String   userId,
                                    String   typeGUID,
                                    String   typeName,
                                    String   name,
                                    String   nameParameterName,
                                    int      startFrom,
                                    int      pageSize,
                                    boolean  forLineage,
                                    boolean  forDuplicateProcessing,
                                    Date     effectiveTime,
                                    String   methodName) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        return this.findAssetsByName(userId,
                                     typeGUID,
                                     typeName,
                                     name,
                                     nameParameterName,
                                     supportedZones,
                                     startFrom,
                                     pageSize,
                                     forLineage,
                                     forDuplicateProcessing,
                                     effectiveTime,
                                     methodName);
    }


    /**
     * Return a list of assets with the requested name in either the display name or qualified name.
     *
     * @param userId calling user
     * @param typeGUID unique identifier of the asset type to search for (null for the generic Asset type)
     * @param typeName unique identifier of the asset type to search for (null for the generic Asset type)
     * @param name name to search for - this is a regular expression (RegEx)
     * @param nameParameterName property that provided the name
     * @param serviceSupportedZones list of supported zones for this service
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of B beans
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B> findAssetsByName(String        userId,
                                    String        typeGUID,
                                    String        typeName,
                                    String        name,
                                    String        nameParameterName,
                                    List<String>  serviceSupportedZones,
                                    int           startFrom,
                                    int           pageSize,
                                    boolean       forLineage,
                                    boolean       forDuplicateProcessing,
                                    Date          effectiveTime,
                                    String        methodName) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        String resultTypeGUID = OpenMetadataAPIMapper.ASSET_TYPE_GUID;
        String resultTypeName = OpenMetadataAPIMapper.ASSET_TYPE_NAME;

        if (typeGUID != null)
        {
            resultTypeGUID = typeGUID;
        }
        if (typeName != null)
        {
            resultTypeName = typeName;
        }

        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME);

        return this.getBeansByValue(userId,
                                    name,
                                    nameParameterName,
                                    resultTypeGUID,
                                    resultTypeName,
                                    specificMatchPropertyNames,
                                    true,
                                    null,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    serviceSupportedZones,
                                    OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                    startFrom,
                                    pageSize,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Return a list of assets with the requested name in either the display name or qualified name.
     * The match must be exact.  SupportedZones set up for this service is used.
     *
     * @param userId calling user
     * @param typeGUID unique identifier of the asset type to search for (null for the generic Asset type)
     * @param typeName unique identifier of the asset type to search for (null for the generic Asset type)
     * @param name name to search for - this must be an exact match either on the display name or the qualified name
     * @param nameParameterName property that provided the name
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of B beans
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<String> getAssetGUIDsByName(String   userId,
                                            String   typeGUID,
                                            String   typeName,
                                            String   name,
                                            String   nameParameterName,
                                            int      startFrom,
                                            int      pageSize,
                                            boolean  forLineage,
                                            boolean  forDuplicateProcessing,
                                            Date     effectiveTime,
                                            String   methodName) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        return this.getAssetGUIDsByName(userId,
                                        typeGUID,
                                        typeName,
                                        name,
                                        nameParameterName,
                                        supportedZones,
                                        startFrom,
                                        pageSize,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Return a list of assets with the requested name in either the display name or qualified name.
     * The match must be exact.
     *
     * @param userId calling user
     * @param typeGUID unique identifier of the asset type to search for (null for the generic Asset type)
     * @param typeName unique identifier of the asset type to search for (null for the generic Asset type)
     * @param name name to search for
     * @param nameParameterName property that provided the name
     * @param serviceSupportedZones list of supported zones for this service
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of B beans
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<String> getAssetGUIDsByName(String       userId,
                                            String       typeGUID,
                                            String       typeName,
                                            String       name,
                                            String       nameParameterName,
                                            List<String> serviceSupportedZones,
                                            int          startFrom,
                                            int          pageSize,
                                            boolean      forLineage,
                                            boolean      forDuplicateProcessing,
                                            Date         effectiveTime,
                                            String       methodName) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        String resultTypeGUID = OpenMetadataAPIMapper.ASSET_TYPE_GUID;
        String resultTypeName = OpenMetadataAPIMapper.ASSET_TYPE_NAME;

        if (typeGUID != null)
        {
            resultTypeGUID = typeGUID;
        }
        if (typeName != null)
        {
            resultTypeName = typeName;
        }

        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME);

        return this.getEntityGUIDsByValue(userId,
                                          name,
                                          nameParameterName,
                                          resultTypeGUID,
                                          resultTypeName,
                                          specificMatchPropertyNames,
                                          true,
                                          null,
                                          null,
                                          forLineage,
                                          forDuplicateProcessing,
                                          serviceSupportedZones,
                                          null,
                                          startFrom,
                                          pageSize,
                                          effectiveTime,
                                          methodName);
    }


    /**
     * Return a list of assets with the requested name in either the display name or qualified name.
     * The match must be exact.  SupportedZones set up for this service is used.
     *
     * @param userId calling user
     * @param typeGUID unique identifier of the asset type to search for (null for the generic Asset type)
     * @param typeName unique identifier of the asset type to search for (null for the generic Asset type)
     * @param name name to search for
     * @param nameParameterName property that provided the name
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param forDuplicateProcessing this request os for duplicate processing so do not deduplicate
     * @param forLineage this request is for lineage so ignore Memento classifications
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of B beans
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B> getAssetsByName(String   userId,
                                   String   typeGUID,
                                   String   typeName,
                                   String   name,
                                   String   nameParameterName,
                                   int      startFrom,
                                   int      pageSize,
                                   boolean  forLineage,
                                   boolean  forDuplicateProcessing,
                                   Date     effectiveTime,
                                   String   methodName) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        return this.getAssetsByName(userId,
                                    typeGUID,
                                    typeName,
                                    name,
                                    nameParameterName,
                                    supportedZones,
                                    startFrom,
                                    pageSize,
                                    forLineage,
                                    forDuplicateProcessing,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Return a list of assets with the requested name in either the display name or qualified name.
     * The match must be exact.
     *
     * @param userId calling user
     * @param typeGUID unique identifier of the asset type to search for (null for the generic Asset type)
     * @param typeName unique identifier of the asset type to search for (null for the generic Asset type)
     * @param name name to search for - this must be an exact match on the display name or qualified name
     * @param nameParameterName property that provided the name
     * @param serviceSupportedZones list of supported zones for this service
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param forDuplicateProcessing this request os for duplicate processing so do not deduplicate
     * @param forLineage this request is for lineage so ignore Memento classifications
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of B beans
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B> getAssetsByName(String       userId,
                                   String       typeGUID,
                                   String       typeName,
                                   String       name,
                                   String       nameParameterName,
                                   List<String> serviceSupportedZones,
                                   int          startFrom,
                                   int          pageSize,
                                   boolean      forLineage,
                                   boolean      forDuplicateProcessing,
                                   Date         effectiveTime,
                                   String       methodName) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        String resultTypeGUID = OpenMetadataAPIMapper.ASSET_TYPE_GUID;
        String resultTypeName = OpenMetadataAPIMapper.ASSET_TYPE_NAME;

        if (typeGUID != null)
        {
            resultTypeGUID = typeGUID;
        }
        if (typeName != null)
        {
            resultTypeName = typeName;
        }

        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME);

        return this.getBeansByValue(userId,
                                    name,
                                    nameParameterName,
                                    resultTypeGUID,
                                    resultTypeName,
                                    specificMatchPropertyNames,
                                    true,
                                    null,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    serviceSupportedZones,
                                    null,
                                    startFrom,
                                    pageSize,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Return a list of assets with the requested metadataCollectionId.
     *
     * @param userId calling user
     * @param typeGUID unique identifier of the asset type to search for (null for generic Asset)
     * @param typeName unique name of the asset type to search for (null for generic Asset)
     * @param metadataCollectionId unique identifier of metadataCollection to search for
     * @param metadataCollectionIdParameterName parameter providing the metadata collection id
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param forDuplicateProcessing this request os for duplicate processing so do not deduplicate
     * @param forLineage this request is for lineage so ignore Memento classifications
     * @param effectiveTime             the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of B beans
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<B> getAssetsByMetadataCollectionId(String   userId,
                                            String   typeGUID,
                                            String   typeName,
                                            String   metadataCollectionId,
                                            String   metadataCollectionIdParameterName,
                                            int      startFrom,
                                            int      pageSize,
                                            boolean  forLineage,
                                            boolean  forDuplicateProcessing,
                                            Date     effectiveTime,
                                            String   methodName) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        List<String> specificMatchPropertyNames = new ArrayList<>();

        specificMatchPropertyNames.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.NAME_PROPERTY_NAME);
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME);

        String resultTypeGUID = OpenMetadataAPIMapper.ASSET_TYPE_GUID;
        String resultTypeName = OpenMetadataAPIMapper.ASSET_TYPE_NAME;

        if (typeGUID != null)
        {
            resultTypeGUID = typeGUID;
        }
        if (typeName != null)
        {
            resultTypeName = typeName;
        }

        return this.getBeansByValue(userId,
                                    metadataCollectionId,
                                    metadataCollectionIdParameterName,
                                    resultTypeGUID,
                                    resultTypeName,
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
     * Return the list of assets that have the same endpoint address.
     *
     * @param userId calling user
     * @param networkAddress address to query on
     * @param networkAddressParameterName parameter name supplying networkAddress
     * @param startFrom place to start in query
     * @param pageSize number of results to return
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of unique identifiers for matching assets
     *
     * @throws InvalidParameterException the networkAddress is invalid
     * @throws PropertyServerException there is a problem access in the property server
     * @throws UserNotAuthorizedException the user does not have access to the properties
     */
    public  List<String> getAssetGUIDsByEndpoint(String  userId,
                                                 String  networkAddress,
                                                 String  networkAddressParameterName,
                                                 int     startFrom,
                                                 int     pageSize,
                                                 boolean forLineage,
                                                 boolean forDuplicateProcessing,
                                                 Date    effectiveTime,
                                                 String  methodName) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        int queryPageSize      = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);
        int maxPageSize        = invalidParameterHandler.getMaxPagingSize();
        int startNextQueryFrom = 0;

        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.NETWORK_ADDRESS_PROPERTY_NAME);

        List<String> relationshipPath = new ArrayList<>();
        relationshipPath.add(OpenMetadataAPIMapper.CONNECTION_ENDPOINT_TYPE_GUID);
        relationshipPath.add(OpenMetadataAPIMapper.ASSET_TO_CONNECTION_TYPE_GUID);

        List<String>  assetGUIDs     = new ArrayList<>();
        List<String>  resultGUIDs    = new ArrayList<>();
        boolean moreResultsAvailable = true;

        while (moreResultsAvailable && ((queryPageSize == 0) || (resultGUIDs.size() < queryPageSize)))
        {
            List<String> endpointGUIDs = this.getEntityGUIDsByValue(userId,
                                                                    networkAddress,
                                                                    networkAddressParameterName,
                                                                    OpenMetadataAPIMapper.ENDPOINT_TYPE_GUID,
                                                                    OpenMetadataAPIMapper.ENDPOINT_TYPE_NAME,
                                                                    specificMatchPropertyNames,
                                                                    true,
                                                                    null,
                                                                    null,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    supportedZones,
                                                                    null,
                                                                    startNextQueryFrom,
                                                                    maxPageSize,
                                                                    effectiveTime,
                                                                    methodName);


            if (endpointGUIDs == null)
            {
                moreResultsAvailable = false;
            }
            else /* from each endpoint, navigate to the asset. The same asset may be returned multiple times */
            {
                for (String endpointGUID : endpointGUIDs)
                {
                    if (endpointGUID != null)
                    {
                        boolean moreResultsFromEndpoint = true;
                        int     endpointStartFrom = 0;

                        while (moreResultsFromEndpoint && ((queryPageSize == 0) || (resultGUIDs.size() < queryPageSize)))
                        {
                            List<String> endpointAssetGUIDs = this.getRelatedEntityGUIDs(userId,
                                                                                         endpointGUID,
                                                                                         networkAddressParameterName,
                                                                                         OpenMetadataAPIMapper.ENDPOINT_TYPE_NAME,
                                                                                         relationshipPath,
                                                                                         OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                                                         supportedZones,
                                                                                         endpointStartFrom,
                                                                                         maxPageSize,
                                                                                         effectiveTime,
                                                                                         methodName);

                            if ((endpointAssetGUIDs == null) || (endpointAssetGUIDs.isEmpty()))
                            {
                                moreResultsFromEndpoint = false;
                            }
                            else
                            {
                                /*
                                 * The generic handler will have removed duplicates from the list it returns.
                                 * This method needs to ensure that assets returned from different endpoints do not
                                 * cause the same asset to appear twice in the list.
                                 */
                                for (String endpointAssetGUID : endpointAssetGUIDs)
                                {
                                    if (endpointAssetGUID != null)
                                    {
                                        if (! assetGUIDs.contains(endpointAssetGUID))
                                        {
                                            assetGUIDs.add(endpointAssetGUID);
                                            if (assetGUIDs.size() > startFrom)
                                            {
                                                resultGUIDs.add(endpointAssetGUID);
                                            }
                                        }
                                    }
                                }

                                endpointStartFrom = endpointStartFrom + endpointAssetGUIDs.size();
                            }
                        }
                    }
                }
            }
        }

        if (resultGUIDs.isEmpty())
        {
            return null;
        }
        else
        {
            return resultGUIDs;
        }
    }


    /**
     * Return the list of assets that have the same endpoint address.
     *
     * @param userId calling user
     * @param networkAddress address to query on
     * @param networkAddressParameterName name of parameter passing the network address
     * @param suppliedTypeName name of asset subtype to validate
     * @param startFrom place to start in query
     * @param pageSize number of results to return
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of  matching assets
     *
     * @throws InvalidParameterException the networkAddress is invalid
     * @throws PropertyServerException there is a problem access in the property server
     * @throws UserNotAuthorizedException the user does not have access to the properties
     */
    public  List<B> getAssetsByEndpoint(String  userId,
                                        String  networkAddress,
                                        String  networkAddressParameterName,
                                        String  suppliedTypeName,
                                        int     startFrom,
                                        int     pageSize,
                                        boolean forLineage,
                                        boolean forDuplicateProcessing,
                                        Date    effectiveTime,
                                        String  methodName) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        String assetTypeName = OpenMetadataAPIMapper.ASSET_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            assetTypeName = suppliedTypeName;
        }

        List<String> assetGUIDs = this.getAssetGUIDsByEndpoint(userId,
                                                               networkAddress,
                                                               networkAddressParameterName,
                                                               startFrom,
                                                               pageSize,
                                                               forLineage,
                                                               forDuplicateProcessing,
                                                               effectiveTime,
                                                               methodName);

        if ((assetGUIDs == null) || (assetGUIDs.isEmpty()))
        {
            return null;
        }
        else
        {
            List<B> assets = new ArrayList<>();

            for (String assetGUID : assetGUIDs)
            {
                if (assetGUID != null)
                {
                    B bean = this.getBeanFromRepository(userId,
                                                        assetGUID,
                                                        networkAddressParameterName,
                                                        assetTypeName,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        supportedZones,
                                                        effectiveTime,
                                                        methodName);

                    assets.add(bean);
                }
            }

            if (assets.isEmpty())
            {
                return null;
            }

            return assets;
        }
    }


    /**
     * Return a list of assets with the requested search string in their name, qualified name
     * or description.
     *
     * @param userId calling user
     * @param searchString string to search for in text
     * @param searchStringParameter name of parameter supplying the search string
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of unique identifiers for assets that match the search string.
     *
     * @throws InvalidParameterException the searchString is invalid
     * @throws PropertyServerException there is a problem access in the property server
     * @throws UserNotAuthorizedException the user does not have access to the properties
     */
    public List<String>  findAssetGUIDs(String   userId,
                                        String   searchString,
                                        String   searchStringParameter,
                                        int      startFrom,
                                        int      pageSize,
                                        boolean  forLineage,
                                        boolean  forDuplicateProcessing,
                                        Date     effectiveTime,
                                        String   methodName) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        return this.findAssetGUIDs(userId,
                                   OpenMetadataAPIMapper.ASSET_TYPE_GUID,
                                   OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                   searchString,
                                   searchStringParameter,
                                   startFrom,
                                   pageSize,
                                   forLineage,
                                   forDuplicateProcessing,
                                   effectiveTime,
                                   methodName);
    }


    /**
     * Return a list of assets with the requested search string in their name, qualified name
     * or description.
     *
     * @param userId calling user
     * @param typeGUID unique identifier of the asset type to search for - null for generic Asset
     * @param typeName unique name of the asset type to search for - null for generic Asset
     * @param searchString string to search for in text
     * @param searchStringParameter parameter providing search string
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of unique identifiers for assets that match the search string.
     *
     * @throws InvalidParameterException the searchString is invalid
     * @throws PropertyServerException there is a problem access in the property server
     * @throws UserNotAuthorizedException the user does not have access to the properties
     */
    public List<String>  findAssetGUIDs(String   userId,
                                        String   typeGUID,
                                        String   typeName,
                                        String   searchString,
                                        String   searchStringParameter,
                                        int      startFrom,
                                        int      pageSize,
                                        boolean  forLineage,
                                        boolean  forDuplicateProcessing,
                                        Date     effectiveTime,
                                        String   methodName) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        String resultTypeGUID = OpenMetadataAPIMapper.ASSET_TYPE_GUID;
        String resultTypeName = OpenMetadataAPIMapper.ASSET_TYPE_NAME;

        if (typeGUID != null)
        {
            resultTypeGUID = typeGUID;
        }
        if (typeName != null)
        {
            resultTypeName = typeName;
        }

        return this.getEntityGUIDsByValue(userId,
                                          searchString,
                                          searchStringParameter,
                                          resultTypeGUID,
                                          resultTypeName,
                                          null,
                                          false,
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
     * Return a list of assets with the requested search string in their name, qualified name
     * or description.
     *
     * @param userId calling user
     * @param searchString string to search for in text
     * @param searchStringParameter parameter providing search string
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of assets that match the search string.
     *
     * @throws InvalidParameterException the searchString is invalid
     * @throws PropertyServerException there is a problem access in the property server
     * @throws UserNotAuthorizedException the user does not have access to the properties
     */
    public List<B>  findAssets(String   userId,
                               String   searchString,
                               String   searchStringParameter,
                               int      startFrom,
                               int      pageSize,
                               boolean  forLineage,
                               boolean  forDuplicateProcessing,
                               Date     effectiveTime,
                               String   methodName) throws InvalidParameterException,
                                                           PropertyServerException,
                                                           UserNotAuthorizedException
    {
        return this.findAssets(userId,
                               OpenMetadataAPIMapper.ASSET_TYPE_GUID,
                               OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                               searchString,
                               searchStringParameter,
                               startFrom,
                               pageSize,
                               forLineage,
                               forDuplicateProcessing,
                               effectiveTime,
                               methodName);
    }


    /**
     * Return a list of assets with the requested search string in their name, qualified name
     * or description.
     *
     * @param userId calling user
     * @param typeGUID unique identifier of the asset type to search for
     * @param typeName unique name of the asset type to search for
     * @param searchString string to search for in text
     * @param searchStringParameter parameter providing search string
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of assets that match the search string.
     *
     * @throws InvalidParameterException the searchString is invalid
     * @throws PropertyServerException there is a problem access in the property server
     * @throws UserNotAuthorizedException the user does not have access to the properties
     */
    public List<B>  findAssets(String  userId,
                               String  typeGUID,
                               String  typeName,
                               String  searchString,
                               String  searchStringParameter,
                               int     startFrom,
                               int     pageSize,
                               boolean forLineage,
                               boolean forDuplicateProcessing,
                               Date    effectiveTime,
                               String  methodName) throws InvalidParameterException,
                                                          PropertyServerException,
                                                          UserNotAuthorizedException
    {
        String resultTypeGUID = OpenMetadataAPIMapper.ASSET_TYPE_GUID;
        String resultTypeName = OpenMetadataAPIMapper.ASSET_TYPE_NAME;

        if (typeGUID != null)
        {
            resultTypeGUID = typeGUID;
        }
        if (typeName != null)
        {
            resultTypeName = typeName;
        }

        /*
         * Notice that the parameter order for findBeans is different from findAssets
         */
        return this.findBeans(userId,
                              searchString,
                              searchStringParameter,
                              resultTypeGUID,
                              resultTypeName,
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
     * Return the list of unique identifiers for assets that are linked to a specific tag either directly, or via one
     * of its schema elements.
     *
     * @param userId the name of the calling user.
     * @param tagGUID unique identifier of tag.
     * @param tagGUIDParameterName name of parameter supplying the GUID
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return asset guid list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<String> getAssetGUIDsByTag(String  userId,
                                           String  tagGUID,
                                           String  tagGUIDParameterName,
                                           int     startFrom,
                                           int     pageSize,
                                           boolean forLineage,
                                           boolean forDuplicateProcessing,
                                           Date    effectiveTime,
                                           String  methodName) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        return this.getAttachedElementGUIDs(userId,
                                            tagGUID,
                                            tagGUIDParameterName,
                                            OpenMetadataAPIMapper.INFORMAL_TAG_TYPE_NAME,
                                            OpenMetadataAPIMapper.REFERENCEABLE_TO_TAG_TYPE_GUID,
                                            OpenMetadataAPIMapper.REFERENCEABLE_TO_TAG_TYPE_NAME,
                                            OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                            forLineage,
                                            forDuplicateProcessing,
                                            supportedZones,
                                            startFrom,
                                            pageSize,
                                            effectiveTime,
                                            methodName);
    }


    /**
     * Return the list of assets that are linked to a specific tag either directly, or via one
     * of its schema elements.
     *
     * @param userId the name of the calling user.
     * @param tagGUID unique identifier of tag.
     * @param tagGUIDParameterName name of parameter supplying the GUID
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return asset guid list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B> getAssetsByTag(String  userId,
                                  String  tagGUID,
                                  String  tagGUIDParameterName,
                                  int     startFrom,
                                  int     pageSize,
                                  boolean forLineage,
                                  boolean forDuplicateProcessing,
                                  Date    effectiveTime,
                                  String  methodName) throws InvalidParameterException,
                                                             PropertyServerException,
                                                             UserNotAuthorizedException
    {
        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        tagGUID,
                                        tagGUIDParameterName,
                                        OpenMetadataAPIMapper.INFORMAL_TAG_TYPE_NAME,
                                        OpenMetadataAPIMapper.REFERENCEABLE_TO_TAG_TYPE_GUID,
                                        OpenMetadataAPIMapper.REFERENCEABLE_TO_TAG_TYPE_NAME,
                                        OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                        null,
                                        null,
                                        0,
                                        forLineage,
                                        forDuplicateProcessing,
                                        supportedZones,
                                        startFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Return the list of unique identifiers for assets that are linked to a specific keyword either directly, or via one
     * of its schema elements.
     *
     * @param userId the name of the calling user.
     * @param keywordGUID unique identifier of keyword.
     * @param keywordGUIDParameterName name of parameter supplying the guid
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return asset guid list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<String> getAssetGUIDsByKeyword(String  userId,
                                               String  keywordGUID,
                                               String  keywordGUIDParameterName,
                                               int     startFrom,
                                               int     pageSize,
                                               boolean forLineage,
                                               boolean forDuplicateProcessing,
                                               Date    effectiveTime,
                                               String  methodName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        return this.getAttachedElementGUIDs(userId,
                                            keywordGUID,
                                            keywordGUIDParameterName,
                                            OpenMetadataAPIMapper.SEARCH_KEYWORD_TYPE_NAME,
                                            OpenMetadataAPIMapper.SEARCH_KEYWORD_TO_RELATED_KEYWORD_TYPE_GUID,
                                            OpenMetadataAPIMapper.SEARCH_KEYWORD_TO_RELATED_KEYWORD_TYPE_NAME,
                                            OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                            forLineage,
                                            forDuplicateProcessing,
                                            supportedZones,
                                            startFrom,
                                            pageSize,
                                            effectiveTime,
                                            methodName);
    }


    /**
     * Return the list of assets that are linked to a specific keyword.
     *
     * @param userId the name of the calling user.
     * @param keywordGUID unique identifier of keyword.
     * @param keywordGUIDParameterName name of parameter supplying the guid
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return asset guid list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B> getAssetsByKeyword(String  userId,
                                      String  keywordGUID,
                                      String  keywordGUIDParameterName,
                                      int     startFrom,
                                      int     pageSize,
                                      boolean forLineage,
                                      boolean forDuplicateProcessing,
                                      Date    effectiveTime,
                                      String  methodName) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        keywordGUID,
                                        keywordGUIDParameterName,
                                        OpenMetadataAPIMapper.SEARCH_KEYWORD_TYPE_NAME,
                                        OpenMetadataAPIMapper.SEARCH_KEYWORD_TO_RELATED_KEYWORD_TYPE_GUID,
                                        OpenMetadataAPIMapper.SEARCH_KEYWORD_TO_RELATED_KEYWORD_TYPE_NAME,
                                        OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                        null,
                                        null,
                                        0,
                                        forLineage,
                                        forDuplicateProcessing,
                                        supportedZones,
                                        startFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Return the list of assets that are linked to a specific glossary term.
     *
     * @param userId the name of the calling user.
     * @param termGUID unique identifier of term.
     * @param termGUIDParameterName name of parameter supplying the guid
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return asset guid list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B> getAssetsByGlossaryTerm(String       userId,
                                           String       termGUID,
                                           String       termGUIDParameterName,
                                           int          startFrom,
                                           int          pageSize,
                                           boolean      forLineage,
                                           boolean      forDuplicateProcessing,
                                           Date         effectiveTime,
                                           String       methodName) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        termGUID,
                                        termGUIDParameterName,
                                        OpenMetadataAPIMapper.GLOSSARY_TYPE_NAME,
                                        OpenMetadataAPIMapper.REFERENCEABLE_TO_MEANING_TYPE_GUID,
                                        OpenMetadataAPIMapper.REFERENCEABLE_TO_MEANING_TYPE_NAME,
                                        OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                        null,
                                        null,
                                        0,
                                        forLineage,
                                        forDuplicateProcessing,
                                        supportedZones,
                                        startFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }
}
