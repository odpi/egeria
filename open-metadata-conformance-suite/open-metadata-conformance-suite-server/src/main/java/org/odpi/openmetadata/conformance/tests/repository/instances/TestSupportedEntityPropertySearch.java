/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.instances;

import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefAttribute;

import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;


import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;


/**
 * Test that all defined entities can be created, retrieved, updated and deleted.
 */
public class TestSupportedEntityPropertySearch extends RepositoryConformanceTestCase
{
    private static final String testCaseId = "repository-entity-property-search";
    private static final String testCaseName = "Repository entity property search test case";

    private static final String assertion1     = testCaseId + "-01";
    private static final String assertionMsg1  = " search returned results.";

    private static final String assertion2     = testCaseId + "-02";
    private static final String assertionMsg2  = " search contained all expected results.";

    private static final String assertion3     = testCaseId + "-03";
    private static final String assertionMsg3  = " search contained no unexpected results.";


    private static final String discoveredProperty_searchSupport       = " search support";

    private String            metadataCollectionId;
    private EntityDef         entityDef;
    private String            testTypeName;


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param entityDef type of valid entities
     */
    public TestSupportedEntityPropertySearch(RepositoryConformanceWorkPad workPad,
                                             EntityDef               entityDef)
    {
        super(workPad,
              RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getProfileId(),
              RepositoryConformanceProfileRequirement.ENTITY_LIFECYCLE.getRequirementId());

        this.metadataCollectionId = workPad.getTutMetadataCollectionId();
        this.entityDef = entityDef;

        this.testTypeName = this.updateTestIdByType(entityDef.getName(),
                                                    testCaseId,
                                                    testCaseName);
    }


    /**
     * Method implemented by the actual test case.
     *
     * @throws Exception something went wrong with the test.
     */
    protected void run() throws Exception
    {

        /*
         * This test will:
         *
         * Create a set of entities of the defined type in the TUT repository, with different/overlapping property values.
         * The entity is not classified - for find by classification tests see the corresponding testcase (TestSupportedEntityClassificationSearch)
         * It will then conduct a series of searches against the repository - some of which should retrieve the entity, others should not.
         *
         * The following searches are performed:
         *   Find By Instance (Match) Properties
         *   Find By Property Value
         * There are more details on each type of test in the relevant section below.
         *
         */


        OMRSMetadataCollection metadataCollection = super.getMetadataCollection();



        /*
         * Find By Instance (Match) Properties
         *
         *   Since this test uses MatchProperties there have to be some attributes defined on the type. If not, the test returns.
         *   Provided the type has at least one attribute the test can proceed.
         *
         *   There are three sets of entities (named 0, 1 & 2).
         *   There will be two entities per set, to ensure multiple 'hits'
         *   The sets of entities and their property values are as follows:
         *
         *   Entity set 0:   base values for all properties, formed as <property-name>.<set> - e.g. qualifiedName.0
         *   Entity set 1:   distinct from e0 but overlaps with e2
         *   Entity set 2:   distinct from e0 but overlaps with e1, on the first property
         *
         * Searches:
         * MatchProperties with all properties and matchCriteria == ALL. This tests that every property is correctly compared.
         * TODO - TESTED
         *   * Use instanceProperties for set 0 & matchCriteria ALL   - this should return all and only entities in set 0
         * TODO - CODED
         *   * Use instanceProperties for set 0 & matchCriteria ANY   - this should return all and only entities in set 0
         *   * Use instanceProperties for set 0 & matchCriteria NONE  - this should return all and only entities in sets 1 & 2
         * TODO - WRITE..
         *    * Use instanceProperties for set 1 & matchCriteria ALL  - this should return all and only entities in set 1
         *    * Use instanceProperties for set 1 & matchCriteria ANY  - this should return all and only entities in sets 1 & 2
         *    * Use instanceProperties for set 1 & matchCriteria NONE  - this should return all and only entities in set 0
         *
         */

        List<TypeDefAttribute> attrList = getAllPropertiesForTypedef(workPad.getLocalServerUserId(),entityDef);

        if (attrList == null || attrList.isEmpty()) {
            /*
             * There are no attributes for this type so a MatchProperties search is not possible.
             */
            return;
        }

        /*
         * instProps0 is always distinct.
         * instProps1 and instProps2 are distinct apart from first prop - for which they have the same value.
         *
         *  Generate property values for all the type's defined properties, including inherited properties
         */

        InstanceProperties instProps0 = populateInstanceProperties(attrList, "0");
        InstanceProperties instProps1 = populateInstanceProperties(attrList, "1");
        InstanceProperties instProps2 = populateInstanceProperties(attrList, "2");

        /*
         * Create two entities for each set.
         */

        List<EntityDetail> entitySet_0 = new ArrayList<>();


        entitySet_0.add(metadataCollection.addEntity(workPad.getLocalServerUserId(), entityDef.getGUID(), instProps0, null, null));
        entitySet_0.add(metadataCollection.addEntity(workPad.getLocalServerUserId(), entityDef.getGUID(), instProps0, null, null));


        List<EntityDetail> entitySet_1 = new ArrayList<>();

        entitySet_1.add(metadataCollection.addEntity(workPad.getLocalServerUserId(), entityDef.getGUID(), instProps1, null, null));
        entitySet_1.add(metadataCollection.addEntity(workPad.getLocalServerUserId(), entityDef.getGUID(), instProps1, null, null));

        List<EntityDetail> entitySet_2 = new ArrayList<>();

        entitySet_2.add(metadataCollection.addEntity(workPad.getLocalServerUserId(), entityDef.getGUID(), instProps2, null, null));
        entitySet_2.add(metadataCollection.addEntity(workPad.getLocalServerUserId(), entityDef.getGUID(), instProps2, null, null));



        /*
         * Do not perform existence assertions and content validations - these are tested in lifecycle tests.
         */

        InstanceProperties        matchProperties = null;
        MatchCriteria             matchCriteria = null;
        int                       fromElement = 0;
        List<EntityDetail>        result = null;


        /*
         *  Use instanceProperties for set 0 & matchCriteria ALL   - this should return all and only entities in set 0
         */

        matchProperties = instProps0;
        matchCriteria = MatchCriteria.ALL;
        fromElement = 0;


        result = metadataCollection.findEntitiesByProperty(workPad.getLocalServerUserId(),
                                                                      entityDef.getGUID(),
                                                                      matchProperties,
                                                                      matchCriteria,
                                                                      fromElement,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      0);



        /*
         * Verify that the result of the above search is not empty includes all set_0 entities and no others
         */

        assertCondition((result != null),
                assertion1,
                testTypeName + assertionMsg1,
                RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getRequirementId());

        /*
         * Verify that the result of the above search includes all set_0 entities
         */

        assertCondition((result.containsAll(entitySet_0)),
                assertion2,
                testTypeName + assertionMsg2,
                RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getRequirementId());

        /*
         * Verify that the result of the above search ONLY includes set_0 entities
         */

        boolean contamination = false;

        for (EntityDetail unwantedEntity : entitySet_1) {
            if (result.contains(unwantedEntity)) {
                contamination = true;
            }
        }

        for (EntityDetail unwantedEntity : entitySet_2) {
            if (result.contains(unwantedEntity)) {
                contamination = true;
            }
        }


        assertCondition((contamination == false),
                         assertion3,
                        testTypeName + assertionMsg3,
                         RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getProfileId(),
                         RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getRequirementId());



        /*
         *
         *  Use instanceProperties for set 0 & matchCriteria ANY   - this should return all and only entities in set 0
         *
         */
        matchProperties = instProps0;
        matchCriteria = MatchCriteria.ANY;
        fromElement = 0;


        result = metadataCollection.findEntitiesByProperty(workPad.getLocalServerUserId(),
                entityDef.getGUID(),
                matchProperties,
                matchCriteria,
                fromElement,
                null,
                null,
                null,
                null,
                null,
                0);



        /*
         * Verify that the result of the above search is not empty includes all set_0 entities and no others
         */

        assertCondition((result != null),
                assertion1,
                testTypeName + assertionMsg1,
                RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getRequirementId());

        /*
         * Verify that the result of the above search includes all set_0 entities
         */

        assertCondition((result.containsAll(entitySet_0)),
                assertion2,
                testTypeName + assertionMsg2,
                RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getRequirementId());

        /*
         * Verify that the result of the above search ONLY includes set_0 entities
         */

        contamination = false;

        for (EntityDetail unwantedEntity : entitySet_1) {
            if (result.contains(unwantedEntity)) {
                contamination = true;
            }
        }

        for (EntityDetail unwantedEntity : entitySet_2) {
            if (result.contains(unwantedEntity)) {
                contamination = true;
            }
        }


        assertCondition((contamination == false),
                assertion3,
                testTypeName + assertionMsg3,
                RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getRequirementId());



        /*
         *
         *  Use instanceProperties for set 0 & matchCriteria NONE  - this should return all and only entities in sets 1 & 2
         */
        matchProperties = instProps0;
        matchCriteria = MatchCriteria.NONE;
        fromElement = 0;


        result = metadataCollection.findEntitiesByProperty(workPad.getLocalServerUserId(),
                entityDef.getGUID(),
                matchProperties,
                matchCriteria,
                fromElement,
                null,
                null,
                null,
                null,
                null,
                0);



        /*
         * Verify that the result of the above search is not empty includes all set_0 entities and no others
         */

        assertCondition((result != null),
                assertion1,
                testTypeName + assertionMsg1,
                RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getRequirementId());

        /*
         * Verify that the result of the above search includes all set_0 entities
         */

        assertCondition((result.containsAll(entitySet_1) && result.containsAll(entitySet_2)),
                assertion2,
                testTypeName + assertionMsg2,
                RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getRequirementId());

        /*
         * Verify that the result of the above search ONLY includes set_0 entities
         */

        contamination = false;

        for (EntityDetail unwantedEntity : entitySet_0) {
            if (result.contains(unwantedEntity)) {
                contamination = true;
            }
        }


        assertCondition((contamination == false),
                assertion3,
                testTypeName + assertionMsg3,
                RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getProfileId(),
                RepositoryConformanceProfileRequirement.CURRENT_PROPERTY_SEARCH.getRequirementId());



        /*
         * Clean up all entities created by this testcase
         */

        for (EntityDetail entity : entitySet_0) {

            try {
                metadataCollection.deleteEntity(workPad.getLocalServerUserId(),
                        entity.getType().getTypeDefGUID(),
                        entity.getType().getTypeDefName(),
                        entity.getGUID());
            } catch (FunctionNotSupportedException exception) {
                // NO OP - can proceed to purge
            }

            metadataCollection.purgeEntity(workPad.getLocalServerUserId(),
                    entity.getType().getTypeDefGUID(),
                    entity.getType().getTypeDefName(),
                    entity.getGUID());
        }

        /*
         *  Find By Property Value
         * Hit the repository with a search string that should match one type defined property
         * Hit the repository with a search string that should match multiple type defined properties
         * Hit the repository with a search string that should match one core property
         */

        // TODO

        super.setSuccessMessage("Entities can be searched by property and property value");
    }


    /**
     * Determine if properties are as expected.
     *
     * @param firstInstanceProps is the target which must always be a non-null InstanceProperties
     * @param secondInstanceProps is the actual to be compared against first param - can be null, or empty....
     * @return match boolean
     */
    private boolean doPropertiesMatch(InstanceProperties firstInstanceProps, InstanceProperties secondInstanceProps)
    {
        boolean matchProperties = false;
        boolean noProperties = false;

        if ( (secondInstanceProps == null) ||
             (secondInstanceProps.getInstanceProperties() == null) ||
             (secondInstanceProps.getInstanceProperties().isEmpty()))
        {
            noProperties = true;
        }

        if (noProperties)
        {
            if ((firstInstanceProps.getInstanceProperties() == null) ||
                (firstInstanceProps.getInstanceProperties().isEmpty()))
            {
                matchProperties = true;
            }
        }
        else
        {
            // non-empty, perform matching

            Map<String, InstancePropertyValue> secondPropertiesMap = secondInstanceProps.getInstanceProperties();
            Map<String, InstancePropertyValue> firstPropertiesMap  = firstInstanceProps.getInstanceProperties();

            boolean matchSizes = (secondPropertiesMap.size() == firstPropertiesMap.size());

            if (matchSizes)
            {
                Set<String> secondPropertiesKeySet = secondPropertiesMap.keySet();
                Set<String> firstPropertiesKeySet  = firstPropertiesMap.keySet();

                boolean matchKeys = secondPropertiesKeySet.containsAll(firstPropertiesKeySet) &&
                                    firstPropertiesKeySet.containsAll(secondPropertiesKeySet);

                if (matchKeys)
                {
                    // Assume the values match and prove it if they don't...
                    boolean matchValues = true;

                    Iterator<String> secondPropertiesKeyIterator = secondPropertiesKeySet.iterator();
                    while (secondPropertiesKeyIterator.hasNext())
                    {
                        String key = secondPropertiesKeyIterator.next();
                        if (!(secondPropertiesMap.get(key).equals(firstPropertiesMap.get(key))))
                        {
                            matchValues = false;
                        }
                    }

                    // If all property values matched....
                    if (matchValues)
                    {
                        matchProperties = true;
                    }
                }
            }
        }

        return matchProperties;
    }


    /**
     * Return type def attributes for the properties defined in the TypeDef and all of its supertypes
     *
     * @param userId calling user
     * @param typeDef  the definition of the type
     * @return properties for an instance of this type
     * @throws Exception problem manipulating types
     */
    protected List<TypeDefAttribute>  getAllPropertiesForTypedef(String userId, TypeDef typeDef) throws Exception
    {


        // Recursively gather all the TypeDefAttributes for the supertype hierarchy...
        List<TypeDefAttribute> allTypeDefAttributes = getPropertiesForTypeDef(userId, typeDef);


        return allTypeDefAttributes;

    }


    /*
     * This method will populate an instance properties object - it does not populate core properties.
     * attrList is a list of all the TypeDefAttributes for the TypeDef to be instantiated.
     * modifier is an instance modifier - some properties will be customised per instance, others not.
     */
    protected InstanceProperties populateInstanceProperties(List<TypeDefAttribute> attrList , String setName)
    {

        /*
         * Get the trivial case out of the way
         */
        if (attrList == null) {
            return null;
        }

        InstanceProperties properties = null;

        Map<String, InstancePropertyValue> propertyMap = new HashMap<>();


        boolean first = true;
        for (TypeDefAttribute typeDefAttribute : attrList)
        {
            String                   attributeName = typeDefAttribute.getAttributeName();
            AttributeTypeDef         attributeType = typeDefAttribute.getAttributeType();
            AttributeTypeDefCategory category      = attributeType.getCategory();

            String modifier = setName;
            if (first) {
                // This is the first property - so for any sets other than set "0" it should match set "1"
                if (!setName.equals("0"))
                    modifier = "1";
                first = false;
            }

            switch(category)
            {
                case PRIMITIVE:
                    PrimitiveDef primitiveDef = (PrimitiveDef)attributeType;
                    propertyMap.put(attributeName, this.getPrimitivePropertyValue(attributeName, primitiveDef, modifier));
                    break;
            }
        }

        if (! propertyMap.isEmpty())
        {
            properties = new InstanceProperties();
            properties.setInstanceProperties(propertyMap);
        }


        return properties;

    }


    /**
     * Create a primitive property value for the requested property
     *
     * @param propertyName name of the property
     * @param propertyType type of the property
     * @return PrimitivePropertyValue object
     */
    private PrimitivePropertyValue getPrimitivePropertyValue(String        propertyName,
                                                             PrimitiveDef  propertyType,
                                                             String        modifier)
    {
        String  strModifier = modifier;
        Integer intModifier = Integer.parseInt(modifier);

        PrimitivePropertyValue propertyValue = new PrimitivePropertyValue();

        propertyValue.setPrimitiveDefCategory(propertyType.getPrimitiveDefCategory());

        switch (propertyType.getPrimitiveDefCategory())
        {
            case OM_PRIMITIVE_TYPE_STRING:
                propertyValue.setPrimitiveValue(propertyName + "." + strModifier);
                break;
            case OM_PRIMITIVE_TYPE_DATE:
                propertyValue.setPrimitiveValue(new Date(100L+new Long(strModifier)));
                break;
            case OM_PRIMITIVE_TYPE_INT:
                propertyValue.setPrimitiveValue(42+intModifier);
                break;
            case OM_PRIMITIVE_TYPE_BOOLEAN:
                propertyValue.setPrimitiveValue(true);
                break;
            case OM_PRIMITIVE_TYPE_SHORT:
                propertyValue.setPrimitiveValue(new Short(strModifier));
                break;
            case OM_PRIMITIVE_TYPE_BYTE:
                propertyValue.setPrimitiveValue(new Byte(strModifier));
                break;
            case OM_PRIMITIVE_TYPE_CHAR:
                propertyValue.setPrimitiveValue(strModifier.charAt(0));
                break;
            case OM_PRIMITIVE_TYPE_LONG:
                propertyValue.setPrimitiveValue(new Long(2000*intModifier));
                break;
            case OM_PRIMITIVE_TYPE_FLOAT:
                propertyValue.setPrimitiveValue(new Float(3.14159*intModifier));
                break;
            case OM_PRIMITIVE_TYPE_DOUBLE:
                propertyValue.setPrimitiveValue(new Double(1000000*intModifier));
                break;
            case OM_PRIMITIVE_TYPE_BIGDECIMAL:
                propertyValue.setPrimitiveValue(new Double(1000000*intModifier));
                break;
            case OM_PRIMITIVE_TYPE_BIGINTEGER:
                propertyValue.setPrimitiveValue(new Double(1000000*intModifier));
                break;
            case OM_PRIMITIVE_TYPE_UNKNOWN:
                break;
        }

        return propertyValue;
    }


}
