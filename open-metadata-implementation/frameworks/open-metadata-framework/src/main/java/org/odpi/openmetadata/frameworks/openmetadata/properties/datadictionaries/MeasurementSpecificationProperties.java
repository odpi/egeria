/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * MeasurementSpecificationProperties describes a measurement that is part of a report.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class MeasurementSpecificationProperties extends DataValueSpecificationProperties
{
    private String              completenessDeclared = null;
    private String              confidenceForm       = null;
    private String              uncertaintyForm      = null;
    private Map<String, String> absenceSentences     = null;


    /**
     * Default constructor
     */
    public MeasurementSpecificationProperties()
    {
        super();
        super.typeName = OpenMetadataType.MEASUREMENT_SPECIFICATION.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public MeasurementSpecificationProperties(MeasurementSpecificationProperties template)
    {
        super(template);

        if (template != null)
        {
            completenessDeclared = template.getCompletenessDeclared();
            confidenceForm       = template.getConfidenceForm();
            uncertaintyForm      = template.getUncertaintyForm();
            absenceSentences     = template.getAbsenceSentences();
        }
    }


    /**
     * Return whether a measured value should count everything or may undercount (complete, lower_bound, unknown).
     *
     * @return string
     */
    public String getCompletenessDeclared()
    {
        return completenessDeclared;
    }


    /**
     * Set up whether a measured value should count everything or may undercount (complete, lower_bound, unknown).
     *
     * @param completenessDeclared string
     */
    public void setCompletenessDeclared(String completenessDeclared)
    {
        this.completenessDeclared = completenessDeclared;
    }


    /**
     * Return how the value is produced (measured, estimated, inferred, judged, generated).
     *
     * @return string
     */
    public String getConfidenceForm()
    {
        return confidenceForm;
    }


    /**
     * Set up how the value is produced (measured, estimated, inferred, judged, generated).
     *
     * @param confidenceForm string
     */
    public void setConfidenceForm(String confidenceForm)
    {
        this.confidenceForm = confidenceForm;
    }


    /**
     * Return how to read the absoluteUncertainty / relativeUncertainty values (plus_minus, low_high, band).
     *
     * @return string
     */
    public String getUncertaintyForm()
    {
        return uncertaintyForm;
    }


    /**
     * Set up how to read the absoluteUncertainty / relativeUncertainty values (plus_minus, low_high, band).
     *
     * @param uncertaintyForm string
     */
    public void setUncertaintyForm(String uncertaintyForm)
    {
        this.uncertaintyForm = uncertaintyForm;
    }


    /**
     * Return the map from an absence state (nothing_found, not_measured, not_permitted, not_applicable) to the sentence to show in place of a value.
     *
     * @return map
     */
    public Map<String, String> getAbsenceSentences()
    {
        return absenceSentences;
    }


    /**
     * Set up the map from an absence state (nothing_found, not_measured, not_permitted, not_applicable) to the sentence to show in place of a value.
     *
     * @param absenceSentences map
     */
    public void setAbsenceSentences(Map<String, String> absenceSentences)
    {
        this.absenceSentences = absenceSentences;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "MeasurementSpecificationProperties{" +
                "completenessDeclared='" + completenessDeclared + '\'' +
                ", confidenceForm='" + confidenceForm + '\'' +
                ", uncertaintyForm='" + uncertaintyForm + '\'' +
                ", absenceSentences=" + absenceSentences +
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
        MeasurementSpecificationProperties that = (MeasurementSpecificationProperties) objectToCompare;
        return Objects.equals(completenessDeclared, that.completenessDeclared) &&
                Objects.equals(confidenceForm, that.confidenceForm) &&
                Objects.equals(uncertaintyForm, that.uncertaintyForm) &&
                Objects.equals(absenceSentences, that.absenceSentences);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), completenessDeclared, confidenceForm, uncertaintyForm, absenceSentences);
    }
}
