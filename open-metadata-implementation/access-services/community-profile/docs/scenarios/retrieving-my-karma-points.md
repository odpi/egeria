<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

## Retrieving my karma points

If an individual has a
[Personal profile](../concepts/personal-profile.md)
the Community Profile OMAS will reward him/her whenever
they contribute to open metadata.
These rewards are in the form of
[karma points](../concepts/karma-point.md).

The Community Profile OMAS is responsible for maintaining the count of
the karma points.  It does this by listening to the metadata changes
occurring in the metadata repositories and updates the personal
profile of each user making a contribution.

The Community Profile OMAS provides a method/operation
to allow an individual to retrieve their current karma point
total.

* [Using Java to query my karma points]
* [Using the REST API to query my karma points]

The access service option property **"KarmaPointPlateau"** indicates
the multiple of karma points for an individual that results in
an external event being published - the default is 500.  This
means that when an individual gets to 500 karma points, and event is sent,
and other event is sent when they get to 1000 karma points and so on.
These events can be used to trigger additional recognition activities for
the individuals concerned.




----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.