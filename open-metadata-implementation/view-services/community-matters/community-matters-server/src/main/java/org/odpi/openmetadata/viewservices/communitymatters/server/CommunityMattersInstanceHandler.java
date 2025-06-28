/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.communitymatters.server;


import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstanceHandler;



/**
 * CommunityMattersInstanceHandler retrieves information from the instance map for the
 * view service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the CommunityMattersAdmin class.
 */
public class CommunityMattersInstanceHandler extends OMVSServiceInstanceHandler
{
    /**
     * Default constructor registers the view service
     */
    public CommunityMattersInstanceHandler()
    {
        super(ViewServiceDescription.COMMUNITY_MATTERS.getViewServiceFullName());

        CommunityMattersRegistration.registerViewService();
    }


}
