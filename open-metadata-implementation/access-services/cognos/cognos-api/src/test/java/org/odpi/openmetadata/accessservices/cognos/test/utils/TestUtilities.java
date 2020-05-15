/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.cognos.test.utils;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

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
		assertEquals(master, output, ds.getClass().getName() + " json failed.");
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


}
