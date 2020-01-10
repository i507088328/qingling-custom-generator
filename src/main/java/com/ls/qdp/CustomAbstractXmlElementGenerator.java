package com.ls.qdp;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import java.util.List;

import static com.ls.qdp.Constants.*;

/**
 * @author:lishuai
 * @date:2020/1/9 3:25 下午
 * @description: 自定义xml生成策略修改
 * @modifyTime:
 * @modifyDescription:
 */
public class CustomAbstractXmlElementGenerator extends AbstractXmlElementGenerator {

    /*实体类路径*/
    private String beanPath;

    /*表名*/
    private String tableName;

    /*实体类约定约定名称*/
    private String sqlBeanName;

    /*table别名*/
    private String tableAlias;

    /*表所有列*/
    private List<IntrospectedColumn> introspectedColumns;

    /*列大小*/
    private Integer columnSize;

    /*if标签的sql id*/
    private String ifSqlId;

    /*公共sqlBean include*/
    private XmlElement include = new XmlElement("include");

    /*公共if条件 include*/
    private XmlElement ifInclude = new XmlElement("include");

    /*公共FROM 带别名*/
    private TextElement fromTextElement;

    @Override
    public void addElements(XmlElement parentElement) {
        //清空原生的element
        parentElement.getElements().clear();
        init();
        enter(parentElement);
        addSqlBeanElement(parentElement);
        enter(parentElement);
        addIfElement(parentElement);
        enter(parentElement);
        addGetElement(parentElement);
        enter(parentElement);
        addAddElement(parentElement);
        enter(parentElement);
        addLogicDeleteElement(parentElement);
        enter(parentElement);
        addLogicBatchDeleteElement(parentElement);
        enter(parentElement);
        addPhysicalDeleteElement(parentElement);
        enter(parentElement);
        addUpdateElement(parentElement);
        enter(parentElement);
        addLoadPageByMapElement(parentElement);
        enter(parentElement);
    }

    /**
     * @author:lishuai
     * @date:2020/1/9 3:25 下午
     * @description: 初始化公共成员
     * @modifyTime:
     * @modifyDescription:
     */
    private void init() {
        beanPath = introspectedTable.getBaseRecordType();
        tableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();
        sqlBeanName = beanPath + "Bean";
        //获取实体类名称
        sqlBeanName = sqlBeanName.substring(sqlBeanName.lastIndexOf(".") + 1, sqlBeanName.length());
        //将第一个字符小写
        sqlBeanName = sqlBeanName.substring(0, 1).toLowerCase().concat(sqlBeanName.substring(1));
        //设置table的别名，规则：实体bean的第一个字母小写
        tableAlias = sqlBeanName.substring(0, 1);
        introspectedColumns = introspectedTable.getAllColumns();
        columnSize = introspectedColumns.size();
        ifSqlId = sqlBeanName + "If";
        include.addAttribute(new Attribute("refid", sqlBeanName));
        fromTextElement = new TextElement("FROM " + tableName + " " + tableAlias);
        ifInclude.addAttribute(new Attribute("refid", ifSqlId));
    }

    /**
     * @author:lishuai
     * @date:2020/1/9 3:25 下午
     * @description: 1. 增加bean sql片段
     * @modifyTime:
     * @modifyDescription:
     */
    private void addSqlBeanElement(XmlElement xe) {
        XmlElement beanSql = new XmlElement("sql");
        beanSql.addAttribute(new Attribute("id", sqlBeanName));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < columnSize; i++) {
            IntrospectedColumn ic = introspectedColumns.get(i);
            if (i != columnSize - 1)
                sb.append(tableAlias + "." + ic.getActualColumnName() + " AS " + ic.getJavaProperty() + ",\n\t");
            else
                sb.append(tableAlias + "." + ic.getActualColumnName() + " AS " + ic.getJavaProperty());
        }
        beanSql.addElement(new TextElement(sb.toString()));
        xe.addElement(beanSql);
    }

    /**
     * @author:lishuai
     * @date:2020/1/9 5:06 下午
     * @description: 2. 增加if sql片段
     * @modifyTime:
     * @modifyDescription:
     */
    private void addIfElement(XmlElement xe) {
        XmlElement ifSql = new XmlElement("sql");
        ifSql.addAttribute(new Attribute("id", ifSqlId));
        for (int i = 0; i < columnSize; i++) {
            XmlElement ifLabel = new XmlElement("if");
            IntrospectedColumn ic = introspectedColumns.get(i);
            String columnName = ic.getActualColumnName();
            String javaProperty = ic.getJavaProperty();
            String jdbcTypeName = ic.getJdbcTypeName();
            if ("VARCHAR".equals(jdbcTypeName) || "CHAR".equals(jdbcTypeName) || "TEXT".equals(jdbcTypeName))
                ifLabel.addAttribute(new Attribute("test", javaProperty + " != null and " + javaProperty + " != ''"));
            else
                ifLabel.addAttribute(new Attribute("test", javaProperty + " != null"));
            ifLabel.addElement(new TextElement("\tAND " + tableAlias + "." + columnName + " = #{" + javaProperty + "}"));
            ifSql.addElement(ifLabel);
        }
        xe.addElement(ifSql);
    }

    /**
     * @author:lishuai
     * @date:2020/1/9 3:25 下午
     * @description: 3.增加get实现（根据ID 和 DATA_STATE=1 查询单条数据）
     * @modifyTime:
     * @modifyDescription:
     */
    private void addGetElement(XmlElement xe) {
        XmlElement get = new XmlElement("select");
        get.addAttribute(new Attribute("id", "get"));
        get.addAttribute(new Attribute("resultType", beanPath));
        get.addElement(new TextElement("SELECT "));
        get.addElement(include);
        get.addElement(fromTextElement);
        StringBuilder sb = new StringBuilder();
        sb.append("WHERE \n");
        sb.append("\t\t" + tableAlias + "." + primaryKey + " = #{_parameter} \n");
        sb.append("\t\tAND " + tableAlias + "." + dataStatus + " = " + dataStatusNormal);
        get.addElement(new TextElement(sb.toString()));
        xe.addElement(get);
    }

    /**
     * @author:lishuai
     * @date:2020/1/9 4:05 下午
     * @description: 4.增加add实现
     * @modifyTime:
     * @modifyDescription:
     */
    private void addAddElement(XmlElement xe) {
        XmlElement add = new XmlElement("insert");
        add.addAttribute(new Attribute("id", "add"));
        add.addAttribute(new Attribute("parameterType", beanPath));
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO \n");
        sb.append("\t\t" + tableName + " \n");
        sb.append("\t\t(");
        for (int i = 0; i < columnSize; i++) {
            IntrospectedColumn ic = introspectedColumns.get(i);
            if (i != columnSize - 1)
                sb.append(ic.getActualColumnName() + ",");
            else
                sb.append(ic.getActualColumnName() + ") \n");
        }
        sb.append("\tVALUES \n");
        sb.append("\t\t(");
        for (int i = 0; i < columnSize; i++) {
            IntrospectedColumn introspectedColumn = introspectedColumns.get(i);
            String javaProperty = introspectedColumn.getJavaProperty();
            if ("createUserId".equals(javaProperty)) {
                javaProperty = "user.id";
            }
            if (i != columnSize - 1)
                sb.append("#{" + javaProperty + "},");
            else
                sb.append("#{" + javaProperty + "})");
        }
        add.addElement(new TextElement(sb.toString()));
        xe.addElement(add);
    }

    /**
     * @author:lishuai
     * @date:2020/1/9 4:27 下午
     * @description: 5.根据主键逻辑删除实现 logicDelete
     * @modifyTime:
     * @modifyDescription:
     */
    private void addLogicDeleteElement(XmlElement xe) {
        XmlElement logicDelete = new XmlElement("update");
        logicDelete.addAttribute(new Attribute("id", "logicDelete"));
        logicDelete.addAttribute(new Attribute("parameterType", "String"));
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE \n");
        sb.append("\t\t" + tableName + " \n");
        sb.append("\tSET " + dataStatus + " = " + dataStatusDelete + " \n");
        sb.append("\tWHERE " + primaryKey + " = #{_parameter} ");
        logicDelete.addElement(new TextElement(sb.toString()));
        xe.addElement(logicDelete);
    }

    /**
     * @author:lishuai
     * @date:2020/1/9 4:43 下午
     * @description: 6.根据主键数组多条数据逻辑删除实现 logicBatchDelete
     * @modifyTime:
     * @modifyDescription:
     */
    private void addLogicBatchDeleteElement(XmlElement xe) {
        XmlElement logicBatchDelete = new XmlElement("update");
        logicBatchDelete.addAttribute(new Attribute("id", "logicBatchDelete"));
        logicBatchDelete.addAttribute(new Attribute("parameterType", "String"));
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE \n");
        sb.append("\t\t" + tableName + " \n");
        sb.append("\tSET \n");
        sb.append("\t\tDATA_STATUS = 0 \n");
        sb.append("\tWHERE \n");
        sb.append("\t\t"+primaryKey+" IN ");
        logicBatchDelete.addElement(new TextElement(sb.toString()));
        XmlElement forEach = new XmlElement("foreach");
        forEach.addAttribute(new Attribute("collection", "array"));
        forEach.addAttribute(new Attribute("item", "item"));
        forEach.addAttribute(new Attribute("index", "index"));
        forEach.addAttribute(new Attribute("open", "("));
        forEach.addAttribute(new Attribute("separator", ","));
        forEach.addAttribute(new Attribute("close", ")"));
        forEach.addElement(new TextElement("\t#{item} "));
        logicBatchDelete.addElement(forEach);
        xe.addElement(logicBatchDelete);
    }

    /**
     * @author:lishuai
     * @date:2020/1/9 4:34 下午
     * @description: 7.根据主键物理删除实现 physicalDelete
     * @modifyTime:
     * @modifyDescription:
     */
    private void addPhysicalDeleteElement(XmlElement xe) {
        XmlElement physicalDelete = new XmlElement("delete");
        physicalDelete.addAttribute(new Attribute("id", "physicalDelete"));
        physicalDelete.addAttribute(new Attribute("parameterType", "String"));
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM \n");
        sb.append("\t\t" + tableName + " \n");
        sb.append("\tWHERE \n");
        sb.append("\t\t" + primaryKey + " = #{_parameter}");
        physicalDelete.addElement(new TextElement(sb.toString()));
        xe.addElement(physicalDelete);
    }

    /**
     * @author:lishuai
     * @date:2020/1/9 5:48 下午
     * @description: 8.根据修改实现 update
     * @modifyTime:
     * @modifyDescription:
     */
    private void addUpdateElement(XmlElement xe) {
        XmlElement update = new XmlElement("update");
        update.addAttribute(new Attribute("id", "update"));
        update.addAttribute(new Attribute("parameterType", beanPath));
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE \n");
        sb.append("\t\t" + tableName);
        update.addElement(new TextElement(sb.toString()));
        sb.setLength(0);
        XmlElement updateSet = new XmlElement("set");
        for (int i = 0; i < columnSize; i++) {
            IntrospectedColumn introspectedColumn = introspectedColumns.get(i);
            String columnName = introspectedColumn.getActualColumnName();
            if (columnName.equals(primaryKey))
                continue;
            String javaProperty = introspectedColumn.getJavaProperty();
            String jdbcTypeName = introspectedColumn.getJdbcTypeName();
            XmlElement ifLabel = new XmlElement("if");
            if ("VARCHAR".equals(jdbcTypeName) || "CHAR".equals(jdbcTypeName))
                if ("CREATE_USER_ID".equals(columnName))
                    ifLabel.addAttribute(new Attribute("test", "user != null and user.id != null and user.id != ''"));
                else
                    ifLabel.addAttribute(new Attribute("test", javaProperty + " != null and " + javaProperty + " != ''"));
            else
                ifLabel.addAttribute(new Attribute("test", javaProperty + " != null"));
            if (i != columnSize - 1) {
                if ("CREATE_USER_ID".equals(columnName))
                    ifLabel.addElement(new TextElement("\t" + columnName + " = #{user.id},"));
                else
                    ifLabel.addElement(new TextElement("\t" + columnName + " = #{" + javaProperty + "},"));
            } else {
                if ("CREATE_USER_ID".equals(columnName))
                    ifLabel.addElement(new TextElement("\t" + columnName + " = #{user.id}"));
                else
                    ifLabel.addElement(new TextElement("\t" + columnName + " = #{" + javaProperty + "}"));
            }
            updateSet.addElement(ifLabel);
        }
        update.addElement(updateSet);
        sb.append("WHERE \n");
        sb.append("\t\t"+primaryKey+" = #{"+primaryKey.toLowerCase()+"}");
        update.addElement(new TextElement(sb.toString()));
        xe.addElement(update);
    }

    /**
     * @author:lishuai
     * @date:2020/1/9 5:00 下午
     * @description: 10.条件分页查询 loadPageByMap
     * @modifyTime:
     * @modifyDescription:
     */
    private void addLoadPageByMapElement(XmlElement xe) {
        XmlElement loadPageByMap = new XmlElement("select");
        loadPageByMap.addAttribute(new Attribute("id", "loadPageByMap"));
        loadPageByMap.addAttribute(new Attribute("resultType", beanPath));
        loadPageByMap.addElement(new TextElement("SELECT"));
        loadPageByMap.addElement(include);
        loadPageByMap.addElement(fromTextElement);
        StringBuilder sb = new StringBuilder();
        sb.append("WHERE \n");
        //sb.append("\t\t" + tableAlias + "." + dataStatus + " = " + dataStatusNormal + " ");
        loadPageByMap.addElement(new TextElement(sb.toString()));
        loadPageByMap.addElement(ifInclude);
        xe.addElement(loadPageByMap);
    }

    private void enter(XmlElement xe) {
        xe.addElement(new TextElement(""));
    }
}
