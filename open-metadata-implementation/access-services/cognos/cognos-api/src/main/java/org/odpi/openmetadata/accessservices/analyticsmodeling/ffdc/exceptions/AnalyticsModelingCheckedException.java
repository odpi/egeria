/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.exceptions;

import org.odpi.openmetadata.commonservices.ffdc.exceptions.OMAGCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;

/**
 * Exception based on AnalyticsModelingErrorCode class {@link org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.AnalyticsModelingErrorCode}
 * 
 */
public class AnalyticsModelingCheckedException extends OMAGCheckedExceptionBase {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor wrapping original external exception.
	 * @param msg standard block of data related to the problem.
	 * @param className where exception was thrown.
	 * @param actionDescription interrupted by the exception.
	 * @param caughtError original cause of the exception.
	 */
    public AnalyticsModelingCheckedException(ExceptionMessageDefinition msg, String className, String actionDescription, Throwable caughtError) {
        super(msg, className, actionDescription, caughtError);
     }
    
	/**
	 * Constructor for exception caused within component.
	 * @param msg standard block of data related to the problem.
	 * @param className where exception was thrown.
	 * @param actionDescription interrupted by the exception.
	 */
    public AnalyticsModelingCheckedException(ExceptionMessageDefinition msg, String className, String actionDescription) {
        super(msg, className, actionDescription);
    }
    
    /**
     * Helper function to provide details of the original error.
     * @return message of original exception.
     */
	public String getErrorCause() {
		return this.getCause() != null ? this.getCause().getMessage() : null;
	}

}
