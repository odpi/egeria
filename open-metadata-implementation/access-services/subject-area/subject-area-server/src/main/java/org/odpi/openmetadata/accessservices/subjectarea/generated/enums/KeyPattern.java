/**
 * Licensed to the Apache Software Foundation (ASF) under one
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
package org.odpi.openmetadata.accessservices.subjectarea.generated.enums;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

// This is a generated file - do not edit - run ReferenceGenerator to regenerate this file.


/**
 *  Defines the type of identifier used for an asset.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum KeyPattern implements Serializable {
    /**
    Unique key allocated and used within the scope of a single system.
     */
    LocalKey
,
    /**
    Key allocated and used within the scope of a single system that is periodically reused for different records.
     */
    RecycledKey
,
    /**
    Key derived from an attribute of the entity, such as email address, passport number.
     */
    NaturalKey
,
    /**
    Key value copied from another system.
     */
    MirrorKey
,
    /**
    Key formed by combining keys from multiple systems.
     */
    AggregateKey
,
    /**
    Key from another system can bey used if system name provided.
     */
    CallersKey
,
    /**
    Key value will remain active even if records are merged.
     */
    StableKey
,
    /**
    Another key pattern.
     */
    Other
}
