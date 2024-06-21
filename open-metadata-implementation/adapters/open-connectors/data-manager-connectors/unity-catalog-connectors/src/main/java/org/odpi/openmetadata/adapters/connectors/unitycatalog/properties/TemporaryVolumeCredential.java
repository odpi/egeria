/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Description of credentials for accessing a volume.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TemporaryVolumeCredential
{
    private String               volume_id   = null;
    private VolumeOperation      operation  = null;


    /**
     * Constructor
     */
    public TemporaryVolumeCredential()
    {
    }

    /**
     * Return the unique identifier of the volume.
     *
     * @return string
     */
    public String getVolume_id()
    {
        return volume_id;
    }


    /**
     * Set up the unique identifier of the volume.
     *
     * @param volume_id string
     */
    public void setVolume_id(String volume_id)
    {
        this.volume_id = volume_id;
    }


    /**
     * Return the operation permitted by the credential.
     *
     * @return text
     */
    public VolumeOperation getOperation()
    {
        return operation;
    }


    /**
     * Set up the operation permitted by the credential.
     *
     * @param operation text
     */
    public void setOperation(VolumeOperation operation)
    {
        this.operation = operation;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "TemporaryVolumeCredential{" +
                "volume_id='" + volume_id + '\'' +
                ", operation=" + operation +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        TemporaryVolumeCredential that = (TemporaryVolumeCredential) objectToCompare;
        return Objects.equals(volume_id, that.volume_id) && operation == that.operation;
    }

    
    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(volume_id, operation);
    }
}
