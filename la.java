import java.util.*;
import java.util.regex.*;
import java.util.Scanner;

public class LexicalAnalyzer {

    // Define token categories
    private static final Set<String> keywords = new HashSet<>(Arrays.asList("int", "float", "char", "if", "else", "while", "for", "return", "void"));
    private static final Set<String> operators = new HashSet<>(Arrays.asList("+", "-", "*", "/", "=", "==", "!=", "<", ">", "<=", ">=", "&&", "||"));
    private static final Set<String> separators = new HashSet<>(Arrays.asList("(", ")", "{", "}", ";", ","));

    // Classify each token
    public static String classifyToken(String token) {
        if (keywords.contains(token)) {
            return "Keyword";
        } else if (operators.contains(token)) {
            return "Operator";
        } else if (separators.contains(token)) {
            return "Separator";
        } else if (token.matches("\\d+(\\.\\d+)?")) {
            return "Literal";
        } else if (token.matches("[a-zA-Z_]\\w*")) {
            return "Identifier";
        } else {
            return "Unknown";
        }
    }

    // Lexical Analyzer function
    public static void lexicalAnalyzer(String code) {
        // Improved token pattern: float, int, words, multi-char ops, single-char ops/seps
        Pattern pattern = Pattern.compile("\\d+\\.\\d+|\\d+|==|!=|<=|>=|[a-zA-Z_]\\w*|[^\s]");
        Matcher matcher = pattern.matcher(code);

        // Set to store token types
        Map<String, Set<String>> tokenTypes = new HashMap<>();
        tokenTypes.put("Keyword", new TreeSet<>());
        tokenTypes.put("Identifier", new TreeSet<>());
        tokenTypes.put("Operator", new TreeSet<>());
        tokenTypes.put("Literal", new TreeSet<>());
        tokenTypes.put("Separator", new TreeSet<>());
        tokenTypes.put("Unknown", new TreeSet<>());

        // Process each token
        while (matcher.find()) {
            String token = matcher.group();
            String category = classifyToken(token);
            tokenTypes.get(category).add(token);
        }

        // Print grouped results
        System.out.println("=== Lexical Analyzer Output (Grouped by Type) ===\n");
        for (String category : new String[]{"Keyword", "Identifier", "Operator", "Literal", "Separator", "Unknown"}) {
            Set<String> tokens = tokenTypes.get(category);
            if (!tokens.isEmpty()) {
                System.out.println(category + "s: " + String.join(", ", tokens));
            }
        }
    }

    public static void main(String[] args) {
        // Take input from the user
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the code to analyze (enter 'exit' to end input):");

        StringBuilder code = new StringBuilder();
        String line;
        
        // Reading the input until the user types "exit"
        while (!(line = scanner.nextLine()).equals("exit")) {
            code.append(line).append("\n");
        }

        // Perform lexical analysis on the entered code
        lexicalAnalyzer(code.toString());

        scanner.close();
    }
}


Enter the code to analyze (enter 'exit' to end input):
int a = 5;
float b = 3.14;
if (a > b) {
    a = a + b;
}
exit
