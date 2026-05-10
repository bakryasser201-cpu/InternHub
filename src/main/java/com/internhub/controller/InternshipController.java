package com.internhub.controller;

import com.internhub.dto.InternshipDTO;
import com.internhub.model.Internship;
import com.internhub.model.InternshipStatus;
import com.internhub.service.InternshipService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/company/internships")
public class InternshipController {

    private final InternshipService internshipService;

    public InternshipController(InternshipService internshipService) {
        this.internshipService = internshipService;
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("internship", new InternshipDTO());
        return "company/create-internship";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("internship") InternshipDTO dto, BindingResult result, HttpSession session) {
        if (result.hasErrors()) {
            return "company/create-internship";
        }
        internshipService.create(requireCompany(session), dto);
        return "redirect:/company/dashboard";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, HttpSession session, Model model) {
        Internship internship = internshipService.findById(id);
        if (!internship.getCompany().getId().equals(requireCompany(session))) {
            throw new IllegalArgumentException("You can edit only your own internships");
        }
        model.addAttribute("internship", internshipService.toDto(internship));
        model.addAttribute("statuses", InternshipStatus.values());
        return "company/edit-internship";
    }

    @PostMapping("/{id}/edit")
    public String edit(@PathVariable Long id, @Valid @ModelAttribute("internship") InternshipDTO dto, BindingResult result, HttpSession session, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("statuses", InternshipStatus.values());
            return "company/edit-internship";
        }
        internshipService.update(id, requireCompany(session), dto);
        return "redirect:/company/dashboard";
    }

    @PostMapping("/{id}/close")
    public String close(@PathVariable Long id, HttpSession session) {
        internshipService.close(id, requireCompany(session));
        return "redirect:/company/dashboard";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, HttpSession session) {
        internshipService.delete(id, requireCompany(session));
        return "redirect:/company/dashboard";
    }

    private Long requireCompany(HttpSession session) {
        Long companyId = (Long) session.getAttribute("companyId");
        if (companyId == null) {
            throw new IllegalArgumentException("Please login as a company");
        }
        return companyId;
    }
}
