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
 * OpenMetadataRootHierarchy contains the properties, header and nested members of an element retrieved
 * from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataRootHierarchy extends OpenMetadataRootElement
{
    private List<OpenMetadataRootHierarchy> openMetadataRootHierarchies = null;


    /**
     * Default constructor
     */
    public OpenMetadataRootHierarchy()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public OpenMetadataRootHierarchy(OpenMetadataRootHierarchy template)
    {
        super(template);

        if (template != null)
        {
            openMetadataRootHierarchies = template.getOpenMetadataRootHierarchies();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public OpenMetadataRootHierarchy(OpenMetadataRootElement template)
    {
        super(template);
    }



    /**
     * Return the members of the collection, organized hierarchically.
     *
     * @return hierarchy of members
     */
    public List<OpenMetadataRootHierarchy> getOpenMetadataRootHierarchies()
    {
        return openMetadataRootHierarchies;
    }


    /**
     * Set up the members of the collection, organized hierarchically.
     *
     * @param openMetadataRootHierarchies  list of category hierarchies
     */
    public void setOpenMetadataRootHierarchies(List<OpenMetadataRootHierarchy> openMetadataRootHierarchies)
    {
        this.openMetadataRootHierarchies = openMetadataRootHierarchies;
    }

    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "OpenMetadataRootHierarchy{" +
                "openMetadataRootHierarchies=" + openMetadataRootHierarchies +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        OpenMetadataRootHierarchy that = (OpenMetadataRootHierarchy) objectToCompare;
        return Objects.equals(openMetadataRootHierarchies, that.openMetadataRootHierarchies);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), openMetadataRootHierarchies);
    }
}
