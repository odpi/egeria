/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.typefvt;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDefAttribute;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.UpdateOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * EntityTypeFVT works through every entity type in the model, one test case per type, and proves that an
 * instance of it can be created, read back with every declared attribute intact, updated, and deleted -
 * all through the connector context, exactly as a connector would do it.
 * <br>
 * The type list comes from the server rather than from a hand-written list, so a type added to the model is
 * covered by the next run without anyone remembering to add it here.  A type that cannot be instantiated at
 * all belongs in {@link TypeCatalog}'s exclusion list with a reason; a type that is simply broken should
 * fail here, which is the point.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class EntityTypeFVT
{
    /**
     * Every entity type the suite should be able to create.
     *
     * @return type names
     * @throws Exception problem talking to the server
     */
    static List<String> entityTypeNames() throws Exception
    {
        return TypeCatalog.entityTypeNames();
    }


    /**
     * Create an instance of this entity type with every attribute it declares populated, read it back and
     * check each one survived, change one property and check the change took, then remove it.
     *
     * @param entityTypeName type under test
     * @throws Exception any failure - which is the finding
     */
    @ParameterizedTest(name = "{0}")
    @MethodSource("entityTypeNames")
    void entityTypeRoundTrips(String entityTypeName) throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        OpenMetadataStore    openMetadataStore = connectorContext.getOpenMetadataStore();
        OpenMetadataTypeDef  typeDef           = TypeCatalog.typeDefinition(entityTypeName);

        assertNotNull(typeDef, "Type " + entityTypeName + " disappeared from the server between listing and use");

        List<OpenMetadataTypeDefAttribute> attributes    = TypeCatalog.instantiableAttributes(typeDef);
        String                             qualifiedName = TypeFvtTestSupport.newQualifiedName(entityTypeName);
        TypeValueFactory                   valueFactory  = new TypeValueFactory(attributes, qualifiedName);

        assertTrue(valueFactory.getUnsupportedAttributes().isEmpty(),
                   entityTypeName + " declares attributes this suite cannot populate - the model has a data type"
                           + " nothing here knows how to write: " + valueFactory.getUnsupportedAttributes());

        // Where the type has a qualifiedName, overwrite the generated value with one that identifies this
        // element as the suite's debris - that is what makes the leftover sweep possible.  Set last so it
        // wins over whatever the value factory produced.
        ElementProperties elementProperties = TypeFvtTestSupport.addQualifiedNameIfSupported(valueFactory.getElementProperties(),
                                                                                              typeDef,
                                                                                              qualifiedName);

        String elementGUID = null;

        try
        {
            NewElementOptions newElementOptions = new NewElementOptions();

            newElementOptions.setIsOwnAnchor(true);

            elementGUID = openMetadataStore.createMetadataElementInStore(entityTypeName,
                                                                          newElementOptions,
                                                                          null,
                                                                          new NewElementProperties(elementProperties),
                                                                          null);

            assertNotNull(elementGUID, "Creating a " + entityTypeName + " returned no GUID");

            OpenMetadataElement createdElement = openMetadataStore.getMetadataElementByGUID(elementGUID);

            assertNotNull(createdElement, entityTypeName + " could not be read back after being created");
            assertEquals(entityTypeName, createdElement.getType().getTypeName(),
                         entityTypeName + " was created but came back as a different type");

            assertPropertiesRoundTripped(entityTypeName,
                                          valueFactory.getExpectedValues(),
                                          createdElement.getElementProperties());

            // Update one property and check the change reached the store without disturbing the rest.
            // qualifiedName is used where the type has one, since it is the property this suite controls;
            // the handful of types with no qualifiedName (Like, Rating, SearchKeyword, TranslationDetail)
            // skip the update leg rather than have a second, type-specific way of picking a property.
            ElementProperties updateProperties = TypeFvtTestSupport.addQualifiedNameIfSupported(null,
                                                                                                 typeDef,
                                                                                                 qualifiedName + ":updated");

            if (updateProperties != null)
            {
                UpdateOptions updateOptions = new UpdateOptions();

                updateOptions.setMergeUpdate(true);

                openMetadataStore.updateMetadataElementInStore(elementGUID,
                                                                updateOptions,
                                                                new NewElementProperties(updateProperties));

                OpenMetadataElement updatedElement = openMetadataStore.getMetadataElementByGUID(elementGUID);

                assertNotNull(updatedElement, entityTypeName + " could not be read back after being updated");

                Map<String, String> updatedProperties = updatedElement.getElementProperties().getPropertiesAsStrings();

                assertEquals(qualifiedName + ":updated",
                             updatedProperties.get(OpenMetadataProperty.QUALIFIED_NAME.name),
                             entityTypeName + ": qualifiedName did not take the updated value");

                // A merge update must leave everything it was not asked to change alone.
                assertPropertiesRoundTripped(entityTypeName + " (after merge update)",
                                              withoutQualifiedName(valueFactory.getExpectedValues()),
                                              updatedElement.getElementProperties());
            }
        }
        finally
        {
            if (elementGUID != null)
            {
                TypeFvtTestSupport.purgeElement(openMetadataStore, elementGUID);
            }
        }

        // ... and it is really gone.  A purged element is reported as an unknown GUID rather than as an
        // empty result, so the check is that the lookup is refused, not that it returns null.
        String purgedGUID = elementGUID;

        assertThrows(InvalidParameterException.class,
                     () -> openMetadataStore.getMetadataElementByGUID(purgedGUID),
                     entityTypeName + " is still readable after being purged");
    }


    /**
     * Check that every property that was sent came back with the same value.
     *
     * @param context what is being checked, for the failure message
     * @param expectedValues property name to expected string form
     * @param actualProperties properties as read back
     */
    private static void assertPropertiesRoundTripped(String            context,
                                                     Map<String, String> expectedValues,
                                                     ElementProperties  actualProperties)
    {
        if (expectedValues.isEmpty())
        {
            return;
        }

        assertNotNull(actualProperties, context + " was created with properties but came back with none");

        Map<String, String> actualValues = actualProperties.getPropertiesAsStrings();

        assertNotNull(actualValues, context + " was created with properties but came back with none");

        for (Map.Entry<String, String> expected : expectedValues.entrySet())
        {
            assertEquals(expected.getValue(),
                         actualValues.get(expected.getKey()),
                         context + ": property '" + expected.getKey() + "' did not survive the round trip");
        }
    }


    /**
     * The qualifiedName expectation is dropped before re-checking an updated element, since the update
     * deliberately changed it.
     *
     * @param expectedValues original expectations
     * @return expectations without qualifiedName
     */
    private static Map<String, String> withoutQualifiedName(Map<String, String> expectedValues)
    {
        Map<String, String> reduced = new java.util.LinkedHashMap<>(expectedValues);

        reduced.remove(OpenMetadataProperty.QUALIFIED_NAME.name);

        return reduced;
    }
}
