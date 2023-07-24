/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasTermAssignmentHeader describes a glossary term that is linked to an instance via a semantic assignment.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasTermAssignmentHeader
{
    private String                    termGuid      = null;
    private String                    relationGuid  = null;
    private String                    description   = null;
    private String                    displayText   = null;
    private String                    expression    = null;
    private String                    createdBy     = null;
    private String                    steward       = null;
    private String                    source        = null;
    private int                       confidence    = 0;
    private AtlasTermAssignmentStatus status        = null;
    private String                    qualifiedName = null;


    public AtlasTermAssignmentHeader()
    {
    }


    public String getTermGuid()
    {
        return termGuid;
    }


    public void setTermGuid(String termGuid)
    {
        this.termGuid = termGuid;
    }


    public String getRelationGuid()
    {
        return relationGuid;
    }


    public void setRelationGuid(String relationGuid)
    {
        this.relationGuid = relationGuid;
    }


    public String getDescription()
    {
        return description;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getDisplayText()
    {
        return displayText;
    }


    public void setDisplayText(String displayText)
    {
        this.displayText = displayText;
    }


    public String getExpression()
    {
        return expression;
    }


    public void setExpression(String expression)
    {
        this.expression = expression;
    }


    public String getCreatedBy()
    {
        return createdBy;
    }


    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }


    public String getSteward()
    {
        return steward;
    }


    public void setSteward(String steward)
    {
        this.steward = steward;
    }


    public String getSource()
    {
        return source;
    }


    public void setSource(String source)
    {
        this.source = source;
    }


    public int getConfidence()
    {
        return confidence;
    }


    public void setConfidence(int confidence)
    {
        this.confidence = confidence;
    }


    public AtlasTermAssignmentStatus getStatus()
    {
        return status;
    }


    public void setStatus(AtlasTermAssignmentStatus status)
    {
        this.status = status;
    }


    public String getQualifiedName()
    {
        return qualifiedName;
    }


    public void setQualifiedName(String qualifiedName)
    {
        this.qualifiedName = qualifiedName;
    }


    @Override
    public String toString()
    {
        return "AtlasTermAssignmentHeader{" +
                       "termGuid='" + termGuid + '\'' +
                       ", relationGuid='" + relationGuid + '\'' +
                       ", description='" + description + '\'' +
                       ", displayText='" + displayText + '\'' +
                       ", expression='" + expression + '\'' +
                       ", createdBy='" + createdBy + '\'' +
                       ", steward='" + steward + '\'' +
                       ", source='" + source + '\'' +
                       ", confidence=" + confidence +
                       ", status=" + status +
                       ", qualifiedName='" + qualifiedName + '\'' +
                       '}';
    }
}
