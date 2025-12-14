/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AuthoredReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Defines the root structure for a data structure within a data specification.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataStructureProperties extends AuthoredReferenceableProperties
{
    private String       namespace         = null;
    private List<String> namePatterns      = null;



    /**
     * Default constructor
     */
    public DataStructureProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.DATA_STRUCTURE.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DataStructureProperties(DataStructureProperties template)
    {
        super(template);

        if (template != null)
        {
            namespace         = template.getNamespace();
            namePatterns      = template.getNamePatterns();
        }
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
     * Return a list of name patterns to use when generating schemas.  Use space separated capitalized works so the
     * schema generators can easily convert to valid language keywords.
     *
     * @return string
     */
    public List<String> getNamePatterns()
    {
        return namePatterns;
    }


    /**
     * Set up a list of name patterns to use when generating schemas.  Use space separated capitalized works so the
     * schema generators can easily convert to valid language keywords.
     *
     * @param namePatterns string
     */
    public void setNamePatterns(List<String> namePatterns)
    {
        this.namePatterns = namePatterns;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DataStructureProperties{" +
                "namespace='" + namespace + '\'' +
                ", namePatterns=" + namePatterns +
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
        DataStructureProperties that = (DataStructureProperties) objectToCompare;
        return Objects.equals(namespace, that.namespace) &&
                Objects.equals(namePatterns, that.namePatterns);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), namespace, namePatterns);
    }
}
