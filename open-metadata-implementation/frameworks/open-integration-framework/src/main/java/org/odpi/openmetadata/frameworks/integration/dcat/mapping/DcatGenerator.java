/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.dcat.mapping;

import org.odpi.openmetadata.frameworks.integration.dcat.DcatDocumentFormatter;
import org.odpi.openmetadata.frameworks.integration.dcat.DcatVocabulary;
import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatAgent;
import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatCatalog;
import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatConcept;
import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatContactPoint;
import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatDataService;
import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatDataset;
import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatDatasetSeries;
import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatDistribution;
import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatDocument;
import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatPeriodOfTime;
import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatResource;
import org.odpi.openmetadata.frameworks.integration.ffdc.OIFErrorCode;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * DcatGenerator creates the DCAT description of open metadata elements.  A DeployedAPI asset is described as a
 * dcat:DataService and a DataSet asset is described as a dcat:Dataset (or dcat:DatasetSeries) whose distributions are
 * the assets that supply its data.  Any other type of asset is described as a dcat:Dataset with a single distribution
 * that describes how to get to the asset.  A collection is described as a dcat:Catalog.
 * <p>
 * An element that was catalogued from a DCAT description is given back its original IRI and the properties that were
 * kept in its additional properties.  Other elements are identified by an IRI built from their unique identifier.
 */
public class DcatGenerator extends DcatMappingBase
{
    private String defaultPublisherName = null;


    /**
     * Constructor
     *
     * @param context the connector context that provides access to open metadata
     */
    public DcatGenerator(ConnectorContextBase context)
    {
        super(context);
    }


    /**
     * Set up the name of the publisher to use for resources that are not linked to a publishing organization.
     *
     * @param defaultPublisherName name of an organization or null for no default publisher
     */
    public void setDefaultPublisherName(String defaultPublisherName)
    {
        this.defaultPublisherName = ((defaultPublisherName == null) || (defaultPublisherName.isBlank())) ? null : defaultPublisherName;
    }


    /**
     * Return the IRI that identifies an open metadata element in a DCAT description.
     *
     * @param element open metadata element
     * @return original IRI if the element was catalogued from a DCAT description; otherwise an IRI based on its unique identifier
     */
    public String getIRI(OpenMetadataElement element)
    {
        String iri = getAdditionalProperties(element).get(ADDITIONAL_PROPERTY_PREFIX + IRI);

        return (iri != null) ? iri : GENERATED_IRI_PREFIX + element.getElementGUID();
    }


    /**
     * Generate a DCAT document that describes a single asset.
     *
     * @param assetGUID unique identifier of the asset
     * @return document containing one dataset, dataset series or data service
     * @throws InvalidParameterException  the unique identifier is not known or is not for an asset.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public DcatDocument generateAssetDocument(String assetGUID) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        return getDocument(null, List.of(generateResource(assetGUID)));
    }


    /**
     * Assemble a document from a catalog and the resources that it lists.
     *
     * @param catalog description of the catalog (may be null)
     * @param resources descriptions of datasets, dataset series and data services
     * @return document
     */
    public DcatDocument getDocument(DcatCatalog        catalog,
                                    List<DcatResource> resources)
    {
        DcatDocument          document     = new DcatDocument();
        List<DcatDataset>     datasets     = new ArrayList<>();
        List<DcatDataService> dataServices = new ArrayList<>();

        if (resources != null)
        {
            for (DcatResource resource : resources)
            {
                if (resource instanceof DcatDataService dataService)
                {
                    dataServices.add(dataService);
                }
                else if (resource instanceof DcatDataset dataset)
                {
                    datasets.add(dataset);
                }
            }
        }

        if (catalog != null)
        {
            document.setCatalogs(List.of(catalog));
        }

        document.setDatasets(datasets.isEmpty() ? null : datasets);
        document.setDataServices(dataServices.isEmpty() ? null : dataServices);

        return document;
    }


    /**
     * Generate the DCAT description of an asset.
     *
     * @param assetGUID unique identifier of the asset
     * @return dataset, dataset series or data service
     * @throws InvalidParameterException  the unique identifier is not known or is not for an asset.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public DcatResource generateResource(String assetGUID) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        final String methodName = "generateResource";

        OpenMetadataElement asset = context.getOpenMetadataStore().getMetadataElementByGUID(assetGUID);

        if ((asset == null) || (! propertyHelper.isTypeOf(asset, OpenMetadataType.ASSET.typeName)))
        {
            throw new InvalidParameterException(OIFErrorCode.WRONG_ELEMENT_TYPE_FOR_DCAT_DOCUMENT.getMessageDefinition(assetGUID,
                                                                                                                       methodName,
                                                                                                                       (asset == null) ? "<unknown>" : asset.getType().getTypeName(),
                                                                                                                       OpenMetadataType.ASSET.typeName),
                                                this.getClass().getName(),
                                                methodName,
                                                "assetGUID");
        }

        Map<String, String> additionalProperties = getAdditionalProperties(asset);
        String              kind                 = additionalProperties.get(ADDITIONAL_PROPERTY_PREFIX + KIND);

        if ((DATA_SERVICE_KIND.equals(kind)) || ((kind == null) && (propertyHelper.isTypeOf(asset, OpenMetadataType.DEPLOYED_API.typeName))))
        {
            return getDataService(asset, additionalProperties);
        }

        return getDataset(asset, additionalProperties, DATASET_SERIES_KIND.equals(kind));
    }


    /**
     * Generate the DCAT description of a collection as a catalog.  The members of the catalog are supplied by the
     * caller, which decides which of the collection's members are to be published.
     *
     * @param collectionGUID unique identifier of the collection
     * @param members descriptions of the resources to list in the catalog
     * @return catalog
     * @throws InvalidParameterException  the unique identifier is not known or is not for a collection.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public DcatCatalog generateCatalog(String             collectionGUID,
                                       List<DcatResource> members) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String methodName = "generateCatalog";

        OpenMetadataElement collection = context.getOpenMetadataStore().getMetadataElementByGUID(collectionGUID);

        if ((collection == null) || (! propertyHelper.isTypeOf(collection, OpenMetadataType.COLLECTION.typeName)))
        {
            throw new InvalidParameterException(OIFErrorCode.WRONG_ELEMENT_TYPE_FOR_DCAT_DOCUMENT.getMessageDefinition(collectionGUID,
                                                                                                                       methodName,
                                                                                                                       (collection == null) ? "<unknown>" : collection.getType().getTypeName(),
                                                                                                                       OpenMetadataType.COLLECTION.typeName),
                                                this.getClass().getName(),
                                                methodName,
                                                "collectionGUID");
        }

        Map<String, String> additionalProperties = getAdditionalProperties(collection);
        DcatCatalog         catalog              = new DcatCatalog();

        fillResource(collection, additionalProperties, catalog);

        catalog.setHomepage(additionalProperties.get(ADDITIONAL_PROPERTY_PREFIX + HOMEPAGE));
        catalog.setThemeTaxonomies(getStringList(additionalProperties, THEME_TAXONOMIES_JSON));
        catalog.setCreators(getBeanList(additionalProperties, CREATORS_JSON, DcatAgent.class));

        List<String> datasetIRIs = new ArrayList<>();
        List<String> serviceIRIs = new ArrayList<>();

        if (members != null)
        {
            for (DcatResource member : members)
            {
                if ((member != null) && (member.getIri() != null))
                {
                    if (member instanceof DcatDataService)
                    {
                        serviceIRIs.add(member.getIri());
                    }
                    else
                    {
                        datasetIRIs.add(member.getIri());
                    }
                }
            }
        }

        catalog.setDatasetIRIs(datasetIRIs.isEmpty() ? null : datasetIRIs);
        catalog.setServiceIRIs(serviceIRIs.isEmpty() ? null : serviceIRIs);

        return catalog;
    }


    /**
     * Describe an asset as a dataset or dataset series.
     *
     * @param asset asset to describe
     * @param additionalProperties additional properties of the asset
     * @param isSeries is this a dataset series?
     * @return dataset
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private DcatDataset getDataset(OpenMetadataElement asset,
                                   Map<String, String> additionalProperties,
                                   boolean             isSeries) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        DcatDataset dataset = isSeries ? new DcatDatasetSeries() : new DcatDataset();

        fillResource(asset, additionalProperties, dataset);
        fillCreators(asset, additionalProperties, dataset);

        dataset.setAccrualPeriodicity(additionalProperties.get(ADDITIONAL_PROPERTY_PREFIX + ACCRUAL_PERIODICITY));
        dataset.setSpatial(getStringList(additionalProperties, SPATIAL_JSON));
        dataset.setTemporal(getBeanList(additionalProperties, TEMPORAL_JSON, DcatPeriodOfTime.class));
        dataset.setSpatialResolutionInMeters(additionalProperties.get(ADDITIONAL_PROPERTY_PREFIX + SPATIAL_RESOLUTION_IN_METERS));
        dataset.setTemporalResolution(additionalProperties.get(ADDITIONAL_PROPERTY_PREFIX + TEMPORAL_RESOLUTION));

        List<DcatDistribution> distributions = new ArrayList<>();
        List<String>           seriesMembers = new ArrayList<>();
        List<String>           inSeries      = new ArrayList<>();

        if (propertyHelper.isTypeOf(asset, OpenMetadataType.DATA_SET.typeName))
        {
            /*
             * The data set is at end 1 of the DataSetContent relationship and the assets that supply its data are at end 2.
             */
            for (RelatedMetadataElement content : getRelationships(asset.getElementGUID(), 1, OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName))
            {
                OpenMetadataElement contentAsset = content.getElement();

                if (propertyHelper.isTypeOf(contentAsset, OpenMetadataType.DATA_SET.typeName))
                {
                    if (isSeries)
                    {
                        seriesMembers.add(getIRI(contentAsset));
                    }
                }
                else if (! propertyHelper.isTypeOf(contentAsset, OpenMetadataType.DEPLOYED_API.typeName))
                {
                    distributions.add(getDistribution(contentAsset));
                }
            }

            for (RelatedMetadataElement supportedDataSet : getRelationships(asset.getElementGUID(), 2, OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName))
            {
                if (DATASET_SERIES_KIND.equals(getAdditionalProperties(supportedDataSet.getElement()).get(ADDITIONAL_PROPERTY_PREFIX + KIND)))
                {
                    inSeries.add(getIRI(supportedDataSet.getElement()));
                }
            }
        }
        else
        {
            /*
             * This is a data store, file or other type of asset.  It is its own distribution.
             */
            DcatDistribution distribution = getDistribution(asset);

            distribution.setIri(null);
            distributions.add(distribution);
        }

        dataset.setDistributions(distributions.isEmpty() ? null : distributions);
        dataset.setInSeries(inSeries.isEmpty() ? null : inSeries);

        if (dataset instanceof DcatDatasetSeries series)
        {
            series.setSeriesMembers(seriesMembers.isEmpty() ? null : seriesMembers);
        }

        return dataset;
    }


    /**
     * Describe an asset as a distribution of a dataset.
     *
     * @param asset asset that supplies data to a data set
     * @return distribution
     */
    private DcatDistribution getDistribution(OpenMetadataElement asset)
    {
        Map<String, String> additionalProperties = getAdditionalProperties(asset);
        DcatDistribution    distribution         = new DcatDistribution();

        distribution.setIri(getIRI(asset));
        distribution.setTitle(getStringProperty(asset, OpenMetadataProperty.DISPLAY_NAME.name));
        distribution.setDescription(getStringProperty(asset, OpenMetadataProperty.DESCRIPTION.name));
        distribution.setAccessURL(additionalProperties.get(ADDITIONAL_PROPERTY_PREFIX + ACCESS_URL));
        distribution.setDownloadURL(additionalProperties.get(ADDITIONAL_PROPERTY_PREFIX + DOWNLOAD_URL));

        if ((distribution.getAccessURL() == null) && (distribution.getDownloadURL() == null))
        {
            /*
             * A distribution needs an access URL.  Use whichever of the asset's properties locates it.
             */
            String location = getStringProperty(asset, OpenMetadataProperty.URL.name);

            if (location == null)
            {
                location = getStringProperty(asset, OpenMetadataProperty.PATH_NAME.name);
            }

            if (location == null)
            {
                location = getStringProperty(asset, OpenMetadataProperty.RESOURCE_NAME.name);
            }

            distribution.setAccessURL(location);
        }

        distribution.setMediaType(additionalProperties.get(ADDITIONAL_PROPERTY_PREFIX + MEDIA_TYPE));
        distribution.setFormat(additionalProperties.get(ADDITIONAL_PROPERTY_PREFIX + FORMAT));

        if (distribution.getFormat() == null)
        {
            distribution.setFormat(getStringProperty(asset, OpenMetadataProperty.FILE_TYPE.name));
        }

        String byteSize = additionalProperties.get(ADDITIONAL_PROPERTY_PREFIX + BYTE_SIZE);

        if (byteSize != null)
        {
            try
            {
                distribution.setByteSize(Long.parseLong(byteSize));
            }
            catch (NumberFormatException notANumber)
            {
                distribution.setByteSize(null);
            }
        }

        distribution.setCompressFormat(additionalProperties.get(ADDITIONAL_PROPERTY_PREFIX + COMPRESS_FORMAT));
        distribution.setPackageFormat(additionalProperties.get(ADDITIONAL_PROPERTY_PREFIX + PACKAGE_FORMAT));
        distribution.setIssued(additionalProperties.get(ADDITIONAL_PROPERTY_PREFIX + ISSUED));
        distribution.setModified(additionalProperties.get(ADDITIONAL_PROPERTY_PREFIX + MODIFIED));
        distribution.setRights(additionalProperties.get(ADDITIONAL_PROPERTY_PREFIX + RIGHTS));
        distribution.setAccessRights(additionalProperties.get(ADDITIONAL_PROPERTY_PREFIX + ACCESS_RIGHTS));
        distribution.setConformsTo(getStringList(additionalProperties, CONFORMS_TO_JSON));
        distribution.setLanguages(getStringList(additionalProperties, LANGUAGES_JSON));
        distribution.setChecksumAlgorithm(additionalProperties.get(ADDITIONAL_PROPERTY_PREFIX + CHECKSUM_ALGORITHM));
        distribution.setChecksumValue(additionalProperties.get(ADDITIONAL_PROPERTY_PREFIX + CHECKSUM_VALUE));
        distribution.setAccessServices(getStringList(additionalProperties, ACCESS_SERVICES_JSON));
        distribution.setLicense(getBean(additionalProperties, LICENSE_JSON, DcatConcept.class));
        distribution.setOtherProperties(getOtherProperties(additionalProperties));

        return distribution;
    }


    /**
     * Describe an asset as a data service.
     *
     * @param asset asset to describe
     * @param additionalProperties additional properties of the asset
     * @return data service
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private DcatDataService getDataService(OpenMetadataElement asset,
                                           Map<String, String> additionalProperties) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        final String methodName = "getDataService";

        DcatDataService dataService = new DcatDataService();

        fillResource(asset, additionalProperties, dataService);
        fillCreators(asset, additionalProperties, dataService);

        dataService.setEndpointDescriptions(getStringList(additionalProperties, ENDPOINT_DESCRIPTIONS_JSON));

        List<String> endpointURLs = new ArrayList<>();

        for (RelatedMetadataElement endpoint : getRelationships(asset.getElementGUID(), 1, OpenMetadataType.API_ENDPOINT_RELATIONSHIP.typeName))
        {
            String networkAddress = propertyHelper.getStringProperty(this.getClass().getName(),
                                                                     OpenMetadataProperty.NETWORK_ADDRESS.name,
                                                                     endpoint.getElement().getElementProperties(),
                                                                     methodName);

            if ((networkAddress != null) && (! endpointURLs.contains(networkAddress)))
            {
                endpointURLs.add(networkAddress);
            }
        }

        List<String> servesDatasets = new ArrayList<>();

        for (RelatedMetadataElement supportedDataSet : getRelationships(asset.getElementGUID(), 2, OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName))
        {
            servesDatasets.add(getIRI(supportedDataSet.getElement()));
        }

        dataService.setEndpointURLs(endpointURLs.isEmpty() ? null : endpointURLs);
        dataService.setServesDatasets(servesDatasets.isEmpty() ? null : servesDatasets);

        return dataService;
    }


    /**
     * Fill in the properties that are common to all DCAT resources.
     *
     * @param element open metadata element that the resource describes
     * @param additionalProperties additional properties of the element
     * @param resource resource to fill
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private void fillResource(OpenMetadataElement element,
                              Map<String, String> additionalProperties,
                              DcatResource        resource) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String methodName = "fillResource";

        String qualifiedName = getStringProperty(element, OpenMetadataProperty.QUALIFIED_NAME.name);
        String displayName   = getStringProperty(element, OpenMetadataProperty.DISPLAY_NAME.name);
        String identifier    = getStringProperty(element, OpenMetadataProperty.IDENTIFIER.name);

        resource.setIri(getIRI(element));
        resource.setNodeId(resource.getIri());
        resource.setTitle((displayName != null) ? displayName : qualifiedName);
        resource.setDescription(getStringProperty(element, OpenMetadataProperty.DESCRIPTION.name));
        resource.setVersion(getStringProperty(element, OpenMetadataProperty.VERSION_IDENTIFIER.name));
        resource.setLandingPage(getStringProperty(element, OpenMetadataProperty.URL.name));

        List<String> identifiers = getStringList(additionalProperties, IDENTIFIERS_JSON);

        if (identifiers == null)
        {
            identifiers = List.of((identifier != null) ? identifier : element.getElementGUID());
        }

        resource.setIdentifiers(identifiers);

        /*
         * An element that did not come from a DCAT description takes its dates from the open metadata repository.
         */
        resource.setIssued(additionalProperties.get(ADDITIONAL_PROPERTY_PREFIX + ISSUED));
        resource.setModified(additionalProperties.get(ADDITIONAL_PROPERTY_PREFIX + MODIFIED));

        if ((! additionalProperties.containsKey(ADDITIONAL_PROPERTY_PREFIX + KIND)) && (element.getVersions() != null))
        {
            if (resource.getIssued() == null)
            {
                resource.setIssued(formatDate(element.getVersions().getCreateTime()));
            }

            if (resource.getModified() == null)
            {
                Date updateTime = element.getVersions().getUpdateTime();

                resource.setModified(formatDate((updateTime != null) ? updateTime : element.getVersions().getCreateTime()));
            }
        }

        resource.setResourceType(additionalProperties.get(ADDITIONAL_PROPERTY_PREFIX + RESOURCE_TYPE));
        resource.setRights(additionalProperties.get(ADDITIONAL_PROPERTY_PREFIX + RIGHTS));
        resource.setAccessRights(additionalProperties.get(ADDITIONAL_PROPERTY_PREFIX + ACCESS_RIGHTS));
        resource.setVersionNotes(additionalProperties.get(ADDITIONAL_PROPERTY_PREFIX + VERSION_NOTES));
        resource.setStatus(additionalProperties.get(ADDITIONAL_PROPERTY_PREFIX + STATUS));
        resource.setPreviousVersion(additionalProperties.get(ADDITIONAL_PROPERTY_PREFIX + PREVIOUS_VERSION));
        resource.setLanguages(getStringList(additionalProperties, LANGUAGES_JSON));
        resource.setThemes(getBeanList(additionalProperties, THEMES_JSON, DcatConcept.class));
        resource.setOtherProperties(getOtherProperties(additionalProperties));

        try
        {
            resource.setTitleTranslations(DcatDocumentFormatter.fromJSONFragmentMap(additionalProperties.get(ADDITIONAL_PROPERTY_PREFIX + TITLE_TRANSLATIONS_JSON)));
            resource.setDescriptionTranslations(DcatDocumentFormatter.fromJSONFragmentMap(additionalProperties.get(ADDITIONAL_PROPERTY_PREFIX + DESCRIPTION_TRANSLATIONS_JSON)));
        }
        catch (IOException error)
        {
            resource.setTitleTranslations(null);
            resource.setDescriptionTranslations(null);
        }

        /*
         * Keywords
         */
        List<String> keywords = new ArrayList<>();

        for (RelatedMetadataElement keyword : getRelationships(element.getElementGUID(), 1, OpenMetadataType.SEARCH_KEYWORD_LINK_RELATIONSHIP.typeName))
        {
            String keywordName = propertyHelper.getStringProperty(this.getClass().getName(),
                                                                  OpenMetadataProperty.DISPLAY_NAME.name,
                                                                  keyword.getElement().getElementProperties(),
                                                                  methodName);

            if ((keywordName != null) && (! keywords.contains(keywordName)))
            {
                keywords.add(keywordName);
            }
        }

        resource.setKeywords(keywords.isEmpty() ? null : keywords);

        /*
         * Publisher - the details from the original description are used if they are for the same organization.
         */
        DcatAgent originalPublisher = getBean(additionalProperties, PUBLISHER_JSON, DcatAgent.class);
        String    publisherName     = null;

        for (RelatedMetadataElement scope : getRelationships(element.getElementGUID(), 1, OpenMetadataType.SCOPED_BY_RELATIONSHIP.typeName))
        {
            if (propertyHelper.isTypeOf(scope.getElement(), OpenMetadataType.ORGANIZATION.typeName))
            {
                publisherName = getStringProperty(scope.getElement(), OpenMetadataProperty.DISPLAY_NAME.name);

                if (publisherName != null)
                {
                    break;
                }
            }
        }

        if (publisherName == null)
        {
            publisherName = (originalPublisher != null) ? originalPublisher.getName() : defaultPublisherName;
        }

        if ((originalPublisher != null) && ((publisherName == null) || (publisherName.equals(originalPublisher.getName())) || (publisherName.equals(originalPublisher.getIri()))))
        {
            resource.setPublisher(originalPublisher);
        }
        else if (publisherName != null)
        {
            DcatAgent publisher = new DcatAgent();

            publisher.setName(publisherName);
            publisher.setAgentType(DcatVocabulary.ORGANIZATION);
            resource.setPublisher(publisher);
        }

        /*
         * Contact points
         */
        List<DcatContactPoint> contactPoints = new ArrayList<>();

        for (RelatedMetadataElement contactDetails : getRelationships(element.getElementGUID(), 1, OpenMetadataType.CONTACT_THROUGH_RELATIONSHIP.typeName))
        {
            DcatContactPoint contactPoint = getBean(getAdditionalProperties(contactDetails.getElement()), CONTACT_POINT_JSON, DcatContactPoint.class);

            if (contactPoint == null)
            {
                String contactValue = getStringProperty(contactDetails.getElement(), OpenMetadataProperty.CONTACT_METHOD_VALUE.name);

                contactPoint = new DcatContactPoint();
                contactPoint.setName(getStringProperty(contactDetails.getElement(), OpenMetadataProperty.DISPLAY_NAME.name));

                if (contactValue != null)
                {
                    if (contactValue.matches("^\\S+@\\S+$"))
                    {
                        contactPoint.setEmail(contactValue);
                    }
                    else if (contactValue.contains("://"))
                    {
                        contactPoint.setUrl(contactValue);
                    }
                    else
                    {
                        contactPoint.setTelephone(contactValue);
                    }
                }
            }

            contactPoints.add(contactPoint);
        }

        resource.setContactPoints(contactPoints.isEmpty() ? null : contactPoints);

        /*
         * Licence - the first licence type is used because a DCAT resource has a single licence.
         */
        for (RelatedMetadataElement license : getRelationships(element.getElementGUID(), 1, OpenMetadataType.LICENSE_RELATIONSHIP.typeName))
        {
            DcatConcept concept    = new DcatConcept();
            String      licenseIRI = getAdditionalProperties(license.getElement()).get(ADDITIONAL_PROPERTY_PREFIX + IRI);
            String      label      = getStringProperty(license.getElement(), OpenMetadataProperty.DISPLAY_NAME.name);

            if (licenseIRI == null)
            {
                licenseIRI = getStringProperty(license.getElement(), OpenMetadataProperty.URL.name);
            }

            concept.setIri(licenseIRI);

            if ((label != null) && (! label.equals(licenseIRI)))
            {
                concept.setLabel(label);
            }

            if ((concept.getIri() != null) || (concept.getLabel() != null))
            {
                resource.setLicense(concept);
                break;
            }
        }

        if (resource.getLicense() == null)
        {
            resource.setLicense(getBean(additionalProperties, LICENSE_JSON, DcatConcept.class));
        }

        /*
         * Standards and documentation
         */
        List<String> conformsTo    = new ArrayList<>();
        List<String> documentation = new ArrayList<>();

        for (RelatedMetadataElement externalReference : getRelationships(element.getElementGUID(), 1, OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName))
        {
            String url = getStringProperty(externalReference.getElement(), OpenMetadataProperty.URL.name);

            if (url != null)
            {
                String label = null;

                if (externalReference.getRelationshipProperties() != null)
                {
                    label = propertyHelper.getStringProperty(this.getClass().getName(),
                                                             OpenMetadataProperty.LABEL.name,
                                                             externalReference.getRelationshipProperties(),
                                                             methodName);
                }

                if (CONFORMS_TO_LABEL.equals(label))
                {
                    conformsTo.add(url);
                }
                else
                {
                    documentation.add(url);
                }
            }
        }

        resource.setConformsTo(conformsTo.isEmpty() ? null : conformsTo);
        resource.setDocumentation(documentation.isEmpty() ? null : documentation);
    }


    /**
     * Fill in the creators of a resource.  The details from the original description are used if there are any;
     * otherwise the asset's authors are used.
     *
     * @param asset asset that the resource describes
     * @param additionalProperties additional properties of the asset
     * @param resource resource to fill
     */
    private void fillCreators(OpenMetadataElement asset,
                              Map<String, String> additionalProperties,
                              DcatResource        resource)
    {
        final String methodName = "fillCreators";

        List<DcatAgent> creators = getBeanList(additionalProperties, CREATORS_JSON, DcatAgent.class);

        if ((creators == null) && (asset.getElementProperties() != null))
        {
            List<String> authors = propertyHelper.getStringArrayProperty(this.getClass().getName(),
                                                                         OpenMetadataProperty.AUTHORS.name,
                                                                         asset.getElementProperties(),
                                                                         methodName);

            if ((authors != null) && (! authors.isEmpty()))
            {
                creators = new ArrayList<>();

                for (String author : authors)
                {
                    if (author != null)
                    {
                        DcatAgent creator = new DcatAgent();

                        creator.setName(author);
                        creators.add(creator);
                    }
                }
            }
        }

        resource.setCreators(creators);
    }


    /**
     * Return the properties from the original DCAT description that have no specific place in open metadata.
     *
     * @param additionalProperties additional properties of an element
     * @return map of property IRI to values, or null
     */
    private Map<String, List<String>> getOtherProperties(Map<String, String> additionalProperties)
    {
        try
        {
            return DcatDocumentFormatter.fromJSONFragmentMapOfLists(additionalProperties.get(ADDITIONAL_PROPERTY_PREFIX + OTHER_PROPERTIES_JSON));
        }
        catch (IOException error)
        {
            return null;
        }
    }


    /**
     * Format a date as an xsd:dateTime.
     *
     * @param date date (may be null)
     * @return string or null
     */
    private String formatDate(Date date)
    {
        return (date == null) ? null : DateTimeFormatter.ISO_INSTANT.format(date.toInstant());
    }
}
