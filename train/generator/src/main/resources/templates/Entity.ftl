package ${packageName}.pojo.entity;
import lombok.Data;

@Data
public class ${className} {
<#list fields?keys as field>
<#if annotations?has_content && annotations[field]?has_content>
    ${annotations[field]}
</#if>
    private ${fields[field]} ${field};
</#list>
}
