package pl.local.mpark.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;
import pl.local.mpark.helper.*;
import pl.local.mpark.service.AppUserRoleService;
import pl.local.mpark.service.BookingService;
import pl.local.mpark.service.PaymentService;
import pl.local.mpark.service.SubscriptionService;
import pl.local.mpark.utils.NotificationInterceptor;

import javax.annotation.Resource;
import java.util.Locale;

@Configuration
@EnableWebMvc
@ComponentScan("pl.local.mpark")
public class AppConfig implements WebMvcConfigurer {

    @Resource(name="paymentService")
    private PaymentService paymentService;
    @Resource(name="subscriptionService")
    private SubscriptionService subscriptionService;
    @Resource(name="bookingService")
    private BookingService bookingService;
    @Resource(name="appUserRoleService")
    private AppUserRoleService appUserRoleService;

    @Bean
    public TilesConfigurer tilesConfigurer() {
        TilesConfigurer tilesConfigurer = new TilesConfigurer();
        tilesConfigurer.setDefinitions("tilesConfiguration/tiles.xml");
        tilesConfigurer.setCheckRefresh(true);
        return tilesConfigurer;
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        TilesViewResolver viewResolver = new TilesViewResolver();
        registry.viewResolver(viewResolver);
    }

    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/views/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("/resources/i18n/site.locales");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver resolver = new CookieLocaleResolver();
        resolver.setDefaultLocale(new Locale("en"));
        resolver.setCookieName("localeCookie");
        resolver.setCookieMaxAge(4800);
        return resolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        var localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        registry.addInterceptor(localeChangeInterceptor);

        var notificationInterceptor = new NotificationInterceptor();
        registry.addInterceptor(notificationInterceptor);

    }

    @Bean
    @Override
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(getMyPaymentConverter());
        registry.addConverter(getMyPaymentListConverter());
        registry.addConverter(getMySubscriptionConverter());
        registry.addConverter(getMyBookingConverter());
        registry.addConverter(getMyBookingListConverter());
        registry.addConverter(getMyAppUserRolesConverter());
        registry.addConverter(getMyAppUserRolesListConverter());

    }

    @Bean
    public PaymentConverter getMyPaymentConverter() {
        return new PaymentConverter(this.paymentService);
    }

    @Bean
    public PaymentListConverter getMyPaymentListConverter() {
        return new PaymentListConverter(this.paymentService);
    }

    @Bean
    public SubscriptionConverter getMySubscriptionConverter() {
        return new SubscriptionConverter(this.subscriptionService);
    }

    @Bean
    public BookingConverter getMyBookingConverter() {
        return new BookingConverter(this.bookingService);
    }

    @Bean
    BookingListConverter getMyBookingListConverter() {
        return new BookingListConverter(this.bookingService);
    }

    @Bean
    public AppUserRolesConverter getMyAppUserRolesConverter() {
        return new AppUserRolesConverter(this.appUserRoleService);
    }

    @Bean
    public AppUserRolesListConverter getMyAppUserRolesListConverter() {
        return new AppUserRolesListConverter(this.appUserRoleService);
    }
}
