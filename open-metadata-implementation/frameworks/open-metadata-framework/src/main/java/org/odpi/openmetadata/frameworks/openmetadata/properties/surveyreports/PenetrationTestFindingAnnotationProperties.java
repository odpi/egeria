/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PenetrationTestFindingAnnotationProperties describes a weakness that a penetration test was able to exploit, with how it was exploited and what was gained.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PenetrationTestFindingAnnotationProperties extends SecurityFindingAnnotationProperties
{
    private String       attackVector         = null;
    private List<String> attackTechniques     = null;
    private String       privilegesGained     = null;
    private List<String> compromisedResources = null;
    private String       reproductionSteps    = null;


    /**
     * Default constructor
     */
    public PenetrationTestFindingAnnotationProperties()
    {
        super();
        super.typeName = OpenMetadataType.PENETRATION_TEST_FINDING_ANNOTATION.typeName;
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public PenetrationTestFindingAnnotationProperties(PenetrationTestFindingAnnotationProperties template)
    {
        super(template);

        if (template != null)
        {
            this.attackVector         = template.getAttackVector();
            this.attackTechniques     = template.getAttackTechniques();
            this.privilegesGained     = template.getPrivilegesGained();
            this.compromisedResources = template.getCompromisedResources();
            this.reproductionSteps    = template.getReproductionSteps();
        }
    }


    /**
     * Return how the weakness was reached - for example Network, Adjacent network, Local, Physical or Social engineering.
     *
     * @return String
     */
    public String getAttackVector()
    {
        return attackVector;
    }


    /**
     * Set up how the weakness was reached - for example Network, Adjacent network, Local, Physical or Social engineering.
     *
     * @param attackVector String
     */
    public void setAttackVector(String attackVector)
    {
        this.attackVector = attackVector;
    }


    /**
     * Return the techniques used to exploit the weakness, such as MITRE ATT&amp;CK technique identifiers.
     *
     * @return list
     */
    public List<String> getAttackTechniques()
    {
        return attackTechniques;
    }


    /**
     * Set up the techniques used to exploit the weakness, such as MITRE ATT&amp;CK technique identifiers.
     *
     * @param attackTechniques list
     */
    public void setAttackTechniques(List<String> attackTechniques)
    {
        this.attackTechniques = attackTechniques;
    }


    /**
     * Return the access or privileges that the tester gained by exploiting the weakness.
     *
     * @return String
     */
    public String getPrivilegesGained()
    {
        return privilegesGained;
    }


    /**
     * Set up the access or privileges that the tester gained by exploiting the weakness.
     *
     * @param privilegesGained String
     */
    public void setPrivilegesGained(String privilegesGained)
    {
        this.privilegesGained = privilegesGained;
    }


    /**
     * Return the names of the resources that the tester was able to reach or control by exploiting the weakness.
     * If these resources are catalogued, the annotation can also be linked to each of them with the
     * AssociatedAnnotation relationship.
     *
     * @return list
     */
    public List<String> getCompromisedResources()
    {
        return compromisedResources;
    }


    /**
     * Set up the names of the resources that the tester was able to reach or control by exploiting the weakness.
     * If these resources are catalogued, the annotation can also be linked to each of them with the
     * AssociatedAnnotation relationship.
     *
     * @param compromisedResources list
     */
    public void setCompromisedResources(List<String> compromisedResources)
    {
        this.compromisedResources = compromisedResources;
    }


    /**
     * Return the steps to reproduce the finding.
     *
     * @return String
     */
    public String getReproductionSteps()
    {
        return reproductionSteps;
    }


    /**
     * Set up the steps to reproduce the finding.
     *
     * @param reproductionSteps String
     */
    public void setReproductionSteps(String reproductionSteps)
    {
        this.reproductionSteps = reproductionSteps;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "PenetrationTestFindingAnnotationProperties{" +
                "attackVector='" + attackVector + '\'' +
                ", attackTechniques=" + attackTechniques +
                ", privilegesGained='" + privilegesGained + '\'' +
                ", compromisedResources=" + compromisedResources +
                ", reproductionSteps='" + reproductionSteps + '\'' +
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
        PenetrationTestFindingAnnotationProperties that = (PenetrationTestFindingAnnotationProperties) objectToCompare;
        return Objects.equals(attackVector, that.attackVector) &&
                Objects.equals(attackTechniques, that.attackTechniques) &&
                Objects.equals(privilegesGained, that.privilegesGained) &&
                Objects.equals(compromisedResources, that.compromisedResources) &&
                Objects.equals(reproductionSteps, that.reproductionSteps);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), attackVector, attackTechniques, privilegesGained, compromisedResources, reproductionSteps);
    }
}
