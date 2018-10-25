/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.server;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.ReportCreator;
import org.odpi.openmetadata.accessservices.informationview.events.ReportRequestBody;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.ReportCreationException;
import org.odpi.openmetadata.accessservices.informationview.responses.VoidResponse;


public class InformationViewRestServices {

    private static ReportCreator reportCreator;

    public static void setReportCreator(ReportCreator reportCreator) {
        InformationViewRestServices.reportCreator = reportCreator;
    }

    public VoidResponse submitReport(String userId,
                             ReportRequestBody requestBody) {
        VoidResponse  response = new VoidResponse();
        try {
            reportCreator.createReportModel(requestBody);
        }
        catch(ReportCreationException e){
            response.setExceptionClassName(e.getReportingClassName());
            response.setExceptionErrorMessage(e.getReportedErrorMessage());
            response.setRelatedHTTPCode(e.getReportedHTTPCode());
            response.setExceptionUserAction(e.getReportedUserAction());
        }

        return response;
    }

}
