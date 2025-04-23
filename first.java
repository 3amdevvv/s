import java.util.*;

public class FirstSetCalculator {

    static Map<String, List<String>> grammar = new LinkedHashMap<>();
    static Map<String, Set<String>> firstSets = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter number of non-terminals:");
        int n = Integer.parseInt(scanner.nextLine());

        System.out.println("Enter productions (e.g., E -> T E' or T' -> * F T' | e):");
        for (int i = 0; i < n; i++) {
            String input = scanner.nextLine();
            String[] parts = input.split("->");
            String lhs = parts[0].trim();
            String[] rhsProductions = parts[1].trim().split("\\|");

            List<String> productions = new ArrayList<>();
            for (String prod : rhsProductions) {
                productions.add(prod.trim());
            }

            grammar.put(lhs, productions);
        }

        // Compute FIRST sets
        for (String nonTerminal : grammar.keySet()) {
            computeFirst(nonTerminal);
        }

        // Print FIRST sets
        System.out.println("\nFIRST sets:");
        for (Map.Entry<String, Set<String>> entry : firstSets.entrySet()) {
            System.out.println("FIRST(" + entry.getKey() + ") = { " + String.join(", ", entry.getValue()) + " }");
        }

        scanner.close();
    }

    static Set<String> computeFirst(String symbol) {
        if (firstSets.containsKey(symbol)) {
            return firstSets.get(symbol);
        }

        Set<String> first = new LinkedHashSet<>();

        // Terminal symbol
        if (!Character.isUpperCase(symbol.charAt(0))) {
            first.add(symbol);
            return first;
        }

        List<String> productions = grammar.get(symbol);

        for (String production : productions) {
            // Handle epsilon written as 'e' or 'eps'
            if (production.equals("e") || production.equals("eps") || production.equals("ε")) {
                first.add("ε"); // Internally we use ε for uniform output
            } else {
                int i = 0;
                while (i < production.length()) {
                    String currentSymbol = String.valueOf(production.charAt(i));

                    Set<String> currentFirst = computeFirst(currentSymbol);

                    // Add all except ε
                    for (String s : currentFirst) {
                        if (!s.equals("ε")) {
                            first.add(s);
                        }
                    }

                    if (!currentFirst.contains("ε")) {
                        break;
                    }

                    i++;
                }

                // If ε is in all FIRST(X_i)
                if (i == production.length()) {
                    first.add("ε");
                }
            }
        }

        firstSets.put(symbol, first);
        return first;
    }
}


Enter number of non-terminals:
5
E -> T E'
E' -> + T E' | e
T -> F T'
T' -> * F T' | eps
F -> ( E ) | id
