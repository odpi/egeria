/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementClassification;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Assertions shared by the actor, community, privacy, security and governance officer test classes.
 * <br>
 * A retrieved {@link OpenMetadataRootElement} sorts its relationships into dozens of named lists (with
 * {@code otherRelatedElements} for the rest), and its classifications into named fields of its header.  A test
 * that links two elements cares that the relationship is there, not which list the converter chose, so these
 * helpers look through all of them.
 */
public class ActorGovernanceFvtSupport
{
    /**
     * Return every relationship summary in a retrieved element.
     *
     * @param element retrieved element
     * @return summaries
     */
    public static List<RelatedMetadataElementSummary> relationships(OpenMetadataRootElement element)
    {
        List<RelatedMetadataElementSummary> summaries = new ArrayList<>();

        for (Method method : OpenMetadataRootElement.class.getMethods())
        {
            if (method.getName().startsWith("get") && (method.getParameterCount() == 0))
            {
                try
                {
                    Object value = method.invoke(element);

                    if (value instanceof RelatedMetadataElementSummary summary)
                    {
                        summaries.add(summary);
                    }
                    else if (value instanceof List<?> list)
                    {
                        for (Object item : list)
                        {
                            if (item instanceof RelatedMetadataElementSummary summary)
                            {
                                summaries.add(summary);
                            }
                        }
                    }
                }
                catch (ReflectiveOperationException error)
                {
                    throw new IllegalStateException("Could not read " + method.getName(), error);
                }
            }
        }

        return summaries;
    }


    /**
     * Return the unique identifiers of the elements linked to a retrieved element by one type of relationship.
     *
     * @param element retrieved element
     * @param relationshipTypeName type of relationship
     * @return linked element GUIDs
     */
    public static Set<String> linkedGUIDs(OpenMetadataRootElement element,
                                          String                  relationshipTypeName)
    {
        Set<String> guids = new HashSet<>();

        for (RelatedMetadataElementSummary summary : relationships(element))
        {
            if ((summary.getRelationshipHeader() != null) &&
                    (summary.getRelationshipHeader().getType() != null) &&
                    relationshipTypeName.equals(summary.getRelationshipHeader().getType().getTypeName()) &&
                    (summary.getRelatedElement() != null) &&
                    (summary.getRelatedElement().getElementHeader() != null))
            {
                guids.add(summary.getRelatedElement().getElementHeader().getGUID());
            }
        }

        return guids;
    }


    /**
     * Return the summary of the relationship of a type to a particular element, or null.
     *
     * @param element retrieved element
     * @param relationshipTypeName type of relationship
     * @param relatedGUID linked element
     * @return summary or null
     */
    public static RelatedMetadataElementSummary relationshipTo(OpenMetadataRootElement element,
                                                               String                  relationshipTypeName,
                                                               String                  relatedGUID)
    {
        for (RelatedMetadataElementSummary summary : relationships(element))
        {
            if ((summary.getRelationshipHeader() != null) &&
                    (summary.getRelationshipHeader().getType() != null) &&
                    relationshipTypeName.equals(summary.getRelationshipHeader().getType().getTypeName()) &&
                    (summary.getRelatedElement() != null) &&
                    (summary.getRelatedElement().getElementHeader() != null) &&
                    relatedGUID.equals(summary.getRelatedElement().getElementHeader().getGUID()))
            {
                return summary;
            }
        }

        return null;
    }


    /**
     * Fail unless a retrieved element is linked to another by a relationship of the named type.
     *
     * @param element retrieved element
     * @param relationshipTypeName type of relationship
     * @param relatedGUID element that should be linked
     */
    public static void assertLinked(OpenMetadataRootElement element,
                                    String                  relationshipTypeName,
                                    String                  relatedGUID)
    {
        assertNotNull(element, "element to check for a " + relationshipTypeName + " relationship is missing");
        assertTrue(linkedGUIDs(element, relationshipTypeName).contains(relatedGUID),
                   element.getElementHeader().getGUID() + " has no " + relationshipTypeName + " relationship to " + relatedGUID +
                           "; it has " + describe(element));
    }


    /**
     * Fail if a retrieved element is linked to another by a relationship of the named type.
     *
     * @param element retrieved element
     * @param relationshipTypeName type of relationship
     * @param relatedGUID element that should not be linked
     */
    public static void assertNotLinked(OpenMetadataRootElement element,
                                       String                  relationshipTypeName,
                                       String                  relatedGUID)
    {
        assertNotNull(element);
        assertFalse(linkedGUIDs(element, relationshipTypeName).contains(relatedGUID),
                    element.getElementHeader().getGUID() + " still has a " + relationshipTypeName + " relationship to " + relatedGUID);
    }


    /**
     * Return the named classification of a retrieved element, or null.
     *
     * @param element retrieved element
     * @param classificationName name of the classification
     * @return classification or null
     */
    public static ElementClassification classification(OpenMetadataRootElement element,
                                                       String                  classificationName)
    {
        ElementHeader header = element.getElementHeader();

        for (Method method : ElementHeader.class.getMethods())
        {
            if (method.getName().startsWith("get") && (method.getParameterCount() == 0))
            {
                try
                {
                    Object value = method.invoke(header);

                    if ((value instanceof ElementClassification classification) && classificationName.equals(classification.getClassificationName()))
                    {
                        return classification;
                    }
                    else if (value instanceof List<?> list)
                    {
                        for (Object item : list)
                        {
                            if ((item instanceof ElementClassification classification) && classificationName.equals(classification.getClassificationName()))
                            {
                                return classification;
                            }
                        }
                    }
                }
                catch (ReflectiveOperationException error)
                {
                    throw new IllegalStateException("Could not read " + method.getName(), error);
                }
            }
        }

        return null;
    }


    /**
     * Return the GUIDs of a list of elements.
     *
     * @param elements elements
     * @return GUIDs
     */
    public static Set<String> guids(List<OpenMetadataRootElement> elements)
    {
        Set<String> guids = new HashSet<>();

        for (OpenMetadataRootElement element : elements)
        {
            guids.add(element.getElementHeader().getGUID());
        }

        return guids;
    }


    private static String describe(OpenMetadataRootElement element)
    {
        List<String> descriptions = new ArrayList<>();

        for (RelatedMetadataElementSummary summary : relationships(element))
        {
            if ((summary.getRelationshipHeader() != null) && (summary.getRelationshipHeader().getType() != null) &&
                    (summary.getRelatedElement() != null) && (summary.getRelatedElement().getElementHeader() != null))
            {
                descriptions.add(summary.getRelationshipHeader().getType().getTypeName() + "->" + summary.getRelatedElement().getElementHeader().getGUID());
            }
        }

        return descriptions.toString();
    }
}
