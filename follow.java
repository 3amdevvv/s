import java.util.*;

public class FollowSetCalculator {

    static Map<String, List<List<String>>> grammar = new HashMap<>();
    static Map<String, Set<String>> firstSets = new HashMap<>();
    static Map<String, Set<String>> followSets = new HashMap<>();
    static Set<String> nonTerminals = new HashSet<>();
    static String startSymbol;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input grammar
        System.out.print("Enter number of productions: ");
        int n = Integer.parseInt(sc.nextLine());
        System.out.println("Enter productions (e.g., S->A B or A->a | e):");

        for (int i = 0; i < n; i++) {
            String[] parts = sc.nextLine().split("->");
            String lhs = parts[0].trim();
            nonTerminals.add(lhs);
            String[] rhsAlternatives = parts[1].split("\\|");

            List<List<String>> productions = new ArrayList<>();
            for (String alt : rhsAlternatives) {
                List<String> symbols = new ArrayList<>(Arrays.asList(alt.trim().split("\\s+")));
                productions.add(symbols);
            }
            grammar.put(lhs, productions);
        }

        System.out.print("Enter the start symbol: ");
        startSymbol = sc.nextLine().trim();

        // Input FIRST sets
        System.out.print("Enter number of FIRST sets: ");
        int f = Integer.parseInt(sc.nextLine());
        System.out.println("Enter FIRST sets (e.g., A: a e):");

        for (int i = 0; i < f; i++) {
            String[] parts = sc.nextLine().split(":");
            String symbol = parts[0].trim();
            Set<String> first = new HashSet<>(Arrays.asList(parts[1].trim().split("\\s+")));
            firstSets.put(symbol, first);
        }

        computeFollow();

        System.out.println("\nFOLLOW sets:");
        for (String nonTerminal : followSets.keySet()) {
            System.out.println("FOLLOW(" + nonTerminal + ") = " + followSets.get(nonTerminal));
        }
    }

    static void computeFollow() {
        for (String nt : grammar.keySet()) {
            followSets.put(nt, new HashSet<>());
        }
        followSets.get(startSymbol).add("$");

        boolean updated = true;
        while (updated) {
            updated = false;

            for (String lhs : grammar.keySet()) {
                for (List<String> production : grammar.get(lhs)) {
                    for (int i = 0; i < production.size(); i++) {
                        String symbol = production.get(i);
                        if (nonTerminals.contains(symbol)) {

                            if (i + 1 < production.size()) {
                                List<String> beta = production.subList(i + 1, production.size());

                                Set<String> firstOfBeta = computeFirstOfSequence(beta);
                                for (String f : firstOfBeta) {
                                    if (!f.equals("e") && followSets.get(symbol).add(f)) {
                                        updated = true;
                                    }
                                }

                                if (firstOfBeta.contains("e")) {
                                    for (String f : followSets.get(lhs)) {
                                        if (followSets.get(symbol).add(f)) {
                                            updated = true;
                                        }
                                    }
                                }
                            } else {
                                for (String f : followSets.get(lhs)) {
                                    if (followSets.get(symbol).add(f)) {
                                        updated = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    static Set<String> computeFirstOfSequence(List<String> symbols) {
        Set<String> result = new HashSet<>();
        for (String symbol : symbols) {
            Set<String> currentFirst = firstSets.getOrDefault(symbol, new HashSet<>());
            result.addAll(currentFirst);
            result.remove("e");
            if (!currentFirst.contains("e")) {
                return result;
            }
        }
        result.add("e");
        return result;
    }
}




Enter number of productions: 5
Enter productions (e.g., S->A B or A->a | e):
E -> T E'
E' -> + T E' | e
T -> F T'
T' -> * F T' | e
F -> ( E ) | id
Enter the start symbol: E
Enter number of FIRST sets: 5
Enter FIRST sets (e.g., A: a e):
E: ( id
E': + e
T: ( id
T': * e
F: ( id

FOLLOW sets:
FOLLOW(E') = [$]
FOLLOW(T') = [$, +]
FOLLOW(T) = [$, +]
FOLLOW(E) = [$]
FOLLOW(F) = [$, *, +]
