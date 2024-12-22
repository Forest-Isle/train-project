package com.senvu;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class CodeGenerator {

    private static final String TEMPLATE_PATH = "generator/src/main/resources/templates/";
    private static final String OUTPUT_PATH = "member/src/main/java/";

    // Database connection configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/db_member";
    private static final String DB_USER = "train_member";
    private static final String DB_PASSWORD = "12306Train@";

    public static void main(String[] args) throws Exception {
        // Configure table and class details
        String tableName = "ticket";
        String packageName = "com.senvu.train.member";
        String className = "Ticket";

        // Fetch fields from the database
        Map<String, String> fields = new HashMap<>();

        if (!tableName.isEmpty()) {
            try {
                // Fetch fields from the database
                fields = getFieldsFromDatabase(tableName);
            } catch (Exception e) {
                System.out.println("Database connection failed or table not found: " + e.getMessage());
            }
        }

        // Generate the CRUD code
        generateCode(tableName, packageName, className, fields);
    }

    private static String convertToCamelCase(String fieldName) {
        StringBuilder result = new StringBuilder();
        boolean nextUpperCase = false;
        for (char c : fieldName.toCharArray()) {
            if (c == '_') {
                nextUpperCase = true;
            } else if (nextUpperCase) {
                result.append(Character.toUpperCase(c));
                nextUpperCase = false;
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    private static Map<String, String> getFieldsFromDatabase(String tableName) throws Exception {
        Map<String, String> fields = new HashMap<>();
        Class.forName("com.mysql.cj.jdbc.Driver"); // Load MySQL driver
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("DESCRIBE " + tableName)) {
            while (resultSet.next()) {
                String fieldName = resultSet.getString("Field");
                fieldName = convertToCamelCase(fieldName);
                String fieldType = mapToJavaType(resultSet.getString("Type"));
                fields.put(fieldName, fieldType);
            }
        }
        return fields;
    }

    private static String mapToJavaType(String sqlType) {
        if (sqlType.startsWith("text") || sqlType.contains("char") || sqlType.contains("json")) {
            return "String";
        } else if (sqlType.startsWith("int")) {
            return "Integer";
        } else if (sqlType.startsWith("bigint")) {
            return "Long";
        } else if (sqlType.startsWith("double") || sqlType.startsWith("float")) {
            return "Double";
        } else if (sqlType.startsWith("datetime") || sqlType.startsWith("timestamp")) {
            return "LocalDateTime";
        } else if (sqlType.equals("time")) {
            return "LocalTime";
        } else if (sqlType.equals("date")) {
            return "LocalDate";
        } else if (sqlType.startsWith("decimal")) {
            return "BigDecimal";
        } else {
            return "Object";
        }
    }

    public static void generateCode(String tableName, String packageName, String className, Map<String, String> fields) throws Exception {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
        cfg.setDirectoryForTemplateLoading(new File(TEMPLATE_PATH));
        cfg.setDefaultEncoding("UTF-8");

        // Data model for templates
        Map<String, Object> data = new HashMap<>();
        data.put("packageName", packageName);
        data.put("className", className);
        data.put("tableName", tableName);
        data.put("fields", fields);
        data.put("annotations", getAnnotationsForFields(fields));

        // Generate files for each layer
        if (!fields.isEmpty()) {
            generateFile(cfg, data, "Entity.ftl", className + ".java", packageName + ".pojo.entity");
//            generateFile(cfg, data, "DTO.ftl", className + "DTO.java", packageName + ".pojo.dto");
//            generateFile(cfg, data, "VO.ftl", className + "VO.java", packageName + ".pojo.vo");
        }
        generateFile(cfg, data, "Controller.ftl", className + "Controller.java", packageName + ".controller");
        generateFile(cfg, data, "Service.ftl", className + "Service.java", packageName + ".service");
        generateFile(cfg, data, "ServiceImpl.ftl", className + "ServiceImpl.java", packageName + ".service.impl");
        generateFile(cfg, data, "Mapper.ftl", className + "Mapper.java", packageName + ".mapper");
//        generateFile(cfg, data, "Query.ftl", className + "QueryDTO.java", packageName + ".pojo.dto.query");
    }

    private static Map<String, String> getAnnotationsForFields(Map<String, String> fields) {
        Map<String, String> annotations = new HashMap<>();
        for (String field : fields.keySet()) {
            if (field.equalsIgnoreCase("createTime")) {
                annotations.put(field, "@TableField(value = \"create_time\", fill = FieldFill.INSERT_UPDATE)");
            } else if (field.equalsIgnoreCase("updateTime")) {
                annotations.put(field, "@TableField(value = \"update_time\", fill = FieldFill.INSERT_UPDATE)");
            } else if (field.equalsIgnoreCase("id")){
                annotations.put(field, "@JsonSerialize(using = ToStringSerializer.class)");
            }else {
                annotations.put(field, ""); // No annotations for other fields
            }
        }
        return annotations;
    }

    private static void generateFile(Configuration cfg, Map<String, Object> data, String templateName, String outputFileName, String subPackage) throws Exception {
        String fullPath = OUTPUT_PATH + subPackage.replace('.', '/') + "/";
        File outputFile = new File(fullPath + outputFileName);
        outputFile.getParentFile().mkdirs(); // Ensure the directory exists

        Template template = cfg.getTemplate(templateName);
        try (Writer writer = new FileWriter(outputFile)) {
            template.process(data, writer);
        }
        System.out.println("Generated: " + outputFile.getAbsolutePath());
    }
}
