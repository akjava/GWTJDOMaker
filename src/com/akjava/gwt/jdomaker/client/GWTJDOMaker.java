package com.akjava.gwt.jdomaker.client;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.akjava.gwt.html5.client.file.ui.FileNameAndText;
import com.akjava.gwt.jdomaker.client.resources.Bundles;
import com.akjava.gwt.jdomaker.client.templates.CodeMakeJETTemplate;
import com.akjava.lib.common.utils.TemplateUtils;
import com.akjava.lib.common.utils.ValuesUtils;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
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
	private TextArea entityOutput;
	private CheckBox detachableCheck;
	private CheckBox longRunningTransactionCheck;
	private CheckBox serializableCheck;
	private TextArea daoOutput;
	private TextBox classNameBox;
	private TextArea pmfOutput;

	private TextArea dtoOutput;

	
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
		
		
		root.add(createAddForm());
		
		HorizontalPanel hpanel=new HorizontalPanel();
		root.add(hpanel);
		hpanel.add(new Label("ClassName"));
		classNameBox = new TextBox();
		classNameBox.setWidth("200px");
		classNameBox.setText("DataEntity");
		hpanel.add(classNameBox);
		
		input = new TextArea();
		input.setSize("600px","400px");
		
		input.setText(
					  "id,Long,valueStrategy=IdGeneratorStrategy.IDENTITY\n");
		
		root.add(input);
		
		
		Button make=new Button("make");
		make.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				doMake();
			}
		});
		root.add(make);
		
		
		
		
		
		TabPanel tab=new TabPanel();
		tab.addSelectionHandler(new SelectionHandler<Integer>() {
			
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				if(event.getSelectedItem()==0){
					entityOutput.selectAll();
				}else if(event.getSelectedItem()==1){
					daoOutput.selectAll();
				}else if(event.getSelectedItem()==2){
					dtoOutput.selectAll();
				}else if(event.getSelectedItem()==3){
					pmfOutput.selectAll();
				}
			}
		});
		root.add(tab);
		
		
		entityOutput = new TextArea();
		entityOutput.setReadOnly(true);
		entityOutput.setSize("600px","400px");
		tab.add(entityOutput,"Entity");
		//hpanel.add(output);
		tab.selectTab(0);
	
		
		daoOutput = new TextArea();
		daoOutput.setReadOnly(true);
		daoOutput.setSize("600px","400px");
		tab.add(daoOutput,"DAO");
		
		dtoOutput = new TextArea();
		dtoOutput.setReadOnly(true);
		dtoOutput.setSize("600px","400px");
		tab.add(dtoOutput,"DTO");
		
		pmfOutput = new TextArea();
		pmfOutput.setReadOnly(true);
		pmfOutput.setSize("600px","400px");
		tab.add(pmfOutput,"PMF");
		pmfOutput.setText(Bundles.INSTANCE.pmf().getText());
		
		RootPanel.get("container").add(root);
	}
	
	

	private HorizontalPanel createAddForm(){
		HorizontalPanel h=new HorizontalPanel();
		
		final TextBox formName=new TextBox();
		h.add(formName);
		formName.setText("value");
		
		final ValueListBox<String> typesForm=new ValueListBox<String>(new Renderer<String>() {

			@Override
			public String render(String object) {
				return object;
			}

			@Override
			public void render(String object, Appendable appendable)
					throws IOException {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		//TODO support Blob,list<Text> and list<Text>
		Collection<String> values=Arrays.asList("String","Long","Integer","Boolean","Text","List<String>","List<Integer>","List<Long>","List<Boolean>");
		typesForm.setValue("String");
		typesForm.setAcceptableValues(values);
		
		h.add(typesForm);
		Button bt=new Button("Add",new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				String line=formName.getText()+","+typesForm.getValue()+",";
				input.setText(input.getText()+line+"\n");
			}
		});
		h.add(bt);
		return h;
	}
	
	protected void doMake() {
		
		JDOBuilder.Settings settings=new JDOBuilder.Settings().className(classNameBox.getText())
				.detachable(detachableCheck.getValue())
				.serializable(serializableCheck.getValue())
				.longRunningTransaction(longRunningTransactionCheck.getValue());
		
		JDOBuilder builder=new JDOBuilder();
		List<FileNameAndText> files=builder.createJdoFilesByCsv(settings,input.getText());
		
		
		
		entityOutput.setText(files.get(0).getText());
		entityOutput.selectAll();
		
		daoOutput.setText(files.get(1).getText());
		
		dtoOutput.setText(files.get(2).getText());
		
		//TODO support PMF package
		
	}
	


	
}
