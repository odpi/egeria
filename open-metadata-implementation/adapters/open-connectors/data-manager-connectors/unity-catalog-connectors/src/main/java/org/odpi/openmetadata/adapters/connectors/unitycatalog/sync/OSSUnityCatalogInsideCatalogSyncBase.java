/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.sync;


import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCAuditCode;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.properties.BasicElementProperties;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.properties.ElementBase;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.OSSUnityCatalogResourceConnector;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ExternalIdentifierProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.integration.context.OpenMetadataAccess;
import org.odpi.openmetadata.frameworks.integration.iterator.IntegrationIterator;
import org.odpi.openmetadata.frameworks.integration.iterator.MemberElement;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.mapper.PropertyFacetValidValues;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.integrationservices.catalog.connector.CatalogIntegratorContext;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Common functions for the synchronizing between Egeria and Unity Catalog.
 */
public abstract class OSSUnityCatalogInsideCatalogSyncBase
{
    protected final String                                 connectorName;
    protected final CatalogIntegratorContext               context;
    protected final String                                 catalogGUID;
    protected final String                                 catalogTypeName = UnityCatalogDeployedImplementationType.OSS_UC_CATALOG.getAssociatedTypeName();
    protected final String                                 catalogQualifiedName;
    protected final String                                 catalogName;
    protected final PermittedSynchronization               targetPermittedSynchronization;
    protected final OSSUnityCatalogResourceConnector       ucConnector;
    protected final String                                 ucServerEndpoint;
    protected final UnityCatalogDeployedImplementationType deployedImplementationType;
    protected final Map<String, String>                    templates;
    protected final Map<String, Object>                    configurationProperties;
    protected final AuditLog                               auditLog;
    protected final OpenMetadataAccess                     openMetadataAccess;
    protected final List<String>                           excludeNames;
    protected final List<String>                           includeNames;

    /*
     * This map lists the elements that are synchronized.
     */
    protected final Map<String, String> ucFullNameToEgeriaGUID;
    protected final PropertyHelper propertyHelper = new PropertyHelper();


    /**
     * Set up the schema synchronizer.
     *
     * @param connectorName name of this connector
     * @param context context for the connector
     * @param catalogName the catalog target name
     * @param catalogGUID guid of the catalog
     * @param catalogQualifiedName name of the catalog
     * @param ucFullNameToEgeriaGUID map of full names from UC to the GUID of the entity in Egeria.
     * @param targetPermittedSynchronization the policy that controls the direction of metadata exchange
     * @param ucConnector connector for accessing UC
     * @param deployedImplementationType technology type
     * @param ucServerEndpoint the server endpoint used to constructing the qualified names
     * @param templates templates supplied through the catalog target
     * @param configurationProperties configuration properties supplied through the catalog target
     * @param excludeNames list of catalogs to ignore (and include all others)
     * @param includeNames list of catalogs to include (and ignore all others) - overrides excludeCatalogs
     * @param auditLog logging destination
     */
    public OSSUnityCatalogInsideCatalogSyncBase(String                                 connectorName,
                                                CatalogIntegratorContext               context,
                                                String catalogName,
                                                String                                 catalogGUID,
                                                String catalogQualifiedName,
                                                Map<String, String>                    ucFullNameToEgeriaGUID,
                                                PermittedSynchronization               targetPermittedSynchronization,
                                                OSSUnityCatalogResourceConnector       ucConnector,
                                                String                                 ucServerEndpoint,
                                                UnityCatalogDeployedImplementationType deployedImplementationType,
                                                Map<String, String>                    templates,
                                                Map<String, Object>                    configurationProperties,
                                                List<String>                           excludeNames,
                                                List<String>                           includeNames,
                                                AuditLog                               auditLog)
    {
        this.connectorName                  = connectorName;
        this.context                        = context;
        this.catalogName                    = catalogName;
        this.catalogGUID                    = catalogGUID;
        this.catalogQualifiedName           = catalogQualifiedName;
        this.ucFullNameToEgeriaGUID         = ucFullNameToEgeriaGUID;
        this.targetPermittedSynchronization = targetPermittedSynchronization;
        this.ucConnector                    = ucConnector;
        this.ucServerEndpoint               = ucServerEndpoint;
        this.deployedImplementationType     = deployedImplementationType;
        this.templates                      = templates;
        this.configurationProperties        = configurationProperties;
        this.excludeNames                   = excludeNames;
        this.includeNames                   = includeNames;
        this.auditLog                       = auditLog;

        this.openMetadataAccess             = context.getIntegrationGovernanceContext().getOpenMetadataAccess();
    }



    /**
     * Synchronize the elements in Unity catalog.
     *
     * @return Map of full schema names to egeria GUIDs
     * @throws InvalidParameterException bad parameter
     * @throws PropertyServerException open metadata repository not available (or has logic error)
     * @throws UserNotAuthorizedException permissions error
     */
    Map<String, String> refresh() throws InvalidParameterException,
                                         PropertyServerException,
                                         UserNotAuthorizedException
    {
        /*
         * Sweep 1 - consider all the elements stored in Egeria.
         */
       IntegrationIterator iterator = this.refreshEgeria();

        /*
         * Sweep 2 - Query UC to discover any elements that have been missed.
         */
        refreshUnityCatalog(iterator);

        /*
         * This map has been filled with the mapped elements.
         */
        return ucFullNameToEgeriaGUID;
    }


    /**
     * Review all the elements for the subtype stored in Egeria.
     *
     * @return MetadataCollectionIterator
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException security error
     */
    protected abstract IntegrationIterator refreshEgeria() throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException;


    /**
     * Review all the elements for the subtype stored in UC.
     *
     * @param iterator  Metadata collection iterator
     *
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException security error
     */
    protected abstract void refreshUnityCatalog(IntegrationIterator iterator) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException;


    /**
     * Convert the long value returned by UC into a date (or null).
     *
     * @param valueFromUC long
     * @return date
     */
    protected Date getDateFromLong(long valueFromUC)
    {
        if (valueFromUC == 0)
        {
            return null;
        }

        return new Date(valueFromUC);
    }


    /**
     * Return the qualified name of an element using UnityCatalog's full name.
     *
     * @param fullName dotted name
     * @return qualified name
     */
    protected String getQualifiedName(String fullName)
    {
        return deployedImplementationType.getDeployedImplementationType() + ":" + ucServerEndpoint + ":" + fullName;
    }


    /**
     * Delete an element from open metadata.
     *
     * @param memberElement element to delete
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    protected void deleteElementInEgeria(MemberElement memberElement) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        openMetadataAccess.deleteMetadataElementInStore(catalogGUID, catalogQualifiedName, memberElement.getElement().getElementGUID());
    }


    /**
     * Populate and return the external identifier properties for a UC Schema.
     *
     * @param ucElement values from Unity Catalog element
     * @param schemaName name of the schema
     * @return external identifier properties
     */
    protected ExternalIdentifierProperties getExternalIdentifierProperties(ElementBase              ucElement,
                                                                           String                   schemaName,
                                                                           String                   elementName,
                                                                           String                   id,
                                                                           PermittedSynchronization instanceSynchronizationDirection)
    {
        ExternalIdentifierProperties externalIdentifierProperties = new ExternalIdentifierProperties();

        externalIdentifierProperties.setExternalIdentifier(id);
        externalIdentifierProperties.setExternalIdentifierSource(UnityCatalogDeployedImplementationType.OSS_UC_CATALOG.getDeployedImplementationType());
        externalIdentifierProperties.setExternalInstanceCreationTime(new Date(ucElement.getCreated_at()));
        externalIdentifierProperties.setExternalInstanceCreatedBy(ucElement.getCreated_by());
        externalIdentifierProperties.setExternalInstanceLastUpdateTime(new Date(ucElement.getUpdated_at()));
        externalIdentifierProperties.setExternalInstanceLastUpdatedBy(ucElement.getUpdated_by());

        Map<String, String> mappingProperties = new HashMap<>();

        mappingProperties.put(UnityCatalogPlaceholderProperty.CATALOG_NAME.getName(), catalogName);
        mappingProperties.put(UnityCatalogPlaceholderProperty.SCHEMA_NAME.getName(), schemaName);
        mappingProperties.put(UnityCatalogPlaceholderProperty.OWNER.getName(), ucElement.getOwner());
        mappingProperties.put(elementName, ucElement.getName());

        mappingProperties.put(PlaceholderProperty.SERVER_NETWORK_ADDRESS.name, ucServerEndpoint);

        externalIdentifierProperties.setMappingProperties(mappingProperties);

        externalIdentifierProperties.setSynchronizationDirection(instanceSynchronizationDirection);

        return externalIdentifierProperties;
    }


    /**
     * Create the property facet for an Egeria element.  This holds the vendor specific properties for the element.
     *
     * @param parentGUID the parent (and anchor) unique name
     * @param parentQualifiedName qualifiedName of the parent element
     * @param basicElementProperties common properties of an element
     * @param facetProperties these are the specialist property for the linked element.
     */
    protected void addPropertyFacet(String                 parentGUID,
                                    String                 parentQualifiedName,
                                    BasicElementProperties basicElementProperties,
                                    Map<String, String>    facetProperties) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                               OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                               parentQualifiedName + "_propertyFacet");

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.SCHEMA_VERSION.name,
                                                             PropertyFacetValidValues.UNITY_CATALOG_SCHEMA_VERSION_VALUE);

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.DESCRIPTION.name,
                                                             PropertyFacetValidValues.VENDOR_PROPERTIES_DESCRIPTION_VALUE);

        Map<String, String> fullFacetProperties = facetProperties;

        if (fullFacetProperties == null)
        {
            fullFacetProperties = new HashMap<>();
        }

        fullFacetProperties.put(UnityCatalogPlaceholderProperty.METASTORE_ID.getName(), basicElementProperties.getMetastore_id());
        fullFacetProperties.put(UnityCatalogPlaceholderProperty.SECURABLE_KIND.getName(), basicElementProperties.getSecurable_kind());
        fullFacetProperties.put(UnityCatalogPlaceholderProperty.SECURABLE_TYPE.getName(), basicElementProperties.getSecurable_type());
        fullFacetProperties.put(UnityCatalogPlaceholderProperty.BROWSE_ONLY.getName(), Boolean.toString(basicElementProperties.isBrowse_only()));

        elementProperties = propertyHelper.addStringMapProperty(elementProperties,
                                                                OpenMetadataProperty.PROPERTIES.name,
                                                                fullFacetProperties);

        openMetadataAccess.createMetadataElementInStore(catalogGUID,
                                                        catalogQualifiedName,
                                                        OpenMetadataType.PROPERTY_FACET.typeName,
                                                        ElementStatus.ACTIVE,
                                                        null,
                                                        parentGUID,
                                                        false,
                                                        null,
                                                        null,
                                                        elementProperties,
                                                        parentGUID,
                                                        OpenMetadataType.REFERENCEABLE_FACET.typeName,
                                                        propertyHelper.addStringProperty(null,
                                                                                         OpenMetadataProperty.SOURCE.name,
                                                                                         PropertyFacetValidValues.UNITY_CATALOG_SOURCE_VALUE),
                                                        true);
    }



    /**
     * Check that the name of the third party element has not changed with respect to Egeria.
     *
     * @param thirdPartyExternalIdentifier id from Unity Catalog
     * @param memberElement element from Egeria
     * @return boolean
     */
    protected boolean noMismatchInExternalIdentifier(String        thirdPartyExternalIdentifier,
                                                     MemberElement memberElement)
    {
        final String methodName = "noMismatchInExternalIdentifier";

        if ((thirdPartyExternalIdentifier == null) || (memberElement == null) || (memberElement.getExternalIdentifier() == null))
        {
            return true;
        }

        if (thirdPartyExternalIdentifier.equals(memberElement.getExternalIdentifier().getExternalIdentifier()))
        {
            return true;
        }

        auditLog.logMessage(methodName, UCAuditCode.IDENTITY_MISMATCH.getMessageDefinition(connectorName,
                                                                                           memberElement.getExternalIdentifier().getExternalIdentifier(),
                                                                                           thirdPartyExternalIdentifier,
                                                                                           ucServerEndpoint));
        return false;
    }


    /**
     * Extract the comment for a UC element from the description attribute in an egeria element.
     *
     * @param memberElement element from Egeria
     * @return comment for UC
     */
    protected String getUCCatalogFromMember(MemberElement memberElement)
    {
        final String methodName = "getUCCatalogFromMember";

        ElementProperties elementProperties = memberElement.getElement().getElementProperties();

        String fullName = propertyHelper.getStringProperty(catalogName,
                                                           OpenMetadataProperty.RESOURCE_NAME.name,
                                                           elementProperties,
                                                           methodName);

        return ucConnector.getCatalogNameFromThreePartName(fullName);
    }


    /**
     * Extract the schema name for a UC element from the description attribute in an egeria element.
     *
     * @param memberElement element from Egeria
     * @return comment for UC
     */
    protected String getUCSchemaFromMember(MemberElement memberElement)
    {
        final String methodName = "getUCSchemaFromMember";

        ElementProperties elementProperties = memberElement.getElement().getElementProperties();

        String fullName = propertyHelper.getStringProperty(catalogName,
                                                           OpenMetadataProperty.RESOURCE_NAME.name,
                                                           elementProperties,
                                                           methodName);

        return ucConnector.getSchemaNameFromThreePartName(fullName);
    }



    /**
     * Extract the name of a UC element from the name attribute in an egeria element (which is the full name).
     *
     * @param memberElement element from Egeria
     * @return name for UC
     */
    protected String getUCNameFromMember(MemberElement memberElement)
    {
        final String methodName = "getUCNameFromMember";

        ElementProperties elementProperties = memberElement.getElement().getElementProperties();

        String fullName = propertyHelper.getStringProperty(catalogName,
                                                           OpenMetadataProperty.RESOURCE_NAME.name,
                                                           elementProperties,
                                                           methodName);

        if (fullName == null)
        {
            fullName = propertyHelper.getStringProperty(catalogName,
                                                        OpenMetadataProperty.NAME.name,
                                                        elementProperties,
                                                        methodName);
        }

        return ucConnector.getNameFromFullName(fullName);
    }


    /**
     * Extract the comment for a UC element from the description attribute in an egeria element.
     *
     * @param memberElement element from Egeria
     * @return comment for UC
     */
    protected String getUCCommentFomMember(MemberElement memberElement)
    {
        final String methodName = "getUCCommentFomMember";

        ElementProperties elementProperties = memberElement.getElement().getElementProperties();

        return propertyHelper.getStringProperty(catalogName,
                                                OpenMetadataProperty.DESCRIPTION.name,
                                                elementProperties,
                                                methodName);
    }


    /**
     * Extract the properties for a UC element from the additionalProperties attribute in an egeria element.
     *
     * @param memberElement element from Egeria
     * @return properties for UC
     */
    protected Map<String, String> getUCPropertiesFromMember(MemberElement memberElement)
    {
        final String methodName = "getUCPropertiesFromMember";

        ElementProperties elementProperties = memberElement.getElement().getElementProperties();

        return propertyHelper.getStringMapFromProperty(catalogName,
                                                       OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                       elementProperties,
                                                       methodName);
    }


    /**
     * Extract the storage location from the
     * @param memberElement elements from egeria
     * @return storage location (may be null
     */
    protected String getUCStorageLocationFromMember(MemberElement memberElement)
    {
        Map<String, String> vendorProperties = memberElement.getVendorProperties(PropertyFacetValidValues.UNITY_CATALOG_SOURCE_VALUE);

        if (vendorProperties != null)
        {
            return vendorProperties.get(UnityCatalogPlaceholderProperty.STORAGE_LOCATION.getName());
        }

        return null;
    }



    /**
     * Convert the US format for the storage location into a proper pathname.
     *
     * @param storageLocation storage location from UC
     * @return pathname
     */
    protected String getPathNameFromStorageLocation(String storageLocation)
    {
        if (storageLocation != null)
        {
            if (storageLocation.startsWith("file:///"))
            {
                return storageLocation.substring(7);
            }
            else
            {
                return storageLocation;
            }
        }

        return null;
    }
}
