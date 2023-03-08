/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.integrationdaemonservices.context;


/**
 * IntegrationContext is the base class for the integration context provided to the integration connector to provide access to open metadata
 * services.  Each integration service specializes this class to provide the method appropriate for the particular type of technology it
 * is supporting.
 *
 * This base class supports the common methods available to all types of integration connectors.
 */
public abstract class IntegrationContext
{
    /**
     * Default constructor
     */
    protected IntegrationContext()
    {
    }
}
