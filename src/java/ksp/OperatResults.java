package ksp;

public class OperatResults {
    int startCode;
    String desc;
    String args[];
    
    public OperatResults(int s, String d, String a[]) {
        startCode=s;
        desc=d;
        args=a;
    }
    
    public String toString() {
        String ret=desc;
        if(args==null)return ret;
        for(int i=startCode;i<args.length;i++)ret+="\n"+args[i];
        return ret;
    }
}
