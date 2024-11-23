package org.firstinspires.ftc.teamcode.deepbot.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.deepbot.DeepBot;
import org.firstinspires.ftc.teamcode.deepbot.DeepBotAuto;
import org.firstinspires.ftc.teamcode.util.Pose;
import org.firstinspires.ftc.teamcode.xdrive.XDrive;

@Autonomous
public class LeftAuto extends DeepBotAuto {

    DeepBot bot = new DeepBot();

    @Override
    public void runOpMode() throws InterruptedException {
        bot.init(hardwareMap);
        super.setBot(bot);

        bot.setClawPosition(DeepBot.CLAW_CLOSED);
        bot.setSlideInches(DeepBot.SLIDE_BASE_LENGTH);

        autoWaitForStart();

        bot.setPose(-31, 7, 180);

        driveTo(normalSpeed, -33, 15, 180, 1);
        driveTo(normalSpeed, -47, 15, 180, 1);
        turnTo(-135, 90, 8, 2);

        Pose pose1 = bot.getPose();

        deliverSampleHighBucket();

        bot.setWristPosition(.2);
        sleep(300);
        bot.setSlideInches(DeepBot.SLIDE_BASE_LENGTH);
        ElapsedTime et = new ElapsedTime();
        while(opModeIsActive() && bot.isSlideBusy() && et.milliseconds()<4000) continue;
        et.reset();
        bot.setArmDegrees(DeepBot.MIN_ARM_DEGREES);
        while(opModeIsActive() && bot.isSlideBusy() && et.milliseconds()<4000) continue;

        Pose pose2 = bot.getPose();
        bot.refreshPose();
        turnTo(180, 90, 8, 2);
        driveTo(normalSpeed, 40, 15, 180, 1);

        while(opModeIsActive()){
            int armTicks = bot.armMotor.getCurrentPosition();
            telemetry.addData("Angle", "ticks = %d  deg = %.1f",
                    armTicks, bot.armDegreesFromTicks(armTicks));
            int slideTicks = bot.slideMotor.getCurrentPosition();
            telemetry.addData("Length", "ticks = %d  inches = %.1f",
                    slideTicks, bot.slideInchesFromTicks(slideTicks));
            addPoseToTelemetry("pose1", pose1);
            addPoseToTelemetry("pose2", pose2);
            telemetry.addData("POS", "x = %.1f  y = %.1f  h = %.1f",
                    bot.getPose().x, bot.getPose().y, Math.toDegrees(bot.getPose().h));
            telemetry.update();
        }

    }
}
