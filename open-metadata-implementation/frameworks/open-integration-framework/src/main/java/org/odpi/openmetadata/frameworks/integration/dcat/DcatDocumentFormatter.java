/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.dcat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.odpi.openmetadata.frameworks.integration.dcat.jsonld.JsonLdReader;
import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatDocument;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * DcatDocumentFormatter reads and writes DCAT documents.  The documents are in JSON-LD format, which is the
 * serialization of DCAT that is also valid JSON.  Any arrangement of a JSON-LD document can be read (nested, flattened,
 * with prefixes or with full IRIs).  Documents written by this class embed their context and nest the description of a
 * dataset's distributions, publisher and contact points inside the dataset.  The class is stateless and its methods are
 * thread-safe.  Parsing problems are reported as IOException so that callers do not need the Jackson libraries on
 * their classpath.
 */
public class DcatDocumentFormatter
{
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private static final ObjectMapper FRAGMENT_MAPPER = new ObjectMapper();


    /**
     * Private constructor since all methods are static.
     */
    private DcatDocumentFormatter()
    {
    }


    /**
     * Parse a DCAT document.
     *
     * @param rawDocument document in JSON-LD format
     * @return bean version of the document
     * @throws IOException the document is not valid JSON-LD
     */
    public static DcatDocument parseDocument(String rawDocument) throws IOException
    {
        if (rawDocument == null)
        {
            throw new IOException("No document supplied");
        }

        try
        {
            return new DcatGraphReader(JsonLdReader.read(rawDocument)).read();
        }
        catch (IOException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            throw new IOException(error.getClass().getName() + " parsing the document: " + error.getMessage(), error);
        }
    }


    /**
     * Return whether a document describes at least one catalog, dataset or data service.
     *
     * @param document bean version of the document
     * @return boolean
     */
    public static boolean hasContent(DcatDocument document)
    {
        return (document != null) &&
               ((isNotEmpty(document.getCatalogs())) || (isNotEmpty(document.getDatasets())) || (isNotEmpty(document.getDataServices())));
    }


    /**
     * Return whether a list has content.
     *
     * @param list list to test
     * @return boolean
     */
    private static boolean isNotEmpty(List<?> list)
    {
        return (list != null) && (! list.isEmpty());
    }


    /**
     * Convert a DCAT document into the structure of a JSON-LD document.  This structure can be embedded in another
     * JSON document, such as the response to a REST API call.
     *
     * @param document bean version of the document
     * @return map that serializes to the JSON-LD document
     */
    public static Map<String, Object> toJSONLDStructure(DcatDocument document)
    {
        if (document == null)
        {
            return null;
        }

        return new DcatGraphWriter().write(document);
    }


    /**
     * Write a DCAT document as JSON-LD.
     *
     * @param document bean version of the document
     * @return JSON-LD string
     * @throws IOException problem serializing the document
     */
    public static String toJSONLD(DcatDocument document) throws IOException
    {
        return OBJECT_MAPPER.writeValueAsString(toJSONLDStructure(document));
    }


    /**
     * Write any DCAT bean (or list of beans) as plain JSON.  This is used to keep fragments of a document, such as the
     * details of its publisher, losslessly in the additional properties of an open metadata element.
     *
     * @param bean bean or list of beans
     * @return JSON string
     * @throws IOException problem serializing the bean
     */
    public static String toJSONFragment(Object bean) throws IOException
    {
        return FRAGMENT_MAPPER.writeValueAsString(bean);
    }


    /**
     * Read a single bean from a JSON fragment written by toJSONFragment.
     *
     * @param json JSON string (may be null)
     * @param beanClass class of the bean
     * @param <T> bean type
     * @return bean or null if the fragment is absent
     * @throws IOException problem parsing the JSON
     */
    public static <T> T fromJSONFragment(String   json,
                                         Class<T> beanClass) throws IOException
    {
        if ((json == null) || (json.isBlank()))
        {
            return null;
        }

        return OBJECT_MAPPER.readValue(json, beanClass);
    }


    /**
     * Read a list of beans from a JSON fragment written by toJSONFragment.
     *
     * @param json JSON string (may be null)
     * @param beanClass class of the list elements
     * @param <T> bean type
     * @return list of beans or null if the fragment is absent
     * @throws IOException problem parsing the JSON
     */
    public static <T> List<T> fromJSONFragmentList(String   json,
                                                   Class<T> beanClass) throws IOException
    {
        if ((json == null) || (json.isBlank()))
        {
            return null;
        }

        return OBJECT_MAPPER.readValue(json, OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, beanClass));
    }


    /**
     * Read a map of lists of strings from a JSON fragment written by toJSONFragment.
     *
     * @param json JSON string (may be null)
     * @return map or null if the fragment is absent
     * @throws IOException problem parsing the JSON
     */
    public static Map<String, List<String>> fromJSONFragmentMapOfLists(String json) throws IOException
    {
        if ((json == null) || (json.isBlank()))
        {
            return null;
        }

        return OBJECT_MAPPER.readValue(json,
                                       OBJECT_MAPPER.getTypeFactory().constructMapType(java.util.LinkedHashMap.class,
                                                                                       OBJECT_MAPPER.getTypeFactory().constructType(String.class),
                                                                                       OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, String.class)));
    }


    /**
     * Read a map of strings from a JSON fragment written by toJSONFragment.
     *
     * @param json JSON string (may be null)
     * @return map or null if the fragment is absent
     * @throws IOException problem parsing the JSON
     */
    public static Map<String, String> fromJSONFragmentMap(String json) throws IOException
    {
        if ((json == null) || (json.isBlank()))
        {
            return null;
        }

        return OBJECT_MAPPER.readValue(json,
                                       OBJECT_MAPPER.getTypeFactory().constructMapType(java.util.LinkedHashMap.class, String.class, String.class));
    }
}
