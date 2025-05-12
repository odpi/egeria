/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.omf.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ConsolidatedDuplicatesRequestBody provides a structure for passing the properties associated with consolidated duplicates.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ConsolidatedDuplicatesRequestBody extends StewardshipRequestBody
{
    private String       consolidatedElementGUID = null;
    private List<String> sourceElementGUIDs      = null;


    /**
     * Default constructor
     */
    public ConsolidatedDuplicatesRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ConsolidatedDuplicatesRequestBody(ConsolidatedDuplicatesRequestBody template)
    {
        super(template);

        if (template != null)
        {
            consolidatedElementGUID = template.getConsolidatedElementGUID();
            sourceElementGUIDs = template.getSourceElementGUIDs();
        }
    }


    /**
     * Gets consolidated metadata element guid.
     *
     * @return the metadata element guid
     */
    public String getConsolidatedElementGUID() {
        return consolidatedElementGUID;
    }


    /**
     * Sets consolidated metadata element guid.
     *
     * @param consolidatedElementGUID the metadata element guid
     */
    public void setConsolidatedElementGUID(String consolidatedElementGUID) {
        this.consolidatedElementGUID = consolidatedElementGUID;
    }


    /**
     * Gets the metadata elements that provided the source properties/classifications.
     *
     * @return list of guids
     */
    public List<String> getSourceElementGUIDs() {
        return sourceElementGUIDs;
    }


    /**
     * Sets the metadata elements that provided the source properties/classifications.
     *
     * @param sourceElementGUIDs list of guids
     */
    public void setSourceElementGUIDs(List<String> sourceElementGUIDs) {
        this.sourceElementGUIDs = sourceElementGUIDs;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "ConsolidatedDuplicatesRequestBody{" +
                       "consolidatedElementGUID='" + consolidatedElementGUID + '\'' +
                       ", sourceElementGUIDs=" + sourceElementGUIDs +
                       ", statusIdentifier=" + getStatusIdentifier() +
                       ", steward='" + getSteward() + '\'' +
                       ", stewardTypeName='" + getStewardTypeName() + '\'' +
                       ", stewardPropertyName='" + getStewardPropertyName() + '\'' +
                       ", source='" + getSource() + '\'' +
                       ", notes='" + getNotes() + '\'' +
                       '}';
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
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
        ConsolidatedDuplicatesRequestBody that = (ConsolidatedDuplicatesRequestBody) objectToCompare;
        return Objects.equals(consolidatedElementGUID, that.consolidatedElementGUID) && Objects.equals(sourceElementGUIDs,
                                                                                                       that.sourceElementGUIDs);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), consolidatedElementGUID, sourceElementGUIDs);
    }
}
