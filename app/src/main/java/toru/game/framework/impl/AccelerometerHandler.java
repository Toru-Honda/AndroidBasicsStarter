package toru.game.framework.impl;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Toru on 2015/04/06.
 * Implementation of SensorEventListener for accelerometer.
 */
public class AccelerometerHandler implements SensorEventListener {
    protected float accelX;
    protected float accelY;
    protected float accelZ;

    public AccelerometerHandler(Context context) {
        SensorManager manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if(manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {
            Sensor accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //memo: プリミティブ型はアトミックなので同期はとらない。
        accelX = event.values[0];
        accelY = event.values[1];
        accelZ = event.values[2];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Nothing to do
    }

    /**
     * X axis sensor value getter.
     * @return X axis sensor value.
     */
    public float getAccelX() {
        return accelX;
    }

    /**
     *YX axis sensor value getter.
     * @return Y axis sensor value.
     */
    public float getAccelY() {
        return accelY;
    }

    /**
     * Z axis sensor value getter.
     * @return Z axis sensor value.
     */
    public float getAccelZ() {
        return accelZ;
    }
}
