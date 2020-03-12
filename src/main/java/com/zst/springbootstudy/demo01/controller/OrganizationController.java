package com.zst.springbootstudy.demo01.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zst.springbootstudy.demo01.entity.Organization;
import com.zst.springbootstudy.demo01.service.OrganizationService;
import com.zst.springbootstudy.demo01.service.impl.OrganizationServiceImpl;
import com.zst.springbootstudy.demo01.tool.JWTUtil;
import com.zst.springbootstudy.demo01.tool.RandomValidateCodeUtil;
import com.zst.springbootstudy.demo01.tool.SendMail;
import org.aspectj.weaver.ast.Or;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zst
 * @since 2020-02-16
 */
@RestController
@RequestMapping("/organization")
public class OrganizationController {

    @Autowired
    OrganizationServiceImpl organizationService;

    @Autowired
    JWTUtil jwtUtil;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @RequestMapping("/test")
    public List<Organization> test(){
        return organizationService.list();
    }
    //add.......................................................................................................
    /**
     * 添加机构
     * @param map
     */
    @RequestMapping("/addOrg")
    public String addOrg(@RequestBody HashMap<String, String> map) throws JSONException {
//        机构设置
        Organization organization = new Organization();
        organization.setAccount(map.get("account"));
        organization.setPassword(map.get("password"));
        organization.setTel(new BigDecimal(map.get("tel")));
        organization.setEmail(map.get("email"));
//        获取code
        String code = map.get("code");
        String sessionCode = stringRedisTemplate.opsForValue().get("code");
//      返回数据设置
        JSONObject mode = new JSONObject();
        mode.put("code","100");
//      code判断
        if(code.equalsIgnoreCase(sessionCode)){
            organization.setCreateTime(LocalDateTime.now());
            organization.setLogoUrl("20721929.png");
            organizationService.save(organization);
            mode.put("code","200");
        }else{
            mode.put("msg","验证码不正确");
        }
        return mode.toString();
    }


    //delete.......................................................................................................
    /**
     * 根据id删除机构
     * @param organization
     */
    @RequestMapping("/deleteOrgById")
    public void deleteOrgById(@RequestBody Organization organization){
        organizationService.removeById(organization.getOrgId());
    }


    //update.......................................................................................................

    /**
     * 更新信息
     * @param organization
     * @return
     */
    @PostMapping("/updateOrg")
    public Organization updateOrg(@RequestBody Organization organization){
        Map map = new HashMap();
        map.put("code","200");
        int id = Integer.parseInt(stringRedisTemplate.opsForValue().get("orgId"));
        organization.setOrgId(id);
        organizationService.updateById(organization);
        return organizationService.getById(id);
    }

    //query.......................................................................................................
    /**
     * 邮件查询
     * @param organization
     * @return
     */
    @RequestMapping("/queryEmail")
    public Map<String, Object> queryEmail(@RequestBody Organization organization){
        Map<String, Object> map = new HashMap<>();
        map.put("code","100");
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email",organization.getEmail());
        List<Organization> organizations = organizationService.list(queryWrapper);
        int size = organizations.size();
        if(size > 0) {
            map.put("msg", "邮箱已存在");
            return map;
        }
        map.put("code","200");
        map.put("msg", "邮箱可用");
        return map;
    }

    /**
     * 查询账号是否存在
     */
    @RequestMapping("/queryAccount")
    public  List<Organization> queryAccount(@RequestBody Organization organization){
        QueryWrapper<Organization> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("account",organization.getAccount());
        return organizationService.list(queryWrapper);
    }

    /**
     * 分页请求
     * @param pageNo 页号
     * @param pageSize 页面大小
     * @return
     */
    @RequestMapping("/page")
    public IPage<Organization> page(Integer pageNo, Integer pageSize){
        IPage<Organization> page = new Page<>(pageNo, pageSize);
        IPage<Organization> organizationIPage = organizationService.page(page);
        return organizationIPage;
    }

    /**
     * 机构找回密码
     * @param organization 机构信息
     * @return
     */
    @RequestMapping("/forgetPassword")
    public Map<String, Object> forgetPassword(@RequestBody Organization organization){
        Map<String, Object> map = new HashMap<>();
        map.put("code","100");
        QueryWrapper<Organization> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("email",organization.getEmail());
        List<Organization> organizations =  organizationService.list(queryWrapper);
        int  size = organizations.size();
        if(size==0) {
            map.put("msg","邮箱未注册或用户不存在");
            return map;
        }
        Organization organization1 = organizations.get(0);
        map.put("msg","密码找回成功请登录邮箱");
        map.put("code","200");
        SendMail mySendMai = new SendMail();
        mySendMai.sendMail(organization1.getEmail(),"系统提示，您的密码为："+organization1.getPassword());
        return map;
    }
    /**
     * 登录
     * @param map 参数集合
     * @param request http请求
     * @return
     */
    @RequestMapping("/login")
    public  String login( @RequestBody HashMap<String, String> map , HttpServletRequest request)  {
        String code = map.get("code");
        Organization organization = new Organization();
        organization.setAccount(map.get("account"));
        organization.setPassword(map.get("password"));
        JSONObject mode = new JSONObject();
        QueryWrapper<Organization> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("account",organization.getAccount()).eq("password",organization.getPassword());
        int size = organizationService.list(queryWrapper).size();
        HttpSession session = request.getSession();
        session.setAttribute("org", organization);
        String sessionCode = stringRedisTemplate.opsForValue().get("code");
        System.out.println(sessionCode);
        try {
            mode.put("code","100");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(size == 0){
            try {
                mode.put("msg","用户名不存在或密码不正确");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(!code.equalsIgnoreCase(sessionCode)){
            try {
                mode.put("msg","验证码不正确");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            organization = organizationService.list(queryWrapper).get(0);
            try {
                mode.put("msg","登录成功");
                mode.put("code","200");
                stringRedisTemplate.opsForValue().set("orgId", String.valueOf(organization.getOrgId()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                mode.put("token",JWTUtil.createToken(new Long(organization.getOrgId())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mode.toString();
    }

    @PostMapping("/queryOrgInfo")
    public Organization queryOrgInfo(){
        String orgId = stringRedisTemplate.opsForValue().get("orgId");
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("orgId",orgId);
        return organizationService.getOne(queryWrapper);
    }
}
