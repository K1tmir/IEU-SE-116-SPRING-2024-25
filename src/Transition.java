public class Transition {
    private char symbol;
    private String currentState;
    private String nextState;

    public Transition(char symbol, String currentState, String nextState) {
        this.symbol = Character.toLowerCase(symbol);
        this.currentState = currentState.toLowerCase();
        this.nextState = nextState.toLowerCase();
    }

    public char getSymbol() {
        return symbol;
    }

    public String getCurrentState() {
        return currentState;
    }

    public String getNextState() {
        return nextState;
    }
}