/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.privacyofficer.server;


import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstanceHandler;



/**
 * PrivacyOfficerInstanceHandler retrieves information from the instance map for the
 * view service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the PrivacyOfficerAdmin class.
 */
public class PrivacyOfficerInstanceHandler extends OMVSServiceInstanceHandler
{
    /**
     * Default constructor registers the view service
     */
    public PrivacyOfficerInstanceHandler()
    {
        super(ViewServiceDescription.PRIVACY_OFFICER.getViewServiceFullName());

        PrivacyOfficerRegistration.registerViewService();
    }


}
