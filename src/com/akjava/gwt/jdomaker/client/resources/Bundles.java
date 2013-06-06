package com.akjava.gwt.jdomaker.client.resources;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface Bundles extends ClientBundle {
public static Bundles INSTANCE =GWT.create(Bundles.class);
	TextResource daobase();
	TextResource dtobase();
	TextResource pmf();
	
	TextResource e2m_string();
	TextResource e2m_int();
	TextResource e2m_long();
	TextResource e2m_boolean();
	TextResource e2m_list_string();
	TextResource e2m_list_int();
	TextResource e2m_list_long();
	TextResource e2m_list_boolean();
	TextResource m2e_string();
	TextResource m2e_int();
	TextResource m2e_long();
	TextResource m2e_boolean();
	TextResource m2e_list_string();
	TextResource m2e_list_int();
	TextResource m2e_list_long();
	TextResource m2e_list_boolean();
}
