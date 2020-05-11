/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.cognos.utils;

import org.springframework.util.StringUtils;

public class QualifiedNameUtils {

	public static final String SEPARATOR = "::";
	public static final String EQUALS = "=";
	public static final String OPEN_BRACKET = "(";
	public static final String CLOSE_BRACKET = ")";

	public static String buildQualifiedName(String parentQualifiedName, String typeName, String value) {
		StringBuilder sb = new StringBuilder();
		sb.append(StringUtils.isEmpty(parentQualifiedName) ? "" : (parentQualifiedName + SEPARATOR))
		.append(OPEN_BRACKET)
		.append(typeName)
		.append(CLOSE_BRACKET)
		.append(EQUALS)
		.append(value);
		return sb.toString();
	}
}
