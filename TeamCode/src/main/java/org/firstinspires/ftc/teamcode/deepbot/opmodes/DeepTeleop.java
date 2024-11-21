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
    Toggle toggleB2 = new Toggle(()-> gamepad2.b);
    Toggle toggleX2 = new Toggle(()->gamepad2.x);
    Toggle toggleDPUp2 = new Toggle(()->gamepad2.dpad_up);

    boolean ascending =false;
    boolean resettingArm = false;
    boolean clawOpen = true;
    boolean wristUp = false;

    private static final double WRIST_P0 = 0.633;
    private static final double WRIST_GAIN = -268;
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

            // handle arm
            if (toggleA2.update()) {
                resettingArm = true;
            } else if (resettingArm && !gamepad2.a){
                resettingArm = false;
                bot.resetArm();
            }

            if (toggleB2.update()){
                boolean powerOff = bot.getArmPowerOff();
                bot.setArmPowerOff(!powerOff);
            }


            double currentArmLength = bot.getArmLength();
            double currentArmAngle = bot.getArmAngle();
            double targetArmLength = bot.getTargetSlideLength();
            double targetArmAngle = bot.getTargetArmAngle();

           if (!resettingArm) {
               bot.setTargetArmAngleSafe(targetArmAngle + gamepad2.left_stick_y * 1.0);
               bot.setTargetSlideLengthSafe(targetArmLength - gamepad2.right_stick_y * 0.2);
                bot.updateArm(ascending);
            } else {
                bot.setTargetArmAngleUnSafe(targetArmAngle + gamepad2.left_stick_y * 1.0);
                bot.setTargetSlideLengthUnSafe(targetArmLength - gamepad2.right_stick_y * 0.2);
                bot.updateArm(true);
            }

            telemetry.addData("targetangle", targetArmAngle);
            telemetry.addData("targetlength", targetArmLength);
            telemetry.addData("armdegrees", currentArmAngle);
            telemetry.addData("slideinches", currentArmLength);

            // handle claw

            if (toggleX2.update()){
                clawOpen = !clawOpen;
                if (clawOpen){
                    bot.openClaw();
                } else {
                    bot.closeClaw();

                }
            }

            // handle wrist

            if (gamepad2.dpad_up){
                targetWristAngle -= 1;
            } else if(gamepad2.dpad_down){
                targetWristAngle += 1;
            } else if (gamepad2.x){
                targetWristAngle = DEFAULT_WRIST_ANGLE;
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

            telemetry.update();
        }
    }
}
