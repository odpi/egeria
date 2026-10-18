/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.rest.BooleanResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.ContentStatusFilterRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.ContentStatusSearchString;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.ElementHeaderResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.FilterRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewAttachmentRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.OpenMetadataRootElementResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResultsRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateElementRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ContentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataSetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.SearchKeywordProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.FindPropertyNameProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.FindPropertyNamesProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.MatchCriteria;
import org.odpi.openmetadata.frameworks.openmetadata.search.PrimitiveTypeCategory;
import org.odpi.openmetadata.frameworks.openmetadata.search.PrimitiveTypePropertyValue;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyComparisonOperator;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyCondition;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.omf.rest.FindRequestBody;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ClassificationExplorerElementsFVT covers the Classification Explorer's element retrieval requests (by GUID,
 * unique name, type, complex query, property value, category and content status) and its search keyword
 * requests.  The elements found are this run's data set and authored collection, so a repository holding
 * other runs' elements does not change the results.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClassificationExplorerElementsFVT
{
    private static final ViewServiceClient explorer          = ClassificationExplorerFvtFixtures.classificationExplorer;
    private static final ViewServiceClient collectionManager = new ViewServiceClient(ViewServiceDescription.COLLECTION_MANAGER);

    private static final String DATA_SET_NAME   = ViewServicesFvtTestSupport.qualifiedName("DataSet", "explored");
    private static final String COLLECTION_NAME = ViewServicesFvtTestSupport.qualifiedName("Collection", "explored");
    private static final String CATEGORY        = "category-" + OMAGPlatformExtension.RUN_ID;
    private static final String KEYWORD         = "keyword-" + OMAGPlatformExtension.RUN_ID;

    private static String dataSetGUID       = null;
    private static String collectionGUID    = null;
    private static String searchKeywordGUID = null;


    @Test
    @Order(1)
    void createElements()
    {
        dataSetGUID = ClassificationExplorerFvtFixtures.createDataSet("explored", "Explored data set");

        CollectionProperties properties = new CollectionProperties();
        properties.setQualifiedName(COLLECTION_NAME);
        properties.setDisplayName("Explored collection");
        properties.setCategory(CATEGORY);
        properties.setContentStatus(ContentStatus.DRAFT);

        NewElementRequestBody requestBody = new NewElementRequestBody();
        requestBody.setIsOwnAnchor(true);
        requestBody.setProperties(properties);

        collectionGUID = collectionManager.create("/collections", requestBody);
    }


    /**
     * getRootElementByGUID, retrieveInstanceForGUID, getRootElementByUniqueName and
     * getRootElementGUIDByUniqueName all find the data set.
     */
    @Test
    @Order(2)
    void retrieveByGUIDAndUniqueName()
    {
        OpenMetadataRootElement element = ClassificationExplorerFvtFixtures.readElement(dataSetGUID);

        assertEquals(OpenMetadataType.DATA_SET.typeName, element.getElementHeader().getType().getTypeName());
        assertEquals("Explored data set", assertInstanceOf(DataSetProperties.class, element.getProperties()).getDisplayName());

        ElementHeader header = explorer.post("/guids/" + dataSetGUID, new GetRequestBody(), ElementHeaderResponse.class).getElement();

        assertNotNull(header, "retrieveInstanceForGUID returned nothing");
        assertEquals(dataSetGUID, header.getGUID());
        assertEquals(OpenMetadataType.DATA_SET.typeName, header.getType().getTypeName());

        FindPropertyNameProperties byName = new FindPropertyNameProperties();
        byName.setPropertyValue(DATA_SET_NAME);
        byName.setPropertyName("qualifiedName");

        OpenMetadataRootElement byUniqueName = explorer.post("/elements/by-unique-name", byName, OpenMetadataRootElementResponse.class).getElement();

        assertNotNull(byUniqueName, "getRootElementByUniqueName returned nothing");
        assertEquals(dataSetGUID, byUniqueName.getElementHeader().getGUID());

        FindPropertyNameProperties byDefaultName = new FindPropertyNameProperties();
        byDefaultName.setPropertyValue(DATA_SET_NAME);

        assertEquals(dataSetGUID, explorer.post("/elements/guid-by-unique-name", byDefaultName, GUIDResponse.class).getGUID(),
                     "getRootElementGUIDByUniqueName (qualifiedName is the default property)");
    }


    /**
     * getRootElementsByType, findRootElements, getRootElementsByPropertyValue and findElementsByPropertyValue.
     */
    @Test
    @Order(3)
    void findElements()
    {
        ResultsRequestBody byType = new ResultsRequestBody();
        byType.setMetadataElementTypeName(OpenMetadataType.DATA_SET.typeName);
        byType.setPageSize(0);

        List<String> found = ClassificationExplorerFvtFixtures.guids(explorer.elements("/elements/by-type", byType));

        assertTrue(found.contains(dataSetGUID), "by-type DataSet does not return the data set");
        assertFalse(found.contains(collectionGUID), "by-type DataSet returns a collection");

        PrimitiveTypePropertyValue value = new PrimitiveTypePropertyValue();
        value.setTypeName("string");
        value.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING);
        value.setPrimitiveValue(DATA_SET_NAME);

        PropertyCondition condition = new PropertyCondition();
        condition.setProperty("qualifiedName");
        condition.setOperator(PropertyComparisonOperator.EQ);
        condition.setValue(value);

        SearchProperties searchProperties = new SearchProperties();
        searchProperties.setConditions(List.of(condition));
        searchProperties.setMatchCriteria(MatchCriteria.ALL);

        FindRequestBody complexQuery = new FindRequestBody();
        complexQuery.setSearchProperties(searchProperties);

        assertEquals(List.of(dataSetGUID), ClassificationExplorerFvtFixtures.guids(explorer.elements("/elements/by-complex-query", complexQuery)),
                     "by-complex-query on the qualified name");

        FindPropertyNamesProperties exact = new FindPropertyNamesProperties();
        exact.setPropertyValue(DATA_SET_NAME);
        exact.setPropertyNames(List.of("qualifiedName"));

        assertEquals(List.of(dataSetGUID), ClassificationExplorerFvtFixtures.guids(explorer.elements("/elements/by-exact-property-value", exact)),
                     "by-exact-property-value on the qualified name");

        FindPropertyNamesProperties search = new FindPropertyNamesProperties();
        search.setPropertyValue(ViewServicesFvtTestSupport.runSearchString());
        search.setPropertyNames(List.of("qualifiedName"));

        found = ClassificationExplorerFvtFixtures.guids(explorer.elements("/elements/by-property-value-search", search));

        assertTrue(found.contains(dataSetGUID), "by-property-value-search does not return the data set");
        assertTrue(found.contains(collectionGUID), "by-property-value-search does not return the collection");
    }


    /**
     * getElementsByCategory, findAuthoredElements and getAuthoredElementsByCategory find the authored collection.
     */
    @Test
    @Order(4)
    void categoryAndAuthoredElements()
    {
        FilterRequestBody byCategory = new FilterRequestBody();
        byCategory.setFilter(CATEGORY);

        assertEquals(List.of(collectionGUID), ClassificationExplorerFvtFixtures.guids(explorer.elements("/elements/by-category", byCategory)),
                     "by-category");

        ContentStatusSearchString authored = new ContentStatusSearchString();
        authored.setSearchString(ViewServicesFvtTestSupport.runSearchString());
        authored.setContentStatusList(List.of(ContentStatus.DRAFT));

        List<String> found = ClassificationExplorerFvtFixtures.guids(explorer.elements("/authored-elements/by-search-string", authored));

        assertTrue(found.contains(collectionGUID), "authored-elements/by-search-string does not return the draft collection");
        assertFalse(found.contains(dataSetGUID), "authored-elements/by-search-string returns a data set, which is not authored");

        ContentStatusSearchString approved = new ContentStatusSearchString();
        approved.setSearchString(ViewServicesFvtTestSupport.runSearchString());
        approved.setContentStatusList(List.of(ContentStatus.APPROVED));

        assertFalse(ClassificationExplorerFvtFixtures.guids(explorer.elements("/authored-elements/by-search-string", approved)).contains(collectionGUID),
                    "authored-elements/by-search-string returns a draft collection for APPROVED");

        ContentStatusFilterRequestBody authoredByCategory = new ContentStatusFilterRequestBody();
        authoredByCategory.setFilter(CATEGORY);
        authoredByCategory.setContentStatusList(List.of(ContentStatus.DRAFT));

        assertEquals(List.of(collectionGUID), ClassificationExplorerFvtFixtures.guids(explorer.elements("/authored-elements/by-category", authoredByCategory)),
                     "authored-elements/by-category");
    }


    /**
     * addSearchKeywordToElement, getSearchKeywordByGUID, getSearchKeywordByKeyword, findSearchKeywords,
     * updateSearchKeyword and removeSearchKeywordFromElement.
     */
    @Test
    @Order(5)
    void searchKeywords()
    {
        SearchKeywordProperties properties = new SearchKeywordProperties();
        properties.setDisplayName(KEYWORD);
        properties.setDescription("Keyword for the explored data set.");

        NewAttachmentRequestBody requestBody = new NewAttachmentRequestBody();
        requestBody.setProperties(properties);

        searchKeywordGUID = explorer.create("/elements/" + dataSetGUID + "/search-keywords", requestBody);

        assertEquals(KEYWORD, keyword().getDisplayName());
        assertEquals(List.of(searchKeywordGUID),
                     ClassificationExplorerFvtFixtures.relatedGUIDs(dataSetGUID, OpenMetadataType.SEARCH_KEYWORD_LINK_RELATIONSHIP.typeName),
                     "the keyword is not linked to the data set");

        FilterRequestBody byKeyword = new FilterRequestBody();
        byKeyword.setFilter(KEYWORD);

        assertEquals(List.of(searchKeywordGUID), ClassificationExplorerFvtFixtures.guids(explorer.elements("/search-keywords/by-keyword", byKeyword)),
                     "search-keywords/by-keyword");

        SearchStringRequestBody search = new SearchStringRequestBody();
        search.setSearchString(OMAGPlatformExtension.RUN_ID);

        assertTrue(ClassificationExplorerFvtFixtures.guids(explorer.elements("/search-keywords/by-search-string", search)).contains(searchKeywordGUID),
                   "search-keywords/by-search-string");

        SearchKeywordProperties newProperties = new SearchKeywordProperties();
        newProperties.setDisplayName(KEYWORD + "-updated");

        UpdateElementRequestBody update = new UpdateElementRequestBody();
        update.setMergeUpdate(true);
        update.setProperties(newProperties);

        assertTrue(explorer.post("/search-keywords/" + searchKeywordGUID + "/update", update, BooleanResponse.class).getFlag(),
                   "updateSearchKeyword reported no change");

        SearchKeywordProperties updated = keyword();

        assertEquals(KEYWORD + "-updated", updated.getDisplayName());
        assertEquals("Keyword for the explored data set.", updated.getDescription(), "a merge update should keep the description");

        explorer.call("/search-keywords/" + searchKeywordGUID + "/remove", new DeleteElementRequestBody());

        ViewServicesFvtTestSupport.assertGone(explorer, "/search-keywords/" + searchKeywordGUID + "/retrieve");
        assertTrue(ClassificationExplorerFvtFixtures.relatedGUIDs(dataSetGUID, OpenMetadataType.SEARCH_KEYWORD_LINK_RELATIONSHIP.typeName).isEmpty(),
                   "the keyword is still linked to the data set");
    }


    @AfterAll
    static void deleteElements()
    {
        ClassificationExplorerFvtFixtures.deleteAsset(dataSetGUID);

        if (collectionGUID != null)
        {
            collectionManager.call("/collections/" + collectionGUID + "/delete", new DeleteElementRequestBody());
            ViewServicesFvtTestSupport.assertGone(collectionManager, "/collections/" + collectionGUID + "/retrieve");
        }
    }


    private static SearchKeywordProperties keyword()
    {
        OpenMetadataRootElement element = explorer.element("/search-keywords/" + searchKeywordGUID + "/retrieve", new GetRequestBody());

        assertNotNull(element, "search keyword " + searchKeywordGUID + " is missing");

        return assertInstanceOf(SearchKeywordProperties.class, element.getProperties());
    }
}
