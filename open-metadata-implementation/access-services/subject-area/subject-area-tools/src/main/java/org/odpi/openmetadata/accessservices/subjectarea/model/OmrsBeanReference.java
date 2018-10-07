/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.model;

import java.util.List;

public class OmrsBeanReference {
    // relationship information
    public String relationshipType = null;
    public String relationshipGuid;
    // end / reference specific fields
    public String relatedEndGuid;
    public String relatedEndType = null;
    // first character uppercase
    public String uReferenceName = null;
    // first character lower case
    public String referenceName = null;
    public List<OmrsBeanAttribute> attrList = null;
    public String myType = null;
    public String description;
//    public String modelName = null;
//    public boolean isList = false;
    public boolean isSet = false;
}
