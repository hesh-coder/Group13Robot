package Test;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.NXTSoundSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;

public class Main {
	// One behaviour goes in a circle, one behaviour goes in a square
	// Clap to swap between them
	// If robot sees an object it stops the program

	public static MovePilot power;// Make pilot to control movement
	public static EV3UltrasonicSensor us = new EV3UltrasonicSensor(SensorPort.S2);// Get the sensor and initialise
	public static SampleProvider sp = us.getDistanceMode();// Get the sample provider and pass it the sensor

	private static Behavior nextBehavior;// Make a variable to hold the next behaviour to run

	private final float MAGIC_WHEEL_DIAMETER = 56;// Wheel diameter in mm
	private final float MAGIC_AXEL_LENGTH = -55;// Axel length in mm

	private void makePilot() {
		RegulatedMotor leftM = new EV3LargeRegulatedMotor(MotorPort.A);// Initialise the motor
		Wheel leftW = WheeledChassis.modelWheel(leftM, MAGIC_WHEEL_DIAMETER).offset(-MAGIC_AXEL_LENGTH / 2);
		// Pass the motor to the wheel class and give it the wheel and axel values

		RegulatedMotor rightM = new EV3LargeRegulatedMotor(MotorPort.B);// Initialise the motor
		Wheel rightW = WheeledChassis.modelWheel(rightM, MAGIC_WHEEL_DIAMETER).offset(MAGIC_AXEL_LENGTH / 2);
		// Pass the motor to the wheel class and give it the wheel and axel values

		Chassis chas = new WheeledChassis(new Wheel[] { rightW, leftW }, WheeledChassis.TYPE_DIFFERENTIAL);
		// Define the chassis

		power = new MovePilot(chas);// Create the move pilot with the chassis
	}// Create the move pilot for the behaviours to call

	public Main() {
		makePilot();// Create the pilot
		float[] samples = new float[2];// Create a float to store the samples
		
		NXTSoundSensor ss = new NXTSoundSensor(SensorPort.S4);//Initialise the sound sensor
		SampleProvider sound = ss.getDBAMode();//Create a sample provider for the sound sensor
		ClapFilter cf = new ClapFilter (ss.getMode(ss.getCurrentMode()),ss,samples[0],500);
		//Create a clap filter and pass it the sensor and initialisation variables
		
		Thread bc = new BehaviorCheck();// Create a new thread to check for the next behaviour to run
		bc.start();// Start the thread
		sp.fetchSample(samples, 0);// Fetch a distance sample
		while (samples[0] < 0.20f) {// loop while an object is not within 20cm of the robot
			sp.fetchSample(samples, 0);// Fetch a distance sample
			cf.fetchSample(samples, 1);//Fetch a sound sample
			if(samples[1] > 0.9) {//If teh value returned is above 0.9(it should be 1 anyway)
				((BehaviorCheck) bc).clapHeard();//Swap the boolean value in the Behaviour check class
			}
			if (nextBehavior != null) {// If there is another behaviour to run
				nextBehavior.action();// Run the behaviour set in the next behaviour
			}
		}
		((BehaviorCheck) bc).stopThread();// Stop the behaviour check thread
	}// Runs the code to do the behaviours

	public static void setNextBehavior(Behavior nextBehaviorIn) {
		nextBehavior = nextBehaviorIn;// Set the next behaviour
	}// Allows the BehaviourCheck Thread to set the next behaviour

	public static void main(String[] args) {
		new Main();// Creates a new instance of the Main class so that not all variables are static
	}// THe main method that starts the program
}
