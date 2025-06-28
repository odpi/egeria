/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.devopspipeline.server;


import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstanceHandler;



/**
 * DevopsPipelineInstanceHandler retrieves information from the instance map for the
 * view service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the DevopsPipelineAdmin class.
 */
public class DevopsPipelineInstanceHandler extends OMVSServiceInstanceHandler
{
    /**
     * Default constructor registers the view service
     */
    public DevopsPipelineInstanceHandler()
    {
        super(ViewServiceDescription.DEVOPS_PIPELINE.getViewServiceFullName());

        DevopsPipelineRegistration.registerViewService();
    }


}
