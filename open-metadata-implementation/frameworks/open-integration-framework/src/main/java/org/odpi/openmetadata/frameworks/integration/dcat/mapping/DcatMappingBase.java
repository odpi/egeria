/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.dcat.mapping;

import org.odpi.openmetadata.frameworks.integration.dcat.DcatDocumentFormatter;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * DcatMappingBase holds the names and helper methods shared by the mapper, which catalogs DCAT documents in open
 * metadata, and the generator, which creates DCAT documents from open metadata.
 * <p>
 * The mapping between the two is as follows.  A dcat:Dataset is a DataSet asset and each of its dcat:Distributions is
 * a DataFile asset (when it has a download URL) or a DataStore asset (when it only has an access URL) that is anchored
 * to the data set and linked to it with a DataSetContent relationship.  A dcat:DatasetSeries is a DataSet asset
 * linked to its member data sets by DataSetContent relationships.  A dcat:DataService is a DeployedAPI asset with an
 * Endpoint for each endpoint URL, linked to the data sets it serves by DataSetContent relationships.  A dcat:Catalog
 * is a CollectionFolder whose members are its datasets, data services and nested catalogs.  Keywords are
 * SearchKeywords, the publisher is an Organization that scopes the element, contact points are ContactDetails, the
 * licence is a LicenseType and the standards and documentation pages are ExternalReferences.  The DCAT properties that
 * have no equivalent in open metadata are kept in the additional properties with names that begin "dcat.".
 */
public abstract class DcatMappingBase
{
    /**
     * Prefix of the additional properties that hold values from a DCAT description.
     */
    public static final String ADDITIONAL_PROPERTY_PREFIX = "dcat.";

    /**
     * Separator used in qualified names.
     */
    public static final String SEPARATOR = "::";

    /**
     * Prefix of the qualified name of a data set catalogued from a dcat:Dataset or dcat:DatasetSeries.
     */
    public static final String DATASET_QUALIFIED_NAME_PREFIX = "DCATDataset" + SEPARATOR;

    /**
     * Prefix of the qualified name of an asset catalogued from a dcat:Distribution.
     */
    public static final String DISTRIBUTION_QUALIFIED_NAME_PREFIX = "DCATDistribution" + SEPARATOR;

    /**
     * Prefix of the qualified name of a deployed API catalogued from a dcat:DataService.
     */
    public static final String DATA_SERVICE_QUALIFIED_NAME_PREFIX = "DCATDataService" + SEPARATOR;

    /**
     * Prefix of the qualified name of a collection catalogued from a dcat:Catalog.
     */
    public static final String CATALOG_QUALIFIED_NAME_PREFIX = "DCATCatalog" + SEPARATOR;

    /*
     * The kinds of resource - stored in the "dcat.kind" additional property.
     */
    public static final String DATASET_KIND        = "Dataset";
    public static final String DATASET_SERIES_KIND = "DatasetSeries";
    public static final String DISTRIBUTION_KIND   = "Distribution";
    public static final String DATA_SERVICE_KIND   = "DataService";
    public static final String CATALOG_KIND        = "Catalog";

    /*
     * Names of the additional properties (without the prefix).
     */
    public static final String KIND                         = "kind";
    public static final String IRI                          = "iri";
    public static final String ISSUED                       = "issued";
    public static final String MODIFIED                     = "modified";
    public static final String RESOURCE_TYPE                = "type";
    public static final String RIGHTS                       = "rights";
    public static final String ACCESS_RIGHTS                = "accessRights";
    public static final String VERSION_NOTES                = "versionNotes";
    public static final String STATUS                       = "status";
    public static final String PREVIOUS_VERSION             = "previousVersion";
    public static final String IDENTIFIERS_JSON             = "identifiers";
    public static final String LANGUAGES_JSON               = "languages";
    public static final String THEMES_JSON                  = "themes";
    public static final String PUBLISHER_JSON               = "publisher";
    public static final String CREATORS_JSON                = "creators";
    public static final String LICENSE_JSON                 = "license";
    public static final String TITLE_TRANSLATIONS_JSON      = "titleTranslations";
    public static final String DESCRIPTION_TRANSLATIONS_JSON = "descriptionTranslations";
    public static final String OTHER_PROPERTIES_JSON        = "otherProperties";
    public static final String ACCRUAL_PERIODICITY          = "accrualPeriodicity";
    public static final String SPATIAL_JSON                 = "spatial";
    public static final String TEMPORAL_JSON                = "temporal";
    public static final String SPATIAL_RESOLUTION_IN_METERS = "spatialResolutionInMeters";
    public static final String TEMPORAL_RESOLUTION          = "temporalResolution";
    public static final String ENDPOINT_DESCRIPTIONS_JSON   = "endpointDescriptions";
    public static final String HOMEPAGE                     = "homepage";
    public static final String THEME_TAXONOMIES_JSON        = "themeTaxonomies";
    public static final String ACCESS_URL                   = "accessURL";
    public static final String DOWNLOAD_URL                 = "downloadURL";
    public static final String MEDIA_TYPE                   = "mediaType";
    public static final String FORMAT                       = "format";
    public static final String BYTE_SIZE                    = "byteSize";
    public static final String COMPRESS_FORMAT              = "compressFormat";
    public static final String PACKAGE_FORMAT               = "packageFormat";
    public static final String CONFORMS_TO_JSON             = "conformsTo";
    public static final String CHECKSUM_ALGORITHM           = "checksumAlgorithm";
    public static final String CHECKSUM_VALUE               = "checksumValue";
    public static final String ACCESS_SERVICES_JSON         = "accessServices";
    public static final String CONTACT_POINT_JSON           = "contactPoint";

    /**
     * Label on the ExternalReferenceLink relationship to a standard that the resource conforms to.
     */
    public static final String CONFORMS_TO_LABEL = "conformsTo";

    /**
     * Label on the ExternalReferenceLink relationship to a page that documents the resource.
     */
    public static final String DOCUMENTATION_LABEL = "documentation";

    /**
     * Prefix of the IRI generated for an open metadata element that was not catalogued from a DCAT description.
     */
    public static final String GENERATED_IRI_PREFIX = "urn:uuid:";

    protected final ConnectorContextBase context;
    protected final PropertyHelper       propertyHelper = new PropertyHelper();


    /**
     * Constructor
     *
     * @param context the connector context that provides access to open metadata
     */
    protected DcatMappingBase(ConnectorContextBase context)
    {
        this.context = context;
    }


    /**
     * Return the element with the requested qualified name.
     *
     * @param qualifiedName unique name
     * @return element or null if it does not exist
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    protected OpenMetadataElement findElement(String qualifiedName) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        return context.getOpenMetadataStore().getMetadataElementByUniqueName(qualifiedName, OpenMetadataProperty.QUALIFIED_NAME.name);
    }


    /**
     * Return the unique identifier of the element with the requested qualified name.
     *
     * @param qualifiedName unique name
     * @return unique identifier or null if the element does not exist
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    protected String findElementGUID(String qualifiedName) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        OpenMetadataElement element = findElement(qualifiedName);

        return (element == null) ? null : element.getElementGUID();
    }


    /**
     * Return all the relationships of a particular type that are attached to an element.
     *
     * @param elementGUID unique identifier of the starting element
     * @param startingAtEnd which end of the relationship the starting element is at (1 or 2)
     * @param relationshipTypeName type of relationship to follow
     * @return list of relationships with the element at the other end
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    protected List<RelatedMetadataElement> getRelationships(String elementGUID,
                                                            int    startingAtEnd,
                                                            String relationshipTypeName) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        final int pageSize = 100;

        List<RelatedMetadataElement> relationships = new ArrayList<>();
        int                          startFrom     = 0;

        while (true)
        {
            RelatedMetadataElementList page = context.getOpenMetadataStore().getRelatedMetadataElements(elementGUID,
                                                                                                         startingAtEnd,
                                                                                                         relationshipTypeName,
                                                                                                         startFrom,
                                                                                                         pageSize);

            if ((page == null) || (page.getElementList() == null) || (page.getElementList().isEmpty()))
            {
                return relationships;
            }

            for (RelatedMetadataElement relationship : page.getElementList())
            {
                if ((relationship != null) && (relationship.getElement() != null))
                {
                    relationships.add(relationship);
                }
            }

            if (page.getElementList().size() < pageSize)
            {
                return relationships;
            }

            startFrom = startFrom + page.getElementList().size();
        }
    }


    /**
     * Return the unique identifiers of the elements that are linked to an element by a particular type of relationship.
     *
     * @param elementGUID unique identifier of the starting element
     * @param startingAtEnd which end of the relationship the starting element is at (1 or 2)
     * @param relationshipTypeName type of relationship to follow
     * @return set of unique identifiers
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    protected Set<String> getLinkedElementGUIDs(String elementGUID,
                                                int    startingAtEnd,
                                                String relationshipTypeName) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        Set<String> linkedGUIDs = new HashSet<>();

        for (RelatedMetadataElement relatedElement : getRelationships(elementGUID, startingAtEnd, relationshipTypeName))
        {
            linkedGUIDs.add(relatedElement.getElement().getElementGUID());
        }

        return linkedGUIDs;
    }


    /**
     * Return a string property of an element.
     *
     * @param element element
     * @param propertyName name of the property
     * @return value or null
     */
    protected String getStringProperty(OpenMetadataElement element,
                                       String              propertyName)
    {
        final String methodName = "getStringProperty";

        if ((element == null) || (element.getElementProperties() == null))
        {
            return null;
        }

        return propertyHelper.getStringProperty(this.getClass().getName(), propertyName, element.getElementProperties(), methodName);
    }


    /**
     * Return the additional properties of an element.
     *
     * @param element element
     * @return map - empty if the element has no additional properties
     */
    protected Map<String, String> getAdditionalProperties(OpenMetadataElement element)
    {
        final String methodName = "getAdditionalProperties";

        if ((element != null) && (element.getElementProperties() != null))
        {
            Map<String, String> additionalProperties = propertyHelper.getStringMapFromProperty(this.getClass().getName(),
                                                                                               OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                                               element.getElementProperties(),
                                                                                               methodName);

            if (additionalProperties != null)
            {
                return additionalProperties;
            }
        }

        return Map.of();
    }


    /**
     * Add a value to the additional properties if it is not null.
     *
     * @param additionalProperties map to add to
     * @param name name of the property (without the prefix)
     * @param value value (may be null)
     */
    protected static void putIfPresent(Map<String, String> additionalProperties,
                                       String              name,
                                       String              value)
    {
        if ((value != null) && (! value.isBlank()))
        {
            additionalProperties.put(ADDITIONAL_PROPERTY_PREFIX + name, value);
        }
    }


    /**
     * Convert a bean, list or map to JSON so that it can be kept in the additional properties.
     *
     * @param bean bean, list or map (may be null)
     * @return JSON string, or null if there is nothing to convert
     */
    protected static String toJSON(Object bean)
    {
        if ((bean == null) ||
            ((bean instanceof List<?> list) && (list.isEmpty())) ||
            ((bean instanceof Map<?, ?> map) && (map.isEmpty())))
        {
            return null;
        }

        try
        {
            return DcatDocumentFormatter.toJSONFragment(bean);
        }
        catch (IOException error)
        {
            return null;
        }
    }


    /**
     * Read a list of strings from a JSON additional property.
     *
     * @param additionalProperties additional properties of an element
     * @param name name of the property (without the prefix)
     * @return list or null
     */
    protected static List<String> getStringList(Map<String, String> additionalProperties,
                                                String              name)
    {
        return getBeanList(additionalProperties, name, String.class);
    }


    /**
     * Read a list of beans from a JSON additional property.
     *
     * @param additionalProperties additional properties of an element
     * @param name name of the property (without the prefix)
     * @param beanClass class of the beans
     * @param <T> type of bean
     * @return list or null
     */
    protected static <T> List<T> getBeanList(Map<String, String> additionalProperties,
                                             String              name,
                                             Class<T>            beanClass)
    {
        try
        {
            List<T> list = DcatDocumentFormatter.fromJSONFragmentList(additionalProperties.get(ADDITIONAL_PROPERTY_PREFIX + name), beanClass);

            return ((list == null) || (list.isEmpty())) ? null : list;
        }
        catch (IOException error)
        {
            return null;
        }
    }


    /**
     * Read a bean from a JSON additional property.
     *
     * @param additionalProperties additional properties of an element
     * @param name name of the property (without the prefix)
     * @param beanClass class of the bean
     * @param <T> type of bean
     * @return bean or null
     */
    protected static <T> T getBean(Map<String, String> additionalProperties,
                                   String              name,
                                   Class<T>            beanClass)
    {
        try
        {
            return DcatDocumentFormatter.fromJSONFragment(additionalProperties.get(ADDITIONAL_PROPERTY_PREFIX + name), beanClass);
        }
        catch (IOException error)
        {
            return null;
        }
    }
}
