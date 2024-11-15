package org.firstinspires.ftc.teamcode.deepbot.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.deepbot.DeepBot;
import org.firstinspires.ftc.teamcode.deepbot.DeepBotAuto;
import org.firstinspires.ftc.teamcode.util.MotionProfile;
import org.firstinspires.ftc.teamcode.util.Pose;
@Autonomous
public class DeepTestAuto  extends DeepBotAuto {
    DeepBot bot = new DeepBot();

    MotionProfile speed = new MotionProfile(15, 30, 15);

    @Override
    public void runOpMode() throws InterruptedException {
        bot.init(hardwareMap);
        setBot(bot);


        bot.setClawPosition(DeepBot.CLAW_CLOSED);


        waitForStart();

        bot.setPose(47, 47, 180);

        deliverSampleHighBucket();

//        driveTo(speed, -47, 47, 180, 1);
//        turnTo(-90, 90, 8, 3);
//        driveTo(speed, -47, -47, -90, 1);
//        turnTo(0, 90, 8, 3);
//        driveTo(speed, 47, -47, 0, 1);
//        turnTo(90, 90, 8, 3);
//        driveTo(speed, 47, 47, 90, 1);

        while (opModeIsActive()){
            Pose pose = bot.getPose();
            telemetry.addData("Pose", "x = %.1f  y = %.1f  h = %.1f",
                    pose.x, pose.y, Math.toDegrees(pose.h));
            telemetry.update();

        }
    }
}
