package bf.novedus.novedustime.config;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import bf.novedus.novedustime.util.Util;

/**
 * MVC Config class
 * 
 * @author Idrissa Ganemtore
 *
 */

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	public static String extDirectory = System.getProperty("user.dir")
			+ "\\src\\main\\resources\\static\\webresources\\extResources";

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		if (new File(Util.EXT_IMG_RESOURCES_DIR).exists() == false) {
			new File(Util.EXT_IMG_RESOURCES_DIR).mkdirs();
			try {
				FileUtils.copyDirectory(new File(extDirectory), new File(Util.EXT_IMG_RESOURCES_DIR));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		registry.addResourceHandler("/images/**").addResourceLocations("file:" + Util.EXT_IMG_RESOURCES_DIR + "\\");
	}

	@Bean
	public LocaleResolver localeResolver() {
		CookieLocaleResolver clr = new CookieLocaleResolver();
		clr.setCookieName("NovedusTimeLanguage");
		clr.setCookieMaxAge(24*7*60*60);
		return clr;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("lang");
		return lci;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}

}
