/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasRelatedObjectId describes an entity related by a relationship.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasRelatedObjectId extends AtlasObjectId
{
    public static final String KEY_RELATIONSHIP_TYPE       = "relationshipType";
    public static final String KEY_RELATIONSHIP_GUID       = "relationshipGuid";
    public static final String KEY_RELATIONSHIP_STATUS     = "relationshipStatus";
    public static final String KEY_RELATIONSHIP_ATTRIBUTES = "relationshipAttributes";

    private AtlasInstanceStatus entityStatus           = null;
    private String              displayText            = null;
    private String              relationshipType       = null;
    private String              relationshipGuid       = null;
    private AtlasInstanceStatus relationshipStatus     = null;
    private AtlasStruct         relationshipAttributes = null;
    private String              qualifiedName          = null;


    public AtlasRelatedObjectId()
    {
    }


    public AtlasInstanceStatus getEntityStatus()
    {
        return entityStatus;
    }


    public void setEntityStatus(AtlasInstanceStatus entityStatus)
    {
        this.entityStatus = entityStatus;
    }


    public String getDisplayText()
    {
        return displayText;
    }


    public void setDisplayText(String displayText)
    {
        this.displayText = displayText;
    }


    public String getRelationshipType()
    {
        return relationshipType;
    }


    public void setRelationshipType(String relationshipType)
    {
        this.relationshipType = relationshipType;
    }


    public String getRelationshipGuid()
    {
        return relationshipGuid;
    }


    public void setRelationshipGuid(String relationshipGuid)
    {
        this.relationshipGuid = relationshipGuid;
    }


    public AtlasInstanceStatus getRelationshipStatus()
    {
        return relationshipStatus;
    }


    public void setRelationshipStatus(AtlasInstanceStatus relationshipStatus)
    {
        this.relationshipStatus = relationshipStatus;
    }


    public AtlasStruct getRelationshipAttributes()
    {
        return relationshipAttributes;
    }


    public void setRelationshipAttributes(AtlasStruct relationshipAttributes)
    {
        this.relationshipAttributes = relationshipAttributes;
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
        return "AtlasRelatedObjectId{" +
                       "entityStatus=" + entityStatus +
                       ", displayText='" + displayText + '\'' +
                       ", relationshipType='" + relationshipType + '\'' +
                       ", relationshipGuid='" + relationshipGuid + '\'' +
                       ", relationshipStatus=" + relationshipStatus +
                       ", relationshipAttributes=" + relationshipAttributes +
                       ", qualifiedName='" + qualifiedName + '\'' +
                       ", guid='" + getGuid() + '\'' +
                       ", typeName='" + getTypeName() + '\'' +
                       ", uniqueAttributes=" + getUniqueAttributes() +
                       '}';
    }
}
