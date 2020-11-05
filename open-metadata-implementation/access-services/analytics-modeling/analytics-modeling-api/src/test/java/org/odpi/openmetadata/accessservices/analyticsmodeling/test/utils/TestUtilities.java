/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerModule;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.BaseObjectType;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.Column;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.DataSource;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.ForeignKey;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.MetadataModule;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.PrimaryKey;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.PropertyType;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.Table;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class for static function helpers to setup, run, assert junit tests.
 * 
 *
 */
public class TestUtilities {
	

	static public void assertObjectJson(Object ds, String master) {
		ObjectMapper om = new ObjectMapper();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		try {
			om.writerWithDefaultPrettyPrinter().writeValue(baos, ds);
		} catch (IOException e) {
			fail("Can't write JSON for object " + ds.getClass().getName());
		}
		String output = new String(baos.toByteArray(), StandardCharsets.UTF_8);
		assertEquals(output, master, ds.getClass().getName() + " json failed.");
	}
	
	static public <T extends Object> T readObjectJson(String json, Class<T> cls) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json, cls);
		} catch (JsonProcessingException e) {
			fail("Can't read from JSON object " + cls.getName());
		}
		return null;	
	}

	public static String readJsonFile(String folder, String fileName) throws IOException {
		String root = Paths.get(".").toAbsolutePath().normalize().toString();
		String input  =  root + folder + fileName + ".json";
		return new String(Files.readAllBytes(Paths.get(input)));
	}

	/**
	 * Validate GUID uniqueness and replace them to predicted values for junit assert.
	 * @param moduleResponse to validate.
	 */
	public static void validateGUIDs(ResponseContainerModule moduleResponse) {
		MetadataModule module = moduleResponse.getPhysicalModule();
		HashSet<String> guids = new HashSet<>();
		
		fixGUID(module, guids);
		
		module.getDataSource().forEach(ds->{
			fixGUID(ds, guids);	// fix data source guid
			
			Optional.ofNullable(ds.getTable()).orElse(Collections.emptyList())
				.forEach(tbl->{
					fixGUID(tbl, guids);	// fix tables guid
					Optional.ofNullable(tbl.getTableItem()).orElse(Collections.emptyList())
						.forEach(item->{
							fixGUID(item.getColumn(), guids);	// fix column guid
						});
				});
		});
	}

	/**
	 * Fix GUIDs from random values to guid_'identifier'
	 * @param object having random GUID
	 * @param guids values from module 
	 */
	private static void fixGUID(BaseObjectType object, HashSet<String> guids) {
		PropertyType guid = object.getPropertyByName("guid");
		
		if (guid == null) {
			return;
		}
		
		assertFalse(guids.contains(guid.getValue()), "Not unique guid.");
		
		guids.add(guid.getValue());		// mark as guid that had been processed
		guid.setValue(generateGUID(object));	// set predefined guid value
	}

	private static String generateGUID(BaseObjectType object) {

		String prefix = object.getClass().getSimpleName() + "@";
		if (object instanceof Column) {
			return prefix + ((Column)object).getName();
		} else if(object instanceof Table) {
			return prefix + ((Table)object).getName();
		} else if(object instanceof DataSource) {
			return prefix + ((DataSource)object).getName();
		} else if(object instanceof MetadataModule) {
			return prefix + ((MetadataModule)object).getIdentifier();
		} 

		fail("Unknown class: " + object.getClass().getName());
		return null;
	}
}
