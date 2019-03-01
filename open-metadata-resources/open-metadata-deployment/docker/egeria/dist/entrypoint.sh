#!/bin/bash
# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project.
set -e

# Set java debug if JAVA_DEBUG is set
export JAVA_TOOL_OPTIONS="${JAVA_DEBUG:+"-agentlib:jdwp=transport=dt_socket,address=5005,server=y,suspend=n"}"

# Launch whatever was called
exec "$@"
