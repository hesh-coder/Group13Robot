import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.NXTSoundSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class DelatTester {
	public static void main(String[] args) {
		
		RegulatedMotor mLeft = new EV3LargeRegulatedMotor(MotorPort.A);
		RegulatedMotor mRight = new EV3LargeRegulatedMotor(MotorPort.B);
		mLeft.synchronizeWith(new RegulatedMotor[] {mRight});
		mLeft.setSpeed(500);
		mRight.setSpeed(500);
		
		float[] levelS = new float[] {0.7f} ;
		
		
		NXTSoundSensor ss = new NXTSoundSensor(SensorPort.S4);
		SampleProvider sound = ss.getDBAMode();
		ClapFilter cf = new ClapFilter (ss.getMode(ss.getCurrentMode()),ss,levelS[0],500);
		
		EV3UltrasonicSensor us = new EV3UltrasonicSensor(SensorPort.S1);
		SampleProvider di = us.getDistanceMode();
		

		float[] dist = new float[1];
		double stopDist = 0.5;
		
		levelS[0] = 0;
		dist[0] = 1 ;
		
		while(levelS[0] == 0) {
			sound.fetchSample(levelS, 0);
			cf.fetchSample(levelS, 0);
		}
		
		
		mLeft.startSynchronization();
		mLeft.forward();
		mRight.forward();
		mLeft.endSynchronization();	
		
		while(dist[0] > stopDist && ! Button.ENTER.isDown()) {
		
			di.fetchSample(dist, 0);
			sound.fetchSample(levelS, 0);
			cf.fetchSample(levelS, 0);
			LCD.drawString("Sound Value: "+ Float.toString(levelS[0]),0,2);
			Delay.msDelay(50);
			
			//Rotates and sets motors forward again if there's a clap
			if(levelS[0] == 1) {
				mLeft.startSynchronization();
				mLeft.stop();
				mRight.stop();
				mLeft.endSynchronization();
				
				mLeft.rotate(360, false);
				
				mLeft.startSynchronization();
				mLeft.forward();
				mRight.forward();
				mLeft.endSynchronization();	
			}
			
		}
		mLeft.startSynchronization();
		mLeft.stop();
		mRight.stop();
		mLeft.endSynchronization();	
		mLeft.close();
		mRight.close();
		us.close();
		ss.close();
	}
}


