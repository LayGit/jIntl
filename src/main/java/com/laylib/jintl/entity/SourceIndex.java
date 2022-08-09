package com.laylib.jintl.entity;

import java.util.*;

/**
 * Source Index
 *
 * @author Lay
 */
public class SourceIndex {

    public SourceIndex() {
        this.items = new ArrayList<>();
    }

    private List<IndexItem> items;

    public List<IndexItem> getItems() {
        return items;
    }

    public void setItems(List<IndexItem> items) {
        this.items = items;
    }

    public static class IndexItem {
        private String tag;
        private Set<Locale> locales;

        public IndexItem() {
            this.locales = new HashSet<>();
        }

        public IndexItem(String tag, Set<Locale> locales) {
            this.tag = tag;
            this.locales = locales;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public Set<Locale> getLocales() {
            return locales;
        }

        public void setLocales(Set<Locale> locales) {
            this.locales = locales;
        }
    }

    public static class SingleIndexItem {
        private String tag;
        private Locale locale;

        public SingleIndexItem(String tag, Locale locale) {
            this.tag = tag;
            this.locale = locale;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public Locale getLocale() {
            return locale;
        }

        public void setLocale(Locale locale) {
            this.locale = locale;
        }
    }
}
