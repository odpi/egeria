/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.events.beans.v1;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceGraph;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventType;

import java.io.Serial;
import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OMRSEventV1InstanceSection describes the properties specific to instance events
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OMRSEventV1InstanceSection implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;

    private OMRSInstanceEventType eventType = null;

    private String         typeDefGUID                      = null;
    private String         typeDefName                      = null;
    private String         instanceGUID                     = null;
    private EntityDetail   originalEntity                   = null;
    private EntityDetail   entity                           = null;
    private EntityProxy    entityProxy                      = null;
    private Relationship   originalRelationship             = null;
    private Relationship   relationship                     = null;
    private Classification originalClassification           = null;
    private Classification classification                   = null;
    private InstanceGraph  instanceBatch                    = null;
    private String         homeMetadataCollectionId         = null;
    private String         originalHomeMetadataCollectionId = null;
    private TypeDefSummary originalTypeDefSummary           = null;
    private String         originalInstanceGUID             = null;


    /**
     * Default constructor
     */
    public OMRSEventV1InstanceSection()
    {
    }


    /**
     * Return the reason for the event.
     *
     * @return enum
     */
    public OMRSInstanceEventType getEventType()
    {
        return eventType;
    }


    /**
     * Set up the reason for the event.
     *
     * @param eventType enum
     */
    public void setEventType(OMRSInstanceEventType eventType)
    {
        this.eventType = eventType;
    }


    /**
     * Return the unique identifier of the instance type.
     *
     * @return guid
     */
    public String getTypeDefGUID()
    {
        return typeDefGUID;
    }


    /**
     * Set up the unique identifier of the instance type.
     *
     * @param typeDefGUID guid
     */
    public void setTypeDefGUID(String typeDefGUID)
    {
        this.typeDefGUID = typeDefGUID;
    }


    /**
     * Return the unique name of the instance type.
     *
     * @return name
     */
    public String getTypeDefName()
    {
        return typeDefName;
    }


    /**
     * Set up the unique name of the instance type.
     *
     * @param typeDefName guid
     */
    public void setTypeDefName(String typeDefName)
    {
        this.typeDefName = typeDefName;
    }


    /**
     * Return the unique identifier of the instance.
     *
     * @return guid
     */
    public String getInstanceGUID()
    {
        return instanceGUID;
    }


    /**
     * Set up the unique identifier of the instance.
     *
     * @param instanceGUID guid
     */
    public void setInstanceGUID(String instanceGUID)
    {
        this.instanceGUID = instanceGUID;
    }


    /**
     * Return the entity values before it was updated.
     *
     * @return entity
     */
    public EntityDetail getOriginalEntity()
    {
        return originalEntity;
    }


    /**
     * Set up the entity values before it was updated.
     *
     * @param originalEntity entity
     */
    public void setOriginalEntity(EntityDetail originalEntity)
    {
        this.originalEntity = originalEntity;
    }


    /**
     * Return the values for the entity at the end of the operation.
     *
     * @return entity
     */
    public EntityDetail getEntity()
    {
        return entity;
    }


    /**
     * Set up the values for the entity at the end of the operation.
     *
     * @param entity entity
     */
    public void setEntity(EntityDetail entity)
    {
        this.entity = entity;
    }


    /**
     * Return associated entity proxy.
     *
     * @return proxy
     */
    public EntityProxy getEntityProxy() { return entityProxy; }


    /**
     * Set up associated entity proxy.
     *
     * @param entityProxy proxy
     */
    public void setEntityProxy(EntityProxy entityProxy) { this.entityProxy = entityProxy; }


    /**
     * Return values for the relationship before it was changed.
     *
     * @return relationship
     */
    public Relationship getOriginalRelationship() { return originalRelationship; }


    /**
     * Set up the values for the relationship before it was changed.
     *
     * @param originalRelationship relationship
     */
    public void setOriginalRelationship(Relationship originalRelationship)
    {
        this.originalRelationship = originalRelationship;
    }


    /**
     * Return new relationship values.
     *
     * @return relationship
     */
    public Relationship getRelationship()
    {
        return relationship;
    }


    /**
     * Set up new relationship values.
     *
     * @param relationship relationship
     */
    public void setRelationship(Relationship relationship)
    {
        this.relationship = relationship;
    }


    /**
     * Return the classification before it was changed.
     *
     * @return classification
     */
    public Classification getOriginalClassification()
    {
        return originalClassification;
    }


    /**
     * Set up the classification before it was changed.
     *
     * @param originalClassification classification
     */
    public void setOriginalClassification(Classification originalClassification)
    {
        this.originalClassification = originalClassification;
    }


    /**
     * Return the new classification.
     *
     * @return classification
     */
    public Classification getClassification()
    {
        return classification;
    }


    /**
     * Set up new classification.
     *
     * @param classification classification
     */
    public void setClassification(Classification classification)
    {
        this.classification = classification;
    }


    /**
     * Return a batch of instances.
     *
     * @return set of instances
     */
    public InstanceGraph getInstanceBatch()
    {
        return instanceBatch;
    }


    /**
     * Set up a batch of instances.
     *
     * @param instanceBatch set of instances
     */
    public void setInstanceBatch(InstanceGraph instanceBatch)
    {
        this.instanceBatch = instanceBatch;
    }


    /**
     * Return the new metadata collection unique identifier.
     *
     * @return guid
     */
    public String getHomeMetadataCollectionId()
    {
        return homeMetadataCollectionId;
    }


    /**
     * Set up the new metadata collection unique identifier.
     *
     * @param homeMetadataCollectionId guid
     */
    public void setHomeMetadataCollectionId(String homeMetadataCollectionId)
    {
        this.homeMetadataCollectionId = homeMetadataCollectionId;
    }


    /**
     * Return the unique identifier of the metadata collection before it was changed.
     *
     * @return guid
     */
    public String getOriginalHomeMetadataCollectionId()
    {
        return originalHomeMetadataCollectionId;
    }


    /**
     * Set up the unique identifier of the metadata collection before it was changed.
     *
     * @param originalHomeMetadataCollectionId guid
     */
    public void setOriginalHomeMetadataCollectionId(String originalHomeMetadataCollectionId)
    {
        this.originalHomeMetadataCollectionId = originalHomeMetadataCollectionId;
    }


    /**
     * Return the type before it was changed.
     *
     * @return type info
     */
    public TypeDefSummary getOriginalTypeDefSummary()
    {
        return originalTypeDefSummary;
    }


    /**
     * Set up the type before it was changed.
     *
     * @param originalTypeDefSummary type info
     */
    public void setOriginalTypeDefSummary(TypeDefSummary originalTypeDefSummary)
    {
        this.originalTypeDefSummary = originalTypeDefSummary;
    }


    /**
     * Return the unique identifier of the instance before it was changed.
     *
     * @return guid
     */
    public String getOriginalInstanceGUID()
    {
        return originalInstanceGUID;
    }


    /**
     * Set up the unique identifier of the instance before it was changed.
     *
     * @param originalInstanceGUID guid
     */
    public void setOriginalInstanceGUID(String originalInstanceGUID)
    {
        this.originalInstanceGUID = originalInstanceGUID;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "OMRSEventV1InstanceSection{" +
                       "eventType=" + eventType +
                       ", typeDefGUID='" + typeDefGUID + '\'' +
                       ", typeDefName='" + typeDefName + '\'' +
                       ", instanceGUID='" + instanceGUID + '\'' +
                       ", originalEntity=" + originalEntity +
                       ", entity=" + entity +
                       ", originalRelationship=" + originalRelationship +
                       ", relationship=" + relationship +
                       ", originalClassification=" + originalClassification +
                       ", classification=" + classification +
                       ", instanceBatch=" + instanceBatch +
                       ", homeMetadataCollectionId='" + homeMetadataCollectionId + '\'' +
                       ", originalHomeMetadataCollectionId='" + originalHomeMetadataCollectionId + '\'' +
                       ", originalTypeDefSummary=" + originalTypeDefSummary +
                       ", originalInstanceGUID='" + originalInstanceGUID + '\'' +
                       '}';
    }
}
