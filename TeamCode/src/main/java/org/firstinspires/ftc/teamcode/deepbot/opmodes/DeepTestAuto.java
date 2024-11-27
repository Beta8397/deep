package org.firstinspires.ftc.teamcode.deepbot.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.deepbot.DeepBot;
import org.firstinspires.ftc.teamcode.deepbot.DeepBotAuto;
import org.firstinspires.ftc.teamcode.util.MotionProfile;
import org.firstinspires.ftc.teamcode.util.Pose;
import org.firstinspires.ftc.teamcode.xdrive.XDrive;

@Autonomous
public class DeepTestAuto  extends DeepBotAuto {
    DeepBot bot = new DeepBot();

    MotionProfile speed = new MotionProfile(15, 30, 15);

    @Override
    public void runOpMode() throws InterruptedException {
        bot.init(hardwareMap);
        setBot(bot);


        bot.setClawPosition(DeepBot.CLAW_CLOSED);
        bot.setSlideInches(DeepBot.SLIDE_BASE_LENGTH);

        autoWaitForStart();

        bot.setPose(0, 0, 0);


        bot.setArmDegrees(70);
        ElapsedTime et = new ElapsedTime();
        while (opModeIsActive() && bot.isArmBusy()){
            continue;
        }

        bot.setSlideInches(44);
        et.reset();
        while (opModeIsActive() && bot.isSlideBusy()){
            continue;
        }

        double seconds = et.seconds();



        while (opModeIsActive()){
            Pose pose = bot.getPose();
            telemetry.addData("Pose", "x = %.1f  y = %.1f  h = %.1f",
                    pose.x, pose.y, Math.toDegrees(pose.h));
            telemetry.addData("arm", "target = %d  actual = %d",
                    bot.armMotor.getTargetPosition(), bot.armMotor.getCurrentPosition());
            telemetry.addData("slide", "target = %d  actual = %d",
                    bot.slideMotor.getTargetPosition(), bot.slideMotor.getCurrentPosition());
            telemetry.addData("seconds", seconds);
            telemetry.update();

        }
    }
}
