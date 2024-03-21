/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.lineagewarehouse.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 *
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class LineageEdge implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;

    protected Object id;
    protected String edgeType;
    protected String sourceNodeID;
    protected String destinationNodeID;

    /**
     * Default constructor for Jackson
     */
    public LineageEdge(){}

    public LineageEdge(Object id,
                       String edgeType,
                       String sourceNodeID,
                       String destinationNodeID)
    {
        this.id = id;
        this.edgeType = edgeType;
        this.sourceNodeID = sourceNodeID;
        this.destinationNodeID = destinationNodeID;
    }

    public Object getId(){
        return id;
    }
    public String getEdgeType() {
        return edgeType;
    }
    public String getSourceNodeID() {
        return sourceNodeID;
    }
    public String getDestinationNodeID() {
        return destinationNodeID;
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        LineageEdge that = (LineageEdge) objectToCompare;
        return Objects.equals(id, that.id) &&
                Objects.equals(edgeType, that.edgeType) &&
                Objects.equals(sourceNodeID, that.sourceNodeID) &&
                Objects.equals(destinationNodeID, that.destinationNodeID);
    }



    @Override
    public int hashCode() {
        return Objects.hash(id, edgeType, sourceNodeID, destinationNodeID);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "LineageEdge{" +
                "id=" + id +
                ", edgeType='" + edgeType + '\'' +
                ", sourceNodeID='" + sourceNodeID + '\'' +
                ", destinationNodeID='" + destinationNodeID + '\'' +
                '}';
    }
}
