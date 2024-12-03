package org.firstinspires.ftc.teamcode.deepbot;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.logging.BetaLog;
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
        XDrive.OtosLocalizer loc =  bot.getLocalizer();
        bot.setClawPosition(DeepBot.CLAW_CLOSED);
        bot.setSlideInches(DeepBot.SLIDE_BASE_LENGTH);
        telemetry.addData("Let Go Of Bot", "");
        telemetry.update();
        sleep(3000);
        loc.calibrateIMU();
        while(opModeInInit() && loc.getCalibrationProgress()>0){
            telemetry.addData("Calibrating...", "");
            telemetry.update();
        }

        telemetry.addData("Press START when ready...", "");
        telemetry.update();
        waitForStart();
    }

    public boolean slideBusy(){
        return Math.abs((bot.slideMotor.getCurrentPosition() - bot.slideMotor.getTargetPosition()) ) > 10;
    }
    public boolean armBusy(){
        return Math.abs((bot.armMotor.getCurrentPosition() - bot.armMotor.getTargetPosition()) ) > 10;
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
        bot.armMotor.getCurrentPosition();
        bot.slideMotor.getCurrentPosition();

        ElapsedTime et = new ElapsedTime();
        bot.setArmDegrees(70);

        BetaLog.d("DELIVERY SAMPLE HIGH BUCKET: ARM LOOP");

        while(opModeIsActive() && et.milliseconds()<5000) {
            int armTarget = bot.armMotor.getTargetPosition();
            int armCurrent = bot.armMotor.getCurrentPosition();
            BetaLog.dd("Arm", "target = %d   current  = %d ",
                    armTarget, armCurrent);
            if (Math.abs(armTarget -armCurrent) < 10){
                break;
            }
        }
        bot.setWristPosition(0.2);
        sleep(300);
        et.reset();
        bot.setSlideInches(44);


        BetaLog.d("DELIVERY SAMPLE HIGH BUCKET: SLIDE LOOP");

        while(opModeIsActive() && et.milliseconds()<5000) {
            int slideTarget = bot.slideMotor.getTargetPosition();
            int slideCurrent = bot.slideMotor.getCurrentPosition();
            BetaLog.dd("Slide", "target = %d   current  = %d ",
                    slideTarget, slideCurrent);
            if (Math.abs(slideTarget -slideCurrent) < 10){
                break;
            }
        }
        bot.setWristPosition(0.8);
        sleep(400);
        bot.setClawPosition(DeepBot.CLAW_OPEN);
        sleep(500);
    }

}
