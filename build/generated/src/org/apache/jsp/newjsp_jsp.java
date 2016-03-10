package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import ksp.*;

public final class newjsp_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List<String> _jspx_dependants;

  private org.glassfish.jsp.api.ResourceInjector _jspx_resourceInjector;

  public java.util.List<String> getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;

    try {
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;
      _jspx_resourceInjector = (org.glassfish.jsp.api.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");

      out.write("\n");
      out.write("<html>\n");
      out.write("<head>\n");
      out.write("<script LANGUAGE=\"javascript\">\n");
      out.write("\n");
      out.write("function check_all_fields(form_obj){\n");
      out.write("\talert(form_obj.searchAttribute.value+\"='\"+form_obj.attributeValue.value+\"'\");\n");
      out.write("\tif( form_obj.attributeValue.value === \"\"){\n");
      out.write("\t\talert(\"Search field should be nonempty\");\n");
      out.write("\t\treturn false;\n");
      out.write("\t}\n");
      out.write("\treturn true;\n");
      out.write("}\n");
      out.write("\n");
      out.write("</script> \n");
      out.write("</head>\n");
      out.write("<body>\n");
      out.write("\n");

String searchAttribute = request.getParameter("searchAttribute");
if( searchAttribute == null ){

      out.write("\n");
      out.write("\n");
      out.write("\tForm1: Search orders on user name:\n");
      out.write("\t<form name=\"user_search\" method=get onsubmit=\"return check_all_fields(this)\" action=\"orders.jsp\">\n");
      out.write("\t\t<input type=hidden name=\"searchAttribute\" value=\"login\">\n");
      out.write("\t\t<input type=text name=\"attributeValue\" length=10>\n");
      out.write("\t\t<input type=submit>\n");
      out.write("\t</form>\n");
      out.write("\t<BR><BR>\n");
      out.write("\tForm2: Search orders on director name:\n");
      out.write("\t<form name=\"director_search\" method=get onsubmit=\"return check_all_fields(this)\" action=\"orders.jsp\">\n");
      out.write("\t\t<input type=hidden name=\"searchAttribute\" value=\"director\">\n");
      out.write("\t\t<input type=text name=\"attributeValue\" length=10>\n");
      out.write("\t\t<input type=submit>\n");
      out.write("\t</form>\n");
      out.write("\n");


} else {

	String attributeValue = request.getParameter("attributeValue");
	//fudandb.Connector connector = new Connector();
	//fudandb.Order order = new Order();
	

      out.write("  \n");
      out.write("\n");
      out.write("  <p><b>Listing orders in JSP: </b><BR><BR>\n");
      out.write("\n");
      out.write("  The orders for query: <b>");
      out.print(searchAttribute);
      out.write('=');
      out.write('\'');
      out.print(attributeValue);
      out.write("'</b> are  as follows:<BR><BR>\n");
      out.write("  ");
      out.print("ss");//order.getOrders(searchAttribute, attributeValue, connector.stmt));
      out.write(" <BR><BR>\n");
      out.write("  \n");
      out.write("  <b>Alternate way (servlet method):</b> <BR><BR>\n");
      out.write("  ");

		out.println("The orders for query: <b>"+searchAttribute+"='"+attributeValue+
					"'</b> are as follows:<BR><BR>");
		//out.println(order.getOrders(searchAttribute, attributeValue, connector.stmt));
  
      out.write('\n');
      out.write('\n');

 //connector.closeStatement();
 //connector.closeConnection();
}  // We are ending the braces for else here

      out.write("\n");
      out.write("\n");
      out.write("<BR><a href=\"orders.jsp\"> New query </a></p>\n");
      out.write("\n");
      out.write("<p>Schema for Order table: <font face=\"Geneva, Arial, Helvetica, sans-serif\">( \n");
      out.write("  title varchar(100), quantity integer, login varchar(8), director varchar(10) \n");
      out.write("  )</font></p>\n");
      out.write("\n");
      out.write("</body>\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
