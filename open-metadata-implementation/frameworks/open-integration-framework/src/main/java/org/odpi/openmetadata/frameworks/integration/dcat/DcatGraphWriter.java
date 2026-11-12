/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.dcat;

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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * DcatGraphWriter converts the DCAT beans into the structure of a JSON-LD document.  The document uses the standard
 * prefixes for the DCAT vocabulary and embeds its context so that it can be read without access to the network.
 */
class DcatGraphWriter
{
    private static final String MAILTO_PREFIX = "mailto:";

    private final Set<String> usedPrefixes = new TreeSet<>();


    /**
     * Default constructor
     */
    DcatGraphWriter()
    {
    }


    /**
     * Convert a document into the structure of a JSON-LD document.  A document that describes a single resource is
     * written as that resource; otherwise the resources are written as a graph.
     *
     * @param document document to write
     * @return map that can be serialized as JSON
     */
    Map<String, Object> write(DcatDocument document)
    {
        List<Map<String, Object>> nodes = new ArrayList<>();

        if (document.getCatalogs() != null)
        {
            for (DcatCatalog catalog : document.getCatalogs())
            {
                if (catalog != null)
                {
                    nodes.add(getCatalogNode(catalog));
                }
            }
        }

        if (document.getDatasets() != null)
        {
            for (DcatDataset dataset : document.getDatasets())
            {
                if (dataset != null)
                {
                    nodes.add(getDatasetNode(dataset));
                }
            }
        }

        if (document.getDataServices() != null)
        {
            for (DcatDataService dataService : document.getDataServices())
            {
                if (dataService != null)
                {
                    nodes.add(getDataServiceNode(dataService));
                }
            }
        }

        Map<String, Object> context = new LinkedHashMap<>();

        for (Map.Entry<String, String> prefix : DcatVocabulary.getStandardPrefixes().entrySet())
        {
            if (usedPrefixes.contains(prefix.getKey()))
            {
                context.put(prefix.getKey(), prefix.getValue());
            }
        }

        Map<String, Object> root = new LinkedHashMap<>();

        root.put("@context", context);

        if (nodes.size() == 1)
        {
            root.putAll(nodes.get(0));
        }
        else
        {
            root.put("@graph", nodes);
        }

        return root;
    }


    /**
     * Return the JSON-LD node for a catalog.
     *
     * @param catalog bean
     * @return node
     */
    private Map<String, Object> getCatalogNode(DcatCatalog catalog)
    {
        Map<String, Object> node = getResourceNode(catalog, DcatVocabulary.CATALOG);

        addDatasetProperties(node, catalog);
        putReference(node, DcatVocabulary.HOMEPAGE, catalog.getHomepage());
        putReferences(node, DcatVocabulary.THEME_TAXONOMY, catalog.getThemeTaxonomies());
        putReferences(node, DcatVocabulary.DATASET_PROPERTY, catalog.getDatasetIRIs());
        putReferences(node, DcatVocabulary.SERVICE_PROPERTY, catalog.getServiceIRIs());
        putReferences(node, DcatVocabulary.CATALOG_PROPERTY, catalog.getCatalogIRIs());
        addOtherProperties(node, catalog.getOtherProperties());

        return node;
    }


    /**
     * Return the JSON-LD node for a dataset or dataset series.
     *
     * @param dataset bean
     * @return node
     */
    private Map<String, Object> getDatasetNode(DcatDataset dataset)
    {
        Map<String, Object> node;

        if (dataset instanceof DcatDatasetSeries series)
        {
            node = getResourceNode(dataset, DcatVocabulary.DATASET_SERIES);
            addDatasetProperties(node, dataset);
            putReferences(node, DcatVocabulary.SERIES_MEMBER, series.getSeriesMembers());
        }
        else
        {
            node = getResourceNode(dataset, DcatVocabulary.DATASET);
            addDatasetProperties(node, dataset);
        }

        addOtherProperties(node, dataset.getOtherProperties());

        return node;
    }


    /**
     * Return the JSON-LD node for a data service.
     *
     * @param dataService bean
     * @return node
     */
    private Map<String, Object> getDataServiceNode(DcatDataService dataService)
    {
        Map<String, Object> node = getResourceNode(dataService, DcatVocabulary.DATA_SERVICE);

        putReferences(node, DcatVocabulary.ENDPOINT_URL, dataService.getEndpointURLs());
        putReferences(node, DcatVocabulary.ENDPOINT_DESCRIPTION, dataService.getEndpointDescriptions());
        putReferences(node, DcatVocabulary.SERVES_DATASET, dataService.getServesDatasets());
        addOtherProperties(node, dataService.getOtherProperties());

        return node;
    }


    /**
     * Return a JSON-LD node holding the properties that are common to all resources.
     *
     * @param resource bean
     * @param typeIRI IRI of the type of resource
     * @return node
     */
    private Map<String, Object> getResourceNode(DcatResource resource,
                                                String       typeIRI)
    {
        Map<String, Object> node = new LinkedHashMap<>();

        if (resource.getIri() != null)
        {
            node.put("@id", resource.getIri());
        }
        node.put("@type", term(typeIRI));

        putLocalizedText(node, DcatVocabulary.TITLE, resource.getTitle(), resource.getTitleTranslations());
        putLocalizedText(node, DcatVocabulary.DESCRIPTION, resource.getDescription(), resource.getDescriptionTranslations());
        putLiterals(node, DcatVocabulary.IDENTIFIER, resource.getIdentifiers());
        putDate(node, DcatVocabulary.ISSUED, resource.getIssued());
        putDate(node, DcatVocabulary.MODIFIED, resource.getModified());
        putValues(node, DcatVocabulary.LANGUAGE, resource.getLanguages());

        if (resource.getPublisher() != null)
        {
            node.put(term(DcatVocabulary.PUBLISHER), getAgentNode(resource.getPublisher()));
        }

        if (resource.getCreators() != null)
        {
            List<Object> creators = new ArrayList<>();

            for (DcatAgent creator : resource.getCreators())
            {
                if (creator != null)
                {
                    creators.add(getAgentNode(creator));
                }
            }

            putList(node, DcatVocabulary.CREATOR, creators);
        }

        if (resource.getContactPoints() != null)
        {
            List<Object> contactPoints = new ArrayList<>();

            for (DcatContactPoint contactPoint : resource.getContactPoints())
            {
                if (contactPoint != null)
                {
                    contactPoints.add(getContactPointNode(contactPoint));
                }
            }

            putList(node, DcatVocabulary.CONTACT_POINT, contactPoints);
        }

        putLiterals(node, DcatVocabulary.KEYWORD, resource.getKeywords());

        if (resource.getThemes() != null)
        {
            List<Object> themes = new ArrayList<>();

            for (DcatConcept theme : resource.getThemes())
            {
                if (theme != null)
                {
                    themes.add(getConceptNode(theme, DcatVocabulary.CONCEPT, DcatVocabulary.SKOS_PREF_LABEL));
                }
            }

            putList(node, DcatVocabulary.THEME, themes);
        }

        putValue(node, DcatVocabulary.TYPE, resource.getResourceType());

        if (resource.getLicense() != null)
        {
            node.put(term(DcatVocabulary.LICENSE), getConceptNode(resource.getLicense(), DcatVocabulary.DCT_NS + "LicenseDocument", DcatVocabulary.TITLE));
        }

        putValue(node, DcatVocabulary.RIGHTS, resource.getRights());
        putValue(node, DcatVocabulary.ACCESS_RIGHTS, resource.getAccessRights());
        putValues(node, DcatVocabulary.CONFORMS_TO, resource.getConformsTo());
        putReference(node, DcatVocabulary.LANDING_PAGE, resource.getLandingPage());
        putReferences(node, DcatVocabulary.PAGE, resource.getDocumentation());
        putLiteral(node, DcatVocabulary.VERSION, resource.getVersion());
        putLiteral(node, DcatVocabulary.VERSION_NOTES, resource.getVersionNotes());
        putValue(node, DcatVocabulary.STATUS, resource.getStatus());
        putReference(node, DcatVocabulary.PREVIOUS_VERSION, resource.getPreviousVersion());

        return node;
    }


    /**
     * Add the properties that are specific to datasets.
     *
     * @param node node to add to
     * @param dataset bean
     */
    private void addDatasetProperties(Map<String, Object> node,
                                      DcatDataset         dataset)
    {
        putValue(node, DcatVocabulary.ACCRUAL_PERIODICITY, dataset.getAccrualPeriodicity());
        putValues(node, DcatVocabulary.SPATIAL, dataset.getSpatial());

        if (dataset.getTemporal() != null)
        {
            List<Object> periods = new ArrayList<>();

            for (DcatPeriodOfTime period : dataset.getTemporal())
            {
                if (period != null)
                {
                    Map<String, Object> periodNode = new LinkedHashMap<>();

                    periodNode.put("@type", term(DcatVocabulary.PERIOD_OF_TIME));
                    putDate(periodNode, DcatVocabulary.START_DATE, period.getStartDate());
                    putDate(periodNode, DcatVocabulary.END_DATE, period.getEndDate());
                    periods.add(periodNode);
                }
            }

            putList(node, DcatVocabulary.TEMPORAL, periods);
        }

        putTypedLiteral(node, DcatVocabulary.SPATIAL_RESOLUTION_IN_METERS, dataset.getSpatialResolutionInMeters(), DcatVocabulary.XSD_DECIMAL);
        putTypedLiteral(node, DcatVocabulary.TEMPORAL_RESOLUTION, dataset.getTemporalResolution(), DcatVocabulary.XSD_DURATION);
        putReferences(node, DcatVocabulary.IN_SERIES, dataset.getInSeries());

        if (dataset.getDistributions() != null)
        {
            List<Object> distributions = new ArrayList<>();

            for (DcatDistribution distribution : dataset.getDistributions())
            {
                if (distribution != null)
                {
                    distributions.add(getDistributionNode(distribution));
                }
            }

            putList(node, DcatVocabulary.DISTRIBUTION_PROPERTY, distributions);
        }
    }


    /**
     * Return the JSON-LD node for a distribution.
     *
     * @param distribution bean
     * @return node
     */
    private Map<String, Object> getDistributionNode(DcatDistribution distribution)
    {
        Map<String, Object> node = new LinkedHashMap<>();

        if (distribution.getIri() != null)
        {
            node.put("@id", distribution.getIri());
        }
        node.put("@type", term(DcatVocabulary.DISTRIBUTION));

        putLiteral(node, DcatVocabulary.TITLE, distribution.getTitle());
        putLiteral(node, DcatVocabulary.DESCRIPTION, distribution.getDescription());
        putReference(node, DcatVocabulary.ACCESS_URL, distribution.getAccessURL());
        putReference(node, DcatVocabulary.DOWNLOAD_URL, distribution.getDownloadURL());
        putValue(node, DcatVocabulary.MEDIA_TYPE, distribution.getMediaType());
        putValue(node, DcatVocabulary.FORMAT, distribution.getFormat());

        if (distribution.getByteSize() != null)
        {
            putTypedLiteral(node, DcatVocabulary.BYTE_SIZE, distribution.getByteSize().toString(), DcatVocabulary.XSD_INTEGER);
        }

        putValue(node, DcatVocabulary.COMPRESS_FORMAT, distribution.getCompressFormat());
        putValue(node, DcatVocabulary.PACKAGE_FORMAT, distribution.getPackageFormat());
        putDate(node, DcatVocabulary.ISSUED, distribution.getIssued());
        putDate(node, DcatVocabulary.MODIFIED, distribution.getModified());

        if (distribution.getLicense() != null)
        {
            node.put(term(DcatVocabulary.LICENSE), getConceptNode(distribution.getLicense(), DcatVocabulary.DCT_NS + "LicenseDocument", DcatVocabulary.TITLE));
        }

        putValue(node, DcatVocabulary.RIGHTS, distribution.getRights());
        putValue(node, DcatVocabulary.ACCESS_RIGHTS, distribution.getAccessRights());
        putValues(node, DcatVocabulary.CONFORMS_TO, distribution.getConformsTo());
        putValues(node, DcatVocabulary.LANGUAGE, distribution.getLanguages());

        if (distribution.getChecksumValue() != null)
        {
            Map<String, Object> checksumNode = new LinkedHashMap<>();

            checksumNode.put("@type", term(DcatVocabulary.CHECKSUM));
            putValue(checksumNode, DcatVocabulary.CHECKSUM_ALGORITHM, distribution.getChecksumAlgorithm());
            putLiteral(checksumNode, DcatVocabulary.CHECKSUM_VALUE, distribution.getChecksumValue());
            node.put(term(DcatVocabulary.CHECKSUM_PROPERTY), checksumNode);
        }

        putReferences(node, DcatVocabulary.ACCESS_SERVICE, distribution.getAccessServices());
        addOtherProperties(node, distribution.getOtherProperties());

        return node;
    }


    /**
     * Return the JSON-LD node for an agent.
     *
     * @param agent bean
     * @return node
     */
    private Map<String, Object> getAgentNode(DcatAgent agent)
    {
        Map<String, Object> node = new LinkedHashMap<>();

        if (agent.getIri() != null)
        {
            node.put("@id", agent.getIri());
        }

        if ((agent.getName() != null) || (agent.getHomepage() != null) || (agent.getMbox() != null) || (agent.getAgentType() != null))
        {
            node.put("@type", term((agent.getAgentType() != null) ? agent.getAgentType() : DcatVocabulary.AGENT));
            putLiteral(node, DcatVocabulary.FOAF_NAME, agent.getName());
            putReference(node, DcatVocabulary.HOMEPAGE, agent.getHomepage());

            if (agent.getMbox() != null)
            {
                putReference(node, DcatVocabulary.FOAF_MBOX, MAILTO_PREFIX + agent.getMbox());
            }
        }

        return node;
    }


    /**
     * Return the JSON-LD node for a contact point.
     *
     * @param contactPoint bean
     * @return node
     */
    private Map<String, Object> getContactPointNode(DcatContactPoint contactPoint)
    {
        Map<String, Object> node = new LinkedHashMap<>();

        if (contactPoint.getIri() != null)
        {
            node.put("@id", contactPoint.getIri());
        }

        node.put("@type", term(DcatVocabulary.VCARD_KIND));
        putLiteral(node, DcatVocabulary.VCARD_FN, contactPoint.getName());

        if (contactPoint.getEmail() != null)
        {
            putReference(node, DcatVocabulary.VCARD_HAS_EMAIL, MAILTO_PREFIX + contactPoint.getEmail());
        }

        putLiteral(node, DcatVocabulary.VCARD_HAS_TELEPHONE, contactPoint.getTelephone());
        putReference(node, DcatVocabulary.VCARD_HAS_URL, contactPoint.getUrl());
        putLiteral(node, DcatVocabulary.VCARD_ORGANIZATION_NAME, contactPoint.getOrganizationName());

        return node;
    }


    /**
     * Return the JSON-LD node for a concept.
     *
     * @param concept bean
     * @param typeIRI IRI of the type of concept
     * @param labelIRI IRI of the property that holds the concept's label
     * @return node
     */
    private Map<String, Object> getConceptNode(DcatConcept concept,
                                               String      typeIRI,
                                               String      labelIRI)
    {
        Map<String, Object> node = new LinkedHashMap<>();

        if (concept.getIri() != null)
        {
            node.put("@id", concept.getIri());
        }

        if (concept.getLabel() != null)
        {
            node.put("@type", term(typeIRI));
            putLiteral(node, labelIRI, concept.getLabel());
        }

        return node;
    }


    /**
     * Add the properties that have no specific field in the beans.
     *
     * @param node node to add to
     * @param otherProperties map of property IRI to values
     */
    private void addOtherProperties(Map<String, Object>       node,
                                    Map<String, List<String>> otherProperties)
    {
        if (otherProperties != null)
        {
            for (Map.Entry<String, List<String>> otherProperty : otherProperties.entrySet())
            {
                if ((otherProperty.getKey() != null) && (! node.containsKey(term(otherProperty.getKey()))))
                {
                    putValues(node, otherProperty.getKey(), otherProperty.getValue());
                }
            }
        }
    }


    /**
     * Return the name to use for a class or property in the document, recording the prefix that it needs.
     *
     * @param iri full IRI
     * @return compact IRI
     */
    private String term(String iri)
    {
        String compactIRI = DcatVocabulary.compact(iri);

        if (! compactIRI.equals(iri))
        {
            usedPrefixes.add(compactIRI.substring(0, compactIRI.indexOf(':')));
        }

        return compactIRI;
    }


    /**
     * Return the JSON-LD form of a value that may be either an IRI or a literal.
     *
     * @param value string value
     * @return reference object or the string
     */
    private Object getValue(String value)
    {
        if (DcatGraphReader.isIRI(value))
        {
            Map<String, Object> reference = new LinkedHashMap<>();

            reference.put("@id", value);

            return reference;
        }

        return value;
    }


    /**
     * Add a list of values to a node: nothing if it is empty, the value itself if there is one and an array otherwise.
     *
     * @param node node to add to
     * @param propertyIRI IRI of the property
     * @param values values to add
     */
    private void putList(Map<String, Object> node,
                         String              propertyIRI,
                         List<Object>        values)
    {
        if ((values != null) && (! values.isEmpty()))
        {
            node.put(term(propertyIRI), (values.size() == 1) ? values.get(0) : values);
        }
    }


    /**
     * Add a string property.
     *
     * @param node node to add to
     * @param propertyIRI IRI of the property
     * @param value value to add (may be null)
     */
    private void putLiteral(Map<String, Object> node,
                            String              propertyIRI,
                            String              value)
    {
        if (value != null)
        {
            node.put(term(propertyIRI), value);
        }
    }


    /**
     * Add a string property with many values.
     *
     * @param node node to add to
     * @param propertyIRI IRI of the property
     * @param values values to add (may be null)
     */
    private void putLiterals(Map<String, Object> node,
                             String              propertyIRI,
                             List<String>        values)
    {
        if (values != null)
        {
            putList(node, propertyIRI, new ArrayList<>(values));
        }
    }


    /**
     * Add a property whose value has a datatype.
     *
     * @param node node to add to
     * @param propertyIRI IRI of the property
     * @param value value to add (may be null)
     * @param datatypeIRI IRI of the datatype
     */
    private void putTypedLiteral(Map<String, Object> node,
                                 String              propertyIRI,
                                 String              value,
                                 String              datatypeIRI)
    {
        if (value != null)
        {
            Map<String, Object> typedValue = new LinkedHashMap<>();

            typedValue.put("@type", term(datatypeIRI));
            typedValue.put("@value", value);

            node.put(term(propertyIRI), typedValue);
        }
    }


    /**
     * Add a date property.  The datatype is chosen from the format of the value.
     *
     * @param node node to add to
     * @param propertyIRI IRI of the property
     * @param value value to add (may be null)
     */
    private void putDate(Map<String, Object> node,
                         String              propertyIRI,
                         String              value)
    {
        if (value != null)
        {
            if (value.matches("^\\d{4}-\\d{2}-\\d{2}T.*"))
            {
                putTypedLiteral(node, propertyIRI, value, DcatVocabulary.XSD_DATE_TIME);
            }
            else if (value.matches("^\\d{4}-\\d{2}-\\d{2}$"))
            {
                putTypedLiteral(node, propertyIRI, value, DcatVocabulary.XSD_DATE);
            }
            else
            {
                putLiteral(node, propertyIRI, value);
            }
        }
    }


    /**
     * Add a property whose value is a reference to another resource.
     *
     * @param node node to add to
     * @param propertyIRI IRI of the property
     * @param iri IRI of the other resource (may be null)
     */
    private void putReference(Map<String, Object> node,
                              String              propertyIRI,
                              String              iri)
    {
        if (iri != null)
        {
            Map<String, Object> reference = new LinkedHashMap<>();

            reference.put("@id", iri);
            node.put(term(propertyIRI), reference);
        }
    }


    /**
     * Add a property whose values are references to other resources.
     *
     * @param node node to add to
     * @param propertyIRI IRI of the property
     * @param iris IRIs of the other resources (may be null)
     */
    private void putReferences(Map<String, Object> node,
                               String              propertyIRI,
                               List<String>        iris)
    {
        if (iris != null)
        {
            List<Object> references = new ArrayList<>();

            for (String iri : iris)
            {
                if ((iri != null) && (! iri.startsWith("_:")))
                {
                    Map<String, Object> reference = new LinkedHashMap<>();

                    reference.put("@id", iri);
                    references.add(reference);
                }
            }

            putList(node, propertyIRI, references);
        }
    }


    /**
     * Add a property whose value may be either an IRI or a literal.
     *
     * @param node node to add to
     * @param propertyIRI IRI of the property
     * @param value value to add (may be null)
     */
    private void putValue(Map<String, Object> node,
                          String              propertyIRI,
                          String              value)
    {
        if (value != null)
        {
            node.put(term(propertyIRI), getValue(value));
        }
    }


    /**
     * Add a property whose values may be either IRIs or literals.
     *
     * @param node node to add to
     * @param propertyIRI IRI of the property
     * @param values values to add (may be null)
     */
    private void putValues(Map<String, Object> node,
                           String              propertyIRI,
                           List<String>        values)
    {
        if (values != null)
        {
            List<Object> results = new ArrayList<>();

            for (String value : values)
            {
                if (value != null)
                {
                    results.add(getValue(value));
                }
            }

            putList(node, propertyIRI, results);
        }
    }


    /**
     * Add a string property that may have translations.
     *
     * @param node node to add to
     * @param propertyIRI IRI of the property
     * @param value principal value (may be null)
     * @param translations values in other languages, keyed by language tag (may be null)
     */
    private void putLocalizedText(Map<String, Object> node,
                                  String              propertyIRI,
                                  String              value,
                                  Map<String, String> translations)
    {
        if (value != null)
        {
            if ((translations == null) || (translations.isEmpty()))
            {
                node.put(term(propertyIRI), value);
            }
            else
            {
                List<Object> values = new ArrayList<>();

                values.add(value);

                for (Map.Entry<String, String> translation : translations.entrySet())
                {
                    Map<String, Object> translatedValue = new LinkedHashMap<>();

                    translatedValue.put("@language", translation.getKey());
                    translatedValue.put("@value", translation.getValue());
                    values.add(translatedValue);
                }

                node.put(term(propertyIRI), values);
            }
        }
    }
}
