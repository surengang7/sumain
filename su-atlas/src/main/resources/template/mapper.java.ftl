package ${package.Mapper};

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ${package.Entity}.${entity};
import org.apache.ibatis.annotations.Mapper;

/**
* ${table.comment!}
*/
@Mapper
public interface ${table.mapperName} extends BaseMapper<${entity}> {
}
