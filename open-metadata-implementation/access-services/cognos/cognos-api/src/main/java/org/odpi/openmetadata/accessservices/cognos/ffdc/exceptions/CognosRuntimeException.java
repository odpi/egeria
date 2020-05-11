/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.cognos.ffdc.exceptions;

import java.text.MessageFormat;

import org.odpi.openmetadata.accessservices.cognos.ffdc.CognosErrorCode;

public class CognosRuntimeException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	private final CognosErrorCode errorCode;
	
    public CognosRuntimeException(CognosErrorCode code, Throwable caughtError, String ... params) {
        super(new MessageFormat(code.getErrorMessage()).format(params), caughtError);
        this.errorCode = code;
    }
    
    public CognosRuntimeException(CognosErrorCode code, String ... params) {
        super(new MessageFormat(code.getErrorMessage()).format(params));
        this.errorCode = code;
    }

    public CognosErrorCode getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorCode.getErrorMessage();
	}
	
	public String getErrorCause() {
		return this.getCause() != null ? this.getCause().getMessage() : null;
	}
}
