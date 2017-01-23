package com.github.opaluchlukasz.junit2spock.core.model.method;

import org.eclipse.jdt.core.dom.MethodDeclaration;

import java.util.List;
import java.util.Optional;

import static com.github.opaluchlukasz.junit2spock.core.util.StringUtil.SEPARATOR;
import static com.github.opaluchlukasz.junit2spock.core.util.StringUtil.indent;
import static com.github.opaluchlukasz.junit2spock.core.util.StringUtil.indentation;
import static com.github.opaluchlukasz.junit2spock.core.util.TypeUtil.isVoid;
import static java.util.stream.Collectors.joining;

public abstract class MethodModel {

    final MethodDeclaration methodDeclaration;

    MethodModel(MethodDeclaration methodDeclaration) {
        this.methodDeclaration = methodDeclaration;
    }

    public String asGroovyMethod(int baseIndentationInTabs) {
        StringBuilder methodBuilder = methodDeclarationBuilder(baseIndentationInTabs);
        methodBuilder.append(" {");
        methodBuilder.append(SEPARATOR);

        methodBuilder.append(body().stream()
                .map(node -> indentation(baseIndentationInTabs + 1) + node.toString())
                .collect(joining(SEPARATOR)));

        indent(methodBuilder, baseIndentationInTabs).append("}");
        methodBuilder.append(SEPARATOR).append(SEPARATOR);

        return methodBuilder.toString();
    }

    public String methodDeclaration(int baseIndentationInTabs) {
        return methodDeclarationBuilder(baseIndentationInTabs).toString();
    }

    private StringBuilder methodDeclarationBuilder(int baseIndentationInTabs) {
        StringBuilder methodBuilder = new StringBuilder();
        new StringBuilder();

        indent(methodBuilder, baseIndentationInTabs).append(methodModifier());

        returnedType().ifPresent(type -> methodBuilder.append(type).append(" "));

        methodBuilder.append(getMethodName());
        methodBuilder.append(methodDeclaration.parameters().stream()
                .map(Object::toString)
                .collect(joining(", ", "(", ")")));
        return methodBuilder;
    }

    protected abstract String getMethodName();

    protected abstract List<Object> body();

    private Optional<String> returnedType() {
        return Optional.ofNullable(methodDeclaration.getReturnType2())
                .filter(type -> !isVoid(type))
                .map(Object::toString);
    }

    protected abstract String methodModifier();
}
