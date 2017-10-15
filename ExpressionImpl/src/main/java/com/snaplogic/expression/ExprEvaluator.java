/*
 * SnapLogic - Data Integration
 *
 * Copyright (C) 2016, SnapLogic, Inc.  All rights reserved.
 *
 * This program is licensed under the terms of
 * the SnapLogic Commercial Subscription agreement.
 *
 * "SnapLogic" is a trademark of SnapLogic, Inc.
 */

package com.snaplogic.expression;

import com.snaplogic.expression.util.JaninoUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.codehaus.commons.compiler.CompileException;
import org.codehaus.commons.compiler.Location;
import org.codehaus.janino.Java;
import org.codehaus.janino.Mod;
import org.codehaus.janino.ScriptEvaluator;

import java.util.List;

import static com.snaplogic.expression.JaninoStringGeneratorVisitor.BLANK_LOC;
import static com.snaplogic.expression.util.JaninoUtils.modifiers;
import static com.snaplogic.expression.util.JaninoUtils.newType;

/**
 * This class is a bit of a hack to add static variables to the class.  Unfortunately, janino
 * does not allow us to just add statics in a script.
 */
public class ExprEvaluator extends ScriptEvaluator {
    protected List<Pair<Java.Type, Java.VariableDeclarator[]>> constants;
    protected Java.Block toStringBlock;

    /**
     * @param constants The list of constants that should be added to the generated class.
     */
    public void setConstants(List<Pair<Java.Type, Java.VariableDeclarator[]>> constants) {
        this.constants = constants;
    }

    /**
     * @param block The block to use for the toString() method on the generated class.
     */
    public void setToStringBlock(Java.Block block) {
        this.toStringBlock = block;
    }

    protected Java.PackageMemberClassDeclaration addPackageMemberClassDeclaration(Location
            location, Java.CompilationUnit compilationUnit) throws CompileException {
        Java.PackageMemberClassDeclaration retval = super.addPackageMemberClassDeclaration(
                location, compilationUnit);

        for (Pair<Java.Type, Java.VariableDeclarator[]> constPair : constants) {
            Java.FieldDeclaration decl = new Java.FieldDeclaration(BLANK_LOC, null,
                    modifiers(Mod.FINAL, Mod.STATIC), constPair.getLeft(),
                    constPair.getRight());

            retval.addFieldDeclaration(decl);
        }

        if (toStringBlock != null) {
            retval.addDeclaredMethod(JaninoUtils.declareMethod(newType(String.class), "toString",
                    new Java.FunctionDeclarator.FormalParameter[0], toStringBlock));
        }

        return retval;
    }
}
