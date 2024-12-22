package ${packageName}.service;

import ${packageName}.pojo.entity.${className};
import java.util.List;

public interface ${className}Service {
    PageResult list(${className}QueryDTO ${className?uncap_first}QueryDTO);
    Result getById(Long id);
    Result save(${className}DTO ${className?uncap_first}DTO);
    Result update(${className}DTO ${className?uncap_first}DTO);
    Result delete(Long id);
}
