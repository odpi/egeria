/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataprivacy.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DigitalServiceImplementationStyle characterizes the implementation style of the digital service.
 * <ul>
 *     <li>
 *         UNCLASSIFIED - The digital service implementation style is undefined.
 *     </li>
 *     <li>
 *         API - The digital service provides one or more APIs.  This means the consumers are going to be
 *         other digital services.  Typically API services are data processors.
 *     </li>
 *     <li>
 *         PERSONAL_APP - The digital service provides a user interface via a client application that is owned and
 *         installed by the end user.  A mobile app for a smart phone or tablet is an example of this type of digital
 *         service.  It means that the data subject and the end user is aligned.
 *     </li>
 *     <li>
 *         BROWSER_APP - The digital service provides a user interface via a browser.  There is no guarantee that
 *         the same user will access the service through a browser instance if the browser is on a shared machine.
 *     </li>
 *     <li>
 *         LOCATION_APP - The digital service is installed at a specific location and is monitoring activity at the
 *         location and providing the service at that location.  Some locations are closely aligned with individuals
 *         (such as in the home) and this needs to be taken into account with reviewing privacy and security
 *         requirements.
 *     </li>
 *     <li>
 *         INFRASTRUCTURE_APP - The digital service is installed as part of some infrastructure (for example
 *         a vehicle, or a power-distribution grid) and it is monitoring
 *         and supporting the operation of that infrastructure.  Some pieces of infrastructure are closely associated
 *         with an individual (such as a person's car) which may have implications for privacy and security.
 *     </li>
 *     <li>
 *         DATA_FEED - The digital service continuously produces data that consumers can subscribe to.
 *     </li>
 *     <li>
 *         DATA_SINK - The digital service is receiving and accumulating data from other digital services.
 *     </li>
 *     <li>
 *         CLIENT_SERVER - The digital service provides a thick client tightly coupled with a backend server.
 *         Often these are standalone applications bought in as a package or home-grown.
 *     </li>
 *     <li>
 *         OTHER - The digital service's implementation style is locally defined.
 *     </li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum DigitalServiceImplementationStyle
{
    UNCLASSIFIED       (0,  "Unclassified",       "The digital service implementation style is undefined."),
    API                (1,  "API",                "The digital service provides one or more APIs."),
    PERSONAL_APP       (2,  "Personal App",       "The digital service provides a user interface via a client application that is owned and " +
                                                      "installed by the end user."),
    BROWSER_APP        (3,  "Browser App",        "The digital service provides a user interface via a browser."),
    LOCATION_APP       (4,  "Location App",       "The digital service is installed at a specific location."),
    INFRASTRUCTURE_APP (5,  "Infrastructure App", "The digital service is installed as part of some infrastructure."),
    DATA_FEED          (6,  "Data Feed",          "The digital service continuously produces data that consumers can subscribe to."),
    DATA_SINK          (7,  "Data Sink",          "The digital service is receiving and accumulating data from other digital services."),
    CLIENT_SERVER      (8,  "Client-Server",      "The digital service provides a thick client tightly coupled with a backend server."),
    OTHER              (99, "Other",              "The digital service's implementation style is locally defined.");

    private final int            ordinal;
    private final String         name;
    private final String         description;


    /**
     * Default constructor for the enumeration.
     *
     * @param ordinal numerical representation of the enumeration
     * @param name default string name of the instance provenance type
     * @param description default string description of the instance provenance type
     */
    DigitalServiceImplementationStyle(int  ordinal, String name, String description)
    {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
    }


    /**
     * Return the numeric representation of the instance provenance type.
     *
     * @return int ordinal
     */
    public int getOrdinal() { return ordinal; }


    /**
     * Return the default name of the instance provenance type.
     *
     * @return String name
     */
    public String getName() { return name; }


    /**
     * Return the default description of the instance provenance type.
     *
     * @return String description
     */
    public String getDescription() { return description; }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "DigitalServiceImplementationStyle{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
