/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.lineage;


import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementStub;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;

/**
 * Describes the relationship from the open metadata repositories that justify the line on the lineage graph.
 */
public class LineageRelationshipEvidence extends ElementHeader
{
    private RelationshipProperties relationshipProperties = null;
    private ElementStub            end1                   = null;
    private ElementStub            end2                   = null;



    /**
     * Default constructor used by subclasses
     */
    public LineageRelationshipEvidence()
    {
    }

    /**
     * Copy/clone constructor.
     *
     * @param template element to copy
     */
    public LineageRelationshipEvidence(ElementHeader template)
    {
        super(template);
    }


    public RelationshipProperties getRelationshipProperties()
    {
        return relationshipProperties;
    }

    public void setRelationshipProperties(RelationshipProperties relationshipProperties)
    {
        this.relationshipProperties = relationshipProperties;
    }

    public ElementStub getEnd1()
    {
        return end1;
    }

    public void setEnd1(ElementStub end1)
    {
        this.end1 = end1;
    }

    public ElementStub getEnd2()
    {
        return end2;
    }

    public void setEnd2(ElementStub end2)
    {
        this.end2 = end2;
    }
}
