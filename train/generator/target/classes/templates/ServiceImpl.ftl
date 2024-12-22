package ${packageName}.service.impl;

import ${packageName}.pojo.entity.${className};
import ${packageName}.mapper.${className}Mapper;
import ${packageName}.service.${className}Service;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ${className}ServiceImpl implements ${className}Service {

    @Autowired
    private ${className}Mapper ${className?uncap_first}Mapper;

    @Override
    public PageResult list(${className}QueryDTO ${className?uncap_first}QueryDTO) {
        Integer page = ${className?uncap_first}QueryDTO.getPage();
        Integer size = ${className?uncap_first}QueryDTO.getSize();
        Page<${className}> pages = new Page<>(page,size);
        LambdaQueryWrapper<${className}> wrapper = new LambdaQueryWrapper<>();
        ${className?uncap_first}Mapper.selectPage(pages,wrapper);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(pages.getTotal());
        pageResult.setList(pages.getRecords());
        return pageResult;
    }

    @Override
    public Result getById(Long id) {
        Result result = new Result();
        result.setData(${className?uncap_first}Mapper.selectById(id));
        result.setCode(Result.SUCCESS_CODE);
        return result;
    }

    @Override
    public Result save(${className}DTO ${className?uncap_first}DTO) {
          ${className} ${className?uncap_first} = new ${className}();
          BeanUtils.copyProperties(${className?uncap_first}DTO,${className?uncap_first});
     //   ${className?uncap_first}Mapper.insert();
        return new Result(Result.SUCCESS_CODE);
    }

    @Override
    public Result update(${className}DTO ${className?uncap_first}DTO) {
        ${className} ${className?uncap_first} = new ${className}();
        BeanUtils.copyProperties(${className?uncap_first}DTO,${className?uncap_first});
     //   ${className?uncap_first}Mapper.updateById();
        return new Result(Result.SUCCESS_CODE);
    }

    @Override
    public Result delete(Long id) {
        ${className?uncap_first}Mapper.deleteById(id);
        return new Result(Result.SUCCESS_CODE);
    }
}
