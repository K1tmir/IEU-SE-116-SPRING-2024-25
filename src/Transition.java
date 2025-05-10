import java.io.Serializable;

// A transition is a triple <a,c,r> consisting of a symbol a, a current state c, and a resulting state r
// TRANSITIONS 0 Q0 q0, 0 Q1 Q1, 0 Q2 q2,
//             1 Q0 q1, 1 Q1 Q2, 1 Q2 q0,
//             2 Q0 q2, 2 Q1 Q0, 2 Q2 q1,
//             3 Q0 q0, 3 Q1 Q1, 3 Q2 q2;
public class Transition implements Serializable {
    private char symbol;
    private String currentState;
    private String nextState;

    public Transition(char symbol, String currentState, String nextState) {
        this.symbol = symbol;
        this.currentState = currentState;
        this.nextState = nextState;
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

    @Override
    public String toString() {
        return "Transition{" +
                "symbol=" + symbol +
                ", currentState='" + currentState + '\'' +
                ", nextState='" + nextState + '\'' +
                '}';
    }

}
