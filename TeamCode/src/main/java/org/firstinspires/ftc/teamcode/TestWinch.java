package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.deepbot.DeepBot;

@TeleOp
public class TestWinch extends LinearOpMode {

    DcMotorEx winch;


    public void runOpMode(){
        winch = hardwareMap.get(DcMotorEx.class, "winchMotor");
        waitForStart();

        while (opModeIsActive()){

            double power = -gamepad1.left_stick_y;
            winch.setPower(power);
            telemetry.addData("power", power);
            telemetry.addData("ticks", winch.getCurrentPosition());
            telemetry.update();
        }



    }

}
