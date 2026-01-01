/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ElementStatus;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The ElementControlHeader bean provides details of the origin and changes associated with the element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ElementControlHeader
{
    /**
     * Version identifier
     */
    public static final long CURRENT_AUDIT_HEADER_VERSION = 1;

    /*
     * Version number for this header.  This is used to ensure that all the critical header information
     * in read in a back-level version of the OCF.  The default is 0 to indicate that the instance came from
     * a version of the OCF that does not have a version number encoded.
     */
    private long headerVersion = 0;


    /*
     * Common header for first class elements from a metadata repository
     */
    private ElementStatus   status   = null;
    private ElementType     type     = null;
    private ElementOrigin   origin   = null;
    private ElementVersions versions = null;


    /**
     * Default constructor used by subclasses
     */
    public ElementControlHeader()
    {
    }


    /**
     * Copy/clone constructor.
     *
     * @param template element to copy
     */
    public ElementControlHeader(ElementControlHeader template)
    {
        if (template != null)
        {
            headerVersion    = template.getHeaderVersion();
            status           = template.getStatus();
            type             = template.getType();
            origin           = template.getOrigin();
            versions         = template.getVersions();
        }
    }


    /**
     * Return the version of this header.  This is used by the OMRS to determine if it is back level and
     * should not process events from a source that is more advanced because it does not have the ability
     * to receive all the header properties.
     *
     * @return long version number - the value is incremented each time a new non-informational field is added
     * to the audit header.
     */
    public long getHeaderVersion()
    {
        return headerVersion;
    }


    /**
     * Return the version of this header.  This is used by the OMRS to determine if it is back level and
     * should not process events from a source that is more advanced because it does not have the ability
     * to receive all the header properties.
     *
     * @param headerVersion long version number - the value is incremented each time a new non-informational field is added
     * to the audit header.
     */
    public void setHeaderVersion(long headerVersion)
    {
        this.headerVersion = headerVersion;
    }


    /**
     * Return the current status of the element - typically ACTIVE.
     *
     * @return status enum
     */
    public ElementStatus getStatus()
    {
        return status;
    }


    /**
     * Set up the current status of the element - typically ACTIVE.
     *
     * @param status status enum
     */
    public void setStatus(ElementStatus status)
    {
        this.status = status;
    }


    /**
     * Return the element type properties for this properties object.  These values are set up by the metadata repository
     * and define details to the metadata entity used to represent this element.
     *
     * @return ElementType type information.
     */
    public ElementType getType()
    {
        return type;
    }


    /**
     * Set up the type of this element.
     *
     * @param type element type properties
     */
    public void setType(ElementType type)
    {
        this.type = type;
    }


    /**
     * Return information about the origin of the element. This includes the metadata collection and license.
     *
     * @return element origin object
     */
    public ElementOrigin getOrigin()
    {
        return origin;
    }


    /**
     * Set up information about the origin of the element. This includes the metadata collection and license.
     *
     * @param origin element origin object
     */
    public void setOrigin(ElementOrigin origin)
    {
        this.origin = origin;
    }


    /**
     * Return detail of the element's current version and the users responsible for maintaining it.
     *
     * @return ElementVersion object
     */
    public ElementVersions getVersions()
    {
        return versions;
    }


    /**
     * Set up detail of the element's current version and the users responsible for maintaining it.
     *
     * @param versions ElementVersion object
     */
    public void setVersions(ElementVersions versions)
    {
        this.versions = versions;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ElementControlHeader{" +
                       "status=" + status +
                       ", type=" + type +
                       ", origin=" + origin +
                       ", versions=" + versions +
                       ", headerVersion=" + getHeaderVersion() +
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
        ElementControlHeader that = (ElementControlHeader) objectToCompare;
        return status == that.status && Objects.equals(type, that.type)
                       && Objects.equals(origin, that.origin) &&
                       Objects.equals(versions, that.versions);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(status, type, origin, versions);
    }
}
