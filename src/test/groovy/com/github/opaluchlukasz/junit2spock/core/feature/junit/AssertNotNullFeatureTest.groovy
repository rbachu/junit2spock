package com.github.opaluchlukasz.junit2spock.core.feature.junit

import com.github.opaluchlukasz.junit2spock.core.ASTNodeFactory
import org.eclipse.jdt.core.dom.AST
import org.eclipse.jdt.core.dom.InfixExpression
import org.eclipse.jdt.core.dom.MethodInvocation
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

import static com.github.opaluchlukasz.junit2spock.core.feature.junit.AssertNotNullFeature.ASSERT_NOT_NULL
import static org.eclipse.jdt.core.dom.AST.*

class AssertNotNullFeatureTest extends Specification {

    private static final AST ast = newAST(JLS8)
    @Shared private ASTNodeFactory nodeFactory = new ASTNodeFactory({
        get: ast
    })

    @Subject AssertNotNullFeature assertNotNullFeature = new AssertNotNullFeature(nodeFactory)

    def 'should return false for non assertNotNull method invocation'() {
        expect:
        !assertNotNullFeature.applicable(node).isPresent()

        where:
        node << [new Object(), nodeFactory.methodInvocation('someMethod', [])]
    }

    def 'should return true for assertNotNull method invocation'() {
        given:
        MethodInvocation methodInvocation = nodeFactory.methodInvocation(ASSERT_NOT_NULL,
                [nodeFactory.numberLiteral('0')])

        expect:
        assertNotNullFeature.applicable(methodInvocation).isPresent()
    }

    def 'should return Spock\' expression for proper assertNotNull method invocation'() {
        when:
        InfixExpression expression = assertNotNullFeature.apply(methodInvocation)

        then:
        expression.toString() == '0 != null'

        where:
        methodInvocation << [nodeFactory.methodInvocation(ASSERT_NOT_NULL, [nodeFactory.numberLiteral('0')]),
                             nodeFactory.methodInvocation(ASSERT_NOT_NULL, [nodeFactory.stringLiteral('equal to null'),
                                                                            nodeFactory.numberLiteral('0')])]
    }

    def 'should throw an exception for incorrect assertNotNull method invocation'() {
        MethodInvocation methodInvocation = nodeFactory.methodInvocation(ASSERT_NOT_NULL,
                [nodeFactory.numberLiteral('0'), nodeFactory.numberLiteral('0'), nodeFactory.numberLiteral('0')])


        when:
        assertNotNullFeature.apply(methodInvocation)

        then:
        UnsupportedOperationException ex = thrown()
        ex.message == 'Supported only 1-, 2-arity assertNotNull invocation'
    }
}
