package ca.uwaterloo.Lab2_201_10;

public class FiniteStateMachine {

	public enum State {
		Still, Swing, Wait
	}

	State currentState;
	float currentMaxX, currentMinX, stillCounter;

	public FiniteStateMachine() {
		this.currentState = FiniteStateMachine.State.Still;
		currentMaxX = 0;
		currentMinX = 0;
		stillCounter = 0;
	}

	public State getState() {
		return this.currentState;
	}

	public void updateState(float[] sum) {
		currentMaxX = getMaxX(sum);
		currentMinX = getMinX(sum);
		
		switch (currentState) {
		case Wait:
			if (currentMaxX > 5) {
				this.currentState = FiniteStateMachine.State.Swing;
			}
			else if (currentMinX < -5){
				this.currentState = FiniteStateMachine.State.Swing;
			}
			break;

		case Swing:
			if (currentMaxX - currentMinX > 9) {
				this.currentState = FiniteStateMachine.State.Still;
				MainActivity.PlaceholderFragment.playSound();
			}
			break;
			
		case Still:
			stillCounter++;
			if (stillCounter == 1){
				this.currentState = FiniteStateMachine.State.Wait;
				stillCounter = 0;
			}
			break;
		}
			
	}
	
	public float getMaxX(float[] x)
	{
		float max = x[0];
		for(int i = 1; i < x.length; i++)
		{
			if(Math.abs(x[i]) > max)
			{
				max = x[i];
			}
		}
		
		return max;
	}
	
	public float getMinX(float[] x)
	{
		float min = x[0];
		for(int i = 1; i < x.length; i++)
		{
			if(Math.abs(x[i]) < min)
			{
				min = x[i];
			}
		}
		
		return min;
	}
}
