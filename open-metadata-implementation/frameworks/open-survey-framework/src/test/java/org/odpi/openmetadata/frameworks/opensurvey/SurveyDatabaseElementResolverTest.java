/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.opensurvey;

import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementType;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.ResourceMeasureAnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

/**
 * SurveyDatabaseElementResolverTest checks that a database survey annotation is linked to the catalogued
 * database, schema, table or column that it describes, and to the surveyed asset when that element has not
 * been catalogued.  The catalogued elements are held in a fake metadata store that pages its results and,
 * like the real one, returns related elements from either end of a relationship.
 */
public class SurveyDatabaseElementResolverTest
{
    private static final String SERVER_GUID     = "server-guid";
    private static final String CAPABILITY_GUID = "capability-guid";
    private static final String DATABASE_GUID   = "database-guid";
    private static final String SCHEMA_GUID     = "schema-guid";
    private static final String SCHEMA_TYPE_GUID = "schema-type-guid";
    private static final String TABLE_GUID      = "table-guid";

    private final PropertyHelper propertyHelper = new PropertyHelper();

    private OpenMetadataStore                                 openMetadataStore;
    private Map<String, Map<String, List<OpenMetadataElement>>> relationships;


    /**
     * Build the catalogued server: server - capability - database "sales" - schema "public" - table "orders",
     * with 150 columns so that they take two pages to retrieve.
     */
    @BeforeMethod
    public void setUp() throws Exception
    {
        relationships = new HashMap<>();

        OpenMetadataElement server     = element(SERVER_GUID, OpenMetadataType.SOFTWARE_SERVER.typeName, "Server", "pg1");
        OpenMetadataElement capability = element(CAPABILITY_GUID, OpenMetadataType.DATABASE_MANAGER.typeName, OpenMetadataType.SOFTWARE_CAPABILITY.typeName, "pg1:DBMS");
        OpenMetadataElement database   = element(DATABASE_GUID, OpenMetadataType.RELATIONAL_DATABASE.typeName, "Asset", "Sales Database");

        /*
         * Databases are matched on resourceName, which need not match displayName.
         */
        database.setElementProperties(propertyHelper.addStringProperty(database.getElementProperties(), OpenMetadataProperty.RESOURCE_NAME.name, "sales"));
        OpenMetadataElement schema     = element(SCHEMA_GUID, OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName, "Asset", "public");
        OpenMetadataElement schemaType = element(SCHEMA_TYPE_GUID, OpenMetadataType.RELATIONAL_DB_SCHEMA_TYPE.typeName, OpenMetadataType.SCHEMA_TYPE.typeName, "Schema Type for public");
        OpenMetadataElement table      = element(TABLE_GUID, OpenMetadataType.RELATIONAL_TABLE.typeName, "SchemaAttribute", "orders");

        link(server, OpenMetadataType.SUPPORTED_SOFTWARE_CAPABILITY_RELATIONSHIP.typeName, capability);
        link(capability, OpenMetadataType.CAPABILITY_ASSET_USE_RELATIONSHIP.typeName, database);
        link(capability, OpenMetadataType.CAPABILITY_ASSET_USE_RELATIONSHIP.typeName,
             element("hr-guid", OpenMetadataType.RELATIONAL_DATABASE.typeName, "Asset", "hr"));
        link(schema, OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName, database);
        link(schema, OpenMetadataType.SCHEMA_RELATIONSHIP.typeName, schemaType);
        link(schemaType, OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName, table);

        /*
         * An element of the wrong type, with the same name as the schema, is ignored.
         */
        link(database, OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName,
             element("other-guid", OpenMetadataType.DATA_FILE.typeName, "Asset", "public"));

        for (int i = 0; i < 150; i++)
        {
            link(table, OpenMetadataType.NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP.typeName,
                 element("column-guid-" + i, OpenMetadataType.RELATIONAL_COLUMN.typeName, "SchemaAttribute", "c" + i));
        }

        openMetadataStore = mock(OpenMetadataStore.class);

        when(openMetadataStore.getRelatedMetadataElements(anyString(), eq(0), anyString(), anyInt(), anyInt())).thenAnswer(invocation ->
        {
            String guid             = invocation.getArgument(0);
            String relationshipType = invocation.getArgument(2);
            int    startFrom        = invocation.getArgument(3);
            int    pageSize         = invocation.getArgument(4);

            List<OpenMetadataElement> related = relationships.getOrDefault(guid, Map.of()).get(relationshipType);

            if ((related == null) || (startFrom >= related.size()))
            {
                return null;
            }

            List<RelatedMetadataElement> page = new ArrayList<>();

            for (OpenMetadataElement relatedElement : related.subList(startFrom, Math.min(startFrom + pageSize, related.size())))
            {
                RelatedMetadataElement relatedMetadataElement = new RelatedMetadataElement();
                relatedMetadataElement.setElement(relatedElement);
                page.add(relatedMetadataElement);
            }

            RelatedMetadataElementList list = new RelatedMetadataElementList();
            list.setElementList(page);

            return list;
        });
    }


    /**
     * A database survey links each annotation to the catalogued element it describes.
     */
    @Test
    public void testDatabaseSurveyLinksToCataloguedElements() throws Exception
    {
        SurveyDatabaseElementResolver resolver = SurveyDatabaseElementResolver.forDatabase(openMetadataStore, DATABASE_GUID);

        assertEquals(resolver.getDescribedElementGUID(annotation("sales", null, null, null)), DATABASE_GUID);
        assertEquals(resolver.getDescribedElementGUID(annotation("sales", "public", null, null)), SCHEMA_GUID);
        assertEquals(resolver.getDescribedElementGUID(annotation("sales", "public", "orders", null)), TABLE_GUID);
        assertEquals(resolver.getDescribedElementGUID(annotation("sales", "public", "orders", "c0")), "column-guid-0");
        assertEquals(resolver.getDescribedElementGUID(annotation("sales", "public", "orders", "c149")), "column-guid-149");
    }


    /**
     * An annotation for an element that has not been catalogued is linked to the surveyed asset.
     */
    @Test
    public void testUncataloguedElementsLinkToSurveyedAsset() throws Exception
    {
        SurveyDatabaseElementResolver resolver = SurveyDatabaseElementResolver.forDatabase(openMetadataStore, DATABASE_GUID);

        assertEquals(resolver.getDescribedElementGUID(annotation("sales", "public", "orders", "missing")), DATABASE_GUID);
        assertEquals(resolver.getDescribedElementGUID(annotation("sales", "public", "missing", "c0")), DATABASE_GUID);
        assertEquals(resolver.getDescribedElementGUID(annotation("sales", "missing", "orders", "c0")), DATABASE_GUID);
    }


    /**
     * A server survey finds each database by its resourceName - or its displayName if it has no resourceName, as
     * for the "hr" database - and links annotations for databases that have not been catalogued to the server.
     */
    @Test
    public void testServerSurveyResolvesDatabases() throws Exception
    {
        SurveyDatabaseElementResolver resolver = SurveyDatabaseElementResolver.forServer(openMetadataStore, SERVER_GUID);

        assertEquals(resolver.getDescribedElementGUID(annotation("sales", null, null, null)), DATABASE_GUID);
        assertEquals(resolver.getDescribedElementGUID(annotation("sales", "public", "orders", "c7")), "column-guid-7");
        assertEquals(resolver.getDescribedElementGUID(annotation("Sales Database", null, null, null)), SERVER_GUID);
        assertEquals(resolver.getDescribedElementGUID(annotation("hr", null, null, null)), "hr-guid");
        assertEquals(resolver.getDescribedElementGUID(annotation("finance", null, null, null)), SERVER_GUID);
        assertEquals(resolver.getDescribedElementGUID(annotation("finance", "public", "orders", "c7")), SERVER_GUID);
    }


    /**
     * The columns of a table are retrieved once, however many of its column annotations are resolved.
     */
    @Test
    public void testColumnsOfATableAreRetrievedOnce() throws Exception
    {
        SurveyDatabaseElementResolver resolver = SurveyDatabaseElementResolver.forDatabase(openMetadataStore, DATABASE_GUID);

        for (int i = 0; i < 150; i++)
        {
            assertEquals(resolver.getDescribedElementGUID(annotation("sales", "public", "orders", "c" + i)), "column-guid-" + i);
        }

        /*
         * Two pages of columns, retrieved once.
         */
        verify(openMetadataStore, times(2)).getRelatedMetadataElements(eq(TABLE_GUID),
                                                                       eq(0),
                                                                       eq(OpenMetadataType.NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP.typeName),
                                                                       anyInt(),
                                                                       anyInt());
    }


    /**
     * Build an annotation for the named resource.
     */
    private DatabaseElementAnnotation annotation(String databaseName,
                                                 String schemaName,
                                                 String tableName,
                                                 String columnName)
    {
        return new DatabaseElementAnnotation(new ResourceMeasureAnnotationProperties(), databaseName, schemaName, tableName, columnName);
    }


    /**
     * Build a metadata element with a displayName.
     */
    private OpenMetadataElement element(String guid,
                                        String typeName,
                                        String superTypeName,
                                        String displayName)
    {
        ElementType elementType = new ElementType();
        elementType.setTypeName(typeName);
        elementType.setSuperTypeNames(List.of(superTypeName, "Referenceable", "OpenMetadataRoot"));

        OpenMetadataElement element = new OpenMetadataElement();
        element.setElementGUID(guid);
        element.setType(elementType);
        element.setElementProperties(propertyHelper.addStringProperty(null, OpenMetadataProperty.DISPLAY_NAME.name, displayName));

        return element;
    }


    /**
     * Record a relationship so that it can be followed from either end.
     */
    private void link(OpenMetadataElement end1,
                      String              relationshipTypeName,
                      OpenMetadataElement end2)
    {
        relationships.computeIfAbsent(end1.getElementGUID(), k -> new HashMap<>()).computeIfAbsent(relationshipTypeName, k -> new ArrayList<>()).add(end2);
        relationships.computeIfAbsent(end2.getElementGUID(), k -> new HashMap<>()).computeIfAbsent(relationshipTypeName, k -> new ArrayList<>()).add(end1);
    }
}
