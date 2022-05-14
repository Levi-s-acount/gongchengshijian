package com.fifteen.webproject.service.user;

import com.fifteen.webproject.bean.vo.UniversityVO;
import com.fifteen.webproject.utils.result.Result;

import java.util.List;

/**
 * @Author Fifteen
 * @Date 2022/4/10
 **/
public interface UniversityService {
    Result<List<UniversityVO>> getUniversity();
}
