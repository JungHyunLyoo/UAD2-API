package com.uad2.application.api.document.utils;

import org.springframework.restdocs.snippet.Attributes;

import static org.springframework.restdocs.snippet.Attributes.key;

/*
 * @USER Jongyeob
 * @DATE 2019-09-10
 */
public interface DocumentFormatGenerator {

    static Attributes.Attribute getDateTimeFormat() {
        return key("format").value("yyyy-MM-dd hh:mm:ss");
    }

    static Attributes.Attribute getDateFormat() {
        return key("format").value("yyyy-MM-dd");
    }

}
