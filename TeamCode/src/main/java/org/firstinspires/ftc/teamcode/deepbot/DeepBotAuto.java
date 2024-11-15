package org.firstinspires.ftc.teamcode.deepbot;

import org.firstinspires.ftc.teamcode.xdrive.XDriveAuto;

public abstract class DeepBotAuto extends XDriveAuto {

    public DeepBot bot;

    public void setBot(DeepBot b){

        super.setBot(b);

        bot = b;
    }

    public void deliverSpecimen(){

    }

    public void deliverSampleHighBucket(){
        bot.setTargetArmAngleSafe(70);
        bot.updateArm(false);
        sleep(5000);
        bot.setWristPosition(0.2);
        sleep(300);
        bot.setTargetSlideLengthSafe(48);
        bot.updateArm(false);
        sleep(5000);
        bot.setWristPosition(0.8);
        sleep(400);
        bot.setClawPosition(DeepBot.CLAW_OPEN);
        sleep(300);


    }

}
