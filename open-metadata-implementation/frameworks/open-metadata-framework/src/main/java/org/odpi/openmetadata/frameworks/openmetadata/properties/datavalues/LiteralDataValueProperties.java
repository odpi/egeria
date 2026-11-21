/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.datavalues;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * LiteralDataValueProperties describes a data value where the data is stored in the open metadata element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class LiteralDataValueProperties extends DataValueProperties
{
    private String              dataValue     = null;
    private List<String>        dataValueList = null;
    private Map<String, String> dataValueMap  = null;


    /**
     * Default constructor
     */
    public LiteralDataValueProperties()
    {
        super();
        super.typeName = OpenMetadataType.LITERAL_DATA_VALUE.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public LiteralDataValueProperties(LiteralDataValueProperties template)
    {
        super(template);

        if (template != null)
        {
            dataValue     = template.getDataValue();
            dataValueList = template.getDataValueList();
            dataValueMap  = template.getDataValueMap();
        }
    }


    /**
     * Return the data expressed as a string, in the format given by dataFormat.
     *
     * @return string
     */
    public String getDataValue()
    {
        return dataValue;
    }


    /**
     * Set up the data expressed as a string, in the format given by dataFormat.
     *
     * @param dataValue string
     */
    public void setDataValue(String dataValue)
    {
        this.dataValue = dataValue;
    }


    /**
     * Return the data expressed as a list of strings.
     *
     * @return list
     */
    public List<String> getDataValueList()
    {
        return dataValueList;
    }


    /**
     * Set up the data expressed as a list of strings.
     *
     * @param dataValueList list
     */
    public void setDataValueList(List<String> dataValueList)
    {
        this.dataValueList = dataValueList;
    }


    /**
     * Return the data expressed as a map of strings to strings.
     *
     * @return map
     */
    public Map<String, String> getDataValueMap()
    {
        return dataValueMap;
    }


    /**
     * Set up the data expressed as a map of strings to strings.
     *
     * @param dataValueMap map
     */
    public void setDataValueMap(Map<String, String> dataValueMap)
    {
        this.dataValueMap = dataValueMap;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "LiteralDataValueProperties{" +
                "dataValue='" + dataValue + '\'' +
                ", dataValueList=" + dataValueList +
                ", dataValueMap=" + dataValueMap +
                "} " + super.toString();
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
        if (!super.equals(objectToCompare)) return false;
        LiteralDataValueProperties that = (LiteralDataValueProperties) objectToCompare;
        return Objects.equals(dataValue, that.dataValue) &&
                Objects.equals(dataValueList, that.dataValueList) &&
                Objects.equals(dataValueMap, that.dataValueMap);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), dataValue, dataValueList, dataValueMap);
    }
}
