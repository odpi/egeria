/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.glossaryview.server.spring;

import org.odpi.openmetadata.accessservices.glossaryview.server.admin.GlossaryViewRegistration;
import org.springframework.web.bind.annotation.RestController;

/**
 * Triggers the registration of this OMAS. No endpoints must be defined in this rest controller
 * TODO: find a better way to do it
 */
@RestController
public class OmasRegistrationResource {

    static{
        GlossaryViewRegistration.registerAccessService();
    }

}
