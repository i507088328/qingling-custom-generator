package com.ls.qdp;

import java.util.*;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;

/**
 * @author:lishuai
 * @date:2020/1/10 9:56 下午
 * @description: 自定义生成mapper接口方法
 * @modifyTime:
 * @modifyDescription:
 */
public class CustomJavaMapperMethodGenerator extends AbstractJavaMapperMethodGenerator {

    @Override
    public void addInterfaceElements(Interface iInterface) {
        addMapperAnnotations(iInterface);
        addInterfaceGet(iInterface);
        addInterfaceAdd(iInterface);
        addInterfaceLogicDelete(iInterface);
        addInterfaceLogicBatchDelete(iInterface);
        addInterfacePhysicalDelete(iInterface);
        addInterfaceUpdate(iInterface);
        addInterfaceLoadPageByMap(iInterface);
    }

    /**
     * @author:lishuai
     * @date:2020/1/10 8:29 上午
     * @description: 添加get接口
     * @modifyTime:
     * @modifyDescription:
     */
    private void addInterfaceGet(Interface iInterface) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        FullyQualifiedJavaType returnType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        importedTypes.add(returnType);
        method.setReturnType(returnType);
        method.setName("get");
        method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "id"));
        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, iInterface, introspectedTable)) {
            iInterface.addImportedTypes(importedTypes);
            iInterface.addMethod(method);
        }
    }

    /**
     * @author:lishuai
     * @date:2020/1/10 8:29 上午
     * @description: 添加add接口
     * @modifyTime:
     * @modifyDescription:
     */
    private void addInterfaceAdd(Interface iInterface) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("Integer"));
        method.setName("add");
        FullyQualifiedJavaType parameterType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        method.addParameter(new Parameter(parameterType, "record"));
        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, iInterface, introspectedTable)) {
            iInterface.addImportedTypes(importedTypes);
            iInterface.addMethod(method);
        }
    }

    /**
     * @author:lishuai
     * @date:2020/1/10 8:29 上午
     * @description: 添加logicDelete接口
     * @modifyTime:
     * @modifyDescription:
     */
    private void addInterfaceLogicDelete(Interface iInterface) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("Integer"));
        method.setName("logicDelete");
        method.addParameter(new Parameter(new FullyQualifiedJavaType("String"), "id"));
        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, iInterface, introspectedTable)) {
            iInterface.addImportedTypes(importedTypes);
            iInterface.addMethod(method);
        }
    }

    /**
     * @author:lishuai
     * @date:2020/1/10 8:29 上午
     * @description: 添加logicBatchDelete接口
     * @modifyTime:
     * @modifyDescription:
     */
    private void addInterfaceLogicBatchDelete(Interface iInterface) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("Integer"));
        method.setName("logicBatchDelete");
        method.addParameter(new Parameter(new FullyQualifiedJavaType("String[]"), "array"));
        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, iInterface, introspectedTable)) {
            iInterface.addImportedTypes(importedTypes);
            iInterface.addMethod(method);
        }
    }

    /**
     * @author:lishuai
     * @date:2020/1/10 8:29 上午
     * @description: 添加physicalDelete接口
     * @modifyTime:
     * @modifyDescription:
     */
    private void addInterfacePhysicalDelete(Interface iInterface) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("Integer"));
        method.setName("physicalDelete");
        method.addParameter(new Parameter(new FullyQualifiedJavaType("String"), "id"));
        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, iInterface, introspectedTable)) {
            iInterface.addImportedTypes(importedTypes);
            iInterface.addMethod(method);
        }
    }

    /**
     * @author:lishuai
     * @date:2020/1/10 8:29 上午
     * @description: 添加 update 接口
     * @modifyTime:
     * @modifyDescription:
     */
    private void addInterfaceUpdate(Interface iInterface) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("Integer"));
        method.setName("update");
        FullyQualifiedJavaType parameterType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        method.addParameter(new Parameter(parameterType, "record"));
        importedTypes.add(parameterType);
        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, iInterface, introspectedTable)) {
            iInterface.addImportedTypes(importedTypes);
            iInterface.addMethod(method);
        }
    }

    /**
     * @author:lishuai
     * @date:2020/1/10 8:29 上午
     * @description: 添加 loadPageByMap 接口
     * @modifyTime:
     * @modifyDescription:
     */
    private void addInterfaceLoadPageByMap(Interface iInterface) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        importedTypes.add(new FullyQualifiedJavaType("com.github.pagehelper.Page"));
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("Page");
        FullyQualifiedJavaType parameterType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        returnType.addTypeArgument(parameterType);
        method.setReturnType(returnType);
        method.setName("loadPageByMap");
        method.addParameter(new Parameter(parameterType, "record"));
        importedTypes.add(returnType);
        importedTypes.add(parameterType);
        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, iInterface, introspectedTable)) {
            iInterface.addImportedTypes(importedTypes);
            iInterface.addMethod(method);
        }
    }

    public void addMapperAnnotations(Interface iInterface) {
        //iInterface.addAnnotation("@Mapper");
    }
}
