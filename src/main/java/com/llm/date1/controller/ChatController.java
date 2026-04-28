package com.llm.date1.controller;

import com.llm.date1.service.LlmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private LlmService llmService;

    @PostMapping
    public String chat(@RequestBody String question) {
        return llmService.ask(question);
    }

    @PostMapping("/pdf")
    public String chatPdf(@RequestBody String question) {
        return llmService.askWithPdf(question);
    }
}
