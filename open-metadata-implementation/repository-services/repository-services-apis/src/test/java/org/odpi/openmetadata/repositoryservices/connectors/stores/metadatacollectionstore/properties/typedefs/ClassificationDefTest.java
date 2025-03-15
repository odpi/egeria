/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.*;

/**
 * ClassificationDefTest provides test of ClassificationDef
 */
public class ClassificationDefTest
{
    private final String                        guid                     = "TestGUID";
    private final String                        name                     = "TestName";
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
    private final InstanceStatus                initialStatus            = InstanceStatus.APPROVED;
    private final List<TypeDefAttribute>        propertiesDefinition     = new ArrayList<>();
    private final List<TypeDefLink>             validEntityDefs          = new ArrayList<>();
    private final boolean                       propagatable             = true;


    public ClassificationDefTest()
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
        validInstanceStatusList.add(InstanceStatus.COMPLETE);

        TypeDefAttribute attribute = new TypeDefAttribute();
        
        attribute.setAttributeName("TestAttributeName");
        attribute.setAttributeDescription("TestAttributeDescription");
        
        propertiesDefinition.add(attribute);

        TypeDefLink  validEntity = new TypeDefLink();

        validEntity.setGUID("TestEntityGUID");
        validEntity.setName("TestEntityName");
        validEntity.setStatus(TypeDefStatus.ACTIVE_TYPEDEF);
        validEntity.setReplacedByTypeGUID("TestNewEntityGUID");
        validEntity.setReplacedByTypeName("TestNewEntityName");

        validEntityDefs.add(validEntity);
    }


    /**
     * Return a filled in test object
     *
     * @return test object
     */
    private ClassificationDef   getTestObject()
    {
        ClassificationDef testObject = new ClassificationDef();

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
        testObject.setPropagatable(propagatable);
        testObject.setValidEntityDefs(validEntityDefs);

        return testObject;
    }


    /**
     * Validate supplied object.
     *
     * @param testObject object to test
     */
    private void validateObject(ClassificationDef   testObject)
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
        assertTrue(testObject.isPropagatable() == propagatable);
        assertTrue(testObject.getValidEntityDefs().equals(validEntityDefs));
    }


    /**
     * Validate that the constructors set up the correct properties
     */
    @Test public void testCloneFromSubclass()
    {
        TypeDef testObject = this.getTestObject();

        validateObject((ClassificationDef) testObject.cloneFromSubclass());
    }


    /**
     * Validate that the constructors set up the correct properties
     */
    @Test public void testConstructors()
    {
        ClassificationDef testObject = new ClassificationDef();

        assertNull(testObject.getGUID());
        assertNull(testObject.getName());
        assertNull(testObject.getStatus());
        assertNull(testObject.getReplacedByTypeGUID());
        assertNull(testObject.getReplacedByTypeName());
        assertTrue(testObject.getVersion() == 0);
        assertNull(testObject.getVersionName());
        assertTrue(testObject.getCategory() == TypeDefCategory.CLASSIFICATION_DEF);
        assertNull(testObject.getSuperType());
        assertNull(testObject.getDescription());
        assertNull(testObject.getDescriptionGUID());
        assertNull(testObject.getDescriptionWiki());
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
        assertNull(testObject.getValidEntityDefs());
        assertFalse(testObject.isPropagatable());

        ClassificationDef anotherTestObject = getTestObject();

        validateObject(new ClassificationDef(anotherTestObject));

        anotherTestObject.setValidInstanceStatusList(new ArrayList<>());
        anotherTestObject.setPropertiesDefinition(new ArrayList<>());
        anotherTestObject.setExternalStandardMappings(new ArrayList<>());
        anotherTestObject.setOptions(new HashMap<>());
        anotherTestObject.setValidEntityDefs(new ArrayList<>());

        assertNull(anotherTestObject.getValidInstanceStatusList());
        assertNull(anotherTestObject.getPropertiesDefinition());
        assertNull(anotherTestObject.getExternalStandardMappings());
        assertNull(anotherTestObject.getOptions());
        assertNull(anotherTestObject.getValidEntityDefs());
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
            validateObject(objectMapper.readValue(jsonString, ClassificationDef.class));
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
            validateObject((ClassificationDef)objectMapper.readValue(jsonString, TypeDef.class));
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
        assertTrue(getTestObject().toString().contains("ClassificationDef"));
    }


    /**
     * Test that equals works
     */
    @Test public void testEquals()
    {
        assertTrue(getTestObject().equals(getTestObject()));

        ClassificationDef testObject = getTestObject();

        assertTrue(testObject.equals(testObject));

        assertFalse(getTestObject().equals(null));
        assertFalse(getTestObject().equals("A String"));

        ClassificationDef  differentObject = new ClassificationDef(TypeDefCategory.ENTITY_DEF, guid, name, 0 , "1.0");
        
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

        ClassificationDef testObject = getTestObject();
        ClassificationDef anotherObject = getTestObject();
        anotherObject.setGUID("DifferentAuthor");

        assertFalse(testObject.hashCode() == anotherObject.hashCode());
    }
}
