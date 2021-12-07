package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.*;

public class Autonomous3 {
    private static final double inchesToGo = 10;
    private static final double wheelDiameter = 6.0;
    private static final double encoderTicksPerRotation = 4096.0;
    private static final double circumf = wheelDiameter * Math.PI;
    private static final double ticksToGo = (inchesToGo / circumf) * encoderTicksPerRotation;
    private static Boolean init = true;

    public static void encoderMove(WPI_TalonSRX leftMotor, WPI_TalonSRX rightMotor) {
        if (init) {
            System.out.println("auto 3 init - tickstogo = " + ticksToGo);
            leftMotor.setSensorPhase(true); // modify based on setup
            leftMotor.setSelectedSensorPosition(0, 0, 10); // encoder set to zero, 2nd zero = closed loop, 10 = timeout
                                                           // in ms
            leftMotor.set(ControlMode.MotionMagic, 0); // set mode to MotionMagic, speed is zero
            leftMotor.config_kP(0, 0.35, 10); // set up the PID parameters
            leftMotor.config_kF(0, 0.25, 10);
            leftMotor.set(ControlMode.MotionMagic, -ticksToGo);
            leftMotor.configMotionAcceleration(100);
            leftMotor.configMotionCruiseVelocity(500);
            leftMotor.set(ControlMode.MotionMagic, 500);
            rightMotor.follow(leftMotor);

            leftMotor.feed(); // feed the motor safety monster
            rightMotor.feed();

            init = false;
        } else if (leftMotor.isMotionProfileFinished()) { // movement is done so feed the motor safety beast
            leftMotor.feed();
            rightMotor.feed();
        }
    }
}