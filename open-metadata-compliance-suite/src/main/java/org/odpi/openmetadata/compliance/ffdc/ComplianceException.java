/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.compliance.ffdc;

/**
 * Compliance exception provides a base class for exceptions that indicate there is a failure in the
 * compliance text.
 */
public class ComplianceException extends Exception
{
    /**
     * Normal constructor for a compliance exception
     *
     * @param errorMessage description of the exception
     */
    public ComplianceException(String   errorMessage)
    {
        super(errorMessage);
    }
}
