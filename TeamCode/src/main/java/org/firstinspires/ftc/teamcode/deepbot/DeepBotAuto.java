package org.firstinspires.ftc.teamcode.deepbot;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.util.MotionProfile;
import org.firstinspires.ftc.teamcode.xdrive.XDrive;
import org.firstinspires.ftc.teamcode.xdrive.XDriveAuto;

public abstract class DeepBotAuto extends XDriveAuto {

    public DeepBot bot;

    public MotionProfile normalSpeed = new MotionProfile(10, 20, 10);

    public void setBot(DeepBot b){

        super.setBot(b);

        bot = b;
    }

    public void autoWaitForStart(){
        XDrive.OtosLocalizer loc = (XDrive.OtosLocalizer) bot.getLocalizer();
        loc.calibrateIMU();
        sleep(3000);
        while(opModeInInit() && loc.getCalibrationProgress()>0){
            telemetry.addData("Calibrating...", "");
            telemetry.update();
        }

        telemetry.addData("Press START when ready...", "");
        telemetry.update();
        waitForStart();
    }

    public void deliverSpecimen(){
        bot.setArmDegrees(30);
        sleep(5000);
        bot.setWristPosition(0.35);
        sleep(500);
        bot.setSlideInches(30);
        sleep(3000);
        bot.setArmDegrees(15);
        sleep(3000);
        bot.setSlideInches(33);
        sleep(1000);
        bot.setClawPosition(DeepBot.CLAW_OPEN);
        sleep(500);
        bot.setSlideInches(DeepBot.SLIDE_BASE_LENGTH);
        sleep(3000);
        bot.setArmDegrees(DeepBot.MIN_ARM_DEGREES);
        sleep(3000);
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
