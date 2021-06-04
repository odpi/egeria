/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.test;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

class QualifiedNameUtilsTest {

	@Test
	void testPatternAllAssetEntities() {
		
		String regex = "\\(SoftwareServerCapability\\)=http://localhost:9300/p2pd/servlet::\\(Asset\\)=i5061E09451574F9290412133FEE9BB5A.*";
		String test = "(SoftwareServerCapability)=http://localhost:9300/p2pd/servlet::(Asset)=i5061E09451574F9290412133FEE9BB5A::(SchemaAttribute)=Country";
		assertTrue(test.matches(regex));
	}

}
