/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.adminservices.configuration.properties;


import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * org.odpi.openmetadata.userinterface.adminservices.configuration.properties.UIAdminServicesConfigHeader provides a common header for configuration properties.  It implements
 * java.io.Serializable.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = UIServerConfig.class, name = "org.odpi.openmetadata.userinterface.adminservices.configuration.properties.UIServerConfig"),
        // TODO add user repository
})
public class UIAdminServicesConfigHeader implements Serializable
{
    private static final long serialVersionUID = 1L;


    /**
     * Default Constructor
     */
    public UIAdminServicesConfigHeader()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public UIAdminServicesConfigHeader(UIAdminServicesConfigHeader template)
    {
    }

}
