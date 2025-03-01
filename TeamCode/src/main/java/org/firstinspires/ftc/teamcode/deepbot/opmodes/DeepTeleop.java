package org.firstinspires.ftc.teamcode.deepbot.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.ftccommon.internal.manualcontrol.ManualControlOpMode;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.deepbot.DeepBot;
import org.firstinspires.ftc.teamcode.util.SavedData;
import org.firstinspires.ftc.teamcode.util.Toggle;
import org.firstinspires.ftc.teamcode.xdrive.XDriveTele;

@TeleOp
public class DeepTeleop extends XDriveTele {
    DeepBot bot = new DeepBot();
    Toggle toggleA2 = new Toggle(()->gamepad2.a);   // Reset Arm
    Toggle toggleRB2 = new Toggle(()->gamepad2.right_bumper);   // Open and Close claw
    Toggle toggleLB2 = new Toggle(()->gamepad2.left_bumper); // set wrist for ascent
    Toggle toggleDPL2 = new Toggle(()->gamepad2.dpad_left); // Hang Specimen Preset
    Toggle toggleDPD2 = new Toggle(()-> gamepad2.dpad_down);    // Collect Specimen from wall preset
    Toggle toggleDPU2 = new Toggle(()-> gamepad2.dpad_up);  // High Basket preset
    Toggle toggleDPR2 = new Toggle(()->gamepad2.dpad_right); // Collect Specimen from Floor Preset
    Toggle toggleDPU1 = new Toggle(()-> gamepad1.dpad_up);  // handle winch state
    Toggle toggleDPD1 = new Toggle(()-> gamepad1.dpad_down);    // handle winch state
    Toggle toggleLB1 = new Toggle(()-> gamepad1.left_bumper);   // Handle Yaw state
    Toggle toggleRB1 = new Toggle(()-> gamepad1.right_bumper);  // Handle yaw state
    Toggle toggleRightStick1 = new Toggle(()->Math.abs(gamepad1.right_stick_y) > 0.5);

    boolean resettingArm = false;

    boolean loweringArm = false;

    boolean clawOpen = true;
    enum  YawState{LEFT, MID, RIGHT, CUSTOM};
    YawState yawState = YawState.MID;

    private static final double WRIST_P0 = 0.88; // Servo setting when wrist is parallel to arm

    private static final double WRIST_GAIN = -265;
    private static final double DEFAULT_WRIST_ANGLE = 0;
    private double targetWristAngle = DEFAULT_WRIST_ANGLE + 90;

    enum WinchState{OFF, RAISING, LOWERING, MANUAL}

    WinchState winchState = WinchState.OFF;


    @Override
    public void runOpMode(){
        bot.init(hardwareMap, false);
        super.setBot(bot);
        bot.setPose(SavedData.pose.x,SavedData.pose.y,Math.toDegrees(SavedData.pose.h));

        bot.openClaw();


        waitForStart();


        while (opModeIsActive()) {

            // handle drive train
            oneDriveCycle();
            telemetry.addData("ticks", "bl %d  fl %d  fr %d  br %d",
                    bot.bl.getCurrentPosition(), bot.fl.getCurrentPosition(),
                    bot.fr.getCurrentPosition(), bot.br.getCurrentPosition());

            // handle arm angle and slide length
            if (toggleA2.update()) {
                resettingArm = true;
            } else if (resettingArm && !gamepad2.a) {
                bot.slideMotor.getCurrentPosition();
                bot.slideMotor.setTargetPosition(bot.slideMotor.getCurrentPosition()+40);
                bot.slideMotor.setPower(1);
                ElapsedTime et = new ElapsedTime();
                while (bot.slideMotor.isBusy() && et.milliseconds() < 1000) continue;
                resettingArm = false;
                bot.resetArm();
            }

            // update toggles
            boolean dpl2Toggled = toggleDPL2.update();
            boolean dpd2Toggled = toggleDPD2.update();
            boolean dpu2Toggled = toggleDPU2.update();
            boolean dpr2Toggled = toggleDPR2.update();
            boolean dpu1Toggled = toggleDPU1.update();
            boolean dpd1Toggled = toggleDPD1.update();
            boolean lb2Toggled = toggleLB2.update();
            boolean rightStick1Toggled = toggleRightStick1.update();

            double currentArmLength = bot.getSlideInches();
            double currentArmAngle = bot.getArmAngle();
            double targetArmLength = bot.getTargetSlideLength();
            double targetArmAngle = bot.getTargetArmAngle();

            boolean justStoppedLoweringArm = false;

            if (!resettingArm && dpr2Toggled){
                loweringArm = true;
            } else if (loweringArm && !gamepad2.dpad_right){
                loweringArm = false;
                justStoppedLoweringArm = true;
            }

            // arm angle
            if (resettingArm){
                bot.setTargetArmAngleUnSafe(targetArmAngle + gamepad2.left_stick_y * 3.0);
            } else if (dpu2Toggled) {
                bot.setTargetArmAngleSafe(67);
            } else if (dpl2Toggled) {
                bot.setTargetArmAngleSafe(54);  // was 39.1
            } else if (dpd2Toggled) {
                bot.setTargetArmAngleSafe(-16.5);  // was 13.5
            } else if (loweringArm){
                if (currentArmLength < 24) {
                    bot.setTargetArmAngleSafe(-26);
                }
            } else {
               bot.setTargetArmAngleSafe(targetArmAngle + gamepad2.left_stick_y * 3.0);
            }


           // slide length
            //
            if (resettingArm){
                bot.setTargetSlideLengthUnSafe(targetArmLength - gamepad2.right_stick_y * 0.8);
            } else if (dpu2Toggled) {
                bot.setTargetSlideLengthSafe(40);
                //changed 42 to 40
            } else if (dpl2Toggled) {
                bot.setTargetSlideLengthSafe(20.1);  // was 22
            } else if (dpd2Toggled) {
                bot.setTargetSlideLengthSafe(20.5);  // was 20
            } else if (loweringArm){
                bot.setTargetSlideLengthSafe(22);
            } else {
                bot.setTargetSlideLengthSafe(targetArmLength - gamepad2.right_stick_y * 0.8);
            }

            bot.updateArm();

            telemetry.addData("targetangle", targetArmAngle);
            telemetry.addData("targetlength", targetArmLength);
            telemetry.addData("armdegrees", currentArmAngle);
            telemetry.addData("slideinches", currentArmLength);
            telemetry.addData("LeftDist", bot.getLeftDistance());


            telemetry.addData("arm current", bot.armMotor.getCurrent(CurrentUnit.AMPS));
            telemetry.addData("slide current",bot.slideMotor.getCurrent(CurrentUnit.AMPS));

            // handle claw

            boolean rb2Toggled = toggleRB2.update();
            if (dpd2Toggled && !resettingArm) {
                clawOpen = true;
                bot.openClaw();
            } else if (loweringArm && !resettingArm){
                clawOpen = true;
                bot.setClawPosition(DeepBot.CLAW_WIDE_OPEN);
            }else if (dpl2Toggled && !resettingArm){
                clawOpen = false;
                bot.closeClaw();
            } else if (rb2Toggled){
                clawOpen = !clawOpen;
                if(clawOpen) {
                    bot.openClaw();
                }else{
                    bot.closeClaw();
                }
            }

            telemetry.addData("claw pos", bot.clawServo.getPosition());


            // handle yaw

            boolean lb1Toggled = toggleLB1.update();

            boolean rb1Toggled = toggleRB1.update();

            double rightStickX1 = gamepad1.right_stick_x;

            switch (yawState){
                case LEFT:
                case RIGHT:
                case CUSTOM:
                    if (lb1Toggled || rb1Toggled){
                        yawState = YawState.MID;
                        } else if (rightStickX1 < -0.05 || rightStickX1 > 0.05){
                        yawState = YawState.CUSTOM;

                    }
                    break;
                case MID:
                    if (lb1Toggled){
                        yawState = YawState.LEFT;
                    } else if (rb1Toggled){
                        yawState = YawState.RIGHT;
                    } else if (rightStickX1 < -0.05 || rightStickX1 > 0.05){
                        yawState = YawState.CUSTOM;
                    }
            }

            telemetry.addData("Yaw State", yawState);



            switch (yawState){
                case LEFT:
                    bot.setYawLeft();
                    break;
                case MID:
                    bot.setYawStraight();
                    break;
                case RIGHT:
                    bot.setYawRight();
                    break;
                case CUSTOM:
                    double currentYawPos = bot.yawServo.getPosition();
                    double yawPos = currentYawPos - 0.01 * rightStickX1;
                    bot.setYawPosition(yawPos);
                    telemetry.addData("YawPos", yawPos);
            }



            // handle wrist

            double wristChange = gamepad2.left_trigger - gamepad2.right_trigger;
            if (lb2Toggled && !resettingArm){
                targetWristAngle = DeepBot.MIN_ARM_DEGREES + 165;
            } else if (dpu2Toggled && !resettingArm){
                targetWristAngle = DEFAULT_WRIST_ANGLE;
            } else if (dpl2Toggled && !resettingArm){
                targetWristAngle = 54; // was DEFAULT_WRIST_ANGLE +90
            } else if (dpd2Toggled && !resettingArm){
                targetWristAngle = DEFAULT_WRIST_ANGLE +100; // was 90
            }else if (loweringArm && !resettingArm) {
                targetWristAngle = DEFAULT_WRIST_ANGLE + 90;
            } else if (justStoppedLoweringArm && !resettingArm){
                targetWristAngle = DEFAULT_WRIST_ANGLE;
            } else if (gamepad2.x){
                targetWristAngle = DEFAULT_WRIST_ANGLE;
            } else if (gamepad2.y){
                targetWristAngle = DEFAULT_WRIST_ANGLE + 70;
            } else {
                targetWristAngle += wristChange;
            }

            double wristPos = WRIST_P0 + (targetWristAngle - currentArmAngle) / WRIST_GAIN;
            if (wristPos > WRIST_P0){
                targetWristAngle = currentArmAngle;
                wristPos = WRIST_P0;
            }else if (wristPos < 0){
                targetWristAngle = currentArmAngle + (0.0 - WRIST_P0) * WRIST_GAIN;
                wristPos = 0;
            }
            bot.setWristPosition(wristPos);

            telemetry.addData("wrist pos", bot.wristServo.getPosition());

            // handle winch

            switch (winchState){
                case OFF:
                    if (dpu1Toggled){
                        winchState = WinchState.RAISING;
                        bot.winchMotor.setTargetPosition(5550);
                        bot.winchMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        bot.winchMotor.setPower(1);
                    } else if (dpd1Toggled){
                        winchState = WinchState.LOWERING;
                        bot.winchMotor.setTargetPosition(0);
                        bot.winchMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        bot.winchMotor.setPower(1);
                    } else if (rightStick1Toggled) {
                        winchState = WinchState.MANUAL;
                        bot.winchMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                        bot.winchMotor.setPower(-Math.signum(gamepad1.right_stick_y));
                    }
                    break;
                case RAISING:
                case LOWERING:
                    if (dpu1Toggled || dpd1Toggled){
                        winchState = WinchState.OFF;
                        bot.winchMotor.setTargetPosition(bot.winchMotor.getCurrentPosition());
                        bot.winchMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        bot.winchMotor.setPower(1);
                    }
                case MANUAL:
                    if (Math.abs(gamepad1.right_stick_y) < 0.5){
                        winchState = WinchState.OFF;
                        bot.winchMotor.setTargetPosition(bot.winchMotor.getCurrentPosition());
                        bot.winchMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        bot.winchMotor.setPower(1);
                    }
            }

            if (winchState == WinchState.MANUAL){
                bot.winchMotor.setPower(-Math.signum(gamepad1.right_stick_y));
            }


            // report distances
            telemetry.addData("leftdistance", bot.getLeftDistance());
            telemetry.addData("backdistance", bot.getBackDistance());
           telemetry.addData("sonic distance", bot.sonicLeft.getDistanceAsync(50, DistanceUnit.INCH));
            telemetry.addData("winch ticks",
                    bot.winchMotor.getCurrentPosition());

            telemetry.update();
        }
    }
}
