/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.solutions;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.LabeledRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SolutionLinkingWireProperties identifies a relationship between solution components that is part of an information supply chain segment implementation.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SolutionLinkingWireProperties extends LabeledRelationshipProperties
{
    private List<String> iscQualifiedNames = null;


    /**
     * Default constructor
     */
    public SolutionLinkingWireProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.SOLUTION_LINKING_WIRE_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SolutionLinkingWireProperties(SolutionLinkingWireProperties template)
    {
        super(template);

        if (template != null)
        {
            this.iscQualifiedNames = template.getISCQualifiedNames();
        }
    }


    /**
     * Return the information supply chain segments that his wire implements.
     *
     * @return list
     */
    public List<String> getISCQualifiedNames()
    {
        return iscQualifiedNames;
    }


    /**
     * Set up the information supply chain segments that his wire implements.
     *
     * @param iscQualifiedNames list
     */
    public void setISCQualifiedNames(List<String> iscQualifiedNames)
    {
        this.iscQualifiedNames = iscQualifiedNames;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SolutionLinkingWireProperties{" +
                "iscQualifiedNames=" + iscQualifiedNames +
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
        if (! (objectToCompare instanceof SolutionLinkingWireProperties that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(iscQualifiedNames, that.iscQualifiedNames);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), iscQualifiedNames);
    }
}
