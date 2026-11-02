/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SecurityFindingAnnotationProperties describes a weakness found in a resource by a security test, such as a vulnerability scan or a penetration test.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
              include = JsonTypeInfo.As.PROPERTY,
              property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = VulnerabilityAnnotationProperties.class, name = "VulnerabilityAnnotationProperties"),
                @JsonSubTypes.Type(value = PenetrationTestFindingAnnotationProperties.class, name = "PenetrationTestFindingAnnotationProperties"),
        })
public class SecurityFindingAnnotationProperties extends AnnotationProperties
{
    private String       severity                 = null;
    private Float        cvssScore                = null;
    private String       cvssVector               = null;
    private List<String> vulnerabilityIdentifiers = null;
    private List<String> weaknessIdentifiers      = null;
    private String       affectedLocation         = null;
    private String       evidence                 = null;
    private String       remediation              = null;
    private Boolean      exploitAvailable         = null;


    /**
     * Default constructor
     */
    public SecurityFindingAnnotationProperties()
    {
        super();
        super.typeName = OpenMetadataType.SECURITY_FINDING_ANNOTATION.typeName;
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public SecurityFindingAnnotationProperties(SecurityFindingAnnotationProperties template)
    {
        super(template);

        if (template != null)
        {
            this.severity                 = template.getSeverity();
            this.cvssScore                = template.getCvssScore();
            this.cvssVector               = template.getCvssVector();
            this.vulnerabilityIdentifiers = template.getVulnerabilityIdentifiers();
            this.weaknessIdentifiers      = template.getWeaknessIdentifiers();
            this.affectedLocation         = template.getAffectedLocation();
            this.evidence                 = template.getEvidence();
            this.remediation              = template.getRemediation();
            this.exploitAvailable         = template.getExploitAvailable();
        }
    }


    /**
     * Return the qualitative severity of the finding.  For security findings, use the CVSS ratings: None, Low, Medium, High or Critical.
     *
     * @return String
     */
    public String getSeverity()
    {
        return severity;
    }


    /**
     * Set up the qualitative severity of the finding.  For security findings, use the CVSS ratings: None, Low, Medium, High or Critical.
     *
     * @param severity String
     */
    public void setSeverity(String severity)
    {
        this.severity = severity;
    }


    /**
     * Return the Common Vulnerability Scoring System (CVSS) base score for the finding, from 0.0 to 10.0.
     *
     * @return float
     */
    public Float getCvssScore()
    {
        return cvssScore;
    }


    /**
     * Set up the Common Vulnerability Scoring System (CVSS) base score for the finding, from 0.0 to 10.0.
     *
     * @param cvssScore float
     */
    public void setCvssScore(Float cvssScore)
    {
        this.cvssScore = cvssScore;
    }


    /**
     * Return the CVSS vector string that shows how the score was calculated.
     *
     * @return String
     */
    public String getCvssVector()
    {
        return cvssVector;
    }


    /**
     * Set up the CVSS vector string that shows how the score was calculated.
     *
     * @param cvssVector String
     */
    public void setCvssVector(String cvssVector)
    {
        this.cvssVector = cvssVector;
    }


    /**
     * Return the identifiers of the publicly disclosed vulnerabilities behind the finding, such as CVE identifiers.
     *
     * @return list
     */
    public List<String> getVulnerabilityIdentifiers()
    {
        return vulnerabilityIdentifiers;
    }


    /**
     * Set up the identifiers of the publicly disclosed vulnerabilities behind the finding, such as CVE identifiers.
     *
     * @param vulnerabilityIdentifiers list
     */
    public void setVulnerabilityIdentifiers(List<String> vulnerabilityIdentifiers)
    {
        this.vulnerabilityIdentifiers = vulnerabilityIdentifiers;
    }


    /**
     * Return the identifiers of the types of weakness behind the finding, such as CWE identifiers.
     *
     * @return list
     */
    public List<String> getWeaknessIdentifiers()
    {
        return weaknessIdentifiers;
    }


    /**
     * Set up the identifiers of the types of weakness behind the finding, such as CWE identifiers.
     *
     * @param weaknessIdentifiers list
     */
    public void setWeaknessIdentifiers(List<String> weaknessIdentifiers)
    {
        this.weaknessIdentifiers = weaknessIdentifiers;
    }


    /**
     * Return where the finding was detected - for example a host and port, a URL, a file path or a software package.
     * If the element at this location is catalogued, the annotation can also be linked to it with the
     * AssociatedAnnotation relationship.
     *
     * @return String
     */
    public String getAffectedLocation()
    {
        return affectedLocation;
    }


    /**
     * Set up where the finding was detected - for example a host and port, a URL, a file path or a software package.
     * If the element at this location is catalogued, the annotation can also be linked to it with the
     * AssociatedAnnotation relationship.
     *
     * @param affectedLocation String
     */
    public void setAffectedLocation(String affectedLocation)
    {
        this.affectedLocation = affectedLocation;
    }


    /**
     * Return the evidence that supports the finding, such as the request and response that demonstrated it.
     *
     * @return String
     */
    public String getEvidence()
    {
        return evidence;
    }


    /**
     * Set up the evidence that supports the finding, such as the request and response that demonstrated it.
     *
     * @param evidence String
     */
    public void setEvidence(String evidence)
    {
        this.evidence = evidence;
    }


    /**
     * Return the recommended action to remove the weakness or reduce its impact.
     *
     * @return String
     */
    public String getRemediation()
    {
        return remediation;
    }


    /**
     * Set up the recommended action to remove the weakness or reduce its impact.
     *
     * @param remediation String
     */
    public void setRemediation(String remediation)
    {
        this.remediation = remediation;
    }


    /**
     * Return whether an exploit for the weakness is publicly available.
     *
     * @return boolean
     */
    public Boolean getExploitAvailable()
    {
        return exploitAvailable;
    }


    /**
     * Set up whether an exploit for the weakness is publicly available.
     *
     * @param exploitAvailable boolean
     */
    public void setExploitAvailable(Boolean exploitAvailable)
    {
        this.exploitAvailable = exploitAvailable;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "SecurityFindingAnnotationProperties{" +
                "severity='" + severity + '\'' +
                ", cvssScore=" + cvssScore +
                ", cvssVector='" + cvssVector + '\'' +
                ", vulnerabilityIdentifiers=" + vulnerabilityIdentifiers +
                ", weaknessIdentifiers=" + weaknessIdentifiers +
                ", affectedLocation='" + affectedLocation + '\'' +
                ", evidence='" + evidence + '\'' +
                ", remediation='" + remediation + '\'' +
                ", exploitAvailable=" + exploitAvailable +
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
        SecurityFindingAnnotationProperties that = (SecurityFindingAnnotationProperties) objectToCompare;
        return Objects.equals(severity, that.severity) &&
                Objects.equals(cvssScore, that.cvssScore) &&
                Objects.equals(cvssVector, that.cvssVector) &&
                Objects.equals(vulnerabilityIdentifiers, that.vulnerabilityIdentifiers) &&
                Objects.equals(weaknessIdentifiers, that.weaknessIdentifiers) &&
                Objects.equals(affectedLocation, that.affectedLocation) &&
                Objects.equals(evidence, that.evidence) &&
                Objects.equals(remediation, that.remediation) &&
                Objects.equals(exploitAvailable, that.exploitAvailable);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), severity, cvssScore, cvssVector, vulnerabilityIdentifiers, weaknessIdentifiers, affectedLocation, evidence, remediation, exploitAvailable);
    }
}
