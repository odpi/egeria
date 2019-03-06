# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project

FROM alpine:3.9

# Install utilities we will use to configure containers
RUN apk add bash curl git jq postgresql-client mariadb-client && apk update && apk upgrade
COPY dist/check-availability.sh /check-availability.sh

ENV CACERT /var/run/secrets/kubernetes.io/serviceaccount/ca.crt
ENV NAMESPACE default

ENTRYPOINT ["/check-availability.sh"]
