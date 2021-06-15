package edu.school21.server.app;

import edu.school21.server.config.SocketsApplicationConfig;
import edu.school21.server.server.Server;
import org.apache.commons.cli.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static int parsePort(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SocketsApplicationConfig.class);
        CommandLineParser parser = context.getBean(CommandLineParser.class);
        Options options = context.getBean(Options.class);

        try {
            CommandLine line = parser.parse( options, args );
            if( line.hasOption( "port" ) ) {
                return (Integer.parseInt(line.getOptionValue( "port" )));
            } else {
                HelpFormatter helpFormatter = context.getBean(HelpFormatter.class);
                helpFormatter.printHelp("messenger server", options);
                System.exit(0);
            }
        }
        catch( ParseException exp ) {
            System.out.println( "Unexpected exception:" + exp.getMessage() );
            System.exit(0);
        }
        return (0);
    }
    public static void startServer(int port) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SocketsApplicationConfig.class);
        Server server = context.getBean(Server.class);
        server.start(port);
    }
    public static void main(String[] args) {
        int port = parsePort(args);
        startServer(port);
    }
}
