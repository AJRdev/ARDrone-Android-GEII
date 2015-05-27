#ARDrone-Android

##Description of the Project:


The aim of this project is to completely control and monitor the Parrot AR. Drone 2.0 quadricopter from the Android platform.
Including controlling the Drone with external devices connected by Bluetooth to the phone such as gamepads and process the Drone’s video stream to do object detection and tracking.

##Status:

The differents goals reached at this point are :

* Controlling the Drone’s movements
* Fetching the different Drone’s sensor data
* Receiving the Drone’s video stream
* Controlling the Drone’s movement using a Bluetooth gamepad (MYON mobile gamepad )
* Enable Color Detection on the Drone’s video stream by using OpenCV
* Basic Color object tracking

###How to Install:
* Download and unzip  “AR-Drone-Android.zip” into an arbitory directory.
* In your Eclipse, choose the chosen directory for the current workspace and then import your project as an “Android Project”
* Build the project on your android phone
* Turn on your Drone and connect your phone to the AR Drone Wifi hotspot
* Launch the AR-Drone-Android application, click on the “Pilotage” button and then on the “Décollage” button
You should now be able to control your Drone


##Library Dependencies:

Our Project uses following libraries :

* Vitamio : https://github.com/yixia/VitamioBundle
Used to handle the Drone’s video stream
* OpenCV : http://opencv.org/
Used to do all image processing for color detection and tracking

###Documentation:
* AR Drone Developer Guide : https://projects.ardrone.org/wiki/ardrone-api/Developer_Guide
* Open CV Documentation : http://docs.opencv.org/
* Vitamio Documentation : https://www.vitamio.org/en/docs/
* Handling Controler Actions : http://developer.android.com/training/game-controllers/controller-input.html

##Authors :

This project has been made by a group of 5 students of [the GEII (Electrical and Industrial Computing Engineering) department](http://www.iut-acy.univ-savoie.fr/dut/geii/) of [the University of Savoie](https://www.univ-smb.fr/) in France for their last year term project.

**Students :**

* Bouguerra Bilal
* Dancre Antoine
* Genoud Quentin
* Nabhan Stephane
* Ranarivelo Andre ([@AJRdev](https://github.com/AJRdev))

**Mentor :**
* Caron Bernard

##License:

This project is licensed under the terms of the MIT license.
