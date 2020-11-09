package org.launchcode.javawebdevtechjobspersistent.controllers;

import org.launchcode.javawebdevtechjobspersistent.models.Employer;
import org.launchcode.javawebdevtechjobspersistent.models.Job;
import org.launchcode.javawebdevtechjobspersistent.models.Skill;
import org.launchcode.javawebdevtechjobspersistent.models.data.EmployerRepository;
import org.launchcode.javawebdevtechjobspersistent.models.data.JobRepository;
import org.launchcode.javawebdevtechjobspersistent.models.data.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Created by LaunchCode
 */
@Controller
public class HomeController {

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private JobRepository jobRepository;


    @RequestMapping("")
    public String index(Model model) {

        model.addAttribute("title", "My Jobs");

        return "index";
    }

    @GetMapping("add")
    public String displayAddJobForm(Model model) {
        model.addAttribute("title", "Add Job");
        model.addAttribute(new Job());
        model.addAttribute("employers", employerRepository.findAll());
        model.addAttribute("skills", skillRepository.findAll());
        return "add";
    }

    @PostMapping("add")
    public String processAddJobForm(@ModelAttribute @Valid Job newJob,
                                       Errors errors, Model model, @RequestParam int employerId, @RequestParam String name, @RequestParam List<Integer> skills)  {
// this was causing an error - , @RequestParam List<Integer> skills)
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Job");
            return "add";
        }
        //select job by the employer ID
        //create a new job object based on this id selected in the drop down and the name provided by user in input box in the form.
        Optional<Employer> selectedEmployer = employerRepository.findById(employerId);
        if (selectedEmployer.isPresent()) {

            Employer employer = selectedEmployer.get();

            Job newJobObject = new Job();
            newJobObject.setEmployer(employer);
            newJobObject.setName(name);

            List<Skill> skillObjs = (List<Skill>) skillRepository.findAllById(skills);
            newJobObject.setSkills(skillObjs);

            jobRepository.save(newJobObject);

            model.addAttribute("job", newJobObject);
            return "view";
        } else {
            return "add";
        }
        //skillRepository.findAllById(skills);

    }

    @GetMapping("view/{jobId}")
    public String displayViewJob(Model model, @PathVariable int jobId) {

        return "view";
    }


}
