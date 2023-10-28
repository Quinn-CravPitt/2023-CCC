// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.OperatorConstants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.Shooter;

/*
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */

public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();
  private final DriveTrain m_DriveTrain = new DriveTrain();

  private final Joystick m_Joystick = new Joystick(0);
  private final CommandXboxController m_XboxController = new CommandXboxController(1);

  private final Shooter m_Shooter = new Shooter();

  private final Arm m_Arm = new Arm();
  public double goalAngle = 0;
  public double previousAngle = 0;

  public float rearArm = 0;
  public float raisedArm = 90;
  public float frontArm = 175;

  // private final SendableChooser<Command> m_autoChooser = new SendableChooser<>();

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {

    // m_autoChooser.setDefaultOption("Score High", Autos.scoreHighTier());
    // Configure the trigger bindings

    configureBindings();

    m_DriveTrain.setDefaultCommand(
        new RunCommand(
            () ->
                m_DriveTrain.arcadeDrive(
                    m_Joystick.getRawAxis(OperatorConstants.JOYSTICK_Y_AXIS),
                    m_Joystick.getRawAxis(OperatorConstants.JOYSTICK_X_AXIS)),
            m_DriveTrain));
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {

    // Schedule `ExampleCommand` when `exampleCondition` changes to `true`
    // arm button mapping

    m_XboxController
        .x()
        .onTrue(
            Commands.runOnce(
                () -> {
                  m_Arm.calledAngle = 0;
                  System.out.println("calledAngle = 0");
                },
                m_Arm));

    m_XboxController
        .y()
        .onTrue(
            Commands.runOnce(
                () -> {
                  m_Arm.calledAngle = Math.PI / 2;
                  System.out.println("calledAngle = pi/2");
                },
                m_Arm));

    m_XboxController
        .b()
        .onTrue(
            Commands.runOnce(
                () -> {
                  m_Arm.calledAngle = Math.PI;
                  System.out.println("calledAngle = pi");
                },
                m_Arm));

    // shooter

    // intake cube
    m_XboxController
        .leftBumper()
        .whileTrue(
            new StartEndCommand(
                () ->
                    m_Shooter.IntakeCube(
                        ShooterConstants.innerOuterSpeedReversed,
                        ShooterConstants.innerOuterSpeedReversed),
                () -> m_Shooter.IntakeCube(0, 0),
                m_Shooter));

    // Shoot cube
    m_XboxController
        .rightBumper()
        .whileTrue(
            new StartEndCommand(
                () ->
                    m_Shooter.ShootCube(
                        ShooterConstants.innerOuterSpeed, ShooterConstants.innerOuterSpeed),
                () -> m_Shooter.ShootCube(0, 0),
                m_Shooter));

    // shoot cube potencial more complicated with wait command
    /*   m_XboxController
    .rightBumper()
    .whileTrue(
        Commands.sequence(
            new RunCommand(
                () -> {
                  m_Shooter.runShooter(ShooterConstants.innerOuterSpeed);
                  new WaitCommand(.25);
                  m_Shooter.runIndex(ShooterConstants.innerOuterSpeed);
         }))); */
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return null;
    // An example command will be run in autonomous

  }
}
