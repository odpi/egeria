/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.serverstandalone.springboot;

import org.springframework.context.ApplicationEvent;

/**
 *   Application event used for the case of startup list fails
 */
public class StartupFailEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private Object source;
    private String message;

    public StartupFailEvent(Object source, String message) {
        super(source);
        this.source = source;
        this.message = message;
    }

    public Object getSource() {
        return source;
    }

    public String getMessage() {
        return message;
    }
}
