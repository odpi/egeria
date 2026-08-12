/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.sync;


import org.odpi.openmetadata.adapters.connectors.controls.UnityCatalogDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCAuditCode;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCErrorCode;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.properties.BasicElementProperties;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.properties.ElementBase;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.OSSUnityCatalogResourceConnector;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.integration.iterator.IntegrationIterator;
import org.odpi.openmetadata.frameworks.integration.iterator.MemberElement;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ExternalIdClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.PropertyFacetClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.SchemaAttributeClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.SchemaTypeClient;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.enums.KeyPattern;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.mapper.PropertyFacetValidValues;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AnchorsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalidentifiers.ExternalIdLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalidentifiers.ExternalIdProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.propertyfacets.PropertyFacetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.propertyfacets.ReferenceableFacetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.*;

/**
 * Common functions for the synchronizing between Egeria and Unity Catalog.
 */
public abstract class OSSUnityCatalogInsideCatalogSyncBase
{
    protected final PropertyHelper propertyHelper = new PropertyHelper();

    protected final String                                 connectorName;
    protected final IntegrationContext                     context;
    protected final String                                 serverGUID;
    protected final String                                 catalogGUID;
    protected final String                                 catalogTypeName = UnityCatalogDeployedImplementationType.OSS_UC_CATALOG.getAssociatedTypeName();
    protected final String                                 metadataCollectionGUID;
    protected final String                                 metadataCollectionQualifiedName;
    protected final String                                 catalogTargetName;
    protected final PermittedSynchronization               targetPermittedSynchronization;
    protected final OSSUnityCatalogResourceConnector       ucConnector;
    protected final String                                 ucServerEndpoint;
    protected final String                                 entityTypeName;
    protected final UnityCatalogDeployedImplementationType deployedImplementationType;
    protected final Map<String, String>                    templates;
    protected final Map<String, Object>                    configurationProperties;
    protected final AuditLog                               auditLog;
    protected final OpenMetadataStore                      openMetadataStore;
    protected final ExternalIdClient                       externalIdClient;
    protected final PropertyFacetClient                    propertyFacetClient;
    protected final AssetClient                            assetClient;
    protected final List<String>                           excludeNames;
    protected final List<String>                           includeNames;
    protected final String                                 templateGUID;


    /*
     * This map lists the elements that are synchronized.
     */
    protected final Map<String, String> ucFullNameToEgeriaGUID;



    /**
     * Set up the schema synchronizer.
     *
     * @param connectorName name of this connector
     * @param context context for the connector
     * @param catalogTargetName the catalog target name
     * @param serverGUID guid of the Unity Catalog server asset that everything catalogued from it is scoped to
     * @param catalogGUID guid of the catalog
     * @param metadataCollectionGUID the managedMetadataCollectionId of the metadata collection for this UC server
     * @param metadataCollectionQualifiedName name of the metadata collection for this UC server
     * @param ucFullNameToEgeriaGUID map of full names from UC to the GUID of the entity in Egeria.
     * @param targetPermittedSynchronization the policy that controls the direction of metadata exchange
     * @param ucConnector connector for accessing UC
     * @param entityTypeName type name of asset entity used to represent this type of element
     * @param deployedImplementationType technology type
     * @param ucServerEndpoint the server endpoint used to constructing the qualified names
     * @param templates templates supplied through the catalog target
     * @param configurationProperties configuration properties supplied through the catalog target
     * @param excludeNames list of catalogs to ignore (and include all others)
     * @param includeNames list of catalogs to include (and ignore all others) - overrides excludeCatalogs
     * @param auditLog logging destination
     * @throws UserNotAuthorizedException connector disconnected
     * @throws InvalidParameterException missing template
     */
    public OSSUnityCatalogInsideCatalogSyncBase(String                                 connectorName,
                                                IntegrationContext                     context,
                                                String                                 catalogTargetName,
                                                String                                 serverGUID,
                                                String                                 catalogGUID,
                                                String                                 metadataCollectionGUID,
                                                String                                 metadataCollectionQualifiedName,
                                                Map<String, String>                    ucFullNameToEgeriaGUID,
                                                PermittedSynchronization               targetPermittedSynchronization,
                                                OSSUnityCatalogResourceConnector       ucConnector,
                                                String                                 ucServerEndpoint,
                                                String                                 entityTypeName,
                                                UnityCatalogDeployedImplementationType deployedImplementationType,
                                                Map<String, String>                    templates,
                                                Map<String, Object>                    configurationProperties,
                                                List<String>                           excludeNames,
                                                List<String>                           includeNames,
                                                AuditLog                               auditLog) throws UserNotAuthorizedException,
                                                                                                        InvalidParameterException
    {
        final String methodName = "OSSUnityCatalogInsideCatalogSyncBase";

        this.connectorName                   = connectorName;
        this.context                         = context;
        this.catalogTargetName               = catalogTargetName;
        this.serverGUID                      = serverGUID;
        this.catalogGUID                     = catalogGUID;
        this.metadataCollectionGUID          = metadataCollectionGUID;
        this.metadataCollectionQualifiedName = metadataCollectionQualifiedName;
        this.ucFullNameToEgeriaGUID          = ucFullNameToEgeriaGUID;
        this.targetPermittedSynchronization  = targetPermittedSynchronization;
        this.ucConnector                     = ucConnector;
        this.ucServerEndpoint                = ucServerEndpoint;
        this.entityTypeName                  = entityTypeName;
        this.deployedImplementationType      = deployedImplementationType;
        this.templates                       = templates;
        this.configurationProperties         = configurationProperties;
        this.excludeNames                    = excludeNames;
        this.includeNames                    = includeNames;
        this.auditLog                        = auditLog;

        /*
         * Simplify access to the clients.  The metadata collection should be set up through the catalog target
         * relationship.
         */
        this.openMetadataStore   = context.getOpenMetadataStore();
        this.externalIdClient    = context.getExternalIdClient();
        this.propertyFacetClient = context.getPropertyFacetClient();
        this.assetClient         = context.getAssetClient(entityTypeName);

        /*
         * All elements created by this connector (and its siblings) are from the same metadata collection,
         * so this is set up centrally here rather than by each caller individually.
         */
        if (metadataCollectionGUID != null)
        {
            this.assetClient.setExternalSource(metadataCollectionGUID, metadataCollectionQualifiedName);
            this.propertyFacetClient.setExternalSource(metadataCollectionGUID, metadataCollectionQualifiedName);
            this.externalIdClient.setExternalSource(metadataCollectionGUID, metadataCollectionQualifiedName);
            this.openMetadataStore.setExternalSource(metadataCollectionGUID, metadataCollectionQualifiedName);
        }

        if (templates != null)
        {
            this.templateGUID = templates.get(deployedImplementationType.getDeployedImplementationType());
        }
        else
        {
            this.templateGUID = null;
        }

        final String templateGUIDParameterName = "templateGUID";
        propertyHelper.validateGUID(templateGUID, templateGUIDParameterName, methodName);
    }


    /**
     * Synchronize the elements in Unity catalog.
     *
     * @param parentGUID unique identifier of the parent
     * @param parentRelationshipTypeName relationship type between parent and elements to iterate through
     * @param relationshipProperties optional properties for the relationship
     * @return Map of full schema names to egeria GUIDs
     * @throws InvalidParameterException bad parameter
     * @throws PropertyServerException open metadata repository not available (or has logic error)
     * @throws UserNotAuthorizedException permissions error
     * @throws ConnectorCheckedException logic error in properties
     */
    Map<String, String> refresh(String                     parentGUID,
                                String                     parentRelationshipTypeName,
                                RelationshipBeanProperties relationshipProperties) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException,
                                                                                          ConnectorCheckedException
    {
        /*
         * Sweep 1 - consider all the elements stored in Egeria.
         */
       IntegrationIterator iterator = this.refreshEgeria(parentGUID, parentRelationshipTypeName, relationshipProperties);

        /*
         * Sweep 2 - Query UC to discover any elements that have been missed.
         */
        refreshUnityCatalog(parentGUID,
                            parentRelationshipTypeName,
                            relationshipProperties,
                            iterator);

        /*
         * This map has been filled with the mapped elements.
         */
        return ucFullNameToEgeriaGUID;
    }


    /**
     * Review all the elements for the subtype stored in Egeria.
     *
     * @param parentGUID unique identifier of the parent
     * @param parentRelationshipTypeName relationship type between parent and elements to iterate through
     * @param relationshipProperties optional properties for the relationship
     * @return iterator
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException security error
     * @throws ConnectorCheckedException logic error in properties
     */
    protected abstract IntegrationIterator refreshEgeria(String                     parentGUID,
                                                         String                     parentRelationshipTypeName,
                                                         RelationshipBeanProperties relationshipProperties) throws InvalidParameterException,
                                                                                                                   PropertyServerException,
                                                                                                                   UserNotAuthorizedException,
                                                                                                                   ConnectorCheckedException;


    /**
     * Review all the elements for the subtype stored in UC.
     *
     * @param parentGUID unique identifier of the parent
     * @param parentRelationshipTypeName relationship type between parent and elements to iterate through
     * @param relationshipProperties optional properties for the relationship
     * @param iterator iterator of choice
     *
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException security error
     * @throws ConnectorCheckedException logic error in properties
     */
    protected abstract void refreshUnityCatalog(String                     parentGUID,
                                                String                     parentRelationshipTypeName,
                                                RelationshipBeanProperties relationshipProperties,
                                                IntegrationIterator        iterator) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException,
                                                                                            ConnectorCheckedException;


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
        return deployedImplementationType.getDeployedImplementationType() + "::" + ucServerEndpoint + "::" + fullName;
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
        openMetadataStore.deleteMetadataElementInStore(memberElement.getElement().getElementHeader().getGUID(), openMetadataStore.getDeleteOptions(true));
    }


    /**
     * Add a new external identifier to an existing open metadata element.
     *
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param parentGUID unique identifier of the immediate parent of openMetadataElementGUID (eg the catalog for a
     *                   schema, the schema for a table/volume/function/model, or the server for the catalog itself)
     * @param ucElement values from Unity Catalog element
     * @param schemaName name of the schema
     * @param elementName element type (from UC)
     * @param instanceSynchronizationDirection direction the metadata is flowing
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void addExternalIdentifier(String                   openMetadataElementGUID,
                                      String                   parentGUID,
                                      ElementBase              ucElement,
                                      String                   schemaName,
                                      String                   elementName,
                                      String                   elementType,
                                      String                   id,
                                      PermittedSynchronization instanceSynchronizationDirection) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        ExternalIdProperties externalIdProperties = getExternalIdProperties(ucElement,
                                                                            elementType,
                                                                            id);

        ExternalIdLinkProperties externalIdLinkProperties = getExternalIdLinkProperties(ucElement,
                                                                                        schemaName,
                                                                                        elementName,
                                                                                        instanceSynchronizationDirection);

        externalIdClient.createExternalId(openMetadataElementGUID,
                                          this.buildAnchorScopeGUIDs(parentGUID),
                                          externalIdLinkProperties,
                                          externalIdProperties);
    }


    /**
     * Build the anchorScopeGUIDs list used throughout this connector for elements catalogued from Unity Catalog:
     * the Unity Catalog server asset, the catalog the element belongs to, any additional ancestors between the
     * catalog and the element (for example the schema, and the table/function for a column or parameter), and
     * the technology-type GUID for the OSS Unity Catalog server product.  Nulls and duplicates are removed since
     * not every element has all of these ancestors (for example, the catalog itself has no separate catalogGUID
     * or ancestor GUIDs distinct from the server).
     *
     * @param ancestorGUIDs unique identifiers of the ancestors of the element being scoped, nearest first - eg
     *                      (schemaGUID) for a table/volume/function/model, or (schemaGUID, assetGUID) for a
     *                      column/parameter nested under a table/function.  May be omitted or contain nulls.
     * @return de-duplicated, non-null list of anchor scope GUIDs
     */
    protected List<String> buildAnchorScopeGUIDs(String... ancestorGUIDs)
    {
        Set<String> anchorScopeGUIDs = new LinkedHashSet<>();

        if (serverGUID != null)
        {
            anchorScopeGUIDs.add(serverGUID);
        }

        if (catalogGUID != null)
        {
            anchorScopeGUIDs.add(catalogGUID);
        }

        if (ancestorGUIDs != null)
        {
            for (String ancestorGUID : ancestorGUIDs)
            {
                if (ancestorGUID != null)
                {
                    anchorScopeGUIDs.add(ancestorGUID);
                }
            }
        }

        anchorScopeGUIDs.add(UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getGUID());

        return new ArrayList<>(anchorScopeGUIDs);
    }


    /**
     * Find an existing schema type with the requested qualified name.  Used to make schema cataloguing (for
     * example table columns, or API parameters) idempotent across repeated refreshes.
     *
     * @param schemaTypeClient client to search with
     * @param qualifiedName unique name to search for
     * @return GUID of the matching schema type, or null if not found
     * @throws InvalidParameterException one of the parameters is not correct
     * @throws UserNotAuthorizedException the connector's userId is not able to work with open metadata
     * @throws PropertyServerException there is an issue with one of the open metadata repositories
     */
    protected String findExistingSchemaType(SchemaTypeClient schemaTypeClient,
                                            String           qualifiedName) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        List<OpenMetadataRootElement> elements = schemaTypeClient.getSchemaTypesByName(qualifiedName, schemaTypeClient.getQueryOptions());

        if (elements != null)
        {
            for (OpenMetadataRootElement element : elements)
            {
                if ((element != null) && (element.getProperties() instanceof SchemaTypeProperties schemaTypeProperties))
                {
                    if (qualifiedName.equals(schemaTypeProperties.getQualifiedName()))
                    {
                        return element.getElementHeader().getGUID();
                    }
                }
            }
        }

        return null;
    }


    /**
     * Find an existing schema attribute with the requested qualified name.  Used to make schema cataloguing (for
     * example table columns, or API parameters) idempotent across repeated refreshes.
     *
     * @param schemaAttributeClient client to search with
     * @param qualifiedName unique name to search for
     * @return GUID of the matching schema attribute, or null if not found
     * @throws InvalidParameterException one of the parameters is not correct
     * @throws UserNotAuthorizedException the connector's userId is not able to work with open metadata
     * @throws PropertyServerException there is an issue with one of the open metadata repositories
     */
    protected String findExistingSchemaAttribute(SchemaAttributeClient schemaAttributeClient,
                                                 String                qualifiedName) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        List<OpenMetadataRootElement> elements = schemaAttributeClient.getSchemaAttributesByName(qualifiedName, schemaAttributeClient.getQueryOptions());

        if (elements != null)
        {
            for (OpenMetadataRootElement element : elements)
            {
                if ((element != null) && (element.getProperties() instanceof SchemaAttributeProperties schemaAttributeProperties))
                {
                    if (qualifiedName.equals(schemaAttributeProperties.getQualifiedName()))
                    {
                        return element.getElementHeader().getGUID();
                    }
                }
            }
        }

        return null;
    }



    /**
     * Populate and return the external identifier properties for a UC Schema.
     *
     * @param ucElement values from Unity Catalog element
     * @param elementType element type (from UC)
     * @param id element id (from UC)
     * @return external identifier properties
     */
    protected ExternalIdProperties getExternalIdProperties(ElementBase              ucElement,
                                                           String                   elementType,
                                                           String                   id)
    {
        ExternalIdProperties externalIdProperties = new ExternalIdProperties();

        externalIdProperties.setQualifiedName(OpenMetadataType.EXTERNAL_ID.typeName + "::" + elementType + "::" + id);

        externalIdProperties.setKey(id);
        externalIdProperties.setKeyPattern(KeyPattern.LOCAL_KEY);
        externalIdProperties.setExternalInstanceTypeName(elementType);
        externalIdProperties.setExternalInstanceCreationTime(new Date(ucElement.getCreated_at()));
        externalIdProperties.setExternalInstanceCreatedBy(ucElement.getCreated_by());
        externalIdProperties.setExternalInstanceLastUpdateTime(new Date(ucElement.getUpdated_at()));
        externalIdProperties.setExternalInstanceLastUpdatedBy(ucElement.getUpdated_by());

        return externalIdProperties;
    }



    /**
     * Populate and return the external identifier properties for a UC Schema.
     *
     * @param ucElement values from Unity Catalog element
     * @param schemaName name of the schema
     * @param elementName element type (from UC)
     * @param instanceSynchronizationDirection direction the metadata is flowing
     * @return external identifier properties
     */
    protected ExternalIdLinkProperties getExternalIdLinkProperties(ElementBase              ucElement,
                                                                   String                   schemaName,
                                                                   String                   elementName,
                                                                   PermittedSynchronization instanceSynchronizationDirection)
    {
        ExternalIdLinkProperties externalIdentifierProperties = new ExternalIdLinkProperties();

        externalIdentifierProperties.setUsage("Differencing between create and rename in UC");
        externalIdentifierProperties.setSource(UnityCatalogDeployedImplementationType.OSS_UC_CATALOG.getDeployedImplementationType());
        externalIdentifierProperties.setLastSynchronized(new Date());

        Map<String, String> mappingProperties = new HashMap<>();

        mappingProperties.put(UnityCatalogPlaceholderProperty.CATALOG_NAME.getName(), catalogTargetName);
        mappingProperties.put(UnityCatalogPlaceholderProperty.SCHEMA_NAME.getName(), schemaName);
        mappingProperties.put(UnityCatalogPlaceholderProperty.OWNER.getName(), ucElement.getOwner());
        mappingProperties.put(elementName, ucElement.getName());

        mappingProperties.put(PlaceholderProperty.SERVER_NETWORK_ADDRESS.name, ucServerEndpoint);

        externalIdentifierProperties.setMappingProperties(mappingProperties);

        externalIdentifierProperties.setPermittedSynchronization(instanceSynchronizationDirection);

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
        PropertyFacetProperties propertyFacetProperties = new PropertyFacetProperties();

        propertyFacetProperties.setQualifiedName(parentQualifiedName + "::propertyFacet");
        propertyFacetProperties.setVersionIdentifier(PropertyFacetValidValues.UNITY_CATALOG_SCHEMA_VERSION_VALUE);
        propertyFacetProperties.setDescription(PropertyFacetValidValues.VENDOR_PROPERTIES_DESCRIPTION_VALUE);

        Map<String, String> fullFacetProperties = facetProperties;

        if (fullFacetProperties == null)
        {
            fullFacetProperties = new HashMap<>();
        }

        fullFacetProperties.put(UnityCatalogPlaceholderProperty.METASTORE_ID.getName(), basicElementProperties.getMetastore_id());
        fullFacetProperties.put(UnityCatalogPlaceholderProperty.SECURABLE_KIND.getName(), basicElementProperties.getSecurable_kind());
        fullFacetProperties.put(UnityCatalogPlaceholderProperty.SECURABLE_TYPE.getName(), basicElementProperties.getSecurable_type());
        fullFacetProperties.put(UnityCatalogPlaceholderProperty.BROWSE_ONLY.getName(), Boolean.toString(basicElementProperties.isBrowse_only()));

        propertyFacetProperties.setProperties(fullFacetProperties);

        ReferenceableFacetProperties referenceableFacetProperties = new ReferenceableFacetProperties();

        referenceableFacetProperties.setSource(PropertyFacetValidValues.UNITY_CATALOG_SOURCE_VALUE);

        String propertyFacetGUID = propertyFacetClient.addPropertyFacetToElement(parentGUID,
                                                                                  propertyFacetClient.getMetadataSourceOptions(),
                                                                                  null,
                                                                                  propertyFacetProperties,
                                                                                  referenceableFacetProperties);

        /*
         * Link the new property facet back to the metadata collection that owns it, so that it can be found from
         * the metadata collection in the same way as an ExternalId (ExternalIdClient does this automatically for
         * ExternalIds, but there is no equivalent automatic behaviour for PropertyFacets, so it is done explicitly
         * here).
         */
        if ((propertyFacetGUID != null) && (metadataCollectionGUID != null))
        {
            OpenMetadataElement metadataCollectionElement = openMetadataStore.getMetadataElementByUniqueName(metadataCollectionGUID,
                                                                                                              OpenMetadataProperty.MANAGED_METADATA_COLLECTION_ID.name);

            if (metadataCollectionElement != null)
            {
                openMetadataStore.createRelatedElementsInStore(OpenMetadataType.SCOPED_BY_RELATIONSHIP.typeName,
                                                                propertyFacetGUID,
                                                                metadataCollectionElement.getElementGUID(),
                                                                null,
                                                                null,
                                                                null);
            }
        }
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

        if (memberElement.getExternalIdentifier().getRelatedElement().getProperties() instanceof ExternalIdProperties externalIdProperties)
        {
            if (thirdPartyExternalIdentifier.equals(externalIdProperties.getKey()))
            {
                return true;
            }

            auditLog.logMessage(methodName, UCAuditCode.IDENTITY_MISMATCH.getMessageDefinition(connectorName,
                                                                                               externalIdProperties.getKey(),
                                                                                               thirdPartyExternalIdentifier,
                                                                                               ucServerEndpoint));
        }

        return false;
    }



    /**
     * Throw an exception because a necessary property is missing.  The description portrays this a s logic error.
     *
     * @param missingPropertyName missing property name
     * @param methodName calling method
     * @param element element in error
     * @throws ConnectorCheckedException resulting exception
     */
    protected void throwMissingPropertyValue(String                  missingPropertyName,
                                             String                  methodName,
                                             OpenMetadataRootElement element) throws ConnectorCheckedException
    {
        String elementString = "<null>";

        if (element != null)
        {
            elementString = element.toString();
        }

        if (auditLog != null)
        {
            auditLog.logMessage(methodName, UCAuditCode.BAD_OM_VALUE.getMessageDefinition(connectorName,
                                                                                           missingPropertyName,
                                                                                           methodName,
                                                                                           elementString));
        }

        throw new ConnectorCheckedException(UCErrorCode.BAD_OM_VALUE.getMessageDefinition(connectorName,
                                                                                          missingPropertyName,
                                                                                          methodName,
                                                                                          elementString),
                                            this.getClass().getName(),
                                            methodName);
    }


    /**
     * Indicate that an element does not have properties of the expected bean class.  This is documented as a logic error.
     *
     * @param expectedType open metadata type information
     * @param methodName calling method
     * @param element element in error
     * @throws ConnectorCheckedException resulting exception
     */
    protected void throwBadBeanClass(OpenMetadataType expectedType,
                                     String                  methodName,
                                     OpenMetadataRootElement element) throws ConnectorCheckedException
    {
        String elementString = "<null>";
        String elementGUID = "<null>";
        String actualBeanPropertyType = "<null>";


        if (element != null)
        {
            elementString = element.toString();
            elementGUID = element.getElementHeader().getGUID();

            if (element.getProperties() != null)
            {
                actualBeanPropertyType = element.getProperties().getClass().getName();
            }
        }

        if (auditLog != null)
        {
            auditLog.logMessage(methodName, UCAuditCode.BAD_OM_PROPERTY_TYPE.getMessageDefinition(connectorName,
                                                                                                   elementGUID,
                                                                                                   expectedType.typeName,
                                                                                                   actualBeanPropertyType,
                                                                                                   expectedType.beanClass.getName(),
                                                                                                   methodName,
                                                                                                   elementString));
        }

        throw new ConnectorCheckedException(UCErrorCode.BAD_OM_PROPERTY_TYPE.getMessageDefinition(connectorName,
                                                                                                   elementGUID,
                                                                                                   expectedType.typeName,
                                                                                                   actualBeanPropertyType,
                                                                                                   expectedType.beanClass.getName(),
                                                                                                   methodName,
                                                                                                   elementString),
                                            this.getClass().getName(),
                                            methodName);
    }



    /**
     * Extract the comment for a UC element from the description attribute in an egeria element.
     *
     * @param memberElement element from Egeria
     * @return catalog for UC
     * @throws ConnectorCheckedException logic error in properties
     */
    protected String getUCCatalogFromMember(MemberElement memberElement) throws ConnectorCheckedException
    {
        final String methodName = "getUCCatalogFromMember";

        if ((memberElement.getElement() != null) && (memberElement.getElement().getProperties() instanceof AssetProperties assetProperties))
        {
            return ucConnector.getCatalogNameFromThreePartName(assetProperties.getResourceName());
        }

        this.throwBadBeanClass(OpenMetadataType.ASSET,
                               methodName,
                               memberElement.getElement());
        // unreachable
        return null;
    }


    /**
     * Extract the GUID of the immediate parent of an element from its Anchors classification.  This is used
     * when pushing an element created directly in open metadata out to Unity Catalog, where the only way to
     * discover its parent (eg the schema for a table/volume/function/model) is via its anchor.
     *
     * @param memberElement element from Egeria
     * @return parent GUID, or null if the element is not anchored
     */
    protected String getParentGUIDFromMember(MemberElement memberElement)
    {
        if ((memberElement.getElement() != null) &&
                (memberElement.getElement().getElementHeader().getAnchor() != null) &&
                (memberElement.getElement().getElementHeader().getAnchor().getClassificationProperties() instanceof AnchorsProperties anchorsProperties))
        {
            return anchorsProperties.getAnchorGUID();
        }

        return null;
    }


    /**
     * Extract the schema name for a UC element from the description attribute in an egeria element.
     *
     * @param memberElement element from Egeria
     * @return schema for UC
     * @throws ConnectorCheckedException logic error in properties
     */
    protected String getUCSchemaFromMember(MemberElement memberElement) throws ConnectorCheckedException
    {
        final String methodName = "getUCSchemaFromMember";

        if ((memberElement.getElement() != null) && (memberElement.getElement().getProperties() instanceof AssetProperties assetProperties))
        {
            return ucConnector.getSchemaNameFromThreePartName(assetProperties.getResourceName());
        }

        this.throwBadBeanClass(OpenMetadataType.ASSET,
                               methodName,
                               memberElement.getElement());

        // unreachable
        return null;
    }



    /**
     * Extract the name of a UC element from the name attribute in an egeria element (which is the full name).
     *
     * @param memberElement element from Egeria
     * @return name for UC
     * @throws ConnectorCheckedException logic error in properties
     */
    protected String getUCNameFromMember(MemberElement memberElement) throws ConnectorCheckedException
    {
        final String methodName = "getUCNameFromMember";

        if ((memberElement.getElement() != null) && (memberElement.getElement().getProperties() instanceof AssetProperties assetProperties))
        {
            String fullName = assetProperties.getResourceName();

            if (fullName == null)
            {
                fullName = assetProperties.getDisplayName();
            }

            return ucConnector.getNameFromFullName(fullName);
        }

        this.throwBadBeanClass(OpenMetadataType.ASSET,
                               methodName,
                               memberElement.getElement());

        // unreachable
        return null;
    }


    /**
     * Extract the comment for a UC element from the description attribute in an egeria element.
     *
     * @param memberElement element from Egeria
     * @return comment for UC
     * @throws ConnectorCheckedException logic error in properties
     */
    protected String getUCCommentFomMember(MemberElement memberElement) throws ConnectorCheckedException
    {
        final String methodName = "getUCCommentFomMember";

        if ((memberElement.getElement() != null) && (memberElement.getElement().getProperties() instanceof AssetProperties assetProperties))
        {
            return assetProperties.getDescription();
        }

        this.throwBadBeanClass(OpenMetadataType.ASSET,
                               methodName,
                               memberElement.getElement());

        // unreachable
        return null;
    }


    /**
     * Extract the properties for a UC element from the additionalProperties attribute in an egeria element.
     *
     * @param memberElement element from Egeria
     * @return properties for UC
     */
    protected Map<String, String> getUCPropertiesFromMember(MemberElement memberElement) throws ConnectorCheckedException
    {
        final String methodName = "getUCPropertiesFromMember";

        if ((memberElement.getElement() != null) && (memberElement.getElement().getProperties() instanceof AssetProperties assetProperties))
        {
            return assetProperties.getAdditionalProperties();
        }

        this.throwBadBeanClass(OpenMetadataType.ASSET,
                               methodName,
                               memberElement.getElement());

        // unreachable
        return null;
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
    protected String getCleanStorageLocation(String storageLocation)
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



    /**
     * Retrieve a configuration property that is a comma-separated list of strings.
     *
     * @param propertyName name of property
     * @param configurationProperties configuration properties
     * @return list of strings or null if not set
     */
    protected List<String> getArrayConfigurationProperty(String              propertyName,
                                                         Map<String, Object> configurationProperties)
    {
        if (configurationProperties != null)
        {
            if (configurationProperties.containsKey(propertyName))
            {
                Object arrayOption = configurationProperties.get(propertyName);

                String[] options = arrayOption.toString().split(",");

                return new ArrayList<>(Arrays.asList(options));
            }
        }

        return null;
    }


}
