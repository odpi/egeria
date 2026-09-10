/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.common;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the AI and semantic context block of a data contract, schema object, data product or output port (ODCS v3.2.0 and ODPS v1.1.0, RFC 0038).  It gives AI agents, LLMs and semantic layer tools instructions on how to use the element, verified questions and answers, and constraints on what must not be done.  In ODCS the block may be written as a plain string, which is equivalent to supplying the instructions alone.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class BitolContext
{
    private String                       instructions = null;
    private List<BitolVerifiedStatement> verifiedStatements = null;
    private List<BitolContextConstraint> constraints = null;


    /**
     * Default constructor
     */
    public BitolContext()
    {
    }


    /**
     * Return natural language guidance for AI agents and tools on how to use the element.
     *
     * @return String
     */
    public String getInstructions()
    {
        return instructions;
    }


    /**
     * Set up natural language guidance for AI agents and tools on how to use the element.
     *
     * @param instructions String
     */
    public void setInstructions(String instructions)
    {
        this.instructions = instructions;
    }


    /**
     * Return the canonical questions and verified answers.
     *
     * @return List<BitolVerifiedStatement>
     */
    public List<BitolVerifiedStatement> getVerifiedStatements()
    {
        return verifiedStatements;
    }


    /**
     * Set up the canonical questions and verified answers.
     *
     * @param verifiedStatements List<BitolVerifiedStatement>
     */
    public void setVerifiedStatements(List<BitolVerifiedStatement> verifiedStatements)
    {
        this.verifiedStatements = verifiedStatements;
    }


    /**
     * Return the things AI agents must not do with the element.
     *
     * @return List<BitolContextConstraint>
     */
    public List<BitolContextConstraint> getConstraints()
    {
        return constraints;
    }


    /**
     * Set up the things AI agents must not do with the element.
     *
     * @param constraints List<BitolContextConstraint>
     */
    public void setConstraints(List<BitolContextConstraint> constraints)
    {
        this.constraints = constraints;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "BitolContext{" +
                       "instructions=" + instructions +
                       ", verifiedStatements=" + verifiedStatements +
                       ", constraints=" + constraints +
                       '}';
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
        BitolContext that = (BitolContext) objectToCompare;
        return Objects.equals(instructions, that.instructions) &&
                       Objects.equals(verifiedStatements, that.verifiedStatements) &&
                       Objects.equals(constraints, that.constraints);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(instructions, verifiedStatements, constraints);
    }
}
