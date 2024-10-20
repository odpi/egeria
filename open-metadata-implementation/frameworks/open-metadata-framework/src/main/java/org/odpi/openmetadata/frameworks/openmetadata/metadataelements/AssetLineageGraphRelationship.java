/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * AssetLineageGraphRelationship describes the derived lineage graph relationship between two assets.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AssetLineageGraphRelationship
{
    private List<String> relationshipTypes = null;
    private String       end1AssetGUID     = null;
    private String       end2AssetGUID     = null;


    /**
     * Default constructor
     */
    public AssetLineageGraphRelationship()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AssetLineageGraphRelationship(AssetLineageGraphRelationship template)
    {
        if (template != null)
        {
            relationshipTypes      = template.getRelationshipTypes();
            end1AssetGUID = template.getEnd1AssetGUID();
            end2AssetGUID = template.getEnd2AssetGUID();
        }
    }


    /**
     * Return the type names from the matching relationships.
     *
     * @return list of type names
     */
    public List<String> getRelationshipTypes()
    {
        return relationshipTypes;
    }


    /**
     * Set up the type names from the matching relationships.
     *
     * @param relationshipTypes list of type names
     */
    public void setRelationshipTypes(List<String> relationshipTypes)
    {
        this.relationshipTypes = relationshipTypes;
    }


    /**
     * Return the unique identifier of the end 1 asset.
     *
     * @return guid
     */
    public String getEnd1AssetGUID()
    {
        return end1AssetGUID;
    }


    /**
     * Set up the  unique identifier of the end 1 asset.
     *
     * @param end1AssetGUID  guid
     */
    public void setEnd1AssetGUID(String end1AssetGUID)
    {
        this.end1AssetGUID = end1AssetGUID;
    }



    /**
     * Return the unique identifier of the end 2 asset.
     *
     * @return guid
     */
    public String getEnd2AssetGUID()
    {
        return end2AssetGUID;
    }


    /**
     * Set up the  unique identifier of the end 2 asset.
     *
     * @param end2AssetGUID  guid
     */
    public void setEnd2AssetGUID(String end2AssetGUID)
    {
        this.end2AssetGUID = end2AssetGUID;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "AssetLineageGraphRelationship{" +
                "relationshipTypes=" + relationshipTypes +
                ", end1AssetGUID='" + end1AssetGUID + '\'' +
                ", end2AssetGUID='" + end2AssetGUID + '\'' +
                '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        AssetLineageGraphRelationship that = (AssetLineageGraphRelationship) objectToCompare;
        return Objects.equals(relationshipTypes, that.relationshipTypes) &&
                       Objects.equals(end1AssetGUID, that.end1AssetGUID) &&
                       Objects.equals(end2AssetGUID, that.end2AssetGUID);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(relationshipTypes, end1AssetGUID, end2AssetGUID);
    }
}
