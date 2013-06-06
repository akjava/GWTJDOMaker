package com.akjava.gwt.jdomaker.client;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.akjava.gwt.jdomaker.client.resources.Bundles;
import com.akjava.gwt.jdomaker.client.templates.CodeMakeJETTemplate;
import com.akjava.lib.common.utils.TemplateUtils;
import com.akjava.lib.common.utils.ValuesUtils;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinderUtil.TempAttachment;
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
	private Map<String,String> e2mMap;
	private Map<String,String> m2eMap;
	private TextArea dtoOutput;

	
	/**
	 * This is the entry point method.
	 * 
	 */
	public void onModuleLoad() {
		
		
		
		e2mMap = new HashMap<String, String>();
		e2mMap.put("String", Bundles.INSTANCE.e2m_string().getText());
		e2mMap.put("Integer", Bundles.INSTANCE.e2m_int().getText());
		e2mMap.put("Long", Bundles.INSTANCE.e2m_long().getText());
		e2mMap.put("Boolean", Bundles.INSTANCE.e2m_boolean().getText());
		e2mMap.put("List<String>", Bundles.INSTANCE.e2m_list_string().getText());
		e2mMap.put("List<Integer>", Bundles.INSTANCE.e2m_list_int().getText());
		e2mMap.put("List<Long>", Bundles.INSTANCE.e2m_list_long().getText());
		e2mMap.put("List<Boolean>", Bundles.INSTANCE.e2m_list_boolean().getText());
		
		m2eMap = new HashMap<String, String>();
		m2eMap.put("String", Bundles.INSTANCE.m2e_string().getText());
		m2eMap.put("Integer", Bundles.INSTANCE.m2e_int().getText());
		m2eMap.put("Long", Bundles.INSTANCE.m2e_long().getText());
		m2eMap.put("Boolean", Bundles.INSTANCE.m2e_boolean().getText());
		m2eMap.put("List<String>", Bundles.INSTANCE.m2e_list_string().getText());
		m2eMap.put("List<Integer>", Bundles.INSTANCE.m2e_list_int().getText());
		m2eMap.put("List<Long>", Bundles.INSTANCE.m2e_list_long().getText());
		m2eMap.put("List<Boolean>", Bundles.INSTANCE.m2e_list_boolean().getText());
		
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
	
	public class JdoValueToE2MTextFunction implements Function<JDOValue,String>{
		@Override
		public String apply(JDOValue value) {
			Map<String,String> map=new HashMap<String, String>();
			map.put("key", value.getName());
			map.put("u+key", ValuesUtils.toUpperCamel(value.getName()));
			
			String template=e2mMap.get(value.getType());
			return TemplateUtils.createText(template, map);
		}
	}
	
	public class JdoValueToM2ETextFunction implements Function<JDOValue,String>{
		@Override
		public String apply(JDOValue value) {
			Map<String,String> map=new HashMap<String, String>();
			map.put("key", value.getName());
			map.put("u+key", ValuesUtils.toUpperCamel(value.getName()));
			
			String template=m2eMap.get(value.getType());
			return TemplateUtils.createText(template, map);
		}
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
		JDOClass jdoc=stringToJdoClass(classNameBox.getText(),input.getText());
		//create entity by jet.however it's too old way
		String text=new CodeMakeJETTemplate().generate(jdoc);
		entityOutput.setText(text);
		entityOutput.selectAll();
		
		//create dao by simple template
		String daobase=Bundles.INSTANCE.daobase().getText();
		Map<String,String> values=new HashMap<String, String>();
		values.put("class", jdoc.getName());
		
		daoOutput.setText(TemplateUtils.createText(daobase, values));
		
		//create dto by simple temaplate & function
		String dtobase=Bundles.INSTANCE.dtobase().getText();
		String e2m=Joiner.on("\n").join(Lists.transform(jdoc.getJdoList(), new JdoValueToE2MTextFunction()));
		values.put("e2m", e2m);
		
		String m2e=Joiner.on("\n").join(Lists.transform(jdoc.getJdoList(), new JdoValueToM2ETextFunction()));
		values.put("m2e", m2e);
		
		dtoOutput.setText(TemplateUtils.createText(dtobase, values));
		
	}
	
	protected JDOClass stringToJdoClass(String name,String text){
		//List<String[]> datas=CSVUtils.parseCSV(text, ' ', 0);
		List<String[]> datas=parseCsvByGuava(text);
				
		JDOClass jdoclass=new JDOClass();
		jdoclass.setDetachable(detachableCheck.getValue());
		jdoclass.setSerializable(serializableCheck.getValue());
		jdoclass.setLongRunningTransaction(longRunningTransactionCheck.getValue());
		
		jdoclass.setName(name);
		//boolean firstOne=false;
		for(String[] data:datas){//TODO change it
			if(data[0].isEmpty()){
				continue;
			}
			JDOValue value=new JDOValue(data);
			jdoclass.add(value);
		}
		return jdoclass;
	}
	
	private List<String[]> parseCsvByGuava(String text){
		 List<String> lines=ValuesUtils.toListLines(text);
		 List<List<String>> lists=Lists.transform(lines, new SplitLineFunction(true, true));
		 return Lists.transform(lists, new IterableToArrayFunction());
	}
	
}
