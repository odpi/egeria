/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.designpatterns;

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
 * DesignPatternProperties provides a class for the properties of a design pattern.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DesignPatternProperties extends AuthoredReferenceableProperties
{
    private String       context             = null;
    private List<String> forces              = null;
    private String       problemStatement    = null;
    private String       problemExample      = null;
    private String       solutionDescription = null;
    private String       solutionExample     = null;
    private List<String> benefits            = null;
    private List<String> liabilities         = null;
    private String       usage               = null;


    public DesignPatternProperties()
    {
        super();
        super.typeName = OpenMetadataType.DESIGN_PATTERN.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DesignPatternProperties(DesignPatternProperties template)
    {
        super(template);

        if (template != null)
        {
            context             = template.getContext();
            forces              = template.getForces();
            problemStatement    = template.getProblemStatement();
            problemExample      = template.getProblemExample();
            solutionDescription = template.getSolutionDescription();
            solutionExample     = template.getSolutionExample();
            benefits            = template.getBenefits();
            liabilities         = template.getLiabilities();
            usage               = template.getUsage();
        }
    }

    /**
     * Return the context for this design pattern.
     *
     * @return string
     */
    public String getContext()
    {
        return context;
    }


    /**
     * Set up the context for this design pattern.
     *
     * @param context string
     */
    public void setContext(String context)
    {
        this.context = context;
    }


    /**
     * Return the forces that drive the use of this design pattern.
     *
     * @return list of strings
     */
    public List<String> getForces()
    {
        return forces;
    }


    /**
     * Set up the forces that drive the use of this design pattern.
     *
     * @param forces list of strings
     */
    public void setForces(List<String> forces)
    {
        this.forces = forces;
    }


    /**
     * Return the problem statement for this design pattern.
     *
     * @return string
     */
    public String getProblemStatement()
    {
        return problemStatement;
    }


    /**
     * Set up the problem statement for this design pattern.
     *
     * @param problemStatement string
     */
    public void setProblemStatement(String problemStatement)
    {
        this.problemStatement = problemStatement;
    }


    /**
     * Return the problem example for this design pattern.
     *
     * @return string
     */
    public String getProblemExample()
    {
        return problemExample;
    }


    /**
     * Set up the problem example for this design pattern.
     *
     * @param problemExample string
     */
    public void setProblemExample(String problemExample)
    {
        this.problemExample = problemExample;
    }


    /**
     * Return the solution description for this design pattern.
     *
     * @return string
     */
    public String getSolutionDescription()
    {
        return solutionDescription;
    }


    /**
     * Set up the solution description for this design pattern.
     *
     * @param solutionDescription string
     */
    public void setSolutionDescription(String solutionDescription)
    {
        this.solutionDescription = solutionDescription;
    }


    /**
     * Return the solution example for this design pattern.
     *
     * @return string
     */
    public String getSolutionExample()
    {
        return solutionExample;
    }


    /**
     * Set up the solution example for this design pattern.
     *
     * @param solutionExample string
     */
    public void setSolutionExample(String solutionExample)
    {
        this.solutionExample = solutionExample;
    }


    /**
     * Return the benefits of using this design pattern.
     *
     * @return list of strings
     */
    public List<String> getBenefits()
    {
        return benefits;
    }


    /**
     * Set up the benefits of using this design pattern.
     *
     * @param benefits list of strings
     */
    public void setBenefits(List<String> benefits)
    {
        this.benefits = benefits;
    }


    /**
     * Return the liabilities of using this design pattern.
     *
     * @return list of strings
     */
    public List<String> getLiabilities()
    {
        return liabilities;
    }


    /**
     * Set up the liabilities of using this design pattern.
     *
     * @param liabilities list of strings
     */
    public void setLiabilities(List<String> liabilities)
    {
        this.liabilities = liabilities;
    }


    /**
     * Return the known usage of this design pattern.
     *
     * @return string
     */
    public String getUsage()
    {
        return usage;
    }


    /**
     * Set up the known usage of this design pattern.
     *
     * @param usage string
     */
    public void setUsage(String usage)
    {
        this.usage = usage;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DesignPatternProperties{" +
                "context='" + context + '\'' +
                ", forces=" + forces +
                ", problemStatement='" + problemStatement + '\'' +
                ", problemExample='" + problemExample + '\'' +
                ", solutionDescription='" + solutionDescription + '\'' +
                ", solutionExample='" + solutionExample + '\'' +
                ", benefits=" + benefits +
                ", liabilities=" + liabilities +
                ", usage='" + usage + '\'' +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        DesignPatternProperties that = (DesignPatternProperties) objectToCompare;
        return Objects.equals(context, that.context) && Objects.equals(forces, that.forces) && Objects.equals(problemStatement, that.problemStatement) && Objects.equals(problemExample, that.problemExample) && Objects.equals(solutionDescription, that.solutionDescription) && Objects.equals(solutionExample, that.solutionExample) && Objects.equals(benefits, that.benefits) && Objects.equals(liabilities, that.liabilities) && Objects.equals(usage, that.usage);
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), context, forces, problemStatement, problemExample, solutionDescription, solutionExample, benefits, liabilities, usage);
    }
}
