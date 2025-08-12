#!/bin/sh

# ----------------------------------------------------------------------------
# Maven Wrapper start script for POSIX environments
# ----------------------------------------------------------------------------

set -euo pipefail

APP_HOME=$(cd "$(dirname "$0")"; pwd -P)
WRAPPER_JAR="$APP_HOME/.mvn/wrapper/maven-wrapper.jar"
WRAPPER_PROPERTIES="$APP_HOME/.mvn/wrapper/maven-wrapper.properties"

DOWNLOAD_URL=$(grep -E '^wrapperUrl=' "$WRAPPER_PROPERTIES" | cut -d'=' -f2-)

if [ ! -f "$WRAPPER_JAR" ]; then
  echo "Downloading Maven Wrapper jar..."
  curl -fsSL "$DOWNLOAD_URL" -o "$WRAPPER_JAR"
fi

JAVA_EXE="java"
if [ -n "${JAVA_HOME:-}" ]; then
  JAVA_EXE="$JAVA_HOME/bin/java"
fi

exec "$JAVA_EXE" -cp "$WRAPPER_JAR" org.apache.maven.wrapper.MavenWrapperMain "$@"
