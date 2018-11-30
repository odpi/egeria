/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.apache.gaiandb.security

import com.ibm.gaiandb.GaianAuthenticator
import com.ibm.gaiandb.Logger
import org.apache.gaiandb.security.ProxyUserAuthenticator

class ProxyUserAuthenticatorTest extends spock.lang.Specification {
    def "check basic auth works ok"() {
        given: "A new instance of the auth class"
         def proxyUserAuthenticator = new ProxyUserAuthenticator()
         def props = new Properties()

        and: "A base authenticator that always succeeds"
        GaianAuthenticator basAuth = Stub(GaianAuthenticator.class)
        basAuth.authenticateUser(_,_,_,_) >> true

        and: "A log method that does nothing"
        Logger logger = Stub(Logger.class)

        when: "We try to auth with default user/pass"
        boolean IsAuth = proxyUserAuthenticator.authenticateUser('gaiandb','passw0rd','gaiandb',props)

        then: "auth passes"
        IsAuth == true
    }


}
