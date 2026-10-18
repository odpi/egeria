/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.FilterRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.TemplateRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateElementRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.LabeledRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRootProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.PartOfRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RoledRelationshipProperties;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Helpers for the design-oriented view service tests (Solution Architect, Data Designer, Schema Maker).  Their
 * requests create elements, link them in many different relationships and classify them, so the checks
 * that matter are "this element, read back, shows that relationship to that element" and "this element
 * carries that classification".
 * <br>
 * OpenMetadataRootElement spreads its relationships over many typed fields (schemaAttributes, foreignKeys,
 * nestedSolutionComponents ...) plus otherRelatedElements for the rest, so {@link #relationships} gathers
 * every RelatedMetadataElementSummary the element carries, whichever field the converter put it in.  The
 * checks then use the relationship's type name and the GUID at the other end, which is exact whatever the
 * field.
 */
public class DesignFvtSupport
{
    /**
     * Every relationship the element carries, from all its relationship fields.
     *
     * @param element element read back from a service
     * @return relationships
     */
    public static List<RelatedMetadataElementSummary> relationships(OpenMetadataRootElement element)
    {
        List<RelatedMetadataElementSummary> results = new ArrayList<>();

        for (Method method : element.getClass().getMethods())
        {
            if ((method.getParameterCount() == 0) && method.getName().startsWith("get"))
            {
                try
                {
                    if (RelatedMetadataElementSummary.class.isAssignableFrom(method.getReturnType()))
                    {
                        Object value = method.invoke(element);

                        if (value instanceof RelatedMetadataElementSummary summary)
                        {
                            results.add(summary);
                        }
                    }
                    else if (List.class.isAssignableFrom(method.getReturnType()))
                    {
                        Object value = method.invoke(element);

                        if (value instanceof List<?> list)
                        {
                            for (Object item : list)
                            {
                                if (item instanceof RelatedMetadataElementSummary summary)
                                {
                                    results.add(summary);
                                }
                            }
                        }
                    }
                }
                catch (ReflectiveOperationException error)
                {
                    fail("Unable to read " + method.getName() + ": " + error);
                }
            }
        }

        return results;
    }


    /**
     * Find the relationship of a type to a particular element.
     *
     * @param element element read back
     * @param relationshipTypeName type of relationship
     * @param otherEndGUID element at the other end
     * @return the relationship, or null
     */
    public static RelatedMetadataElementSummary findRelationship(OpenMetadataRootElement element,
                                                                 String                  relationshipTypeName,
                                                                 String                  otherEndGUID)
    {
        for (RelatedMetadataElementSummary summary : relationships(element))
        {
            if ((summary.getRelatedElement() != null) &&
                    (summary.getRelatedElement().getElementHeader() != null) &&
                    otherEndGUID.equals(summary.getRelatedElement().getElementHeader().getGUID()) &&
                    (summary.getRelationshipHeader() != null) &&
                    (summary.getRelationshipHeader().getType() != null) &&
                    relationshipTypeName.equals(summary.getRelationshipHeader().getType().getTypeName()))
            {
                return summary;
            }
        }

        return null;
    }


    /**
     * Read an element and check it shows a relationship to another element.
     *
     * @param client service client
     * @param retrievePath path that returns the element
     * @param relationshipTypeName type of relationship
     * @param otherEndGUID element at the other end
     * @return the relationship
     */
    public static RelatedMetadataElementSummary assertLinked(ViewServiceClient client,
                                                             String            retrievePath,
                                                             String            relationshipTypeName,
                                                             String            otherEndGUID)
    {
        OpenMetadataRootElement element = client.element(retrievePath, new GetRequestBody());

        assertNotNull(element, retrievePath + " returned nothing");

        RelatedMetadataElementSummary relationship = findRelationship(element, relationshipTypeName, otherEndGUID);

        assertNotNull(relationship, retrievePath + " does not show a " + relationshipTypeName + " relationship to " + otherEndGUID
                + "; it shows " + describe(element));

        return relationship;
    }


    /**
     * Read an element and check it no longer shows a relationship to another element.
     *
     * @param client service client
     * @param retrievePath path that returns the element
     * @param relationshipTypeName type of relationship
     * @param otherEndGUID element at the other end
     */
    public static void assertNotLinked(ViewServiceClient client,
                                       String            retrievePath,
                                       String            relationshipTypeName,
                                       String            otherEndGUID)
    {
        OpenMetadataRootElement element = client.element(retrievePath, new GetRequestBody());

        assertNotNull(element, retrievePath + " returned nothing");
        assertTrue(findRelationship(element, relationshipTypeName, otherEndGUID) == null,
                   retrievePath + " still shows a " + relationshipTypeName + " relationship to " + otherEndGUID + " after it was detached");
    }


    /**
     * Attach two elements with the supplied relationship properties, check the relationship (and its label or
     * position, if the properties carry one) is visible from the retrieved element, then detach it and check it
     * has gone.
     *
     * @param client service client
     * @param path the link request's path without its /attach or /detach suffix
     * @param retrievePath path that returns the element at one end
     * @param relationshipTypeName type of relationship
     * @param otherEndGUID element at the other end
     * @param properties relationship properties (may be null)
     */
    public static void linkCheckAndDetach(ViewServiceClient          client,
                                          String                     path,
                                          String                     retrievePath,
                                          String                     relationshipTypeName,
                                          String                     otherEndGUID,
                                          RelationshipBeanProperties properties)
    {
        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(properties);

        client.call(path + "/attach", requestBody);

        RelatedMetadataElementSummary relationship = assertLinked(client, retrievePath, relationshipTypeName, otherEndGUID);

        assertRelationshipProperties(path, properties, relationship);

        client.call(path + "/detach", new DeleteRelationshipRequestBody());

        assertNotLinked(client, retrievePath, relationshipTypeName, otherEndGUID);
    }


    /**
     * Check the label/description or position supplied when a relationship was created are returned with it.
     *
     * @param path the request (for failure messages)
     * @param properties properties supplied (may be null)
     * @param relationship relationship read back
     */
    public static void assertRelationshipProperties(String                        path,
                                                    RelationshipBeanProperties    properties,
                                                    RelatedMetadataElementSummary relationship)
    {
        if (properties instanceof LabeledRelationshipProperties labeled)
        {
            LabeledRelationshipProperties readBack = assertInstanceOf(LabeledRelationshipProperties.class,
                                                                      relationship.getRelationshipProperties(),
                                                                      path + " lost the relationship properties");
            assertEquals(labeled.getLabel(), readBack.getLabel(), path + " lost the relationship's label");
            assertEquals(labeled.getDescription(), readBack.getDescription(), path + " lost the relationship's description");
        }
        else if (properties instanceof RoledRelationshipProperties roled)
        {
            RoledRelationshipProperties readBack = assertInstanceOf(RoledRelationshipProperties.class,
                                                                    relationship.getRelationshipProperties(),
                                                                    path + " lost the relationship properties");
            assertEquals(roled.getRole(), readBack.getRole(), path + " lost the relationship's role");
            assertEquals(roled.getDescription(), readBack.getDescription(), path + " lost the relationship's description");
        }
        else if (properties instanceof PartOfRelationshipProperties partOf)
        {
            PartOfRelationshipProperties readBack = assertInstanceOf(PartOfRelationshipProperties.class,
                                                                     relationship.getRelationshipProperties(),
                                                                     path + " lost the relationship properties");
            assertEquals(partOf.getPosition(), readBack.getPosition(), path + " lost the relationship's position");
        }
    }


    /**
     * Build a link request with relationship properties.
     *
     * @param properties properties (may be null)
     * @return request body
     */
    public static NewRelationshipRequestBody relationship(RelationshipBeanProperties properties)
    {
        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(properties);

        return requestBody;
    }


    /**
     * Fill in a labeled relationship's properties.
     *
     * @param properties properties object
     * @param label label to give it
     * @return the properties
     * @param <T> type of properties
     */
    public static <T extends LabeledRelationshipProperties> T labeled(T      properties,
                                                                      String label)
    {
        properties.setLabel(label);
        properties.setDescription("Linked by the view services FVT");

        return properties;
    }


    /**
     * Fill in a roled relationship's properties.
     *
     * @param properties properties object
     * @param role role to give it
     * @return the properties
     * @param <T> type of properties
     */
    public static <T extends RoledRelationshipProperties> T roled(T      properties,
                                                                  String role)
    {
        properties.setRole(role);
        properties.setDescription("Linked by the view services FVT");

        return properties;
    }


    /**
     * Fill in a part-of relationship's position.
     *
     * @param properties properties object
     * @param position position to give it
     * @return the properties
     * @param <T> type of properties
     */
    public static <T extends PartOfRelationshipProperties> T positioned(T   properties,
                                                                        int position)
    {
        properties.setPosition(position);

        return properties;
    }


    /**
     * Summarize an element's relationships for a failure message.
     *
     * @param element element
     * @return text
     */
    public static String describe(OpenMetadataRootElement element)
    {
        List<String> descriptions = new ArrayList<>();

        for (RelatedMetadataElementSummary summary : relationships(element))
        {
            descriptions.add(((summary.getRelationshipHeader() == null) || (summary.getRelationshipHeader().getType() == null)
                    ? "?" : summary.getRelationshipHeader().getType().getTypeName())
                                     + "->" + ((summary.getRelatedElement() == null) || (summary.getRelatedElement().getElementHeader() == null)
                    ? "?" : summary.getRelatedElement().getElementHeader().getGUID()));
        }

        return descriptions.toString();
    }


    /**
     * Build a create request for an element that is its own anchor.
     *
     * @param properties properties of the element
     * @return request body
     */
    public static NewElementRequestBody newElement(OpenMetadataRootProperties properties)
    {
        NewElementRequestBody requestBody = new NewElementRequestBody();

        requestBody.setIsOwnAnchor(true);
        requestBody.setProperties(properties);

        return requestBody;
    }


    /**
     * Build a create-from-template request that gives the new element its own qualified name.
     *
     * @param templateGUID element to copy
     * @param replacementProperties properties of the new element (includes its qualified name)
     * @return request body
     */
    public static TemplateRequestBody fromTemplate(String                  templateGUID,
                                                   ReferenceableProperties replacementProperties)
    {
        TemplateRequestBody requestBody = new TemplateRequestBody();

        requestBody.setIsOwnAnchor(true);
        requestBody.setTemplateGUID(templateGUID);
        requestBody.setReplacementProperties(replacementProperties);

        return requestBody;
    }


    /**
     * Build an update request that changes only the supplied properties.
     *
     * @param properties properties to change
     * @return request body
     */
    public static UpdateElementRequestBody update(OpenMetadataRootProperties properties)
    {
        UpdateElementRequestBody requestBody = new UpdateElementRequestBody();

        requestBody.setMergeUpdate(true);
        requestBody.setProperties(properties);

        return requestBody;
    }


    /**
     * Build a by-name request.
     *
     * @param name name to match
     * @return request body
     */
    public static FilterRequestBody byName(String name)
    {
        FilterRequestBody requestBody = new FilterRequestBody();

        requestBody.setFilter(name);

        return requestBody;
    }


    /**
     * Build a search request that finds only this run's elements.
     *
     * @return request body
     */
    public static SearchStringRequestBody runSearch()
    {
        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(ViewServicesFvtTestSupport.runSearchString());

        return requestBody;
    }


    /**
     * Read an element and check its type and qualified name.
     *
     * @param client service client
     * @param retrievePath retrieve request
     * @param expectedTypeName type the element should have
     * @param expectedQualifiedName its qualified name
     * @return the element
     */
    public static OpenMetadataRootElement assertReadable(ViewServiceClient client,
                                                         String            retrievePath,
                                                         String            expectedTypeName,
                                                         String            expectedQualifiedName)
    {
        OpenMetadataRootElement element = client.element(retrievePath, new GetRequestBody());

        assertNotNull(element, retrievePath + " returned nothing");
        assertEquals(expectedTypeName, element.getElementHeader().getType().getTypeName(), retrievePath + " returned the wrong type");
        assertTrue(element.getProperties() instanceof ReferenceableProperties, retrievePath + " returned no referenceable properties");
        assertEquals(expectedQualifiedName, ((ReferenceableProperties)element.getProperties()).getQualifiedName());

        return element;
    }


    /**
     * Check a list of elements contains the element.
     *
     * @param elements list returned by a request
     * @param guid expected element
     * @param request the request (for the failure message)
     */
    public static void assertContains(List<OpenMetadataRootElement> elements,
                                      String                        guid,
                                      String                        request)
    {
        assertTrue(elements.stream().anyMatch(element -> guid.equals(element.getElementHeader().getGUID())),
                   request + " did not return " + guid);
    }


    /**
     * Check a list of elements does not contain the element.
     *
     * @param elements list returned by a request
     * @param guid unexpected element
     * @param request the request (for the failure message)
     */
    public static void assertNotContains(List<OpenMetadataRootElement> elements,
                                         String                        guid,
                                         String                        request)
    {
        assertFalse(elements.stream().anyMatch(element -> guid.equals(element.getElementHeader().getGUID())),
                    request + " returned " + guid);
    }


    /**
     * Delete an element and check it has gone.
     *
     * @param client service client
     * @param elementPath path of the element's requests, e.g. /schema-types/{guid}
     * @param retrievePath its retrieve request
     */
    public static void deleteAndCheck(ViewServiceClient client,
                                      String            elementPath,
                                      String            retrievePath)
    {
        client.call(elementPath + "/delete", new DeleteElementRequestBody());
        ViewServicesFvtTestSupport.assertGone(client, retrievePath);
    }
}
