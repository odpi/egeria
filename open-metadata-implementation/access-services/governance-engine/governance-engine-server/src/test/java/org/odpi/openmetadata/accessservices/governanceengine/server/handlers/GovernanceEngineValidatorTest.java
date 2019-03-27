/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceengine.server.handlers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.odpi.openmetadata.accessservices.governanceengine.server.handlers.GovernanceEngineValidator.validateGUID;
import static org.odpi.openmetadata.accessservices.governanceengine.server.handlers.GovernanceEngineValidator.validateUserId;

@SuiteDisplayName("Governance Engine Server GovernanceEngineValidator")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
public class GovernanceEngineValidatorTest {

    private static final String defMethodName = "GovernanceEngineValidatorTest()";
    private static final String emptyString = "";
    private static final String defUserId = "ernie46";
    private static final String defParameterName = "parm1";
    
    @InjectMocks
    GovernanceEngineValidator validator;
    @Mock
    private OMRSRepositoryConnector omrsRepositoryConnector; // simple mock - we just need this to exist for our test class
    private Throwable thrown;

    @Test
    @DisplayName("validateUserId - check null handled")
    void validateUserIdNull() {
        thrown = assertThrows(InvalidParameterException.class, () -> validateUserId(null, defMethodName));
        assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-400-003"));
    }

    @Test
    @DisplayName("validateUserId - check empty string handled")
    void validateUserIdEmpty() {
        thrown = assertThrows(InvalidParameterException.class, () -> validateUserId(emptyString, defMethodName));
        assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-400-003"));
    }

    @Test
    @DisplayName("validateUserId - check good userid passes")
    void validateUserIdGood() {
        try {
            validateUserId(defUserId, defMethodName);
        } catch (InvalidParameterException e) {
        }
    }

    @Test
    @DisplayName("validateGuid - check null handled")
    void validateGuidNull() {
        thrown = assertThrows(InvalidParameterException.class, () -> validateGUID(null, defParameterName, defMethodName));
        assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-400-004"));
    }

    @Test
    @DisplayName("validateGuid - check good guid passes")
    void validateGuidGood() {
        try {
            validateGUID(defUserId, defParameterName, defMethodName);
        } catch (InvalidParameterException e) {
        }
    }


    @Test
    @DisplayName("validateAssetType - check null handled")
    void validateAssetTypeNull() {
        try {
            validateGUID(null, defParameterName, defMethodName);
        } catch (InvalidParameterException e) {
        }
    }

}
