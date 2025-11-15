/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.sync;

import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCAuditCode;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.properties.SchemaInfo;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.properties.VolumeInfo;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.OSSUnityCatalogResourceConnector;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.integration.iterator.IntegrationIterator;
import org.odpi.openmetadata.frameworks.integration.iterator.MemberAction;
import org.odpi.openmetadata.frameworks.integration.iterator.MemberElement;
import org.odpi.openmetadata.frameworks.integration.iterator.RelatedElementsIterator;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.mapper.PropertyFacetValidValues;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.DataFileCollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.TemplateOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.UpdateOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides the specialist methods for working with Unity Catalog (UC) Volumes.
 */
public class OSSUnityCatalogInsideCatalogSyncVolumes extends OSSUnityCatalogInsideCatalogSyncBase
{
    /**
     * Set up the volume synchronizer.
     *
     * @param connectorName name of this connector
     * @param context context for the connector
     * @param catalogTargetName the catalog target name
     * @param catalogGUID guid of the catalog
     * @param catalogName name of the catalog
     * @param ucFullNameToEgeriaGUID map of full names from UC to the GUID of the entity in Egeria.
     * @param targetPermittedSynchronization the policy that controls the direction of metadata exchange
     * @param ucConnector connector for accessing UC
     * @param ucServerEndpoint the server endpoint used to constructing the qualified names
     * @param templates templates supplied through the catalog target
     * @param configurationProperties configuration properties supplied through the catalog target
     * @param excludeNames list of catalogs to ignore (and include all others)
     * @param includeNames list of catalogs to include (and ignore all others) - overrides excludeCatalogs
     * @param auditLog logging destination
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    public OSSUnityCatalogInsideCatalogSyncVolumes(String                           connectorName,
                                                   IntegrationContext               context,
                                                   String                           catalogTargetName,
                                                   String                           catalogGUID,
                                                   String                           catalogName,
                                                   Map<String, String>              ucFullNameToEgeriaGUID,
                                                   PermittedSynchronization         targetPermittedSynchronization,
                                                   OSSUnityCatalogResourceConnector ucConnector,
                                                   String                           ucServerEndpoint,
                                                   Map<String, String>              templates,
                                                   Map<String, Object>              configurationProperties,
                                                   List<String>                     excludeNames,
                                                   List<String>                     includeNames,
                                                   AuditLog                         auditLog) throws UserNotAuthorizedException,
                                                                                                     InvalidParameterException
    {
        super(connectorName,
              context,
              catalogTargetName,
              catalogGUID,
              catalogName,
              ucFullNameToEgeriaGUID,
              targetPermittedSynchronization,
              ucConnector,
              ucServerEndpoint,
              OpenMetadataType.DATA_FILE_COLLECTION.typeName,
              UnityCatalogDeployedImplementationType.OSS_UC_VOLUME,
              templates,
              configurationProperties,
              excludeNames,
              includeNames,
              auditLog);
    }


    /**
     * Review all the volumes stored in Egeria.
     *
     * @param parentGUID unique identifier of the parent
     * @param parentRelationshipTypeName relationship type between parent and elements to iterate through
     * @param relationshipProperties optional properties for the relationship
     * @return iterator
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException security error
     */
    protected IntegrationIterator refreshEgeria(String                     parentGUID,
                                                String                     parentRelationshipTypeName,
                                                RelationshipBeanProperties relationshipProperties) throws InvalidParameterException,
                                                                                                          PropertyServerException,
                                                                                                          UserNotAuthorizedException, ConnectorCheckedException
    {
        RelatedElementsIterator volumeIterator = new RelatedElementsIterator(context.getMetadataSourceGUID(),
                                                                            catalogTargetName,
                                                                            connectorName,
                                                                            parentGUID,
                                                                            parentRelationshipTypeName,
                                                                            1,
                                                                            entityTypeName,
                                                                            context,
                                                                            targetPermittedSynchronization,
                                                                            context.getMaxPageSize(),
                                                                            auditLog);

        while (volumeIterator.moreToReceive())
        {
            MemberElement nextElement = volumeIterator.getNextMember();

            if ((nextElement != null) && (nextElement.getElement() != null) && (nextElement.getElement().getProperties() instanceof DataFileCollectionProperties dataFileCollectionProperties))
            {
                /*
                 * Check that this is a Volume and not part of a table.
                 */
                if (UnityCatalogDeployedImplementationType.OSS_UC_VOLUME.getDeployedImplementationType().equals(dataFileCollectionProperties.getDeployedImplementationType()))
                {
                    VolumeInfo volumeInfo = null;

                    String volumeName = dataFileCollectionProperties.getResourceName();

                    if (context.elementShouldBeCatalogued(volumeName, excludeNames, includeNames))
                    {
                        try
                        {
                            volumeInfo = ucConnector.getVolume(volumeName);
                        }
                        catch (Exception missing)
                        {
                            // this is not necessarily an error
                        }

                        MemberAction memberAction = MemberAction.NO_ACTION;
                        if (volumeInfo == null)
                        {
                            memberAction = nextElement.getMemberAction(null, null);
                        }
                        else if (noMismatchInExternalIdentifier(volumeInfo.getVolume_id(), nextElement))
                        {
                            memberAction = nextElement.getMemberAction(this.getDateFromLong(volumeInfo.getCreated_at()),
                                                                       this.getDateFromLong(volumeInfo.getUpdated_at()));
                        }

                        this.takeAction(parentGUID,
                                        super.getUCSchemaFromMember(nextElement),
                                        memberAction,
                                        nextElement,
                                        volumeInfo);
                    }
                }
            }
        }

        return volumeIterator;
    }


    /**
     * Review all the volumes stored in UC.
     *
     * @param parentGUID unique identifier of the parent
     * @param parentRelationshipTypeName relationship type between parent and elements to iterate through
     * @param relationshipProperties optional properties for the relationship
     * @param iterator  Metadata collection iterator
     *
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException security error
     * @throws ConnectorCheckedException logic error in properties
     */
    protected void refreshUnityCatalog(String                     parentGUID,
                                       String                     parentRelationshipTypeName,
                                       RelationshipBeanProperties relationshipProperties,
                                       IntegrationIterator        iterator) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException,
                                                                                   ConnectorCheckedException
    {
        List<SchemaInfo> ucSchemaList = ucConnector.listSchemas(catalogTargetName);

        if (ucSchemaList != null)
        {
            for (SchemaInfo schemaInfo : ucSchemaList)
            {
                if (schemaInfo != null)
                {
                    String schemaGUID = ucFullNameToEgeriaGUID.get(schemaInfo.getFull_name());

                    if (schemaGUID != null)
                    {
                        List<VolumeInfo> ucVolumeList = ucConnector.listVolumes(catalogTargetName, schemaInfo.getName());

                        if (ucVolumeList != null)
                        {
                            for (VolumeInfo volumeInfo : ucVolumeList)
                            {
                                if (volumeInfo != null)
                                {
                                    if (ucFullNameToEgeriaGUID.get(volumeInfo.getFull_name()) == null)
                                    {
                                        String ucVolumeQualifiedName = this.getQualifiedName(volumeInfo.getFull_name());

                                        MemberElement memberElement = iterator.getMemberByQualifiedName(ucVolumeQualifiedName);
                                        MemberAction memberAction = memberElement.getMemberAction(this.getDateFromLong(volumeInfo.getCreated_at()),
                                                                                                  this.getDateFromLong(volumeInfo.getUpdated_at()));
                                        if (noMismatchInExternalIdentifier(volumeInfo.getVolume_id(), memberElement))
                                        {
                                            this.takeAction(schemaGUID, volumeInfo.getSchema_name(), memberAction, memberElement, volumeInfo);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * Use the member action enum to request that the correct action is taken.
     *
     * @param schemaGUID unique identifier of the schema in Egeria
     * @param memberAction enum
     * @param memberElement element from egeria
     * @param volumeInfo element from UC
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException security error
     * @throws ConnectorCheckedException logic error in properties
     */
    private void takeAction(String        schemaGUID,
                            String        schemaName,
                            MemberAction  memberAction,
                            MemberElement memberElement,
                            VolumeInfo    volumeInfo) throws InvalidParameterException,
                                                             PropertyServerException,
                                                             UserNotAuthorizedException,
                                                             ConnectorCheckedException
    {
        switch (memberAction)
        {
            case CREATE_INSTANCE_IN_OPEN_METADATA -> this.createElementInEgeria(schemaGUID, volumeInfo);
            case UPDATE_INSTANCE_IN_OPEN_METADATA -> this.updateElementInEgeria(volumeInfo, memberElement);
            case DELETE_INSTANCE_IN_OPEN_METADATA -> this.deleteElementInEgeria(memberElement);
            case CREATE_INSTANCE_IN_THIRD_PARTY   -> this.createElementInThirdParty(schemaName, memberElement);
            case UPDATE_INSTANCE_IN_THIRD_PARTY   -> this.updateElementInThirdParty(volumeInfo, memberElement);
            case DELETE_INSTANCE_IN_THIRD_PARTY   -> this.deleteElementInThirdParty(volumeInfo);
        }
    }


    /**
     * Create a volume element in open metadata.
     *
     * @param volumeInfo object from UC
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    private void createElementInEgeria(String     schemaGUID,
                                       VolumeInfo volumeInfo) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String parentLinkTypeName = OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName;
        final boolean parentAtEnd1 = true;

        ElementProperties replacementProperties = propertyHelper.addStringProperty(null,
                                                                                   OpenMetadataProperty.PATH_NAME.name,
                                                                                   super.getPathNameFromStorageLocation(volumeInfo.getStorage_location()));

        TemplateOptions templateOptions = new TemplateOptions(assetClient.getMetadataSourceOptions());

        templateOptions.setAnchorGUID(schemaGUID);
        templateOptions.setIsOwnAnchor(false);
        templateOptions.setAnchorScopeGUID(UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getGUID());

        templateOptions.setParentGUID(schemaGUID);
        templateOptions.setParentAtEnd1(parentAtEnd1);
        templateOptions.setParentRelationshipTypeName(parentLinkTypeName);

        String ucVolumeGUID = openMetadataStore.createMetadataElementFromTemplate(deployedImplementationType.getAssociatedTypeName(),
                                                                           templateOptions,
                                                                           templateGUID,
                                                                           replacementProperties,
                                                                           this.getPlaceholderProperties(volumeInfo),
                                                                           null);

        super.addExternalIdentifier(ucVolumeGUID,
                                    volumeInfo,
                                    volumeInfo.getSchema_name(),
                                    UnityCatalogPlaceholderProperty.VOLUME_NAME.getName(),
                                    "volume",
                                    volumeInfo.getVolume_id(),
                                    PermittedSynchronization.FROM_THIRD_PARTY);

        ucFullNameToEgeriaGUID.put(volumeInfo.getFull_name(), ucVolumeGUID);
    }


    /**
     * Using the information from UC, update the elements Egeria.
     *
     * @param volumeInfo info from UC
     * @param memberElement elements from Egeria
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    private void updateElementInEgeria(VolumeInfo    volumeInfo,
                                       MemberElement memberElement) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        String egeriaVolumeGUID = memberElement.getElement().getElementHeader().getGUID();

        UpdateOptions updateOptions = new UpdateOptions(assetClient.getUpdateOptions(true));

        openMetadataStore.updateMetadataElementInStore(egeriaVolumeGUID,
                                                       updateOptions,
                                                       this.getElementProperties(volumeInfo));

        externalIdClient.confirmSynchronization(memberElement.getElement(), volumeInfo.getVolume_id());
    }


    /**
     * Create an equivalent element in UC.
     *
     * @param memberElement elements from Egeria
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error or problem communicating with UC
     * @throws UserNotAuthorizedException authorization error
     * @throws ConnectorCheckedException problem with properties
     */
    private void createElementInThirdParty(String        schemaName,
                                           MemberElement memberElement) throws PropertyServerException,
                                                                               InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               ConnectorCheckedException
    {
        VolumeInfo volumeInfo = ucConnector.createVolume(super.getUCNameFromMember(memberElement),
                                                         catalogTargetName,
                                                         schemaName,
                                                         super.getUCCommentFomMember(memberElement),
                                                         this.getUCVolumeTypeFromMember(memberElement),
                                                         super.getUCStorageLocationFromMember(memberElement));

        if (memberElement.getExternalIdentifier() == null)
        {
            super.addExternalIdentifier(memberElement.getElement().getElementHeader().getGUID(),
                                        volumeInfo,
                                        volumeInfo.getSchema_name(),
                                        UnityCatalogPlaceholderProperty.VOLUME_NAME.getName(),
                                        "volume",
                                        volumeInfo.getVolume_id(),
                                        PermittedSynchronization.TO_THIRD_PARTY);
        }
        else
        {
            externalIdClient.confirmSynchronization(memberElement.getElement(), volumeInfo.getVolume_id());
        }
    }


    /**
     * Retrieve the value for volume type.  This is stored in the property facet.
     *
     * @param memberElement elements from Egeria
     * @return volume type enum
     */
    private String getUCVolumeTypeFromMember(MemberElement memberElement)
    {
        Map<String, String> vendorProperties = memberElement.getVendorProperties(PropertyFacetValidValues.UNITY_CATALOG_SOURCE_VALUE);

        if (vendorProperties != null)
        {
            return vendorProperties.get(UnityCatalogPlaceholderProperty.VOLUME_TYPE.getName());
        }

        return null;
    }


    /**
     * Update the element in Unity Catalog using the values from Egeria.
     *
     * @param volumeInfo info
     * @param memberElement elements from Egeria
     *
     * @throws InvalidParameterException bad call to Egeria
     * @throws UserNotAuthorizedException security problem
     * @throws PropertyServerException problem communicating with UC
     */
    private void updateElementInThirdParty(VolumeInfo    volumeInfo,
                                           MemberElement memberElement) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException
    {
        final String methodName = "updateElementInThirdParty";

        auditLog.logMessage(methodName,
                            UCAuditCode.VOLUME_UPDATE.getMessageDefinition(connectorName,
                                                                           memberElement.getElement().getElementHeader().getGUID(),
                                                                           volumeInfo.getFull_name(),
                                                                           ucServerEndpoint));

        externalIdClient.confirmSynchronization(memberElement.getElement(), volumeInfo.getVolume_id());
    }


    /**
     * Delete the volume from Unity Catalog.
     *
     * @param volumeInfo info object describing the element to delete.
     *
     * @throws PropertyServerException problem connecting to UC
     */
    private void deleteElementInThirdParty(VolumeInfo  volumeInfo) throws PropertyServerException
    {
        final String methodName = "deleteElementInThirdParty";

        auditLog.logMessage(methodName,
                            UCAuditCode.UC_ELEMENT_DELETE.getMessageDefinition(connectorName,
                                                                               volumeInfo.getFull_name(),
                                                                               ucServerEndpoint));

        ucConnector.deleteVolume(volumeInfo.getFull_name());
    }


    /**
     * Return the template's placeholder properties populated with the info object's values.
     *
     * @param info object from UC
     * @return map of placeholder values
     */
    private Map<String, String> getPlaceholderProperties(VolumeInfo info)
    {
        Map<String, String> placeholderProperties = new HashMap<>();

        placeholderProperties.put(PlaceholderProperty.SERVER_NETWORK_ADDRESS.getName(), ucServerEndpoint);
        placeholderProperties.put(UnityCatalogPlaceholderProperty.CATALOG_NAME.getName(), info.getCatalog_name());
        placeholderProperties.put(UnityCatalogPlaceholderProperty.SCHEMA_NAME.getName(), info.getSchema_name());
        placeholderProperties.put(UnityCatalogPlaceholderProperty.VOLUME_NAME.getName(), info.getName());
        placeholderProperties.put(PlaceholderProperty.DESCRIPTION.getName(), info.getComment());
        placeholderProperties.put(PlaceholderProperty.VERSION_IDENTIFIER.getName(), null);
        placeholderProperties.put(UnityCatalogPlaceholderProperty.STORAGE_LOCATION.getName(), info.getStorage_location());
        placeholderProperties.put(UnityCatalogPlaceholderProperty.VOLUME_TYPE.getName(), info.getVolume_type());

        return placeholderProperties;
    }


    /**
     * Set up the element properties for an asset from the info object.
     *
     * @param info  information extracted from UC
     *
     * @return element properties suitable for create or update
     */
    private ElementProperties getElementProperties(VolumeInfo info)
    {
        ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                               OpenMetadataProperty.DISPLAY_NAME.name,
                                                                               info.getName());

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.RESOURCE_NAME.name,
                                                             info.getFull_name());

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.DESCRIPTION.name,
                                                             info.getComment());

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.PATH_NAME.name,
                                                             getPathNameFromStorageLocation(info.getStorage_location()));

       // elementProperties = propertyHelper.addStringMapProperty(elementProperties,
       //                                                         OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
       //                                                         info.getProperties());

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                             UnityCatalogDeployedImplementationType.OSS_UC_VOLUME.getDeployedImplementationType());

        return elementProperties;
    }


    /**
     * Set up the element properties from the info object and qualified name.
     *
     * @param qualifiedName calculated qualified name
     * @param info  information extracted from UC
     *
     * @return element properties suitable for create or update
     */
    private ElementProperties getElementProperties(String     qualifiedName,
                                                   VolumeInfo info)
    {
        ElementProperties elementProperties = this.getElementProperties(info);

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.QUALIFIED_NAME.name,
                                                             qualifiedName);

        return elementProperties;
    }
}
