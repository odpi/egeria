<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# addLogMessageToAsset

```
   /**
     * Creates an Audit log record about the asset.  This log record is stored in the local server's Audit Log.
     *
     * @param userId               userId of user making request.
     * @param requestType            unique id for the asset.
     * @param connectorInstanceId  (optional) id of connector in use (if any).
     * @param connectionName       (optional) name of the connection (extracted from the connector).
     * @param connectorType        (optional) type of connector in use (if any).
     * @param contextId            (optional) function name, or processId of the activity that the caller is performing.
     * @param message              log record content.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException There is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void  addLogMessageToAsset(String userId,
                               String requestType,
                               String connectorInstanceId,
                               String connectionName,
                               String connectorType,
                               String contextId,
                               String message) throws InvalidParameterException,
                                                      PropertyServerException,
                                                      UserNotAuthorizedException;
```




----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.