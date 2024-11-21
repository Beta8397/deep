package org.firstinspires.ftc.teamcode.deepbot.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.deepbot.DeepBot;
import org.firstinspires.ftc.teamcode.deepbot.DeepBotAuto;

@Autonomous
public class LeftAuto extends DeepBotAuto {

    DeepBot bot = new DeepBot();

    @Override
    public void runOpMode() throws InterruptedException {
        bot.init(hardwareMap);
        super.setBot(bot);

        bot.setClawPosition(DeepBot.CLAW_CLOSED);

        waitForStart();

        bot.setPose(-31, 7, 180);

        driveTo(normalSpeed, -33, 17, 180, 1);
        driveTo(normalSpeed, -45, 17, 180, 1);
        turnTo(-135, 90, 8, 2);

        deliverSampleHighBucket();

        bot.setWristPosition(.2);
        sleep(300);
        bot.setSlideInches(DeepBot.SLIDE_BASE_LENGTH);
        ElapsedTime et = new ElapsedTime();
        while(opModeIsActive() && bot.isSlideBusy() && et.milliseconds()<4000) continue;
        et.reset();
        bot.setArmDegrees(DeepBot.MIN_ARM_DEGREES);
        while(opModeIsActive() && bot.isSlideBusy() && et.milliseconds()<4000) continue;

        turnTo(180, 90, 8, 2);
        driveTo(normalSpeed, 40, 17, 180, 1);

        while(opModeIsActive()){
            int armTicks = bot.armMotor.getCurrentPosition();
            telemetry.addData("Angle", "ticks = %d  deg = %.1f",
                    armTicks, bot.armDegreesFromTicks(armTicks));
            int slideTicks = bot.slideMotor.getCurrentPosition();
            telemetry.addData("Length", "ticks = %d  inches = %.1f",
                    slideTicks, bot.slideInchesFromTicks(slideTicks));
            telemetry.addData("POS", "x = %.1f  y = %.1f  h = %.1f",
                    bot.getPose().x, bot.getPose().y, Math.toDegrees(bot.getPose().h));
            telemetry.update();
        }

    }
}
