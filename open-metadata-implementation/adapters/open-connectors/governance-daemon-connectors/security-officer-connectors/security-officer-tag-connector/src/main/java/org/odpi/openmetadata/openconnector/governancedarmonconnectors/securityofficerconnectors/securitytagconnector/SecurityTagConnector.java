/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnector.governancedarmonconnectors.securityofficerconnectors.securitytagconnector;


import org.odpi.openmetadata.accessservices.securityofficer.api.model.SchemaElementEntity;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.SecurityClassification;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.openconnector.governancedarmonconnectors.securityofficerconnectors.SecurityOfficerConnector;

import java.util.Collections;

public class SecurityTagConnector extends ConnectorBase implements SecurityOfficerConnector {

    @Override
    public SecurityClassification resolveSecurityClassification(SchemaElementEntity schemaElementEntity) {
        if (schemaElementEntity.getSecurityClassification() != null) {
            return schemaElementEntity.getSecurityClassification();
        }

        SecurityClassification securityClassification = new SecurityClassification();
        securityClassification.setSecurityLabels(Collections.singletonList("C3"));
        securityClassification.setSecurityProperties(Collections.singletonMap("user", "SecurityTagConnector"));
        return securityClassification;
    }

}