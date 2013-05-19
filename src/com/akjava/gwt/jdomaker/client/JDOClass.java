package com.akjava.gwt.jdomaker.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JDOClass {
private String name;
private List<JDOValue> jdoList=new ArrayList<JDOValue>();

public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public List<JDOValue> getJdoList() {
	return jdoList;
}
public void setJdoList(List<JDOValue> jdoList) {
	this.jdoList = jdoList;
	
}

private boolean detachable;
private boolean longRunningTransaction;
private boolean serializable;

public String createSerializableString(){
	if(serializable){
		return "implements Serializable{\n" +
				"\tprivate static final long serialVersionUID = 1L;";
	}else{
		return "{";
	}
}

public boolean isSerializable() {
	return serializable;
}
public void setSerializable(boolean serializable) {
	this.serializable = serializable;
}
public String createDetachableString(){
	if(detachable){
		return ",detachable=\"true\"";
	}else{
		return "";
	}
}

public String createLongRunningTransactionString(){
	if(longRunningTransaction){
		return "@Version(strategy = VersionStrategy.VERSION_NUMBER, column = \"version\")";
	}else{
		return "";
	}
}

public boolean isDetachable() {
	return detachable;
}
public void setDetachable(boolean detachable) {
	this.detachable = detachable;
}
public boolean isLongRunningTransaction() {
	return longRunningTransaction;
}
public void setLongRunningTransaction(boolean longRunningTransaction) {
	this.longRunningTransaction = longRunningTransaction;
}
public void add(JDOValue value) {
	jdoList.add(value);
}
}
