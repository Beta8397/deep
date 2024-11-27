package org.firstinspires.ftc.teamcode.deepbot.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.deepbot.DeepBot;
import org.firstinspires.ftc.teamcode.deepbot.DeepBotAuto;

@Autonomous
@Disabled
public class RedLeftAutoSimple extends DeepBotAuto {

    DeepBot bot = new DeepBot();

    @Override
    public void runOpMode() throws InterruptedException {
        bot.init(hardwareMap);
        super.setBot(bot);

        waitForStart();

        bot.setPose(64,-29, -90);


        deliverSampleHighBucket();

        driveTo(medium, 58, -29, -90, 1);

        driveTo(medium, 62, 42, -90,1);

    }
}
