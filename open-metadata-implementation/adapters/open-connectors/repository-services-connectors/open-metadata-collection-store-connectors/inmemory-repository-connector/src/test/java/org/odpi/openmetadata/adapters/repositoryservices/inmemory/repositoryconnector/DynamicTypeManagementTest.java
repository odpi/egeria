/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.inmemory.repositoryconnector;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.utilities.OMRSRepositoryPropertiesUtilities;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidTypeDefException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeDefInUseException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

/**
 * Verify how a repository built on OMRSDynamicTypeMetadataCollectionBase handles the types defined through the API -
 * the ones homed in the repository.  They are checked against the known types, kept in the type store, and can only
 * be deleted once nothing uses them.  Types from elsewhere pass through without being checked or stored.
 * <br>
 * The known types live in a small map behind a stubbed repository helper, standing in for the repository content
 * manager, so each test controls exactly what the type system holds.
 */
public class DynamicTypeManagementTest
{
    private static final String USER_ID         = "testUser";
    private static final String REPOSITORY_NAME = "testRepository";
    private static final String COLLECTION_ID   = "c9f5e7a4-1c3d-4b2a-9e8f-7d6c5b4a3210";
    private static final String ARCHIVE_ID      = "bce3b0a0-662a-4f87-b8dc-844078a11a6e";

    private final Map<String, TypeDef>          knownTypeDefs          = new LinkedHashMap<>();
    private final Map<String, AttributeTypeDef> knownAttributeTypeDefs = new LinkedHashMap<>();

    private OMRSRepositoryHelper           repositoryHelper;
    private InMemoryOMRSMetadataCollection metadataCollection;


    /**
     * Build a metadata collection whose known types are a string primitive and a Referenceable entity type with a
     * qualifiedName attribute, both from an archive.
     *
     * @throws Exception unexpected
     */
    @BeforeMethod
    public void setUp() throws Exception
    {
        knownTypeDefs.clear();
        knownAttributeTypeDefs.clear();

        repositoryHelper = mock(OMRSRepositoryHelper.class);

        OMRSRepositoryValidator repositoryValidator = mock(OMRSRepositoryValidator.class);

        when(repositoryHelper.getTypeDefByName(anyString(), anyString()))
                .thenAnswer(invocation -> knownTypeDefs.get((String) invocation.getArgument(1)));
        when(repositoryHelper.getAttributeTypeDefByName(anyString(), anyString()))
                .thenAnswer(invocation -> knownAttributeTypeDefs.get((String) invocation.getArgument(1)));
        when(repositoryHelper.getKnownTypeDefs())
                .thenAnswer(invocation -> new ArrayList<>(knownTypeDefs.values()));
        when(repositoryHelper.getAllPropertiesForTypeDef(anyString(), any(TypeDef.class), anyString()))
                .thenAnswer(invocation -> this.getAllProperties(invocation.getArgument(1)));
        when(repositoryHelper.applyPatch(anyString(), any(TypeDef.class), any(TypeDefPatch.class)))
                .thenAnswer(invocation -> new OMRSRepositoryPropertiesUtilities().applyPatch(invocation.getArgument(0),
                                                                                             invocation.getArgument(1),
                                                                                             invocation.getArgument(2),
                                                                                             "applyPatch"));
        when(repositoryValidator.validateTypeDefPatch(anyString(), any(TypeDefPatch.class), anyString()))
                .thenAnswer(invocation -> knownTypeDefs.get(((TypeDefPatch) invocation.getArgument(1)).getTypeDefName()));

        metadataCollection = new InMemoryOMRSMetadataCollection(mock(InMemoryOMRSRepositoryConnector.class),
                                                                REPOSITORY_NAME,
                                                                repositoryHelper,
                                                                repositoryValidator,
                                                                COLLECTION_ID);

        PrimitiveDef stringDef = new PrimitiveDef(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);

        stringDef.setGUID("b34a64b9-554a-42b1-8f8a-7d5c2339f9c4");
        stringDef.setName("string");
        knownAttributeTypeDefs.put(stringDef.getName(), stringDef);

        EntityDef referenceable = this.getEntityDef("a32316b8-dc8c-48c5-b12b-71c1b2a080bf", "Referenceable", null, ARCHIVE_ID);

        referenceable.setPropertiesDefinition(List.of(this.getAttribute("qualifiedName", stringDef)));
        knownTypeDefs.put(referenceable.getName(), referenceable);
    }


    /**
     * A new entity type defined through the API is checked and kept in the type store.
     *
     * @throws Exception unexpected
     */
    @Test
    public void testHomedTypeDefIsStored() throws Exception
    {
        EntityDef newType = this.getEntityDef("0d0e4e3c-4b6b-4f39-9a8e-1b6d6a7ef001", "Recipe", "Referenceable", COLLECTION_ID);

        newType.setPropertiesDefinition(List.of(this.getAttribute("cuisine", knownAttributeTypeDefs.get("string"))));

        metadataCollection.addTypeDef(USER_ID, newType);

        TypeDefGallery storedTypes = metadataCollection.getStoredTypes(USER_ID);

        assertNotNull(storedTypes);
        assertEquals(storedTypes.getTypeDefs().size(), 1);
        assertEquals(storedTypes.getTypeDefs().get(0).getName(), "Recipe");
    }


    /**
     * A type from an archive is neither checked nor stored - its originator supplies it again at each restart.
     *
     * @throws Exception unexpected
     */
    @Test
    public void testArchiveTypeDefIsNotStored() throws Exception
    {
        EntityDef archiveType = this.getEntityDef("0d0e4e3c-4b6b-4f39-9a8e-1b6d6a7ef002", "Asset", "NotAKnownType", ARCHIVE_ID);

        metadataCollection.addTypeDef(USER_ID, archiveType);

        assertNull(metadataCollection.getStoredTypes(USER_ID));
    }


    /**
     * A homed type must build on a known supertype.
     */
    @Test
    public void testUnknownSupertypeIsRejected()
    {
        EntityDef newType = this.getEntityDef("0d0e4e3c-4b6b-4f39-9a8e-1b6d6a7ef003", "Recipe", "NotAKnownType", COLLECTION_ID);

        assertThrows(InvalidTypeDefException.class, () -> metadataCollection.addTypeDef(USER_ID, newType));
    }


    /**
     * A homed type may not redefine an attribute that it inherits.
     */
    @Test
    public void testInheritedAttributeIsRejected()
    {
        EntityDef newType = this.getEntityDef("0d0e4e3c-4b6b-4f39-9a8e-1b6d6a7ef004", "Recipe", "Referenceable", COLLECTION_ID);

        newType.setPropertiesDefinition(List.of(this.getAttribute("qualifiedName", knownAttributeTypeDefs.get("string"))));

        assertThrows(InvalidTypeDefException.class, () -> metadataCollection.addTypeDef(USER_ID, newType));
    }


    /**
     * A homed type's attributes must be of known attribute types.
     */
    @Test
    public void testUnknownAttributeTypeIsRejected()
    {
        EnumDef unknownEnum = this.getEnumDef("0d0e4e3c-4b6b-4f39-9a8e-1b6d6a7ef005", "CuisineType", COLLECTION_ID);

        EntityDef newType = this.getEntityDef("0d0e4e3c-4b6b-4f39-9a8e-1b6d6a7ef006", "Recipe", "Referenceable", COLLECTION_ID);

        newType.setPropertiesDefinition(List.of(this.getAttribute("cuisine", unknownEnum)));

        assertThrows(InvalidTypeDefException.class, () -> metadataCollection.addTypeDef(USER_ID, newType));
    }


    /**
     * The ends of a homed relationship type must be entity types.
     */
    @Test
    public void testRelationshipEndMustBeEntityType()
    {
        ClassificationDef classificationDef = new ClassificationDef();

        classificationDef.setGUID("0d0e4e3c-4b6b-4f39-9a8e-1b6d6a7ef007");
        classificationDef.setName("Confidential");
        classificationDef.setVersion(1);
        classificationDef.setVersionName("1.0");
        classificationDef.setOrigin(ARCHIVE_ID);
        knownTypeDefs.put(classificationDef.getName(), classificationDef);

        RelationshipDef relationshipDef = new RelationshipDef();

        relationshipDef.setGUID("0d0e4e3c-4b6b-4f39-9a8e-1b6d6a7ef008");
        relationshipDef.setName("RecipeIngredient");
        relationshipDef.setVersion(1);
        relationshipDef.setVersionName("1.0");
        relationshipDef.setOrigin(COLLECTION_ID);
        relationshipDef.setEndDef1(this.getEndDef("Referenceable", "recipes"));
        relationshipDef.setEndDef2(this.getEndDef("Confidential", "ingredients"));

        assertThrows(InvalidTypeDefException.class, () -> metadataCollection.addTypeDef(USER_ID, relationshipDef));
    }


    /**
     * A well-formed enum defined through the API is stored; a malformed one, or a primitive, is rejected.
     *
     * @throws Exception unexpected
     */
    @Test
    public void testHomedEnumDefs() throws Exception
    {
        metadataCollection.addAttributeTypeDef(USER_ID, this.getEnumDef("0d0e4e3c-4b6b-4f39-9a8e-1b6d6a7ef009", "CuisineType", COLLECTION_ID));

        assertEquals(metadataCollection.getStoredTypes(USER_ID).getAttributeTypeDefs().size(), 1);

        EnumDef duplicateOrdinals = this.getEnumDef("0d0e4e3c-4b6b-4f39-9a8e-1b6d6a7ef010", "SpiceLevel", COLLECTION_ID);
        List<EnumElementDef> elementDefs = duplicateOrdinals.getElementDefs();

        elementDefs.get(1).setOrdinal(elementDefs.get(0).getOrdinal());
        duplicateOrdinals.setElementDefs(elementDefs);

        assertThrows(InvalidTypeDefException.class, () -> metadataCollection.addAttributeTypeDef(USER_ID, duplicateOrdinals));

        PrimitiveDef primitiveDef = new PrimitiveDef(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT);

        primitiveDef.setGUID("0d0e4e3c-4b6b-4f39-9a8e-1b6d6a7ef011");
        primitiveDef.setName("myInt");
        primitiveDef.setOrigin(COLLECTION_ID);

        assertThrows(InvalidTypeDefException.class, () -> metadataCollection.addAttributeTypeDef(USER_ID, primitiveDef));
    }


    /**
     * A patch to a homed type replaces the stored copy.
     *
     * @throws Exception unexpected
     */
    @Test
    public void testUpdatedTypeDefIsStored() throws Exception
    {
        EntityDef newType = this.getEntityDef("0d0e4e3c-4b6b-4f39-9a8e-1b6d6a7ef012", "Recipe", "Referenceable", COLLECTION_ID);

        metadataCollection.addTypeDef(USER_ID, newType);
        knownTypeDefs.put(newType.getName(), newType);

        TypeDefPatch typeDefPatch = new TypeDefPatch();

        typeDefPatch.setTypeDefGUID(newType.getGUID());
        typeDefPatch.setTypeDefName(newType.getName());
        typeDefPatch.setApplyToVersion(1);
        typeDefPatch.setUpdateToVersion(2);
        typeDefPatch.setNewVersionName("2.0");
        typeDefPatch.setUpdatedBy(USER_ID);
        typeDefPatch.setUpdateTime(new Date());
        typeDefPatch.setPropertyDefinitions(List.of(this.getAttribute("servings", knownAttributeTypeDefs.get("string"))));

        TypeDef updatedTypeDef = metadataCollection.updateTypeDef(USER_ID, typeDefPatch);

        assertEquals(updatedTypeDef.getVersion(), 2L);

        TypeDef storedTypeDef = metadataCollection.getStoredTypes(USER_ID).getTypeDefs().get(0);

        assertEquals(storedTypeDef.getVersion(), 2L);
        assertEquals(storedTypeDef.getPropertiesDefinition().get(0).getAttributeName(), "servings");
    }


    /**
     * An unused homed type can be deleted, and leaves the type store.
     *
     * @throws Exception unexpected
     */
    @Test
    public void testUnusedTypeDefIsDeleted() throws Exception
    {
        EntityDef newType = this.getEntityDef("0d0e4e3c-4b6b-4f39-9a8e-1b6d6a7ef013", "Recipe", "Referenceable", COLLECTION_ID);

        metadataCollection.addTypeDef(USER_ID, newType);
        knownTypeDefs.put(newType.getName(), newType);

        metadataCollection.deleteTypeDef(USER_ID, newType.getGUID(), newType.getName());

        assertNull(metadataCollection.getStoredTypes(USER_ID));
    }


    /**
     * A type with a soft-deleted instance is still in use.
     *
     * @throws Exception unexpected
     */
    @Test
    public void testTypeDefWithDeletedInstanceIsInUse() throws Exception
    {
        EntityDef newType = this.getEntityDef("0d0e4e3c-4b6b-4f39-9a8e-1b6d6a7ef014", "Recipe", "Referenceable", COLLECTION_ID);

        metadataCollection.addTypeDef(USER_ID, newType);
        knownTypeDefs.put(newType.getName(), newType);

        EntityDetail entityDetail = new EntityDetail();

        entityDetail.setGUID("5e3f9d1c-8b7a-4c6d-9e0f-1a2b3c4d5e6f");
        entityDetail.setType(this.getInstanceType(newType));
        entityDetail.setStatus(InstanceStatus.DELETED);
        entityDetail.setVersion(2);
        entityDetail.setMetadataCollectionId("another-metadata-collection");
        entityDetail.setInstanceProvenanceType(InstanceProvenanceType.LOCAL_COHORT);

        metadataCollection.saveEntityReferenceCopy(USER_ID, entityDetail);

        assertThrows(TypeDefInUseException.class,
                     () -> metadataCollection.deleteTypeDef(USER_ID, newType.getGUID(), newType.getName()));
    }


    /**
     * A type that is the supertype of another cannot be deleted.
     *
     * @throws Exception unexpected
     */
    @Test
    public void testSupertypeIsInUse() throws Exception
    {
        EntityDef newType = this.getEntityDef("0d0e4e3c-4b6b-4f39-9a8e-1b6d6a7ef015", "Recipe", "Referenceable", COLLECTION_ID);
        EntityDef subType = this.getEntityDef("0d0e4e3c-4b6b-4f39-9a8e-1b6d6a7ef016", "Dessert", "Recipe", COLLECTION_ID);

        metadataCollection.addTypeDef(USER_ID, newType);
        knownTypeDefs.put(newType.getName(), newType);
        metadataCollection.addTypeDef(USER_ID, subType);
        knownTypeDefs.put(subType.getName(), subType);

        assertThrows(TypeDefInUseException.class,
                     () -> metadataCollection.deleteTypeDef(USER_ID, newType.getGUID(), newType.getName()));
    }


    /**
     * An enum can only be deleted once no type has an attribute of that type.
     *
     * @throws Exception unexpected
     */
    @Test
    public void testEnumDefInUse() throws Exception
    {
        EnumDef enumDef = this.getEnumDef("0d0e4e3c-4b6b-4f39-9a8e-1b6d6a7ef017", "CuisineType", COLLECTION_ID);

        metadataCollection.addAttributeTypeDef(USER_ID, enumDef);
        knownAttributeTypeDefs.put(enumDef.getName(), enumDef);

        EntityDef newType = this.getEntityDef("0d0e4e3c-4b6b-4f39-9a8e-1b6d6a7ef018", "Recipe", "Referenceable", COLLECTION_ID);

        newType.setPropertiesDefinition(List.of(this.getAttribute("cuisine", enumDef)));
        metadataCollection.addTypeDef(USER_ID, newType);
        knownTypeDefs.put(newType.getName(), newType);

        assertThrows(TypeDefInUseException.class,
                     () -> metadataCollection.deleteAttributeTypeDef(USER_ID, enumDef.getGUID(), enumDef.getName()));

        metadataCollection.deleteTypeDef(USER_ID, newType.getGUID(), newType.getName());
        knownTypeDefs.remove(newType.getName());

        metadataCollection.deleteAttributeTypeDef(USER_ID, enumDef.getGUID(), enumDef.getName());

        assertNull(metadataCollection.getStoredTypes(USER_ID));
    }


    /**
     * The store looks through every version of every instance, and at classifications as well as entities.
     *
     * @throws Exception unexpected
     */
    @Test
    public void testStoreFindsClassificationsAndHistory() throws Exception
    {
        InMemoryOMRSMetadataStore store = new InMemoryOMRSMetadataStore(REPOSITORY_NAME, repositoryHelper, COLLECTION_ID);

        TypeDef referenceable = knownTypeDefs.get("Referenceable");

        Classification classification = new Classification();

        classification.setName("Confidential");
        classification.setType(this.getInstanceTypeFromIds("0d0e4e3c-4b6b-4f39-9a8e-1b6d6a7ef019", "Confidential"));
        classification.setMetadataCollectionId("another-metadata-collection");

        EntityDetail firstVersion = new EntityDetail();

        firstVersion.setGUID("5e3f9d1c-8b7a-4c6d-9e0f-1a2b3c4d5e70");
        firstVersion.setType(this.getInstanceType(referenceable));
        firstVersion.setStatus(InstanceStatus.ACTIVE);
        firstVersion.setVersion(1);
        firstVersion.setClassifications(List.of(classification));

        store.addEntityToStore(firstVersion);

        /*
         * The classification is removed in the next version, but the first version is still in the history.
         */
        EntityDetail secondVersion = new EntityDetail(firstVersion);

        secondVersion.setVersion(2);
        secondVersion.setClassifications(null);

        store.addEntityToStore(secondVersion);

        assertTrue(store.isTypeDefInstantiated("0d0e4e3c-4b6b-4f39-9a8e-1b6d6a7ef019", "Confidential"));
        assertTrue(store.isTypeDefInstantiated(referenceable.getGUID(), referenceable.getName()));
        assertFalse(store.isTypeDefInstantiated("0d0e4e3c-4b6b-4f39-9a8e-1b6d6a7ef020", "Unused"));
    }


    /* ===================
     * Test data builders
     */


    /**
     * Return the attributes of a type and its supertypes from the known types.
     *
     * @param typeDef type to start from
     * @return list of attributes
     */
    private List<TypeDefAttribute> getAllProperties(TypeDef typeDef)
    {
        List<TypeDefAttribute> attributes = new ArrayList<>();

        while (typeDef != null)
        {
            if (typeDef.getPropertiesDefinition() != null)
            {
                attributes.addAll(typeDef.getPropertiesDefinition());
            }

            typeDef = (typeDef.getSuperType() == null) ? null : knownTypeDefs.get(typeDef.getSuperType().getName());
        }

        return attributes;
    }


    /**
     * Return a version 1 entity type.
     *
     * @param guid unique identifier
     * @param name type name
     * @param superTypeName name of the supertype, or null
     * @param origin origin of the type
     * @return entity type
     */
    private EntityDef getEntityDef(String guid,
                                   String name,
                                   String superTypeName,
                                   String origin)
    {
        EntityDef entityDef = new EntityDef();

        entityDef.setGUID(guid);
        entityDef.setName(name);
        entityDef.setVersion(1);
        entityDef.setVersionName("1.0");
        entityDef.setOrigin(origin);

        if (superTypeName != null)
        {
            TypeDefLink superType = new TypeDefLink();

            superType.setName(superTypeName);
            entityDef.setSuperType(superType);
        }

        return entityDef;
    }


    /**
     * Return an attribute definition.
     *
     * @param name attribute name
     * @param attributeType attribute type
     * @return attribute definition
     */
    private TypeDefAttribute getAttribute(String           name,
                                          AttributeTypeDef attributeType)
    {
        TypeDefAttribute attribute = new TypeDefAttribute();

        attribute.setAttributeName(name);
        attribute.setAttributeType(attributeType);

        return attribute;
    }


    /**
     * Return a relationship end for an entity type.
     *
     * @param entityTypeName name of the entity type
     * @param attributeName name of the end
     * @return end definition
     */
    private RelationshipEndDef getEndDef(String entityTypeName,
                                         String attributeName)
    {
        TypeDefLink entityType = new TypeDefLink();

        entityType.setName(entityTypeName);

        RelationshipEndDef endDef = new RelationshipEndDef();

        endDef.setEntityType(entityType);
        endDef.setAttributeName(attributeName);

        return endDef;
    }


    /**
     * Return a version 1 enum with three values.
     *
     * @param guid unique identifier
     * @param name enum name
     * @param origin origin of the enum
     * @return enum definition
     */
    private EnumDef getEnumDef(String guid,
                               String name,
                               String origin)
    {
        EnumDef enumDef = new EnumDef();

        enumDef.setGUID(guid);
        enumDef.setName(name);
        enumDef.setVersion(1);
        enumDef.setVersionName("1.0");
        enumDef.setOrigin(origin);

        List<EnumElementDef> elementDefs = new ArrayList<>();

        for (String value : List.of("First", "Second", "Third"))
        {
            EnumElementDef elementDef = new EnumElementDef();

            elementDef.setOrdinal(elementDefs.size());
            elementDef.setValue(value);
            elementDefs.add(elementDef);
        }

        enumDef.setElementDefs(elementDefs);
        enumDef.setDefaultValue(elementDefs.get(0));

        return enumDef;
    }


    /**
     * Return the instance type for a type.
     *
     * @param typeDef type
     * @return instance type
     */
    private InstanceType getInstanceType(TypeDef typeDef)
    {
        return this.getInstanceTypeFromIds(typeDef.getGUID(), typeDef.getName());
    }


    /**
     * Return an instance type.
     *
     * @param typeGUID unique identifier of the type
     * @param typeName name of the type
     * @return instance type
     */
    private InstanceType getInstanceTypeFromIds(String typeGUID,
                                                String typeName)
    {
        InstanceType instanceType = new InstanceType();

        instanceType.setTypeDefGUID(typeGUID);
        instanceType.setTypeDefName(typeName);

        return instanceType;
    }
}
