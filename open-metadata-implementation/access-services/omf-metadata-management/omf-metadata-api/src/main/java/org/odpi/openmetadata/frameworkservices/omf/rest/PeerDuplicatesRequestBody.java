/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.omf.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PeerDuplicatesRequestBody provides a structure for passing the properties associated with duplicates.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PeerDuplicatesRequestBody extends StewardshipRequestBody
{
    private String  metadataElement1GUID = null;
    private String  metadataElement2GUID = null;
    private boolean setKnownDuplicate    = true;


    /**
     * Default constructor
     */
    public PeerDuplicatesRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public PeerDuplicatesRequestBody(PeerDuplicatesRequestBody template)
    {
        super(template);

        if (template != null)
        {
            metadataElement1GUID = template.getMetadataElement1GUID();
            metadataElement2GUID = template.getMetadataElement2GUID();
            setKnownDuplicate = template.getSetKnownDuplicate();
        }
    }


    /**
     * Gets metadata element 1 guid.
     *
     * @return the metadata element 1 guid
     */
    public String getMetadataElement1GUID() {
        return metadataElement1GUID;
    }


    /**
     * Sets metadata element 1 guid.
     *
     * @param metadataElement1GUID the metadata element 1 guid
     */
    public void setMetadataElement1GUID(String metadataElement1GUID) {
        this.metadataElement1GUID = metadataElement1GUID;
    }


    /**
     * Gets metadata element 2 guid.
     *
     * @return the metadata element 2 guid
     */
    public String getMetadataElement2GUID()
    {
        return metadataElement2GUID;
    }


    /**
     * Sets metadata element 2 guid.
     *
     * @param metadataElement2GUID the metadata element 2 guid
     */
    public void setMetadataElement2GUID(String metadataElement2GUID) {
        this.metadataElement2GUID = metadataElement2GUID;
    }


    /**
     * Return the boolean flag indicating whether the KnownDuplicate classification should be set on the
     * linked entities.
     *
     * @return boolean flag
     */
    public boolean getSetKnownDuplicate()
    {
        return setKnownDuplicate;
    }


    /**
     * Set up the boolean flag indicating whether the KnownDuplicate classification should be set on the
     * linked entities.
     *
     * @param setKnownDuplicate boolean flag
     */
    public void setSetKnownDuplicate(boolean setKnownDuplicate)
    {
        this.setKnownDuplicate = setKnownDuplicate;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "PeerDuplicatesRequestBody{" +
                       "metadataElement1GUID='" + metadataElement1GUID + '\'' +
                       ", metadataElement2GUID='" + metadataElement2GUID + '\'' +
                       ", setKnownDuplicate=" + setKnownDuplicate +
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
        PeerDuplicatesRequestBody that = (PeerDuplicatesRequestBody) objectToCompare;
        return setKnownDuplicate == that.setKnownDuplicate && Objects.equals(metadataElement1GUID, that.metadataElement1GUID) &&
                       Objects.equals(metadataElement2GUID, that.metadataElement2GUID);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), metadataElement1GUID, metadataElement2GUID, setKnownDuplicate);
    }
}
