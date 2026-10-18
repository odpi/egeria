/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementClassification;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * RelatedElementChecks looks through a retrieved element for its relationships and classifications without the
 * test naming the property of OpenMetadataRootElement that the converter files each one under.  A relationship
 * is identified by its type name and the GUID of the element at the other end, which is what the tests know.
 */
public class RelatedElementChecks
{
    /**
     * Return every related element summary held in the element's relationship lists.
     *
     * @param element retrieved element
     * @return relationships, possibly empty
     */
    public static List<RelatedMetadataElementSummary> relationships(OpenMetadataRootElement element)
    {
        List<RelatedMetadataElementSummary> results = new ArrayList<>();

        if (element == null)
        {
            return results;
        }

        for (Method method : element.getClass().getMethods())
        {
            if (method.getName().startsWith("get") && (method.getParameterCount() == 0) &&
                    List.class.isAssignableFrom(method.getReturnType()))
            {
                try
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
                catch (Exception error)
                {
                    throw new IllegalStateException("Unable to call " + method.getName(), error);
                }
            }
        }

        return results;
    }


    /**
     * Return the relationship of the named type to the element with the GUID, or null.
     *
     * @param element retrieved element
     * @param relationshipTypeName type of relationship
     * @param relatedGUID unique identifier of the element at the other end
     * @return relationship summary or null
     */
    public static RelatedMetadataElementSummary relationship(OpenMetadataRootElement element,
                                                             String                  relationshipTypeName,
                                                             String                  relatedGUID)
    {
        for (RelatedMetadataElementSummary summary : relationships(element))
        {
            if ((summary.getRelatedElement() != null) && (summary.getRelatedElement().getElementHeader() != null) &&
                    relatedGUID.equals(summary.getRelatedElement().getElementHeader().getGUID()) &&
                    (summary.getRelationshipHeader() != null) &&
                    relationshipTypeName.equals(summary.getRelationshipHeader().getType().getTypeName()))
            {
                return summary;
            }
        }

        return null;
    }


    /**
     * Is the element linked to the other element by a relationship of the named type?
     *
     * @param element retrieved element
     * @param relationshipTypeName type of relationship
     * @param relatedGUID unique identifier of the element at the other end
     * @return boolean
     */
    public static boolean isLinked(OpenMetadataRootElement element,
                                   String                  relationshipTypeName,
                                   String                  relatedGUID)
    {
        return relationship(element, relationshipTypeName, relatedGUID) != null;
    }


    /**
     * Return the names of the classifications in the element header.
     *
     * @param header element header
     * @return set of classification names
     */
    public static Set<String> classificationNames(ElementHeader header)
    {
        Set<String> results = new TreeSet<>();

        if (header == null)
        {
            return results;
        }

        for (Method method : header.getClass().getMethods())
        {
            if (method.getName().startsWith("get") && (method.getParameterCount() == 0))
            {
                try
                {
                    if (ElementClassification.class.isAssignableFrom(method.getReturnType()))
                    {
                        if (method.invoke(header) instanceof ElementClassification classification)
                        {
                            results.add(classification.getClassificationName());
                        }
                    }
                    else if (List.class.isAssignableFrom(method.getReturnType()))
                    {
                        if (method.invoke(header) instanceof List<?> list)
                        {
                            for (Object item : list)
                            {
                                if (item instanceof ElementClassification classification)
                                {
                                    results.add(classification.getClassificationName());
                                }
                            }
                        }
                    }
                }
                catch (Exception error)
                {
                    throw new IllegalStateException("Unable to call " + method.getName(), error);
                }
            }
        }

        return results;
    }


    /**
     * Return the classification of the given name from the element header, or null.
     *
     * @param header element header
     * @param classificationName name of classification
     * @return classification or null
     */
    public static ElementClassification classification(ElementHeader header,
                                                       String        classificationName)
    {
        if (header == null)
        {
            return null;
        }

        for (Method method : header.getClass().getMethods())
        {
            if (method.getName().startsWith("get") && (method.getParameterCount() == 0))
            {
                try
                {
                    if (ElementClassification.class.isAssignableFrom(method.getReturnType()))
                    {
                        if ((method.invoke(header) instanceof ElementClassification classification) &&
                                classificationName.equals(classification.getClassificationName()))
                        {
                            return classification;
                        }
                    }
                    else if (List.class.isAssignableFrom(method.getReturnType()))
                    {
                        if (method.invoke(header) instanceof List<?> list)
                        {
                            for (Object item : list)
                            {
                                if ((item instanceof ElementClassification classification) &&
                                        classificationName.equals(classification.getClassificationName()))
                                {
                                    return classification;
                                }
                            }
                        }
                    }
                }
                catch (Exception error)
                {
                    throw new IllegalStateException("Unable to call " + method.getName(), error);
                }
            }
        }

        return null;
    }


    /**
     * Does the list contain the element with this GUID?
     *
     * @param elements list of retrieved elements
     * @param guid unique identifier
     * @return boolean
     */
    public static boolean contains(List<OpenMetadataRootElement> elements,
                                   String                        guid)
    {
        if (elements != null)
        {
            for (OpenMetadataRootElement element : elements)
            {
                if ((element != null) && (element.getElementHeader() != null) && guid.equals(element.getElementHeader().getGUID()))
                {
                    return true;
                }
            }
        }

        return false;
    }
}
