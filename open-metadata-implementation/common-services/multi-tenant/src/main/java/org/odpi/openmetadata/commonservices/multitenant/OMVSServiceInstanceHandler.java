/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.multitenant;


import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;

/**
 * OMVSServiceInstanceHandler retrieves information from the instance map for the
 * view service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the ViewServiceAdmin class.
 */
public class OMVSServiceInstanceHandler extends AuditableServerServiceInstanceHandler
{
    private RESTExceptionHandler  exceptionHandler = new RESTExceptionHandler();


    /**
     * Constructor
     *
     * @param serviceName a descriptive name for the OMVS
     */
    public OMVSServiceInstanceHandler(String   serviceName)
    {
        super(serviceName);
    }

    /**
     * Retrieve the exception handler that can package up common exceptions and pack them into
     * a REST Response.
     *
     * @return exception handler object
     */
    public RESTExceptionHandler getExceptionHandler() { return exceptionHandler; }


}
