import java.util.*;

public class CodeOptimizer {

    // Constant folding function
    public static List<String> constantFolding(List<String> code) {
        List<String> optimized = new ArrayList<>();
        Map<String, String> replacements = new HashMap<>();

        for (String line : code) {
            String[] parts = line.split("=");
            if (parts.length != 2) {
                optimized.add(line);
                continue;
            }

            String lhs = parts[0].trim();
            String rhs = parts[1].trim();

            try {
                int result = evaluateExpression(rhs);
                replacements.put(lhs, String.valueOf(result));
                optimized.add(lhs + " = " + result);
            } catch (Exception e) {
                // Replace known constants in the RHS
                for (Map.Entry<String, String> entry : replacements.entrySet()) {
                    rhs = rhs.replace(entry.getKey(), entry.getValue());
                }
                optimized.add(lhs + " = " + rhs);
            }
        }

        return optimized;
    }

    // Common Subexpression Elimination (CSE)
    public static List<String> commonSubexpressionElimination(List<String> code) {
        Map<String, String> expressions = new HashMap<>();
        List<String> optimized = new ArrayList<>();

        for (String line : code) {
            if (!line.contains("=")) {
                optimized.add(line);
                continue;
            }

            String[] parts = line.split("=");
            String lhs = parts[0].trim();
            String rhs = parts[1].trim();

            // Skip replacement if RHS is a constant value
            try {
                evaluateExpression(rhs);
                // It's a constant, don't replace with a variable
                optimized.add(lhs + " = " + rhs);
                continue;
            } catch (Exception e) {
            }

            if (expressions.containsKey(rhs)) {
                optimized.add(lhs + " = " + expressions.get(rhs));
            } else {
                expressions.put(rhs, lhs);
                optimized.add(lhs + " = " + rhs);
            }
        }

        return optimized;
    }

    // Evaluate an arithmetic expression and return the result
    public static int evaluateExpression(String expression) throws Exception {
        String[] tokens = expression.split("[+\\-*/]");
        int result = 0;
        String[] operators = expression.split("[0-9]+");
        int index = 0;

        // Perform basic evaluation (handling +, -, *, /)
        for (String token : tokens) {
            if (!token.isEmpty()) {
                int value = Integer.parseInt(token.trim());
                if (index > 0) {
                    String operator = operators[index].trim();
                    if (operator.equals("+")) {
                        result += value;
                    } else if (operator.equals("-")) {
                        result -= value;
                    } else if (operator.equals("*")) {
                        result *= value;
                    } else if (operator.equals("/")) {
                        result /= value;
                    }
                } else {
                    result = value;
                }
                index++;
            }
        }

        return result;
    }

    // Main function to optimize code
    public static void optimizeCode(List<String> code) {
        System.out.println("\nStep 1: Constant Folding");
        code = constantFolding(code);
        for (String line : code) {
            System.out.println(line);
        }

        System.out.println("\nStep 2: Common Subexpression Elimination");
        code = commonSubexpressionElimination(code);
        for (String line : code) {
            System.out.println(line);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<String> code = new ArrayList<>();

        // Take input from the user
        System.out.println("Enter the number of code lines:");
        int n = Integer.parseInt(sc.nextLine());

        System.out.println("Enter code lines:");
        for (int i = 0; i < n; i++) {
            code.add(sc.nextLine());
        }

        System.out.println("\nOriginal Code:");
        for (String line : code) {
            System.out.println(line);
        }

        // Perform optimization
        optimizeCode(code);

        sc.close();
    }
}



Enter the number of code lines:
7
Enter code lines:
t1 = 4 + 5
t2 = a + b
t3 = 4 + 5
t4 = t1 + t2
t5 = t3 + t2
t6 = t1 + t2
t7 = 2 * 3
