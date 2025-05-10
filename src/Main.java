import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class Main {
    private static FSM fsm;
    private static PrintWriter logWriter;
    private static final String VERSION = "1.0";

    public static void main(String[] args) {
        fsm = new FSM();

        if (args.length > 0) {
            executeFile(args[0]);
        } else {
            System.out.println("FSM DESIGNER " + VERSION + " " +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMM d, yyyy, HH:mm")));

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("? ");
                String command = readCommand(scanner);
                if (command.isEmpty()) continue;

                String response = processCommand(command);
                if (response != null) {
                    System.out.println(response);
                    logResponse(response);
                }
            }
        }
    }

    private static String readCommand(Scanner scanner) {
        StringBuilder command = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.startsWith(";")) continue;

            int commentIndex = line.indexOf(';');
            if (commentIndex >= 0) {
                command.append(line.substring(0, commentIndex));
                return command.toString().trim();
            }

            command.append(line).append(" ");
            if (line.endsWith(";")) {
                return command.substring(0, command.length() - 2).trim();
            }
        }
        return "";
    }


    private static String processCommand(String command) {
        String[] splittedCommand = command.trim().split("\\s+");
        if (splittedCommand.length == 0) return null;

        String cmd = splittedCommand[0].toUpperCase();
        String[] args = Arrays.copyOfRange(splittedCommand, 1, splittedCommand.length);
        try {
            switch (cmd) {
                case "EXIT":
                    closeLog();
                    System.out.println("TERMINATED BY USER");
                    System.exit(0);
                    return null;
                case "LOG":
                    return handleLog(args);
                case "SYMBOLS":
                    return handleSymbols(args);
                case "STATES":
                    return handleStates(args);
                case "INITIAL-STATE":
                    return handleInitialState(args);
                case "FINAL-STATES":
                    return handleFinalStates(args);
                case "TRANSITIONS":
                    return handleTransitions(args);
                case "PRINT":
                    return handlePrint(args);
                case "COMPILE":
                    return handleCompile(args);
                case "CLEAR":
                    return handleClear();
                case "LOAD":
                    return handleLoad(args);
                case "EXECUTE":
                    return handleExecute(args);
                default:
                    return "Error: Unknown command " + cmd;
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private static String handleLog(String[] args) {
        if (args.length == 0) {
            if (logWriter != null) {
                closeLog();
                return "STOPPED LOGGING";
            }
            return "LOGGING was not enabled";
        }

        try {
            closeLog();
            logWriter = new PrintWriter(new FileWriter(args[0]));
            return "Logging started to " + args[0];
        } catch (IOException e) {
            return "Error creating log file: " + e.getMessage();
        }
    }

    private static void closeLog() {
        if (logWriter != null) {
            logWriter.close();
            logWriter = null;
        }
    }

    private static void logResponse(String response) {
        if (logWriter != null) {
            logWriter.println(response);
            logWriter.flush();
        }
    }

    private static String handleSymbols(String[] args) {
        if (args.length == 0) {
            return "SYMBOLS " + fsm.getSymbols();
        }

        StringBuilder response = new StringBuilder();
        for (String symbol : args) {
            if (symbol.length() != 1) {
                response.append("Warning: invalid symbol ").append(symbol).append("\n");
                continue;
            }
            char c = symbol.charAt(0);
            if (!fsm.isValidSymbol(c)) {
                response.append("Warning: invalid symbol ").append(c).append("\n");
                continue;
            }
            if (!fsm.addSymbol(c)) {
                response.append("Warning: ").append(c).append(" was already declared as a symbol\n");
            }
        }
        return !response.isEmpty() ? response.toString().trim() : "Symbols added successfully";
    }

    private static String handleStates(String[] args) {
        if (args.length == 0) {
            return "Current states: " + fsm.getStates();
        }

        StringBuilder response = new StringBuilder();
        for (String state : args) {
            if (!fsm.addState(state)) {
                response.append("Warning: invalid state ").append(state).append("\n");
            }
        }
        return !response.isEmpty() ? response.toString().trim() : "States added successfully";
    }

    private static String handleInitialState(String[] args) {
        if (args.length != 1) {
            return "Error: INITIAL-STATE command requires exactly one state";
        }
        String state = args[0];
        if (fsm.setInitialState(state)) {
            if (!fsm.getStates().contains(state.toLowerCase())) {
                return "Warning: " + state + " was not previously declared as a state";
            }
            return "Initial state set to " + state;
        }
        return "Error: Invalid state " + state;
    }

    private static String handleFinalStates(String[] args) {
        if (args.length == 0) {
            return "Error: FINAL-STATES command requires at least one state";
        }

        StringBuilder response = new StringBuilder();
        for (String state : args) {
            if (fsm.addFinalState(state)) {
                if (!fsm.getStates().contains(state.toLowerCase())) {
                    response.append("Warning: ").append(state)
                            .append(" was not previously declared as a state\n");
                }
            } else {
                response.append("Warning: invalid state ").append(state).append("\n");
            }
        }
        return !response.isEmpty() ? response.toString().trim() : "Final states added successfully";
    }

    private static String handleTransitions(String[] args) {
        if (args.length == 0) {
            return "Error: TRANSITIONS command requires at least one transition";
        }

        String transitionsStr = String.join(" ", args);
        String[] transitions = transitionsStr.split(",");

        StringBuilder response = new StringBuilder();
        for (String transition : transitions) {
            String[] elements = transition.trim().split("\\s+");
            if (elements.length != 3) {
                response.append("Error: invalid transition format ").append(transition).append("\n");
                continue;
            }

            String symbol = elements[0];
            String currentState = elements[1];
            String nextState = elements[2];

            if (symbol.length() != 1) {
                response.append("Error: invalid symbol ").append(symbol).append("\n");
                continue;
            }

            if (!fsm.addTransition(symbol.charAt(0), currentState, nextState)) {
                response.append("Error: invalid transition <").append(symbol).append(",")
                        .append(currentState).append(",").append(nextState).append(">\n");
            }
        }
        return !response.isEmpty() ? response.toString().trim() : "Transitions added successfully";
    }

    private static String handlePrint(String[] args) {
        if (args.length == 0) {
            return printFSM();
        }
        if (args.length != 1) {
            return "Error: PRINT command requires zero or one argument";
        }
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(args[0]));
            writer.println(printFSM());
            writer.close();
            return "FSM printed to file " + args[0];
        } catch (IOException e) {
            return "Error writing to file: " + e.getMessage();
        }
    }

    private static String printFSM() {
        return "SYMBOLS " + fsm.getSymbols() + "\n" +
                "STATES " + fsm.getStates() + "\n" +
                "INITIAL STATE " + fsm.getInitialState() + "\n" +
                "FINAL STATES " + fsm.getFinalStates();
    }

    private static String handleCompile(String[] args) {
        if (args.length != 1) {
            return "Error: COMPILE command requires exactly one argument";
        }
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(args[0]));
            out.writeObject(fsm);
            out.close();
            return "Compile successful";
        } catch (IOException e) {
            return "Error compiling FSM: " + e.getMessage();
        }
    }

    private static String handleClear() {
        fsm.clear();
        return "FSM cleared";
    }

    private static String handleLoad(String[] args) {
        if (args.length != 1) {
            return "Error: LOAD command requires exactly one argument";
        }
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(args[0]));
            fsm = (FSM) in.readObject();
            in.close();
            return "FSM loaded from " + args[0];
        } catch (IOException | ClassNotFoundException e) {
            return "Error loading FSM: " + e.getMessage();
        }
    }

    private static String handleExecute(String[] args) {
        if (args.length != 1) {
            return "Error: EXECUTE command requires exactly one argument";
        }
        try {
            return fsm.executeInput(args[0]);
        } catch (IllegalArgumentException e) {
            return "Error: " + e.getMessage();
        }
    }

    private static void executeFile(String filename) {
        try {
            Scanner fileScanner = new Scanner(new File(filename));
            while (fileScanner.hasNextLine()) {
                String command = readCommand(fileScanner);
                if (!command.isEmpty()) {
                    String response = processCommand(command);
                    if (response != null) {
                        System.out.println(response);
                    }
                }
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Error: Cannot read file " + filename);
            System.exit(1);
        }
    }
}