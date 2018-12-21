/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.handlers;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.testng.annotations.Test;

;
import static org.testng.Assert.assertFalse;

public class TestErrorHandler
{
    @Test
    void testErrorMessageInserts() {
        try
        {
            ErrorHandler.validateGUID(null,"2","3");
        } catch (InvalidParameterException e)
        {
            assertFalse(e.getErrorMessage().contains("{"));
            assertFalse(e.getErrorMessage().contains("}"));
        }
        try
        {
            ErrorHandler.validateEnum(null,"2","3");
        } catch (InvalidParameterException e)
        {
            assertFalse(e.getErrorMessage().contains("{"));
            assertFalse(e.getErrorMessage().contains("}"));
        }
        try
        {
            ErrorHandler.validateName(null,"2","3");
        } catch (InvalidParameterException e)
        {
            assertFalse(e.getErrorMessage().contains("{"));
            assertFalse(e.getErrorMessage().contains("}"));
        }
        try
        {
            ErrorHandler.validateText(null,"2","3");
        } catch (InvalidParameterException e)
        {
            assertFalse(e.getErrorMessage().contains("{"));
            assertFalse(e.getErrorMessage().contains("}"));
        }

        try
        {
            ErrorHandler.handleEntityNotKnownError("1","2","3","4");
        } catch (UnrecognizedGUIDException e)
        {
            assertFalse(e.getErrorMessage().contains("{"));
            assertFalse(e.getErrorMessage().contains("}"));

        }
        try
        {
            ErrorHandler.handleEntityProxyOnlyException(new org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException(1,"1","2","3","4","5"),"6","7","8");
        } catch (MetadataServerUncontactableException e)
        {
            assertFalse(e.getErrorMessage().contains("{"));
            assertFalse(e.getErrorMessage().contains("}"));
        }

        try
        {
            ErrorHandler.handleUnauthorizedUser("1","2","3","4");
        } catch (UserNotAuthorizedException e)
        {
            assertFalse(e.getErrorMessage().contains("{"));
            assertFalse(e.getErrorMessage().contains("}"));
        }
        try
        {
            ErrorHandler.handleInvalidParameterException(new org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException(1,"1","2","3","4","5"),"6","7","8");
        } catch (InvalidParameterException e)
        {
            assertFalse(e.getErrorMessage().contains("{"));
            assertFalse(e.getErrorMessage().contains("}"));
        }
        try
        {
            ErrorHandler.handleClassificationErrorException(new org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException(1,"1","2","3","4","5"),"6","7","8");
        } catch (ClassificationException e)
        {
            assertFalse(e.getErrorMessage().contains("{"));
            assertFalse(e.getErrorMessage().contains("}"));
        }
        try
        {
            ErrorHandler.handleEntityNotDeletedException("1","2","3","4");
        } catch (GUIDNotDeletedException e)
        {
            assertFalse(e.getErrorMessage().contains("{"));
            assertFalse(e.getErrorMessage().contains("}"));
        }
        try
        {

            ErrorHandler.handleEntityNotDeletedException(new org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotDeletedException(1,"2","3","4","5","6"), "7","8","9","10" );

        } catch (GUIDNotPurgedException e)
        {
            assertFalse(e.getErrorMessage().contains("{"));
            assertFalse(e.getErrorMessage().contains("}"));
        }
        try
        {
            ErrorHandler.handleTypeErrorException(new org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException(1,"2","3","4","5","6"),"7","8","9");
        } catch (InvalidParameterException e)
            {
            assertFalse(e.getErrorMessage().contains("{"));
            assertFalse(e.getErrorMessage().contains("}"));
        }

        try
        {
            ErrorHandler.handleFunctionNotSupportedException(new org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException(1,"2","3","4","5","6"),"7","8","9");
        } catch (FunctionNotSupportedException e)
        {
            assertFalse(e.getErrorMessage().contains("{"));
            assertFalse(e.getErrorMessage().contains("}"));
        }

        try
        {
            ErrorHandler.handlePagingErrorException(new org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException(1,"2","3","4","5","6"),"7","8","9");
        } catch (InvalidParameterException e)
        {
            assertFalse(e.getErrorMessage().contains("{"));
            assertFalse(e.getErrorMessage().contains("}"));
        }
        try
        {
            ErrorHandler.handleEntityNotDeletedException("1","2","3","4");
        } catch (GUIDNotDeletedException e)
        {
            assertFalse(e.getErrorMessage().contains("{"));
            assertFalse(e.getErrorMessage().contains("}"));
        }

        try
        {
            ErrorHandler.handleUnknownAsset(new Exception("error msg 1"),"1","2","3","4");
        } catch (UnrecognizedGUIDException e)
        {
            assertFalse(e.getErrorMessage().contains("{"));
            assertFalse(e.getErrorMessage().contains("}"));
        }
        try
        {
            ErrorHandler.handleTypeDefNotKnownException("1","2","3","4");
        } catch (InvalidParameterException e)
        {
            assertFalse(e.getErrorMessage().contains("{"));
            assertFalse(e.getErrorMessage().contains("}"));
        }
        try
        {
            ErrorHandler.handleStatusNotSupportedException(new org.odpi.openmetadata.repositoryservices.ffdc.exception.StatusNotSupportedException(1,
                    "1","2","3","4","5"),"4","5","6");
        } catch (StatusNotSupportedException e)
        {
            assertFalse(e.getErrorMessage().contains("{"));
            assertFalse(e.getErrorMessage().contains("}"));
        }
        try
        {
            ErrorHandler.handleRepositoryError(new Exception("error msg 1"),"1","2","3");
        } catch (MetadataServerUncontactableException e)
        {
            assertFalse(e.getErrorMessage().contains("{"));
            assertFalse(e.getErrorMessage().contains("}"));
        }
        try
        {
            ErrorHandler.handleRelationshipNotKnownException("1","2","3","4");

        } catch (UnrecognizedGUIDException e)
        {
            assertFalse(e.getErrorMessage().contains("{"));
            assertFalse(e.getErrorMessage().contains("}"));
        }
        try
        {
            ErrorHandler.handleRelationshipNotDeletedException("1","2","3","4");
        } catch (GUIDNotDeletedException e)
        {
            assertFalse(e.getErrorMessage().contains("{"));
            assertFalse(e.getErrorMessage().contains("}"));
        }


    }
}
