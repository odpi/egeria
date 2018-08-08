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

@SuiteDisplayName("Governance Engine Server GovernanceEngineValidator")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)


public class GovernanceEngineValidatorTest {

    @Mock
    private OMRSRepositoryConnector omrsRepositoryConnector; // simple mock - we just need this to exist for our test class
    private Throwable thrown;
    static final String defMethodName = "GovernanceEngineValidatorTest()";
    static final String emptyString = "";
    static final String defUserId = "ernie46";
    static final String defParameterName = "parm1";
    static final List<String> singleword = Arrays.asList("one");
    static final List<String> multiword = Arrays.asList("one", "two", "three");
    static final List<String> wordsonebad = Arrays.asList("one", null, "three");
    static final List<String> emptyword = Arrays.asList("");

    @InjectMocks
    GovernanceEngineValidator validator;


    @Test
    @DisplayName("validateUserId - check null handled")
    void validateUserIdNull() {
        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            validator.validateUserId(null, defMethodName);
        });
        assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-400-003"));
    }

    @Test
    @DisplayName("validateUserId - check empty string handled")
    void validateUserIdEmpty() {
        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            validator.validateUserId(emptyString, defMethodName);
        });
        assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-400-003"));
    }

    @Test
    @DisplayName("validateUserId - check good userid passes")
    void validateUserIdGood() {
        try {
            validator.validateUserId(defUserId, defMethodName);
        } catch (InvalidParameterException e) {
        }
        ;
    }

    @Test
    @DisplayName("validateGuid - check null handled")
    void validateGuidNull() {
        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            validator.validateGUID(null, defParameterName, defMethodName);
        });
        assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-400-004"));
    }

    @Test
    @DisplayName("validateGuidId - check empty string handled")
    void validateGuidEmpty() {
        thrown = assertThrows(InvalidParameterException.class, () ->
        {
            validator.validateGUID(emptyString, defParameterName, defMethodName);
        });
        assertTrue(thrown.getMessage().contains("OMAS-GOVERNANCEENGINE-400-004"));
    }

    @Test
    @DisplayName("validateGuid - check good guid passes")
    void validateGuidGood() {
        try {
            validator.validateGUID(defUserId, defParameterName, defMethodName);
        } catch (InvalidParameterException e) {
        }
        ;
    }


    @Test
    @DisplayName("validateAssetType - check null handled")
    void validateAssetTypeNull() {
        try {
            validator.validateGUID(null, defParameterName, defMethodName);
        } catch (InvalidParameterException e) {
        }
        ;
    }

    @Test
    @DisplayName("validateAssetType - check null in multi values")
    void validateAssetTypeOneNull() {
        try {
            validator.validateType(wordsonebad, defParameterName, defMethodName);
        } catch (InvalidParameterException e) {
        }
        ;
    }

    @Test
    @DisplayName("validateAssetType - check empty string handled")
    void validateAssetTypeEmpty() {
        try {
            validator.validateType(emptyword, defParameterName, defMethodName);
        } catch (InvalidParameterException e) {
        }
        ;
    }

    @Test
    @DisplayName("validateAssetType - check single good value passes")
    void validateAssetTypeGoodSingle() {
        try {
            validator.validateType(singleword, defParameterName, defMethodName);
        } catch (InvalidParameterException e) {
        }
        ;
    }

    @Test
    @DisplayName("validateAssetType - check multi good value passes")
    void validateAssetTypeGoodMulti() {
        try {
            validator.validateType(multiword, defParameterName, defMethodName);
        } catch (InvalidParameterException e) {
        }
        ;
    }

    @Test
    @DisplayName("validateClassification - check null handled")
    void validateClassificationNull() {
        try {
            validator.validateClassification(null, defParameterName, defMethodName);
        } catch (InvalidParameterException e) {
        }
        ;
    }

    @Test
    @DisplayName("validateClassification - check null in multi values")
    void validateClassificationOneNull() {
        try {
            validator.validateClassification(wordsonebad, defParameterName, defMethodName);
        } catch (InvalidParameterException e) {
        }
        ;
    }

    @Test
    @DisplayName("validateClassification - check empty string handled")
    void validateClassificationEmpty() {
        try {
            validator.validateClassification(emptyword, defParameterName, defMethodName);
        } catch (InvalidParameterException e) {
        }
        ;
    }

    @Test
    @DisplayName("validateClassification - check single good value passes")
    void validateClassificationGoodSingle() {
        try {
            validator.validateType(singleword, defParameterName, defMethodName);
        } catch (InvalidParameterException e) {
        }
        ;
    }

    @Test
    @DisplayName("validateClassification - check multi good value passes")
    void validateClassificationGoodMulti() {
        try {
            validator.validateClassification(multiword, defParameterName, defMethodName);
        } catch (InvalidParameterException e) {
        }
        ;
    }

}
