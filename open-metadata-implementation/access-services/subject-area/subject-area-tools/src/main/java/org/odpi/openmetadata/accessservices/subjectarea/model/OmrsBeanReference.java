/**
 * Licensed to the Apache Software Foundation (ASF) under oneØ
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
