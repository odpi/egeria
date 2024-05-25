/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataprivacy.properties;

import java.util.Objects;

public class DigitalServiceDependency extends DataPrivacyElementHeader
{
    private String  callerDigitalServiceGUID = null;
    private String  calledDigitalServiceGUID = null;

    public DigitalServiceDependency()
    {
        super();
    }

    public DigitalServiceDependency(DigitalServiceDependency template)
    {
        super(template);

        if (template != null)
        {
            this.callerDigitalServiceGUID = template.getCallerDigitalServiceGUID();
            this.calledDigitalServiceGUID = template.getCalledDigitalServiceGUID();
        }
    }

    public String getCallerDigitalServiceGUID()
    {
        return callerDigitalServiceGUID;
    }

    public void setCallerDigitalServiceGUID(String callerDigitalServiceGUID)
    {
        this.callerDigitalServiceGUID = callerDigitalServiceGUID;
    }

    public String getCalledDigitalServiceGUID()
    {
        return calledDigitalServiceGUID;
    }

    public void setCalledDigitalServiceGUID(String calledDigitalServiceGUID)
    {
        this.calledDigitalServiceGUID = calledDigitalServiceGUID;
    }

    @Override
    public String toString()
    {
        return "DigitalServiceDependency{" +
                "callerDigitalServiceGUID='" + callerDigitalServiceGUID + '\'' +
                ", calledDigitalServiceGUID='" + calledDigitalServiceGUID + '\'' +
                '}';
    }


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

    @Override
    public int hashCode()
    {

        return Objects.hash(getCallerDigitalServiceGUID(), getCalledDigitalServiceGUID());
    }
}
