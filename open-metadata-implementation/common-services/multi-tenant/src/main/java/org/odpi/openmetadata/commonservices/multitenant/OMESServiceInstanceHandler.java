/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.multitenant;


import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;

/**
 * OMESServiceInstanceHandler retrieves information from the instance map for the
 * engine service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the EngineServiceAdmin class.
 */
public class OMESServiceInstanceHandler extends GovernanceServerServiceInstanceHandler
{


    /**
     * Constructor
     *
     * @param serviceName a descriptive name for the OMVS
     */
    public OMESServiceInstanceHandler(String   serviceName)
    {
        super(serviceName);
    }

}
