<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Receiving Asset Consumer OMAS Events using Java

The Asset Consumer OMAS sends out events on its [OutTopic](https://egeria-project.org/concepts/out-topic)
whenever an asset is created or updated.

In the header of each
event (see `AssetConsumerEventHeader`) is an event version number and an event type enum.

 * The event version number indicates which version of the payload is in use.  With the version number in
place it is possible to change the payload over time and enable the consumer to adjust.

 * The event type enum defines the type of event.
 
Following the header are the common properties for all asset events.   This includes the latest version of
the asset, its origin and the license associated with the Asset's metadata.

Finally are the specialist extensions for each type of event.  New asset events include the creation time of the
asset.  Updated asset events include the update time and the original values of the asset (if available).

----



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.