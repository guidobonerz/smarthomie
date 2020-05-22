package de.drazil.homeautomation.xml;

import java.io.File;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class XmlHandler
{
	@SuppressWarnings("unchecked")
	public <BEAN> BEAN readFromXml(final Class<? super BEAN> beanClass, final String fileName) throws Exception {
		BEAN bean = null;
		final JAXBContext jaxbContext = JAXBContext.newInstance(beanClass);
		final Unmarshaller jaxbMarschaller = jaxbContext.createUnmarshaller();
		final InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
		bean = (BEAN) jaxbMarschaller.unmarshal(is);
		return bean;
	}

	public <BEAN> void writeToXml(final Class<? super BEAN> beanClass, final BEAN bean, final String fileName)
			throws Exception {
		final JAXBContext jaxbContext = JAXBContext.newInstance(beanClass);
		final Marshaller jaxbMarschaller = jaxbContext.createMarshaller();
		jaxbMarschaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		jaxbMarschaller.marshal(bean, new File(""));
	}
}
