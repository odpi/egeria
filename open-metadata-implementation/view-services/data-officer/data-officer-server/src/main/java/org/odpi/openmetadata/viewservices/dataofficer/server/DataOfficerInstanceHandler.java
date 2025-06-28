/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.dataofficer.server;


import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstanceHandler;



/**
 * DataOfficerInstanceHandler retrieves information from the instance map for the
 * view service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the DataOfficerAdmin class.
 */
public class DataOfficerInstanceHandler extends OMVSServiceInstanceHandler
{
    /**
     * Default constructor registers the view service
     */
    public DataOfficerInstanceHandler()
    {
        super(ViewServiceDescription.DATA_OFFICER.getViewServiceFullName());

        DataOfficerRegistration.registerViewService();
    }


}
