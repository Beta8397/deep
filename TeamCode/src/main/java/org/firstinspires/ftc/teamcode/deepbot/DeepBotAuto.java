package org.firstinspires.ftc.teamcode.deepbot;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.util.MotionProfile;
import org.firstinspires.ftc.teamcode.xdrive.XDriveAuto;

public abstract class DeepBotAuto extends XDriveAuto {

    public DeepBot bot;

    public MotionProfile normalSpeed = new MotionProfile(10, 20, 10);

    public void setBot(DeepBot b){

        super.setBot(b);

        bot = b;
    }

    public void deliverSpecimen(){

    }

    public void deliverSampleHighBucket(){
        ElapsedTime et = new ElapsedTime();
        bot.setArmDegrees(70);
        sleep(5000);
//        while(opModeIsActive() && bot.isArmBusy() && et.milliseconds()<6000) continue;
        bot.setWristPosition(0.2);
        sleep(300);
        bot.setSlideInches(44);
        et.reset();
        sleep(5000);
//        while(opModeIsActive() && bot.isSlideBusy() && et.milliseconds()<6000) continue;
        bot.setWristPosition(0.8);
        sleep(400);
        bot.setClawPosition(DeepBot.CLAW_OPEN);
        sleep(300);
    }

}
