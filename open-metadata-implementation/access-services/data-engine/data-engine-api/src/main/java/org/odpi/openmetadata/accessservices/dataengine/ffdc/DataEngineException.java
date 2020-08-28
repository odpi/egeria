/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;

import java.util.Objects;

public class DataEngineException extends OCFCheckedExceptionBase {
    /**
     * This is the typical constructor used for creating an exception.
     *
     * @param messageDefinition content of the message
     * @param className         name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     */
    public DataEngineException(ExceptionMessageDefinition messageDefinition, String className,
                               String actionDescription) {
        super(messageDefinition, className, actionDescription);
    }
}
