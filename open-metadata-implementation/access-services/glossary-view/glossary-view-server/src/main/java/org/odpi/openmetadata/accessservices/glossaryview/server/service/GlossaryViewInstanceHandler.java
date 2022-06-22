/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.glossaryview.server.service;

import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetail;
import org.odpi.openmetadata.accessservices.glossaryview.server.admin.GlossaryViewServiceInstance;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericHandler;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

/**
 * GlossaryViewInstanceHandler provides the bridge between the REST call and the server instance.
 */
public class GlossaryViewInstanceHandler extends OMASServiceInstanceHandler {

    public GlossaryViewInstanceHandler() {
        super(AccessServiceDescription.GLOSSARY_VIEW_OMAS.getAccessServiceFullName());
    }

    public OpenMetadataAPIGenericHandler<GlossaryViewEntityDetail> getEntitiesHandler(String userId, String serverName,
                                                                                      String serviceOperationName)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        GlossaryViewServiceInstance instance = (GlossaryViewServiceInstance) getServerServiceInstance(userId, serverName,
                serviceOperationName);
        return instance.getEntitiesHandler();
    }

}
