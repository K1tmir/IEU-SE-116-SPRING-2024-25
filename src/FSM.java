import java.util.HashSet;
import java.util.Set;

public class FSM {
    private Set<Character> symbols;
    private Set<String> states;
    private String initialState;
    private Set<String> finalStates;

    public FSM() {
        symbols = new HashSet<>();
        states = new HashSet<>();
        finalStates = new HashSet<>();
    }

    public boolean addSymbol(char symbol) {
        if (!isValidSymbol(symbol)) {
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
        initialState = state.toLowerCase();
        return true;
    }

    public boolean isValidState(String state) {
        if (state == null || state.isEmpty()) return false;
        return state.chars().allMatch(Character::isLetterOrDigit);
    }

    public boolean isValidSymbol(char symbol) {
        return Character.isLetterOrDigit(symbol);
    }

    public Set<Character> getSymbols() {
        return new HashSet<>(symbols);
    }
}