/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.apache.ranger.services.gaian

import org.odpi.openmetadata.adapters.governanceenginesplugins.gaianrangerplugin.RangerPolicyResultFilter

class RangerPolicyResultFilterTest extends spock.lang.Specification {
    def "check SetForwardingNode always returns true"() {
        given:
        def rangerPolicyResultFilter = new RangerPolicyResultFilter()

        expect: "verify we get true regardless of input"
        result == rangerPolicyResultFilter.setForwardingNode(inputString)

        where:
        inputString | result
        "banana"    | true
        null        | true
    }

    def "check setUserCredentials always returns true"() {
        given:
        def rangerPolicyResultFilter = new RangerPolicyResultFilter()

        expect: "verify we get true regardless of input"
        result == rangerPolicyResultFilter.setUserCredentials(inputString)

        where:
        inputString | result
        "banana"    | true
        null        | true
    }
}
