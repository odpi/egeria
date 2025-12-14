/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.*;

/**
 * TypeDefPatchTest provides test of TypeDefPatch
 */
public class TypeDefPatchTest
{
    private String                        typeDefGUID              = "TestTypeDefGUID";
    private String                        typeDefName              = "TestTypeDefName";
    private long                          applyToVersion           = 5L;
    private long                          updateToVersion          = 6L;
    private String                        newVersionName           = "TestNewVersionName";
    private String                        description              = "TestDescription";
    private String                        descriptionGUID          = "TestDescriptionGUID";
    private List<TypeDefAttribute>        typeDefAttributes        = new ArrayList<>();
    private Map<String, String>           typeDefOptions           = new HashMap<>();
    private List<ExternalStandardMapping> externalStandardMappings = new ArrayList<>();
    private List<InstanceStatus>          validInstanceStatusList  = new ArrayList<>();
    private InstanceStatus                initialStatus            = InstanceStatus.INVALID;
    private List<TypeDefLink>             validEntityDefs          = new ArrayList<>(); // ClassificationDefs
    private RelationshipEndDef            endDef1                  = new RelationshipEndDef(); // RelationshipDefs
    private RelationshipEndDef            endDef2                  = new RelationshipEndDef(); // RelationshipDefs



    public TypeDefPatchTest()
    {
        TypeDefAttribute attribute = new TypeDefAttribute();

        attribute.setAttributeName("TestAttributeName");
        attribute.setAttributeDescription("TestAttributeDescription");

        typeDefAttributes.add(attribute);
        
        typeDefOptions.put("TestOptionName", "TestOptionValue");
        
        ExternalStandardMapping  mapping = new ExternalStandardMapping();
        
        mapping.setStandardName("TestStandardName");
        mapping.setStandardOrganization("TestStandardOrg");
        mapping.setStandardTypeName("TestStandardTypeName");
        
        externalStandardMappings.add(mapping);

        validInstanceStatusList.add(InstanceStatus.ACTIVE);
        validInstanceStatusList.add(InstanceStatus.UNKNOWN);



        TypeDefLink validEntityDef = new TypeDefLink();

        validEntityDef.setGUID("TestSuperGUID");
        validEntityDef.setName("TestSuperName");
        validEntityDef.setStatus(TypeDefStatus.ACTIVE_TYPEDEF);
        validEntityDef.setReplacedByTypeGUID("TestNewSuperGUID");
        validEntityDef.setReplacedByTypeName("TestNewSuperName");

        validEntityDefs.add(validEntityDef);

        endDef1.setAttributeName("TestEndDef1");
        endDef2.setAttributeName("TestEndDef2");
    }


    /**
     * Return a filled in test object
     *
     * @return test object
     */
    private TypeDefPatch   getTestObject()
    {
        TypeDefPatch testObject = new TypeDefPatch();

        testObject.setTypeDefGUID(typeDefGUID);
        testObject.setTypeDefName(typeDefName);
        testObject.setApplyToVersion(applyToVersion);
        testObject.setUpdateToVersion(updateToVersion);
        testObject.setNewVersionName(newVersionName);
        testObject.setDescription(description);
        testObject.setDescriptionGUID(descriptionGUID);
        testObject.setPropertyDefinitions(typeDefAttributes);
        testObject.setTypeDefOptions(typeDefOptions);
        testObject.setExternalStandardMappings(externalStandardMappings);
        testObject.setInitialStatus(initialStatus);
        testObject.setValidInstanceStatusList(validInstanceStatusList);
        testObject.setInitialStatus(initialStatus);
        testObject.setValidEntityDefs(validEntityDefs);
        testObject.setEndDef1(endDef1);
        testObject.setEndDef2(endDef2);

        return testObject;
    }


    /**
     * Validate supplied object.
     *
     * @param testObject object to test
     */
    private void validateObject(TypeDefPatch   testObject)
    {
        assertEquals(testObject.getTypeDefGUID(), typeDefGUID);
        assertEquals(testObject.getTypeDefName(), typeDefName);
        assertEquals(testObject.getApplyToVersion(), applyToVersion);
        assertEquals(testObject.getUpdateToVersion(), updateToVersion);
        assertEquals(testObject.getNewVersionName(), newVersionName);
        assertEquals(testObject.getDescription(), description);
        assertEquals(testObject.getDescriptionGUID(), descriptionGUID);
        assertEquals(testObject.getPropertyDefinitions(), typeDefAttributes);
        assertEquals(testObject.getTypeDefOptions(), typeDefOptions);
        assertEquals(testObject.getExternalStandardMappings(), externalStandardMappings);
        assertEquals(testObject.getValidInstanceStatusList(), validInstanceStatusList);
        assertEquals(testObject.getInitialStatus(), initialStatus);
        assertEquals(testObject.getValidEntityDefs(), validEntityDefs);
        assertEquals(testObject.getEndDef1(), endDef1);
        assertEquals(testObject.getEndDef2(), endDef2);
    }


    /**
     * Validate that the constructors set up the correct properties
     */
    @Test public void testConstructors()
    {
        TypeDefPatch testObject = new TypeDefPatch();

        assertNull(testObject.getTypeDefGUID());
        assertNull(testObject.getTypeDefName());
        assertTrue(testObject.getApplyToVersion() == 0);
        assertTrue(testObject.getUpdateToVersion() == 0);
        assertNull(testObject.getNewVersionName());
        assertNull(testObject.getDescription());
        assertNull(testObject.getDescriptionGUID());
        assertNull(testObject.getPropertyDefinitions());
        assertNull(testObject.getTypeDefOptions());
        assertNull(testObject.getExternalStandardMappings());
        assertNull(testObject.getValidInstanceStatusList());
        assertNull(testObject.getInitialStatus());
        assertNull(testObject.getValidEntityDefs());
        assertNull(testObject.getEndDef1());
        assertNull(testObject.getEndDef2());

        TypeDefPatch anotherTestObject = getTestObject();

        validateObject(new TypeDefPatch(anotherTestObject));

        anotherTestObject.setValidInstanceStatusList(new ArrayList<>());
        anotherTestObject.setPropertyDefinitions(new ArrayList<>());
        anotherTestObject.setExternalStandardMappings(new ArrayList<>());
        anotherTestObject.setTypeDefOptions(new HashMap<>());

        assertNull(anotherTestObject.getValidInstanceStatusList());
        assertNull(anotherTestObject.getPropertyDefinitions());
        assertNull(anotherTestObject.getExternalStandardMappings());
        assertNull(anotherTestObject.getTypeDefOptions());
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
            validateObject(objectMapper.readValue(jsonString, TypeDefPatch.class));
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
        assertTrue(getTestObject().toString().contains("TypeDefPatch"));
    }


    /**
     * Test that equals works
     */
    @Test public void testEquals()
    {
        assertTrue(getTestObject().equals(getTestObject()));

        TypeDefPatch testObject = getTestObject();

        assertTrue(testObject.equals(testObject));

        assertFalse(getTestObject().equals(null));
        assertFalse(getTestObject().equals("A String"));

        TypeDefPatch  differentObject = new TypeDefPatch();

        assertFalse(testObject.equals(differentObject));

        differentObject = getTestObject();

        differentObject.setNewVersionName("DifferentHomeId");

        assertFalse(testObject.equals(differentObject));
    }


    /**
     * Test that hashcode is consistent
     */
    @Test public void testHash()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());

        TypeDefPatch testObject = getTestObject();
        TypeDefPatch anotherObject = getTestObject();
        anotherObject.setTypeDefGUID("DifferentAuthor");

        assertFalse(testObject.hashCode() == anotherObject.hashCode());
    }
}
