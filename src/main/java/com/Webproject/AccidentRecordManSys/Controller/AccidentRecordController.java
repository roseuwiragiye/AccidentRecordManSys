package com.Webproject.AccidentRecordManSys.Controller;


import com.Webproject.AccidentRecordManSys.Model.Accident;
import com.Webproject.AccidentRecordManSys.Repository.AccidentRepository;
import com.Webproject.AccidentRecordManSys.Service.AccidentPDFService;
import com.Webproject.AccidentRecordManSys.Service.AccidentRecService;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Controller

public class AccidentRecordController {
    @Autowired
    private AccidentRecService accidentRecService;

    @GetMapping("/accident")
    public String home(Model model) {
        return findPaginated(1, "name", "asc", model);
    }


    @GetMapping("/showAccident")
    public String showAccident(Model model) {
        Accident accident = new Accident();
        model.addAttribute("accident", accident);
        return "saveAccidentRec";
    }

    @PostMapping("/saveAccident")
    public String addAccident(@ModelAttribute("accident") Accident accident) {

        accidentRecService.saveAccident(accident);
        return "redirect:/accident";
    }

    @GetMapping("/showUpdateAccident/{id}")
    public String showUpdateAccident(@PathVariable(value = "id") long id,
                                   Model model) {
        Accident accident = accidentRecService.getAccidentById(id);
        model.addAttribute("accident", accident);
        return "updateAccidentRec";
    }

    @GetMapping("/deleteAccident/{id}")
    public String deleteAccident(@PathVariable(value = "id") long id) {
        this.accidentRecService.deleteAccidentById(id);
        return "redirect:/accident";
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir,
                                Model model) {
        int pageSize = 3;

        Page<Accident> page = accidentRecService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<Accident> listAccidents = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("listAccidents", listAccidents);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");


        return "index";
    }


    @Autowired
    AccidentRepository accidentRepository;

    @GetMapping(value = "/exportAccidentPdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> accidentReport() throws IOException {
        List<Accident> accidents = (List<Accident>) accidentRepository.findAll();

        ByteArrayInputStream bis = AccidentPDFService.accidentPDFReport(accidents);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=AccidentReport.pdf");

        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @GetMapping("/exportAccidentCsv")
    public void exportAccidentCSV(HttpServletResponse response)
            throws Exception {

        //set file name and content type
        String filename = "Accident-data.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");
        //create a csv writer
        StatefulBeanToCsv<Accident> writer = new StatefulBeanToCsvBuilder<Accident>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withSeparator(CSVWriter.DEFAULT_SEPARATOR).withOrderedResults(false)
                .build();
        //write all employees data to csv file
        writer.write(accidentRecService.getAllAccidents());

    }

    @GetMapping("/searchAccident")
    public String searchMethod(Model model) {
        model.addAttribute("search", new Accident());
        return "searchAccidentRec";
    }

    @PostMapping("/searchAccident")
    public String getEmployee(@ModelAttribute("search") Accident accident, Model model) {
        Accident accident1 = accidentRecService.getAccidentById(accident.getId());
        if (accident1 != null) {
            model.addAttribute("accident1", accident1);
            return "searchAccidentRec";
        } else {
            model.addAttribute("error", "The accident is not found");
            return "searchAccidentRec";
        }
    }

//    @Autowired
//    private EmailService emailService;
//    @Autowired
//    private VolunteerService volunteerService;
//
//    @GetMapping("/sendEmail")
//    public String sendEmail(Model model) {
//        model.addAttribute("volunteer", volunteerService.getAllVolunteers());
//
//        return "sendEmail";
//    }
//
//    @PostMapping("/postEmail")
//    public String postEmail(@ModelAttribute SendEmail sendEmail, HttpSession session) {
//        emailService.sendEmail(sendEmail);
//        session.setAttribute("msg", "Email Sent Successfully");
//        return "redirect:/sendEmail";
//
//    }
}
