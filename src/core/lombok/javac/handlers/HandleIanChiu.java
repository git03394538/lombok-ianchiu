package lombok.javac.handlers;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import lombok.IanChiu;
import lombok.core.AnnotationValues;
import lombok.javac.Javac;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;
import org.mangosdk.spi.ProviderFor;

import java.lang.reflect.Modifier;

import static lombok.javac.Javac.*;
import static lombok.javac.handlers.JavacHandlerUtil.*;

/**
 * Created by ian on 7/2/16.
 */
@ProviderFor(JavacAnnotationHandler.class)
@SuppressWarnings("restriction")
public class HandleIanChiu extends JavacAnnotationHandler<IanChiu> {

    @Override
    public void handle(AnnotationValues<IanChiu> annotation, JCAnnotation ast, JavacNode annotationNode) {
        JavacHandlerUtil.deleteAnnotationIfNeccessary(annotationNode, IanChiu.class);
        deleteImportFromCompilationUnit(annotationNode, "lombok.AccessLevel");
        JavacNode typeNode = annotationNode.up();

        JavacHandlerUtil.injectMethod(typeNode, createHelloWorld(typeNode));
    }

    private JCMethodDecl createHelloWorld(JavacNode type) {
        //This is the artist/ bad guy
        JavacTreeMaker treeMaker = type.getTreeMaker();

        //Define public
        JCModifiers modifiers          = treeMaker.Modifiers(Modifier.PUBLIC);
        //Define Generic type
        List<JCTree.JCTypeParameter> methodGenericTypes = List.<JCTypeParameter>nil();
        //Define return type
        JCExpression methodType         = treeMaker.Type(Javac.createVoidType(treeMaker, CTC_VOID));
        //Define method name
        Name methodName         = type.toName("helloWorld");
        //Define parameter
        List<JCTree.JCVariableDecl>  methodParameters   = List.<JCVariableDecl>nil();
        //Define throw
        List<JCTree.JCExpression>    methodThrows       = List.<JCExpression>nil();

        //Write our system.out.prinln("hello world") method
        JCExpression printlnMethod = JavacHandlerUtil.chainDots(type, "System", "out", "println");
        List<JCTree.JCExpression> printlnArgs = List.<JCExpression>of(treeMaker.Literal("hello world"));
        JCMethodInvocation printlnInvocation = treeMaker.Apply(List.<JCExpression>nil(), printlnMethod, printlnArgs);
        //Define method
        JCBlock methodBody = treeMaker.Block(0, List.<JCStatement>of(treeMaker.Exec(printlnInvocation)));
        //Default value
        JCExpression defaultValue = null;

        return treeMaker.MethodDef(
                modifiers,
                methodName,
                methodType,
                methodGenericTypes,
                methodParameters,
                methodThrows,
                methodBody,
                defaultValue
        );
    }
}
