/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.assetcatalog.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Describe a node in a lineage graph.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class"
)
public class LineageVertex
{
    private Object id;
    private String nodeID;
    private String nodeType;
    private String displayName;
    private String guid;
    private String qualifiedName;
    private Map<String, String> properties;


    /**
     * Default constructor
     */
    public LineageVertex()
    {
    }

    public LineageVertex(String nodeID, String nodeType)
    {
        this.nodeID = nodeID;
        this.nodeType = nodeType;
    }

    public String getNodeType() {
        return nodeType;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getNodeID() {
        return nodeID;
    }

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "LineageVertex{" +
                       "id=" + id +
                       ", nodeID='" + nodeID + '\'' +
                       ", nodeType='" + nodeType + '\'' +
                       ", displayName='" + displayName + '\'' +
                       ", guid='" + guid + '\'' +
                       ", qualifiedName='" + qualifiedName + '\'' +
                       ", properties=" + properties +
                       '}';
    }

    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        LineageVertex that = (LineageVertex) objectToCompare;
        return Objects.equals(nodeID, that.nodeID) &&
                       Objects.equals(nodeType, that.nodeType) &&
                       Objects.equals(displayName, that.displayName) &&
                       Objects.equals(guid, that.guid) &&
                       Objects.equals(qualifiedName, that.qualifiedName) &&
                       Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeID, nodeType, displayName, guid, qualifiedName, properties);
    }


}
