/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.compliance.tests.repository;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Test that all type definitions are retrieved by findTypeDefsByCategory.
 */
public class TestFindTypeDefsByCategory extends OpenMetadataRepositoryTestCase
{
    private static final String testUserId = "ComplianceTestUser";

    private static final String testCaseId = "repository-find-typedefs-by-category";
    private static final String testCaseName = "Repository find type definitions by category test case";

    private static final String assertion1     = testCaseId + "-01";
    private static final String assertionMsg1  = " all type definitions returned by category.";

    private static final  String discoveredProperty_numberOfEntityDefs = "Number of supported EntityDefs";
    private static final  String discoveredProperty_entityDefs = "Supported EntityDefs";
    private static final  String discoveredProperty_numberOfRelationshipDefs = "Number of supported RelationshipDefs";
    private static final  String discoveredProperty_relationshipDefs = "Supported RelationshipDefs";
    private static final  String discoveredProperty_numberOfClassificationDefs = "Number of supported ClassificationDefs";
    private static final  String discoveredProperty_classificationDefs = "Supported ClassificationDefs";

    private List<TypeDef>  allTypeDefs;
    private Map<String, EntityDef> entityDefs = new HashMap<>();
    private List<RelationshipDef> relationshipDefs = new ArrayList<>();
    private List<ClassificationDef> classificationDefs = new ArrayList<>();

    /**
     * Typical constructor sets up superclass
     *
     * @param workbenchId identifier of calling workbench
     * @param allTypeDefs list of all type definitions returned by repository
     */
    TestFindTypeDefsByCategory(String         workbenchId,
                               List<TypeDef>  allTypeDefs)
    {
        super(workbenchId, testCaseId, testCaseName);
        this.allTypeDefs = allTypeDefs;
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


    /**
     * Method implemented by the actual test case.
     *
     * @throws Exception something went wrong with the test.
     */
    protected void run() throws Exception
    {
        Map<String, Object>    discoveredProperties = new HashMap<>();
        OMRSMetadataCollection metadataCollection = super.getMetadataCollection();

        List<TypeDef>  typeDefGallery = metadataCollection.findTypeDefsByCategory(testUserId, TypeDefCategory.ENTITY_DEF);

        if (typeDefGallery == null)
        {
            discoveredProperties.put(discoveredProperty_numberOfEntityDefs, 0);
        }
        else
        {
            discoveredProperties.put(discoveredProperty_numberOfEntityDefs, typeDefGallery.size());

            List<String>    supportedTypes = new ArrayList<>();

            for (TypeDef   typeDef : typeDefGallery)
            {
                this.entityDefs.put(typeDef.getName(), (EntityDef)typeDef);
                supportedTypes.add(typeDef.getName());
            }

            discoveredProperties.put(discoveredProperty_entityDefs, supportedTypes);
        }


        typeDefGallery = metadataCollection.findTypeDefsByCategory(testUserId, TypeDefCategory.RELATIONSHIP_DEF);

        if (typeDefGallery == null)
        {
            discoveredProperties.put(discoveredProperty_numberOfRelationshipDefs, 0);
        }
        else
        {
            discoveredProperties.put(discoveredProperty_numberOfRelationshipDefs, typeDefGallery.size());

            List<String>    supportedTypes = new ArrayList<>();

            for (TypeDef   typeDef : typeDefGallery)
            {
                this.relationshipDefs.add((RelationshipDef) typeDef);
                supportedTypes.add(typeDef.getName());
            }

            discoveredProperties.put(discoveredProperty_relationshipDefs, supportedTypes);
        }


        typeDefGallery = metadataCollection.findTypeDefsByCategory(testUserId, TypeDefCategory.CLASSIFICATION_DEF);

        if (typeDefGallery == null)
        {
            discoveredProperties.put(discoveredProperty_numberOfClassificationDefs, 0);
        }
        else
        {
            discoveredProperties.put(discoveredProperty_numberOfClassificationDefs, typeDefGallery.size());

            List<String>    supportedTypes = new ArrayList<>();

            for (TypeDef   typeDef : typeDefGallery)
            {
                this.classificationDefs.add((ClassificationDef) typeDef);
                supportedTypes.add(typeDef.getName());
            }

            discoveredProperties.put(discoveredProperty_classificationDefs, supportedTypes);
        }

        assertCondition((entityDefs.size() + relationshipDefs.size() + classificationDefs. size() == allTypeDefs.size()), assertion1,  assertionMsg1);

        super.result.setSuccessMessage("Type definitions can be extracted by category");

        super.result.setDiscoveredProperties(discoveredProperties);
    }
}
