/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.tabulardatafvt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinitionEnum;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductGovernanceDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.GovernanceZoneName;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Products in the security zone hold information that only the security team may see.  Jacquard copies a
 * product's zones onto its asset and its subscription processes, which keeps the data hidden; the license is
 * what tells a subscriber who has been let in that it must not be passed on.  The two must agree: a security
 * product without the security-team license could be shared on, and the security-team license on a product
 * outside the zone promises a protection that is not there.
 * <br><br>
 * This needs no platform - it checks the catalogue definitions themselves.
 */
public class ProductSecurityLicenseFVT
{
    @Test
    @DisplayName("Products in the security zone carry the security-team-only license, and only they do")
    void securityZoneAndLicenseAgree()
    {
        final String securityZone = GovernanceZoneName.SECURITY.getZoneName();

        List<String> mismatches = new ArrayList<>();

        for (ProductDefinitionEnum definition : ProductDefinitionEnum.values())
        {
            boolean inSecurityZone      = (definition.zoneMembership() != null) && definition.zoneMembership().contains(securityZone);
            boolean securityTeamLicense = definition.getLicense() == ProductGovernanceDefinition.SECURITY_TEAM_ONLY;

            if (inSecurityZone != securityTeamLicense)
            {
                mismatches.add(definition.getProductName() + " (zone " + definition.zoneMembership() + ", license " + definition.getLicense() + ")");
            }
        }

        assertTrue(mismatches.isEmpty(), "Security zone and license disagree for: " + mismatches);
    }
}
