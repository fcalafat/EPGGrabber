EPGGrabber
==========

Simple Java Grabber to retreive zipped xmltv files

It can also serve to simply download and extract zip files from an url on a regular basis


Usage
=====

Just modify config.properties file to setup the following properties:

INPUT_URL=Url pointing to the zip you want to download
REFRESH_PERIOD=In number of days. Time since last refresh before downloading a new file
REFRESH_TIME=In minutes. Time between each check
OUTPUT_PATH=Where to put the extracted file(s)
