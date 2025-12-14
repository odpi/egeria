/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.*;

/**
 * RelationshipDefTest provides test of RelationshipDef
 */
public class RelationshipDefTest
{
    private final String guid = "TestGUID";
    private final String name = "TestName";
    private final TypeDefStatus                 status                   = TypeDefStatus.RENAMED_TYPEDEF;
    private final String                        replacedByTypeGUID       = "TestReplacedByGUID";
    private final String                        replacedByTypeName       = "TestReplacedByName";
    private final long                          version                  = 6;
    private final String                        versionName              = "TestVersionName";
    private final TypeDefCategory               category                 = TypeDefCategory.CLASSIFICATION_DEF;
    private final TypeDefLink                   superType                = new TypeDefLink();
    private final String                        description              = "TestDescription";
    private final String                        descriptionGUID          = "TestDescriptionGUID";
    private final String                        descriptionWiki          = "TestDescriptionWiki";
    private final String                        origin                   = "TestOrigin";
    private final String                        createdBy                = "TestCreatedBy";
    private final String                        updatedBy                = "TestUpdatedBy";
    private final Date                          createTime               = new Date(6555);
    private final Date                          updateTime               = new Date(9999);
    private final Map<String, String>           options                  = new HashMap<>();
    private final List<ExternalStandardMapping> externalStandardMappings = new ArrayList<>();
    private final List<InstanceStatus>          validInstanceStatusList  = new ArrayList<>();
    private final InstanceStatus                initialStatus            = InstanceStatus.INVALID;
    private final List<TypeDefAttribute>        propertiesDefinition     = new ArrayList<>();
    private final ClassificationPropagationRule propagationRule          = ClassificationPropagationRule.TWO_TO_ONE;
    private final RelationshipEndDef            endDef1                  = new RelationshipEndDef();
    private final RelationshipEndDef            endDef2                  = new RelationshipEndDef();


    public RelationshipDefTest()
    {
        superType.setGUID("TestSuperGUID");
        superType.setName("TestSuperName");
        superType.setStatus(TypeDefStatus.ACTIVE_TYPEDEF);
        superType.setReplacedByTypeGUID("TestNewSuperGUID");
        superType.setReplacedByTypeName("TestNewSuperName");
        
        options.put("TestOptionName", "TestOptionValue");
        
        ExternalStandardMapping  mapping = new ExternalStandardMapping();
        
        mapping.setStandardName("TestStandardName");
        mapping.setStandardOrganization("TestStandardOrg");
        mapping.setStandardTypeName("TestStandardTypeName");
        
        externalStandardMappings.add(mapping);

        validInstanceStatusList.add(InstanceStatus.ACTIVE);
        validInstanceStatusList.add(InstanceStatus.UNKNOWN);

        TypeDefAttribute attribute = new TypeDefAttribute();
        
        attribute.setAttributeName("TestAttributeName");
        attribute.setAttributeDescription("TestAttributeDescription");
        
        propertiesDefinition.add(attribute);

        endDef1.setAttributeCardinality(RelationshipEndCardinality.ANY_NUMBER);
        endDef1.setAttributeDescription("TestEnd1Description");
        endDef1.setAttributeDescriptionGUID("TestEnd1DescriptionGUID");
        endDef1.setAttributeName("TestEnd1Name");

        endDef2.setAttributeCardinality(RelationshipEndCardinality.AT_MOST_ONE);
        endDef2.setAttributeDescription("TestEnd2Description");
        endDef2.setAttributeDescriptionGUID("TestEnd2DescriptionGUID");
        endDef2.setAttributeName("TestEnd2Name");
    }


    /**
     * Return a filled in test object
     *
     * @return test object
     */
    private RelationshipDef   getTestObject()
    {
        RelationshipDef testObject = new RelationshipDef();

        testObject.setGUID(guid);
        testObject.setName(name);
        testObject.setStatus(status);
        testObject.setReplacedByTypeGUID(replacedByTypeGUID);
        testObject.setReplacedByTypeName(replacedByTypeName);
        testObject.setVersion(version);
        testObject.setVersionName(versionName);
        testObject.setCategory(category);
        testObject.setSuperType(superType);
        testObject.setDescription(description);
        testObject.setDescriptionGUID(descriptionGUID);
        testObject.setDescriptionWiki(descriptionWiki);
        testObject.setOrigin(origin);
        testObject.setCreatedBy(createdBy);
        testObject.setUpdatedBy(updatedBy);
        testObject.setCreateTime(createTime);
        testObject.setUpdateTime(updateTime);
        testObject.setOptions(options);
        testObject.setExternalStandardMappings(externalStandardMappings);
        testObject.setInitialStatus(initialStatus);
        testObject.setValidInstanceStatusList(validInstanceStatusList);
        testObject.setPropertiesDefinition(propertiesDefinition);
        testObject.setEndDef1(endDef1);
        testObject.setEndDef2(endDef2);
        testObject.setPropagationRule(propagationRule);

        return testObject;
    }


    /**
     * Validate supplied object.
     *
     * @param testObject object to test
     */
    private void validateObject(RelationshipDef   testObject)
    {
        assertTrue(testObject.getGUID().equals(guid));
        assertTrue(testObject.getName().equals(name));
        assertTrue(testObject.getStatus().equals(status));
        assertTrue(testObject.getReplacedByTypeGUID().equals(replacedByTypeGUID));
        assertTrue(testObject.getReplacedByTypeName().equals(replacedByTypeName));
        assertTrue(testObject.getVersion() == version);
        assertTrue(testObject.getVersionName().equals(versionName));
        assertTrue(testObject.getCategory().equals(category));
        assertTrue(testObject.getSuperType().equals(superType));
        assertTrue(testObject.getDescription().equals(description));
        assertTrue(testObject.getDescriptionGUID().equals(descriptionGUID));
        assertTrue(testObject.getDescriptionWiki().equals(descriptionWiki));
        assertTrue(testObject.getOrigin().equals(origin));
        assertTrue(testObject.getCreatedBy().equals(createdBy));
        assertTrue(testObject.getUpdatedBy().equals(updatedBy));
        assertTrue(testObject.getCreateTime().equals(createTime));
        assertTrue(testObject.getUpdateTime().equals(updateTime));
        assertTrue(testObject.getOptions().equals(options));
        assertTrue(testObject.getExternalStandardMappings().equals(externalStandardMappings));
        assertTrue(testObject.getValidInstanceStatusList().equals(validInstanceStatusList));
        assertTrue(testObject.getInitialStatus().equals(initialStatus));
        assertTrue(testObject.getPropertiesDefinition().equals(propertiesDefinition));
        assertTrue(testObject.getEndDef1().equals(endDef1));
        assertTrue(testObject.getEndDef2().equals(endDef2));
        assertTrue(testObject.getPropagationRule().equals(propagationRule));

        assertFalse(testObject.getEndDef1().equals(endDef2));
    }


    /**
     * Validate that the constructors set up the correct properties
     */
    @Test public void testCloneFromSubclass()
    {
        TypeDef testObject = this.getTestObject();

        validateObject((RelationshipDef) testObject.cloneFromSubclass());
    }


    /**
     * Validate that the constructors set up the correct properties
     */
    @Test public void testConstructors()
    {
        RelationshipDef testObject = new RelationshipDef();

        assertNull(testObject.getGUID());
        assertNull(testObject.getName());
        assertNull(testObject.getStatus());
        assertNull(testObject.getReplacedByTypeGUID());
        assertNull(testObject.getReplacedByTypeName());
        assertTrue(testObject.getVersion() == 0);
        assertNull(testObject.getVersionName());
        assertTrue(testObject.getCategory() == TypeDefCategory.RELATIONSHIP_DEF);
        assertNull(testObject.getSuperType());
        assertNull(testObject.getDescription());
        assertNull(testObject.getDescriptionGUID());
        assertNull(testObject.getOrigin());
        assertNull(testObject.getCreatedBy());
        assertNull(testObject.getUpdatedBy());
        assertNull(testObject.getCreateTime());
        assertNull(testObject.getUpdateTime());
        assertNull(testObject.getOptions());
        assertNull(testObject.getExternalStandardMappings());
        assertNull(testObject.getValidInstanceStatusList());
        assertNull(testObject.getInitialStatus());
        assertNull(testObject.getPropertiesDefinition());
        assertNull(testObject.getEndDef1());
        assertNull(testObject.getEndDef2());
        assertTrue(testObject.getPropagationRule() == ClassificationPropagationRule.NONE);


        RelationshipDef anotherTestObject = getTestObject();

        validateObject(new RelationshipDef(anotherTestObject));

        anotherTestObject.setValidInstanceStatusList(new ArrayList<>());
        anotherTestObject.setPropertiesDefinition(new ArrayList<>());
        anotherTestObject.setExternalStandardMappings(new ArrayList<>());
        anotherTestObject.setOptions(new HashMap<>());

        assertNull(anotherTestObject.getValidInstanceStatusList());
        assertNull(anotherTestObject.getPropertiesDefinition());
        assertNull(anotherTestObject.getExternalStandardMappings());
        assertNull(anotherTestObject.getOptions());
    }


    /**
     * Validate that an object generated from a JSON String has the same content as the object used to
     * create the JSON String.
     */
    @Test public void testJSON()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String       jsonString   = null;

        try
        {
            jsonString = objectMapper.writeValueAsString(getTestObject());
        }
        catch (Exception   exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateObject(objectMapper.readValue(jsonString, RelationshipDef.class));
        }
        catch (Exception   exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }


        /*
         * Through super class
         */
        TypeDef testObject = getTestObject();

        try
        {
            jsonString = objectMapper.writeValueAsString(testObject);
        }
        catch (Exception   exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateObject((RelationshipDef)objectMapper.readValue(jsonString, TypeDef.class));
        }
        catch (Exception   exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("RelationshipDef"));
    }


    /**
     * Test that equals works
     */
    @Test public void testEquals()
    {
        assertTrue(getTestObject().equals(getTestObject()));

        RelationshipDef testObject = getTestObject();

        assertTrue(testObject.equals(testObject));

        assertFalse(getTestObject().equals(null));
        assertFalse(getTestObject().equals("A String"));

        RelationshipDef  differentObject = new RelationshipDef(TypeDefCategory.ENTITY_DEF, guid, name, 0 , "1.0");
        
        assertFalse(testObject.equals(differentObject));

        differentObject = getTestObject();

        differentObject.setVersionName("DifferentHomeId");

        assertFalse(testObject.equals(differentObject));
    }


    /**
     * Test that hashcode is consistent
     */
    @Test public void testHash()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());

        RelationshipDef testObject = getTestObject();
        RelationshipDef anotherObject = getTestObject();
        anotherObject.setGUID("DifferentAuthor");

        assertFalse(testObject.hashCode() == anotherObject.hashCode());
    }
}
