package restapi;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class YandexDisk {
    private static String token = "<token>";
    public static String EXIT = "exit";
    public static String RM = "rm";
    public static String CP = "cp";
    public static String MV = "mv";
    public static String LS = "ls";

    public static void main(String[] args) throws Exception {
        while (true) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String line = reader.readLine().trim();

            if (line.equals(EXIT)) {
                return;
            }

            String[] words = line.split("\\s+");

            if (line.startsWith(CP)) {
                CommandCP commandCP = new CommandCP(token);
                if (words.length < 3) {
                    System.out.println(commandCP.HELP);
                }
                commandCP.execute(words[1], words[2]);
            } else if (line.startsWith(MV)) {
                CommandMV commandMV = new CommandMV(token);
                if (words.length < 3) {
                    System.out.println(commandMV.HELP);
                }
                commandMV.execute(words[1], words[2]);
            } else if (line.startsWith(LS)) {
                CommandLS commandLS = new CommandLS(token);
                if (words.length < 2) {
                    System.out.println(commandLS.HELP);
                }
                commandLS.execute(words[1]);
            } else if (line.startsWith(RM)) {
                CommandRM commandRM = new CommandRM(token);
                if (words.length < 2) {
                    System.out.println(commandRM.HELP);
                }
                commandRM.execute(words[1]);
            }
        }
    }
}
