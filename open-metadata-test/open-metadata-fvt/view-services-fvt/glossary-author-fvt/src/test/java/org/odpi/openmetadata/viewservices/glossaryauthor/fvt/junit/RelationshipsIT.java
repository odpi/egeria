/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.glossaryauthor.fvt.junit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.odpi.openmetadata.viewservices.glossaryauthor.fvt.RelationshipsFVT;
import org.odpi.openmetadata.http.HttpHelper;
import org.apache.commons.lang3.StringUtils;
import org.odpi.openmetadata.fvt.utilities.FVTConstants;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class RelationshipsIT {
    @BeforeAll
    public static void disableStrictSSL(){
        HttpHelper.noStrictSSL();
    }

    @ParameterizedTest
//    @ValueSource(strings = {"serverinmem","servergraph"})
    @ValueSource(strings = {"serverview"})
    public void testRelationships(String server) {
        assertDoesNotThrow(() -> RelationshipsFVT.runIt(StringUtils.defaultIfEmpty(System.getProperty("fvt.url"),FVTConstants.SERVER_PLATFORM_URL_ROOT), server, "garygeeke"));
    }
}
