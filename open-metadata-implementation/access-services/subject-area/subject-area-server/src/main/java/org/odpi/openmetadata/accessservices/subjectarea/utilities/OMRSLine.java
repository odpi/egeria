/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.utilities;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SystemAttributes;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.LineType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

import java.io.Serializable;


/**
 * A relationship between 2 subject area OMRS entities. It is called types as it has named fields for the attributes and references.
 */


public class OMRSLine extends Line implements Serializable {

    /**
     * Default constructor
     */
    public OMRSLine() {}
    public OMRSLine(Line template) {
        super();
    }
    public OMRSLine(OMRSLine template) {
        super();
//        this.setEntity1Guid(template.getEntity1Guid());
//        this.setEntity1Label(template.getEntity1Label());
//        this.setEntity1Name(template.getEntity1Name());
//        this.setEntity1PropertyName(template.getEntity1PropertyName());
//        this.setEntity2Guid(template.getEntity2Guid());
//        this.setEntity2Label(template.getEntity2Label());
//        this.setEntity2Name(template.getEntity2Name());
//        this.setEntity2PropertyName(template.getEntity2PropertyName());
    }
    public OMRSLine(String name) {
        this.name=name;
        this.lineType=LineType.Unknown;
    }

    public OMRSLine(String name, LineType lineType) {
        this.name=name;
        this.lineType=lineType;
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
                String type = omrsRelationship.getEntityOneProxy().getType().getTypeDefName();
                if (type.equals("GlossaryTerm")) {
                    type = "Term";
                }
                this.getEnd1().setNodeType(type);
            }
            this.getEnd1().setNodeGuid(omrsRelationship.getEntityOneProxy().getGUID());

        }
        if (omrsRelationship.getEntityTwoProxy() != null)
        {
            String type = omrsRelationship.getEntityTwoProxy().getType().getTypeDefName();
            if (type.equals("GlossaryTerm")) {
                type = "Term";
            }
            this.getEnd2().setNodeType(type);
        }
        this.getEnd2().setNodeGuid(omrsRelationship.getEntityTwoProxy().getGUID());
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



    public String getName() {
        return name;
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
        entityOne.setGUID(line.getEnd1().getNodeGuid());
        String type1 = line.getEnd1().getNodeType();
        if (type1.equals("Term")) {
            type1 = "GlossaryTerm";
        }
        InstanceType instancetype1 = new InstanceType();
        instancetype1.setTypeDefName(type1);
        entityOne.setType(instancetype1);
        //set proxy 2
        EntityProxy entityTwo = new EntityProxy();
        entityTwo.setGUID(line.getEnd2().getNodeGuid());
        String type2 = line.getEnd2().getNodeType();
        if (type2.equals("Term")) {
            type2 = "GlossaryTerm";
        }
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
        sb.append("lineType=").append(lineType.name()).append(",");
        sb.append("name=").append(name);
        sb.append('}');
        return sb;
    }
}