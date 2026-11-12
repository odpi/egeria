/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.dcat.mapping;

import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatAgent;
import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatCatalog;
import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatConcept;
import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatContactPoint;
import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatDataService;
import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatDataset;
import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatDatasetSeries;
import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatDistribution;
import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatDocument;
import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatResource;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.CollectionClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.GovernanceDefinitionClient;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ContactMethodType;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ContactDetailsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ContactThroughProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.OrganizationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataAssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataSetContentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataSetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataStoreProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.apis.APIEndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.apis.DeployedAPIProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.DataFileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionFolderProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.EndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.ExternalReferenceLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.ExternalReferenceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.SearchKeywordProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.LicenseProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.LicenseTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.ScopedByProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * DcatMapper catalogs the resources described in a DCAT document as open metadata elements.  Each resource is matched
 * to an existing element through a qualified name built from the resource's IRI (or its identifier or title if it has
 * no IRI), so sending the same description again updates the elements rather than creating new ones.
 */
public class DcatMapper extends DcatMappingBase
{
    /**
     * Constructor
     *
     * @param context the connector context that provides access to open metadata
     */
    public DcatMapper(ConnectorContextBase context)
    {
        super(context);
    }


    /**
     * Return the value that identifies a resource: its IRI, or failing that its first identifier, or failing that
     * its title.
     *
     * @param resource resource from a DCAT document
     * @return identifying string or null if the resource has none of these values
     */
    public static String getResourceKey(DcatResource resource)
    {
        if (resource == null)
        {
            return null;
        }

        if ((resource.getIri() != null) && (! resource.getIri().isBlank()))
        {
            return resource.getIri();
        }

        if ((resource.getIdentifiers() != null) && (! resource.getIdentifiers().isEmpty()))
        {
            return resource.getIdentifiers().get(0);
        }

        if ((resource.getTitle() != null) && (! resource.getTitle().isBlank()))
        {
            return resource.getTitle();
        }

        return null;
    }


    /**
     * Catalog the catalogs, datasets, dataset series and data services described in a DCAT document.
     *
     * @param document document to catalog
     * @param source name of the caller - used to describe where the metadata came from
     * @return description of the elements that were created or updated
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public DcatMappingResult catalogueDocument(DcatDocument document,
                                               String       source) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        DcatMappingResult result = new DcatMappingResult();

        if (document == null)
        {
            result.addWarning("There is no document to catalog");
            return result;
        }

        /*
         * This map is how the references between the resources in the document are resolved.
         */
        Map<String, String> nodeIdToGUID = new HashMap<>();

        if (document.getDatasets() != null)
        {
            for (DcatDataset dataset : document.getDatasets())
            {
                if (dataset != null)
                {
                    String datasetGUID = catalogueDataset(dataset, result);

                    if ((datasetGUID != null) && (dataset.getNodeId() != null))
                    {
                        nodeIdToGUID.put(dataset.getNodeId(), datasetGUID);
                    }
                }
            }
        }

        if (document.getDataServices() != null)
        {
            for (DcatDataService dataService : document.getDataServices())
            {
                if (dataService != null)
                {
                    String dataServiceGUID = catalogueDataService(dataService, result);

                    if ((dataServiceGUID != null) && (dataService.getNodeId() != null))
                    {
                        nodeIdToGUID.put(dataService.getNodeId(), dataServiceGUID);
                    }
                }
            }
        }

        if (document.getDatasets() != null)
        {
            for (DcatDataset dataset : document.getDatasets())
            {
                if ((dataset instanceof DcatDatasetSeries series) && (series.getSeriesMembers() != null) &&
                    (nodeIdToGUID.get(series.getNodeId()) != null))
                {
                    String seriesGUID = nodeIdToGUID.get(series.getNodeId());

                    for (String memberId : series.getSeriesMembers())
                    {
                        String memberGUID = resolveReference(memberId, DATASET_QUALIFIED_NAME_PREFIX, nodeIdToGUID);

                        if (memberGUID != null)
                        {
                            linkDataSetContent(seriesGUID, memberGUID, source);
                        }
                        else
                        {
                            result.addWarning("Dataset " + memberId + " is a member of dataset series " + getResourceKey(series) + " but it has not been catalogued, so it is not linked to the series");
                        }
                    }
                }
            }
        }

        if (document.getDataServices() != null)
        {
            for (DcatDataService dataService : document.getDataServices())
            {
                if ((dataService != null) && (dataService.getServesDatasets() != null) && (nodeIdToGUID.get(dataService.getNodeId()) != null))
                {
                    String dataServiceGUID = nodeIdToGUID.get(dataService.getNodeId());

                    for (String datasetId : dataService.getServesDatasets())
                    {
                        String datasetGUID = resolveReference(datasetId, DATASET_QUALIFIED_NAME_PREFIX, nodeIdToGUID);

                        if (datasetGUID != null)
                        {
                            linkDataSetContent(datasetGUID, dataServiceGUID, source);
                        }
                        else
                        {
                            result.addWarning("Dataset " + datasetId + " is served by data service " + getResourceKey(dataService) + " but it has not been catalogued, so it is not linked to the service");
                        }
                    }
                }
            }
        }

        if (document.getCatalogs() != null)
        {
            Map<String, String> catalogNodeIdToGUID = new HashMap<>();

            for (DcatCatalog catalog : document.getCatalogs())
            {
                if (catalog != null)
                {
                    String catalogGUID = catalogueCatalog(catalog, nodeIdToGUID, source, result);

                    if ((catalogGUID != null) && (catalog.getNodeId() != null))
                    {
                        catalogNodeIdToGUID.put(catalog.getNodeId(), catalogGUID);
                    }
                }
            }

            for (DcatCatalog catalog : document.getCatalogs())
            {
                if ((catalog != null) && (catalog.getCatalogIRIs() != null) && (catalogNodeIdToGUID.get(catalog.getNodeId()) != null))
                {
                    String      catalogGUID = catalogNodeIdToGUID.get(catalog.getNodeId());
                    Set<String> memberGUIDs = getLinkedElementGUIDs(catalogGUID, 1, OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName);

                    for (String nestedCatalogId : catalog.getCatalogIRIs())
                    {
                        String nestedCatalogGUID = resolveReference(nestedCatalogId, CATALOG_QUALIFIED_NAME_PREFIX, catalogNodeIdToGUID);

                        if ((nestedCatalogGUID != null) && (! nestedCatalogGUID.equals(catalogGUID)))
                        {
                            addCatalogMember(catalogGUID, nestedCatalogGUID, memberGUIDs, source);
                        }
                    }
                }
            }
        }

        return result;
    }


    /**
     * Find the open metadata element for a resource that is referred to from another resource.  The resource may
     * have been described in the same document or catalogued from an earlier one.
     *
     * @param reference IRI or document node identifier of the resource
     * @param qualifiedNamePrefix prefix of the qualified name for this kind of resource
     * @param nodeIdToGUID the elements catalogued from this document
     * @return unique identifier of the element or null if the resource has not been catalogued
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private String resolveReference(String              reference,
                                    String              qualifiedNamePrefix,
                                    Map<String, String> nodeIdToGUID) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        if (reference == null)
        {
            return null;
        }

        String elementGUID = nodeIdToGUID.get(reference);

        if ((elementGUID == null) && (! reference.startsWith("_:")))
        {
            elementGUID = findElementGUID(qualifiedNamePrefix + reference);
        }

        return elementGUID;
    }


    /**
     * Catalog a dataset or dataset series as a DataSet asset, with an asset for each of its distributions.
     *
     * @param dataset dataset to catalog
     * @param result description of what has been catalogued
     * @return unique identifier of the data set, or null if the dataset could not be catalogued
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private String catalogueDataset(DcatDataset       dataset,
                                    DcatMappingResult result) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        String kind = (dataset instanceof DcatDatasetSeries) ? DATASET_SERIES_KIND : DATASET_KIND;
        String key  = getResourceKey(dataset);

        if (key == null)
        {
            result.addWarning("A " + kind + " has no IRI, identifier or title so it can not be catalogued");
            return null;
        }

        String            qualifiedName = DATASET_QUALIFIED_NAME_PREFIX + key;
        AssetClient       assetClient   = context.getAssetClient(OpenMetadataType.DATA_SET.typeName);
        DataSetProperties properties    = new DataSetProperties();

        fillAssetProperties(dataset, kind, key, qualifiedName, properties);

        Map<String, String> additionalProperties = properties.getAdditionalProperties();

        putIfPresent(additionalProperties, ACCRUAL_PERIODICITY, dataset.getAccrualPeriodicity());
        putIfPresent(additionalProperties, SPATIAL_JSON, toJSON(dataset.getSpatial()));
        putIfPresent(additionalProperties, TEMPORAL_JSON, toJSON(dataset.getTemporal()));
        putIfPresent(additionalProperties, SPATIAL_RESOLUTION_IN_METERS, dataset.getSpatialResolutionInMeters());
        putIfPresent(additionalProperties, TEMPORAL_RESOLUTION, dataset.getTemporalResolution());

        String datasetGUID = findElementGUID(qualifiedName);

        if (datasetGUID == null)
        {
            datasetGUID = assetClient.createAsset(new NewElementOptions(assetClient.getMetadataSourceOptions()), null, properties, null);
            result.addCataloguedResource(new DcatMappingResult.CataloguedResource(kind, key, qualifiedName, datasetGUID, DcatMappingResult.Action.CREATED));
        }
        else
        {
            assetClient.updateAsset(datasetGUID, assetClient.getUpdateOptions(false), properties);
            result.addCataloguedResource(new DcatMappingResult.CataloguedResource(kind, key, qualifiedName, datasetGUID, DcatMappingResult.Action.UPDATED));
        }

        linkAttachments(dataset, datasetGUID, qualifiedName);
        catalogueDistributions(dataset, key, datasetGUID);

        return datasetGUID;
    }


    /**
     * Catalog the distributions of a dataset.  Each one is an asset that is anchored to the data set.  The assets for
     * distributions that are no longer part of the dataset are removed.
     *
     * @param dataset dataset from the document
     * @param datasetKey the value that identifies the dataset
     * @param datasetGUID unique identifier of the data set asset
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private void catalogueDistributions(DcatDataset dataset,
                                        String      datasetKey,
                                        String      datasetGUID) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        final String methodName = "catalogueDistributions";

        Set<String> currentDistributionGUIDs = new HashSet<>();

        if (dataset.getDistributions() != null)
        {
            int index = 0;

            for (DcatDistribution distribution : dataset.getDistributions())
            {
                if (distribution != null)
                {
                    index++;
                    currentDistributionGUIDs.add(catalogueDistribution(dataset, distribution, datasetKey, datasetGUID, index));
                }
            }
        }

        AssetClient assetClient = context.getAssetClient();

        for (RelatedMetadataElement content : getRelationships(datasetGUID, 1, OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName))
        {
            String contentQualifiedName = propertyHelper.getStringProperty(this.getClass().getName(),
                                                                           OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                           content.getElement().getElementProperties(),
                                                                           methodName);

            if ((contentQualifiedName != null) && (contentQualifiedName.startsWith(DISTRIBUTION_QUALIFIED_NAME_PREFIX)) &&
                (! currentDistributionGUIDs.contains(content.getElement().getElementGUID())))
            {
                assetClient.deleteAsset(content.getElement().getElementGUID(), assetClient.getDeleteOptions(true));
            }
        }
    }


    /**
     * Catalog a distribution of a dataset as a DataFile (if it has a download URL) or a DataStore.
     *
     * @param dataset dataset that the distribution belongs to
     * @param distribution distribution to catalog
     * @param datasetKey the value that identifies the dataset
     * @param datasetGUID unique identifier of the data set asset
     * @param index position of the distribution in the dataset's list
     * @return unique identifier of the asset
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private String catalogueDistribution(DcatDataset      dataset,
                                         DcatDistribution distribution,
                                         String           datasetKey,
                                         String           datasetGUID,
                                         int              index) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        String location = (distribution.getDownloadURL() != null) ? distribution.getDownloadURL() : distribution.getAccessURL();
        String qualifiedName;

        if (distribution.getIri() != null)
        {
            qualifiedName = DISTRIBUTION_QUALIFIED_NAME_PREFIX + distribution.getIri();
        }
        else if (location != null)
        {
            qualifiedName = DISTRIBUTION_QUALIFIED_NAME_PREFIX + datasetKey + SEPARATOR + location;
        }
        else if (distribution.getTitle() != null)
        {
            qualifiedName = DISTRIBUTION_QUALIFIED_NAME_PREFIX + datasetKey + SEPARATOR + distribution.getTitle();
        }
        else
        {
            qualifiedName = DISTRIBUTION_QUALIFIED_NAME_PREFIX + datasetKey + SEPARATOR + index;
        }

        String              typeName;
        DataStoreProperties properties;

        if (distribution.getDownloadURL() != null)
        {
            DataFileProperties fileProperties = new DataFileProperties();
            String             fileName       = getFileName(distribution.getDownloadURL());

            fileProperties.setFileName(fileName);
            fileProperties.setFileExtension(getFileExtension(fileName));
            fileProperties.setFileType((distribution.getFormat() != null) ? distribution.getFormat() : distribution.getMediaType());

            typeName   = OpenMetadataType.DATA_FILE.typeName;
            properties = fileProperties;
        }
        else
        {
            typeName   = OpenMetadataType.DATA_STORE.typeName;
            properties = new DataStoreProperties();
        }

        String displayName = distribution.getTitle();

        if (displayName == null)
        {
            String datasetName = (dataset.getTitle() != null) ? dataset.getTitle() : datasetKey;

            displayName = (distribution.getFormat() != null) ? datasetName + " (" + distribution.getFormat() + ")" : datasetName + " distribution " + index;
        }

        properties.setQualifiedName(qualifiedName);
        properties.setDisplayName(displayName);
        properties.setDescription(distribution.getDescription());
        properties.setResourceName(location);
        properties.setPathName(location);
        properties.setStoreCreateTime(getDate(distribution.getIssued()));
        properties.setStoreUpdateTime(getDate(distribution.getModified()));

        Map<String, String> additionalProperties = new HashMap<>();

        putIfPresent(additionalProperties, KIND, DISTRIBUTION_KIND);
        putIfPresent(additionalProperties, IRI, distribution.getIri());
        putIfPresent(additionalProperties, ACCESS_URL, distribution.getAccessURL());
        putIfPresent(additionalProperties, DOWNLOAD_URL, distribution.getDownloadURL());
        putIfPresent(additionalProperties, MEDIA_TYPE, distribution.getMediaType());
        putIfPresent(additionalProperties, FORMAT, distribution.getFormat());
        putIfPresent(additionalProperties, BYTE_SIZE, (distribution.getByteSize() == null) ? null : distribution.getByteSize().toString());
        putIfPresent(additionalProperties, COMPRESS_FORMAT, distribution.getCompressFormat());
        putIfPresent(additionalProperties, PACKAGE_FORMAT, distribution.getPackageFormat());
        putIfPresent(additionalProperties, ISSUED, distribution.getIssued());
        putIfPresent(additionalProperties, MODIFIED, distribution.getModified());
        putIfPresent(additionalProperties, RIGHTS, distribution.getRights());
        putIfPresent(additionalProperties, ACCESS_RIGHTS, distribution.getAccessRights());
        putIfPresent(additionalProperties, CONFORMS_TO_JSON, toJSON(distribution.getConformsTo()));
        putIfPresent(additionalProperties, LANGUAGES_JSON, toJSON(distribution.getLanguages()));
        putIfPresent(additionalProperties, CHECKSUM_ALGORITHM, distribution.getChecksumAlgorithm());
        putIfPresent(additionalProperties, CHECKSUM_VALUE, distribution.getChecksumValue());
        putIfPresent(additionalProperties, ACCESS_SERVICES_JSON, toJSON(distribution.getAccessServices()));
        putIfPresent(additionalProperties, LICENSE_JSON, toJSON(distribution.getLicense()));
        putIfPresent(additionalProperties, OTHER_PROPERTIES_JSON, toJSON(distribution.getOtherProperties()));

        properties.setAdditionalProperties(additionalProperties);

        AssetClient         assetClient = context.getAssetClient(typeName);
        OpenMetadataElement existing    = findElement(qualifiedName);

        if ((existing != null) && (! typeName.equals(existing.getType().getTypeName())))
        {
            /*
             * The distribution has gained or lost its download URL, which changes the type of asset that represents it.
             */
            assetClient.deleteAsset(existing.getElementGUID(), assetClient.getDeleteOptions(true));
            existing = null;
        }

        if (existing != null)
        {
            assetClient.updateAsset(existing.getElementGUID(), assetClient.getUpdateOptions(false), properties);

            return existing.getElementGUID();
        }

        NewElementOptions newElementOptions = new NewElementOptions(assetClient.getMetadataSourceOptions());

        newElementOptions.setAnchorGUID(datasetGUID);
        newElementOptions.setIsOwnAnchor(false);
        newElementOptions.setParentGUID(datasetGUID);
        newElementOptions.setParentRelationshipTypeName(OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName);
        newElementOptions.setParentAtEnd1(true);

        DataSetContentProperties contentProperties = new DataSetContentProperties();

        contentProperties.setLabel("distribution");

        return assetClient.createAsset(newElementOptions, null, properties, contentProperties);
    }


    /**
     * Catalog a data service as a DeployedAPI asset with an Endpoint for each of its endpoint URLs.
     *
     * @param dataService data service to catalog
     * @param result description of what has been catalogued
     * @return unique identifier of the deployed API, or null if the data service could not be catalogued
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private String catalogueDataService(DcatDataService   dataService,
                                        DcatMappingResult result) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        String key = getResourceKey(dataService);

        if (key == null)
        {
            result.addWarning("A " + DATA_SERVICE_KIND + " has no IRI, identifier or title so it can not be catalogued");
            return null;
        }

        String                qualifiedName = DATA_SERVICE_QUALIFIED_NAME_PREFIX + key;
        AssetClient           assetClient   = context.getAssetClient(OpenMetadataType.DEPLOYED_API.typeName);
        DeployedAPIProperties properties    = new DeployedAPIProperties();

        fillAssetProperties(dataService, DATA_SERVICE_KIND, key, qualifiedName, properties);
        putIfPresent(properties.getAdditionalProperties(), ENDPOINT_DESCRIPTIONS_JSON, toJSON(dataService.getEndpointDescriptions()));

        String dataServiceGUID = findElementGUID(qualifiedName);

        if (dataServiceGUID == null)
        {
            dataServiceGUID = assetClient.createAsset(new NewElementOptions(assetClient.getMetadataSourceOptions()), null, properties, null);
            result.addCataloguedResource(new DcatMappingResult.CataloguedResource(DATA_SERVICE_KIND, key, qualifiedName, dataServiceGUID, DcatMappingResult.Action.CREATED));
        }
        else
        {
            assetClient.updateAsset(dataServiceGUID, assetClient.getUpdateOptions(false), properties);
            result.addCataloguedResource(new DcatMappingResult.CataloguedResource(DATA_SERVICE_KIND, key, qualifiedName, dataServiceGUID, DcatMappingResult.Action.UPDATED));
        }

        linkAttachments(dataService, dataServiceGUID, qualifiedName);

        if (dataService.getEndpointURLs() != null)
        {
            for (String endpointURL : dataService.getEndpointURLs())
            {
                if (endpointURL != null)
                {
                    String endpointQualifiedName = qualifiedName + SEPARATOR + OpenMetadataType.ENDPOINT.typeName + SEPARATOR + endpointURL;

                    if (findElementGUID(endpointQualifiedName) == null)
                    {
                        EndpointProperties endpointProperties = new EndpointProperties();

                        endpointProperties.setQualifiedName(endpointQualifiedName);
                        endpointProperties.setDisplayName(properties.getDisplayName() + " endpoint");
                        endpointProperties.setNetworkAddress(endpointURL);

                        NewElementOptions newElementOptions = new NewElementOptions(context.getEndpointClient().getMetadataSourceOptions());

                        newElementOptions.setAnchorGUID(dataServiceGUID);
                        newElementOptions.setIsOwnAnchor(false);
                        newElementOptions.setParentGUID(dataServiceGUID);
                        newElementOptions.setParentRelationshipTypeName(OpenMetadataType.API_ENDPOINT_RELATIONSHIP.typeName);
                        newElementOptions.setParentAtEnd1(true);

                        context.getEndpointClient().createEndpoint(newElementOptions, null, endpointProperties, new APIEndpointProperties());
                    }
                }
            }
        }

        return dataServiceGUID;
    }


    /**
     * Catalog a catalog as a CollectionFolder whose members are the datasets and data services that it lists.
     *
     * @param catalog catalog to catalog
     * @param nodeIdToGUID the datasets and data services catalogued from this document
     * @param source name of the caller
     * @param result description of what has been catalogued
     * @return unique identifier of the collection, or null if the catalog could not be catalogued
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private String catalogueCatalog(DcatCatalog         catalog,
                                    Map<String, String> nodeIdToGUID,
                                    String              source,
                                    DcatMappingResult   result) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        String key = getResourceKey(catalog);

        if (key == null)
        {
            result.addWarning("A " + CATALOG_KIND + " has no IRI, identifier or title so it can not be catalogued");
            return null;
        }

        String                     qualifiedName    = CATALOG_QUALIFIED_NAME_PREFIX + key;
        CollectionClient           collectionClient = context.getCollectionClient(OpenMetadataType.COLLECTION_FOLDER.typeName);
        CollectionFolderProperties properties       = new CollectionFolderProperties();

        fillReferenceableProperties(catalog, CATALOG_KIND, key, qualifiedName, properties);

        Map<String, String> additionalProperties = properties.getAdditionalProperties();

        putIfPresent(additionalProperties, HOMEPAGE, catalog.getHomepage());
        putIfPresent(additionalProperties, THEME_TAXONOMIES_JSON, toJSON(catalog.getThemeTaxonomies()));
        putIfPresent(additionalProperties, CREATORS_JSON, toJSON(catalog.getCreators()));

        if (properties.getURL() == null)
        {
            properties.setURL(catalog.getHomepage());
        }

        String catalogGUID = findElementGUID(qualifiedName);

        if (catalogGUID == null)
        {
            catalogGUID = collectionClient.createCollection(new NewElementOptions(collectionClient.getMetadataSourceOptions()), null, properties, null);
            result.addCataloguedResource(new DcatMappingResult.CataloguedResource(CATALOG_KIND, key, qualifiedName, catalogGUID, DcatMappingResult.Action.CREATED));
        }
        else
        {
            collectionClient.updateCollection(catalogGUID, collectionClient.getUpdateOptions(false), properties);
            result.addCataloguedResource(new DcatMappingResult.CataloguedResource(CATALOG_KIND, key, qualifiedName, catalogGUID, DcatMappingResult.Action.UPDATED));
        }

        linkAttachments(catalog, catalogGUID, qualifiedName);

        Set<String> memberGUIDs = getLinkedElementGUIDs(catalogGUID, 1, OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName);

        if (catalog.getDatasetIRIs() != null)
        {
            for (String datasetId : catalog.getDatasetIRIs())
            {
                String datasetGUID = resolveReference(datasetId, DATASET_QUALIFIED_NAME_PREFIX, nodeIdToGUID);

                if (datasetGUID != null)
                {
                    addCatalogMember(catalogGUID, datasetGUID, memberGUIDs, source);
                }
                else
                {
                    result.addWarning("Dataset " + datasetId + " is listed in catalog " + key + " but it is not described in the document and has not been catalogued before, so it is not a member of the catalog's collection");
                }
            }
        }

        if (catalog.getServiceIRIs() != null)
        {
            for (String serviceId : catalog.getServiceIRIs())
            {
                String serviceGUID = resolveReference(serviceId, DATA_SERVICE_QUALIFIED_NAME_PREFIX, nodeIdToGUID);

                if (serviceGUID != null)
                {
                    addCatalogMember(catalogGUID, serviceGUID, memberGUIDs, source);
                }
                else
                {
                    result.addWarning("Data service " + serviceId + " is listed in catalog " + key + " but it is not described in the document and has not been catalogued before, so it is not a member of the catalog's collection");
                }
            }
        }

        return catalogGUID;
    }


    /**
     * Add an element to the collection for a catalog, unless it is already a member.
     *
     * @param catalogGUID unique identifier of the collection
     * @param memberGUID unique identifier of the new member
     * @param memberGUIDs the existing members (updated by this method)
     * @param source name of the caller
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private void addCatalogMember(String      catalogGUID,
                                  String      memberGUID,
                                  Set<String> memberGUIDs,
                                  String      source) throws InvalidParameterException,
                                                             PropertyServerException,
                                                             UserNotAuthorizedException
    {
        if (memberGUIDs.add(memberGUID))
        {
            CollectionMembershipProperties membershipProperties = new CollectionMembershipProperties();

            membershipProperties.setMembershipRationale("Listed in the DCAT catalog");
            membershipProperties.setSource(source);

            context.getCollectionClient().addToCollection(catalogGUID,
                                                          memberGUID,
                                                          context.getCollectionClient().getMakeAnchorOptions(false),
                                                          membershipProperties);
        }
    }


    /**
     * Link a data set to an asset that supplies its data, unless they are already linked.
     *
     * @param dataSetGUID unique identifier of the data set
     * @param contentGUID unique identifier of the asset that supplies the data
     * @param source name of the caller
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private void linkDataSetContent(String dataSetGUID,
                                    String contentGUID,
                                    String source) throws InvalidParameterException,
                                                          PropertyServerException,
                                                          UserNotAuthorizedException
    {
        if ((! dataSetGUID.equals(contentGUID)) &&
            (! getLinkedElementGUIDs(dataSetGUID, 1, OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName).contains(contentGUID)))
        {
            DataSetContentProperties contentProperties = new DataSetContentProperties();

            contentProperties.setDescription("Described in a DCAT document received by " + source);

            context.getAssetClient().linkDataSetContent(dataSetGUID,
                                                        contentGUID,
                                                        context.getAssetClient().getMakeAnchorOptions(false),
                                                        contentProperties);
        }
    }


    /**
     * Fill in the properties of an asset from a DCAT resource.
     *
     * @param resource resource from the document
     * @param kind kind of resource
     * @param key the value that identifies the resource
     * @param qualifiedName qualified name for the asset
     * @param properties properties to fill
     */
    private void fillAssetProperties(DcatResource    resource,
                                     String          kind,
                                     String          key,
                                     String          qualifiedName,
                                     AssetProperties properties)
    {
        fillReferenceableProperties(resource, kind, key, qualifiedName, properties);

        properties.setResourceName(resource.getIri());

        if (resource.getCreators() != null)
        {
            List<String> authors = new ArrayList<>();

            for (DcatAgent creator : resource.getCreators())
            {
                if ((creator != null) && (creator.getName() != null))
                {
                    authors.add(creator.getName());
                }
            }

            if ((! authors.isEmpty()) && (properties instanceof DataAssetProperties dataAssetProperties))
            {
                dataAssetProperties.setAuthors(authors);
            }

            putIfPresent(properties.getAdditionalProperties(), CREATORS_JSON, toJSON(resource.getCreators()));
        }
    }


    /**
     * Fill in the properties that are common to all the open metadata elements created from a DCAT resource.
     *
     * @param resource resource from the document
     * @param kind kind of resource
     * @param key the value that identifies the resource
     * @param qualifiedName qualified name for the element
     * @param properties properties to fill
     */
    private void fillReferenceableProperties(DcatResource            resource,
                                             String                  kind,
                                             String                  key,
                                             String                  qualifiedName,
                                             ReferenceableProperties properties)
    {
        properties.setQualifiedName(qualifiedName);
        properties.setDisplayName((resource.getTitle() != null) ? resource.getTitle() : key);
        properties.setDescription(resource.getDescription());
        properties.setVersionIdentifier(resource.getVersion());
        properties.setURL(resource.getLandingPage());

        if ((resource.getIdentifiers() != null) && (! resource.getIdentifiers().isEmpty()))
        {
            properties.setIdentifier(resource.getIdentifiers().get(0));
        }

        Map<String, String> additionalProperties = new HashMap<>();

        putIfPresent(additionalProperties, KIND, kind);
        putIfPresent(additionalProperties, IRI, resource.getIri());
        putIfPresent(additionalProperties, ISSUED, resource.getIssued());
        putIfPresent(additionalProperties, MODIFIED, resource.getModified());
        putIfPresent(additionalProperties, RESOURCE_TYPE, resource.getResourceType());
        putIfPresent(additionalProperties, RIGHTS, resource.getRights());
        putIfPresent(additionalProperties, ACCESS_RIGHTS, resource.getAccessRights());
        putIfPresent(additionalProperties, VERSION_NOTES, resource.getVersionNotes());
        putIfPresent(additionalProperties, STATUS, resource.getStatus());
        putIfPresent(additionalProperties, PREVIOUS_VERSION, resource.getPreviousVersion());
        putIfPresent(additionalProperties, LANGUAGES_JSON, toJSON(resource.getLanguages()));
        putIfPresent(additionalProperties, THEMES_JSON, toJSON(resource.getThemes()));
        putIfPresent(additionalProperties, PUBLISHER_JSON, toJSON(resource.getPublisher()));
        putIfPresent(additionalProperties, LICENSE_JSON, toJSON(resource.getLicense()));
        putIfPresent(additionalProperties, TITLE_TRANSLATIONS_JSON, toJSON(resource.getTitleTranslations()));
        putIfPresent(additionalProperties, DESCRIPTION_TRANSLATIONS_JSON, toJSON(resource.getDescriptionTranslations()));
        putIfPresent(additionalProperties, OTHER_PROPERTIES_JSON, toJSON(resource.getOtherProperties()));

        if ((resource.getIdentifiers() != null) && (resource.getIdentifiers().size() > 1))
        {
            putIfPresent(additionalProperties, IDENTIFIERS_JSON, toJSON(resource.getIdentifiers()));
        }

        properties.setAdditionalProperties(additionalProperties);
    }


    /**
     * Attach the elements that describe a resource: its keywords, publisher, contact points, licence and the standards
     * and pages that it refers to.
     *
     * @param resource resource from the document
     * @param elementGUID unique identifier of the element that represents the resource
     * @param qualifiedName qualified name of the element that represents the resource
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private void linkAttachments(DcatResource resource,
                                 String       elementGUID,
                                 String       qualifiedName) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        addSearchKeywords(resource.getKeywords(), elementGUID);
        linkPublisher(resource.getPublisher(), elementGUID);
        linkContactPoints(resource.getContactPoints(), elementGUID, qualifiedName);
        linkLicense(resource.getLicense(), elementGUID);
        linkExternalReferences(resource.getConformsTo(), CONFORMS_TO_LABEL, elementGUID);
        linkExternalReferences(resource.getDocumentation(), DOCUMENTATION_LABEL, elementGUID);
    }


    /**
     * Attach each keyword to an element as a search keyword, unless the element already has it.
     *
     * @param keywords list of keywords
     * @param elementGUID unique identifier of the element
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private void addSearchKeywords(List<String> keywords,
                                   String       elementGUID) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        final String methodName = "addSearchKeywords";

        if ((keywords != null) && (! keywords.isEmpty()))
        {
            Set<String> knownKeywords = new HashSet<>();

            for (RelatedMetadataElement keyword : getRelationships(elementGUID, 1, OpenMetadataType.SEARCH_KEYWORD_LINK_RELATIONSHIP.typeName))
            {
                String keywordName = propertyHelper.getStringProperty(this.getClass().getName(),
                                                                      OpenMetadataProperty.DISPLAY_NAME.name,
                                                                      keyword.getElement().getElementProperties(),
                                                                      methodName);

                if (keywordName != null)
                {
                    knownKeywords.add(keywordName);
                }
            }

            for (String keyword : keywords)
            {
                if ((keyword != null) && (! keyword.isBlank()) && (knownKeywords.add(keyword)))
                {
                    SearchKeywordProperties properties = new SearchKeywordProperties();

                    properties.setDisplayName(keyword);

                    context.getSearchKeywordClient().addSearchKeywordToElement(elementGUID,
                                                                               context.getSearchKeywordClient().getMetadataSourceOptions(),
                                                                               null,
                                                                               properties);
                }
            }
        }
    }


    /**
     * Link an element to the organization that publishes it.  The organization is created if it is not already known.
     *
     * @param publisher publisher of the resource
     * @param elementGUID unique identifier of the element
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private void linkPublisher(DcatAgent publisher,
                               String    elementGUID) throws InvalidParameterException,
                                                             PropertyServerException,
                                                             UserNotAuthorizedException
    {
        if (publisher == null)
        {
            return;
        }

        String publisherName = (publisher.getName() != null) ? publisher.getName() : publisher.getIri();

        if ((publisherName == null) || (publisherName.isBlank()))
        {
            return;
        }

        String qualifiedName    = OpenMetadataType.ORGANIZATION.typeName + SEPARATOR + publisherName;
        String organizationGUID = findElementGUID(qualifiedName);

        if (organizationGUID == null)
        {
            OrganizationProperties properties = new OrganizationProperties();

            properties.setQualifiedName(qualifiedName);
            properties.setDisplayName(publisherName);
            properties.setURL(publisher.getHomepage());

            Map<String, String> additionalProperties = new HashMap<>();

            putIfPresent(additionalProperties, IRI, publisher.getIri());
            properties.setAdditionalProperties(additionalProperties);

            organizationGUID = context.getActorProfileClient(OpenMetadataType.ORGANIZATION.typeName).createActorProfile(new NewElementOptions(context.getActorProfileClient().getMetadataSourceOptions()),
                                                                                                                         null,
                                                                                                                         properties,
                                                                                                                         null);
        }

        if (! getLinkedElementGUIDs(elementGUID, 1, OpenMetadataType.SCOPED_BY_RELATIONSHIP.typeName).contains(organizationGUID))
        {
            context.getClassificationExplorerClient().addScopeToElement(elementGUID,
                                                                        organizationGUID,
                                                                        context.getClassificationExplorerClient().getMakeAnchorOptions(false),
                                                                        new ScopedByProperties());
        }
    }


    /**
     * Attach the contact points to an element as contact details.
     *
     * @param contactPoints contact points for the resource
     * @param elementGUID unique identifier of the element
     * @param elementQualifiedName qualified name of the element
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private void linkContactPoints(List<DcatContactPoint> contactPoints,
                                   String                 elementGUID,
                                   String                 elementQualifiedName) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        if (contactPoints == null)
        {
            return;
        }

        for (DcatContactPoint contactPoint : contactPoints)
        {
            if (contactPoint == null)
            {
                continue;
            }

            String contactName = contactPoint.getName();

            if (contactName == null)
            {
                contactName = (contactPoint.getEmail() != null) ? contactPoint.getEmail() : contactPoint.getIri();
            }

            if (contactName == null)
            {
                contactName = (contactPoint.getUrl() != null) ? contactPoint.getUrl() : contactPoint.getTelephone();
            }

            String qualifiedName = elementQualifiedName + SEPARATOR + "ContactPoint" + SEPARATOR + contactName;

            ContactDetailsProperties properties = new ContactDetailsProperties();

            properties.setQualifiedName(qualifiedName);
            properties.setDisplayName(contactName);
            properties.setContactType(contactPoint.getOrganizationName());

            if (contactPoint.getEmail() != null)
            {
                properties.setContactMethodType(ContactMethodType.EMAIL);
                properties.setContactMethodValue(contactPoint.getEmail());
            }
            else if (contactPoint.getTelephone() != null)
            {
                properties.setContactMethodType(ContactMethodType.PHONE);
                properties.setContactMethodValue(contactPoint.getTelephone());
            }
            else if (contactPoint.getUrl() != null)
            {
                properties.setContactMethodType(ContactMethodType.OTHER);
                properties.setContactMethodValue(contactPoint.getUrl());
            }

            Map<String, String> additionalProperties = new HashMap<>();

            putIfPresent(additionalProperties, CONTACT_POINT_JSON, toJSON(contactPoint));
            properties.setAdditionalProperties(additionalProperties);

            String contactDetailsGUID = findElementGUID(qualifiedName);

            if (contactDetailsGUID == null)
            {
                NewElementOptions newElementOptions = new NewElementOptions(context.getContactDetailsClient().getMetadataSourceOptions());

                newElementOptions.setAnchorGUID(elementGUID);
                newElementOptions.setIsOwnAnchor(false);
                newElementOptions.setParentGUID(elementGUID);
                newElementOptions.setParentRelationshipTypeName(OpenMetadataType.CONTACT_THROUGH_RELATIONSHIP.typeName);
                newElementOptions.setParentAtEnd1(true);

                context.getContactDetailsClient().createContactDetails(newElementOptions, null, properties, new ContactThroughProperties());
            }
            else
            {
                context.getContactDetailsClient().updateContactDetails(contactDetailsGUID,
                                                                       context.getContactDetailsClient().getUpdateOptions(false),
                                                                       properties);
            }
        }
    }


    /**
     * Link an element to the type of licence that it is made available under.  The licence type is created if it is
     * not already known.
     *
     * @param license licence from the document
     * @param elementGUID unique identifier of the element
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private void linkLicense(DcatConcept license,
                             String      elementGUID) throws InvalidParameterException,
                                                             PropertyServerException,
                                                             UserNotAuthorizedException
    {
        if (license == null)
        {
            return;
        }

        String licenseKey = (license.getIri() != null) ? license.getIri() : license.getLabel();

        if ((licenseKey == null) || (licenseKey.isBlank()))
        {
            return;
        }

        GovernanceDefinitionClient licenseTypeClient = context.getGovernanceDefinitionClient(OpenMetadataType.LICENSE_TYPE.typeName);
        String                     qualifiedName     = OpenMetadataType.LICENSE_TYPE.typeName + SEPARATOR + licenseKey;
        String                     licenseTypeGUID   = findElementGUID(qualifiedName);

        if (licenseTypeGUID == null)
        {
            LicenseTypeProperties properties = new LicenseTypeProperties();

            properties.setQualifiedName(qualifiedName);
            properties.setDisplayName((license.getLabel() != null) ? license.getLabel() : license.getIri());
            properties.setSummary("Licence referenced by a DCAT description.");
            properties.setURL(license.getIri());

            Map<String, String> additionalProperties = new HashMap<>();

            putIfPresent(additionalProperties, IRI, license.getIri());
            properties.setAdditionalProperties(additionalProperties);

            licenseTypeGUID = licenseTypeClient.createGovernanceDefinition(new NewElementOptions(licenseTypeClient.getMetadataSourceOptions()),
                                                                           null,
                                                                           properties,
                                                                           null);
        }

        if (! getLinkedElementGUIDs(elementGUID, 1, OpenMetadataType.LICENSE_RELATIONSHIP.typeName).contains(licenseTypeGUID))
        {
            LicenseProperties licenseProperties = new LicenseProperties();

            licenseProperties.setLicenseId(licenseKey);

            licenseTypeClient.licenseElement(elementGUID,
                                             licenseTypeGUID,
                                             licenseTypeClient.getMakeAnchorOptions(false),
                                             licenseProperties);
        }
    }


    /**
     * Link an element to the external references for a list of URLs.  The external references are created if they
     * are not already known.
     *
     * @param urls locations of the external resources
     * @param label describes the relationship between the element and the external resources
     * @param elementGUID unique identifier of the element
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private void linkExternalReferences(List<String> urls,
                                        String       label,
                                        String       elementGUID) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        if ((urls == null) || (urls.isEmpty()))
        {
            return;
        }

        Set<String> linkedReferenceGUIDs = getLinkedElementGUIDs(elementGUID, 1, OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName);

        for (String url : urls)
        {
            if ((url != null) && (! url.isBlank()))
            {
                String qualifiedName         = OpenMetadataType.EXTERNAL_REFERENCE.typeName + SEPARATOR + url;
                String externalReferenceGUID = findElementGUID(qualifiedName);

                if (externalReferenceGUID == null)
                {
                    ExternalReferenceProperties properties = new ExternalReferenceProperties();

                    properties.setQualifiedName(qualifiedName);
                    properties.setDisplayName(url);
                    properties.setURL(url);

                    externalReferenceGUID = context.getExternalReferenceClient().createExternalReference(new NewElementOptions(context.getExternalReferenceClient().getMetadataSourceOptions()),
                                                                                                         null,
                                                                                                         properties,
                                                                                                         null);
                }

                if (linkedReferenceGUIDs.add(externalReferenceGUID))
                {
                    ExternalReferenceLinkProperties linkProperties = new ExternalReferenceLinkProperties();

                    linkProperties.setLabel(label);

                    context.getExternalReferenceClient().linkExternalReference(elementGUID,
                                                                               externalReferenceGUID,
                                                                               context.getExternalReferenceClient().getMakeAnchorOptions(false),
                                                                               linkProperties);
                }
            }
        }
    }


    /**
     * Return the file name from a URL.
     *
     * @param url location of a file
     * @return name of the file
     */
    private String getFileName(String url)
    {
        String path = url;

        int queryStart = path.indexOf('?');

        if (queryStart >= 0)
        {
            path = path.substring(0, queryStart);
        }

        int lastSlash = path.lastIndexOf('/');

        if ((lastSlash >= 0) && (lastSlash < path.length() - 1))
        {
            return path.substring(lastSlash + 1);
        }

        return path;
    }


    /**
     * Return the extension of a file name.
     *
     * @param fileName name of a file
     * @return extension or null if the file name has none
     */
    private String getFileExtension(String fileName)
    {
        int lastDot = fileName.lastIndexOf('.');

        if ((lastDot > 0) && (lastDot < fileName.length() - 1))
        {
            return fileName.substring(lastDot + 1);
        }

        return null;
    }


    /**
     * Convert a date from a DCAT description (xsd:date, xsd:dateTime) into a Date.
     *
     * @param value string date (may be null)
     * @return date or null if the value is not in a recognized format
     */
    static Date getDate(String value)
    {
        if ((value == null) || (value.isBlank()))
        {
            return null;
        }

        try
        {
            return Date.from(OffsetDateTime.parse(value).toInstant());
        }
        catch (DateTimeParseException notOffsetDateTime)
        {
            try
            {
                return Date.from(Instant.parse(value + (value.contains("T") ? "Z" : "T00:00:00Z")));
            }
            catch (DateTimeParseException notInstant)
            {
                try
                {
                    return Date.from(LocalDate.parse(value.substring(0, Math.min(10, value.length()))).atStartOfDay().toInstant(ZoneOffset.UTC));
                }
                catch (DateTimeParseException | IndexOutOfBoundsException notDate)
                {
                    return null;
                }
            }
        }
    }
}
