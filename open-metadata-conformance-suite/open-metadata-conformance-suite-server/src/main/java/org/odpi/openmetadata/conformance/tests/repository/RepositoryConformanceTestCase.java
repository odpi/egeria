/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository;

import org.odpi.openmetadata.conformance.auditlog.ConformanceSuiteAuditCode;
import org.odpi.openmetadata.conformance.beans.OpenMetadataTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.*;

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
            OMRSAuditLog auditLog = repositoryConformanceWorkPad.getAuditLog();

            ConformanceSuiteAuditCode auditCode = ConformanceSuiteAuditCode.TEST_CASE_INITIALIZING;
            auditLog.logRecord(methodName,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(testCaseId,
                                                                testCaseDescriptionURL),
                               null,
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());
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
            Integer    exceptionCount;

            if (exceptionBean == null)
            {
                exceptionCount = 0;
            }
            else
            {
                exceptionCount = 1;
            }

            OMRSAuditLog auditLog = repositoryConformanceWorkPad.getAuditLog();

            if (successMessage == null)
            {
                ConformanceSuiteAuditCode auditCode = ConformanceSuiteAuditCode.TEST_CASE_COMPLETED;
                auditLog.logRecord(methodName,
                                   auditCode.getLogMessageId(),
                                   auditCode.getSeverity(),
                                   auditCode.getFormattedLogMessage(testCaseId,
                                                                    Integer.toString(successfulAssertions.size()),
                                                                    Integer.toString(unsuccessfulAssertions.size()),
                                                                    Integer.toString(exceptionCount),
                                                                    Integer.toString(discoveredProperties.size())),
                                   null,
                                   auditCode.getSystemAction(),
                                   auditCode.getUserAction());
            }
            else
            {
                ConformanceSuiteAuditCode auditCode = ConformanceSuiteAuditCode.TEST_CASE_COMPLETED_SUCCESSFULLY;
                auditLog.logRecord(methodName,
                                   auditCode.getLogMessageId(),
                                   auditCode.getSeverity(),
                                   auditCode.getFormattedLogMessage(testCaseId,
                                                                    Integer.toString(successfulAssertions.size()),
                                                                    Integer.toString(unsuccessfulAssertions.size()),
                                                                    Integer.toString(exceptionCount),
                                                                    Integer.toString(discoveredProperties.size()),
                                                                    successMessage),
                                   null,
                                   auditCode.getSystemAction(),
                                   auditCode.getUserAction());
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
     * @return typeName (or "<null>" if null so messages are displayed properly.)
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
     * Create a primitive property value for the requested property.
     *
     * @param propertyName name of the property
     * @param propertyType type of the property
     * @return PrimitivePropertyValue object
     */
    private PrimitivePropertyValue getPrimitivePropertyValue(String        propertyName,
                                                             PrimitiveDef  propertyType)
    {
        PrimitivePropertyValue propertyValue = new PrimitivePropertyValue();

        propertyValue.setPrimitiveDefCategory(propertyType.getPrimitiveDefCategory());

        switch (propertyType.getPrimitiveDefCategory())
        {
            case OM_PRIMITIVE_TYPE_STRING:
                propertyValue.setPrimitiveValue("Test" + propertyName + "Value");
                break;
            case OM_PRIMITIVE_TYPE_DATE:
                propertyValue.setPrimitiveValue(new Date());
                break;
            case OM_PRIMITIVE_TYPE_INT:
                propertyValue.setPrimitiveValue(42);
                break;
            case OM_PRIMITIVE_TYPE_BOOLEAN:
                propertyValue.setPrimitiveValue(true);
                break;
            case OM_PRIMITIVE_TYPE_SHORT:
                propertyValue.setPrimitiveValue(new Short("34"));
                break;
            case OM_PRIMITIVE_TYPE_BYTE:
                propertyValue.setPrimitiveValue(new Byte("7"));
                break;
            case OM_PRIMITIVE_TYPE_CHAR:
                propertyValue.setPrimitiveValue(new Character('o'));
                break;
            case OM_PRIMITIVE_TYPE_LONG:
                propertyValue.setPrimitiveValue(new Long(2452));
                break;
            case OM_PRIMITIVE_TYPE_FLOAT:
                propertyValue.setPrimitiveValue(new Float(245332));
                break;
            case OM_PRIMITIVE_TYPE_DOUBLE:
                propertyValue.setPrimitiveValue(new Double(2459992));
                break;
            case OM_PRIMITIVE_TYPE_BIGDECIMAL:
                propertyValue.setPrimitiveValue(new Double(245339992));
                break;
            case OM_PRIMITIVE_TYPE_BIGINTEGER:
                propertyValue.setPrimitiveValue(new Double(245559992));
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

                switch(category)
                {
                    case PRIMITIVE:
                        PrimitiveDef  primitiveDef = (PrimitiveDef)attributeType;
                        propertyMap.put(attributeName, this.getPrimitivePropertyValue(attributeName, primitiveDef));
                        break;
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
     * Return instance properties for the properties defined in the TypeDef and all of its supertypes
     *
     * @param typeDef  the definition of the type
     * @return properties for an instance of this type
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

                switch(category)
                {
                    case PRIMITIVE:
                        PrimitiveDef  primitiveDef = (PrimitiveDef)attributeType;
                        propertyMap.put(attributeName, this.getPrimitivePropertyValue(attributeName, primitiveDef));
                        break;
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
     * Return instance properties for only the mandatory properties defined in the TypeDef and all of its supertypes
     *
     * @param typeDef  the definition of the type
     * @return properties for an instance of this type
     */
    protected InstanceProperties  getMinPropertiesForInstance(String userId, TypeDef typeDef) throws Exception {

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

                if (attributeCardinality == AttributeCardinality.AT_LEAST_ONE_ORDERED    ||
                    attributeCardinality == AttributeCardinality.AT_LEAST_ONE_UNORDERED) {

                    switch (category) {
                        case PRIMITIVE:
                            PrimitiveDef primitiveDef = (PrimitiveDef) attributeType;
                            propertyMap.put(attributeName, this.getPrimitivePropertyValue(attributeName, primitiveDef));
                            break;
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
     * Recursively walk the supertype hierarchy starting at the given typeDef, and collect all the TypeDefAttributes
     *
     * @param userId   the userId of the caller, needed for retrieving type definitions
     * @param typeDef  the definition of the type
     * @return properties for an instance of this type
     */
    protected List<TypeDefAttribute> getPropertiesForTypeDef(String userId, TypeDef typeDef) throws Exception
    {

        OMRSMetadataCollection metadataCollection = this.getMetadataCollection();

        List<TypeDefAttribute> propDefs = new ArrayList<>();

        // Look at the supertype (if any) first and then get any properties for the current type def

        // Move up the supertype hierarchy until you hit the top
        if (typeDef.getSuperType() != null)
        {
            // Get the supertype's type def
            TypeDefLink superTypeDefLink = typeDef.getSuperType();
            String superTypeName = superTypeDefLink.getName();
            TypeDef superTypeDef = metadataCollection.getTypeDefByName(userId, superTypeName);
            List<TypeDefAttribute> inheritedProps = getPropertiesForTypeDef(userId, superTypeDef);

            if (inheritedProps != null && !inheritedProps.isEmpty())
            {
                propDefs.addAll(inheritedProps);
            }

        }

        // Add any properties defined for the current type
        List<TypeDefAttribute> currentTypePropDefs = typeDef.getPropertiesDefinition();

        if (currentTypePropDefs != null && !currentTypePropDefs.isEmpty())
        {
            propDefs.addAll(currentTypePropDefs);
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

        /*
         * Need to look up entity def (or a suitable subclass).
         */
        List<String>   possibleEntityNameMatch = new ArrayList<>();
        possibleEntityNameMatch.add(entityIdentifiers.getName());

        while ((entityDef == null) && (! possibleEntityNameMatch.isEmpty()))
        {
            List<String>   possibleEntitySubtypeNameMatch = new ArrayList<>();

            for (String   entityTypeName : possibleEntityNameMatch)
            {
                entityDef = supportedEntityDefs.get(entityTypeName);

                /*
                 * First check that we have a match
                 */
                if (entityDef == null)
                {
                    /*
                     * Now look for subclasses of this entity and set up list with subclasses for next iteration.
                     */
                    for (EntityDef supportedEntityDef : supportedEntityDefs.values())
                    {
                        TypeDefLink superType = supportedEntityDef.getSuperType();
                        if ((superType != null) && (entityTypeName.equals(superType.getName())))
                        {
                            possibleEntitySubtypeNameMatch.add(supportedEntityDef.getName());
                        }
                    }
                }
            }

            possibleEntityNameMatch = possibleEntitySubtypeNameMatch;
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



}
