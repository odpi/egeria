/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.governanceaction.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceActionProcessGraph contains the complete flow of a governance action process.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceActionProcessGraph
{
    private GovernanceActionProcessElement                    governanceActionProcess = null;
    private FirstGovernanceActionProcessStepElement           firstProcessStep        = null;
    private List<GovernanceActionProcessStepExecutionElement> nextProcessSteps        = null;
    private List<NextGovernanceActionProcessStepLink>         processStepLinks        = null;


    /**
     * Default Constructor
     */
    public GovernanceActionProcessGraph()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceActionProcessGraph(GovernanceActionProcessGraph template)
    {
        if (template != null)
        {
            this.governanceActionProcess = template.getGovernanceActionProcess();
            this.firstProcessStep        = template.getFirstProcessStep();
            this.nextProcessSteps        = template.getNextProcessSteps();
            this.processStepLinks        = template.getProcessStepLinks();
        }
    }


    /**
     * Return the descriptive information for a governance action process.
     *
     * @return element
     */
    public GovernanceActionProcessElement getGovernanceActionProcess()
    {
        return governanceActionProcess;
    }


    /**
     * Set up descriptive information for a governance action process.
     *
     * @param governanceActionProcess element
     */
    public void setGovernanceActionProcess(GovernanceActionProcessElement governanceActionProcess)
    {
        this.governanceActionProcess = governanceActionProcess;
    }


    /**
     * Return the first process step
     *
     * @return process step
     */
    public FirstGovernanceActionProcessStepElement getFirstProcessStep()
    {
        return firstProcessStep;
    }


    /**
     * Set up the first process step.
     *
     * @param firstProcessStep first process step
     */
    public void setFirstProcessStep(FirstGovernanceActionProcessStepElement firstProcessStep)
    {
        this.firstProcessStep = firstProcessStep;
    }


    /**
     * Return the list of process steps in the process.
     *
     * @return list of step elements
     */
    public List<GovernanceActionProcessStepExecutionElement> getNextProcessSteps()
    {
        return nextProcessSteps;
    }


    /**
     * Set up the list of process steps in the process.
     *
     * @param nextProcessSteps list of step elements
     */
    public void setNextProcessSteps(List<GovernanceActionProcessStepExecutionElement> nextProcessSteps)
    {
        this.nextProcessSteps = nextProcessSteps;
    }


    /**
     * Return the list of links between process steps.
     *
     * @return list of step links
     */
    public List<NextGovernanceActionProcessStepLink> getProcessStepLinks()
    {
        return processStepLinks;
    }


    /**
     * Set up the list of links between process steps.
     *
     * @param processStepLinks list of step links
     */
    public void setProcessStepLinks(List<NextGovernanceActionProcessStepLink> processStepLinks)
    {
        this.processStepLinks = processStepLinks;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GovernanceActionProcessGraph{" +
                "governanceActionProcess=" + governanceActionProcess +
                ", firstProcessStep" + firstProcessStep +
                ", NextProcessSteps=" + nextProcessSteps +
                ", processStepLinks=" + processStepLinks +
                '}';
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
        GovernanceActionProcessGraph that = (GovernanceActionProcessGraph) objectToCompare;
        return Objects.equals(governanceActionProcess, that.governanceActionProcess) &&
                Objects.equals(firstProcessStep, that.firstProcessStep) &&
                Objects.equals(nextProcessSteps, that.nextProcessSteps) &&
                Objects.equals(processStepLinks, that.processStepLinks);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(governanceActionProcess, firstProcessStep, nextProcessSteps, processStepLinks);
    }
}
