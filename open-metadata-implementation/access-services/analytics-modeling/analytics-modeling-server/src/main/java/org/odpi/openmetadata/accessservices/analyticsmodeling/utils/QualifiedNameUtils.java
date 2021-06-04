/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.IdMap;

public class QualifiedNameUtils {

	public static final String SEPARATOR = "::";
	public static final String EQUALS = "=";
	public static final String OPEN_BRACKET = "(";
	public static final String CLOSE_BRACKET = ")";
	
	private static final String ID_SEPARATOR_REGEX = escapeQualifiedNameRegExPattern(
			new StringBuilder()
			.append(SEPARATOR)
			.append(OPEN_BRACKET)
			.append(IdMap.SCHEMA_ATTRIBUTE_TYPE_NAME)
			.append(CLOSE_BRACKET)
			.append(EQUALS)
			.toString()
			);

	public static String buildQualifiedName(String parentQualifiedName, String typeName, String value) {
		StringBuilder sb = new StringBuilder();
		sb.append((parentQualifiedName == null || parentQualifiedName.isEmpty()) ? "" : (parentQualifiedName + SEPARATOR))
		.append(OPEN_BRACKET)
		.append(typeName)
		.append(CLOSE_BRACKET)
		.append(EQUALS)
		.append(value);
		
		return sb.toString();
	}
	
	
	/**
	 * Extract identifiers from qualified name.
	 * @param qualifiedName like "(SoftwareServerCapability)=xxx::(Asset)=yyy::(SchemaAttribute)=NAME1::(SchemaAttribute)=NAME2"
	 * @return stack of identifiers.
	 */
	public static List <String> extractIdentifiersFromQualifiedName(String qualifiedName) {
		
		if (qualifiedName == null || qualifiedName.isEmpty()) {
			return Collections.emptyList();	// throw exception ?
		}
		
		return Arrays.stream(qualifiedName.split(ID_SEPARATOR_REGEX))
				.skip(1).collect(Collectors.toList());
	}
	
	/**
	 * Escape meta characters in qualifies name. 
	 * @param input with qualified name.
	 * @return escaped regex.
	 */
	public static String escapeQualifiedNameRegExPattern(String input) {
		return input.replace("(", "\\(").replace(")", "\\)");
	}

}
