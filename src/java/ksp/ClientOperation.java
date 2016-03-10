package ksp;

import java.sql.*;
import java.util.*;

public class ClientOperation extends Operation {
    String loginName;
    HashMap<String,Integer> cart;
    
    
    public ClientOperation() {
        super();
        loginName=null;
        cart=new HashMap<String,Integer>();
    }
    
    public OperatResults registe(String args[]) throws Exception{
        String re[]=query(String.format("select * from customer where login=\"%s\"",args[0]));
        if(re.length>1)return new OperatResults(1,"Loginname existed.",null);
        if(args.length==2)execute(String.format("insert customer value(\"%s\",\"%s\",\"\",\"\")",args[0],args[1]));
        else if(args.length==3)execute(String.format("insert customer value(\"%s\",\"%s\",\"%s\",\"\")",args[0],args[1],args[2]));
        else execute(String.format("insert customer value(\"%s\",\"%s\",\"%s\",\"%s\")",args[0],args[1],args[2],args[3]));
        return new OperatResults(0,"registe Successful.",null);  
    }
    
    public OperatResults login(String args[]) throws Exception{
        String re[]=query(String.format("select * from customer where login=\"%s\"",args[0]));
        if(re.length==1)return new OperatResults(1,"Invalid loginname.",null);
        re=query(String.format("select * from customer where login=\"%s\" and password=\"%s\"",args[0],args[1]));
        if(re.length==1)return new OperatResults(1,"Wrong password.",null);
        loginName=args[0];
        return new OperatResults(0,"Login Successful.",null);
    }
    
    public OperatResults add(String args[]) throws Exception{
        String re[]=query(String.format("select * from book where isbn=\"%s\"",args[0]));
        if(re.length==1)return new OperatResults(1,"Book not exists.",null);
        if(cart.get(args[0])==null)cart.put(args[0],0);
        int num=cart.get(args[0]),delta=1;
        if(args.length==2) {
            delta=Integer.parseInt(args[1].trim());
            if(delta<=0)return new OperatResults(1,"Number must be positive.",null);
        }
        num+=delta;
        cart.put(args[0],num);
        
        
        re=query(String.format("select * from book natural join (select isbn,sum(number) as salescount from item natural join "+
                                                                    "(select o.oid,o.login from orders o "+
                                                                        "where o.login in (select login from (orders natural join "+
                                                                            "(select i.oid from item i where i.isbn=\"%s\") t))) s group by isbn) k "+
                                    "where isbn!=\"%s\" order by salescount desc limit 10",args[0],args[0]));
        if(re.length==1)return new OperatResults(0,"Add Successful.",null);
        return new OperatResults(0,"Add Successful. And you may like these books:",re);
    }
    
    public OperatResults remove(String args[]) throws Exception{
        String re[]=query(String.format("select * from book where isbn=\"%s\"",args[0]));
        if(re.length==1)return new OperatResults(1,"Book not exists.",null);
        if(cart.get(args[0])==null)return new OperatResults(1,"This book is not in cart.",null);
        int num=cart.get(args[0]),delta=1;
        if(args.length==2) {
            delta=Integer.parseInt(args[1].trim());
            if(delta<=0)return new OperatResults(1,"Number must be positive.",null);
        }
        num-=delta;
        if(num>0)cart.put(args[0],num);
        else cart.put(args[0],null);
        return new OperatResults(0,"Remove Successful.",null);
    }
    
    public OperatResults feedback(String args[]) throws Exception{
        if(loginName==null)return new OperatResults(1,"Please login.",null);
        String re[]=query(String.format("select * from book where isbn=\"%s\"",args[0]));
        if(re.length==1)return new OperatResults(1,"Book not exists.",null);
        re=query(String.format("select * from feedback where login=\"%s\" and isbn=\"%s\"",loginName,args[0]));
        if(re.length!=1)return new OperatResults(1,"You have given a feedback about this book before.",null);
        int score=Integer.parseInt(args[1].trim());
        if(score<0 || score>10)return new OperatResults(1,"Score out of range.",null);
        if(args.length==2)execute(String.format("insert feedback(login,isbn,fscore,fdesc) value(\"%s\",\"%s\",%s,\"\")",loginName,args[0],args[1]));
        else execute(String.format("insert feedback(login,isbn,fscore,fdesc) value(\"%s\",\"%s\",%s,\"%s\")",loginName,args[0],args[1],args[2]));
        return new OperatResults(0,"Feedback Successful.",null);
    }
    
    public OperatResults rate(String args[]) throws Exception{
        if(loginName==null)return new OperatResults(1,"Please login.",null);
        if(loginName.equals(args[0]))return new OperatResults(1,"You can not rate yourself's feedback.",null);
        String re[]=query(String.format("select * from feedback where login=\"%s\" and isbn=\"%s\"",args[0],args[1]));
        if(re.length==1)return new OperatResults(1,"Feedback not exists.",null);
        re=query(String.format("select * from rate where rater=\"%s\" and login=\"%s\" and isbn=\"%s\"",loginName,args[0],args[1]));
        if(re.length!=1)return new OperatResults(1,"You have rated about this feedback before.",null);
        int score=Integer.parseInt(args[2].trim());
        if(score<0 || score>2)return new OperatResults(1,"Score out of range.",null);
        execute(String.format("insert rate(rater,login,isbn,rscore) value(\"%s\",\"%s\",\"%s\",%s)",loginName,args[0],args[1],args[2]));
        return new OperatResults(0,"Rate Successful.",null);
    }
    
    public OperatResults declear(String args[]) throws Exception{
        if(loginName==null)return new OperatResults(1,"Please login.",null);
        if(loginName.equals(args[0]))return new OperatResults(1,"You can not declear yourself.",null);
        String re[]=query(String.format("select * from customer where login=\"%s\"",args[0]));
        if(re.length==1)return new OperatResults(1,"User not exists.",null);
        re=query(String.format("select * from declear where decer=\"%s\" and login=\"%s\"",loginName,args[0]));
        if(re.length>1)return new OperatResults(1,"You have decleared this user before.",null);
        execute(String.format("insert declear(decer,login,istrusted) value(\"%s\",\"%s\",\"%s\")",loginName,args[0],args[1]));
        return new OperatResults(0,"Declear Successful.",null);
    }
    
    public OperatResults brows(String args[]) throws Exception{
        String ac="false",pc="false",wc="false",sc="false";
        String opts[]=args[0].trim().split("\\s+");
        for(int i=0;i<opts.length;i+=2) {
            if(opts[i].equals("-a"))ac+=String.format(" or a.aname like \"%%%s%%\"",opts[i+1]);
            else if(opts[i].equals("-p"))pc+=String.format(" or pname like \"%%%s%%\"",opts[i+1]);
            else if(opts[i].equals("-w"))wc+=String.format(" or title like \"%%%s%%\"",opts[i+1]);
            else if(opts[i].equals("-s"))sc+=String.format(" or s.sname like \"%%%s%%\"",opts[i+1]);
        }
        
        String sql=" where true";
        if(!pc.equals("false"))sql+=" and ("+pc+")";
        if(!wc.equals("false"))sql+=" and ("+wc+")";
        if(!ac.equals("false"))sql+=" and book.isbn in (select a.isbn from authof a where "+ac+")";
        if(!sc.equals("false"))sql+=" and book.isbn in (select s.isbn from issub s where "+sc+")";
        
        if(args.length==1)sql="select * from book"+sql;
        else if(args[1].equals("y"))sql="select * from book "+sql+" order by year";
        else if(args[1].equals("f"))sql="select book.isbn,title,pname,year,stock,price,format,keywords,ifnull(avgsc,0) as avgsc "+
                                            "from (book left join (select f.isbn,avg(f.fscore) as avgsc from feedback f group by f.isbn) t on book.isbn=t.isbn)"+sql+" order by avgsc desc";
        else if(loginName!=null)sql="select book.isbn,title,pname,year,stock,price,format,keywords,ifnull(avgsc,0) as avgsc from (book left join (select f.isbn,avg(f.fscore) as avgsc from feedback f "+
                                                                            "where (\""+loginName+"\",f.login,\"trusted\") in (select d.decer,d.login,d.istrusted "+
                                                                                                                          "from declear d) group by f.isbn) t on book.isbn=t.isbn)"+sql+" order by avgsc desc";
        else return new OperatResults(1,"Please login.",null);
        
        String re[]=query(sql);
        if(re.length==1)return new OperatResults(0,"No book suits.",null);
        return new OperatResults(0,"Following are book(s) you are looking for:",re);
    }
    
    public OperatResults askfeed(String args[]) throws Exception{
        String re[]=query(String.format("select * from book where isbn=\"%s\"",args[0]));
        if(re.length==1)return new OperatResults(1,"Book not exists.",null);
        int num=10;
        if(args.length>1)num=Integer.parseInt(args[1].trim());;
        if(num<=0)return new OperatResults(1,"Number must be positive.",null);
        re=query(String.format("select * from"+ 
                               "    (feedback natural join (select r.login,r.isbn,avg(r.rscore) as avgscore from rate r "+
                               "                            where r.isbn=\"%s\" group by r.login) tmp)"+
                               "    order by avgscore desc limit %d;",args[0],num));
        if(re.length==1)return new OperatResults(0,"No feedback finded.",null);
        return new OperatResults(0,String.format("Following are top %s(or less) feedback(s):",num),re);
    }
    
    public OperatResults showcart(String args[]) throws Exception{
        String re[]=null;
        int numbook=0,amount=0;
        String ret="";
        Iterator iter=cart.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry en=(Map.Entry)iter.next();
            if(en.getValue()==null)continue;
            re=query(String.format("select price from book where isbn=\"%s\"",en.getKey()));
            numbook+=(int)en.getValue();
            amount+=(int)en.getValue()*Integer.parseInt(re[1].trim());
            ret+=String.format("\n%s :%8d       subtotle %8d cents",en.getKey(),(int)en.getValue(),(int)en.getValue()*Integer.parseInt(re[1].trim()));
        }
        if(numbook==0)return new OperatResults(0,"The cart is empty.",null);
        return new OperatResults(0,String.format("Following are what you have selected:"+ret+"\nTotle price for these %d book(s) is %d cents.",numbook,amount),null);
    }
    
    public OperatResults submit(String args[]) throws Exception{
        if(loginName==null)return new OperatResults(1,"Please login.",null);
        String re[]=null;
        int numbook=0,amount=0;
        String redesc="";
        Iterator iter=cart.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry en=(Map.Entry)iter.next();
            if(en.getValue()==null)continue;
            re=query(String.format("select stock from book where isbn=\"%s\"",en.getKey()));
            int stock=Integer.parseInt(re[1].trim());
            if(stock<(int)en.getValue())redesc+=String.format("\nBook %s's stock is too low, only %s remained.",en.getKey(),stock);
            re=query(String.format("select price from book where isbn=\"%s\"",en.getKey()));
            numbook+=(int)en.getValue();
            amount+=(int)en.getValue()*Integer.parseInt(re[1].trim());
        }
        if(numbook==0)return new OperatResults(0,"The cart is empty.",null);
        if(!redesc.equals(""))return new OperatResults(0,"Submit failed:"+redesc,null);
        
        execute(String.format("insert orders(login) value(\"%s\")",loginName));
        re=query("select last_insert_id() from orders");
        int oid=Integer.parseInt(re[1].trim()),iid=1;
        iter=cart.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry en=(Map.Entry)iter.next();
            if(en.getValue()==null)continue;
            execute(String.format("insert item value(%d,%d,\"%s\",%d)",oid,iid,en.getKey(),en.getValue()));
            re=query(String.format("select stock from book where isbn=\"%s\"",en.getKey()));
            int stock=Integer.parseInt(re[1].trim());
            execute(String.format("update book set stock=%d where isbn=\"%s\"",stock-(int)en.getValue(),en.getKey()));
            iid++;
        }
        cart=new HashMap<String,Integer>();
        
        return new OperatResults(0,String.format("Order submited. Totle price for these %d book(s) is %d cents.",numbook,amount),null);
    }
}
