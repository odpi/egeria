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

public class OmrsBeanRelationship {
    public String label = null;
    public String relationshipGuid;
    public String entityProxy1Guid;
    public String entityProxy1Type = null;
    public String entityProxy1Name = null;
    public String entityProxy2Guid;
    public String entityProxy2Type = null;
    public String entityProxy2Name = null;
    public List<OmrsBeanAttribute> attrList = null;
    public String description;
    public String modelName = null;
}
