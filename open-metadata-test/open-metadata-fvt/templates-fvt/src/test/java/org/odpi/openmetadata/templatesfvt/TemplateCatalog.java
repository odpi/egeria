/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.templatesfvt;

import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.search.ClassificationCondition;
import org.odpi.openmetadata.frameworks.openmetadata.search.MatchCriteria;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyComparisonOperator;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyCondition;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchClassifications;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TemplateCatalog finds the templates shipped in the content packs and reads each one's placeholder
 * specification, so that the tests can use a template the way a caller is expected to.
 * <br>
 * A template is an ordinary element carrying the <b>Template</b> classification.  What makes it usable is the
 * specification attached to it: each {@code SpecificationPropertyAssignment} relationship whose
 * {@code propertyName} is {@code placeholderProperty} points at a valid value describing one placeholder the
 * caller must supply.  The placeholder's variable name is that valid value's {@code preferredValue}, and its
 * {@code additionalProperties} carry an {@code example} and whether it is {@code required}.
 * <br>
 * Placeholders appear in the template's own property values as <code>~{variableName}~</code>, and the point of
 * the exercise is that none of those markers should survive into the element created from the template.
 */
final class TemplateCatalog
{
    /**
     * The value of a {@code SpecificationPropertyAssignment}'s {@code propertyName} that marks the valid value
     * at the far end as describing a placeholder.
     */
    static final String PLACEHOLDER_PROPERTY_TYPE = "placeholderProperty";

    /** How a placeholder appears inside a template's property values. */
    static final String PLACEHOLDER_PREFIX = "~{";
    static final String PLACEHOLDER_SUFFIX = "}~";

    /** Page size for the template search - the server refuses a page larger than its configured maximum. */
    private static final int PAGE_SIZE = 100;


    private TemplateCatalog()
    {
        // no instances
    }


    /**
     * One template found in the content packs, with everything the tests need to use it.
     *
     * @param templateGUID unique identifier of the template element
     * @param templateName the template's name, for the test case label
     * @param typeName the type of element the template creates
     * @param placeholders the placeholders the caller must supply, in specification order
     */
    record Template(String templateGUID,
                    String templateName,
                    String typeName,
                    List<Placeholder> placeholders,
                    boolean placeholdersWereDeclared)
    {
        /**
         * A short label for the test case name - the full record would be unreadable in a test report.
         *
         * @return template name and type
         */
        @Override
        public String toString()
        {
            return templateName + " (" + typeName + ")";
        }
    }


    /**
     * One placeholder a template declares.
     *
     * @param name the variable name, as it appears between the markers in the template
     * @param example an example value from the specification, if it gave one
     * @param required whether the specification says the caller must supply it
     */
    record Placeholder(String name, String example, boolean required)
    {
        /**
         * Return a value to supply for this placeholder.
         * <br>
         * The specification's own example is used as the basis, because it is the value the template's author
         * expected and keeps the shape the template implies - a URL, a port number, a path.  A marker naming this
         * suite is appended to it, for two reasons.  It makes a value that leaks somewhere unexpected traceable
         * back to the run that produced it; and, more importantly, it keeps the value out of the way of content
         * the archives already ship.  Several templates document an example that names real shipped content (the
         * clinical trial templates give "PROJ-CT-TBDF", the identifier of the Teddy Bear Drop Foot trial that the
         * Coco archives also catalogue in full).  Using the example verbatim builds the qualified name of that
         * shipped element, so the create either fails as a duplicate or silently matches the shipped element -
         * and the test then goes on to purge content it never created.
         * <br>
         * Where there is no example a recognisable generated value is used instead.
         *
         * @param templateMarker marker unique to the template being used - see {@link #templateMarker}
         * @return value to substitute
         */
        String testValue(String templateMarker)
        {
            if ((example != null) && (! example.isBlank()))
            {
                return example + "-" + templateMarker;
            }

            return templateMarker + "-" + name;
        }
    }


    /**
     * Find every template in the repository, with its placeholder specification.
     *
     * @param connectorContext live connector context
     * @param maxTemplates maximum number to return
     * @return templates, in name order so that test reports are stable between runs
     * @throws Exception problem reading from the repository
     */
    static List<Template> findTemplates(ConnectorContextBase connectorContext,
                                        int                  maxTemplates) throws Exception
    {
        OpenMetadataStore openMetadataStore = connectorContext.getOpenMetadataStore();

        /*
         * Any element can be a template, so the search is by classification rather than by type.  There is no
         * "everything carrying this classification" call - every by-classification method wants a property
         * value to match - so the search is built here with a NOT_NULL condition on the classification's
         * qualifiedName, which every Template carries.  That selects on the presence of the classification
         * rather than on any particular value in it.
         */
        QueryOptions queryOptions = new QueryOptions();

        queryOptions.setMetadataElementTypeName(OpenMetadataType.OPEN_METADATA_ROOT.typeName);
        queryOptions.setPageSize(PAGE_SIZE);

        PropertyCondition anyTemplate = new PropertyCondition();

        anyTemplate.setProperty(OpenMetadataProperty.DISPLAY_NAME.name);
        anyTemplate.setOperator(PropertyComparisonOperator.NOT_NULL);

        SearchProperties classificationProperties = new SearchProperties();

        classificationProperties.setConditions(List.of(anyTemplate));
        classificationProperties.setMatchCriteria(MatchCriteria.ANY);

        ClassificationCondition templateCondition = new ClassificationCondition();

        templateCondition.setName(OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName);
        templateCondition.setSearchProperties(classificationProperties);

        SearchClassifications searchClassifications = new SearchClassifications();

        searchClassifications.setConditions(List.of(templateCondition));
        searchClassifications.setMatchCriteria(MatchCriteria.ALL);

        /*
         * Paged rather than fetched in one go - the server caps a single page well below the number of
         * templates the content packs ship, so asking for them all at once is refused.
         */
        List<Template> templates = new ArrayList<>();

        for (int startFrom = 0; startFrom < maxTemplates; startFrom += PAGE_SIZE)
        {
            queryOptions.setStartFrom(startFrom);

            List<OpenMetadataElement> templateElements = openMetadataStore.findMetadataElements(null,
                                                                                                 searchClassifications,
                                                                                                 queryOptions);

            if ((templateElements == null) || templateElements.isEmpty())
            {
                break;
            }

            for (OpenMetadataElement templateElement : templateElements)
            {
                List<Placeholder> declared = getPlaceholders(openMetadataStore, templateElement.getElementGUID());

                /*
                 * Where a template declares no specification, the placeholders it actually uses are read out
                 * of its own property values instead.  That keeps the template usable by this suite - the
                 * point of part one is to prove the substitution works - while
                 * ContentPackTemplateFVT.everyTemplateWithPlaceholdersDeclaresThem reports the missing
                 * specification as the separate problem it is.  A caller who follows the documented route and
                 * reads the specification would have no idea what to supply.
                 */
                boolean           wereDeclared = ! declared.isEmpty();
                List<Placeholder> placeholders = wereDeclared ? declared : discoverPlaceholders(templateElement);

                templates.add(new Template(templateElement.getElementGUID(),
                                            getTemplateName(templateElement),
                                            templateElement.getType().getTypeName(),
                                            placeholders,
                                            wereDeclared));
            }

            if (templateElements.size() < PAGE_SIZE)
            {
                break;
            }
        }

        templates.sort(Comparator.comparing(Template::templateName).thenComparing(Template::templateGUID));

        return templates;
    }


    /**
     * Read the placeholder specification attached to one template.
     *
     * @param openMetadataStore store to read through
     * @param templateGUID template to describe
     * @return the placeholders it declares, empty if it declares none
     * @throws Exception problem reading from the repository
     */
    static List<Placeholder> getPlaceholders(OpenMetadataStore openMetadataStore,
                                             String            templateGUID) throws Exception
    {
        List<Placeholder> placeholders = new ArrayList<>();

        RelatedMetadataElementList specifications =
                openMetadataStore.getRelatedMetadataElements(templateGUID,
                                                              0,
                                                              OpenMetadataType.SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP.typeName,
                                                              new QueryOptions());

        if ((specifications == null) || (specifications.getElementList() == null))
        {
            return placeholders;
        }

        for (RelatedMetadataElement specification : specifications.getElementList())
        {
            if (specification == null) continue;

            /*
             * The relationship says what kind of specification property this is.  A template's specification
             * also carries supported request types, configuration properties and the like - only the
             * placeholder ones describe a value the caller has to supply.
             */
            if (! PLACEHOLDER_PROPERTY_TYPE.equals(getStringProperty(specification.getRelationshipProperties(),
                                                                       OpenMetadataProperty.PROPERTY_NAME.name)))
            {
                continue;
            }

            OpenMetadataElement validValue = specification.getElement();

            if (validValue == null) continue;

            ElementProperties validValueProperties = validValue.getElementProperties();

            String name = getStringProperty(validValueProperties, OpenMetadataProperty.PREFERRED_VALUE.name);

            if (name == null)
            {
                name = getStringProperty(validValueProperties, OpenMetadataProperty.DISPLAY_NAME.name);
            }

            if (name == null) continue;

            Map<String, String> additionalProperties = getAdditionalProperties(validValueProperties);

            placeholders.add(new Placeholder(name,
                                              additionalProperties.get(OpenMetadataProperty.EXAMPLE.name),
                                              ! "false".equalsIgnoreCase(additionalProperties.get(OpenMetadataProperty.REQUIRED.name))));
        }

        return placeholders;
    }


    /**
     * Read the placeholder variable names out of a template's own property values.
     * <br>
     * Used only as a fallback for templates that ship no specification.  A marker looks like
     * <code>~{variableName}~</code>, and the same variable may appear in several properties.
     *
     * @param templateElement the template
     * @return the placeholders it uses, in the order they were met
     */
    private static List<Placeholder> discoverPlaceholders(OpenMetadataElement templateElement)
    {
        Map<String, Placeholder> discovered = new LinkedHashMap<>();

        if ((templateElement.getElementProperties() != null)
                    && (templateElement.getElementProperties().getPropertiesAsStrings() != null))
        {
            for (String value : templateElement.getElementProperties().getPropertiesAsStrings().values())
            {
                if (value == null) continue;

                int start = value.indexOf(PLACEHOLDER_PREFIX);

                while (start >= 0)
                {
                    int end = value.indexOf(PLACEHOLDER_SUFFIX, start);

                    if (end < 0) break;

                    String name = value.substring(start + PLACEHOLDER_PREFIX.length(), end);

                    discovered.putIfAbsent(name, new Placeholder(name, null, true));

                    start = value.indexOf(PLACEHOLDER_PREFIX, end);
                }
            }
        }

        return new ArrayList<>(discovered.values());
    }


    /**
     * Build the map of placeholder name to value that a caller passes when using this template.
     *
     * @param template template being used
     * @return placeholder values, or null if the template declares none
     */
    static Map<String, String> getPlaceholderValues(Template template)
    {
        if (template.placeholders().isEmpty())
        {
            return null;
        }

        Map<String, String> placeholderValues = new LinkedHashMap<>();

        for (Placeholder placeholder : template.placeholders())
        {
            placeholderValues.put(placeholder.name(), placeholder.testValue(templateMarker(template)));
        }

        return placeholderValues;
    }


    /**
     * Return a marker that is unique to this template, used to build the values supplied for its placeholders.
     * <br>
     * The values have to differ from one template to the next, not just from the archive content.  Templates are
     * not independent of each other: a template that has other templates as collection members brings copies of
     * them with it when it is used, and those copies are built from the same placeholder values.  The Coco
     * clinical trial supply chain is the example - using it creates the treatment validation and subject
     * onboarding supply chains too.  With one shared set of values, the later test case for one of those member
     * templates finds the copy the earlier case already made, so the create matches an existing element instead
     * of making one, no SourcedFrom relationship is written, and the case fails describing a problem that is
     * really an artefact of the run.  A per-template marker keeps each case creating its own element.
     *
     * @param template template being used
     * @return marker to embed in the placeholder values
     */
    private static String templateMarker(Template template)
    {
        String guid = template.templateGUID();

        return "templates-fvt-" + guid.substring(0, guid.indexOf('-'));
    }


    /**
     * Return a readable name for a template, for the test case label.
     *
     * @param templateElement the template
     * @return its qualified name, display name or GUID - whichever it has
     */
    private static String getTemplateName(OpenMetadataElement templateElement)
    {
        ElementProperties properties = templateElement.getElementProperties();

        String name = getStringProperty(properties, OpenMetadataProperty.DISPLAY_NAME.name);

        if (name == null)
        {
            name = getStringProperty(properties, OpenMetadataProperty.QUALIFIED_NAME.name);
        }

        return (name != null) ? name : templateElement.getElementGUID();
    }


    /**
     * Read one string property, without consuming it.
     *
     * @param properties properties to read
     * @param propertyName property to read
     * @return its value, or null
     */
    static String getStringProperty(ElementProperties properties,
                                    String            propertyName)
    {
        if (properties == null) return null;

        Map<String, String> asStrings = properties.getPropertiesAsStrings();

        return (asStrings == null) ? null : asStrings.get(propertyName);
    }


    /**
     * Read a valid value's additionalProperties, which is where the specification records the example value
     * and whether the placeholder is required.
     *
     * @param properties properties to read
     * @return the additional properties, never null
     */
    private static Map<String, String> getAdditionalProperties(ElementProperties properties)
    {
        Map<String, String> additionalProperties = new LinkedHashMap<>();

        String rendered = getStringProperty(properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name);

        if (rendered == null) return additionalProperties;

        /*
         * The map renders as "{key=value, key=value}".  Only the two keys this suite cares about are read, and
         * a value that cannot be parsed simply means the placeholder falls back to a generated value.
         */
        String body = rendered.replace("{", "").replace("}", "");

        for (String entry : body.split(", "))
        {
            int separator = entry.indexOf('=');

            if (separator > 0)
            {
                additionalProperties.put(entry.substring(0, separator).trim(), entry.substring(separator + 1).trim());
            }
        }

        return additionalProperties;
    }
}
