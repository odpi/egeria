/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.queryfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.CollectionClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyComparisonOperator;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * CountPushDownFVT covers the {@code pushDown} request parameter on the two counting endpoints.
 * <br><br>
 * The two ways of answering "how many match?" do not always agree.  With {@code pushDown=true} - the default -
 * the repository counts the matching rows itself and never materialises them, which is fast but counts what
 * matches the search rather than what a caller would be given: the retrieval puts every element through a
 * visibility check and silently drops the ones whose anchor the caller cannot read.  With
 * {@code pushDown=false} the elements are retrieved and counted, so the answer agrees with the list by
 * construction, at the cost of reading every one of them.
 * <br><br>
 * The elements this test creates are all plainly visible to the caller that created them, so both routes have
 * to agree here.  That is the point: it pins down that the parameter is threaded through every layer and that
 * the retrieval route counts correctly, which a boolean quietly lost between the REST resource and the handler
 * would break.  It cannot cover the case where the two legitimately disagree - that needs an element the
 * calling user cannot read, which this suite has no way to arrange.
 * <br><br>
 * The counts are taken twice over: once over raw REST, which is the contract an outside caller such as
 * pyegeria sees, and once through the Java client, which builds that request for itself.  Both are checked
 * because the flag travels differently in each - it is a query parameter, not a field of the request body, so
 * the client has to put it in the URL rather than serialise it with everything else.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class CountPushDownFVT
{
    private static final int    ELEMENT_COUNT = 12;
    private static final String CATEGORY      = "CountPushDown";

    private static final String COUNT_URL_TEMPLATE = "%s/servers/%s/open-metadata/access-services/open-metadata-store"
            + "/users/%s/metadata-elements/by-search-conditions/count";


    @Test
    void bothCountingRoutesAgreeWhenEveryElementIsVisible() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        CollectionClient     collectionClient  = connectorContext.getCollectionClient();
        OpenMetadataStore    openMetadataStore = connectorContext.getOpenMetadataStore();

        String       runPrefix    = QueryFvtTestSupport.newQualifiedName(CATEGORY);
        List<String> createdGUIDs = new ArrayList<>();

        try
        {
            for (int i = 0; i < ELEMENT_COUNT; i++)
            {
                NewElementOptions newElementOptions = new NewElementOptions();

                newElementOptions.setIsOwnAnchor(true);

                CollectionProperties createProperties = new CollectionProperties();

                createProperties.setQualifiedName(runPrefix + ":" + String.format("%02d", i));
                createProperties.setDisplayName("query-fvt Count PushDown Collection " + i);

                createdGUIDs.add(collectionClient.createCollection(newElementOptions, null, createProperties, null));
            }

            long pushedDown = countOverRest(runPrefix, "?pushDown=true");
            long retrieved  = countOverRest(runPrefix, "?pushDown=false");
            long defaulted  = countOverRest(runPrefix, "");

            assertEquals(ELEMENT_COUNT, pushedDown,
                         "Counting in the repository should find every element this test created");

            assertEquals(ELEMENT_COUNT, retrieved,
                         "Counting by retrieval should find every element this test created.  A count short of "
                                 + "this means the retrieval route stopped paging early; a count over it means it "
                                 + "counted something twice");

            assertEquals(pushedDown, retrieved,
                         "The two counting routes should agree when every matching element is visible to the "
                                 + "caller - they may only differ where the visibility check removes something");

            assertEquals(pushedDown, defaulted,
                         "Omitting pushDown should behave as pushDown=true, which is the documented default - "
                                 + "the parameter must not change the answer for callers that do not supply it");

            /*
             * The same three calls through the Java client, which reaches the same endpoint but builds the
             * request itself.  A client that dropped the flag on the way to the URL would still return the
             * right number here - both routes agree on this data - so what this pins down is that the new
             * client methods exist, are wired to the endpoint, and default the way the REST API does.
             */
            SearchProperties searchProperties = new SearchProperties();

            searchProperties.setConditions(new PropertyHelper().addStringProperty(null,
                                                                                   OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                   runPrefix,
                                                                                   PropertyComparisonOperator.STARTS_WITH));

            QueryOptions queryOptions = new QueryOptions();

            queryOptions.setMetadataElementTypeName(OpenMetadataType.COLLECTION.typeName);

            assertEquals(ELEMENT_COUNT, openMetadataStore.countMetadataElements(searchProperties, null, queryOptions, true),
                         "Counting through the client with pushDown=true should find every element this test created");

            assertEquals(ELEMENT_COUNT, openMetadataStore.countMetadataElements(searchProperties, null, queryOptions, false),
                         "Counting through the client with pushDown=false should find every element this test created");

            assertEquals(ELEMENT_COUNT, openMetadataStore.countMetadataElements(searchProperties, null, queryOptions),
                         "The client call that names no pushDown should behave as pushDown=true");
        }
        finally
        {
            for (String guid : createdGUIDs)
            {
                QueryFvtTestSupport.purgeElement(openMetadataStore, guid);
            }
        }
    }


    /**
     * Count the collections of this run by calling the counting endpoint directly, so that the request
     * parameter under test is actually sent.
     *
     * @param runPrefix qualified name prefix that selects exactly this run's elements
     * @param queryString the pushDown parameter to send, or empty to send none
     * @return the count the server reported
     */
    @SuppressWarnings("unchecked")
    private long countOverRest(String runPrefix,
                               String queryString)
    {
        String url = String.format(COUNT_URL_TEMPLATE,
                                    OMAGPlatformExtension.getPlatformURLRoot(),
                                    OMAGPlatformExtension.SERVER_NAME,
                                    OMAGPlatformExtension.USER_ID) + queryString;

        /*
         * The request body is assembled here rather than through the client's request-body classes so that
         * this test exercises the REST contract as an outside caller sees it - which is where pushDown lives.
         */
        Map<String, Object> propertyCondition = Map.of("class", "PropertyCondition",
                                                        "property", "qualifiedName",
                                                        "operator", "STARTS_WITH",
                                                        "value", Map.of("class", "PrimitiveTypePropertyValue",
                                                                        "typeName", "string",
                                                                        "primitiveTypeCategory", "OM_PRIMITIVE_TYPE_STRING",
                                                                        "primitiveValue", runPrefix));

        Map<String, Object> requestBody = Map.of("class", "FindRequestBody",
                                                  "metadataElementTypeName", OpenMetadataType.COLLECTION.typeName,
                                                  "searchProperties", Map.of("class", "SearchProperties",
                                                                             "matchCriteria", "ALL",
                                                                             "conditions", List.of(propertyCondition)));

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> response = new RestTemplate().postForObject(url,
                                                                        new HttpEntity<>(requestBody, headers),
                                                                        Map.class);

        assertNotNull(response, "The counting endpoint returned no response for "
                              + (queryString.isEmpty() ? "the default pushDown" : queryString));

        assertNull(response.get("exceptionErrorMessage"),
                   "The counting endpoint reported an error for "
                           + (queryString.isEmpty() ? "the default pushDown" : queryString) + ": "
                           + response.get("exceptionErrorMessage"));

        Object count = response.get("count");

        assertNotNull(count, "The counting endpoint returned no count for "
                              + (queryString.isEmpty() ? "the default pushDown" : queryString));

        return ((Number) count).longValue();
    }
}
