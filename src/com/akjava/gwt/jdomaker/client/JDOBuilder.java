package com.akjava.gwt.jdomaker.client;

import java.util.ArrayList;
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
import com.google.gwt.user.client.Window;

public class JDOBuilder {
	private Map<String,String> e2mMap;
	private Map<String,String> m2eMap;
	
	public static class Settings{
		private String className;
		private boolean detachable;
		public boolean isDetachable() {
			return detachable;
		}
		public boolean isSerializable() {
			return serializable;
		}
		public boolean isLongRunningTransaction() {
			return longRunningTransaction;
		}
		private boolean serializable;
		private boolean longRunningTransaction;
		public String getClassName() {
			return className;
		}
		public Settings className(String className){
			this.className=className;
			return this;
		}
		
		public Settings detachable(boolean detachable){
			this.detachable=detachable;
			return this;
		}
		public Settings serializable(boolean serializable){
			this.serializable=serializable;
			return this;
		}
		public Settings longRunningTransaction(boolean longRunningTransaction){
			this.longRunningTransaction=longRunningTransaction;
			return this;
		}
		
	}
	public JDOBuilder(){

		
		e2mMap = new HashMap<String, String>();
		e2mMap.put("String", Bundles.INSTANCE.e2m_string().getText());
		e2mMap.put("Integer", Bundles.INSTANCE.e2m_int().getText());
		e2mMap.put("Long", Bundles.INSTANCE.e2m_long().getText());
		e2mMap.put("Boolean", Bundles.INSTANCE.e2m_boolean().getText());
		e2mMap.put("Text", Bundles.INSTANCE.e2m_text().getText());
		e2mMap.put("List<String>", Bundles.INSTANCE.e2m_list_string().getText());
		e2mMap.put("List<Integer>", Bundles.INSTANCE.e2m_list_int().getText());
		e2mMap.put("List<Long>", Bundles.INSTANCE.e2m_list_long().getText());
		e2mMap.put("List<Boolean>", Bundles.INSTANCE.e2m_list_boolean().getText());
		
		m2eMap = new HashMap<String, String>();
		m2eMap.put("String", Bundles.INSTANCE.m2e_string().getText());
		m2eMap.put("Integer", Bundles.INSTANCE.m2e_int().getText());
		m2eMap.put("Long", Bundles.INSTANCE.m2e_long().getText());
		m2eMap.put("Boolean", Bundles.INSTANCE.m2e_boolean().getText());
		m2eMap.put("Text", Bundles.INSTANCE.m2e_text().getText());
		m2eMap.put("List<String>", Bundles.INSTANCE.m2e_list_string().getText());
		m2eMap.put("List<Integer>", Bundles.INSTANCE.m2e_list_int().getText());
		m2eMap.put("List<Long>", Bundles.INSTANCE.m2e_list_long().getText());
		m2eMap.put("List<Boolean>", Bundles.INSTANCE.m2e_list_boolean().getText());
	}
	
	public List<FileNameAndText> createJdoFilesByCsv(Settings settings,String csvText){
		List<FileNameAndText> files=new ArrayList<FileNameAndText>();
		
		JDOClass jdoc=stringToJdoClass(settings,csvText);
		//create entity by jet.however it's too old way
		String text=new CodeMakeJETTemplate().generate(jdoc);
		files.add(new FileNameAndText(settings.getClassName(), text));
		
		//create dao by simple template
		String daobase=Bundles.INSTANCE.daobase().getText();
		Map<String,String> values=new HashMap<String, String>();
		values.put("class", jdoc.getName());
		
		files.add(new FileNameAndText(settings.getClassName()+"Dao", TemplateUtils.createText(daobase, values)));
		
		//create dto by simple temaplate & function
		String dtobase=Bundles.INSTANCE.dtobase().getText();
		String e2m=Joiner.on("\n").join(Lists.transform(jdoc.getJdoList(), new JdoValueToE2MTextFunction()));
		values.put("e2m", e2m);
		
		String m2e=Joiner.on("\n").join(Lists.transform(jdoc.getJdoList(), new JdoValueToM2ETextFunction()));
		values.put("m2e", m2e);
		
		files.add(new FileNameAndText(settings.getClassName()+"Dto", TemplateUtils.createText(dtobase, values)));
		
		return files;
	}
	
	protected JDOClass stringToJdoClass(Settings settings,String text){
		//List<String[]> datas=CSVUtils.parseCSV(text, ' ', 0);
		List<String[]> datas=parseCsvByGuava(text);
				
		JDOClass jdoclass=new JDOClass();
		jdoclass.setDetachable(settings.isDetachable());
		jdoclass.setSerializable(settings.isSerializable());
		jdoclass.setLongRunningTransaction(settings.isLongRunningTransaction());
		
		jdoclass.setName(settings.getClassName());
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
	public class JdoValueToE2MTextFunction implements Function<JDOValue,String>{
		@Override
		public String apply(JDOValue value) {
			Map<String,String> map=new HashMap<String, String>();
			map.put("key", value.getName());
			map.put("u+key", ValuesUtils.toUpperCamel(value.getName()));
			
			String template=e2mMap.get(value.getType());
			if(template==null){
				Window.alert("jdo type:"+value.getType()+" not found");
			}
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
}
