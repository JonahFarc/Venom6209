# BoschIMU

					Second Release = 26 October 2015

Corrected the typecasting and sign extension bugs in the getIMUGyroAngles method that were
reported on the FTC Technology Forum by "yjw558" and "GearTicks" at:
http://ftcforum.usfirst.org/showthread.php?4421-Recommendation-for-a-gyro-sensor-that-will-work-on-new-control-system/



                          Initial Release - 19 October 2015

This software publication consists of the Java class "AdafruitIMU.java" and an FTC SDK-
compatible "OpMode" called "IMUtest.java". IMUtest exists to exercise and demonstrate wnat
"AdafruitIMU" can do, when an Adafruit board (Adafruit product #2472) is connected to an
I2C port on a Modern Robotics Core Device Interface Module.
(https://www.adafruit.com/products/2472 ($34.95, as of 25 August 2015))

The datasheet for the Bosch BNO055 IMU mounted on the Adafruit board amy be found at:
http://www.adafruit.com/datasheets/BST_BNO055_DS000_12.pdf . This explains the IMU's
functions and interfaces in detail. Also, the software provided here borrows heavily
from the open source IMU sensor driver code located on GitHub at:
https://github.com/adafruit/Adafruit_BNO055
The relevant C++ and header files are:
Adafruit_BNO055.cpp and Adafruit_BNO055.h

The software presented here has two purposes:
1. It shows one possible software design "recipe" for using the I2C device classes in the FTC
SDK (October 2015 release) to operate an I2C device; that is: initialize it, write to it,
and read from it.
2. It offers to other FTC code an interface to the Bosch IMU, in order to perform these specific
functions:
	a. Initialize the IMU, verify that at least the gyros are calibrated, and start its "IMU mode",
	b. Start a self-perpetuating series of 20-byte "reads" from the IMU, to provide angle data
	c. Implement a "get" method that provides both pitch and heading(yaw) angles to other
	OpMode code used either in autonomous or teleop mode. 
Because of concerns for accuracy the "get" method reports two versions of the heading and pitch angles.
It reports the Euler angles calculated by the BNO055's built-in microcontroller. It also reports
angles calculated using the 4 components of the quaternion; these are also outputs generated by the
BNO055's built-in microcontroller.

The IMU datasheet describes the Euler angles in Section 3.6.5.4, and the quaternion in Section 3.6.5.5
both on p.35. (For an explanation of Euler angles, see  https://en.wikipedia.org/wiki/Euler_angles  )
A different version of the Euler angles is calculated from the 4 components of the quaternion, using the
Tait-Bryan equations listed in:
https://en.wikipedia.org/wiki/Conversion_between_quaternions_and_Euler_angles

It is very easy to use "AdfruitIMU.java" and "IMUtest.java":
1. Import "IMUtest.java" into the "opmodes" package of Android Studio's "ftc_app" (or "ftc_app-master")
project. Modify the "FtcOpModeRegister" class as needed. Modify the "import" statement for "AdafruitIMU"
as needed to name the package in which "AdafruitIMU.java" will be installed.
2. Import "AdafruitIMU.java" into whatever Java package has been created to hold team-specific non-FTC
code.

The hardware configuration needed to run the "IMUtest" OpMode requires no motors at all and no sensors
other than a Bosch IMU plugged into one of the 6 I2C ports on a Modern Robotics Core Device Interface
module. The IMU name typed into that Robot Controller configuration file must be the same as the name
that appears in the "new AdafruitIMU" call on line 31 of "IMUtest".

When OpMode IMUtest runs on the Robot Controller, the telemetry display on the Driver Station will show
the 2 versions of the heading(yaw) angle and the 2 versions of the pitch angle. I will also show both
the maximum and the (exponential rolling) average time between successive calls to the "portIsReady"
callback method that's part of the self-perpetuating stream of "read" operations.

I hope that FTC teams will find this software useful for either or both of my intended purposes. I hope
that FTC teams will also share with the FTC community the Bosch IMU performance data they gather when
using this software. It helps us all to learn the strengths and weaknesses of various candidate sensors.

Alan M. Gilkes
Alternate Coach, FTC Team 3734 - Imperial Robotics
Software Mentor, FTC Team 6832 - Iron Reign
FTC Technology Forum ID : "AlanG"


