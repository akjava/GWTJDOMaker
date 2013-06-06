package com.akjava.gwt.jdomaker.client;

public class JDOValue {

public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getType() {
	return type;
}

public String getAnnotation() {
	return annotation;
}
public void setAnnotation(String annotation) {
	this.annotation = annotation;
}
public String getLabel() {
	return label;
}
public void setLabel(String label) {
	this.label = label;
}
private String name;
private String type="String";
private String annotation;
private String label;

public  JDOValue(String[] values){
	setName(values[0]);
	
	if(values.length>1){
		setType(values[1]);
	}
	
	//TODO future support
	if(values.length>2){
		setAnnotation(values[2]);
	}
	if(values.length>3){
		setLabel(values[3]);
	}
}

public void setType(String value){
	//do short version.
	//type=toUpperStartString(value);
	//support short version
	if(value.isEmpty()){
		value="String";
	}
	this.type=value;
}

public String createAnnotationString(){
	if(hasAnnotation()){
		return "("+getAnnotation()+")";
	}else{
		return "";
	}
}
public boolean hasAnnotation(){
	return annotation!=null && !annotation.isEmpty();
}

private static String toUpperStartString(String str){
	if(str.length()==0){
		return str;
	}
	String ret=str.substring(0,1).toUpperCase();
	if(str.length()>1){
	ret+=str.substring(1);
	}
	return ret;
}

public String getUpperStartName(){
	String ret=name.substring(0,1).toUpperCase();
	if(name.length()>1){
	ret+=name.substring(1);
	}
	return ret;
}


public String createArgType(){
	if(type.equals("Text")){
		return "String";
	}else if(type.equals("Blob")){
		return "byte[]";
	}
		else{
		return getType();
	}
}

public String createReturnType(){
	if(type.equals("Text")){
		return "String";
	}else if(type.equals("Blob")){
		return "byte[]";
	}
		else{
		return getType();
	}
}
public String createGetMethod(){
	if(type.equals("Text")){
		String ret=""+
		"\tif(" +getName()+"==null){\n"+
			"\t\treturn null;\n"+
		"\t}else{\n"+
			"\t\treturn "+getName()+".getValue();\n"+
		"\t}";
		return ret;
	}else if(type.equals("Blob")){
		String ret=""+
		"\tif(" +getName()+"==null){\n"+
			"\t\treturn null;\n"+
		"\t}else{\n"+
			"\t\treturn "+getName()+".getBytes();\n"+
		"\t}";
		return ret;
	}else{
		return "return "+getName()+";";
	}
}

public String createSetMethod(){
	if(type.equals("Text") ){
		return "this."+getName()+"= new Text("+getName()+");";
		
	}else if(type.equals("Blob")){
		return "this."+getName()+"= new Blob("+getName()+");";
	}
	else{
		return "this."+getName()+"="+getName()+";";
	}
}

}
