/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.beans;

import org.odpi.openmetadata.frameworks.openmetadata.builders.OpenMetadataClassificationBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.builders.OpenMetadataElementBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.builders.OpenMetadataRelationshipBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.EntityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRootProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.opentypes.OpenMetadataTypesArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveTypeStore;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefAttribute;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefPatch;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.utilities.OMRSRepositoryPropertiesUtilities;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Every property a builder writes must be declared by the type it is written to, or the repository rejects
 * the create or update (OMRS-REPOSITORY-400-028).  OpenMetadataTypeTest works from the type outwards, so it
 * only sees properties the type declares; a bean field that the builder writes but the type never declared
 * is invisible to it.  This test works from the bean inwards: it fills in every setter of each type's bean,
 * runs the builder, and checks each property written against the type's attributes (including those of its
 * supertypes and those added by patches).
 */
public class BuilderWritesDeclaredPropertiesTest
{
    private static final int SEED = 2;

    private final OpenMetadataElementBuilder        elementBuilder        = new OpenMetadataElementBuilder();
    private final OpenMetadataRelationshipBuilder   relationshipBuilder   = new OpenMetadataRelationshipBuilder();
    private final OpenMetadataClassificationBuilder classificationBuilder = new OpenMetadataClassificationBuilder();


    /**
     * Return the attribute names that each type declares, including those inherited from its supertypes.
     *
     * @return map of type name to attribute names
     * @throws Exception problem applying a patch
     */
    private Map<String, Set<String>> getDeclaredAttributes() throws Exception
    {
        OpenMetadataArchiveTypeStore typeStore = new OpenMetadataTypesArchive().getOpenMetadataArchive().getArchiveTypeStore();
        Map<String, TypeDef>         typeMap   = new HashMap<>();

        for (TypeDef typeDef : typeStore.getNewTypeDefs())
        {
            typeMap.put(typeDef.getName(), typeDef);
        }

        if (typeStore.getTypeDefPatches() != null)
        {
            OMRSRepositoryPropertiesUtilities utilities = new OMRSRepositoryPropertiesUtilities();

            for (TypeDefPatch typeDefPatch : typeStore.getTypeDefPatches())
            {
                typeMap.put(typeDefPatch.getTypeDefName(),
                            utilities.applyPatch(this.getClass().getName(),
                                                 typeMap.get(typeDefPatch.getTypeDefName()),
                                                 typeDefPatch,
                                                 "getDeclaredAttributes"));
            }
        }

        Map<String, Set<String>> declaredAttributes = new HashMap<>();

        for (String typeName : typeMap.keySet())
        {
            Set<String> attributeNames = new HashSet<>();
            TypeDef     typeDef        = typeMap.get(typeName);

            while (typeDef != null)
            {
                if (typeDef.getPropertiesDefinition() != null)
                {
                    for (TypeDefAttribute typeDefAttribute : typeDef.getPropertiesDefinition())
                    {
                        attributeNames.add(typeDefAttribute.getAttributeName());
                    }
                }

                typeDef = (typeDef.getSuperType() == null) ? null : typeMap.get(typeDef.getSuperType().getName());
            }

            declaredAttributes.put(typeName, attributeNames);
        }

        return declaredAttributes;
    }


    /**
     * Fill in each type's bean, build its properties and check that every property is declared by the type.
     *
     * @throws Exception problem setting up
     */
    @Test
    public void everyBuiltPropertyIsDeclaredByItsType() throws Exception
    {
        Map<String, Set<String>> declaredAttributes = this.getDeclaredAttributes();
        List<Class<?>>           beans              = FrameworkBeans.beanClasses();
        Set<String>              beanClassNames     = new HashSet<>();

        for (Class<?> bean : beans)
        {
            beanClassNames.add(bean.getName());
        }

        BeanValueFactory valueFactory = new BeanValueFactory(beanClassNames);
        SoftAssert       softAssert   = new SoftAssert();
        int              checkedTypes = 0;

        for (OpenMetadataType openMetadataType : OpenMetadataType.values())
        {
            Set<String> attributeNames = declaredAttributes.get(openMetadataType.typeName);

            if ((openMetadataType.beanClass == null) || (attributeNames == null))
            {
                continue;
            }

            Object            bean              = valueFactory.filledBean(openMetadataType.beanClass, SEED);
            ElementProperties elementProperties = null;

            /*
             * The type name and extended properties are filled with made-up values by the factory.  The
             * builder writes extended properties as they are, so they are cleared to leave only the
             * properties the builder maps itself.
             */
            if (bean instanceof OpenMetadataRootProperties rootProperties)
            {
                rootProperties.setTypeName(openMetadataType.typeName);
                rootProperties.setExtendedProperties(null);
                elementProperties = elementBuilder.getElementProperties((EntityProperties) rootProperties);
            }
            else if (bean instanceof RelationshipBeanProperties relationshipProperties)
            {
                relationshipProperties.setTypeName(openMetadataType.typeName);
                relationshipProperties.setExtendedProperties(null);
                elementProperties = relationshipBuilder.getElementProperties(relationshipProperties);
            }
            else if (bean instanceof ClassificationBeanProperties classificationProperties)
            {
                classificationProperties.setTypeName(openMetadataType.typeName);
                classificationProperties.setExtendedProperties(null);
                elementProperties = classificationBuilder.getElementProperties(classificationProperties);
            }

            if (elementProperties != null)
            {
                checkedTypes++;

                Iterator<String> propertyNames = elementProperties.getPropertyNames();

                while (propertyNames.hasNext())
                {
                    String propertyName = propertyNames.next();

                    softAssert.assertTrue(attributeNames.contains(propertyName),
                                          "The builder writes " + propertyName + " for " + openMetadataType.typeName +
                                                  " but the type does not declare it");
                }
            }
        }

        softAssert.assertTrue(checkedTypes > 500, "Only " + checkedTypes + " types were checked");
        softAssert.assertAll();
    }
}
