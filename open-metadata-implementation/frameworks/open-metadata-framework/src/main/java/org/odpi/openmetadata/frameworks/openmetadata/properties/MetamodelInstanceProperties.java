/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * MetamodelInstanceProperties describes the properties for the MetamodelInstance classification that links an element to
 * the element in a metamodel that it is an instance of.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class MetamodelInstanceProperties extends ClassificationBeanProperties
{
    private String metamodelElementGUID = null;


    /**
     * Default constructor
     */
    public MetamodelInstanceProperties()
    {
        super();
        super.typeName = OpenMetadataType.METAMODEL_INSTANCE_CLASSIFICATION.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public MetamodelInstanceProperties(MetamodelInstanceProperties template)
    {
        super(template);

        if (template != null)
        {
            metamodelElementGUID = template.getMetamodelElementGUID();
        }
    }


    /**
     * Return the unique identifier of the element in the metamodel that this element is an instance of.
     *
     * @return string guid
     */
    public String getMetamodelElementGUID()
    {
        return metamodelElementGUID;
    }


    /**
     * Set up the unique identifier of the element in the metamodel that this element is an instance of.
     *
     * @param metamodelElementGUID string guid
     */
    public void setMetamodelElementGUID(String metamodelElementGUID)
    {
        this.metamodelElementGUID = metamodelElementGUID;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "MetamodelInstanceProperties{" +
                "metamodelElementGUID='" + metamodelElementGUID + '\'' +
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        MetamodelInstanceProperties that = (MetamodelInstanceProperties) objectToCompare;
        return Objects.equals(metamodelElementGUID, that.metamodelElementGUID);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), metamodelElementGUID);
    }
}
