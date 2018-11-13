package Test;

import lejos.robotics.subsumption.Behavior;

public class BehaviorCheck extends Thread {

	Behavior[] behaviors = new Behavior[] { new Circle(), new Square() };
	// Stores all the behaviours, in priority order
	boolean run;// Boolean that keeps the thread running
	boolean circle = false;

	public void stopThread() {
		run = false;
	}// Sets the run variable to false so that the thread stops

	public void clapHeard() {
		circle = !circle;
	}//Set the boolean variable to its opposite

	public void run() {
		while (run) {// Loops while the run variable is true
			if (circle) {//If the value circle is true
				Main.setNextBehavior(behaviors[0]);// Set the circle behaviour as the next to run in the Main class
			} else {//Otherwise
				Main.setNextBehavior(behaviors[1]);// Set the square behaviour as the next to run in the Main class
			}
		}
	}
}
