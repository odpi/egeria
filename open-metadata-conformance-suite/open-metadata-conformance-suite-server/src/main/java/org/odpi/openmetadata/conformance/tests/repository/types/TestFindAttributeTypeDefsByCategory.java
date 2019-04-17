/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.types;

import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Test that all attribute type definitions are retrieved by findAttributeTypeDefsByCategory.
 */
public class TestFindAttributeTypeDefsByCategory extends RepositoryConformanceTestCase
{
    private static final String testCaseId   = "repository-find-attribute-typedefs-by-category";
    private static final String testCaseName = "Repository find attribute type definitions by category test case";

    private static final String assertion1     = testCaseId + "-01";
    private static final String assertionMsg1  = "All attribute type definitions returned by category.";

    private static final String discoveredProperty_numberOfPrimitiveDefs  = "Number of supported PrimitiveDefs";
    private static final String discoveredProperty_primitiveDefs          = "Supported PrimitiveDefs";
    private static final String discoveredProperty_numberOfCollectionDefs = "Number of supported CollectionDefs";
    private static final String discoveredProperty_collectionDefs         = "Supported CollectionDefs";
    private static final String discoveredProperty_numberOfEnumDefs       = "Number of supported EnumDefs";
    private static final String discoveredProperty_enumDefs               = "Supported EnumDefs";

    private static final String successMessage  = "Attribute type definitions can be extracted by category";

    private List<AttributeTypeDef>    allAttributeTypeDefs;
    private Map<String, PrimitiveDef> primitiveDefs  = new HashMap<>();
    private List<CollectionDef>       collectionDefs = new ArrayList<>();
    private List<EnumDef>             enumDefs       = new ArrayList<>();


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param allAttributeTypeDefs list of all type definitions returned by repository
     */
    public TestFindAttributeTypeDefsByCategory(RepositoryConformanceWorkPad workPad,
                                               List<AttributeTypeDef>       allAttributeTypeDefs)
    {
        super(workPad,
              testCaseId,
              testCaseName,
              RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
              RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());

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
        OMRSMetadataCollection metadataCollection = super.getMetadataCollection();

        List<AttributeTypeDef>  typeList = metadataCollection.findAttributeTypeDefsByCategory(workPad.getLocalServerUserId(),
                                                                                              AttributeTypeDefCategory.PRIMITIVE);

        if (typeList == null)
        {
            super.addDiscoveredProperty(discoveredProperty_numberOfPrimitiveDefs,
                                        0,
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());
        }
        else
        {
            super.addDiscoveredProperty(discoveredProperty_numberOfPrimitiveDefs, typeList.size(),
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());

            List<String>    supportedTypes = new ArrayList<>();

            for (AttributeTypeDef   attributeTypeDef : typeList)
            {
                this.primitiveDefs.put(attributeTypeDef.getName(), (PrimitiveDef) attributeTypeDef);
                supportedTypes.add(attributeTypeDef.getName());
            }

            super.addDiscoveredProperty(discoveredProperty_primitiveDefs,
                                        supportedTypes,
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());
        }


        typeList = metadataCollection.findAttributeTypeDefsByCategory(workPad.getLocalServerUserId(),
                                                                      AttributeTypeDefCategory.COLLECTION);

        if (typeList == null)
        {
            super.addDiscoveredProperty(discoveredProperty_numberOfCollectionDefs,
                                        0,
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());
        }
        else
        {
            discoveredProperties.put(discoveredProperty_numberOfCollectionDefs, typeList.size());

            List<String>    supportedTypes = new ArrayList<>();

            for (AttributeTypeDef   attributeTypeDef : typeList)
            {
                this.collectionDefs.add((CollectionDef) attributeTypeDef);
                supportedTypes.add(attributeTypeDef.getName());
            }

            super.addDiscoveredProperty(discoveredProperty_collectionDefs,
                                        supportedTypes,
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());
        }


        typeList = metadataCollection.findAttributeTypeDefsByCategory(workPad.getLocalServerUserId(),
                                                                      AttributeTypeDefCategory.ENUM_DEF);

        if (typeList == null)
        {
            super.addDiscoveredProperty(discoveredProperty_numberOfEnumDefs,
                                        0,
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());
        }
        else
        {
            super.addDiscoveredProperty(discoveredProperty_numberOfEnumDefs, typeList.size(),
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());

            List<String>    supportedTypes = new ArrayList<>();

            for (AttributeTypeDef   attributeTypeDef : typeList)
            {
                this.enumDefs.add((EnumDef) attributeTypeDef);
                supportedTypes.add(attributeTypeDef.getName());
            }

            super.addDiscoveredProperty(discoveredProperty_enumDefs, supportedTypes,
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());
        }

        assertCondition((primitiveDefs.size() + collectionDefs.size() + enumDefs.size() == allAttributeTypeDefs.size()),
                        assertion1,
                        assertionMsg1,
                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());

        super.setSuccessMessage(successMessage);
    }
}
