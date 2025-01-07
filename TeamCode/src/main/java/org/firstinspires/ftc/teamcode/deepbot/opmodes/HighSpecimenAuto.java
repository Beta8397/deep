package org.firstinspires.ftc.teamcode.deepbot.opmodes;

import android.util.Pair;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.deepbot.DeepBot;
import org.firstinspires.ftc.teamcode.deepbot.DeepBotAuto;
import org.firstinspires.ftc.teamcode.util.Pose;

@Autonomous
public class HighSpecimenAuto extends DeepBotAuto {

    DeepBot bot = new DeepBot();
    boolean stopEarly = true;

    @Override
    public void runOpMode() throws InterruptedException {
        bot.init(hardwareMap);
        super.setBot(bot);

        autoWaitForStart();

        bot.setPose(0, 7.5, 90);

        bot.setArmDegrees(21);
        bot.setSlideInches(20);
        bot.setWristPosition(0.5);


        driveTo(normalSpeed, 0, 39, 90, 1);

        driveUntilStopped(8, 90, p->p.y < 0.5);

//
//        bot.setDriveSpeed(0,8,0);
//        ProgressChecker pc = new ProgressChecker(500);
//        ElapsedTime et = new ElapsedTime();
//        while(opModeIsActive()){
//            bot.updateOdometry();
//            if(et.milliseconds()> 500){
//                Pair<Double, Pose> progress = pc.check();
//                if (progress != null  && progress.second.y < 0.5){
//                    break;
//                }
//            }
//        }
//
//        bot.setDriveSpeed(0,0,0);

        bot.openClaw();
        sleep(300);
        bot.setSlideInches(DeepBot.SLIDE_BASE_LENGTH);

        bot.setPose(bot.getPose().x, 41, 90);
        driveTo(fast, 28, 24, 90, 1);
        driveTo(fast, 28, 58, 90, 1);
        driveTo(fast, 45, 58, 90, 1);
        turnTo(-90, 90, 8, 2);
        bot.setArmDegrees(30);
        driveTo(fast, 45, 18, -90, 1);

        driveUntilStopped(8, -90, p->p.y > -0.5);

//        bot.setDriveSpeed(0,8,0);
//        pc = new ProgressChecker(500);
//        et.reset();
//        while(opModeIsActive()){
//            bot.updateOdometry();
//            if(et.milliseconds()> 500){
//                Pair<Double, Pose> progress = pc.check();
//                if (progress != null  && progress.second.y > -0.5){
//                    break;
//                }
//            }
//        }
//        bot.setDrivePower(0, 0 ,0 );


        bot.setPose(bot.getPose().x, 7.5, Math.toDegrees(bot.getPose().h));
        driveTo(fast, 45, 28, -90, 1);
        bot.setSlideInches(20);
        bot.setArmDegrees(-15);
        bot.setWristPosition(0.5);
        bot.openClaw();
        sleep(3000);
        driveTo(normalSpeed, 45, 24, -90, .5);


        while (opModeIsActive()){
            continue;
        }
    }
}
