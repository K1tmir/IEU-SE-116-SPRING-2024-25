import java.util.ArrayList;
import java.util.List;

public class TransitionContainer {
    private List<Transition> transitions;

    public TransitionContainer() {
        transitions = new ArrayList<>();
    }

    public void addTransition(Transition transition) {
        transitions.add(transition);
    }

    public Transition getTransitionWithSymbolAndCurrentState(char symbol, String currentState) {
        symbol = Character.toLowerCase(symbol);
        currentState = currentState.toLowerCase();
        
        for (Transition transition : transitions) {
            if (transition.getSymbol() == symbol && 
                transition.getCurrentState().equals(currentState)) {
                return transition;
            }
        }
        return null;
    }

    public void clear() {
        transitions.clear();
    }
}