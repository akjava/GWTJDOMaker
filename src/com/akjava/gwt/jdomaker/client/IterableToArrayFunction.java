package com.akjava.gwt.jdomaker.client;

import java.util.Collection;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.ObjectArrays;

public class IterableToArrayFunction implements Function<Iterable<String>, String[]> {
/*
 * somehow guava original not work on gwt?
 * (non-Javadoc)
 * @see com.google.common.base.Function#apply(java.lang.Object)
 */
	@Override
	public String[] apply(Iterable<String> value) {
		Collection<String> collection =  Lists.newArrayList(value);
	    String[] array = ObjectArrays.newArray(new String[0],collection.size());
	    return collection.toArray(array);
	}

}
