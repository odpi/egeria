/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;

public class NodeUtils {
    private static final String className = NodeUtils.class.getName();
    /**
     * Found a goverance classification in the supplied classifications - reject this.
     * @param classificationName name of classification
     * @throws InvalidParameterException Governance classification supplied as classifications.
     */
    public static void foundGovernanceClassifications(String classificationName) throws InvalidParameterException {
        SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.GOVERNANCE_CLASSIFICATION_SUPPLIED_IN_CLASSIFICATIONS;
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
