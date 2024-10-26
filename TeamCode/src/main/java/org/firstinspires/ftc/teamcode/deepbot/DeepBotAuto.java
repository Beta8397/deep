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

    }

}
