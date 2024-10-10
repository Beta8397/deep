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

    public final int ARM_MIN = 0;
    public final int ARM_MAX = 415;
    public final int SLIDE_MIN = 0;
    public final int SLIDE_MAX = 3000;


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
