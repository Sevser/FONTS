package ru.pl.projects.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.telegram.telegrambots.ApiContext;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.generics.LongPollingBot;
import ru.pl.projects.telegram.Bot;

import javax.sql.DataSource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

@Configuration
@Slf4j
public class AppConfig {

    @Bean
    public DataSource dataSource(@Value("${database.url}") String url,
                                 @Value("${database.username}") String username,
                                 @Value("${database.password}") String password,
                                 @Value("${database.driverClassName}") String driverName) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverName);
        return dataSource;
    }

    @Bean
    @Profile("ssl")
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors(redirectConnector());
        return tomcat;
    }

    private Connector redirectConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(8080);    // if requires comes from 8080 then redurect to 8443
        connector.setSecure(false);
        connector.setScheme("https");
        connector.setRedirectPort(8443);   // application will run on 8443
        return connector;
    }

    @Bean
    public Bot telegramBotsApi(@Value("${telegram.bot.name}") String name,
                               @Value("${telegram.bot.token}") String token,
                               @Value("${telegram.proxy.host}") String proxyHost,
                               @Value("${telegram.proxy.port}") int proxyPort) {

        try {
            Path path = Paths.get(ClassLoader.getSystemResource("proxy.csv").toURI());
            try {
                List<String> list = Files.readAllLines(path);

                Iterator<String> iterator = list.iterator();
                while (iterator.hasNext()) {
                    String next = iterator.next();
                    String[] split = next.split(":");
//                    proxyHost = split[0];
                    proxyHost = "d2a5e5.reconnect.rocks";
//                    proxyPort = Integer.valueOf(split[1]);
                    proxyPort = 1080;
                    try {
                        System.getProperties().put("proxySet", "true");
                        System.getProperties().put("socksProxyHost", proxyHost);
                        System.getProperties().put("socksProxyPort", proxyPort);

                        ApiContextInitializer.init();

                        TelegramBotsApi botsApi = new TelegramBotsApi();

                        DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);

                        CredentialsProvider credsProvider = new BasicCredentialsProvider();
                        credsProvider.setCredentials(
                                new AuthScope(proxyHost, proxyPort),
                                new UsernamePasswordCredentials("3559738", "11a1fcc2"));

                        HttpHost httpHost = new HttpHost(proxyHost, proxyPort);

                        RequestConfig requestConfig = RequestConfig.custom().setProxy(httpHost).setAuthenticationEnabled(true).build();
                        botOptions.setRequestConfig(requestConfig);
                        botOptions.setCredentialsProvider(credsProvider);
                        botOptions.setHttpProxy(httpHost);

                        // Register your newly created AbilityBot
                        Bot bot = new Bot(botOptions, name, token);
                        botsApi.registerBot(bot);
                        return bot;
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
