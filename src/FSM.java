import java.io.Serializable;
import java.util.*;

public class FSM implements Serializable {
    private Set<Character> symbols;
    private Set<String> states;
    private String initialState;
    private Set<String> finalStates;
    private TransitionContainer transitionContainer = new TransitionContainer();

    public FSM() {
        symbols = new HashSet<>();
        states = new HashSet<>();
        finalStates = new HashSet<>();
    }

    public boolean addSymbol(char symbol) {
        if (!Character.isLetterOrDigit(symbol)) {
            return false;
        }
        return symbols.add(Character.toLowerCase(symbol));
    }

    public boolean addState(String state) {
        if (!isValidState(state)) {
            return false;
        }
        return states.add(state.toLowerCase());
    }

    public boolean setInitialState(String state) {
        if (!isValidState(state)) {
            return false;
        }
        String lowerState = state.toLowerCase();
        if (!states.contains(lowerState)) {
            states.add(lowerState);
        }
        initialState = lowerState;
        return true;
    }

    public boolean addFinalState(String state) {
        if (!isValidState(state)) {
            return false;
        }
        String lowerState = state.toLowerCase();
        if (!states.contains(lowerState)) {
            states.add(lowerState);
        }
        return finalStates.add(lowerState);
    }

    public boolean addTransition(char symbol, String currentState, String nextState) {
        if (!isValidSymbol(symbol) || !isValidState(currentState) || !isValidState(nextState)) {
            return false;
        }
        
        symbol = Character.toLowerCase(symbol);
        currentState = currentState.toLowerCase();
        nextState = nextState.toLowerCase();
        
        if (!symbols.contains(symbol)) return false;
        if (!states.contains(currentState)) return false;
        if (!states.contains(nextState)) return false;

        transitionContainer.addTransition(new Transition(symbol, currentState,nextState));
        return true;
    }

    public void clear() {
        symbols.clear();
        states.clear();
        finalStates.clear();
        transitionContainer.clear();
        initialState = null;
    }

    public boolean isValidSymbol(char symbol) {
        return Character.isLetterOrDigit(symbol);
    }

    public boolean isValidState(String state) {
        if (state == null || state.isEmpty()) return false;
        return state.chars().allMatch(Character::isLetterOrDigit);
    }

    public Set<Character> getSymbols() {
        return Collections.unmodifiableSet(symbols);
    }

    public Set<String> getStates() {
        return Collections.unmodifiableSet(states);
    }

    public String getInitialState() {
        return initialState;
    }

    public Set<String> getFinalStates() {
        return Collections.unmodifiableSet(finalStates);
    }

    public String executeInput(String input) {
        if (initialState == null) return "NO";
        
        StringBuilder stateSequence = new StringBuilder();
        String currentState = initialState;
        stateSequence.append(currentState);

        for (char c : input.toCharArray()) {
            c = Character.toLowerCase(c);
            if (!symbols.contains(c)) {
                throw new IllegalArgumentException("Invalid symbol: " + c);
            }

            Transition transition = transitionContainer.getTransitionWithSymbolAndCurrentState(c, currentState);
            if (transition == null) {
                return stateSequence.toString() + " NO";
            }
            stateSequence.append(" ").append(transition.getNextState());
            currentState = transition.getNextState();
        }

        return stateSequence + (finalStates.contains(currentState) ? " YES" : " NO");
    }
}