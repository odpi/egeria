/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.server;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.DataViewHandler;
import org.odpi.openmetadata.accessservices.informationview.contentmanager.ReportHandler;
import org.odpi.openmetadata.accessservices.informationview.events.DataViewRequestBody;
import org.odpi.openmetadata.accessservices.informationview.events.ReportRequestBody;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.InformationViewExceptionBase;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.ReportCreationException;
import org.odpi.openmetadata.accessservices.informationview.responses.InformationViewOMASAPIResponse;
import org.odpi.openmetadata.accessservices.informationview.responses.VoidResponse;


public class InformationViewRestServices {

    InformationViewInstanceHandler  instanceHandler = new InformationViewInstanceHandler();

    public VoidResponse submitReport(String serverName,
                                     String userId,
                                     ReportRequestBody requestBody) {

        VoidResponse  response = new VoidResponse();

        try {
            ReportHandler reportCreator = instanceHandler.getReportCreator(serverName);
            reportCreator.submitReportModel(requestBody);
        }
        catch (ReportCreationException | PropertyServerException e) {
            response.setExceptionClassName(e.getReportingClassName());
            response.setExceptionErrorMessage(e.getReportedErrorMessage());
            response.setRelatedHTTPCode(e.getReportedHTTPCode());
            response.setExceptionUserAction(e.getReportedUserAction());
        }

        return response;
    }

    public InformationViewOMASAPIResponse submitDataView(String serverName,
                                                         String userId,
                                                         DataViewRequestBody requestBody) {

        VoidResponse response = new VoidResponse();

        try {
            DataViewHandler dataViewHandler = instanceHandler.getDataViewHandler(serverName);
            dataViewHandler.createReportDataView(requestBody);
        }
        catch ( PropertyServerException e) {
            return handleErrorResponse( e);
        }

        return response;
    }

    private InformationViewOMASAPIResponse handleErrorResponse(InformationViewExceptionBase e) {
        VoidResponse  response = new VoidResponse();
        response.setExceptionClassName(e.getReportingClassName());
        response.setExceptionErrorMessage(e.getReportedErrorMessage());
        response.setRelatedHTTPCode(e.getReportedHTTPCode());
        response.setExceptionUserAction(e.getReportedUserAction());
        return response;
    }

}
