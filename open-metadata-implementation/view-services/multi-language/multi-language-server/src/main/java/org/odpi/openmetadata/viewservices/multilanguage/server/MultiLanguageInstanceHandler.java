/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.multilanguage.server;


import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstanceHandler;



/**
 * MultiLanguageInstanceHandler retrieves information from the instance map for the
 * view service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the MultiLanguageAdmin class.
 */
public class MultiLanguageInstanceHandler extends OMVSServiceInstanceHandler
{
    /**
     * Default constructor registers the view service
     */
    public MultiLanguageInstanceHandler()
    {
        super(ViewServiceDescription.MULTI_LANGUAGE.getViewServiceFullName());

        MultiLanguageRegistration.registerViewService();
    }


}
