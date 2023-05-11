/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossarybrowser.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryTermRelationshipStatus;

import java.io.Serial;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * GlossaryTermRelationshipRequestBody is the request body structure used on Glossary REST API calls that
 * request the retrieval of glossary term relationships.  These relationships have a status field
 * that can be used to filter the request.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GlossaryTermRelationshipRequestBody extends EffectiveTimeQueryRequestBody
{
    @Serial
    private static final long serialVersionUID = 1L;

    private String                               glossaryGUID         = null;
    private List<GlossaryTermRelationshipStatus> limitResultsByStatus = null;

    /**
     * Default constructor
     */
    public GlossaryTermRelationshipRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GlossaryTermRelationshipRequestBody(GlossaryTermRelationshipRequestBody template)
    {
        super(template);

        if (template != null)
        {
            glossaryGUID = template.getGlossaryGUID();
            limitResultsByStatus = template.getLimitResultsByStatus();
        }
    }


    /**
     * Return the unique identifier of the glossary scope.
     *
     * @return string guid
     */
    public String getGlossaryGUID()
    {
        return glossaryGUID;
    }


    /**
     * Set up the unique identifier of the glossary scope.
     *
     * @param glossaryGUID string
     */
    public void setGlossaryGUID(String glossaryGUID)
    {
        this.glossaryGUID = glossaryGUID;
    }


    /**
     * Return the list of statuses to return (null for all).
     *
     * @return list of statuses (terms only)
     */
    public List<GlossaryTermRelationshipStatus> getLimitResultsByStatus()
    {
        return limitResultsByStatus;
    }


    /**
     * Set up the list of statuses to return (null for all).
     *
     * @param limitResultsByStatus list of statuses (terms only)
     */
    public void setLimitResultsByStatus(List<GlossaryTermRelationshipStatus> limitResultsByStatus)
    {
        this.limitResultsByStatus = limitResultsByStatus;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "GlossaryTermRelationshipRequestBody{" +
                       "glossaryGUID='" + glossaryGUID + '\'' +
                       ", limitResultsByStatus=" + limitResultsByStatus +
                       ", effectiveTime=" + getEffectiveTime() + '\'' +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        GlossaryTermRelationshipRequestBody that = (GlossaryTermRelationshipRequestBody) objectToCompare;
        return Objects.equals(glossaryGUID, that.glossaryGUID) &&
                Objects.equals(limitResultsByStatus, that.limitResultsByStatus);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), glossaryGUID, limitResultsByStatus);
    }
}
