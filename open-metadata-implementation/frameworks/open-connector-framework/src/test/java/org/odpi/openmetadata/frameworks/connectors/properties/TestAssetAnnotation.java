/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Annotation;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.AnnotationStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Classification;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementType;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the AssetAnnotation can function as a facade for its bean.
 */
public class TestAssetAnnotation
{
    private ElementType          type                 = new ElementType();
    private Date                 creationDate         = new Date(27);
    private Date                 reviewDate           = new Date(1234);
    private List<Classification> classifications      = new ArrayList<>();
    private Map<String, Object>  analysisParameters   = new HashMap<>();
    private Map<String, Object>  additionalProperties = new HashMap<>();


    /**
     * Default constructor
     */
    public TestAssetAnnotation()
    {
        type.setElementTypeName("TestType");
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private AssetAnnotation getTestObject()
    {
        Annotation testObject = new Annotation();

        testObject.setType(type);
        testObject.setGUID("TestGUID");
        testObject.setURL("TestURL");
        testObject.setClassifications(classifications);
        testObject.setReportName("TestReportName");
        testObject.setReportDescription("TestReportDescription");
        testObject.setCreationDate(creationDate);
        testObject.setAnalysisParameters(analysisParameters);
        testObject.setAnnotationType("TestAnnotationType");
        testObject.setSummary("TestSummary");
        testObject.setConfidenceLevel(5);
        testObject.setExpression("TestExpression");
        testObject.setExplanation("TestExplanation");
        testObject.setAnalysisStep("TestAnalysisStep");
        testObject.setJsonProperties("TestJsonProperties");
        testObject.setAnnotationStatus(AnnotationStatus.UNKNOWN_STATUS);
        testObject.setReviewDate(reviewDate);
        testObject.setSteward("TestSteward");
        testObject.setReviewComment("TestReviewComment");
        testObject.setAdditionalProperties(additionalProperties);

        return new AssetAnnotation(testObject);
    }


    /**
     * Set up an example object to test.  This has a property from the superclass set differently.
     *
     * @return filled in object
     */
    private AssetAnnotation getDifferentObject()
    {
        Annotation testObject = new Annotation();

        testObject.setType(type);
        testObject.setGUID("TestDifferentGUID");
        testObject.setURL("TestURL");
        testObject.setClassifications(classifications);

        testObject.setReportName("TestReportName");
        testObject.setReportDescription("TestReportDescription");
        testObject.setCreationDate(creationDate);
        testObject.setAnalysisParameters(analysisParameters);
        testObject.setAnnotationType("TestAnnotationType");
        testObject.setSummary("TestSummary");
        testObject.setConfidenceLevel(5);
        testObject.setExpression("TestExpression");
        testObject.setExplanation("TestExplanation");
        testObject.setAnalysisStep("TestAnalysisStep");
        testObject.setJsonProperties("TestJsonProperties");
        testObject.setAnnotationStatus(AnnotationStatus.UNKNOWN_STATUS);
        testObject.setReviewDate(reviewDate);
        testObject.setSteward("TestSteward");
        testObject.setReviewComment("TestReviewComment");
        testObject.setAdditionalProperties(additionalProperties);

        return new AssetAnnotation(testObject);
    }


    /**
     * Set up an example object to test.  This has a property from the subclass set differently.
     *
     * @return filled in object
     */
    private AssetAnnotation getAnotherDifferentObject()
    {
        Annotation testObject = new Annotation();

        testObject.setType(type);
        testObject.setGUID("TestGUID");
        testObject.setURL("TestURL");
        testObject.setClassifications(classifications);
        testObject.setReportName("TestReportName");
        testObject.setReportDescription("TestReportDescription");
        testObject.setCreationDate(creationDate);
        testObject.setAnalysisParameters(analysisParameters);
        testObject.setAnnotationType("TestAnnotationType");
        testObject.setSummary("TestSummary");
        testObject.setConfidenceLevel(5);
        testObject.setExpression("TestExpression");
        testObject.setExplanation("TestExplanation");
        testObject.setAnalysisStep("TestAnalysisStep");
        testObject.setJsonProperties("TestJsonProperties");
        testObject.setAnnotationStatus(AnnotationStatus.OTHER_STATUS);
        testObject.setReviewDate(reviewDate);
        testObject.setSteward("TestSteward");
        testObject.setReviewComment("TestReviewComment");
        testObject.setAdditionalProperties(additionalProperties);

        return new AssetAnnotation(testObject);
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(AssetAnnotation  resultObject)
    {
        assertTrue(resultObject.getType().getElementTypeBean().equals(type));
        assertTrue(resultObject.getGUID().equals("TestGUID"));
        assertTrue(resultObject.getURL().equals("TestURL"));
        assertTrue(resultObject.getAssetClassifications() == null);

        assertTrue(resultObject.getReportName().equals("TestReportName"));
        assertTrue(resultObject.getReportDescription().equals("TestReportDescription"));
        assertTrue(resultObject.getCreationDate().equals(creationDate));
        assertTrue(resultObject.getAnalysisParameters() == null);
        assertTrue(resultObject.getAnnotationType().equals("TestAnnotationType"));
        assertTrue(resultObject.getSummary().equals("TestSummary"));
        assertTrue(resultObject.getConfidenceLevel() == 5);
        assertTrue(resultObject.getExpression().equals("TestExpression"));
        assertTrue(resultObject.getExplanation().equals("TestExplanation"));
        assertTrue(resultObject.getAnalysisStep().equals("TestAnalysisStep"));
        assertTrue(resultObject.getJsonProperties().equals("TestJsonProperties"));
        assertTrue(resultObject.getAnnotationStatus().equals(AnnotationStatus.UNKNOWN_STATUS));
        assertTrue(resultObject.getReviewDate().equals(reviewDate));
        assertTrue(resultObject.getSteward().equals("TestSteward"));
        assertTrue(resultObject.getReviewComment().equals("TestReviewComment"));
        assertTrue(resultObject.getAdditionalProperties() == null);
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param nullObject object to test
     */
    private void validateNullObject(AssetAnnotation  nullObject)
    {
        assertTrue(nullObject.getType() == null);
        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getURL() == null);
        assertTrue(nullObject.getAssetClassifications() == null);

        assertTrue(nullObject.getReportName() == null);
        assertTrue(nullObject.getReportDescription() == null);
        assertTrue(nullObject.getCreationDate() == null);
        assertTrue(nullObject.getAnalysisParameters() == null);
        assertTrue(nullObject.getAnnotationType() == null);
        assertTrue(nullObject.getSummary() == null);
        assertTrue(nullObject.getConfidenceLevel() == 0);
        assertTrue(nullObject.getExpression() == null);
        assertTrue(nullObject.getExplanation() == null);
        assertTrue(nullObject.getAnalysisStep() == null);
        assertTrue(nullObject.getJsonProperties() == null);
        assertTrue(nullObject.getAnnotationStatus() == null);
        assertTrue(nullObject.getReviewDate() == null);
        assertTrue(nullObject.getSteward() == null);
        assertTrue(nullObject.getReviewComment() == null);
        assertTrue(nullObject.getAdditionalProperties() == null);
    }


    /**
     * Validate that additional properties are handled properly.
     */
    @Test public void testAdditionalProperties()
    {
        Map<String, Object>  propertyMap = new HashMap<>();

        propertyMap.put("property1", "TestString");
        propertyMap.put("property2", new Integer(2));

        Annotation annotationBean = new Annotation();
        annotationBean.setAdditionalProperties(propertyMap);

        AssetAnnotation testObject = new AssetAnnotation(annotationBean);

        AdditionalProperties additionalProperties = testObject.getAdditionalProperties();

        assertTrue(additionalProperties.getPropertyNames() != null);

        Iterator<String> iterator = additionalProperties.getPropertyNames();

        String propertyName;

        propertyName = iterator.next();
        assertTrue(propertyName.equals("property2"));
        assertTrue(additionalProperties.getProperty(propertyName).equals(new Integer(2)));

        propertyName = iterator.next();
        assertTrue(propertyName.equals("property1"));
        assertTrue(additionalProperties.getProperty(propertyName).equals("TestString"));

        try
        {
            iterator.next();
            assertTrue(false);
        }
        catch (Throwable   exc)
        {
            assertTrue(true);
        }

        annotationBean = new Annotation();
        testObject = new AssetAnnotation(annotationBean);

        additionalProperties = testObject.getAdditionalProperties();

        assertTrue(additionalProperties == null);

        propertyMap = new HashMap<>();
        annotationBean = new Annotation();
        annotationBean.setAdditionalProperties(propertyMap);
        testObject = new AssetAnnotation(annotationBean);

        additionalProperties = testObject.getAdditionalProperties();

        assertTrue(additionalProperties == null);
    }


    /**
     * Validate that additional properties are handled properly.
     */
    @Test public void testAnalysisParameters()
    {
        Map<String, Object>  propertyMap = new HashMap<>();

        propertyMap.put("property1", "TestString");
        propertyMap.put("property2", new Integer(2));

        Annotation annotationBean = new Annotation();
        annotationBean.setAnalysisParameters(propertyMap);

        AssetAnnotation testObject = new AssetAnnotation(annotationBean);

        AdditionalProperties analysisParameters = testObject.getAnalysisParameters();

        assertTrue(analysisParameters.getPropertyNames() != null);

        Iterator<String> iterator = analysisParameters.getPropertyNames();

        String propertyName;

        propertyName = iterator.next();
        assertTrue(propertyName.equals("property2"));
        assertTrue(analysisParameters.getProperty(propertyName).equals(new Integer(2)));

        propertyName = iterator.next();
        assertTrue(propertyName.equals("property1"));
        assertTrue(analysisParameters.getProperty(propertyName).equals("TestString"));

        try
        {
            iterator.next();
            assertTrue(false);
        }
        catch (Throwable   exc)
        {
            assertTrue(true);
        }

        annotationBean = new Annotation();
        testObject = new AssetAnnotation(annotationBean);

        analysisParameters = testObject.getAnalysisParameters();

        assertTrue(analysisParameters == null);

        propertyMap = new HashMap<>();
        annotationBean = new Annotation();
        annotationBean.setAnalysisParameters(propertyMap);
        testObject = new AssetAnnotation(annotationBean);

        analysisParameters = testObject.getAnalysisParameters();

        assertTrue(analysisParameters == null);
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        Annotation         nullBean;
        AssetAnnotation    nullObject;
        AssetAnnotation    nullTemplate;
        AssetDescriptor    parentAsset;

        nullBean = null;
        nullObject = new AssetAnnotation(nullBean);
        validateNullObject(nullObject);

        nullBean = new Annotation();
        nullObject = new AssetAnnotation(nullBean);
        validateNullObject(nullObject);

        nullBean = new Annotation(null);
        nullObject = new AssetAnnotation(nullBean);
        validateNullObject(nullObject);

        parentAsset = null;
        nullBean = null;
        nullObject = new AssetAnnotation(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullBean = new Annotation();
        nullObject = new AssetAnnotation(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullBean = new Annotation(null);
        nullObject = new AssetAnnotation(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullTemplate = null;
        nullObject = new AssetAnnotation(parentAsset, nullTemplate);
        validateNullObject(nullObject);
    }


    /**
     * Validate that 2 different objects with the same content are evaluated as equal.
     * Also that different objects are considered not equal.
     */
    @Test public void testEquals()
    {
        assertFalse(getTestObject().equals(null));
        assertFalse(getTestObject().equals("DummyString"));
        assertTrue(getTestObject().equals(getTestObject()));

        AssetAnnotation  sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        assertFalse(getTestObject().equals(getDifferentObject()));
        assertFalse(getTestObject().equals(getAnotherDifferentObject()));
    }


    /**
     *  Validate that 2 different objects with the same content have the same hash code.
     */
    @Test public void testHashCode()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());
    }


    /**
     *  Validate that an object cloned from another object has the same content as the original
     */
    @Test public void testClone()
    {
        validateResultObject(new AssetAnnotation(null, getTestObject()));
    }

    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("Annotation"));
    }
}
