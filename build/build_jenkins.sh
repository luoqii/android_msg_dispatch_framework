#! /bin/bash
#
#
#
#
# use for jenkins only.
# sketch:
#export PATH=/home/bysong/adt-bundle-linux-x86_64/sdk/tools/:$PATH
#export ANDROID_TARGET="android-16"
#./build/build.sh
#
#

# you must export $ANDROID_TARGET
# bysong@tudou.com

echo $WORKSPACE
echo $PATH
mv $WORKSPACE/project.properties $WORKSPACE/project.properties.bak
android update project -p . -t $ANDROID_TARGET
cd $WORKSPACE
ant release