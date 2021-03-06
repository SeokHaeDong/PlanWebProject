package com.make.plan.controller;


import com.make.plan.service.forCustomer.edit.EditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpSession;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.util.Map;

@Controller
@RequestMapping("/edit")
public class EditController {
    @Autowired
    private EditService editService;

    @GetMapping("")
    public String editMain()
    {
        return "/edit/editmain";
    }

    @GetMapping("/edit_status")
    public void unsubscribe(){};

    @GetMapping("/editmain")
    public void editmain2(){}



    @GetMapping("/edit_info")
    public void editInfo(HttpSession session) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {

        String nick = (String) session.getAttribute("nick");
        Map<String, Object> result = editService.bringUserInfo(nick);
        String userId = (String) result.get("userId");
        String userEmail = (String)result.get("userEmail");
        String userGender = null;
        userGender = (String)result.get("userGender");
        Long userCode = (Long)result.get("userCode");
        String userAnswer = editService.bringAnswerInfo(userCode);
            System.out.println(userAnswer);
        String userContext = editService.bringQuestionInfo(userCode);

        if(userGender.equals("m")==true){
        userGender="Male";
    }else if(userGender.equals("f")==true){
        userGender="Female";
    }
    LocalDate userBirthday = (LocalDate) result.get("userBirthday");


        session.setAttribute("id",userId);
        session.setAttribute("email",userEmail);
        session.setAttribute("birthday",userBirthday);
        session.setAttribute("gender",userGender);
        session.setAttribute("answer",userAnswer);
        session.setAttribute("context",userContext);
}

    /**
     * ?????? ?????? ?????? page ?????? ???????????? method =============================================================================
     */
    @PostMapping("/editing")
    public String editingUserInfo(HttpSession session ,
                                  String nick,
                                  String pw,
                                  String answer,
                                  Model model,
                                  String pwCheck,
                                  String gender,
                                  String birthday,
                                  String context)
    {
        /**
         *  paramValid : key---> isValid
         *  true --> error ?????? x
         *  false --> error ?????????
         */
        Map<String, Object> paramValid = editService.parameterValidCheck
                (
                        session,
                        nick,
                        pw,
                        answer,
                        gender,
                        birthday,
                        context
                );

        // error ??? ???????????? ?????? : ?????? ????????? ????????? ??? -----------------------------------------------------------------
        if((boolean)paramValid.get("isValid"))
        {
            nick = (String)paramValid.get("nick");
            pw = (String)paramValid.get("pw");
            answer = (String)paramValid.get("answer");
            birthday = (String)paramValid.get("birthday");
            gender = (String)paramValid.get("gender");
            context = (String)paramValid.get("context");

            // session ?????? nick name ??? ????????? : ?????? nick ??? ???????????? ????????? ----------------------------------------------
            String currentNick = (String)session.getAttribute("nick");

            /**
             * @return --------------------------------------------------------------------
             * key
             * result ---> true : ?????? ??????????????? ??????
             * result ---> false : error ??????
             *
             * error
             * key
             * nickErrorMessage
             * pwErrorMessage
             */
            Map<String, Object> result = editService.changeUserInfo
                    (
                            session,
                            currentNick,
                            nick,
                            pw,
                            pwCheck,
                            gender,
                            birthday,
                            answer,
                            context
                    );
            // ??????????????? ??????????????? ?????? ??? -----------------------------------------------
            if ((boolean) result.get("result"))
            {
                session.setAttribute("nick", nick);
            }

            // error ??? ???????????? ??? ------------------------------------------------------
            // ????????? ??????, ???????????? ???????????? ?????? ?????????
            else
            {

                // ???????????? ?????? ????????? ?????? ----------------------------
                if(result.containsKey("nickErrorMessage"))
                {
                    model.addAttribute("nickErrorMessage",(String)result.get("nickErrorMessage"));
                }

                // ??????????????? ???????????? ?????? ?????? ???????????? ?????? ?????? -----------------------------
                if(result.containsKey("pwErrorMessage"))
                {
                    model.addAttribute("pwErrorMessage",(String)result.get("pwErrorMessage"));
                }

                return "edit/edit_info";
            }


        }
        // parameter ?????? ????????? ?????? ?????? ???  ------------------------------------------------------
        else if(!(boolean)paramValid.get("isValid"))
        {
            // ????????? ???????????? ????????? ------------------
            if(paramValid.containsKey("nickValidError"))
            {
                model.addAttribute("nickValidError",(String)paramValid.get("nickValidError"));
            }


            // ???????????? ???????????? ?????????----------
            if(paramValid.containsKey("pwValidError"))
            {
                model.addAttribute("pwValidError",(String)paramValid.get("pwValidError"));
            }


            // ????????? ?????? ??? ????????? ----------
            if(paramValid.containsKey("answerValidError"))
            {
                model.addAttribute("answerValidError",(String)paramValid.get("answerValidError"));
            }
            // ????????? ?????? ??? ????????? ----------


            // ????????? ?????????????????? ?????? ???????????? ????????? ??? -----------
            if(paramValid.containsKey("errorMessage"))
            {
                model.addAttribute("errorMessage", paramValid.get("errorMessage"));
            }

            return "edit/edit_info";
        }

        return "edit/editmain";
    }
}
