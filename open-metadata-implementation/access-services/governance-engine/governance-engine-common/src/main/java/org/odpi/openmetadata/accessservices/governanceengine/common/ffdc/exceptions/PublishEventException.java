/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.governanceengine.common.ffdc.exceptions;


public class PublishEventException extends GovernanceEngineCheckedExceptionBase {

    public PublishEventException(int reportedHTTPCode, String reportingClassName, String reportingActionDescription, String reportedErrorMessage, String reportedSystemAction, String reportedUserAction) {
        super(reportedHTTPCode, reportingClassName, reportingActionDescription, reportedErrorMessage, reportedSystemAction, reportedUserAction);
    }
}
