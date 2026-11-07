/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ReportSpecProperties describes a report type for reports about open lineage and related data.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ReportSpecProperties extends ReportTypeProperties
{
    private String       microflow       = null;
    private String       targetTypeName  = null;
    private List<String> aliases         = null;
    private List<String> outputTypes     = null;
    private Boolean      includePreamble = null;


    /**
     * Default constructor
     */
    public ReportSpecProperties()
    {
        super();
        super.typeName = OpenMetadataType.REPORT_SPEC.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public ReportSpecProperties(ReportSpecProperties template)
    {
        super(template);

        if (template != null)
        {
            microflow       = template.getMicroflow();
            targetTypeName  = template.getTargetTypeName();
            aliases         = template.getAliases();
            outputTypes     = template.getOutputTypes();
            includePreamble = template.getIncludePreamble();
        }
    }


    /**
     * Return the ordered list of stages to run, with their parameters (in JSON).
     *
     * @return string
     */
    public String getMicroflow()
    {
        return microflow;
    }


    /**
     * Set up the ordered list of stages to run, with their parameters (in JSON).
     *
     * @param microflow string
     */
    public void setMicroflow(String microflow)
    {
        this.microflow = microflow;
    }


    /**
     * Return the type of element that is being reported on.
     *
     * @return string
     */
    public String getTargetTypeName()
    {
        return targetTypeName;
    }


    /**
     * Set up the type of element that is being reported on.
     *
     * @param targetTypeName string
     */
    public void setTargetTypeName(String targetTypeName)
    {
        this.targetTypeName = targetTypeName;
    }


    /**
     * Return the alternative names for the report specification.
     *
     * @return list
     */
    public List<String> getAliases()
    {
        return aliases;
    }


    /**
     * Set up the alternative names for the report specification.
     *
     * @param aliases list
     */
    public void setAliases(List<String> aliases)
    {
        this.aliases = aliases;
    }


    /**
     * Return the output types that can be generated from this specification.
     *
     * @return list
     */
    public List<String> getOutputTypes()
    {
        return outputTypes;
    }


    /**
     * Set up the output types that can be generated from this specification.
     *
     * @param outputTypes list
     */
    public void setOutputTypes(List<String> outputTypes)
    {
        this.outputTypes = outputTypes;
    }


    /**
     * Return whether the preamble should be included in the report.
     *
     * @return boolean
     */
    public Boolean getIncludePreamble()
    {
        return includePreamble;
    }


    /**
     * Set up whether the preamble should be included in the report.
     *
     * @param includePreamble boolean
     */
    public void setIncludePreamble(Boolean includePreamble)
    {
        this.includePreamble = includePreamble;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ReportSpecProperties{" +
                "microflow='" + microflow + '\'' +
                ", targetTypeName='" + targetTypeName + '\'' +
                ", aliases=" + aliases +
                ", outputTypes=" + outputTypes +
                ", includePreamble=" + includePreamble +
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
        ReportSpecProperties that = (ReportSpecProperties) objectToCompare;
        return Objects.equals(microflow, that.microflow) &&
                Objects.equals(targetTypeName, that.targetTypeName) &&
                Objects.equals(aliases, that.aliases) &&
                Objects.equals(outputTypes, that.outputTypes) &&
                Objects.equals(includePreamble, that.includePreamble);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), microflow, targetTypeName, aliases, outputTypes, includePreamble);
    }
}
