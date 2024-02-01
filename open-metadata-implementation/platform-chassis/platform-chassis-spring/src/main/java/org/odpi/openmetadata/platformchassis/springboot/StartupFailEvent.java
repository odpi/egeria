/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformchassis.springboot;

import org.springframework.context.ApplicationEvent;

import java.io.Serial;

/**
 *   Application event used for the case of startup list fails
 */
public class StartupFailEvent extends ApplicationEvent
{

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Source of the failure
     */
    private final Object source;

    /**
     * Error message
     */
    private final String message;


    /**
     * Constructor
     *
     * @param source source
     * @param message message
     */
    public StartupFailEvent(Object source, String message)
    {
        super(source);
        this.source = source;
        this.message = message;
    }


    /**
     * Return the source.
     *
     * @return object
     */
    @Override
    public Object getSource()
    {
        return source;
    }


    /**
     * Return the message.
     *
     * @return string
     */
    public String getMessage()
    {
        return message;
    }
}
