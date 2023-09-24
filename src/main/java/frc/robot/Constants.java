// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
  }

  public static class DriveTrainConstants {
    public static final int frontLeftMotorID = 7;
    public static final int backLeftMotorID = 6;
    public static final int frontRightMotorID = 8;
    public static final int backRightMotorID = 5;
    public static final int kDriverControllerPort = 1;
    public static final int JoystickID = 0;
  }

  public static class ShooterConstants {
    public static final int frontShooterId = 7;
    public static final int backShooterId = 8;

    public static final double frontShooterSpeed = .5;
    public static final double frontShooterSpeedReverse = -.5;

    public static final double backIndexSpeed = .5;
    public static final double backIndexSpeedReverse = -.5;

    public static final double innerSpeed = .5;
    public static final double outerSpeed = .5;

    public static final double innerSpeedReversed = -.3;
    public static final double outerSpeedReversed = -.3;
  }

  public static class ArmConstants {
    public static final double armSpeed = .1;
    public static final int RELETIVE_ENCODER_A = 9;
    public static final int RELETIVE_ENCODER_B = 7;
    public static final int ABSOLUTE_ENCODER_PORT = 8;
    public static final double reletiveEncoderDistancePerPulse = 360 / 1024;
    public static final double dutyCycleEncoderDistancePerRotation = 360;
  }
}
