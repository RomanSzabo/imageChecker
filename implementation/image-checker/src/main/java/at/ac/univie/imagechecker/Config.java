package at.ac.univie.imagechecker;

import at.ac.univie.imagechecker.handlers.CheckImageHandler;
import at.ac.univie.imagechecker.handlers.EthereumHandler;
import at.ac.univie.imagechecker.handlers.MessageHandler;
import at.ac.univie.imagechecker.handlers.RegisterImageHandler;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;

@Configuration
public class Config {

    @Bean
    public RegisterImageHandler registerImageHandler() {
        return new RegisterImageHandler();
    }

    @Bean
    public EthereumHandler ethereumHandler() {
        return new EthereumHandler();
    }

    @Bean
    public CheckImageHandler checkImageHandler() {
        return new CheckImageHandler();
    }

    @Bean
    public MessageHandler messageHandler() {
        return new MessageHandler();
    }

    // EMAIL
    @Bean
    public SpringResourceTemplateResolver thymeleafTemplateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setPrefix("classpath:/templates/mail/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine thymeleafTemplateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(thymeleafTemplateResolver());
        return templateEngine;
    }

    //EMAIL

}
