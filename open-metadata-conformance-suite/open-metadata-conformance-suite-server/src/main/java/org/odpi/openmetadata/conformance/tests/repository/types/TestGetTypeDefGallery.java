/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.types;

import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.ClassificationDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefGallery;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Discover the types supported by the system under test.
 */
public class TestGetTypeDefGallery extends RepositoryConformanceTestCase
{
    private static final  String testCaseId = "repository-get-typedef-gallery";
    private static final  String testCaseName = "Repository get type definition gallery test case";
    private static final  String testCaseSuccessMessage = "Repository type definition gallery retrieved";

    private static final  String assertion1     = testCaseId + "-01";
    private static final  String assertionMsg1  = "TypeDefGallery retrieved.";

    private static final  String discoveredProperty_numberOfAttributeTypeDefs = "Number of supported AttributeTypeDefs";
    private static final  String discoveredProperty_attributeTypeDefs = "Supported AttributeTypeDefs";
    private static final  String discoveredProperty_numberOfTypeDefs = "Number of supported TypeDefs";
    private static final  String discoveredProperty_typeDefs = "Supported TypeDefs";

    private List<AttributeTypeDef> attributeTypeDefs = null;

    /*
     * These type defs are used to test the types
     */
    private       List<TypeDef>          allTypeDefs = null;

    /*
     * These type defs are used to test instances.
     */
    private       List<TypeDef>          typesForInstanceTesting = new ArrayList<>();
    private final Map<String, EntityDef> entityDefs              = new HashMap<>();
    private final List<RelationshipDef>   relationshipDefs   = new ArrayList<>();
    private final List<ClassificationDef> classificationDefs = new ArrayList<>();

    RepositoryConformanceWorkPad repositoryConformanceWorkPad;

    /**
     * Typical constructor sets up superclass
     *
     * @param workPad place for parameters and results
     */
    public TestGetTypeDefGallery(RepositoryConformanceWorkPad workPad)
    {
        super(workPad,
              testCaseId,
              testCaseName,
              RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
              RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());

        this.repositoryConformanceWorkPad = workPad;
    }


    /**
     * Method implemented by the actual test case.
     *
     * @throws Exception something went wrong with the test.
     */
    protected void run() throws Exception
    {
        OMRSMetadataCollection metadataCollection = super.getMetadataCollection();

        long start = System.currentTimeMillis();
        TypeDefGallery  typeDefGallery = metadataCollection.getAllTypes(workPad.getLocalServerUserId());
        long elapsedTime = System.currentTimeMillis() - start;
        assertCondition((true),
                        assertion1,
                        assertionMsg1,
                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId(),
                        "getAllTypes",
                        elapsedTime);

        if (typeDefGallery == null)
        {
            super.addDiscoveredProperty(discoveredProperty_numberOfAttributeTypeDefs,
                                        0,
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());
            super.addDiscoveredProperty(discoveredProperty_numberOfTypeDefs,
                                        0,
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());
        }
        else
        {
            attributeTypeDefs = typeDefGallery.getAttributeTypeDefs();
            repositoryConformanceWorkPad.setSupportedAttributeTypeDefsFromRESTAPI(attributeTypeDefs);
            allTypeDefs = typeDefGallery.getTypeDefs();
            repositoryConformanceWorkPad.setSupportedTypeDefsFromRESTAPI(allTypeDefs);

            if (attributeTypeDefs == null)
            {
                super.addDiscoveredProperty(discoveredProperty_numberOfAttributeTypeDefs,
                                            0,
                                            RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                                            RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());
            }
            else
            {
                super.addDiscoveredProperty(discoveredProperty_numberOfAttributeTypeDefs,
                                            attributeTypeDefs.size(),
                                            RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                                            RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());

                List<String>    supportedTypes = new ArrayList<>();

                for (AttributeTypeDef   attributeTypeDef : attributeTypeDefs)
                {
                    supportedTypes.add(attributeTypeDef.getName());
                }

                super.addDiscoveredProperty(discoveredProperty_attributeTypeDefs,
                                            supportedTypes,
                                            RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                                            RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());
            }

            if (allTypeDefs == null)
            {
                super.addDiscoveredProperty(discoveredProperty_numberOfTypeDefs,
                                            0,
                                            RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                                            RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());
            }
            else
            {
                super.addDiscoveredProperty(discoveredProperty_numberOfTypeDefs,
                                            allTypeDefs.size(),
                                            RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                                            RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());


                List<String>    supportedTypes = new ArrayList<>();

                for (TypeDef   typeDef : allTypeDefs)
                {
                    supportedTypes.add(typeDef.getName());
                }

                super.addDiscoveredProperty(discoveredProperty_typeDefs,
                                            supportedTypes,
                                            RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                                            RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());
            }
        }

        /*
         * Split up the retrieved types.
         */
        if ((repositoryConformanceWorkPad.getTestEntityTypes() == null) ||
            (repositoryConformanceWorkPad.getTestEntityTypes().isEmpty()))
        {
            /*
             * Test all types.
             */
            typesForInstanceTesting = allTypeDefs;
            splitUpTypeDefsForInstanceTests(allTypeDefs);
        }
        else
        {
            /*
             * Test requested types.
             */
            typesForInstanceTesting = determineTypeDefsForInstanceTests(allTypeDefs);
            splitUpTypeDefsForInstanceTests(typesForInstanceTesting);
        }

        super.setSuccessMessage(testCaseSuccessMessage);
    }


    /**
     * Take the list of typeDefs for instance testing and split up into entity, relationship and classification types.
     *
     * @param typeDefs list of typedefs to test
     */
    private void splitUpTypeDefsForInstanceTests(List<TypeDef> typeDefs)
    {
        /*
         * Map the known types for easy retrieval
         */
        for (TypeDef knownTypeDef : typeDefs)
        {
            if (knownTypeDef instanceof EntityDef entityDef)
            {
                entityDefs.put(entityDef.getName(), entityDef);
            }
            else if (knownTypeDef instanceof RelationshipDef relationshipDef)
            {
                relationshipDefs.add(relationshipDef);
            }
            else if (knownTypeDef instanceof ClassificationDef classificationDef)
            {
                classificationDefs.add(classificationDef);
            }
        }
    }


    /**
     * Take the configured list of entity typeDefs for instance testing and add the matching relationships and classifications.
     *
     * @param typeDefs list of typedefs to test
     * @return list of requested types
     */
    private List<TypeDef> determineTypeDefsForInstanceTests(List<TypeDef> typeDefs)
    {
        Map<String, TypeDef> knownTypeDefMap         = new HashMap<>();
        Map<String, TypeDef> matchingTypes           = new HashMap<>();

        /*
         * Map the known types ofr easy retrieval
         */
        for (TypeDef knownTypeDef : typeDefs)
        {
            knownTypeDefMap.put(knownTypeDef.getName(), knownTypeDef);
        }

        /*
         * Add the matching EntityDefs (including their super types.)
         */
        for (TypeDef knownTypeDef : typeDefs)
        {
            if (repositoryConformanceWorkPad.getTestEntityTypes().contains(knownTypeDef.getName()))
            {
                matchingTypes.put(knownTypeDef.getName(), knownTypeDef);

                String superTypeName = knownTypeDef.getSuperType().getName();
                while (superTypeName != null)
                {
                    TypeDef superType = knownTypeDefMap.get(superTypeName);
                    matchingTypes.put(superTypeName, superType);

                    if (superType.getSuperType() == null)
                    {
                        superTypeName = null;
                    }
                    else
                    {
                        superTypeName = superType.getSuperType().getName();
                    }
                }
            }
        }

        List<String> matchingEntityNames = new ArrayList<>(matchingTypes.keySet());

        /*
         * Add the matching RelationshipDefs and ClassificationDefs
         */
        for (TypeDef knownTypeDef : typeDefs)
        {
            if (knownTypeDef instanceof RelationshipDef relationshipDef)
            {
                /*
                 * Both ends need to match
                 */
                if ((matchingEntityNames.contains(relationshipDef.getEndDef1().getEntityType().getName())) &&
                    (matchingEntityNames.contains(relationshipDef.getEndDef2().getEntityType().getName())))
                {
                    matchingTypes.put(knownTypeDef.getName(), knownTypeDef);
                }
            }
            else if (knownTypeDef instanceof ClassificationDef classificationDef)
            {
                for (TypeDefLink validEntityDef : classificationDef.getValidEntityDefs())
                {
                    if (matchingEntityNames.contains(validEntityDef.getName()))
                    {
                        matchingTypes.put(knownTypeDef.getName(), knownTypeDef);
                    }
                }
            }
        }

        return new ArrayList<>(matchingTypes.values());
    }


    /**
     * Return the list of retrieved attribute type definitions.
     *
     * @return list of AttributeTypeDefs
     */
    public List<AttributeTypeDef> getAttributeTypeDefs()
    {
        return attributeTypeDefs;
    }


    /**
     * Return the list of retrieved type definitions used to guide the testing of types.
     *
     * @return list of TypeDefs
     */
    public List<TypeDef> getAllTypeDefs()
    {
        return allTypeDefs;
    }


    /**
     * Return the list of retrieved type definitions used to guide the testing of instances.
     *
     * @return list of TypeDefs
     */
    public List<TypeDef> getInstanceTestTypeDefs()
    {
        if (typesForInstanceTesting.isEmpty())
        {
            return null;
        }

        return typesForInstanceTesting;
    }



    /**
     * Return the list of entity definitions returned by the repository.
     *
     * @return map of EntityDef name to EntityDefs
     */
    public Map<String, EntityDef> getEntityDefs()
    {
        if (entityDefs.isEmpty())
        {
            return null;
        }
        else
        {
            return entityDefs;
        }
    }


    /**
     * Return the list of relationship definitions returned by the repository.
     *
     * @return list of RelationshipDefs
     */
    public List<RelationshipDef> getRelationshipDefs()
    {
        if (relationshipDefs.isEmpty())
        {
            return null;
        }
        else
        {
            return relationshipDefs;
        }
    }


    /**
     * Return the list of classification definitions returned by the repository.
     *
     * @return list of classifications
     */
    public List<ClassificationDef> getClassificationDefs()
    {
        if (classificationDefs.isEmpty())
        {
            return null;
        }
        else
        {
            return classificationDefs;
        }
    }
}
