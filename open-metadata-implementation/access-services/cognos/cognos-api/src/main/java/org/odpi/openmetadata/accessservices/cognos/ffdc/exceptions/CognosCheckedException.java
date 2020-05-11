/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.cognos.ffdc.exceptions;

import org.odpi.openmetadata.accessservices.cognos.ffdc.CognosErrorCode;

public class CognosCheckedException extends Exception {
	
	private CognosErrorCode errorCode;
	
	
    public CognosCheckedException(CognosErrorCode code, Throwable caughtError) {
        super(code.getErrorMessage(), caughtError);
        this.errorCode = code;
    }
    
    public CognosCheckedException(CognosErrorCode code) {
        this.errorCode = code;
    }

}
