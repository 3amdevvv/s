import java.util.*;

public class RemoveLeftRecursion {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Map<String, List<String>> grammar = new LinkedHashMap<>();

        System.out.print("Enter number of non-terminals: ");
        int n = Integer.parseInt(sc.nextLine());

        System.out.println("Enter productions (e.g., A -> Aa | b):");
        for (int i = 0; i < n; i++) {
            System.out.print("Production " + (i + 1) + ": ");
            String[] parts = sc.nextLine().split("->");
            String lhs = parts[0].trim();
            String[] rhs = parts[1].trim().split("\\|");

            List<String> productions = new ArrayList<>();
            for (String prod : rhs) {
                productions.add(prod.trim());
            }

            grammar.put(lhs, productions);
        }

        Map<String, List<String>> updatedGrammar = removeLeftRecursion(grammar);

        System.out.println("\nModified Grammar (without left recursion):");
        for (String nt : updatedGrammar.keySet()) {
            System.out.println(nt + " -> " + String.join(" | ", updatedGrammar.get(nt)));
        }

        sc.close();
    }

    public static Map<String, List<String>> removeLeftRecursion(Map<String, List<String>> grammar) {
        Map<String, List<String>> newGrammar = new LinkedHashMap<>();

        for (String nonTerminal : grammar.keySet()) {
            List<String> alpha = new ArrayList<>();
            List<String> beta = new ArrayList<>();

            for (String prod : grammar.get(nonTerminal)) {
                if (prod.startsWith(nonTerminal)) {
                    alpha.add(prod.substring(nonTerminal.length()).trim());
                } else {
                    beta.add(prod);
                }
            }

            if (!alpha.isEmpty()) {
                String newNT = nonTerminal + "'";

                List<String> betaProductions = new ArrayList<>();
                for (String b : beta) {
                    betaProductions.add(b + newNT);
                }
                newGrammar.put(nonTerminal, betaProductions);

                List<String> alphaProductions = new ArrayList<>();
                for (String a : alpha) {
                    alphaProductions.add(a + newNT);
                }
                alphaProductions.add("e");  // Using 'e' as epsilon
                newGrammar.put(newNT, alphaProductions);
            } else {
                newGrammar.put(nonTerminal, grammar.get(nonTerminal));
            }
        }

        return newGrammar;
    }
}



Enter number of non-terminals: 2
Enter productions (e.g., A -> Aa | b):
Production 1: A -> Aa | b
Production 2: B -> Bc | Bd | e

Modified Grammar (without left recursion):
A -> bA'
A' -> aA' | e
B -> eB'
B' -> cB' | dB' | e
