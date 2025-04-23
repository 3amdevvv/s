import java.util.*;
import java.util.regex.*;

public class MacroProcessor {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder sourceCode = new StringBuilder();

        System.out.println("Enter Assembly Code (type 'END' alone on a line to finish):");

        String line;
        while (!(line = scanner.nextLine()).equals("END")) {
            sourceCode.append(line).append("\n");
        }
        sourceCode.append("END"); // Add END marker for the code

        // Call the macro processor
        processMacros(sourceCode.toString());

        scanner.close();
    }

    public static void processMacros(String sourceCode) {
        Map<String, List<String>> macros = new HashMap<>();
        Map<String, Integer> mnt = new LinkedHashMap<>();
        List<String> mdt = new ArrayList<>();
        Map<String, List<String>> at = new HashMap<>();

        String currentMacroName = null;
        List<String> currentParams = new ArrayList<>();
        List<String> currentBody = new ArrayList<>();

        String[] lines = sourceCode.split("\\r?\\n");

        for (String line : lines) {
            line = line.trim();

            Matcher macroDef = Pattern.compile("MACRO\\s+(\\w+)\\s*(.*)").matcher(line);
            if (macroDef.matches()) {
                if (currentMacroName != null) {
                    macros.put(currentMacroName, new ArrayList<>(currentBody));
                    mnt.put(currentMacroName, mdt.size());
                    mdt.addAll(currentBody);
                }
                currentMacroName = macroDef.group(1);
                currentParams = new ArrayList<>();
                if (!macroDef.group(2).isEmpty()) {
                    currentParams = Arrays.asList(macroDef.group(2).split("\\s*,\\s*"));
                }
                currentBody = new ArrayList<>();
                continue;
            }

            if (line.equals("MEND")) {
                if (currentMacroName != null) {
                    macros.put(currentMacroName, new ArrayList<>(currentBody));
                    mnt.put(currentMacroName, mdt.size());
                    mdt.addAll(currentBody);
                    currentMacroName = null;
                    currentParams = new ArrayList<>();
                    currentBody = new ArrayList<>();
                }
                continue;
            }

            if (currentMacroName != null) {
                currentBody.add(line);
                continue;
            }

            Matcher macroCall = Pattern.compile("(\\w+)\\s*(.*)").matcher(line);
            if (macroCall.matches()) {
                String macroName = macroCall.group(1);
                if (macros.containsKey(macroName)) {
                    List<String> args = new ArrayList<>();
                    if (!macroCall.group(2).isEmpty()) {
                        args = Arrays.asList(macroCall.group(2).split("\\s*,\\s*"));
                    }
                    at.put(macroName, args);
                    mdt.add("CALL " + macroName);
                }
            }
        }

        // Print MDT
        System.out.println("\n=== Macro Definition Table (MDT) ===");
        for (String entry : mdt) {
            System.out.println(entry);
        }

        // Print MNT
        System.out.println("\n=== Macro Name Table (MNT) ===");
        for (Map.Entry<String, Integer> entry : mnt.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        // Print AT
        System.out.println("\n=== Argument Table (AT) ===");
        for (Map.Entry<String, List<String>> entry : at.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}


Enter Assembly Code (type 'END' alone on a line to finish):
MACRO ADD X, Y
    LOAD X
    ADD Y
MEND
MACRO SUB A, B
    LOAD A
    SUB B
MEND
START
    ADD 5, 3
    SUB 7, 4
END
