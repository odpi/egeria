/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.digitalservice.properties.DigitalService;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The request body of creating the new type of the asset deployed database schema.
 *
 * @author wbittles
 * @version $Id: $Id
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DigitalServiceRequestBody extends DigitalServiceOMASAPIRequestBody {

    private DigitalService digitalService;

    /**
     * Default constructor
     */
    public DigitalServiceRequestBody() {
    }


    /**
     * Gets deployed database schema.
     *
     * @return the deployed database schema
     */
    public DigitalService getDigitalService() {
        return digitalService;
    }

    /**
     * Sets deployed database schema.
     *
     * @param digitalService the digital service
     */
    public void setDigitalService(DigitalService digitalService) {
        this.digitalService = digitalService;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DigitalServiceRequestBody that = (DigitalServiceRequestBody) o;
        return Objects.equals(digitalService, that.digitalService);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(digitalService);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "DigitalServiceRequestBody{" +
                "digitalService=" + digitalService +
                "} " + super.toString();
    }
}
