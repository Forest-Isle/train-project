package ${packageName}.pojo.dto;
import lombok.Data;

@Data
public class ${className}DTO {
<#list fields?keys as field>
    private ${fields[field]} ${field};
</#list>
}
