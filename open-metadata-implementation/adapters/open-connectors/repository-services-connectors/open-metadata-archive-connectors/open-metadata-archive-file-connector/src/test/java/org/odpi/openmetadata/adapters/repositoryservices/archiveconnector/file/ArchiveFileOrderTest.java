/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.file;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveTypeStore;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeCardinality;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefAttribute;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * The archive file is written with its properties in alphabetical order, so the same contents always produce the
 * same file.  TypeDefAttribute's boolean properties are found through its isIndexable()/isUnique() getters, and the
 * order Jackson wrote them in used to depend on Java reflection, which can change from one JVM run to the next.
 */
public class ArchiveFileOrderTest
{
    @Test
    public void testPropertiesAreWrittenInAlphabeticalOrder() throws Exception
    {
        TypeDefAttribute attribute = new TypeDefAttribute();

        attribute.setAttributeName("cuisine");
        attribute.setAttributeCardinality(AttributeCardinality.AT_MOST_ONE);
        attribute.setIndexable(true);
        attribute.setUnique(false);

        EntityDef entityDef = new EntityDef();

        entityDef.setGUID("recipe-type-guid");
        entityDef.setName("Recipe");
        entityDef.setPropertiesDefinition(new ArrayList<>(List.of(attribute)));

        OpenMetadataArchiveTypeStore typeStore = new OpenMetadataArchiveTypeStore();

        typeStore.setNewTypeDefs(new ArrayList<>(List.of(entityDef)));

        OpenMetadataArchive archive = new OpenMetadataArchive();

        archive.setArchiveTypeStore(typeStore);

        String contents = FileBasedOpenMetadataArchiveStoreConnector.getArchiveFileContents(archive);

        assertTrue(contents.indexOf("\"attributeCardinality\"") < contents.indexOf("\"indexable\""), contents);
        assertTrue(contents.indexOf("\"indexable\"") < contents.indexOf("\"unique\""), contents);

        this.assertSorted(new ObjectMapper().readTree(contents), "");
    }


    /**
     * Check that every object in the tree has its fields in alphabetical order, apart from the "class" type marker,
     * which Jackson always writes first.
     *
     * @param node tree to check
     * @param path where the node is, for the failure message
     */
    private void assertSorted(JsonNode node, String path)
    {
        if (node.isObject())
        {
            List<String> fieldNames = new ArrayList<>();

            node.fieldNames().forEachRemaining(fieldNames::add);
            fieldNames.remove("class");

            List<String> sortedFieldNames = new ArrayList<>(fieldNames);

            Collections.sort(sortedFieldNames);

            assertEquals(fieldNames, sortedFieldNames, "Fields out of order at " + path);

            node.fields().forEachRemaining(field -> this.assertSorted(field.getValue(), path + "/" + field.getKey()));
        }
        else if (node.isArray())
        {
            for (int i = 0; i < node.size(); i++)
            {
                this.assertSorted(node.get(i), path + "[" + i + "]");
            }
        }
    }
}
