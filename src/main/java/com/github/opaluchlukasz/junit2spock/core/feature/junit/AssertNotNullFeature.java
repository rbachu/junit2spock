package com.github.opaluchlukasz.junit2spock.core.feature.junit;

import com.github.opaluchlukasz.junit2spock.core.ASTNodeFactory;
import com.github.opaluchlukasz.junit2spock.core.feature.Feature;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;

import java.util.List;
import java.util.Optional;

import static com.github.opaluchlukasz.junit2spock.core.util.AstNodeFinder.methodInvocation;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.NOT_EQUALS;

public class AssertNotNullFeature extends Feature<MethodInvocation> {

    public static final String ASSERT_NOT_NULL = "assertNotNull";

    private final ASTNodeFactory astNodeFactory;

    public AssertNotNullFeature(ASTNodeFactory astNodeFactory) {
        this.astNodeFactory = astNodeFactory;
    }

    @Override
    public Optional<MethodInvocation> applicable(Object astNode) {
        return methodInvocation(astNode, ASSERT_NOT_NULL);
    }

    @Override
    public InfixExpression apply(Object object, MethodInvocation methodInvocation) {
        List arguments = methodInvocation.arguments();
        if (arguments.size() == 1) {
            return astNodeFactory.infixExpression(NOT_EQUALS,
                    argumentAsExpression(arguments.get(0)),
                    astNodeFactory.nullLiteral());
        }
        if (arguments.size() == 2) {
            return astNodeFactory.infixExpression(NOT_EQUALS,
                    argumentAsExpression(arguments.get(1)),
                    astNodeFactory.nullLiteral());
        }
        throw new UnsupportedOperationException("Supported only 1-, 2-arity assertNotNull invocation");
    }

    private Expression argumentAsExpression(Object argument) {
        return argument instanceof Expression ? astNodeFactory.clone((Expression) argument) :
                astNodeFactory.simpleName(argument.toString());
    }
}
