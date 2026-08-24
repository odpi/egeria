/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository;

import org.odpi.openmetadata.conformance.ffdc.ConformanceSuiteAuditCode;
import org.odpi.openmetadata.conformance.beans.OpenMetadataTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ArrayPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.MapPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.StructPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EnumDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EnumElementDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.CollectionDef;
import java.util.function.Predicate;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


import java.util.*;

import static org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE;

/**
 * OpenMetadataTestCase is the superclass for an open metadata conformance test.  It manages the
 * test environment and reporting.
 */
public abstract class RepositoryConformanceTestCase extends OpenMetadataTestCase
{
    private static final  String   assertion1    = "repository-test-case-base-01";
    private static final  String   assertionMsg1 = "Repository connector supplied to conformance suite.";
    private static final  String   assertion2    = "repository-test-case-base-02";
    private static final  String   assertionMsg2 = "Metadata collection for repository connector supplied to conformance suite.";

    protected RepositoryConformanceWorkPad repositoryConformanceWorkPad;
    protected OMRSRepositoryConnector      cohortRepositoryConnector = null;
    private   int                          maxSearchResults = 50;

    int       successfulExecutionCount = 0;
    int       unSuccessfulExecutionCount = 0;




    /**
     * Typical constructor used when the name of the test case id is fixed
     *
     * @param workPad location for workbench results
     * @param testCaseId identifier of test case
     * @param testCaseName name of test case
     * @param defaultProfileId identifier of default profile (for unexpected exceptions)
     * @param defaultRequirementId identifier of default required (for unexpected exceptions)
     */
    protected RepositoryConformanceTestCase(RepositoryConformanceWorkPad  workPad,
                                            String                        testCaseId,
                                            String                        testCaseName,
                                            Integer                       defaultProfileId,
                                            Integer                       defaultRequirementId)
    {
        super(workPad, testCaseId, testCaseName, defaultProfileId, defaultRequirementId);

        this.repositoryConformanceWorkPad = workPad;

        cohortRepositoryConnector = workPad.getTutRepositoryConnector();
        maxSearchResults = workPad.getMaxSearchResults();
    }

    /**
     * Typical constructor used when the test case id needs to be constructed by th test case code.
     *
     * @param workPad location for workbench results
     * @param defaultProfileId identifier of default profile (for unexpected exceptions)
     * @param defaultRequirementId identifier of default required (for unexpected exceptions)
     */
    protected RepositoryConformanceTestCase(RepositoryConformanceWorkPad  workPad,
                                            Integer                       defaultProfileId,
                                            Integer                       defaultRequirementId)
    {
        super(workPad, defaultProfileId, defaultRequirementId);

        this.repositoryConformanceWorkPad = workPad;

        if (workPad != null)
        {
            cohortRepositoryConnector = workPad.getTutRepositoryConnector();
            maxSearchResults = workPad.getMaxSearchResults();
        }
    }


    /**
     * Log that the test case is starting.
     *
     * @param methodName calling method name
     */
    protected void logTestStart(String methodName)
    {
        if (workPad != null)
        {
            AuditLog auditLog = repositoryConformanceWorkPad.getAuditLog();

            auditLog.logMessage(methodName,
                                ConformanceSuiteAuditCode.TEST_CASE_INITIALIZING.getMessageDefinition(testCaseId,
                                                                                                      testCaseDescriptionURL));
        }
    }


    /**
     * Log that the test case is ending.
     *
     * @param methodName calling method name
     */
    protected void logTestEnd(String methodName)
    {
        if (workPad != null)
        {
            int exceptionCount;

            if (exceptionBean == null)
            {
                exceptionCount = 0;
            }
            else
            {
                exceptionCount = 1;
            }

            AuditLog auditLog = repositoryConformanceWorkPad.getAuditLog();

            if (successMessage == null)
            {
                auditLog.logMessage(methodName,
                                    ConformanceSuiteAuditCode.TEST_CASE_COMPLETED.getMessageDefinition(testCaseId,
                                                                                                       Integer.toString(successfulAssertions.size()),
                                                                                                       Integer.toString(unsuccessfulAssertions.size()),
                                                                                                       Integer.toString(exceptionCount),
                                                                                                       Integer.toString(discoveredProperties.size())));
            }
            else
            {
                auditLog.logMessage(methodName,
                                    ConformanceSuiteAuditCode.TEST_CASE_COMPLETED_SUCCESSFULLY.getMessageDefinition(testCaseId,
                                                                                                                    Integer.toString(successfulAssertions.size()),
                                                                                                                    Integer.toString(unsuccessfulAssertions.size()),
                                                                                                                    Integer.toString(exceptionCount),
                                                                                                                    Integer.toString(discoveredProperties.size()),
                                                                                                                    successMessage));
            }
        }
    }


    /**
     * For test cases that are invoked many times, count the successful invocations.
     */
    protected void  incrementSuccessfulCount()
    {
        successfulExecutionCount ++;
    }


    /**
     * For test cases that are invoked many times, count the unsuccessful invocations.
     */
    protected void incrementUnsuccessfulCount()
    {
        unSuccessfulExecutionCount ++;
    }


    /**
     * Verify that the name of the type (which forms part of the test id) is not null.
     *
     * @param typeName name of the type being tested
     * @param rootTestCaseId base test case Id
     * @param testCaseName name of the test case
     * @return typeName (or "null" if null so messages are displayed properly.)
     */
    protected  String updateTestIdByType(String   typeName,
                                         String   rootTestCaseId,
                                         String   testCaseName)
    {
        String            testTypeName = typeName;

        if (testTypeName == null)
        {
            testTypeName = "<null>";
        }

        super.updateTestId(rootTestCaseId, rootTestCaseId + "-" + testTypeName, testCaseName);

        return testTypeName;
    }


    /**
     * Return the page size to use for testing the repository.
     *
     * @return page size
     */
    protected int getMaxSearchResults() {
        return maxSearchResults;
    }


    /**
     * Return the repository connector generated from the cohort registration event.
     *
     * @return OMRSRepositoryConnector object
     * @throws Exception if the connector is not properly set up.
     */
    protected OMRSRepositoryConnector getRepositoryConnector() throws Exception
    {
        assertCondition((cohortRepositoryConnector != null),
                        assertion1,
                        assertionMsg1,
                        RepositoryConformanceProfileRequirement.REPOSITORY_CONNECTOR.getProfileId(),
                        RepositoryConformanceProfileRequirement.REPOSITORY_CONNECTOR.getRequirementId());

        return cohortRepositoryConnector;
    }


    /**
     * Return the metadata collection used to call the repository.
     *
     * @return OMRSMetadataCollection object
     * @throws Exception if the connector is not properly set up.
     */
    protected OMRSMetadataCollection getMetadataCollection() throws Exception
    {
        OMRSMetadataCollection metadataCollection = null;

        if (cohortRepositoryConnector != null)
        {
            metadataCollection = cohortRepositoryConnector.getMetadataCollection();
        }

        assertCondition((cohortRepositoryConnector != null),
                        assertion1,
                        assertionMsg1,
                        RepositoryConformanceProfileRequirement.REPOSITORY_CONNECTOR.getProfileId(),
                        RepositoryConformanceProfileRequirement.REPOSITORY_CONNECTOR.getRequirementId());
        assertCondition((metadataCollection != null),
                        assertion2,
                        assertionMsg2,
                        RepositoryConformanceProfileRequirement.REPOSITORY_CONNECTOR.getProfileId(),
                        RepositoryConformanceProfileRequirement.REPOSITORY_CONNECTOR.getRequirementId());

        return metadataCollection;
    }


    /**
     * Return a generated value for any attribute type, not just the primitive ones.
     * <br>
     * The open metadata types use enumerations, arrays and maps as freely as they use primitives, so a test
     * that only ever populates primitive attributes leaves whole categories of the model unexercised - in
     * create, in update and in search alike.  A repository that stores or searches those values then has
     * nothing checking that it does so correctly.
     *
     * @param attributeName name of the attribute being populated
     * @param attributeType its type
     * @return a value to store, or null if no value can be generated for that type
     */
    protected InstancePropertyValue getPropertyValueForAttributeType(String           attributeName,
                                                                     AttributeTypeDef attributeType)
    {
        if (attributeType == null)
        {
            return null;
        }

        switch (attributeType.getCategory())
        {
            case PRIMITIVE ->
            {
                return this.getPrimitivePropertyValue(attributeName, (PrimitiveDef) attributeType);
            }

            case ENUM_DEF ->
            {
                return this.getEnumPropertyValue((EnumDef) attributeType);
            }

            case COLLECTION ->
            {
                return this.getCollectionPropertyValue(attributeName, (CollectionDef) attributeType);
            }
        }

        return null;
    }


    /**
     * Return a value for an enumeration attribute - the first value the enumeration defines, so it is always
     * one the type actually allows.
     *
     * @param enumDef the enumeration
     * @return property value, or null if the enumeration defines no values
     */
    private EnumPropertyValue getEnumPropertyValue(EnumDef enumDef)
    {
        List<EnumElementDef> elementDefs = enumDef.getElementDefs();

        if ((elementDefs == null) || (elementDefs.isEmpty()))
        {
            return null;
        }

        EnumElementDef    elementDef    = elementDefs.get(0);
        EnumPropertyValue propertyValue = new EnumPropertyValue();

        propertyValue.setTypeGUID(enumDef.getGUID());
        propertyValue.setTypeName(enumDef.getName());
        propertyValue.setOrdinal(elementDef.getOrdinal());
        propertyValue.setSymbolicName(elementDef.getValue());
        propertyValue.setDescription(elementDef.getDescription());

        return propertyValue;
    }


    /**
     * Return a value for an array or map attribute, holding one element whose value is generated the same way a
     * primitive attribute's would be - so a search for that value finds it wherever it is held.
     *
     * @param attributeName name of the attribute being populated
     * @param collectionDef the array or map definition
     * @return property value, or null if no value can be built for the collection's element types
     */
    private InstancePropertyValue getCollectionPropertyValue(String        attributeName,
                                                             CollectionDef collectionDef)
    {
        List<PrimitiveDefCategory> argumentTypes = collectionDef.getArgumentTypes();

        if ((argumentTypes == null) || (argumentTypes.isEmpty()))
        {
            return null;
        }

        switch (collectionDef.getCollectionDefCategory())
        {
            case OM_COLLECTION_ARRAY ->
            {
                PrimitivePropertyValue elementValue = this.getPrimitivePropertyValue(attributeName,
                                                                                     argumentTypes.get(argumentTypes.size() - 1));

                if (elementValue == null)
                {
                    return null;
                }

                Map<String, InstancePropertyValue> arrayElements = new HashMap<>();

                arrayElements.put("0", elementValue);

                InstanceProperties arrayProperties = new InstanceProperties();

                arrayProperties.setInstanceProperties(arrayElements);

                ArrayPropertyValue arrayPropertyValue = new ArrayPropertyValue();

                arrayPropertyValue.setTypeGUID(collectionDef.getGUID());
                arrayPropertyValue.setTypeName(collectionDef.getName());
                arrayPropertyValue.setArrayCount(1);
                arrayPropertyValue.setArrayValues(arrayProperties);

                return arrayPropertyValue;
            }

            case OM_COLLECTION_MAP ->
            {
                /*
                 * A collection's argument types are the key type followed by the value type.  Only maps keyed
                 * by string can be populated here, because an InstanceProperties map is keyed by string - and
                 * those are the only maps the open metadata types use.
                 */
                if ((argumentTypes.size() < 2) || (argumentTypes.get(0) != PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING))
                {
                    return null;
                }

                PrimitivePropertyValue entryValue = this.getPrimitivePropertyValue(attributeName, argumentTypes.get(1));

                if (entryValue == null)
                {
                    return null;
                }

                Map<String, InstancePropertyValue> mapEntries = new HashMap<>();

                mapEntries.put("Test" + attributeName + "Key", entryValue);

                InstanceProperties mapProperties = new InstanceProperties();

                mapProperties.setInstanceProperties(mapEntries);

                MapPropertyValue mapPropertyValue = new MapPropertyValue();

                mapPropertyValue.setTypeGUID(collectionDef.getGUID());
                mapPropertyValue.setTypeName(collectionDef.getName());
                mapPropertyValue.setMapValues(mapProperties);

                return mapPropertyValue;
            }
        }

        return null;
    }


    /**
     * Return a primitive value of the requested category, used for the elements held inside arrays and maps.
     *
     * @param propertyName name of the attribute the value belongs to
     * @param primitiveDefCategory category of value wanted
     * @return property value, or null if no value can be generated for that category
     */
    private PrimitivePropertyValue getPrimitivePropertyValue(String               propertyName,
                                                             PrimitiveDefCategory primitiveDefCategory)
    {
        if (primitiveDefCategory == null)
        {
            return null;
        }

        PrimitiveDef primitiveDef = new PrimitiveDef(primitiveDefCategory);

        primitiveDef.setGUID(primitiveDefCategory.getGUID());
        primitiveDef.setName(primitiveDefCategory.getName());

        PrimitivePropertyValue propertyValue = this.getPrimitivePropertyValue(propertyName, primitiveDef);

        if ((propertyValue != null) && (propertyValue.getPrimitiveValue() == null))
        {
            /*
             * The generator has no value for this category - OM_PRIMITIVE_TYPE_UNKNOWN, which is how the open
             * metadata types spell "object", as in map<string,object>.  Storing a value object with nothing in
             * it would put a property in the instance that has no value, so a string is used instead: "object"
             * accommodates one, and it gives these map entries a value that can be searched for and compared
             * like any other.
             */
            propertyValue.setPrimitiveValue("Test" + propertyName + "Value");
        }

        return propertyValue;
    }


    /**
     * Create a primitive property value for the requested property.
     *
     * @param propertyName name of the property
     * @param propertyType type of the property
     * @return PrimitiveTypePropertyValue object
     */
    private PrimitivePropertyValue getPrimitivePropertyValue(String        propertyName,
                                                             PrimitiveDef  propertyType)
    {
        PrimitivePropertyValue propertyValue = new PrimitivePropertyValue();

        propertyValue.setPrimitiveDefCategory(propertyType.getPrimitiveDefCategory());
        propertyValue.setTypeGUID(propertyType.getGUID());
        propertyValue.setTypeName(propertyType.getName());

        switch (propertyType.getPrimitiveDefCategory())
        {
            case OM_PRIMITIVE_TYPE_STRING:
                propertyValue.setPrimitiveValue("Test" + propertyName + "Value");
                break;
            case OM_PRIMITIVE_TYPE_DATE:
                Date date = new Date();              // Date and Time now
                Long timestamp = date.getTime();
                propertyValue.setPrimitiveValue(timestamp);    // Dates are stored as Long values
                break;
            case OM_PRIMITIVE_TYPE_INT:
                propertyValue.setPrimitiveValue(42);
                break;
            case OM_PRIMITIVE_TYPE_BOOLEAN:
                propertyValue.setPrimitiveValue(true);
                break;
            case OM_PRIMITIVE_TYPE_SHORT:
                propertyValue.setPrimitiveValue(Short.valueOf("34"));
                break;
            case OM_PRIMITIVE_TYPE_BYTE:
                propertyValue.setPrimitiveValue(Byte.valueOf("7"));
                break;
            case OM_PRIMITIVE_TYPE_CHAR:
                propertyValue.setPrimitiveValue('o');
                break;
            case OM_PRIMITIVE_TYPE_LONG:
                propertyValue.setPrimitiveValue(2452L);
                break;
            case OM_PRIMITIVE_TYPE_FLOAT:
                propertyValue.setPrimitiveValue(245332f);
                break;
            case OM_PRIMITIVE_TYPE_DOUBLE:
                propertyValue.setPrimitiveValue(2459992d);
                break;
            case OM_PRIMITIVE_TYPE_BIGDECIMAL:
                propertyValue.setPrimitiveValue(245339992d);
                break;
            case OM_PRIMITIVE_TYPE_BIGINTEGER:
                propertyValue.setPrimitiveValue(245559992d);
                break;
            case OM_PRIMITIVE_TYPE_UNKNOWN:
                break;
        }

        return propertyValue;
    }


    /**
     * Create a primitive property value for the requested property.
     *
     * @param propertyName name of the property
     * @param propertyType type of the property
     * @param attrUnique whether this property value should be distinct (or common)
     * @param instanceCount counter to be used in customisation of distinct values
     * @return PrimitiveTypePropertyValue object
     */
    private PrimitivePropertyValue getPrimitivePropertyValue(String        propertyName,
                                                             PrimitiveDef  propertyType,
                                                             boolean       attrUnique,
                                                             int           instanceCount)
    {
        PrimitivePropertyValue propertyValue = new PrimitivePropertyValue();

        propertyValue.setPrimitiveDefCategory(propertyType.getPrimitiveDefCategory());
        propertyValue.setTypeGUID(propertyType.getGUID());
        propertyValue.setTypeName(propertyType.getName());

       boolean distinct = attrUnique || (instanceCount%2)!=0;

        switch (propertyType.getPrimitiveDefCategory())
        {
            case OM_PRIMITIVE_TYPE_STRING:
                if (distinct)
                    propertyValue.setPrimitiveValue(propertyName + "." + instanceCount);
                else
                    propertyValue.setPrimitiveValue(propertyName);
                break;
            case OM_PRIMITIVE_TYPE_DATE:
                Date date = new Date();                        // Date and Time now - these are always distinct
                Long timestamp = date.getTime();
                propertyValue.setPrimitiveValue(timestamp);    // Dates are stored as Long values
                break;
            case OM_PRIMITIVE_TYPE_INT:
                if (distinct)
                    propertyValue.setPrimitiveValue(42 + instanceCount);    // instanceCount is never 0 for distinct cases
                else
                    propertyValue.setPrimitiveValue(42);
                break;
            case OM_PRIMITIVE_TYPE_BOOLEAN:
                propertyValue.setPrimitiveValue(instanceCount%2==0);        // every other one is false
                break;
            case OM_PRIMITIVE_TYPE_SHORT:
                if (distinct)
                    propertyValue.setPrimitiveValue((short)(3+ instanceCount));  // instanceCount is never 0 for distinct cases
                else
                    propertyValue.setPrimitiveValue((short)3);
                break;
            case OM_PRIMITIVE_TYPE_BYTE:
                propertyValue.setPrimitiveValue((byte)(1 + instanceCount));    // always distinct
                break;
            case OM_PRIMITIVE_TYPE_CHAR:
                propertyValue.setPrimitiveValue('o');                  // never distinct
                break;
            case OM_PRIMITIVE_TYPE_LONG:
                if (distinct)
                    propertyValue.setPrimitiveValue((long)(2452 + instanceCount));  // instanceCount is never 0 for distinct cases
                else
                    propertyValue.setPrimitiveValue((long)(2452));
                break;
            case OM_PRIMITIVE_TYPE_FLOAT:
                if (distinct)
                    propertyValue.setPrimitiveValue((float)(245332+instanceCount));  // instanceCount is never 0 for distinct cases
                else
                    propertyValue.setPrimitiveValue((float)(245332));
                break;
            case OM_PRIMITIVE_TYPE_DOUBLE:
                if (distinct)
                    propertyValue.setPrimitiveValue((double)(2459992+instanceCount));  // instanceCount is never 0 for distinct cases
                else
                    propertyValue.setPrimitiveValue((double)(2459992));
                break;
            case OM_PRIMITIVE_TYPE_BIGDECIMAL:
            case OM_PRIMITIVE_TYPE_BIGINTEGER:
                if (distinct)
                    propertyValue.setPrimitiveValue((double)(245339992+instanceCount));  // instanceCount is never 0 for distinct cases
                else
                    propertyValue.setPrimitiveValue((double)(245339992));
                break;
            case OM_PRIMITIVE_TYPE_UNKNOWN:
                break;
        }

        return propertyValue;
    }

    /**
     * Return instance properties for the properties defined in the TypeDef, but do not include properties from supertypes.
     *
     * @param typeDefAttributes  attributes defined for a specific type
     * @return properties for an instance of this type
     */
    protected InstanceProperties  getPropertiesForInstance(List<TypeDefAttribute> typeDefAttributes)
    {
        InstanceProperties   properties = null;

        if (typeDefAttributes != null)
        {
            Map<String, InstancePropertyValue> propertyMap = new HashMap<>();


            for (TypeDefAttribute  typeDefAttribute : typeDefAttributes)
            {
                String                   attributeName = typeDefAttribute.getAttributeName();
                AttributeTypeDef         attributeType = typeDefAttribute.getAttributeType();
                AttributeTypeDefCategory category = attributeType.getCategory();

                InstancePropertyValue propertyValue = this.getPropertyValueForAttributeType(attributeName, attributeType);

                if (propertyValue != null)
                {
                    propertyMap.put(attributeName, propertyValue);
                }
            }

            if (! propertyMap.isEmpty())
            {
                properties = new InstanceProperties();
                properties.setInstanceProperties(propertyMap);
            }
        }


        return properties;
    }


    /**
     * Return instance properties for the properties defined in the TypeDef and all of its supertypes.
     *
     * @param userId calling user
     * @param typeDef  the definition of the type
     * @return properties for an instance of this type
     * @throws Exception problem manipulating types
     */
    protected InstanceProperties  getAllPropertiesForInstance(String userId, TypeDef typeDef) throws Exception
    {
        InstanceProperties   properties = null;

        // Recursively gather all the TypeDefAttributes for the supertype hierarchy...
        List<TypeDefAttribute> allTypeDefAttributes = getPropertiesForTypeDef(userId, typeDef);

        if (allTypeDefAttributes != null)
        {
            Map<String, InstancePropertyValue> propertyMap = new HashMap<>();

            for (TypeDefAttribute  typeDefAttribute : allTypeDefAttributes)
            {
                String                   attributeName = typeDefAttribute.getAttributeName();
                AttributeTypeDef         attributeType = typeDefAttribute.getAttributeType();
                AttributeTypeDefCategory category = attributeType.getCategory();

                /*
                 * Every attribute type is populated here, not just the primitives - enumerations, arrays and
                 * maps are used throughout the open metadata types, and a repository is only exercised on
                 * them if the instances this test creates actually carry them.
                 */
                InstancePropertyValue propertyValue = this.getPropertyValueForAttributeType(attributeName, attributeType);

                if (propertyValue != null)
                {
                    propertyMap.put(attributeName, propertyValue);
                }
            }

            if (! propertyMap.isEmpty())
            {
                properties = new InstanceProperties();
                properties.setInstanceProperties(propertyMap);
            }
        }

        return properties;

    }

    /**
     * Return generated instance properties specialised for search tests
     *
     * @param userId calling user
     * @param allTypeDefAttributes list of all the TDAs for type
     * @param instanceCount a generator parameter for customisation of individual instance properties
     * @return properties for an instance of this type
     * @throws Exception problem manipulating types
     */
    protected InstanceProperties  generatePropertiesForInstance(String userId, List<TypeDefAttribute> allTypeDefAttributes, int instanceCount) throws Exception
    {
        InstanceProperties   properties = null;

        if (allTypeDefAttributes != null)
        {
            Map<String, InstancePropertyValue> propertyMap = new HashMap<>();

            for (TypeDefAttribute  typeDefAttribute : allTypeDefAttributes)
            {
                String                   attributeName = typeDefAttribute.getAttributeName();
                AttributeTypeDef         attributeType = typeDefAttribute.getAttributeType();
                AttributeTypeDefCategory category      = attributeType.getCategory();
                boolean                  attrUnique    = typeDefAttribute.isUnique();

                if (category == AttributeTypeDefCategory.PRIMITIVE)
                {
                    PrimitiveDef primitiveDef = (PrimitiveDef) attributeType;
                    propertyMap.put(attributeName, this.getPrimitivePropertyValue(attributeName, primitiveDef, attrUnique, instanceCount));
                }
            }

            if (! propertyMap.isEmpty())
            {
                properties = new InstanceProperties();
                properties.setInstanceProperties(propertyMap);
            }
        }

        return properties;

    }


    /**
     * Return only the unique properties for the properties defined in the TypeDef and all of its supertypes.
     *
     * @param userId calling user
     * @param typeDef  the definition of the type
     * @return properties for an instance of this type
     * @throws Exception problem manipulating types
     */
    protected InstanceProperties  getAllUniquePropertiesForInstance(String userId, TypeDef typeDef) throws Exception
    {
        InstanceProperties   properties = null;

        // Recursively gather all the TypeDefAttributes for the supertype hierarchy...
        List<TypeDefAttribute> allTypeDefAttributes = getPropertiesForTypeDef(userId, typeDef);

        if (allTypeDefAttributes != null)
        {
            Map<String, InstancePropertyValue> propertyMap = new HashMap<>();


            for (TypeDefAttribute  typeDefAttribute : allTypeDefAttributes)
            {
                String                   attributeName = typeDefAttribute.getAttributeName();
                AttributeTypeDef         attributeType = typeDefAttribute.getAttributeType();
                AttributeTypeDefCategory category = attributeType.getCategory();

                if (typeDefAttribute.isUnique())
                {
                    if (category == AttributeTypeDefCategory.PRIMITIVE)
                    {
                        PrimitiveDef primitiveDef = (PrimitiveDef) attributeType;
                        propertyMap.put(attributeName, this.getPrimitivePropertyValue(attributeName, primitiveDef));
                    }
                }
            }

            if (! propertyMap.isEmpty())
            {
                properties = new InstanceProperties();
                properties.setInstanceProperties(propertyMap);
            }
        }

        return properties;

    }


    /**
     * Does any value anywhere in these properties satisfy the caller's match test?
     * <br>
     * findEntitiesByPropertyValue and findRelationshipsByPropertyValue match against the string values held in an
     * instance, and "held in" reaches inside the structured property types.  The shared implementation of that
     * rule - OMRSRepositoryContentValidator.verifyInstancePropertiesMatchSearchCriteria - matches on a primitive
     * string, on an enum's symbolic name, or on any value nested inside a struct, an array or a map.  A check
     * that only looked at primitive strings would call a repository wrong for correctly returning an instance
     * whose matching value happened to live in a map or an array, which is a legitimate result.
     *
     * @param properties properties of the instance that was returned
     * @param matches decides whether one string value satisfies the search
     * @return whether some value in the instance justifies it being returned
     */
    protected boolean propertyValueMatchesSearch(InstanceProperties  properties,
                                                 Predicate<String>   matches)
    {
        if ((properties == null) || (properties.getInstanceProperties() == null))
        {
            return false;
        }

        for (InstancePropertyValue propertyValue : properties.getInstanceProperties().values())
        {
            if (this.propertyValueMatchesSearch(propertyValue, matches))
            {
                return true;
            }
        }

        return false;
    }


    /**
     * Does this value - or any value nested inside it - satisfy the caller's match test?
     *
     * @param propertyValue value to test
     * @param matches decides whether one string value satisfies the search
     * @return whether this value justifies the instance being returned
     */
    protected boolean propertyValueMatchesSearch(InstancePropertyValue propertyValue,
                                                 Predicate<String>     matches)
    {
        if (propertyValue == null)
        {
            return false;
        }

        switch (propertyValue.getInstancePropertyCategory())
        {
            case PRIMITIVE ->
            {
                PrimitivePropertyValue primitivePropertyValue = (PrimitivePropertyValue) propertyValue;

                if (primitivePropertyValue.getPrimitiveDefCategory() == PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING)
                {
                    Object primitiveValue = primitivePropertyValue.getPrimitiveValue();

                    return (primitiveValue != null) && (matches.test((String) primitiveValue));
                }
            }

            case ENUM ->
            {
                String symbolicName = ((EnumPropertyValue) propertyValue).getSymbolicName();

                return (symbolicName != null) && (matches.test(symbolicName));
            }

            case ARRAY ->
            {
                return this.propertyValueMatchesSearch(((ArrayPropertyValue) propertyValue).getArrayValues(), matches);
            }

            case MAP ->
            {
                return this.propertyValueMatchesSearch(((MapPropertyValue) propertyValue).getMapValues(), matches);
            }

            case STRUCT ->
            {
                return this.propertyValueMatchesSearch(((StructPropertyValue) propertyValue).getAttributes(), matches);
            }
        }

        return false;
    }


    /**
     * Return instance properties for only the mandatory properties defined in the TypeDef and all of its supertypes.
     *
     * @param userId calling user
     * @param typeDef  the definition of the type
     * @return properties for an instance of this type
     * @throws Exception problem manipulating types
     */
    protected InstanceProperties  getMinPropertiesForInstance(String userId, TypeDef typeDef) throws Exception
    {
        /*
         * Recursively gather all the TypeDefAttributes for the supertype hierarchy...
         */
        List<TypeDefAttribute> allTypeDefAttributes = getPropertiesForTypeDef(userId, typeDef);
        Map<String, InstancePropertyValue> propertyMap = new HashMap<>();

        if (allTypeDefAttributes != null)
        {
            for (TypeDefAttribute  typeDefAttribute : allTypeDefAttributes)
            {
                String                   attributeName = typeDefAttribute.getAttributeName();
                AttributeTypeDef         attributeType = typeDefAttribute.getAttributeType();
                AttributeTypeDefCategory category = attributeType.getCategory();
                AttributeCardinality     attributeCardinality = typeDefAttribute.getAttributeCardinality();

                /*
                 * ONE_ONLY belongs here as much as the AT_LEAST_ONE cardinalities do: it means "1 for one
                 * instance, no more and no less", so an attribute declared that way is mandatory.  It is also
                 * the common case - qualifiedName is ONE_ONLY - so leaving it out made this return an empty
                 * property set for most types, and an update that strips a type's mandatory identifying
                 * property is one a repository is right to refuse.
                 */
                if (attributeCardinality == AttributeCardinality.ONE_ONLY                ||
                    attributeCardinality == AttributeCardinality.AT_LEAST_ONE_ORDERED    ||
                    attributeCardinality == AttributeCardinality.AT_LEAST_ONE_UNORDERED)
                {
                    InstancePropertyValue propertyValue = this.getPropertyValueForAttributeType(attributeName, attributeType);

                    if (propertyValue != null)
                    {
                        propertyMap.put(attributeName, propertyValue);
                    }
                }
            }
        }

        /*
         * Get an InstanceProperties, even if there are no properties in the propertyMap - you cannot pass a null
         * to updateEntityProperties. So if necessary, pass an empty InstanceProperties object.
         */
        InstanceProperties properties = new InstanceProperties();
        properties.setInstanceProperties(propertyMap);

        return properties;

    }

    /**
     * Recursively walk the supertype hierarchy starting at the given typeDef, and collect all the TypeDefAttributes.
     *
     * This method does not use the properties defined in the TypeDef provided since that TypeDef is
     * from the gallery returned by the repository connector. Instead it uses the name of the TypeDef
     * to look up the TypeDef in the RepositoryHelper - using the Known types rather than the Active types.
     * This is to ensure consistency with the open metadata type definition.
     *
     *
     * @param userId   the userId of the caller, needed for retrieving type definitions
     * @param typeDef  the definition of the type
     * @return properties for an instance of this type
     */
    protected List<TypeDefAttribute> getPropertiesForTypeDef(String userId, TypeDef typeDef)
    {

        OMRSRepositoryHelper repositoryHelper = cohortRepositoryConnector.getRepositoryHelper();


        List<TypeDefAttribute> propDefs = new ArrayList<>();

        /*
         * Look at the supertype (if any) first and then get any properties for the current type def
         */

        /*
         * Move up the supertype hierarchy until you hit the top
         */
        if (typeDef.getSuperType() != null)
        {
            TypeDefLink superTypeDefLink = typeDef.getSuperType();
            String superTypeName = superTypeDefLink.getName();
            TypeDef superTypeDef = repositoryHelper.getTypeDefByName(userId, superTypeName);
            List<TypeDefAttribute> inheritedProps = getPropertiesForTypeDef(userId, superTypeDef);

            if (inheritedProps != null && !inheritedProps.isEmpty())
            {
                propDefs.addAll(inheritedProps);
            }

        }

        /*
         * Add any non-deprecated properties defined for the current type, again using the known type from the repository helper
         */
        TypeDef knownTypeDef = repositoryHelper.getTypeDefByName(userId, typeDef.getName());
        List<TypeDefAttribute> currentTypePropDefs = knownTypeDef.getPropertiesDefinition();

        if (currentTypePropDefs != null && !currentTypePropDefs.isEmpty())
        {
            for (TypeDefAttribute tda : currentTypePropDefs)
            {
                if (tda.getAttributeStatus() != DEPRECATED_ATTRIBUTE)
                {
                    propDefs.add(tda);
                }
            }

        }

        return propDefs;
    }


    /**
     * Returns the appropriate entity definition for the supplied entity identifiers.
     * This may be the entity specified, or a subclass of the entity that is supported.
     *
     * @param supportedEntityDefs map of entity type name to entity type definition
     * @param entityIdentifiers guid and name of desired entity definition
     * @return entity definition (EntityDef)
     */
    public EntityDef  getEntityDef(Map<String, EntityDef>  supportedEntityDefs,
                                   TypeDefLink             entityIdentifiers)
    {
        EntityDef  entityDef = null;

        OMRSRepositoryHelper repositoryHelper = cohortRepositoryConnector.getRepositoryHelper();
        /*
         * Need to look up entity def (or a suitable subclass).
         */
        String candidateTypeDef = entityIdentifiers.getName();
        for (EntityDef supportedEntityDef : supportedEntityDefs.values())
        {
            if (repositoryHelper.isTypeOf(cohortRepositoryConnector.getRepositoryName(), supportedEntityDef.getName(), candidateTypeDef)) {
                entityDef = supportedEntityDef;
                break;
            }
        }
        return entityDef;

    }


    /**
     * Adds an entity of the requested type to the repository.
     *
     * @param userId userId for the new entity
     * @param metadataCollection metadata connection to access the repository
     * @param entityDef type of entity to create
     * @return new entity
     * @throws Exception error in create
     */
    public EntityDetail addEntityToRepository(String                   userId,
                                              OMRSMetadataCollection   metadataCollection,
                                              EntityDef                entityDef) throws Exception
    {
        /*
         * Supply all properties for the instance, including those inherited from supertypes, since they may be mandatory.
         * An alternative here would be to use getMinPropertiesForInstance, but providing all properties creates a logically
         * complete entity
         */
        InstanceProperties properties = this.getAllPropertiesForInstance(userId, entityDef);

        return metadataCollection.addEntity(userId, entityDef.getGUID(), properties, null, null );
    }

    /**
     * Adds an entity proxy of the requested type to the repository.
     *
     * @param userId userId for the new entity
     * @param metadataCollection metadata connection to access the repository
     * @param entityDef type of entity to create
     * @param homeMetadataCollectionId - metadataCollectionId of the repository that is master for instance
     * @return string - entity proxy's GUID
     * @throws Exception error in create
     */
    public String addEntityProxyToRepository(String                   userId,
                                             OMRSMetadataCollection   metadataCollection,
                                             EntityDef                entityDef,
                                             String                   homeMetadataCollectionId) throws Exception
    {
        /*
         * Supply all unique properties for the instance, including those inherited from supertypes, since they may be mandatory.
         */
        InstanceProperties uniqueProperties = this.getAllUniquePropertiesForInstance(userId, entityDef);

        OMRSRepositoryHelper repositoryHelper = cohortRepositoryConnector.getRepositoryHelper();

        EntityProxy entityProxy =  repositoryHelper.getNewEntityProxy(cohortRepositoryConnector.getRepositoryName(),
                                                                      homeMetadataCollectionId,
                                                                      InstanceProvenanceType.LOCAL_COHORT,
                                                                      userId,
                                                                      entityDef.getName(),
                                                                      uniqueProperties,
                                                                      null);


        metadataCollection.addEntityProxy(userId, entityProxy);

        return entityProxy.getGUID();

    }


    protected String buildExceptionMessage(String testName,
                                           String methodName,
                                           String operationDescription,
                                           Map<String,String> parameters,
                                           String originalExceptionClassName,
                                           String originalExceptionMessage) {

        StringBuilder msg = new StringBuilder();
        msg.append("CTS test ").append(testName);
        msg.append(" caught exception ").append(originalExceptionClassName);
        msg.append(" from method ").append(methodName);
        msg.append(" whilst trying to ").append(operationDescription);
        msg.append(". ");
        msg.append(" Exception message was : ").append(originalExceptionMessage);
        msg.append(". ");
        msg.append(" Method was invoked with parameters: ");
        if (parameters != null) {
            Set<String> keys = parameters.keySet();
            Iterator<String> keyIter = keys.iterator();
            if (keyIter.hasNext()) {
                String key = keyIter.next();
                msg.append(key).append(" : ").append(parameters.get(key));
                while (keyIter.hasNext()) {
                    msg.append(", ");
                    key = keyIter.next();
                    msg.append(key).append(" : ").append(parameters.get(key));
                }
            }
        }
        return msg.toString();
    }

}
