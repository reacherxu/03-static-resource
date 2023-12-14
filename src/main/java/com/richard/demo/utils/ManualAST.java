package com.richard.demo.utils;

import org.junit.Test;

import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import io.github.laplacedemon.light.expr.BaseExpression;
import io.github.laplacedemon.light.expr.parse.ParseExpressionException;
import io.github.laplacedemon.light.expr.parse.Parser;

/**
 * @author richard.xu03@sap.com
 * @version v 0.1 2021/6/9 1:20 PM richard.xu Exp $
 */
public class ManualAST {
    @Test
    public void test() throws ParseExpressionException {
        // 表达式" (order > 10) && (plant = SAP)"
        String expression = "x = a + b + c*d + sin(e) + max(e,f)";

        // 构建解析器
        Parser parser = new Parser(expression);

        // 开始解析，得到结果
        BaseExpression be = parser.parse();

        // 打印
        System.out.println(be);

    }

    private static class MethodVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(MethodDeclaration n, Void arg) {
            /* here you can access the attributes of the method.
             this method will be called for all methods in this
             CompilationUnit, including inner class methods */
            System.out.println("method:"+n.getName());
            super.visit(n, arg);
        }

        @Override
        public void visit(ClassOrInterfaceDeclaration n, Void arg) {
            System.out.println("class:"+n.getName());
            System.out.println("extends:"+n.getExtendedTypes());
            System.out.println("implements:"+n.getImplementedTypes());

            super.visit(n, arg);
        }

        @Override
        public void visit(PackageDeclaration n, Void arg) {
            System.out.println("package:"+n.getName());
            super.visit(n, arg);
        }
    }
}
