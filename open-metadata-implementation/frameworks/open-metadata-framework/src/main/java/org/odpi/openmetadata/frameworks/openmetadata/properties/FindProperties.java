/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.FindAssetOriginProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.LevelIdentifierQueryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.SemanticAssignmentQueryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.DataFieldQueryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityTagQueryProperties;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * FindProperties provides the base class for find by property requests.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = LevelIdentifierQueryProperties.class, name = "LevelIdentifierQueryProperties"),
                @JsonSubTypes.Type(value = DataFieldQueryProperties.class, name = "DataFieldQueryProperties"),
                @JsonSubTypes.Type(value = FindNameProperties.class, name = "FindNameProperties"),
                @JsonSubTypes.Type(value = FindAssetOriginProperties.class, name = "FindAssetOriginProperties"),
                @JsonSubTypes.Type(value = FindPropertyNamesProperties.class, name = "FindPropertyNamesProperties"),
                @JsonSubTypes.Type(value = SecurityTagQueryProperties.class, name = "SecurityTagQueryProperties"),
                @JsonSubTypes.Type(value = SemanticAssignmentQueryProperties.class, name = "SemanticAssignmentQueryProperties"),
        })
public class FindProperties extends FindRequest
{
    private String              openMetadataTypeName = null;
    private List<ElementStatus> limitResultsByStatus = null;
    private String              sequencingProperty   = null;
    private SequencingOrder     sequencingOrder      = SequencingOrder.LAST_UPDATE_RECENT;

    /**
     * Default constructor
     */
    public FindProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.  Retrieve values from the supplied template
     *
     * @param template element to copy
     */
    public FindProperties(FindProperties template)
    {
        super (template);

        if (template != null)
        {
            openMetadataTypeName = template.getOpenMetadataTypeName();
            limitResultsByStatus = template.getLimitResultsByStatus();
            sequencingProperty   = template.getSequencingProperty();
            sequencingOrder      = template.getSequencingOrder();
        }
    }


    /**
     * Return the open metadata type name to filter by.
     *
     * @return string name
     */
    public String getOpenMetadataTypeName()
    {
        return openMetadataTypeName;
    }


    /**
     * Set up the open metadata type name to filer by.
     *
     * @param openMetadataTypeName string name
     */
    public void setOpenMetadataTypeName(String openMetadataTypeName)
    {
        this.openMetadataTypeName = openMetadataTypeName;
    }


    /**
     * Return the status values that the resulting metadata elements must match.
     * By default, relationships in all non-DELETED statuses are returned.  However, it is possible
     * to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     * status values except DELETED.
     *
     * @return status values
     */
    public List<ElementStatus> getLimitResultsByStatus()
    {
        return limitResultsByStatus;
    }


    /**
     * Set up the status values that the resulting metadata elements must match.
     *
     * @param limitResultsByStatus By default, relationships in all non-DELETED statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values except DELETED.
     */
    public void setLimitResultsByStatus(List<ElementStatus> limitResultsByStatus)
    {
        this.limitResultsByStatus = limitResultsByStatus;
    }


    /**
     * Return the name of the property whose value will be used to sequence the results.
     *
     * @return property name
     */
    public String getSequencingProperty()
    {
        return sequencingProperty;
    }


    /**
     * Set up the name of the property whose value will be used to sequence the results.
     *
     * @param sequencingProperty property name
     */
    public void setSequencingProperty(String sequencingProperty)
    {
        this.sequencingProperty = sequencingProperty;
    }


    /**
     * Return the order that the results should be returned in.
     *
     * @return enum for the sequencing order
     */
    public SequencingOrder getSequencingOrder()
    {
        return sequencingOrder;
    }


    /**
     * Set up the order that the results should be returned in.
     *
     * @param sequencingOrder enum for the sequencing order
     */
    public void setSequencingOrder(SequencingOrder sequencingOrder)
    {
        this.sequencingOrder = sequencingOrder;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "FindProperties{" +
                "openMetadataTypeName='" + openMetadataTypeName + '\'' +
                ", limitResultsByStatus=" + limitResultsByStatus +
                ", sequencingProperty='" + sequencingProperty + '\'' +
                ", sequencingOrder=" + sequencingOrder +
                "} " + super.toString();
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        FindProperties that = (FindProperties) objectToCompare;
        return Objects.equals(openMetadataTypeName, that.openMetadataTypeName) &&
                Objects.equals(limitResultsByStatus, that.limitResultsByStatus) &&
                Objects.equals(sequencingProperty, that.sequencingProperty) &&
                sequencingOrder == that.sequencingOrder;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(openMetadataTypeName, limitResultsByStatus, sequencingProperty, sequencingOrder);
    }
}