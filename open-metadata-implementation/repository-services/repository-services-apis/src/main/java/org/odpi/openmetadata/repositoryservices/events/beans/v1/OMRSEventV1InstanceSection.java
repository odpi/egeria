/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.events.beans.v1;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceGraph;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;


import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OMRSEventV1InstanceSection describes the properties specific to instance events
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OMRSEventV1InstanceSection extends OMRSEventV1
{

    private OMRSInstanceEventType eventType = null;

    private String         typeDefGUID                      = null;
    private String         typeDefName                      = null;
    private String         instanceGUID                     = null;
    private EntityDetail   originalEntity                   = null;
    private EntityDetail   entity                           = null;
    private Relationship   originalRelationship             = null;
    private Relationship   relationship                     = null;
    private InstanceGraph  instanceBatch                    = null;
    private String         homeMetadataCollectionId         = null;
    private String         originalHomeMetadataCollectionId = null;
    private TypeDefSummary originalTypeDefSummary           = null;
    private String         originalInstanceGUID             = null;

    public OMRSEventV1InstanceSection()
    {
    }

    public OMRSInstanceEventType getEventType()
    {
        return eventType;
    }

    public void setEventType(OMRSInstanceEventType eventType)
    {
        this.eventType = eventType;
    }

    public String getTypeDefGUID()
    {
        return typeDefGUID;
    }

    public void setTypeDefGUID(String typeDefGUID)
    {
        this.typeDefGUID = typeDefGUID;
    }

    public String getTypeDefName()
    {
        return typeDefName;
    }

    public void setTypeDefName(String typeDefName)
    {
        this.typeDefName = typeDefName;
    }

    public String getInstanceGUID()
    {
        return instanceGUID;
    }

    public void setInstanceGUID(String instanceGUID)
    {
        this.instanceGUID = instanceGUID;
    }

    public EntityDetail getOriginalEntity()
    {
        return originalEntity;
    }

    public void setOriginalEntity(EntityDetail originalEntity)
    {
        this.originalEntity = originalEntity;
    }

    public EntityDetail getEntity()
    {
        return entity;
    }

    public void setEntity(EntityDetail entity)
    {
        this.entity = entity;
    }

    public Relationship getOriginalRelationship()
    {
        return originalRelationship;
    }

    public void setOriginalRelationship(Relationship originalRelationship)
    {
        this.originalRelationship = originalRelationship;
    }

    public Relationship getRelationship()
    {
        return relationship;
    }

    public void setRelationship(Relationship relationship)
    {
        this.relationship = relationship;
    }

    public InstanceGraph getInstanceBatch()
    {
        return instanceBatch;
    }

    public void setInstanceBatch(InstanceGraph instanceBatch)
    {
        this.instanceBatch = instanceBatch;
    }

    public String getHomeMetadataCollectionId()
    {
        return homeMetadataCollectionId;
    }

    public void setHomeMetadataCollectionId(String homeMetadataCollectionId)
    {
        this.homeMetadataCollectionId = homeMetadataCollectionId;
    }

    public String getOriginalHomeMetadataCollectionId()
    {
        return originalHomeMetadataCollectionId;
    }

    public void setOriginalHomeMetadataCollectionId(String originalHomeMetadataCollectionId)
    {
        this.originalHomeMetadataCollectionId = originalHomeMetadataCollectionId;
    }

    public TypeDefSummary getOriginalTypeDefSummary()
    {
        return originalTypeDefSummary;
    }

    public void setOriginalTypeDefSummary(TypeDefSummary originalTypeDefSummary)
    {
        this.originalTypeDefSummary = originalTypeDefSummary;
    }

    public String getOriginalInstanceGUID()
    {
        return originalInstanceGUID;
    }

    public void setOriginalInstanceGUID(String originalInstanceGUID)
    {
        this.originalInstanceGUID = originalInstanceGUID;
    }
}
