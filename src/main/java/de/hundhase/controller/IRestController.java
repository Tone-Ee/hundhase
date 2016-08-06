package de.hundhase.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.google.common.base.CharMatcher;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.context.request.WebRequest;

import java.util.*;
import java.util.stream.Collectors;

public interface IRestController {

    default Optional<Pageable> getPageable(WebRequest webRequest) {
        if (getSize(webRequest).isPresent() && getPage(webRequest).isPresent()) {
            return Optional.of(new PageRequest(getPage(webRequest).get(), getSize(webRequest).get()));
        }
        return Optional.empty();
    }

    default Optional<Integer> getSize(WebRequest webRequest) {
        return getIntParameter("size", webRequest);
    }

    default Optional<Integer> getPage(WebRequest webRequest) {
        return getIntParameter("page", webRequest);
    }

    default Optional<String> getParameter(String parameter, WebRequest webRequest) {
        return Optional.ofNullable(webRequest.getParameter(parameter));
    }

    default Optional<Integer> getIntParameter(String parameter, WebRequest webRequest) {
        Optional<String> stringOptional = getParameter(parameter, webRequest);
        if (stringOptional.isPresent() && CharMatcher.JAVA_DIGIT.matchesAllOf(stringOptional.get())) {
            return Optional.of(Integer.valueOf(stringOptional.get()));
        }
        return Optional.empty();
    }

    default Optional<FilterProvider> getJSONFilter(WebRequest webRequest) {
        Optional<String> stringOptional = getParameter("select", webRequest);
        if (stringOptional.isPresent()) {
            Set<String> fields = new HashSet<>(Arrays.asList(stringOptional.get().split(",")));
            SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider();
            simpleFilterProvider.addFilter("selectFilter", SimpleBeanPropertyFilter.filterOutAllExcept(fields));
            return Optional.of(simpleFilterProvider);
        }
        return Optional.empty();
    }


    default <T> Map<String, Object> partializeObject(T entity, Optional<String> select) {
        Map<String, Object> beanMap = BeanMap.create(entity);
        if (select.isPresent()) {
            List<String> fields = Arrays.asList(select.get().split(","));
            Map<String, Object> body = beanMap.entrySet()
                    .stream()
                    .filter(e -> fields.contains(e.getKey()))
                    .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
            return body;
        }
        return beanMap;
    }

}
