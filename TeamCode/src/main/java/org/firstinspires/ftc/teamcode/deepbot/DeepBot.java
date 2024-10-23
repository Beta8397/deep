package org.firstinspires.ftc.teamcode.deepbot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.xdrive.XDrive;

public class DeepBot extends XDrive {

    public DcMotorEx armMotor;
    public DcMotorEx slideMotor;

    public static final int ARM_MIN = 0;
    public static final int ARM_MAX = 415;
    public static final int SLIDE_MIN = 0;
    public static final int SLIDE_MAX = 3000;
    public static final double ARM_TICKS_PER_DEGREE = 3.9;
    public static final double MIN_ARM_DEGREES = -15;
    public static final double MAX_ARM_DEGREES = 70;
    public static final double SLIDE_BASE_LENGTH = 14.25;
    public static final double SLIDE_STAGE_LENGTH = 13.25;
    public static final double SLIDE_STAGE_THROW = 9.5;
    public static final double[] SLIDE_STAGE_MASS = {1.0, 1.0, 1.0, 1.0, 0.5};
    public static final double PAYLOAD_MASS_OFFSET = 3.0;
    public static final double SLIDE_TICKS_PER_INCH = 114.04;
    public static final double MAX_SLIDE_LENGTH = 40;
    public static final double SAFE_SLIDE_LENGTH = 33;
    public static final double SAFE_ARM_ANGLE = Math.toDegrees( Math.acos(SAFE_SLIDE_LENGTH/MAX_SLIDE_LENGTH));

    public static final double TORQUE_CONSTANT = 0.1;
    public static final double INERTIA_CONSTANT = 0.001;

    private double targetArmLength = SLIDE_BASE_LENGTH;
    private double targetArmAngle = MIN_ARM_DEGREES;




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

    }

    public double setTargetArmLength(double inches){
        inches = Range.clip(inches, SLIDE_BASE_LENGTH, MAX_SLIDE_LENGTH);
        inches = Math.min(inches, SAFE_SLIDE_LENGTH / Math.cos(Math.toRadians( targetArmAngle )));
        targetArmLength= inches;
        return targetArmLength;
    }

    public double getTargetArmLength(){
        return targetArmLength;
    }

    public double setTargetArmAngle(double degrees){
        targetArmAngle = Range.clip(degrees, MIN_ARM_DEGREES, MAX_ARM_DEGREES);
        return targetArmAngle;
    }

    public double getTargetArmAngle(){
        return targetArmAngle;
    }

    public void seekArmTargets(double degrees, double inches){
        int targetArmTicks = (int) armTicksFromDegrees(degrees);
        int targetSlideTicks = (int)slideTicksFromInches(inches);
        armMotor.setTargetPosition(targetArmTicks);
        slideMotor.setTargetPosition(targetSlideTicks);
        armMotor.setPower(1.0);
        slideMotor.setPower(1.0);
    }

    public void updateArm(){
        seekArmTargets(targetArmAngle, targetArmLength);

    }


    public void updateArmNew(){
        int armTicks = armMotor.getCurrentPosition();
        int slideTicks = slideMotor.getCurrentPosition();
        double armDegrees = armDegreesFromTicks(armTicks);
        double slideInches = slideTicksFromInches(slideTicks);
        double[] x = {0, 0, 0, 0, 0};
        if (slideInches < SLIDE_BASE_LENGTH + SLIDE_STAGE_THROW){
            x[1] = slideInches - SLIDE_BASE_LENGTH;
            x[2] = x[1];
            x[3] = x[2];
        } else if (slideInches < SLIDE_BASE_LENGTH + 2 * SLIDE_STAGE_THROW){
            x[1] = SLIDE_STAGE_THROW;
            x[2] = slideInches - SLIDE_BASE_LENGTH;
            x[3] = x[2];
        } else {
            x[1] = SLIDE_STAGE_THROW;
            x[2] = 2 * SLIDE_STAGE_THROW;
            x[3] = slideInches -SLIDE_BASE_LENGTH;
        }

        x[4] = slideInches + PAYLOAD_MASS_OFFSET;

        double gravityTorque = 0;
        double inertiaMoment = 0;

        for (int i = 0; i < 5; i++){
            gravityTorque += SLIDE_STAGE_MASS[i] * (x[i] + SLIDE_STAGE_LENGTH/2);
            inertiaMoment += SLIDE_STAGE_MASS[i] * (x[i] + SLIDE_STAGE_LENGTH/2) * (x[i] + SLIDE_STAGE_LENGTH/2);
        }

        gravityTorque *= Math.cos(Math.toRadians(armDegrees));

        double armPower = TORQUE_CONSTANT * gravityTorque + INERTIA_CONSTANT * inertiaMoment * (targetArmAngle - armDegrees);
        armMotor.setPower(armPower);
        slideMotor.setTargetPosition((int) slideTicksFromInches(targetArmLength));
        slideMotor.setPower(1);
    }

    public double armTicksFromDegrees(double degrees){
        return (degrees - MIN_ARM_DEGREES) * ARM_TICKS_PER_DEGREE;
    }

    public double slideTicksFromInches(double inches){
        return (inches - SLIDE_BASE_LENGTH) * SLIDE_TICKS_PER_INCH;
    }

    public double armDegreesFromTicks(int ticks){
        return MIN_ARM_DEGREES + ticks/ ARM_TICKS_PER_DEGREE;
    }

    public double armInchesFromTicks(int ticks){
        return SLIDE_BASE_LENGTH + ticks/ SLIDE_TICKS_PER_INCH;
    }

    public double getArmAngle(){
        return armDegreesFromTicks(armMotor.getCurrentPosition());
    }

    public double getArmLength(){
        return armInchesFromTicks(slideMotor.getCurrentPosition());
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

}

