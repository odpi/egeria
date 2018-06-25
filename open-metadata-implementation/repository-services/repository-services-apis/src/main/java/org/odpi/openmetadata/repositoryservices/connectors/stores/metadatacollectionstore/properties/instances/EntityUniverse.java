/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * EntityUniverse extends EntityDetail to add the relationships that this entity has.  These are available
 * in an iterator to make them easy to process.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class EntityUniverse extends EntityDetail
{
    private List<Relationship>  entityRelationships = null;


    /**
     * Default constructor - initializes entity's universe as empty.
     */
    public EntityUniverse()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param templateElement template to copy.  If null passed, the EntityUniverse is initialized as empty.
     */
    public EntityUniverse(EntityUniverse   templateElement)
    {
        super(templateElement);

        if (templateElement != null)
        {
            this.setEntityRelationships(templateElement.getEntityRelationships());
        }
    }


    /**
     * Copy/clone constructor from an EntityDetail.
     *
     * @param templateElement template to copy.  If null passed, the EntityUniverse is initialized as empty.
     */
    public EntityUniverse(EntityDetail   templateElement)
    {
        super(templateElement);
    }


    /**
     * Return a copy of the relationships for this entity in an iterator.
     *
     * @return Relationships list.
     */
    public List<Relationship> getEntityRelationships()
    {
        if (entityRelationships == null)
        {
            return null;
        }
        else
        {
            return new ArrayList<>(entityRelationships);
        }
    }


    /**
     * Set up the list of relationships for this entity.
     *
     * @param entityRelationships Relationships list
     */
    public void setEntityRelationships(List<Relationship> entityRelationships)
    {
        if (entityRelationships == null)
        {
            this.entityRelationships = null;
        }
        else
        {
            this.entityRelationships = new ArrayList<>(entityRelationships);
        }
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "EntityUniverse{" +
                "entityRelationships=" + entityRelationships +
                ", properties=" + getProperties() +
                ", classifications=" + getClassifications() +
                ", type=" + getType() +
                ", instanceProvenanceType=" + getInstanceProvenanceType() +
                ", metadataCollectionId='" + getMetadataCollectionId() + '\'' +
                ", instanceURL='" + getInstanceURL() + '\'' +
                ", GUID='" + getGUID() + '\'' +
                ", status=" + getStatus() +
                ", createdBy='" + getCreatedBy() + '\'' +
                ", updatedBy='" + getUpdatedBy() + '\'' +
                ", createTime=" + getCreateTime() +
                ", updateTime=" + getUpdateTime() +
                ", version=" + getVersion() +
                ", statusOnDelete=" + getStatusOnDelete() +
                '}';
    }
}
