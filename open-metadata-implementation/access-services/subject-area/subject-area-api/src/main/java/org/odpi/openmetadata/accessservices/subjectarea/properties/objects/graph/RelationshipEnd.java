/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.OmasObject;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipEndCardinality;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * A Relationship end, that is associated with a Node.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "class",
        defaultImpl = RelationshipEnd.class,
        visible = true
)
public class RelationshipEnd implements Serializable, OmasObject {
    protected static final long serialVersionUID = 1L;
    private String nodeGuid =null;
    private String nodeTypeName = null;
    private String nodeQualifiedName = null;
    private String name = null;
    private String description = null;
    private RelationshipEndCardinality cardinality;

    /**
     * Default constructor
     */
    public RelationshipEnd() {
    }

    /**
     * constructor
     * @param nodeTypeName node type associated with this end of the relationship
     * @param name name of the end
     * @param description description of the end
     * @param cardinality cardinality of the end
     */
    public RelationshipEnd(String nodeTypeName, String name, String description, RelationshipEndCardinality cardinality) {
        this.setNodeTypeName(nodeTypeName);
        this.setName(name);
        this.setDescription(description);
        this.setCardinality(cardinality);
        this.setNodeGuid(null);
        this.setNodeQualifiedName(null);
    }
    /**
     * Copy constructor
     * @param endToCopy relationshipEnd to copy
     */
    public RelationshipEnd(RelationshipEnd endToCopy) {
        this.setNodeTypeName(endToCopy.getNodeTypeName());
        this.setName(endToCopy.getName());
        this.setDescription(endToCopy.getDescription());
        this.setCardinality(endToCopy.getCardinality());
        this.setNodeGuid(endToCopy.getNodeGuid());
        this.setNodeQualifiedName(endToCopy.getNodeQualifiedName());
    }

    /**
     * get the node guid for this end
     * @return guid
     */
    public String getNodeGuid() {
        return nodeGuid;
    }

    /**
     * Set the node guid for this end
     * @param nodeGuid node guid
     */
    public void setNodeGuid(String nodeGuid) {
        this.nodeGuid = nodeGuid;
    }

    /**
     * get the node qualified name for this end
     * @return node qualified name
     */
    public String getNodeQualifiedName() {
        return nodeQualifiedName;
    }

    /**
     * Set the node qualified name for this end
     * @param nodeQualifiedName node qualified name
     */
    public void setNodeQualifiedName(String nodeQualifiedName) {
        this.nodeQualifiedName = nodeQualifiedName;
    }

    /**
     * Get the node type for this end
     * @return node type
     */
    public String getNodeTypeName() {
        return nodeTypeName;
    }

    /**
     * Set the node type for this end
     * @param nodeTypeName node type
     */
    public void setNodeTypeName(String nodeTypeName) {
        this.nodeTypeName = nodeTypeName;
    }

    /**
     * Get the name of this end
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of this end
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the description of this end
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * set the description of this end
     * @param description description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the cardinality of this end
     * @return cardinality
     */
    public RelationshipEndCardinality getCardinality() {
        return cardinality;
    }

    /**
     * Set the cardnicality of this end
     * @param cardinality cardinality
     */
    public void setCardinality(RelationshipEndCardinality cardinality) {
        this.cardinality = cardinality;
    }

    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }

        sb.append("RelationshipEnd{");
        sb.append("nodeGuid=").append(nodeGuid).append(",");
        sb.append("nodeQualifiedName=").append(nodeQualifiedName).append(",");
        sb.append("nodeType=").append(nodeTypeName).append(",");
        sb.append("name=").append(name).append(",");
        sb.append("description=").append(description).append(",");
        sb.append("cardinality=").append(cardinality);
        sb.append('}');
        return sb;
    }

    @Override
    public String toString() {
        return this.toString(null).toString();
    }
}
