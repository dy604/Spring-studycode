import static org.junit.Assert.*;

import bean.MyTestBean;

@SuppressWarnings("deprecation")
public class BeanFactoryTest {

	@Test
	public void testSimpleLoad() {
		
		BeanFactory bf = new XmlBeanFactory(new ClassPathResource("beanFactoryTest.xml"));
		MyTestBean bean = (MyTestBean) bf.getBean("myTestBean");
		assertEquals("testStr", bean.getTestStr());
	}
}
