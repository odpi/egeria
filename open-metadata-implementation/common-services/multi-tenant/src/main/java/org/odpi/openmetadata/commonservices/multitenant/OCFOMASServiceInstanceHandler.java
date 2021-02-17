/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.multitenant;


/**
 * Provide access to the common handlers for OMAS's that use the OCF beans.
 */
public abstract class OCFOMASServiceInstanceHandler extends OMASServiceInstanceHandler
{
    /**
     * Constructor
     *
     * @param serviceName name of the service
     */
    public OCFOMASServiceInstanceHandler(String serviceName)
    {
        super(serviceName);
    }
}
