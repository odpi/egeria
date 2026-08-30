/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.queryfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.CollectionClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyComparisonOperator;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.UpdateOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * SpecialCharacterFVT checks that string property values containing characters that are significant to
 * SQL - most notably the single quote in a name such as "Coco Pharmaceutical's Database" - survive a
 * round trip through the PostgreSQL repository connector unchanged, and can still be found by a query.
 * <br>
 * The specific failure this suite guards against is quote doubling on the write path: values are bound
 * into INSERT statements as prepared-statement parameters, so any SQL-literal escaping applied before
 * binding is stored verbatim.  Because nothing reverses it on the read path, the extra quotes are handed
 * back to the caller and accumulate on every subsequent update - "Coco Pharmaceutical's Database" becomes
 * "Coco Pharmaceutical''s Database", then "Coco Pharmaceutical''''s Database", and so on.  The
 * "unchanged across repeated updates" test below is therefore the important one: a single create/read
 * cycle only shows the first doubling, whereas re-storing what was read is what makes the growth obvious.
 * <br>
 * The query tests cover both routes a search string with a quote in it can take through the query
 * builder: an exact property comparison (which embeds the value as a SQL literal, so it does need
 * escaping - correctly, once) and the "contains this string" search used by
 * {@code findMetadataElementsWithString}.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class SpecialCharacterFVT
{
    /**
     * Appended to the assertions that check a search found nothing.  Null is the end of a result set;
     * an empty list means only that this batch was filtered out, so a caller that honours the paging
     * contract keeps asking.  Accepting either here would let a regression from one to the other -
     * which would make every such caller page for ever - pass unnoticed.
     */
    private static final String NOTHING_MEANS_NULL =
            ".  A search that matches nothing returns null, not an empty list";


    /**
     * A name with a single quote (apostrophe) in it - the character that has to be escaped when it is
     * embedded in a SQL literal, and must not be escaped when it is bound as a statement parameter.
     */
    private static final String QUOTED_DISPLAY_NAME = "Coco Pharmaceutical's Database";

    /**
     * A value with several of the characters that matter to the query builder: a quote, a percent sign
     * and an underscore (both SQL LIKE wildcards) and a backslash (the LIKE escape character).
     */
    private static final String AWKWARD_DESCRIPTION = "100% of Coco's data_lake \\ everything else";

    /**
     * Message identifier the connector reports when a value contains a null (U+0000) character.
     */
    private static final String NULL_CHARACTER_MESSAGE_ID = "JDBC-RESOURCE-CONNECTOR-400-004";

    private static final int UPDATE_CYCLES = 3;


    /**
     * Create an element whose displayName and qualifiedName both contain a single quote, then read it
     * back and re-store it several times over, checking after every cycle that neither value has grown
     * any extra quotes.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void singleQuotesSurviveRepeatedUpdates() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        CollectionClient     collectionClient  = connectorContext.getCollectionClient();
        OpenMetadataStore    openMetadataStore = connectorContext.getOpenMetadataStore();
        PropertyHelper       propertyHelper    = new PropertyHelper();

        String qualifiedName = QueryFvtTestSupport.newQualifiedName("SpecialCharacter") + ":Coco Pharmaceutical's Collection";

        NewElementOptions newElementOptions = new NewElementOptions();
        newElementOptions.setIsOwnAnchor(true);

        CollectionProperties createProperties = new CollectionProperties();
        createProperties.setQualifiedName(qualifiedName);
        createProperties.setDisplayName(QUOTED_DISPLAY_NAME);
        createProperties.setDescription(AWKWARD_DESCRIPTION);

        String elementGUID = collectionClient.createCollection(newElementOptions, null, createProperties, null);

        try
        {
            OpenMetadataElement createdElement = openMetadataStore.getMetadataElementByGUID(elementGUID, new GetOptions());

            assertNotNull(createdElement, "The newly created element should be retrievable by GUID");
            assertEquals(QUOTED_DISPLAY_NAME, getStringProperty(propertyHelper, createdElement, OpenMetadataProperty.DISPLAY_NAME.name),
                         "A displayName containing a single quote should be stored and returned unchanged");
            assertEquals(qualifiedName, getStringProperty(propertyHelper, createdElement, OpenMetadataProperty.QUALIFIED_NAME.name),
                         "A qualifiedName containing a single quote should be stored and returned unchanged");
            assertEquals(AWKWARD_DESCRIPTION, getStringProperty(propertyHelper, createdElement, OpenMetadataProperty.DESCRIPTION.name),
                         "A description containing quote, wildcard and escape characters should be stored and returned unchanged");

            // Re-store exactly what was read back, several times over.  This is what a caller that
            // refreshes an element and saves it again does, and it is what turns a single doubling of
            // each quote into unbounded growth.
            for (int cycle = 1; cycle <= UPDATE_CYCLES; cycle++)
            {
                OpenMetadataElement currentElement = openMetadataStore.getMetadataElementByGUID(elementGUID, new GetOptions());

                CollectionProperties updateProperties = new CollectionProperties();
                updateProperties.setQualifiedName(getStringProperty(propertyHelper, currentElement, OpenMetadataProperty.QUALIFIED_NAME.name));
                updateProperties.setDisplayName(getStringProperty(propertyHelper, currentElement, OpenMetadataProperty.DISPLAY_NAME.name));
                updateProperties.setDescription(getStringProperty(propertyHelper, currentElement, OpenMetadataProperty.DESCRIPTION.name));

                collectionClient.updateCollection(elementGUID, new UpdateOptions(), updateProperties);

                OpenMetadataElement updatedElement = openMetadataStore.getMetadataElementByGUID(elementGUID, new GetOptions());

                assertEquals(QUOTED_DISPLAY_NAME, getStringProperty(propertyHelper, updatedElement, OpenMetadataProperty.DISPLAY_NAME.name),
                             "displayName should be unchanged after update cycle " + cycle + " - no quotes added or removed");
                assertEquals(qualifiedName, getStringProperty(propertyHelper, updatedElement, OpenMetadataProperty.QUALIFIED_NAME.name),
                             "qualifiedName should be unchanged after update cycle " + cycle + " - no quotes added or removed");
                assertEquals(AWKWARD_DESCRIPTION, getStringProperty(propertyHelper, updatedElement, OpenMetadataProperty.DESCRIPTION.name),
                             "description should be unchanged after update cycle " + cycle + " - no characters added or removed");
            }
        }
        finally
        {
            QueryFvtTestSupport.purgeElement(openMetadataStore, elementGUID);
        }
    }


    /**
     * Check that a value containing a single quote can still be found - both by an exact property
     * comparison and by the "contains this string" search - using the unescaped value the caller
     * originally supplied.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void singleQuotedValuesCanBeFound() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        CollectionClient     collectionClient  = connectorContext.getCollectionClient();
        OpenMetadataStore    openMetadataStore = connectorContext.getOpenMetadataStore();
        PropertyHelper       propertyHelper    = new PropertyHelper();

        String runPrefix     = QueryFvtTestSupport.newQualifiedName("SpecialCharacterSearch");
        String qualifiedName = runPrefix + ":Coco Pharmaceutical's Collection";

        NewElementOptions newElementOptions = new NewElementOptions();
        newElementOptions.setIsOwnAnchor(true);

        CollectionProperties createProperties = new CollectionProperties();
        createProperties.setQualifiedName(qualifiedName);
        createProperties.setDisplayName(QUOTED_DISPLAY_NAME);

        String elementGUID = collectionClient.createCollection(newElementOptions, null, createProperties, null);

        try
        {
            // Exact match on the quoted displayName.  The query builder embeds this value as a SQL
            // literal, so it does have to escape the quote - exactly once.
            SearchProperties exactMatchProperties = new SearchProperties();
            exactMatchProperties.setConditions(propertyHelper.addStringProperty(null,
                                                                               OpenMetadataProperty.DISPLAY_NAME.name,
                                                                               QUOTED_DISPLAY_NAME,
                                                                               PropertyComparisonOperator.EQ));

            QueryOptions queryOptions = new QueryOptions();
            queryOptions.setMetadataElementTypeName(OpenMetadataType.COLLECTION.typeName);
            queryOptions.setPageSize(QueryFvtTestSupport.MAX_PAGE_SIZE);

            List<OpenMetadataElement> exactMatchResults = openMetadataStore.findMetadataElements(exactMatchProperties, null, queryOptions);

            assertNotNull(exactMatchResults, "An exact match on a displayName containing a single quote should find the element");
            assertEquals(1, countMatchingGUIDs(exactMatchResults, elementGUID),
                         "An exact match on a displayName containing a single quote should find exactly the element this test created");

            // Exact match on the quoted qualifiedName, this time through the unique-name lookup, which
            // reads the value from its own dedicated column rather than the attribute table.
            OpenMetadataElement elementByUniqueName = openMetadataStore.getMetadataElementByUniqueName(qualifiedName,
                                                                                                      OpenMetadataProperty.QUALIFIED_NAME.name);

            assertNotNull(elementByUniqueName, "A qualifiedName containing a single quote should be usable as a unique name lookup key");
            assertEquals(elementGUID, elementByUniqueName.getElementGUID(),
                         "The unique name lookup should return the element this test created");

            // "Contains this string" search for a fragment that includes the quote.
            List<OpenMetadataElement> searchStringResults = openMetadataStore.findMetadataElementsWithString("Pharmaceutical's Database",
                                                                                                            OpenMetadataType.COLLECTION.typeName,
                                                                                                            0,
                                                                                                            QueryFvtTestSupport.MAX_PAGE_SIZE);

            assertNotNull(searchStringResults, "A search string containing a single quote should find the element");
            assertEquals(1, countMatchingGUIDs(searchStringResults, elementGUID),
                         "A search string containing a single quote should find exactly the element this test created");
        }
        finally
        {
            QueryFvtTestSupport.purgeElement(openMetadataStore, elementGUID);
        }
    }


    /**
     * Check that the SQL LIKE wildcards ({@code %} and {@code _}) and the LIKE escape character
     * ({@code \}) are all treated as ordinary characters when they appear in the value being searched
     * for, rather than changing what the pattern means (or making it invalid).
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void wildcardAndEscapeCharactersAreTreatedAsLiterals() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        CollectionClient     collectionClient  = connectorContext.getCollectionClient();
        OpenMetadataStore    openMetadataStore = connectorContext.getOpenMetadataStore();
        PropertyHelper       propertyHelper    = new PropertyHelper();

        String runPrefix = QueryFvtTestSupport.newQualifiedName("SpecialCharacterWildcard");

        // A run tag with no punctuation in it, used to keep every search below anchored to this run's
        // own elements - the loaded archives contain plenty of other content that could otherwise match.
        String runTag = "SCW" + java.util.UUID.randomUUID().toString().replace("-", "");

        String wildcardName  = runTag + " 100% pure_data";
        String decoyName     = runTag + " 100X pureXdata";
        String backslashName = runTag + " windows path C:\\temp\\";

        String wildcardGUID  = createCollection(collectionClient, runPrefix + ":wildcard", wildcardName);
        String decoyGUID     = null;
        String backslashGUID = null;

        try
        {
            // Identical to wildcardName except that every wildcard character is replaced by an ordinary
            // one: it must NOT be found by a search for the literal value, but would be if "%" and "_"
            // were passed through to the SQL pattern unescaped.
            decoyGUID     = createCollection(collectionClient, runPrefix + ":decoy", decoyName);
            backslashGUID = createCollection(collectionClient, runPrefix + ":backslash", backslashName);

            List<OpenMetadataElement> wildcardResults = findByDisplayName(openMetadataStore,
                                                                         propertyHelper,
                                                                         "100% pure_data",
                                                                         PropertyComparisonOperator.LIKE);

            assertEquals(1, countMatchingGUIDs(wildcardResults, wildcardGUID),
                         "A LIKE search for a value containing % and _ should find the element that literally contains them");
            assertEquals(0, countMatchingGUIDs(wildcardResults, decoyGUID),
                         "% and _ in the search value must not act as SQL wildcards and match the decoy element");

            // A backslash is the LIKE escape character, so it too has to be escaped before it reaches
            // the pattern - otherwise it silently swallows the character that follows it.
            List<OpenMetadataElement> backslashResults = findByDisplayName(openMetadataStore,
                                                                           propertyHelper,
                                                                           "path C:\\temp",
                                                                           PropertyComparisonOperator.LIKE);

            assertEquals(1, countMatchingGUIDs(backslashResults, backslashGUID),
                         "A LIKE search for a value containing a backslash should find the element that contains it");

            // An ENDS_WITH search puts the value at the very end of the pattern, so a value that ends
            // with a backslash leaves the pattern ending in a lone escape character - which PostgreSQL
            // rejects outright ("LIKE pattern must not end with escape character") rather than simply
            // failing to match.
            List<OpenMetadataElement> endsWithResults = findByDisplayName(openMetadataStore,
                                                                          propertyHelper,
                                                                          "C:\\temp\\",
                                                                          PropertyComparisonOperator.ENDS_WITH);

            assertEquals(1, countMatchingGUIDs(endsWithResults, backslashGUID),
                         "An ENDS_WITH search for a value ending in a backslash should find the element, not fail the query");

            // The same characters, through the "contains this string" search rather than a property
            // comparison - a different route into the query builder.
            List<OpenMetadataElement> searchStringResults = openMetadataStore.findMetadataElementsWithString("path C:\\temp\\",
                                                                                                            OpenMetadataType.COLLECTION.typeName,
                                                                                                            0,
                                                                                                            QueryFvtTestSupport.MAX_PAGE_SIZE);

            assertEquals(1, countMatchingGUIDs(searchStringResults, backslashGUID),
                         "A search string containing backslashes should find the element that contains them");
        }
        finally
        {
            QueryFvtTestSupport.purgeElement(openMetadataStore, wildcardGUID);

            if (decoyGUID != null)
            {
                QueryFvtTestSupport.purgeElement(openMetadataStore, decoyGUID);
            }

            if (backslashGUID != null)
            {
                QueryFvtTestSupport.purgeElement(openMetadataStore, backslashGUID);
            }
        }
    }


    /**
     * Check that a property *name* containing a single quote is handled as data rather than being
     * pasted straight into the SQL.  No such property exists, so the query should simply return nothing
     * - what it must not do is fail with a SQL syntax error.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void quotedPropertyNameDoesNotBreakTheQuery() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        OpenMetadataStore    openMetadataStore = connectorContext.getOpenMetadataStore();
        PropertyHelper       propertyHelper    = new PropertyHelper();

        SearchProperties searchProperties = new SearchProperties();
        searchProperties.setConditions(propertyHelper.addStringProperty(null,
                                                                       "Coco Pharmaceutical's property",
                                                                       "any value",
                                                                       PropertyComparisonOperator.EQ));

        QueryOptions queryOptions = new QueryOptions();
        queryOptions.setMetadataElementTypeName(OpenMetadataType.COLLECTION.typeName);
        queryOptions.setPageSize(QueryFvtTestSupport.MAX_PAGE_SIZE);

        List<OpenMetadataElement> results = openMetadataStore.findMetadataElements(searchProperties, null, queryOptions);

        assertNull(results,
                   "A search for a property name that does not exist should return nothing" + NOTHING_MEANS_NULL);
    }


    /**
     * Check that a value containing a null (U+0000) character is rejected with an FFDC message that
     * names the offending column, rather than being passed down to the database - which rejects the
     * whole statement with an error that says nothing about which property was at fault.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void nullCharacterIsRejectedWithAUsefulMessage() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        CollectionClient     collectionClient  = connectorContext.getCollectionClient();
        OpenMetadataStore    openMetadataStore = connectorContext.getOpenMetadataStore();

        String qualifiedName = QueryFvtTestSupport.newQualifiedName("SpecialCharacterNullChar");
        String displayName   = "Coco Pharmaceuticals " + (char) 0 + " Database";

        Exception caughtException = assertThrows(Exception.class,
                                                 () -> createCollection(collectionClient, qualifiedName, displayName),
                                                 "Storing a value containing a null character should fail rather than silently truncating it");

        assertTrue((caughtException.getMessage() != null) && (caughtException.getMessage().contains(NULL_CHARACTER_MESSAGE_ID)),
                   "The failure should be reported as " + NULL_CHARACTER_MESSAGE_ID + ", not as a raw SQL error.  Actual message: " +
                           caughtException.getMessage());

        // The rejected element must not have been left half-created.
        OpenMetadataElement leftoverElement = openMetadataStore.getMetadataElementByUniqueName(qualifiedName,
                                                                                              OpenMetadataProperty.QUALIFIED_NAME.name);

        assertNull(leftoverElement, "A create that was rejected for a null character should not leave an element behind");
    }


    /**
     * Create a collection with the supplied qualified name and display name.
     *
     * @param collectionClient client to create through
     * @param qualifiedName unique name for the new collection
     * @param displayName display name for the new collection
     * @return unique identifier of the new collection
     * @throws Exception problem creating the collection
     */
    private String createCollection(CollectionClient collectionClient,
                                    String           qualifiedName,
                                    String           displayName) throws Exception
    {
        NewElementOptions newElementOptions = new NewElementOptions();
        newElementOptions.setIsOwnAnchor(true);

        CollectionProperties collectionProperties = new CollectionProperties();
        collectionProperties.setQualifiedName(qualifiedName);
        collectionProperties.setDisplayName(displayName);

        return collectionClient.createCollection(newElementOptions, null, collectionProperties, null);
    }


    /**
     * Run a displayName property comparison with the supplied operator.
     *
     * @param openMetadataStore store to query
     * @param propertyHelper helper to build the search condition
     * @param displayNameValue value to compare against
     * @param operator how to compare it
     * @return query results
     * @throws Exception problem running the query
     */
    private List<OpenMetadataElement> findByDisplayName(OpenMetadataStore          openMetadataStore,
                                                        PropertyHelper             propertyHelper,
                                                        String                     displayNameValue,
                                                        PropertyComparisonOperator operator) throws Exception
    {
        SearchProperties searchProperties = new SearchProperties();
        searchProperties.setConditions(propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.DISPLAY_NAME.name,
                                                                        displayNameValue,
                                                                        operator));

        QueryOptions queryOptions = new QueryOptions();
        queryOptions.setMetadataElementTypeName(OpenMetadataType.COLLECTION.typeName);
        queryOptions.setPageSize(QueryFvtTestSupport.MAX_PAGE_SIZE);

        return openMetadataStore.findMetadataElements(searchProperties, null, queryOptions);
    }


    /**
     * Count how many of the supplied results are the element this test created.  Other elements may
     * legitimately match (the loaded content-packs archives are full of Coco Pharmaceuticals content),
     * so the assertions above check for this test's own element rather than for an exact result count.
     *
     * @param results results returned by a query
     * @param elementGUID unique identifier of the element the test created
     * @return number of results with this GUID
     */
    private int countMatchingGUIDs(List<OpenMetadataElement> results,
                                   String                    elementGUID)
    {
        int matchCount = 0;

        if (results != null)
        {
            for (OpenMetadataElement result : results)
            {
                if ((result != null) && (elementGUID.equals(result.getElementGUID())))
                {
                    matchCount++;
                }
            }
        }

        return matchCount;
    }


    /**
     * Extract a named string property from an element's properties.
     *
     * @param propertyHelper helper to unpack the property value
     * @param element element returned by the repository
     * @param propertyName name of the property to extract
     * @return property value
     */
    private String getStringProperty(PropertyHelper      propertyHelper,
                                     OpenMetadataElement element,
                                     String              propertyName)
    {
        return propertyHelper.getStringProperty("query-fvt",
                                                propertyName,
                                                element.getElementProperties(),
                                                "getStringProperty");
    }
}
