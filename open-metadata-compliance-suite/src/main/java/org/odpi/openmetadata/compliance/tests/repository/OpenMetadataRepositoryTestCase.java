/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.compliance.tests.repository;

import org.odpi.openmetadata.compliance.OpenMetadataTestCase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefAttribute;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @param workbenchId - identifier of the workbench used to build the documentation URL.
     * @param testCaseId - id of the test case
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
     * Return the instance properties defined for the TypeDef.
     *
     * @param typeDefAttributes - attributes defined for a specific type
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
}
