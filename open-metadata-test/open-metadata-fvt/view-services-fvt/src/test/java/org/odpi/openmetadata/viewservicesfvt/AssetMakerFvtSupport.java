/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewElementRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRootProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Helpers shared by the Asset Maker, Connection Maker and Template Manager FVTs: creating the elements the
 * requests link together, and reading back an element's relationships and classifications whatever property of
 * OpenMetadataRootElement the converter files them under.
 * <br>
 * Elements are read back through the Classification Explorer's generic getRootElementByGUID so that any type
 * (asset, software capability, port, endpoint, connection, ...) can be checked the same way.  A relationship is
 * identified by its type name and the GUID of the element at the other end, found among the top-level
 * relationship summaries of the retrieved element only, so an element two hops away never counts.
 */
public class AssetMakerFvtSupport
{
    static final ViewServiceClient assetMaker     = new ViewServiceClient(ViewServiceDescription.ASSET_MAKER);
    static final ViewServiceClient explorer       = new ViewServiceClient(ViewServiceDescription.CLASSIFICATION_EXPLORER);
    static final ViewServiceClient metadataExpert = new ViewServiceClient(ViewServiceDescription.METADATA_EXPERT);

    private static final ObjectMapper   objectMapper   = new ObjectMapper();
    private static final PropertyHelper propertyHelper = new PropertyHelper();


    /**
     * Give a properties bean a run-unique qualified name and a display name.
     *
     * @param properties bean to fill
     * @param typeName type of the element, used in the qualified name
     * @param name distinguishing name
     * @return the same bean
     * @param <T> bean type
     */
    static <T extends ReferenceableProperties> T named(T      properties,
                                                       String typeName,
                                                       String name)
    {
        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName(typeName, name));
        properties.setDisplayName(name);

        return properties;
    }


    /**
     * Wrap properties in a request body for an element that is its own anchor.
     *
     * @param properties properties of the new element
     * @return request body
     */
    static NewElementRequestBody newElement(OpenMetadataRootProperties properties)
    {
        NewElementRequestBody requestBody = new NewElementRequestBody();

        requestBody.setIsOwnAnchor(true);
        requestBody.setProperties(properties);

        return requestBody;
    }


    /**
     * Create an asset through Asset Maker.
     *
     * @param properties asset properties (any subtype of AssetProperties)
     * @return new GUID
     */
    static String createAsset(OpenMetadataRootProperties properties)
    {
        return assetMaker.create("/assets", newElement(properties));
    }


    /**
     * Create an element of a type that no Asset Maker request creates (a Port, for example) through the
     * Metadata Expert's generic create.  The request bean lives in the OMF metadata API, so it is sent as JSON.
     *
     * @param typeName open metadata type name
     * @param name distinguishing name
     * @return new GUID
     */
    static String createGenericElement(String typeName,
                                       String name)
    {
        ElementProperties elementProperties = propertyHelper.addStringProperty(null, "qualifiedName", ViewServicesFvtTestSupport.qualifiedName(typeName, name));

        elementProperties = propertyHelper.addStringProperty(elementProperties, "displayName", name);

        Map<String, Object> requestBody = new HashMap<>();

        requestBody.put("class", "NewOpenMetadataElementRequestBody");
        requestBody.put("typeName", typeName);
        requestBody.put("isOwnAnchor", true);
        requestBody.put("properties", new NewElementProperties(elementProperties));

        return metadataExpert.create("/metadata-elements", requestBody);
    }


    /**
     * Read back any element.
     *
     * @param guid unique identifier
     * @return element, checked to exist
     */
    static OpenMetadataRootElement retrieve(String guid)
    {
        OpenMetadataRootElement element = explorer.element("/elements/" + guid, new GetRequestBody());

        assertNotNull(element, "element " + guid + " is missing");

        return element;
    }


    /**
     * Delete an element with Asset Maker's deleteAsset (which deletes any element) and check it is gone.
     *
     * @param guid unique identifier
     */
    static void delete(String guid)
    {
        assetMaker.call("/assets/" + guid + "/delete", new DeleteElementRequestBody());
        ViewServicesFvtTestSupport.assertGone(explorer, "/elements/" + guid);
    }


    /**
     * Return the "relationshipTypeName -> relatedGUID" pairs of the element's top-level relationship summaries.
     *
     * @param element retrieved element
     * @return set of "type:guid" strings
     */
    static Set<String> relationships(OpenMetadataRootElement element)
    {
        Set<String> results = new TreeSet<>();
        JsonNode    root    = objectMapper.valueToTree(element);

        for (Map.Entry<String, JsonNode> field : root.properties())
        {
            JsonNode value = field.getValue();

            if (value.isArray())
            {
                for (JsonNode item : value)
                {
                    addRelationship(item, results);
                }
            }
            else
            {
                addRelationship(value, results);
            }
        }

        return results;
    }


    /**
     * Return the JSON of the relationship properties of the relationships from one element to another, as seen
     * in the first element's top-level relationship summaries.
     *
     * @param guid element to retrieve
     * @param relatedGUID element at the other end
     * @return JSON text (empty if not related)
     */
    static String relationshipPropertiesJSON(String guid,
                                             String relatedGUID)
    {
        StringBuilder results = new StringBuilder();
        JsonNode      root    = objectMapper.valueToTree(retrieve(guid));

        for (Map.Entry<String, JsonNode> field : root.properties())
        {
            JsonNode value = field.getValue();

            for (JsonNode item : (value.isArray() ? value : List.of(value)))
            {
                if (item.isObject() && relatedGUID.equals(item.path("relatedElement").path("elementHeader").path("guid").asText()))
                {
                    results.append(item.path("relationshipProperties").toString());
                }
            }
        }

        return results.toString();
    }


    private static void addRelationship(JsonNode node, Set<String> results)
    {
        if (node.isObject() && node.has("relatedElement") && node.has("relationshipHeader"))
        {
            results.add(node.path("relationshipHeader").path("type").path("typeName").asText()
                                + ":" + node.path("relatedElement").path("elementHeader").path("guid").asText());
        }
    }


    /**
     * Return the names of the classifications in an element's header.
     *
     * @param element retrieved element
     * @return classification names
     */
    static Set<String> classifications(OpenMetadataRootElement element)
    {
        Set<String> results = new TreeSet<>();

        collectClassifications(objectMapper.valueToTree(element.getElementHeader()), results);

        return results;
    }


    private static void collectClassifications(JsonNode node, Set<String> results)
    {
        if (node.isObject())
        {
            if (node.has("classificationName"))
            {
                results.add(node.get("classificationName").asText());
            }

            node.forEach(child -> collectClassifications(child, results));
        }
        else if (node.isArray())
        {
            node.forEach(child -> collectClassifications(child, results));
        }
    }


    /**
     * Check that a relationship of the type links the two elements, as seen from both ends.
     *
     * @param relationshipTypeName relationship type
     * @param end1GUID one end
     * @param end2GUID other end
     */
    static void assertLinked(String relationshipTypeName,
                             String end1GUID,
                             String end2GUID)
    {
        assertTrue(relationships(retrieve(end1GUID)).contains(relationshipTypeName + ":" + end2GUID),
                   relationshipTypeName + " to " + end2GUID + " not visible from " + end1GUID + ": " + relationships(retrieve(end1GUID)));
        assertTrue(relationships(retrieve(end2GUID)).contains(relationshipTypeName + ":" + end1GUID),
                   relationshipTypeName + " to " + end1GUID + " not visible from " + end2GUID + ": " + relationships(retrieve(end2GUID)));
    }


    /**
     * Check that no relationship of the type links the two elements, from either end.
     *
     * @param relationshipTypeName relationship type
     * @param end1GUID one end
     * @param end2GUID other end
     */
    static void assertNotLinked(String relationshipTypeName,
                                String end1GUID,
                                String end2GUID)
    {
        assertFalse(relationships(retrieve(end1GUID)).contains(relationshipTypeName + ":" + end2GUID),
                    relationshipTypeName + " to " + end2GUID + " still visible from " + end1GUID);
        assertFalse(relationships(retrieve(end2GUID)).contains(relationshipTypeName + ":" + end1GUID),
                    relationshipTypeName + " to " + end1GUID + " still visible from " + end2GUID);
    }


    /**
     * Check whether an element carries a classification.
     *
     * @param guid element
     * @param classificationName classification type name
     * @param expected whether it should be there
     */
    static void assertClassified(String  guid,
                                 String  classificationName,
                                 boolean expected)
    {
        Set<String> classifications = classifications(retrieve(guid));

        if (expected)
        {
            assertTrue(classifications.contains(classificationName), guid + " is not classified " + classificationName + ": " + classifications);
        }
        else
        {
            assertFalse(classifications.contains(classificationName), guid + " is still classified " + classificationName);
        }
    }
}
