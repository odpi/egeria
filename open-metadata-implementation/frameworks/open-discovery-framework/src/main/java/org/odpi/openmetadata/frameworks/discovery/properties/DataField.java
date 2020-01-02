/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.PropertyBase;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * DataField describes a single data field (column, attribute or property) discovered during the analysis of an asset.  It provides an anchor for
 * annotations that are specific to the field.
 */
public class DataField extends PropertyBase
{
    private static final long    serialVersionUID = 1L;

    private int                       dataFieldPosition    = 0;
    private String                    dataFieldName        = null;
    private String                    dataFieldType        = null;
    private List<DataFieldAnnotation> dataFieldAnnotations = null;
    private Map<String, String>       additionalProperties = null;
    private int                       nestedDataFields     = 0;

    /**
     * Default constructor
     */
    public DataField()
    {
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public DataField(DataField template)
    {
        super(template);

        if (template != null)
        {
            dataFieldPosition = template.getDataFieldPosition();
            dataFieldName = template.getDataFieldName();
            dataFieldType = template.getDataFieldType();
            dataFieldAnnotations = template.getDataFieldAnnotations();
            additionalProperties = template.getAdditionalProperties();
            nestedDataFields = template.getNestedDataFields();
        }
    }


    public int getDataFieldPosition()
    {
        return dataFieldPosition;
    }


    public void setDataFieldPosition(int dataFieldPosition)
    {
        this.dataFieldPosition = dataFieldPosition;
    }


    public String getDataFieldName()
    {
        return dataFieldName;
    }


    public void setDataFieldName(String dataFieldName)
    {
        this.dataFieldName = dataFieldName;
    }


    public String getDataFieldType()
    {
        return dataFieldType;
    }


    public void setDataFieldType(String dataFieldType)
    {
        this.dataFieldType = dataFieldType;
    }


    public List<DataFieldAnnotation> getDataFieldAnnotations()
    {
        return dataFieldAnnotations;
    }


    public void setDataFieldAnnotations(
            List<DataFieldAnnotation> dataFieldAnnotations)
    {
        this.dataFieldAnnotations = dataFieldAnnotations;
    }


    public Map<String, String> getAdditionalProperties()
    {
        return additionalProperties;
    }


    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    public int getNestedDataFields()
    {
        return nestedDataFields;
    }


    public void setNestedDataFields(int nestedDataFields)
    {
        this.nestedDataFields = nestedDataFields;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataField{" +
                       "dataFieldPosition=" + dataFieldPosition +
                       ", dataFieldName='" + dataFieldName + '\'' +
                       ", dataFieldType='" + dataFieldType + '\'' +
                       ", dataFieldAnnotations=" + dataFieldAnnotations +
                       ", additionalProperties=" + additionalProperties +
                       ", nestedDataFields=" + nestedDataFields +
                       '}';
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        DataField dataField = (DataField) objectToCompare;
        return getDataFieldPosition() == dataField.getDataFieldPosition() &&
                       getNestedDataFields() == dataField.getNestedDataFields() &&
                       Objects.equals(getDataFieldName(), dataField.getDataFieldName()) &&
                       Objects.equals(getDataFieldType(), dataField.getDataFieldType()) &&
                       Objects.equals(getDataFieldAnnotations(), dataField.getDataFieldAnnotations()) &&
                       Objects.equals(getAdditionalProperties(), dataField.getAdditionalProperties());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getDataFieldPosition(), getDataFieldName(), getDataFieldType(), getDataFieldAnnotations(),
                            getAdditionalProperties(), getNestedDataFields());
    }
}
