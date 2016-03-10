package ksp;

import java.sql.*;
import java.util.*;
import java.text.*;

public class ServerOperation extends Operation {
    public OperatResults newbook(String args[]) throws Exception{
        String re[]=query(String.format("select * from book where isbn=\"%s\"",args[0]));
        if(re.length!=1)return new OperatResults(1,"Book exists.",null);
        System.out.println(String.format("insert book value(\"%s\",\"%s\",\"%s\",%s,%s,%s,\"%s\",\"\")",args[0],args[1],args[2],args[3],args[4],args[5],args[6]));
        execute(String.format("insert book value(\"%s\",\"%s\",\"%s\",%s,%s,%s,\"%s\",\"\")",args[0],args[1],args[2],args[3],args[4],args[5],args[6]));
        String opts[]=args[7].trim().split("\\s+");
        String keywords="";
        for(int i=0;i<opts.length;i+=2) {
            if(opts[i].equals("-a")) {
                re=query(String.format("select * from author where aname=\"%s\"",opts[i+1]));
                if(re.length==1)execute(String.format("insert author value(\"%s\")",opts[i+1]));
                re=query(String.format("select * from authof where aname=\"%s\" and isbn=\"%s\"",opts[i+1],args[0]));
                if(re.length==1)execute(String.format("insert authof value(\"%s\",\"%s\")",opts[i+1],args[0]));
            } else if(opts[i].equals("-s")) {
                re=query(String.format("select * from subject where sname=\"%s\"",opts[i+1]));
                if(re.length==1)execute(String.format("insert subject value(\"%s\",\"\")",opts[i+1]));
                re=query(String.format("select * from issub where isbn=\"%s\" and sname=\"%s\"",args[0],opts[i+1]));
                if(re.length==1)execute(String.format("insert issub value(\"%s\",\"%s\")",args[0],opts[i+1]));
            } else {
                if(!keywords.equals(""))keywords+=";";
                keywords+=opts[i+1];
            }
        }
        execute(String.format("update book set keywords=\"%s\" where isbn=\"%s\"",keywords,args[0]));
        return new OperatResults(0,"Newbook added.",null);  
    }
    
    public OperatResults arrival(String args[]) throws Exception{
        String re[]=query(String.format("select stock from book where isbn=\"%s\"",args[0]));
        if(re.length==1)return new OperatResults(1,"Book not exists.",null);
        int stock=Integer.parseInt(re[1].trim()),delta=Integer.parseInt(args[1].trim());
        if(delta<=0)return new OperatResults(1,"Number must be positive.",null);
        stock+=delta;
        execute(String.format("update book set stock=%d where isbn=\"%s\"",stock,args[0]));
        return new OperatResults(0,"Stock updated.",null);  
    }
    
    public OperatResults twodeg(String args[]) throws Exception{
        String re[]=query(String.format("select * from author where aname=\"%s\"",args[0]));
        if(re.length==1)return new OperatResults(1,"Author not exists.",null);
        re=query(String.format("select distinct a3.aname from authof a3 "+
                                    "where a3.isbn in (select a2.isbn from authof a2 "+
                                                        "where a2.aname in (select a1.aname from authof a1 "+
                                                                            "where a1.isbn in (select a0.isbn from authof a0 "+
                                                                                                "where a0.aname=\"%s\"))) "+
                                          "and a3.aname not in (select a1.aname from authof a1 "+
                                                                "where a1.isbn in (select a0.isbn from authof a0 "+
                                                                                    "where a0.aname=\"%s\"))",args[0],args[0]));
        return new OperatResults(1,String.format("These are author(s) whose degree with %s is 2:",args[0]),re);  
    }
    
    public OperatResults popbook(String args[]) throws Exception{
        java.util.Date now=new java.util.Date();
        SimpleDateFormat sdf;
        if(Calendar.getInstance().get(Calendar.MONTH)<6)sdf=new SimpleDateFormat("yyyy-01-01");
        else sdf=new SimpleDateFormat("yyyy-07-01");
        String re[]=query(String.format("select isbn,sum(number) as sales from item natural join (select * from orders "+
                                        "where odate>=\"%s 00:00:00\") t group by isbn order by sales desc limit %s",sdf.format(now),args[0]));
        return new OperatResults(0,"The most popular "+args[0]+"(or less) book(s) of this semester are:",re);  
    }
    
    public OperatResults popauth(String args[]) throws Exception{
        java.util.Date now=new java.util.Date();
        SimpleDateFormat sdf;
        if(Calendar.getInstance().get(Calendar.MONTH)<6)sdf=new SimpleDateFormat("yyyy-01-01");
        else sdf=new SimpleDateFormat("yyyy-07-01");
        String re[]=query(String.format("select pname,sum(number) as sales from book natural join item natural join (select * from orders "+
                                        "where odate>=\"%s 00:00:00\") t group by pname order by sales desc limit %s",sdf.format(now),args[0]));
        return new OperatResults(0,"The most popular "+args[0]+"(or less) publisher(s) of this semester are:",re);
     }
     
     public OperatResults poppub(String args[]) throws Exception{
        java.util.Date now=new java.util.Date();
        SimpleDateFormat sdf;
        if(Calendar.getInstance().get(Calendar.MONTH)<6)sdf=new SimpleDateFormat("yyyy-01-01");
        else sdf=new SimpleDateFormat("yyyy-07-01");   
        String re[]=query(String.format("select pname,sum(number) as sales from book natural join item natural join (select * from orders "+
                                        "where odate>=\"%s 00:00:00\") t group by pname order by sales desc limit %s",sdf.format(now),args[0]));
        return new OperatResults(0,"The most popular "+args[0]+"(or less) publisher(s) of this semester are:",re);
    }
    
    public OperatResults trusted(String args[]) throws Exception{
        String re[]=query(String.format("select p.login,p.pos-n.neg as trustcount "+
                                            "from (select customer.login,ifnull(pos,0) as pos "+
                                                    "from customer left join (select login,count(*) as pos "+
                                                                                "from declear where istrusted=\"trusted\" group by login) t on customer.login=t.login) p,"+
                                                 "(select customer.login,ifnull(neg,0) as neg "+
                                                    "from customer left join (select login,count(*) as neg "+
                                                                                "from declear where istrusted=\"not-trusted\" group by login) t on customer.login=t.login) n "+
                                            "where p.login=n.login order by trustcount desc limit %s",args[0]));
        return new OperatResults(0,"The most 'trusted' "+args[0]+"(or less) customer(s) are:",re);  
    }
    
    public OperatResults useful(String args[]) throws Exception{    
        String re[]=query(String.format("select customer.login,ifnull(avgrs,0) as avgRateScore from "+
                                            "customer left join (select login,avg(rscore) as avgrs from rate group by login) t on customer.login=t.login order by avgRateScore desc limit %s",args[0]));
        return new OperatResults(0,"The most 'useful' "+args[0]+"(or less) customer(s) are:",re);
    }
}
