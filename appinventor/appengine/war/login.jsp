<%@page import="javax.servlet.http.HttpServletRequest"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!doctype html>
<%
   String error = (String) session.getAttribute("error");
   if (error != null) {
      session.removeAttribute("error");
   }
   String useGoogleLabel = (String) request.getAttribute("useGoogleLabel");
   String locale = request.getParameter("locale");
   if (locale == null) {
       locale = "en";
   }
   if("zh_CN".equals(locale) || "en".equals(locale)){
       session.setAttribute("locale", locale);
   }
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta HTTP-EQUIV="pragma" CONTENT="no-cache"/>
    <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate"/>
    <meta HTTP-EQUIV="expires" CONTENT="0"/>
    <title>VEDILS</title>
  </head>
<body>
  <center>
    <h1>${pleaselogin}</h1></center>
  </center>
<% if (error != null) {
out.println("<center><font color=red><b>" + error + "</b></font></center><br/>");
   } %>
<form method=POST action="/login">
<center><table>
<tr><td>${emailAddressLabel}</td><td><input type=text name=email value="" size="35"></td></tr>
<tr><td></td></td>
<tr><td>${passwordLabel}</td><td><input type=password name=password value="" size="35"></td></tr>
</table></center>
<p></p>
<center><input type=Submit value="${login}" style="font-size: 300%;"></center>
</form>
<p></p>
<!--<center><p><a href="/login/sendlink"  style="text-decoration:none;">${passwordclickhereLabel}</a></p></center>-->
<center><p>
If you have experienced problems to access, please contact with us at vedils at uca.es
<br/>
Si tiene problemas para acceder, póngase en contacto con nosotros en el correo vedils(arroba)uca.es
</p></center>
<%    if (useGoogleLabel != null && useGoogleLabel.equals("true")) { %>
<center><p><a href="/login/google" style="text-decoration:none;">Click Here to use your Google Account to login</a></p></center>
<%    } %>
<footer>
<center>
<%    if (locale != null && locale.equals("zh_CN")) { %>
<a href="http://vedils.uca.es/web" target="_blank"><img class="img-scale"
                  src="/images/mzl.png" width="30" height="30" title="Sina WeiBo"></a>&nbsp;
<%    } %>
<a href="http://vedils.uca.es/web" target="_blank"><img class="img-scale"
                src="/images/logo.png" width="50" height="50" title="VEDILS"></a></center>
<p></p>
</footer>
</body></html>

