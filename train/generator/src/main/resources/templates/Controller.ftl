package ${packageName}.controller;

import org.springframework.web.bind.annotation.*;
import ${packageName}.service.${className}Service;
import ${packageName}.pojo.entity.${className};

import java.util.List;

@RestController
@RequestMapping("/api/${tableName}")
public class ${className}Controller {

    @Autowired
    private ${className}Service ${className?uncap_first}Service;

    @GetMapping("/query")
    public PageResult list(${className}QueryDTO ${className?uncap_first}QueryDTO) {
        return ${className?uncap_first}Service.list(${className?uncap_first}QueryDTO);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return ${className?uncap_first}Service.getById(id);
    }

    @PostMapping
    public Result save(@RequestBody ${className}DTO ${className?uncap_first}DTO) {
         return ${className?uncap_first}Service.save(${className?uncap_first}DTO);
    }

    @PutMapping
    public Result update(@RequestBody ${className}DTO ${className?uncap_first}DTO) {
        return ${className?uncap_first}Service.update(${className?uncap_first}DTO);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return  ${className?uncap_first}Service.delete(id);
    }
}
