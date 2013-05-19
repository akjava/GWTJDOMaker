package com.akjava.gwt.jdomaker.client.templates;

import java.util.List;
import com.akjava.gwt.jdomaker.client.*;

public class CodeMakeJETTemplate
{
  protected static String nl;
  public static synchronized CodeMakeJETTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    CodeMakeJETTemplate result = new CodeMakeJETTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? ("\r\n") : nl;
  protected final String TEXT_1 = "import javax.jdo.annotations.IdentityType;" + NL + "import javax.jdo.annotations.PersistenceCapable;" + NL + "import javax.jdo.annotations.Persistent;" + NL + "import javax.jdo.annotations.PrimaryKey;" + NL + "import javax.jdo.annotations.Version;" + NL + "import javax.jdo.annotations.VersionStrategy;" + NL + "" + NL + "" + NL + "@PersistenceCapable(identityType = IdentityType.APPLICATION,detachable=\"true\")" + NL + "@Version(strategy = VersionStrategy.VERSION_NUMBER, column = \"version\")  " + NL + "public class ";
  protected final String TEXT_2 = " {" + NL + "" + NL + "\t@PrimaryKey" + NL + "\t";
  protected final String TEXT_3 = NL + "\t@Persistent" + NL + "\tprivate ";
  protected final String TEXT_4 = " ";
  protected final String TEXT_5 = ";" + NL + "\t" + NL + "\tpublic ";
  protected final String TEXT_6 = " get";
  protected final String TEXT_7 = "() {" + NL + "\t\treturn ";
  protected final String TEXT_8 = ";" + NL + "\t}" + NL + "" + NL + "\tpublic void set";
  protected final String TEXT_9 = "(";
  protected final String TEXT_10 = " ";
  protected final String TEXT_11 = ") {" + NL + "\t\tthis.";
  protected final String TEXT_12 = " = ";
  protected final String TEXT_13 = ";" + NL + "\t}" + NL + "\t";
  protected final String TEXT_14 = "\t" + NL + "}";
  protected final String TEXT_15 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
     JDOClass jdoclass=(JDOClass)argument; 
    stringBuffer.append(TEXT_1);
    stringBuffer.append( jdoclass.getName() );
    stringBuffer.append(TEXT_2);
      List<JDOValue> values=jdoclass.getJdoList();
	for(JDOValue value:values){
	
    stringBuffer.append(TEXT_3);
    stringBuffer.append(value.getType());
    stringBuffer.append(TEXT_4);
    stringBuffer.append(value.getName());
    stringBuffer.append(TEXT_5);
    stringBuffer.append(value.getType());
    stringBuffer.append(TEXT_6);
    stringBuffer.append(value.getUpperStartName());
    stringBuffer.append(TEXT_7);
    stringBuffer.append(value.getName());
    stringBuffer.append(TEXT_8);
    stringBuffer.append(value.getUpperStartName());
    stringBuffer.append(TEXT_9);
    stringBuffer.append(value.getType());
    stringBuffer.append(TEXT_10);
    stringBuffer.append(value.getName());
    stringBuffer.append(TEXT_11);
    stringBuffer.append(value.getName());
    stringBuffer.append(TEXT_12);
    stringBuffer.append(value.getName());
    stringBuffer.append(TEXT_13);
    
	}
	
    stringBuffer.append(TEXT_14);
    stringBuffer.append(TEXT_15);
    return stringBuffer.toString();
  }
}
