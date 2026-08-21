/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.typefvt;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationshipDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationshipEndDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDefAttribute;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.MakeAnchorOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * RelationshipTypeFVT works through every relationship in the model, one test case per type: it creates an
 * entity of each end's declared type, links them with every attribute the relationship declares populated,
 * reads the relationship back and checks the properties arrived intact, then unlinks it.
 * <br>
 * Because the end types come from the relationship's own {@code endDef1} and {@code endDef2}, this also
 * quietly proves that both ends name types that can actually hold an instance - a relationship whose end
 * points at something uncreatable is unusable in practice however well-formed it looks in the archive.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class RelationshipTypeFVT
{
    /**
     * Every relationship type in the model.
     *
     * @return type names
     * @throws Exception problem talking to the server
     */
    static List<String> relationshipTypeNames() throws Exception
    {
        return TypeCatalog.relationshipTypeNames();
    }


    /**
     * Link two entities with this relationship, read it back, check its properties, then unlink it.
     *
     * @param relationshipTypeName type under test
     * @throws Exception any failure - which is the finding
     */
    @ParameterizedTest(name = "{0}")
    @MethodSource("relationshipTypeNames")
    void relationshipTypeRoundTrips(String relationshipTypeName) throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        OpenMetadataStore    openMetadataStore = connectorContext.getOpenMetadataStore();
        OpenMetadataTypeDef  typeDef           = TypeCatalog.typeDefinition(relationshipTypeName);

        assertNotNull(typeDef, "Type " + relationshipTypeName + " disappeared from the server between listing and use");
        assertTrue(typeDef instanceof OpenMetadataRelationshipDef,
                   relationshipTypeName + " is listed as a relationship but the server reports it as "
                           + typeDef.getClass().getSimpleName());

        OpenMetadataRelationshipDef relationshipDef = (OpenMetadataRelationshipDef) typeDef;

        String end1TypeName = endTypeName(relationshipTypeName, "endDef1", relationshipDef.getEndDef1());
        String end2TypeName = endTypeName(relationshipTypeName, "endDef2", relationshipDef.getEndDef2());

        List<OpenMetadataTypeDefAttribute> attributes    = TypeCatalog.instantiableAttributes(typeDef);
        String                             qualifiedName = TypeFvtTestSupport.newQualifiedName(relationshipTypeName);
        TypeValueFactory                   valueFactory  = new TypeValueFactory(attributes, qualifiedName);

        assertTrue(valueFactory.getUnsupportedAttributes().isEmpty(),
                   relationshipTypeName + " declares attributes this suite cannot populate: "
                           + valueFactory.getUnsupportedAttributes());

        String end1GUID         = null;
        String end2GUID         = null;
        String relationshipGUID = null;

        try
        {
            end1GUID = TypeFvtTestSupport.createPlainElement(openMetadataStore, end1TypeName, qualifiedName + ":end1");
            end2GUID = TypeFvtTestSupport.createPlainElement(openMetadataStore, end2TypeName, qualifiedName + ":end2");

            NewElementProperties relationshipProperties = (valueFactory.getElementProperties() == null)
                                                                  ? null
                                                                  : new NewElementProperties(valueFactory.getElementProperties());

            relationshipGUID = openMetadataStore.createRelatedElementsInStore(relationshipTypeName,
                                                                               end1GUID,
                                                                               end2GUID,
                                                                               new MakeAnchorOptions(),
                                                                               relationshipProperties);

            assertNotNull(relationshipGUID, "Linking a " + end1TypeName + " to a " + end2TypeName + " with "
                                  + relationshipTypeName + " returned no GUID");

            OpenMetadataRelationship relationship = openMetadataStore.getRelationshipByGUID(relationshipGUID);

            assertNotNull(relationship, relationshipTypeName + " could not be read back after being created");
            assertEquals(relationshipTypeName,
                         relationship.getType().getTypeName(),
                         relationshipTypeName + " was created but came back as a different type");

            assertPropertiesRoundTripped(relationshipTypeName,
                                          valueFactory.getExpectedValues(),
                                          relationship.getRelationshipProperties());
        }
        finally
        {
            if (relationshipGUID != null)
            {
                TypeFvtTestSupport.purgeRelationship(openMetadataStore, relationshipGUID);
            }

            if (end1GUID != null)
            {
                TypeFvtTestSupport.purgeElement(openMetadataStore, end1GUID);
            }

            if (end2GUID != null)
            {
                TypeFvtTestSupport.purgeElement(openMetadataStore, end2GUID);
            }
        }
    }


    /**
     * Return the entity type name for one end, failing the test with a clear message if the end is not
     * usable.
     *
     * @param relationshipTypeName relationship under test
     * @param endName which end
     * @param endDef end definition
     * @return entity type name
     */
    private static String endTypeName(String                         relationshipTypeName,
                                      String                         endName,
                                      OpenMetadataRelationshipEndDef endDef)
    {
        assertNotNull(endDef, relationshipTypeName + " has no " + endName);
        assertNotNull(endDef.getEntityType(), relationshipTypeName + "." + endName + " names no entity type");

        return endDef.getEntityType().getName();
    }


    /**
     * Check that every relationship property that was sent came back with the same value.
     *
     * @param relationshipTypeName relationship being checked
     * @param expectedValues property name to expected string form
     * @param actualProperties properties as read back
     */
    private static void assertPropertiesRoundTripped(String              relationshipTypeName,
                                                     Map<String, String> expectedValues,
                                                     ElementProperties   actualProperties)
    {
        if (expectedValues.isEmpty())
        {
            return;
        }

        assertNotNull(actualProperties, relationshipTypeName + " was created with properties but came back with none");

        Map<String, String> actualValues = actualProperties.getPropertiesAsStrings();

        assertNotNull(actualValues, relationshipTypeName + " was created with properties but came back with none");

        for (Map.Entry<String, String> expected : expectedValues.entrySet())
        {
            assertEquals(expected.getValue(),
                         actualValues.get(expected.getKey()),
                         relationshipTypeName + ": property '" + expected.getKey() + "' did not survive the round trip");
        }
    }
}
