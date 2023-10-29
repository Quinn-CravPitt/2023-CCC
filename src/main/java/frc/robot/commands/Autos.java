// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Shooter;
import frc.robot.Constants.ShooterConstants;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.ExampleSubsystem;



public final class Autos {
  /** Example static factory for an autonomous command. */
  public static CommandBase exampleAuto(ExampleSubsystem subsystem) {
    return Commands.sequence(subsystem.exampleMethodCommand(), new ExampleCommand(subsystem));
  }

  public static CommandBase scoreHigh(Shooter shooter, Arm arm) {
    return Commands.sequence(
        Commands.runOnce(
            () -> {
              
            shooter.ShootCube(ShooterConstants.shootHighSpeed, ShooterConstants.shootHighSpeed);

            },
            shooter),
        //wait for x seconds to shoot cube 
        new WaitCommand(.5),
        
        //set roller speeds to zero 
        Commands.runOnce(
            (() ->
                shooter.ShootCube(
                    0, 0)),
            shooter));
  }

  public static CommandBase scoreMid(Shooter shooter, Arm arm) {
    return Commands.sequence(
        Commands.runOnce(
            () -> {
              
            shooter.ShootCube(ShooterConstants.shootMidSpeed, ShooterConstants.shootMidSpeed);

            },
            shooter),
        //wait for x seconds to shoot cube 
        new WaitCommand(.5),
        
        //set roller speeds to zero 
        Commands.runOnce(
            (() ->
                shooter.ShootCube(
                    0, 0)),
            shooter));
  }

  private Autos() {
    throw new UnsupportedOperationException("This is a utility class!");
  }
}
