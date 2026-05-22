/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.types;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.frameworks.openmetadata.builders.OpenMetadataClassificationBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.builders.OpenMetadataElementBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.builders.OpenMetadataRelationshipBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.converters.OpenMetadataPropertyConverterBase;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementControlHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementType;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.opentypes.OpenMetadataTypesArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveTypeStore;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.utilities.OMRSRepositoryPropertiesUtilities;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.*;

/**
 * Verify the type definition matches the bean implementation.
 */
public class OpenMetadataTypeTest
{
    OpenMetadataClassificationBuilder classificationBuilder = new OpenMetadataClassificationBuilder();
    OpenMetadataRelationshipBuilder   relationshipBuilder = new OpenMetadataRelationshipBuilder();
    OpenMetadataElementBuilder        elementBuilder = new OpenMetadataElementBuilder();
    OpenMetadataPropertyConverterBase propertyConverter = new OpenMetadataPropertyConverterBase(new PropertyHelper(),
                                                                                                this.getClass().getName());

    private final PropertyHelper propertyHelper = new PropertyHelper();
    private final Map<String, TypeDef>                typeMap          = new HashMap<>();
    private final Map<String, String>                 typeSuperTypeMap = new HashMap<>();
    private final Map<String, List<TypeDefAttribute>> typePropertyMap  = new HashMap<>();
    private final Map<String, String>                 usedGUIDMap      = new HashMap<>();

    private void setUpTypeMaps() throws InvalidParameterException
    {
        final String methodName = "setUpTypeMaps";

        OpenMetadataTypesArchive archive = new OpenMetadataTypesArchive();
        //load the archive
        OpenMetadataArchive          archiveProperties = archive.getOpenMetadataArchive();
        OpenMetadataArchiveTypeStore typeStore         = archiveProperties.getArchiveTypeStore();

        assert (typeStore != null);
        assert (typeStore.getNewTypeDefs() != null);

        for (TypeDef newTypeDef : typeStore.getNewTypeDefs())
        {
            setUpType(newTypeDef);
        }

        if (typeStore.getTypeDefPatches() != null)
        {
            for (TypeDefPatch typeDefPatch : typeStore.getTypeDefPatches())
            {
                if (typeDefPatch.getSuperType() != null)
                {
                    OMRSRepositoryPropertiesUtilities utilities = new OMRSRepositoryPropertiesUtilities();
                    TypeDef newTypeDef = utilities.applyPatch("OpenMetadataTypeTest",
                                                              typeMap.get(typeDefPatch.getTypeDefName()),
                                                                          typeDefPatch,
                                                                          methodName);
                    setUpType(newTypeDef);
                }
            }
        }
    }


    private void setUpType(TypeDef newTypeDef)
    {
        typeMap.put(newTypeDef.getName(), newTypeDef);

        if (newTypeDef.getSuperType() != null)
        {
            typeSuperTypeMap.put(newTypeDef.getName(), newTypeDef.getSuperType().getName());
        }

        List<TypeDefAttribute> typeDefAttributes = getSuperTypeProperties(newTypeDef.getName());

        if (typeDefAttributes != null)
        {
            typePropertyMap.put(newTypeDef.getName(), typeDefAttributes);
        }
    }


    private List<TypeDefAttribute> getSuperTypeProperties(String typeName)
    {
        String superTypeName = typeSuperTypeMap.get(typeName);

        List<TypeDefAttribute> typeDefAttributes = null;

        if (superTypeName != null)
        {
            typeDefAttributes = getSuperTypeProperties(superTypeName);
        }

        TypeDef typeDef = typeMap.get(typeName);

        if (typeDef.getPropertiesDefinition() != null)
        {
            if (typeDefAttributes == null)
            {
                typeDefAttributes = new ArrayList<>(typeDef.getPropertiesDefinition());
            }
            else
            {
                typeDefAttributes.addAll(typeDef.getPropertiesDefinition());
            }
        }

        return typeDefAttributes;
    }


    /**
     * Set up the type - including super types.
     *
     * @param openMetadataType type
     * @return type
     */
    private ElementType getElementType(OpenMetadataType openMetadataType)
    {
       ElementType elementType = new ElementType();

       elementType.setTypeName(openMetadataType.typeName);
       elementType.setTypeId(openMetadataType.typeGUID);
       elementType.setTypeDescription(openMetadataType.description);

       List<String> superTypes = new ArrayList<>();

       String superType = typeSuperTypeMap.get(openMetadataType.typeName);

       while (superType != null)
       {
           superTypes.add(superType);
           superType = typeSuperTypeMap.get(superType);
       }

       if (!superTypes.isEmpty())
       {
           elementType.setSuperTypeNames(superTypes);
       }

       return elementType;
   }


   @Test public void testAllGUIDs()
   {
       for (OpenMetadataType openMetadataType: OpenMetadataType.values())
       {
           String existingName = usedGUIDMap.put(openMetadataType.typeGUID, "Type::" + openMetadataType.typeName);
           assertNull(existingName, openMetadataType.typeName + " is using an already used GUID of " + openMetadataType.typeGUID + " that is shared with " + existingName);

           existingName = usedGUIDMap.put(openMetadataType.descriptionGUID, "TypeDescription::" + openMetadataType.typeName);
           assertNull(existingName, openMetadataType.typeName + " is using an already used GUID of " + openMetadataType.descriptionGUID + " that is shared with " + existingName);
       }

       for (OpenMetadataProperty openMetadataProperty: OpenMetadataProperty.values())
       {
           String existingName = usedGUIDMap.put(openMetadataProperty.descriptionGUID, "Property::" + openMetadataProperty.name);
           assertNull(existingName, openMetadataProperty.name + " is using an already used GUID of " + openMetadataProperty.descriptionGUID + " that is shared with " + existingName);
       }
   }

    /**
     * Validated the values of the enum.
     */
    @Test public void testAllTypes() throws InvalidParameterException
    {
        setUpTypeMaps();

        int typeCount = 0;
        int beanCount = 0;

        for (OpenMetadataType openMetadataType : OpenMetadataType.values())
        {
            typeCount++;

            TypeDef currentTypeDef = typeMap.get(openMetadataType.typeName);

            if (currentTypeDef == null)
            {
                System.out.println("Missing typeDef for " + openMetadataType.typeName);
            }

            if (openMetadataType.beanClass != null)
            {
                beanCount++;

                /*
                 * Check that the name of the bean for the type matches the type name.
                 */
                String[] classNameParts = openMetadataType.beanClass.getName().split("\\.");

                assertEquals(openMetadataType.typeName + "Properties", classNameParts[classNameParts.length - 1], "Bad bean class name");

                /*
                 * Check that the type name is set in the bean.
                 */
                try
                {
                    Object beanInstance = openMetadataType.beanClass.getDeclaredConstructor().newInstance();

                    if (beanInstance instanceof OpenMetadataRootProperties openMetadataRootProperties)
                    {
                        assertEquals(openMetadataType.typeName, openMetadataRootProperties.getTypeName(), "Bad entity bean type name");

                        if (currentTypeDef != null)
                        {
                            System.out.println("Checking properties for " + openMetadataType.typeName);

                            ElementProperties elementProperties = this.getElementPropertiesForType(openMetadataType);

                            OpenMetadataElement openMetadataElement = new OpenMetadataElement();

                            openMetadataElement.setElementProperties(elementProperties);
                            openMetadataElement.setType(getElementType(openMetadataType));

                            OpenMetadataRootProperties newBeanProperties = propertyConverter.getBeanProperties(openMetadataElement);


                            assertEquals(newBeanProperties.getClass().getName(), beanInstance.getClass().getName(), "Bad entity bean class name returned: " + newBeanProperties.getClass().getName() + " expected: "  + beanInstance.getClass().getName());

                            assertNull(newBeanProperties.getExtendedProperties(), "Unexpected extended properties found for " + openMetadataType.typeName);

                            ElementProperties returnedElementProperties = elementBuilder.getElementProperties(newBeanProperties);

                            if (returnedElementProperties == null)
                            {
                                System.out.println("Null returned element properties for " + openMetadataType.typeName);
                                assertNull(elementProperties, "Null returned element properties for " + openMetadataType.typeName);
                            }
                            else if (elementProperties != null)
                            {
                                openMetadataElement.setElementProperties(returnedElementProperties);
                                assertEquals(newBeanProperties, propertyConverter.getBeanProperties(openMetadataElement));
                            }

                            OpenMetadataRootProperties mappedBeanProperties = this.testJackson(newBeanProperties);

                            returnedElementProperties = elementBuilder.getElementProperties(mappedBeanProperties);

                            if (returnedElementProperties == null)
                            {
                                System.out.println("Null returned mapped element properties for " + openMetadataType.typeName);
                                assertNull(returnedElementProperties, "Null returned mapped element properties for " + openMetadataType.typeName);
                            }
                        }
                    }
                    else if (beanInstance instanceof RelationshipBeanProperties relationshipBeanProperties)
                    {
                        assertEquals(openMetadataType.typeName, relationshipBeanProperties.getTypeName(), "Bad relationship bean type name " + openMetadataType.typeName);

                        if (currentTypeDef != null)
                        {
                            System.out.println("Checking properties for " + openMetadataType.typeName);

                            ElementProperties elementProperties = this.getElementPropertiesForType(openMetadataType);

                            ElementControlHeader relationshipHeader = new ElementControlHeader();
                            relationshipHeader.setType(getElementType(openMetadataType));

                            RelationshipBeanProperties newBeanProperties = propertyConverter.getRelationshipProperties(relationshipHeader, elementProperties);

                            assertEquals(newBeanProperties.getClass().getName(), beanInstance.getClass().getName(), "Bad relationship bean class name returned: " + newBeanProperties.getClass().getName() + " expected: "  + beanInstance.getClass().getName());

                            assertNull(newBeanProperties.getExtendedProperties(), "Unexpected extended properties found for " + openMetadataType.typeName);

                            ElementProperties returnedElementProperties = relationshipBuilder.getElementProperties(newBeanProperties);

                            if (returnedElementProperties == null)
                            {
                                System.out.println("Null returned element properties for " + openMetadataType.typeName);
                                assertNull(returnedElementProperties, "Null returned element properties for " + openMetadataType.typeName);
                            }
                            else if (elementProperties != null)
                            {
                                assertEquals(newBeanProperties, propertyConverter.getRelationshipProperties(relationshipHeader, returnedElementProperties));
                            }

                            RelationshipBeanProperties mappedBeanProperties = this.testJackson(newBeanProperties);

                            returnedElementProperties = relationshipBuilder.getElementProperties(mappedBeanProperties);

                            if (returnedElementProperties == null)
                            {
                                System.out.println("Null returned mapped element properties for " + openMetadataType.typeName);
                                assertNull(returnedElementProperties, "Null returned mapped element properties for " + openMetadataType.typeName);
                            }
                            else if (elementProperties != null)
                            {
                                assertEquals(newBeanProperties, propertyConverter.getRelationshipProperties(relationshipHeader, returnedElementProperties));
                            }
                        }
                    }
                    else if (beanInstance instanceof ClassificationBeanProperties classificationBeanProperties)
                    {
                        assertEquals(openMetadataType.typeName, classificationBeanProperties.getTypeName(), "Bad classification bean type name");

                        if (currentTypeDef != null)
                        {
                            System.out.println("Checking properties for " + openMetadataType.typeName);

                            ElementProperties elementProperties = this.getElementPropertiesForType(openMetadataType);

                            AttachedClassification attachedClassification = new AttachedClassification();

                            attachedClassification.setType(getElementType(openMetadataType));
                            attachedClassification.setClassificationProperties(elementProperties);

                            ClassificationBeanProperties newBeanProperties = propertyConverter.getClassificationProperties(attachedClassification);

                            assertEquals(newBeanProperties.getClass().getName(), beanInstance.getClass().getName());

                            assertNull(newBeanProperties.getExtendedProperties(), "Unexpected extended properties found for " + openMetadataType.typeName);

                            ElementProperties returnedElementProperties = classificationBuilder.getElementProperties(newBeanProperties);

                            if (returnedElementProperties == null)
                            {
                                System.out.println("Null returned element properties for " + openMetadataType.typeName);
                                assertNull(elementProperties, "Null returned element properties for " + openMetadataType.typeName);
                            }
                            else if (elementProperties != null)
                            {
                                attachedClassification.setClassificationProperties(returnedElementProperties);
                                assertEquals(newBeanProperties, propertyConverter.getClassificationProperties(attachedClassification));
                            }

                            ClassificationBeanProperties mappedBeanProperties = this.testJackson(newBeanProperties);

                            returnedElementProperties = classificationBuilder.getElementProperties(mappedBeanProperties);

                            if (returnedElementProperties == null)
                            {
                                System.out.println("Null returned mapped element properties for " + openMetadataType.typeName);
                                assertNull(returnedElementProperties, "Null returned mapped element properties for " + openMetadataType.typeName);
                            }
                        }
                    }

                    /*
                     * Check that the toString has the right class name in it
                     */
                    assertTrue(beanInstance.toString().contains(openMetadataType.typeName), "Bad toString");
                }
                catch (Exception error)
                {
                    fail("Unable to create instance", error);
                }
            }
            else
            {
                System.out.println("No bean for " + openMetadataType.typeName);
            }
        }



        System.out.println("Open Metadata Type Count: " + typeCount);
        System.out.println("Open Metadata Bean Count: " + beanCount);
    }


    private ElementProperties getElementPropertiesForType(OpenMetadataType openMetadataType)
    {
        if (openMetadataType.beanClass != null)
        {
            TypeDef currentTypeDef = typeMap.get(openMetadataType.typeName);

            if (currentTypeDef != null)
            {

                List<TypeDefAttribute> typeDefAttributes = typePropertyMap.get(openMetadataType.typeName);

                if (typeDefAttributes != null)
                {
                    ElementProperties elementProperties = new ElementProperties();

                    for (TypeDefAttribute typeDefAttribute : typeDefAttributes)
                    {
                        addProperty(elementProperties,
                                    typeDefAttribute.getAttributeName(),
                                    typeDefAttribute.getAttributeType());
                    }

                    if (elementProperties.getPropertyCount() > 0)
                    {
                        return elementProperties;
                    }
                }
            }
        }

        return null;
    }


    /**
     * Add property to the element properties based on the attribute type definition.
     *
     * @param elementProperties destination
     * @param propertyName name of property
     * @param attributeTypeDef type of property
     */
    private void addProperty(ElementProperties elementProperties,
                             String            propertyName,
                             AttributeTypeDef  attributeTypeDef)
    {
        if (attributeTypeDef instanceof PrimitiveDef primitiveDef)
        {
            if (primitiveDef.getPrimitiveDefCategory() == PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING)
            {
                propertyHelper.addStringProperty(elementProperties,
                                                 propertyName,
                                                 propertyName + " value");
            }
            else if (primitiveDef.getPrimitiveDefCategory() == PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BOOLEAN)
            {
                propertyHelper.addBooleanProperty(elementProperties,
                                                  propertyName,
                                                  true);
            }
            else if (primitiveDef.getPrimitiveDefCategory() == PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT)
            {
                propertyHelper.addIntProperty(elementProperties,
                                                  propertyName,
                                                  propertyName.length());
            }
            else if (primitiveDef.getPrimitiveDefCategory() == PrimitiveDefCategory.OM_PRIMITIVE_TYPE_LONG)
            {
                propertyHelper.addLongProperty(elementProperties,
                                               propertyName,
                                               (propertyName.length() * 2L));
            }
            else if (primitiveDef.getPrimitiveDefCategory() == PrimitiveDefCategory.OM_PRIMITIVE_TYPE_FLOAT)
            {
                propertyHelper.addFloatProperty(elementProperties,
                                               propertyName,
                                               (propertyName.length() * 3));
            }
            else if (primitiveDef.getPrimitiveDefCategory() == PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE)
            {
                propertyHelper.addDateProperty(elementProperties,
                                               propertyName,
                                               new Date());
            }
        }
    }


    /**
     * Validate that an object generated from a JSON String has the same content as the object used to
     * create the JSON String.
     */
    private OpenMetadataRootProperties testJackson(OpenMetadataRootProperties openMetadataRootProperties)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String       jsonString   = null;

        /*
         * This class
         */
        try
        {
            jsonString = objectMapper.writeValueAsString(openMetadataRootProperties);
        }
        catch (Throwable exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        OpenMetadataRootProperties mappedBeanProperties = null;

        try
        {
            mappedBeanProperties = objectMapper.readValue(jsonString, OpenMetadataRootProperties.class);
        }
        catch (Throwable exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        if (mappedBeanProperties != null)
        {
            assertEquals(openMetadataRootProperties, mappedBeanProperties, "Bean properties must match after passing through Jackson");
        }

        return mappedBeanProperties;
    }



    /**
     * Validate that an object generated from a JSON String has the same content as the object used to
     * create the JSON String.
     */
    private RelationshipBeanProperties testJackson(RelationshipBeanProperties relationshipBeanProperties)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String       jsonString   = null;

        /*
         * This class
         */
        try
        {
            jsonString = objectMapper.writeValueAsString(relationshipBeanProperties);
        }
        catch (Throwable exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        RelationshipBeanProperties mappedBeanProperties = null;

        try
        {
            mappedBeanProperties = objectMapper.readValue(jsonString, RelationshipBeanProperties.class);
        }
        catch (Throwable exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        if (mappedBeanProperties != null)
        {
            assertEquals(relationshipBeanProperties, mappedBeanProperties, "Bean properties must match after passing through Jackson");
        }

        return mappedBeanProperties;
    }


    /**
     * Validate that an object generated from a JSON String has the same content as the object used to
     * create the JSON String.
     */
    private ClassificationBeanProperties testJackson(ClassificationBeanProperties classificationBeanProperties)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String       jsonString   = null;

        /*
         * This class
         */
        try
        {
            jsonString = objectMapper.writeValueAsString(classificationBeanProperties);
        }
        catch (Throwable exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        ClassificationBeanProperties mappedBeanProperties = null;

        try
        {
            mappedBeanProperties = objectMapper.readValue(jsonString, ClassificationBeanProperties.class);
        }
        catch (Throwable exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        if (mappedBeanProperties != null)
        {
            assertEquals(classificationBeanProperties, mappedBeanProperties, "Bean properties must match after passing through Jackson");
        }

        return mappedBeanProperties;
    }
}
