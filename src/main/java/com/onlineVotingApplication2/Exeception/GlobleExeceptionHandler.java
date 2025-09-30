package com.onlineVotingApplication2.Exeception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobleExeceptionHandler {


        @ExceptionHandler(DataIntegrityViolationException.class)
        public String handleDataIntegrityViolation(DataIntegrityViolationException e,
                                                   RedirectAttributes redirectAttributes) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Data integrity violation. Please check your input.");
            return "redirect:/admin/candidates";
        }
    }

