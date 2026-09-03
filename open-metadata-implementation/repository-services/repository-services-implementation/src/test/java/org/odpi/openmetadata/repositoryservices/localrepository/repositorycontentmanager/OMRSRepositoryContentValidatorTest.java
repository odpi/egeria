/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyComparisonOperator;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.PropertyCondition;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ArrayPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.expectThrows;


/**
 * Validate the property matching used to push search criteria down into a repository.
 */
public class OMRSRepositoryContentValidatorTest
{
    private static final String testGUID = "0e2f1e0b-6b1a-4b0e-9a4d-9f2a5e0f1c33";


    /**
     * The validator requires a content manager, but property matching does not consult it.
     *
     * @return validator to test
     */
    private OMRSRepositoryContentValidator createValidator()
    {
        return new OMRSRepositoryContentValidator(new OMRSRepositoryContentManager("testUserId", null));
    }


    /**
     * Build the effectivity window that RepositoryHandler pushes into findRelationships():
     * each bound is a condition that only groups a nested "unset OR compares" pair.
     *
     * @param effectiveTime moment of interest
     * @return search properties
     */
    private SearchProperties getEffectivitySearchProperties(Date effectiveTime)
    {
        List<PropertyCondition> conditions = new ArrayList<>();

        conditions.add(this.getEffectivityBound("effectiveFromTime", PropertyComparisonOperator.LTE, effectiveTime));
        conditions.add(this.getEffectivityBound("effectiveToTime", PropertyComparisonOperator.GT, effectiveTime));

        SearchProperties searchProperties = new SearchProperties();

        searchProperties.setConditions(conditions);
        searchProperties.setMatchCriteria(MatchCriteria.ALL);

        return searchProperties;
    }


    private PropertyCondition getEffectivityBound(String                     propertyName,
                                                  PropertyComparisonOperator operator,
                                                  Date                       effectiveTime)
    {
        PropertyCondition unset = new PropertyCondition();

        unset.setProperty(propertyName);
        unset.setOperator(PropertyComparisonOperator.IS_NULL);

        PrimitivePropertyValue dateValue = new PrimitivePropertyValue();

        dateValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE);
        dateValue.setPrimitiveValue(effectiveTime.getTime());

        PropertyCondition compared = new PropertyCondition();

        compared.setProperty(propertyName);
        compared.setOperator(operator);
        compared.setValue(dateValue);

        List<PropertyCondition> eitherWay = new ArrayList<>();

        eitherWay.add(unset);
        eitherWay.add(compared);

        SearchProperties nested = new SearchProperties();

        nested.setConditions(eitherWay);
        nested.setMatchCriteria(MatchCriteria.ANY);

        PropertyCondition bound = new PropertyCondition();

        bound.setNestedConditions(nested);

        return bound;
    }


    /**
     * A relationship with no properties of its own - such as ResourceConnection - must survive the
     * effectivity window when its effectivity dates are unset.  The nesting-only conditions carry no
     * property, operator or value at their own level, so they must contribute only their nested result.
     *
     * @throws InvalidParameterException unexpected
     */
    @Test
    public void testEffectivityMatchesInstanceWithNoProperties() throws InvalidParameterException
    {
        OMRSRepositoryContentValidator validator = createValidator();

        Relationship relationship = new Relationship();

        assertTrue(validator.verifyMatchingInstancePropertyValues(this.getEffectivitySearchProperties(new Date()),
                                                                  testGUID,
                                                                  relationship,
                                                                  null),
                   "an instance with no properties was excluded by the effectivity window");
    }


    /**
     * The same window must continue to match an instance that does have properties.
     *
     * @throws InvalidParameterException unexpected
     */
    @Test
    public void testEffectivityMatchesInstanceWithProperties() throws InvalidParameterException
    {
        OMRSRepositoryContentValidator validator = createValidator();

        Relationship relationship = new Relationship();

        PrimitivePropertyValue propertyValue = new PrimitivePropertyValue();

        propertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
        propertyValue.setPrimitiveValue("move-file");

        InstanceProperties instanceProperties = new InstanceProperties();

        instanceProperties.setProperty("requestType", propertyValue);

        assertTrue(validator.verifyMatchingInstancePropertyValues(this.getEffectivitySearchProperties(new Date()),
                                                                  testGUID,
                                                                  relationship,
                                                                  instanceProperties),
                   "an instance with properties was excluded by the effectivity window");
    }


    /**
     * An instance whose effectivity has expired must still be excluded.
     *
     * @throws InvalidParameterException unexpected
     */
    @Test
    public void testEffectivityExcludesExpiredInstance() throws InvalidParameterException
    {
        OMRSRepositoryContentValidator validator = createValidator();

        Relationship relationship = new Relationship();

        InstanceProperties instanceProperties = new InstanceProperties();

        instanceProperties.setEffectiveToTime(new Date(System.currentTimeMillis() - 100000L));

        assertTrue(! validator.verifyMatchingInstancePropertyValues(this.getEffectivitySearchProperties(new Date()),
                                                                    testGUID,
                                                                    relationship,
                                                                    instanceProperties),
                   "an expired instance was included by the effectivity window");
    }


    /**
     * Return a minimal entity type to validate against.
     *
     * @return type definition
     */
    private EntityDef getTestTypeDef()
    {
        EntityDef entityDef = new EntityDef();

        entityDef.setGUID("6f0a4a4c-6a20-4a9f-8c2f-1a3fbb3b0d21");
        entityDef.setName("TestType");
        entityDef.setCategory(TypeDefCategory.ENTITY_DEF);

        return entityDef;
    }


    /**
     * Return a string property value.
     *
     * @param value the string
     * @return property value
     */
    private PrimitivePropertyValue getStringValue(String value)
    {
        PrimitivePropertyValue propertyValue = new PrimitivePropertyValue();

        propertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
        propertyValue.setPrimitiveValue(value);

        return propertyValue;
    }


    /**
     * A null character cannot be stored by any repository, so it has to be refused before the store is
     * asked - otherwise the same request succeeds or fails depending on which repository is deployed.
     */
    @Test
    public void testNullCharacterInPropertyIsRejected()
    {
        InstanceProperties instanceProperties = new InstanceProperties();

        instanceProperties.setProperty("displayName", this.getStringValue("Coco Pharmaceuticals " + (char) 0 + " Database"));

        PropertyErrorException error = expectThrows(PropertyErrorException.class,
                                                    () -> createValidator().validatePropertiesForType("testSource",
                                                                                                      "properties",
                                                                                                      this.getTestTypeDef(),
                                                                                                      instanceProperties,
                                                                                                      "testMethod"));

        assertTrue(error.getReportedErrorMessage().contains("OMRS-REPOSITORY-400-084"),
                   "the rejection should name the null character rule.  Actual: " + error.getReportedErrorMessage());
        assertTrue(error.getReportedErrorMessage().contains("displayName"),
                   "the rejection should name the offending property.  Actual: " + error.getReportedErrorMessage());
    }


    /**
     * A null character nested inside an array is no more storable than one at the top level.
     */
    @Test
    public void testNullCharacterInsideAnArrayIsRejected()
    {
        ArrayPropertyValue arrayPropertyValue = new ArrayPropertyValue();

        arrayPropertyValue.setArrayCount(1);
        arrayPropertyValue.setArrayValue(0, this.getStringValue("zero " + (char) 0 + " byte"));

        InstanceProperties instanceProperties = new InstanceProperties();

        instanceProperties.setProperty("aliases", arrayPropertyValue);

        PropertyErrorException error = expectThrows(PropertyErrorException.class,
                                                    () -> createValidator().validatePropertiesForType("testSource",
                                                                                                      "properties",
                                                                                                      this.getTestTypeDef(),
                                                                                                      instanceProperties,
                                                                                                      "testMethod"));

        assertTrue(error.getReportedErrorMessage().contains("OMRS-REPOSITORY-400-084"),
                   "a nested null character should be refused too.  Actual: " + error.getReportedErrorMessage());
    }


    /**
     * Ordinary text - including the awkward but perfectly storable characters that the rest of the special
     * character testing is about - must not be caught by the null character rule.
     * <br>
     * The stub type here declares no attributes, so this value is refused for not belonging to the type.
     * That is a different rule and the point of the assertion: whatever this validator objects to, it is
     * not the content of the string.
     */
    @Test
    public void testOrdinaryTextIsNotCaughtByTheNullCharacterRule()
    {
        InstanceProperties instanceProperties = new InstanceProperties();

        instanceProperties.setProperty("displayName", this.getStringValue("Coco Pharmaceutical's \"data_lake\" 100% \\ done"));

        PropertyErrorException error = expectThrows(PropertyErrorException.class,
                                                    () -> createValidator().validatePropertiesForType("testSource",
                                                                                                      "properties",
                                                                                                      this.getTestTypeDef(),
                                                                                                      instanceProperties,
                                                                                                      "testMethod"));

        assertTrue((! error.getReportedErrorMessage().contains("OMRS-REPOSITORY-400-084")),
                   "ordinary text was rejected as containing a null character.  Actual: " + error.getReportedErrorMessage());
    }
}
