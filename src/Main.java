public class Main {
    public static void main(String[] args) {
        FSM fsm = new FSM();
        
        // Add symbols
        fsm.addSymbol('0');
        fsm.addSymbol('1');
        
        // Add states
        fsm.addState("q0");
        fsm.addState("q1");
        fsm.addState("q2");
        
        // Set initial and final states
        fsm.setInitialState("q0");
        fsm.addFinalState("q2");
        
        // Add transitions
        fsm.addTransition('0', "q0", "q1");
        fsm.addTransition('1', "q1", "q2");
        
        // Test the FSM
        try {
            System.out.println(fsm.executeInput("01")); // Should output: q0 q1 q2 YES
            System.out.println(fsm.executeInput("00")); // Should output: q0 q1 NO
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}