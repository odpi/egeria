/* SPDX-License-Identifier: Apache-2.0 */
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
 * A Line end, that is associated with a Node.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "class",
        defaultImpl = LineEnd.class,
        visible = true
)
public class LineEnd implements Serializable, OmasObject {
    protected static final long serialVersionUID = 1L;
    private String nodeGuid;
    private String nodeType;
    private String name;
    private String description;
    private RelationshipEndCardinality cardinality;

    /**
     * Default constructor
     */
    public LineEnd() {
    }

    public LineEnd(String nodeType, String name, String description, RelationshipEndCardinality cardinality ) {
        this.setNodeType(nodeType);
        this.setName(name);
        this.setDescription(description);
        this.setCardinality(cardinality);
    }

    public String getNodeGuid() {
        return nodeGuid;
    }

    public void setNodeGuid(String nodeGuid) {
        this.nodeGuid = nodeGuid;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RelationshipEndCardinality getCardinality() {
        return cardinality;
    }

    public void setCardinality(RelationshipEndCardinality cardinality) {
        this.cardinality = cardinality;
    }

    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }

        sb.append("LineEnd{");
        sb.append("nodeGuid=").append(nodeGuid).append(",");
        sb.append("nodeType=").append(nodeType).append(",");
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