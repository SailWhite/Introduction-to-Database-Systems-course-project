package ksp;

import java.util.regex.*;
import java.lang.*;
import java.io.*;

public class ServerCore {
	public static void main(String[] args)  throws Exception{
		System.out.println("               /\n"+
                           "              //\n"+     
                           "             / /   |  / |---  |--\\  |--\\    /\\   |\n"+
                           "            / /    | /  |     |   | |   |  /  \\  |\n"+
                           "          /    /   |/   |     |--/  |__/  /    \\ |\n"+
                           "         /    /    |\\   |---  |\\    |  \\  |----| |\n"+
                           "        /    /     | \\  |     | \\   |   | |    | |\n"+
                           "        /  /       |  \\ |___  |  \\  |__/  |    | |____\n"+
                           "       /   \\\n"+
                           " --====     ====-----------------------------------------\n"+
                           "       \\  /                STORE   PROGRAM\n"+
                           "        \\/               Type 'help' for help");
		
		String command;
		ServerOperation defaultServer=new ServerOperation();
		OperatResults result=null;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));     
        
        while(!defaultServer.isQuit())
        {
            System.out.print("\nksp> ");
            while ((command = in.readLine()) == null && command.length() == 0);
            try {
        	    result=excute(defaultServer,command);
        	} catch(Exception e) {
                e.printStackTrace();
                continue;
            }
        	System.out.println(result);
        }
	}
	
	static OperatResults excute(ServerOperation server,String command) throws Exception{
	    String args[]=null;
	    if((args=isSpace      (command))!=null)return new OperatResults(0,"",null);
	    if((args=isNewbook    (command))!=null)return server.newbook  (args);
	    if((args=isArrival    (command))!=null)return server.arrival  (args);
	    if((args=isTwodeg     (command))!=null)return server.twodeg   (args);
	    if((args=isPopbook    (command))!=null)return server.popbook  (args);
	    if((args=isPopauth    (command))!=null)return server.popauth  (args);
	    if((args=isPoppub     (command))!=null)return server.poppub   (args);
	    if((args=isTrusted    (command))!=null)return server.trusted  (args);
	    if((args=isUseful     (command))!=null)return server.useful   (args);	    
	    if((args=isQuit       (command))!=null)return server.quit     (args);
	    if((args=isHelp       (command))!=null)return help();
	    return invCommand();
	}
	
	final static String SP="\\s+",
	                    SPC="\\s*",
	                    STR="\\s+(\\S+)",
	                    STRC="(?:\\s+(\\S+))?",
	                    NUM="\\s+(\\d+)",
	                    NUMC="(?:\\s+(\\d+))?";
	
	static String[] isSpace(String command) {
	    return regMatch(command,SPC);
	}
	
	static String[] isNewbook(String command) {
	    return regMatch(command,SPC+"newbook"+NUM+STR+STR+NUM+NUM+NUM+SP+"(softcover|hardcover)"+"((?:"+SP+"(?:-a|-k|-s)\\s+\\S+)*)"+SPC);
	}
	
	static String[] isArrival(String command) {
	    return regMatch(command,SPC+"arrival"+NUM+NUM+SPC);
	}
	
	static String[] isTwodeg(String command) {
	    return regMatch(command,SPC+"twodeg"+STR+SPC);
	}
	
	static String[] isPopbook(String command) {
	    return regMatch(command,SPC+"popbook"+NUM+SPC);
	}
	
	static String[] isPopauth(String command) {
	    return regMatch(command,SPC+"popauth"+NUM+SPC);
	}
	
	static String[] isPoppub(String command) {
	    return regMatch(command,SPC+"poppub"+NUM+SPC);
	}
	
	static String[] isTrusted(String command) {
	    return regMatch(command,SPC+"trusted"+NUM+SPC);
	}
	
	static String[] isUseful(String command) {
	    return regMatch(command,SPC+"useful"+NUM+SPC);
	}
	
	static String[] isHelp(String command) {
	    return regMatch(command,SPC+"help"+SPC);
	}
	
	static String[] isQuit(String command) {
	    return regMatch(command,SPC+"quit"+SPC);
	}
	
	static String[] regMatch(String command,String reg) {
	    Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(command);
        if(m.matches()) {
            int argc=m.groupCount();
            while(m.group(argc)==null)argc--;
            String args[]=new String[argc];
            for(int i=1;i<=argc;i++)args[i-1]=m.group(i);
            return args;
        } else return null;
	}
	
	static OperatResults help()
	{
		 return new OperatResults(0,"Usage: command [options|arguments]\n"+
                           		    "Note that space words is not allowed in options and arguments, you can replace them by '_' like \"Kerbin_Publish\"\n"+
		                            "Commands:\n"+
		                            "   newbook <isbn> <title> <publisher> <year> <stock> <price> <format> [options]...\n"+
		                            "                                                       Add new book information into the system\n"+
		                            "       Options:\n"+
		                            "           -a <author name>\n"+
		                            "           -k <keyword>\n"+
		                            "           -s <subject>\n"+
		                            "   arrival <isbn> [number]                             Add a number of book(s) into stock\n"+
		                            "   twodeg <author name>                                Show author(s) whose degree is 2 with this author\n"+
		                            "   popbook <number>                                    Show the most popular number book(s), whose sales is 0 will not be count in\n"+
		                            "   popauth <number>                                    Show the most popular number author(s) whose book's sales is 0 will not be count in\n"+
		                            "   poppub <number>                                     Show the most popular number publisher(s), whose book's sales is 0 will not be count in\n"+
		                            "   trusted <number>                                    Show the most 'trusted' number customer(s) customer(s)\n"+
		                            "   useful <number>                                     Show the most 'useful' number customer(s)\n"+
		                            "   help                                                Display this information\n"+
		                            "   quit                                                Quit the Sail's Bookstore monitor"
		                           ,null);
	}
	
	static OperatResults invCommand() {
	    return new OperatResults(0,"invalid command, type 'help' for help.",null);
	}
}
