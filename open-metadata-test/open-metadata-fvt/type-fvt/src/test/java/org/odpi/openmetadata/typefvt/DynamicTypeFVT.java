/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.typefvt;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataTypesClient;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataAttributeTypeDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataAttributeTypeDefCategory;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataEntityDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataEnumDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataEnumElementDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataPrimitiveDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDefAttribute;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDefCategory;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDefLink;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDefPatch;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * DynamicTypeFVT drives the dynamic type APIs - adding, patching and deleting types and enums through the
 * open metadata store - over REST, the way a client such as pyegeria uses them.
 * <br>
 * Both defects these tests guard against were invisible to the unit tests, which build the repository's patch by
 * hand and mock the validator: every patch sent through the REST API was rejected because the converter never set
 * who made it and when (updatedBy and updateTime are mandatory), and every enum delete failed with "unknown TypeDef"
 * because the validator passed the GUID's parameter name as the GUID.
 * <br>
 * The repository is PostgreSQL, so the types added here outlive the run.  Each run uses new names, and each test
 * deletes what it added.  Anything a failed run left behind is removed before the tests start.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class DynamicTypeFVT
{
    private static final String ENTITY_PREFIX      = "TypeFvtRecipe";
    private static final String ENUM_PREFIX        = "TypeFvtCuisine";
    private static final String UNUSED_ENUM_PREFIX = "TypeFvtUnusedEnum";

    private final String runSuffix = Long.toString(System.currentTimeMillis());


    /**
     * Remove the types and enums that an earlier, failed run left in the repository - the entity types first,
     * since an enum cannot be deleted while a type uses it.
     *
     * @throws Exception problem talking to the server
     */
    @BeforeAll
    static void removeLeftoverTypes() throws Exception
    {
        OpenMetadataTypesClient typesClient = ConnectorContextFactory.newContext().getOpenMetadataTypesClient();

        List<OpenMetadataTypeDef> typeDefs = typesClient.getAllTypes(false, false).getTypeDefs();

        if (typeDefs != null)
        {
            for (OpenMetadataTypeDef typeDef : typeDefs)
            {
                if ((typeDef.getName() != null) && (typeDef.getName().startsWith(ENTITY_PREFIX)))
                {
                    typesClient.deleteTypeDef(typeDef.getGUID(), typeDef.getName());
                }
            }
        }

        List<OpenMetadataAttributeTypeDef> enumDefs = typesClient.getAttributeTypeDefs(OpenMetadataAttributeTypeDefCategory.ENUM);

        if (enumDefs != null)
        {
            for (OpenMetadataAttributeTypeDef enumDef : enumDefs)
            {
                if ((enumDef.getName() != null) &&
                        ((enumDef.getName().startsWith(ENUM_PREFIX)) || (enumDef.getName().startsWith(UNUSED_ENUM_PREFIX))))
                {
                    typesClient.deleteEnumDef(enumDef.getGUID(), enumDef.getName());
                }
            }
        }
    }


    /**
     * Add an enum and an entity type that uses it, patch a new attribute into the entity type, then delete
     * the entity type and the enum.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void typeCanBePatchedAndRemovedWithItsEnum() throws Exception
    {
        OpenMetadataTypesClient typesClient = ConnectorContextFactory.newContext().getOpenMetadataTypesClient();

        String enumName   = ENUM_PREFIX + runSuffix;
        String entityName = ENTITY_PREFIX + runSuffix;
        String enumGUID   = typesClient.addEnumDef(this.getEnumDef(enumName));

        assertNotNull(enumGUID, "No GUID returned for the new enum");

        String entityGUID = typesClient.addTypeDef(this.getEntityDef(entityName, enumName));

        assertNotNull(entityGUID, "No GUID returned for the new entity type");

        OpenMetadataTypeDef created = typesClient.getTypeDefByName(false, false, entityName);

        assertNotNull(created, "The new entity type could not be retrieved");

        /*
         * Patch an extra attribute into the entity type.
         */
        OpenMetadataTypeDefPatch patch = new OpenMetadataTypeDefPatch();

        patch.setTypeDefGUID(entityGUID);
        patch.setTypeDefName(entityName);
        patch.setApplyToVersion(created.getVersion());
        patch.setAttributeDefinitions(List.of(this.getAttribute("servings", this.getStringType())));

        OpenMetadataTypeDef patched = typesClient.updateTypeDef(patch);

        assertNotNull(patched, "No type returned from the patch");
        assertEquals(created.getVersion() + 1, patched.getVersion(), "The patch did not create the next version");

        OpenMetadataTypeDef retrieved = typesClient.getTypeDefByName(false, false, entityName);

        assertEquals(patched.getVersion(), retrieved.getVersion(), "The patched version was not stored");
        assertTrue(this.getAttributeNames(retrieved).contains("cuisine"), "Original attribute lost: " + this.getAttributeNames(retrieved));
        assertTrue(this.getAttributeNames(retrieved).contains("servings"), "Patched attribute missing: " + this.getAttributeNames(retrieved));
        assertEquals(OMAGPlatformExtension.USER_ID, retrieved.getUpdatedBy(), "The patch did not record who made it");
        assertNotNull(retrieved.getUpdateTime(), "The patch did not record when it was made");

        /*
         * Remove the entity type, then the enum it used.
         */
        typesClient.deleteTypeDef(entityGUID, entityName);
        typesClient.deleteEnumDef(enumGUID, enumName);

        assertThrows(Exception.class,
                     () -> typesClient.getAttributeTypeDefByGUID(enumGUID),
                     "The enum is still known after it was deleted");
    }


    /**
     * An enum that nothing uses can be deleted as soon as it is added.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void unusedEnumCanBeDeleted() throws Exception
    {
        OpenMetadataTypesClient typesClient = ConnectorContextFactory.newContext().getOpenMetadataTypesClient();

        String enumName = UNUSED_ENUM_PREFIX + runSuffix;
        String enumGUID = typesClient.addEnumDef(this.getEnumDef(enumName));

        assertEquals(enumName, typesClient.getAttributeTypeDefByGUID(enumGUID).getName(), "The new enum could not be retrieved");

        typesClient.deleteEnumDef(enumGUID, enumName);

        assertThrows(Exception.class,
                     () -> typesClient.getAttributeTypeDefByGUID(enumGUID),
                     "The enum is still known after it was deleted");
    }


    /**
     * Build an enum with two values.
     *
     * @param enumName name of the enum
     * @return enum definition
     */
    private OpenMetadataEnumDef getEnumDef(String enumName)
    {
        OpenMetadataEnumDef enumDef = new OpenMetadataEnumDef();

        enumDef.setName(enumName);
        enumDef.setCategory(OpenMetadataAttributeTypeDefCategory.ENUM);
        enumDef.setDescription("Throwaway enum created by type-fvt.");

        List<OpenMetadataEnumElementDef> elementDefs = new ArrayList<>();

        elementDefs.add(this.getEnumElement(0, "Italian"));
        elementDefs.add(this.getEnumElement(1, "Thai"));

        enumDef.setElementDefs(elementDefs);

        return enumDef;
    }


    private OpenMetadataEnumElementDef getEnumElement(int ordinal, String value)
    {
        OpenMetadataEnumElementDef elementDef = new OpenMetadataEnumElementDef();

        elementDef.setOrdinal(ordinal);
        elementDef.setValue(value);

        return elementDef;
    }


    /**
     * Build an entity type, a subtype of Referenceable, with one attribute typed by the enum.
     *
     * @param entityName name of the entity type
     * @param enumName name of the enum for its attribute
     * @return entity type definition
     */
    private OpenMetadataEntityDef getEntityDef(String entityName, String enumName)
    {
        OpenMetadataEntityDef entityDef = new OpenMetadataEntityDef();

        entityDef.setName(entityName);
        entityDef.setCategory(OpenMetadataTypeDefCategory.ENTITY_DEF);
        entityDef.setDescription("Throwaway entity type created by type-fvt.");

        OpenMetadataTypeDefLink superType = new OpenMetadataTypeDefLink();

        superType.setGUID(OpenMetadataType.REFERENCEABLE.typeGUID);
        superType.setName(OpenMetadataType.REFERENCEABLE.typeName);

        entityDef.setSuperType(superType);

        OpenMetadataEnumDef enumType = new OpenMetadataEnumDef();

        enumType.setName(enumName);
        enumType.setCategory(OpenMetadataAttributeTypeDefCategory.ENUM);

        entityDef.setAttributeDefinitions(List.of(this.getAttribute("cuisine", enumType)));

        return entityDef;
    }


    private OpenMetadataPrimitiveDef getStringType()
    {
        OpenMetadataPrimitiveDef stringType = new OpenMetadataPrimitiveDef();

        stringType.setName("string");
        stringType.setCategory(OpenMetadataAttributeTypeDefCategory.PRIMITIVE);

        return stringType;
    }


    private OpenMetadataTypeDefAttribute getAttribute(String                       attributeName,
                                                      OpenMetadataAttributeTypeDef attributeType)
    {
        OpenMetadataTypeDefAttribute attribute = new OpenMetadataTypeDefAttribute();

        attribute.setAttributeName(attributeName);
        attribute.setAttributeType(attributeType);
        attribute.setAttributeDescription("Attribute added by type-fvt.");

        return attribute;
    }


    private List<String> getAttributeNames(OpenMetadataTypeDef typeDef)
    {
        List<String> names = new ArrayList<>();

        if (typeDef.getAttributeDefinitions() != null)
        {
            for (OpenMetadataTypeDefAttribute attribute : typeDef.getAttributeDefinitions())
            {
                names.add(attribute.getAttributeName());
            }
        }

        return names;
    }
}
