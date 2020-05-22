package de.drazil.homeautomation.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import lombok.Getter;
import de.drazil.homeautomation.bean.Device;
import de.drazil.homeautomation.xml.DeviceListAdapter.DeviceListProvider;

public class DeviceListAdapter extends
		List2MapXmlAdapter<DeviceListProvider, Device> {
	public static class DeviceListProvider extends
			List2MapXmlAdapter.ListProvider<Device> {
		@XmlElement(name = "device")
		@Getter
		public List<Device> list = new ArrayList<Device>();
	}

	@Override
	protected DeviceListProvider createListProvider() {
		return new DeviceListProvider();
	}

	@Override
	protected String getKey(final Device value) {
		return value.getId();
	}
}
