package com.github.opaluchlukasz.junit2spock.core.groovism;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.regex.Pattern.quote;

public class NoClassKeyword extends Groovism {

    NoClassKeyword() {
        super(empty());
    }

    NoClassKeyword(Groovism next) {
        super(Optional.of(next));
    }

    @Override
    protected String applyGroovism(String line) {
        return line.replaceAll(quote(".class"), "");
    }
}
