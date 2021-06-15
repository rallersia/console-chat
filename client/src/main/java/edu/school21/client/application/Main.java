package edu.school21.client.application;

import edu.school21.client.client.Client;
import org.apache.commons.cli.*;

import java.util.Scanner;

public class Main {
    public static int parsePort(String[] args) {
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();

        options.addOption( OptionBuilder.withLongOpt( "server-port" )
                .withDescription( "port to connect" )
                .hasArg()
                .withArgName("VALUE")
                .create() );
        try {
            CommandLine line = parser.parse( options, args );
            if( line.hasOption( "server-port" ) ) {
                return (Integer.parseInt(line.getOptionValue( "server-port" )));
            } else {
                HelpFormatter helpFormatter = new HelpFormatter();
                helpFormatter.printHelp("messenger client", options);
                System.exit(0);
            }
        }
        catch( ParseException exp ) {
            System.out.println( "Unexpected exception:" + exp.getMessage() );
            System.exit(0);
        }
        return (0);
    }
    public static boolean signUp(Scanner in, Client client) {
        client.sendMessage("signUp");
        System.out.println("Enter username:");
        String username = in.nextLine();
        System.out.println("Enter password:");
        String password = in.nextLine();
        client.sendMessage(username);
        client.sendMessage(password);
        String answer = client.getMessage();
        if (answer.equals("SUCCESS")) {
            System.out.println("Successful!");
            return true;
        } else {
            System.out.println("Can't sign up! Please, try again");
            return false;
        }
    }
    public static boolean signIn(Scanner in, Client client) {
        client.sendMessage("signIn");
        System.out.println("Enter username:");
        String username = in.nextLine();
        System.out.println("Enter password:");
        String password = in.nextLine();
        client.sendMessage(username);
        client.sendMessage(password);
        String answer = client.getMessage();
        if (answer.equals("SUCCESS")) {
            System.out.println("Successful!");
            return true;
        } else {
            System.out.println("Can't sign in! Please, try again");
            return false;
        }
    }
    public static void main(String[] args) {
        int port = parsePort(args);
        Client client = new Client();
        client.startConnection("127.0.0.1", port);
        System.out.println("Hello from Server!");
        Scanner in = new Scanner(System.in);
        String command = in.nextLine();
        while (!command.equals("exit")) {
            if (command.equals("signUp")) {
                while (!signUp(in, client));
                break;
            } else if (command.equals("signIn")) {
                while (!signIn(in, client));
                break;
            }
            System.out.println("Unknown command!");
            command = in.nextLine();
        }
        if (command.equals("exit")) {
            System.exit(0);
        }
        Runnable gettingMessage =
                new Runnable(){
                    public void run(){
                        while (client.isAvailable()) {
                            String message = client.getMessage();
                            if (message == null)
                                break;
                            System.out.println(message);
                        }
                    }
                };
        Thread messageGetter = new Thread(gettingMessage);
        messageGetter.start();
        command = in.nextLine();
        while (!command.equals("exit")) {
            client.sendMessage(command);
            command = in.nextLine();
        }
        client.stopConnection();
    }
}
