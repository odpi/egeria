/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveInstanceStore;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveTypeStore;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * Elements that are unchanged from the previous edition of an archive keep their previous version number, so that
 * rebuilding an archive without changing it reproduces it, and a repository reloading it only replaces what changed.
 */
public class ArchiveVersionStabilizerTest
{
    private static final long PREVIOUS_VERSION = 1000L;
    private static final long NEW_VERSION      = 2000L;
    private static final Date CREATION_DATE    = new Date(1639984840038L);

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    public void testUnchangedElementsKeepTheirPreviousVersion() throws Exception
    {
        OpenMetadataArchive previous = this.getArchive(PREVIOUS_VERSION, "Unchanged description", 5L);
        OpenMetadataArchive rebuilt  = this.getArchive(NEW_VERSION, "Unchanged description", 5L);

        int kept = new ArchiveVersionStabilizer().stabilizeVersions(rebuilt, this.readBack(previous));

        assertEquals(kept, 3);
        assertEquals(rebuilt.getArchiveInstanceStore().getEntities().get(0).getVersion(), PREVIOUS_VERSION);
        assertEquals(rebuilt.getArchiveInstanceStore().getEntities().get(0).getClassifications().get(0).getVersion(), PREVIOUS_VERSION);
        assertEquals(rebuilt.getArchiveInstanceStore().getEntities().get(1).getVersion(), PREVIOUS_VERSION);
        assertEquals(rebuilt.getArchiveInstanceStore().getRelationships().get(0).getVersion(), PREVIOUS_VERSION);

        /*
         * Rebuilding again reproduces the archive exactly.
         */
        assertEquals(objectMapper.writeValueAsString(rebuilt), objectMapper.writeValueAsString(previous));
    }


    @Test
    public void testChangedElementsGetTheNewVersion() throws Exception
    {
        OpenMetadataArchive previous = this.getArchive(PREVIOUS_VERSION, "Old description", 5L);
        OpenMetadataArchive rebuilt  = this.getArchive(NEW_VERSION, "New description", 5L);

        new ArchiveVersionStabilizer().stabilizeVersions(rebuilt, this.readBack(previous));

        assertEquals(rebuilt.getArchiveInstanceStore().getEntities().get(0).getVersion(), NEW_VERSION,
                     "The entity whose description changed must get the new version");
        assertEquals(rebuilt.getArchiveInstanceStore().getEntities().get(1).getVersion(), PREVIOUS_VERSION,
                     "The other entity has not changed");
        assertEquals(rebuilt.getArchiveInstanceStore().getRelationships().get(0).getVersion(), PREVIOUS_VERSION,
                     "The relationship has not changed");
    }


    @Test
    public void testChangedClassificationGivesTheEntityTheNewVersion() throws Exception
    {
        OpenMetadataArchive previous = this.getArchive(PREVIOUS_VERSION, "Unchanged description", 5L);
        OpenMetadataArchive rebuilt  = this.getArchive(NEW_VERSION, "Unchanged description", 6L);

        new ArchiveVersionStabilizer().stabilizeVersions(rebuilt, this.readBack(previous));

        EntityDetail entity = rebuilt.getArchiveInstanceStore().getEntities().get(0);

        assertEquals(entity.getVersion(), NEW_VERSION);
        assertEquals(entity.getClassifications().get(0).getVersion(), NEW_VERSION);
    }


    @Test
    public void testNewElementsKeepTheNewVersion()
    {
        OpenMetadataArchive rebuilt = this.getArchive(NEW_VERSION, "Description", 5L);

        int kept = new ArchiveVersionStabilizer().stabilizeVersions(rebuilt, new OpenMetadataArchive());

        assertEquals(kept, 0);
        assertEquals(rebuilt.getArchiveInstanceStore().getEntities().get(0).getVersion(), NEW_VERSION);
    }


    @Test
    public void testHighestVersionIsFoundAcrossMixedVersions() throws Exception
    {
        OpenMetadataArchive              archive = this.getArchive(PREVIOUS_VERSION, "Description", 5L);
        OpenMetadataArchiveInstanceStore store   = archive.getArchiveInstanceStore();
        List<Relationship>               relationships = store.getRelationships();

        /*
         * The getters return copies, so the change is set back.
         */
        relationships.get(0).setVersion(PREVIOUS_VERSION + 7);
        store.setRelationships(relationships);
        archive.setArchiveInstanceStore(store);

        assertEquals(new ArchiveVersionStabilizer().getHighestVersion(archive), PREVIOUS_VERSION + 7);
    }


    @Test
    public void testVersionsInEntityProxiesAreIgnored() throws Exception
    {
        OpenMetadataArchive previous = this.getArchive(PREVIOUS_VERSION, "Unchanged description", 5L);
        OpenMetadataArchive rebuilt  = this.getArchive(NEW_VERSION, "Unchanged description", 5L);

        this.setRelationshipEnds(previous, PREVIOUS_VERSION);
        this.setRelationshipEnds(rebuilt, NEW_VERSION);

        new ArchiveVersionStabilizer().stabilizeVersions(rebuilt, this.readBack(previous));

        Relationship relationship = rebuilt.getArchiveInstanceStore().getRelationships().get(0);

        assertEquals(relationship.getVersion(), PREVIOUS_VERSION);
        assertEquals(relationship.getEntityOneProxy().getVersion(), PREVIOUS_VERSION);
        assertEquals(objectMapper.writeValueAsString(rebuilt), objectMapper.writeValueAsString(previous));
    }


    @Test
    public void testUnchangedTypeDefsKeepTheirPreviousVersion() throws Exception
    {
        OpenMetadataArchive previous = this.getTypesArchive(PREVIOUS_VERSION, "A recipe");
        OpenMetadataArchive rebuilt  = this.getTypesArchive(NEW_VERSION, "A recipe");
        OpenMetadataArchive changed  = this.getTypesArchive(NEW_VERSION, "A changed recipe");

        new ArchiveVersionStabilizer().stabilizeVersions(rebuilt, this.readBack(previous));
        new ArchiveVersionStabilizer().stabilizeVersions(changed, this.readBack(previous));

        assertEquals(rebuilt.getArchiveTypeStore().getNewTypeDefs().get(0).getVersion(), PREVIOUS_VERSION);
        assertEquals(changed.getArchiveTypeStore().getNewTypeDefs().get(0).getVersion(), NEW_VERSION);
    }


    private void setRelationshipEnds(OpenMetadataArchive archive, long version)
    {
        OpenMetadataArchiveInstanceStore store         = archive.getArchiveInstanceStore();
        List<Relationship>               relationships = store.getRelationships();
        Relationship                     relationship  = relationships.get(0);

        EntityProxy endOne = new EntityProxy();
        endOne.setGUID("first-guid");
        endOne.setVersion(version);
        endOne.setType(this.getType("Referenceable"));

        EntityProxy endTwo = new EntityProxy();
        endTwo.setGUID("second-guid");
        endTwo.setVersion(version);
        endTwo.setType(this.getType("Referenceable"));

        relationship.setEntityOneProxy(endOne);
        relationship.setEntityTwoProxy(endTwo);
        store.setRelationships(relationships);
        archive.setArchiveInstanceStore(store);
    }


    private OpenMetadataArchive getTypesArchive(long version, String description)
    {
        EntityDef entityDef = new EntityDef();

        entityDef.setGUID("recipe-type-guid");
        entityDef.setName("Recipe");
        entityDef.setVersion(version);
        entityDef.setVersionName("1.0");
        entityDef.setDescription(description);

        OpenMetadataArchiveTypeStore typeStore = new OpenMetadataArchiveTypeStore();

        typeStore.setNewTypeDefs(new ArrayList<>(List.of(entityDef)));

        OpenMetadataArchive archive = new OpenMetadataArchive();

        archive.setArchiveTypeStore(typeStore);

        return archive;
    }


    /**
     * Write an archive out and read it back, as the archive writer reads the previous edition from its file.  This is
     * what makes the comparison meet the type differences of values read from JSON.
     */
    private OpenMetadataArchive readBack(OpenMetadataArchive archive) throws Exception
    {
        return objectMapper.readValue(objectMapper.writeValueAsString(archive), OpenMetadataArchive.class);
    }


    /**
     * Build an archive of two entities - the first with a classification - and a relationship between them.  The
     * long property is what changes type when read back from JSON.
     */
    private OpenMetadataArchive getArchive(long version, String description, long classificationValue)
    {
        EntityDetail first  = this.getEntity("first-guid", version, description);
        EntityDetail second = this.getEntity("second-guid", version, "Second entity");

        Classification classification = new Classification();

        classification.setName("Confidentiality");
        classification.setVersion(version);
        classification.setCreateTime(CREATION_DATE);
        classification.setProperties(this.getLongProperty("level", classificationValue));

        List<Classification> classifications = new ArrayList<>();
        classifications.add(classification);
        first.setClassifications(classifications);

        Relationship relationship = new Relationship();

        relationship.setGUID("relationship-guid");
        relationship.setVersion(version);
        relationship.setCreateTime(CREATION_DATE);
        relationship.setType(this.getType("Linked"));

        OpenMetadataArchiveInstanceStore store = new OpenMetadataArchiveInstanceStore();

        store.setEntities(new ArrayList<>(List.of(first, second)));
        store.setRelationships(new ArrayList<>(List.of(relationship)));

        OpenMetadataArchive archive = new OpenMetadataArchive();

        archive.setArchiveInstanceStore(store);

        return archive;
    }


    private EntityDetail getEntity(String guid, long version, String description)
    {
        EntityDetail entity = new EntityDetail();

        entity.setGUID(guid);
        entity.setVersion(version);
        entity.setCreateTime(CREATION_DATE);
        entity.setType(this.getType("Referenceable"));

        InstanceProperties properties = this.getLongProperty("count", 42L);

        PrimitivePropertyValue descriptionValue = new PrimitivePropertyValue();

        descriptionValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
        descriptionValue.setPrimitiveValue(description);
        properties.setProperty("description", descriptionValue);

        entity.setProperties(properties);

        return entity;
    }


    private InstanceProperties getLongProperty(String name, long value)
    {
        PrimitivePropertyValue longValue = new PrimitivePropertyValue();

        longValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_LONG);
        longValue.setPrimitiveValue(value);

        InstanceProperties properties = new InstanceProperties();

        properties.setProperty(name, longValue);

        return properties;
    }


    private InstanceType getType(String typeName)
    {
        InstanceType type = new InstanceType();

        type.setTypeDefName(typeName);

        return type;
    }
}
