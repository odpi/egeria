/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * FingerprintAnnotationProperties describes properties that describe a fingerprint that captures the characteristics
 * of a data source in a single value.   The aim is to be able to quickly identify if the data has changed since the
 * last survey.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class FingerprintAnnotationProperties extends DataFieldAnnotationProperties
{
    private String fingerprint          = null;
    private String fingerprintAlgorithm = null;
    private long   hash                 = 0L;
    private String hashAlgorithm        = null;


    /**
     * Default constructor
     */
    public FingerprintAnnotationProperties()
    {
        super();
        super.typeName = OpenMetadataType.FINGERPRINT_ANNOTATION.typeName;
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public FingerprintAnnotationProperties(FingerprintAnnotationProperties template)
    {
        super(template);

        if (template != null)
        {
            fingerprint          = template.getFingerprint();
            fingerprintAlgorithm = template.getFingerprintAlgorithm();
            hash          = template.getHash();
            hashAlgorithm = template.getHashAlgorithm();
        }
    }


    /**
     * Return the string value of the fingerprint.
     *
     * @return string
     */
    public String getFingerprint()
    {
        return fingerprint;
    }


    /**
     * Set up the string value of the fingerprint.
     *
     * @param fingerprint string
     */
    public void setFingerprint(String fingerprint)
    {
        this.fingerprint = fingerprint;
    }


    /**
     * Return the description of the algorithm used to calculate the fingerprint.
     *
     * @return string
     */
    public String getFingerprintAlgorithm()
    {
        return fingerprintAlgorithm;
    }


    /**
     * Set up the description of the algorithm used to calculate the fingerprint.
     *
     * @param fingerprintAlgorithm string
     */
    public void setFingerprintAlgorithm(String fingerprintAlgorithm)
    {
        this.fingerprintAlgorithm = fingerprintAlgorithm;
    }


    /**
     * Return the hash value for the fingerprint.
     *
     * @return long
     */
    public long getHash()
    {
        return hash;
    }


    /**
     * Set up the hash value for the fingerprint.
     *
     * @param hash long
     */
    public void setHash(long hash)
    {
        this.hash = hash;
    }


    /**
     * Return the algorithm used to calculate the hash.
     *
     * @return string
     */
    public String getHashAlgorithm()
    {
        return hashAlgorithm;
    }


    /**
     * Set up the algorithm used to calculate the hash.
     *
     * @param hashAlgorithm string
     */
    public void setHashAlgorithm(String hashAlgorithm)
    {
        this.hashAlgorithm = hashAlgorithm;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "FingerprintAnnotationProperties{" +
                "fingerprint='" + fingerprint + '\'' +
                ", fingerprintAlgorithm='" + fingerprintAlgorithm + '\'' +
                ", hash=" + hash +
                ", hashAlgorithm='" + hashAlgorithm + '\'' +
                "} " + super.toString();
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        FingerprintAnnotationProperties that = (FingerprintAnnotationProperties) objectToCompare;
        return getHashAlgorithm() == that.getHashAlgorithm() &&
                       Objects.equals(getFingerprint(), that.getFingerprint()) &&
                       Objects.equals(getFingerprintAlgorithm(), that.getFingerprintAlgorithm()) &&
                       Objects.equals(getHash(), that.getHash());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getFingerprint(), getFingerprintAlgorithm(), getHashAlgorithm());
    }
}
