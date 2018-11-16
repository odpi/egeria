/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.model;

import java.util.List;

public class OmrsBeanRelationship {
    public String label = null;
    public String relationshipGuid;
    public String entityProxy1Guid;
    public String entityProxy1Type = null;
    public String entityProxy1Name = null;
    public String entityProxy2Guid;
    public String entityProxy2Type = null;
    public String entityProxy2Name = null;
    public String typeDefGuid =null;
    public List<OmrsBeanAttribute> attrList = null;
    public String description;
    public String modelName = null;
}
