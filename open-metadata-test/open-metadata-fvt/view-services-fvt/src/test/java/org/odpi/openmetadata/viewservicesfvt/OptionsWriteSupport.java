/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResultsRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.DataFileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworkservices.omf.rest.NewOpenMetadataElementRequestBody;
import org.odpi.openmetadata.frameworkservices.omf.rest.NewRelatedElementsRequestBody;
import org.odpi.openmetadata.frameworkservices.omf.rest.OpenMetadataDeleteRequestBody;
import org.odpi.openmetadata.frameworkservices.omf.rest.OpenMetadataElementResponse;
import org.odpi.openmetadata.frameworkservices.omf.rest.OpenMetadataRelationshipListResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Helpers shared by the OptionsWrite*FVT classes, which check that each write-side option field (the fields of
 * BasicOptions, MetadataSourceOptions, AnchorOptions, NewLinkedElementOptions, NewElementOptions, TemplateOptions,
 * UpdateOptions, UpdateWithTemplateOptions, DeleteOptions and MakeAnchorOptions) has its documented effect.
 * <br>
 * The effects are checked on the raw element that the Metadata Expert returns ({@link OpenMetadataElement}),
 * because it carries everything an option can change - the header (origin, versions, lastRequestId), every
 * classification with its properties, and the effectivity dates - without the interpretation a view service's
 * own bean adds.
 */
public class OptionsWriteSupport
{
    static final ViewServiceClient assetMaker             = new ViewServiceClient(ViewServiceDescription.ASSET_MAKER);
    static final ViewServiceClient metadataExpert         = new ViewServiceClient(ViewServiceDescription.METADATA_EXPERT);
    static final ViewServiceClient classificationExplorer = new ViewServiceClient(ViewServiceDescription.CLASSIFICATION_EXPLORER);
    static final ViewServiceClient collectionManager      = new ViewServiceClient(ViewServiceDescription.COLLECTION_MANAGER);
    static final ViewServiceClient automatedCuration      = new ViewServiceClient(ViewServiceDescription.AUTOMATED_CURATION);
    static final ViewServiceClient glossaryManager        = new ViewServiceClient(ViewServiceDescription.GLOSSARY_MANAGER);

    static final PropertyHelper propertyHelper = new PropertyHelper();

    private static final String SOURCE = OptionsWriteSupport.class.getSimpleName();


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
        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("options-write::" + typeName, name));
        properties.setDisplayName(name);

        return properties;
    }


    /**
     * Properties for a data file.
     *
     * @param name distinguishing name
     * @return properties
     */
    static DataFileProperties dataFile(String name)
    {
        DataFileProperties properties = named(new DataFileProperties(), "DataFile", name);

        properties.setDescription("Description of " + name);

        return properties;
    }


    /**
     * Create an asset that is its own anchor through Asset Maker.
     *
     * @param properties asset properties
     * @param created list to record the GUID in for clean up
     * @return new GUID
     */
    static String createAsset(ReferenceableProperties properties,
                              List<String>            created)
    {
        NewElementRequestBody requestBody = new NewElementRequestBody();

        requestBody.setIsOwnAnchor(true);
        requestBody.setProperties(properties);

        return record(assetMaker.create("/assets", requestBody), created);
    }


    /**
     * Create an element of any type through the Metadata Expert's generic create.
     *
     * @param requestBody request, already filled in apart from the properties
     * @param typeName type of element
     * @param name distinguishing name
     * @param created list to record the GUID in for clean up
     * @return new GUID
     */
    static String createGenericElement(NewOpenMetadataElementRequestBody requestBody,
                                       String                            typeName,
                                       String                            name,
                                       List<String>                      created)
    {
        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                        ViewServicesFvtTestSupport.qualifiedName("options-write::" + typeName, name));

        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DISPLAY_NAME.name, name);

        requestBody.setTypeName(typeName);
        requestBody.setProperties(new NewElementProperties(properties));

        return record(metadataExpert.create("/metadata-elements", requestBody), created);
    }


    /**
     * Link two elements through the Metadata Expert's generic relationship create.
     *
     * @param relationshipTypeName type of relationship
     * @param end1GUID element at end 1
     * @param end2GUID element at end 2
     * @return relationship GUID
     */
    static String link(String relationshipTypeName,
                       String end1GUID,
                       String end2GUID)
    {
        NewRelatedElementsRequestBody requestBody = new NewRelatedElementsRequestBody();

        requestBody.setTypeName(relationshipTypeName);
        requestBody.setMetadataElement1GUID(end1GUID);
        requestBody.setMetadataElement2GUID(end2GUID);

        return metadataExpert.create("/related-elements", requestBody);
    }


    /**
     * Record a GUID for clean up.
     *
     * @param guid new GUID
     * @param created list
     * @return guid
     */
    static String record(String       guid,
                         List<String> created)
    {
        created.add(guid);

        return guid;
    }


    /**
     * Read an element through the Metadata Expert with default options, failing if it is not there.
     *
     * @param guid unique identifier
     * @return element
     */
    static OpenMetadataElement raw(String guid)
    {
        return raw(guid, new GetRequestBody());
    }


    /**
     * Read an element through the Metadata Expert, failing if it is not there.
     *
     * @param guid unique identifier
     * @param requestBody options for the read
     * @return element
     */
    static OpenMetadataElement raw(String         guid,
                                   GetRequestBody requestBody)
    {
        OpenMetadataElement element = metadataExpert.post("/metadata-elements/" + guid, requestBody, OpenMetadataElementResponse.class).getElement();

        assertNotNull(element, "element " + guid + " is missing");

        return element;
    }


    /**
     * Read an element through the Metadata Expert, returning null if the service reports it missing or returns
     * nothing.
     *
     * @param guid unique identifier
     * @param requestBody options for the read
     * @return element or null
     */
    static OpenMetadataElement rawOrNull(String         guid,
                                         GetRequestBody requestBody)
    {
        OpenMetadataElementResponse response = metadataExpert.postUnchecked("/metadata-elements/" + guid, requestBody, OpenMetadataElementResponse.class);

        assertNotNull(response);

        return (response.getRelatedHTTPCode() == 200) ? response.getElement() : null;
    }


    /**
     * Read options that see mementos.
     *
     * @return request body
     */
    static GetRequestBody forLineage()
    {
        GetRequestBody requestBody = new GetRequestBody();

        requestBody.setForLineage(true);

        return requestBody;
    }


    /**
     * Return the named classification of an element, or null.
     *
     * @param element element
     * @param classificationName name of classification
     * @return classification or null
     */
    static AttachedClassification classification(OpenMetadataElement element,
                                                 String              classificationName)
    {
        if (element.getClassifications() != null)
        {
            for (AttachedClassification classification : element.getClassifications())
            {
                if (classificationName.equals(classification.getClassificationName()))
                {
                    return classification;
                }
            }
        }

        return null;
    }


    /**
     * Return the names of an element's classifications.
     *
     * @param element element
     * @return names
     */
    static List<String> classificationNames(OpenMetadataElement element)
    {
        List<String> names = new ArrayList<>();

        if (element.getClassifications() != null)
        {
            for (AttachedClassification classification : element.getClassifications())
            {
                names.add(classification.getClassificationName());
            }
        }

        Collections.sort(names);

        return names;
    }


    /**
     * Return a string property.
     *
     * @param properties properties
     * @param propertyName name
     * @return value or null
     */
    static String string(ElementProperties properties,
                         String            propertyName)
    {
        return propertyHelper.getStringProperty(SOURCE, propertyName, properties, "string");
    }


    /**
     * Return a string array property.
     *
     * @param properties properties
     * @param propertyName name
     * @return value or null
     */
    static List<String> strings(ElementProperties properties,
                                String            propertyName)
    {
        return propertyHelper.getStringArrayProperty(SOURCE, propertyName, properties, "strings");
    }


    /**
     * Return a string map property.
     *
     * @param properties properties
     * @param propertyName name
     * @return value or null
     */
    static Map<String, String> stringMap(ElementProperties properties,
                                         String            propertyName)
    {
        return propertyHelper.getStringMapFromProperty(SOURCE, propertyName, properties, "stringMap");
    }


    /**
     * Return a string property of an element.
     *
     * @param element element
     * @param propertyName name
     * @return value or null
     */
    static String string(OpenMetadataElement element,
                         String              propertyName)
    {
        return string(element.getElementProperties(), propertyName);
    }


    /**
     * Return the relationships of a type between two elements, either way round.
     *
     * @param end1GUID one element
     * @param relationshipTypeName type of relationship
     * @param end2GUID other element
     * @return relationships, empty if there are none
     */
    static List<OpenMetadataRelationship> relationships(String end1GUID,
                                                        String relationshipTypeName,
                                                        String end2GUID)
    {
        OpenMetadataRelationshipListResponse response = metadataExpert.post("/metadata-elements/" + end1GUID + "/linked-by-type/" + relationshipTypeName + "/to-elements/" + end2GUID,
                                                                            new ResultsRequestBody(),
                                                                            OpenMetadataRelationshipListResponse.class);

        if ((response.getRelationshipList() == null) || (response.getRelationshipList().getRelationships() == null))
        {
            return List.of();
        }

        return response.getRelationshipList().getRelationships();
    }


    /**
     * Send a request that must be refused and return the reason.
     *
     * @param client service client
     * @param path request path
     * @param requestBody request body
     * @return the exception error message
     */
    static String refusal(ViewServiceClient client,
                          String            path,
                          Object            requestBody)
    {
        return client.postForFailure(path, requestBody).path("exceptionErrorMessage").asText();
    }


    /**
     * Check that a request was refused with a particular message.
     *
     * @param client service client
     * @param path request path
     * @param requestBody request body
     * @param messageId expected message identifier
     */
    static void assertRefused(ViewServiceClient client,
                              String            path,
                              Object            requestBody,
                              String            messageId)
    {
        String refusal = refusal(client, path, requestBody);

        assertTrue(refusal.contains(messageId), path + " was refused for the wrong reason (expected " + messageId + "): " + refusal);
    }


    /**
     * Remove everything a test class created, whatever state it is in.  Each element is soft-deleted (with
     * forLineage so mementos are seen) and then purged.  Failures are ignored: an element may already have gone
     * with its anchor, or have been removed by the test itself.
     *
     * @param created GUIDs, most recent last
     */
    static void purgeAll(List<String> created)
    {
        purgeAll(created, null, null);
    }


    /**
     * Remove everything a test class created, whatever state it is in, on behalf of an external source.
     *
     * @param created GUIDs, most recent last
     * @param externalSourceGUID external source that owns the elements (or null)
     * @param externalSourceName its name
     */
    static void purgeAll(List<String> created,
                         String       externalSourceGUID,
                         String       externalSourceName)
    {
        List<String> reversed = new ArrayList<>(created);

        Collections.reverse(reversed);

        for (DeleteMethod deleteMethod : List.of(DeleteMethod.SOFT_DELETE, DeleteMethod.PURGE))
        {
            for (String guid : reversed)
            {
                OpenMetadataDeleteRequestBody requestBody = new OpenMetadataDeleteRequestBody();

                requestBody.setDeleteMethod(deleteMethod);
                requestBody.setCascadedDelete(true);
                requestBody.setForLineage(true);
                requestBody.setExternalSourceGUID(externalSourceGUID);
                requestBody.setExternalSourceName(externalSourceName);

                metadataExpert.postUnchecked("/metadata-elements/" + guid + "/delete", requestBody, VoidResponse.class);
            }
        }

        created.clear();
    }
}
