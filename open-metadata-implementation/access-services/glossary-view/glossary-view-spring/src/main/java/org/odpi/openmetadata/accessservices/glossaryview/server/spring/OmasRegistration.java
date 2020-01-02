/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.glossaryview.server.spring;

import org.odpi.openmetadata.accessservices.glossaryview.server.admin.GlossaryViewRegistration;
import org.springframework.web.bind.annotation.RestController;

/**
 * Triggers the registration of this OMAS. No endpoints must be defined in this rest controller
 * TODO: find a better way to do it. Big doubts that registration on spring controller init is correct
 */
@RestController
public class OmasRegistration {

    static final String PAGE_FROM_DEFAULT_VALUE = "0";
    static final String PAGE_SIZE_DEFAULT_VALUE = "100";
    static final int PAGE_SIZE_MAX_VALUE = 1000;

    public OmasRegistration(){
        GlossaryViewRegistration.registerAccessService();
    }

}
