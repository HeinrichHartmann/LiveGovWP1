package eu.liveandgov.sensorcollectorv3;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

import eu.liveandgov.sensorcollectorv3.SensorValueObjects.AccSensorValue;
import eu.liveandgov.sensorcollectorv3.SensorValueObjects.GravSensorValue;
import eu.liveandgov.sensorcollectorv3.SensorValueObjects.LacSensorValue;
import eu.liveandgov.sensorcollectorv3.SensorValueObjects.SensorValue;

/**
 * Created by hartmann on 9/15/13.
 */
public class SensorParser {
    private String id;

    public SensorParser(String id){
        this.id = id;
    }

    public String parse(SensorEvent event) {
        int sensorType= event.sensor.getType();
        if ( sensorType == Sensor.TYPE_ACCELEROMETER){
            return fillString("ACC", event.timestamp / 1000, id, event.values);
        } else if (sensorType == Sensor.TYPE_LINEAR_ACCELERATION){
            return fillString("LAC", event.timestamp / 1000, id, event.values);
        } else if (sensorType == Sensor.TYPE_GRAVITY) {
            return fillString("GRA", event.timestamp / 1000, id, event.values);
        }
        return "ERR,,,Unknown sensor " + sensorType;
    }

    /**
     * Writes sensor values in SSF format. (cf. project Wiki)
     * @param type of Sensor
     * @param timestamp of recording in ms
     * @param deviceId unique device identified
     * @param values float array
     * @return ssfRow
     */
    private static String fillString(String type, long timestamp, String deviceId, float[] values) {
        assert(values.length >= 2);
        return String.format("%s,%d,%s,%f %f %f", type, timestamp, deviceId, values[0], values[1], values[2]);
    }
}
