#!/bin/bash

set -e

if [ -z ${BINTRAY_USERNAME+x} ];
    then echo "BINTRAY_USERNAME is unset";
    exit 1;
fi

if [ -z ${BINTRAY_PASSWORD+x} ];
    then echo "BINTRAY_PASSWORD is unset";
    exit 1;
fi

MYDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

BUILD_NUMBER=${CIRCLE_BUILD_NUM}

CURRENT_VERSION=$(mvn help:evaluate -Dexpression=project.version | grep -Ev '(^\[|Download\w+:)')
NEW_VERSION=$(echo ${CURRENT_VERSION} | sed -e "s/-SNAPSHOT/\.$BUILD_NUMBER/")

mvn versions:set -DnewVersion=${NEW_VERSION}
mvn -N versions:update-child-modules
mvn -s ${MYDIR}/settings.xml -DskipUnitTests=true deploy




