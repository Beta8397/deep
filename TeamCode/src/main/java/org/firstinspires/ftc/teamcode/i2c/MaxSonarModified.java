package org.firstinspires.ftc.teamcode.i2c;

import com.qualcomm.hardware.maxbotix.MaxSonarI2CXL;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.I2cDeviceType;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@I2cDeviceType
@DeviceProperties(xmlTag = "MaxSonarModified", name = "MaxSonar Modified")
public class MaxSonarModified extends MaxSonarI2CXL {


    //------------------------------------------------------------------------------------------
    // Constructor
    //------------------------------------------------------------------------------------------

    public MaxSonarModified(I2cDeviceSynch deviceClient)
    {
        super(deviceClient);
    }


    /**
     * If at least sonarPropagationDelayMs have elapsed since the last ping, returns the ranging
     * result, and sends another ping. Otherwise, returns -1.
     *
     * @param sonarPropagationDelayMs Number of milliseconds that are required to elapse since the
     *                                last ping before a ranging result will be returned and a new
     *                                ping sent.
     *
     * @param units DistanceUnit to be used for the result
     *
     * @return ranging result (if sufficient time has elapsed since the last ping), or -1.
     */
    @Override
    public double getDistanceAsync(int sonarPropagationDelayMs, DistanceUnit units)
    {
        long curTime = System.currentTimeMillis();

        double distance;

        if (sonarPropagationDelayMs == 0) {
            ping();
            distance = -1;
        } else if(((curTime - lastPingTime) > sonarPropagationDelayMs)) {
            distance = getRangingResult(units);
            ping();
            lastPingTime = System.currentTimeMillis();
        } else {
            distance = -1;
        }

        return distance;
    }

}
