package org.cleverframe.core.dao;

import org.cleverframe.common.persistence.Page;
import org.cleverframe.common.persistence.Parameter;
import org.cleverframe.core.CoreBeanNames;
import org.cleverframe.core.entity.MDict;
import org.cleverframe.core.persistence.dao.BaseDao;
import org.cleverframe.core.utils.QLScriptUtils;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 作者：LiZW <br/>
 * 创建时间：2016-6-18 23:02 <br/>
 */
@Repository(CoreBeanNames.MDictDao)
public class MDictDao extends BaseDao<MDict> {

    /**
     * 根据字典类型获取数据字典(模糊查询一棵树上的数据字典)，使用分页<br/>
     *
     * @param page      分页对象
     * @param mdictType 查询参数：字典分类,不能为空
     * @param mdictKey  查询参数：字典键
     * @param id        查询参数：ID
     * @param uuid      查询参数：UUID
     * @param delFlag   查询参数：删除标记
     * @return 分页数据
     */
    public Page<MDict> findByPage(Page<MDict> page, String mdictType, String mdictKey, Long id, String uuid, Character delFlag) {
        Parameter param = new Parameter(delFlag);
        param.put("mdictKey", mdictKey);
        param.put("mdictType", mdictType);
        param.put("id", id);
        param.put("uuid", uuid);
        String sql = QLScriptUtils.getSQLScript("org.cleverframe.core.dao.MDictDao.findByPage");
        return hibernateDao.findBySql(page, sql, param);
    }

    /**
     * 获取所有的根节点数据字典
     *
     * @return 根节点数据字典集合
     */
    public List<MDict> findAllRoot() {
        Parameter param = new Parameter(MDict.DEL_FLAG_NORMAL);
        String sql = QLScriptUtils.getSQLScript("org.cleverframe.core.dao.MDictDao.findAllRoot");
        return hibernateDao.findBySql(sql, param);
    }

    /**
     * 查询所有类型的数据字典
     *
     * @return 数据字典集合
     */
    public List<MDict> findAllType() {
        Parameter param = new Parameter(MDict.DEL_FLAG_NORMAL);
        String sql = QLScriptUtils.getSQLScript("org.cleverframe.core.dao.MDictDao.findAllType");
        return hibernateDao.findBySql(sql, param);
    }

    /**
     * 根据字典类型删除所有数据字典，直接从数据库删除
     *
     * @param dictType 查询参数：字典分类
     * @return 成功返回true，失败返回false
     */
    public boolean deleteByType(String dictType) {
        Parameter param = new Parameter();
        param.put("dictType", dictType);
        String sql = QLScriptUtils.getSQLScript("org.cleverframe.core.dao.MDictDao.deleteByType");
        SQLQuery sqlQuery = hibernateDao.createSqlQuery(sql, param);
        sqlQuery.executeUpdate();
        return true;
    }

    /**
     * 直接从数据库删除当前节点和其所有的子节点数据
     *
     * @param fullPath 节点路径
     * @return 成功返回true，失败返回false
     */
    public boolean deleteChild(String fullPath) {
        Parameter param = new Parameter();
        param.put("fullPath", fullPath);
        String sql = QLScriptUtils.getSQLScript("org.cleverframe.core.dao.MDictDao.deleteChild");
        SQLQuery sqlQuery = hibernateDao.createSqlQuery(sql, param);
        sqlQuery.executeUpdate();
        return true;
    }
}
