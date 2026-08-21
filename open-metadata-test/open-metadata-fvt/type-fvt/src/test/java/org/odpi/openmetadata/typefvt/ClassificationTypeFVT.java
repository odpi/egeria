/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.typefvt;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataClassificationDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDefAttribute;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDefLink;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.MetadataSourceOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ClassificationTypeFVT works through every classification in the model, one test case per type: it creates
 * an entity of a type that classification says it can be attached to, attaches the classification with every
 * attribute it declares populated, reads the entity back and checks the classification arrived intact, then
 * detaches it and checks it is gone.
 * <br>
 * The entity used for each classification is chosen from the classification's own {@code validEntityDefs},
 * so this also exercises whether that list is usable in practice - a classification whose only declared
 * target is a type that cannot be created is a real problem, and it surfaces here rather than the first time
 * somebody tries to use it.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class ClassificationTypeFVT
{
    /**
     * Every classification type in the model.
     *
     * @return type names
     * @throws Exception problem talking to the server
     */
    static List<String> classificationTypeNames() throws Exception
    {
        return TypeCatalog.classificationTypeNames();
    }


    /**
     * Attach this classification to a valid entity with all its attributes populated, read it back, check it,
     * then detach it.
     *
     * @param classificationTypeName type under test
     * @throws Exception any failure - which is the finding
     */
    @ParameterizedTest(name = "{0}")
    @MethodSource("classificationTypeNames")
    void classificationTypeRoundTrips(String classificationTypeName) throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        OpenMetadataStore    openMetadataStore = connectorContext.getOpenMetadataStore();
        OpenMetadataTypeDef  typeDef           = TypeCatalog.typeDefinition(classificationTypeName);

        assertNotNull(typeDef, "Type " + classificationTypeName + " disappeared from the server between listing and use");
        assertTrue(typeDef instanceof OpenMetadataClassificationDef,
                   classificationTypeName + " is listed as a classification but the server reports it as "
                           + typeDef.getClass().getSimpleName());

        String anchorEntityTypeName = firstValidEntityTypeName((OpenMetadataClassificationDef) typeDef);

        assertNotNull(anchorEntityTypeName,
                      classificationTypeName + " declares no entity type it can be attached to, so nothing can ever"
                              + " carry it");

        List<OpenMetadataTypeDefAttribute> attributes    = TypeCatalog.instantiableAttributes(typeDef);
        String                             qualifiedName = TypeFvtTestSupport.newQualifiedName(classificationTypeName);
        TypeValueFactory                   valueFactory  = new TypeValueFactory(attributes, qualifiedName);

        assertTrue(valueFactory.getUnsupportedAttributes().isEmpty(),
                   classificationTypeName + " declares attributes this suite cannot populate: "
                           + valueFactory.getUnsupportedAttributes());

        String elementGUID = null;

        try
        {
            elementGUID = TypeFvtTestSupport.createPlainElement(openMetadataStore, anchorEntityTypeName, qualifiedName);

            // forLineage is set on the write calls as well as the reads: attaching Memento hides the element
            // from an ordinary lookup, and the detach call has to look it up first, so without this the
            // element it just classified would be invisible to the very call that removes the classification.
            MetadataSourceOptions metadataSourceOptions = new MetadataSourceOptions();

            metadataSourceOptions.setForLineage(true);

            NewElementProperties classificationProperties = (valueFactory.getElementProperties() == null)
                                                                    ? null
                                                                    : new NewElementProperties(valueFactory.getElementProperties());

            openMetadataStore.classifyMetadataElementInStore(elementGUID,
                                                              classificationTypeName,
                                                              metadataSourceOptions,
                                                              classificationProperties);

            // forLineage is needed so that an element carrying Memento - which deliberately hides it from an
            // ordinary read - is still visible to the check that the classification arrived.
            GetOptions getOptions = new GetOptions();

            getOptions.setForLineage(true);

            OpenMetadataElement classifiedElement = openMetadataStore.getMetadataElementByGUID(elementGUID, getOptions);

            assertNotNull(classifiedElement, "The " + anchorEntityTypeName + " carrying " + classificationTypeName
                                  + " could not be read back");

            AttachedClassification attachedClassification = findClassification(classifiedElement, classificationTypeName);

            assertNotNull(attachedClassification,
                          classificationTypeName + " was attached to a " + anchorEntityTypeName
                                  + " but is not present on the element when it is read back");

            assertClassificationPropertiesRoundTripped(classificationTypeName,
                                                        valueFactory.getExpectedValues(),
                                                        attachedClassification.getClassificationProperties());

            openMetadataStore.declassifyMetadataElementInStore(elementGUID, classificationTypeName, metadataSourceOptions);

            OpenMetadataElement declassifiedElement = openMetadataStore.getMetadataElementByGUID(elementGUID, getOptions);

            assertNotNull(declassifiedElement, "The " + anchorEntityTypeName + " could not be read back after declassifying");
            assertEquals(null,
                         findClassification(declassifiedElement, classificationTypeName),
                         classificationTypeName + " is still attached after being removed");
        }
        finally
        {
            if (elementGUID != null)
            {
                TypeFvtTestSupport.purgeElement(openMetadataStore, elementGUID);
            }
        }
    }


    /**
     * Return the first entity type this classification can be attached to that this suite is willing to
     * create.
     *
     * @param classificationDef classification under test
     * @return entity type name, or null if there is nothing usable
     * @throws Exception problem talking to the server
     */
    private static String firstValidEntityTypeName(OpenMetadataClassificationDef classificationDef) throws Exception
    {
        List<OpenMetadataTypeDefLink> validEntityDefs = classificationDef.getValidEntityDefs();

        // An empty (or absent) validEntityDefs list means the classification is not restricted to particular
        // entity types, not that there is nowhere to put it - Anchors, Memento and Template are all like
        // this.  Referenceable is used for those: it is the root of everything a caller would realistically
        // classify, and it can be instantiated.
        if ((validEntityDefs == null) || validEntityDefs.isEmpty())
        {
            return OpenMetadataType.REFERENCEABLE.typeName;
        }

        List<String> creatableEntityTypeNames = TypeCatalog.entityTypeNames();

        for (OpenMetadataTypeDefLink validEntityDef : validEntityDefs)
        {
            if ((validEntityDef != null) && creatableEntityTypeNames.contains(validEntityDef.getName()))
            {
                return validEntityDef.getName();
            }
        }

        return null;
    }


    /**
     * Find one classification on an element.
     *
     * @param element element to look at
     * @param classificationName classification to find
     * @return the attached classification, or null if it is not there
     */
    private static AttachedClassification findClassification(OpenMetadataElement element,
                                                             String              classificationName)
    {
        if (element.getClassifications() != null)
        {
            for (AttachedClassification attachedClassification : element.getClassifications())
            {
                if (classificationName.equals(attachedClassification.getClassificationName()))
                {
                    return attachedClassification;
                }
            }
        }

        return null;
    }


    /**
     * Check that every classification property that was sent came back with the same value.
     *
     * @param classificationTypeName classification being checked
     * @param expectedValues property name to expected string form
     * @param actualProperties properties as read back
     */
    private static void assertClassificationPropertiesRoundTripped(String              classificationTypeName,
                                                                   Map<String, String> expectedValues,
                                                                   ElementProperties   actualProperties)
    {
        if (expectedValues.isEmpty())
        {
            return;
        }

        assertNotNull(actualProperties,
                      classificationTypeName + " was attached with properties but came back with none");

        Map<String, String> actualValues = actualProperties.getPropertiesAsStrings();

        assertNotNull(actualValues, classificationTypeName + " was attached with properties but came back with none");

        for (Map.Entry<String, String> expected : expectedValues.entrySet())
        {
            assertEquals(expected.getValue(),
                         actualValues.get(expected.getKey()),
                         classificationTypeName + ": property '" + expected.getKey() + "' did not survive the round trip");
        }
    }
}
