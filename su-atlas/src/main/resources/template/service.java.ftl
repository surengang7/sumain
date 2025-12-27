package ${package.Service};

import ${package.Entity}.${entity};
import java.util.List;

/**
* ${table.comment!}
*/
public interface ${table.serviceName} {

${entity} getById(${table.idType!Long} id);

List<${entity}> listAll();

boolean save(${entity} entity);

boolean updateById(${entity} entity);

boolean deleteById(${table.idType!Long} id);
}
