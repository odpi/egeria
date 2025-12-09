/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.rest;

import org.odpi.openmetadata.adminservices.configuration.properties.BasicServerProperties;


/**
 * ServerPropertiesRequestBody is the request body to add the basic server properties to an OMAG Server's configuration document.
 */
public class ServerPropertiesRequestBody extends BasicServerProperties
{

    /**
     * Default constructor.
     */
    public ServerPropertiesRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public ServerPropertiesRequestBody(BasicServerProperties template)
    {
        super(template);
    }

    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "ServerPropertiesRequestBody{} " + super.toString();
    }
}
