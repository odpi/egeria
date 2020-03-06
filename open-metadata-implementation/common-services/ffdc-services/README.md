<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![Released](../../../open-metadata-publication/website/images/egeria-content-status-released.png#pagewidth)

# First Failure Data Capture (FFDC) Services

The FFDC Services provide common exception and exception handling services for
clients and specialized services running in the OMAG Server Platform.

First Failure Data Capture (FFDC) is an approach to error handling that aims to guide
the support team to the cause of an error based on the output of a single
message.  This is not always possible, particularly for bugs and unexpected
runtime conditions, but it is a worthy goal because it is rarely practical
to turn on debug tracing in a runtime system.

FFDC requires careful design by the developer because they need to anticipate
the likely errors and design the error handling accordingly.

Here are some of the basic aspects of designing for FFDC.

* Ensure that each type of message has a unique identifier and
  The parameters embedded in it are sufficient to determine the
  call parameters and the code path to the point where the error
  is detected.

* Use different types of exceptions to separate:
  * Invalid parameters from the caller
  * User security errors that need administrator action
  * Temporary problems in the server
  * Bugs and logic errors (ie reaching a point in the path that should be impossible).
  
  Typically, it is helpful to use checked exceptions for the first three types of errors and
  runtime exceptions for the last.
  
* Where there is no direct external caller, consider logging an AuditLog error
  rather than throwing an exception.
  
* Avoid serializing Exception objects generated in the server because they
  contain the stack trace from the server.  Capture the type of exception,
  and error, user, and server messages from the exception and send those
  as the response.  The client can then recreate the exception with the
  messages and without the stack trace.

The FFDC Services provide base services for implementing FFDC in an Egeria
module.

 * Common audit log messages and exception codes.
 * Common exceptions and base exceptions.
 * Common REST Structures.
 * Invalid parameter handler for common parameter types.
 * REST Call logger for debug messages and performance logging.
 * Handler for common exceptions.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.