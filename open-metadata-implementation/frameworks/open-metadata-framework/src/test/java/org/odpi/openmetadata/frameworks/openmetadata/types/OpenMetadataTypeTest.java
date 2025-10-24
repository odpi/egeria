/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.types;


import org.odpi.openmetadata.frameworks.openmetadata.builders.OpenMetadataClassificationBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.builders.OpenMetadataElementBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.builders.OpenMetadataRelationshipBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.converters.OpenMetadataPropertyConverterBase;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementControlHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementType;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.opentypes.OpenMetadataTypesArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveTypeStore;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

/**
 * Verify the OMFErrorCode enum contains unique message ids, non-null names and descriptions and can be
 * serialized to JSON and back again.
 */
public class OpenMetadataTypeTest
{
    OpenMetadataClassificationBuilder classificationBuilder = new OpenMetadataClassificationBuilder();
    OpenMetadataRelationshipBuilder   relationshipBuilder = new OpenMetadataRelationshipBuilder();
    OpenMetadataElementBuilder        elementBuilder = new OpenMetadataElementBuilder();
    OpenMetadataPropertyConverterBase propertyConverter = new OpenMetadataPropertyConverterBase(new PropertyHelper(),
                                                                                                this.getClass().getName());

    private final Map<String, TypeDef> typeMap          = new HashMap<>();
    private final Map<String, String>  typeSuperTypeMap = new HashMap<>();

    private void setUpTypeMaps()
    {
        OpenMetadataTypesArchive archive = new OpenMetadataTypesArchive();
        //load the archive
        OpenMetadataArchive          archiveProperties = archive.getOpenMetadataArchive();
        OpenMetadataArchiveTypeStore typeStore         = archiveProperties.getArchiveTypeStore();

        assert (typeStore != null);
        assert (typeStore.getNewTypeDefs() != null);

        for (TypeDef newTypeDef : typeStore.getNewTypeDefs())
        {
            typeMap.put(newTypeDef.getName(), newTypeDef);

            if (newTypeDef.getSuperType() != null)
            {
                typeSuperTypeMap.put(newTypeDef.getName(), newTypeDef.getSuperType().getName());
            }
        }
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

       String       superType  = typeSuperTypeMap.get(openMetadataType.typeName);
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


    /**
     * Validated the values of the enum.
     */
    @Test public void testAllTypes()
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
                            ElementProperties elementProperties = elementBuilder.getElementProperties(openMetadataRootProperties);

                            OpenMetadataElement openMetadataElement = new OpenMetadataElement();

                            openMetadataElement.setElementProperties(elementProperties);
                            openMetadataElement.setType(getElementType(openMetadataType));

                            OpenMetadataRootProperties returnedProperties = propertyConverter.getBeanProperties(openMetadataElement);

                            assertEquals(returnedProperties.getClass().getName(), beanInstance.getClass().getName());
                        }
                    }
                    else if (beanInstance instanceof RelationshipBeanProperties relationshipBeanProperties)
                    {
                        assertEquals(openMetadataType.typeName, relationshipBeanProperties.getTypeName(), "Bad relationship bean type name");

                        if (currentTypeDef != null)
                        {
                            ElementProperties relationshipProperties = relationshipBuilder.getElementProperties(relationshipBeanProperties);

                            ElementControlHeader relationshipHeader = new ElementControlHeader();

                            relationshipHeader.setType(getElementType(openMetadataType));

                            RelationshipBeanProperties returnedProperties = propertyConverter.getRelationshipProperties(relationshipHeader,
                                                                                                                        relationshipProperties);

                            assertEquals(returnedProperties.getClass().getName(), beanInstance.getClass().getName());
                        }
                    }
                    else if (beanInstance instanceof ClassificationBeanProperties classificationBeanProperties)
                    {
                        assertEquals(openMetadataType.typeName, classificationBeanProperties.getTypeName(), "Bad classification bean type name");

                        if (currentTypeDef != null)
                        {
                            ElementProperties classificationProperties = classificationBuilder.getElementProperties(classificationBeanProperties);

                            AttachedClassification attachedClassification = new AttachedClassification();

                            attachedClassification.setType(getElementType(openMetadataType));
                            attachedClassification.setClassificationProperties(classificationProperties);

                            ClassificationBeanProperties returnedProperties = propertyConverter.getClassificationProperties(attachedClassification);

                            assertEquals(returnedProperties.getClass().getName(), beanInstance.getClass().getName());
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
        }

        System.out.println("Open Metadata Type Count: " + typeCount);
        System.out.println("Open Metadata Bean Count: " + beanCount);
    }
}
