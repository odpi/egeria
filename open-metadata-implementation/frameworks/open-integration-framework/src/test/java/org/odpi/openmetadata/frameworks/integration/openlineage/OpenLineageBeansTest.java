/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * Validate that every OpenLineage bean supports the standard bean contract: a public default constructor,
 * matching getters and setters, equals/hashCode that reflect the property values, a toString that names the
 * class, and a lossless round trip through Jackson JSON.  The beans are populated through their setters using
 * reflection so that every property is exercised without needing a hand-written test per bean.
 */
public class OpenLineageBeansTest
{
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * All of the beans in the OpenLineage package.
     *
     * @return classes
     */
    @DataProvider(name = "beans")
    public Object[][] beans()
    {
        return new Object[][]
                {
                        /* core event structures */
                        {OpenLineageRunEvent.class},
                        {OpenLineageJobEvent.class},
                        {OpenLineageDataSetEvent.class},
                        {OpenLineageRun.class},
                        {OpenLineageJob.class},
                        {OpenLineageInputDataSet.class},
                        {OpenLineageOutputDataSet.class},
                        {OpenLineageStaticDataSet.class},
                        {OpenLineageRunFacets.class},
                        {OpenLineageJobFacets.class},
                        {OpenLineageDataSetFacets.class},
                        {OpenLineageInputDataSetInputFacets.class},
                        {OpenLineageOutputDataSetOutputFacets.class},
                        /* generic facets */
                        {OpenLineageRunFacet.class},
                        {OpenLineageJobFacet.class},
                        {OpenLineageDataSetFacet.class},
                        {OpenLineageInputDataSetInputFacet.class},
                        {OpenLineageOutputDataSetOutputFacet.class},
                        /* run facets */
                        {OpenLineageParentRunFacet.class},
                        {OpenLineageParentRunFacetRun.class},
                        {OpenLineageParentRunFacetJob.class},
                        {OpenLineageParentRunFacetRoot.class},
                        {OpenLineageNominalTimeRunFacet.class},
                        {OpenLineageEnvironmentVariablesRunFacet.class},
                        {OpenLineageEnvironmentVariablesRunFacetVariable.class},
                        {OpenLineageErrorMessageRunFacet.class},
                        {OpenLineageExecutionParametersRunFacet.class},
                        {OpenLineageExecutionParametersRunFacetParameter.class},
                        {OpenLineageExternalQueryRunFacet.class},
                        {OpenLineageExtractionErrorRunFacet.class},
                        {OpenLineageExtractionErrorRunFacetError.class},
                        {OpenLineageJobDependenciesRunFacet.class},
                        {OpenLineageJobDependenciesRunFacetDependency.class},
                        {OpenLineageJobDependenciesRunFacetJobIdentifier.class},
                        {OpenLineageJobDependenciesRunFacetRunIdentifier.class},
                        {OpenLineageProcessingEngineRunFacet.class},
                        {OpenLineageTagsRunFacet.class},
                        {OpenLineageTagsRunFacetTag.class},
                        {OpenLineageTestRunFacet.class},
                        {OpenLineageTestRunFacetTestExecution.class},
                        /* job facets */
                        {OpenLineageDocumentationJobFacet.class},
                        {OpenLineageSQLJobFacet.class},
                        {OpenLineageSourceCodeLocationJobFacet.class},
                        {OpenLineageSourceCodeJobFacet.class},
                        {OpenLineageJobTypeJobFacet.class},
                        {OpenLineageJobTypeJobFacetEmissionPattern.class},
                        {OpenLineageOwnershipJobFacet.class},
                        {OpenLineageOwnershipJobFacetOwner.class},
                        {OpenLineageTagsJobFacet.class},
                        {OpenLineageTagsJobFacetTag.class},
                        {OpenLineageLineageJobFacet.class},
                        {OpenLineageLineageEntry.class},
                        {OpenLineageLineageInput.class},
                        {OpenLineageLineageFieldEntry.class},
                        {OpenLineageLineageTransformation.class},
                        /* dataset facets */
                        {OpenLineageDocumentationDataSetFacet.class},
                        {OpenLineageDataSourceDataSetFacet.class},
                        {OpenLineageSchemaDataSetFacet.class},
                        {OpenLineageSchemaDataSetFacetField.class},
                        {OpenLineageCatalogDataSetFacet.class},
                        {OpenLineageColumnLineageDataSetFacet.class},
                        {OpenLineageColumnLineageDataSetFacetField.class},
                        {OpenLineageColumnLineageDataSetFacetInputField.class},
                        {OpenLineageColumnLineageDataSetFacetTransformation.class},
                        {OpenLineageDataSetTypeDataSetFacet.class},
                        {OpenLineageDataSetVersionDataSetFacet.class},
                        {OpenLineageLifecycleStateChangeDataSetFacet.class},
                        {OpenLineageLifecycleStateChangeDataSetFacetPreviousIdentifier.class},
                        {OpenLineageOwnershipDataSetFacet.class},
                        {OpenLineageOwnershipDataSetFacetOwner.class},
                        {OpenLineageStorageDataSetFacet.class},
                        {OpenLineageSymlinksDataSetFacet.class},
                        {OpenLineageSymlinksDataSetFacetIdentifier.class},
                        {OpenLineageTagsDataSetFacet.class},
                        {OpenLineageTagsDataSetFacetTag.class},
                        {OpenLineageHierarchyDataSetFacet.class},
                        {OpenLineageHierarchyDataSetFacetLevel.class},
                        {OpenLineageLineageDataSetFacet.class},
                        {OpenLineageDataQualityMetricsDataSetFacet.class},
                        {OpenLineageDataQualityMetricsColumnMetrics.class},
                        /* input facets */
                        {OpenLineageDataQualityAssertionsInputDataSetFacet.class},
                        {OpenLineageDataQualityAssertionsInputDataSetFacetAssertions.class},
                        {OpenLineageDataQualityMetricsInputDataSetFacet.class},
                        {OpenLineageInputStatisticsInputDataSetFacet.class},
                        {OpenLineageInputSubsetInputDataSetFacet.class},
                        /* output facets */
                        {OpenLineageOutputStatisticsOutputDataSetFacet.class},
                        {OpenLineageOutputSubsetOutputDataSetFacet.class},
                        /* registered custom facets */
                        {OpenLineageGcpComposerJobFacet.class},
                        {OpenLineageGcpComposerRunFacet.class},
                        {OpenLineageGcpDataprocRunFacet.class},
                        {OpenLineageGcpLineageJobFacet.class},
                        {OpenLineageGcpLineageJobFacetOrigin.class},
                        {OpenLineageIcebergCommitReportOutputDataSetFacet.class},
                        {OpenLineageIcebergCommitReportOutputDataSetFacetMetrics.class},
                        {OpenLineageIcebergScanReportInputDataSetFacet.class},
                        {OpenLineageIcebergScanReportInputDataSetFacetMetrics.class},
                };
    }


    /**
     * A default constructed bean should equal another default constructed bean, and have a matching hash code.
     *
     * @param beanClass class under test
     * @throws Exception reflection problem
     */
    @Test(dataProvider = "beans")
    public void testDefaultConstructor(Class<?> beanClass) throws Exception
    {
        Object bean1 = beanClass.getDeclaredConstructor().newInstance();
        Object bean2 = beanClass.getDeclaredConstructor().newInstance();

        assertNotNull(bean1);
        assertEquals(bean1, bean2, beanClass.getSimpleName());
        assertEquals(bean1.hashCode(), bean2.hashCode(), beanClass.getSimpleName());
        assertNotEquals(bean1, null, beanClass.getSimpleName());
        assertNotEquals(bean1, "DummyString", beanClass.getSimpleName());
        assertTrue(bean1.toString().contains(beanClass.getSimpleName()), beanClass.getSimpleName());
    }


    /**
     * Facets must carry their schema URL from the OpenLineage spec by default.
     *
     * @param beanClass class under test
     * @throws Exception reflection problem
     */
    @Test(dataProvider = "beans")
    public void testFacetSchemaURL(Class<?> beanClass) throws Exception
    {
        Object bean = beanClass.getDeclaredConstructor().newInstance();

        if (bean instanceof OpenLineageFacet facet)
        {
            assertNotNull(facet.get_schemaURL(), beanClass.getSimpleName());
            assertTrue(facet.get_schemaURL().toString().startsWith("https://openlineage.io/spec/"), facet.get_schemaURL().toString());
            assertTrue(facet.get_schemaURL().toString().contains("#/$defs/"), facet.get_schemaURL().toString());
            assertNull(facet.get_producer());
        }
    }


    /**
     * Every setter should have a matching getter, and populating the bean should change equals/hashCode
     * and be reflected in toString.
     *
     * @param beanClass class under test
     * @throws Exception reflection problem
     */
    @Test(dataProvider = "beans")
    public void testAccessors(Class<?> beanClass) throws Exception
    {
        Object populated = populate(beanClass, 1);
        Object empty     = beanClass.getDeclaredConstructor().newInstance();

        assertNotEquals(populated, empty, beanClass.getSimpleName());
        assertNotEquals(populated.hashCode(), empty.hashCode(), beanClass.getSimpleName());

        for (Method setter : getSetters(beanClass))
        {
            Method getter = getGetter(beanClass, setter);

            assertNotNull(getter, beanClass.getSimpleName() + " has no getter for " + setter.getName());
            assertEquals(getter.invoke(populated), makeValue(setter.getGenericParameterTypes()[0], 1), beanClass.getSimpleName() + "." + getter.getName());
        }

        Object populatedAgain = populate(beanClass, 1);

        assertEquals(populated, populatedAgain, beanClass.getSimpleName());
        assertEquals(populated.hashCode(), populatedAgain.hashCode(), beanClass.getSimpleName());

        Object differentlyPopulated = populate(beanClass, 2);

        assertNotEquals(populated, differentlyPopulated, beanClass.getSimpleName());
    }


    /**
     * A populated bean should survive a round trip through JSON, and the JSON produced must not contain
     * the internal "additionalProperties" wrapper - unknown properties must be flattened into the object.
     *
     * @param beanClass class under test
     * @throws Exception reflection problem
     */
    @Test(dataProvider = "beans")
    public void testJSON(Class<?> beanClass) throws Exception
    {
        Object populated = populate(beanClass, 1);

        String json = OBJECT_MAPPER.writeValueAsString(populated);

        assertFalse(json.contains("\"additionalProperties\""), beanClass.getSimpleName() + ": " + json);

        Object roundTrip = OBJECT_MAPPER.readValue(json, beanClass);

        assertEquals(roundTrip, populated, beanClass.getSimpleName() + ": " + json);
        assertEquals(OBJECT_MAPPER.writeValueAsString(roundTrip), json, beanClass.getSimpleName());
    }


    /**
     * Unknown properties are captured in additionalProperties rather than dropped, and written back out.
     *
     * @param beanClass class under test
     * @throws Exception reflection problem
     */
    @Test(dataProvider = "beans")
    public void testUnknownPropertiesRetained(Class<?> beanClass) throws Exception
    {
        Method anyGetter = null;

        try
        {
            anyGetter = beanClass.getMethod("getAdditionalProperties");
        }
        catch (NoSuchMethodException noSuchMethod)
        {
            /* bean does not support additional properties - nothing to test */
            return;
        }

        Type mapType = anyGetter.getGenericReturnType();
        Type valueType = ((ParameterizedType) mapType).getActualTypeArguments()[1];

        /*
         * Build an unknown property whose value matches the declared type of the map (either Object or a facet class).
         */
        Object unknownValue = makeValue(valueType, 1);
        Map<String, Object> jsonMap = new LinkedHashMap<>();
        jsonMap.put("egeriaUnknownProperty", unknownValue);

        String json = OBJECT_MAPPER.writeValueAsString(jsonMap);
        Object bean = OBJECT_MAPPER.readValue(json, beanClass);

        Map<?, ?> additionalProperties = (Map<?, ?>) anyGetter.invoke(bean);

        assertNotNull(additionalProperties, beanClass.getSimpleName() + " dropped unknown property: " + json);
        assertEquals(additionalProperties.get("egeriaUnknownProperty"), unknownValue, beanClass.getSimpleName() + ": " + json);
        assertTrue(OBJECT_MAPPER.writeValueAsString(bean).contains("egeriaUnknownProperty"), beanClass.getSimpleName());
    }


    /*
     * ============================================================================
     * Reflection helpers
     */

    private List<Method> getSetters(Class<?> beanClass)
    {
        List<Method> setters = new ArrayList<>();

        for (Method method : beanClass.getMethods())
        {
            if ((method.getName().startsWith("set")) &&
                (method.getParameterCount() == 1) &&
                (Modifier.isPublic(method.getModifiers())) &&
                (! "setAdditionalProperty".equals(method.getName())) &&
                (! "setAdditionalProperties".equals(method.getName())))
            {
                setters.add(method);
            }
        }

        return setters;
    }


    private Method getGetter(Class<?> beanClass, Method setter)
    {
        String propertyName = setter.getName().substring(3);

        for (Method method : beanClass.getMethods())
        {
            if ((method.getParameterCount() == 0) &&
                ((method.getName().equals("get" + propertyName)) || (method.getName().equals("is" + propertyName))))
            {
                return method;
            }
        }

        return null;
    }


    private Object populate(Class<?> beanClass, int seed) throws Exception
    {
        Object bean = beanClass.getDeclaredConstructor().newInstance();

        for (Method setter : getSetters(beanClass))
        {
            setter.invoke(bean, makeValue(setter.getGenericParameterTypes()[0], seed));
        }

        return bean;
    }


    /**
     * Create a deterministic value for a property type.  Nested beans are populated one level deep only,
     * with lists and maps holding a single entry, so recursive structures (such as nested schema fields) terminate.
     *
     * @param type generic type of the property
     * @param seed value to vary the content
     * @return value
     * @throws Exception reflection problem
     */
    private Object makeValue(Type type, int seed) throws Exception
    {
        if (type instanceof ParameterizedType parameterizedType)
        {
            Class<?> rawType = (Class<?>) parameterizedType.getRawType();

            if (List.class.isAssignableFrom(rawType))
            {
                List<Object> list = new ArrayList<>();
                list.add(makeLeafValue(parameterizedType.getActualTypeArguments()[0], seed));
                return list;
            }
            else if (Map.class.isAssignableFrom(rawType))
            {
                Map<Object, Object> map = new HashMap<>();
                map.put("key" + seed, makeLeafValue(parameterizedType.getActualTypeArguments()[1], seed));
                return map;
            }
        }

        return makeLeafValue(type, seed);
    }


    private Object makeLeafValue(Type type, int seed) throws Exception
    {
        if (type == String.class)  return "value" + seed;
        if (type == Long.class)    return (long) seed * 1000;
        if (type == Double.class)  return seed * 1.5;
        if (type == Boolean.class) return (seed % 2 == 1);
        if (type == boolean.class) return (seed % 2 == 1);
        if (type == URI.class)     return URI.create("https://example.com/" + seed);
        if (type == UUID.class)    return UUID.nameUUIDFromBytes(("seed" + seed).getBytes());
        if (type == Object.class)  return "object" + seed;

        if (type instanceof Class<?> nestedClass)
        {
            /*
             * Populate nested beans shallowly: scalar properties only, so that self-referential
             * structures terminate.
             */
            Object nested = nestedClass.getDeclaredConstructor().newInstance();

            for (Method setter : getSetters(nestedClass))
            {
                Type parameterType = setter.getGenericParameterTypes()[0];

                if ((parameterType == String.class) || (parameterType == Long.class) || (parameterType == Double.class) ||
                    (parameterType == Boolean.class) || (parameterType == boolean.class) || (parameterType == URI.class) || (parameterType == UUID.class))
                {
                    setter.invoke(nested, makeLeafValue(parameterType, seed));
                }
            }

            return nested;
        }

        throw new IllegalArgumentException("Unsupported property type in test: " + type);
    }
}
