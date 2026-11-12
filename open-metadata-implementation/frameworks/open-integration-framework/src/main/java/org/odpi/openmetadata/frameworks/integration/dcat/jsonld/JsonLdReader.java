/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.dcat.jsonld;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.frameworks.integration.dcat.DcatVocabulary;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * JsonLdReader reads a JSON-LD document into a graph of nodes whose types and properties are identified by full IRIs.
 * It covers the parts of JSON-LD that are used to describe data catalogs: embedded contexts (prefixes, term definitions,
 * keyword aliases, default vocabulary, base IRI and default language), nested node objects, value objects, language
 * maps, lists, sets, graphs and reverse properties.
 * <p>
 * The reader is deliberately lenient and never retrieves anything from the network.  If a document refers to a context
 * by its location, the reader carries on using the standard prefixes and term names of the DCAT vocabulary, and the
 * common "id" and "type" aliases, so that documents that rely on a published DCAT context can still be understood.
 */
public class JsonLdReader
{
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String KEYWORD_CONTEXT  = "@context";
    private static final String KEYWORD_ID       = "@id";
    private static final String KEYWORD_TYPE     = "@type";
    private static final String KEYWORD_VALUE    = "@value";
    private static final String KEYWORD_LANGUAGE = "@language";
    private static final String KEYWORD_GRAPH    = "@graph";
    private static final String KEYWORD_INCLUDED = "@included";
    private static final String KEYWORD_LIST     = "@list";
    private static final String KEYWORD_SET      = "@set";
    private static final String KEYWORD_REVERSE  = "@reverse";
    private static final String KEYWORD_VOCAB    = "@vocab";
    private static final String KEYWORD_BASE     = "@base";
    private static final String KEYWORD_CONTAINER = "@container";
    private static final String KEYWORD_NONE     = "@none";

    private static final int MAX_TERM_DEPTH = 10;

    private final JsonLdGraph graph;


    /**
     * The definition of a term from a context.
     */
    private static class TermDefinition
    {
        private String      id           = null;
        private String      type         = null;
        private String      language     = null;
        private boolean     hasLanguage  = false;
        private boolean     reverse      = false;
        private JsonNode    localContext = null;
        private final Set<String> containers = new HashSet<>();
    }


    /**
     * The context that is active at a point in the document.
     */
    private static class Context
    {
        private String  vocab    = null;
        private String  base     = null;
        private String  language = null;
        private boolean remote   = false;
        private final Map<String, TermDefinition> terms = new HashMap<>();


        /**
         * Return a copy of the context that can be extended without affecting this one.
         *
         * @return new context
         */
        private Context copy()
        {
            Context copy = new Context();

            copy.vocab    = vocab;
            copy.base     = base;
            copy.language = language;
            copy.remote   = remote;
            copy.terms.putAll(terms);

            return copy;
        }
    }


    /**
     * Create a reader that adds to a graph.
     *
     * @param graph graph to fill
     */
    private JsonLdReader(JsonLdGraph graph)
    {
        this.graph = graph;
    }


    /**
     * Read a JSON-LD document.
     *
     * @param document JSON-LD document
     * @return graph of the nodes described in the document
     * @throws IOException the document is not valid JSON or is not a JSON object or array
     */
    public static JsonLdGraph read(String document) throws IOException
    {
        JsonNode root = OBJECT_MAPPER.readTree(document);

        if ((root == null) || ((! root.isObject()) && (! root.isArray())))
        {
            throw new IOException("A JSON-LD document must be a JSON object or array");
        }

        JsonLdGraph graph = new JsonLdGraph();

        new JsonLdReader(graph).expandNodes(new Context(), root);

        return graph;
    }


    /**
     * Expand a node object, or an array of node objects.
     *
     * @param activeContext context in force
     * @param json object or array
     */
    private void expandNodes(Context  activeContext,
                             JsonNode json)
    {
        if (json.isArray())
        {
            for (JsonNode element : json)
            {
                expandNodes(activeContext, element);
            }
        }
        else if (json.isObject())
        {
            expandNode(activeContext, json);
        }
    }


    /**
     * Expand a node object, adding its types and properties to the graph.
     *
     * @param activeContext context in force
     * @param nodeObject JSON object describing the node
     * @return identifier of the node
     */
    private String expandNode(Context  activeContext,
                              JsonNode nodeObject)
    {
        Context context = activeContext;

        if (nodeObject.has(KEYWORD_CONTEXT))
        {
            context = processContext(activeContext, nodeObject.get(KEYWORD_CONTEXT));
        }

        List<String>   keys         = new ArrayList<>();
        List<String>   expandedKeys = new ArrayList<>();
        List<JsonNode> values       = new ArrayList<>();
        String         id           = null;

        for (Map.Entry<String, JsonNode> field : nodeObject.properties())
        {
            String expandedKey = expandKey(context, field.getKey());

            keys.add(field.getKey());
            expandedKeys.add(expandedKey);
            values.add(field.getValue());

            if ((KEYWORD_ID.equals(expandedKey)) && (field.getValue().isTextual()))
            {
                id = expandIRI(context, field.getValue().asText(), false, true, 0);
            }
        }

        if ((id == null) || (id.isBlank()))
        {
            id = graph.newBlankNodeId();
        }

        JsonLdNode node = graph.getOrCreateNode(id);

        for (int i = 0; i < keys.size(); i++)
        {
            String   expandedKey = expandedKeys.get(i);
            JsonNode value       = values.get(i);

            if ((expandedKey == null) || (KEYWORD_ID.equals(expandedKey)) || (KEYWORD_CONTEXT.equals(expandedKey)))
            {
                continue;
            }

            if (KEYWORD_TYPE.equals(expandedKey))
            {
                for (JsonNode typeNode : asList(value))
                {
                    if (typeNode.isTextual())
                    {
                        String typeIRI = expandIRI(context, typeNode.asText(), true, false, 0);

                        if (typeIRI != null)
                        {
                            node.getTypes().add(typeIRI);
                        }
                    }
                }
            }
            else if ((KEYWORD_GRAPH.equals(expandedKey)) || (KEYWORD_INCLUDED.equals(expandedKey)))
            {
                expandNodes(context, value);
            }
            else if (KEYWORD_REVERSE.equals(expandedKey))
            {
                if (value.isObject())
                {
                    for (Map.Entry<String, JsonNode> reverseField : value.properties())
                    {
                        String propertyIRI = expandIRI(context, reverseField.getKey(), true, false, 0);

                        if ((propertyIRI != null) && (propertyIRI.contains(":")))
                        {
                            for (JsonNode subject : asList(reverseField.getValue()))
                            {
                                String subjectId = null;

                                if (subject.isObject())
                                {
                                    subjectId = expandNode(context, subject);
                                }
                                else if (subject.isTextual())
                                {
                                    subjectId = expandIRI(context, subject.asText(), false, true, 0);
                                }

                                if (subjectId != null)
                                {
                                    graph.getOrCreateNode(subjectId).addValue(propertyIRI, JsonLdValue.reference(id));
                                }
                            }
                        }
                    }
                }
            }
            else if ((! expandedKey.startsWith("@")) && (expandedKey.contains(":")))
            {
                TermDefinition termDefinition = context.terms.get(keys.get(i));
                Context        valueContext   = context;

                if ((termDefinition != null) && (termDefinition.localContext != null))
                {
                    valueContext = processContext(context, termDefinition.localContext);
                }

                expandValue(valueContext, termDefinition, value, node, expandedKey);
            }
        }

        return id;
    }


    /**
     * Expand the value of a property and add it to the node.
     *
     * @param context context in force
     * @param termDefinition definition of the term used for the property (may be null)
     * @param value JSON value
     * @param node node that the property belongs to
     * @param propertyIRI IRI of the property
     */
    private void expandValue(Context        context,
                             TermDefinition termDefinition,
                             JsonNode       value,
                             JsonLdNode     node,
                             String         propertyIRI)
    {
        if ((value == null) || (value.isNull()))
        {
            return;
        }

        if (value.isArray())
        {
            for (JsonNode element : value)
            {
                expandValue(context, termDefinition, element, node, propertyIRI);
            }

            return;
        }

        if (value.isObject())
        {
            if ((termDefinition != null) && (termDefinition.containers.contains(KEYWORD_LANGUAGE)))
            {
                for (Map.Entry<String, JsonNode> languageEntry : value.properties())
                {
                    String language = KEYWORD_NONE.equals(languageEntry.getKey()) ? null : languageEntry.getKey();

                    for (JsonNode text : asList(languageEntry.getValue()))
                    {
                        if (text.isTextual())
                        {
                            node.addValue(propertyIRI, JsonLdValue.literal(text.asText(), language, null));
                        }
                    }
                }

                return;
            }

            JsonNode valueNode     = null;
            JsonNode containerNode = null;
            String   language      = null;
            String   rawType       = null;
            boolean  isValueObject = false;

            for (Map.Entry<String, JsonNode> field : value.properties())
            {
                String expandedKey = expandKey(context, field.getKey());

                if (KEYWORD_VALUE.equals(expandedKey))
                {
                    valueNode     = field.getValue();
                    isValueObject = true;
                }
                else if ((KEYWORD_LANGUAGE.equals(expandedKey)) && (field.getValue().isTextual()))
                {
                    language = field.getValue().asText();
                }
                else if ((KEYWORD_TYPE.equals(expandedKey)) && (field.getValue().isTextual()))
                {
                    rawType = field.getValue().asText();
                }
                else if ((KEYWORD_LIST.equals(expandedKey)) || (KEYWORD_SET.equals(expandedKey)))
                {
                    containerNode = field.getValue();
                }
            }

            if (isValueObject)
            {
                if ((valueNode != null) && (! valueNode.isNull()) && (valueNode.isValueNode()))
                {
                    String datatype = (rawType == null) ? null : expandIRI(context, rawType, true, false, 0);

                    node.addValue(propertyIRI, JsonLdValue.literal(valueNode.asText(), language, datatype));
                }
            }
            else if (containerNode != null)
            {
                expandValue(context, termDefinition, containerNode, node, propertyIRI);
            }
            else
            {
                addReference(termDefinition, node, propertyIRI, expandNode(context, value));
            }

            return;
        }

        if (value.isTextual())
        {
            String type = (termDefinition == null) ? null : termDefinition.type;

            if (KEYWORD_ID.equals(type))
            {
                addReference(termDefinition, node, propertyIRI, expandIRI(context, value.asText(), false, true, 0));
            }
            else if (KEYWORD_VOCAB.equals(type))
            {
                String iri = expandIRI(context, value.asText(), true, true, 0);

                addReference(termDefinition, node, propertyIRI, (iri == null) ? value.asText() : iri);
            }
            else
            {
                String datatype = ((type != null) && (! type.startsWith("@"))) ? expandIRI(context, type, true, false, 0) : null;
                String language = null;

                if (datatype == null)
                {
                    language = ((termDefinition != null) && (termDefinition.hasLanguage)) ? termDefinition.language : context.language;
                }

                node.addValue(propertyIRI, JsonLdValue.literal(value.asText(), language, datatype));
            }
        }
        else if (value.isValueNode())
        {
            node.addValue(propertyIRI, JsonLdValue.literal(value.asText(), null, null));
        }
    }


    /**
     * Add a reference from one node to another, taking account of reverse properties.
     *
     * @param termDefinition definition of the term used for the property (may be null)
     * @param node node that the property was found in
     * @param propertyIRI IRI of the property
     * @param referencedNodeId identifier of the node that was referenced
     */
    private void addReference(TermDefinition termDefinition,
                              JsonLdNode     node,
                              String         propertyIRI,
                              String         referencedNodeId)
    {
        if ((referencedNodeId == null) || (referencedNodeId.isBlank()))
        {
            return;
        }

        if ((termDefinition != null) && (termDefinition.reverse))
        {
            graph.getOrCreateNode(referencedNodeId).addValue(propertyIRI, JsonLdValue.reference(node.getId()));
        }
        else
        {
            node.addValue(propertyIRI, JsonLdValue.reference(referencedNodeId));
        }
    }


    /**
     * Apply a local context to the active context.
     *
     * @param activeContext context in force
     * @param localContext value of an @context property
     * @return new active context
     */
    private Context processContext(Context  activeContext,
                                   JsonNode localContext)
    {
        if ((localContext == null) || (localContext.isNull()))
        {
            return new Context();
        }

        Context result = activeContext.copy();

        if (localContext.isTextual())
        {
            /*
             * Contexts are never retrieved from the network.
             */
            graph.addUnresolvedContext(localContext.asText());
            result.remote = true;
        }
        else if (localContext.isArray())
        {
            for (JsonNode element : localContext)
            {
                result = processContext(result, element);
            }
        }
        else if (localContext.isObject())
        {
            for (Map.Entry<String, JsonNode> field : localContext.properties())
            {
                String   key   = field.getKey();
                JsonNode value = field.getValue();

                if (KEYWORD_VOCAB.equals(key))
                {
                    result.vocab = value.isTextual() ? expandIRI(result, value.asText(), false, false, 0) : null;
                }
                else if (KEYWORD_BASE.equals(key))
                {
                    result.base = value.isTextual() ? value.asText() : null;
                }
                else if (KEYWORD_LANGUAGE.equals(key))
                {
                    result.language = value.isTextual() ? value.asText() : null;
                }
                else if (! key.startsWith("@"))
                {
                    result.terms.put(key, getTermDefinition(value));
                }
            }
        }

        return result;
    }


    /**
     * Convert the definition of a term from a context into a TermDefinition.
     *
     * @param value value of the term in the context
     * @return term definition or null if the term is explicitly undefined
     */
    private TermDefinition getTermDefinition(JsonNode value)
    {
        if ((value == null) || (value.isNull()))
        {
            return null;
        }

        TermDefinition termDefinition = new TermDefinition();

        if (value.isTextual())
        {
            termDefinition.id = value.asText();
        }
        else if (value.isObject())
        {
            if ((value.has(KEYWORD_REVERSE)) && (value.get(KEYWORD_REVERSE).isTextual()))
            {
                termDefinition.id      = value.get(KEYWORD_REVERSE).asText();
                termDefinition.reverse = true;
            }
            else if ((value.has(KEYWORD_ID)) && (value.get(KEYWORD_ID).isTextual()))
            {
                termDefinition.id = value.get(KEYWORD_ID).asText();
            }

            if ((value.has(KEYWORD_TYPE)) && (value.get(KEYWORD_TYPE).isTextual()))
            {
                termDefinition.type = value.get(KEYWORD_TYPE).asText();
            }

            if (value.has(KEYWORD_LANGUAGE))
            {
                termDefinition.hasLanguage = true;
                termDefinition.language    = value.get(KEYWORD_LANGUAGE).isTextual() ? value.get(KEYWORD_LANGUAGE).asText() : null;
            }

            if (value.has(KEYWORD_CONTAINER))
            {
                for (JsonNode container : asList(value.get(KEYWORD_CONTAINER)))
                {
                    if (container.isTextual())
                    {
                        termDefinition.containers.add(container.asText());
                    }
                }
            }

            if (value.has(KEYWORD_CONTEXT))
            {
                termDefinition.localContext = value.get(KEYWORD_CONTEXT);
            }
        }

        return termDefinition;
    }


    /**
     * Expand the key of a JSON object into a keyword or the IRI of a property.
     *
     * @param context context in force
     * @param key key from the document
     * @return keyword, IRI or null if the key has no meaning
     */
    private String expandKey(Context context,
                             String  key)
    {
        String expandedKey = expandIRI(context, key, true, false, 0);

        /*
         * These aliases are defined by most published DCAT contexts.  They are assumed when the document's context could
         * not be read and the document has not defined the term itself.
         */
        if ((context.remote) && (! context.terms.containsKey(key)))
        {
            if ("id".equals(key))
            {
                return KEYWORD_ID;
            }
            if ("type".equals(key))
            {
                return KEYWORD_TYPE;
            }
        }

        return expandedKey;
    }


    /**
     * Expand a term, compact IRI or relative IRI into a full IRI.
     *
     * @param context context in force
     * @param value value to expand
     * @param vocabularyRelative is the value allowed to be a term or be relative to the default vocabulary?
     * @param documentRelative is the value allowed to be relative to the base IRI of the document?
     * @param depth number of term definitions followed to get here
     * @return keyword, full IRI, blank node identifier or null if the value has no meaning
     */
    private String expandIRI(Context context,
                             String  value,
                             boolean vocabularyRelative,
                             boolean documentRelative,
                             int     depth)
    {
        if (value == null)
        {
            return null;
        }

        if ((value.startsWith("@")) || (depth > MAX_TERM_DEPTH))
        {
            return value;
        }

        if ((vocabularyRelative) && (context.terms.containsKey(value)))
        {
            TermDefinition termDefinition = context.terms.get(value);

            if (termDefinition == null)
            {
                return null;
            }

            if ((termDefinition.id != null) && (! termDefinition.id.equals(value)))
            {
                return expandIRI(context, termDefinition.id, true, false, depth + 1);
            }
        }

        int colon = value.indexOf(':');

        if (colon > 0)
        {
            String prefix = value.substring(0, colon);
            String suffix = value.substring(colon + 1);

            if (("_".equals(prefix)) || (suffix.startsWith("//")))
            {
                return value;
            }

            TermDefinition prefixDefinition = context.terms.get(prefix);

            if ((prefixDefinition != null) && (prefixDefinition.id != null))
            {
                String namespace = expandIRI(context, prefixDefinition.id, true, false, depth + 1);

                if (namespace != null)
                {
                    return namespace + suffix;
                }
            }

            String standardNamespace = DcatVocabulary.getStandardNamespace(prefix);

            if (standardNamespace != null)
            {
                return standardNamespace + suffix;
            }

            return value;
        }

        if (vocabularyRelative)
        {
            if (context.vocab != null)
            {
                return context.vocab + value;
            }

            String plainTerm = DcatVocabulary.getPlainTerms().get(value);

            if (plainTerm != null)
            {
                return plainTerm;
            }

            if ((context.remote) && (! graph.getUnresolvedContexts().isEmpty()))
            {
                return graph.getUnresolvedContexts().iterator().next() + "#" + value;
            }

            return null;
        }

        if ((documentRelative) && (context.base != null))
        {
            try
            {
                return URI.create(context.base).resolve(value).toString();
            }
            catch (IllegalArgumentException invalidIRI)
            {
                return value;
            }
        }

        return value;
    }


    /**
     * Treat a JSON value as a list: an array is returned as its elements and anything else as a list of one.
     *
     * @param value JSON value
     * @return list of values
     */
    private List<JsonNode> asList(JsonNode value)
    {
        List<JsonNode> results = new ArrayList<>();

        if (value != null)
        {
            if (value.isArray())
            {
                for (JsonNode element : value)
                {
                    results.add(element);
                }
            }
            else if (! value.isNull())
            {
                results.add(value);
            }
        }

        return results;
    }
}
