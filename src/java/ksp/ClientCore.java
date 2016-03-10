package ksp;

import java.util.regex.*;
import java.lang.*;
import java.io.*;

/*******************************************************************************
 *@author Sail White
 *
 ******************************************************************************/
public class ClientCore {
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
		ClientOperation defaultClient=new ClientOperation();
		OperatResults result=null;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));     
        
        while(!defaultClient.isQuit())
        {
            System.out.print("\nksp> ");
            while ((command = in.readLine()) == null && command.length() == 0);
            try {
        	    result=excute(defaultClient,command);
        	} catch(Exception e) {
                e.printStackTrace();
                continue;
            }
        	System.out.println(result);
        }
	}
	
	static OperatResults excute(ClientOperation client,String command) throws Exception{
	    String args[]=null;
	    if((args=isSpace      (command))!=null)return new OperatResults(0,"",null);
	    if((args=isRegiste    (command))!=null)return client.registe  (args);
	    if((args=isLogin      (command))!=null)return client.login    (args);
	    if((args=isAdd        (command))!=null)return client.add      (args);
	    if((args=isRemove     (command))!=null)return client.remove   (args);
	    if((args=isFeedback   (command))!=null)return client.feedback (args);
	    if((args=isRate       (command))!=null)return client.rate     (args);
	    if((args=isDeclear    (command))!=null)return client.declear  (args);
	    if((args=isBrows      (command))!=null)return client.brows    (args);
	    if((args=isAskfeed    (command))!=null)return client.askfeed  (args);
	    if((args=isShowcart   (command))!=null)return client.showcart (args);
	    if((args=isSubmit     (command))!=null)return client.submit   (args);
	    if((args=isQuit       (command))!=null)return client.quit     (args);
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
	
	static String[] isRegiste(String command) {
	    return regMatch(command,SPC+"registe"+STR+STR+STRC+NUMC+SPC);
	}
	
	static String[] isLogin(String command) {
	    return regMatch(command,SPC+"login"+STR+STR+SPC);
	}
	
	static String[] isAdd(String command) {
	    return regMatch(command,SPC+"add"+NUM+NUMC+SPC);
	}
	
	static String[] isRemove(String command) {
	    return regMatch(command,SPC+"remove"+NUM+NUMC+SPC);
	}
	
	static String[] isFeedback(String command) {
	    return regMatch(command,SPC+"feedback"+NUM+NUM+STRC+SPC);
	}
	
	static String[] isRate(String command) {
	    return regMatch(command,SPC+"rate"+STR+NUM+NUM+SPC);
	}
	
	static String[] isDeclear(String command) {
	    return regMatch(command,SPC+"declear"+STR+SP+"(trusted|not-trusted)"+SPC);
	}
	
	static String[] isBrows(String command) {
	    return regMatch(command,SPC+"brows"+"((?:"+SP+"(?:-a|-p|-w|-s)\\s+\\S+)*)"+"(?:"+SP+"-o"+SP+"(y|f|t))?"+SPC);
	}
	
	static String[] isAskfeed(String command) {
	    return regMatch(command,SPC+"askfeed"+NUM+NUMC+SPC);
	}
	
	static String[] isShowcart(String command) {
	    return regMatch(command,SPC+"showcart"+SPC);
	}
	
	static String[] isSubmit(String command) {
	    return regMatch(command,SPC+"submit"+SPC);
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
		                            "   registe <loginname> <password> [address] [phone]    Registe a new account with optinal address and phone number, phone number shuold be pure digits\n"+
		                            "   longin <loginname> <password>                       User login\n"+
		                            "   add <bookid> [number]                               Add a number(default by 1) of book(s) into the cart, and get at most 10 buying suggestion for this book\n"+
		                            "   remove <bookid> [number]                            Remove a number(default by 1) of book(s) from the cart\n"+
		                            "   feedback <bookid> <score> [description]             Give the book a feedback with a 0(terrible)..10(masterpiece) score, and a optional description\n"+
		                            "   rate <login> <isbn> <score>                         Rat a feedback gived by another user about this book with a 0(useless)..2(very useful) score\n"+
		                            "   declear <loginname> [trusted|not-trusted]           Declear another user as 'trusted' or 'not-trusted'\n"+
		                            "   brows [options]... [-o order]                       Brows books where authors, publisher, title-words and subject suits these options and sorted by the order\n"+
		                            "       Options:\n"+
		                            "           -a <author name>\n"+
		                            "           -p <publisher>\n"+
		                            "           -w <title-word>\n"+
		                            "           -s <subject>\n"+
		                            "       Order:\n"+
		                            "           y:by year\n"+
		                            "           f:by the average numerical score of the feedbacks\n"+
		                            "           t:by the average numerical score of the trusted user feedbacks\n"+
		                            "       For example:\n"+
		                            "           brows -a Jeb_Kerman -p Kerbin_Publisher -a Bill_Kerman -s space -o f\n"+
		                            "       will shows all books writed by Jeb Kerman or Bill Kerman and published by Kerbin Publisher and the subject is about space, sorted by the average numerical score of the feedbacks\n"+
		                            "   askfeed <bookid> [number]                           Ask for the most useful number feedbacks about the book\n"+
		                            "   showcart                                            Display selected book(s)\n"+		                            
		                            "   submit                                              Submit a order contains books in the cart\n"+
		                            "   help                                                Display this information\n"+
		                            "   quit                                                Quit the Sail's Bookstore monitor"
		                           ,null);
	}
	
	static OperatResults invCommand() {
	    return new OperatResults(0,"invalid command, type 'help' for help.",null);
	}
}
