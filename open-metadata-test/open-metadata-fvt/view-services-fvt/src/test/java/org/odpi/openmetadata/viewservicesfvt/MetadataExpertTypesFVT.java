/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataAttributeTypeDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataEntityDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataEnumDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataEnumElementDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataPrimitiveDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDefAttribute;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDefLink;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDefPatch;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.omf.rest.AttributeTypeDefListResponse;
import org.odpi.openmetadata.frameworkservices.omf.rest.TypeDefResponse;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * MetadataExpertTypesFVT covers the Metadata Expert OMVS's type definition requests: it defines an enum, uses
 * it in a new entity type, extends that type with a patch, and deletes both again.  Each step is checked through
 * the Valid Metadata OMVS's type queries, as a caller would see the types.  The names are unique to the run, so
 * a run against a repository that kept a previous run's types is not affected.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MetadataExpertTypesFVT
{
    private static final ViewServiceClient metadataExpert = new ViewServiceClient(ViewServiceDescription.METADATA_EXPERT);
    private static final ViewServiceClient validMetadata  = new ViewServiceClient(ViewServiceDescription.VALID_METADATA);

    private static final String ENUM_NAME = "ViewServicesFvtCuisine" + OMAGPlatformExtension.RUN_ID;
    private static final String TYPE_NAME = "ViewServicesFvtRecipe" + OMAGPlatformExtension.RUN_ID;

    private static String enumDefGUID = null;
    private static String typeDefGUID = null;


    /**
     * A new enum is listed with the other enums.
     */
    @Test
    @Order(1)
    void addEnumDef()
    {
        OpenMetadataEnumDef enumDef = new OpenMetadataEnumDef();

        enumDef.setName(ENUM_NAME);
        enumDef.setDescription("The style of cooking that a recipe belongs to.");
        enumDef.setElementDefs(List.of(enumElement(0, "Unclassified"), enumElement(1, "Italian"), enumElement(99, "Other")));
        enumDef.setDefaultValue(enumElement(0, "Unclassified"));

        enumDefGUID = metadataExpert.create("/open-metadata-attribute-types/enum-defs", enumDef);

        OpenMetadataEnumDef stored = (OpenMetadataEnumDef) findAttributeTypeDef(ENUM_NAME);

        assertNotNull(stored, "the new enum is not listed");
        assertEquals(enumDefGUID, stored.getGUID());
        assertEquals(List.of("Unclassified", "Italian", "Other"), stored.getElementDefs().stream().map(OpenMetadataEnumElementDef::getValue).toList());
    }


    /**
     * A new entity type, with an attribute of the new enum type, can be read back by name.
     */
    @Test
    @Order(2)
    void addTypeDef()
    {
        OpenMetadataEntityDef entityDef = new OpenMetadataEntityDef();

        OpenMetadataTypeDefLink superType = new OpenMetadataTypeDefLink();

        superType.setName(OpenMetadataType.REFERENCEABLE.typeName);

        OpenMetadataEnumDef cuisineType = new OpenMetadataEnumDef();

        cuisineType.setName(ENUM_NAME);

        entityDef.setName(TYPE_NAME);
        entityDef.setDescription("A description of how to prepare a dish.");
        entityDef.setSuperType(superType);
        entityDef.setAttributeDefinitions(List.of(attribute("cuisine", cuisineType), attribute("servings", primitive("int"))));

        typeDefGUID = metadataExpert.create("/open-metadata-types", entityDef);

        OpenMetadataTypeDef stored = getTypeDef();

        assertEquals(typeDefGUID, stored.getGUID());
        assertEquals(OpenMetadataType.REFERENCEABLE.typeName, stored.getSuperType().getName());
        assertEquals(List.of("cuisine", "servings"), attributeNames(stored).stream().sorted().toList());
    }


    /**
     * A patch adds an attribute to the new type.
     */
    @Test
    @Order(3)
    void updateTypeDef()
    {
        OpenMetadataTypeDef before = getTypeDef();

        OpenMetadataTypeDefPatch patch = new OpenMetadataTypeDefPatch();

        patch.setTypeDefGUID(typeDefGUID);
        patch.setTypeDefName(TYPE_NAME);
        patch.setApplyToVersion(before.getVersion());
        patch.setAttributeDefinitions(List.of(attribute("preparationTimeMinutes", primitive("int"))));

        OpenMetadataTypeDef updated = metadataExpert.post("/open-metadata-types/update", patch, TypeDefResponse.class).getTypeDef();

        assertNotNull(updated, "the update returned no type definition");
        assertTrue(attributeNames(updated).contains("preparationTimeMinutes"), "the update did not return the new attribute");
        assertTrue(updated.getVersion() > before.getVersion(), "the update did not increase the version");

        assertEquals(List.of("cuisine", "preparationTimeMinutes", "servings"), attributeNames(getTypeDef()).stream().sorted().toList(),
                     "the stored type definition does not have the new attribute");
    }


    /**
     * The type is deleted, and then the enum it used.
     */
    @Test
    @Order(4)
    void deleteTypeDefAndEnumDef()
    {
        metadataExpert.call("/open-metadata-types/guid/" + typeDefGUID + "/delete?typeDefName=" + TYPE_NAME, new NullRequestBody());

        TypeDefResponse afterDelete = new RestTemplate().getForObject(OMAGPlatformExtension.getPlatformURLRoot()
                                                                              + "/servers/" + OMAGPlatformExtension.VIEW_SERVER_NAME
                                                                              + "/api/open-metadata/" + ViewServiceDescription.VALID_METADATA.getViewServiceURLMarker()
                                                                              + "/open-metadata-types/name/" + TYPE_NAME,
                                                                      TypeDefResponse.class);

        assertNotNull(afterDelete);
        assertTrue((afterDelete.getRelatedHTTPCode() != 200) || (afterDelete.getTypeDef() == null), "the type is still returned after it was deleted");

        metadataExpert.call("/open-metadata-attribute-types/enum-defs/guid/" + enumDefGUID + "/delete?enumDefName=" + ENUM_NAME, new NullRequestBody());

        assertFalse(findAttributeTypeDef(ENUM_NAME) != null, "the enum is still listed after it was deleted");
    }


    private static OpenMetadataEnumElementDef enumElement(int    ordinal,
                                                          String value)
    {
        OpenMetadataEnumElementDef elementDef = new OpenMetadataEnumElementDef();

        elementDef.setOrdinal(ordinal);
        elementDef.setValue(value);
        elementDef.setDescription(value + " cooking.");

        return elementDef;
    }


    private static OpenMetadataPrimitiveDef primitive(String name)
    {
        OpenMetadataPrimitiveDef primitiveDef = new OpenMetadataPrimitiveDef();

        primitiveDef.setName(name);

        return primitiveDef;
    }


    private static OpenMetadataTypeDefAttribute attribute(String                       name,
                                                          OpenMetadataAttributeTypeDef type)
    {
        OpenMetadataTypeDefAttribute attribute = new OpenMetadataTypeDefAttribute();

        attribute.setAttributeName(name);
        attribute.setAttributeType(type);
        attribute.setAttributeDescription("The " + name + " of the recipe.");

        return attribute;
    }


    private static OpenMetadataTypeDef getTypeDef()
    {
        OpenMetadataTypeDef typeDef = validMetadata.get("/open-metadata-types/name/" + TYPE_NAME, TypeDefResponse.class).getTypeDef();

        assertNotNull(typeDef, "type " + TYPE_NAME + " is not found");
        assertEquals(TYPE_NAME, typeDef.getName());

        return typeDef;
    }


    private static List<String> attributeNames(OpenMetadataTypeDef typeDef)
    {
        assertNotNull(typeDef.getAttributeDefinitions(), typeDef.getName() + " has no attributes");

        return typeDef.getAttributeDefinitions().stream().map(OpenMetadataTypeDefAttribute::getAttributeName).toList();
    }


    private static OpenMetadataAttributeTypeDef findAttributeTypeDef(String name)
    {
        List<OpenMetadataAttributeTypeDef> attributeTypeDefs = validMetadata.get("/open-metadata-types/attribute-defs?category=ENUM",
                                                                                 AttributeTypeDefListResponse.class).getAttributeTypeDefs();

        assertNotNull(attributeTypeDefs);

        return attributeTypeDefs.stream().filter(attributeTypeDef -> name.equals(attributeTypeDef.getName())).findFirst().orElse(null);
    }
}
