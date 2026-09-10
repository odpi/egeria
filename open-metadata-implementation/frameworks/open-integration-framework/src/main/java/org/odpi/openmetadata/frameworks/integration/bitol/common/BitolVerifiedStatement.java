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
 * This class represents a canonical question, optionally with its verified answer, in the AI context of a data contract, schema object, data product or output port (RFC 0038).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class BitolVerifiedStatement
{
    private String                             id = null;
    private String                             question = null;
    private String                             answer = null;
    private List<BitolAuthoritativeDefinition> authoritativeDefinitions = null;
    private List<String>                       tags = null;
    private List<BitolCustomProperty>          customProperties = null;


    /**
     * Default constructor
     */
    public BitolVerifiedStatement()
    {
    }


    /**
     * Return the stable identifier of the statement.
     *
     * @return String
     */
    public String getId()
    {
        return id;
    }


    /**
     * Set up the stable identifier of the statement.
     *
     * @param id String
     */
    public void setId(String id)
    {
        this.id = id;
    }


    /**
     * Return the canonical question.
     *
     * @return String
     */
    public String getQuestion()
    {
        return question;
    }


    /**
     * Set up the canonical question.
     *
     * @param question String
     */
    public void setQuestion(String question)
    {
        this.question = question;
    }


    /**
     * Return the expected response or result description; absent for an unanswered sample question.
     *
     * @return String
     */
    public String getAnswer()
    {
        return answer;
    }


    /**
     * Set up the expected response or result description; absent for an unanswered sample question.
     *
     * @param answer String
     */
    public void setAnswer(String answer)
    {
        this.answer = answer;
    }


    /**
     * Return links to the sources behind this statement.
     *
     * @return List<BitolAuthoritativeDefinition>
     */
    public List<BitolAuthoritativeDefinition> getAuthoritativeDefinitions()
    {
        return authoritativeDefinitions;
    }


    /**
     * Set up links to the sources behind this statement.
     *
     * @param authoritativeDefinitions List<BitolAuthoritativeDefinition>
     */
    public void setAuthoritativeDefinitions(List<BitolAuthoritativeDefinition> authoritativeDefinitions)
    {
        this.authoritativeDefinitions = authoritativeDefinitions;
    }


    /**
     * Return the tags attached to this statement.
     *
     * @return List<String>
     */
    public List<String> getTags()
    {
        return tags;
    }


    /**
     * Set up the tags attached to this statement.
     *
     * @param tags List<String>
     */
    public void setTags(List<String> tags)
    {
        this.tags = tags;
    }


    /**
     * Return the custom properties of this statement.
     *
     * @return List<BitolCustomProperty>
     */
    public List<BitolCustomProperty> getCustomProperties()
    {
        return customProperties;
    }


    /**
     * Set up the custom properties of this statement.
     *
     * @param customProperties List<BitolCustomProperty>
     */
    public void setCustomProperties(List<BitolCustomProperty> customProperties)
    {
        this.customProperties = customProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "BitolVerifiedStatement{" +
                       "id=" + id +
                       ", question=" + question +
                       ", answer=" + answer +
                       ", authoritativeDefinitions=" + authoritativeDefinitions +
                       ", tags=" + tags +
                       ", customProperties=" + customProperties +
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
        BitolVerifiedStatement that = (BitolVerifiedStatement) objectToCompare;
        return Objects.equals(id, that.id) &&
                       Objects.equals(question, that.question) &&
                       Objects.equals(answer, that.answer) &&
                       Objects.equals(authoritativeDefinitions, that.authoritativeDefinitions) &&
                       Objects.equals(tags, that.tags) &&
                       Objects.equals(customProperties, that.customProperties);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(id, question, answer, authoritativeDefinitions, tags, customProperties);
    }
}
