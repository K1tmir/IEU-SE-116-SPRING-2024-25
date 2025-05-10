import java.io.Serializable;
import java.util.ArrayList;

public class TransitionContainer implements Serializable {
     ArrayList<Transition> transitions = new ArrayList<>();

    public ArrayList<Transition> getTransitions() {
        return transitions;
    }

    public void addTransition(Transition transition) {
        transitions.add(transition);
    }

    public Transition getTransitionWithSymbolAndCurrentState(char symbol, String currentState) {
        for (Transition transition : transitions) {
            if (transition.getSymbol() == symbol && transition.getCurrentState().equals(currentState)) {
                return transition;
            }
        }
        return null;
    }

    public void clear() {
        transitions.clear();
    }


}
