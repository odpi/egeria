/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.line;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SystemAttributes;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

import java.io.Serializable;
import java.util.Map;


/**
 * A relationship between 2 subject area OMAS entities. It is called types as it has named fields for the attributes and references.
 */
@JsonAutoDetect(getterVisibility= JsonAutoDetect.Visibility.PUBLIC_ONLY, setterVisibility= JsonAutoDetect.Visibility.PUBLIC_ONLY, fieldVisibility= JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)

public class Line implements Serializable {
    protected static final long serialVersionUID = 1L;
    private SystemAttributes systemAttributes = null;
    private Map<String, Object> extraAttributes;
    protected String typeDefGuid;
    protected LineType lineType;
    protected String entity1Name;
    protected String entity1Type;
    protected String entity1Guid;
    protected String entity1PropertyName;
    protected String entity1Label;

    protected String entity2Name;
    protected String entity2Type;
    protected String entity2Guid;
    protected String entity2PropertyName;
    protected String entity2Label;

    protected String guid;
    protected String name;

    /**
     * Default constructor
     */
    public Line() {}
    public Line(String name) {
        this.name=name;
        this.lineType=LineType.Other;
    }

    public Line(String name,LineType lineType) {
        this.name=name;
        this.lineType=lineType;
    }
    /**
     * Create a typedRelationship from an omrs relationship
     * @param omrsRelationship omrs relationship
     */
    public Line(Relationship omrsRelationship) {
        if (this.systemAttributes ==null) {
            this.systemAttributes = new SystemAttributes();
        }
        this.systemAttributes.setUpdateTime(omrsRelationship.getUpdateTime());
        this.systemAttributes.setCreateTime(omrsRelationship.getCreateTime());
        this.systemAttributes.setCreatedBy(omrsRelationship.getCreatedBy());
        this.systemAttributes.setUpdatedBy(omrsRelationship.getUpdatedBy());
        this.systemAttributes.setVersion(omrsRelationship.getVersion());

        this.entity1Name = omrsRelationship.getEntityOnePropertyName();
        this.entity1Type = omrsRelationship.getEntityOneProxy().getType().getTypeDefName();
        this.entity1Guid = omrsRelationship.getEntityOneProxy().getGUID();

        this.entity2Name = omrsRelationship.getEntityTwoPropertyName();
        this.entity2Type = omrsRelationship.getEntityTwoProxy().getType().getTypeDefName();
        this.entity2Guid = omrsRelationship.getEntityTwoProxy().getGUID();
    }

    public String getTypeDefGuid() {
        return typeDefGuid;
    }

    public void setTypeDefGuid(String typeDefGuid) {
        this.typeDefGuid = typeDefGuid;
    }

    public LineType getLineType() {
        return lineType;
    }

    public void setLineType(LineType lineType) {
        this.lineType = lineType;
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

    public SystemAttributes getSystemAttributes() {
        return systemAttributes;
    }

    public void setSystemAttributes(SystemAttributes systemAttributes) {
        this.systemAttributes = systemAttributes;
    }

    public String getEntity1Guid() {
        return entity1Guid;
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

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
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

    /**
     * Get the extra attributes - ones that are in addition to the standard types.
     * @return extra attributes
     */
    public Map<String, Object> getExtraAttributes() {
        return extraAttributes;
    }
    public void setExtraAttributes(Map<String, Object> extraAttributes) {
        this.extraAttributes = extraAttributes;
    }

    /**
     * Create an omrs relationship with the basic fields filled in.
     * @param line this is a line to create the relationship from
     * @return Relationship the created omrs relationship
     */
    public static Relationship createOmrsRelationship(Line line) {
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
}
