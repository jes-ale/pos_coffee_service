#!/bin/sh
# ensure jvm installed
REQUIRED_PKG="openjdk-11-jdk"
PKG_OK=$(dpkg-query -W --showformat='${Status}\n' $REQUIRED_PKG|grep "install ok installed")
echo Checking for $REQUIRED_PKG: $PKG_OK
if [ "" = "$PKG_OK" ]; then
      echo "No $REQUIRED_PKG. Setting up $REQUIRED_PKG."
        sudo apt-get --yes install $REQUIRED_PKG
fi
# load .env file
[ ! -f .env ] || export $(grep -v '^#' .env | xargs)
# build JVM executable
./gradlew installDist
# run generated !bin/sh (runs the JVM executable)
cd build/install/com.coffee_service.quadro.org.manufacture_service/bin
chmod +x com.coffee_service.quadro.org.manufacture_service
if [ $1 = "dev" ]; then
./com.coffee_service.quadro.org.manufacture_service
fi
if [ $1 = "prod" ]; then
./com.coffee_service.quadro.org.manufacture_service &
