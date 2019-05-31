/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnector.governancedarmonconnectors.securityofficerconnectors;

import org.odpi.openmetadata.accessservices.securityofficer.api.model.SchemaElementEntity;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.SecurityClassification;

public interface SecurityOfficerConnector {

    SecurityClassification resolveSecurityClassification(SchemaElementEntity schemaElementEntity);
}