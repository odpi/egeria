/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolDocument;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolModule;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContract;
import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProduct;

import java.io.IOException;
import java.util.List;

/**
 * BitolDocumentFormatter converts Bitol documents between their serialized form (YAML or JSON) and the Egeria beans.
 * The Bitol standards use YAML as their primary encoding but JSON is also valid; since JSON is a subset of YAML, a
 * single YAML-based reader handles both.  Documents written by this class are YAML.  The class is stateless and its
 * methods are thread-safe.  Parsing problems are reported as IOException so that callers do not need the Jackson
 * libraries on their classpath.
 */
public class BitolDocumentFormatter
{
    private static final ObjectMapper YAML_MAPPER = newYAMLMapper();
    private static final ObjectMapper JSON_MAPPER = newJSONMapper();


    /**
     * Create a Jackson ObjectMapper that reads and writes Bitol documents in YAML.  It also reads JSON.  A new mapper
     * is returned each time so that the caller can configure it further.
     *
     * @return configured mapper
     */
    public static ObjectMapper newYAMLMapper()
    {
        return new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)).registerModule(new BitolModule());
    }


    /**
     * Create a Jackson ObjectMapper that reads and writes Bitol documents in JSON.  A new mapper is returned each time
     * so that the caller can configure it further.
     *
     * @return configured mapper
     */
    public static ObjectMapper newJSONMapper()
    {
        return new ObjectMapper().registerModule(new BitolModule());
    }


    /**
     * Private constructor since all methods are static.
     */
    private BitolDocumentFormatter()
    {
    }


    /**
     * Return the value of the "kind" property of a document without mapping the whole document to a bean.  This is
     * useful for routing a document whose full content cannot be parsed.
     *
     * @param rawDocument document in YAML or JSON format
     * @return the kind (typically DataContract or DataProduct) or null if the document has no kind property
     * @throws IOException the document is not valid YAML/JSON
     */
    public static String getKind(String rawDocument) throws IOException
    {
        JsonNode root = YAML_MAPPER.readTree(rawDocument);

        if (root != null)
        {
            JsonNode kind = root.get("kind");

            if ((kind != null) && (kind.isTextual()))
            {
                return kind.asText();
            }
        }

        return null;
    }


    /**
     * Parse a Bitol document of either kind.  The subclass is selected from the document's "kind" property.
     *
     * @param rawDocument document in YAML or JSON format
     * @return DataContract or DataProduct bean
     * @throws IOException the document is not valid or has an unrecognized kind
     */
    public static BitolDocument parseDocument(String rawDocument) throws IOException
    {
        return YAML_MAPPER.readValue(rawDocument, BitolDocument.class);
    }


    /**
     * Parse an Open Data Contract Standard (ODCS) data contract.
     *
     * @param rawDocument document in YAML or JSON format
     * @return DataContract bean
     * @throws IOException the document is not valid or is not a data contract
     */
    public static DataContract parseDataContract(String rawDocument) throws IOException
    {
        return YAML_MAPPER.readValue(rawDocument, DataContract.class);
    }


    /**
     * Parse an Open Data Product Standard (ODPS) data product.
     *
     * @param rawDocument document in YAML or JSON format
     * @return DataProduct bean
     * @throws IOException the document is not valid or is not a data product
     */
    public static DataProduct parseDataProduct(String rawDocument) throws IOException
    {
        return YAML_MAPPER.readValue(rawDocument, DataProduct.class);
    }


    /**
     * Write a Bitol document as YAML (the standards' primary encoding).
     *
     * @param document DataContract or DataProduct bean
     * @return YAML string
     * @throws IOException problem serializing the bean
     */
    public static String toYAML(BitolDocument document) throws IOException
    {
        return YAML_MAPPER.writeValueAsString(document);
    }


    /**
     * Write any Bitol bean (or list of beans) as JSON.  This is used to keep fragments of a document, such as its SLA
     * properties, losslessly in the additional properties of an open metadata element.
     *
     * @param bean bean or list of beans
     * @return JSON string
     * @throws IOException problem serializing the bean
     */
    public static String toJSONFragment(Object bean) throws IOException
    {
        return JSON_MAPPER.writeValueAsString(bean);
    }


    /**
     * Read a list of Bitol beans from a JSON fragment written by toJSONFragment.
     *
     * @param json JSON string
     * @param beanClass class of the list elements
     * @param <T> bean type
     * @return list of beans
     * @throws IOException problem parsing the JSON
     */
    public static <T> List<T> fromJSONFragmentList(String   json,
                                                   Class<T> beanClass) throws IOException
    {
        return JSON_MAPPER.readValue(json, JSON_MAPPER.getTypeFactory().constructCollectionType(List.class, beanClass));
    }


    /**
     * Write a Bitol document as JSON.
     *
     * @param document DataContract or DataProduct bean
     * @return JSON string
     * @throws IOException problem serializing the bean
     */
    public static String toJSON(BitolDocument document) throws IOException
    {
        return JSON_MAPPER.writeValueAsString(document);
    }
}
