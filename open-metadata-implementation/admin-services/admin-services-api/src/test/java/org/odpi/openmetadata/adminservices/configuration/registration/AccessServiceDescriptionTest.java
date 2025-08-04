/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.registration;


import org.testng.annotations.Test;

import java.util.HashSet;

import static org.testng.Assert.assertTrue;

public class AccessServiceDescriptionTest {

    @Test
    public void checkAccessServiceCodeIsUnique() {
        AccessServiceDescription accessServiceDescription;
        HashSet<Integer> set = new HashSet<>();
        boolean result = false;
        for( AccessServiceDescription desc: AccessServiceDescription.values()) {
            result = set.add(desc.getServiceCode());
            assertTrue(result);
        }

    }
}