package org.firstinspires.ftc.teamcode.deepbot.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.deepbot.DeepBot;
import org.firstinspires.ftc.teamcode.deepbot.DeepBotAuto;

@Autonomous
@Disabled
public class RedRightAutoSimple extends DeepBotAuto {

    DeepBot bot = new DeepBot();

    @Override
    public void runOpMode() throws InterruptedException {
        bot.init(hardwareMap);
        super.setBot(bot);

        waitForStart();

        bot.setPose(64,16, 180);

        driveTo(medium, 41, 7, 180, 1);

        deliverSpecimen();

        driveTo(medium, 64, 64, 180, 1);

    }
}
