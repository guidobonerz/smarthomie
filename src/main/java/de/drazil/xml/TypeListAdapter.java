package de.drazil.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import lombok.Getter;
import de.drazil.homegear.bean.Type;
import de.drazil.xml.TypeListAdapter.TypeListProvider;

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
