/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewElementRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementClassification;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRootProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Helpers shared by the DevopsPipeline*FVT classes: clients for the services that build the elements the Devops
 * Pipeline OMVS links together, and checks for the relationships and classifications that a retrieved element
 * carries.
 * <br>
 * The converters spread an element's relationships over dozens of named lists (with the rest in
 * otherRelatedElements) and its classifications over dozens of named header fields (with the rest in
 * otherClassifications).  Which list a relationship lands in is not what these tests are about, so the checks
 * gather every list reflectively and look for the relationship or classification by type name.
 */
public class DevopsPipelineFvtSupport
{
    static final ViewServiceClient devops            = new ViewServiceClient(ViewServiceDescription.DEVOPS_PIPELINE);
    static final ViewServiceClient assetMaker        = new ViewServiceClient(ViewServiceDescription.ASSET_MAKER);
    static final ViewServiceClient collectionManager = new ViewServiceClient(ViewServiceDescription.COLLECTION_MANAGER);
    static final ViewServiceClient connectionMaker   = new ViewServiceClient(ViewServiceDescription.CONNECTION_MAKER);


    /**
     * Fill in the qualified name, display name and description of a new element's properties.
     *
     * @param properties properties to fill in
     * @param typeName type of element
     * @param name distinguishing name
     * @return the properties
     * @param <T> properties type
     */
    static <T extends ReferenceableProperties> T named(T      properties,
                                                       String typeName,
                                                       String name)
    {
        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName(typeName, name));
        properties.setDisplayName(typeName + " " + name);
        properties.setDescription("Created by the Devops Pipeline FVT");

        return properties;
    }


    /**
     * Wrap properties in a create request for an element that is its own anchor.
     *
     * @param properties properties
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
     * @param properties asset properties
     * @return guid
     */
    static String createAsset(OpenMetadataRootProperties properties)
    {
        return assetMaker.create("/assets", newElement(properties));
    }


    /**
     * Delete an asset through Asset Maker and check it has gone.
     *
     * @param assetGUID guid
     */
    static void deleteAsset(String assetGUID)
    {
        if (assetGUID != null)
        {
            assetMaker.call("/assets/" + assetGUID + "/delete", new DeleteElementRequestBody());
            ViewServicesFvtTestSupport.assertGone(assetMaker, "/assets/" + assetGUID + "/retrieve");
        }
    }


    /**
     * Retrieve an asset through Asset Maker.
     *
     * @param assetGUID guid
     * @return element
     */
    static OpenMetadataRootElement asset(String assetGUID)
    {
        return retrieve(assetMaker, "/assets/" + assetGUID + "/retrieve");
    }


    /**
     * Create a collection (or a subtype such as SoftwareComponent) through Collection Manager.
     *
     * @param properties collection properties
     * @return guid
     */
    static String createCollection(OpenMetadataRootProperties properties)
    {
        return collectionManager.create("/collections", newElement(properties));
    }


    /**
     * Delete a collection through Collection Manager and check it has gone.
     *
     * @param collectionGUID guid
     */
    static void deleteCollection(String collectionGUID)
    {
        if (collectionGUID != null)
        {
            collectionManager.call("/collections/" + collectionGUID + "/delete", new DeleteElementRequestBody());
            ViewServicesFvtTestSupport.assertGone(collectionManager, "/collections/" + collectionGUID + "/retrieve");
        }
    }


    /**
     * Retrieve a collection through Collection Manager.
     *
     * @param collectionGUID guid
     * @return element
     */
    static OpenMetadataRootElement collection(String collectionGUID)
    {
        return retrieve(collectionManager, "/collections/" + collectionGUID + "/retrieve");
    }


    /**
     * Retrieve an element with its directly related elements, failing if it is missing.
     *
     * @param client client for the service
     * @param retrievePath retrieve request
     * @return element
     */
    static OpenMetadataRootElement retrieve(ViewServiceClient client,
                                            String            retrievePath)
    {
        GetRequestBody requestBody = new GetRequestBody();

        requestBody.setGraphQueryDepth(1);

        OpenMetadataRootElement element = client.element(retrievePath, requestBody);

        assertNotNull(element, retrievePath + " returned no element");

        return element;
    }


    /**
     * Return the qualified name of a retrieved element.
     *
     * @param element element
     * @return qualified name or null
     */
    static String qualifiedName(OpenMetadataRootElement element)
    {
        if (element.getProperties() instanceof ReferenceableProperties referenceableProperties)
        {
            return referenceableProperties.getQualifiedName();
        }

        return null;
    }


    /**
     * Return whether any element in the list has the GUID.
     *
     * @param elements elements
     * @param guid guid
     * @return flag
     */
    static boolean contains(List<OpenMetadataRootElement> elements,
                            String                        guid)
    {
        return elements.stream().anyMatch(element -> guid.equals(element.getElementHeader().getGUID()));
    }


    /**
     * Return the relationships of a type between an element and another element.
     *
     * @param element retrieved element
     * @param relationshipTypeName relationship type
     * @param otherGUID guid of the element at the other end
     * @return matching relationships (empty if none)
     */
    static List<RelatedMetadataElementSummary> related(OpenMetadataRootElement element,
                                                       String                  relationshipTypeName,
                                                       String                  otherGUID)
    {
        List<RelatedMetadataElementSummary> results = new ArrayList<>();

        for (RelatedMetadataElementSummary relatedElement : allRelatedElements(element))
        {
            if ((relatedElement.getRelationshipHeader() != null) &&
                    (relatedElement.getRelationshipHeader().getType() != null) &&
                    relationshipTypeName.equals(relatedElement.getRelationshipHeader().getType().getTypeName()) &&
                    (relatedElement.getRelatedElement() != null) &&
                    (relatedElement.getRelatedElement().getElementHeader() != null) &&
                    otherGUID.equals(relatedElement.getRelatedElement().getElementHeader().getGUID()))
            {
                results.add(relatedElement);
            }
        }

        return results;
    }


    /**
     * Return the named classification of an element.
     *
     * @param element retrieved element
     * @param classificationName classification type
     * @return classification or null
     */
    static ElementClassification classification(OpenMetadataRootElement element,
                                                String                  classificationName)
    {
        for (ElementClassification classification : allClassifications(element.getElementHeader()))
        {
            if (classificationName.equals(classification.getClassificationName()))
            {
                return classification;
            }
        }

        return null;
    }


    /**
     * Gather every related element the converter attached to the element, whichever list it is in.
     *
     * @param element element
     * @return related elements
     */
    private static List<RelatedMetadataElementSummary> allRelatedElements(OpenMetadataRootElement element)
    {
        List<RelatedMetadataElementSummary> results = new ArrayList<>();

        for (Object value : getterValues(element))
        {
            if (value instanceof RelatedMetadataElementSummary relatedElement)
            {
                results.add(relatedElement);
            }
            else if (value instanceof List<?> list)
            {
                for (Object item : list)
                {
                    if (item instanceof RelatedMetadataElementSummary relatedElement)
                    {
                        results.add(relatedElement);
                    }
                }
            }
        }

        return results;
    }


    /**
     * Gather every classification in an element header, whichever field it is in.
     *
     * @param header element header
     * @return classifications
     */
    private static List<ElementClassification> allClassifications(ElementHeader header)
    {
        List<ElementClassification> results = new ArrayList<>();

        for (Object value : getterValues(header))
        {
            if (value instanceof ElementClassification classification)
            {
                results.add(classification);
            }
            else if (value instanceof List<?> list)
            {
                for (Object item : list)
                {
                    if (item instanceof ElementClassification classification)
                    {
                        results.add(classification);
                    }
                }
            }
        }

        return results;
    }


    /**
     * Call every public no-argument getter of a bean.
     *
     * @param bean bean
     * @return values (nulls skipped)
     */
    private static List<Object> getterValues(Object bean)
    {
        List<Object> values = new ArrayList<>();

        for (Method method : bean.getClass().getMethods())
        {
            if ((method.getParameterCount() == 0) &&
                    method.getName().startsWith("get") &&
                    (method.getDeclaringClass() != Object.class))
            {
                try
                {
                    Object value = method.invoke(bean);

                    if (value != null)
                    {
                        values.add(value);
                    }
                }
                catch (ReflectiveOperationException error)
                {
                    throw new IllegalStateException("Unable to call " + method, error);
                }
            }
        }

        return values;
    }
}
