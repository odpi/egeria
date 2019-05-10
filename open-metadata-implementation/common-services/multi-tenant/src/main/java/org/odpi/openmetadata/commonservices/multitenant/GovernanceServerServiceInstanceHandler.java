/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.multitenant;

/**
 * GovernanceServerServiceInstanceHandler provides the base class for a governance
 * server's instance handler.
 */
public class GovernanceServerServiceInstanceHandler extends AuditableServerServiceInstanceHandler
{
    /**
     * Constructor passes the service name that is used on all calls to this instance.
     *
     * @param serviceName unique identifier for this service with a human meaningful value
     */
    public GovernanceServerServiceInstanceHandler(String       serviceName)
    {
        super(serviceName);
    }
}
