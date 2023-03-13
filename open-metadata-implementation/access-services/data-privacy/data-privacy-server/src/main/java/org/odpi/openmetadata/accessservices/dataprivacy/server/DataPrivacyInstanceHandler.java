/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataprivacy.server;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstanceHandler;


/**
 * DataPrivacyInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the DataPrivacyAdmin class.
 */
class DataPrivacyInstanceHandler extends OMASServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    DataPrivacyInstanceHandler()
    {
        super(AccessServiceDescription.DATA_PRIVACY_OMAS.getAccessServiceFullName());

        DataPrivacyRegistration.registerAccessService();
    }
}
