/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveInstanceStore;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveTypeStore;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ClassificationEntityExtension;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * Carries the version numbers of unchanged elements over from the previous edition of an archive.
 * <br>
 * An archive writer stamps every element with the same new version number (see
 * EgeriaBaseArchiveWriter.getVersionNumber()), which tells a repository that the element is newer than the copy it
 * already holds.  That is right for an element whose content has changed, but stamping the unchanged ones as well
 * meant that every rebuild changed every element: the archive could not be reproduced, a review of a content change
 * was buried in thousands of version changes, and a repository reloading the archive rewrote every element in it.
 * <br>
 * This class compares each element and type in the new archive with the one of the same unique identifier in the
 * previous edition.  If they are identical apart from version numbers - their own and those of the classifications,
 * entity proxies and attribute types they carry - the previous edition's copy is used, so it keeps its previous
 * versions.  New and changed elements keep the new version.  An entity is compared together with its
 * classifications, so it keeps its previous version only if none of them has changed.
 */
public class ArchiveVersionStabilizer
{
    private static final String      VERSION_FIELD       = "version";
    private static final Set<String> PROPERTY_MAP_FIELDS = Set.of("instanceProperties", "mappingProperties", "additionalProperties");

    private final ObjectMapper objectMapper = new ObjectMapper();


    /**
     * Give each element and type of the new archive that is unchanged from the previous edition its previous version
     * numbers.  An unchanged element is replaced by its copy from the previous edition, so its version - and the
     * versions of the classifications and entity proxies it carries - are exactly as they were.
     *
     * @param newArchive archive about to be written - updated in place
     * @param previousArchive previous edition of the archive, or null if there is none
     * @return number of elements and types whose previous version was kept
     */
    public int stabilizeVersions(OpenMetadataArchive newArchive,
                                 OpenMetadataArchive previousArchive)
    {
        if ((newArchive == null) || (previousArchive == null))
        {
            return 0;
        }

        int keptCount = 0;

        /*
         * The getters of the archive and its stores return copies - of the store, of its lists and of the elements
         * in them - so the lists are updated and then set back.
         */
        OpenMetadataArchiveInstanceStore newStore      = newArchive.getArchiveInstanceStore();
        OpenMetadataArchiveInstanceStore previousStore = previousArchive.getArchiveInstanceStore();

        if ((newStore != null) && (previousStore != null))
        {
            List<EntityDetail> entities = newStore.getEntities();
            keptCount += this.keepUnchanged(entities, previousStore.getEntities(), EntityDetail::getGUID);
            newStore.setEntities(entities);

            List<Relationship> relationships = newStore.getRelationships();
            keptCount += this.keepUnchanged(relationships, previousStore.getRelationships(), Relationship::getGUID);
            newStore.setRelationships(relationships);

            List<ClassificationEntityExtension> extensions = newStore.getClassifications();
            keptCount += this.keepUnchanged(extensions, previousStore.getClassifications(), this::getExtensionKey);
            newStore.setClassifications(extensions);

            newArchive.setArchiveInstanceStore(newStore);
        }

        OpenMetadataArchiveTypeStore newTypeStore      = newArchive.getArchiveTypeStore();
        OpenMetadataArchiveTypeStore previousTypeStore = previousArchive.getArchiveTypeStore();

        if ((newTypeStore != null) && (previousTypeStore != null))
        {
            List<AttributeTypeDef> attributeTypeDefs = newTypeStore.getAttributeTypeDefs();
            keptCount += this.keepUnchanged(attributeTypeDefs, previousTypeStore.getAttributeTypeDefs(), AttributeTypeDef::getGUID);
            newTypeStore.setAttributeTypeDefs(attributeTypeDefs);

            List<TypeDef> typeDefs = newTypeStore.getNewTypeDefs();
            keptCount += this.keepUnchanged(typeDefs, previousTypeStore.getNewTypeDefs(), TypeDef::getGUID);
            newTypeStore.setNewTypeDefs(typeDefs);

            newArchive.setArchiveTypeStore(newTypeStore);
        }

        return keptCount;
    }


    /**
     * Replace each item of the new list that is unchanged from the item with the same key in the previous list by
     * that previous item.
     *
     * @param newItems items about to be written - updated in place (may be null)
     * @param previousItems items from the previous edition (may be null)
     * @param keyFunction returns the key that matches an item to its previous edition
     * @param <T> type of item
     * @return number of items replaced
     */
    private <T> int keepUnchanged(List<T>             newItems,
                                  List<T>             previousItems,
                                  Function<T, String> keyFunction)
    {
        if ((newItems == null) || (previousItems == null))
        {
            return 0;
        }

        Map<String, T> previousByKey = new HashMap<>();

        for (T previousItem : previousItems)
        {
            previousByKey.put(keyFunction.apply(previousItem), previousItem);
        }

        int keptCount = 0;

        for (int i = 0; i < newItems.size(); i++)
        {
            T previousItem = previousByKey.get(keyFunction.apply(newItems.get(i)));

            if ((previousItem != null) && (this.isUnchanged(newItems.get(i), previousItem)))
            {
                newItems.set(i, previousItem);
                keptCount++;
            }
        }

        return keptCount;
    }


    /**
     * Return the highest version number of any element in an archive, or zero if it has none.  Once versions are
     * carried over from one edition to the next, the elements of an archive no longer all share one version.
     *
     * @param archive archive to examine
     * @return highest version number, or zero
     */
    public long getHighestVersion(OpenMetadataArchive archive)
    {
        long highestVersion = 0L;

        if ((archive != null) && (archive.getArchiveInstanceStore() != null))
        {
            OpenMetadataArchiveInstanceStore store = archive.getArchiveInstanceStore();

            if (store.getEntities() != null)
            {
                for (EntityDetail entity : store.getEntities())
                {
                    highestVersion = Math.max(highestVersion, entity.getVersion());
                }
            }

            if (store.getRelationships() != null)
            {
                for (Relationship relationship : store.getRelationships())
                {
                    highestVersion = Math.max(highestVersion, relationship.getVersion());
                }
            }

            if (store.getClassifications() != null)
            {
                for (ClassificationEntityExtension extension : store.getClassifications())
                {
                    if (extension.getClassification() != null)
                    {
                        highestVersion = Math.max(highestVersion, extension.getClassification().getVersion());
                    }
                }
            }
        }

        return highestVersion;
    }


    /**
     * Return whether two instances are identical apart from their version numbers.  Both are compared as JSON,
     * written out and read back, so that a value read from the previous archive file and the same value built in
     * memory compare equal even where their Java types differ (an Integer read back for a Long, for example).
     *
     * @param newInstance instance from the new archive
     * @param previousInstance instance from the previous edition
     * @return boolean
     */
    private boolean isUnchanged(Object newInstance,
                                Object previousInstance)
    {
        try
        {
            return this.getComparableTree(newInstance).equals(this.getComparableTree(previousInstance));
        }
        catch (JsonProcessingException error)
        {
            /*
             * An instance that cannot be compared is treated as changed, which is always safe: it is given the new
             * version and so replaces the stored copy.
             */
            return false;
        }
    }


    /**
     * Return the instance as a JSON tree with its version numbers removed - see removeVersions().
     *
     * @param instance instance to convert
     * @return JSON tree
     * @throws JsonProcessingException the instance could not be converted
     */
    private JsonNode getComparableTree(Object instance) throws JsonProcessingException
    {
        JsonNode tree = objectMapper.readTree(objectMapper.writeValueAsString(instance));

        this.removeVersions(tree);

        return tree;
    }


    /**
     * Remove every version field from a tree - the element's own and those of the classifications, entity proxies
     * and attribute types it carries - except within property maps, where "version" may be the name of a property.
     *
     * @param tree tree to update
     */
    private void removeVersions(JsonNode tree)
    {
        if (tree instanceof ObjectNode objectNode)
        {
            objectNode.remove(VERSION_FIELD);

            Iterator<Map.Entry<String, JsonNode>> fields = objectNode.fields();

            while (fields.hasNext())
            {
                Map.Entry<String, JsonNode> field = fields.next();

                if (! PROPERTY_MAP_FIELDS.contains(field.getKey()))
                {
                    this.removeVersions(field.getValue());
                }
            }
        }
        else if (tree instanceof ArrayNode arrayNode)
        {
            for (JsonNode element : arrayNode)
            {
                this.removeVersions(element);
            }
        }
    }


    /**
     * Return the key that identifies a classification extension: the entity it classifies and the classification's
     * name.
     *
     * @param extension classification extension
     * @return key
     */
    private String getExtensionKey(ClassificationEntityExtension extension)
    {
        String entityGUID         = extension.getEntityToClassify() == null ? null : extension.getEntityToClassify().getGUID();
        String classificationName = extension.getClassification() == null ? null : extension.getClassification().getName();

        return entityGUID + ":" + classificationName;
    }
}
