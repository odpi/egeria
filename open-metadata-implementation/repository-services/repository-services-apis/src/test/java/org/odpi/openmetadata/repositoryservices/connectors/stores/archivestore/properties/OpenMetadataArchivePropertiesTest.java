/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * OpenMetadataArchive stores details of changes to the open metadata types.
 * Validate that it can be created, cloned, serialized and compared.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataArchivePropertiesTest
{
    private String                  archiveGUID            = "TestGUID";
    private String                  archiveName            = "TestArchiveName";
    private String                  archiveDescription     = "TestArchiveDescription";
    private OpenMetadataArchiveType archiveType            = OpenMetadataArchiveType.METADATA_EXPORT;
    private String                  originatorName         = "TestOriginatorName";
    private String                  originatorOrganization = "TestOrganization";
    private String                  originatorLicense      = "TestLicense";
    private Date                    creationDate           = new Date();
    private List<String>            dependsOnArchives      = new ArrayList<>();


    /**
     * Constructor sets up the complex types
     */
    public OpenMetadataArchivePropertiesTest()
    {
       dependsOnArchives.add("TestDependency");
    }


    /**
     * Return a filled in test object
     *
     * @return test object
     */
    private OpenMetadataArchiveProperties   getTestObject()
    {
        OpenMetadataArchiveProperties testObject = new OpenMetadataArchiveProperties();

        testObject.setArchiveGUID(archiveGUID);
        testObject.setArchiveName(archiveName);
        testObject.setArchiveDescription(archiveDescription);
        testObject.setArchiveType(archiveType);
        testObject.setOriginatorName(originatorName);
        testObject.setOriginatorOrganization(originatorOrganization);
        testObject.setOriginatorLicense(originatorLicense);
        testObject.setCreationDate(creationDate);
        testObject.setDependsOnArchives(dependsOnArchives);

        return testObject;
    }


    /**
     * Validate supplied object.
     *
     * @param testObject object to test
     */
    private void validateObject(OpenMetadataArchiveProperties   testObject)
    {
        assertTrue(testObject.getArchiveGUID().equals(archiveGUID));
        assertTrue(testObject.getArchiveName().equals(archiveName));
        assertTrue(testObject.getArchiveDescription().equals(archiveDescription));
        assertTrue(testObject.getArchiveType().equals(archiveType));
        assertTrue(testObject.getOriginatorName().equals(originatorName));
        assertTrue(testObject.getOriginatorOrganization().equals(originatorOrganization));
        assertTrue(testObject.getOriginatorLicense().equals(originatorLicense));
        assertTrue(testObject.getCreationDate().equals(creationDate));
        assertTrue(testObject.getDependsOnArchives().equals(dependsOnArchives));
    }



    /**
     * Validate that the constructors set up the correct properties
     */
    @Test
    public void testConstructors()
    {
        OpenMetadataArchiveProperties testObject = new OpenMetadataArchiveProperties();

        assertTrue(testObject.getArchiveGUID() == null);
        assertTrue(testObject.getArchiveName() == null);
        assertTrue(testObject.getArchiveDescription() == null);
        assertTrue(testObject.getArchiveType() == null);
        assertTrue(testObject.getOriginatorName() == null);
        assertTrue(testObject.getOriginatorOrganization() == null);
        assertTrue(testObject.getOriginatorLicense() == null);
        assertTrue(testObject.getCreationDate() == null);
        assertTrue(testObject.getDependsOnArchives() == null);

        testObject.setDependsOnArchives(new ArrayList<>());

        assertTrue(testObject.getDependsOnArchives() == null);

        OpenMetadataArchiveProperties anotherTestObject = getTestObject();

        validateObject(new OpenMetadataArchiveProperties(anotherTestObject));
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
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateObject(objectMapper.readValue(jsonString, OpenMetadataArchiveProperties.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }


        /*
         * Through super class
         */
        OpenMetadataArchiveElementHeader testObject = getTestObject();

        try
        {
            jsonString = objectMapper.writeValueAsString(testObject);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateObject((OpenMetadataArchiveProperties)objectMapper.readValue(jsonString, OpenMetadataArchiveElementHeader.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }
    }



    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("OpenMetadataArchiveProperties"));
    }


    /**
     * Test that equals works
     */
    @Test public void testEquals()
    {
        assertTrue(getTestObject().equals(getTestObject()));

        OpenMetadataArchiveProperties testObject = getTestObject();

        assertTrue(testObject.equals(testObject));

        assertFalse(getTestObject().equals(null));
        assertFalse(getTestObject().equals("A String"));

        OpenMetadataArchiveProperties  differentObject = getTestObject();

        differentObject = getTestObject();
        differentObject.setArchiveGUID(null);
        assertFalse(testObject.equals(differentObject));

        differentObject = getTestObject();
        differentObject.setArchiveName(null);
        assertFalse(testObject.equals(differentObject));

        differentObject = getTestObject();
        differentObject.setArchiveDescription(null);
        assertFalse(testObject.equals(differentObject));

        differentObject = getTestObject();
        differentObject.setArchiveType(null);
        assertFalse(testObject.equals(differentObject));

        differentObject = getTestObject();
        differentObject.setOriginatorName(null);
        assertFalse(testObject.equals(differentObject));

        differentObject = getTestObject();
        differentObject.setOriginatorOrganization(null);
        assertFalse(testObject.equals(differentObject));

        differentObject = getTestObject();
        differentObject.setOriginatorLicense(null);
        assertFalse(testObject.equals(differentObject));

        differentObject = getTestObject();
        differentObject.setCreationDate(null);
        assertFalse(testObject.equals(differentObject));

        differentObject = getTestObject();
        differentObject.setDependsOnArchives(null);
        assertFalse(testObject.equals(differentObject));
    }


    /**
     * Test that hashcode is consistent
     */
    @Test public void testHash()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());

        OpenMetadataArchiveProperties testObject = getTestObject();
        OpenMetadataArchiveProperties anotherObject = getTestObject();
        anotherObject.setOriginatorLicense(null);

        assertFalse(testObject.hashCode() == anotherObject.hashCode());
    }
}
