/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.virtualdataconnector.igc.connectors.eventmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopic;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.virtualdataconnector.igc.connectors.eventmapper.model.IGCKafkaEvent;
import org.odpi.openmetadata.virtualdataconnector.igc.connectors.repositoryconnector.model.IGCObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;
import org.testng.log4testng.Logger;

import java.io.*;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class IGCOMRSRepositoryEventMapperTest {

    // Dummy Files

    private static final String KAFKA_CATEGORY = "jsons/kafka_event_category_import.json";
    private static final String KAFKA_TERM = "jsons/kafka_event_term_import.json";
    private static final String IGC_OBJECT_CATEGORY = "jsons/igc_object_category.json";
    private static final String IGC_OBJECT_TERM = "jsons/igc_object_term.json";

    private IGCKafkaEvent dummyIGCKafkaEventCategory;
    private IGCKafkaEvent dummyIGCKafkaEventTerm;
    private IGCObject dummyIGCObjectCategory;
    private IGCObject dummyIGCObjectTerm;
    private ObjectMapper objectMapper;
    private Object classInstance;

    private final static Logger logger = Logger.getLogger(IGCOMRSRepositoryEventMapperTest.class);

    @BeforeTest
    public void loadDummyData() {

       objectMapper = new ObjectMapper();

        try {
            dummyIGCKafkaEventCategory = objectMapper.readValue(readFile(new File(this.getClass().getClassLoader().getResource(KAFKA_CATEGORY).getFile())), IGCKafkaEvent.class);
            dummyIGCKafkaEventTerm = objectMapper.readValue(readFile(new File(this.getClass().getClassLoader().getResource(KAFKA_TERM).getFile())), IGCKafkaEvent.class);
            dummyIGCObjectCategory = objectMapper.readValue(readFile(new File(this.getClass().getClassLoader().getResource(IGC_OBJECT_CATEGORY).getFile())), IGCObject.class);
            dummyIGCObjectTerm = objectMapper.readValue(readFile(new File(this.getClass().getClassLoader().getResource(IGC_OBJECT_TERM).getFile())), IGCObject.class);
        } catch (Exception e){
            logger.error("Something went wrong while loading the JSON objects: " + e.getMessage());
        }
    }

    @BeforeTest
    public void setClassInstance() {
        // Reflection on the private method.
        Class<IGCOMRSRepositoryEventMapper> igcomrsRepositoryEventMapperClass = IGCOMRSRepositoryEventMapper.class;

        try{
            classInstance = igcomrsRepositoryEventMapperClass.newInstance();
        } catch (Exception e){
            logger.error("Something is wrong while doing reflection: " + e.getMessage());
        }
    }


//    @Test
//    public void testStart() {
//    }
//
//    @Test
//    public void testRunConsumer() {
//    }
//
//    @Test
//    public void testProcessEvent() {
//    }
//
//    @Test
//    public void testProcessIGCEvent() {
//    }

    @Test
    public void testGetTypeQualifiedName() {
        try {
            Method method = classInstance.getClass().getDeclaredMethod("getTypeQualifiedName", String.class);
            method.setAccessible(true);
            Object result = method.invoke(classInstance, "test");
            Assert.assertEquals("test_type", result.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetGlossaryTermName() {
        try {
            Method method = classInstance.getClass().getDeclaredMethod("getGlossaryTermName", IGCObject.class);
            method.setAccessible(true);
            Object result = method.invoke(classInstance, dummyIGCObjectTerm);
            Assert.assertEquals("Coco Pharmaceuticals.Department Code", result.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // The following case should not be counted since the dummy date don't contain the connection information.
    @Test
    public void testGetConnectionQualifiedName() {
        try {
            Method method = classInstance.getClass().getDeclaredMethod("getConnectionQualifiedName", IGCObject.class);
            method.setAccessible(true);
            Object result = method.invoke(classInstance, dummyIGCObjectTerm);
//            Assert.assertEquals("null.Connection", result.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetGlossaryTermInstanceProperties() {
        try {
            Method method = classInstance.getClass().getDeclaredMethod("getGlossaryTermInstanceProperties", IGCObject.class);
            method.setAccessible(true);
            Object result = method.invoke(classInstance, dummyIGCObjectTerm);
            Map<String, InstancePropertyValue> propertyValueMap = (Map<String, InstancePropertyValue>) result;
            Assert.assertEquals("PrimitivePropertyValue{primitiveDefCategory=PrimitiveDefCategory{code=11, name='string', javaClassName='java.lang.String', guid='b34a64b9-554a-42b1-8f8a-7d5c2339f9c4'}, primitiveValue=null, instancePropertyCategory=InstancePropertyCategory{typeCode=1, typeName='Primitive', typeDescription='A primitive type.'}, typeGUID='null', typeName='null'}", propertyValueMap.get("examples").toString());
            Assert.assertEquals("PrimitivePropertyValue{primitiveDefCategory=PrimitiveDefCategory{code=11, name='string', javaClassName='java.lang.String', guid='b34a64b9-554a-42b1-8f8a-7d5c2339f9c4'}, primitiveValue=null, instancePropertyCategory=InstancePropertyCategory{typeCode=1, typeName='Primitive', typeDescription='A primitive type.'}, typeGUID='null', typeName='null'}", propertyValueMap.get("usage").toString());
            Assert.assertEquals("PrimitivePropertyValue{primitiveDefCategory=PrimitiveDefCategory{code=11, name='string', javaClassName='java.lang.String', guid='b34a64b9-554a-42b1-8f8a-7d5c2339f9c4'}, primitiveValue=null, instancePropertyCategory=InstancePropertyCategory{typeCode=1, typeName='Primitive', typeDescription='A primitive type.'}, typeGUID='null', typeName='null'}", propertyValueMap.get("abbreviation").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetStringPropertyValue() {
        try {
            Method method = classInstance.getClass().getDeclaredMethod("getStringPropertyValue", String.class);
            method.setAccessible(true);
            Object result = method.invoke(classInstance, "test");
            PrimitivePropertyValue primitivePropertyValue = (PrimitivePropertyValue) result;
            Assert.assertEquals("test", primitivePropertyValue.getPrimitiveValue().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetBooleanPropertyValue() {
        try {
            Method method = classInstance.getClass().getDeclaredMethod("getBooleanPropertyValue", Boolean.class);
            method.setAccessible(true);
            Object result = method.invoke(classInstance, true);
            PrimitivePropertyValue primitivePropertyValue = (PrimitivePropertyValue) result;
            Assert.assertEquals(true, primitivePropertyValue.getPrimitiveValue());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String readFile(File fileName){
        BufferedReader reader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line;

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                // stringBuilder.append(ls);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return stringBuilder.toString();
    }
}