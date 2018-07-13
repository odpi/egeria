/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.compliance.ffdc;

/**
 * AssertionFailureException is used when a test case fails an assertion.
 */
public class AssertionFailureException extends ComplianceException
{
    private String assertionId;

    public AssertionFailureException(String    assertionId,
                                     String    assertionMessage)
    {
        super(assertionMessage);
        this.assertionId = assertionId;
    }


    /**
     * Return the identifier of the assertion that failed.
     *
     * @return string identifier
     */
    public String getAssertionId()
    {
        return assertionId;
    }
}
