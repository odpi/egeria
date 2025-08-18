/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.dataengineer.server;


import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstanceHandler;



/**
 * DataEngineerInstanceHandler retrieves information from the instance map for the
 * view service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the DataEngineerAdmin class.
 */
public class DataEngineerInstanceHandler extends OMVSServiceInstanceHandler
{
    /**
     * Default constructor registers the view service
     */
    public DataEngineerInstanceHandler()
    {
        super(ViewServiceDescription.DATA_ENGINEER.getViewServiceFullName());

        DataEngineerRegistration.registerViewService();
    }


}
