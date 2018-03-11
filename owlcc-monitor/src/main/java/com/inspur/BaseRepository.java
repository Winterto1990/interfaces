package com.inspur;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by xuds on 2017/6/22.
 */
public abstract class BaseRepository<T> {
    @Autowired
    //@Qualifier("masterSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate;


    /**
     * 查询集合
     *
     * @param sql
     * @param param
     * @return
     */
    @SuppressWarnings({"unchecked", "hiding"})
    public <T> List<T> selectList(String sql, Object param) {
        SqlSession session = this.getSqlSession(null, false);
        return (List<T>) session.selectList(sql, param);
    }

    /**
     * 获取SqlSession
     *
     * @param template
     * @param isAuto
     * @return
     */
    private SqlSession getSqlSession(SqlSessionTemplate template, boolean isAuto) {
        if (template == null) {
            //sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, isAuto);
            return sqlSessionTemplate;
        } else {
            return template.getSqlSessionFactory().openSession(ExecutorType.BATCH, isAuto);
        }
    }

    /**
     * 查询对象
     *
     * @param sql
     * @param param
     * @return
     */
    @SuppressWarnings({"unchecked", "hiding"})
    public <T> T selectOne(String sql, Object param) {
        SqlSession session = this.getSqlSession(null, false);
        return (T) session.selectOne(sql, param);
    }

    /**
     * 删除参数
     *
     * @param sql
     * @param param
     * @return
     */
    public int delete(String sql, Object param) {
        SqlSession session = this.getSqlSession(null, true);
        return session.delete(sql, param);
    }

    /**
     * 插入参数
     *
     * @param
     * @param
     * @return
     */
    public int save(String sql, Object param) {
        SqlSession session = this.getSqlSession(null, true);
        return session.insert(sql, param);
    }


}
