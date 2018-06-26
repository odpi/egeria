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
 *  Defines the level of confidence to place in the accuracy of a data item.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum ConfidenceLevel implements Serializable {
    /**
    There is no assessment of the confidence level of this data.
     */
    Unclassified
,
    /**
    The data comes from an ad hoc process.
     */
    AdHoc
,
    /**
    The data comes from a transactional system so it may have a narrow scope.
     */
    Transactional
,
    /**
    The data comes from an authoritative source.
     */
    Authoritative
,
    /**
    The data is derived from other data through an analytical process.
     */
    Derived
,
    /**
    The data comes from an obsolete source and must no longer be used.
     */
    Obsolete
,
    /**
    Another confidence level.
     */
    Other
}
