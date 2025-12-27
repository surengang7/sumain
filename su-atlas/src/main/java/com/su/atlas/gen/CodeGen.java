package com.su.atlas.gen;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 无数据库、无 MyBatis-Plus 依赖：基于实体类生成 Mapper + XML + Service + ServiceImpl
 *
 * 约定：
 *  - 实体包：com.su.atlas
 *  - Mapper：com.su.atlas.mapper
 *  - Service：com.su.atlas.service
 *  - ServiceImpl：com.su.atlas.service.impl
 *
 * 读取（可选）注解：
 *  - com.baomidou.mybatisplus.annotation.TableName -> value
 *  - com.baomidou.mybatisplus.annotation.TableField -> value
 *  - com.baomidou.mybatisplus.annotation.TableId -> value
 *
 * 若没有注解：默认 CamelCase -> snake_case
 */
public class CodeGen {

    // ====== 你只需要改这里 ======
    private static final String ENTITY_PACKAGE = "com.su.atlas.entity";
    private static final String MAPPER_PACKAGE = "com.su.atlas.mapper";
    private static final String SERVICE_PACKAGE = "com.su.atlas.service";
    private static final String SERVICE_IMPL_PACKAGE = "com.su.atlas.service.impl";

    // 输出目录（按你的项目结构）
    private static final Path JAVA_OUT_DIR = Paths.get(System.getProperty("user.dir"), "src", "main", "java");
    private static final Path XML_OUT_DIR  = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "mapper");
    // ==========================

    public static void main(String[] args) throws Exception {
        List<Class<?>> entities = scanClasses(ENTITY_PACKAGE).stream()
                .map(CodeGen::tryLoadClass)
                .filter(Objects::nonNull)
                .filter(c -> !c.isInterface() && !c.isEnum() && !c.isAnnotation())
                .filter(c -> c.getPackageName().equals(ENTITY_PACKAGE)) // 只处理该包下实体
                .collect(Collectors.toList());

        if (entities.isEmpty()) {
            System.out.println("[WARN] 未扫描到实体类，请确认已编译并且实体在包 " + ENTITY_PACKAGE);
            return;
        }

        Files.createDirectories(JAVA_OUT_DIR);
        Files.createDirectories(XML_OUT_DIR);

        for (Class<?> entity : entities) {
            EntityMeta meta = parseEntity(entity);

            // 1) Mapper
            writeJava(MAPPER_PACKAGE, meta.mapperName + ".java", renderMapper(meta));

            // 2) Mapper XML
            writeXml(meta.mapperName + ".xml", renderMapperXml(meta));

            // 3) Service
            writeJava(SERVICE_PACKAGE, meta.serviceName + ".java", renderService(meta));

            // 4) ServiceImpl
            writeJava(SERVICE_IMPL_PACKAGE, meta.serviceImplName + ".java", renderServiceImpl(meta));
        }

        System.out.println("[OK] 生成完成：");
        System.out.println(" - Java: " + JAVA_OUT_DIR);
        System.out.println(" - XML : " + XML_OUT_DIR);
    }

    // ===================== 扫描 class =====================

    private static List<String> scanClasses(String basePackage) throws IOException, URISyntaxException {
        String path = basePackage.replace('.', '/');
        Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(path);

        List<String> classNames = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            // 只处理 file:（IDEA 运行 test main 一般是 file:）
            if (!"file".equalsIgnoreCase(url.getProtocol())) continue;

            Path dir = Paths.get(url.toURI());
            if (!Files.exists(dir)) continue;

            Files.walk(dir)
                    .filter(p -> p.toString().endsWith(".class"))
                    .forEach(p -> {
                        String rel = dir.relativize(p).toString().replace(File.separatorChar, '.');
                        String cls = basePackage + "." + rel.substring(0, rel.length() - ".class".length());
                        // 排除内部类
                        if (!cls.contains("$")) classNames.add(cls);
                    });
        }
        return classNames;
    }

    private static Class<?> tryLoadClass(String className) {
        try {
            return Class.forName(className);
        } catch (Throwable t) {
            System.out.println("[WARN] 加载类失败: " + className + " -> " + t.getMessage());
            return null;
        }
    }

    // ===================== 元数据解析 =====================

    private static EntityMeta parseEntity(Class<?> entity) {
        EntityMeta meta = new EntityMeta();
        meta.entityClass = entity;
        meta.entitySimpleName = entity.getSimpleName();
        meta.entityFqn = entity.getName();

        meta.tableName = readAnnotationValue(entity, "com.baomidou.mybatisplus.annotation.TableName", "value")
                .orElseGet(() -> camelToSnake(meta.entitySimpleName));

        meta.mapperName = meta.entitySimpleName + "Mapper";
        meta.serviceName = meta.entitySimpleName + "Service";
        meta.serviceImplName = meta.entitySimpleName + "ServiceImpl";

        List<Field> fields = Arrays.stream(entity.getDeclaredFields())
                .filter(f -> !java.lang.reflect.Modifier.isStatic(f.getModifiers()))
                .filter(f -> !java.lang.reflect.Modifier.isTransient(f.getModifiers()))
                .collect(Collectors.toList());

        for (Field f : fields) {
            ColumnMeta cm = new ColumnMeta();
            cm.field = f;
            cm.fieldName = f.getName();
            cm.javaType = f.getType();

            cm.columnName = readAnnotationValue(f, "com.baomidou.mybatisplus.annotation.TableField", "value")
                    .orElseGet(() -> camelToSnake(cm.fieldName));

            // 是否主键
            boolean isId = readAnnotationValue(f, "com.baomidou.mybatisplus.annotation.TableId", "value").isPresent();
            cm.isId = isId;

            meta.columns.add(cm);
        }

        // 推断主键：优先 @TableId
        List<ColumnMeta> ids = meta.columns.stream().filter(c -> c.isId).collect(Collectors.toList());
        if (ids.size() == 1) {
            meta.id = ids.get(0);
        } else {
            // 如果没有 @TableId，尝试猜测：唯一一个 *Id 或 id 字段
            List<ColumnMeta> guess = meta.columns.stream()
                    .filter(c -> "id".equalsIgnoreCase(c.fieldName) || c.fieldName.endsWith("Id") || c.fieldName.endsWith("ID"))
                    .collect(Collectors.toList());
            if (guess.size() == 1) meta.id = guess.get(0);
        }

        return meta;
    }

    /**
     * 通过反射读取注解值，避免编译期依赖 MP 注解。
     */
    private static Optional<String> readAnnotationValue(Class<?> targetClass, String annoFqn, String method) {
        try {
            for (Annotation a : targetClass.getAnnotations()) {
                if (a.annotationType().getName().equals(annoFqn)) {
                    Object v = a.annotationType().getMethod(method).invoke(a);
                    if (v != null) {
                        String s = String.valueOf(v).trim();
                        if (!s.isEmpty()) return Optional.of(s);
                    }
                }
            }
        } catch (Throwable ignored) { }
        return Optional.empty();
    }

    private static Optional<String> readAnnotationValue(Field field, String annoFqn, String method) {
        try {
            for (Annotation a : field.getAnnotations()) {
                if (a.annotationType().getName().equals(annoFqn)) {
                    Object v = a.annotationType().getMethod(method).invoke(a);
                    if (v != null) {
                        String s = String.valueOf(v).trim();
                        if (!s.isEmpty()) return Optional.of(s);
                    }
                }
            }
        } catch (Throwable ignored) { }
        return Optional.empty();
    }

    // ===================== 模板渲染 =====================

    private static String renderMapper(EntityMeta m) {
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(MAPPER_PACKAGE).append(";\n\n");
        sb.append("import ").append(m.entityFqn).append(";\n");
        sb.append("import org.apache.ibatis.annotations.Mapper;\n");
        sb.append("import org.apache.ibatis.annotations.Param;\n");
        sb.append("import java.util.List;\n\n");
        sb.append("@Mapper\n");
        sb.append("public interface ").append(m.mapperName).append(" {\n\n");

        // selectAll
        sb.append("    List<").append(m.entitySimpleName).append("> selectAll();\n\n");

        // byId 仅在识别到唯一主键时生成
        if (m.id != null) {
            sb.append("    ").append(m.entitySimpleName).append(" selectById(@Param(\"id\") ")
                    .append(simpleType(m.id.javaType)).append(" id);\n\n");
            sb.append("    int deleteById(@Param(\"id\") ").append(simpleType(m.id.javaType)).append(" id);\n\n");
            sb.append("    int updateById(").append(m.entitySimpleName).append(" entity);\n\n");
        }

        sb.append("    int insert(").append(m.entitySimpleName).append(" entity);\n\n");

        // update/delete（无主键时也给一个“全量更新/条件删除”会很危险，先不生成）
        if (m.id == null) {
            sb.append("    // 未检测到唯一主键（可能是复合主键表），这里只生成 insert/selectAll。\n");
            sb.append("    // 你可以自行补充按条件查询/删除/更新方法。\n");
        }

        sb.append("}\n");
        return sb.toString();
    }

    private static String renderMapperXml(EntityMeta m) {
        String namespace = MAPPER_PACKAGE + "." + m.mapperName;

        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" ")
                .append("\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n");
        sb.append("<mapper namespace=\"").append(namespace).append("\">\n\n");

        // resultMap
        sb.append("  <resultMap id=\"BaseResultMap\" type=\"").append(m.entityFqn).append("\">\n");
        for (ColumnMeta c : m.columns) {
            String tag = (m.id != null && c == m.id) ? "id" : "result";
            sb.append("    <").append(tag)
                    .append(" column=\"").append(c.columnName).append("\"")
                    .append(" property=\"").append(c.fieldName).append("\"/>\n");
        }
        sb.append("  </resultMap>\n\n");

        String baseCols = m.columns.stream().map(c -> c.columnName).collect(Collectors.joining(", "));
        sb.append("  <sql id=\"Base_Column_List\">").append(baseCols).append("</sql>\n\n");

        // selectAll
        sb.append("  <select id=\"selectAll\" resultMap=\"BaseResultMap\">\n");
        sb.append("    SELECT <include refid=\"Base_Column_List\"/> FROM ").append(m.tableName).append("\n");
        sb.append("  </select>\n\n");

        if (m.id != null) {
            // selectById
            sb.append("  <select id=\"selectById\" parameterType=\"").append(m.id.javaType.getName())
                    .append("\" resultMap=\"BaseResultMap\">\n");
            sb.append("    SELECT <include refid=\"Base_Column_List\"/> FROM ").append(m.tableName)
                    .append(" WHERE ").append(m.id.columnName).append(" = #{id}\n");
            sb.append("  </select>\n\n");

            // deleteById
            sb.append("  <delete id=\"deleteById\" parameterType=\"").append(m.id.javaType.getName()).append("\">\n");
            sb.append("    DELETE FROM ").append(m.tableName).append(" WHERE ").append(m.id.columnName).append(" = #{id}\n");
            sb.append("  </delete>\n\n");

            // updateById（全字段更新，null 也会覆盖；你要动态更新可以再优化）
            sb.append("  <update id=\"updateById\" parameterType=\"").append(m.entityFqn).append("\">\n");
            sb.append("    UPDATE ").append(m.tableName).append("\n");
            sb.append("    SET\n");
            List<ColumnMeta> nonIdCols = m.columns.stream().filter(c -> c != m.id).collect(Collectors.toList());
            for (int i = 0; i < nonIdCols.size(); i++) {
                ColumnMeta c = nonIdCols.get(i);
                sb.append("      ").append(c.columnName).append(" = #{").append(c.fieldName).append("}");
                sb.append(i == nonIdCols.size() - 1 ? "\n" : ",\n");
            }
            sb.append("    WHERE ").append(m.id.columnName).append(" = #{").append(m.id.fieldName).append("}\n");
            sb.append("  </update>\n\n");
        }

        // insert（全字段插入；如果 id 是数据库生成，你可以在实体里让它为 null）
        sb.append("  <insert id=\"insert\" parameterType=\"").append(m.entityFqn).append("\">\n");
        sb.append("    INSERT INTO ").append(m.tableName).append(" (\n");
        for (int i = 0; i < m.columns.size(); i++) {
            ColumnMeta c = m.columns.get(i);
            sb.append("      ").append(c.columnName);
            sb.append(i == m.columns.size() - 1 ? "\n" : ",\n");
        }
        sb.append("    ) VALUES (\n");
        for (int i = 0; i < m.columns.size(); i++) {
            ColumnMeta c = m.columns.get(i);
            sb.append("      #{").append(c.fieldName).append("}");
            sb.append(i == m.columns.size() - 1 ? "\n" : ",\n");
        }
        sb.append("    )\n");
        sb.append("  </insert>\n\n");

        sb.append("</mapper>\n");
        return sb.toString();
    }

    private static String renderService(EntityMeta m) {
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(SERVICE_PACKAGE).append(";\n\n");
        sb.append("import ").append(m.entityFqn).append(";\n");
        sb.append("import java.util.List;\n\n");
        sb.append("public interface ").append(m.serviceName).append(" {\n\n");
        sb.append("    List<").append(m.entitySimpleName).append("> selectAll();\n\n");

        if (m.id != null) {
            sb.append("    ").append(m.entitySimpleName).append(" selectById(")
                    .append(simpleType(m.id.javaType)).append(" id);\n\n");
            sb.append("    boolean deleteById(").append(simpleType(m.id.javaType)).append(" id);\n\n");
            sb.append("    boolean updateById(").append(m.entitySimpleName).append(" entity);\n\n");
        }

        sb.append("    boolean insert(").append(m.entitySimpleName).append(" entity);\n\n");

        if (m.id == null) {
            sb.append("    // 未检测到唯一主键（可能是复合主键表），这里只生成 insert/selectAll。\n");
        }

        sb.append("}\n");
        return sb.toString();
    }

    private static String renderServiceImpl(EntityMeta m) {
        String mapperVar = lowerFirst(m.mapperName);
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(SERVICE_IMPL_PACKAGE).append(";\n\n");
        sb.append("import ").append(m.entityFqn).append(";\n");
        sb.append("import ").append(MAPPER_PACKAGE).append(".").append(m.mapperName).append(";\n");
        sb.append("import ").append(SERVICE_PACKAGE).append(".").append(m.serviceName).append(";\n");
        sb.append("import jakarta.annotation.Resource;\n");
        sb.append("import org.springframework.stereotype.Service;\n");
        sb.append("import java.util.List;\n\n");

        sb.append("@Service\n");
        sb.append("public class ").append(m.serviceImplName).append(" implements ").append(m.serviceName).append(" {\n\n");
        sb.append("    @Resource\n");
        sb.append("    private ").append(m.mapperName).append(" ").append(mapperVar).append(";\n\n");

        sb.append("    @Override\n");
        sb.append("    public List<").append(m.entitySimpleName).append("> selectAll() {\n");
        sb.append("        return ").append(mapperVar).append(".selectAll();\n");
        sb.append("    }\n\n");

        if (m.id != null) {
            sb.append("    @Override\n");
            sb.append("    public ").append(m.entitySimpleName).append(" selectById(")
                    .append(simpleType(m.id.javaType)).append(" id) {\n");
            sb.append("        return ").append(mapperVar).append(".selectById(id);\n");
            sb.append("    }\n\n");

            sb.append("    @Override\n");
            sb.append("    public boolean deleteById(").append(simpleType(m.id.javaType)).append(" id) {\n");
            sb.append("        return ").append(mapperVar).append(".deleteById(id) > 0;\n");
            sb.append("    }\n\n");

            sb.append("    @Override\n");
            sb.append("    public boolean updateById(").append(m.entitySimpleName).append(" entity) {\n");
            sb.append("        return ").append(mapperVar).append(".updateById(entity) > 0;\n");
            sb.append("    }\n\n");
        }

        sb.append("    @Override\n");
        sb.append("    public boolean insert(").append(m.entitySimpleName).append(" entity) {\n");
        sb.append("        return ").append(mapperVar).append(".insert(entity) > 0;\n");
        sb.append("    }\n\n");

        sb.append("}\n");
        return sb.toString();
    }

    // ===================== 文件写入 =====================

    private static void writeJava(String pkg, String fileName, String content) throws IOException {
        Path dir = JAVA_OUT_DIR.resolve(pkg.replace('.', File.separatorChar));
        Files.createDirectories(dir);
        Path file = dir.resolve(fileName);
        writeFile(file, content);
    }

    private static void writeXml(String fileName, String content) throws IOException {
        Files.createDirectories(XML_OUT_DIR);
        Path file = XML_OUT_DIR.resolve(fileName);
        writeFile(file, content);
    }

    private static void writeFile(Path file, String content) throws IOException {
        Files.write(file, content.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        System.out.println("Generated: " + file);
    }

    // ===================== 工具函数 =====================

    private static String camelToSnake(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (Character.isUpperCase(ch)) {
                if (i > 0) sb.append('_');
                sb.append(Character.toLowerCase(ch));
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    private static String lowerFirst(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toLowerCase(s.charAt(0)) + s.substring(1);
    }

    private static String simpleType(Class<?> t) {
        if (t == null) return "Object";
        if (t.isArray()) return simpleType(t.getComponentType()) + "[]";
        return t.getSimpleName();
    }

    // ===================== 元数据结构 =====================

    private static class EntityMeta {
        Class<?> entityClass;
        String entitySimpleName;
        String entityFqn;

        String tableName;

        String mapperName;
        String serviceName;
        String serviceImplName;

        List<ColumnMeta> columns = new ArrayList<>();
        ColumnMeta id; // 唯一主键才会设置
    }

    private static class ColumnMeta {
        Field field;
        String fieldName;
        String columnName;
        Class<?> javaType;
        boolean isId;
    }
}
