package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* ${table.comment!}
*/
@Service
public class ${table.serviceImplName} implements ${table.serviceName} {

@Resource
private ${table.mapperName} ${table.mapperName?uncap_first};

@Override
public ${entity} getById(${table.idType!Long} id) {
return ${table.mapperName?uncap_first}.selectById(id);
}

@Override
public List<${entity}> listAll() {
return ${table.mapperName?uncap_first}.selectList(null);
}

@Override
public boolean save(${entity} entity) {
return ${table.mapperName?uncap_first}.insert(entity) > 0;
}

@Override
public boolean updateById(${entity} entity) {
return ${table.mapperName?uncap_first}.updateById(entity) > 0;
}

@Override
public boolean deleteById(${table.idType!Long} id) {
return ${table.mapperName?uncap_first}.deleteById(id) > 0;
}
}
