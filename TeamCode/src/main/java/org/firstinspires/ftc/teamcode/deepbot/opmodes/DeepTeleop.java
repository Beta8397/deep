package org.firstinspires.ftc.teamcode.deepbot.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.deepbot.DeepBot;
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

    boolean ascending =false;
    boolean resettingArm = false;
    boolean clawOpen = true;
    boolean wristUp = false;

    private static final double WRIST_P0 = 0.642;
    private static final double WRIST_GAIN = -270;
    private static final double DEFAULT_WRIST_ANGLE = 0;
    private double targetWristAngle = DEFAULT_WRIST_ANGLE;


    @Override
    public void runOpMode(){
        bot.init(hardwareMap);
        super.setBot(bot);
        bot.setPose(0,0,0);

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

            boolean lb2Toggled = toggleLB2.update(); // lower arm and adjust slide and wrist for picking up samples
            boolean b2andDPR2Toggled = toggleB2andDPR2.update(); // Bring slide all the way in
            boolean dpl2Toggled = toggleDPL2.update();  // Move arm to 70 degrees

            double currentArmLength = bot.getArmLength();
            double currentArmAngle = bot.getArmAngle();
            double targetArmLength = bot.getTargetSlideLength();
            double targetArmAngle = bot.getTargetArmAngle();

            // arm angle
            if (dpl2Toggled && !resettingArm){
                bot.setTargetArmAngleSafe(70);
            } else if (lb2Toggled && !resettingArm){
                bot.setTargetArmAngleSafe(-45);
            } else if (!resettingArm) {
               bot.setTargetArmAngleSafe(targetArmAngle + gamepad2.left_stick_y * 1.0);
            } else {
                bot.setTargetArmAngleUnSafe(targetArmAngle + gamepad2.left_stick_y * 1.0);
            }


           // slide length
            if (lb2Toggled && !resettingArm){
                bot.setTargetSlideLengthSafe(16);
            } else if (b2andDPR2Toggled && !resettingArm){
                bot.setTargetSlideLengthSafe(DeepBot.SLIDE_BASE_LENGTH);
            } else if (!resettingArm) {
                bot.setTargetSlideLengthSafe(targetArmLength - gamepad2.right_stick_y * 0.2);
            } else {
                bot.setTargetSlideLengthUnSafe(targetArmLength - gamepad2.right_stick_y * 0.2);
            }

            bot.updateArm();

            telemetry.addData("targetangle", targetArmAngle);
            telemetry.addData("targetlength", targetArmLength);
            telemetry.addData("armdegrees", currentArmAngle);
            telemetry.addData("slideinches", currentArmLength);

            // handle claw

            if (toggleRB2.update()){
                clawOpen = !clawOpen;
                if (clawOpen){
                    bot.openClaw();
                } else {
                    bot.closeClaw();

                }
            }

            // handle wrist

            double wristChange = gamepad2.left_trigger - gamepad2.right_trigger;
            if (lb2Toggled && !resettingArm){
                targetWristAngle = DEFAULT_WRIST_ANGLE;
            } else if (gamepad2.x){
                targetWristAngle = DEFAULT_WRIST_ANGLE;
            } else if (gamepad2.y){
                targetWristAngle = DEFAULT_WRIST_ANGLE - 90;
            } else {
                targetWristAngle += wristChange;
            }

            double wristPos = WRIST_P0 + (targetWristAngle - currentArmAngle) / WRIST_GAIN;
            if (wristPos > 1){
                targetWristAngle = currentArmAngle + (1.0 - WRIST_P0) * WRIST_GAIN;
                wristPos = 1;
            }else if (wristPos < 0){
                targetWristAngle = currentArmAngle + (0.0 - WRIST_P0) * WRIST_GAIN;
                wristPos = 0;
            }
            bot.setWristPosition(wristPos);

            // report distances
            telemetry.addData("leftdistance", bot.getLeftDistance());
            telemetry.addData("backdistance", bot.getBackDistance());

            telemetry.update();
        }
    }
}
