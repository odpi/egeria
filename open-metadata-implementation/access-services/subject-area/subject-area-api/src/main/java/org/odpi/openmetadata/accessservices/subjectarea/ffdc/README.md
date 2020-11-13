<!--SPDX-License-Identifier: Apache-2.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# SubjectArea OMAS FFDC

Package org.odpi.openmetadata.accessservices.subjectarea.common.ffdc provides the
first failure data capture support for the SubjectArea OMAS module.
This includes an error code enum,
a runtime exception, a base class for checked exceptions plus
implementation of each specific checked exception.

The error code enum (SubjectAreaErrorCode) has an entry for each unique situation
where an exception is returned.  Each entry defines:

* A unique id for the error
* An HTTP error code for rest calls
* A unique message Id
* Message text with place holders for specific values
* A description of the cause of the error and system action as a result.
* A description of how to correct the error (if known)

Each exception (whether a checked or runtime exception) has two constructors.

* The first constructor is used when a new error has been detected.

* The second constructor is used when another exception has been caught.
This caught exception is passed on the constructor so it is effectively
embedded in the Subject Area OMAS exception.

Both constructors take the values from the SubjectAreaErrorCode
enum to define the cause and resolution.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
