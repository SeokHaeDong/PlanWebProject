package com.make.plan.controller;

import com.make.plan.dto.FriendDTO;
import com.make.plan.dto.UserDTO;
import com.make.plan.entity.User;
import com.make.plan.service.FriendService;
import com.make.plan.service.forCustomer.find.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("calendar/member")
@Log4j2
@RequiredArgsConstructor
public class FriendController {

    private final UserService userService;

    private final FriendService friendService;

    @PostMapping("/friendAdd")
    public List<UserDTO> getUserInfo(@RequestBody String data){
        log.info(data);

//        List<User> userInfo = friendService.userSearching(data);

//        log.info(userInfo);




        return null;
    }

}
