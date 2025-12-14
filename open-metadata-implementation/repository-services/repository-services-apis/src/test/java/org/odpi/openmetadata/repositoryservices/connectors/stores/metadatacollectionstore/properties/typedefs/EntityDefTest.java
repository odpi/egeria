/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.*;

/**
 * EntityDefTest provides test of EntityDef
 */
public class EntityDefTest
{
    protected String                        guid                     = "TestGUID";
    protected String                        name                     = "TestName";
    protected TypeDefStatus                 status                   = TypeDefStatus.RENAMED_TYPEDEF;
    protected String                        replacedByTypeGUID       = "TestReplacedByGUID";
    protected String                        replacedByTypeName       = "TestReplacedByName";
    protected long                          version                  = 6;
    protected String                        versionName              = "TestVersionName";
    protected TypeDefCategory               category                 = TypeDefCategory.CLASSIFICATION_DEF;
    protected TypeDefLink                   superType                = new TypeDefLink();
    protected String                        description              = "TestDescription";
    protected String                        descriptionGUID          = "TestDescriptionGUID";
    protected String                        descriptionWiki          = "TestDescriptionWiki";
    protected String                        origin                   = "TestOrigin";
    protected String                        createdBy                = "TestCreatedBy";
    protected String                        updatedBy                = "TestUpdatedBy";
    protected Date                          createTime               = new Date(6555);
    protected Date                          updateTime               = new Date(9999);
    protected Map<String, String>           options                  = new HashMap<>();
    protected List<ExternalStandardMapping> externalStandardMappings = new ArrayList<>();
    protected List<InstanceStatus>          validInstanceStatusList  = new ArrayList<>();
    protected InstanceStatus                initialStatus            = InstanceStatus.INVALID;
    protected List<TypeDefAttribute>        propertiesDefinition     = new ArrayList<>();


    public EntityDefTest()
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
    }


    /**
     * Return a filled in test object
     *
     * @return test object
     */
    private EntityDef   getTestObject()
    {
        EntityDef testObject = new EntityDef();

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

        return testObject;
    }


    /**
     * Validate supplied object.
     *
     * @param testObject object to test
     */
    private void validateObject(EntityDef   testObject)
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
    }


    /**
     * Validate that the constructors set up the correct properties
     */
    @Test public void testCloneFromSubclass()
    {
        TypeDef testObject = this.getTestObject();

        validateObject((EntityDef) testObject.cloneFromSubclass());
    }


    /**
     * Validate that the constructors set up the correct properties
     */
    @Test public void testConstructors()
    {
        EntityDef testObject = new EntityDef();

        assertNull(testObject.getGUID());
        assertNull(testObject.getName());
        assertNull(testObject.getStatus());
        assertNull(testObject.getReplacedByTypeGUID());
        assertNull(testObject.getReplacedByTypeName());
        assertTrue(testObject.getVersion() == 0);
        assertNull(testObject.getVersionName());
        assertTrue(testObject.getCategory() == TypeDefCategory.ENTITY_DEF);
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

        EntityDef anotherTestObject = getTestObject();

        validateObject(new EntityDef(anotherTestObject));

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
            validateObject(objectMapper.readValue(jsonString, EntityDef.class));
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
            validateObject((EntityDef)objectMapper.readValue(jsonString, TypeDef.class));
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
        assertTrue(getTestObject().toString().contains("EntityDef"));
    }


    /**
     * Test that equals works
     */
    @Test public void testEquals()
    {
        assertTrue(getTestObject().equals(getTestObject()));

        EntityDef testObject = getTestObject();

        assertTrue(testObject.equals(testObject));

        assertFalse(getTestObject().equals(null));
        assertFalse(getTestObject().equals("A String"));

        EntityDef  differentObject = new EntityDef(TypeDefCategory.ENTITY_DEF, guid, name, 0 , "1.0");
        
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

        EntityDef testObject = getTestObject();
        EntityDef anotherObject = getTestObject();
        anotherObject.setGUID("DifferentAuthor");

        assertFalse(testObject.hashCode() == anotherObject.hashCode());
    }
}
