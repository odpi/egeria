<!-- SPDX-License-Identifier: Apache-2.0 -->

# Open Metadata Repository Services (OMRS) FFDC

Package org.apache.atlas.omrs.ffdc provides the first failure data capture
support for the OMRS module.  This includes an error code enum,
a runtime exception, a base class for checked exceptions plus
implementation of each specific checked exception.

The error code enum (OMRSErrorCode) has an entry for each unique situation
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
embedded in the OMRS exception.

Both constructors take the values from the OMRSErrorCode
enum to define the cause and resolution.  These values are passed
as individual parameters so that subclasses implemented outside of
the OMRS packages can define their own FFDC values.