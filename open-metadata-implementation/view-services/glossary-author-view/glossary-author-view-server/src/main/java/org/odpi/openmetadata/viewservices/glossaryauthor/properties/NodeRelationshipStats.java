/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
/**
 * This class holds a count associated with a node by NodeType name or a relationship by relationshipType name.
 */
public class NodeRelationshipStats {
    private String nodeOrRelationshipTypeName;
    private Integer count;

    public NodeRelationshipStats(String nodeOrRelationshipTypeName, Integer count) {
       this.nodeOrRelationshipTypeName = nodeOrRelationshipTypeName;
       this.count = count;
    }

    /*
     * Getters for Jackson
     */

    /**
     * get the name
     * @return name
     */
    public String getNodeOrRelationshipTypeName() { return nodeOrRelationshipTypeName; }

    /**
     * get the count for a type
     * @return count
     */
    public Integer getCount() { return count; }


    /**
     * St the type name
     * @param nodeOrRelationshipTypeName name to set
     */
    public void setNodeOrRelationshipTypeName(String nodeOrRelationshipTypeName) { this.nodeOrRelationshipTypeName = nodeOrRelationshipTypeName; }

    /**
     * Set the could
     * @param count supplied count
     */
    public void setCount(Integer count) { this.count = count; }



    @Override
    public String toString()
    {
        return "CountForNodeOrRelationshipType{" +
                ", nodeOrRelationshipTypeName=" + nodeOrRelationshipTypeName +
                ", count=" + count +
                '}';
    }



}
