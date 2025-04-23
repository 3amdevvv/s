import java.util.*;

public class ThreeAddressCode {

    static boolean isOperator(String c) {
        return c.equals("+") || c.equals("-") || c.equals("*") || c.equals("/");
    }

    static int precedence(String op) {
        switch (op) {
            case "+": case "-":
                return 1;
            case "*": case "/":
                return 2;
            default:
                return 0;
        }
    }

    static List<String> infixToPostfix(String expression) {
        Stack<String> stack = new Stack<>();
        List<String> postfix = new ArrayList<>();
        String[] tokens = expression.split("\\s+");

        for (String token : tokens) {
            if (token.matches("[a-zA-Z0-9]+")) { // Operand
                postfix.add(token);
            } else if (isOperator(token)) {
                while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(token)) {
                    postfix.add(stack.pop());
                }
                stack.push(token);
            } else if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    postfix.add(stack.pop());
                }
                stack.pop(); // Pop '('
            }
        }

        while (!stack.isEmpty()) {
            postfix.add(stack.pop());
        }

        return postfix;
    }

    static List<String> generateTAC(String expression) {
        String[] parts = expression.split("=");
        String lhs = parts[0].trim();
        String rhs = parts[1].trim();

        List<String> postfix = infixToPostfix(rhs);
        Stack<String> stack = new Stack<>();
        List<String> tac = new ArrayList<>();
        int tempCount = 1;

        for (String token : postfix) {
            if (token.matches("[a-zA-Z0-9]+")) {
                stack.push(token);
            } else if (isOperator(token)) {
                String op2 = stack.pop();
                String op1 = stack.pop();
                String temp = "t" + tempCount++;
                tac.add(temp + " = " + op1 + " " + token + " " + op2);
                stack.push(temp);
            }
        }

        String finalResult = stack.pop();
        tac.add(lhs + " = " + finalResult);

        return tac;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter an expression (e.g., a = b + c * d):");
        String expr = scanner.nextLine();

        List<String> tacCode = generateTAC(expr);

        System.out.println("\nThree Address Code:");
        for (String line : tacCode) {
            System.out.println(line);
        }
    }
}
