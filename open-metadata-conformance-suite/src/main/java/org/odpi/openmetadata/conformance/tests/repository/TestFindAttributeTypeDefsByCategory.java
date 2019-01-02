/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Test that all attribute type definitions are retrieved by findAttributeTypeDefsByCategory.
 */
public class TestFindAttributeTypeDefsByCategory extends OpenMetadataRepositoryTestCase
{
    private static final String testUserId = "ConformanceTestUser";

    private static final String testCaseId = "repository-find-attribute-typedefs-by-category";
    private static final String testCaseName = "Repository find attribute type definitions by category test case";

    private static final String assertion1     = testCaseId + "-01";
    private static final String assertionMsg1  = "All attribute type definitions returned by category.";

    private static final  String discoveredProperty_numberOfPrimitiveDefs  = "Number of supported PrimitiveDefs";
    private static final  String discoveredProperty_primitiveDefs          = "Supported PrimitiveDefs";
    private static final  String discoveredProperty_numberOfCollectionDefs = "Number of supported CollectionDefs";
    private static final  String discoveredProperty_collectionDefs         = "Supported CollectionDefs";
    private static final  String discoveredProperty_numberOfEnumDefs       = "Number of supported EnumDefs";
    private static final  String discoveredProperty_enumDefs               = "Supported EnumDefs";

    private List<AttributeTypeDef>    allAttributeTypeDefs;
    private Map<String, PrimitiveDef> primitiveDefs  = new HashMap<>();
    private List<CollectionDef>       collectionDefs = new ArrayList<>();
    private List<EnumDef>             enumDefs       = new ArrayList<>();


    /**
     * Typical constructor sets up superclass
     *
     * @param workbenchId identifier of calling workbench
     * @param allAttributeTypeDefs list of all type definitions returned by repository
     */
    TestFindAttributeTypeDefsByCategory(String                 workbenchId,
                                        List<AttributeTypeDef> allAttributeTypeDefs)
    {
        super(workbenchId, testCaseId, testCaseName);
        this.allAttributeTypeDefs = allAttributeTypeDefs;
    }


    /**
     * Return the list of primitive type definitions returned by the repository.
     *
     * @return map of PrimitiveDef name to PrimitiveDefs
     */
    public Map<String, PrimitiveDef> getPrimitiveDefs()
    {
        if (primitiveDefs.isEmpty())
        {
            return null;
        }
        else
        {
            return primitiveDefs;
        }
    }


    /**
     * Return the list of collection type definitions returned by the repository.
     *
     * @return list of CollectionDefs
     */
    public List<CollectionDef> getCollectionDefs()
    {
        if (collectionDefs.isEmpty())
        {
            return null;
        }
        else
        {
            return collectionDefs;
        }
    }


    /**
     * Return the list of enum type definitions returned by the repository.
     *
     * @return list of EnumDefs
     */
    public List<EnumDef> getEnumDefs()
    {
        if (enumDefs.isEmpty())
        {
            return null;
        }
        else
        {
            return enumDefs;
        }
    }


    /**
     * Method implemented by the actual test case.
     *
     * @throws Exception something went wrong with the test.
     */
    protected void run() throws Exception
    {
        Map<String, Object>    discoveredProperties = new HashMap<>();
        OMRSMetadataCollection metadataCollection = super.getMetadataCollection();

        List<AttributeTypeDef>  typeDefGallery = metadataCollection.findAttributeTypeDefsByCategory(testUserId, AttributeTypeDefCategory.PRIMITIVE);

        if (typeDefGallery == null)
        {
            discoveredProperties.put(discoveredProperty_numberOfPrimitiveDefs, 0);
        }
        else
        {
            discoveredProperties.put(discoveredProperty_numberOfPrimitiveDefs, typeDefGallery.size());

            List<String>    supportedTypes = new ArrayList<>();

            for (AttributeTypeDef   attributeTypeDef : typeDefGallery)
            {
                this.primitiveDefs.put(attributeTypeDef.getName(), (PrimitiveDef) attributeTypeDef);
                supportedTypes.add(attributeTypeDef.getName());
            }

            discoveredProperties.put(discoveredProperty_primitiveDefs, supportedTypes);
        }


        typeDefGallery = metadataCollection.findAttributeTypeDefsByCategory(testUserId, AttributeTypeDefCategory.COLLECTION);

        if (typeDefGallery == null)
        {
            discoveredProperties.put(discoveredProperty_numberOfCollectionDefs, 0);
        }
        else
        {
            discoveredProperties.put(discoveredProperty_numberOfCollectionDefs, typeDefGallery.size());

            List<String>    supportedTypes = new ArrayList<>();

            for (AttributeTypeDef   attributeTypeDef : typeDefGallery)
            {
                this.collectionDefs.add((CollectionDef) attributeTypeDef);
                supportedTypes.add(attributeTypeDef.getName());
            }

            discoveredProperties.put(discoveredProperty_collectionDefs, supportedTypes);
        }


        typeDefGallery = metadataCollection.findAttributeTypeDefsByCategory(testUserId, AttributeTypeDefCategory.ENUM_DEF);

        if (typeDefGallery == null)
        {
            discoveredProperties.put(discoveredProperty_numberOfEnumDefs, 0);
        }
        else
        {
            discoveredProperties.put(discoveredProperty_numberOfEnumDefs, typeDefGallery.size());

            List<String>    supportedTypes = new ArrayList<>();

            for (AttributeTypeDef   attributeTypeDef : typeDefGallery)
            {
                this.enumDefs.add((EnumDef) attributeTypeDef);
                supportedTypes.add(attributeTypeDef.getName());
            }

            discoveredProperties.put(discoveredProperty_enumDefs, supportedTypes);
        }

        assertCondition((primitiveDefs.size() + collectionDefs.size() + enumDefs.size() == allAttributeTypeDefs.size()), assertion1, assertionMsg1);

        super.result.setSuccessMessage("Attribute type definitions can be extracted by category");

        super.result.setDiscoveredProperties(discoveredProperties);
    }
}
