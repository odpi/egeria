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
 * Test that all type definitions are retrieved by findTypeDefsByCategory.
 */
public class TestFindTypeDefsByCategory extends RepositoryConformanceTestCase
{
    private static final String testCaseId = "repository-find-typedefs-by-category";
    private static final String testCaseName = "Repository find type definitions by category test case";

    private static final String assertion1     = testCaseId + "-01";
    private static final String assertionMsg1  = "All type definitions returned by category.";

    private static final  String discoveredProperty_numberOfEntityDefs = "Number of supported EntityDefs";
    private static final  String discoveredProperty_entityDefs = "Supported EntityDefs";
    private static final  String discoveredProperty_numberOfRelationshipDefs = "Number of supported RelationshipDefs";
    private static final  String discoveredProperty_relationshipDefs = "Supported RelationshipDefs";
    private static final  String discoveredProperty_numberOfClassificationDefs = "Number of supported ClassificationDefs";
    private static final  String discoveredProperty_classificationDefs = "Supported ClassificationDefs";

    private final List<TypeDef>          allTypeDefs;
    private final Map<String, EntityDef>  entityDefs         = new HashMap<>();
    private final List<RelationshipDef>   relationshipDefs   = new ArrayList<>();
    private final List<ClassificationDef> classificationDefs = new ArrayList<>();


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param allTypeDefs list of all type definitions returned by repository
     */
    public TestFindTypeDefsByCategory(RepositoryConformanceWorkPad workPad,
                                      List<TypeDef>                allTypeDefs)
    {
        super(workPad,
              testCaseId,
              testCaseName,
              RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
              RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());

        this.allTypeDefs = allTypeDefs;
    }


    /**
     * Method implemented by the actual test case.
     *
     * @throws Exception something went wrong with the test.
     */
    protected void run() throws Exception
    {
        OMRSMetadataCollection metadataCollection = super.getMetadataCollection();

        List<TypeDef>  typeDefList = metadataCollection.findTypeDefsByCategory(workPad.getLocalServerUserId(), TypeDefCategory.ENTITY_DEF);

        if (typeDefList == null)
        {
            super.addDiscoveredProperty(discoveredProperty_numberOfEntityDefs,
                                        0,
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());
        }
        else
        {
            super.addDiscoveredProperty(discoveredProperty_numberOfEntityDefs,
                                        typeDefList.size(),
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());

            List<String>    supportedTypes = new ArrayList<>();

            for (TypeDef   typeDef : typeDefList)
            {
                this.entityDefs.put(typeDef.getName(), (EntityDef)typeDef);
                supportedTypes.add(typeDef.getName());
            }

            super.addDiscoveredProperty(discoveredProperty_entityDefs,
                                        supportedTypes,
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());
        }


        typeDefList = metadataCollection.findTypeDefsByCategory(workPad.getLocalServerUserId(), TypeDefCategory.RELATIONSHIP_DEF);

        if (typeDefList == null)
        {
            super.addDiscoveredProperty(discoveredProperty_numberOfRelationshipDefs,
                                        0,
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());
        }
        else
        {
            super.addDiscoveredProperty(discoveredProperty_numberOfRelationshipDefs,
                                        typeDefList.size(),
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());

            List<String>    supportedTypes = new ArrayList<>();

            for (TypeDef   typeDef : typeDefList)
            {
                this.relationshipDefs.add((RelationshipDef) typeDef);
                supportedTypes.add(typeDef.getName());
            }

            super.addDiscoveredProperty(discoveredProperty_relationshipDefs,
                                        supportedTypes,
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());
        }


        typeDefList = metadataCollection.findTypeDefsByCategory(workPad.getLocalServerUserId(),
                                                                TypeDefCategory.CLASSIFICATION_DEF);

        if (typeDefList == null)
        {
            super.addDiscoveredProperty(discoveredProperty_numberOfClassificationDefs,
                                        0,
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());
        }
        else
        {
            super.addDiscoveredProperty(discoveredProperty_numberOfClassificationDefs,
                                        typeDefList.size(),
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());

            List<String>    supportedTypes = new ArrayList<>();

            for (TypeDef   typeDef : typeDefList)
            {
                this.classificationDefs.add((ClassificationDef) typeDef);
                supportedTypes.add(typeDef.getName());
            }

            super.addDiscoveredProperty(discoveredProperty_classificationDefs,
                                        supportedTypes,
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());
        }

        assertCondition((entityDefs.size() + relationshipDefs.size() + classificationDefs. size() == allTypeDefs.size()),
                        assertion1,
                        assertionMsg1,
                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());

        super.setSuccessMessage("Type definitions can be extracted by category");
    }
}
