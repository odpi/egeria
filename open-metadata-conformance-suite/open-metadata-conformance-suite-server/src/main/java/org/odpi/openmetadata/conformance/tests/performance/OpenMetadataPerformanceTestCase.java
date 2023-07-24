/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.performance;

import org.odpi.openmetadata.conformance.ffdc.ConformanceSuiteAuditCode;
import org.odpi.openmetadata.conformance.beans.OpenMetadataTestCase;
import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceWorkPad;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


import java.util.*;

import static org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE;

/**
 * OpenMetadataPerformanceTestCase is the superclass for an open metadata performance test.  It manages the
 * test environment and reporting.
 */
public abstract class OpenMetadataPerformanceTestCase extends OpenMetadataTestCase
{
    private static final  String   assertion1    = "repository-performance-test-case-base-01";
    private static final  String   assertionMsg1 = "Repository connector supplied to performance suite.";
    private static final  String   assertion2    = "repository-performance-test-case-base-02";
    private static final  String   assertionMsg2 = "Metadata collection for repository connector supplied to performance suite.";

    protected PerformanceWorkPad           performanceWorkPad;
    protected OMRSRepositoryConnector      cohortRepositoryConnector = null;

    int       successfulExecutionCount = 0;
    int       unSuccessfulExecutionCount = 0;


    /**
     * Typical constructor used when the name of the test case id is fixed
     *
     * @param workPad location for workbench results
     * @param testCaseId identifier of test case
     * @param testCaseName name of test case
     * @param defaultProfileId identifier of default profile (for unexpected exceptions)
     */
    protected OpenMetadataPerformanceTestCase(PerformanceWorkPad  workPad,
                                              String              testCaseId,
                                              String              testCaseName,
                                              Integer             defaultProfileId)
    {
        super(workPad, testCaseId, testCaseName, defaultProfileId, null);

        this.performanceWorkPad = workPad;

        if (workPad != null)
        {
            cohortRepositoryConnector = workPad.getTutRepositoryConnector();
        }
    }

    /**
     * Typical constructor used when the test case id needs to be constructed by th test case code.
     *
     * @param workPad location for workbench results
     * @param defaultProfileId identifier of default profile (for unexpected exceptions)
     */
    protected OpenMetadataPerformanceTestCase(PerformanceWorkPad  workPad,
                                              Integer             defaultProfileId)
    {
        super(workPad, defaultProfileId, null);

        this.performanceWorkPad = workPad;

        if (workPad != null)
        {
            cohortRepositoryConnector = workPad.getTutRepositoryConnector();
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
            AuditLog auditLog = performanceWorkPad.getAuditLog();

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

            AuditLog auditLog = performanceWorkPad.getAuditLog();

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
     * Return the number of instances that should exist per type in the repository.
     *
     * @return number of instances per type
     */
    protected int getInstancesPerType() {
        return performanceWorkPad.getInstancesPerType();
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
                null);

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
                null);
        assertCondition((metadataCollection != null),
                assertion2,
                assertionMsg2,
                RepositoryConformanceProfileRequirement.REPOSITORY_CONNECTOR.getProfileId(),
                null);

        return metadataCollection;
    }


    /**
     * Return the repository helper for the repository.
     *
     * @return OMRSRepositoryHelper object
     * @throws Exception if the connector is not properly set up.
     */
    protected OMRSRepositoryHelper getRepositoryHelper() throws Exception
    {
        OMRSRepositoryHelper repositoryHelper = null;
        if (cohortRepositoryConnector != null)
        {
            repositoryHelper = cohortRepositoryConnector.getRepositoryHelper();
        }
        return repositoryHelper;
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
    protected PrimitivePropertyValue getPrimitivePropertyValue(String        propertyName,
                                                               PrimitiveDef  propertyType,
                                                               boolean       attrUnique,
                                                               int           instanceCount)
    {
        PrimitivePropertyValue propertyValue = new PrimitivePropertyValue();

        boolean distinct = attrUnique || (instanceCount % 2) != 0;
        SplittableRandom random = new SplittableRandom();
        propertyValue.setPrimitiveDefCategory(propertyType.getPrimitiveDefCategory());

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
            case OM_PRIMITIVE_TYPE_SHORT:
                if (distinct)
                    propertyValue.setPrimitiveValue(getInstancesPerType() + instanceCount);
                else
                    propertyValue.setPrimitiveValue(random.nextInt(0, getInstancesPerType()));
                break;
            case OM_PRIMITIVE_TYPE_BOOLEAN:
                propertyValue.setPrimitiveValue(random.nextBoolean());
                break;
            case OM_PRIMITIVE_TYPE_BYTE:
                if (distinct)
                    propertyValue.setPrimitiveValue((byte) (getInstancesPerType() + instanceCount));
                else
                    propertyValue.setPrimitiveValue((byte) random.nextInt(0, getInstancesPerType()));
                break;
            case OM_PRIMITIVE_TYPE_CHAR:
                propertyValue.setPrimitiveValue(Character.forDigit(random.nextInt(0, getInstancesPerType() % 10), 10));
                break;
            case OM_PRIMITIVE_TYPE_LONG:
                if (distinct)
                    propertyValue.setPrimitiveValue((10 * getInstancesPerType()) + instanceCount);
                else
                    propertyValue.setPrimitiveValue(random.nextLong(0, 10 * getInstancesPerType()));
                break;
            case OM_PRIMITIVE_TYPE_FLOAT:
                if (distinct)
                    propertyValue.setPrimitiveValue((1.1 * getInstancesPerType()) + instanceCount);
                else
                    propertyValue.setPrimitiveValue(random.nextDouble(0, 1.1 * getInstancesPerType()));
                break;
            case OM_PRIMITIVE_TYPE_DOUBLE:
                if (distinct)
                    propertyValue.setPrimitiveValue((10.123 * getInstancesPerType()) + instanceCount);
                else
                    propertyValue.setPrimitiveValue(random.nextDouble(0, 10.123 * getInstancesPerType()));
                break;
            case OM_PRIMITIVE_TYPE_BIGDECIMAL:
            case OM_PRIMITIVE_TYPE_BIGINTEGER:
                if (distinct)
                    propertyValue.setPrimitiveValue((1000000 * getInstancesPerType()) + instanceCount);
                else
                    propertyValue.setPrimitiveValue(random.nextLong(0, 1000000 * getInstancesPerType()));
                break;
            case OM_PRIMITIVE_TYPE_UNKNOWN:
                break;
        }

        return propertyValue;
    }


    /**
     * Return instance properties for the properties defined in the TypeDef and all of its supertypes.
     *
     * @param userId calling user
     * @param typeDef  the definition of the type
     * @param instanceCount a generator parameter for customisation of individual instance properties
     * @return properties for an instance of this type
     * @throws Exception problem manipulating types
     */
    protected InstanceProperties  getAllPropertiesForInstance(String userId, TypeDef typeDef, int instanceCount) throws Exception
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

                if (category == AttributeTypeDefCategory.PRIMITIVE) {
                    PrimitiveDef primitiveDef = (PrimitiveDef) attributeType;
                    propertyMap.put(attributeName, this.getPrimitivePropertyValue(attributeName, primitiveDef, typeDefAttribute.isUnique(), instanceCount));
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

                if (category == AttributeTypeDefCategory.PRIMITIVE) {
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


    protected String buildExceptionMessage(String testName,
                                           String methodName,
                                           String operationDescription,
                                           Map<String,String> parameters,
                                           Exception cause) {

        StringBuilder msg = new StringBuilder();
        msg.append("CTS test ").append(testName);
        msg.append(" caught exception ").append(cause.getClass().getSimpleName());
        msg.append(" from method ").append(methodName);
        msg.append(" whilst trying to ").append(operationDescription);
        msg.append(". ");
        msg.append(" Exception message was : ").append(cause.getMessage());
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
