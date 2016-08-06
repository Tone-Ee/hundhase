package de.hundhase.util;

import com.ecwid.consul.v1.catalog.model.CatalogService;

import java.net.MalformedURLException;
import java.net.URL;

public class ConsulUtil {

    public static URL getURL(String protocol, String host, int port, String path) throws MalformedURLException {
        return new URL(protocol, host, port, path);
    }

    public static URL getURL(CatalogService catalogService, String path) throws MalformedURLException {
        return getURL("http", catalogService.getServiceAddress(), catalogService.getServicePort(), path);
    }


    public static URL getURLUnchecked(String protocol, String host, int port, String path) {
        try {
            return new URL(protocol, host, port, path);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static URL getURLUnchecked(CatalogService catalogService, String path) {
        return getURLUnchecked("http", catalogService.getServiceAddress(), catalogService.getServicePort(), path);
    }

}
