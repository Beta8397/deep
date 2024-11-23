package org.firstinspires.ftc.teamcode.deepbot.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

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

        deliverSpecimen();


        while (opModeIsActive()){
            Pose pose = bot.getPose();
            telemetry.addData("Pose", "x = %.1f  y = %.1f  h = %.1f",
                    pose.x, pose.y, Math.toDegrees(pose.h));
            telemetry.update();

        }
    }
}
