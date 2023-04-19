/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetmanager.rest.AssetManagerIdentifiersRequestBody;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The EmbeddedConnectionProperties is used within a VirtualConnection to link to the embedded connections.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class EmbeddedConnectionProperties extends RelationshipProperties
{
    @Serial
    private static final long serialVersionUID = 1L;

    /*
     * Attributes of an embedded connection
     */
    protected int                 position           = 0;
    protected String              displayName        = null;
    protected Map<String, Object> arguments          = null;


    /**
     * Default constructor
     */
    public EmbeddedConnectionProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template element to copy
     */
    public EmbeddedConnectionProperties(EmbeddedConnectionProperties template)
    {
        super (template);

        if (template != null)
        {
            position = template.getPosition();
            displayName = template.getDisplayName();
            arguments = template.getArguments();
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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "EmbeddedConnectionProperties{" +
                       "position=" + position +
                       ", displayName='" + displayName + '\'' +
                       ", arguments=" + arguments +
                       ", effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
                       ", extendedProperties=" + getExtendedProperties() +
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
        EmbeddedConnectionProperties that = (EmbeddedConnectionProperties) objectToCompare;
        return position == that.position && Objects.equals(displayName, that.displayName) && Objects.equals(arguments,
                                                                                                            that.arguments);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), position, displayName, arguments);
    }
}