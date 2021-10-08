/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.glossaryauthor.fvt.junit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.apache.commons.lang3.StringUtils;
import org.odpi.openmetadata.fvt.utilities.FVTConstants;
import org.odpi.openmetadata.accessservices.glossaryauthor.fvt.CategoryFVT;
import org.odpi.openmetadata.http.HttpHelper;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class CategoryIT {
    @BeforeAll
    public static void disableStrictSSL(){
        HttpHelper.noStrictSSL();
    }

    @ParameterizedTest
    //@ValueSource(strings = {"serverinmem","serverview"})
    @ValueSource(strings = {"serverview"})
    public void testCategory(String server) {
       assertDoesNotThrow(() -> CategoryFVT.runIt(StringUtils.defaultIfEmpty(System.getProperty("fvt.url"), FVTConstants.SERVER_PLATFORM_URL_ROOT),
                                                    server,
                                                    "garygeeke"));
    }
}
