/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


/**
 * OMAS permissions
 * WRITE - can create children
 * UPDATE - can update
 * DELETE -can delete
 */

// For the future we could include ATTRIBUTE, ARRAY, MAP, ENUM here so that the tree could naturally show these things
@JsonAutoDetect(getterVisibility= JsonAutoDetect.Visibility.PUBLIC_ONLY, setterVisibility= JsonAutoDetect.Visibility.PUBLIC_ONLY, fieldVisibility= JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum Permission {
    WRITE, UPDATE, DELETE
}
