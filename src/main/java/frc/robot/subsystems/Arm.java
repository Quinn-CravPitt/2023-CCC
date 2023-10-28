package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.REVLibError;
import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.simulation.EncoderSim;
import edu.wpi.first.wpilibj.simulation.SingleJointedArmSim;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismRoot2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Color8Bit;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ProfiledPIDSubsystem;
import frc.robot.Constants.ArmConstants;
import frc.robot.RobotContainer;

public class Arm extends ProfiledPIDSubsystem {
  // motor controllers
  private CANSparkMax m_armLeft =
      new CANSparkMax(ArmConstants.leftArmMotorID, MotorType.kBrushless);
  private CANSparkMax m_armRight =
      new CANSparkMax(ArmConstants.rightArmMotorID, MotorType.kBrushless);

  // encoders
  private final Encoder m_relativeEncoder =
      new Encoder(ArmConstants.RELATIVE_ENCODER_A, ArmConstants.RELATIVE_ENCODER_B);
  private final DutyCycleEncoder m_absoluteEncoder =
      new DutyCycleEncoder(ArmConstants.ABSOLUTE_ENCODER_PORT);

  // Limit Switches
  private final DigitalInput m_limitSwitch = new DigitalInput(4);
  // Simulation classes
  private SingleJointedArmSim m_ArmSim =
      new SingleJointedArmSim(
          DCMotor.getNEO(2), 60 / 18, 0.58, 0.5844, ArmConstants.kAngleOfOffset, 2, true);

  private final EncoderSim m_relativEncoderSim = new EncoderSim(m_relativeEncoder);

  // Create arm SmartDashboard visualization
  private final Mechanism2d m_mech2d = new Mechanism2d(60, 60);
  private final MechanismRoot2d m_armPivot = m_mech2d.getRoot("ArmPivot", 30, 30);
  private final MechanismLigament2d m_armTower =
      m_armPivot.append(new MechanismLigament2d("ArmTower", 30, -90));
  private final MechanismLigament2d m_arm =
      m_armPivot.append(
          new MechanismLigament2d(
              "Arm",
              30,
              Units.radiansToDegrees(m_ArmSim.getAngleRads()),
              6,
              new Color8Bit(Color.kYellow)));

  private final ArmFeedforward m_ArmFeedforward =
      new ArmFeedforward(ArmConstants.ks, ArmConstants.kg, ArmConstants.kv, ArmConstants.ka);
  public Command setIdleMode;

  public Arm() {
    super(
        new ProfiledPIDController(
            ArmConstants.kp,
            ArmConstants.ki,
            ArmConstants.kd,
            new TrapezoidProfile.Constraints(
                ArmConstants.kMaxVelocityRadPerSecond,
                ArmConstants.kMaxAccelerationRadPerSecondSquared)));

    configureMotors();

    // m_relativeEncoder.reset();
    m_relativeEncoder.setDistancePerPulse(ArmConstants.relativeEncoderDistancePerPulse);
    m_absoluteEncoder.setDistancePerRotation(ArmConstants.dutyCycleEncoderDistancePerRotation);

    SmartDashboard.putData("Arm Sim", m_mech2d);
    m_armTower.setColor(new Color8Bit(Color.kPurple));

    //m_controller.disableContinuousInput();
  }

  /* 
  public void backInvertMotors() {
    m_armLeft.setInverted(true);
    m_armRight.setInverted(false);
  }

  public void forwardInvertMotors() {
    m_armLeft.setInverted(false);
    m_armRight.setInverted(true);
  }

  */

  private void configureMotors() {
    m_armLeft.restoreFactoryDefaults();
    m_armRight.restoreFactoryDefaults();

    if (m_armLeft.setIdleMode(IdleMode.kBrake) != REVLibError.kOk) {
      System.out.println("ERROR while setting Left arm motor to Brake Mode");
    }
    if (m_armRight.setIdleMode(IdleMode.kBrake) != REVLibError.kOk) {
      System.out.println("ERROR while setting Right arm motor to Brake mode");
    }

    // burn all changes to flash
    m_armLeft.burnFlash();
    m_armRight.burnFlash();
  }

  // runs arm with feedforward control
  public void set(double speed) {
    m_armLeft.set(speed);
    m_armRight.setInverted(true);
    m_armRight.set(speed);
  }

  // Return raw absolute encoder position
  public double getRawAbsolutePosition() {
    return m_absoluteEncoder.getDistance();
  }


         // error message appears here


  @Override
  protected double getMeasurement() {

    //SmartDashboard.putData("Arm PID", getController());
    //SmartDashboard.putNumber("Arm Position", m_relativeEncoder.getDistance() * 3 / 10);
    //SmartDashboard.putNumber("goal", RobotContainer.previousAngle);
    if (!m_limitSwitch.get()) {
      m_relativeEncoder.reset();
    }
    return m_relativeEncoder.getDistance() * 3 / 10;
  }

  @Override
  protected void useOutput(double output, TrapezoidProfile.State setpoint) {
    // calculate feedforward from setpoint
    double feedforward = m_ArmFeedforward.calculate(setpoint.position, setpoint.velocity);
    //SmartDashboard.putNumber("setpointVelocity", setpoint.velocity);
    // System.out.println("Voltage" + output + feedforward);
    // add the feedforward to the PID output to get the motor output
    m_armLeft.setVoltage(output + feedforward);
    m_armRight.setInverted(true);
    m_armRight.setVoltage(output + feedforward);
    // System.out.println("vel" + setpoint.velocity);
    // System.out.println("fed"+ feedforward);

  }

  @Override
  public void simulationPeriodic() {
    m_ArmSim.setInputVoltage(m_armLeft.get() * RobotController.getInputVoltage());
    m_ArmSim.update(.020);
    m_relativEncoderSim.setDistance(m_ArmSim.getAngleRads());
    m_arm.setAngle(Units.degreesToRadians(m_ArmSim.getAngleRads()));
  }
}
