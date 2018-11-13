package Test;

import lejos.robotics.subsumption.Behavior;

public class Circle implements Behavior {

	public void action() {
		Main.power.arcForward(0.2f);// Moves the robot in a circle with radius of 20cm
	}// The action of the behaviour

	public void suppress() {
	}// What to do if the behaviour is suppressed

	public boolean takeControl() {
		return true;
	}// Returns whether the behaviour wants to take control
}
