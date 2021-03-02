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
 * This class holds a count associated with a nodes by NodeType name or lines by LineType name.
 */
public class NodeLineStats {
    private String nodeOrLineTypeName;
    private Integer count;

    public NodeLineStats(String nodeOrLineTypeName, Integer count) {
       this.nodeOrLineTypeName = nodeOrLineTypeName;
       this.count = count;
    }

    /*
     * Getters for Jackson
     */


    public String getNodeOrLineTypeName() { return nodeOrLineTypeName; }
    public Integer getCount() { return count; }



    public void setNodeOrLineTypeName(String nodeOrLineTypeName) { this.nodeOrLineTypeName = nodeOrLineTypeName; }
    public void setCount(Integer count) { this.count = count; }



    @Override
    public String toString()
    {
        return "CountForNodeOrLineType{" +
                ", nodeOrLineTypeName=" + nodeOrLineTypeName +
                ", count=" + count +
                '}';
    }



}
