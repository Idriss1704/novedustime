package bf.novedus.novedustime.config;
//package bf.novedus.novedustime.config;
//
//import java.util.Properties;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.core.env.Environment;
//
//@Configuration
//@PropertySource("classpath:testmail.properties")
//public class MailConfig {
//
//	@Autowired
//	private Environment env;
//
//	@Bean
//	public JavaMailSender javaMailService() {
//		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
//
//		javaMailSender.setHost(env.getProperty("mail.host"));
//		javaMailSender.setPort(Integer.parseInt(env.getProperty("mail.port")));
//
//		javaMailSender.setJavaMailProperties(getMailProperties());
//
//		return javaMailSender;
//	}
//
//	private Properties getMailProperties() {
//		Properties properties = new Properties();
//		properties.setProperty("mail.transport.protocol", "smtp");
//		properties.setProperty("mail.smtp.auth", "false");
//		properties.setProperty("mail.smtp.starttls.enable", "false");
//		properties.setProperty("mail.debug", "false");
//		return properties;
//	}
//}
