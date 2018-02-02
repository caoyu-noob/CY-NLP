package com.config;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.jena.rdf.model.ModelCon;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.constants.ModelConstant;
import com.service.AnswerService;
import com.service.FileAndDatasetNameService;
import com.service.FileReader;
import com.service.SearchService;

/**
 * Created by cao_y on 2018/1/27.
 */
@Configuration
@ComponentScan("com.controller")
@EnableCaching
@EnableWebMvc
public class QAFrontConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/bootstrap/**").addResourceLocations("/WEB-INF/bootstrap/");
        registry.addResourceHandler("/images/**").addResourceLocations("/WEB-INF/images/");
        registry.addResourceHandler("/js/**").addResourceLocations("/WEB-INF/js/");
        registry.addResourceHandler("/css/**").addResourceLocations("/WEB-INF/css/");
    }

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(new ConcurrentMapCache("item")));
        return cacheManager;
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public InternalResourceViewResolver jspViewResolver() {
        InternalResourceViewResolver bean = new InternalResourceViewResolver();
        bean.setPrefix("/WEB-INF/jsp/");
        return bean;
    }

    @Bean
    public AnswerService answerService() throws IOException{
        return new AnswerService(ModelConstant.WEB_MODE);
    }

    @Bean
    public SearchService searchService() {
        return new SearchService(ModelConstant.WEB_MODE);
    }

    @Bean
    public FileAndDatasetNameService fileAndDatasetNameService() {
        return new FileAndDatasetNameService();
    }

    @Bean
    public FileReader fileReader() {
        Map<String, String> datasetNames = fileAndDatasetNameService().getDatasetNamesMap(ModelConstant.WEB_MODE);
        Map<String, String> fileNames = fileAndDatasetNameService().getFileNamesMap(ModelConstant.WEB_MODE);
//        fileNames.put(ModelConstant.ModelNames.SANGUO_EVENT.get(), ModelConstant.FileNames.EVENT.get());
//        fileNames.put(ModelConstant.ModelNames.SANGUO_FIGURE.get(), ModelConstant.FileNames.FIGURE.get());
//        fileNames.put(ModelConstant.ModelNames.SANGUO_LOCATION.get(), ModelConstant.FileNames.LOCATION.get());
//        fileNames.put(ModelConstant.ModelNames.SANGUO_TIME.get(), ModelConstant.FileNames.TIME.get());
//        Map<String, String> datasetNames = new HashMap<>();
//        datasetNames.put(ModelConstant.ModelNames.SANGUO_EVENT.get(), ModelConstant.DatasetNames.SANGUO_EVENT.get());
//        datasetNames.put(ModelConstant.ModelNames.SANGUO_FIGURE.get(), ModelConstant.DatasetNames.SANGUO_FIGURE.get());
//        datasetNames.put(ModelConstant.ModelNames.SANGUO_LOCATION.get(), ModelConstant.DatasetNames.SANGUO_LOCATION.get());
//        datasetNames.put(ModelConstant.ModelNames.SANGUO_TIME.get(), ModelConstant.DatasetNames.SANGUO_TIME.get());
        return new FileReader(fileNames, datasetNames);
    }

}
