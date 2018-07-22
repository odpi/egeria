/**
 * Licensed to the Apache Software Foundation (ASF) under oneØ
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.odpi.openmetadata.accessservices.subjectarea.model;

public class OmrsBeanAttribute {
    public String name = null;
    public String description = null;
    public String type = null;
    public boolean isEnum = false;
    public boolean isMap = false;
    public boolean isList = false;
    public boolean isSet = false;
    // it is useful to process references as attributes- in that case stash the package for reference here.
    public boolean isReference = false;
    public String referencePackage;

    // reference relationship
    public String referenceRelationshipName;
    // TODO unique attribute ?
}
