
/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.adminservices.configuration.properties.validators;

import org.odpi.openmetadata.adminservices.configuration.registration.GovernanceServicesDescription;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * org.odpi.openmetadata.userinterface.adminservices.configuration.properties.GovernanceServerEndpoint an endpoint for each governance server.
 * If specified then the endpoint overrides the metadata server endpoint
 */
public class UIConfigValidators {
    public static boolean isURLValid(String str) {
        boolean isValid = false;
        if (str != null) {
            try {
                new URL(str);
                isValid = true;
            } catch (MalformedURLException e) {
               // catch url error
            }
        }
      return isValid;
    }
    public static boolean isGovernanceServerNameValid(String str) {
        boolean isValid = false;
        if (str != null) {
            List<GovernanceServicesDescription> servicesList = GovernanceServicesDescription.getGovernanceServersDescriptionList();
            for (GovernanceServicesDescription service : servicesList) {
                if (str.equals(service.getServiceURLMarker())) {
                    isValid = true;
                }
            }
        }
        return isValid;
    }
}
