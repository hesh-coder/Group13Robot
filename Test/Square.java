package Test;

import lejos.robotics.subsumption.Behavior;

public class Square implements Behavior {

	public void action() {
		Main.power.travel(0.2);// Moves the robot forwards by 20cm
		Main.power.rotate(90);// Rotates the robot by 90 degrees
	}// The action of the behaviour

	public void suppress() {
	}// What to do if the behaviour is suppressed

	public boolean takeControl() {
		return true;
	}// Returns whether the behaviour wants to take control
}