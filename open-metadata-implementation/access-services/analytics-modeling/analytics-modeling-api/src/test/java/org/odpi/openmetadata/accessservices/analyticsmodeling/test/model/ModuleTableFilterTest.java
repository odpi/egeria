/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.test.model;

import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.util.Arrays;
import java.util.List;

import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ModuleTableFilter;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.JsonMocks;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.TestUtilities;
import org.testng.annotations.Test;

public class ModuleTableFilterTest {

	private List<String> tables = Arrays.asList("ORDERDETAIL", "PRODUCT");

	@Test
	public void toJsonExcludedTables() {
		ModuleTableFilter obj = new ModuleTableFilter();
		obj.getMeta().setExcludedTables(tables);
		TestUtilities.assertObjectJson(obj, JsonMocks.getModuleTableExcludedFilterBody());
	}
	@Test
	public void toJsonIncludedTables() {
		ModuleTableFilter obj = new ModuleTableFilter();
		obj.getMeta().setIncludedTables(tables);
		TestUtilities.assertObjectJson(obj, JsonMocks.getModuleTableIncludedFilterBody());
	}
	@Test
	public void fromJsonExcludedTables() {
		ModuleTableFilter obj = TestUtilities.readObjectJson(JsonMocks.getModuleTableExcludedFilterBody(), ModuleTableFilter.class);
		assertEquals(tables, obj.getMeta().getExcludedTables());
		assertNull(obj.getMeta().getIncludedTables());
	}
	@Test
	public void fromJsonIncludedTables() {
		ModuleTableFilter obj = TestUtilities.readObjectJson(JsonMocks.getModuleTableIncludedFilterBody(), ModuleTableFilter.class);
		assertEquals(tables, obj.getMeta().getIncludedTables());
		assertNull(obj.getMeta().getExcludedTables());
	}
	@Test
	public void filterIncludedTables() {
		ModuleTableFilter obj = TestUtilities.readObjectJson(JsonMocks.getModuleTableIncludedFilterBody(), ModuleTableFilter.class);
		assertTrue(obj.match(tables.get(0)));
		assertFalse(obj.match("NOT_INCLUDED_TABLE"));
	}
	@Test
	public void filterExcludedTables() {
		ModuleTableFilter obj = TestUtilities.readObjectJson(JsonMocks.getModuleTableExcludedFilterBody(), ModuleTableFilter.class);
		assertFalse(obj.match(tables.get(0)));
		assertTrue(obj.match("NOT_EXCLUDED_TABLE"));
	}
	@Test
	public void filterEmptyTables() {
		ModuleTableFilter obj = new ModuleTableFilter();
		assertTrue(obj.match(tables.get(0)));
	}

}
