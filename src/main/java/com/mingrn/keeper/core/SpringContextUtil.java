package com.mingrn.keeper.core;

/*import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;*/

/**
 * 获取Spring容器中bean工具类
 */
//@Component
public class SpringContextUtil /*implements ApplicationContextAware*/ {
	/*private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (SpringContextUtil.applicationContext == null) {
			SpringContextUtil.applicationContext = applicationContext;
		}
	}


	*//**
	 * @throws
	 * @Title: getApplicationContext
	 * @Description: 获取applicationContext
	 * @return: ApplicationContext
	 *//*
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}


	*//**
	 * @throws
	 * @Title: getBean
	 * @Description: 通过name获取 Bean.
	 * @param: @param name
	 * @param: @return
	 * @return: Object
	 *//*
	public static Object getBean(String name) {
		return getApplicationContext().getBean(name);
	}


	*//**
	 * @throws
	 * @Title: getBean
	 * @Description: 通过class获取Bean.
	 * @param: @param clazz
	 * @param: @return
	 * @return: T
	 *//*
	public static <T> T getBean(Class<T> clazz) {
		return getApplicationContext().getBean(clazz);
	}


	*//**
	 * @throws
	 * @Title: getBean
	 * @Description: 通过name, 以及Clazz返回指定的Bean
	 * @param: @param name
	 * @param: @param clazz
	 * @param: @return
	 * @return: T
	 *//*
	public static <T> T getBean(String name, Class<T> clazz) {
		return getApplicationContext().getBean(name, clazz);
	}
*/
}
