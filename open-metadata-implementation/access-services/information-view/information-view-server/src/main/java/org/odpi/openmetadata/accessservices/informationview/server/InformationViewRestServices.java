/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.server;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.ReportHandler;
import org.odpi.openmetadata.accessservices.informationview.events.ReportRequestBody;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.ReportCreationException;
import org.odpi.openmetadata.accessservices.informationview.responses.VoidResponse;


public class InformationViewRestServices {

    InformationViewInstanceHandler  instanceHandler = new InformationViewInstanceHandler();

    public VoidResponse submitReport(String serverName,
                                     String userId,
                                     ReportRequestBody requestBody) {

        VoidResponse  response = new VoidResponse();

        try {

            ReportHandler reportCreator2 = instanceHandler.getReportCreator(serverName);
            reportCreator2.submitReportModel(requestBody);
        }
        catch (ReportCreationException | PropertyServerException e) {
            response.setExceptionClassName(e.getReportingClassName());
            response.setExceptionErrorMessage(e.getReportedErrorMessage());
            response.setRelatedHTTPCode(e.getReportedHTTPCode());
            response.setExceptionUserAction(e.getReportedUserAction());
        }

        return response;
    }

}
