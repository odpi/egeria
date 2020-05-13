/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.cognos.ffdc.exceptions;

import org.odpi.openmetadata.commonservices.ffdc.exceptions.OMAGCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;

public class CognosCheckedException extends OMAGCheckedExceptionBase {
	
	private static final long serialVersionUID = 1L;

    public CognosCheckedException(ExceptionMessageDefinition msg, String className, String actionDescription, Throwable caughtError) {
        super(msg, className, actionDescription, caughtError);
     }
    
    public CognosCheckedException(ExceptionMessageDefinition msg, String className, String actionDescription) {
        super(msg, className, actionDescription);
    }
	public String getErrorCause() {
		return this.getCause() != null ? this.getCause().getMessage() : null;
	}

}
