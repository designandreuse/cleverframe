package org.cleverframe.generator.vo.model;

import java.io.Serializable;

/**
 * 数据库详细信息，概要信息使用类{@link DataBaseSummaryVo}<br/>
 * <p/>
 * 作者：LiZW <br/>
 * 创建时间：2016-6-20 15:13 <br/>
 */
public class DataBaseSchemaVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 数据库名称
     */
    private String schemaName;

    /**
     * 默认编码格式
     */
    private String defaultCharset;

    /**
     * 默认排序字符集
     */
    private String defaultCollation;

    /**
     * Sql Path
     */
    private String sqlPath;

    /*--------------------------------------------------------------
     *          getter、setter
     * -------------------------------------------------------------*/

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getDefaultCharset() {
        return defaultCharset;
    }

    public void setDefaultCharset(String defaultCharset) {
        this.defaultCharset = defaultCharset;
    }

    public String getDefaultCollation() {
        return defaultCollation;
    }

    public void setDefaultCollation(String defaultCollation) {
        this.defaultCollation = defaultCollation;
    }

    public String getSqlPath() {
        return sqlPath;
    }

    public void setSqlPath(String sqlPath) {
        this.sqlPath = sqlPath;
    }
}
