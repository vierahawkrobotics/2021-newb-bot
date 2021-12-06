package frc.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.*;

public class Autonomous2 {
    private static Timer timer = new Timer();
    private static double time;
    private static final double SPEED = 0.20;
    private static final double STOPPED = 0.00;

    private static enum STATE {
        DRIVE, SIT;
    }

    private static enum LOCK {
        LOCKED, UNLOCKED;
    }

    private static STATE state = STATE.DRIVE;
    private static LOCK lock = LOCK.UNLOCKED;

    private static void updateSD(double time, double speed) {
        SmartDashboard.putNumber("  time              = ", time);
        SmartDashboard.putNumber("  Left motor speed  = ", speed);
        SmartDashboard.putNumber("  Right motor speed = ", speed);
    }

    public static void crossLine(DifferentialDrive drive) {
        switch (state) {
            case DRIVE: {
                if (lock == LOCK.UNLOCKED) {
                    timer.reset();
                    timer.start();
                    lock = LOCK.LOCKED;
                }
                time = timer.get();
                if (time < 5.0) {
                    // System.out.println(time + " driving");
                    updateSD(time, SPEED);
                    drive.tankDrive(SPEED, SPEED, false);
                } else {
                    updateSD(time, 0.0);
                    drive.tankDrive(0.0, 0.0, false);
                    state = STATE.SIT;
                    lock = LOCK.UNLOCKED;
                }
                break;
            }
            case SIT: {
                // System.out.println(timer.get() + " sitting");
                if (lock == LOCK.UNLOCKED) {
                    timer.reset();
                    timer.start();
                    lock = LOCK.LOCKED;
                }
                time = timer.get();
                if (time < 12.0) {
                    // System.out.println(time + " driving");
                    updateSD(time, STOPPED);
                    drive.tankDrive(STOPPED, STOPPED, false);
                } else {
                    updateSD(time, 0.0);
                    drive.tankDrive(0.0, 0.0, false);
                    state = STATE.DRIVE;
                    lock = LOCK.UNLOCKED;
                }
                break;
            }
        }
    }
}
