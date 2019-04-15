/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime;

public class EntityNotFoundException extends InformationViewUncheckedExceptionBase{


    public EntityNotFoundException(String className, String errorMessage,
                                   String systemAction, String userAction, Throwable caughtError) {
        super(className, errorMessage, systemAction, userAction, caughtError);
    }




}
