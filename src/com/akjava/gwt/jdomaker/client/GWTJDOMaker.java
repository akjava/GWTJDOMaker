package com.akjava.gwt.jdomaker.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.akjava.gwt.jdomaker.client.templates.CodeMakeJETTemplate;
import com.akjava.gwt.libs.client.ValueUtils;
import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.ObjectArrays;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 * 
 * 
 * need The import javax.annotation.CheckReturnValue cannot be resolved
 * 
 */
public class GWTJDOMaker implements EntryPoint {


	private TextArea input;
	private TextArea output;
	private CheckBox detachableCheck;
	private CheckBox longRunningTransactionCheck;
	private CheckBox serializableCheck;

	
	/**
	 * This is the entry point method.
	 * 
	 */
	public void onModuleLoad() {
		
		
		
		
		
		VerticalPanel root=new VerticalPanel();
		
		HorizontalPanel checks=new HorizontalPanel();
		root.add(checks);
		detachableCheck = new CheckBox("detachable");
		detachableCheck.setValue(true);
		checks.add(detachableCheck);
		
		serializableCheck = new CheckBox("serialize");
		checks.add(serializableCheck);
		
		longRunningTransactionCheck = new CheckBox("Long Running Transaction");
		checks.add(longRunningTransactionCheck);
		
		
		HorizontalPanel hpanel=new HorizontalPanel();
		root.add(hpanel);
		
		Button make=new Button("make");
		make.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				doMake();
			}
		});
		root.add(make);
		
		input = new TextArea();
		input.setSize("400px","400px");
		
		input.setText("DataEntity\n" +
					  "id Long valueStrategy=IdGeneratorStrategy.IDENTITY\n" +
					  "name");
		
		hpanel.add(input);
		
		output = new TextArea();
		output.setSize("400px","400px");
		hpanel.add(output);
		
	
		
		RootPanel.get("container").add(root);
	}

	protected void doMake() {
		JDOClass jdoc=stringToJdoClass(input.getText());
		String text=new CodeMakeJETTemplate().generate(jdoc);
		output.setText(text);
	}
	
	protected JDOClass stringToJdoClass(String text){
		//List<String[]> datas=CSVUtils.parseCSV(text, ' ', 0);
		List<String[]> datas=parseCsvByGuava(text);
				
		JDOClass jdoclass=new JDOClass();
		jdoclass.setDetachable(detachableCheck.getValue());
		jdoclass.setSerializable(serializableCheck.getValue());
		jdoclass.setLongRunningTransaction(longRunningTransactionCheck.getValue());
		
		boolean firstOne=false;
		for(String[] data:datas){//TODO change it
			if(!firstOne){
			jdoclass.setName(data[0]);//first one is name
			System.out.println("name:"+jdoclass.getName());
			firstOne=true;
			}else{
			JDOValue value=new JDOValue(data);
			System.out.println(value.getType());
			jdoclass.add(value);
			}
		}
		return jdoclass;
	}
	
	private List<String[]> parseCsvByGuava(String text){
		List<String[]> lists=new ArrayList<String[]>();
		 text=ValueUtils.toNLineSeparator(text);
		 
		 Splitter splitter=Splitter.on(CharMatcher.WHITESPACE);
		 String[] lines=text.split("\n");
		 for(String line:lines){
			 Iterable<String> vs=splitter.split(line);
			// String[] v=Iterables.toArray(vs, String.class);
			 String[] v=toArray(vs);
			 lists.add(v);
		 }
		return lists;
	}
	
	private String[] toArray(Iterable<String> vs){
		Collection<String> collection =  Lists.newArrayList(vs);
	    String[] array = ObjectArrays.newArray(new String[0],collection.size());
	    return collection.toArray(array);
	}
	
}
