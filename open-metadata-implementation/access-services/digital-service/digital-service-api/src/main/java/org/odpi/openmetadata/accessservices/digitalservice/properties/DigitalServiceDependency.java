/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.properties;

import java.util.Objects;

/**
 * <p>DigitalServiceDependency class.</p>
 *
 */
public class DigitalServiceDependency extends DigitalServiceElementHeader
{
    private static final long    serialVersionUID = 1L;

    private String  callerDigitalServiceGUID = null;
    private String  calledDigitalServiceGUID = null;

    /**
     * <p>Constructor for DigitalServiceDependency.</p>
     */
    public DigitalServiceDependency()
    {
        super();
    }

    /**
     * <p>Constructor for DigitalServiceDependency.</p>
     *
     * @param template a {@link DigitalServiceDependency} object.
     */
    public DigitalServiceDependency(DigitalServiceDependency template)
    {
        super(template);

        if (template != null)
        {
            this.callerDigitalServiceGUID = template.getCallerDigitalServiceGUID();
            this.calledDigitalServiceGUID = template.getCalledDigitalServiceGUID();
        }
    }

    /**
     * <p>Getter for the field <code>callerDigitalServiceGUID</code>.</p>
     *
     * @return a {@link String} object.
     */
    public String getCallerDigitalServiceGUID()
    {
        return callerDigitalServiceGUID;
    }

    /**
     * <p>Setter for the field <code>callerDigitalServiceGUID</code>.</p>
     *
     * @param callerDigitalServiceGUID a {@link String} object.
     */
    public void setCallerDigitalServiceGUID(String callerDigitalServiceGUID)
    {
        this.callerDigitalServiceGUID = callerDigitalServiceGUID;
    }

    /**
     * <p>Getter for the field <code>calledDigitalServiceGUID</code>.</p>
     *
     * @return a {@link String} object.
     */
    public String getCalledDigitalServiceGUID()
    {
        return calledDigitalServiceGUID;
    }

    /**
     * <p>Setter for the field <code>calledDigitalServiceGUID</code>.</p>
     *
     * @param calledDigitalServiceGUID a {@link String} object.
     */
    public void setCalledDigitalServiceGUID(String calledDigitalServiceGUID)
    {
        this.calledDigitalServiceGUID = calledDigitalServiceGUID;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "DigitalServiceDependency{" +
                "callerDigitalServiceGUID='" + callerDigitalServiceGUID + '\'' +
                ", calledDigitalServiceGUID='" + calledDigitalServiceGUID + '\'' +
                '}';
    }


    /** {@inheritDoc} */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof DigitalServiceDependency))
        {
            return false;
        }
        DigitalServiceDependency that = (DigitalServiceDependency) objectToCompare;
        return Objects.equals(getCallerDigitalServiceGUID(), that.getCallerDigitalServiceGUID()) &&
                Objects.equals(getCalledDigitalServiceGUID(), that.getCalledDigitalServiceGUID());
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {

        return Objects.hash(getCallerDigitalServiceGUID(), getCalledDigitalServiceGUID());
    }
}
