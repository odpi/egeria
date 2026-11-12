/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.dcat;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * DcatVocabulary defines the IRIs of the classes and properties from the W3C Data Catalog Vocabulary (DCAT) version 3,
 * and the vocabularies that it reuses, that are understood by Egeria.  It also supplies the standard prefixes for these
 * vocabularies and the plain names commonly used for the terms in JSON-LD contexts.
 */
public class DcatVocabulary
{
    /*
     * Namespaces
     */
    public static final String DCAT_NS  = "http://www.w3.org/ns/dcat#";
    public static final String DCT_NS   = "http://purl.org/dc/terms/";
    public static final String FOAF_NS  = "http://xmlns.com/foaf/0.1/";
    public static final String VCARD_NS = "http://www.w3.org/2006/vcard/ns#";
    public static final String SKOS_NS  = "http://www.w3.org/2004/02/skos/core#";
    public static final String ADMS_NS  = "http://www.w3.org/ns/adms#";
    public static final String RDF_NS   = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    public static final String RDFS_NS  = "http://www.w3.org/2000/01/rdf-schema#";
    public static final String OWL_NS   = "http://www.w3.org/2002/07/owl#";
    public static final String XSD_NS   = "http://www.w3.org/2001/XMLSchema#";
    public static final String SPDX_NS  = "http://spdx.org/rdf/terms#";
    public static final String PROV_NS  = "http://www.w3.org/ns/prov#";
    public static final String SCHEMA_NS = "http://schema.org/";

    /*
     * Classes
     */
    public static final String CATALOG        = DCAT_NS + "Catalog";
    public static final String DATASET        = DCAT_NS + "Dataset";
    public static final String DATASET_SERIES = DCAT_NS + "DatasetSeries";
    public static final String DISTRIBUTION   = DCAT_NS + "Distribution";
    public static final String DATA_SERVICE   = DCAT_NS + "DataService";
    public static final String AGENT          = FOAF_NS + "Agent";
    public static final String ORGANIZATION   = FOAF_NS + "Organization";
    public static final String PERSON         = FOAF_NS + "Person";
    public static final String VCARD_KIND     = VCARD_NS + "Kind";
    public static final String PERIOD_OF_TIME = DCT_NS + "PeriodOfTime";
    public static final String CONCEPT        = SKOS_NS + "Concept";
    public static final String CHECKSUM       = SPDX_NS + "Checksum";

    /*
     * Properties of resources
     */
    public static final String TITLE               = DCT_NS + "title";
    public static final String DESCRIPTION         = DCT_NS + "description";
    public static final String IDENTIFIER          = DCT_NS + "identifier";
    public static final String ISSUED              = DCT_NS + "issued";
    public static final String MODIFIED            = DCT_NS + "modified";
    public static final String LANGUAGE            = DCT_NS + "language";
    public static final String PUBLISHER           = DCT_NS + "publisher";
    public static final String CREATOR             = DCT_NS + "creator";
    public static final String TYPE                = DCT_NS + "type";
    public static final String LICENSE             = DCT_NS + "license";
    public static final String RIGHTS              = DCT_NS + "rights";
    public static final String ACCESS_RIGHTS       = DCT_NS + "accessRights";
    public static final String CONFORMS_TO         = DCT_NS + "conformsTo";
    public static final String ACCRUAL_PERIODICITY = DCT_NS + "accrualPeriodicity";
    public static final String SPATIAL             = DCT_NS + "spatial";
    public static final String TEMPORAL            = DCT_NS + "temporal";
    public static final String FORMAT              = DCT_NS + "format";
    public static final String CONTACT_POINT       = DCAT_NS + "contactPoint";
    public static final String KEYWORD             = DCAT_NS + "keyword";
    public static final String THEME               = DCAT_NS + "theme";
    public static final String LANDING_PAGE        = DCAT_NS + "landingPage";
    public static final String VERSION             = DCAT_NS + "version";
    public static final String PREVIOUS_VERSION    = DCAT_NS + "previousVersion";
    public static final String VERSION_INFO        = OWL_NS + "versionInfo";
    public static final String VERSION_NOTES       = ADMS_NS + "versionNotes";
    public static final String STATUS              = ADMS_NS + "status";
    public static final String PAGE                = FOAF_NS + "page";

    /*
     * Properties of datasets, dataset series and catalogs
     */
    public static final String DISTRIBUTION_PROPERTY        = DCAT_NS + "distribution";
    public static final String SPATIAL_RESOLUTION_IN_METERS = DCAT_NS + "spatialResolutionInMeters";
    public static final String TEMPORAL_RESOLUTION          = DCAT_NS + "temporalResolution";
    public static final String IN_SERIES                    = DCAT_NS + "inSeries";
    public static final String SERIES_MEMBER                = DCAT_NS + "seriesMember";
    public static final String START_DATE                   = DCAT_NS + "startDate";
    public static final String END_DATE                     = DCAT_NS + "endDate";
    public static final String DATASET_PROPERTY             = DCAT_NS + "dataset";
    public static final String SERVICE_PROPERTY             = DCAT_NS + "service";
    public static final String CATALOG_PROPERTY             = DCAT_NS + "catalog";
    public static final String THEME_TAXONOMY               = DCAT_NS + "themeTaxonomy";
    public static final String HOMEPAGE                     = FOAF_NS + "homepage";

    /*
     * Properties of distributions
     */
    public static final String ACCESS_URL      = DCAT_NS + "accessURL";
    public static final String DOWNLOAD_URL    = DCAT_NS + "downloadURL";
    public static final String MEDIA_TYPE      = DCAT_NS + "mediaType";
    public static final String BYTE_SIZE       = DCAT_NS + "byteSize";
    public static final String COMPRESS_FORMAT = DCAT_NS + "compressFormat";
    public static final String PACKAGE_FORMAT  = DCAT_NS + "packageFormat";
    public static final String ACCESS_SERVICE  = DCAT_NS + "accessService";
    public static final String CHECKSUM_PROPERTY = SPDX_NS + "checksum";
    public static final String CHECKSUM_ALGORITHM = SPDX_NS + "algorithm";
    public static final String CHECKSUM_VALUE  = SPDX_NS + "checksumValue";

    /*
     * Properties of data services
     */
    public static final String ENDPOINT_URL         = DCAT_NS + "endpointURL";
    public static final String ENDPOINT_DESCRIPTION = DCAT_NS + "endpointDescription";
    public static final String SERVES_DATASET       = DCAT_NS + "servesDataset";

    /*
     * Properties of agents, contact points and concepts
     */
    public static final String FOAF_NAME          = FOAF_NS + "name";
    public static final String FOAF_MBOX          = FOAF_NS + "mbox";
    public static final String VCARD_FN           = VCARD_NS + "fn";
    public static final String VCARD_HAS_EMAIL    = VCARD_NS + "hasEmail";
    public static final String VCARD_HAS_TELEPHONE = VCARD_NS + "hasTelephone";
    public static final String VCARD_HAS_URL      = VCARD_NS + "hasURL";
    public static final String VCARD_HAS_VALUE    = VCARD_NS + "hasValue";
    public static final String VCARD_ORGANIZATION_NAME = VCARD_NS + "organization-name";
    public static final String SKOS_PREF_LABEL    = SKOS_NS + "prefLabel";
    public static final String RDFS_LABEL         = RDFS_NS + "label";
    public static final String RDF_VALUE          = RDF_NS + "value";

    /*
     * Datatypes
     */
    public static final String XSD_DATE      = XSD_NS + "date";
    public static final String XSD_DATE_TIME = XSD_NS + "dateTime";
    public static final String XSD_INTEGER   = XSD_NS + "nonNegativeInteger";
    public static final String XSD_DECIMAL   = XSD_NS + "decimal";
    public static final String XSD_DURATION  = XSD_NS + "duration";

    private static final Map<String, String> STANDARD_PREFIXES = new LinkedHashMap<>();
    private static final Map<String, String> PLAIN_TERMS       = new LinkedHashMap<>();

    static
    {
        STANDARD_PREFIXES.put("dcat", DCAT_NS);
        STANDARD_PREFIXES.put("dct", DCT_NS);
        STANDARD_PREFIXES.put("foaf", FOAF_NS);
        STANDARD_PREFIXES.put("vcard", VCARD_NS);
        STANDARD_PREFIXES.put("skos", SKOS_NS);
        STANDARD_PREFIXES.put("adms", ADMS_NS);
        STANDARD_PREFIXES.put("spdx", SPDX_NS);
        STANDARD_PREFIXES.put("prov", PROV_NS);
        STANDARD_PREFIXES.put("owl", OWL_NS);
        STANDARD_PREFIXES.put("rdf", RDF_NS);
        STANDARD_PREFIXES.put("rdfs", RDFS_NS);
        STANDARD_PREFIXES.put("xsd", XSD_NS);
        STANDARD_PREFIXES.put("schema", SCHEMA_NS);
        STANDARD_PREFIXES.put("dcterms", DCT_NS);
        STANDARD_PREFIXES.put("dc", "http://purl.org/dc/elements/1.1/");
        STANDARD_PREFIXES.put("locn", "http://www.w3.org/ns/locn#");
        STANDARD_PREFIXES.put("odrl", "http://www.w3.org/ns/odrl/2/");
        STANDARD_PREFIXES.put("time", "http://www.w3.org/2006/time#");
        STANDARD_PREFIXES.put("org", "http://www.w3.org/ns/org#");
        STANDARD_PREFIXES.put("dcatap", "http://data.europa.eu/r5r/");
        STANDARD_PREFIXES.put("pod", "https://project-open-data.cio.gov/v1.1/schema#");

        for (String className : new String[]{"Catalog", "Dataset", "DatasetSeries", "Distribution", "DataService"})
        {
            PLAIN_TERMS.put(className, DCAT_NS + className);
        }

        for (String propertyName : new String[]{"contactPoint", "keyword", "theme", "landingPage", "version", "previousVersion",
                                                "distribution", "spatialResolutionInMeters", "temporalResolution", "inSeries",
                                                "seriesMember", "startDate", "endDate", "dataset", "service", "catalog",
                                                "themeTaxonomy", "accessURL", "downloadURL", "mediaType", "byteSize",
                                                "compressFormat", "packageFormat", "accessService", "endpointURL",
                                                "endpointDescription", "servesDataset"})
        {
            PLAIN_TERMS.put(propertyName, DCAT_NS + propertyName);
        }

        for (String propertyName : new String[]{"title", "description", "identifier", "issued", "modified", "language",
                                                "publisher", "creator", "license", "rights", "accessRights", "conformsTo",
                                                "accrualPeriodicity", "spatial", "temporal", "format"})
        {
            PLAIN_TERMS.put(propertyName, DCT_NS + propertyName);
        }

        PLAIN_TERMS.put("name", FOAF_NAME);
        PLAIN_TERMS.put("mbox", FOAF_MBOX);
        PLAIN_TERMS.put("homepage", HOMEPAGE);
        PLAIN_TERMS.put("page", PAGE);
        PLAIN_TERMS.put("Agent", AGENT);
        PLAIN_TERMS.put("Organization", ORGANIZATION);
        PLAIN_TERMS.put("Person", PERSON);
        PLAIN_TERMS.put("fn", VCARD_FN);
        PLAIN_TERMS.put("hasEmail", VCARD_HAS_EMAIL);
        PLAIN_TERMS.put("hasTelephone", VCARD_HAS_TELEPHONE);
        PLAIN_TERMS.put("hasURL", VCARD_HAS_URL);
        PLAIN_TERMS.put("prefLabel", SKOS_PREF_LABEL);
        PLAIN_TERMS.put("label", RDFS_LABEL);
        PLAIN_TERMS.put("versionNotes", VERSION_NOTES);
        PLAIN_TERMS.put("status", STATUS);
        PLAIN_TERMS.put("versionInfo", VERSION_INFO);
        PLAIN_TERMS.put("checksum", CHECKSUM_PROPERTY);
        PLAIN_TERMS.put("algorithm", CHECKSUM_ALGORITHM);
        PLAIN_TERMS.put("checksumValue", CHECKSUM_VALUE);
    }


    /**
     * Private constructor since all methods are static.
     */
    private DcatVocabulary()
    {
    }


    /**
     * Return the standard prefixes for the vocabularies used in DCAT descriptions.  They are used when writing a
     * document and, when reading, to resolve a prefix that the document did not define.
     *
     * @return map of prefix to namespace IRI
     */
    public static Map<String, String> getStandardPrefixes()
    {
        return STANDARD_PREFIXES;
    }


    /**
     * Return the IRIs for the plain names that are commonly given to DCAT terms by JSON-LD contexts (for example
     * "title" for dct:title).  They are used when reading a document that relies on a context that is not embedded in
     * the document.
     *
     * @return map of plain name to IRI
     */
    public static Map<String, String> getPlainTerms()
    {
        return PLAIN_TERMS;
    }


    /**
     * Return the IRI of the standard namespace for a prefix.
     *
     * @param prefix prefix
     * @return namespace IRI or null
     */
    public static String getStandardNamespace(String prefix)
    {
        return STANDARD_PREFIXES.get(prefix);
    }


    /**
     * Shorten an IRI using the standard prefixes.
     *
     * @param iri full IRI
     * @return compact IRI (prefix:name) or the supplied IRI if it is not from one of the standard namespaces
     */
    public static String compact(String iri)
    {
        if (iri != null)
        {
            for (Map.Entry<String, String> prefix : STANDARD_PREFIXES.entrySet())
            {
                if ((iri.startsWith(prefix.getValue())) && (iri.length() > prefix.getValue().length()))
                {
                    String localName = iri.substring(prefix.getValue().length());

                    if ((! localName.contains("/")) && (! localName.contains("#")) && (! localName.contains(":")))
                    {
                        return prefix.getKey() + ":" + localName;
                    }
                }
            }
        }

        return iri;
    }
}
