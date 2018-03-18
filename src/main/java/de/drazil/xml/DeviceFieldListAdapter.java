package de.drazil.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import lombok.Getter;
import de.drazil.homegear.bean.DeviceField;
import de.drazil.xml.DeviceFieldListAdapter.DeviceFieldListProvider;

public class DeviceFieldListAdapter extends
		List2MapXmlAdapter<DeviceFieldListProvider, DeviceField> {
	public static class DeviceFieldListProvider extends
			List2MapXmlAdapter.ListProvider<DeviceField> {
		@XmlElement(name = "field")
		@Getter
		public List<DeviceField> list = new ArrayList<DeviceField>();
	}

	@Override
	protected DeviceFieldListProvider createListProvider() {
		return new DeviceFieldListProvider();
	}

	@Override
	protected String getKey(DeviceField value) {
		return value.getId();
	}
}
