/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataStructureProperties;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataStructureElement contains the properties and header for a data structure entity retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataStructureElement extends AttributedMetadataElement
{
    private DataStructureProperties             properties           = null;
    private List<MemberDataField>               memberDataFields     = null;
    private RelatedMetadataElementSummary       equivalentSchemaType = null;
    private String                              mermaidGraph         = null;

    /**
     * Default constructor
     */
    public DataStructureElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DataStructureElement(DataStructureElement template)
    {
        super(template);

        if (template != null)
        {
            properties = template.getProperties();
            memberDataFields = template.getMemberDataFields();
            equivalentSchemaType = template.getEquivalentSchemaType();
            mermaidGraph = template.getMermaidGraph();
        }
    }


    /**
     * Return details of the data field
     *
     * @return data field properties
     */
    public DataStructureProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up data field properties
     *
     * @param properties data field properties
     */
    public void setProperties(DataStructureProperties properties)
    {
        this.properties = properties;
    }


    /**
     * Return any nested data fields.
     *
     * @return list
     */
    public List<MemberDataField> getMemberDataFields()
    {
        return memberDataFields;
    }


    /**
     * Set up any nested data fields.
     *
     * @param memberDataFields list
     */
    public void setMemberDataFields(List<MemberDataField> memberDataFields)
    {
        this.memberDataFields = memberDataFields;
    }


    /**
     * Return the equivalent schema type for this data field.
     *
     * @return related element
     */
    public RelatedMetadataElementSummary getEquivalentSchemaType()
    {
        return equivalentSchemaType;
    }


    /**
     * Set up the equivalent schema type for this data field.
     *
     * @param equivalentSchemaType related element
     */
    public void setEquivalentSchemaType(RelatedMetadataElementSummary equivalentSchemaType)
    {
        this.equivalentSchemaType = equivalentSchemaType;
    }


    /**
     * Return the mermaid representation of this data structure.
     *
     * @return string markdown
     */
    public String getMermaidGraph()
    {
        return mermaidGraph;
    }


    /**
     * Set up the mermaid representation of this data structure.
     *
     * @param mermaidGraph markdown string
     */
    public void setMermaidGraph(String mermaidGraph)
    {
        this.mermaidGraph = mermaidGraph;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DataStructureElement{" +
                "properties=" + properties +
                ", memberDataFields=" + memberDataFields +
                ", equivalentSchemaType=" + equivalentSchemaType +
                ", mermaidGraph='" + mermaidGraph + '\'' +
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
        DataStructureElement that = (DataStructureElement) objectToCompare;
        return Objects.equals(properties, that.properties) &&
                Objects.equals(memberDataFields, that.memberDataFields) &&
                Objects.equals(equivalentSchemaType, that.equivalentSchemaType) &&
                Objects.equals(mermaidGraph, that.mermaidGraph);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), properties, memberDataFields, equivalentSchemaType, mermaidGraph);
    }
}
