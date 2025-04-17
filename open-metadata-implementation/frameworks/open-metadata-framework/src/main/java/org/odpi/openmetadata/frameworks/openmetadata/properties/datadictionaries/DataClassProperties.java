/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataClassProperties is used to provide the characterizations of the data values stored in a data field
 * described by the attached element.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataClassProperties extends ReferenceableProperties
{
    private String              displayName           = null;
    private String              description           = null;
    private String              classCode             = null;
    private boolean             userDefined           = true;
    private String              namespace             = null;
    private String              specification         = null;
    private Map<String, String> specificationDetails  = null;
    private String              dataType              = null;
    private Float               matchThreshold        = 0F;
    private boolean             allowsDuplicateValues = true;
    private String              defaultValue          = null;
    private List<String>        sampleValues          = null;
    private List<String>        dataPatterns          = null;
    private List<String>        namePatterns          = null;


    /**
     * Default constructor
     */
    public DataClassProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor for an editing glossary classification.
     *
     * @param template template object to copy.
     */
    public DataClassProperties(DataClassProperties template)
    {
        super(template);

        if (template != null)
        {
            displayName           = template.getDisplayName();
            description           = template.getDescription();
            classCode             = template.getClassCode();
            userDefined           = template.getUserDefined();
            namespace             = template.getNamespace();
            specification         = template.getSpecification();
            specificationDetails  = template.getSpecificationDetails();
            dataType              = template.getDataType();
            matchThreshold        = template.getMatchThreshold();
            allowsDuplicateValues = template.getAllowsDuplicateValues();
            defaultValue          = template.getDefaultValue();
            sampleValues          = template.getSampleValues();
            dataPatterns          = template.getDataPatterns();
            namePatterns          = template.getNamePatterns();
        }
    }



    /**
     * Return the display name of the data class.
     *
     * @return string name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the display name of the data class.
     *
     * @param displayName string name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Return the description of the data class.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the data class.
     *
     * @param description string description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the name of processing class that can identify the data class.
     *
     * @return string name
     */
    public String getClassCode()
    {
        return classCode;
    }


    /**
     * Set up the name of processing class that can identify the data class.
     *
     * @param classCode string name
     */
    public void setClassCode(String classCode)
    {
        this.classCode = classCode;
    }


    /**
     * Return whether this data class is defined by the local organization or a vendor/industry standard.
     *
     * @return boolean
     */
    public boolean getUserDefined()
    {
        return userDefined;
    }


    /**
     * Set up  whether this data class is defined by the local organization or a vendor/industry standard.
     *
     * @param userDefined boolean
     */
    public void setUserDefined(boolean userDefined)
    {
        this.userDefined = userDefined;
    }


    /**
     * Return the parsing string used to identify values of this data class.
     *
     * @return string
     */
    public String getSpecification()
    {
        return specification;
    }


    /**
     *  Set up the parsing string used to identify values of this data class.
     *
     * @param specification string
     */
    public void setSpecification(String specification)
    {
        this.specification = specification;
    }


    /**
     * Return any additional properties used in the specification.
     *
     * @return property map
     */
    public Map<String, String> getSpecificationDetails()
    {
        return specificationDetails;
    }


    /**
     * Set up any additional properties used in the specification.
     *
     * @param specificationDetails property map
     */
    public void setSpecificationDetails(Map<String, String> specificationDetails)
    {
        this.specificationDetails = specificationDetails;
    }


    /**
     * Return the data type for this element.  Null means unknown data type.
     *
     * @return String data type name
     */
    public String getDataType() { return dataType; }


    /**
     * Set up the data type for this element.  Null means unknown data type.
     *
     * @param dataType data type name
     */
    public void setDataType(String dataType)
    {
        this.dataType = dataType;
    }


    /**
     * Return the match threshold that a data field is expected to achieve to be assigned this data class.
     *
     * @return float
     */
    public Float getMatchThreshold()
    {
        return matchThreshold;
    }


    /**
     * Set up the match threshold that a data field is expected to achieve to be assigned this data class.
     *
     * @param matchThreshold float
     */
    public void setMatchThreshold(Float matchThreshold)
    {
        this.matchThreshold = matchThreshold;
    }

    /**
     * Return whether the same value can be used by more than one instance of this attribute.
     *
     * @return boolean flag
     */
    public boolean getAllowsDuplicateValues()
    {
        return allowsDuplicateValues;
    }


    /**
     * Set up whether the same value can be used by more than one instance of this attribute.
     *
     * @param allowsDuplicateValues boolean flag
     */
    public void setAllowsDuplicateValues(boolean allowsDuplicateValues)
    {
        this.allowsDuplicateValues = allowsDuplicateValues;
    }


    /**
     * Return the name of the namespace that this type belongs to.
     *
     * @return string name
     */
    public String getNamespace()
    {
        return namespace;
    }


    /**
     * Set up the name of the namespace that this type belongs to.
     *
     * @param namespace string name
     */
    public void setNamespace(String namespace)
    {
        this.namespace = namespace;
    }


    /**
     * Return the default value typically assigned to these types of data fields.
     *
     * @return string description
     */
    public String getDefaultValue()
    {
        return defaultValue;
    }


    /**
     * Set up the description (typically and overview of the revision) of the glossary.
     *
     * @param defaultValue string description
     */
    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }


    /**
     * Return a list of sample values for the data field.
     *
     * @return list of strings
     */
    public List<String> getSampleValues()
    {
        return sampleValues;
    }


    /**
     * Set up a list of sample values for the data field.
     *
     * @param sampleValues list of strings
     */
    public void setSampleValues(List<String> sampleValues)
    {
        this.sampleValues = sampleValues;
    }


    /**
     * Return a regular expression that characterizes the data values stored in this data field.
     *
     * @return string
     */
    public List<String> getDataPatterns()
    {
        return dataPatterns;
    }


    /**
     * Set up a regular expression that characterizes the data values stored in this data field.
     *
     * @param dataPatterns string
     */
    public void setDataPatterns(List<String> dataPatterns)
    {
        this.dataPatterns = dataPatterns;
    }


    /**
     * Return a regular expression that characterizes the name used for this type of data field.
     *
     * @return string
     */
    public List<String> getNamePatterns()
    {
        return namePatterns;
    }


    /**
     * Set up  a regular expression that characterizes the name used for this type of data field.
     *
     * @param namePatterns string
     */
    public void setNamePatterns(List<String> namePatterns)
    {
        this.namePatterns = namePatterns;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataClassProperties{" +
                "displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", classCode='" + classCode + '\'' +
                ", userDefined=" + userDefined +
                ", namespace='" + namespace + '\'' +
                ", specification='" + specification + '\'' +
                ", specificationDetails=" + specificationDetails +
                ", dataType='" + dataType + '\'' +
                ", matchThreshold=" + matchThreshold +
                ", allowsDuplicateValues=" + allowsDuplicateValues +
                ", defaultValue='" + defaultValue + '\'' +
                ", sampleValues=" + sampleValues +
                ", dataPatterns=" + dataPatterns +
                ", namePatterns=" + namePatterns +
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
        DataClassProperties that = (DataClassProperties) objectToCompare;
        return userDefined == that.userDefined &&
                allowsDuplicateValues == that.allowsDuplicateValues &&
                Objects.equals(displayName, that.displayName) &&
                Objects.equals(description, that.description) &&
                Objects.equals(classCode, that.classCode) &&
                Objects.equals(namespace, that.namespace) &&
                Objects.equals(specification, that.specification) &&
                Objects.equals(specificationDetails, that.specificationDetails) &&
                Objects.equals(dataType, that.dataType) &&
                Objects.equals(matchThreshold, that.matchThreshold) &&
                Objects.equals(defaultValue, that.defaultValue) &&
                Objects.equals(sampleValues, that.sampleValues) &&
                Objects.equals(dataPatterns, that.dataPatterns) &&
                Objects.equals(namePatterns, that.namePatterns);
    }

    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), displayName, description, classCode, userDefined, namespace,
                            specification, specificationDetails, dataType, matchThreshold, allowsDuplicateValues,
                            defaultValue, sampleValues, dataPatterns, namePatterns);
    }
}
