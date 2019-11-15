/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.governanceservers.openlineage.util;

import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageException;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageResponse;

public class OpenLineageExceptionHandler extends RESTExceptionHandler {

    public void captureOpenLineageException(LineageResponse response, OpenLineageException e) {
        response.setRelatedHTTPCode(e.getReportedHTTPCode());
        response.setExceptionClassName(e.getClass().getName());
        response.setExceptionErrorMessage(e.getErrorMessage());
        response.setExceptionSystemAction(e.getReportedSystemAction());
        response.setExceptionUserAction(e.getReportedUserAction());
    }


}
