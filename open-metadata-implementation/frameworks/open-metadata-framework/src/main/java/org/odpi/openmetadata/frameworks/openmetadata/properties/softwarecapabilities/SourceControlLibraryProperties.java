/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * SourceControlLibraryProperties describes a source control library.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)

public class SourceControlLibraryProperties extends ResourceManagerProperties
{
    private String libraryType = null;


    /**
     * Default constructor
     */
    public SourceControlLibraryProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.SOURCE_CONTROL_LIBRARY_CLASSIFICATION.typeName);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public SourceControlLibraryProperties(SourceControlLibraryProperties template)
    {
        super(template);

        if (template != null)
        {
            libraryType = template.getLibraryType();
        }
    }


    /**
     * Return the format of the file system.
     *
     * @return string name
     */
    public String getLibraryType()
    {
        return libraryType;
    }


    /**
     * Set up the format of the file system.
     *
     * @param libraryType string name
     */
    public void setLibraryType(String libraryType)
    {
        this.libraryType = libraryType;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "SourceControlLibraryProperties{" +
                "libraryType='" + libraryType + '\'' +
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
        SourceControlLibraryProperties that = (SourceControlLibraryProperties) objectToCompare;
        return Objects.equals(libraryType, that.libraryType);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), libraryType);
    }
}
