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

LOCAL_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

echo 'Extracting base version...'
BASE_VERSION=$(mvn help:evaluate -Dexpression=project.version | grep -Ev '(^\[|Download\w+:)' | sed -r "s/([0-9]+).([0-9]+)(.*)/\1.\2/g")
echo "Base version ${BASE_VERSION}"

echo 'Refreshing local tags from remote...'
git tag -l | xargs git tag -d
git fetch --tags

GREP_SEARCH="^v${BASE_VERSION}"
echo 'Calculating last released version...'
LAST_RELEASED_VERSION=$(git tag --sort=v:refname | grep -e ${GREP_SEARCH} | sed -r "s/v(.*)/\1/g" | tail -n 1)


echo 'Calculating release version...'
if [ -z "$LAST_RELEASED_VERSION" ]; then
    echo "This is the first release for base version ${BASE_VERSION}"
    RELEASE_VERSION="$BASE_VERSION.0"
else
    echo "Last release version for base version ${BASE_VERSION} is ${LAST_RELEASED_VERSION}"
    # See https://stackoverflow.com/a/14348899/462152
    RELEASE_VERSION=$(echo ${LAST_RELEASED_VERSION} | sed -r 's/('"${BASE_VERSION}"'.)(.*)/echo \1$(echo "\2 + 1"|bc)/ge')
fi

echo "Release version is ${RELEASE_VERSION}"

mvn versions:set -DnewVersion=${RELEASE_VERSION}
mvn -N versions:update-child-modules
mvn -s ${LOCAL_DIR}/settings.xml -DskipUnitTests=true deploy


RELEASE_TAG="v${RELEASE_VERSION}"
echo "Tagging source with ${RELEASE_TAG} tag"

git tag -a ${RELEASE_TAG} -m "Release version "${RELEASE_VERSION}
git push origin ${RELEASE_TAG}





