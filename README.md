# LocMess
CMU Project


Project made for the Mobile and Ubiquitous Computing Course

This project consists in a Client Server Solution to store notes in public places. The user through is smartphone can create virtual locations based on Bluetooth beacons or GPS positions and later can post public notes to existing locations. This system works by checking the GPS positons of clients against the existing Location List. If the client applicattion (Android app) detects the presence of a location in the current position of the user (GPS position or Bluetooth address), it requests to the server the list of messages in this location.
The user can also add key-value pairs to his profile (eg. Club = Benfica) and messages can be targeted to specific key-value pairs or not shown to a specific key-value pair.

The Client Application was made to work in Android and the Server side is a REST API secured with Basic HTTP Authentication made with Spring Framework.

More detail can be found in the final report uploaded to this repository.
