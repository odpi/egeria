/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.utilities;

import java.util.UUID;

/**
 * Holds the identifier of the operation running on the current thread.  An operation is a single command that can
 * write many instances - a delete that cascades through an anchored graph, an archive, a create from a template -
 * and every instance version it writes is stamped with this identifier (the lastOperationId in its instance
 * header).  That scopes the command: restore uses it to find exactly what one delete or archive changed, and it
 * can be used to check what any complex command touched.
 * <br><br>
 * The identifier is minted by the server for each operation and is held only while the operation runs.  It is
 * not the request id: a caller can supply the same request id on several calls, so a request id can span more
 * than one command.
 */
public class OperationId
{
    private static final ThreadLocal<String> operationId = new ThreadLocal<>();


    /**
     * Default constructor.
     */
    public OperationId()
    {
    }


    /**
     * Start an operation on the current thread.  A new identifier is minted unless an operation is already
     * running - a command that runs another, such as a delete that cascades into an anchored member, is part of
     * the same operation and shares its identifier.
     *
     * @return true if this call started the operation, and so must end it with {@link #endOperation()}
     */
    public boolean startOperation()
    {
        if (operationId.get() == null)
        {
            operationId.set(UUID.randomUUID().toString());

            return true;
        }

        return false;
    }


    /**
     * End the operation running on the current thread.
     */
    public void endOperation()
    {
        operationId.remove();
    }


    /**
     * Return the identifier of the operation running on the current thread.
     *
     * @return identifier, or null when no operation is running
     */
    public String getOperationId()
    {
        return operationId.get();
    }
}
