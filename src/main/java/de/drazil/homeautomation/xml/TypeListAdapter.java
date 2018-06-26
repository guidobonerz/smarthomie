package de.drazil.homeautomation.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import lombok.Getter;
import de.drazil.homeautomation.bean.Type;
import de.drazil.homeautomation.xml.TypeListAdapter.TypeListProvider;

public class TypeListAdapter extends List2MapXmlAdapter<TypeListProvider, Type> {
	public static class TypeListProvider extends
			List2MapXmlAdapter.ListProvider<Type> {
		@XmlElement(name = "type")
		@Getter
		public List<Type> list = new ArrayList<Type>();
	}

	@Override
	protected TypeListProvider createListProvider() {
		return new TypeListProvider();
	}

	@Override
	protected String getKey(Type value) {
		return value.getId();
	}
}
