import java.util.*;

public class FirstSetCalculator {

    static Map<String, List<String>> grammar = new LinkedHashMap<>();
    static Map<String, Set<String>> firstSets = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter number of non-terminals:");
        int n = Integer.parseInt(scanner.nextLine());

        System.out.println("Enter productions (e.g., E -> TE' or E' -> +TE' | e):");
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

        // Compute FIRST sets for all non-terminals
        for (String nonTerminal : grammar.keySet()) {
            computeFirst(nonTerminal);
        }

        // Print FIRST sets
        System.out.println("\nFIRST sets:");
        for (String nt : grammar.keySet()) {
            Set<String> first = firstSets.get(nt);
            System.out.println("FIRST(" + nt + ") = { " + String.join(", ", first) + " }");
        }

        scanner.close();
    }

    static Set<String> computeFirst(String symbol) {
        if (firstSets.containsKey(symbol)) {
            return firstSets.get(symbol);
        }

        Set<String> first = new LinkedHashSet<>();

        // If terminal, FIRST is the symbol itself
        if (!grammar.containsKey(symbol)) {
            first.add(symbol);
            return first;
        }

        List<String> productions = grammar.get(symbol);

        for (String production : productions) {
            if (production.equals("e")) {
                first.add("e");
                continue;
            }

            boolean allNullable = true;
            String[] symbols = production.split("");

            for (String sym : symbols) {
                Set<String> symFirst = computeFirst(sym);

                // Add all symbols except e
                for (String s : symFirst) {
                    if (!s.equals("e")) {
                        first.add(s);
                    }
                }

                if (!symFirst.contains("e")) {
                    allNullable = false;
                    break;
                }
            }

            if (allNullable) {
                first.add("e");
            }
        }

        firstSets.put(symbol, first);
        return first;
    }
}


Enter number of non-terminals:
5
Enter productions (e.g., E -> TE' or E' -> +TE' | e):
E -> TE'
E' -> +TE' | e
T -> FT'
T' -> *FT' | e
F -> (E) | id

FIRST sets:
FIRST(E) = { (, i }
FIRST(E') = { +, e }
FIRST(T) = { (, i }
FIRST(T') = { *, e }
FIRST(F) = { (, i }
