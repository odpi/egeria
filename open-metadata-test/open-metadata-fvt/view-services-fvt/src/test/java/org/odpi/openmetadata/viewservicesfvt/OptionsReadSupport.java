/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResultsRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworkservices.omf.rest.NewOpenMetadataClassificationRequestBody;
import org.odpi.openmetadata.frameworkservices.omf.rest.NewOpenMetadataElementRequestBody;
import org.odpi.openmetadata.frameworkservices.omf.rest.NewRelatedElementsRequestBody;
import org.odpi.openmetadata.frameworkservices.omf.rest.OpenMetadataDeleteRequestBody;
import org.odpi.openmetadata.frameworkservices.omf.rest.OpenMetadataElementResponse;
import org.odpi.openmetadata.frameworkservices.omf.rest.OpenMetadataElementsResponse;
import org.odpi.openmetadata.frameworkservices.omf.rest.RelatedMetadataElementListResponse;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Helpers for the OptionsRead* FVTs, which check that each read-side option on the request bodies (GetOptions,
 * PagingOptions, QueryOptions, SearchOptions, HistoricalQueryOptions and BasicOptions.effectiveTime) has its
 * documented effect.
 * <br>
 * Elements are created with the Metadata Expert's generic create so that the tests control the type, the
 * properties, the effectivity dates and the anchor of each one.  Every qualified name these classes create starts
 * with {@link #prefix(String)} for its scenario, so a search on that prefix returns the scenario's elements and
 * nothing else - even in a PostgreSQL repository that holds earlier runs.
 */
public class OptionsReadSupport
{
    static final ViewServiceClient metadataExpert = new ViewServiceClient(ViewServiceDescription.METADATA_EXPERT);
    static final ViewServiceClient assetMaker     = new ViewServiceClient(ViewServiceDescription.ASSET_MAKER);
    static final ViewServiceClient actorManager   = new ViewServiceClient(ViewServiceDescription.ACTOR_MANAGER);

    static final PropertyHelper propertyHelper = new PropertyHelper();

    static final String QUALIFIED_NAME = OpenMetadataProperty.QUALIFIED_NAME.name;
    static final String DISPLAY_NAME   = OpenMetadataProperty.DISPLAY_NAME.name;
    static final String DESCRIPTION    = OpenMetadataProperty.DESCRIPTION.name;

    static final long ONE_DAY = 86_400_000L;


    /**
     * The start of every qualified name created for a scenario.
     *
     * @param scenario name of the scenario
     * @return prefix, unique to this run
     */
    static String prefix(String scenario)
    {
        return ViewServicesFvtTestSupport.qualifiedName("OptRead", scenario) + "::";
    }


    /**
     * Create an element that is its own anchor.
     *
     * @param typeName type
     * @param qualifiedName qualified name
     * @param displayName display name (may be null)
     * @param description description (may be null)
     * @param effectiveFrom effectivity start (may be null)
     * @param effectiveTo effectivity end (may be null)
     * @return new GUID
     */
    static String createElement(String typeName,
                                String qualifiedName,
                                String displayName,
                                String description,
                                Date   effectiveFrom,
                                Date   effectiveTo)
    {
        ElementProperties properties = propertyHelper.addStringProperty(null, QUALIFIED_NAME, qualifiedName);

        if (displayName != null)
        {
            properties = propertyHelper.addStringProperty(properties, DISPLAY_NAME, displayName);
        }

        if (description != null)
        {
            properties = propertyHelper.addStringProperty(properties, DESCRIPTION, description);
        }

        NewElementProperties newElementProperties = new NewElementProperties(properties);

        newElementProperties.setEffectiveFrom(effectiveFrom);
        newElementProperties.setEffectiveTo(effectiveTo);

        NewOpenMetadataElementRequestBody requestBody = new NewOpenMetadataElementRequestBody();

        requestBody.setTypeName(typeName);
        requestBody.setIsOwnAnchor(true);
        requestBody.setProperties(newElementProperties);

        return metadataExpert.create("/metadata-elements", requestBody);
    }


    /**
     * Create an element that is its own anchor, with no effectivity dates.
     *
     * @param typeName type
     * @param qualifiedName qualified name
     * @param displayName display name (may be null)
     * @param description description (may be null)
     * @return new GUID
     */
    static String createElement(String typeName,
                                String qualifiedName,
                                String displayName,
                                String description)
    {
        return createElement(typeName, qualifiedName, displayName, description, null, null);
    }


    /**
     * Link two elements.
     *
     * @param typeName relationship type
     * @param end1GUID element at end 1
     * @param end2GUID element at end 2
     * @param effectiveFrom effectivity start (may be null)
     * @param effectiveTo effectivity end (may be null)
     * @return relationship GUID
     */
    static String relate(String typeName,
                         String end1GUID,
                         String end2GUID,
                         Date   effectiveFrom,
                         Date   effectiveTo)
    {
        NewRelatedElementsRequestBody requestBody = new NewRelatedElementsRequestBody();

        requestBody.setTypeName(typeName);
        requestBody.setMetadataElement1GUID(end1GUID);
        requestBody.setMetadataElement2GUID(end2GUID);

        if ((effectiveFrom != null) || (effectiveTo != null))
        {
            NewElementProperties properties = new NewElementProperties();

            properties.setEffectiveFrom(effectiveFrom);
            properties.setEffectiveTo(effectiveTo);

            requestBody.setProperties(properties);
        }

        return metadataExpert.create("/related-elements", requestBody);
    }


    /**
     * Add a classification with optional properties.
     *
     * @param elementGUID element to classify
     * @param classificationName classification type
     * @param properties properties (may be null)
     */
    static void classify(String            elementGUID,
                         String            classificationName,
                         ElementProperties properties)
    {
        NewOpenMetadataClassificationRequestBody requestBody = new NewOpenMetadataClassificationRequestBody();

        if (properties != null)
        {
            requestBody.setProperties(new NewElementProperties(properties));
        }

        metadataExpert.call("/metadata-elements/" + elementGUID + "/classifications/" + classificationName, requestBody);
    }


    /**
     * Read an element through the Metadata Expert.
     *
     * @param guid element
     * @param requestBody options
     * @return element, or null if the service reports it missing or returns nothing
     */
    static OpenMetadataElement getElement(String         guid,
                                          GetRequestBody requestBody)
    {
        OpenMetadataElementResponse response = metadataExpert.postUnchecked("/metadata-elements/" + guid, requestBody, OpenMetadataElementResponse.class);

        assertNotNull(response);

        return (response.getRelatedHTTPCode() == 200) ? response.getElement() : null;
    }


    /**
     * Run the Metadata Expert's search by string.
     *
     * @param requestBody search string and options
     * @return elements, empty rather than null
     */
    static List<OpenMetadataElement> search(SearchStringRequestBody requestBody)
    {
        List<OpenMetadataElement> elements = metadataExpert.post("/metadata-elements/by-search-string",
                                                                 requestBody,
                                                                 OpenMetadataElementsResponse.class).getElements();

        return (elements == null) ? List.of() : elements;
    }


    /**
     * Run the Metadata Expert's search by string and return the GUIDs found, in order.
     *
     * @param requestBody search string and options
     * @return GUIDs
     */
    static List<String> searchGUIDs(SearchStringRequestBody requestBody)
    {
        return guids(search(requestBody));
    }


    /**
     * A search request for a string with default options.
     *
     * @param searchString string to search for
     * @return request body
     */
    static SearchStringRequestBody searchFor(String searchString)
    {
        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);

        return requestBody;
    }


    /**
     * Return the GUIDs of the elements related to an element through the Metadata Expert.
     *
     * @param elementGUID starting element
     * @param requestBody options
     * @return related GUIDs
     */
    static List<String> relatedGUIDs(String             elementGUID,
                                     ResultsRequestBody requestBody)
    {
        RelatedMetadataElementListResponse response = metadataExpert.post("/related-elements/" + elementGUID + "/any-type",
                                                                          requestBody,
                                                                          RelatedMetadataElementListResponse.class);
        List<String> results = new ArrayList<>();

        if ((response.getRelatedElementList() != null) && (response.getRelatedElementList().getElementList() != null))
        {
            for (RelatedMetadataElement relatedMetadataElement : response.getRelatedElementList().getElementList())
            {
                results.add(relatedMetadataElement.getElement().getElementGUID());
            }
        }

        return results;
    }


    /**
     * Return the GUIDs of a list of elements, in order.
     *
     * @param elements elements
     * @return GUIDs
     */
    static List<String> guids(List<? extends OpenMetadataElement> elements)
    {
        List<String> results = new ArrayList<>();

        if (elements != null)
        {
            for (OpenMetadataElement element : elements)
            {
                results.add(element.getElementGUID());
            }
        }

        return results;
    }


    /**
     * Keep only the GUIDs that belong to the fixture, preserving their order.
     *
     * @param found GUIDs returned by a query
     * @param fixture GUIDs the test created
     * @return the fixture GUIDs that were returned
     */
    static List<String> only(List<String>       found,
                             Collection<String> fixture)
    {
        return found.stream().filter(fixture::contains).toList();
    }


    /**
     * Return a string property of an element.
     *
     * @param element element
     * @param propertyName property name
     * @return value or null
     */
    static String stringProperty(OpenMetadataElement element,
                                 String              propertyName)
    {
        return propertyHelper.getStringProperty("OptionsReadSupport", propertyName, element.getElementProperties(), "stringProperty");
    }


    /**
     * Delete an element with the requested method.  Failures are ignored - this is clean-up.
     *
     * @param guid element
     * @param deleteMethod how to delete it
     */
    static void deleteQuietly(String       guid,
                              DeleteMethod deleteMethod)
    {
        OpenMetadataDeleteRequestBody requestBody = new OpenMetadataDeleteRequestBody();

        requestBody.setDeleteMethod(deleteMethod);

        try
        {
            metadataExpert.postUnchecked("/metadata-elements/" + guid + "/delete", requestBody, org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse.class);
        }
        catch (Exception ignored)
        {
            // clean-up is best effort
        }
    }


    /**
     * Purge every element in the list.  Failures are ignored - this is clean-up.
     *
     * @param guids elements
     */
    static void purgeAll(List<String> guids)
    {
        for (String guid : guids)
        {
            deleteQuietly(guid, DeleteMethod.PURGE);
        }
    }


    /**
     * Pause long enough for the repository to give the next change a later timestamp.
     */
    static void tick()
    {
        try
        {
            Thread.sleep(25);
        }
        catch (InterruptedException interrupted)
        {
            Thread.currentThread().interrupt();
        }
    }


    /**
     * Run an action and return everything written to System.out while it ran.  The output still reaches the
     * original stream.  The in-process platform's console audit log writes to System.out, so this captures the
     * audit log messages that the action caused.
     *
     * @param action action to run
     * @return captured output
     */
    static String captureStandardOut(Runnable action)
    {
        PrintStream           original = System.out;
        ByteArrayOutputStream captured = new ByteArrayOutputStream();

        OutputStream tee = new OutputStream()
        {
            @Override
            public void write(int b)
            {
                original.write(b);
                captured.write(b);
            }

            @Override
            public void write(byte[] bytes, int offset, int length)
            {
                original.write(bytes, offset, length);
                captured.write(bytes, offset, length);
            }
        };

        System.setOut(new PrintStream(tee, true, StandardCharsets.UTF_8));

        try
        {
            action.run();
        }
        finally
        {
            System.out.flush();
            System.setOut(original);
        }

        return captured.toString(StandardCharsets.UTF_8);
    }
}
