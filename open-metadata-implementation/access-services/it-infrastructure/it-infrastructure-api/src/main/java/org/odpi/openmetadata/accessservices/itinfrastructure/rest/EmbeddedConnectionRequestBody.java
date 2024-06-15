/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The EmbeddedConnectionRequestBody is used within a VirtualConnection to link to the embedded connections.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class EmbeddedConnectionRequestBody
{
    /*
     * Attributes of an embedded connection
     */
    protected int                 position           = 0;
    protected String              displayName        = null;
    protected Map<String, Object> arguments          = null;

    /*
     * data manager
     */
    private String externalSourceGUID = null;
    private String externalSourceName = null;


    /**
     * Default constructor
     */
    public EmbeddedConnectionRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template element to copy
     */
    public EmbeddedConnectionRequestBody(EmbeddedConnectionRequestBody template)
    {
        if (template != null)
        {
            position = template.getPosition();
            displayName = template.getDisplayName();
            arguments = template.getArguments();
            externalSourceGUID = template.getExternalSourceGUID();
            externalSourceName = template.getExternalSourceName();
        }
    }


    /**
     * Return the position that this connector is in the list of embedded connectors.
     *
     * @return int
     */
    public int getPosition()
    {
        return position;
    }


    /**
     * Set up the position that this connector is in the list of embedded connectors.
     *
     * @param position int
     */
    public void setPosition(int position)
    {
        this.position = position;
    }


    /**
     * Return the printable name of the embedded connection.
     *
     * @return String name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the printable name of the embedded connection.
     *
     * @param displayName String name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Return the arguments for the embedded connection.
     *
     * @return property map
     */
    public Map<String, Object> getArguments()
    {
        if (arguments == null)
        {
            return null;
        }
        else if (arguments.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(arguments);
        }
    }


    /**
     * Set up the arguments for the embedded connection.
     *
     * @param arguments property map
     */
    public void setArguments(Map<String, Object> arguments)
    {
        this.arguments = arguments;
    }



    /**
     * Return the unique identifier of the software server capability entity that represented the external source - null for local.
     *
     * @return string guid
     */
    public String getExternalSourceGUID()
    {
        return externalSourceGUID;
    }


    /**
     * Set up the unique identifier of the software server capability entity that represented the external source - null for local.
     *
     * @param externalSourceGUID string guid
     */
    public void setExternalSourceGUID(String externalSourceGUID)
    {
        this.externalSourceGUID = externalSourceGUID;
    }


    /**
     * Return the unique name of the software server capability entity that represented the external source.
     *
     * @return string name
     */
    public String getExternalSourceName()
    {
        return externalSourceName;
    }


    /**
     * Set up the unique name of the software server capability entity that represented the external source.
     *
     * @param externalSourceName string name
     */
    public void setExternalSourceName(String externalSourceName)
    {
        this.externalSourceName = externalSourceName;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "EmbeddedConnectionRequestBody{" +
                       "position=" + position +
                       ", displayName='" + displayName + '\'' +
                       ", arguments=" + arguments +
                       ", externalSourceGUID='" + externalSourceGUID + '\'' +
                       ", externalSourceName='" + externalSourceName + '\'' +
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
        EmbeddedConnectionRequestBody that = (EmbeddedConnectionRequestBody) objectToCompare;
        return position == that.position &&
                       Objects.equals(displayName, that.displayName) &&
                       Objects.equals(arguments, that.arguments) &&
                       Objects.equals(externalSourceGUID, that.externalSourceGUID) &&
                       Objects.equals(externalSourceName, that.externalSourceName);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(position, displayName, arguments, externalSourceGUID, externalSourceName);
    }
}