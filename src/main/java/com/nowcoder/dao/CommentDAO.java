package com.nowcoder.dao;

import com.nowcoder.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 李丹慧 on 2017/4/7.
 */
@Repository
@Mapper//说明是和MyBatis关联的一个DAO
public interface CommentDAO {
    // 注意空格 空格多了没事...
    String TABLE_NAME = " question ";
    String INSERT_FIELDS = " title, content, user_id, created_date, comment_count ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;//id+上面的insert字段 这里全查了 相当于*

    //注解方式
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{title},#{content},#{userId},#{createdDate},#{commentCount})"})
    //这是驼峰式的
    int addQuestion(Question question);

    //xml方式
    List<Question> selectLatestQuestions(@Param("userId") int userId,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);

    @Select({"select ",SELECT_FIELDS, " from ", TABLE_NAME , "where id = #{id}"})
    Question getById(int id);

}
