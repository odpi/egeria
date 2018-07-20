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
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.node;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;

public class NodeUtils {
    private static final String className = NodeUtils.class.getName();
    /**
     * Found a goverance classification in the supplied classifications - reject this.
     * @param classificationName
     * @throws InvalidParameterException
     */
    public static void foundGovernanceClassifications(String classificationName) throws InvalidParameterException {
        SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.GOVERNNCE_CLASSIFICATION_SUPPLIED_IN_CLASSIFICATIONS;
        String errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(className,
                classificationName );
        throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                className,
                "foundGovernanceClassifications",
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }
}
