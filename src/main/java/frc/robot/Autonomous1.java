package frc.robot;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Autonomous1 {

    public static void doNothingAuto(DifferentialDrive drive) {
        drive.tankDrive(0.0, 0.0);
    }

}
