/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.fvt.opentypes.common;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

import java.io.Serializable;


/**
 * A relationship between 2 subject area OMRS entities. It is called types as it has named fields for the attributes and references.
 */


public class OMRSLine extends Line implements Serializable {
    protected static final long serialVersionUID = 1L;
    protected String entity1Guid;
    protected String entity1PropertyName;
    protected String entity1Label;
    protected String entity2Guid;
    protected String entity2PropertyName;
    protected String entity2Label;

    /**
     * Default constructor
     */
    public OMRSLine() {}
    public OMRSLine(Line template) {
        super();
    }
    public OMRSLine(OMRSLine template) {
        super();
        this.setEntity1Guid(template.getEntity1Guid());
        this.setEntity1Label(template.getEntity1Label());
        this.setEntity1Name(template.getEntity1Name());
        this.setEntity1PropertyName(template.getEntity1PropertyName());
        this.setEntity2Guid(template.getEntity2Guid());
        this.setEntity2Label(template.getEntity2Label());
        this.setEntity2Name(template.getEntity2Name());
        this.setEntity2PropertyName(template.getEntity2PropertyName());
    }

    /**
     * Create a typedRelationship from an omrs relationship
     * @param omrsRelationship omrs relationship
     */
    public OMRSLine(Relationship omrsRelationship) {
        super(omrsRelationship);
        if (omrsRelationship.getEntityOneProxy() != null)
        {
            if (omrsRelationship.getEntityOneProxy().getType() != null)
            {
                this.entity1Type = omrsRelationship.getEntityOneProxy().getType().getTypeDefName();
            }

            this.entity1Guid = omrsRelationship.getEntityOneProxy().getGUID();
        }
        if (omrsRelationship.getEntityTwoProxy() != null)
        {
            if (omrsRelationship.getEntityTwoProxy().getType() != null)
            {
                this.entity2Type = omrsRelationship.getEntityTwoProxy().getType().getTypeDefName();
            }
            this.entity2Guid = omrsRelationship.getEntityTwoProxy().getGUID();
        }
    }

    public String getTypeDefGuid() {
        return typeDefGuid;
    }

    public void setTypeDefGuid(String typeDefGuid) {
        this.typeDefGuid = typeDefGuid;
    }

    public void setEntity1Name(String entity1Name) {
        this.entity1Name = entity1Name;
    }
    public void setEntity1Type(String entity1Type) {
        this.entity1Type = entity1Type;
    }

    public String getEntity1PropertyName() {
        return entity1PropertyName;
    }

    public void setEntity1PropertyName(String entity1PropertyName) {
        this.entity1PropertyName = entity1PropertyName;
    }

    public String getEntity1Label() {
        return entity1Label;
    }

    public void setEntity1Label(String entity1Label) {
        this.entity1Label = entity1Label;
    }

    public void setEntity2Name(String entity2Name) {
        this.entity2Name = entity2Name;
    }

    public void setEntity2Type(String entity2Type) {
        this.entity2Type = entity2Type;
    }

    public String getEntity2PropertyName() {
        return entity2PropertyName;
    }

    public void setEntity2PropertyName(String entity2PropertyName) {
        this.entity2PropertyName = entity2PropertyName;
    }

    public String getEntity2Label() {
        return entity2Label;
    }

    public void setEntity2Label(String entity2Label) {
        this.entity2Label = entity2Label;
    }

    public void setEntity1Guid(String entity1Guid) {
        this.entity1Guid = entity1Guid;
    }

    public String getEntity2Guid() {
        return entity2Guid;
    }

    public void setEntity2Guid(String entity2Guid) {
        this.entity2Guid = entity2Guid;
    }

    public String getName() {
        return name;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
    public String getEntity1Name() {
        return entity1Name;
    }
    public String getEntity2Name() {
        return entity2Name;
    }
    public String getEntity1Type() { return entity1Type; }
    public String getEntity2Type() { return entity2Type; }

    public String getEntity1Guid()
    {
        return entity1Guid;
    }

    /**
     * Create an omrs relationship with the basic fields filled in.
     * @param line this is a line to create the relationship from
     * @return Relationship the created omrs relationship
     */
    public static Relationship createOmrsRelationship(OMRSLine line) {
        Relationship omrsRelationship = new Relationship();
        InstanceType typeOfRelationship = new InstanceType();
        typeOfRelationship.setTypeDefName(line.getName());
        typeOfRelationship.setTypeDefGUID(line.getTypeDefGuid());
        omrsRelationship.setType(typeOfRelationship);
        SystemAttributes systemAttributes = line.getSystemAttributes();
        if (systemAttributes ==null) {
            systemAttributes = new SystemAttributes();
        }
        omrsRelationship.setCreatedBy(systemAttributes.getCreatedBy());
        omrsRelationship.setUpdatedBy(systemAttributes.getUpdatedBy());
        omrsRelationship.setCreateTime(systemAttributes.getCreateTime());
        omrsRelationship.setUpdateTime(systemAttributes.getUpdateTime());
        if (systemAttributes.getVersion()==null) {
            omrsRelationship.setVersion(0L);
        } else {
            omrsRelationship.setVersion(systemAttributes.getVersion());
        }
        line.setSystemAttributes(systemAttributes);
        //set proxy 1
        EntityProxy entityOne = new EntityProxy();
        entityOne.setGUID(line.getEntity1Guid());
        String type1 = line.getEntity1Type();
        InstanceType instancetype1 = new InstanceType();
        instancetype1.setTypeDefName(type1);
        entityOne.setType(instancetype1);
        //set proxy 2
        EntityProxy entityTwo = new EntityProxy();
        entityTwo.setGUID(line.getEntity2Guid());
        String type2 = line.getEntity2Type();
        InstanceType instancetype2 = new InstanceType();
        instancetype2.setTypeDefName(type2);
        entityTwo.setType(instancetype2);

        omrsRelationship.setEntityOneProxy(entityOne);
        omrsRelationship.setEntityTwoProxy(entityTwo);
        return omrsRelationship;
    }

    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }

        sb.append("OMRSLine{");
        sb.append("line="+"super.toString(sb),");
        sb.append("entity1Name="+entity1Name+",");
        sb.append("entity1Type="+entity1Type+",");
        sb.append("relatedTerm1Guid="+entity1Guid+",");
        sb.append("entity1PropertyName="+ entity1PropertyName+",");
        sb.append("entity1Label="+ entity1Label+",");

        sb.append("entity2Name="+entity2Name+",");
        sb.append("entity2Type="+entity2Type+",");
        sb.append("relatedTerm2Guid="+entity2Guid+",");
        sb.append("entity2PropertyName="+ entity2PropertyName+",");
        sb.append("entity2Label="+ entity2Label+",");
        sb.append("name=" + name);
        sb.append('}');
        return sb;
    }
}
