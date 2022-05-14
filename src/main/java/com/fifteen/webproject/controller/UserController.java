package com.fifteen.webproject.controller;

import com.fifteen.webproject.bean.dto.RegisterDTO;
import com.fifteen.webproject.bean.dto.StudentLoginDTO;
import com.fifteen.webproject.bean.entity.User;
import com.fifteen.webproject.bean.vo.*;
import com.fifteen.webproject.service.user.*;
import com.fifteen.webproject.utils.FdfsClientUtil;
import com.fifteen.webproject.utils.ImageCode;
import com.fifteen.webproject.utils.TokenUtils;
import com.fifteen.webproject.utils.exception.AppException;
import com.fifteen.webproject.utils.redis.RedisService;
import com.fifteen.webproject.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;


/**
 * @Author Fifteen
 * @Date 2022/4/8
 **/
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private UniversityService universityService;
    @Autowired
    private MajorService majorService;
    @Autowired
    private CollegeService collegeService;
    @Autowired
    private GradeService gradeService;
    @Autowired
    private ClassService classService;
    @Autowired
    private FdfsClientUtil fdfsClientUtil;


    @PostMapping("/login")
    public Result login(@RequestBody StudentLoginDTO studentLoginDTO, HttpServletRequest request,HttpServletResponse response) {
        String code = (String) redisService.get(request.getHeader("uuid"));
        if (!studentLoginDTO.getVerificationCode().toLowerCase().equals(code)) {
            throw new AppException("请输入正确的验证码");
        }
        Integer id = userService.studentLogin(studentLoginDTO.getAccount(), studentLoginDTO.getPassword());
        if (id == 0) {
            throw new AppException("账号或者密码错误");
        }
        String token = TokenUtils.getToken(studentLoginDTO.getAccount(), id);
        Map<String, String> result = new HashMap<>(4);
        result.put("token", token);

        User user = userService.getStudentByAccount(studentLoginDTO.getAccount());
        redisService.set(String.valueOf(id), user, 60 * 60 * 24);
        System.out.println(user.getRoleId());
        response.addHeader("token",token);
        response.setHeader("Access-Control-Expose-Headers", "token");
        return new Result(result, true, "登陆成功");
    }

    @PostMapping("/register")
    public Result registerStudent(@RequestBody RegisterDTO registerDTO, HttpServletRequest request) {
        String code = (String) redisService.get(request.getHeader("uuid"));
        if (!registerDTO.getVerificationCode().toLowerCase().equals(code)) {
            return new Result(null, false, "错误的验证码");
        }
        Boolean aBoolean = userService.userRegister(registerDTO.getName(), registerDTO.getPassword(), registerDTO.getAccount(), registerDTO.getGender(), registerDTO.getClassId(), registerDTO.getPhoto(), registerDTO.getGradeId(), registerDTO.getMajorId(), registerDTO.getCollegeId(), registerDTO.getUniversityId());
        if (!aBoolean) {
            return new Result(null, false, "注册失败");
        }
        return new Result(null, "注册成功");
    }

    @GetMapping("/getCode")
    public String getCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Map<String, Object> map = ImageCode.getImageCode(100, 40);
        String simpleCaptcha = "simpleCaptcha";
        // System.out.println(map.get("strEnsure").toString().toLowerCase());

        String key = UUID.randomUUID().toString().replace("-", "");
        response.addHeader("uuid", key);
        response.setHeader("Access-Control-Expose-Headers", "uuid");
        redisService.set(key, map.get("strEnsure").toString().toLowerCase(), 60 * 5);
        // System.out.println("strEnsure==========" + map.get("strEnsure").toString().toLowerCase());
        ImageIO.write((BufferedImage) map.get("image"), "JPEG", os);
        byte[] bytes = os.toByteArray();
        Base64.Encoder encoder = Base64.getEncoder();
        return "data:image/png;base64," + encoder.encodeToString(bytes);
    }

    @GetMapping("/getUniversity")
    public Result<List<UniversityVO>> getUniversity() {
        return universityService.getUniversity();
    }

    @GetMapping("/getCollege")
    public Result<List<CollegeVO>> getCollege(@RequestParam Integer universityId) {
        return collegeService.getCollege(universityId);
    }

    @GetMapping("/getMajor")
    public Result<List<MajorVO>> getMajor(@RequestParam Integer collegeId) {
        return majorService.getMajor(collegeId);
    }

    @GetMapping("/getGrade")
    public Result<List<GradeVO>> getGrade(@RequestParam Integer majorId) {
        return gradeService.getGrade(majorId);
    }

    @GetMapping("/getClass")
    public Result<List<ClassVO>> getClass(@RequestParam Integer gradeId, @RequestParam Integer majorId) {
        return classService.getClass(gradeId, majorId);
    }

    @GetMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        String s = null;
        try {
            s = fdfsClientUtil.uploadFile(file);
            return new Result<>(s,true,"上传成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new AppException("上传失败");
    }
}
