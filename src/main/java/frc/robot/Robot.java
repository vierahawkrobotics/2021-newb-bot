
/**
 * Phoenix Software License Agreement
 *
 * Copyright (C) Cross The Road Electronics.  All rights
 * reserved.
 * 
 * Cross The Road Electronics (CTRE) licenses to you the right to 
 * use, publish, and distribute copies of CRF (Cross The Road) firmware files (*.crf) and 
 * Phoenix Software API Libraries ONLY when in use with CTR Electronics hardware products
 * as well as the FRC roboRIO when in use in FRC Competition.
 * 
 * THE SOFTWARE AND DOCUMENTATION ARE PROVIDED "AS IS" WITHOUT
 * WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING WITHOUT
 * LIMITATION, ANY WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE AND NON-INFRINGEMENT. IN NO EVENT SHALL
 * CROSS THE ROAD ELECTRONICS BE LIABLE FOR ANY INCIDENTAL, SPECIAL, 
 * INDIRECT OR CONSEQUENTIAL DAMAGES, LOST PROFITS OR LOST DATA, COST OF
 * PROCUREMENT OF SUBSTITUTE GOODS, TECHNOLOGY OR SERVICES, ANY CLAIMS
 * BY THIRD PARTIES (INCLUDING BUT NOT LIMITED TO ANY DEFENSE
 * THEREOF), ANY CLAIMS FOR INDEMNITY OR CONTRIBUTION, OR OTHER
 * SIMILAR COSTS, WHETHER ASSERTED ON THE BASIS OF CONTRACT, TORT
 * (INCLUDING NEGLIGENCE), BREACH OF WARRANTY, OR OTHERWISE
 */

/**
 * Description:
 * The SixTalonArcadeDrive example demonstrates the ability to create WPI Talons/Victors
 * to be used with WPI's drivetrain classes. WPI Talons/Victors contain all the functionality
 * of normally created Talons/Victors (Phoenix) with the remaining SpeedController functions
 * to be supported by WPI's classes. 
 * 
 * The example uses two master motor controllers passed into WPI's DifferentialDrive Class 
 * to control the remaining 4 Talons (Follower Mode) to provide a simple Tank Arcade Drive 
 * configuration.
 *
 * Controls:
 * Left Joystick Y-Axis: Drive robot in forward and reverse direction
 * Right Joystick X-Axis: Turn robot in right and left direction
 */
package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class Robot extends TimedRobot {
  /* Master Talons for arcade drive */
  WPI_TalonSRX frontLeftMotor = new WPI_TalonSRX(1);
  WPI_TalonSRX frontRightMotor = new WPI_TalonSRX(2);

  /* Follower Talons + Victors for six motor drives */
  WPI_TalonSRX leftFollower1 = new WPI_TalonSRX(3);
  WPI_TalonSRX rightFollower1 = new WPI_TalonSRX(4);

  /* Construct drivetrain by providing master motor controllers */
  DifferentialDrive drive = new DifferentialDrive(frontLeftMotor, frontRightMotor);

  /* Joystick for control */
  Joystick joy = new Joystick(0);

  /**
   * This function is called once at the beginning during operator control
   */
  public void teleopInit() {
    /* Factory Default all hardware to prevent unexpected behaviour */
    frontLeftMotor.configFactoryDefault();
    frontRightMotor.configFactoryDefault();
    leftFollower1.configFactoryDefault();
    rightFollower1.configFactoryDefault();

    /**
     * Take our extra motor controllers and have them follow the Talons updated in
     * arcadeDrive
     */
    leftFollower1.follow(frontLeftMotor);
    rightFollower1.follow(frontRightMotor);

    /**
     * Drive robot forward and make sure all motors spin the correct way. Toggle
     * booleans accordingly....
     */
    frontLeftMotor.setInverted(false); // <<<<<< Adjust this until robot drives forward when stick is forward
    frontRightMotor.setInverted(true); // <<<<<< Adjust this until robot drives forward when stick is forward
    leftFollower1.setInverted(InvertType.FollowMaster);
    rightFollower1.setInverted(InvertType.FollowMaster);
    /*
     * Talon FX does not need sensor phase set for its integrated sensor This is
     * because it will always be correct if the selected feedback device is
     * integrated sensor (default value) and the user calls getSelectedSensor* to
     * get the sensor's position/velocity.
     * 
     * https://phoenix-documentation.readthedocs.io/en/latest/ch14_MCSensor.html#
     * sensor-phase
     */
    // _frontLeftMotor.setSensorPhase(true);
    // _frontRightMotor.setSensorPhase(true);

    /*
     * diff drive assumes (by default) that right side must be negative to move
     * forward. Change to 'false' so positive/green-LEDs moves robot forward
     */
    //drive.setRightSideInverted(false); // do not change this
  }

  /**
   * This function is called periodically during operatxxor control
   */
  public void teleopPeriodic() {
    /* Gamepad processing */
    double forward = -1.0 * joy.getY(); // Sign this so forward is positive
    double turn = +1.0 * joy.getZ(); // Sign this so right is positive

    /* Deadband - within 10% joystick, make it zero */
    if (Math.abs(forward) < 0.10) {
      forward = 0;
    }
    if (Math.abs(turn) < 0.10) {
      turn = 0;
    }

    /**
     * Print the joystick values to sign them, comment out this line after checking
     * the joystick directions.
     */
    // System.out.println("JoyY:" + forward + " turn:" + turn);

    /**
     * Drive the robot,
     */
    drive.arcadeDrive(forward, turn);
  }
}