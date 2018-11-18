/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.compliance.tests.repository;

import org.odpi.openmetadata.compliance.OpenMetadataTestCase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.*;

/**
 * OpenMetadataTestCase is the superclass for an open metadata compliance test.  It manages the
 * test environment and reporting.
 */
public abstract class OpenMetadataRepositoryTestCase extends OpenMetadataTestCase
{
    private static final  String   assertion1 =  "repository-test-case-base-01";
    private static final  String   assertionMsg1 = "Connector supplied to test case.";
    private static final  String   assertion2 = "repository-test-case-base-02";
    private static final  String   assertionMsg2 = "Metadata collection for connector supplied to test case.";

    protected OMRSRepositoryConnector connector = null;


    /**
     * Constructor passes the standard descriptive information to the superclass.
     *
     * @param workbenchId  identifier of the workbench used to build the documentation URL.
     * @param testCaseId  id of the test case
     * @param testCaseName - name of the test case
     */
    protected OpenMetadataRepositoryTestCase(String workbenchId,
                                             String testCaseId,
                                             String testCaseName)
    {
        super(workbenchId, testCaseId, testCaseName);
    }


    /**
     * Set up the connector to the repository that is being tested.
     *
     * @param connector initialized and started OMRSRepositoryConnector object
     */
    public void setConnector(OMRSRepositoryConnector connector)
    {
        this.connector = connector;
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

        if (connector != null)
        {
            metadataCollection = connector.getMetadataCollection();
        }

        /*
         * The assertions are issued only if there is a problem so that the test mechanism is not
         * cluttering up the results with successful assertions.
         * Errors at this point are likely to be errors in the test framework.
         */
        if (metadataCollection == null)
        {
            assertCondition((connector != null), assertion1, assertionMsg1);
            assertCondition((false), assertion2, assertionMsg2);
        }

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
    protected InstanceProperties  getAllPropertiesForInstance(String userId, TypeDef typeDef) throws Exception {

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

        // Recursively gather all the TypeDefAttributes for the supertype hierarchy...
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

        /* Get an InstanceProperties, even if there are no properties in the propertyMap - you cannot pass a null
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
    protected List<TypeDefAttribute> getPropertiesForTypeDef(String userId, TypeDef typeDef) throws Exception {

        OMRSMetadataCollection metadataCollection = this.getMetadataCollection();

        List<TypeDefAttribute> propDefs = new ArrayList<>();

        // Look at the supertype (if any) first and then get any properties for the current type def

        // Move up the supertype hierarchy until you hit the top
        if (typeDef.getSuperType() != null) {

            // Get the supertype's type def
            TypeDefLink superTypeDefLink = typeDef.getSuperType();
            String superTypeName = superTypeDefLink.getName();
            TypeDef superTypeDef = metadataCollection.getTypeDefByName(userId, superTypeName);
            List<TypeDefAttribute> inheritedProps = getPropertiesForTypeDef(userId, superTypeDef);
            if (inheritedProps != null && !inheritedProps.isEmpty()) {
                propDefs.addAll(inheritedProps);
            }

        }
        // Add any properties defined for the current type
        List<TypeDefAttribute> currentTypePropDefs = typeDef.getPropertiesDefinition();
        if (currentTypePropDefs != null && !currentTypePropDefs.isEmpty()) {
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
    EntityDef  getEntityDef(Map<String, EntityDef>  supportedEntityDefs,
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
    EntityDetail addEntityToRepository(String                   userId,
                                       OMRSMetadataCollection   metadataCollection,
                                       EntityDef                entityDef) throws Exception
    {
        /* Supply all properties for the instance, including those inherited from supertypes, since they may be mandatory.
         * An alternative here would be to use getMinPropertiesForInstance, but providing all properties creates a logically
         * complete entity
         */
        InstanceProperties properties = this.getAllPropertiesForInstance(userId, entityDef);

        return metadataCollection.addEntity(userId, entityDef.getGUID(), properties, null, null );
    }
}
