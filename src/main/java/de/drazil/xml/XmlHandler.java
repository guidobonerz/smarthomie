package de.drazil.xml;

import java.io.File;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class XmlHandler
{
	@SuppressWarnings("unchecked")
	public <BEAN> BEAN readFromXml(Class<? super BEAN> beanClass, String fileName) throws Exception
	{
		BEAN bean = null;
		JAXBContext jaxbContext = JAXBContext.newInstance(beanClass);
		Unmarshaller jaxbMarschaller = jaxbContext.createUnmarshaller();
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
		bean = (BEAN) jaxbMarschaller.unmarshal(is);
		return bean;
	}

	public <BEAN> void writeToXml(Class<? super BEAN> beanClass, BEAN bean, String fileName) throws Exception
	{
		JAXBContext jaxbContext = JAXBContext.newInstance(beanClass);
		Marshaller jaxbMarschaller = jaxbContext.createMarshaller();
		jaxbMarschaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		jaxbMarschaller.marshal(bean, new File(""));
	}
}
