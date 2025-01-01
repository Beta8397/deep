package org.firstinspires.ftc.teamcode.deepbot.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.deepbot.DeepBot;
import org.firstinspires.ftc.teamcode.util.SavedData;
import org.firstinspires.ftc.teamcode.util.Toggle;
import org.firstinspires.ftc.teamcode.xdrive.XDriveTele;

@TeleOp
public class DeepTeleop extends XDriveTele {
    DeepBot bot = new DeepBot();
    Toggle toggleA2 = new Toggle(()->gamepad2.a);
    Toggle toggleRB2 = new Toggle(()->gamepad2.right_bumper);
    Toggle toggleB2andDPR2 = new Toggle(()->gamepad2.b && gamepad2.dpad_right);
    Toggle toggleLB2 = new Toggle(()->gamepad2.left_bumper);
    Toggle toggleDPL2 = new Toggle(()->gamepad2.dpad_left);
    Toggle toggleDPD2 = new Toggle(()-> gamepad2.dpad_down);
    Toggle toggleDPU2 = new Toggle(()-> gamepad2.dpad_up);
    Toggle toggleDPU1 = new Toggle(()-> gamepad1.dpad_up);
    Toggle toggleDPD1 = new Toggle(()-> gamepad1.dpad_down);
    Toggle toggleLB1 = new Toggle(()-> gamepad1.left_bumper);
    Toggle toggleRB1 = new Toggle(()-> gamepad1.right_bumper);

    boolean resettingArm = false;

    enum ClawState {OPEN, CLOSED, LOOSE};
    ClawState clawState = ClawState.OPEN;

    enum  YawState{LEFT, MID, RIGHT, CUSTOM};
    YawState yawState = YawState.MID;

    private static final double WRIST_P0 = 0.576;

    private static final double WRIST_90 = 0.207;

    private static final double WRIST_BACK = 0.576;
    private static final double WRIST_GAIN = -245;
    private static final double DEFAULT_WRIST_ANGLE = 0;
    private double targetWristAngle = DEFAULT_WRIST_ANGLE + 90;

    enum WinchState{OFF, RAISING, LOWERING}

    WinchState winchState = WinchState.OFF;


    @Override
    public void runOpMode(){
        bot.init(hardwareMap);
        super.setBot(bot);
        bot.setPose(SavedData.pose.x,SavedData.pose.y,Math.toDegrees(SavedData.pose.h));

        bot.closeClaw();

        bot.setWristPosition(0);

        waitForStart();


        while (opModeIsActive()){

            // handle drive train
            oneDriveCycle();

            // handle arm angle and slide length
            if (toggleA2.update()) {
                resettingArm = true;
            } else if (resettingArm && !gamepad2.a){
                resettingArm = false;
                bot.resetArm();
            }

            // update toggles
            boolean lb2Toggled = toggleLB2.update(); // lower arm and adjust slide and wrist for picking up samples
            boolean b2andDPR2Toggled = toggleB2andDPR2.update(); // Bring slide all the way in
            boolean dpl2Toggled = toggleDPL2.update();  // Move arm to 70 degrees
            boolean dpd2Toggled = toggleDPD2.update();
            boolean dpu2Toggled = toggleDPU2.update();
            boolean dpu1Toggled = toggleDPU1.update();
            boolean dpd1Toggled = toggleDPD1.update();

            double currentArmLength = bot.getSlideInches();
            double currentArmAngle = bot.getArmAngle();
            double targetArmLength = bot.getTargetSlideLength();
            double targetArmAngle = bot.getTargetArmAngle();

            // arm angle
            if (dpu2Toggled && !resettingArm){
                bot.setTargetArmAngleSafe(0);
            } else if (dpl2Toggled && !resettingArm){
                bot.setTargetArmAngleSafe(72);
            } else if (lb2Toggled && !resettingArm){
                bot.setTargetArmAngleSafe(-45);
            } else if (!resettingArm) {
               bot.setTargetArmAngleSafe(targetArmAngle + gamepad2.left_stick_y * 3.0);
            } else {
                bot.setTargetArmAngleUnSafe(targetArmAngle + gamepad2.left_stick_y * 3.0);
            }


           // slide length
            if (dpd2Toggled && !resettingArm) {
                bot.setTargetSlideLengthSafe(46);
            } else if (lb2Toggled && !resettingArm){
                bot.setTargetSlideLengthSafe(16);
            } else if (b2andDPR2Toggled && !resettingArm){
                bot.setTargetSlideLengthSafe(DeepBot.SLIDE_BASE_LENGTH);
            } else if (!resettingArm) {
                bot.setTargetSlideLengthSafe(targetArmLength - gamepad2.right_stick_y * 0.8);
            } else {
                bot.setTargetSlideLengthUnSafe(targetArmLength - gamepad2.right_stick_y * 0.8);
            }

            bot.updateArm();

            telemetry.addData("targetangle", targetArmAngle);
            telemetry.addData("targetlength", targetArmLength);
            telemetry.addData("armdegrees", currentArmAngle);
            telemetry.addData("slideinches", currentArmLength);
            telemetry.addData("LeftDist", bot.getLeftDistance());

            // handle claw

            boolean rb2Toggled = toggleRB2.update();

            switch (clawState){
                case OPEN:
                    if (rb2Toggled){
                        clawState = ClawState.CLOSED;
                        bot.closeClaw();
                    }
                    break;
                case CLOSED:
                    if (rb2Toggled){
                        clawState = ClawState.LOOSE;
                        bot.loosenClaw();
                    }
                    break;
                case LOOSE:
                    if (rb2Toggled){
                        clawState = ClawState.OPEN;
                        bot.openClaw();
                    }
            }

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
                targetWristAngle = DEFAULT_WRIST_ANGLE;
            } else if (gamepad2.x){
                targetWristAngle = DEFAULT_WRIST_ANGLE;
            } else if (gamepad2.y){
                targetWristAngle = DEFAULT_WRIST_ANGLE + 90;
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

            // handle winch

            switch (winchState){
                case OFF:
                    if (dpu1Toggled){
                        winchState = WinchState.RAISING;
                        bot.winchMotor.setTargetPosition(22800);
                        bot.winchMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        bot.winchMotor.setPower(1);
                    } else if (dpd1Toggled){
                        winchState = WinchState.LOWERING;
                        bot.winchMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                        bot.winchMotor.setPower(-1);
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
            }


            // report distances
            telemetry.addData("leftdistance", bot.getLeftDistance());
            telemetry.addData("backdistance", bot.getBackDistance());
            telemetry.addData("winch ticks",
                    bot.winchMotor.getCurrentPosition());

            telemetry.update();
        }
    }
}
