/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.templatemanager.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.frameworkservices.gaf.rest.OpenMetadataElementsResponse;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.odpi.openmetadata.viewservices.templatemanager.rest.TemplateClassificationRequestBody;
import org.slf4j.LoggerFactory;


/**
 * The TemplateManagerRESTServices provides the server-side implementation of the Template Manager Open Metadata
 * View Service (OMVS).  This interface provides access to a person's profile, roles and network.
 */
public class TemplateManagerRESTServices extends TokenController
{
    private static final TemplateManagerInstanceHandler instanceHandler = new TemplateManagerInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(TemplateManagerRESTServices.class),
                                                                            instanceHandler.getServiceName());


    /**
     * Default constructor
     */
    public TemplateManagerRESTServices()
    {
    }

    /**
     * Retrieve the elements with the template classification.  The request can include the .
     *
     * @param serverName     name of server instance to route request to
     * @param elementTypeName optional type name for the template
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody searchString  to retrieve
     *
     * @return list of matching metadata elements (or null if no elements match the name) or
     *  InvalidParameterException the qualified name is null
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException there is a problem accessing the metadata store
     */
    public OpenMetadataElementsResponse findTemplates(String                            serverName,
                                                      String                            elementTypeName,
                                                      String                            viewServiceURLMarker,
                                                      String                            accessServiceURLMarker,
                                                      int                               startFrom,
                                                      int                               pageSize,
                                                      TemplateClassificationRequestBody requestBody)
    {
        return null; // todo
    }
}
