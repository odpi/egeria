<!-- SPDX-License-Identifier: Apache-2.0 -->

# Data Platform Open Metadata Access Service (OMAS)

The Data Platform OMAS provides APIs for tools and applications wishing to register
new data assets.  It provides the ability to register the host, platform and location of the
data platform itself along with the data assets it hosts.

There are specific APIs for different types of data platforms and assets.  These reflect
the terminology typically associated with the specific type of data platform to make it easier
for people to map the Data Platform OMAS APIs and events to the actual technology.
However, the specific implementation objects supported by thees APIs all inherit from common
classes so it is possible to use this interface in a technology-agnostic mode.

To date, the Data Platform OMAS supports:

* Relational database platforms

More will follow.

The Data Platform OMAS APIs need to accommodate slight variations between different vendor
implementations of data platforms, along with information relating to local deployment standards.
As such there is provision in these interfaces to support:

* VendorProperties for properties unique to a specific vendor implementation and
* AdditionalProperties for properties that the infrastructure team wish to add to the metadata.

