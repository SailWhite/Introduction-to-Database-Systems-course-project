package ksp;

import java.sql.*;

public class Operation {
    Connector con=null;
    boolean   _quit=false;
    
    protected String[] query(String sql) throws Exception {
        ResultSet rs=con.executeQuery(sql);
		ResultSetMetaData rsmd = rs.getMetaData();
		int numCols = rsmd.getColumnCount();
		int maxlen[]=new int[numCols+1];
		
		for(int i=1;i<=numCols;i++)maxlen[i]=(""+rsmd.getColumnName(i)).length();
        while(rs.next())
            for(int i=1;i<=numCols;i++)
                if(maxlen[i]<(""+rs.getString(i)).length())maxlen[i]=(""+rs.getString(i)).length();
		
		rs.last();
		String ret[]=new String[rs.getRow()+1];
		ret[0]="";
		for(int i=1;i<=numCols;i++){
		    ret[0]+=rsmd.getColumnName(i)+"  ";
		    for(int j=rsmd.getColumnName(i).length();j<maxlen[i];j++)ret[0]+=" ";
		}
		if(ret.length==1)return ret;
        rs.first();
        do {
            ret[rs.getRow()]="";
            for (int i=1; i<=numCols;i++) {
                ret[rs.getRow()]+=rs.getString(i)+"  ";
                for(int j=(""+rs.getString(i)).length();j<maxlen[i];j++)ret[rs.getRow()]+=" ";
            }
        } while (rs.next());
	    return ret;
	}
	
	protected boolean execute(String sql) throws Exception {
	    return con.execute(sql);
	}
	
    public Operation() {
        try {
            con= new Connector();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println ("Cannot connect to database server");
        }
    }
    
    public OperatResults quit(String args[]) {
        _quit=true;
        return new OperatResults(0,"Bye.",null);
    }
    
    public boolean isQuit() {
        return _quit;
    }
}

