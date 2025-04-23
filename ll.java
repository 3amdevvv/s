import java.util.*;

public class LL1Parser {
    static Map<String, List<String>> grammar = new HashMap<>();
    static Map<String, Set<String>> firstSets = new HashMap<>();
    static Map<String, Set<String>> followSets = new HashMap<>();
    static Set<String> terminals = new HashSet<>(Arrays.asList("id", "+", "*", "(", ")", "$"));
    static List<String> nonTerminals = new ArrayList<>();
    static Map<String, Map<String, String>> parsingTable = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input Grammar
        System.out.println("Enter number of grammar rules:");
        int ruleCount = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter grammar rules (Format: A -> production1 | production2):");

        for (int i = 0; i < ruleCount; i++) {
            String[] parts = scanner.nextLine().split("->");
            String head = parts[0].trim();
            String[] productions = parts[1].trim().split("\\|");

            grammar.put(head, new ArrayList<>());
            for (String prod : productions) {
                grammar.get(head).add(prod.trim());
            }

            if (!nonTerminals.contains(head)) nonTerminals.add(head);
        }

        // Input FIRST sets
        System.out.println("\nEnter FIRST sets (Format: A -> a,b,c):");
        for (String nt : nonTerminals) {
            System.out.print("FIRST(" + nt + "): ");
            String[] firsts = scanner.nextLine().split(",");
            firstSets.put(nt, new HashSet<>(Arrays.asList(firsts)));
        }

        // Input FOLLOW sets
        System.out.println("\nEnter FOLLOW sets (Format: A -> a,b,c):");
        for (String nt : nonTerminals) {
            System.out.print("FOLLOW(" + nt + "): ");
            String[] follows = scanner.nextLine().split(",");
            followSets.put(nt, new HashSet<>(Arrays.asList(follows)));
        }

        // Initialize Parsing Table
        for (String nt : nonTerminals) {
            parsingTable.put(nt, new HashMap<>());
            for (String t : terminals) {
                parsingTable.get(nt).put(t, "");
            }
        }

        // Fill Parsing Table
        for (String head : grammar.keySet()) {
            for (String production : grammar.get(head)) {
                Set<String> first = getFirstOfString(production);

                for (String terminal : first) {
                    if (!terminal.equals("e")) {
                        parsingTable.get(head).put(terminal, production);
                    }
                }

                if (first.contains("e")) {
                    for (String followSymbol : followSets.get(head)) {
                        parsingTable.get(head).put(followSymbol, "e");
                    }
                }
            }
        }

        // Display Parsing Table
        System.out.println("\nLL(1) Parsing Table:");
        System.out.printf("%-6s", "");
        List<String> sortedTerminals = new ArrayList<>(terminals);
        Collections.sort(sortedTerminals);
        for (String terminal : sortedTerminals) {
            System.out.printf("%-12s", terminal);
        }
        System.out.println();

        for (String nt : nonTerminals) {
            System.out.printf("%-6s", nt);
            for (String terminal : sortedTerminals) {
                String prod = parsingTable.get(nt).get(terminal);
                System.out.printf("%-12s", prod == null ? "" : prod);
            }
            System.out.println();
        }

        scanner.close();
    }

    private static Set<String> getFirstOfString(String symbols) {
        Set<String> result = new HashSet<>();
        String[] tokens = symbols.trim().split("\\s+");

        for (String symbol : tokens) {
            if (!firstSets.containsKey(symbol)) {
                result.add(symbol); // terminal
                break;
            }
            result.addAll(firstSets.get(symbol));
            if (!firstSets.get(symbol).contains("e")) break;
        }

        boolean allEpsilon = Arrays.stream(tokens)
            .allMatch(sym -> firstSets.containsKey(sym) && firstSets.get(sym).contains("e"));
        if (allEpsilon) result.add("e");

        return result;
    }
}


Enter number of grammar rules:
5
Enter grammar rules (Format: A -> production1 | production2):
E -> T E'
E' -> + T E' | e
T -> F T'
T' -> * F T' | e
F -> ( E ) | id

Enter FIRST sets (Format: A -> a,b,c):
FIRST(E): (,id
FIRST(E'): +,e
FIRST(T): (,id
FIRST(T'): *,e
FIRST(F): (,id

Enter FOLLOW sets (Format: A -> a,b,c):
FOLLOW(E): ),$
FOLLOW(E'): ),$
FOLLOW(T): +,),$
FOLLOW(T'): +,),$
FOLLOW(F): *,+,),$

