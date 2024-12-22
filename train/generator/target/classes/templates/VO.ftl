package ${packageName}.pojo.vo;
import lombok.Data;

@Data
public class ${className}VO {
<#list fields?keys as field>
    private ${fields[field]} ${field};
</#list>
}
