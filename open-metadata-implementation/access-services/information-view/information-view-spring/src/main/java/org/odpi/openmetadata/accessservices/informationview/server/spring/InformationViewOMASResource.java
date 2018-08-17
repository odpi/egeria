/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.informationview.server.spring;


import org.odpi.openmetadata.accessservices.informationview.server.InformationViewOMASRegistration;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class InformationViewOMASResource {
    InformationViewOMASRegistration registration;

    public InformationViewOMASResource() {
        registration = new InformationViewOMASRegistration();
    }
}
