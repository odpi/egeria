/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction;

import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.governanceaction.ffdc.GAFErrorCode;
import org.odpi.openmetadata.frameworks.governanceaction.ffdc.GovernanceServiceException;

/**
 * GovernanceService describes the base class for a specific type of connector that is responsible for preforming
 * specific governance actions on demand.
 */
public abstract class GovernanceService extends ConnectorBase
{
    protected String           governanceServiceName = "<Unknown>";

    
    /**
     * Set up the governance action service name.  This is used in error messages.
     *
     * @param governanceServiceName name of the service
     */
    public void setGovernanceServiceName(String governanceServiceName)
    {
        this.governanceServiceName = governanceServiceName;
    }


    /**
     * Provide a common exception for unexpected errors.
     *
     * @param methodName calling method
     * @param error caught exception
     * @throws GovernanceServiceException wrapped exception
     */
    protected void handleUnexpectedException(String      methodName,
                                             Throwable   error) throws ConnectorCheckedException
    {
        throw new GovernanceServiceException(GAFErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(governanceServiceName,
                                                                                                    error.getClass().getName(),
                                                                                                    methodName,
                                                                                                    error.getMessage()),
                                             this.getClass().getName(),
                                             methodName);
    }

}
