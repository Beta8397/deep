package org.firstinspires.ftc.teamcode.deepbot.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.deepbot.DeepBot;
import org.firstinspires.ftc.teamcode.util.Toggle;
import org.firstinspires.ftc.teamcode.xdrive.XDriveTele;

@TeleOp
public class DeepTeleop extends XDriveTele {
    DeepBot bot = new DeepBot();

    Toggle toggleLB1 = new Toggle(()->gamepad1.left_bumper);
    Toggle toggleA2 = new Toggle(()->gamepad2.a);
    Toggle toggleRB1 = new Toggle(()->gamepad1.right_bumper);
    Toggle toggleDPUp1 = new Toggle(()->gamepad1.dpad_up);

    boolean ascending =false;
    boolean resettingArm = false;
    boolean clawOpen = true;
    boolean wristUp = false;

    @Override
    public void runOpMode(){
        bot.init(hardwareMap);
        super.setBot(bot);
        bot.setPose(0,0,0);

        bot.closeClaw();
        bot.setWristDown();

        waitForStart();


        while (opModeIsActive()){

            // handle drive train
            oneDriveCycle();

            // handle arm
            if (toggleA2.update()){
                resettingArm = true;
            } else if (resettingArm && !gamepad2.a){
                resettingArm = false;
                bot.resetArm();
            }

            double currentArmLength = bot.getArmLength();
            double currentArmAngle = bot.getArmAngle();
            double targetArmLength = bot.getTargetSlideLength();
            double targetArmAngle = bot.getTargetArmAngle();

            if (!resettingArm) {
                bot.setTargetArmAngleSafe(targetArmAngle + gamepad1.left_stick_y * 0.5);
                bot.setTargetSlideLengthSafe(targetArmLength - gamepad1.right_stick_y * 0.2);
                bot.updateArm(ascending);
            } else {
                bot.setTargetArmAngleUnSafe(targetArmAngle + gamepad1.left_stick_y * 0.5);
                bot.setTargetSlideLengthUnSafe(targetArmLength - gamepad1.right_stick_y * 0.2);
                bot.updateArm(true);
            }

            telemetry.addData("targetangle", targetArmAngle);
            telemetry.addData("targetlength", targetArmLength);
            telemetry.addData("armdegrees", currentArmAngle);
            telemetry.addData("slideinches", currentArmLength);

            // handle claw

            if (toggleRB1.update()){
                clawOpen = !clawOpen;
                if (clawOpen){
                    bot.openClaw();
                } else {
                    bot.closeClaw();

                }
            }

            // handle wrist

            if (toggleDPUp1.update()){
                wristUp = !wristUp;
                if (wristUp){
                    bot.setWristUp();
                } else {
                    bot.setWristDown();

                }
            }

            telemetry.update();
        }
    }
}
