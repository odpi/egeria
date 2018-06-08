/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataArchiveInstanceStore defines the contents of the InstanceStore in an open metadata archive.  It
 * consists of a list of entities and a list of relationships.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataArchiveInstanceStore
{
    private List<EntityDetail> entities      = null;
    private List<Relationship> relationships = null;


    /**
     * Default constructor relying on the initialization of variables in their declaration.
     */
    public OpenMetadataArchiveInstanceStore()
    {
    }


    /**
     * Return the list of entities defined in the open metadata archive.
     *
     * @return list of entities
     */
    public List<EntityDetail> getEntities()
    {
        if (entities == null)
        {
            return null;
        }
        else
        {
            return new ArrayList<>(entities);
        }
    }


    /**
     * Set up the list of entities defined in the open metadata archive.
     *
     * @param entities  list of entities
     */
    public void setEntities(List<EntityDetail> entities)
    {
        if (entities == null)
        {
            this.entities = null;
        }
        else
        {
            this.entities = new ArrayList<>(entities);
        }
    }


    /**
     * Return the list of relationships defined in this open metadata archive.
     *
     * @return list of relationships
     */
    public List<Relationship> getRelationships()
    {
        if (relationships == null)
        {
            return null;
        }
        else
        {
            return new ArrayList<>(relationships);
        }
    }


    /**
     * Set up the list of relationships defined in this open metadata archive.
     *
     * @param relationships  list of relationship objects
     */
    public void setRelationships(List<Relationship> relationships)
    {
        if (relationships == null)
        {
            this.relationships = null;
        }
        else
        {
            this.relationships = new ArrayList<>(relationships);
        }
    }
}
