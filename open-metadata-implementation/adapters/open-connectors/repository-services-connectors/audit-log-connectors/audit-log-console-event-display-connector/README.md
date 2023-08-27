<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# Event Display Audit Log Connector (event-display-audit-log-connector)

The *event display audit log connector* is an *Audit Log Store Connector* that outputs
the content of events flowing between the different servers in the open metadata ecosystem.

By default event contents are not displayed on the console because they may contain sensitive
content.  However, the event content is stored in the *additionalInformation*
field of the log record.  This connector detects log record that describe events and
outputs the event content as part of displaying the message.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.