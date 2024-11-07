package org.firstinspires.ftc.teamcode.deepbot;

import android.net.MailTo;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.IncludedFirmwareFileInfo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.xdrive.XDrive;
import org.opencv.core.Mat;

public class DeepBot extends XDrive {

    public DcMotorEx armMotor;
    public DcMotorEx slideMotor;
    public Servo wristServo;
    public Servo clawServo;

    public static final int ARM_MIN = 0;
    public static final int ARM_MAX = 1398;
    public static final int SLIDE_MIN = 0;
    public static final int SLIDE_MAX = 3000;
    public static final double ARM_TICKS_PER_DEGREE = 14.56;  // Ticks on arm motor per degree elevation
    public static final double SLIDE_TICKS_PER_INCH = 114.04;  // Ticks on slide motor per inch of travel
    public static final double MIN_ARM_DEGREES = -32;   // Smallest (most negative) allowed arm angle
    public static final double MAX_ARM_DEGREES = 95;    // Largest allowed arm angle
    public static final double MAX_SLIDE_LENGTH = 40;   // Maximum allowed slide length (arm motor shaft to end of slide)
    public static final double MAX_ARM_UP_LENGTH = 30;
    public static final double SAFE_SLIDE_LENGTH = 33;  // Maximum slide length that will fit within 42" bounding box for all arm angles
    public static final double PAYLOAD_DIST_OFFSET = 5;  // Max distance from end of slide to end of payload, inches
    public static final double SAFE_ARM_ANGLE = 30;     // Min arm angle that respects 42" bounding box for all slide lengths
    public static final double SLIDE_BASE_LENGTH = 14.25;   // Arm shaft to end of slide when fully retracted
    public static final double ARM_FULCRUM_HIEGHT = 7.5;
    public static final double SLIDE_STAGE_LENGTH = 13.25;  // Length of individual slide stage
    public static final double SLIDE_STAGE_THROW = 9.5; // Greatest distance moved by individual slide stage relative to the stage below
    public static final double[] SLIDE_STAGE_MASS = {1.0, 1.0, 1.0, 1.0, 0.0};  // Relative mass of individual slide stages (last entry is payload)
    public static final double PAYLOAD_MASS_OFFSET = 3.0;   // Distance from end of slide to center of mass of payload

    public static final double TORQUE_CONSTANT = 0.1;   // Feedforward constant for arm elevation control
    public static final double INERTIA_CONSTANT = 0.001;    // Proportionate constant for arm elevation control

    private double targetSlideLength = SLIDE_BASE_LENGTH;
    private double targetArmAngle = MIN_ARM_DEGREES;

    private static final double CLAW_OPEN = 0.35;
    private static final double CLAW_CLOSED = 0.19;
    public static final double WRIST_UP = 0.8;
    public static final double WRIST_DOWN = 0.45;

//    public boolean raisingArm = true;

    private boolean armPowerOff = false;




    @Override
    public void init(HardwareMap hwMap) {
        super.init(hwMap);

        armMotor = hwMap.get(DcMotorEx.class,"arm_motor");
        slideMotor = hwMap.get(DcMotorEx.class,"slide_motor");

        armMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        slideMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        armMotor.setTargetPosition(0);
        slideMotor.setTargetPosition(0);

        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        wristServo = hwMap.get(Servo.class, "wristServo");
        clawServo = hwMap.get(Servo.class, "clawServo");

    }

    public void resetArm(){
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        armMotor.setTargetPosition(0);
        slideMotor.setTargetPosition(0);

        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        targetArmAngle = armDegreesFromTicks(0);
        targetSlideLength = slideInchesFromTicks(0);
    }

    /*
     * Set target slide length to requested value, but constrained to respect 42" boundary
     */
    public double setTargetSlideLengthSafe(double inches){
        inches = Range.clip(inches, SLIDE_BASE_LENGTH, MAX_SLIDE_LENGTH);
        if (targetArmAngle < SAFE_ARM_ANGLE || armPowerOff){
            inches = Math.min(inches, SAFE_SLIDE_LENGTH-2);
        } else if (targetArmAngle > 75){
            inches = Math.min(inches, MAX_ARM_UP_LENGTH);
        }
        targetSlideLength = inches;
        return targetSlideLength;
    }

    public double setTargetSlideLengthUnSafe(double inches){
        inches = Math.min(inches, SAFE_SLIDE_LENGTH);
        targetSlideLength = inches;
        return targetSlideLength;
    }

    public double getTargetSlideLength(){
        return targetSlideLength;
    }

    /*
     * Set target arm angle to requested value, but simultaneously constrain the target
     * slide length to respect 42" boundary
     */
    public double setTargetArmAngleSafe(double degrees){
        targetArmAngle = Range.clip(degrees, MIN_ARM_DEGREES, MAX_ARM_DEGREES);
        if (targetArmAngle < SAFE_ARM_ANGLE) {
            targetSlideLength = Math.min(targetSlideLength, SAFE_SLIDE_LENGTH-2);
        }else if (targetArmAngle > 75){
            targetSlideLength = Math.min(targetSlideLength, MAX_ARM_UP_LENGTH);
        }
        return targetArmAngle;
    }

    public double setTargetArmAngleUnSafe(double degrees){
        targetArmAngle = Math.min(targetArmAngle, 75);
        return targetArmAngle;
    }

    public double getTargetArmAngle(){
        return targetArmAngle;
    }

    public void seekArmTargets(double degrees, double inches, boolean ascending){
        int targetArmTicks = armTicksFromDegrees(degrees);

//        if (targetArmTicks > armMotor.getTargetPosition()){
//            raisingArm = true;
//        } else if (targetArmTicks < armMotor.getTargetPosition()){
//            raisingArm = false;
//        }

        int targetSlideTicks = slideTicksFromInches(inches);
        armMotor.setTargetPosition(targetArmTicks);
        slideMotor.setTargetPosition(targetSlideTicks);

//        double armShutOffAngle = -Math.asin(ARM_FULCRUM_HIEGHT/targetSlideLength);
//        double armDegrees = armDegreesFromTicks(armMotor.getCurrentPosition());
//        if (targetArmAngle < armShutOffAngle && armDegrees < armShutOffAngle + 2 && !ascending && !raisingArm){
//            armMotor.setPower(0);
//        } else {
//            armMotor.setPower(1.0);
//        }

        if (armPowerOff) {
            armMotor.setPower(0);
        } else {
            armMotor.setPower(1);
        }

        slideMotor.setPower(1.0);
    }

    public void updateArm(boolean ascending){
        seekArmTargets(targetArmAngle, targetSlideLength, ascending);
    }


    /*
     * Update arm and slide motors using a combination of feedforward and proportionate control
     * for arm angle, and RUN_TO_POSITION mode for slide length.
     *
     * For arm angle control, the feedforward component of motor power is based on the torque being
     * applied to the arm by gravity. The TORQUE_CONSTANT should be adjusted to that the arm holds
     * its current elevation when powered only with the feedforward control. The proportionate
     * component of motor power is based on the moment of inertia of the arm.
     */
    public void updateArmNew(){
        int armTicks = armMotor.getCurrentPosition();
        int slideTicks = slideMotor.getCurrentPosition();
        double armDegrees = armDegreesFromTicks(armTicks);  // Current elevation angle of arm
        double slideInches = slideInchesFromTicks(slideTicks);  // Current length of slide

        /*
         * Current position of the center of each slide stage relative to the arm motor shaft.
         * The final element of this array is the position of payload center of mass relative to the
         * arm motor shaft.
         */
        double[] x = {SLIDE_BASE_LENGTH-SLIDE_STAGE_LENGTH/2, 0, 0, 0, 0};
        if (slideInches < SLIDE_BASE_LENGTH + SLIDE_STAGE_THROW){
            x[1] = slideInches - SLIDE_STAGE_LENGTH/2;
            x[2] = x[1];
            x[3] = x[2];
        } else if (slideInches < SLIDE_BASE_LENGTH + 2 * SLIDE_STAGE_THROW){
            x[1] = x[0] + SLIDE_STAGE_THROW;
            x[2] = slideInches - SLIDE_STAGE_LENGTH/2;
            x[3] = x[2];
        } else {
            x[1] = x[0] + SLIDE_STAGE_THROW;
            x[2] = x[0] + 2 * SLIDE_STAGE_THROW;
            x[3] = slideInches -SLIDE_BASE_LENGTH;
        }

        x[4] = slideInches + PAYLOAD_MASS_OFFSET;

        /*
         * Compute torque being applied by gravity to the arm, and arm rotational moment of inertia,
         * by adding components for each individual stage (as well as the payload)
         */

        double gravityTorque = 0;
        double inertiaMoment = 0;

        for (int i = 0; i < 5; i++){
            gravityTorque += SLIDE_STAGE_MASS[i] * x[i];
            inertiaMoment += SLIDE_STAGE_MASS[i] * x[i]*x[i];
            if (i<4){
                inertiaMoment += SLIDE_STAGE_MASS[i] * SLIDE_STAGE_LENGTH * SLIDE_STAGE_LENGTH/12;
            }
        }

        gravityTorque *= Math.cos(Math.toRadians(armDegrees));

        /*
         * Use temporary target angle and length to avoid exceeding 42" boundary during travel
         * from current state to actual target state.
         */
        double tempTargetAngle = targetArmAngle;
        double tempTargetLength = targetSlideLength;

        if (armDegrees > SAFE_ARM_ANGLE){
            if (targetArmAngle < SAFE_ARM_ANGLE && slideInches > SAFE_SLIDE_LENGTH){
                tempTargetAngle = armDegrees;
            }
        } else {
            tempTargetLength = Math.min(SAFE_SLIDE_LENGTH, targetSlideLength);
        }

        double armPower = TORQUE_CONSTANT * gravityTorque + INERTIA_CONSTANT * inertiaMoment * (tempTargetAngle - armDegrees);
        armMotor.setPower(armPower);
        slideMotor.setTargetPosition(slideTicksFromInches(tempTargetLength));
        slideMotor.setPower(1);
    }

    public int armTicksFromDegrees(double degrees){
        return (int)((degrees - MIN_ARM_DEGREES) * ARM_TICKS_PER_DEGREE);
    }

    public int slideTicksFromInches(double inches){
        return (int)((inches - SLIDE_BASE_LENGTH) * SLIDE_TICKS_PER_INCH);
    }

    public double armDegreesFromTicks(int ticks){
        return MIN_ARM_DEGREES + ticks/ ARM_TICKS_PER_DEGREE;
    }

    public double slideInchesFromTicks(int ticks){
        return SLIDE_BASE_LENGTH + ticks/ SLIDE_TICKS_PER_INCH;
    }

    public double getArmAngle(){
        return armDegreesFromTicks(armMotor.getCurrentPosition());
    }

    public double getArmLength(){
        return slideInchesFromTicks(slideMotor.getCurrentPosition());
    }

    public void setLiftTilt(int pos){
        pos = Range.clip(pos, ARM_MIN, ARM_MAX);
        armMotor.setTargetPosition(pos);
        armMotor.setPower(1);
    }

    public void setLiftExtend(int pos){
        pos = Range.clip(pos, SLIDE_MIN, SLIDE_MAX);
        slideMotor.setTargetPosition(pos);
        slideMotor.setPower(1);
    }

    public void setWristPosition(double pos){
        wristServo.setPosition(pos);
    }

    public void  setClawPosition(double pos){
        clawServo.setPosition(pos);
    }

    public void  closeClaw(){
        setClawPosition(CLAW_CLOSED);
    }

    public void openClaw(){
        setClawPosition(CLAW_OPEN);
    }

    public void setWristUp(){
        setWristPosition(WRIST_UP);
    }

    public void  setWristDown(){
        setWristPosition(WRIST_DOWN);
    }


    public void setArmPowerOff(boolean powerOff){
        if (powerOff && getArmAngle() > -10){
            return;
        } else if (!powerOff && armPowerOff){
            targetArmAngle = getArmAngle();
        }
        armPowerOff = powerOff;
    }

    public boolean getArmPowerOff(){
        return armPowerOff;
    }

}

