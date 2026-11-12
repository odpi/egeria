/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.dcat;

import org.odpi.openmetadata.frameworks.integration.dcat.jsonld.JsonLdGraph;
import org.odpi.openmetadata.frameworks.integration.dcat.jsonld.JsonLdNode;
import org.odpi.openmetadata.frameworks.integration.dcat.jsonld.JsonLdValue;
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
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * DcatGraphReader converts the graph of nodes read from a DCAT document into the DCAT beans.  It is tolerant of the
 * different ways that DCAT publishers describe the same thing - for example, a publisher may be a nested node, a
 * reference to a node described elsewhere in the document, a bare IRI or a simple name.
 */
class DcatGraphReader
{
    private static final String MAILTO_PREFIX = "mailto:";
    private static final String TEL_PREFIX    = "tel:";

    private static final String[] LABEL_PROPERTIES = {DcatVocabulary.SKOS_PREF_LABEL,
                                                      DcatVocabulary.TITLE,
                                                      DcatVocabulary.RDFS_LABEL,
                                                      DcatVocabulary.FOAF_NAME,
                                                      DcatVocabulary.VCARD_FN,
                                                      DcatVocabulary.VCARD_HAS_VALUE,
                                                      DcatVocabulary.RDF_VALUE};

    private final JsonLdGraph graph;


    /**
     * Create a reader for a graph.
     *
     * @param graph graph of nodes from a DCAT document
     */
    DcatGraphReader(JsonLdGraph graph)
    {
        this.graph = graph;
    }


    /**
     * Build the beans for the catalogs, datasets, dataset series and data services found in the graph.
     *
     * @return document
     */
    DcatDocument read()
    {
        List<DcatCatalog>     catalogs     = new ArrayList<>();
        List<DcatDataset>     datasets     = new ArrayList<>();
        List<DcatDataService> dataServices = new ArrayList<>();

        for (JsonLdNode node : graph.getNodes())
        {
            if (node.hasType(DcatVocabulary.CATALOG))
            {
                catalogs.add(getCatalog(node));
            }
            else if (node.hasType(DcatVocabulary.DATA_SERVICE))
            {
                dataServices.add(getDataService(node));
            }
            else if (node.hasType(DcatVocabulary.DATASET_SERIES))
            {
                datasets.add(getDatasetSeries(node));
            }
            else if ((node.hasType(DcatVocabulary.DATASET)) ||
                     ((node.getTypes().isEmpty()) && (! node.getValues(DcatVocabulary.DISTRIBUTION_PROPERTY).isEmpty())))
            {
                DcatDataset dataset = new DcatDataset();

                fillDataset(node, dataset, new HashSet<>());
                datasets.add(dataset);
            }
        }

        /*
         * A dataset series can be described from either end: the series lists its members, or each member names its series.
         */
        for (DcatDataset dataset : datasets)
        {
            if ((dataset instanceof DcatDatasetSeries series) && (series.getNodeId() != null))
            {
                List<String> members = (series.getSeriesMembers() == null) ? new ArrayList<>() : new ArrayList<>(series.getSeriesMembers());

                for (DcatDataset candidate : datasets)
                {
                    if ((candidate.getNodeId() != null) && (candidate.getInSeries() != null) &&
                        (candidate.getInSeries().contains(series.getNodeId())) && (! members.contains(candidate.getNodeId())))
                    {
                        members.add(candidate.getNodeId());
                    }
                }

                series.setSeriesMembers(members.isEmpty() ? null : members);
            }
        }

        DcatDocument document = new DcatDocument();

        document.setCatalogs(catalogs.isEmpty() ? null : catalogs);
        document.setDatasets(datasets.isEmpty() ? null : datasets);
        document.setDataServices(dataServices.isEmpty() ? null : dataServices);

        return document;
    }


    /**
     * Build the bean for a catalog.
     *
     * @param node node from the graph
     * @return bean
     */
    private DcatCatalog getCatalog(JsonLdNode node)
    {
        DcatCatalog catalog = new DcatCatalog();
        Set<String> handled = new HashSet<>();

        catalog.setHomepage(getFirstText(node, handled, DcatVocabulary.HOMEPAGE));
        catalog.setThemeTaxonomies(getTexts(node, handled, DcatVocabulary.THEME_TAXONOMY));
        catalog.setDatasetIRIs(getReferences(node, handled, DcatVocabulary.DATASET_PROPERTY));
        catalog.setServiceIRIs(getReferences(node, handled, DcatVocabulary.SERVICE_PROPERTY));
        catalog.setCatalogIRIs(getReferences(node, handled, DcatVocabulary.CATALOG_PROPERTY));

        fillDataset(node, catalog, handled);

        return catalog;
    }


    /**
     * Build the bean for a dataset series.
     *
     * @param node node from the graph
     * @return bean
     */
    private DcatDatasetSeries getDatasetSeries(JsonLdNode node)
    {
        DcatDatasetSeries series  = new DcatDatasetSeries();
        Set<String>       handled = new HashSet<>();

        series.setSeriesMembers(getReferences(node, handled, DcatVocabulary.SERIES_MEMBER));

        fillDataset(node, series, handled);

        return series;
    }


    /**
     * Build the bean for a data service.
     *
     * @param node node from the graph
     * @return bean
     */
    private DcatDataService getDataService(JsonLdNode node)
    {
        DcatDataService dataService = new DcatDataService();
        Set<String>     handled     = new HashSet<>();

        dataService.setEndpointURLs(getTexts(node, handled, DcatVocabulary.ENDPOINT_URL));
        dataService.setEndpointDescriptions(getTexts(node, handled, DcatVocabulary.ENDPOINT_DESCRIPTION));
        dataService.setServesDatasets(getReferences(node, handled, DcatVocabulary.SERVES_DATASET));

        fillResource(node, dataService, handled);

        return dataService;
    }


    /**
     * Fill in the properties of a dataset.
     *
     * @param node node from the graph
     * @param dataset bean to fill
     * @param handled the properties of the node that have been mapped to the bean
     */
    private void fillDataset(JsonLdNode  node,
                             DcatDataset dataset,
                             Set<String> handled)
    {
        List<DcatDistribution> distributions = new ArrayList<>();

        handled.add(DcatVocabulary.DISTRIBUTION_PROPERTY);
        for (JsonLdValue value : node.getValues(DcatVocabulary.DISTRIBUTION_PROPERTY))
        {
            DcatDistribution distribution = getDistribution(value);

            if (distribution != null)
            {
                distributions.add(distribution);
            }
        }

        List<DcatPeriodOfTime> periods = new ArrayList<>();

        handled.add(DcatVocabulary.TEMPORAL);
        for (JsonLdValue value : node.getValues(DcatVocabulary.TEMPORAL))
        {
            JsonLdNode periodNode = getReferencedNode(value);

            if (periodNode != null)
            {
                DcatPeriodOfTime period = new DcatPeriodOfTime();

                period.setStartDate(getFirstText(periodNode, null, DcatVocabulary.START_DATE, DcatVocabulary.SCHEMA_NS + "startDate"));
                period.setEndDate(getFirstText(periodNode, null, DcatVocabulary.END_DATE, DcatVocabulary.SCHEMA_NS + "endDate"));

                if ((period.getStartDate() != null) || (period.getEndDate() != null))
                {
                    periods.add(period);
                }
            }
        }

        dataset.setDistributions(distributions.isEmpty() ? null : distributions);
        dataset.setTemporal(periods.isEmpty() ? null : periods);
        dataset.setAccrualPeriodicity(getFirstText(node, handled, DcatVocabulary.ACCRUAL_PERIODICITY));
        dataset.setSpatial(getTexts(node, handled, DcatVocabulary.SPATIAL));
        dataset.setSpatialResolutionInMeters(getFirstText(node, handled, DcatVocabulary.SPATIAL_RESOLUTION_IN_METERS));
        dataset.setTemporalResolution(getFirstText(node, handled, DcatVocabulary.TEMPORAL_RESOLUTION));
        dataset.setInSeries(getReferences(node, handled, DcatVocabulary.IN_SERIES));

        fillResource(node, dataset, handled);
    }


    /**
     * Fill in the properties that are common to all resources.
     *
     * @param node node from the graph
     * @param resource bean to fill
     * @param handled the properties of the node that have been mapped to the bean
     */
    private void fillResource(JsonLdNode   node,
                              DcatResource resource,
                              Set<String>  handled)
    {
        Map<String, String> titleTranslations       = new LinkedHashMap<>();
        Map<String, String> descriptionTranslations = new LinkedHashMap<>();

        resource.setIri(node.getIRI());
        resource.setNodeId(node.getId());
        resource.setTitle(getLocalizedText(node, handled, DcatVocabulary.TITLE, titleTranslations));
        resource.setTitleTranslations(titleTranslations.isEmpty() ? null : titleTranslations);
        resource.setDescription(getLocalizedText(node, handled, DcatVocabulary.DESCRIPTION, descriptionTranslations));
        resource.setDescriptionTranslations(descriptionTranslations.isEmpty() ? null : descriptionTranslations);
        resource.setIdentifiers(getTexts(node, handled, DcatVocabulary.IDENTIFIER));
        resource.setIssued(getFirstText(node, handled, DcatVocabulary.ISSUED));
        resource.setModified(getFirstText(node, handled, DcatVocabulary.MODIFIED));
        resource.setLanguages(getTexts(node, handled, DcatVocabulary.LANGUAGE));
        resource.setKeywords(getTexts(node, handled, DcatVocabulary.KEYWORD));
        resource.setResourceType(getFirstText(node, handled, DcatVocabulary.TYPE));
        resource.setRights(getFirstText(node, handled, DcatVocabulary.RIGHTS));
        resource.setAccessRights(getFirstText(node, handled, DcatVocabulary.ACCESS_RIGHTS));
        resource.setConformsTo(getTexts(node, handled, DcatVocabulary.CONFORMS_TO));
        resource.setLandingPage(getFirstText(node, handled, DcatVocabulary.LANDING_PAGE));
        resource.setDocumentation(getTexts(node, handled, DcatVocabulary.PAGE));
        resource.setVersion(getFirstText(node, handled, DcatVocabulary.VERSION, DcatVocabulary.VERSION_INFO));
        resource.setVersionNotes(getFirstText(node, handled, DcatVocabulary.VERSION_NOTES));
        resource.setStatus(getFirstText(node, handled, DcatVocabulary.STATUS));
        resource.setPreviousVersion(getFirstText(node, handled, DcatVocabulary.PREVIOUS_VERSION));

        handled.add(DcatVocabulary.LICENSE);
        if (! node.getValues(DcatVocabulary.LICENSE).isEmpty())
        {
            resource.setLicense(getConcept(node.getValues(DcatVocabulary.LICENSE).get(0)));
        }

        List<DcatConcept> themes = new ArrayList<>();

        handled.add(DcatVocabulary.THEME);
        for (JsonLdValue value : node.getValues(DcatVocabulary.THEME))
        {
            DcatConcept theme = getConcept(value);

            if (theme != null)
            {
                themes.add(theme);
            }
        }
        resource.setThemes(themes.isEmpty() ? null : themes);

        handled.add(DcatVocabulary.PUBLISHER);
        if (! node.getValues(DcatVocabulary.PUBLISHER).isEmpty())
        {
            resource.setPublisher(getAgent(node.getValues(DcatVocabulary.PUBLISHER).get(0)));
        }

        List<DcatAgent> creators = new ArrayList<>();

        handled.add(DcatVocabulary.CREATOR);
        for (JsonLdValue value : node.getValues(DcatVocabulary.CREATOR))
        {
            DcatAgent creator = getAgent(value);

            if (creator != null)
            {
                creators.add(creator);
            }
        }
        resource.setCreators(creators.isEmpty() ? null : creators);

        List<DcatContactPoint> contactPoints = new ArrayList<>();

        handled.add(DcatVocabulary.CONTACT_POINT);
        for (JsonLdValue value : node.getValues(DcatVocabulary.CONTACT_POINT))
        {
            DcatContactPoint contactPoint = getContactPoint(value);

            if (contactPoint != null)
            {
                contactPoints.add(contactPoint);
            }
        }
        resource.setContactPoints(contactPoints.isEmpty() ? null : contactPoints);

        resource.setOtherProperties(getOtherProperties(node, handled));
    }


    /**
     * Build the bean for a distribution.
     *
     * @param value value of a dcat:distribution property
     * @return bean or null if the distribution is not described
     */
    private DcatDistribution getDistribution(JsonLdValue value)
    {
        DcatDistribution distribution = new DcatDistribution();

        if (! value.isReference())
        {
            /*
             * The distribution is just the location of the data.
             */
            distribution.setAccessURL(value.getValue());

            return distribution;
        }

        JsonLdNode node = graph.getNode(value.getNodeId());

        if (node == null)
        {
            distribution.setIri(value.getNodeId());
            distribution.setAccessURL(value.getNodeId());

            return distribution;
        }

        Set<String> handled = new HashSet<>();

        distribution.setIri(node.getIRI());
        distribution.setTitle(getLocalizedText(node, handled, DcatVocabulary.TITLE, new LinkedHashMap<>()));
        distribution.setDescription(getLocalizedText(node, handled, DcatVocabulary.DESCRIPTION, new LinkedHashMap<>()));
        distribution.setAccessURL(getFirstText(node, handled, DcatVocabulary.ACCESS_URL));
        distribution.setDownloadURL(getFirstText(node, handled, DcatVocabulary.DOWNLOAD_URL));
        distribution.setMediaType(getFirstText(node, handled, DcatVocabulary.MEDIA_TYPE));
        distribution.setFormat(getFirstText(node, handled, DcatVocabulary.FORMAT));
        distribution.setByteSize(getLong(getFirstText(node, handled, DcatVocabulary.BYTE_SIZE)));
        distribution.setCompressFormat(getFirstText(node, handled, DcatVocabulary.COMPRESS_FORMAT));
        distribution.setPackageFormat(getFirstText(node, handled, DcatVocabulary.PACKAGE_FORMAT));
        distribution.setIssued(getFirstText(node, handled, DcatVocabulary.ISSUED));
        distribution.setModified(getFirstText(node, handled, DcatVocabulary.MODIFIED));
        distribution.setRights(getFirstText(node, handled, DcatVocabulary.RIGHTS));
        distribution.setAccessRights(getFirstText(node, handled, DcatVocabulary.ACCESS_RIGHTS));
        distribution.setConformsTo(getTexts(node, handled, DcatVocabulary.CONFORMS_TO));
        distribution.setLanguages(getTexts(node, handled, DcatVocabulary.LANGUAGE));
        distribution.setAccessServices(getTexts(node, handled, DcatVocabulary.ACCESS_SERVICE));

        handled.add(DcatVocabulary.LICENSE);
        if (! node.getValues(DcatVocabulary.LICENSE).isEmpty())
        {
            distribution.setLicense(getConcept(node.getValues(DcatVocabulary.LICENSE).get(0)));
        }

        handled.add(DcatVocabulary.CHECKSUM_PROPERTY);
        if (! node.getValues(DcatVocabulary.CHECKSUM_PROPERTY).isEmpty())
        {
            JsonLdNode checksumNode = getReferencedNode(node.getValues(DcatVocabulary.CHECKSUM_PROPERTY).get(0));

            if (checksumNode != null)
            {
                distribution.setChecksumAlgorithm(getFirstText(checksumNode, null, DcatVocabulary.CHECKSUM_ALGORITHM));
                distribution.setChecksumValue(getFirstText(checksumNode, null, DcatVocabulary.CHECKSUM_VALUE));
            }
        }

        distribution.setOtherProperties(getOtherProperties(node, handled));

        return distribution;
    }


    /**
     * Build the bean for a concept such as a theme or a licence.
     *
     * @param value value of the property
     * @return bean or null if there is no usable value
     */
    private DcatConcept getConcept(JsonLdValue value)
    {
        DcatConcept concept = new DcatConcept();

        if (value.isReference())
        {
            JsonLdNode node = graph.getNode(value.getNodeId());

            if (! value.getNodeId().startsWith(JsonLdNode.BLANK_NODE_PREFIX))
            {
                concept.setIri(value.getNodeId());
            }

            if (node != null)
            {
                concept.setLabel(getLabel(node));
            }
        }
        else if (isIRI(value.getValue()))
        {
            concept.setIri(value.getValue());
        }
        else
        {
            concept.setLabel(value.getValue());
        }

        if ((concept.getIri() == null) && (concept.getLabel() == null))
        {
            return null;
        }

        return concept;
    }


    /**
     * Build the bean for an agent.
     *
     * @param value value of the property
     * @return bean or null if there is no usable value
     */
    private DcatAgent getAgent(JsonLdValue value)
    {
        DcatAgent agent = new DcatAgent();

        if (value.isReference())
        {
            JsonLdNode node = graph.getNode(value.getNodeId());

            if (! value.getNodeId().startsWith(JsonLdNode.BLANK_NODE_PREFIX))
            {
                agent.setIri(value.getNodeId());
            }

            if (node != null)
            {
                agent.setName(getLabel(node));
                agent.setHomepage(getFirstText(node, null, DcatVocabulary.HOMEPAGE));
                agent.setMbox(stripPrefix(getFirstText(node, null, DcatVocabulary.FOAF_MBOX), MAILTO_PREFIX));

                if (! node.getTypes().isEmpty())
                {
                    agent.setAgentType(node.getTypes().iterator().next());
                }
            }
        }
        else
        {
            agent.setName(value.getValue());
        }

        if ((agent.getIri() == null) && (agent.getName() == null))
        {
            return null;
        }

        return agent;
    }


    /**
     * Build the bean for a contact point.
     *
     * @param value value of the property
     * @return bean or null if there is no usable value
     */
    private DcatContactPoint getContactPoint(JsonLdValue value)
    {
        DcatContactPoint contactPoint = new DcatContactPoint();

        if (value.isReference())
        {
            JsonLdNode node = graph.getNode(value.getNodeId());

            if (! value.getNodeId().startsWith(JsonLdNode.BLANK_NODE_PREFIX))
            {
                contactPoint.setIri(value.getNodeId());
            }

            if (node != null)
            {
                contactPoint.setName(getFirstText(node, null, DcatVocabulary.VCARD_FN, DcatVocabulary.FOAF_NAME, DcatVocabulary.RDFS_LABEL));
                contactPoint.setEmail(stripPrefix(getFirstText(node, null, DcatVocabulary.VCARD_HAS_EMAIL, DcatVocabulary.FOAF_MBOX), MAILTO_PREFIX));
                contactPoint.setTelephone(stripPrefix(getFirstText(node, null, DcatVocabulary.VCARD_HAS_TELEPHONE), TEL_PREFIX));
                contactPoint.setUrl(getFirstText(node, null, DcatVocabulary.VCARD_HAS_URL));
                contactPoint.setOrganizationName(getFirstText(node, null, DcatVocabulary.VCARD_ORGANIZATION_NAME));
            }
        }
        else if (value.getValue().startsWith(MAILTO_PREFIX))
        {
            contactPoint.setEmail(stripPrefix(value.getValue(), MAILTO_PREFIX));
        }
        else
        {
            contactPoint.setName(value.getValue());
        }

        if ((contactPoint.getIri() == null) && (contactPoint.getName() == null) && (contactPoint.getEmail() == null) &&
            (contactPoint.getTelephone() == null) && (contactPoint.getUrl() == null))
        {
            return null;
        }

        return contactPoint;
    }


    /**
     * Return the properties of a node that have not been mapped to a specific field in a bean.  Values that are
     * references to blank nodes are represented by the label of the blank node, if it has one.
     *
     * @param node node from the graph
     * @param handled the properties of the node that have been mapped to the bean
     * @return map of property IRI to values, or null if there are none
     */
    private Map<String, List<String>> getOtherProperties(JsonLdNode  node,
                                                         Set<String> handled)
    {
        Map<String, List<String>> otherProperties = new LinkedHashMap<>();

        for (String propertyIRI : node.getProperties().keySet())
        {
            if (! handled.contains(propertyIRI))
            {
                List<String> values = getTexts(node, null, propertyIRI);

                if (values != null)
                {
                    otherProperties.put(propertyIRI, values);
                }
            }
        }

        return otherProperties.isEmpty() ? null : otherProperties;
    }


    /**
     * Return the node that a value refers to.
     *
     * @param value value of a property
     * @return node or null if the value is a literal or the node is not described in the document
     */
    private JsonLdNode getReferencedNode(JsonLdValue value)
    {
        return value.isReference() ? graph.getNode(value.getNodeId()) : null;
    }


    /**
     * Return the human-readable label of a node.
     *
     * @param node node from the graph
     * @return label or null
     */
    private String getLabel(JsonLdNode node)
    {
        return getLocalizedText(node, null, LABEL_PROPERTIES);
    }


    /**
     * Return a value as a string.  A literal is returned as is, a reference to a node with an IRI is returned as the
     * IRI, and a reference to a blank node is returned as the label of the blank node.
     *
     * @param value value of a property
     * @return string or null
     */
    private String getText(JsonLdValue value)
    {
        if (! value.isReference())
        {
            return value.getValue();
        }

        if (! value.getNodeId().startsWith(JsonLdNode.BLANK_NODE_PREFIX))
        {
            return value.getNodeId();
        }

        JsonLdNode node = graph.getNode(value.getNodeId());

        return (node == null) ? null : getLabel(node);
    }


    /**
     * Return the first value of the first of the listed properties that has one.
     *
     * @param node node from the graph
     * @param handled the properties of the node that have been mapped to the bean (may be null)
     * @param propertyIRIs properties to look in, in order of preference
     * @return string or null
     */
    private String getFirstText(JsonLdNode  node,
                                Set<String> handled,
                                String...   propertyIRIs)
    {
        String result = null;

        for (String propertyIRI : propertyIRIs)
        {
            if (handled != null)
            {
                handled.add(propertyIRI);
            }

            if (result == null)
            {
                for (JsonLdValue value : node.getValues(propertyIRI))
                {
                    String text = getText(value);

                    if ((text != null) && (! text.isBlank()))
                    {
                        result = text;
                        break;
                    }
                }
            }
        }

        return result;
    }


    /**
     * Return all the values of a property.
     *
     * @param node node from the graph
     * @param handled the properties of the node that have been mapped to the bean (may be null)
     * @param propertyIRI property to look in
     * @return list of strings or null if there are none
     */
    private List<String> getTexts(JsonLdNode  node,
                                  Set<String> handled,
                                  String      propertyIRI)
    {
        List<String> results = new ArrayList<>();

        if (handled != null)
        {
            handled.add(propertyIRI);
        }

        for (JsonLdValue value : node.getValues(propertyIRI))
        {
            String text = getText(value);

            if ((text != null) && (! text.isBlank()) && (! results.contains(text)))
            {
                results.add(text);
            }
        }

        return results.isEmpty() ? null : results;
    }


    /**
     * Return the identifiers of the nodes that a property refers to.  Unlike getTexts, a reference to a blank node is
     * returned as the blank node's identifier so that it can be matched to the resource described by that node.
     *
     * @param node node from the graph
     * @param handled the properties of the node that have been mapped to the bean (may be null)
     * @param propertyIRI property to look in
     * @return list of node identifiers or null if there are none
     */
    private List<String> getReferences(JsonLdNode  node,
                                       Set<String> handled,
                                       String      propertyIRI)
    {
        List<String> results = new ArrayList<>();

        if (handled != null)
        {
            handled.add(propertyIRI);
        }

        for (JsonLdValue value : node.getValues(propertyIRI))
        {
            String reference = value.isReference() ? value.getNodeId() : value.getValue();

            if ((reference != null) && (! reference.isBlank()) && (! results.contains(reference)))
            {
                results.add(reference);
            }
        }

        return results.isEmpty() ? null : results;
    }


    /**
     * Return the value of a property that may be supplied in many languages.  The value with no language is preferred,
     * then English, then whichever value comes first.
     *
     * @param node node from the graph
     * @param handled the properties of the node that have been mapped to the bean (may be null)
     * @param propertyIRI property to look in
     * @param translations map to add the values in other languages to
     * @return string or null
     */
    private String getLocalizedText(JsonLdNode          node,
                                    Set<String>         handled,
                                    String              propertyIRI,
                                    Map<String, String> translations)
    {
        if (handled != null)
        {
            handled.add(propertyIRI);
        }

        JsonLdValue preferred = null;

        for (JsonLdValue value : node.getValues(propertyIRI))
        {
            if ((! value.isReference()) && (value.getValue() != null) && (! value.getValue().isBlank()))
            {
                if (preferred == null)
                {
                    preferred = value;
                }
                else if ((preferred.getLanguage() != null) &&
                         ((value.getLanguage() == null) ||
                          ((value.getLanguage().toLowerCase().startsWith("en")) && (! preferred.getLanguage().toLowerCase().startsWith("en")))))
                {
                    preferred = value;
                }
            }
        }

        if (preferred == null)
        {
            return null;
        }

        for (JsonLdValue value : node.getValues(propertyIRI))
        {
            if ((value != preferred) && (! value.isReference()) && (value.getValue() != null) && (value.getLanguage() != null))
            {
                translations.putIfAbsent(value.getLanguage(), value.getValue());
            }
        }

        return preferred.getValue();
    }


    /**
     * Return the first value found in a list of properties, preferring the value with no language, then English.
     *
     * @param node node from the graph
     * @param handled the properties of the node that have been mapped to the bean (may be null)
     * @param propertyIRIs properties to look in, in order of preference
     * @return string or null
     */
    private String getLocalizedText(JsonLdNode  node,
                                    Set<String> handled,
                                    String[]    propertyIRIs)
    {
        for (String propertyIRI : propertyIRIs)
        {
            String text = getLocalizedText(node, handled, propertyIRI, new LinkedHashMap<>());

            if (text != null)
            {
                return text;
            }
        }

        return null;
    }


    /**
     * Remove a scheme prefix such as "mailto:" from a value.
     *
     * @param value value (may be null)
     * @param prefix prefix to remove
     * @return value without the prefix
     */
    private String stripPrefix(String value,
                               String prefix)
    {
        if ((value != null) && (value.startsWith(prefix)))
        {
            return value.substring(prefix.length());
        }

        return value;
    }


    /**
     * Convert a string to a long.
     *
     * @param value string value (may be null)
     * @return long or null if the value is not a number
     */
    private Long getLong(String value)
    {
        if (value != null)
        {
            try
            {
                return (long) Double.parseDouble(value.trim());
            }
            catch (NumberFormatException notANumber)
            {
                return null;
            }
        }

        return null;
    }


    /**
     * Return whether a string looks like an absolute IRI.
     *
     * @param value string to test
     * @return boolean
     */
    static boolean isIRI(String value)
    {
        return (value != null) &&
               (! value.isBlank()) &&
               (value.matches("^[A-Za-z][A-Za-z0-9+.-]*:\\S+$")) &&
               ((value.contains("://")) || (value.startsWith("urn:")) || (value.startsWith("mailto:")) || (value.startsWith("tel:")));
    }
}
